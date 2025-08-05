package com.sun.org.glassfish.gmbal;

import com.sun.org.glassfish.gmbal.ManagedObjectManager;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.ResourceBundle;
import javax.management.MBeanServer;
import javax.management.ObjectName;

/* loaded from: rt.jar:com/sun/org/glassfish/gmbal/ManagedObjectManagerNOPImpl.class */
class ManagedObjectManagerNOPImpl implements ManagedObjectManager {
    static final ManagedObjectManager self = new ManagedObjectManagerNOPImpl();
    private static final GmbalMBean gmb = new GmbalMBeanNOPImpl();

    private ManagedObjectManagerNOPImpl() {
    }

    @Override // com.sun.org.glassfish.gmbal.ManagedObjectManager
    public void suspendJMXRegistration() {
    }

    @Override // com.sun.org.glassfish.gmbal.ManagedObjectManager
    public void resumeJMXRegistration() {
    }

    @Override // com.sun.org.glassfish.gmbal.ManagedObjectManager
    public boolean isManagedObject(Object obj) {
        return false;
    }

    @Override // com.sun.org.glassfish.gmbal.ManagedObjectManager
    public GmbalMBean createRoot() {
        return gmb;
    }

    @Override // com.sun.org.glassfish.gmbal.ManagedObjectManager
    public GmbalMBean createRoot(Object root) {
        return gmb;
    }

    @Override // com.sun.org.glassfish.gmbal.ManagedObjectManager
    public GmbalMBean createRoot(Object root, String name) {
        return gmb;
    }

    @Override // com.sun.org.glassfish.gmbal.ManagedObjectManager
    public Object getRoot() {
        return null;
    }

    @Override // com.sun.org.glassfish.gmbal.ManagedObjectManager
    public GmbalMBean register(Object parent, Object obj, String name) {
        return gmb;
    }

    @Override // com.sun.org.glassfish.gmbal.ManagedObjectManager
    public GmbalMBean register(Object parent, Object obj) {
        return gmb;
    }

    @Override // com.sun.org.glassfish.gmbal.ManagedObjectManager
    public GmbalMBean registerAtRoot(Object obj, String name) {
        return gmb;
    }

    @Override // com.sun.org.glassfish.gmbal.ManagedObjectManager
    public GmbalMBean registerAtRoot(Object obj) {
        return gmb;
    }

    @Override // com.sun.org.glassfish.gmbal.ManagedObjectManager
    public void unregister(Object obj) {
    }

    @Override // com.sun.org.glassfish.gmbal.ManagedObjectManager
    public ObjectName getObjectName(Object obj) {
        return null;
    }

    @Override // com.sun.org.glassfish.gmbal.ManagedObjectManager
    public Object getObject(ObjectName oname) {
        return null;
    }

    @Override // com.sun.org.glassfish.gmbal.ManagedObjectManager
    public void stripPrefix(String... str) {
    }

    @Override // com.sun.org.glassfish.gmbal.ManagedObjectManager
    public String getDomain() {
        return null;
    }

    @Override // com.sun.org.glassfish.gmbal.ManagedObjectManager
    public void setMBeanServer(MBeanServer server) {
    }

    @Override // com.sun.org.glassfish.gmbal.ManagedObjectManager
    public MBeanServer getMBeanServer() {
        return null;
    }

    @Override // com.sun.org.glassfish.gmbal.ManagedObjectManager
    public void setResourceBundle(ResourceBundle rb) {
    }

    @Override // com.sun.org.glassfish.gmbal.ManagedObjectManager
    public ResourceBundle getResourceBundle() {
        return null;
    }

    @Override // com.sun.org.glassfish.gmbal.ManagedObjectManager
    public void addAnnotation(AnnotatedElement element, Annotation annotation) {
    }

    @Override // com.sun.org.glassfish.gmbal.ManagedObjectManager
    public void setRegistrationDebug(ManagedObjectManager.RegistrationDebugLevel level) {
    }

    @Override // com.sun.org.glassfish.gmbal.ManagedObjectManager
    public void setRuntimeDebug(boolean flag) {
    }

    @Override // com.sun.org.glassfish.gmbal.ManagedObjectManager
    public String dumpSkeleton(Object obj) {
        return "";
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
    }

    @Override // com.sun.org.glassfish.gmbal.ManagedObjectManager
    public void setTypelibDebug(int level) {
    }

    @Override // com.sun.org.glassfish.gmbal.ManagedObjectManager
    public void stripPackagePrefix() {
    }

    @Override // com.sun.org.glassfish.gmbal.ManagedObjectManager
    public void suppressDuplicateRootReport(boolean suppressReport) {
    }

    @Override // com.sun.org.glassfish.gmbal.ManagedObjectManager
    public AMXClient getAMXClient(Object obj) {
        return null;
    }

    @Override // com.sun.org.glassfish.gmbal.ManagedObjectManager
    public void setJMXRegistrationDebug(boolean flag) {
    }
}
