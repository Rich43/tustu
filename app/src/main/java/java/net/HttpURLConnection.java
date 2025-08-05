package java.net;

import java.io.IOException;
import java.io.InputStream;
import java.security.Permission;
import java.util.Date;
import jdk.internal.dynalink.CallSiteDescriptor;
import sun.security.util.SecurityConstants;

/* loaded from: rt.jar:java/net/HttpURLConnection.class */
public abstract class HttpURLConnection extends URLConnection {
    protected String method;
    protected int chunkLength;
    protected int fixedContentLength;
    protected long fixedContentLengthLong;
    private static final int DEFAULT_CHUNK_SIZE = 4096;
    protected int responseCode;
    protected String responseMessage;
    protected boolean instanceFollowRedirects;
    public static final int HTTP_OK = 200;
    public static final int HTTP_CREATED = 201;
    public static final int HTTP_ACCEPTED = 202;
    public static final int HTTP_NOT_AUTHORITATIVE = 203;
    public static final int HTTP_NO_CONTENT = 204;
    public static final int HTTP_RESET = 205;
    public static final int HTTP_PARTIAL = 206;
    public static final int HTTP_MULT_CHOICE = 300;
    public static final int HTTP_MOVED_PERM = 301;
    public static final int HTTP_MOVED_TEMP = 302;
    public static final int HTTP_SEE_OTHER = 303;
    public static final int HTTP_NOT_MODIFIED = 304;
    public static final int HTTP_USE_PROXY = 305;
    public static final int HTTP_BAD_REQUEST = 400;
    public static final int HTTP_UNAUTHORIZED = 401;
    public static final int HTTP_PAYMENT_REQUIRED = 402;
    public static final int HTTP_FORBIDDEN = 403;
    public static final int HTTP_NOT_FOUND = 404;
    public static final int HTTP_BAD_METHOD = 405;
    public static final int HTTP_NOT_ACCEPTABLE = 406;
    public static final int HTTP_PROXY_AUTH = 407;
    public static final int HTTP_CLIENT_TIMEOUT = 408;
    public static final int HTTP_CONFLICT = 409;
    public static final int HTTP_GONE = 410;
    public static final int HTTP_LENGTH_REQUIRED = 411;
    public static final int HTTP_PRECON_FAILED = 412;
    public static final int HTTP_ENTITY_TOO_LARGE = 413;
    public static final int HTTP_REQ_TOO_LONG = 414;
    public static final int HTTP_UNSUPPORTED_TYPE = 415;

    @Deprecated
    public static final int HTTP_SERVER_ERROR = 500;
    public static final int HTTP_INTERNAL_ERROR = 500;
    public static final int HTTP_NOT_IMPLEMENTED = 501;
    public static final int HTTP_BAD_GATEWAY = 502;
    public static final int HTTP_UNAVAILABLE = 503;
    public static final int HTTP_GATEWAY_TIMEOUT = 504;
    public static final int HTTP_VERSION = 505;
    private static boolean followRedirects = true;
    private static final String[] methods = {"GET", "POST", "HEAD", "OPTIONS", "PUT", "DELETE", "TRACE"};

    public abstract void disconnect();

    public abstract boolean usingProxy();

    @Override // java.net.URLConnection
    public String getHeaderFieldKey(int i2) {
        return null;
    }

    public void setFixedLengthStreamingMode(int i2) {
        if (this.connected) {
            throw new IllegalStateException("Already connected");
        }
        if (this.chunkLength != -1) {
            throw new IllegalStateException("Chunked encoding streaming mode set");
        }
        if (i2 < 0) {
            throw new IllegalArgumentException("invalid content length");
        }
        this.fixedContentLength = i2;
    }

    public void setFixedLengthStreamingMode(long j2) {
        if (this.connected) {
            throw new IllegalStateException("Already connected");
        }
        if (this.chunkLength != -1) {
            throw new IllegalStateException("Chunked encoding streaming mode set");
        }
        if (j2 < 0) {
            throw new IllegalArgumentException("invalid content length");
        }
        this.fixedContentLengthLong = j2;
    }

    public void setChunkedStreamingMode(int i2) {
        if (this.connected) {
            throw new IllegalStateException("Can't set streaming mode: already connected");
        }
        if (this.fixedContentLength != -1 || this.fixedContentLengthLong != -1) {
            throw new IllegalStateException("Fixed length streaming mode set");
        }
        this.chunkLength = i2 <= 0 ? 4096 : i2;
    }

    @Override // java.net.URLConnection
    public String getHeaderField(int i2) {
        return null;
    }

    protected HttpURLConnection(URL url) {
        super(url);
        this.method = "GET";
        this.chunkLength = -1;
        this.fixedContentLength = -1;
        this.fixedContentLengthLong = -1L;
        this.responseCode = -1;
        this.responseMessage = null;
        this.instanceFollowRedirects = followRedirects;
    }

    public static void setFollowRedirects(boolean z2) {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkSetFactory();
        }
        followRedirects = z2;
    }

    public static boolean getFollowRedirects() {
        return followRedirects;
    }

    public void setInstanceFollowRedirects(boolean z2) {
        this.instanceFollowRedirects = z2;
    }

    public boolean getInstanceFollowRedirects() {
        return this.instanceFollowRedirects;
    }

    public void setRequestMethod(String str) throws ProtocolException {
        SecurityManager securityManager;
        if (this.connected) {
            throw new ProtocolException("Can't reset method: already connected");
        }
        for (int i2 = 0; i2 < methods.length; i2++) {
            if (methods[i2].equals(str)) {
                if (str.equals("TRACE") && (securityManager = System.getSecurityManager()) != null) {
                    securityManager.checkPermission(new NetPermission("allowHttpTrace"));
                }
                this.method = str;
                return;
            }
        }
        throw new ProtocolException("Invalid HTTP method: " + str);
    }

    public String getRequestMethod() {
        return this.method;
    }

    public int getResponseCode() throws IOException {
        int iIndexOf;
        if (this.responseCode != -1) {
            return this.responseCode;
        }
        Exception exc = null;
        try {
            getInputStream();
        } catch (Exception e2) {
            exc = e2;
        }
        String headerField = getHeaderField(0);
        if (headerField == null) {
            if (exc != null) {
                if (exc instanceof RuntimeException) {
                    throw ((RuntimeException) exc);
                }
                throw ((IOException) exc);
            }
            return -1;
        }
        if (headerField.startsWith("HTTP/1.") && (iIndexOf = headerField.indexOf(32)) > 0) {
            int iIndexOf2 = headerField.indexOf(32, iIndexOf + 1);
            if (iIndexOf2 > 0 && iIndexOf2 < headerField.length()) {
                this.responseMessage = headerField.substring(iIndexOf2 + 1);
            }
            if (iIndexOf2 < 0) {
                iIndexOf2 = headerField.length();
            }
            try {
                this.responseCode = Integer.parseInt(headerField.substring(iIndexOf + 1, iIndexOf2));
                return this.responseCode;
            } catch (NumberFormatException e3) {
                return -1;
            }
        }
        return -1;
    }

    public String getResponseMessage() throws IOException {
        getResponseCode();
        return this.responseMessage;
    }

    @Override // java.net.URLConnection
    public long getHeaderFieldDate(String str, long j2) {
        String headerField = getHeaderField(str);
        try {
            if (headerField.indexOf("GMT") == -1) {
                headerField = headerField + " GMT";
            }
            return Date.parse(headerField);
        } catch (Exception e2) {
            return j2;
        }
    }

    @Override // java.net.URLConnection
    public Permission getPermission() throws IOException {
        int port = this.url.getPort();
        return new SocketPermission(this.url.getHost() + CallSiteDescriptor.TOKEN_DELIMITER + (port < 0 ? 80 : port), SecurityConstants.SOCKET_CONNECT_ACTION);
    }

    public InputStream getErrorStream() {
        return null;
    }
}
