package sun.net;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Socket;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Arrays;

/* loaded from: rt.jar:sun/net/NetworkClient.class */
public class NetworkClient {
    public static final int DEFAULT_READ_TIMEOUT = -1;
    public static final int DEFAULT_CONNECT_TIMEOUT = -1;
    public PrintStream serverOutput;
    public InputStream serverInput;
    protected static int defaultSoTimeout;
    protected static int defaultConnectTimeout;
    protected static String encoding;
    protected Proxy proxy = Proxy.NO_PROXY;
    protected Socket serverSocket = null;
    protected int readTimeout = -1;
    protected int connectTimeout = -1;

    static {
        final int[] iArr = {0, 0};
        final String[] strArr = {null};
        AccessController.doPrivileged(new PrivilegedAction<Void>() { // from class: sun.net.NetworkClient.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public Void run2() {
                iArr[0] = Integer.getInteger("sun.net.client.defaultReadTimeout", 0).intValue();
                iArr[1] = Integer.getInteger("sun.net.client.defaultConnectTimeout", 0).intValue();
                strArr[0] = System.getProperty("file.encoding", "ISO8859_1");
                return null;
            }
        });
        if (iArr[0] != 0) {
            defaultSoTimeout = iArr[0];
        }
        if (iArr[1] != 0) {
            defaultConnectTimeout = iArr[1];
        }
        encoding = strArr[0];
        try {
            if (!isASCIISuperset(encoding)) {
                encoding = "ISO8859_1";
            }
        } catch (Exception e2) {
            encoding = "ISO8859_1";
        }
    }

    private static boolean isASCIISuperset(String str) throws Exception {
        return Arrays.equals("0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz-_.!~*'();/?:@&=+$,".getBytes(str), new byte[]{48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 45, 95, 46, 33, 126, 42, 39, 40, 41, 59, 47, 63, 58, 64, 38, 61, 43, 36, 44});
    }

    public void openServer(String str, int i2) throws IOException {
        if (this.serverSocket != null) {
            closeServer();
        }
        this.serverSocket = doConnect(str, i2);
        try {
            this.serverOutput = new PrintStream((OutputStream) new BufferedOutputStream(this.serverSocket.getOutputStream()), true, encoding);
            this.serverInput = new BufferedInputStream(this.serverSocket.getInputStream());
        } catch (UnsupportedEncodingException e2) {
            throw new InternalError(encoding + "encoding not found", e2);
        }
    }

    protected Socket doConnect(String str, int i2) throws IOException {
        Socket socketCreateSocket;
        if (this.proxy != null) {
            if (this.proxy.type() == Proxy.Type.SOCKS) {
                socketCreateSocket = (Socket) AccessController.doPrivileged(new PrivilegedAction<Socket>() { // from class: sun.net.NetworkClient.2
                    /* JADX WARN: Can't rename method to resolve collision */
                    @Override // java.security.PrivilegedAction
                    /* renamed from: run */
                    public Socket run2() {
                        return new Socket(NetworkClient.this.proxy);
                    }
                });
            } else if (this.proxy.type() == Proxy.Type.DIRECT) {
                socketCreateSocket = createSocket();
            } else {
                socketCreateSocket = new Socket(Proxy.NO_PROXY);
            }
        } else {
            socketCreateSocket = createSocket();
        }
        if (this.connectTimeout >= 0) {
            socketCreateSocket.connect(new InetSocketAddress(str, i2), this.connectTimeout);
        } else if (defaultConnectTimeout > 0) {
            socketCreateSocket.connect(new InetSocketAddress(str, i2), defaultConnectTimeout);
        } else {
            socketCreateSocket.connect(new InetSocketAddress(str, i2));
        }
        if (this.readTimeout >= 0) {
            socketCreateSocket.setSoTimeout(this.readTimeout);
        } else if (defaultSoTimeout > 0) {
            socketCreateSocket.setSoTimeout(defaultSoTimeout);
        }
        return socketCreateSocket;
    }

    protected Socket createSocket() throws IOException {
        return new Socket();
    }

    protected InetAddress getLocalAddress() throws IOException {
        if (this.serverSocket == null) {
            throw new IOException("not connected");
        }
        return (InetAddress) AccessController.doPrivileged(new PrivilegedAction<InetAddress>() { // from class: sun.net.NetworkClient.3
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public InetAddress run2() {
                return NetworkClient.this.serverSocket.getLocalAddress();
            }
        });
    }

    public void closeServer() throws IOException {
        if (!serverIsOpen()) {
            return;
        }
        this.serverSocket.close();
        this.serverSocket = null;
        this.serverInput = null;
        this.serverOutput = null;
    }

    public boolean serverIsOpen() {
        return this.serverSocket != null;
    }

    public NetworkClient(String str, int i2) throws IOException {
        openServer(str, i2);
    }

    public NetworkClient() {
    }

    public void setConnectTimeout(int i2) {
        this.connectTimeout = i2;
    }

    public int getConnectTimeout() {
        return this.connectTimeout;
    }

    public void setReadTimeout(int i2) {
        if (i2 == -1) {
            i2 = defaultSoTimeout;
        }
        if (this.serverSocket != null && i2 >= 0) {
            try {
                this.serverSocket.setSoTimeout(i2);
            } catch (IOException e2) {
            }
        }
        this.readTimeout = i2;
    }

    public int getReadTimeout() {
        return this.readTimeout;
    }
}
