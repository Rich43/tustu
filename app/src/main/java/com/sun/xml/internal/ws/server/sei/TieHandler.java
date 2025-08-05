package com.sun.xml.internal.ws.server.sei;

import com.oracle.webservices.internal.api.databinding.JavaCallInfo;
import com.sun.xml.internal.ws.api.SOAPVersion;
import com.sun.xml.internal.ws.api.WSBinding;
import com.sun.xml.internal.ws.api.databinding.EndpointCallBridge;
import com.sun.xml.internal.ws.api.message.Message;
import com.sun.xml.internal.ws.api.message.MessageContextFactory;
import com.sun.xml.internal.ws.api.message.Packet;
import com.sun.xml.internal.ws.api.model.JavaMethod;
import com.sun.xml.internal.ws.fault.SOAPFaultBuilder;
import com.sun.xml.internal.ws.model.CheckedExceptionImpl;
import com.sun.xml.internal.ws.model.JavaMethodImpl;
import com.sun.xml.internal.ws.model.ParameterImpl;
import com.sun.xml.internal.ws.model.WrapperParameter;
import com.sun.xml.internal.ws.server.sei.EndpointArgumentsBuilder;
import com.sun.xml.internal.ws.server.sei.EndpointResponseMessageBuilder;
import com.sun.xml.internal.ws.server.sei.MessageFiller;
import com.sun.xml.internal.ws.wsdl.DispatchException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jws.WebParam;
import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLStreamException;
import javax.xml.ws.ProtocolException;
import javax.xml.ws.WebServiceException;

/* loaded from: rt.jar:com/sun/xml/internal/ws/server/sei/TieHandler.class */
public final class TieHandler implements EndpointCallBridge {
    private final SOAPVersion soapVersion;
    private final Method method;
    private final int noOfArgs;
    private final JavaMethodImpl javaMethodModel;
    private final Boolean isOneWay;
    private final EndpointArgumentsBuilder argumentsBuilder = createArgumentsBuilder();
    private final EndpointResponseMessageBuilder bodyBuilder;
    private final MessageFiller[] outFillers;
    protected MessageContextFactory packetFactory;
    private static final Logger LOGGER = Logger.getLogger(TieHandler.class.getName());

    public TieHandler(JavaMethodImpl method, WSBinding binding, MessageContextFactory mcf) {
        this.soapVersion = binding.getSOAPVersion();
        this.method = method.getMethod();
        this.javaMethodModel = method;
        List<MessageFiller> fillers = new ArrayList<>();
        this.bodyBuilder = createResponseMessageBuilder(fillers);
        this.outFillers = (MessageFiller[]) fillers.toArray(new MessageFiller[fillers.size()]);
        this.isOneWay = Boolean.valueOf(method.getMEP().isOneWay());
        this.noOfArgs = this.method.getParameterTypes().length;
        this.packetFactory = mcf;
    }

    private EndpointArgumentsBuilder createArgumentsBuilder() {
        EndpointArgumentsBuilder argsBuilder;
        List<ParameterImpl> rp = this.javaMethodModel.getRequestParameters();
        List<EndpointArgumentsBuilder> builders = new ArrayList<>();
        for (ParameterImpl param : rp) {
            EndpointValueSetter setter = EndpointValueSetter.get(param);
            switch (param.getInBinding().kind) {
                case BODY:
                    if (param.isWrapperStyle()) {
                        if (param.getParent().getBinding().isRpcLit()) {
                            builders.add(new EndpointArgumentsBuilder.RpcLit((WrapperParameter) param));
                            break;
                        } else {
                            builders.add(new EndpointArgumentsBuilder.DocLit((WrapperParameter) param, WebParam.Mode.OUT));
                            break;
                        }
                    } else {
                        builders.add(new EndpointArgumentsBuilder.Body(param.getXMLBridge(), setter));
                        break;
                    }
                case HEADER:
                    builders.add(new EndpointArgumentsBuilder.Header(this.soapVersion, param, setter));
                    break;
                case ATTACHMENT:
                    builders.add(EndpointArgumentsBuilder.AttachmentBuilder.createAttachmentBuilder(param, setter));
                    break;
                case UNBOUND:
                    builders.add(new EndpointArgumentsBuilder.NullSetter(setter, EndpointArgumentsBuilder.getVMUninitializedValue(param.getTypeInfo().type)));
                    break;
                default:
                    throw new AssertionError();
            }
        }
        List<ParameterImpl> resp = this.javaMethodModel.getResponseParameters();
        for (ParameterImpl param2 : resp) {
            if (param2.isWrapperStyle()) {
                WrapperParameter wp = (WrapperParameter) param2;
                List<ParameterImpl> children = wp.getWrapperChildren();
                for (ParameterImpl p2 : children) {
                    if (p2.isOUT() && p2.getIndex() != -1) {
                        EndpointValueSetter setter2 = EndpointValueSetter.get(p2);
                        builders.add(new EndpointArgumentsBuilder.NullSetter(setter2, null));
                    }
                }
            } else if (param2.isOUT() && param2.getIndex() != -1) {
                EndpointValueSetter setter3 = EndpointValueSetter.get(param2);
                builders.add(new EndpointArgumentsBuilder.NullSetter(setter3, null));
            }
        }
        switch (builders.size()) {
            case 0:
                argsBuilder = EndpointArgumentsBuilder.NONE;
                break;
            case 1:
                argsBuilder = builders.get(0);
                break;
            default:
                argsBuilder = new EndpointArgumentsBuilder.Composite(builders);
                break;
        }
        return argsBuilder;
    }

    private EndpointResponseMessageBuilder createResponseMessageBuilder(List<MessageFiller> fillers) {
        EndpointResponseMessageBuilder tmpBodyBuilder = null;
        List<ParameterImpl> rp = this.javaMethodModel.getResponseParameters();
        for (ParameterImpl param : rp) {
            ValueGetter getter = ValueGetter.get(param);
            switch (param.getOutBinding().kind) {
                case BODY:
                    if (param.isWrapperStyle()) {
                        if (param.getParent().getBinding().isRpcLit()) {
                            tmpBodyBuilder = new EndpointResponseMessageBuilder.RpcLit((WrapperParameter) param, this.soapVersion);
                            break;
                        } else {
                            tmpBodyBuilder = new EndpointResponseMessageBuilder.DocLit((WrapperParameter) param, this.soapVersion);
                            break;
                        }
                    } else {
                        tmpBodyBuilder = new EndpointResponseMessageBuilder.Bare(param, this.soapVersion);
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
            switch (this.soapVersion) {
                case SOAP_11:
                    tmpBodyBuilder = EndpointResponseMessageBuilder.EMPTY_SOAP11;
                    break;
                case SOAP_12:
                    tmpBodyBuilder = EndpointResponseMessageBuilder.EMPTY_SOAP12;
                    break;
                default:
                    throw new AssertionError();
            }
        }
        return tmpBodyBuilder;
    }

    public Object[] readRequest(Message reqMsg) {
        Object[] args = new Object[this.noOfArgs];
        try {
            this.argumentsBuilder.readRequest(reqMsg, args);
            return args;
        } catch (JAXBException e2) {
            throw new WebServiceException(e2);
        } catch (XMLStreamException e3) {
            throw new WebServiceException(e3);
        }
    }

    public Message createResponse(JavaCallInfo call) {
        Message responseMessage;
        if (call.getException() == null) {
            responseMessage = this.isOneWay.booleanValue() ? null : createResponseMessage(call.getParameters(), call.getReturnValue());
        } else {
            Throwable e2 = call.getException();
            Throwable serviceException = getServiceException(e2);
            if ((e2 instanceof InvocationTargetException) || serviceException != null) {
                if (serviceException != null) {
                    LOGGER.log(Level.FINE, serviceException.getMessage(), serviceException);
                    responseMessage = SOAPFaultBuilder.createSOAPFaultMessage(this.soapVersion, this.javaMethodModel.getCheckedException(serviceException.getClass()), serviceException);
                } else {
                    Throwable cause = e2.getCause();
                    if (cause instanceof ProtocolException) {
                        LOGGER.log(Level.FINE, cause.getMessage(), cause);
                    } else {
                        LOGGER.log(Level.SEVERE, cause.getMessage(), cause);
                    }
                    responseMessage = SOAPFaultBuilder.createSOAPFaultMessage(this.soapVersion, (CheckedExceptionImpl) null, cause);
                }
            } else if (e2 instanceof DispatchException) {
                responseMessage = ((DispatchException) e2).fault;
            } else {
                LOGGER.log(Level.SEVERE, e2.getMessage(), e2);
                responseMessage = SOAPFaultBuilder.createSOAPFaultMessage(this.soapVersion, (CheckedExceptionImpl) null, e2);
            }
        }
        return responseMessage;
    }

    Throwable getServiceException(Throwable throwable) {
        if (this.javaMethodModel.getCheckedException(throwable.getClass()) != null) {
            return throwable;
        }
        if (throwable.getCause() != null) {
            Throwable cause = throwable.getCause();
            if (this.javaMethodModel.getCheckedException(cause.getClass()) != null) {
                return cause;
            }
            return null;
        }
        return null;
    }

    private Message createResponseMessage(Object[] args, Object returnValue) {
        Message msg = this.bodyBuilder.createMessage(args, returnValue);
        for (MessageFiller filler : this.outFillers) {
            filler.fillIn(args, returnValue, msg);
        }
        return msg;
    }

    public Method getMethod() {
        return this.method;
    }

    @Override // com.sun.xml.internal.ws.api.databinding.EndpointCallBridge
    public JavaCallInfo deserializeRequest(Packet req) {
        com.sun.xml.internal.ws.api.databinding.JavaCallInfo call = new com.sun.xml.internal.ws.api.databinding.JavaCallInfo();
        call.setMethod(getMethod());
        Object[] args = readRequest(req.getMessage());
        call.setParameters(args);
        return call;
    }

    @Override // com.sun.xml.internal.ws.api.databinding.EndpointCallBridge
    public Packet serializeResponse(JavaCallInfo call) {
        Message msg = createResponse(call);
        Packet p2 = msg == null ? (Packet) this.packetFactory.createContext() : (Packet) this.packetFactory.createContext(msg);
        p2.setState(Packet.State.ServerResponse);
        return p2;
    }

    @Override // com.sun.xml.internal.ws.api.databinding.EndpointCallBridge
    public JavaMethod getOperationModel() {
        return this.javaMethodModel;
    }
}
