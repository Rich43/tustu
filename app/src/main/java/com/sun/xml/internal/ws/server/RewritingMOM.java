package com.sun.xml.internal.ws.server;

import com.sun.org.glassfish.gmbal.AMXClient;
import com.sun.org.glassfish.gmbal.GmbalMBean;
import com.sun.org.glassfish.gmbal.ManagedObjectManager;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.ResourceBundle;
import javax.management.MBeanServer;
import javax.management.ObjectName;

/* compiled from: MonitorBase.java */
/* loaded from: rt.jar:com/sun/xml/internal/ws/server/RewritingMOM.class */
class RewritingMOM implements ManagedObjectManager {
    private final ManagedObjectManager mom;
    private static final String gmbalQuotingCharsRegex = "\n|\\|\"|\\*|\\?|:|=|,";
    private static final String replacementChar = "-";

    RewritingMOM(ManagedObjectManager mom) {
        this.mom = mom;
    }

    private String rewrite(String x2) {
        return x2.replaceAll(gmbalQuotingCharsRegex, "-");
    }

    @Override // com.sun.org.glassfish.gmbal.ManagedObjectManager
    public void suspendJMXRegistration() {
        this.mom.suspendJMXRegistration();
    }

    @Override // com.sun.org.glassfish.gmbal.ManagedObjectManager
    public void resumeJMXRegistration() {
        this.mom.resumeJMXRegistration();
    }

    @Override // com.sun.org.glassfish.gmbal.ManagedObjectManager
    public GmbalMBean createRoot() {
        return this.mom.createRoot();
    }

    @Override // com.sun.org.glassfish.gmbal.ManagedObjectManager
    public GmbalMBean createRoot(Object root) {
        return this.mom.createRoot(root);
    }

    @Override // com.sun.org.glassfish.gmbal.ManagedObjectManager
    public GmbalMBean createRoot(Object root, String name) {
        return this.mom.createRoot(root, rewrite(name));
    }

    @Override // com.sun.org.glassfish.gmbal.ManagedObjectManager
    public Object getRoot() {
        return this.mom.getRoot();
    }

    @Override // com.sun.org.glassfish.gmbal.ManagedObjectManager
    public GmbalMBean register(Object parent, Object obj, String name) {
        return this.mom.register(parent, obj, rewrite(name));
    }

    @Override // com.sun.org.glassfish.gmbal.ManagedObjectManager
    public GmbalMBean register(Object parent, Object obj) {
        return this.mom.register(parent, obj);
    }

    @Override // com.sun.org.glassfish.gmbal.ManagedObjectManager
    public GmbalMBean registerAtRoot(Object obj, String name) {
        return this.mom.registerAtRoot(obj, rewrite(name));
    }

    @Override // com.sun.org.glassfish.gmbal.ManagedObjectManager
    public GmbalMBean registerAtRoot(Object obj) {
        return this.mom.registerAtRoot(obj);
    }

    @Override // com.sun.org.glassfish.gmbal.ManagedObjectManager
    public void unregister(Object obj) {
        this.mom.unregister(obj);
    }

    @Override // com.sun.org.glassfish.gmbal.ManagedObjectManager
    public ObjectName getObjectName(Object obj) {
        return this.mom.getObjectName(obj);
    }

    @Override // com.sun.org.glassfish.gmbal.ManagedObjectManager
    public AMXClient getAMXClient(Object obj) {
        return this.mom.getAMXClient(obj);
    }

    @Override // com.sun.org.glassfish.gmbal.ManagedObjectManager
    public Object getObject(ObjectName oname) {
        return this.mom.getObject(oname);
    }

    @Override // com.sun.org.glassfish.gmbal.ManagedObjectManager
    public void stripPrefix(String... str) {
        this.mom.stripPrefix(str);
    }

    @Override // com.sun.org.glassfish.gmbal.ManagedObjectManager
    public void stripPackagePrefix() {
        this.mom.stripPackagePrefix();
    }

    @Override // com.sun.org.glassfish.gmbal.ManagedObjectManager
    public String getDomain() {
        return this.mom.getDomain();
    }

    @Override // com.sun.org.glassfish.gmbal.ManagedObjectManager
    public void setMBeanServer(MBeanServer server) {
        this.mom.setMBeanServer(server);
    }

    @Override // com.sun.org.glassfish.gmbal.ManagedObjectManager
    public MBeanServer getMBeanServer() {
        return this.mom.getMBeanServer();
    }

    @Override // com.sun.org.glassfish.gmbal.ManagedObjectManager
    public void setResourceBundle(ResourceBundle rb) {
        this.mom.setResourceBundle(rb);
    }

    @Override // com.sun.org.glassfish.gmbal.ManagedObjectManager
    public ResourceBundle getResourceBundle() {
        return this.mom.getResourceBundle();
    }

    @Override // com.sun.org.glassfish.gmbal.ManagedObjectManager
    public void addAnnotation(AnnotatedElement element, Annotation annotation) {
        this.mom.addAnnotation(element, annotation);
    }

    @Override // com.sun.org.glassfish.gmbal.ManagedObjectManager
    public void setRegistrationDebug(ManagedObjectManager.RegistrationDebugLevel level) {
        this.mom.setRegistrationDebug(level);
    }

    @Override // com.sun.org.glassfish.gmbal.ManagedObjectManager
    public void setRuntimeDebug(boolean flag) {
        this.mom.setRuntimeDebug(flag);
    }

    @Override // com.sun.org.glassfish.gmbal.ManagedObjectManager
    public void setTypelibDebug(int level) {
        this.mom.setTypelibDebug(level);
    }

    @Override // com.sun.org.glassfish.gmbal.ManagedObjectManager
    public String dumpSkeleton(Object obj) {
        return this.mom.dumpSkeleton(obj);
    }

    @Override // com.sun.org.glassfish.gmbal.ManagedObjectManager
    public void suppressDuplicateRootReport(boolean suppressReport) {
        this.mom.suppressDuplicateRootReport(suppressReport);
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        this.mom.close();
    }

    @Override // com.sun.org.glassfish.gmbal.ManagedObjectManager
    public void setJMXRegistrationDebug(boolean x2) {
        this.mom.setJMXRegistrationDebug(x2);
    }

    @Override // com.sun.org.glassfish.gmbal.ManagedObjectManager
    public boolean isManagedObject(Object x2) {
        return this.mom.isManagedObject(x2);
    }
}
