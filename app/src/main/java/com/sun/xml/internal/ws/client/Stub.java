package com.sun.xml.internal.ws.client;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import com.sun.org.glassfish.gmbal.ManagedObjectManager;
import com.sun.xml.internal.stream.buffer.XMLStreamBuffer;
import com.sun.xml.internal.ws.addressing.WSEPRExtension;
import com.sun.xml.internal.ws.api.BindingID;
import com.sun.xml.internal.ws.api.Component;
import com.sun.xml.internal.ws.api.ComponentFeature;
import com.sun.xml.internal.ws.api.ComponentRegistry;
import com.sun.xml.internal.ws.api.ComponentsFeature;
import com.sun.xml.internal.ws.api.EndpointAddress;
import com.sun.xml.internal.ws.api.WSBinding;
import com.sun.xml.internal.ws.api.WSService;
import com.sun.xml.internal.ws.api.addressing.AddressingVersion;
import com.sun.xml.internal.ws.api.addressing.WSEndpointReference;
import com.sun.xml.internal.ws.api.client.WSPortInfo;
import com.sun.xml.internal.ws.api.message.AddressingUtils;
import com.sun.xml.internal.ws.api.message.Header;
import com.sun.xml.internal.ws.api.message.MessageHeaders;
import com.sun.xml.internal.ws.api.message.Packet;
import com.sun.xml.internal.ws.api.model.SEIModel;
import com.sun.xml.internal.ws.api.model.wsdl.WSDLPort;
import com.sun.xml.internal.ws.api.pipe.ClientTubeAssemblerContext;
import com.sun.xml.internal.ws.api.pipe.Engine;
import com.sun.xml.internal.ws.api.pipe.Fiber;
import com.sun.xml.internal.ws.api.pipe.FiberContextSwitchInterceptorFactory;
import com.sun.xml.internal.ws.api.pipe.SyncStartForAsyncFeature;
import com.sun.xml.internal.ws.api.pipe.Tube;
import com.sun.xml.internal.ws.api.pipe.TubelineAssembler;
import com.sun.xml.internal.ws.api.pipe.TubelineAssemblerFactory;
import com.sun.xml.internal.ws.api.server.Container;
import com.sun.xml.internal.ws.api.server.ContainerResolver;
import com.sun.xml.internal.ws.binding.BindingImpl;
import com.sun.xml.internal.ws.developer.JAXWSProperties;
import com.sun.xml.internal.ws.developer.WSBindingProvider;
import com.sun.xml.internal.ws.model.wsdl.WSDLDirectProperties;
import com.sun.xml.internal.ws.model.wsdl.WSDLPortProperties;
import com.sun.xml.internal.ws.model.wsdl.WSDLProperties;
import com.sun.xml.internal.ws.resources.ClientMessages;
import com.sun.xml.internal.ws.util.Pool;
import com.sun.xml.internal.ws.util.RuntimeVersion;
import com.sun.xml.internal.ws.wsdl.OperationDispatcher;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.Executor;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.management.ObjectName;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.EndpointReference;
import javax.xml.ws.RespectBindingFeature;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.http.HTTPBinding;
import javax.xml.ws.wsaddressing.W3CEndpointReference;

/* loaded from: rt.jar:com/sun/xml/internal/ws/client/Stub.class */
public abstract class Stub implements WSBindingProvider, ResponseContextReceiver, ComponentRegistry {
    public static final String PREVENT_SYNC_START_FOR_ASYNC_INVOKE = "com.sun.xml.internal.ws.client.StubRequestSyncStartForAsyncInvoke";
    private Pool<Tube> tubes;
    private final Engine engine;
    protected final WSServiceDelegate owner;

    @Nullable
    protected WSEndpointReference endpointReference;
    protected final BindingImpl binding;
    protected final WSPortInfo portInfo;
    protected AddressingVersion addrVersion;
    public RequestContext requestContext;
    private final RequestContext cleanRequestContext;
    private ResponseContext responseContext;

    @Nullable
    protected final WSDLPort wsdlPort;
    protected QName portname;

    @Nullable
    private volatile Header[] userOutboundHeaders;

    @NotNull
    private final WSDLProperties wsdlProperties;
    protected OperationDispatcher operationDispatcher;

    @NotNull
    private final ManagedObjectManager managedObjectManager;
    private boolean managedObjectManagerClosed;
    private final Set<Component> components;
    private static final Logger monitoringLogger = Logger.getLogger("com.sun.xml.internal.ws.monitoring");

    @NotNull
    protected abstract QName getPortName();

    @Deprecated
    protected Stub(WSServiceDelegate owner, Tube master, BindingImpl binding, WSDLPort wsdlPort, EndpointAddress defaultEndPointAddress, @Nullable WSEndpointReference epr) {
        this(owner, master, null, null, binding, wsdlPort, defaultEndPointAddress, epr);
    }

    @Deprecated
    protected Stub(QName portname, WSServiceDelegate owner, Tube master, BindingImpl binding, WSDLPort wsdlPort, EndpointAddress defaultEndPointAddress, @Nullable WSEndpointReference epr) {
        this(owner, master, null, portname, binding, wsdlPort, defaultEndPointAddress, epr);
    }

    protected Stub(WSPortInfo portInfo, BindingImpl binding, Tube master, EndpointAddress defaultEndPointAddress, @Nullable WSEndpointReference epr) {
        this((WSServiceDelegate) portInfo.getOwner(), master, portInfo, null, binding, portInfo.getPort(), defaultEndPointAddress, epr);
    }

    protected Stub(WSPortInfo portInfo, BindingImpl binding, EndpointAddress defaultEndPointAddress, @Nullable WSEndpointReference epr) {
        this(portInfo, binding, null, defaultEndPointAddress, epr);
    }

    private Stub(WSServiceDelegate owner, @Nullable Tube master, @Nullable WSPortInfo portInfo, QName portname, BindingImpl binding, @Nullable WSDLPort wsdlPort, EndpointAddress defaultEndPointAddress, @Nullable WSEndpointReference epr) {
        this.requestContext = new RequestContext();
        this.operationDispatcher = null;
        this.managedObjectManagerClosed = false;
        this.components = new CopyOnWriteArraySet();
        Container old = ContainerResolver.getDefault().enterContainer(owner.getContainer());
        try {
            this.owner = owner;
            this.portInfo = portInfo;
            this.wsdlPort = wsdlPort != null ? wsdlPort : portInfo != null ? portInfo.getPort() : null;
            this.portname = portname;
            if (portname == null) {
                if (portInfo != null) {
                    this.portname = portInfo.getPortName();
                } else if (wsdlPort != null) {
                    this.portname = wsdlPort.getName();
                }
            }
            this.binding = binding;
            ComponentFeature cf = (ComponentFeature) binding.getFeature(ComponentFeature.class);
            if (cf != null && ComponentFeature.Target.STUB.equals(cf.getTarget())) {
                this.components.add(cf.getComponent());
            }
            ComponentsFeature csf = (ComponentsFeature) binding.getFeature(ComponentsFeature.class);
            if (csf != null) {
                for (ComponentFeature cfi : csf.getComponentFeatures()) {
                    if (ComponentFeature.Target.STUB.equals(cfi.getTarget())) {
                        this.components.add(cfi.getComponent());
                    }
                }
            }
            if (epr != null) {
                this.requestContext.setEndPointAddressString(epr.getAddress());
            } else {
                this.requestContext.setEndpointAddress(defaultEndPointAddress);
            }
            this.engine = new Engine(getStringId(), owner.getContainer(), owner.getExecutor());
            this.endpointReference = epr;
            this.wsdlProperties = wsdlPort == null ? new WSDLDirectProperties(owner.getServiceName(), portname) : new WSDLPortProperties(wsdlPort);
            this.cleanRequestContext = this.requestContext.copy();
            this.managedObjectManager = new MonitorRootClient(this).createManagedObjectManager(this);
            if (master != null) {
                this.tubes = new Pool.TubePool(master);
            } else {
                this.tubes = new Pool.TubePool(createPipeline(portInfo, binding));
            }
            this.addrVersion = binding.getAddressingVersion();
            this.managedObjectManager.resumeJMXRegistration();
            ContainerResolver.getDefault().exitContainer(old);
        } catch (Throwable th) {
            ContainerResolver.getDefault().exitContainer(old);
            throw th;
        }
    }

    private Tube createPipeline(WSPortInfo portInfo, WSBinding binding) {
        checkAllWSDLExtensionsUnderstood(portInfo, binding);
        SEIModel seiModel = null;
        Class sei = null;
        if (portInfo instanceof SEIPortInfo) {
            SEIPortInfo sp = (SEIPortInfo) portInfo;
            seiModel = sp.model;
            sei = sp.sei;
        }
        BindingID bindingId = portInfo.getBindingId();
        TubelineAssembler assembler = TubelineAssemblerFactory.create(Thread.currentThread().getContextClassLoader(), bindingId, this.owner.getContainer());
        if (assembler == null) {
            throw new WebServiceException("Unable to process bindingID=" + ((Object) bindingId));
        }
        return assembler.createClient(new ClientTubeAssemblerContext(portInfo.getEndpointAddress(), portInfo.getPort(), this, binding, this.owner.getContainer(), ((BindingImpl) binding).createCodec(), seiModel, sei));
    }

    public WSDLPort getWSDLPort() {
        return this.wsdlPort;
    }

    public WSService getService() {
        return this.owner;
    }

    public Pool<Tube> getTubes() {
        return this.tubes;
    }

    private static void checkAllWSDLExtensionsUnderstood(WSPortInfo port, WSBinding binding) {
        if (port.getPort() != null && binding.isFeatureEnabled(RespectBindingFeature.class)) {
            port.getPort().areRequiredExtensionsUnderstood();
        }
    }

    @Override // com.sun.xml.internal.ws.developer.WSBindingProvider
    public WSPortInfo getPortInfo() {
        return this.portInfo;
    }

    @Nullable
    public OperationDispatcher getOperationDispatcher() {
        if (this.operationDispatcher == null && this.wsdlPort != null) {
            this.operationDispatcher = new OperationDispatcher(this.wsdlPort, this.binding, null);
        }
        return this.operationDispatcher;
    }

    @NotNull
    protected final QName getServiceName() {
        return this.owner.getServiceName();
    }

    public final Executor getExecutor() {
        return this.owner.getExecutor();
    }

    protected final Packet process(Packet packet, RequestContext requestContext, ResponseContextReceiver receiver) {
        packet.isSynchronousMEP = true;
        packet.component = this;
        configureRequestPacket(packet, requestContext);
        Pool<Tube> pool = this.tubes;
        if (pool == null) {
            throw new WebServiceException("close method has already been invoked");
        }
        Fiber fiber = this.engine.createFiber();
        configureFiber(fiber);
        Tube tube = pool.take();
        try {
            Packet packetRunSync = fiber.runSync(tube, packet);
            Packet reply = fiber.getPacket() == null ? packet : fiber.getPacket();
            receiver.setResponseContext(new ResponseContext(reply));
            pool.recycle(tube);
            return packetRunSync;
        } catch (Throwable th) {
            Packet reply2 = fiber.getPacket() == null ? packet : fiber.getPacket();
            receiver.setResponseContext(new ResponseContext(reply2));
            pool.recycle(tube);
            throw th;
        }
    }

    private void configureRequestPacket(Packet packet, RequestContext requestContext) {
        packet.proxy = this;
        packet.handlerConfig = this.binding.getHandlerConfig();
        Header[] hl = this.userOutboundHeaders;
        if (hl != null) {
            MessageHeaders mh = packet.getMessage().getHeaders();
            for (Header h2 : hl) {
                mh.add(h2);
            }
        }
        requestContext.fill(packet, this.binding.getAddressingVersion() != null);
        packet.addSatellite(this.wsdlProperties);
        if (this.addrVersion != null) {
            MessageHeaders headerList = packet.getMessage().getHeaders();
            AddressingUtils.fillRequestAddressingHeaders(headerList, this.wsdlPort, this.binding, packet);
            if (this.endpointReference != null) {
                this.endpointReference.addReferenceParametersToList(packet.getMessage().getHeaders());
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final void processAsync(AsyncResponseImpl<?> receiver, Packet request, RequestContext requestContext, final Fiber.CompletionCallback completionCallback) {
        request.component = this;
        configureRequestPacket(request, requestContext);
        final Pool<Tube> pool = this.tubes;
        if (pool == null) {
            throw new WebServiceException("close method has already been invoked");
        }
        Fiber fiber = this.engine.createFiber();
        configureFiber(fiber);
        receiver.setCancelable(fiber);
        if (receiver.isCancelled()) {
            return;
        }
        FiberContextSwitchInterceptorFactory fcsif = (FiberContextSwitchInterceptorFactory) this.owner.getSPI(FiberContextSwitchInterceptorFactory.class);
        if (fcsif != null) {
            fiber.addInterceptor(fcsif.create());
        }
        final Tube tube = pool.take();
        Fiber.CompletionCallback fiberCallback = new Fiber.CompletionCallback() { // from class: com.sun.xml.internal.ws.client.Stub.1
            @Override // com.sun.xml.internal.ws.api.pipe.Fiber.CompletionCallback
            public void onCompletion(@NotNull Packet response) {
                pool.recycle(tube);
                completionCallback.onCompletion(response);
            }

            @Override // com.sun.xml.internal.ws.api.pipe.Fiber.CompletionCallback
            public void onCompletion(@NotNull Throwable error) {
                completionCallback.onCompletion(error);
            }
        };
        fiber.start(tube, request, fiberCallback, getBinding().isFeatureEnabled(SyncStartForAsyncFeature.class) && !requestContext.containsKey(PREVENT_SYNC_START_FOR_ASYNC_INVOKE));
    }

    protected void configureFiber(Fiber fiber) {
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        Pool.TubePool tp = (Pool.TubePool) this.tubes;
        if (tp != null) {
            Tube p2 = tp.takeMaster();
            p2.preDestroy();
            this.tubes = null;
        }
        if (!this.managedObjectManagerClosed) {
            try {
                ObjectName name = this.managedObjectManager.getObjectName(this.managedObjectManager.getRoot());
                if (name != null) {
                    monitoringLogger.log(Level.INFO, "Closing Metro monitoring root: {0}", name);
                }
                this.managedObjectManager.close();
            } catch (IOException e2) {
                monitoringLogger.log(Level.WARNING, "Ignoring error when closing Managed Object Manager", (Throwable) e2);
            }
            this.managedObjectManagerClosed = true;
        }
    }

    @Override // javax.xml.ws.BindingProvider
    public final WSBinding getBinding() {
        return this.binding;
    }

    @Override // javax.xml.ws.BindingProvider
    public final Map<String, Object> getRequestContext() {
        return this.requestContext.asMap();
    }

    public void resetRequestContext() {
        this.requestContext = this.cleanRequestContext.copy();
    }

    @Override // javax.xml.ws.BindingProvider
    public final ResponseContext getResponseContext() {
        return this.responseContext;
    }

    @Override // com.sun.xml.internal.ws.client.ResponseContextReceiver
    public void setResponseContext(ResponseContext rc) {
        this.responseContext = rc;
    }

    private String getStringId() {
        return ((Object) RuntimeVersion.VERSION) + ": Stub for " + getRequestContext().get(BindingProvider.ENDPOINT_ADDRESS_PROPERTY);
    }

    public String toString() {
        return getStringId();
    }

    @Override // com.sun.xml.internal.ws.developer.WSBindingProvider
    public final WSEndpointReference getWSEndpointReference() {
        if (this.binding.getBindingID().equals(HTTPBinding.HTTP_BINDING)) {
            throw new UnsupportedOperationException(ClientMessages.UNSUPPORTED_OPERATION("BindingProvider.getEndpointReference(Class<T> class)", "XML/HTTP Binding", "SOAP11 or SOAP12 Binding"));
        }
        if (this.endpointReference != null) {
            return this.endpointReference;
        }
        String eprAddress = this.requestContext.getEndpointAddress().toString();
        QName portTypeName = null;
        String wsdlAddress = null;
        List<WSEndpointReference.EPRExtension> wsdlEPRExtensions = new ArrayList<>();
        if (this.wsdlPort != null) {
            portTypeName = this.wsdlPort.getBinding().getPortTypeName();
            wsdlAddress = eprAddress + "?wsdl";
            try {
                WSEndpointReference wsdlEpr = this.wsdlPort.getEPR();
                if (wsdlEpr != null) {
                    for (WSEndpointReference.EPRExtension extnEl : wsdlEpr.getEPRExtensions()) {
                        wsdlEPRExtensions.add(new WSEPRExtension(XMLStreamBuffer.createNewBufferFromXMLStreamReader(extnEl.readAsXMLStreamReader()), extnEl.getQName()));
                    }
                }
            } catch (XMLStreamException ex) {
                throw new WebServiceException(ex);
            }
        }
        AddressingVersion av2 = AddressingVersion.W3C;
        this.endpointReference = new WSEndpointReference(av2, eprAddress, getServiceName(), getPortName(), portTypeName, null, wsdlAddress, null, wsdlEPRExtensions, null);
        return this.endpointReference;
    }

    @Override // javax.xml.ws.BindingProvider
    public final W3CEndpointReference getEndpointReference() {
        if (this.binding.getBindingID().equals(HTTPBinding.HTTP_BINDING)) {
            throw new UnsupportedOperationException(ClientMessages.UNSUPPORTED_OPERATION("BindingProvider.getEndpointReference()", "XML/HTTP Binding", "SOAP11 or SOAP12 Binding"));
        }
        return (W3CEndpointReference) getEndpointReference(W3CEndpointReference.class);
    }

    @Override // javax.xml.ws.BindingProvider
    public final <T extends EndpointReference> T getEndpointReference(Class<T> cls) {
        return (T) getWSEndpointReference().toSpec(cls);
    }

    @Override // com.sun.xml.internal.ws.developer.WSBindingProvider
    @NotNull
    public ManagedObjectManager getManagedObjectManager() {
        return this.managedObjectManager;
    }

    @Override // com.sun.xml.internal.ws.developer.WSBindingProvider
    public final void setOutboundHeaders(List<Header> headers) {
        if (headers == null) {
            this.userOutboundHeaders = null;
            return;
        }
        for (Header h2 : headers) {
            if (h2 == null) {
                throw new IllegalArgumentException();
            }
        }
        this.userOutboundHeaders = (Header[]) headers.toArray(new Header[headers.size()]);
    }

    @Override // com.sun.xml.internal.ws.developer.WSBindingProvider
    public final void setOutboundHeaders(Header... headers) {
        if (headers == null) {
            this.userOutboundHeaders = null;
            return;
        }
        for (Header h2 : headers) {
            if (h2 == null) {
                throw new IllegalArgumentException();
            }
        }
        Header[] hl = new Header[headers.length];
        System.arraycopy(headers, 0, hl, 0, headers.length);
        this.userOutboundHeaders = hl;
    }

    @Override // com.sun.xml.internal.ws.developer.WSBindingProvider
    public final List<Header> getInboundHeaders() {
        return Collections.unmodifiableList(((MessageHeaders) this.responseContext.get(JAXWSProperties.INBOUND_HEADER_LIST_PROPERTY)).asList());
    }

    @Override // com.sun.xml.internal.ws.developer.WSBindingProvider
    public final void setAddress(String address) {
        this.requestContext.put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, address);
    }

    @Override // com.sun.xml.internal.ws.api.Component
    public <S> S getSPI(Class<S> cls) {
        Iterator<Component> it = this.components.iterator();
        while (it.hasNext()) {
            S s2 = (S) it.next().getSPI(cls);
            if (s2 != null) {
                return s2;
            }
        }
        return (S) this.owner.getSPI(cls);
    }

    @Override // com.sun.xml.internal.ws.api.ComponentRegistry
    public Set<Component> getComponents() {
        return this.components;
    }
}
