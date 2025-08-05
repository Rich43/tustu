package com.sun.org.glassfish.gmbal;

import java.io.Closeable;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.ResourceBundle;
import javax.management.MBeanServer;
import javax.management.ObjectName;

/* loaded from: rt.jar:com/sun/org/glassfish/gmbal/ManagedObjectManager.class */
public interface ManagedObjectManager extends Closeable {

    /* loaded from: rt.jar:com/sun/org/glassfish/gmbal/ManagedObjectManager$RegistrationDebugLevel.class */
    public enum RegistrationDebugLevel {
        NONE,
        NORMAL,
        FINE
    }

    void suspendJMXRegistration();

    void resumeJMXRegistration();

    boolean isManagedObject(Object obj);

    GmbalMBean createRoot();

    GmbalMBean createRoot(Object obj);

    GmbalMBean createRoot(Object obj, String str);

    Object getRoot();

    GmbalMBean register(Object obj, Object obj2, String str);

    GmbalMBean register(Object obj, Object obj2);

    GmbalMBean registerAtRoot(Object obj, String str);

    GmbalMBean registerAtRoot(Object obj);

    void unregister(Object obj);

    ObjectName getObjectName(Object obj);

    AMXClient getAMXClient(Object obj);

    Object getObject(ObjectName objectName);

    void stripPrefix(String... strArr);

    void stripPackagePrefix();

    String getDomain();

    void setMBeanServer(MBeanServer mBeanServer);

    MBeanServer getMBeanServer();

    void setResourceBundle(ResourceBundle resourceBundle);

    ResourceBundle getResourceBundle();

    void addAnnotation(AnnotatedElement annotatedElement, Annotation annotation);

    void setRegistrationDebug(RegistrationDebugLevel registrationDebugLevel);

    void setRuntimeDebug(boolean z2);

    void setTypelibDebug(int i2);

    void setJMXRegistrationDebug(boolean z2);

    String dumpSkeleton(Object obj);

    void suppressDuplicateRootReport(boolean z2);
}
