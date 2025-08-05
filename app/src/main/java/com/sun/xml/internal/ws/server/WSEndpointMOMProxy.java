package com.sun.xml.internal.ws.server;

import com.sun.istack.internal.NotNull;
import com.sun.org.glassfish.gmbal.AMXClient;
import com.sun.org.glassfish.gmbal.GmbalMBean;
import com.sun.org.glassfish.gmbal.ManagedObjectManager;
import com.sun.xml.internal.ws.api.SOAPVersion;
import com.sun.xml.internal.ws.api.WSBinding;
import com.sun.xml.internal.ws.api.message.Packet;
import com.sun.xml.internal.ws.api.model.SEIModel;
import com.sun.xml.internal.ws.api.model.wsdl.WSDLPort;
import com.sun.xml.internal.ws.api.pipe.Codec;
import com.sun.xml.internal.ws.api.pipe.FiberContextSwitchInterceptor;
import com.sun.xml.internal.ws.api.pipe.ServerTubeAssemblerContext;
import com.sun.xml.internal.ws.api.pipe.ThrowableContainerPropertySet;
import com.sun.xml.internal.ws.api.server.Container;
import com.sun.xml.internal.ws.api.server.ServiceDefinition;
import com.sun.xml.internal.ws.api.server.WSEndpoint;
import com.sun.xml.internal.ws.policy.PolicyMap;
import com.sun.xml.internal.ws.wsdl.OperationDispatcher;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.concurrent.Executor;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.xml.namespace.QName;
import javax.xml.ws.EndpointReference;
import org.w3c.dom.Element;

/* loaded from: rt.jar:com/sun/xml/internal/ws/server/WSEndpointMOMProxy.class */
public class WSEndpointMOMProxy extends WSEndpoint implements ManagedObjectManager {

    @NotNull
    private final WSEndpointImpl wsEndpoint;
    private ManagedObjectManager managedObjectManager;

    WSEndpointMOMProxy(@NotNull WSEndpointImpl wsEndpoint) {
        this.wsEndpoint = wsEndpoint;
    }

    @Override // com.sun.xml.internal.ws.api.server.WSEndpoint
    public ManagedObjectManager getManagedObjectManager() {
        if (this.managedObjectManager == null) {
            this.managedObjectManager = this.wsEndpoint.obtainManagedObjectManager();
        }
        return this.managedObjectManager;
    }

    void setManagedObjectManager(ManagedObjectManager managedObjectManager) {
        this.managedObjectManager = managedObjectManager;
    }

    public boolean isInitialized() {
        return this.managedObjectManager != null;
    }

    public WSEndpointImpl getWsEndpoint() {
        return this.wsEndpoint;
    }

    @Override // com.sun.org.glassfish.gmbal.ManagedObjectManager
    public void suspendJMXRegistration() {
        getManagedObjectManager().suspendJMXRegistration();
    }

    @Override // com.sun.org.glassfish.gmbal.ManagedObjectManager
    public void resumeJMXRegistration() {
        getManagedObjectManager().resumeJMXRegistration();
    }

    @Override // com.sun.org.glassfish.gmbal.ManagedObjectManager
    public boolean isManagedObject(Object obj) {
        return getManagedObjectManager().isManagedObject(obj);
    }

    @Override // com.sun.org.glassfish.gmbal.ManagedObjectManager
    public GmbalMBean createRoot() {
        return getManagedObjectManager().createRoot();
    }

    @Override // com.sun.org.glassfish.gmbal.ManagedObjectManager
    public GmbalMBean createRoot(Object root) {
        return getManagedObjectManager().createRoot(root);
    }

    @Override // com.sun.org.glassfish.gmbal.ManagedObjectManager
    public GmbalMBean createRoot(Object root, String name) {
        return getManagedObjectManager().createRoot(root, name);
    }

    @Override // com.sun.org.glassfish.gmbal.ManagedObjectManager
    public Object getRoot() {
        return getManagedObjectManager().getRoot();
    }

    @Override // com.sun.org.glassfish.gmbal.ManagedObjectManager
    public GmbalMBean register(Object parent, Object obj, String name) {
        return getManagedObjectManager().register(parent, obj, name);
    }

    @Override // com.sun.org.glassfish.gmbal.ManagedObjectManager
    public GmbalMBean register(Object parent, Object obj) {
        return getManagedObjectManager().register(parent, obj);
    }

    @Override // com.sun.org.glassfish.gmbal.ManagedObjectManager
    public GmbalMBean registerAtRoot(Object obj, String name) {
        return getManagedObjectManager().registerAtRoot(obj, name);
    }

    @Override // com.sun.org.glassfish.gmbal.ManagedObjectManager
    public GmbalMBean registerAtRoot(Object obj) {
        return getManagedObjectManager().registerAtRoot(obj);
    }

    @Override // com.sun.org.glassfish.gmbal.ManagedObjectManager
    public void unregister(Object obj) {
        getManagedObjectManager().unregister(obj);
    }

    @Override // com.sun.org.glassfish.gmbal.ManagedObjectManager
    public ObjectName getObjectName(Object obj) {
        return getManagedObjectManager().getObjectName(obj);
    }

    @Override // com.sun.org.glassfish.gmbal.ManagedObjectManager
    public AMXClient getAMXClient(Object obj) {
        return getManagedObjectManager().getAMXClient(obj);
    }

    @Override // com.sun.org.glassfish.gmbal.ManagedObjectManager
    public Object getObject(ObjectName oname) {
        return getManagedObjectManager().getObject(oname);
    }

    @Override // com.sun.org.glassfish.gmbal.ManagedObjectManager
    public void stripPrefix(String... str) {
        getManagedObjectManager().stripPrefix(str);
    }

    @Override // com.sun.org.glassfish.gmbal.ManagedObjectManager
    public void stripPackagePrefix() {
        getManagedObjectManager().stripPackagePrefix();
    }

    @Override // com.sun.org.glassfish.gmbal.ManagedObjectManager
    public String getDomain() {
        return getManagedObjectManager().getDomain();
    }

    @Override // com.sun.org.glassfish.gmbal.ManagedObjectManager
    public void setMBeanServer(MBeanServer server) {
        getManagedObjectManager().setMBeanServer(server);
    }

    @Override // com.sun.org.glassfish.gmbal.ManagedObjectManager
    public MBeanServer getMBeanServer() {
        return getManagedObjectManager().getMBeanServer();
    }

    @Override // com.sun.org.glassfish.gmbal.ManagedObjectManager
    public void setResourceBundle(ResourceBundle rb) {
        getManagedObjectManager().setResourceBundle(rb);
    }

    @Override // com.sun.org.glassfish.gmbal.ManagedObjectManager
    public ResourceBundle getResourceBundle() {
        return getManagedObjectManager().getResourceBundle();
    }

    @Override // com.sun.org.glassfish.gmbal.ManagedObjectManager
    public void addAnnotation(AnnotatedElement element, Annotation annotation) {
        getManagedObjectManager().addAnnotation(element, annotation);
    }

    @Override // com.sun.org.glassfish.gmbal.ManagedObjectManager
    public void setRegistrationDebug(ManagedObjectManager.RegistrationDebugLevel level) {
        getManagedObjectManager().setRegistrationDebug(level);
    }

    @Override // com.sun.org.glassfish.gmbal.ManagedObjectManager
    public void setRuntimeDebug(boolean flag) {
        getManagedObjectManager().setRuntimeDebug(flag);
    }

    @Override // com.sun.org.glassfish.gmbal.ManagedObjectManager
    public void setTypelibDebug(int level) {
        getManagedObjectManager().setTypelibDebug(level);
    }

    @Override // com.sun.org.glassfish.gmbal.ManagedObjectManager
    public void setJMXRegistrationDebug(boolean flag) {
        getManagedObjectManager().setJMXRegistrationDebug(flag);
    }

    @Override // com.sun.org.glassfish.gmbal.ManagedObjectManager
    public String dumpSkeleton(Object obj) {
        return getManagedObjectManager().dumpSkeleton(obj);
    }

    @Override // com.sun.org.glassfish.gmbal.ManagedObjectManager
    public void suppressDuplicateRootReport(boolean suppressReport) {
        getManagedObjectManager().suppressDuplicateRootReport(suppressReport);
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        getManagedObjectManager().close();
    }

    @Override // com.sun.xml.internal.ws.api.server.WSEndpoint
    public boolean equalsProxiedInstance(WSEndpoint endpoint) {
        if (this.wsEndpoint == null) {
            return endpoint == null;
        }
        return this.wsEndpoint.equals(endpoint);
    }

    @Override // com.sun.xml.internal.ws.api.server.WSEndpoint
    public Codec createCodec() {
        return this.wsEndpoint.createCodec();
    }

    @Override // com.sun.xml.internal.ws.api.server.WSEndpoint
    public QName getServiceName() {
        return this.wsEndpoint.getServiceName();
    }

    @Override // com.sun.xml.internal.ws.api.server.WSEndpoint
    public QName getPortName() {
        return this.wsEndpoint.getPortName();
    }

    @Override // com.sun.xml.internal.ws.api.server.WSEndpoint
    public Class getImplementationClass() {
        return this.wsEndpoint.getImplementationClass();
    }

    @Override // com.sun.xml.internal.ws.api.server.WSEndpoint
    public WSBinding getBinding() {
        return this.wsEndpoint.getBinding();
    }

    @Override // com.sun.xml.internal.ws.api.server.WSEndpoint
    public Container getContainer() {
        return this.wsEndpoint.getContainer();
    }

    @Override // com.sun.xml.internal.ws.api.server.WSEndpoint
    public WSDLPort getPort() {
        return this.wsEndpoint.getPort();
    }

    @Override // com.sun.xml.internal.ws.api.server.WSEndpoint
    public void setExecutor(Executor exec) {
        this.wsEndpoint.setExecutor(exec);
    }

    @Override // com.sun.xml.internal.ws.api.server.WSEndpoint
    public void schedule(Packet request, WSEndpoint.CompletionCallback callback, FiberContextSwitchInterceptor interceptor) {
        this.wsEndpoint.schedule(request, callback, interceptor);
    }

    @Override // com.sun.xml.internal.ws.api.server.WSEndpoint
    public WSEndpoint.PipeHead createPipeHead() {
        return this.wsEndpoint.createPipeHead();
    }

    @Override // com.sun.xml.internal.ws.api.server.WSEndpoint
    public void dispose() {
        if (this.wsEndpoint != null) {
            this.wsEndpoint.dispose();
        }
    }

    @Override // com.sun.xml.internal.ws.api.server.WSEndpoint
    public ServiceDefinition getServiceDefinition() {
        return this.wsEndpoint.getServiceDefinition();
    }

    @Override // com.sun.xml.internal.ws.api.server.WSEndpoint
    public Set getComponentRegistry() {
        return this.wsEndpoint.getComponentRegistry();
    }

    @Override // com.sun.xml.internal.ws.api.server.WSEndpoint
    public SEIModel getSEIModel() {
        return this.wsEndpoint.getSEIModel();
    }

    @Override // com.sun.xml.internal.ws.api.server.WSEndpoint
    public PolicyMap getPolicyMap() {
        return this.wsEndpoint.getPolicyMap();
    }

    @Override // com.sun.xml.internal.ws.api.server.WSEndpoint
    public void closeManagedObjectManager() {
        this.wsEndpoint.closeManagedObjectManager();
    }

    @Override // com.sun.xml.internal.ws.api.server.WSEndpoint
    public ServerTubeAssemblerContext getAssemblerContext() {
        return this.wsEndpoint.getAssemblerContext();
    }

    @Override // com.sun.xml.internal.ws.api.server.WSEndpoint
    public EndpointReference getEndpointReference(Class clazz, String address, String wsdlAddress, Element... referenceParameters) {
        return this.wsEndpoint.getEndpointReference(clazz, address, wsdlAddress, referenceParameters);
    }

    @Override // com.sun.xml.internal.ws.api.server.WSEndpoint
    public EndpointReference getEndpointReference(Class clazz, String address, String wsdlAddress, List metadata, List referenceParameters) {
        return this.wsEndpoint.getEndpointReference(clazz, address, wsdlAddress, metadata, referenceParameters);
    }

    @Override // com.sun.xml.internal.ws.api.server.WSEndpoint
    public OperationDispatcher getOperationDispatcher() {
        return this.wsEndpoint.getOperationDispatcher();
    }

    @Override // com.sun.xml.internal.ws.api.server.WSEndpoint
    public Packet createServiceResponseForException(ThrowableContainerPropertySet tc, Packet responsePacket, SOAPVersion soapVersion, WSDLPort wsdlPort, SEIModel seiModel, WSBinding binding) {
        return this.wsEndpoint.createServiceResponseForException(tc, responsePacket, soapVersion, wsdlPort, seiModel, binding);
    }
}
