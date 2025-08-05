package com.sun.xml.internal.ws.client.sei;

import com.sun.xml.internal.ws.api.message.Message;
import com.sun.xml.internal.ws.client.sei.BodyBuilder;
import com.sun.xml.internal.ws.client.sei.MessageFiller;
import com.sun.xml.internal.ws.client.sei.ResponseBuilder;
import com.sun.xml.internal.ws.model.CheckedExceptionImpl;
import com.sun.xml.internal.ws.model.JavaMethodImpl;
import com.sun.xml.internal.ws.model.ParameterImpl;
import com.sun.xml.internal.ws.model.WrapperParameter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.namespace.QName;

/* loaded from: rt.jar:com/sun/xml/internal/ws/client/sei/SEIMethodHandler.class */
abstract class SEIMethodHandler extends MethodHandler {
    private BodyBuilder bodyBuilder;
    private MessageFiller[] inFillers;
    protected String soapAction;
    protected boolean isOneWay;
    protected JavaMethodImpl javaMethod;
    protected Map<QName, CheckedExceptionImpl> checkedExceptions;

    abstract ValueGetterFactory getValueGetterFactory();

    SEIMethodHandler(SEIStub owner) {
        super(owner, null);
    }

    SEIMethodHandler(SEIStub owner, JavaMethodImpl method) {
        super(owner, null);
        this.checkedExceptions = new HashMap();
        for (CheckedExceptionImpl ce : method.getCheckedExceptions()) {
            this.checkedExceptions.put(ce.getBond().getTypeInfo().tagName, ce);
        }
        if (method.getInputAction() != null && !method.getBinding().getSOAPAction().equals("")) {
            this.soapAction = method.getInputAction();
        } else {
            this.soapAction = method.getBinding().getSOAPAction();
        }
        this.javaMethod = method;
        List<ParameterImpl> rp = method.getRequestParameters();
        BodyBuilder tmpBodyBuilder = null;
        List<MessageFiller> fillers = new ArrayList<>();
        for (ParameterImpl param : rp) {
            ValueGetter getter = getValueGetterFactory().get(param);
            switch (param.getInBinding().kind) {
                case BODY:
                    if (param.isWrapperStyle()) {
                        if (param.getParent().getBinding().isRpcLit()) {
                            tmpBodyBuilder = new BodyBuilder.RpcLit((WrapperParameter) param, owner.soapVersion, getValueGetterFactory());
                            break;
                        } else {
                            tmpBodyBuilder = new BodyBuilder.DocLit((WrapperParameter) param, owner.soapVersion, getValueGetterFactory());
                            break;
                        }
                    } else {
                        tmpBodyBuilder = new BodyBuilder.Bare(param, owner.soapVersion, getter);
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
        if (tmpBodyBuilder == null) {
            switch (owner.soapVersion) {
                case SOAP_11:
                    tmpBodyBuilder = BodyBuilder.EMPTY_SOAP11;
                    break;
                case SOAP_12:
                    tmpBodyBuilder = BodyBuilder.EMPTY_SOAP12;
                    break;
                default:
                    throw new AssertionError();
            }
        }
        this.bodyBuilder = tmpBodyBuilder;
        this.inFillers = (MessageFiller[]) fillers.toArray(new MessageFiller[fillers.size()]);
        this.isOneWay = method.getMEP().isOneWay();
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
                    builders.add(new ResponseBuilder.Header(this.owner.soapVersion, param, setter2));
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

    Message createRequestMessage(Object[] args) {
        Message msg = this.bodyBuilder.createMessage(args);
        for (MessageFiller filler : this.inFillers) {
            filler.fillIn(args, msg);
        }
        return msg;
    }
}
