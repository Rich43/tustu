package com.sun.xml.internal.ws.api.server;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import com.sun.org.glassfish.gmbal.ManagedObjectManager;
import com.sun.xml.internal.ws.api.Component;
import com.sun.xml.internal.ws.api.ComponentRegistry;
import com.sun.xml.internal.ws.api.SOAPVersion;
import com.sun.xml.internal.ws.api.WSBinding;
import com.sun.xml.internal.ws.api.config.management.EndpointCreationAttributes;
import com.sun.xml.internal.ws.api.config.management.ManagedEndpointFactory;
import com.sun.xml.internal.ws.api.databinding.MetadataReader;
import com.sun.xml.internal.ws.api.message.Packet;
import com.sun.xml.internal.ws.api.model.SEIModel;
import com.sun.xml.internal.ws.api.model.wsdl.WSDLPort;
import com.sun.xml.internal.ws.api.pipe.Codec;
import com.sun.xml.internal.ws.api.pipe.Engine;
import com.sun.xml.internal.ws.api.pipe.FiberContextSwitchInterceptor;
import com.sun.xml.internal.ws.api.pipe.ServerTubeAssemblerContext;
import com.sun.xml.internal.ws.api.pipe.ThrowableContainerPropertySet;
import com.sun.xml.internal.ws.policy.PolicyMap;
import com.sun.xml.internal.ws.server.EndpointAwareTube;
import com.sun.xml.internal.ws.server.EndpointFactory;
import com.sun.xml.internal.ws.util.ServiceFinder;
import com.sun.xml.internal.ws.util.xml.XmlUtil;
import com.sun.xml.internal.ws.wsdl.OperationDispatcher;
import java.net.URL;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executor;
import javax.xml.namespace.QName;
import javax.xml.ws.EndpointReference;
import org.w3c.dom.Element;
import org.xml.sax.EntityResolver;

/* loaded from: rt.jar:com/sun/xml/internal/ws/api/server/WSEndpoint.class */
public abstract class WSEndpoint<T> implements ComponentRegistry {

    /* loaded from: rt.jar:com/sun/xml/internal/ws/api/server/WSEndpoint$CompletionCallback.class */
    public interface CompletionCallback {
        void onCompletion(@NotNull Packet packet);
    }

    /* loaded from: rt.jar:com/sun/xml/internal/ws/api/server/WSEndpoint$PipeHead.class */
    public interface PipeHead {
        @NotNull
        Packet process(@NotNull Packet packet, @Nullable WebServiceContextDelegate webServiceContextDelegate, @Nullable TransportBackChannel transportBackChannel);
    }

    @NotNull
    public abstract Codec createCodec();

    @NotNull
    public abstract QName getServiceName();

    @NotNull
    public abstract QName getPortName();

    @NotNull
    public abstract Class<T> getImplementationClass();

    @NotNull
    public abstract WSBinding getBinding();

    @NotNull
    public abstract Container getContainer();

    @Nullable
    public abstract WSDLPort getPort();

    public abstract void setExecutor(@NotNull Executor executor);

    public abstract void schedule(@NotNull Packet packet, @NotNull CompletionCallback completionCallback, @Nullable FiberContextSwitchInterceptor fiberContextSwitchInterceptor);

    @NotNull
    public abstract PipeHead createPipeHead();

    public abstract void dispose();

    @Nullable
    public abstract ServiceDefinition getServiceDefinition();

    @NotNull
    public abstract Set<EndpointComponent> getComponentRegistry();

    @Nullable
    public abstract SEIModel getSEIModel();

    public abstract PolicyMap getPolicyMap();

    @NotNull
    public abstract ManagedObjectManager getManagedObjectManager();

    public abstract void closeManagedObjectManager();

    @NotNull
    public abstract ServerTubeAssemblerContext getAssemblerContext();

    /* JADX WARN: Incorrect return type in method signature: <T:Ljavax/xml/ws/EndpointReference;>(Ljava/lang/Class<TT;>;Ljava/lang/String;Ljava/lang/String;[Lorg/w3c/dom/Element;)TT; */
    public abstract EndpointReference getEndpointReference(Class cls, String str, String str2, Element... elementArr);

    /* JADX WARN: Incorrect return type in method signature: <T:Ljavax/xml/ws/EndpointReference;>(Ljava/lang/Class<TT;>;Ljava/lang/String;Ljava/lang/String;Ljava/util/List<Lorg/w3c/dom/Element;>;Ljava/util/List<Lorg/w3c/dom/Element;>;)TT; */
    public abstract EndpointReference getEndpointReference(Class cls, String str, String str2, List list, List list2);

    @Nullable
    public abstract OperationDispatcher getOperationDispatcher();

    public abstract Packet createServiceResponseForException(ThrowableContainerPropertySet throwableContainerPropertySet, Packet packet, SOAPVersion sOAPVersion, WSDLPort wSDLPort, SEIModel sEIModel, WSBinding wSBinding);

    public final void schedule(@NotNull Packet request, @NotNull CompletionCallback callback) {
        schedule(request, callback, null);
    }

    public void process(@NotNull Packet request, @NotNull CompletionCallback callback, @Nullable FiberContextSwitchInterceptor interceptor) {
        schedule(request, callback, interceptor);
    }

    public Engine getEngine() {
        throw new UnsupportedOperationException();
    }

    public List<BoundEndpoint> getBoundEndpoints() {
        Module m2 = (Module) getContainer().getSPI(Module.class);
        if (m2 != null) {
            return m2.getBoundEndpoints();
        }
        return null;
    }

    @Override // com.sun.xml.internal.ws.api.ComponentRegistry
    @NotNull
    public Set<Component> getComponents() {
        return Collections.emptySet();
    }

    @Override // com.sun.xml.internal.ws.api.Component
    @Nullable
    public <S> S getSPI(@NotNull Class<S> cls) {
        Set<Component> components = getComponents();
        if (components != null) {
            Iterator<Component> it = components.iterator();
            while (it.hasNext()) {
                S s2 = (S) it.next().getSPI(cls);
                if (s2 != null) {
                    return s2;
                }
            }
        }
        return (S) getContainer().getSPI(cls);
    }

    public static <T> WSEndpoint<T> create(@NotNull Class<T> implType, boolean processHandlerAnnotation, @Nullable Invoker invoker, @Nullable QName serviceName, @Nullable QName portName, @Nullable Container container, @Nullable WSBinding binding, @Nullable SDDocumentSource primaryWsdl, @Nullable Collection<? extends SDDocumentSource> metadata, @Nullable EntityResolver resolver, boolean isTransportSynchronous) {
        return create(implType, processHandlerAnnotation, invoker, serviceName, portName, container, binding, primaryWsdl, metadata, resolver, isTransportSynchronous, true);
    }

    public static <T> WSEndpoint<T> create(@NotNull Class<T> implType, boolean processHandlerAnnotation, @Nullable Invoker invoker, @Nullable QName serviceName, @Nullable QName portName, @Nullable Container container, @Nullable WSBinding binding, @Nullable SDDocumentSource primaryWsdl, @Nullable Collection<? extends SDDocumentSource> metadata, @Nullable EntityResolver resolver, boolean isTransportSynchronous, boolean isStandard) {
        WSEndpoint<T> endpoint = EndpointFactory.createEndpoint(implType, processHandlerAnnotation, invoker, serviceName, portName, container, binding, primaryWsdl, metadata, resolver, isTransportSynchronous, isStandard);
        Iterator<T> it = ServiceFinder.find(ManagedEndpointFactory.class).iterator();
        if (it.hasNext()) {
            ManagedEndpointFactory managementFactory = (ManagedEndpointFactory) it.next();
            EndpointCreationAttributes attributes = new EndpointCreationAttributes(processHandlerAnnotation, invoker, resolver, isTransportSynchronous);
            WSEndpoint<T> managedEndpoint = managementFactory.createEndpoint(endpoint, attributes);
            if (endpoint.getAssemblerContext().getTerminalTube() instanceof EndpointAwareTube) {
                ((EndpointAwareTube) endpoint.getAssemblerContext().getTerminalTube()).setEndpoint(managedEndpoint);
            }
            return managedEndpoint;
        }
        return endpoint;
    }

    @Deprecated
    public static <T> WSEndpoint<T> create(@NotNull Class<T> implType, boolean processHandlerAnnotation, @Nullable Invoker invoker, @Nullable QName serviceName, @Nullable QName portName, @Nullable Container container, @Nullable WSBinding binding, @Nullable SDDocumentSource primaryWsdl, @Nullable Collection<? extends SDDocumentSource> metadata, @Nullable EntityResolver resolver) {
        return create(implType, processHandlerAnnotation, invoker, serviceName, portName, container, binding, primaryWsdl, metadata, resolver, false);
    }

    public static <T> WSEndpoint<T> create(@NotNull Class<T> implType, boolean processHandlerAnnotation, @Nullable Invoker invoker, @Nullable QName serviceName, @Nullable QName portName, @Nullable Container container, @Nullable WSBinding binding, @Nullable SDDocumentSource primaryWsdl, @Nullable Collection<? extends SDDocumentSource> metadata, @Nullable URL catalogUrl) {
        return create(implType, processHandlerAnnotation, invoker, serviceName, portName, container, binding, primaryWsdl, metadata, XmlUtil.createEntityResolver(catalogUrl), false);
    }

    @NotNull
    public static QName getDefaultServiceName(Class endpointClass) {
        return getDefaultServiceName(endpointClass, true, null);
    }

    @NotNull
    public static QName getDefaultServiceName(Class endpointClass, MetadataReader metadataReader) {
        return getDefaultServiceName(endpointClass, true, metadataReader);
    }

    @NotNull
    public static QName getDefaultServiceName(Class endpointClass, boolean isStandard) {
        return getDefaultServiceName(endpointClass, isStandard, null);
    }

    @NotNull
    public static QName getDefaultServiceName(Class endpointClass, boolean isStandard, MetadataReader metadataReader) {
        return EndpointFactory.getDefaultServiceName(endpointClass, isStandard, metadataReader);
    }

    @NotNull
    public static QName getDefaultPortName(@NotNull QName serviceName, Class endpointClass) {
        return getDefaultPortName(serviceName, endpointClass, (MetadataReader) null);
    }

    @NotNull
    public static QName getDefaultPortName(@NotNull QName serviceName, Class endpointClass, MetadataReader metadataReader) {
        return getDefaultPortName(serviceName, endpointClass, true, metadataReader);
    }

    @NotNull
    public static QName getDefaultPortName(@NotNull QName serviceName, Class endpointClass, boolean isStandard) {
        return getDefaultPortName(serviceName, endpointClass, isStandard, null);
    }

    @NotNull
    public static QName getDefaultPortName(@NotNull QName serviceName, Class endpointClass, boolean isStandard, MetadataReader metadataReader) {
        return EndpointFactory.getDefaultPortName(serviceName, endpointClass, isStandard, metadataReader);
    }

    public boolean equalsProxiedInstance(WSEndpoint endpoint) {
        if (endpoint == null) {
            return false;
        }
        return equals(endpoint);
    }
}
