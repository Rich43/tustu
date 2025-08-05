package com.oracle.net;

import java.io.IOException;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketImpl;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.security.AccessController;
import java.security.PrivilegedAction;
import sun.net.sdp.SdpSupport;
import sun.nio.ch.Secrets;

/* loaded from: rt.jar:com/oracle/net/Sdp.class */
public final class Sdp {
    private static final Constructor<ServerSocket> serverSocketCtor;
    private static final Constructor<SocketImpl> socketImplCtor;

    private Sdp() {
    }

    static {
        try {
            serverSocketCtor = ServerSocket.class.getDeclaredConstructor(SocketImpl.class);
            setAccessible(serverSocketCtor);
            try {
                socketImplCtor = Class.forName("java.net.SdpSocketImpl", true, null).getDeclaredConstructor(new Class[0]);
                setAccessible(socketImplCtor);
            } catch (ClassNotFoundException e2) {
                throw new AssertionError(e2);
            } catch (NoSuchMethodException e3) {
                throw new AssertionError(e3);
            }
        } catch (NoSuchMethodException e4) {
            throw new AssertionError(e4);
        }
    }

    private static void setAccessible(final AccessibleObject accessibleObject) {
        AccessController.doPrivileged(new PrivilegedAction<Void>() { // from class: com.oracle.net.Sdp.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public Void run2() throws SecurityException {
                accessibleObject.setAccessible(true);
                return null;
            }
        });
    }

    /* loaded from: rt.jar:com/oracle/net/Sdp$SdpSocket.class */
    private static class SdpSocket extends Socket {
        SdpSocket(SocketImpl socketImpl) throws SocketException {
            super(socketImpl);
        }
    }

    private static SocketImpl createSocketImpl() {
        try {
            return socketImplCtor.newInstance(new Object[0]);
        } catch (IllegalAccessException e2) {
            throw new AssertionError(e2);
        } catch (InstantiationException e3) {
            throw new AssertionError(e3);
        } catch (InvocationTargetException e4) {
            throw new AssertionError(e4);
        }
    }

    public static Socket openSocket() throws IOException {
        return new SdpSocket(createSocketImpl());
    }

    public static ServerSocket openServerSocket() throws IOException {
        try {
            return serverSocketCtor.newInstance(createSocketImpl());
        } catch (IllegalAccessException e2) {
            throw new AssertionError(e2);
        } catch (InstantiationException e3) {
            throw new AssertionError(e3);
        } catch (InvocationTargetException e4) {
            Throwable cause = e4.getCause();
            if (cause instanceof IOException) {
                throw ((IOException) cause);
            }
            if (cause instanceof RuntimeException) {
                throw ((RuntimeException) cause);
            }
            throw new RuntimeException(e4);
        }
    }

    public static SocketChannel openSocketChannel() throws IOException {
        return Secrets.newSocketChannel(SdpSupport.createSocket());
    }

    public static ServerSocketChannel openServerSocketChannel() throws IOException {
        return Secrets.newServerSocketChannel(SdpSupport.createSocket());
    }
}
