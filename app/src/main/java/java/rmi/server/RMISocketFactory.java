package java.rmi.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import sun.rmi.transport.proxy.RMIMasterSocketFactory;

/* loaded from: rt.jar:java/rmi/server/RMISocketFactory.class */
public abstract class RMISocketFactory implements RMIClientSocketFactory, RMIServerSocketFactory {
    private static RMISocketFactory defaultSocketFactory;
    private static RMISocketFactory factory = null;
    private static RMIFailureHandler handler = null;

    @Override // java.rmi.server.RMIClientSocketFactory
    public abstract Socket createSocket(String str, int i2) throws IOException;

    @Override // java.rmi.server.RMIServerSocketFactory
    public abstract ServerSocket createServerSocket(int i2) throws IOException;

    public static synchronized void setSocketFactory(RMISocketFactory rMISocketFactory) throws IOException {
        if (factory != null) {
            throw new SocketException("factory already defined");
        }
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkSetFactory();
        }
        factory = rMISocketFactory;
    }

    public static synchronized RMISocketFactory getSocketFactory() {
        return factory;
    }

    public static synchronized RMISocketFactory getDefaultSocketFactory() {
        if (defaultSocketFactory == null) {
            defaultSocketFactory = new RMIMasterSocketFactory();
        }
        return defaultSocketFactory;
    }

    public static synchronized void setFailureHandler(RMIFailureHandler rMIFailureHandler) {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkSetFactory();
        }
        handler = rMIFailureHandler;
    }

    public static synchronized RMIFailureHandler getFailureHandler() {
        return handler;
    }
}
