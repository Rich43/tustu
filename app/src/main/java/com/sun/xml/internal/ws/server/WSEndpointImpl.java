package com.sun.xml.internal.ws.server;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import com.sun.org.glassfish.gmbal.ManagedObjectManager;
import com.sun.xml.internal.stream.buffer.XMLStreamBuffer;
import com.sun.xml.internal.ws.addressing.EPRSDDocumentFilter;
import com.sun.xml.internal.ws.addressing.WSEPRExtension;
import com.sun.xml.internal.ws.api.Component;
import com.sun.xml.internal.ws.api.ComponentFeature;
import com.sun.xml.internal.ws.api.ComponentsFeature;
import com.sun.xml.internal.ws.api.SOAPVersion;
import com.sun.xml.internal.ws.api.WSBinding;
import com.sun.xml.internal.ws.api.addressing.AddressingVersion;
import com.sun.xml.internal.ws.api.addressing.WSEndpointReference;
import com.sun.xml.internal.ws.api.message.Message;
import com.sun.xml.internal.ws.api.message.Packet;
import com.sun.xml.internal.ws.api.model.SEIModel;
import com.sun.xml.internal.ws.api.model.wsdl.WSDLPort;
import com.sun.xml.internal.ws.api.pipe.Codec;
import com.sun.xml.internal.ws.api.pipe.Engine;
import com.sun.xml.internal.ws.api.pipe.Fiber;
import com.sun.xml.internal.ws.api.pipe.FiberContextSwitchInterceptor;
import com.sun.xml.internal.ws.api.pipe.ServerPipeAssemblerContext;
import com.sun.xml.internal.ws.api.pipe.ServerTubeAssemblerContext;
import com.sun.xml.internal.ws.api.pipe.SyncStartForAsyncFeature;
import com.sun.xml.internal.ws.api.pipe.ThrowableContainerPropertySet;
import com.sun.xml.internal.ws.api.pipe.Tube;
import com.sun.xml.internal.ws.api.pipe.TubeCloner;
import com.sun.xml.internal.ws.api.pipe.TubelineAssembler;
import com.sun.xml.internal.ws.api.pipe.TubelineAssemblerFactory;
import com.sun.xml.internal.ws.api.server.Container;
import com.sun.xml.internal.ws.api.server.ContainerResolver;
import com.sun.xml.internal.ws.api.server.EndpointAwareCodec;
import com.sun.xml.internal.ws.api.server.EndpointComponent;
import com.sun.xml.internal.ws.api.server.EndpointReferenceExtensionContributor;
import com.sun.xml.internal.ws.api.server.LazyMOMProvider;
import com.sun.xml.internal.ws.api.server.TransportBackChannel;
import com.sun.xml.internal.ws.api.server.WSEndpoint;
import com.sun.xml.internal.ws.api.server.WebServiceContextDelegate;
import com.sun.xml.internal.ws.binding.BindingImpl;
import com.sun.xml.internal.ws.fault.SOAPFaultBuilder;
import com.sun.xml.internal.ws.model.CheckedExceptionImpl;
import com.sun.xml.internal.ws.model.wsdl.WSDLDirectProperties;
import com.sun.xml.internal.ws.model.wsdl.WSDLPortProperties;
import com.sun.xml.internal.ws.model.wsdl.WSDLProperties;
import com.sun.xml.internal.ws.policy.PolicyMap;
import com.sun.xml.internal.ws.resources.HandlerMessages;
import com.sun.xml.internal.ws.util.Pool;
import com.sun.xml.internal.ws.util.ServiceFinder;
import com.sun.xml.internal.ws.wsdl.OperationDispatcher;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.Executor;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PreDestroy;
import javax.management.ObjectName;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.ws.EndpointReference;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.handler.Handler;
import org.w3c.dom.Element;

/* loaded from: rt.jar:com/sun/xml/internal/ws/server/WSEndpointImpl.class */
public class WSEndpointImpl<T> extends WSEndpoint<T> implements LazyMOMProvider.WSEndpointScopeChangeListener {
    private static final Logger logger;

    @NotNull
    private final QName serviceName;

    @NotNull
    private final QName portName;
    protected final WSBinding binding;
    private final SEIModel seiModel;

    @NotNull
    private final Container container;
    private final WSDLPort port;
    protected final Tube masterTubeline;
    private final ServiceDefinitionImpl serviceDef;
    private final SOAPVersion soapVersion;
    private final Engine engine;

    @NotNull
    private final Codec masterCodec;

    @NotNull
    private final PolicyMap endpointPolicy;
    private final Pool<Tube> tubePool;
    private final OperationDispatcher operationDispatcher;

    @NotNull
    private ManagedObjectManager managedObjectManager;
    private boolean managedObjectManagerClosed;
    private final Object managedObjectManagerLock;
    private LazyMOMProvider.Scope lazyMOMProviderScope;

    @NotNull
    private final ServerTubeAssemblerContext context;
    private Map<QName, WSEndpointReference.EPRExtension> endpointReferenceExtensions;
    private boolean disposed;
    private final Class<T> implementationClass;

    @NotNull
    private final WSDLProperties wsdlProperties;
    private final Set<Component> componentRegistry;
    private static final Logger monitoringLogger;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !WSEndpointImpl.class.desiredAssertionStatus();
        logger = Logger.getLogger("com.sun.xml.internal.ws.server.endpoint");
        monitoringLogger = Logger.getLogger("com.sun.xml.internal.ws.monitoring");
    }

    protected WSEndpointImpl(@NotNull QName serviceName, @NotNull QName portName, WSBinding binding, Container container, SEIModel seiModel, WSDLPort port, Class<T> implementationClass, @Nullable ServiceDefinitionImpl serviceDef, EndpointAwareTube terminalTube, boolean isSynchronous, PolicyMap endpointPolicy) {
        this.managedObjectManagerClosed = false;
        this.managedObjectManagerLock = new Object();
        this.lazyMOMProviderScope = LazyMOMProvider.Scope.STANDALONE;
        this.endpointReferenceExtensions = new HashMap();
        this.componentRegistry = new CopyOnWriteArraySet();
        this.serviceName = serviceName;
        this.portName = portName;
        this.binding = binding;
        this.soapVersion = binding.getSOAPVersion();
        this.container = container;
        this.port = port;
        this.implementationClass = implementationClass;
        this.serviceDef = serviceDef;
        this.seiModel = seiModel;
        this.endpointPolicy = endpointPolicy;
        LazyMOMProvider.INSTANCE.registerEndpoint(this);
        initManagedObjectManager();
        if (serviceDef != null) {
            serviceDef.setOwner(this);
        }
        ComponentFeature cf = (ComponentFeature) binding.getFeature(ComponentFeature.class);
        if (cf != null) {
            switch (cf.getTarget()) {
                case ENDPOINT:
                    this.componentRegistry.add(cf.getComponent());
                    break;
                case CONTAINER:
                    container.getComponents().add(cf.getComponent());
                    break;
                default:
                    throw new IllegalArgumentException();
            }
        }
        ComponentsFeature csf = (ComponentsFeature) binding.getFeature(ComponentsFeature.class);
        if (csf != null) {
            for (ComponentFeature cfi : csf.getComponentFeatures()) {
                switch (cfi.getTarget()) {
                    case ENDPOINT:
                        this.componentRegistry.add(cfi.getComponent());
                        break;
                    case CONTAINER:
                        container.getComponents().add(cfi.getComponent());
                        break;
                    default:
                        throw new IllegalArgumentException();
                }
            }
        }
        TubelineAssembler assembler = TubelineAssemblerFactory.create(Thread.currentThread().getContextClassLoader(), binding.getBindingId(), container);
        if (!$assertionsDisabled && assembler == null) {
            throw new AssertionError();
        }
        this.operationDispatcher = port == null ? null : new OperationDispatcher(port, binding, seiModel);
        this.context = createServerTubeAssemblerContext(terminalTube, isSynchronous);
        this.masterTubeline = assembler.createServer(this.context);
        Codec c2 = this.context.getCodec();
        if (c2 instanceof EndpointAwareCodec) {
            c2 = c2.copy();
            ((EndpointAwareCodec) c2).setEndpoint(this);
        }
        this.masterCodec = c2;
        this.tubePool = new Pool.TubePool(this.masterTubeline);
        terminalTube.setEndpoint(this);
        this.engine = new Engine(toString(), container);
        this.wsdlProperties = port == null ? new WSDLDirectProperties(serviceName, portName, seiModel) : new WSDLPortProperties(port, seiModel);
        Map<QName, WSEndpointReference.EPRExtension> eprExtensions = new HashMap<>();
        if (port != null) {
            try {
                WSEndpointReference wsdlEpr = port.getEPR();
                if (wsdlEpr != null) {
                    for (WSEndpointReference.EPRExtension extnEl : wsdlEpr.getEPRExtensions()) {
                        eprExtensions.put(extnEl.getQName(), extnEl);
                    }
                }
            } catch (XMLStreamException ex) {
                throw new WebServiceException(ex);
            }
        }
        EndpointReferenceExtensionContributor[] eprExtnContributors = (EndpointReferenceExtensionContributor[]) ServiceFinder.find(EndpointReferenceExtensionContributor.class).toArray();
        for (EndpointReferenceExtensionContributor eprExtnContributor : eprExtnContributors) {
            WSEndpointReference.EPRExtension wsdlEPRExtn = eprExtensions.remove(eprExtnContributor.getQName());
            WSEndpointReference.EPRExtension endpointEprExtn = eprExtnContributor.getEPRExtension(this, wsdlEPRExtn);
            if (endpointEprExtn != null) {
                eprExtensions.put(endpointEprExtn.getQName(), endpointEprExtn);
            }
        }
        for (WSEndpointReference.EPRExtension extn : eprExtensions.values()) {
            this.endpointReferenceExtensions.put(extn.getQName(), new WSEPRExtension(XMLStreamBuffer.createNewBufferFromXMLStreamReader(extn.readAsXMLStreamReader()), extn.getQName()));
        }
        if (!eprExtensions.isEmpty()) {
            serviceDef.addFilter(new EPRSDDocumentFilter(this));
        }
    }

    protected ServerTubeAssemblerContext createServerTubeAssemblerContext(EndpointAwareTube terminalTube, boolean isSynchronous) {
        ServerTubeAssemblerContext ctx = new ServerPipeAssemblerContext(this.seiModel, this.port, this, terminalTube, isSynchronous);
        return ctx;
    }

    protected WSEndpointImpl(@NotNull QName serviceName, @NotNull QName portName, WSBinding binding, Container container, SEIModel seiModel, WSDLPort port, Tube masterTubeline) {
        this.managedObjectManagerClosed = false;
        this.managedObjectManagerLock = new Object();
        this.lazyMOMProviderScope = LazyMOMProvider.Scope.STANDALONE;
        this.endpointReferenceExtensions = new HashMap();
        this.componentRegistry = new CopyOnWriteArraySet();
        this.serviceName = serviceName;
        this.portName = portName;
        this.binding = binding;
        this.soapVersion = binding.getSOAPVersion();
        this.container = container;
        this.endpointPolicy = null;
        this.port = port;
        this.seiModel = seiModel;
        this.serviceDef = null;
        this.implementationClass = null;
        this.masterTubeline = masterTubeline;
        this.masterCodec = ((BindingImpl) this.binding).createCodec();
        LazyMOMProvider.INSTANCE.registerEndpoint(this);
        initManagedObjectManager();
        this.operationDispatcher = port == null ? null : new OperationDispatcher(port, binding, seiModel);
        this.context = new ServerPipeAssemblerContext(seiModel, port, this, null, false);
        this.tubePool = new Pool.TubePool(masterTubeline);
        this.engine = new Engine(toString(), container);
        this.wsdlProperties = port == null ? new WSDLDirectProperties(serviceName, portName, seiModel) : new WSDLPortProperties(port, seiModel);
    }

    public Collection<WSEndpointReference.EPRExtension> getEndpointReferenceExtensions() {
        return this.endpointReferenceExtensions.values();
    }

    @Override // com.sun.xml.internal.ws.api.server.WSEndpoint
    @Nullable
    public OperationDispatcher getOperationDispatcher() {
        return this.operationDispatcher;
    }

    @Override // com.sun.xml.internal.ws.api.server.WSEndpoint
    public PolicyMap getPolicyMap() {
        return this.endpointPolicy;
    }

    @Override // com.sun.xml.internal.ws.api.server.WSEndpoint
    @NotNull
    public Class<T> getImplementationClass() {
        return this.implementationClass;
    }

    @Override // com.sun.xml.internal.ws.api.server.WSEndpoint
    @NotNull
    public WSBinding getBinding() {
        return this.binding;
    }

    @Override // com.sun.xml.internal.ws.api.server.WSEndpoint
    @NotNull
    public Container getContainer() {
        return this.container;
    }

    @Override // com.sun.xml.internal.ws.api.server.WSEndpoint
    public WSDLPort getPort() {
        return this.port;
    }

    @Override // com.sun.xml.internal.ws.api.server.WSEndpoint
    @Nullable
    public SEIModel getSEIModel() {
        return this.seiModel;
    }

    @Override // com.sun.xml.internal.ws.api.server.WSEndpoint
    public void setExecutor(Executor exec) {
        this.engine.setExecutor(exec);
    }

    @Override // com.sun.xml.internal.ws.api.server.WSEndpoint
    public Engine getEngine() {
        return this.engine;
    }

    @Override // com.sun.xml.internal.ws.api.server.WSEndpoint
    public void schedule(Packet request, WSEndpoint.CompletionCallback callback, FiberContextSwitchInterceptor interceptor) {
        processAsync(request, callback, interceptor, true);
    }

    private void processAsync(final Packet request, final WSEndpoint.CompletionCallback callback, FiberContextSwitchInterceptor interceptor, boolean schedule) {
        Container old = ContainerResolver.getDefault().enterContainer(this.container);
        try {
            request.endpoint = this;
            request.addSatellite(this.wsdlProperties);
            Fiber fiber = this.engine.createFiber();
            fiber.setDeliverThrowableInPacket(true);
            if (interceptor != null) {
                fiber.addInterceptor(interceptor);
            }
            final Tube tube = this.tubePool.take();
            Fiber.CompletionCallback cbak = new Fiber.CompletionCallback() { // from class: com.sun.xml.internal.ws.server.WSEndpointImpl.1
                @Override // com.sun.xml.internal.ws.api.pipe.Fiber.CompletionCallback
                public void onCompletion(@NotNull Packet response) {
                    ThrowableContainerPropertySet tc = (ThrowableContainerPropertySet) response.getSatellite(ThrowableContainerPropertySet.class);
                    if (tc == null) {
                        WSEndpointImpl.this.tubePool.recycle(tube);
                    }
                    if (callback != null) {
                        if (tc != null) {
                            response = WSEndpointImpl.this.createServiceResponseForException(tc, response, WSEndpointImpl.this.soapVersion, request.endpoint.getPort(), null, request.endpoint.getBinding());
                        }
                        callback.onCompletion(response);
                    }
                }

                @Override // com.sun.xml.internal.ws.api.pipe.Fiber.CompletionCallback
                public void onCompletion(@NotNull Throwable error) {
                    throw new IllegalStateException();
                }
            };
            fiber.start(tube, request, cbak, this.binding.isFeatureEnabled(SyncStartForAsyncFeature.class) || !schedule);
            ContainerResolver.getDefault().exitContainer(old);
        } catch (Throwable th) {
            ContainerResolver.getDefault().exitContainer(old);
            throw th;
        }
    }

    @Override // com.sun.xml.internal.ws.api.server.WSEndpoint
    public Packet createServiceResponseForException(ThrowableContainerPropertySet tc, Packet responsePacket, SOAPVersion soapVersion, WSDLPort wsdlPort, SEIModel seiModel, WSBinding binding) {
        if (tc.isFaultCreated()) {
            return responsePacket;
        }
        Message faultMessage = SOAPFaultBuilder.createSOAPFaultMessage(soapVersion, (CheckedExceptionImpl) null, tc.getThrowable());
        Packet result = responsePacket.createServerResponse(faultMessage, wsdlPort, seiModel, binding);
        tc.setFaultMessage(faultMessage);
        tc.setResponsePacket(responsePacket);
        tc.setFaultCreated(true);
        return result;
    }

    @Override // com.sun.xml.internal.ws.api.server.WSEndpoint
    public void process(Packet request, WSEndpoint.CompletionCallback callback, FiberContextSwitchInterceptor interceptor) {
        processAsync(request, callback, interceptor, false);
    }

    @Override // com.sun.xml.internal.ws.api.server.WSEndpoint
    @NotNull
    public WSEndpoint.PipeHead createPipeHead() {
        return new WSEndpoint.PipeHead() { // from class: com.sun.xml.internal.ws.server.WSEndpointImpl.2
            private final Tube tube;

            {
                this.tube = TubeCloner.clone(WSEndpointImpl.this.masterTubeline);
            }

            @Override // com.sun.xml.internal.ws.api.server.WSEndpoint.PipeHead
            @NotNull
            public Packet process(Packet request, WebServiceContextDelegate wscd, TransportBackChannel tbc) {
                Packet response;
                Container old = ContainerResolver.getDefault().enterContainer(WSEndpointImpl.this.container);
                try {
                    request.webServiceContextDelegate = wscd;
                    request.transportBackChannel = tbc;
                    request.endpoint = WSEndpointImpl.this;
                    request.addSatellite(WSEndpointImpl.this.wsdlProperties);
                    Fiber fiber = WSEndpointImpl.this.engine.createFiber();
                    try {
                        response = fiber.runSync(this.tube, request);
                    } catch (RuntimeException re) {
                        Message faultMsg = SOAPFaultBuilder.createSOAPFaultMessage(WSEndpointImpl.this.soapVersion, (CheckedExceptionImpl) null, re);
                        response = request.createServerResponse(faultMsg, request.endpoint.getPort(), (SEIModel) null, request.endpoint.getBinding());
                    }
                    Packet packet = response;
                    ContainerResolver.getDefault().exitContainer(old);
                    return packet;
                } catch (Throwable th) {
                    ContainerResolver.getDefault().exitContainer(old);
                    throw th;
                }
            }
        };
    }

    @Override // com.sun.xml.internal.ws.api.server.WSEndpoint
    public synchronized void dispose() {
        if (this.disposed) {
            return;
        }
        this.disposed = true;
        this.masterTubeline.preDestroy();
        for (Handler handler : this.binding.getHandlerChain()) {
            Method[] methods = handler.getClass().getMethods();
            int length = methods.length;
            int i2 = 0;
            while (true) {
                if (i2 < length) {
                    Method method = methods[i2];
                    if (method.getAnnotation(PreDestroy.class) == null) {
                        i2++;
                    } else {
                        try {
                            method.invoke(handler, new Object[0]);
                            break;
                        } catch (Exception e2) {
                            logger.log(Level.WARNING, HandlerMessages.HANDLER_PREDESTROY_IGNORE(e2.getMessage()), (Throwable) e2);
                        }
                    }
                }
            }
        }
        closeManagedObjectManager();
        LazyMOMProvider.INSTANCE.unregisterEndpoint(this);
    }

    @Override // com.sun.xml.internal.ws.api.server.WSEndpoint
    public ServiceDefinitionImpl getServiceDefinition() {
        return this.serviceDef;
    }

    @Override // com.sun.xml.internal.ws.api.server.WSEndpoint
    public Set<EndpointComponent> getComponentRegistry() {
        Set<EndpointComponent> sec = new EndpointComponentSet();
        for (Component c2 : this.componentRegistry) {
            sec.add(c2 instanceof EndpointComponentWrapper ? ((EndpointComponentWrapper) c2).component : new ComponentWrapper(c2));
        }
        return sec;
    }

    /* loaded from: rt.jar:com/sun/xml/internal/ws/server/WSEndpointImpl$EndpointComponentSet.class */
    private class EndpointComponentSet extends HashSet<EndpointComponent> {
        private EndpointComponentSet() {
        }

        @Override // java.util.HashSet, java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.List
        public Iterator<EndpointComponent> iterator() {
            final Iterator<EndpointComponent> it = super.iterator();
            return new Iterator<EndpointComponent>() { // from class: com.sun.xml.internal.ws.server.WSEndpointImpl.EndpointComponentSet.1
                private EndpointComponent last = null;

                @Override // java.util.Iterator
                public boolean hasNext() {
                    return it.hasNext();
                }

                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.util.Iterator
                public EndpointComponent next() {
                    this.last = (EndpointComponent) it.next();
                    return this.last;
                }

                @Override // java.util.Iterator
                public void remove() {
                    it.remove();
                    if (this.last != null) {
                        WSEndpointImpl.this.componentRegistry.remove(this.last instanceof ComponentWrapper ? ((ComponentWrapper) this.last).component : new EndpointComponentWrapper(this.last));
                    }
                    this.last = null;
                }
            };
        }

        @Override // java.util.HashSet, java.util.AbstractCollection, java.util.Collection, java.util.List
        public boolean add(EndpointComponent e2) {
            boolean result = super.add((EndpointComponentSet) e2);
            if (result) {
                WSEndpointImpl.this.componentRegistry.add(new EndpointComponentWrapper(e2));
            }
            return result;
        }

        @Override // java.util.HashSet, java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean remove(Object o2) {
            boolean result = super.remove(o2);
            if (result) {
                WSEndpointImpl.this.componentRegistry.remove(o2 instanceof ComponentWrapper ? ((ComponentWrapper) o2).component : new EndpointComponentWrapper((EndpointComponent) o2));
            }
            return result;
        }
    }

    /* loaded from: rt.jar:com/sun/xml/internal/ws/server/WSEndpointImpl$ComponentWrapper.class */
    private static class ComponentWrapper implements EndpointComponent {
        private final Component component;

        public ComponentWrapper(Component component) {
            this.component = component;
        }

        @Override // com.sun.xml.internal.ws.api.server.EndpointComponent
        public <S> S getSPI(Class<S> cls) {
            return (S) this.component.getSPI(cls);
        }

        public int hashCode() {
            return this.component.hashCode();
        }

        public boolean equals(Object obj) {
            return this.component.equals(obj);
        }
    }

    /* loaded from: rt.jar:com/sun/xml/internal/ws/server/WSEndpointImpl$EndpointComponentWrapper.class */
    private static class EndpointComponentWrapper implements Component {
        private final EndpointComponent component;

        public EndpointComponentWrapper(EndpointComponent component) {
            this.component = component;
        }

        @Override // com.sun.xml.internal.ws.api.Component
        public <S> S getSPI(Class<S> cls) {
            return (S) this.component.getSPI(cls);
        }

        public int hashCode() {
            return this.component.hashCode();
        }

        public boolean equals(Object obj) {
            return this.component.equals(obj);
        }
    }

    @Override // com.sun.xml.internal.ws.api.server.WSEndpoint, com.sun.xml.internal.ws.api.ComponentRegistry
    @NotNull
    public Set<Component> getComponents() {
        return this.componentRegistry;
    }

    /* JADX WARN: Incorrect return type in method signature: <T:Ljavax/xml/ws/EndpointReference;>(Ljava/lang/Class<TT;>;Ljava/lang/String;Ljava/lang/String;[Lorg/w3c/dom/Element;)TT; */
    @Override // com.sun.xml.internal.ws.api.server.WSEndpoint
    public EndpointReference getEndpointReference(Class cls, String address, String wsdlAddress, Element... referenceParameters) {
        List<Element> refParams = null;
        if (referenceParameters != null) {
            refParams = Arrays.asList(referenceParameters);
        }
        return getEndpointReference(cls, address, wsdlAddress, null, refParams);
    }

    /* JADX WARN: Incorrect return type in method signature: <T:Ljavax/xml/ws/EndpointReference;>(Ljava/lang/Class<TT;>;Ljava/lang/String;Ljava/lang/String;Ljava/util/List<Lorg/w3c/dom/Element;>;Ljava/util/List<Lorg/w3c/dom/Element;>;)TT; */
    @Override // com.sun.xml.internal.ws.api.server.WSEndpoint
    public EndpointReference getEndpointReference(Class cls, String address, String wsdlAddress, List list, List list2) {
        QName portType = null;
        if (this.port != null) {
            portType = this.port.getBinding().getPortTypeName();
        }
        AddressingVersion av2 = AddressingVersion.fromSpecClass(cls);
        return new WSEndpointReference(av2, address, this.serviceName, this.portName, portType, list, wsdlAddress, list2, this.endpointReferenceExtensions.values(), null).toSpec(cls);
    }

    @Override // com.sun.xml.internal.ws.api.server.WSEndpoint
    @NotNull
    public QName getPortName() {
        return this.portName;
    }

    @Override // com.sun.xml.internal.ws.api.server.WSEndpoint
    @NotNull
    public Codec createCodec() {
        return this.masterCodec.copy();
    }

    @Override // com.sun.xml.internal.ws.api.server.WSEndpoint
    @NotNull
    public QName getServiceName() {
        return this.serviceName;
    }

    private void initManagedObjectManager() {
        synchronized (this.managedObjectManagerLock) {
            if (this.managedObjectManager == null) {
                switch (this.lazyMOMProviderScope) {
                    case GLASSFISH_NO_JMX:
                        this.managedObjectManager = new WSEndpointMOMProxy(this);
                        break;
                    default:
                        this.managedObjectManager = obtainManagedObjectManager();
                        break;
                }
            }
        }
    }

    @Override // com.sun.xml.internal.ws.api.server.WSEndpoint
    @NotNull
    public ManagedObjectManager getManagedObjectManager() {
        return this.managedObjectManager;
    }

    @NotNull
    ManagedObjectManager obtainManagedObjectManager() {
        MonitorRootService monitorRootService = new MonitorRootService(this);
        ManagedObjectManager mOM = monitorRootService.createManagedObjectManager(this);
        mOM.resumeJMXRegistration();
        return mOM;
    }

    @Override // com.sun.xml.internal.ws.api.server.LazyMOMProvider.ScopeChangeListener
    public void scopeChanged(LazyMOMProvider.Scope scope) {
        synchronized (this.managedObjectManagerLock) {
            if (this.managedObjectManagerClosed) {
                return;
            }
            this.lazyMOMProviderScope = scope;
            if (this.managedObjectManager == null) {
                if (scope != LazyMOMProvider.Scope.GLASSFISH_NO_JMX) {
                    this.managedObjectManager = obtainManagedObjectManager();
                } else {
                    this.managedObjectManager = new WSEndpointMOMProxy(this);
                }
            } else if ((this.managedObjectManager instanceof WSEndpointMOMProxy) && !((WSEndpointMOMProxy) this.managedObjectManager).isInitialized()) {
                ((WSEndpointMOMProxy) this.managedObjectManager).setManagedObjectManager(obtainManagedObjectManager());
            }
        }
    }

    @Override // com.sun.xml.internal.ws.api.server.WSEndpoint
    public void closeManagedObjectManager() {
        synchronized (this.managedObjectManagerLock) {
            if (this.managedObjectManagerClosed) {
                return;
            }
            if (this.managedObjectManager != null) {
                boolean close = true;
                if ((this.managedObjectManager instanceof WSEndpointMOMProxy) && !((WSEndpointMOMProxy) this.managedObjectManager).isInitialized()) {
                    close = false;
                }
                if (close) {
                    try {
                        ObjectName name = this.managedObjectManager.getObjectName(this.managedObjectManager.getRoot());
                        if (name != null) {
                            monitoringLogger.log(Level.INFO, "Closing Metro monitoring root: {0}", name);
                        }
                        this.managedObjectManager.close();
                    } catch (IOException e2) {
                        monitoringLogger.log(Level.WARNING, "Ignoring error when closing Managed Object Manager", (Throwable) e2);
                    }
                }
            }
            this.managedObjectManagerClosed = true;
        }
    }

    @Override // com.sun.xml.internal.ws.api.server.WSEndpoint
    @NotNull
    public ServerTubeAssemblerContext getAssemblerContext() {
        return this.context;
    }
}
