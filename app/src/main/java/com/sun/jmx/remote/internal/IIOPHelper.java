package com.sun.jmx.remote.internal;

import java.io.IOException;
import java.rmi.NoSuchObjectException;
import java.rmi.Remote;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Properties;

/* loaded from: rt.jar:com/sun/jmx/remote/internal/IIOPHelper.class */
public final class IIOPHelper {
    private static final String IMPL_CLASS = "com.sun.jmx.remote.protocol.iiop.IIOPProxyImpl";
    private static final IIOPProxy proxy = (IIOPProxy) AccessController.doPrivileged(new PrivilegedAction<IIOPProxy>() { // from class: com.sun.jmx.remote.internal.IIOPHelper.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.security.PrivilegedAction
        /* renamed from: run */
        public IIOPProxy run2() {
            try {
                return (IIOPProxy) Class.forName(IIOPHelper.IMPL_CLASS, true, IIOPHelper.class.getClassLoader()).newInstance();
            } catch (ClassNotFoundException e2) {
                return null;
            } catch (IllegalAccessException e3) {
                throw new AssertionError(e3);
            } catch (InstantiationException e4) {
                throw new AssertionError(e4);
            }
        }
    });

    private IIOPHelper() {
    }

    public static boolean isAvailable() {
        return proxy != null;
    }

    private static void ensureAvailable() {
        if (proxy == null) {
            throw new AssertionError((Object) "Should not here");
        }
    }

    public static boolean isStub(Object obj) {
        if (proxy == null) {
            return false;
        }
        return proxy.isStub(obj);
    }

    public static Object getDelegate(Object obj) {
        ensureAvailable();
        return proxy.getDelegate(obj);
    }

    public static void setDelegate(Object obj, Object obj2) {
        ensureAvailable();
        proxy.setDelegate(obj, obj2);
    }

    public static Object getOrb(Object obj) {
        ensureAvailable();
        return proxy.getOrb(obj);
    }

    public static void connect(Object obj, Object obj2) throws IOException {
        if (proxy == null) {
            throw new IOException("Connection to ORB failed, RMI/IIOP not available");
        }
        proxy.connect(obj, obj2);
    }

    public static boolean isOrb(Object obj) {
        if (proxy == null) {
            return false;
        }
        return proxy.isOrb(obj);
    }

    public static Object createOrb(String[] strArr, Properties properties) throws IOException {
        if (proxy == null) {
            throw new IOException("ORB initialization failed, RMI/IIOP not available");
        }
        return proxy.createOrb(strArr, properties);
    }

    public static Object stringToObject(Object obj, String str) {
        ensureAvailable();
        return proxy.stringToObject(obj, str);
    }

    public static String objectToString(Object obj, Object obj2) {
        ensureAvailable();
        return proxy.objectToString(obj, obj2);
    }

    public static <T> T narrow(Object obj, Class<T> cls) {
        ensureAvailable();
        return (T) proxy.narrow(obj, cls);
    }

    public static void exportObject(Remote remote) throws IOException {
        if (proxy == null) {
            throw new IOException("RMI object cannot be exported, RMI/IIOP not available");
        }
        proxy.exportObject(remote);
    }

    public static void unexportObject(Remote remote) throws IOException {
        if (proxy == null) {
            throw new NoSuchObjectException("Object not exported");
        }
        proxy.unexportObject(remote);
    }

    public static Remote toStub(Remote remote) throws IOException {
        if (proxy == null) {
            throw new NoSuchObjectException("Object not exported");
        }
        return proxy.toStub(remote);
    }
}
