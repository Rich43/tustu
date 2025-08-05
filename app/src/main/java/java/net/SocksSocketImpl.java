package java.net;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.Proxy;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.Iterator;
import jdk.internal.dynalink.CallSiteDescriptor;
import org.apache.commons.net.ftp.FTP;
import sun.net.SocksProxy;
import sun.net.www.ParseUtil;
import sun.security.action.GetPropertyAction;

/* loaded from: rt.jar:java/net/SocksSocketImpl.class */
class SocksSocketImpl extends PlainSocketImpl implements SocksConsts {
    private String server;
    private int serverPort;
    private InetSocketAddress external_address;
    private boolean useV4;
    private Socket cmdsock;
    private InputStream cmdIn;
    private OutputStream cmdOut;
    private boolean applicationSetProxy;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !SocksSocketImpl.class.desiredAssertionStatus();
    }

    SocksSocketImpl() {
        this.server = null;
        this.serverPort = SocksConsts.DEFAULT_PORT;
        this.useV4 = false;
        this.cmdsock = null;
        this.cmdIn = null;
        this.cmdOut = null;
    }

    SocksSocketImpl(String str, int i2) {
        this.server = null;
        this.serverPort = SocksConsts.DEFAULT_PORT;
        this.useV4 = false;
        this.cmdsock = null;
        this.cmdIn = null;
        this.cmdOut = null;
        this.server = str;
        this.serverPort = i2 == -1 ? SocksConsts.DEFAULT_PORT : i2;
    }

    SocksSocketImpl(Proxy proxy) {
        this.server = null;
        this.serverPort = SocksConsts.DEFAULT_PORT;
        this.useV4 = false;
        this.cmdsock = null;
        this.cmdIn = null;
        this.cmdOut = null;
        SocketAddress socketAddressAddress = proxy.address();
        if (socketAddressAddress instanceof InetSocketAddress) {
            InetSocketAddress inetSocketAddress = (InetSocketAddress) socketAddressAddress;
            this.server = inetSocketAddress.getHostString();
            this.serverPort = inetSocketAddress.getPort();
        }
    }

    void setV4() {
        this.useV4 = true;
    }

    private synchronized void privilegedConnect(final String str, final int i2, final int i3) throws IOException {
        try {
            AccessController.doPrivileged(new PrivilegedExceptionAction<Void>() { // from class: java.net.SocksSocketImpl.1
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.security.PrivilegedExceptionAction
                public Void run() throws IOException {
                    SocksSocketImpl.this.superConnectServer(str, i2, i3);
                    SocksSocketImpl.this.cmdIn = SocksSocketImpl.this.getInputStream();
                    SocksSocketImpl.this.cmdOut = SocksSocketImpl.this.getOutputStream();
                    return null;
                }
            });
        } catch (PrivilegedActionException e2) {
            throw ((IOException) e2.getException());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void superConnectServer(String str, int i2, int i3) throws IOException {
        super.connect(new InetSocketAddress(str, i2), i3);
    }

    private static int remainingMillis(long j2) throws IOException {
        if (j2 == 0) {
            return 0;
        }
        long jCurrentTimeMillis = j2 - System.currentTimeMillis();
        if (jCurrentTimeMillis > 0) {
            return (int) jCurrentTimeMillis;
        }
        throw new SocketTimeoutException();
    }

    private int readSocksReply(InputStream inputStream, byte[] bArr) throws IOException {
        return readSocksReply(inputStream, bArr, 0L);
    }

    private int readSocksReply(InputStream inputStream, byte[] bArr, long j2) throws IOException {
        int length = bArr.length;
        int i2 = 0;
        while (true) {
            int i3 = i2;
            if (i3 < length) {
                try {
                    int i4 = ((SocketInputStream) inputStream).read(bArr, i3, length - i3, remainingMillis(j2));
                    if (i4 < 0) {
                        throw new SocketException("Malformed reply from SOCKS server");
                    }
                    i2 = i3 + i4;
                } catch (SocketTimeoutException e2) {
                    throw new SocketTimeoutException("Connect timed out");
                }
            } else {
                return i3;
            }
        }
    }

    private boolean authenticate(byte b2, InputStream inputStream, BufferedOutputStream bufferedOutputStream) throws IOException {
        return authenticate(b2, inputStream, bufferedOutputStream, 0L);
    }

    private boolean authenticate(byte b2, InputStream inputStream, BufferedOutputStream bufferedOutputStream, long j2) throws IOException {
        String userName;
        if (b2 == 0) {
            return true;
        }
        if (b2 == 2) {
            String str = null;
            final InetAddress byName = InetAddress.getByName(this.server);
            PasswordAuthentication passwordAuthentication = (PasswordAuthentication) AccessController.doPrivileged(new PrivilegedAction<PasswordAuthentication>() { // from class: java.net.SocksSocketImpl.2
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.security.PrivilegedAction
                /* renamed from: run */
                public PasswordAuthentication run2() {
                    return Authenticator.requestPasswordAuthentication(SocksSocketImpl.this.server, byName, SocksSocketImpl.this.serverPort, "SOCKS5", "SOCKS authentication", null);
                }
            });
            if (passwordAuthentication != null) {
                userName = passwordAuthentication.getUserName();
                str = new String(passwordAuthentication.getPassword());
            } else {
                userName = (String) AccessController.doPrivileged(new GetPropertyAction("user.name"));
            }
            if (userName == null) {
                return false;
            }
            bufferedOutputStream.write(1);
            bufferedOutputStream.write(userName.length());
            try {
                bufferedOutputStream.write(userName.getBytes(FTP.DEFAULT_CONTROL_ENCODING));
            } catch (UnsupportedEncodingException e2) {
                if (!$assertionsDisabled) {
                    throw new AssertionError();
                }
            }
            if (str != null) {
                bufferedOutputStream.write(str.length());
                try {
                    bufferedOutputStream.write(str.getBytes(FTP.DEFAULT_CONTROL_ENCODING));
                } catch (UnsupportedEncodingException e3) {
                    if (!$assertionsDisabled) {
                        throw new AssertionError();
                    }
                }
            } else {
                bufferedOutputStream.write(0);
            }
            bufferedOutputStream.flush();
            byte[] bArr = new byte[2];
            if (readSocksReply(inputStream, bArr, j2) != 2 || bArr[1] != 0) {
                bufferedOutputStream.close();
                inputStream.close();
                return false;
            }
            return true;
        }
        return false;
    }

    private void connectV4(InputStream inputStream, OutputStream outputStream, InetSocketAddress inetSocketAddress, long j2) throws IOException {
        if (!(inetSocketAddress.getAddress() instanceof Inet4Address)) {
            throw new SocketException("SOCKS V4 requires IPv4 only addresses");
        }
        outputStream.write(4);
        outputStream.write(1);
        outputStream.write((inetSocketAddress.getPort() >> 8) & 255);
        outputStream.write((inetSocketAddress.getPort() >> 0) & 255);
        outputStream.write(inetSocketAddress.getAddress().getAddress());
        try {
            outputStream.write(getUserName().getBytes(FTP.DEFAULT_CONTROL_ENCODING));
        } catch (UnsupportedEncodingException e2) {
            if (!$assertionsDisabled) {
                throw new AssertionError();
            }
        }
        outputStream.write(0);
        outputStream.flush();
        byte[] bArr = new byte[8];
        int socksReply = readSocksReply(inputStream, bArr, j2);
        if (socksReply != 8) {
            throw new SocketException("Reply from SOCKS server has bad length: " + socksReply);
        }
        if (bArr[0] != 0 && bArr[0] != 4) {
            throw new SocketException("Reply from SOCKS server has bad version");
        }
        SocketException socketException = null;
        switch (bArr[1]) {
            case 90:
                this.external_address = inetSocketAddress;
                break;
            case 91:
                socketException = new SocketException("SOCKS request rejected");
                break;
            case 92:
                socketException = new SocketException("SOCKS server couldn't reach destination");
                break;
            case 93:
                socketException = new SocketException("SOCKS authentication failed");
                break;
            default:
                socketException = new SocketException("Reply from SOCKS server contains bad status");
                break;
        }
        if (socketException != null) {
            inputStream.close();
            outputStream.close();
            throw socketException;
        }
    }

    @Override // java.net.PlainSocketImpl, java.net.AbstractPlainSocketImpl, java.net.SocketImpl
    protected void connect(SocketAddress socketAddress, int i2) throws IOException {
        long j2;
        URI uri;
        if (i2 == 0) {
            j2 = 0;
        } else {
            long jCurrentTimeMillis = System.currentTimeMillis() + i2;
            j2 = jCurrentTimeMillis < 0 ? Long.MAX_VALUE : jCurrentTimeMillis;
        }
        SecurityManager securityManager = System.getSecurityManager();
        if (socketAddress == null || !(socketAddress instanceof InetSocketAddress)) {
            throw new IllegalArgumentException("Unsupported address type");
        }
        InetSocketAddress inetSocketAddress = (InetSocketAddress) socketAddress;
        if (securityManager != null) {
            if (inetSocketAddress.isUnresolved()) {
                securityManager.checkConnect(inetSocketAddress.getHostName(), inetSocketAddress.getPort());
            } else {
                securityManager.checkConnect(inetSocketAddress.getAddress().getHostAddress(), inetSocketAddress.getPort());
            }
        }
        if (this.server == null) {
            ProxySelector proxySelector = (ProxySelector) AccessController.doPrivileged(new PrivilegedAction<ProxySelector>() { // from class: java.net.SocksSocketImpl.3
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.security.PrivilegedAction
                /* renamed from: run */
                public ProxySelector run2() {
                    return ProxySelector.getDefault();
                }
            });
            if (proxySelector == null) {
                super.connect(inetSocketAddress, remainingMillis(j2));
                return;
            }
            String hostString = inetSocketAddress.getHostString();
            if ((inetSocketAddress.getAddress() instanceof Inet6Address) && !hostString.startsWith("[") && hostString.indexOf(CallSiteDescriptor.TOKEN_DELIMITER) >= 0) {
                hostString = "[" + hostString + "]";
            }
            try {
                uri = new URI("socket://" + ParseUtil.encodePath(hostString) + CallSiteDescriptor.TOKEN_DELIMITER + inetSocketAddress.getPort());
            } catch (URISyntaxException e2) {
                if (!$assertionsDisabled) {
                    throw new AssertionError(e2);
                }
                uri = null;
            }
            IOException iOException = null;
            Iterator<Proxy> it = proxySelector.select(uri).iterator();
            if (it == null || !it.hasNext()) {
                super.connect(inetSocketAddress, remainingMillis(j2));
                return;
            }
            while (it.hasNext()) {
                Proxy next = it.next();
                if (next == null || next.type() != Proxy.Type.SOCKS) {
                    super.connect(inetSocketAddress, remainingMillis(j2));
                    return;
                }
                if (!(next.address() instanceof InetSocketAddress)) {
                    throw new SocketException("Unknown address type for proxy: " + ((Object) next));
                }
                this.server = ((InetSocketAddress) next.address()).getHostString();
                this.serverPort = ((InetSocketAddress) next.address()).getPort();
                if ((next instanceof SocksProxy) && ((SocksProxy) next).protocolVersion() == 4) {
                    this.useV4 = true;
                }
                try {
                    privilegedConnect(this.server, this.serverPort, remainingMillis(j2));
                    break;
                } catch (IOException e3) {
                    proxySelector.connectFailed(uri, next.address(), e3);
                    this.server = null;
                    this.serverPort = -1;
                    iOException = e3;
                }
            }
            if (this.server == null) {
                throw new SocketException("Can't connect to SOCKS proxy:" + iOException.getMessage());
            }
        } else {
            try {
                privilegedConnect(this.server, this.serverPort, remainingMillis(j2));
            } catch (IOException e4) {
                throw new SocketException(e4.getMessage());
            }
        }
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(this.cmdOut, 512);
        InputStream inputStream = this.cmdIn;
        if (this.useV4) {
            if (inetSocketAddress.isUnresolved()) {
                throw new UnknownHostException(inetSocketAddress.toString());
            }
            connectV4(inputStream, bufferedOutputStream, inetSocketAddress, j2);
            return;
        }
        bufferedOutputStream.write(5);
        bufferedOutputStream.write(2);
        bufferedOutputStream.write(0);
        bufferedOutputStream.write(2);
        bufferedOutputStream.flush();
        byte[] bArr = new byte[2];
        if (readSocksReply(inputStream, bArr, j2) != 2 || bArr[0] != 5) {
            if (inetSocketAddress.isUnresolved()) {
                throw new UnknownHostException(inetSocketAddress.toString());
            }
            connectV4(inputStream, bufferedOutputStream, inetSocketAddress, j2);
            return;
        }
        if (bArr[1] == -1) {
            throw new SocketException("SOCKS : No acceptable methods");
        }
        if (!authenticate(bArr[1], inputStream, bufferedOutputStream, j2)) {
            throw new SocketException("SOCKS : authentication failed");
        }
        bufferedOutputStream.write(5);
        bufferedOutputStream.write(1);
        bufferedOutputStream.write(0);
        if (inetSocketAddress.isUnresolved()) {
            bufferedOutputStream.write(3);
            bufferedOutputStream.write(inetSocketAddress.getHostName().length());
            try {
                bufferedOutputStream.write(inetSocketAddress.getHostName().getBytes(FTP.DEFAULT_CONTROL_ENCODING));
            } catch (UnsupportedEncodingException e5) {
                if (!$assertionsDisabled) {
                    throw new AssertionError();
                }
            }
            bufferedOutputStream.write((inetSocketAddress.getPort() >> 8) & 255);
            bufferedOutputStream.write((inetSocketAddress.getPort() >> 0) & 255);
        } else if (inetSocketAddress.getAddress() instanceof Inet6Address) {
            bufferedOutputStream.write(4);
            bufferedOutputStream.write(inetSocketAddress.getAddress().getAddress());
            bufferedOutputStream.write((inetSocketAddress.getPort() >> 8) & 255);
            bufferedOutputStream.write((inetSocketAddress.getPort() >> 0) & 255);
        } else {
            bufferedOutputStream.write(1);
            bufferedOutputStream.write(inetSocketAddress.getAddress().getAddress());
            bufferedOutputStream.write((inetSocketAddress.getPort() >> 8) & 255);
            bufferedOutputStream.write((inetSocketAddress.getPort() >> 0) & 255);
        }
        bufferedOutputStream.flush();
        byte[] bArr2 = new byte[4];
        if (readSocksReply(inputStream, bArr2, j2) != 4) {
            throw new SocketException("Reply from SOCKS server has bad length");
        }
        SocketException socketException = null;
        switch (bArr2[1]) {
            case 0:
                switch (bArr2[3]) {
                    case 1:
                        if (readSocksReply(inputStream, new byte[4], j2) != 4) {
                            throw new SocketException("Reply from SOCKS server badly formatted");
                        }
                        if (readSocksReply(inputStream, new byte[2], j2) != 2) {
                            throw new SocketException("Reply from SOCKS server badly formatted");
                        }
                        break;
                    case 2:
                    default:
                        socketException = new SocketException("Reply from SOCKS server contains wrong code");
                        break;
                    case 3:
                        byte[] bArr3 = new byte[1];
                        if (readSocksReply(inputStream, bArr3, j2) != 1) {
                            throw new SocketException("Reply from SOCKS server badly formatted");
                        }
                        int i3 = bArr3[0] & 255;
                        if (readSocksReply(inputStream, new byte[i3], j2) != i3) {
                            throw new SocketException("Reply from SOCKS server badly formatted");
                        }
                        if (readSocksReply(inputStream, new byte[2], j2) != 2) {
                            throw new SocketException("Reply from SOCKS server badly formatted");
                        }
                        break;
                    case 4:
                        if (readSocksReply(inputStream, new byte[16], j2) != 16) {
                            throw new SocketException("Reply from SOCKS server badly formatted");
                        }
                        if (readSocksReply(inputStream, new byte[2], j2) != 2) {
                            throw new SocketException("Reply from SOCKS server badly formatted");
                        }
                        break;
                }
            case 1:
                socketException = new SocketException("SOCKS server general failure");
                break;
            case 2:
                socketException = new SocketException("SOCKS: Connection not allowed by ruleset");
                break;
            case 3:
                socketException = new SocketException("SOCKS: Network unreachable");
                break;
            case 4:
                socketException = new SocketException("SOCKS: Host unreachable");
                break;
            case 5:
                socketException = new SocketException("SOCKS: Connection refused");
                break;
            case 6:
                socketException = new SocketException("SOCKS: TTL expired");
                break;
            case 7:
                socketException = new SocketException("SOCKS: Command not supported");
                break;
            case 8:
                socketException = new SocketException("SOCKS: address type not supported");
                break;
        }
        if (socketException != null) {
            inputStream.close();
            bufferedOutputStream.close();
            throw socketException;
        }
        this.external_address = inetSocketAddress;
    }

    private void bindV4(InputStream inputStream, OutputStream outputStream, InetAddress inetAddress, int i2) throws IOException {
        if (!(inetAddress instanceof Inet4Address)) {
            throw new SocketException("SOCKS V4 requires IPv4 only addresses");
        }
        super.bind(inetAddress, i2);
        byte[] address = inetAddress.getAddress();
        if (inetAddress.isAnyLocalAddress()) {
            address = ((InetAddress) AccessController.doPrivileged(new PrivilegedAction<InetAddress>() { // from class: java.net.SocksSocketImpl.4
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.security.PrivilegedAction
                /* renamed from: run */
                public InetAddress run2() {
                    return SocksSocketImpl.this.cmdsock.getLocalAddress();
                }
            })).getAddress();
        }
        outputStream.write(4);
        outputStream.write(2);
        outputStream.write((super.getLocalPort() >> 8) & 255);
        outputStream.write((super.getLocalPort() >> 0) & 255);
        outputStream.write(address);
        try {
            outputStream.write(getUserName().getBytes(FTP.DEFAULT_CONTROL_ENCODING));
        } catch (UnsupportedEncodingException e2) {
            if (!$assertionsDisabled) {
                throw new AssertionError();
            }
        }
        outputStream.write(0);
        outputStream.flush();
        byte[] bArr = new byte[8];
        int socksReply = readSocksReply(inputStream, bArr);
        if (socksReply != 8) {
            throw new SocketException("Reply from SOCKS server has bad length: " + socksReply);
        }
        if (bArr[0] != 0 && bArr[0] != 4) {
            throw new SocketException("Reply from SOCKS server has bad version");
        }
        SocketException socketException = null;
        switch (bArr[1]) {
            case 90:
                this.external_address = new InetSocketAddress(inetAddress, i2);
                break;
            case 91:
                socketException = new SocketException("SOCKS request rejected");
                break;
            case 92:
                socketException = new SocketException("SOCKS server couldn't reach destination");
                break;
            case 93:
                socketException = new SocketException("SOCKS authentication failed");
                break;
            default:
                socketException = new SocketException("Reply from SOCKS server contains bad status");
                break;
        }
        if (socketException != null) {
            inputStream.close();
            outputStream.close();
            throw socketException;
        }
    }

    /* JADX WARN: Type inference failed for: r0v221 */
    /* JADX WARN: Type inference failed for: r0v222 */
    protected synchronized void socksBind(InetSocketAddress inetSocketAddress) throws IOException {
        URI uri;
        if (this.socket != null) {
            return;
        }
        if (this.server == null) {
            ProxySelector proxySelector = (ProxySelector) AccessController.doPrivileged(new PrivilegedAction<ProxySelector>() { // from class: java.net.SocksSocketImpl.5
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.security.PrivilegedAction
                /* renamed from: run */
                public ProxySelector run2() {
                    return ProxySelector.getDefault();
                }
            });
            if (proxySelector == null) {
                return;
            }
            String hostString = inetSocketAddress.getHostString();
            if ((inetSocketAddress.getAddress() instanceof Inet6Address) && !hostString.startsWith("[") && hostString.indexOf(CallSiteDescriptor.TOKEN_DELIMITER) >= 0) {
                hostString = "[" + hostString + "]";
            }
            try {
                uri = new URI("serversocket://" + ParseUtil.encodePath(hostString) + CallSiteDescriptor.TOKEN_DELIMITER + inetSocketAddress.getPort());
            } catch (URISyntaxException e2) {
                if (!$assertionsDisabled) {
                    throw new AssertionError(e2);
                }
                uri = null;
            }
            Exception exc = null;
            Iterator<Proxy> it = proxySelector.select(uri).iterator();
            if (it == null || !it.hasNext()) {
                return;
            }
            while (it.hasNext()) {
                Proxy next = it.next();
                if (next == null || next.type() != Proxy.Type.SOCKS) {
                    return;
                }
                if (!(next.address() instanceof InetSocketAddress)) {
                    throw new SocketException("Unknown address type for proxy: " + ((Object) next));
                }
                this.server = ((InetSocketAddress) next.address()).getHostString();
                this.serverPort = ((InetSocketAddress) next.address()).getPort();
                if ((next instanceof SocksProxy) && ((SocksProxy) next).protocolVersion() == 4) {
                    this.useV4 = true;
                }
                try {
                    AccessController.doPrivileged(new PrivilegedExceptionAction<Void>() { // from class: java.net.SocksSocketImpl.6
                        /* JADX WARN: Can't rename method to resolve collision */
                        @Override // java.security.PrivilegedExceptionAction
                        public Void run() throws Exception {
                            SocksSocketImpl.this.cmdsock = new Socket(new PlainSocketImpl());
                            SocksSocketImpl.this.cmdsock.connect(new InetSocketAddress(SocksSocketImpl.this.server, SocksSocketImpl.this.serverPort));
                            SocksSocketImpl.this.cmdIn = SocksSocketImpl.this.cmdsock.getInputStream();
                            SocksSocketImpl.this.cmdOut = SocksSocketImpl.this.cmdsock.getOutputStream();
                            return null;
                        }
                    });
                } catch (Exception e3) {
                    proxySelector.connectFailed(uri, next.address(), new SocketException(e3.getMessage()));
                    this.server = null;
                    this.serverPort = -1;
                    this.cmdsock = null;
                    exc = e3;
                }
            }
            if (this.server == null || this.cmdsock == null) {
                throw new SocketException("Can't connect to SOCKS proxy:" + exc.getMessage());
            }
        } else {
            try {
                AccessController.doPrivileged(new PrivilegedExceptionAction<Void>() { // from class: java.net.SocksSocketImpl.7
                    /* JADX WARN: Can't rename method to resolve collision */
                    @Override // java.security.PrivilegedExceptionAction
                    public Void run() throws Exception {
                        SocksSocketImpl.this.cmdsock = new Socket(new PlainSocketImpl());
                        SocksSocketImpl.this.cmdsock.connect(new InetSocketAddress(SocksSocketImpl.this.server, SocksSocketImpl.this.serverPort));
                        SocksSocketImpl.this.cmdIn = SocksSocketImpl.this.cmdsock.getInputStream();
                        SocksSocketImpl.this.cmdOut = SocksSocketImpl.this.cmdsock.getOutputStream();
                        return null;
                    }
                });
            } catch (Exception e4) {
                throw new SocketException(e4.getMessage());
            }
        }
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(this.cmdOut, 512);
        InputStream inputStream = this.cmdIn;
        if (this.useV4) {
            bindV4(inputStream, bufferedOutputStream, inetSocketAddress.getAddress(), inetSocketAddress.getPort());
            return;
        }
        bufferedOutputStream.write(5);
        bufferedOutputStream.write(2);
        bufferedOutputStream.write(0);
        bufferedOutputStream.write(2);
        bufferedOutputStream.flush();
        byte[] bArr = new byte[2];
        if (readSocksReply(inputStream, bArr) != 2 || bArr[0] != 5) {
            bindV4(inputStream, bufferedOutputStream, inetSocketAddress.getAddress(), inetSocketAddress.getPort());
            return;
        }
        if (bArr[1] == -1) {
            throw new SocketException("SOCKS : No acceptable methods");
        }
        if (!authenticate(bArr[1], inputStream, bufferedOutputStream)) {
            throw new SocketException("SOCKS : authentication failed");
        }
        bufferedOutputStream.write(5);
        bufferedOutputStream.write(2);
        bufferedOutputStream.write(0);
        int port = inetSocketAddress.getPort();
        if (inetSocketAddress.isUnresolved()) {
            bufferedOutputStream.write(3);
            bufferedOutputStream.write(inetSocketAddress.getHostName().length());
            try {
                bufferedOutputStream.write(inetSocketAddress.getHostName().getBytes(FTP.DEFAULT_CONTROL_ENCODING));
            } catch (UnsupportedEncodingException e5) {
                if (!$assertionsDisabled) {
                    throw new AssertionError();
                }
            }
            bufferedOutputStream.write((port >> 8) & 255);
            bufferedOutputStream.write((port >> 0) & 255);
        } else if (inetSocketAddress.getAddress() instanceof Inet4Address) {
            byte[] address = inetSocketAddress.getAddress().getAddress();
            bufferedOutputStream.write(1);
            bufferedOutputStream.write(address);
            bufferedOutputStream.write((port >> 8) & 255);
            bufferedOutputStream.write((port >> 0) & 255);
            bufferedOutputStream.flush();
        } else if (inetSocketAddress.getAddress() instanceof Inet6Address) {
            byte[] address2 = inetSocketAddress.getAddress().getAddress();
            bufferedOutputStream.write(4);
            bufferedOutputStream.write(address2);
            bufferedOutputStream.write((port >> 8) & 255);
            bufferedOutputStream.write((port >> 0) & 255);
            bufferedOutputStream.flush();
        } else {
            this.cmdsock.close();
            throw new SocketException("unsupported address type : " + ((Object) inetSocketAddress));
        }
        byte[] bArr2 = new byte[4];
        readSocksReply(inputStream, bArr2);
        SocketException socketException = null;
        switch (bArr2[1] == true ? 1 : 0) {
            case 0:
                switch (bArr2[3] == true ? 1 : 0) {
                    case 1:
                        byte[] bArr3 = new byte[4];
                        if (readSocksReply(inputStream, bArr3) != 4) {
                            throw new SocketException("Reply from SOCKS server badly formatted");
                        }
                        byte[] bArr4 = new byte[2];
                        if (readSocksReply(inputStream, bArr4) != 2) {
                            throw new SocketException("Reply from SOCKS server badly formatted");
                        }
                        this.external_address = new InetSocketAddress(new Inet4Address("", bArr3), ((bArr4[0] & 255) << 8) + (bArr4[1] & 255));
                        break;
                    case 3:
                        int i2 = bArr2[1];
                        byte[] bArr5 = new byte[i2];
                        if (readSocksReply(inputStream, bArr5) != i2) {
                            throw new SocketException("Reply from SOCKS server badly formatted");
                        }
                        byte[] bArr6 = new byte[2];
                        if (readSocksReply(inputStream, bArr6) != 2) {
                            throw new SocketException("Reply from SOCKS server badly formatted");
                        }
                        this.external_address = new InetSocketAddress(new String(bArr5), ((bArr6[0] & 255) << 8) + (bArr6[1] & 255));
                        break;
                    case 4:
                        int i3 = bArr2[1];
                        byte[] bArr7 = new byte[i3];
                        if (readSocksReply(inputStream, bArr7) != i3) {
                            throw new SocketException("Reply from SOCKS server badly formatted");
                        }
                        byte[] bArr8 = new byte[2];
                        if (readSocksReply(inputStream, bArr8) != 2) {
                            throw new SocketException("Reply from SOCKS server badly formatted");
                        }
                        this.external_address = new InetSocketAddress(new Inet6Address("", bArr7), ((bArr8[0] & 255) << 8) + (bArr8[1] & 255));
                        break;
                }
            case 1:
                socketException = new SocketException("SOCKS server general failure");
                break;
            case 2:
                socketException = new SocketException("SOCKS: Bind not allowed by ruleset");
                break;
            case 3:
                socketException = new SocketException("SOCKS: Network unreachable");
                break;
            case 4:
                socketException = new SocketException("SOCKS: Host unreachable");
                break;
            case 5:
                socketException = new SocketException("SOCKS: Connection refused");
                break;
            case 6:
                socketException = new SocketException("SOCKS: TTL expired");
                break;
            case 7:
                socketException = new SocketException("SOCKS: Command not supported");
                break;
            case 8:
                socketException = new SocketException("SOCKS: address type not supported");
                break;
        }
        if (socketException != null) {
            inputStream.close();
            bufferedOutputStream.close();
            this.cmdsock.close();
            this.cmdsock = null;
            throw socketException;
        }
        this.cmdIn = inputStream;
        this.cmdOut = bufferedOutputStream;
    }

    protected void acceptFrom(SocketImpl socketImpl, InetSocketAddress inetSocketAddress) throws IOException {
        if (this.cmdsock == null) {
            return;
        }
        InputStream inputStream = this.cmdIn;
        socksBind(inetSocketAddress);
        inputStream.read();
        int i2 = inputStream.read();
        inputStream.read();
        SocketException socketException = null;
        InetSocketAddress inetSocketAddress2 = null;
        switch (i2) {
            case 0:
                switch (inputStream.read()) {
                    case 1:
                        byte[] bArr = new byte[4];
                        readSocksReply(inputStream, bArr);
                        inetSocketAddress2 = new InetSocketAddress(new Inet4Address("", bArr), (inputStream.read() << 8) + inputStream.read());
                        break;
                    case 3:
                        byte[] bArr2 = new byte[inputStream.read()];
                        readSocksReply(inputStream, bArr2);
                        inetSocketAddress2 = new InetSocketAddress(new String(bArr2), (inputStream.read() << 8) + inputStream.read());
                        break;
                    case 4:
                        byte[] bArr3 = new byte[16];
                        readSocksReply(inputStream, bArr3);
                        inetSocketAddress2 = new InetSocketAddress(new Inet6Address("", bArr3), (inputStream.read() << 8) + inputStream.read());
                        break;
                }
            case 1:
                socketException = new SocketException("SOCKS server general failure");
                break;
            case 2:
                socketException = new SocketException("SOCKS: Accept not allowed by ruleset");
                break;
            case 3:
                socketException = new SocketException("SOCKS: Network unreachable");
                break;
            case 4:
                socketException = new SocketException("SOCKS: Host unreachable");
                break;
            case 5:
                socketException = new SocketException("SOCKS: Connection refused");
                break;
            case 6:
                socketException = new SocketException("SOCKS: TTL expired");
                break;
            case 7:
                socketException = new SocketException("SOCKS: Command not supported");
                break;
            case 8:
                socketException = new SocketException("SOCKS: address type not supported");
                break;
        }
        if (socketException != null) {
            this.cmdIn.close();
            this.cmdOut.close();
            this.cmdsock.close();
            this.cmdsock = null;
            throw socketException;
        }
        if (socketImpl instanceof SocksSocketImpl) {
            ((SocksSocketImpl) socketImpl).external_address = inetSocketAddress2;
        }
        if (socketImpl instanceof PlainSocketImpl) {
            PlainSocketImpl plainSocketImpl = (PlainSocketImpl) socketImpl;
            plainSocketImpl.setInputStream((SocketInputStream) inputStream);
            plainSocketImpl.setFileDescriptor(this.cmdsock.getImpl().getFileDescriptor());
            plainSocketImpl.setAddress(this.cmdsock.getImpl().getInetAddress());
            plainSocketImpl.setPort(this.cmdsock.getImpl().getPort());
            plainSocketImpl.setLocalPort(this.cmdsock.getImpl().getLocalPort());
        } else {
            socketImpl.fd = this.cmdsock.getImpl().fd;
            socketImpl.address = this.cmdsock.getImpl().address;
            socketImpl.port = this.cmdsock.getImpl().port;
            socketImpl.localport = this.cmdsock.getImpl().localport;
        }
        this.cmdsock = null;
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

    @Override // java.net.PlainSocketImpl, java.net.AbstractPlainSocketImpl, java.net.SocketImpl
    protected void close() throws IOException {
        if (this.cmdsock != null) {
            this.cmdsock.close();
        }
        this.cmdsock = null;
        super.close();
    }

    private String getUserName() {
        String property = "";
        if (this.applicationSetProxy) {
            try {
                property = System.getProperty("user.name");
            } catch (SecurityException e2) {
            }
        } else {
            property = (String) AccessController.doPrivileged(new GetPropertyAction("user.name"));
        }
        return property;
    }
}
