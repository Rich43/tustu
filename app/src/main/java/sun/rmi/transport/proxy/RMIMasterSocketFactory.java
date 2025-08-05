package sun.rmi.transport.proxy;

import com.sun.xml.internal.ws.wsdl.parser.WSDLConstants;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.server.LogStream;
import java.rmi.server.RMISocketFactory;
import java.security.AccessControlContext;
import java.security.AccessController;
import java.util.Hashtable;
import java.util.Vector;
import sun.rmi.runtime.Log;
import sun.security.action.GetBooleanAction;
import sun.security.action.GetLongAction;
import sun.security.action.GetPropertyAction;

/* loaded from: rt.jar:sun/rmi/transport/proxy/RMIMasterSocketFactory.class */
public class RMIMasterSocketFactory extends RMISocketFactory {
    static int logLevel = LogStream.parseLevel(getLogLevel());
    static final Log proxyLog = Log.getLog("sun.rmi.transport.tcp.proxy", WSDLConstants.ATTR_TRANSPORT, logLevel);
    private static long connectTimeout = getConnectTimeout();
    private static final boolean eagerHttpFallback = ((Boolean) AccessController.doPrivileged(new GetBooleanAction("sun.rmi.transport.proxy.eagerHttpFallback"))).booleanValue();
    private static final int MaxRememberedHosts = 64;
    private Hashtable<String, RMISocketFactory> successTable = new Hashtable<>();
    private Vector<String> hostList = new Vector<>(64);
    protected RMISocketFactory initialFactory = new RMIDirectSocketFactory();
    protected Vector<RMISocketFactory> altFactoryList = new Vector<>(2);

    private static String getLogLevel() {
        return (String) AccessController.doPrivileged(new GetPropertyAction("sun.rmi.transport.proxy.logLevel"));
    }

    private static long getConnectTimeout() {
        return ((Long) AccessController.doPrivileged(new GetLongAction("sun.rmi.transport.proxy.connectTimeout", 15000L))).longValue();
    }

    public RMIMasterSocketFactory() {
        boolean z2 = false;
        try {
            String str = (String) AccessController.doPrivileged(new GetPropertyAction("http.proxyHost"));
            str = str == null ? (String) AccessController.doPrivileged(new GetPropertyAction("proxyHost")) : str;
            if (!((String) AccessController.doPrivileged(new GetPropertyAction("java.rmi.server.disableHttp", "true"))).equalsIgnoreCase("true") && str != null) {
                if (str.length() > 0) {
                    z2 = true;
                }
            }
        } catch (Exception e2) {
        }
        if (z2) {
            this.altFactoryList.addElement(new RMIHttpToPortSocketFactory());
            this.altFactoryList.addElement(new RMIHttpToCGISocketFactory());
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:281:0x05bd A[EXC_TOP_SPLITTER, SYNTHETIC] */
    @Override // java.rmi.server.RMISocketFactory, java.rmi.server.RMIClientSocketFactory
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public java.net.Socket createSocket(java.lang.String r9, int r10) throws java.io.IOException {
        /*
            Method dump skipped, instructions count: 1581
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: sun.rmi.transport.proxy.RMIMasterSocketFactory.createSocket(java.lang.String, int):java.net.Socket");
    }

    void rememberFactory(String str, RMISocketFactory rMISocketFactory) {
        synchronized (this.successTable) {
            while (this.hostList.size() >= 64) {
                this.successTable.remove(this.hostList.elementAt(0));
                this.hostList.removeElementAt(0);
            }
            this.hostList.addElement(str);
            this.successTable.put(str, rMISocketFactory);
        }
    }

    Socket checkConnector(AsyncConnector asyncConnector) throws IOException {
        Exception exception = asyncConnector.getException();
        if (exception != null) {
            exception.fillInStackTrace();
            if (exception instanceof IOException) {
                throw ((IOException) exception);
            }
            if (exception instanceof RuntimeException) {
                throw ((RuntimeException) exception);
            }
            throw new Error("internal error: unexpected checked exception: " + exception.toString());
        }
        return asyncConnector.getSocket();
    }

    @Override // java.rmi.server.RMISocketFactory, java.rmi.server.RMIServerSocketFactory
    public ServerSocket createServerSocket(int i2) throws IOException {
        return this.initialFactory.createServerSocket(i2);
    }

    /* loaded from: rt.jar:sun/rmi/transport/proxy/RMIMasterSocketFactory$AsyncConnector.class */
    private class AsyncConnector implements Runnable {
        private RMISocketFactory factory;
        private String host;
        private int port;
        private AccessControlContext acc;
        private Exception exception = null;
        private Socket socket = null;
        private boolean cleanUp = false;

        AsyncConnector(RMISocketFactory rMISocketFactory, String str, int i2, AccessControlContext accessControlContext) {
            this.factory = rMISocketFactory;
            this.host = str;
            this.port = i2;
            this.acc = accessControlContext;
            SecurityManager securityManager = System.getSecurityManager();
            if (securityManager != null) {
                securityManager.checkConnect(str, i2);
            }
        }

        @Override // java.lang.Runnable
        public void run() {
            try {
                Socket socketCreateSocket = this.factory.createSocket(this.host, this.port);
                synchronized (this) {
                    this.socket = socketCreateSocket;
                    notify();
                }
                RMIMasterSocketFactory.this.rememberFactory(this.host, this.factory);
                synchronized (this) {
                    if (this.cleanUp) {
                        try {
                            this.socket.close();
                        } catch (IOException e2) {
                        }
                    }
                }
            } catch (Exception e3) {
                synchronized (this) {
                    this.exception = e3;
                    notify();
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public synchronized Exception getException() {
            return this.exception;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public synchronized Socket getSocket() {
            return this.socket;
        }

        synchronized void notUsed() {
            if (this.socket != null) {
                try {
                    this.socket.close();
                } catch (IOException e2) {
                }
            }
            this.cleanUp = true;
        }
    }
}
