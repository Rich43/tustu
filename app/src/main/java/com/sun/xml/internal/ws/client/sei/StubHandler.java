package com.sun.xml.internal.ws.client.sei;

import com.oracle.webservices.internal.api.databinding.JavaCallInfo;
import com.sun.xml.internal.ws.api.SOAPVersion;
import com.sun.xml.internal.ws.api.databinding.ClientCallBridge;
import com.sun.xml.internal.ws.api.message.Message;
import com.sun.xml.internal.ws.api.message.MessageContextFactory;
import com.sun.xml.internal.ws.api.message.Packet;
import com.sun.xml.internal.ws.api.model.JavaMethod;
import com.sun.xml.internal.ws.client.sei.BodyBuilder;
import com.sun.xml.internal.ws.client.sei.MessageFiller;
import com.sun.xml.internal.ws.client.sei.ResponseBuilder;
import com.sun.xml.internal.ws.fault.SOAPFaultBuilder;
import com.sun.xml.internal.ws.model.CheckedExceptionImpl;
import com.sun.xml.internal.ws.model.JavaMethodImpl;
import com.sun.xml.internal.ws.model.ParameterImpl;
import com.sun.xml.internal.ws.model.WrapperParameter;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.namespace.QName;

/* loaded from: rt.jar:com/sun/xml/internal/ws/client/sei/StubHandler.class */
public class StubHandler implements ClientCallBridge {
    private final BodyBuilder bodyBuilder;
    private final MessageFiller[] inFillers;
    protected final String soapAction;
    protected final boolean isOneWay;
    protected final JavaMethodImpl javaMethod;
    protected final Map<QName, CheckedExceptionImpl> checkedExceptions = new HashMap();
    protected SOAPVersion soapVersion;
    protected ResponseBuilder responseBuilder;
    protected MessageContextFactory packetFactory;

    public StubHandler(JavaMethodImpl method, MessageContextFactory mcf) {
        this.soapVersion = SOAPVersion.SOAP_11;
        for (CheckedExceptionImpl ce : method.getCheckedExceptions()) {
            this.checkedExceptions.put(ce.getBond().getTypeInfo().tagName, ce);
        }
        String soapActionFromBinding = method.getBinding().getSOAPAction();
        if (method.getInputAction() != null && soapActionFromBinding != null && !soapActionFromBinding.equals("")) {
            this.soapAction = method.getInputAction();
        } else {
            this.soapAction = soapActionFromBinding;
        }
        this.javaMethod = method;
        this.packetFactory = mcf;
        this.soapVersion = this.javaMethod.getBinding().getSOAPVersion();
        List<ParameterImpl> rp = method.getRequestParameters();
        BodyBuilder bodyBuilder = null;
        List<MessageFiller> fillers = new ArrayList<>();
        for (ParameterImpl param : rp) {
            ValueGetter getter = getValueGetterFactory().get(param);
            switch (param.getInBinding().kind) {
                case BODY:
                    if (param.isWrapperStyle()) {
                        if (param.getParent().getBinding().isRpcLit()) {
                            bodyBuilder = new BodyBuilder.RpcLit((WrapperParameter) param, this.soapVersion, getValueGetterFactory());
                            break;
                        } else {
                            bodyBuilder = new BodyBuilder.DocLit((WrapperParameter) param, this.soapVersion, getValueGetterFactory());
                            break;
                        }
                    } else {
                        bodyBuilder = new BodyBuilder.Bare(param, this.soapVersion, getter);
                        break;
                    }
                case HEADER:
                    fillers.add(new MessageFiller.Header(param.getIndex(), param.getXMLBridge(), getter));
                    break;
                case ATTACHMENT:
                    fillers.add(MessageFiller.AttachmentFiller.createAttachmentFiller(param, getter));
                    break;
                case UNBOUND:
                    break;
                default:
                    throw new AssertionError();
            }
        }
        if (bodyBuilder == null) {
            switch (this.soapVersion) {
                case SOAP_11:
                    bodyBuilder = BodyBuilder.EMPTY_SOAP11;
                    break;
                case SOAP_12:
                    bodyBuilder = BodyBuilder.EMPTY_SOAP12;
                    break;
                default:
                    throw new AssertionError();
            }
        }
        this.bodyBuilder = bodyBuilder;
        this.inFillers = (MessageFiller[]) fillers.toArray(new MessageFiller[fillers.size()]);
        this.isOneWay = method.getMEP().isOneWay();
        this.responseBuilder = buildResponseBuilder(method, ValueSetterFactory.SYNC);
    }

    ResponseBuilder buildResponseBuilder(JavaMethodImpl method, ValueSetterFactory setterFactory) {
        ResponseBuilder rb;
        List<ParameterImpl> rp = method.getResponseParameters();
        List<ResponseBuilder> builders = new ArrayList<>();
        for (ParameterImpl param : rp) {
            switch (param.getOutBinding().kind) {
                case BODY:
                    if (param.isWrapperStyle()) {
                        if (param.getParent().getBinding().isRpcLit()) {
                            builders.add(new ResponseBuilder.RpcLit((WrapperParameter) param, setterFactory));
                            break;
                        } else {
                            builders.add(new ResponseBuilder.DocLit((WrapperParameter) param, setterFactory));
                            break;
                        }
                    } else {
                        ValueSetter setter = setterFactory.get(param);
                        builders.add(new ResponseBuilder.Body(param.getXMLBridge(), setter));
                        break;
                    }
                case HEADER:
                    ValueSetter setter2 = setterFactory.get(param);
                    builders.add(new ResponseBuilder.Header(this.soapVersion, param, setter2));
                    break;
                case ATTACHMENT:
                    ValueSetter setter3 = setterFactory.get(param);
                    builders.add(ResponseBuilder.AttachmentBuilder.createAttachmentBuilder(param, setter3));
                    break;
                case UNBOUND:
                    ValueSetter setter4 = setterFactory.get(param);
                    builders.add(new ResponseBuilder.NullSetter(setter4, ResponseBuilder.getVMUninitializedValue(param.getTypeInfo().type)));
                    break;
                default:
                    throw new AssertionError();
            }
        }
        switch (builders.size()) {
            case 0:
                rb = ResponseBuilder.NONE;
                break;
            case 1:
                rb = builders.get(0);
                break;
            default:
                rb = new ResponseBuilder.Composite(builders);
                break;
        }
        return rb;
    }

    @Override // com.sun.xml.internal.ws.api.databinding.ClientCallBridge
    public Packet createRequestPacket(JavaCallInfo args) {
        Message msg = this.bodyBuilder.createMessage(args.getParameters());
        for (MessageFiller filler : this.inFillers) {
            filler.fillIn(args.getParameters(), msg);
        }
        Packet req = (Packet) this.packetFactory.createContext(msg);
        req.setState(Packet.State.ClientRequest);
        req.soapAction = this.soapAction;
        req.expectReply = Boolean.valueOf(!this.isOneWay);
        req.getMessage().assertOneWay(this.isOneWay);
        req.setWSDLOperation(getOperationName());
        return req;
    }

    ValueGetterFactory getValueGetterFactory() {
        return ValueGetterFactory.SYNC;
    }

    @Override // com.sun.xml.internal.ws.api.databinding.ClientCallBridge
    public JavaCallInfo readResponse(Packet p2, JavaCallInfo call) throws Exception {
        Message msg = p2.getMessage();
        if (msg.isFault()) {
            SOAPFaultBuilder faultBuilder = SOAPFaultBuilder.create(msg);
            Throwable t2 = faultBuilder.createException(this.checkedExceptions);
            call.setException(t2);
            throw t2;
        }
        initArgs(call.getParameters());
        Object ret = this.responseBuilder.readResponse(msg, call.getParameters());
        call.setReturnValue(ret);
        return call;
    }

    public QName getOperationName() {
        return this.javaMethod.getOperationQName();
    }

    public String getSoapAction() {
        return this.soapAction;
    }

    public boolean isOneWay() {
        return this.isOneWay;
    }

    protected void initArgs(Object[] args) throws Exception {
    }

    @Override // com.sun.xml.internal.ws.api.databinding.ClientCallBridge
    public Method getMethod() {
        return this.javaMethod.getMethod();
    }

    @Override // com.sun.xml.internal.ws.api.databinding.ClientCallBridge
    public JavaMethod getOperationModel() {
        return this.javaMethod;
    }
}
