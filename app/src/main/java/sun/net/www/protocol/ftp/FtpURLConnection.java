package sun.net.www.protocol.ftp;

import com.sun.glass.ui.Clipboard;
import com.sun.xml.internal.ws.transport.http.DeploymentDescriptorParser;
import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.FilterInputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.ProxySelector;
import java.net.SocketPermission;
import java.net.URI;
import java.net.URL;
import java.net.UnknownHostException;
import java.security.AccessController;
import java.security.Permission;
import java.security.PrivilegedAction;
import java.util.Iterator;
import java.util.StringTokenizer;
import jdk.internal.dynalink.CallSiteDescriptor;
import org.icepdf.core.util.PdfOps;
import sun.net.ProgressMonitor;
import sun.net.ProgressSource;
import sun.net.ftp.FtpClient;
import sun.net.ftp.FtpLoginException;
import sun.net.ftp.FtpProtocolException;
import sun.net.util.IPAddressUtil;
import sun.net.www.MessageHeader;
import sun.net.www.MeteredStream;
import sun.net.www.ParseUtil;
import sun.net.www.URLConnection;
import sun.net.www.protocol.http.HttpURLConnection;
import sun.security.action.GetPropertyAction;
import sun.security.util.SecurityConstants;

/* loaded from: rt.jar:sun/net/www/protocol/ftp/FtpURLConnection.class */
public class FtpURLConnection extends URLConnection {
    HttpURLConnection http;
    private Proxy instProxy;
    InputStream is;
    OutputStream os;
    FtpClient ftp;
    Permission permission;
    String password;
    String user;
    String host;
    String pathname;
    String filename;
    String fullpath;
    int port;
    static final int NONE = 0;
    static final int ASCII = 1;
    static final int BIN = 2;
    static final int DIR = 3;
    int type;
    private int connectTimeout;
    private int readTimeout;

    /* loaded from: rt.jar:sun/net/www/protocol/ftp/FtpURLConnection$FtpInputStream.class */
    protected class FtpInputStream extends FilterInputStream {
        FtpClient ftp;

        FtpInputStream(FtpClient ftpClient, InputStream inputStream) {
            super(new BufferedInputStream(inputStream));
            this.ftp = ftpClient;
        }

        @Override // java.io.FilterInputStream, java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
        public void close() throws IOException {
            super.close();
            if (this.ftp != null) {
                this.ftp.close();
            }
        }
    }

    /* loaded from: rt.jar:sun/net/www/protocol/ftp/FtpURLConnection$FtpOutputStream.class */
    protected class FtpOutputStream extends FilterOutputStream {
        FtpClient ftp;

        FtpOutputStream(FtpClient ftpClient, OutputStream outputStream) {
            super(outputStream);
            this.ftp = ftpClient;
        }

        @Override // java.io.FilterOutputStream, java.io.OutputStream, java.io.Closeable, java.lang.AutoCloseable
        public void close() throws IOException {
            super.close();
            if (this.ftp != null) {
                this.ftp.close();
            }
        }
    }

    static URL checkURL(URL url) throws IllegalArgumentException {
        if (url != null && url.toExternalForm().indexOf(10) > -1) {
            MalformedURLException malformedURLException = new MalformedURLException("Illegal character in URL");
            throw new IllegalArgumentException(malformedURLException.getMessage(), malformedURLException);
        }
        String strCheckAuthority = IPAddressUtil.checkAuthority(url);
        if (strCheckAuthority != null) {
            MalformedURLException malformedURLException2 = new MalformedURLException(strCheckAuthority);
            throw new IllegalArgumentException(malformedURLException2.getMessage(), malformedURLException2);
        }
        return url;
    }

    public FtpURLConnection(URL url) {
        this(url, null);
    }

    FtpURLConnection(URL url, Proxy proxy) {
        super(checkURL(url));
        this.http = null;
        this.is = null;
        this.os = null;
        this.ftp = null;
        this.type = 0;
        this.connectTimeout = -1;
        this.readTimeout = -1;
        this.instProxy = proxy;
        this.host = url.getHost();
        this.port = url.getPort();
        String userInfo = url.getUserInfo();
        if (userInfo != null) {
            int iIndexOf = userInfo.indexOf(58);
            if (iIndexOf == -1) {
                this.user = ParseUtil.decode(userInfo);
                this.password = null;
            } else {
                this.user = ParseUtil.decode(userInfo.substring(0, iIndexOf));
                this.password = ParseUtil.decode(userInfo.substring(iIndexOf + 1));
            }
        }
    }

    private void setTimeouts() {
        if (this.ftp != null) {
            if (this.connectTimeout >= 0) {
                this.ftp.setConnectTimeout(this.connectTimeout);
            }
            if (this.readTimeout >= 0) {
                this.ftp.setReadTimeout(this.readTimeout);
            }
        }
    }

    @Override // java.net.URLConnection
    public synchronized void connect() throws IOException {
        if (this.connected) {
            return;
        }
        Proxy next = null;
        if (this.instProxy == null) {
            ProxySelector proxySelector = (ProxySelector) AccessController.doPrivileged(new PrivilegedAction<ProxySelector>() { // from class: sun.net.www.protocol.ftp.FtpURLConnection.1
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.security.PrivilegedAction
                /* renamed from: run */
                public ProxySelector run2() {
                    return ProxySelector.getDefault();
                }
            });
            if (proxySelector != null) {
                URI uri = ParseUtil.toURI(this.url);
                Iterator<Proxy> it = proxySelector.select(uri).iterator();
                while (it.hasNext()) {
                    next = it.next();
                    if (next == null || next == Proxy.NO_PROXY || next.type() == Proxy.Type.SOCKS) {
                        break;
                    }
                    if (next.type() != Proxy.Type.HTTP || !(next.address() instanceof InetSocketAddress)) {
                        proxySelector.connectFailed(uri, next.address(), new IOException("Wrong proxy type"));
                    } else {
                        InetSocketAddress inetSocketAddress = (InetSocketAddress) next.address();
                        try {
                            this.http = new HttpURLConnection(this.url, next);
                            this.http.setDoInput(getDoInput());
                            this.http.setDoOutput(getDoOutput());
                            if (this.connectTimeout >= 0) {
                                this.http.setConnectTimeout(this.connectTimeout);
                            }
                            if (this.readTimeout >= 0) {
                                this.http.setReadTimeout(this.readTimeout);
                            }
                            this.http.connect();
                            this.connected = true;
                            return;
                        } catch (IOException e2) {
                            proxySelector.connectFailed(uri, inetSocketAddress, e2);
                            this.http = null;
                        }
                    }
                }
            }
        } else {
            next = this.instProxy;
            if (next.type() == Proxy.Type.HTTP) {
                this.http = new HttpURLConnection(this.url, this.instProxy);
                this.http.setDoInput(getDoInput());
                this.http.setDoOutput(getDoOutput());
                if (this.connectTimeout >= 0) {
                    this.http.setConnectTimeout(this.connectTimeout);
                }
                if (this.readTimeout >= 0) {
                    this.http.setReadTimeout(this.readTimeout);
                }
                this.http.connect();
                this.connected = true;
                return;
            }
        }
        if (this.user == null) {
            this.user = "anonymous";
            this.password = (String) AccessController.doPrivileged(new GetPropertyAction("ftp.protocol.user", "Java" + ((String) AccessController.doPrivileged(new GetPropertyAction("java.version"))) + "@"));
        }
        try {
            this.ftp = FtpClient.create();
            if (next != null) {
                this.ftp.setProxy(next);
            }
            setTimeouts();
            if (this.port != -1) {
                this.ftp.connect(new InetSocketAddress(this.host, this.port));
            } else {
                this.ftp.connect(new InetSocketAddress(this.host, FtpClient.defaultPort()));
            }
            try {
                this.ftp.login(this.user, this.password == null ? null : this.password.toCharArray());
                this.connected = true;
            } catch (FtpProtocolException e3) {
                this.ftp.close();
                throw new FtpLoginException("Invalid username/password");
            }
        } catch (UnknownHostException e4) {
            throw e4;
        } catch (FtpProtocolException e5) {
            if (this.ftp != null) {
                try {
                    this.ftp.close();
                } catch (IOException e6) {
                    e5.addSuppressed(e6);
                }
            }
            throw new IOException(e5);
        }
    }

    private void decodePath(String str) {
        int iIndexOf = str.indexOf(";type=");
        if (iIndexOf >= 0) {
            String strSubstring = str.substring(iIndexOf + 6, str.length());
            if (PdfOps.i_TOKEN.equalsIgnoreCase(strSubstring)) {
                this.type = 2;
            }
            if ("a".equalsIgnoreCase(strSubstring)) {
                this.type = 1;
            }
            if (PdfOps.d_TOKEN.equalsIgnoreCase(strSubstring)) {
                this.type = 3;
            }
            str = str.substring(0, iIndexOf);
        }
        if (str != null && str.length() > 1 && str.charAt(0) == '/') {
            str = str.substring(1);
        }
        if (str == null || str.length() == 0) {
            str = "./";
        }
        if (!str.endsWith("/")) {
            int iLastIndexOf = str.lastIndexOf(47);
            if (iLastIndexOf > 0) {
                this.filename = str.substring(iLastIndexOf + 1, str.length());
                this.filename = ParseUtil.decode(this.filename);
                this.pathname = str.substring(0, iLastIndexOf);
            } else {
                this.filename = ParseUtil.decode(str);
                this.pathname = null;
            }
        } else {
            this.pathname = str.substring(0, str.length() - 1);
            this.filename = null;
        }
        if (this.pathname != null) {
            this.fullpath = this.pathname + "/" + (this.filename != null ? this.filename : "");
        } else {
            this.fullpath = this.filename;
        }
    }

    private void cd(String str) throws FtpProtocolException, IOException {
        if (str == null || str.isEmpty()) {
            return;
        }
        if (str.indexOf(47) == -1) {
            this.ftp.changeDirectory(ParseUtil.decode(str));
            return;
        }
        StringTokenizer stringTokenizer = new StringTokenizer(str, "/");
        while (stringTokenizer.hasMoreTokens()) {
            this.ftp.changeDirectory(ParseUtil.decode(stringTokenizer.nextToken()));
        }
    }

    @Override // java.net.URLConnection
    public InputStream getInputStream() throws IOException {
        if (!this.connected) {
            connect();
        }
        if (this.http != null) {
            return this.http.getInputStream();
        }
        if (this.os != null) {
            throw new IOException("Already opened for output");
        }
        if (this.is != null) {
            return this.is;
        }
        MessageHeader messageHeader = new MessageHeader();
        try {
            decodePath(this.url.getPath());
            if (this.filename == null || this.type == 3) {
                this.ftp.setAsciiType();
                cd(this.pathname);
                if (this.filename == null) {
                    this.is = new FtpInputStream(this.ftp, this.ftp.list(null));
                } else {
                    this.is = new FtpInputStream(this.ftp, this.ftp.nameList(this.filename));
                }
            } else {
                if (this.type == 1) {
                    this.ftp.setAsciiType();
                } else {
                    this.ftp.setBinaryType();
                }
                cd(this.pathname);
                this.is = new FtpInputStream(this.ftp, this.ftp.getFileStream(this.filename));
            }
            try {
                long lastTransferSize = this.ftp.getLastTransferSize();
                messageHeader.add("content-length", Long.toString(lastTransferSize));
                if (lastTransferSize > 0) {
                    ProgressSource progressSource = null;
                    if (ProgressMonitor.getDefault().shouldMeterInput(this.url, "GET")) {
                        progressSource = new ProgressSource(this.url, "GET", lastTransferSize);
                        progressSource.beginTracking();
                    }
                    this.is = new MeteredStream(this.is, progressSource, lastTransferSize);
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
            if (0 != 0) {
                messageHeader.add("content-type", Clipboard.TEXT_TYPE);
                messageHeader.add("access-type", "directory");
            } else {
                messageHeader.add("access-type", DeploymentDescriptorParser.ATTR_FILE);
                String strGuessContentTypeFromName = guessContentTypeFromName(this.fullpath);
                if (strGuessContentTypeFromName == null && this.is.markSupported()) {
                    strGuessContentTypeFromName = guessContentTypeFromStream(this.is);
                }
                if (strGuessContentTypeFromName != null) {
                    messageHeader.add("content-type", strGuessContentTypeFromName);
                }
            }
        } catch (FileNotFoundException e3) {
            try {
                cd(this.fullpath);
                this.ftp.setAsciiType();
                this.is = new FtpInputStream(this.ftp, this.ftp.list(null));
                messageHeader.add("content-type", Clipboard.TEXT_TYPE);
                messageHeader.add("access-type", "directory");
            } catch (IOException e4) {
                FileNotFoundException fileNotFoundException = new FileNotFoundException(this.fullpath);
                if (this.ftp != null) {
                    try {
                        this.ftp.close();
                    } catch (IOException e5) {
                        fileNotFoundException.addSuppressed(e5);
                    }
                }
                throw fileNotFoundException;
            } catch (FtpProtocolException e6) {
                FileNotFoundException fileNotFoundException2 = new FileNotFoundException(this.fullpath);
                if (this.ftp != null) {
                    try {
                        this.ftp.close();
                    } catch (IOException e7) {
                        fileNotFoundException2.addSuppressed(e7);
                    }
                }
                throw fileNotFoundException2;
            }
        } catch (FtpProtocolException e8) {
            if (this.ftp != null) {
                try {
                    this.ftp.close();
                } catch (IOException e9) {
                    e8.addSuppressed(e9);
                }
            }
            throw new IOException(e8);
        }
        setProperties(messageHeader);
        return this.is;
    }

    @Override // java.net.URLConnection
    public OutputStream getOutputStream() throws IOException {
        if (!this.connected) {
            connect();
        }
        if (this.http != null) {
            OutputStream outputStream = this.http.getOutputStream();
            this.http.getInputStream();
            return outputStream;
        }
        if (this.is != null) {
            throw new IOException("Already opened for input");
        }
        if (this.os != null) {
            return this.os;
        }
        decodePath(this.url.getPath());
        if (this.filename == null || this.filename.length() == 0) {
            throw new IOException("illegal filename for a PUT");
        }
        try {
            if (this.pathname != null) {
                cd(this.pathname);
            }
            if (this.type == 1) {
                this.ftp.setAsciiType();
            } else {
                this.ftp.setBinaryType();
            }
            this.os = new FtpOutputStream(this.ftp, this.ftp.putFileStream(this.filename, false));
            return this.os;
        } catch (FtpProtocolException e2) {
            throw new IOException(e2);
        }
    }

    String guessContentTypeFromFilename(String str) {
        return guessContentTypeFromName(str);
    }

    @Override // java.net.URLConnection
    public Permission getPermission() {
        if (this.permission == null) {
            int port = this.url.getPort();
            this.permission = new SocketPermission(this.host + CallSiteDescriptor.TOKEN_DELIMITER + (port < 0 ? FtpClient.defaultPort() : port), SecurityConstants.SOCKET_CONNECT_ACTION);
        }
        return this.permission;
    }

    @Override // sun.net.www.URLConnection, java.net.URLConnection
    public void setRequestProperty(String str, String str2) {
        super.setRequestProperty(str, str2);
        if ("type".equals(str)) {
            if (PdfOps.i_TOKEN.equalsIgnoreCase(str2)) {
                this.type = 2;
            } else if ("a".equalsIgnoreCase(str2)) {
                this.type = 1;
            } else {
                if (PdfOps.d_TOKEN.equalsIgnoreCase(str2)) {
                    this.type = 3;
                    return;
                }
                throw new IllegalArgumentException("Value of '" + str + "' request property was '" + str2 + "' when it must be either 'i', 'a' or 'd'");
            }
        }
    }

    @Override // sun.net.www.URLConnection, java.net.URLConnection
    public String getRequestProperty(String str) {
        String requestProperty = super.getRequestProperty(str);
        if (requestProperty == null && "type".equals(str)) {
            requestProperty = this.type == 1 ? "a" : this.type == 3 ? PdfOps.d_TOKEN : PdfOps.i_TOKEN;
        }
        return requestProperty;
    }

    @Override // java.net.URLConnection
    public void setConnectTimeout(int i2) {
        if (i2 < 0) {
            throw new IllegalArgumentException("timeouts can't be negative");
        }
        this.connectTimeout = i2;
    }

    @Override // java.net.URLConnection
    public int getConnectTimeout() {
        if (this.connectTimeout < 0) {
            return 0;
        }
        return this.connectTimeout;
    }

    @Override // java.net.URLConnection
    public void setReadTimeout(int i2) {
        if (i2 < 0) {
            throw new IllegalArgumentException("timeouts can't be negative");
        }
        this.readTimeout = i2;
    }

    @Override // java.net.URLConnection
    public int getReadTimeout() {
        if (this.readTimeout < 0) {
            return 0;
        }
        return this.readTimeout;
    }
}
