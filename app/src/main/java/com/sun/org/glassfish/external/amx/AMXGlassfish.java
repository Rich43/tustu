package com.sun.org.glassfish.external.amx;

import com.sun.org.glassfish.external.amx.MBeanListener;
import java.io.IOException;
import javax.management.MBeanServer;
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import jdk.internal.dynalink.CallSiteDescriptor;

/* loaded from: rt.jar:com/sun/org/glassfish/external/amx/AMXGlassfish.class */
public final class AMXGlassfish {
    public static final String DEFAULT_JMX_DOMAIN = "amx";
    public static final AMXGlassfish DEFAULT = new AMXGlassfish(DEFAULT_JMX_DOMAIN);
    private final String mJMXDomain;
    private final ObjectName mDomainRoot = newObjectName("", "domain-root", null);

    public AMXGlassfish(String jmxDomain) {
        this.mJMXDomain = jmxDomain;
    }

    public static String getGlassfishVersion() {
        String version = System.getProperty("glassfish.version");
        return version;
    }

    public String amxJMXDomain() {
        return this.mJMXDomain;
    }

    public String amxSupportDomain() {
        return amxJMXDomain() + "-support";
    }

    public String dasName() {
        return "server";
    }

    public String dasConfig() {
        return dasName() + "-config";
    }

    public ObjectName domainRoot() {
        return this.mDomainRoot;
    }

    public ObjectName monitoringRoot() {
        return newObjectName("/", "mon", null);
    }

    public ObjectName serverMon(String serverName) {
        return newObjectName("/mon", "server-mon", serverName);
    }

    public ObjectName serverMonForDAS() {
        return serverMon("server");
    }

    public ObjectName newObjectName(String pp, String type, String name) {
        String props = prop(AMX.PARENT_PATH_KEY, pp) + "," + prop("type", type);
        if (name != null) {
            props = props + "," + prop("name", name);
        }
        return newObjectName(props);
    }

    public ObjectName newObjectName(String s2) {
        String name = s2;
        if (!name.startsWith(amxJMXDomain())) {
            name = amxJMXDomain() + CallSiteDescriptor.TOKEN_DELIMITER + name;
        }
        return AMXUtil.newObjectName(name);
    }

    private static String prop(String key, String value) {
        return key + "=" + value;
    }

    public ObjectName getBootAMXMBeanObjectName() {
        return AMXUtil.newObjectName(amxSupportDomain() + ":type=boot-amx");
    }

    public void invokeBootAMX(MBeanServerConnection conn) {
        try {
            conn.invoke(getBootAMXMBeanObjectName(), BootAMXMBean.BOOT_AMX_OPERATION_NAME, null, null);
        } catch (Exception e2) {
            e2.printStackTrace();
            throw new RuntimeException(e2);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void invokeWaitAMXReady(MBeanServerConnection conn, ObjectName objectName) {
        try {
            conn.invoke(objectName, "waitAMXReady", null, null);
        } catch (Exception e2) {
            throw new RuntimeException(e2);
        }
    }

    public <T extends MBeanListener.Callback> MBeanListener<T> listenForDomainRoot(MBeanServerConnection server, T callback) {
        MBeanListener<T> listener = new MBeanListener<>(server, domainRoot(), callback);
        listener.startListening();
        return listener;
    }

    /* loaded from: rt.jar:com/sun/org/glassfish/external/amx/AMXGlassfish$WaitForDomainRootListenerCallback.class */
    private static final class WaitForDomainRootListenerCallback extends MBeanListener.CallbackImpl {
        private final MBeanServerConnection mConn;

        public WaitForDomainRootListenerCallback(MBeanServerConnection conn) {
            this.mConn = conn;
        }

        @Override // com.sun.org.glassfish.external.amx.MBeanListener.CallbackImpl, com.sun.org.glassfish.external.amx.MBeanListener.Callback
        public void mbeanRegistered(ObjectName objectName, MBeanListener listener) {
            super.mbeanRegistered(objectName, listener);
            AMXGlassfish.invokeWaitAMXReady(this.mConn, objectName);
            this.mLatch.countDown();
        }
    }

    public ObjectName waitAMXReady(MBeanServerConnection server) {
        WaitForDomainRootListenerCallback callback = new WaitForDomainRootListenerCallback(server);
        listenForDomainRoot(server, callback);
        callback.await();
        return callback.getRegistered();
    }

    public <T extends MBeanListener.Callback> MBeanListener<T> listenForBootAMX(MBeanServerConnection server, T callback) {
        MBeanListener<T> listener = new MBeanListener<>(server, getBootAMXMBeanObjectName(), callback);
        listener.startListening();
        return listener;
    }

    /* loaded from: rt.jar:com/sun/org/glassfish/external/amx/AMXGlassfish$BootAMXCallback.class */
    public static class BootAMXCallback extends MBeanListener.CallbackImpl {
        private final MBeanServerConnection mConn;

        public BootAMXCallback(MBeanServerConnection conn) {
            this.mConn = conn;
        }

        @Override // com.sun.org.glassfish.external.amx.MBeanListener.CallbackImpl, com.sun.org.glassfish.external.amx.MBeanListener.Callback
        public void mbeanRegistered(ObjectName objectName, MBeanListener listener) {
            super.mbeanRegistered(objectName, listener);
            this.mLatch.countDown();
        }
    }

    public ObjectName bootAMX(MBeanServerConnection conn) throws IOException {
        ObjectName domainRoot = domainRoot();
        if (!conn.isRegistered(domainRoot)) {
            BootAMXCallback callback = new BootAMXCallback(conn);
            listenForBootAMX(conn, callback);
            callback.await();
            invokeBootAMX(conn);
            WaitForDomainRootListenerCallback drCallback = new WaitForDomainRootListenerCallback(conn);
            listenForDomainRoot(conn, drCallback);
            drCallback.await();
            invokeWaitAMXReady(conn, domainRoot);
        } else {
            invokeWaitAMXReady(conn, domainRoot);
        }
        return domainRoot;
    }

    public ObjectName bootAMX(MBeanServer server) {
        try {
            return bootAMX((MBeanServerConnection) server);
        } catch (IOException e2) {
            throw new RuntimeException(e2);
        }
    }
}
