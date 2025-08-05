package sun.rmi.transport.proxy;

import com.sun.media.jfxmedia.locator.Locator;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketImpl;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.security.AccessController;
import sun.rmi.runtime.Log;
import sun.security.action.GetPropertyAction;

/* loaded from: rt.jar:sun/rmi/transport/proxy/HttpSendSocket.class */
class HttpSendSocket extends Socket implements RMISocketInfo {
    protected String host;
    protected int port;
    protected URL url;
    protected URLConnection conn;
    protected InputStream in;
    protected OutputStream out;
    protected HttpSendInputStream inNotifier;
    protected HttpSendOutputStream outNotifier;
    private String lineSeparator;

    public HttpSendSocket(String str, int i2, URL url) throws IOException {
        super((SocketImpl) null);
        this.conn = null;
        this.in = null;
        this.out = null;
        this.lineSeparator = (String) AccessController.doPrivileged(new GetPropertyAction("line.separator"));
        if (RMIMasterSocketFactory.proxyLog.isLoggable(Log.VERBOSE)) {
            RMIMasterSocketFactory.proxyLog.log(Log.VERBOSE, "host = " + str + ", port = " + i2 + ", url = " + ((Object) url));
        }
        this.host = str;
        this.port = i2;
        this.url = url;
        this.inNotifier = new HttpSendInputStream(null, this);
        this.outNotifier = new HttpSendOutputStream(writeNotify(), this);
    }

    public HttpSendSocket(String str, int i2) throws IOException {
        this(str, i2, new URL("http", str, i2, "/"));
    }

    public HttpSendSocket(InetAddress inetAddress, int i2) throws IOException {
        this(inetAddress.getHostName(), i2);
    }

    @Override // sun.rmi.transport.proxy.RMISocketInfo
    public boolean isReusable() {
        return false;
    }

    public synchronized OutputStream writeNotify() throws IOException {
        if (this.conn != null) {
            throw new IOException("attempt to write on HttpSendSocket after request has been sent");
        }
        this.conn = this.url.openConnection();
        this.conn.setDoOutput(true);
        this.conn.setUseCaches(false);
        this.conn.setRequestProperty("Content-type", Locator.DEFAULT_CONTENT_TYPE);
        this.inNotifier.deactivate();
        this.in = null;
        OutputStream outputStream = this.conn.getOutputStream();
        this.out = outputStream;
        return outputStream;
    }

    public synchronized InputStream readNotify() throws IOException {
        String str;
        RMIMasterSocketFactory.proxyLog.log(Log.VERBOSE, "sending request and activating input stream");
        this.outNotifier.deactivate();
        this.out.close();
        this.out = null;
        try {
            this.in = this.conn.getInputStream();
            String contentType = this.conn.getContentType();
            if (contentType == null || !this.conn.getContentType().equals(Locator.DEFAULT_CONTENT_TYPE)) {
                if (RMIMasterSocketFactory.proxyLog.isLoggable(Log.BRIEF)) {
                    if (contentType == null) {
                        str = "missing content type in response" + this.lineSeparator;
                    } else {
                        str = "invalid content type in response: " + contentType + this.lineSeparator;
                    }
                    String str2 = str + "HttpSendSocket.readNotify: response body: ";
                    try {
                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(this.in));
                        while (true) {
                            String line = bufferedReader.readLine();
                            if (line == null) {
                                break;
                            }
                            str2 = str2 + line + this.lineSeparator;
                        }
                    } catch (IOException e2) {
                    }
                    RMIMasterSocketFactory.proxyLog.log(Log.BRIEF, str2);
                }
                throw new IOException("HTTP request failed");
            }
            return this.in;
        } catch (IOException e3) {
            RMIMasterSocketFactory.proxyLog.log(Log.BRIEF, "failed to get input stream, exception: ", e3);
            throw new IOException("HTTP request failed");
        }
    }

    @Override // java.net.Socket
    public InetAddress getInetAddress() {
        try {
            return InetAddress.getByName(this.host);
        } catch (UnknownHostException e2) {
            return null;
        }
    }

    @Override // java.net.Socket
    public InetAddress getLocalAddress() {
        try {
            return InetAddress.getLocalHost();
        } catch (UnknownHostException e2) {
            return null;
        }
    }

    @Override // java.net.Socket
    public int getPort() {
        return this.port;
    }

    @Override // java.net.Socket
    public int getLocalPort() {
        return -1;
    }

    @Override // java.net.Socket
    public InputStream getInputStream() throws IOException {
        return this.inNotifier;
    }

    @Override // java.net.Socket
    public OutputStream getOutputStream() throws IOException {
        return this.outNotifier;
    }

    @Override // java.net.Socket
    public void setTcpNoDelay(boolean z2) throws SocketException {
    }

    @Override // java.net.Socket
    public boolean getTcpNoDelay() throws SocketException {
        return false;
    }

    @Override // java.net.Socket
    public void setSoLinger(boolean z2, int i2) throws SocketException {
    }

    @Override // java.net.Socket
    public int getSoLinger() throws SocketException {
        return -1;
    }

    @Override // java.net.Socket
    public synchronized void setSoTimeout(int i2) throws SocketException {
    }

    @Override // java.net.Socket
    public synchronized int getSoTimeout() throws SocketException {
        return 0;
    }

    @Override // java.net.Socket, java.io.Closeable, java.lang.AutoCloseable
    public synchronized void close() throws IOException {
        if (this.out != null) {
            this.out.close();
        }
    }

    @Override // java.net.Socket
    public String toString() {
        return "HttpSendSocket[host=" + this.host + ",port=" + this.port + ",url=" + ((Object) this.url) + "]";
    }
}
