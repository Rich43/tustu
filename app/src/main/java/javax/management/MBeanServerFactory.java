package javax.management;

import com.sun.jmx.defaults.JmxProperties;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import javax.management.loading.ClassLoaderRepository;
import sun.reflect.misc.ReflectUtil;

/* loaded from: rt.jar:javax/management/MBeanServerFactory.class */
public class MBeanServerFactory {
    private static MBeanServerBuilder builder = null;
    private static final ArrayList<MBeanServer> mBeanServerList = new ArrayList<>();

    private MBeanServerFactory() {
    }

    public static void releaseMBeanServer(MBeanServer mBeanServer) throws SecurityException {
        checkPermission("releaseMBeanServer");
        removeMBeanServer(mBeanServer);
    }

    public static MBeanServer createMBeanServer() {
        return createMBeanServer(null);
    }

    public static MBeanServer createMBeanServer(String str) throws SecurityException {
        checkPermission("createMBeanServer");
        MBeanServer mBeanServerNewMBeanServer = newMBeanServer(str);
        addMBeanServer(mBeanServerNewMBeanServer);
        return mBeanServerNewMBeanServer;
    }

    public static MBeanServer newMBeanServer() {
        return newMBeanServer(null);
    }

    public static MBeanServer newMBeanServer(String str) throws SecurityException {
        MBeanServer mBeanServerNewMBeanServer;
        checkPermission("newMBeanServer");
        MBeanServerBuilder newMBeanServerBuilder = getNewMBeanServerBuilder();
        synchronized (newMBeanServerBuilder) {
            MBeanServerDelegate mBeanServerDelegateNewMBeanServerDelegate = newMBeanServerBuilder.newMBeanServerDelegate();
            if (mBeanServerDelegateNewMBeanServerDelegate == null) {
                throw new JMRuntimeException("MBeanServerBuilder.newMBeanServerDelegate() returned null");
            }
            mBeanServerNewMBeanServer = newMBeanServerBuilder.newMBeanServer(str, null, mBeanServerDelegateNewMBeanServerDelegate);
            if (mBeanServerNewMBeanServer == null) {
                throw new JMRuntimeException("MBeanServerBuilder.newMBeanServer() returned null");
            }
        }
        return mBeanServerNewMBeanServer;
    }

    public static synchronized ArrayList<MBeanServer> findMBeanServer(String str) throws SecurityException {
        checkPermission("findMBeanServer");
        if (str == null) {
            return new ArrayList<>(mBeanServerList);
        }
        ArrayList<MBeanServer> arrayList = new ArrayList<>();
        Iterator<MBeanServer> it = mBeanServerList.iterator();
        while (it.hasNext()) {
            MBeanServer next = it.next();
            if (str.equals(mBeanServerId(next))) {
                arrayList.add(next);
            }
        }
        return arrayList;
    }

    public static ClassLoaderRepository getClassLoaderRepository(MBeanServer mBeanServer) {
        return mBeanServer.getClassLoaderRepository();
    }

    private static String mBeanServerId(MBeanServer mBeanServer) {
        try {
            return (String) mBeanServer.getAttribute(MBeanServerDelegate.DELEGATE_NAME, "MBeanServerId");
        } catch (JMException e2) {
            JmxProperties.MISC_LOGGER.finest("Ignoring exception while getting MBeanServerId: " + ((Object) e2));
            return null;
        }
    }

    private static void checkPermission(String str) throws SecurityException {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkPermission(new MBeanServerPermission(str));
        }
    }

    private static synchronized void addMBeanServer(MBeanServer mBeanServer) {
        mBeanServerList.add(mBeanServer);
    }

    private static synchronized void removeMBeanServer(MBeanServer mBeanServer) {
        if (!mBeanServerList.remove(mBeanServer)) {
            JmxProperties.MBEANSERVER_LOGGER.logp(Level.FINER, MBeanServerFactory.class.getName(), "removeMBeanServer(MBeanServer)", "MBeanServer was not in list!");
            throw new IllegalArgumentException("MBeanServer was not in list!");
        }
    }

    private static Class<?> loadBuilderClass(String str) throws ClassNotFoundException {
        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        if (contextClassLoader != null) {
            return contextClassLoader.loadClass(str);
        }
        return ReflectUtil.forName(str);
    }

    private static MBeanServerBuilder newBuilder(Class<?> cls) {
        try {
            return (MBeanServerBuilder) cls.newInstance();
        } catch (RuntimeException e2) {
            throw e2;
        } catch (Exception e3) {
            throw new JMRuntimeException("Failed to instantiate a MBeanServerBuilder from " + ((Object) cls) + ": " + ((Object) e3), e3);
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:7:0x001d  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private static synchronized void checkMBeanServerBuilder() {
        /*
            com.sun.jmx.mbeanserver.GetPropertyAction r0 = new com.sun.jmx.mbeanserver.GetPropertyAction     // Catch: java.lang.RuntimeException -> L70
            r1 = r0
            java.lang.String r2 = "javax.management.builder.initial"
            r1.<init>(r2)     // Catch: java.lang.RuntimeException -> L70
            r6 = r0
            r0 = r6
            java.lang.Object r0 = java.security.AccessController.doPrivileged(r0)     // Catch: java.lang.RuntimeException -> L70
            java.lang.String r0 = (java.lang.String) r0     // Catch: java.lang.RuntimeException -> L70
            r7 = r0
            r0 = r7
            if (r0 == 0) goto L1d
            r0 = r7
            int r0 = r0.length()     // Catch: java.lang.ClassNotFoundException -> L45 java.lang.RuntimeException -> L70
            if (r0 != 0) goto L23
        L1d:
            java.lang.Class<javax.management.MBeanServerBuilder> r0 = javax.management.MBeanServerBuilder.class
            r8 = r0
            goto L28
        L23:
            r0 = r7
            java.lang.Class r0 = loadBuilderClass(r0)     // Catch: java.lang.ClassNotFoundException -> L45 java.lang.RuntimeException -> L70
            r8 = r0
        L28:
            javax.management.MBeanServerBuilder r0 = javax.management.MBeanServerFactory.builder     // Catch: java.lang.ClassNotFoundException -> L45 java.lang.RuntimeException -> L70
            if (r0 == 0) goto L3b
            javax.management.MBeanServerBuilder r0 = javax.management.MBeanServerFactory.builder     // Catch: java.lang.ClassNotFoundException -> L45 java.lang.RuntimeException -> L70
            java.lang.Class r0 = r0.getClass()     // Catch: java.lang.ClassNotFoundException -> L45 java.lang.RuntimeException -> L70
            r9 = r0
            r0 = r8
            r1 = r9
            if (r0 != r1) goto L3b
            return
        L3b:
            r0 = r8
            javax.management.MBeanServerBuilder r0 = newBuilder(r0)     // Catch: java.lang.ClassNotFoundException -> L45 java.lang.RuntimeException -> L70
            javax.management.MBeanServerFactory.builder = r0     // Catch: java.lang.ClassNotFoundException -> L45 java.lang.RuntimeException -> L70
            goto L6d
        L45:
            r8 = move-exception
            java.lang.StringBuilder r0 = new java.lang.StringBuilder     // Catch: java.lang.RuntimeException -> L70
            r1 = r0
            r1.<init>()     // Catch: java.lang.RuntimeException -> L70
            java.lang.String r1 = "Failed to load MBeanServerBuilder class "
            java.lang.StringBuilder r0 = r0.append(r1)     // Catch: java.lang.RuntimeException -> L70
            r1 = r7
            java.lang.StringBuilder r0 = r0.append(r1)     // Catch: java.lang.RuntimeException -> L70
            java.lang.String r1 = ": "
            java.lang.StringBuilder r0 = r0.append(r1)     // Catch: java.lang.RuntimeException -> L70
            r1 = r8
            java.lang.StringBuilder r0 = r0.append(r1)     // Catch: java.lang.RuntimeException -> L70
            java.lang.String r0 = r0.toString()     // Catch: java.lang.RuntimeException -> L70
            r9 = r0
            javax.management.JMRuntimeException r0 = new javax.management.JMRuntimeException     // Catch: java.lang.RuntimeException -> L70
            r1 = r0
            r2 = r9
            r3 = r8
            r1.<init>(r2, r3)     // Catch: java.lang.RuntimeException -> L70
            throw r0     // Catch: java.lang.RuntimeException -> L70
        L6d:
            goto Lb3
        L70:
            r6 = move-exception
            java.util.logging.Logger r0 = com.sun.jmx.defaults.JmxProperties.MBEANSERVER_LOGGER
            java.util.logging.Level r1 = java.util.logging.Level.FINEST
            boolean r0 = r0.isLoggable(r1)
            if (r0 == 0) goto Lb1
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r1 = r0
            r1.<init>()
            java.lang.String r1 = "Failed to instantiate MBeanServerBuilder: "
            java.lang.StringBuilder r0 = r0.append(r1)
            r1 = r6
            java.lang.StringBuilder r0 = r0.append(r1)
            java.lang.String r1 = "\n\t\tCheck the value of the "
            java.lang.StringBuilder r0 = r0.append(r1)
            java.lang.String r1 = "javax.management.builder.initial"
            java.lang.StringBuilder r0 = r0.append(r1)
            java.lang.String r1 = " property."
            java.lang.StringBuilder r0 = r0.append(r1)
            r7 = r0
            java.util.logging.Logger r0 = com.sun.jmx.defaults.JmxProperties.MBEANSERVER_LOGGER
            java.util.logging.Level r1 = java.util.logging.Level.FINEST
            java.lang.Class<javax.management.MBeanServerFactory> r2 = javax.management.MBeanServerFactory.class
            java.lang.String r2 = r2.getName()
            java.lang.String r3 = "checkMBeanServerBuilder"
            r4 = r7
            java.lang.String r4 = r4.toString()
            r0.logp(r1, r2, r3, r4)
        Lb1:
            r0 = r6
            throw r0
        Lb3:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: javax.management.MBeanServerFactory.checkMBeanServerBuilder():void");
    }

    private static synchronized MBeanServerBuilder getNewMBeanServerBuilder() {
        checkMBeanServerBuilder();
        return builder;
    }
}
