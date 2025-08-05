package sun.net.httpserver;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpPrincipal;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URI;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLSession;

/* loaded from: rt.jar:sun/net/httpserver/ExchangeImpl.class */
class ExchangeImpl {
    Headers reqHdrs;
    Request req;
    String method;
    boolean writefinished;
    URI uri;
    HttpConnection connection;
    long reqContentLen;
    long rspContentLen;
    InputStream ris;
    OutputStream ros;
    Thread thread;
    boolean close;
    boolean closed;
    private static final String pattern = "EEE, dd MMM yyyy HH:mm:ss zzz";
    private static final TimeZone gmtTZ;
    private static final ThreadLocal<DateFormat> dateFormat;
    private static final String HEAD = "HEAD";
    InputStream uis;
    OutputStream uos;
    LeftOverInputStream uis_orig;
    PlaceholderOutputStream uos_orig;
    boolean sentHeaders;
    Map<String, Object> attributes;
    HttpPrincipal principal;
    static final /* synthetic */ boolean $assertionsDisabled;
    boolean http10 = false;
    int rcode = -1;
    private byte[] rspbuf = new byte[128];
    Headers rspHdrs = new Headers();
    ServerImpl server = getServerImpl();

    static {
        $assertionsDisabled = !ExchangeImpl.class.desiredAssertionStatus();
        gmtTZ = TimeZone.getTimeZone("GMT");
        dateFormat = new ThreadLocal<DateFormat>() { // from class: sun.net.httpserver.ExchangeImpl.1
            /* JADX INFO: Access modifiers changed from: protected */
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.lang.ThreadLocal
            public DateFormat initialValue() {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(ExchangeImpl.pattern, Locale.US);
                simpleDateFormat.setTimeZone(ExchangeImpl.gmtTZ);
                return simpleDateFormat;
            }
        };
    }

    ExchangeImpl(String str, URI uri, Request request, long j2, HttpConnection httpConnection) throws IOException {
        this.req = request;
        this.reqHdrs = request.headers();
        this.method = str;
        this.uri = uri;
        this.connection = httpConnection;
        this.reqContentLen = j2;
        this.ros = request.outputStream();
        this.ris = request.inputStream();
        this.server.startExchange();
    }

    public Headers getRequestHeaders() {
        return new UnmodifiableHeaders(this.reqHdrs);
    }

    public Headers getResponseHeaders() {
        return this.rspHdrs;
    }

    public URI getRequestURI() {
        return this.uri;
    }

    public String getRequestMethod() {
        return this.method;
    }

    public HttpContextImpl getHttpContext() {
        return this.connection.getHttpContext();
    }

    private boolean isHeadRequest() {
        return HEAD.equals(getRequestMethod());
    }

    public void close() {
        if (this.closed) {
            return;
        }
        this.closed = true;
        try {
            if (this.uis_orig == null || this.uos == null) {
                this.connection.close();
            } else {
                if (!this.uos_orig.isWrapped()) {
                    this.connection.close();
                    return;
                }
                if (!this.uis_orig.isClosed()) {
                    this.uis_orig.close();
                }
                this.uos.close();
            }
        } catch (IOException e2) {
            this.connection.close();
        }
    }

    public InputStream getRequestBody() {
        if (this.uis != null) {
            return this.uis;
        }
        if (this.reqContentLen == -1) {
            this.uis_orig = new ChunkedInputStream(this, this.ris);
            this.uis = this.uis_orig;
        } else {
            this.uis_orig = new FixedLengthInputStream(this, this.ris, this.reqContentLen);
            this.uis = this.uis_orig;
        }
        return this.uis;
    }

    LeftOverInputStream getOriginalInputStream() {
        return this.uis_orig;
    }

    public int getResponseCode() {
        return this.rcode;
    }

    public OutputStream getResponseBody() {
        if (this.uos == null) {
            this.uos_orig = new PlaceholderOutputStream(null);
            this.uos = this.uos_orig;
        }
        return this.uos;
    }

    PlaceholderOutputStream getPlaceholderResponseBody() {
        getResponseBody();
        return this.uos_orig;
    }

    public void sendResponseHeaders(int i2, long j2) throws IOException {
        if (this.sentHeaders) {
            throw new IOException("headers already sent");
        }
        this.rcode = i2;
        String str = "HTTP/1.1 " + i2 + Code.msg(i2) + "\r\n";
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(this.ros);
        PlaceholderOutputStream placeholderResponseBody = getPlaceholderResponseBody();
        bufferedOutputStream.write(bytes(str, 0), 0, str.length());
        boolean z2 = false;
        this.rspHdrs.set("Date", dateFormat.get().format(new Date()));
        if ((i2 >= 100 && i2 < 200) || i2 == 204 || i2 == 304) {
            if (j2 != -1) {
                this.server.getLogger().warning("sendResponseHeaders: rCode = " + i2 + ": forcing contentLen = -1");
            }
            j2 = -1;
        }
        if (isHeadRequest()) {
            if (j2 >= 0) {
                this.server.getLogger().warning("sendResponseHeaders: being invoked with a content length for a HEAD request");
            }
            z2 = true;
            j2 = 0;
        } else if (j2 == 0) {
            if (this.http10) {
                placeholderResponseBody.setWrappedStream(new UndefLengthOutputStream(this, this.ros));
                this.close = true;
            } else {
                this.rspHdrs.set("Transfer-encoding", "chunked");
                placeholderResponseBody.setWrappedStream(new ChunkedOutputStream(this, this.ros));
            }
        } else {
            if (j2 == -1) {
                z2 = true;
                j2 = 0;
            }
            this.rspHdrs.set("Content-length", Long.toString(j2));
            placeholderResponseBody.setWrappedStream(new FixedLengthOutputStream(this, this.ros, j2));
        }
        write(this.rspHdrs, bufferedOutputStream);
        this.rspContentLen = j2;
        bufferedOutputStream.flush();
        this.sentHeaders = true;
        if (z2) {
            this.server.addEvent(new WriteFinishedEvent(this));
            this.closed = true;
        }
        this.server.logReply(i2, this.req.requestLine(), null);
    }

    void write(Headers headers, OutputStream outputStream) throws IOException {
        for (Map.Entry<String, List<String>> entry : headers.entrySet()) {
            String key = entry.getKey();
            for (String str : entry.getValue()) {
                int length = key.length();
                byte[] bArrBytes = bytes(key, 2);
                int i2 = length + 1;
                bArrBytes[length] = 58;
                bArrBytes[i2] = 32;
                outputStream.write(bArrBytes, 0, i2 + 1);
                byte[] bArrBytes2 = bytes(str, 2);
                int length2 = str.length();
                int i3 = length2 + 1;
                bArrBytes2[length2] = 13;
                bArrBytes2[i3] = 10;
                outputStream.write(bArrBytes2, 0, i3 + 1);
            }
        }
        outputStream.write(13);
        outputStream.write(10);
    }

    private byte[] bytes(String str, int i2) {
        int length = str.length();
        if (length + i2 > this.rspbuf.length) {
            this.rspbuf = new byte[2 * (this.rspbuf.length + ((length + i2) - this.rspbuf.length))];
        }
        char[] charArray = str.toCharArray();
        for (int i3 = 0; i3 < charArray.length; i3++) {
            this.rspbuf[i3] = (byte) charArray[i3];
        }
        return this.rspbuf;
    }

    public InetSocketAddress getRemoteAddress() {
        Socket socket = this.connection.getChannel().socket();
        return new InetSocketAddress(socket.getInetAddress(), socket.getPort());
    }

    public InetSocketAddress getLocalAddress() {
        Socket socket = this.connection.getChannel().socket();
        return new InetSocketAddress(socket.getLocalAddress(), socket.getLocalPort());
    }

    public String getProtocol() {
        String strRequestLine = this.req.requestLine();
        return strRequestLine.substring(strRequestLine.lastIndexOf(32) + 1);
    }

    public SSLSession getSSLSession() {
        SSLEngine sSLEngine = this.connection.getSSLEngine();
        if (sSLEngine == null) {
            return null;
        }
        return sSLEngine.getSession();
    }

    public Object getAttribute(String str) {
        if (str == null) {
            throw new NullPointerException("null name parameter");
        }
        if (this.attributes == null) {
            this.attributes = getHttpContext().getAttributes();
        }
        return this.attributes.get(str);
    }

    public void setAttribute(String str, Object obj) {
        if (str == null) {
            throw new NullPointerException("null name parameter");
        }
        if (this.attributes == null) {
            this.attributes = getHttpContext().getAttributes();
        }
        this.attributes.put(str, obj);
    }

    public void setStreams(InputStream inputStream, OutputStream outputStream) {
        if (!$assertionsDisabled && this.uis == null) {
            throw new AssertionError();
        }
        if (inputStream != null) {
            this.uis = inputStream;
        }
        if (outputStream != null) {
            this.uos = outputStream;
        }
    }

    HttpConnection getConnection() {
        return this.connection;
    }

    ServerImpl getServerImpl() {
        return getHttpContext().getServerImpl();
    }

    public HttpPrincipal getPrincipal() {
        return this.principal;
    }

    void setPrincipal(HttpPrincipal httpPrincipal) {
        this.principal = httpPrincipal;
    }

    static ExchangeImpl get(HttpExchange httpExchange) {
        if (httpExchange instanceof HttpExchangeImpl) {
            return ((HttpExchangeImpl) httpExchange).getExchangeImpl();
        }
        if ($assertionsDisabled || (httpExchange instanceof HttpsExchangeImpl)) {
            return ((HttpsExchangeImpl) httpExchange).getExchangeImpl();
        }
        throw new AssertionError();
    }
}
