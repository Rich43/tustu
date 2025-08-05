package com.sun.xml.internal.ws.transport.http.server;

import com.oracle.webservices.internal.api.message.BasePropertySet;
import com.oracle.webservices.internal.api.message.PropertySet;
import com.sun.istack.internal.NotNull;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpsExchange;
import com.sun.xml.internal.ws.api.message.Packet;
import com.sun.xml.internal.ws.api.server.PortAddressResolver;
import com.sun.xml.internal.ws.api.server.WSEndpoint;
import com.sun.xml.internal.ws.api.server.WebServiceContextDelegate;
import com.sun.xml.internal.ws.developer.JAXWSProperties;
import com.sun.xml.internal.ws.resources.WsservletMessages;
import com.sun.xml.internal.ws.transport.http.HttpAdapter;
import com.sun.xml.internal.ws.transport.http.WSHTTPConnection;
import com.sun.xml.internal.ws.util.ReadAllStream;
import java.io.FilterInputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.handler.MessageContext;
import jdk.internal.dynalink.CallSiteDescriptor;

/* loaded from: rt.jar:com/sun/xml/internal/ws/transport/http/server/ServerConnectionImpl.class */
final class ServerConnectionImpl extends WSHTTPConnection implements WebServiceContextDelegate {
    private final HttpExchange httpExchange;
    private int status;
    private final HttpAdapter adapter;
    private LWHSInputStream in;
    private OutputStream out;
    private static final BasePropertySet.PropertyMap model = parse(ServerConnectionImpl.class);

    public ServerConnectionImpl(@NotNull HttpAdapter adapter, @NotNull HttpExchange httpExchange) {
        this.adapter = adapter;
        this.httpExchange = httpExchange;
    }

    @Override // com.sun.xml.internal.ws.transport.http.WSHTTPConnection
    @PropertySet.Property({MessageContext.HTTP_REQUEST_HEADERS, Packet.INBOUND_TRANSPORT_HEADERS})
    @NotNull
    public Map<String, List<String>> getRequestHeaders() {
        return this.httpExchange.getRequestHeaders();
    }

    @Override // com.sun.xml.internal.ws.transport.http.WSHTTPConnection
    public String getRequestHeader(String headerName) {
        return this.httpExchange.getRequestHeaders().getFirst(headerName);
    }

    @Override // com.sun.xml.internal.ws.transport.http.WSHTTPConnection
    public void setResponseHeaders(Map<String, List<String>> headers) {
        Headers r2 = this.httpExchange.getResponseHeaders();
        r2.clear();
        for (Map.Entry<String, List<String>> entry : headers.entrySet()) {
            String name = entry.getKey();
            List<String> values = entry.getValue();
            if (!"Content-Length".equalsIgnoreCase(name) && !"Content-Type".equalsIgnoreCase(name)) {
                r2.put(name, (List<String>) new ArrayList(values));
            }
        }
    }

    @Override // com.sun.xml.internal.ws.transport.http.WSHTTPConnection
    public void setResponseHeader(String key, List<String> value) {
        this.httpExchange.getResponseHeaders().put(key, value);
    }

    @Override // com.sun.xml.internal.ws.transport.http.WSHTTPConnection
    public Set<String> getRequestHeaderNames() {
        return this.httpExchange.getRequestHeaders().keySet();
    }

    @Override // com.sun.xml.internal.ws.transport.http.WSHTTPConnection
    public List<String> getRequestHeaderValues(String headerName) {
        return this.httpExchange.getRequestHeaders().get((Object) headerName);
    }

    @Override // com.sun.xml.internal.ws.transport.http.WSHTTPConnection
    @PropertySet.Property({MessageContext.HTTP_RESPONSE_HEADERS, Packet.OUTBOUND_TRANSPORT_HEADERS})
    public Map<String, List<String>> getResponseHeaders() {
        return this.httpExchange.getResponseHeaders();
    }

    @Override // com.sun.xml.internal.ws.transport.http.WSHTTPConnection
    public void setContentTypeResponseHeader(@NotNull String value) {
        this.httpExchange.getResponseHeaders().set("Content-Type", value);
    }

    @Override // com.sun.xml.internal.ws.transport.http.WSHTTPConnection
    public void setStatus(int status) {
        this.status = status;
    }

    @Override // com.sun.xml.internal.ws.transport.http.WSHTTPConnection
    @PropertySet.Property({MessageContext.HTTP_RESPONSE_CODE})
    public int getStatus() {
        return this.status;
    }

    @Override // com.sun.xml.internal.ws.transport.http.WSHTTPConnection
    @NotNull
    public InputStream getInput() {
        if (this.in == null) {
            this.in = new LWHSInputStream(this.httpExchange.getRequestBody());
        }
        return this.in;
    }

    /* loaded from: rt.jar:com/sun/xml/internal/ws/transport/http/server/ServerConnectionImpl$LWHSInputStream.class */
    private static class LWHSInputStream extends FilterInputStream {
        boolean closed;
        boolean readAll;

        LWHSInputStream(InputStream in) {
            super(in);
        }

        void readAll() throws IOException {
            if (!this.closed && !this.readAll) {
                ReadAllStream all = new ReadAllStream();
                all.readAll(this.in, 4000000L);
                this.in.close();
                this.in = all;
                this.readAll = true;
            }
        }

        @Override // java.io.FilterInputStream, java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
        public void close() throws IOException {
            if (!this.closed) {
                readAll();
                super.close();
                this.closed = true;
            }
        }
    }

    @Override // com.sun.xml.internal.ws.transport.http.WSHTTPConnection
    @NotNull
    public OutputStream getOutput() throws IOException {
        if (this.out == null) {
            String lenHeader = this.httpExchange.getResponseHeaders().getFirst("Content-Length");
            int length = lenHeader != null ? Integer.parseInt(lenHeader) : 0;
            this.httpExchange.sendResponseHeaders(getStatus(), length);
            this.out = new FilterOutputStream(this.httpExchange.getResponseBody()) { // from class: com.sun.xml.internal.ws.transport.http.server.ServerConnectionImpl.1
                boolean closed;

                @Override // java.io.FilterOutputStream, java.io.OutputStream, java.io.Closeable, java.lang.AutoCloseable
                public void close() throws IOException {
                    if (!this.closed) {
                        this.closed = true;
                        ServerConnectionImpl.this.in.readAll();
                        try {
                            super.close();
                        } catch (IOException e2) {
                        }
                    }
                }

                @Override // java.io.FilterOutputStream, java.io.OutputStream
                public void write(byte[] buf, int start, int len) throws IOException {
                    this.out.write(buf, start, len);
                }
            };
        }
        return this.out;
    }

    @Override // com.sun.xml.internal.ws.transport.http.WSHTTPConnection
    @NotNull
    public WebServiceContextDelegate getWebServiceContextDelegate() {
        return this;
    }

    @Override // com.sun.xml.internal.ws.api.server.WebServiceContextDelegate
    public Principal getUserPrincipal(Packet request) {
        return this.httpExchange.getPrincipal();
    }

    @Override // com.sun.xml.internal.ws.api.server.WebServiceContextDelegate
    public boolean isUserInRole(Packet request, String role) {
        return false;
    }

    @Override // com.sun.xml.internal.ws.api.server.WebServiceContextDelegate
    @NotNull
    public String getEPRAddress(Packet request, WSEndpoint endpoint) {
        PortAddressResolver resolver = this.adapter.owner.createPortAddressResolver(getBaseAddress(), endpoint.getImplementationClass());
        String address = resolver.getAddressFor(endpoint.getServiceName(), endpoint.getPortName().getLocalPart());
        if (address == null) {
            throw new WebServiceException(WsservletMessages.SERVLET_NO_ADDRESS_AVAILABLE(endpoint.getPortName()));
        }
        return address;
    }

    @Override // com.sun.xml.internal.ws.api.server.WebServiceContextDelegate
    public String getWSDLAddress(@NotNull Packet request, @NotNull WSEndpoint endpoint) {
        String eprAddress = getEPRAddress(request, endpoint);
        if (this.adapter.getEndpoint().getPort() != null) {
            return eprAddress + "?wsdl";
        }
        return null;
    }

    @Override // com.sun.xml.internal.ws.transport.http.WSHTTPConnection
    public boolean isSecure() {
        return this.httpExchange instanceof HttpsExchange;
    }

    @Override // com.sun.xml.internal.ws.transport.http.WSHTTPConnection
    @PropertySet.Property({MessageContext.HTTP_REQUEST_METHOD})
    @NotNull
    public String getRequestMethod() {
        return this.httpExchange.getRequestMethod();
    }

    @Override // com.sun.xml.internal.ws.transport.http.WSHTTPConnection
    @PropertySet.Property({MessageContext.QUERY_STRING})
    public String getQueryString() {
        URI requestUri = this.httpExchange.getRequestURI();
        String query = requestUri.getQuery();
        if (query != null) {
            return query;
        }
        return null;
    }

    @Override // com.sun.xml.internal.ws.transport.http.WSHTTPConnection
    @PropertySet.Property({MessageContext.PATH_INFO})
    public String getPathInfo() {
        URI requestUri = this.httpExchange.getRequestURI();
        String reqPath = requestUri.getPath();
        String ctxtPath = this.httpExchange.getHttpContext().getPath();
        if (reqPath.length() > ctxtPath.length()) {
            return reqPath.substring(ctxtPath.length());
        }
        return null;
    }

    @PropertySet.Property({JAXWSProperties.HTTP_EXCHANGE})
    public HttpExchange getExchange() {
        return this.httpExchange;
    }

    @Override // com.sun.xml.internal.ws.transport.http.WSHTTPConnection
    @NotNull
    public String getBaseAddress() {
        StringBuilder strBuf = new StringBuilder();
        strBuf.append(this.httpExchange instanceof HttpsExchange ? "https" : "http");
        strBuf.append("://");
        String hostHeader = this.httpExchange.getRequestHeaders().getFirst("Host");
        if (hostHeader != null) {
            strBuf.append(hostHeader);
        } else {
            strBuf.append(this.httpExchange.getLocalAddress().getHostName());
            strBuf.append(CallSiteDescriptor.TOKEN_DELIMITER);
            strBuf.append(this.httpExchange.getLocalAddress().getPort());
        }
        return strBuf.toString();
    }

    @Override // com.sun.xml.internal.ws.transport.http.WSHTTPConnection
    public String getProtocol() {
        return this.httpExchange.getProtocol();
    }

    @Override // com.sun.xml.internal.ws.transport.http.WSHTTPConnection
    public void setContentLengthResponseHeader(int value) {
        this.httpExchange.getResponseHeaders().set("Content-Length", "" + value);
    }

    @Override // com.sun.xml.internal.ws.transport.http.WSHTTPConnection
    public String getRequestURI() {
        return this.httpExchange.getRequestURI().toString();
    }

    @Override // com.sun.xml.internal.ws.transport.http.WSHTTPConnection
    public String getRequestScheme() {
        return this.httpExchange instanceof HttpsExchange ? "https" : "http";
    }

    @Override // com.sun.xml.internal.ws.transport.http.WSHTTPConnection
    public String getServerName() {
        return this.httpExchange.getLocalAddress().getHostName();
    }

    @Override // com.sun.xml.internal.ws.transport.http.WSHTTPConnection
    public int getServerPort() {
        return this.httpExchange.getLocalAddress().getPort();
    }

    @Override // com.oracle.webservices.internal.api.message.BasePropertySet
    protected BasePropertySet.PropertyMap getPropertyMap() {
        return model;
    }
}
