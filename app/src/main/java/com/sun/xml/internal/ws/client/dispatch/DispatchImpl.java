package com.sun.xml.internal.ws.client.dispatch;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import com.sun.xml.internal.ws.api.BindingID;
import com.sun.xml.internal.ws.api.SOAPVersion;
import com.sun.xml.internal.ws.api.WSBinding;
import com.sun.xml.internal.ws.api.addressing.AddressingVersion;
import com.sun.xml.internal.ws.api.addressing.WSEndpointReference;
import com.sun.xml.internal.ws.api.client.WSPortInfo;
import com.sun.xml.internal.ws.api.message.AddressingUtils;
import com.sun.xml.internal.ws.api.message.Attachment;
import com.sun.xml.internal.ws.api.message.AttachmentSet;
import com.sun.xml.internal.ws.api.message.Message;
import com.sun.xml.internal.ws.api.message.Packet;
import com.sun.xml.internal.ws.api.pipe.Fiber;
import com.sun.xml.internal.ws.api.pipe.Tube;
import com.sun.xml.internal.ws.api.server.Container;
import com.sun.xml.internal.ws.api.server.ContainerResolver;
import com.sun.xml.internal.ws.binding.BindingImpl;
import com.sun.xml.internal.ws.client.AsyncInvoker;
import com.sun.xml.internal.ws.client.AsyncResponseImpl;
import com.sun.xml.internal.ws.client.RequestContext;
import com.sun.xml.internal.ws.client.ResponseContext;
import com.sun.xml.internal.ws.client.ResponseContextReceiver;
import com.sun.xml.internal.ws.client.Stub;
import com.sun.xml.internal.ws.client.WSServiceDelegate;
import com.sun.xml.internal.ws.encoding.soap.DeserializationException;
import com.sun.xml.internal.ws.fault.SOAPFaultBuilder;
import com.sun.xml.internal.ws.message.AttachmentSetImpl;
import com.sun.xml.internal.ws.message.DataHandlerAttachment;
import com.sun.xml.internal.ws.resources.DispatchMessages;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXMLLoader;
import javax.activation.DataHandler;
import javax.xml.bind.JAXBException;
import javax.xml.namespace.QName;
import javax.xml.transform.Source;
import javax.xml.ws.AsyncHandler;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Dispatch;
import javax.xml.ws.Response;
import javax.xml.ws.Service;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.http.HTTPBinding;
import javax.xml.ws.soap.SOAPFaultException;

/* loaded from: rt.jar:com/sun/xml/internal/ws/client/dispatch/DispatchImpl.class */
public abstract class DispatchImpl<T> extends Stub implements Dispatch<T> {
    private static final Logger LOGGER = Logger.getLogger(DispatchImpl.class.getName());
    final Service.Mode mode;
    final SOAPVersion soapVersion;
    final boolean allowFaultResponseMsg;
    static final long AWAIT_TERMINATION_TIME = 800;
    static final String HTTP_REQUEST_METHOD_GET = "GET";
    static final String HTTP_REQUEST_METHOD_POST = "POST";
    static final String HTTP_REQUEST_METHOD_PUT = "PUT";

    abstract Packet createPacket(T t2);

    abstract T toReturnValue(Packet packet);

    @Deprecated
    protected DispatchImpl(QName port, Service.Mode mode, WSServiceDelegate owner, Tube pipe, BindingImpl binding, @Nullable WSEndpointReference epr) {
        super(port, owner, pipe, binding, owner.getWsdlService() != null ? owner.getWsdlService().get(port) : null, owner.getEndpointAddress(port), epr);
        this.mode = mode;
        this.soapVersion = binding.getSOAPVersion();
        this.allowFaultResponseMsg = false;
    }

    protected DispatchImpl(WSPortInfo portInfo, Service.Mode mode, BindingImpl binding, @Nullable WSEndpointReference epr) {
        this(portInfo, mode, binding, epr, false);
    }

    protected DispatchImpl(WSPortInfo portInfo, Service.Mode mode, BindingImpl binding, @Nullable WSEndpointReference epr, boolean allowFaultResponseMsg) {
        this(portInfo, mode, binding, (Tube) null, epr, allowFaultResponseMsg);
    }

    protected DispatchImpl(WSPortInfo portInfo, Service.Mode mode, BindingImpl binding, Tube pipe, @Nullable WSEndpointReference epr, boolean allowFaultResponseMsg) {
        super(portInfo, binding, pipe, portInfo.getEndpointAddress(), epr);
        this.mode = mode;
        this.soapVersion = binding.getSOAPVersion();
        this.allowFaultResponseMsg = allowFaultResponseMsg;
    }

    protected DispatchImpl(WSPortInfo portInfo, Service.Mode mode, Tube pipe, BindingImpl binding, @Nullable WSEndpointReference epr, boolean allowFaultResponseMsg) {
        super(portInfo, binding, pipe, portInfo.getEndpointAddress(), epr);
        this.mode = mode;
        this.soapVersion = binding.getSOAPVersion();
        this.allowFaultResponseMsg = allowFaultResponseMsg;
    }

    @Override // javax.xml.ws.Dispatch
    public final Response<T> invokeAsync(T param) {
        Container old = ContainerResolver.getDefault().enterContainer(this.owner.getContainer());
        try {
            if (LOGGER.isLoggable(Level.FINE)) {
                dumpParam(param, "invokeAsync(T)");
            }
            AsyncInvoker invoker = new DispatchAsyncInvoker(param);
            AsyncResponseImpl<T> ft = new AsyncResponseImpl<>(invoker, null);
            invoker.setReceiver(ft);
            ft.run();
            ContainerResolver.getDefault().exitContainer(old);
            return ft;
        } catch (Throwable th) {
            ContainerResolver.getDefault().exitContainer(old);
            throw th;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Multi-variable type inference failed */
    public void dumpParam(T t2, String method) {
        if (t2 instanceof Packet) {
            Packet message = (Packet) t2;
            if (LOGGER.isLoggable(Level.FINE)) {
                AddressingVersion av2 = getBinding().getAddressingVersion();
                SOAPVersion sv = getBinding().getSOAPVersion();
                String action = (av2 == null || message.getMessage() == null) ? null : AddressingUtils.getAction(message.getMessage().getHeaders(), av2, sv);
                String msgId = (av2 == null || message.getMessage() == null) ? null : AddressingUtils.getMessageID(message.getMessage().getHeaders(), av2, sv);
                LOGGER.fine("In DispatchImpl." + method + " for message with action: " + action + " and msg ID: " + msgId + " msg: " + ((Object) message.getMessage()));
                if (message.getMessage() == null) {
                    LOGGER.fine("Dispatching null message for action: " + action + " and msg ID: " + msgId);
                }
            }
        }
    }

    @Override // javax.xml.ws.Dispatch
    public final Future<?> invokeAsync(T param, AsyncHandler<T> asyncHandler) {
        Container old = ContainerResolver.getDefault().enterContainer(this.owner.getContainer());
        try {
            if (LOGGER.isLoggable(Level.FINE)) {
                dumpParam(param, "invokeAsync(T, AsyncHandler<T>)");
            }
            AsyncInvoker invoker = new DispatchAsyncInvoker(param);
            AsyncResponseImpl<T> ft = new AsyncResponseImpl<>(invoker, asyncHandler);
            invoker.setReceiver(ft);
            invoker.setNonNullAsyncHandlerGiven(asyncHandler != null);
            ft.run();
            ContainerResolver.getDefault().exitContainer(old);
            return ft;
        } catch (Throwable th) {
            ContainerResolver.getDefault().exitContainer(old);
            throw th;
        }
    }

    public final T doInvoke(T in, RequestContext rc, ResponseContextReceiver receiver) {
        Packet response = null;
        try {
            try {
                checkNullAllowed(in, rc, this.binding, this.mode);
                Packet message = createPacket(in);
                message.setState(Packet.State.ClientRequest);
                resolveEndpointAddress(message, rc);
                setProperties(message, true);
                response = process(message, rc, receiver);
                Message msg = response.getMessage();
                if (msg != null && msg.isFault() && !this.allowFaultResponseMsg) {
                    SOAPFaultBuilder faultBuilder = SOAPFaultBuilder.create(msg);
                    throw ((SOAPFaultException) faultBuilder.createException(null));
                }
                T returnValue = toReturnValue(response);
                if (response != null && response.transportBackChannel != null) {
                    response.transportBackChannel.close();
                }
                return returnValue;
            } catch (JAXBException e2) {
                throw new DeserializationException(DispatchMessages.INVALID_RESPONSE_DESERIALIZATION(), e2);
            } catch (WebServiceException e3) {
                throw e3;
            } catch (Throwable e4) {
                throw new WebServiceException(e4);
            }
        } catch (Throwable th) {
            if (response != null && response.transportBackChannel != null) {
                response.transportBackChannel.close();
            }
            throw th;
        }
    }

    @Override // javax.xml.ws.Dispatch
    public final T invoke(T in) {
        Container old = ContainerResolver.getDefault().enterContainer(this.owner.getContainer());
        try {
            if (LOGGER.isLoggable(Level.FINE)) {
                dumpParam(in, "invoke(T)");
            }
            T tDoInvoke = doInvoke(in, this.requestContext, this);
            ContainerResolver.getDefault().exitContainer(old);
            return tDoInvoke;
        } catch (Throwable th) {
            ContainerResolver.getDefault().exitContainer(old);
            throw th;
        }
    }

    @Override // javax.xml.ws.Dispatch
    public final void invokeOneWay(T in) {
        Container old = ContainerResolver.getDefault().enterContainer(this.owner.getContainer());
        try {
            if (LOGGER.isLoggable(Level.FINE)) {
                dumpParam(in, "invokeOneWay(T)");
            }
            try {
                checkNullAllowed(in, this.requestContext, this.binding, this.mode);
                Packet request = createPacket(in);
                request.setState(Packet.State.ClientRequest);
                setProperties(request, false);
                process(request, this.requestContext, this);
                ContainerResolver.getDefault().exitContainer(old);
            } catch (WebServiceException e2) {
                throw e2;
            } catch (Throwable e3) {
                throw new WebServiceException(e3);
            }
        } catch (Throwable th) {
            ContainerResolver.getDefault().exitContainer(old);
            throw th;
        }
    }

    void setProperties(Packet packet, boolean expectReply) {
        packet.expectReply = Boolean.valueOf(expectReply);
    }

    static boolean isXMLHttp(@NotNull WSBinding binding) {
        return binding.getBindingId().equals(BindingID.XML_HTTP);
    }

    static boolean isPAYLOADMode(@NotNull Service.Mode mode) {
        return mode == Service.Mode.PAYLOAD;
    }

    static void checkNullAllowed(@Nullable Object in, RequestContext rc, WSBinding binding, Service.Mode mode) {
        if (in != null) {
            return;
        }
        if (isXMLHttp(binding)) {
            if (methodNotOk(rc)) {
                throw new WebServiceException(DispatchMessages.INVALID_NULLARG_XMLHTTP_REQUEST_METHOD(HTTP_REQUEST_METHOD_POST, HTTP_REQUEST_METHOD_GET));
            }
        } else if (mode == Service.Mode.MESSAGE) {
            throw new WebServiceException(DispatchMessages.INVALID_NULLARG_SOAP_MSGMODE(mode.name(), Service.Mode.PAYLOAD.toString()));
        }
    }

    static boolean methodNotOk(@NotNull RequestContext rc) {
        String requestMethod = (String) rc.get(MessageContext.HTTP_REQUEST_METHOD);
        String request = requestMethod == null ? HTTP_REQUEST_METHOD_POST : requestMethod;
        return HTTP_REQUEST_METHOD_POST.equalsIgnoreCase(request) || HTTP_REQUEST_METHOD_PUT.equalsIgnoreCase(request);
    }

    public static void checkValidSOAPMessageDispatch(WSBinding binding, Service.Mode mode) {
        if (isXMLHttp(binding)) {
            throw new WebServiceException(DispatchMessages.INVALID_SOAPMESSAGE_DISPATCH_BINDING(HTTPBinding.HTTP_BINDING, "http://schemas.xmlsoap.org/wsdl/soap/http or http://www.w3.org/2003/05/soap/bindings/HTTP/"));
        }
        if (isPAYLOADMode(mode)) {
            throw new WebServiceException(DispatchMessages.INVALID_SOAPMESSAGE_DISPATCH_MSGMODE(mode.name(), Service.Mode.MESSAGE.toString()));
        }
    }

    public static void checkValidDataSourceDispatch(WSBinding binding, Service.Mode mode) {
        if (!isXMLHttp(binding)) {
            throw new WebServiceException(DispatchMessages.INVALID_DATASOURCE_DISPATCH_BINDING("SOAP/HTTP", HTTPBinding.HTTP_BINDING));
        }
        if (isPAYLOADMode(mode)) {
            throw new WebServiceException(DispatchMessages.INVALID_DATASOURCE_DISPATCH_MSGMODE(mode.name(), Service.Mode.MESSAGE.toString()));
        }
    }

    @Override // com.sun.xml.internal.ws.client.Stub
    @NotNull
    public final QName getPortName() {
        return this.portname;
    }

    void resolveEndpointAddress(@NotNull Packet message, @NotNull RequestContext requestContext) {
        String endpoint;
        boolean p2 = message.packetTakesPriorityOverRequestContext;
        if (p2 && message.endpointAddress != null) {
            endpoint = message.endpointAddress.toString();
        } else {
            endpoint = (String) requestContext.get(BindingProvider.ENDPOINT_ADDRESS_PROPERTY);
        }
        if (endpoint == null) {
            if (message.endpointAddress == null) {
                throw new WebServiceException(DispatchMessages.INVALID_NULLARG_URI());
            }
            endpoint = message.endpointAddress.toString();
        }
        String pathInfo = null;
        String queryString = null;
        if (p2 && message.invocationProperties.get(MessageContext.PATH_INFO) != null) {
            pathInfo = (String) message.invocationProperties.get(MessageContext.PATH_INFO);
        } else if (requestContext.get(MessageContext.PATH_INFO) != null) {
            pathInfo = (String) requestContext.get(MessageContext.PATH_INFO);
        }
        if (p2 && message.invocationProperties.get(MessageContext.QUERY_STRING) != null) {
            queryString = (String) message.invocationProperties.get(MessageContext.QUERY_STRING);
        } else if (requestContext.get(MessageContext.QUERY_STRING) != null) {
            queryString = (String) requestContext.get(MessageContext.QUERY_STRING);
        }
        if (pathInfo != null || queryString != null) {
            String pathInfo2 = checkPath(pathInfo);
            String queryString2 = checkQuery(queryString);
            if (endpoint != null) {
                try {
                    URI endpointURI = new URI(endpoint);
                    endpoint = resolveURI(endpointURI, pathInfo2, queryString2);
                } catch (URISyntaxException e2) {
                    throw new WebServiceException(DispatchMessages.INVALID_URI(endpoint));
                }
            }
        }
        requestContext.put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, endpoint);
    }

    @NotNull
    protected String resolveURI(@NotNull URI endpointURI, @Nullable String pathInfo, @Nullable String queryString) {
        String query = null;
        String fragment = null;
        if (queryString != null) {
            try {
                URI tp = new URI(null, null, endpointURI.getPath(), queryString, null);
                URI result = endpointURI.resolve(tp);
                query = result.getQuery();
                fragment = result.getFragment();
            } catch (URISyntaxException e2) {
                throw new WebServiceException(DispatchMessages.INVALID_QUERY_STRING(queryString));
            }
        }
        String path = pathInfo != null ? pathInfo : endpointURI.getPath();
        try {
            StringBuilder spec = new StringBuilder();
            if (path != null) {
                spec.append(path);
            }
            if (query != null) {
                spec.append("?");
                spec.append(query);
            }
            if (fragment != null) {
                spec.append(FXMLLoader.CONTROLLER_METHOD_PREFIX);
                spec.append(fragment);
            }
            return new URL(endpointURI.toURL(), spec.toString()).toExternalForm();
        } catch (MalformedURLException e3) {
            throw new WebServiceException(DispatchMessages.INVALID_URI_RESOLUTION(path));
        }
    }

    private static String checkPath(@Nullable String path) {
        return (path == null || path.startsWith("/")) ? path : "/" + path;
    }

    private static String checkQuery(@Nullable String query) {
        if (query == null) {
            return null;
        }
        if (query.indexOf(63) == 0) {
            throw new WebServiceException(DispatchMessages.INVALID_QUERY_LEADING_CHAR(query));
        }
        return query;
    }

    protected AttachmentSet setOutboundAttachments() {
        HashMap<String, DataHandler> attachments = (HashMap) getRequestContext().get(MessageContext.OUTBOUND_MESSAGE_ATTACHMENTS);
        if (attachments != null) {
            List<Attachment> alist = new ArrayList<>();
            for (Map.Entry<String, DataHandler> att : attachments.entrySet()) {
                DataHandlerAttachment dha = new DataHandlerAttachment(att.getKey(), att.getValue());
                alist.add(dha);
            }
            return new AttachmentSetImpl(alist);
        }
        return new AttachmentSetImpl();
    }

    /* loaded from: rt.jar:com/sun/xml/internal/ws/client/dispatch/DispatchImpl$Invoker.class */
    private class Invoker implements Callable {
        private final T param;
        private final RequestContext rc;
        private ResponseContextReceiver receiver;

        Invoker(T param) {
            this.rc = DispatchImpl.this.requestContext.copy();
            this.param = param;
        }

        @Override // java.util.concurrent.Callable
        public T call() throws Exception {
            if (DispatchImpl.LOGGER.isLoggable(Level.FINE)) {
                DispatchImpl.this.dumpParam(this.param, "call()");
            }
            return (T) DispatchImpl.this.doInvoke(this.param, this.rc, this.receiver);
        }

        void setReceiver(ResponseContextReceiver receiver) {
            this.receiver = receiver;
        }
    }

    /* loaded from: rt.jar:com/sun/xml/internal/ws/client/dispatch/DispatchImpl$DispatchAsyncInvoker.class */
    private class DispatchAsyncInvoker extends AsyncInvoker {
        private final T param;
        private final RequestContext rc;

        DispatchAsyncInvoker(T param) {
            this.rc = DispatchImpl.this.requestContext.copy();
            this.param = param;
        }

        @Override // com.sun.xml.internal.ws.client.AsyncInvoker
        public void do_run() {
            DispatchImpl.checkNullAllowed(this.param, this.rc, DispatchImpl.this.binding, DispatchImpl.this.mode);
            Packet message = DispatchImpl.this.createPacket(this.param);
            message.setState(Packet.State.ClientRequest);
            message.nonNullAsyncHandlerGiven = Boolean.valueOf(this.nonNullAsyncHandlerGiven);
            DispatchImpl.this.resolveEndpointAddress(message, this.rc);
            DispatchImpl.this.setProperties(message, true);
            String action = null;
            String msgId = null;
            if (DispatchImpl.LOGGER.isLoggable(Level.FINE)) {
                AddressingVersion av2 = DispatchImpl.this.getBinding().getAddressingVersion();
                SOAPVersion sv = DispatchImpl.this.getBinding().getSOAPVersion();
                action = (av2 == null || message.getMessage() == null) ? null : AddressingUtils.getAction(message.getMessage().getHeaders(), av2, sv);
                msgId = (av2 == null || message.getMessage() == null) ? null : AddressingUtils.getMessageID(message.getMessage().getHeaders(), av2, sv);
                DispatchImpl.LOGGER.fine("In DispatchAsyncInvoker.do_run for async message with action: " + action + " and msg ID: " + msgId);
            }
            final String actionUse = action;
            final String msgIdUse = msgId;
            Fiber.CompletionCallback callback = new Fiber.CompletionCallback() { // from class: com.sun.xml.internal.ws.client.dispatch.DispatchImpl.DispatchAsyncInvoker.1
                /* JADX WARN: Multi-variable type inference failed */
                @Override // com.sun.xml.internal.ws.api.pipe.Fiber.CompletionCallback
                public void onCompletion(@NotNull Packet response) {
                    if (DispatchImpl.LOGGER.isLoggable(Level.FINE)) {
                        DispatchImpl.LOGGER.fine("Done with processAsync in DispatchAsyncInvoker.do_run, and setting response for async message with action: " + actionUse + " and msg ID: " + msgIdUse);
                    }
                    Message msg = response.getMessage();
                    if (DispatchImpl.LOGGER.isLoggable(Level.FINE)) {
                        DispatchImpl.LOGGER.fine("Done with processAsync in DispatchAsyncInvoker.do_run, and setting response for async message with action: " + actionUse + " and msg ID: " + msgIdUse + " msg: " + ((Object) msg));
                    }
                    if (msg != null) {
                        try {
                            if (msg.isFault() && !DispatchImpl.this.allowFaultResponseMsg) {
                                SOAPFaultBuilder faultBuilder = SOAPFaultBuilder.create(msg);
                                throw ((SOAPFaultException) faultBuilder.createException(null));
                            }
                        } catch (JAXBException e2) {
                            DispatchAsyncInvoker.this.responseImpl.set(null, new DeserializationException(DispatchMessages.INVALID_RESPONSE_DESERIALIZATION(), e2));
                            return;
                        } catch (WebServiceException e3) {
                            DispatchAsyncInvoker.this.responseImpl.set(null, e3);
                            return;
                        } catch (Throwable e4) {
                            DispatchAsyncInvoker.this.responseImpl.set(null, new WebServiceException(e4));
                            return;
                        }
                    }
                    DispatchAsyncInvoker.this.responseImpl.setResponseContext(new ResponseContext(response));
                    DispatchAsyncInvoker.this.responseImpl.set(DispatchImpl.this.toReturnValue(response), null);
                }

                @Override // com.sun.xml.internal.ws.api.pipe.Fiber.CompletionCallback
                public void onCompletion(@NotNull Throwable error) {
                    if (DispatchImpl.LOGGER.isLoggable(Level.FINE)) {
                        DispatchImpl.LOGGER.fine("Done with processAsync in DispatchAsyncInvoker.do_run, and setting response for async message with action: " + actionUse + " and msg ID: " + msgIdUse + " Throwable: " + error.toString());
                    }
                    if (error instanceof WebServiceException) {
                        DispatchAsyncInvoker.this.responseImpl.set(null, error);
                    } else {
                        DispatchAsyncInvoker.this.responseImpl.set(null, new WebServiceException(error));
                    }
                }
            };
            DispatchImpl.this.processAsync(this.responseImpl, message, this.rc, callback);
        }
    }

    @Override // com.sun.xml.internal.ws.developer.WSBindingProvider
    public void setOutboundHeaders(Object... headers) {
        throw new UnsupportedOperationException();
    }

    @Deprecated
    public static Dispatch<Source> createSourceDispatch(QName port, Service.Mode mode, WSServiceDelegate owner, Tube pipe, BindingImpl binding, WSEndpointReference epr) {
        if (isXMLHttp(binding)) {
            return new RESTSourceDispatch(port, mode, owner, pipe, binding, epr);
        }
        return new SOAPSourceDispatch(port, mode, owner, pipe, binding, epr);
    }

    public static Dispatch<Source> createSourceDispatch(WSPortInfo portInfo, Service.Mode mode, BindingImpl binding, WSEndpointReference epr) {
        if (isXMLHttp(binding)) {
            return new RESTSourceDispatch(portInfo, mode, binding, epr);
        }
        return new SOAPSourceDispatch(portInfo, mode, binding, epr);
    }
}
