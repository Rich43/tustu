package java.net;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.Proxy;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.HashMap;
import java.util.Map;
import jdk.internal.dynalink.CallSiteDescriptor;

/* loaded from: rt.jar:java/net/HttpConnectSocketImpl.class */
class HttpConnectSocketImpl extends PlainSocketImpl {
    private static final String httpURLClazzStr = "sun.net.www.protocol.http.HttpURLConnection";
    private static final String netClientClazzStr = "sun.net.NetworkClient";
    private static final String doTunnelingStr = "doTunneling";
    private static final Field httpField;
    private static final Field serverSocketField;
    private static final Method doTunneling;
    private final String server;
    private InetSocketAddress external_address;
    private HashMap<Integer, Object> optionsMap = new HashMap<>();

    static {
        try {
            Class<?> cls = Class.forName(httpURLClazzStr, true, null);
            httpField = cls.getDeclaredField("http");
            doTunneling = cls.getDeclaredMethod(doTunnelingStr, new Class[0]);
            serverSocketField = Class.forName(netClientClazzStr, true, null).getDeclaredField("serverSocket");
            AccessController.doPrivileged(new PrivilegedAction<Void>() { // from class: java.net.HttpConnectSocketImpl.1
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.security.PrivilegedAction
                /* renamed from: run */
                public Void run2() {
                    HttpConnectSocketImpl.httpField.setAccessible(true);
                    HttpConnectSocketImpl.serverSocketField.setAccessible(true);
                    return null;
                }
            });
        } catch (ReflectiveOperationException e2) {
            throw new InternalError("Should not reach here", e2);
        }
    }

    HttpConnectSocketImpl(String str, int i2) {
        this.server = str;
        this.port = i2;
    }

    HttpConnectSocketImpl(Proxy proxy) {
        SocketAddress socketAddressAddress = proxy.address();
        if (!(socketAddressAddress instanceof InetSocketAddress)) {
            throw new IllegalArgumentException("Unsupported address type");
        }
        InetSocketAddress inetSocketAddress = (InetSocketAddress) socketAddressAddress;
        this.server = inetSocketAddress.getHostString();
        this.port = inetSocketAddress.getPort();
    }

    @Override // java.net.PlainSocketImpl, java.net.AbstractPlainSocketImpl, java.net.SocketImpl
    protected void connect(SocketAddress socketAddress, int i2) throws IOException {
        if (socketAddress == null || !(socketAddress instanceof InetSocketAddress)) {
            throw new IllegalArgumentException("Unsupported address type");
        }
        InetSocketAddress inetSocketAddress = (InetSocketAddress) socketAddress;
        String hostName = inetSocketAddress.isUnresolved() ? inetSocketAddress.getHostName() : inetSocketAddress.getAddress().getHostAddress();
        int port = inetSocketAddress.getPort();
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkConnect(hostName, port);
        }
        Socket socketPrivilegedDoTunnel = privilegedDoTunnel("http://" + hostName + CallSiteDescriptor.TOKEN_DELIMITER + port, i2);
        this.external_address = inetSocketAddress;
        close();
        AbstractPlainSocketImpl abstractPlainSocketImpl = (AbstractPlainSocketImpl) socketPrivilegedDoTunnel.impl;
        getSocket().impl = abstractPlainSocketImpl;
        try {
            for (Map.Entry<Integer, Object> entry : this.optionsMap.entrySet()) {
                abstractPlainSocketImpl.setOption(entry.getKey().intValue(), entry.getValue());
            }
        } catch (IOException e2) {
        }
    }

    @Override // java.net.PlainSocketImpl, java.net.AbstractPlainSocketImpl, java.net.SocketOptions
    public void setOption(int i2, Object obj) throws SocketException {
        super.setOption(i2, obj);
        if (this.external_address != null) {
            return;
        }
        this.optionsMap.put(Integer.valueOf(i2), obj);
    }

    private Socket privilegedDoTunnel(final String str, final int i2) throws IOException {
        try {
            return (Socket) AccessController.doPrivileged(new PrivilegedExceptionAction<Socket>() { // from class: java.net.HttpConnectSocketImpl.2
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.security.PrivilegedExceptionAction
                public Socket run() throws IOException {
                    return HttpConnectSocketImpl.this.doTunnel(str, i2);
                }
            });
        } catch (PrivilegedActionException e2) {
            throw ((IOException) e2.getException());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Socket doTunnel(String str, int i2) throws IOException, IllegalArgumentException {
        HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(str).openConnection(new Proxy(Proxy.Type.HTTP, new InetSocketAddress(this.server, this.port)));
        httpURLConnection.setConnectTimeout(i2);
        httpURLConnection.setReadTimeout(this.timeout);
        httpURLConnection.connect();
        doTunneling(httpURLConnection);
        try {
            return (Socket) serverSocketField.get(httpField.get(httpURLConnection));
        } catch (IllegalAccessException e2) {
            throw new InternalError("Should not reach here", e2);
        }
    }

    private void doTunneling(HttpURLConnection httpURLConnection) throws IllegalArgumentException {
        try {
            doTunneling.invoke(httpURLConnection, new Object[0]);
        } catch (ReflectiveOperationException e2) {
            throw new InternalError("Should not reach here", e2);
        }
    }

    @Override // java.net.PlainSocketImpl, java.net.SocketImpl
    protected InetAddress getInetAddress() {
        if (this.external_address != null) {
            return this.external_address.getAddress();
        }
        return super.getInetAddress();
    }

    @Override // java.net.PlainSocketImpl, java.net.SocketImpl
    protected int getPort() {
        if (this.external_address != null) {
            return this.external_address.getPort();
        }
        return super.getPort();
    }

    @Override // java.net.PlainSocketImpl, java.net.SocketImpl
    protected int getLocalPort() {
        if (this.socket != null) {
            return super.getLocalPort();
        }
        if (this.external_address != null) {
            return this.external_address.getPort();
        }
        return super.getLocalPort();
    }
}
