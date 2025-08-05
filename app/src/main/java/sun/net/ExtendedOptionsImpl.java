package sun.net;

import java.io.FileDescriptor;
import java.net.SocketException;
import java.net.SocketOption;
import java.security.AccessController;
import jdk.net.NetworkPermission;
import jdk.net.SocketFlow;

/* loaded from: rt.jar:sun/net/ExtendedOptionsImpl.class */
public class ExtendedOptionsImpl {
    private static native void init();

    public static native void setFlowOption(FileDescriptor fileDescriptor, SocketFlow socketFlow);

    public static native void getFlowOption(FileDescriptor fileDescriptor, SocketFlow socketFlow);

    public static native boolean flowSupported();

    public static native void setTcpKeepAliveProbes(FileDescriptor fileDescriptor, int i2) throws SocketException;

    public static native void setTcpKeepAliveTime(FileDescriptor fileDescriptor, int i2) throws SocketException;

    public static native void setTcpKeepAliveIntvl(FileDescriptor fileDescriptor, int i2) throws SocketException;

    public static native int getTcpKeepAliveProbes(FileDescriptor fileDescriptor) throws SocketException;

    public static native int getTcpKeepAliveTime(FileDescriptor fileDescriptor) throws SocketException;

    public static native int getTcpKeepAliveIntvl(FileDescriptor fileDescriptor) throws SocketException;

    public static native boolean keepAliveOptionsSupported();

    static {
        AccessController.doPrivileged(() -> {
            System.loadLibrary("net");
            return null;
        });
        init();
    }

    private ExtendedOptionsImpl() {
    }

    public static void checkSetOptionPermission(SocketOption<?> socketOption) {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager == null) {
            return;
        }
        securityManager.checkPermission(new NetworkPermission("setOption." + socketOption.name()));
    }

    public static void checkGetOptionPermission(SocketOption<?> socketOption) {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager == null) {
            return;
        }
        securityManager.checkPermission(new NetworkPermission("getOption." + socketOption.name()));
    }

    public static void checkValueType(Object obj, Class<?> cls) {
        if (!cls.isAssignableFrom(obj.getClass())) {
            throw new IllegalArgumentException("Found: " + obj.getClass().toString() + " Expected: " + cls.toString());
        }
    }
}
