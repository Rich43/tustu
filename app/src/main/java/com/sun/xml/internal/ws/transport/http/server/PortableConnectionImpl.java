package com.sun.xml.internal.ws.transport.http.server;

import com.oracle.webservices.internal.api.message.BasePropertySet;
import com.oracle.webservices.internal.api.message.PropertySet;
import com.sun.istack.internal.NotNull;
import com.sun.xml.internal.ws.api.message.Packet;
import com.sun.xml.internal.ws.api.server.PortAddressResolver;
import com.sun.xml.internal.ws.api.server.WSEndpoint;
import com.sun.xml.internal.ws.api.server.WebServiceContextDelegate;
import com.sun.xml.internal.ws.developer.JAXWSProperties;
import com.sun.xml.internal.ws.resources.WsservletMessages;
import com.sun.xml.internal.ws.transport.http.HttpAdapter;
import com.sun.xml.internal.ws.transport.http.WSHTTPConnection;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.spi.http.HttpExchange;
import jdk.internal.dynalink.CallSiteDescriptor;

/* loaded from: rt.jar:com/sun/xml/internal/ws/transport/http/server/PortableConnectionImpl.class */
final class PortableConnectionImpl extends WSHTTPConnection implements WebServiceContextDelegate {
    private final HttpExchange httpExchange;
    private int status;
    private final HttpAdapter adapter;
    private boolean outputWritten;
    private static final BasePropertySet.PropertyMap model;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !PortableConnectionImpl.class.desiredAssertionStatus();
        model = parse(PortableConnectionImpl.class);
    }

    public PortableConnectionImpl(@NotNull HttpAdapter adapter, @NotNull HttpExchange httpExchange) {
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
        return this.httpExchange.getRequestHeader(headerName);
    }

    @Override // com.sun.xml.internal.ws.transport.http.WSHTTPConnection
    public void setResponseHeaders(Map<String, List<String>> headers) {
        Map<String, List<String>> r2 = this.httpExchange.getResponseHeaders();
        r2.clear();
        for (Map.Entry<String, List<String>> entry : headers.entrySet()) {
            String name = entry.getKey();
            List<String> values = entry.getValue();
            if (!name.equalsIgnoreCase("Content-Length") && !name.equalsIgnoreCase("Content-Type")) {
                r2.put(name, new ArrayList(values));
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
        return this.httpExchange.getRequestHeaders().get(headerName);
    }

    @Override // com.sun.xml.internal.ws.transport.http.WSHTTPConnection
    @PropertySet.Property({MessageContext.HTTP_RESPONSE_HEADERS, Packet.OUTBOUND_TRANSPORT_HEADERS})
    public Map<String, List<String>> getResponseHeaders() {
        return this.httpExchange.getResponseHeaders();
    }

    @Override // com.sun.xml.internal.ws.transport.http.WSHTTPConnection
    public void setContentTypeResponseHeader(@NotNull String value) {
        this.httpExchange.addResponseHeader("Content-Type", value);
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
    public InputStream getInput() throws IOException {
        return this.httpExchange.getRequestBody();
    }

    @Override // com.sun.xml.internal.ws.transport.http.WSHTTPConnection
    @NotNull
    public OutputStream getOutput() throws IOException {
        if (!$assertionsDisabled && this.outputWritten) {
            throw new AssertionError();
        }
        this.outputWritten = true;
        this.httpExchange.setStatus(getStatus());
        return this.httpExchange.getResponseBody();
    }

    @Override // com.sun.xml.internal.ws.transport.http.WSHTTPConnection
    @NotNull
    public WebServiceContextDelegate getWebServiceContextDelegate() {
        return this;
    }

    @Override // com.sun.xml.internal.ws.api.server.WebServiceContextDelegate
    public Principal getUserPrincipal(Packet request) {
        return this.httpExchange.getUserPrincipal();
    }

    @Override // com.sun.xml.internal.ws.api.server.WebServiceContextDelegate
    public boolean isUserInRole(Packet request, String role) {
        return this.httpExchange.isUserInRole(role);
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

    @PropertySet.Property({MessageContext.SERVLET_CONTEXT})
    public Object getServletContext() {
        return this.httpExchange.getAttribute(MessageContext.SERVLET_CONTEXT);
    }

    @PropertySet.Property({MessageContext.SERVLET_RESPONSE})
    public Object getServletResponse() {
        return this.httpExchange.getAttribute(MessageContext.SERVLET_RESPONSE);
    }

    @PropertySet.Property({MessageContext.SERVLET_REQUEST})
    public Object getServletRequest() {
        return this.httpExchange.getAttribute(MessageContext.SERVLET_REQUEST);
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
        return this.httpExchange.getScheme().equals("https");
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
        return this.httpExchange.getQueryString();
    }

    @Override // com.sun.xml.internal.ws.transport.http.WSHTTPConnection
    @PropertySet.Property({MessageContext.PATH_INFO})
    public String getPathInfo() {
        return this.httpExchange.getPathInfo();
    }

    @PropertySet.Property({JAXWSProperties.HTTP_EXCHANGE})
    public HttpExchange getExchange() {
        return this.httpExchange;
    }

    @Override // com.sun.xml.internal.ws.transport.http.WSHTTPConnection
    @NotNull
    public String getBaseAddress() {
        return this.httpExchange.getScheme() + "://" + this.httpExchange.getLocalAddress().getHostName() + CallSiteDescriptor.TOKEN_DELIMITER + this.httpExchange.getLocalAddress().getPort() + this.httpExchange.getContextPath();
    }

    @Override // com.sun.xml.internal.ws.transport.http.WSHTTPConnection
    public String getProtocol() {
        return this.httpExchange.getProtocol();
    }

    @Override // com.sun.xml.internal.ws.transport.http.WSHTTPConnection
    public void setContentLengthResponseHeader(int value) {
        this.httpExchange.addResponseHeader("Content-Length", "" + value);
    }

    @Override // com.sun.xml.internal.ws.transport.http.WSHTTPConnection
    public String getRequestURI() {
        return this.httpExchange.getRequestURI().toString();
    }

    @Override // com.sun.xml.internal.ws.transport.http.WSHTTPConnection
    public String getRequestScheme() {
        return this.httpExchange.getScheme();
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
