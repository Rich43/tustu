package com.sun.xml.internal.ws.transport.http.client;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import com.sun.xml.internal.ws.api.EndpointAddress;
import com.sun.xml.internal.ws.api.message.Packet;
import com.sun.xml.internal.ws.client.BindingProviderProperties;
import com.sun.xml.internal.ws.client.ClientTransportException;
import com.sun.xml.internal.ws.developer.JAXWSProperties;
import com.sun.xml.internal.ws.resources.ClientMessages;
import com.sun.xml.internal.ws.transport.Headers;
import java.io.FilterInputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.handler.MessageContext;

/* loaded from: rt.jar:com/sun/xml/internal/ws/transport/http/client/HttpClientTransport.class */
public class HttpClientTransport {
    private static final byte[] THROW_AWAY_BUFFER = new byte[8192];
    int statusCode;
    String statusMessage;
    int contentLength;
    private final Map<String, List<String>> reqHeaders;
    private OutputStream outputStream;
    private boolean https;
    private final EndpointAddress endpoint;
    private final Packet context;
    private final Integer chunkSize;
    private Map<String, List<String>> respHeaders = null;
    private HttpURLConnection httpConnection = null;

    static {
        try {
            JAXBContext.newInstance(new Class[0]).createUnmarshaller();
        } catch (JAXBException e2) {
        }
    }

    public HttpClientTransport(@NotNull Packet packet, @NotNull Map<String, List<String>> reqHeaders) {
        this.endpoint = packet.endpointAddress;
        this.context = packet;
        this.reqHeaders = reqHeaders;
        this.chunkSize = (Integer) this.context.invocationProperties.get(JAXWSProperties.HTTP_CLIENT_STREAMING_CHUNK_SIZE);
    }

    OutputStream getOutput() {
        try {
            createHttpConnection();
            if (requiresOutputStream()) {
                this.outputStream = this.httpConnection.getOutputStream();
                if (this.chunkSize != null) {
                    this.outputStream = new WSChunkedOuputStream(this.outputStream, this.chunkSize.intValue());
                }
                List<String> contentEncoding = this.reqHeaders.get("Content-Encoding");
                if (contentEncoding != null && contentEncoding.get(0).contains("gzip")) {
                    this.outputStream = new GZIPOutputStream(this.outputStream);
                }
            }
            this.httpConnection.connect();
            return this.outputStream;
        } catch (Exception ex) {
            throw new ClientTransportException(ClientMessages.localizableHTTP_CLIENT_FAILED(ex), ex);
        }
    }

    void closeOutput() throws IOException {
        if (this.outputStream != null) {
            this.outputStream.close();
            this.outputStream = null;
        }
    }

    @Nullable
    InputStream getInput() {
        String contentEncoding;
        try {
            InputStream in = readResponse();
            if (in != null && (contentEncoding = this.httpConnection.getContentEncoding()) != null && contentEncoding.contains("gzip")) {
                in = new GZIPInputStream(in);
            }
            return in;
        } catch (IOException e2) {
            throw new ClientTransportException(ClientMessages.localizableHTTP_STATUS_CODE(Integer.valueOf(this.statusCode), this.statusMessage), e2);
        }
    }

    public Map<String, List<String>> getHeaders() {
        if (this.respHeaders != null) {
            return this.respHeaders;
        }
        this.respHeaders = new Headers();
        this.respHeaders.putAll(this.httpConnection.getHeaderFields());
        return this.respHeaders;
    }

    @Nullable
    protected InputStream readResponse() {
        InputStream is;
        try {
            is = this.httpConnection.getInputStream();
        } catch (IOException e2) {
            is = this.httpConnection.getErrorStream();
        }
        if (is == null) {
            return is;
        }
        final InputStream temp = is;
        return new FilterInputStream(temp) { // from class: com.sun.xml.internal.ws.transport.http.client.HttpClientTransport.1
            boolean closed;

            @Override // java.io.FilterInputStream, java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
            public void close() throws IOException {
                if (!this.closed) {
                    this.closed = true;
                    while (temp.read(HttpClientTransport.THROW_AWAY_BUFFER) != -1) {
                    }
                    super.close();
                }
            }
        };
    }

    protected void readResponseCodeAndMessage() {
        try {
            this.statusCode = this.httpConnection.getResponseCode();
            this.statusMessage = this.httpConnection.getResponseMessage();
            this.contentLength = this.httpConnection.getContentLength();
        } catch (IOException ioe) {
            throw new WebServiceException(ioe);
        }
    }

    protected HttpURLConnection openConnection(Packet packet) {
        return null;
    }

    protected boolean checkHTTPS(HttpURLConnection connection) {
        if (connection instanceof HttpsURLConnection) {
            String verificationProperty = (String) this.context.invocationProperties.get(BindingProviderProperties.HOSTNAME_VERIFICATION_PROPERTY);
            if (verificationProperty != null && verificationProperty.equalsIgnoreCase("true")) {
                ((HttpsURLConnection) connection).setHostnameVerifier(new HttpClientVerifier());
            }
            HostnameVerifier verifier = (HostnameVerifier) this.context.invocationProperties.get(JAXWSProperties.HOSTNAME_VERIFIER);
            if (verifier != null) {
                ((HttpsURLConnection) connection).setHostnameVerifier(verifier);
            }
            SSLSocketFactory sslSocketFactory = (SSLSocketFactory) this.context.invocationProperties.get(JAXWSProperties.SSL_SOCKET_FACTORY);
            if (sslSocketFactory != null) {
                ((HttpsURLConnection) connection).setSSLSocketFactory(sslSocketFactory);
                return true;
            }
            return true;
        }
        return false;
    }

    private void createHttpConnection() throws IOException {
        this.httpConnection = openConnection(this.context);
        if (this.httpConnection == null) {
            this.httpConnection = (HttpURLConnection) this.endpoint.openConnection();
        }
        String scheme = this.endpoint.getURI().getScheme();
        if (scheme.equals("https")) {
            this.https = true;
        }
        if (checkHTTPS(this.httpConnection)) {
            this.https = true;
        }
        this.httpConnection.setAllowUserInteraction(true);
        this.httpConnection.setDoOutput(true);
        this.httpConnection.setDoInput(true);
        String requestMethod = (String) this.context.invocationProperties.get(MessageContext.HTTP_REQUEST_METHOD);
        String method = requestMethod != null ? requestMethod : "POST";
        this.httpConnection.setRequestMethod(method);
        Integer reqTimeout = (Integer) this.context.invocationProperties.get(JAXWSProperties.REQUEST_TIMEOUT);
        if (reqTimeout != null) {
            this.httpConnection.setReadTimeout(reqTimeout.intValue());
        }
        Integer connectTimeout = (Integer) this.context.invocationProperties.get(JAXWSProperties.CONNECT_TIMEOUT);
        if (connectTimeout != null) {
            this.httpConnection.setConnectTimeout(connectTimeout.intValue());
        }
        Integer chunkSize = (Integer) this.context.invocationProperties.get(JAXWSProperties.HTTP_CLIENT_STREAMING_CHUNK_SIZE);
        if (chunkSize != null) {
            this.httpConnection.setChunkedStreamingMode(chunkSize.intValue());
        }
        for (Map.Entry<String, List<String>> entry : this.reqHeaders.entrySet()) {
            if (!"Content-Length".equals(entry.getKey())) {
                for (String value : entry.getValue()) {
                    this.httpConnection.addRequestProperty(entry.getKey(), value);
                }
            }
        }
    }

    boolean isSecure() {
        return this.https;
    }

    protected void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    private boolean requiresOutputStream() {
        return (this.httpConnection.getRequestMethod().equalsIgnoreCase("GET") || this.httpConnection.getRequestMethod().equalsIgnoreCase("HEAD") || this.httpConnection.getRequestMethod().equalsIgnoreCase("DELETE")) ? false : true;
    }

    @Nullable
    String getContentType() {
        return this.httpConnection.getContentType();
    }

    public int getContentLength() {
        return this.httpConnection.getContentLength();
    }

    /* loaded from: rt.jar:com/sun/xml/internal/ws/transport/http/client/HttpClientTransport$HttpClientVerifier.class */
    private static class HttpClientVerifier implements HostnameVerifier {
        private HttpClientVerifier() {
        }

        @Override // javax.net.ssl.HostnameVerifier
        public boolean verify(String s2, SSLSession sslSession) {
            return true;
        }
    }

    /* loaded from: rt.jar:com/sun/xml/internal/ws/transport/http/client/HttpClientTransport$LocalhostHttpClientVerifier.class */
    private static class LocalhostHttpClientVerifier implements HostnameVerifier {
        private LocalhostHttpClientVerifier() {
        }

        @Override // javax.net.ssl.HostnameVerifier
        public boolean verify(String s2, SSLSession sslSession) {
            return "localhost".equalsIgnoreCase(s2) || "127.0.0.1".equals(s2);
        }
    }

    /* loaded from: rt.jar:com/sun/xml/internal/ws/transport/http/client/HttpClientTransport$WSChunkedOuputStream.class */
    private static final class WSChunkedOuputStream extends FilterOutputStream {
        final int chunkSize;

        WSChunkedOuputStream(OutputStream actual, int chunkSize) {
            super(actual);
            this.chunkSize = chunkSize;
        }

        @Override // java.io.FilterOutputStream, java.io.OutputStream
        public void write(byte[] b2, int off, int len) throws IOException {
            while (len > 0) {
                int sent = len > this.chunkSize ? this.chunkSize : len;
                this.out.write(b2, off, sent);
                len -= sent;
                off += sent;
            }
        }
    }
}
