package jdk.net;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.DatagramSocket;
import java.net.MulticastSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketOption;
import java.net.StandardSocketOptions;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import jdk.Exported;
import sun.net.ExtendedOptionsHelper;
import sun.net.ExtendedOptionsImpl;

@Exported
/* loaded from: rt.jar:jdk/net/Sockets.class */
public class Sockets {
    private static final HashMap<Class<?>, Set<SocketOption<?>>> options = new HashMap<>();
    private static Method siSetOption;
    private static Method siGetOption;
    private static Method dsiSetOption;
    private static Method dsiGetOption;

    static {
        initOptionSets();
        AccessController.doPrivileged(new PrivilegedAction<Void>() { // from class: jdk.net.Sockets.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public Void run2() {
                Sockets.initMethods();
                return null;
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void initMethods() {
        try {
            Class<?> cls = Class.forName("java.net.SocketSecrets");
            siSetOption = cls.getDeclaredMethod("setOption", Object.class, SocketOption.class, Object.class);
            siSetOption.setAccessible(true);
            siGetOption = cls.getDeclaredMethod("getOption", Object.class, SocketOption.class);
            siGetOption.setAccessible(true);
            dsiSetOption = cls.getDeclaredMethod("setOption", DatagramSocket.class, SocketOption.class, Object.class);
            dsiSetOption.setAccessible(true);
            dsiGetOption = cls.getDeclaredMethod("getOption", DatagramSocket.class, SocketOption.class);
            dsiGetOption.setAccessible(true);
        } catch (ReflectiveOperationException e2) {
            throw new InternalError(e2);
        }
    }

    private static <T> void invokeSet(Method method, Object obj, SocketOption<T> socketOption, T t2) throws IOException {
        try {
            method.invoke(null, obj, socketOption, t2);
        } catch (Exception e2) {
            if (e2 instanceof InvocationTargetException) {
                Throwable targetException = ((InvocationTargetException) e2).getTargetException();
                if (targetException instanceof IOException) {
                    throw ((IOException) targetException);
                }
                if (targetException instanceof RuntimeException) {
                    throw ((RuntimeException) targetException);
                }
            }
            throw new RuntimeException(e2);
        }
    }

    private static <T> T invokeGet(Method method, Object obj, SocketOption<T> socketOption) throws IOException {
        try {
            return (T) method.invoke(null, obj, socketOption);
        } catch (Exception e2) {
            if (e2 instanceof InvocationTargetException) {
                Throwable targetException = ((InvocationTargetException) e2).getTargetException();
                if (targetException instanceof IOException) {
                    throw ((IOException) targetException);
                }
                if (targetException instanceof RuntimeException) {
                    throw ((RuntimeException) targetException);
                }
            }
            throw new RuntimeException(e2);
        }
    }

    private Sockets() {
    }

    public static <T> void setOption(Socket socket, SocketOption<T> socketOption, T t2) throws IOException {
        if (!isSupported(Socket.class, socketOption)) {
            throw new UnsupportedOperationException(socketOption.name());
        }
        invokeSet(siSetOption, socket, socketOption, t2);
    }

    public static <T> T getOption(Socket socket, SocketOption<T> socketOption) throws IOException {
        if (!isSupported(Socket.class, socketOption)) {
            throw new UnsupportedOperationException(socketOption.name());
        }
        return (T) invokeGet(siGetOption, socket, socketOption);
    }

    public static <T> void setOption(ServerSocket serverSocket, SocketOption<T> socketOption, T t2) throws IOException {
        if (!isSupported(ServerSocket.class, socketOption)) {
            throw new UnsupportedOperationException(socketOption.name());
        }
        invokeSet(siSetOption, serverSocket, socketOption, t2);
    }

    public static <T> T getOption(ServerSocket serverSocket, SocketOption<T> socketOption) throws IOException {
        if (!isSupported(ServerSocket.class, socketOption)) {
            throw new UnsupportedOperationException(socketOption.name());
        }
        return (T) invokeGet(siGetOption, serverSocket, socketOption);
    }

    public static <T> void setOption(DatagramSocket datagramSocket, SocketOption<T> socketOption, T t2) throws IOException {
        if (!isSupported(datagramSocket.getClass(), socketOption)) {
            throw new UnsupportedOperationException(socketOption.name());
        }
        invokeSet(dsiSetOption, datagramSocket, socketOption, t2);
    }

    public static <T> T getOption(DatagramSocket datagramSocket, SocketOption<T> socketOption) throws IOException {
        if (!isSupported(datagramSocket.getClass(), socketOption)) {
            throw new UnsupportedOperationException(socketOption.name());
        }
        return (T) invokeGet(dsiGetOption, datagramSocket, socketOption);
    }

    public static Set<SocketOption<?>> supportedOptions(Class<?> cls) {
        Set<SocketOption<?>> set = options.get(cls);
        if (set == null) {
            throw new IllegalArgumentException("unknown socket type");
        }
        return set;
    }

    private static boolean isSupported(Class<?> cls, SocketOption<?> socketOption) {
        return supportedOptions(cls).contains(socketOption);
    }

    private static void initOptionSets() {
        boolean zFlowSupported = ExtendedOptionsImpl.flowSupported();
        HashSet hashSet = new HashSet();
        hashSet.add(StandardSocketOptions.SO_KEEPALIVE);
        hashSet.add(StandardSocketOptions.SO_SNDBUF);
        hashSet.add(StandardSocketOptions.SO_RCVBUF);
        hashSet.add(StandardSocketOptions.SO_REUSEADDR);
        hashSet.add(StandardSocketOptions.SO_LINGER);
        hashSet.add(StandardSocketOptions.IP_TOS);
        hashSet.add(StandardSocketOptions.TCP_NODELAY);
        if (zFlowSupported) {
            hashSet.add(ExtendedSocketOptions.SO_FLOW_SLA);
        }
        hashSet.addAll(ExtendedOptionsHelper.keepAliveOptions());
        options.put(Socket.class, Collections.unmodifiableSet(hashSet));
        HashSet hashSet2 = new HashSet();
        hashSet2.add(StandardSocketOptions.SO_RCVBUF);
        hashSet2.add(StandardSocketOptions.SO_REUSEADDR);
        hashSet2.add(StandardSocketOptions.IP_TOS);
        hashSet2.addAll(ExtendedOptionsHelper.keepAliveOptions());
        options.put(ServerSocket.class, Collections.unmodifiableSet(hashSet2));
        HashSet hashSet3 = new HashSet();
        hashSet3.add(StandardSocketOptions.SO_SNDBUF);
        hashSet3.add(StandardSocketOptions.SO_RCVBUF);
        hashSet3.add(StandardSocketOptions.SO_REUSEADDR);
        hashSet3.add(StandardSocketOptions.IP_TOS);
        if (zFlowSupported) {
            hashSet3.add(ExtendedSocketOptions.SO_FLOW_SLA);
        }
        options.put(DatagramSocket.class, Collections.unmodifiableSet(hashSet3));
        HashSet hashSet4 = new HashSet();
        hashSet4.add(StandardSocketOptions.SO_SNDBUF);
        hashSet4.add(StandardSocketOptions.SO_RCVBUF);
        hashSet4.add(StandardSocketOptions.SO_REUSEADDR);
        hashSet4.add(StandardSocketOptions.IP_TOS);
        hashSet4.add(StandardSocketOptions.IP_MULTICAST_IF);
        hashSet4.add(StandardSocketOptions.IP_MULTICAST_TTL);
        hashSet4.add(StandardSocketOptions.IP_MULTICAST_LOOP);
        if (zFlowSupported) {
            hashSet4.add(ExtendedSocketOptions.SO_FLOW_SLA);
        }
        options.put(MulticastSocket.class, Collections.unmodifiableSet(hashSet4));
    }
}
