package com.sun.xml.internal.ws.transport.http;

import com.oracle.webservices.internal.api.message.PropertySet;
import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import com.sun.org.apache.xerces.internal.xinclude.XIncludeHandler;
import com.sun.org.apache.xml.internal.serializer.SerializerConstants;
import com.sun.xml.internal.ws.api.Component;
import com.sun.xml.internal.ws.api.EndpointAddress;
import com.sun.xml.internal.ws.api.SOAPVersion;
import com.sun.xml.internal.ws.api.addressing.AddressingVersion;
import com.sun.xml.internal.ws.api.addressing.NonAnonymousResponseProcessor;
import com.sun.xml.internal.ws.api.ha.HaInfo;
import com.sun.xml.internal.ws.api.message.ExceptionHasMessage;
import com.sun.xml.internal.ws.api.message.Message;
import com.sun.xml.internal.ws.api.message.Packet;
import com.sun.xml.internal.ws.api.model.SEIModel;
import com.sun.xml.internal.ws.api.pipe.Codec;
import com.sun.xml.internal.ws.api.pipe.ContentType;
import com.sun.xml.internal.ws.api.server.AbstractServerAsyncTransport;
import com.sun.xml.internal.ws.api.server.Adapter;
import com.sun.xml.internal.ws.api.server.BoundEndpoint;
import com.sun.xml.internal.ws.api.server.DocumentAddressResolver;
import com.sun.xml.internal.ws.api.server.Module;
import com.sun.xml.internal.ws.api.server.PortAddressResolver;
import com.sun.xml.internal.ws.api.server.SDDocument;
import com.sun.xml.internal.ws.api.server.ServiceDefinition;
import com.sun.xml.internal.ws.api.server.TransportBackChannel;
import com.sun.xml.internal.ws.api.server.WSEndpoint;
import com.sun.xml.internal.ws.api.server.WebServiceContextDelegate;
import com.sun.xml.internal.ws.fault.SOAPFaultBuilder;
import com.sun.xml.internal.ws.model.CheckedExceptionImpl;
import com.sun.xml.internal.ws.resources.WsservletMessages;
import com.sun.xml.internal.ws.server.UnsupportedMediaException;
import com.sun.xml.internal.ws.util.ByteArrayBuffer;
import com.sun.xml.internal.ws.util.Pool;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.ws.Binding;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.http.HTTPBinding;
import org.icepdf.core.pobjects.annotations.FreeTextAnnotation;
import org.icepdf.core.util.PdfOps;

/* loaded from: rt.jar:com/sun/xml/internal/ws/transport/http/HttpAdapter.class */
public class HttpAdapter extends Adapter<HttpToolkit> {
    private static final Logger LOGGER;
    protected Map<String, SDDocument> wsdls;
    private Map<SDDocument, String> revWsdls;
    private ServiceDefinition serviceDefinition;
    public final HttpAdapterList<? extends HttpAdapter> owner;
    public final String urlPattern;
    protected boolean stickyCookie;
    protected boolean disableJreplicaCookie;
    public static final CompletionCallback NO_OP_COMPLETION_CALLBACK;
    public static volatile boolean dump;
    public static volatile int dump_threshold;
    public static volatile boolean publishStatusPage;
    static final /* synthetic */ boolean $assertionsDisabled;

    /* loaded from: rt.jar:com/sun/xml/internal/ws/transport/http/HttpAdapter$CompletionCallback.class */
    public interface CompletionCallback {
        void onCompletion();
    }

    static {
        $assertionsDisabled = !HttpAdapter.class.desiredAssertionStatus();
        LOGGER = Logger.getLogger(HttpAdapter.class.getName());
        NO_OP_COMPLETION_CALLBACK = new CompletionCallback() { // from class: com.sun.xml.internal.ws.transport.http.HttpAdapter.2
            @Override // com.sun.xml.internal.ws.transport.http.HttpAdapter.CompletionCallback
            public void onCompletion() {
            }
        };
        dump = false;
        dump_threshold = 4096;
        publishStatusPage = true;
        try {
            dump = Boolean.getBoolean(HttpAdapter.class.getName() + ".dump");
        } catch (SecurityException e2) {
            if (LOGGER.isLoggable(Level.CONFIG)) {
                LOGGER.log(Level.CONFIG, "Cannot read ''{0}'' property, using defaults.", new Object[]{HttpAdapter.class.getName() + ".dump"});
            }
        }
        try {
            dump_threshold = Integer.getInteger(HttpAdapter.class.getName() + ".dumpTreshold", 4096).intValue();
        } catch (SecurityException e3) {
            if (LOGGER.isLoggable(Level.CONFIG)) {
                LOGGER.log(Level.CONFIG, "Cannot read ''{0}'' property, using defaults.", new Object[]{HttpAdapter.class.getName() + ".dumpTreshold"});
            }
        }
        try {
            setPublishStatus(Boolean.getBoolean(HttpAdapter.class.getName() + ".publishStatusPage"));
        } catch (SecurityException e4) {
            if (LOGGER.isLoggable(Level.CONFIG)) {
                LOGGER.log(Level.CONFIG, "Cannot read ''{0}'' property, using defaults.", new Object[]{HttpAdapter.class.getName() + ".publishStatusPage"});
            }
        }
    }

    public static HttpAdapter createAlone(WSEndpoint endpoint) {
        return new DummyList().createAdapter("", "", (WSEndpoint<?>) endpoint);
    }

    protected HttpAdapter(WSEndpoint endpoint, HttpAdapterList<? extends HttpAdapter> owner) {
        this(endpoint, owner, null);
    }

    protected HttpAdapter(WSEndpoint endpoint, HttpAdapterList<? extends HttpAdapter> owner, String urlPattern) {
        super(endpoint);
        this.serviceDefinition = null;
        this.disableJreplicaCookie = false;
        this.owner = owner;
        this.urlPattern = urlPattern;
        initWSDLMap(endpoint.getServiceDefinition());
    }

    public ServiceDefinition getServiceDefinition() {
        return this.serviceDefinition;
    }

    public final void initWSDLMap(ServiceDefinition sdef) {
        this.serviceDefinition = sdef;
        if (sdef == null) {
            this.wsdls = Collections.emptyMap();
            this.revWsdls = Collections.emptyMap();
            return;
        }
        this.wsdls = new HashMap();
        Map<String, SDDocument> systemIds = new TreeMap<>();
        for (SDDocument sdd : sdef) {
            if (sdd == sdef.getPrimary()) {
                this.wsdls.put("wsdl", sdd);
                this.wsdls.put("WSDL", sdd);
            } else {
                systemIds.put(sdd.getURL().toString(), sdd);
            }
        }
        int wsdlnum = 1;
        int xsdnum = 1;
        Iterator<Map.Entry<String, SDDocument>> it = systemIds.entrySet().iterator();
        while (it.hasNext()) {
            SDDocument sdd2 = it.next().getValue();
            if (sdd2.isWSDL()) {
                int i2 = wsdlnum;
                wsdlnum++;
                this.wsdls.put("wsdl=" + i2, sdd2);
            }
            if (sdd2.isSchema()) {
                int i3 = xsdnum;
                xsdnum++;
                this.wsdls.put("xsd=" + i3, sdd2);
            }
        }
        this.revWsdls = new HashMap();
        for (Map.Entry<String, SDDocument> e2 : this.wsdls.entrySet()) {
            if (!e2.getKey().equals("WSDL")) {
                this.revWsdls.put(e2.getValue(), e2.getKey());
            }
        }
    }

    public String getValidPath() {
        if (this.urlPattern.endsWith("/*")) {
            return this.urlPattern.substring(0, this.urlPattern.length() - 2);
        }
        return this.urlPattern;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.sun.xml.internal.ws.api.server.Adapter
    public HttpToolkit createToolkit() {
        return new HttpToolkit();
    }

    public void handle(@NotNull WSHTTPConnection connection) throws IOException {
        if (handleGet(connection)) {
            return;
        }
        Pool<HttpToolkit> currentPool = getPool();
        HttpToolkit tk = currentPool.take();
        try {
            tk.handle(connection);
            currentPool.recycle(tk);
        } catch (Throwable th) {
            currentPool.recycle(tk);
            throw th;
        }
    }

    public boolean handleGet(@NotNull WSHTTPConnection connection) throws IOException {
        if (connection.getRequestMethod().equals("GET")) {
            for (Component c2 : this.endpoint.getComponents()) {
                HttpMetadataPublisher spi = (HttpMetadataPublisher) c2.getSPI(HttpMetadataPublisher.class);
                if (spi != null && spi.handleMetadataRequest(this, connection)) {
                    return true;
                }
            }
            if (isMetadataQuery(connection.getQueryString())) {
                publishWSDL(connection);
                return true;
            }
            Binding binding = getEndpoint().getBinding();
            if (!(binding instanceof HTTPBinding)) {
                writeWebServicesHtmlPage(connection);
                return true;
            }
            return false;
        }
        if (connection.getRequestMethod().equals("HEAD")) {
            connection.getInput().close();
            Binding binding2 = getEndpoint().getBinding();
            if (isMetadataQuery(connection.getQueryString())) {
                SDDocument doc = this.wsdls.get(connection.getQueryString());
                connection.setStatus(doc != null ? 200 : 404);
                connection.getOutput().close();
                connection.close();
                return true;
            }
            if (!(binding2 instanceof HTTPBinding)) {
                connection.setStatus(404);
                connection.getOutput().close();
                connection.close();
                return true;
            }
            return false;
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Packet decodePacket(@NotNull WSHTTPConnection con, @NotNull Codec codec) throws IOException {
        String ct = con.getRequestHeader("Content-Type");
        InputStream in = con.getInput();
        Packet packet = new Packet();
        packet.soapAction = fixQuotesAroundSoapAction(con.getRequestHeader("SOAPAction"));
        packet.wasTransportSecure = con.isSecure();
        packet.acceptableMimeTypes = con.getRequestHeader(XIncludeHandler.HTTP_ACCEPT);
        packet.addSatellite(con);
        addSatellites(packet);
        packet.isAdapterDeliversNonAnonymousResponse = true;
        packet.component = this;
        packet.transportBackChannel = new Oneway(con);
        packet.webServiceContextDelegate = con.getWebServiceContextDelegate();
        packet.setState(Packet.State.ServerRequest);
        if (dump || LOGGER.isLoggable(Level.FINER)) {
            ByteArrayBuffer buf = new ByteArrayBuffer();
            buf.write(in);
            in.close();
            dump(buf, "HTTP request", con.getRequestHeaders());
            in = buf.newInputStream();
        }
        codec.decode(in, ct, packet);
        return packet;
    }

    protected void addSatellites(Packet packet) {
    }

    public static String fixQuotesAroundSoapAction(String soapAction) {
        if (soapAction != null && (!soapAction.startsWith(PdfOps.DOUBLE_QUOTE__TOKEN) || !soapAction.endsWith(PdfOps.DOUBLE_QUOTE__TOKEN))) {
            if (LOGGER.isLoggable(Level.INFO)) {
                LOGGER.log(Level.INFO, "Received WS-I BP non-conformant Unquoted SoapAction HTTP header: {0}", soapAction);
            }
            String fixedSoapAction = soapAction;
            if (!soapAction.startsWith(PdfOps.DOUBLE_QUOTE__TOKEN)) {
                fixedSoapAction = PdfOps.DOUBLE_QUOTE__TOKEN + fixedSoapAction;
            }
            if (!soapAction.endsWith(PdfOps.DOUBLE_QUOTE__TOKEN)) {
                fixedSoapAction = fixedSoapAction + PdfOps.DOUBLE_QUOTE__TOKEN;
            }
            return fixedSoapAction;
        }
        return soapAction;
    }

    protected NonAnonymousResponseProcessor getNonAnonymousResponseProcessor() {
        return NonAnonymousResponseProcessor.getDefault();
    }

    protected void writeClientError(int connStatus, @NotNull OutputStream os, @NotNull Packet packet) throws IOException {
    }

    private boolean isClientErrorStatus(int connStatus) {
        return connStatus == 403;
    }

    private boolean isNonAnonymousUri(EndpointAddress addr) {
        return (addr == null || addr.toString().equals(AddressingVersion.W3C.anonymousUri) || addr.toString().equals(AddressingVersion.MEMBER.anonymousUri)) ? false : true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void encodePacket(@NotNull Packet packet, @NotNull WSHTTPConnection con, @NotNull Codec codec) throws IOException {
        if (isNonAnonymousUri(packet.endpointAddress) && packet.getMessage() != null) {
            try {
                packet = getNonAnonymousResponseProcessor().process(packet);
            } catch (RuntimeException re) {
                SOAPVersion soapVersion = packet.getBinding().getSOAPVersion();
                Message faultMsg = SOAPFaultBuilder.createSOAPFaultMessage(soapVersion, (CheckedExceptionImpl) null, re);
                packet = packet.createServerResponse(faultMsg, packet.endpoint.getPort(), (SEIModel) null, packet.endpoint.getBinding());
            }
        }
        if (con.isClosed()) {
            return;
        }
        Message responseMessage = packet.getMessage();
        addStickyCookie(con);
        addReplicaCookie(con, packet);
        if (responseMessage == null) {
            if (!con.isClosed()) {
                if (con.getStatus() == 0) {
                    con.setStatus(202);
                }
                OutputStream os = con.getProtocol().contains(SerializerConstants.XMLVERSION11) ? con.getOutput() : new Http10OutputStream(con);
                if (dump || LOGGER.isLoggable(Level.FINER)) {
                    ByteArrayBuffer buf = new ByteArrayBuffer();
                    codec.encode(packet, buf);
                    dump(buf, "HTTP response " + con.getStatus(), con.getResponseHeaders());
                    buf.writeTo(os);
                } else {
                    codec.encode(packet, os);
                }
                try {
                    os.close();
                    return;
                } catch (IOException e2) {
                    throw new WebServiceException(e2);
                }
            }
            return;
        }
        if (con.getStatus() == 0) {
            con.setStatus(responseMessage.isFault() ? 500 : 200);
        }
        if (isClientErrorStatus(con.getStatus())) {
            OutputStream os2 = con.getOutput();
            if (dump || LOGGER.isLoggable(Level.FINER)) {
                ByteArrayBuffer buf2 = new ByteArrayBuffer();
                writeClientError(con.getStatus(), buf2, packet);
                dump(buf2, "HTTP response " + con.getStatus(), con.getResponseHeaders());
                buf2.writeTo(os2);
            } else {
                writeClientError(con.getStatus(), os2, packet);
            }
            os2.close();
            return;
        }
        ContentType contentType = codec.getStaticContentType(packet);
        if (contentType != null) {
            con.setContentTypeResponseHeader(contentType.getContentType());
            OutputStream os3 = con.getProtocol().contains(SerializerConstants.XMLVERSION11) ? con.getOutput() : new Http10OutputStream(con);
            if (dump || LOGGER.isLoggable(Level.FINER)) {
                ByteArrayBuffer buf3 = new ByteArrayBuffer();
                codec.encode(packet, buf3);
                dump(buf3, "HTTP response " + con.getStatus(), con.getResponseHeaders());
                buf3.writeTo(os3);
            } else {
                codec.encode(packet, os3);
            }
            os3.close();
            return;
        }
        ByteArrayBuffer buf4 = new ByteArrayBuffer();
        con.setContentTypeResponseHeader(codec.encode(packet, buf4).getContentType());
        if (dump || LOGGER.isLoggable(Level.FINER)) {
            dump(buf4, "HTTP response " + con.getStatus(), con.getResponseHeaders());
        }
        OutputStream os4 = con.getOutput();
        buf4.writeTo(os4);
        os4.close();
    }

    private void addStickyCookie(WSHTTPConnection con) {
        String proxyJroute;
        if (!this.stickyCookie || (proxyJroute = con.getRequestHeader("proxy-jroute")) == null) {
            return;
        }
        String jrouteId = con.getCookie("JROUTE");
        if (jrouteId == null || !jrouteId.equals(proxyJroute)) {
            con.setCookie("JROUTE", proxyJroute);
        }
    }

    private void addReplicaCookie(WSHTTPConnection con, Packet packet) {
        if (this.stickyCookie) {
            HaInfo haInfo = null;
            if (packet.supports(Packet.HA_INFO)) {
                haInfo = (HaInfo) packet.get(Packet.HA_INFO);
            }
            if (haInfo != null) {
                con.setCookie("METRO_KEY", haInfo.getKey());
                if (!this.disableJreplicaCookie) {
                    con.setCookie("JREPLICA", haInfo.getReplicaInstance());
                }
            }
        }
    }

    public void invokeAsync(WSHTTPConnection con) throws IOException {
        invokeAsync(con, NO_OP_COMPLETION_CALLBACK);
    }

    public void invokeAsync(final WSHTTPConnection con, final CompletionCallback callback) throws IOException {
        if (handleGet(con)) {
            callback.onCompletion();
            return;
        }
        final Pool<HttpToolkit> currentPool = getPool();
        final HttpToolkit tk = currentPool.take();
        try {
            Packet request = decodePacket(con, tk.codec);
            this.endpoint.process(request, new WSEndpoint.CompletionCallback() { // from class: com.sun.xml.internal.ws.transport.http.HttpAdapter.1
                @Override // com.sun.xml.internal.ws.api.server.WSEndpoint.CompletionCallback
                public void onCompletion(@NotNull Packet response) {
                    try {
                        try {
                            HttpAdapter.this.encodePacket(response, con, tk.codec);
                        } catch (IOException ioe) {
                            HttpAdapter.LOGGER.log(Level.SEVERE, ioe.getMessage(), (Throwable) ioe);
                        }
                        currentPool.recycle(tk);
                    } finally {
                        con.close();
                        callback.onCompletion();
                    }
                }
            }, null);
        } catch (ExceptionHasMessage e2) {
            LOGGER.log(Level.SEVERE, e2.getMessage(), (Throwable) e2);
            Packet response = new Packet();
            response.setMessage(e2.getFaultMessage());
            encodePacket(response, con, tk.codec);
            currentPool.recycle(tk);
            con.close();
            callback.onCompletion();
        } catch (UnsupportedMediaException e3) {
            LOGGER.log(Level.SEVERE, e3.getMessage(), (Throwable) e3);
            Packet response2 = new Packet();
            con.setStatus(415);
            encodePacket(response2, con, tk.codec);
            currentPool.recycle(tk);
            con.close();
            callback.onCompletion();
        }
    }

    /* loaded from: rt.jar:com/sun/xml/internal/ws/transport/http/HttpAdapter$AsyncTransport.class */
    final class AsyncTransport extends AbstractServerAsyncTransport<WSHTTPConnection> {
        public AsyncTransport() {
            super(HttpAdapter.this.endpoint);
        }

        public void handleAsync(WSHTTPConnection con) throws IOException {
            super.handle(con);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.sun.xml.internal.ws.api.server.AbstractServerAsyncTransport
        public void encodePacket(WSHTTPConnection con, @NotNull Packet packet, @NotNull Codec codec) throws IOException {
            HttpAdapter.this.encodePacket(packet, con, codec);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.sun.xml.internal.ws.api.server.AbstractServerAsyncTransport
        @Nullable
        public String getAcceptableMimeTypes(WSHTTPConnection con) {
            return null;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.sun.xml.internal.ws.api.server.AbstractServerAsyncTransport
        @Nullable
        public TransportBackChannel getTransportBackChannel(WSHTTPConnection con) {
            return new Oneway(con);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.sun.xml.internal.ws.api.server.AbstractServerAsyncTransport
        @NotNull
        public PropertySet getPropertySet(WSHTTPConnection con) {
            return con;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.sun.xml.internal.ws.api.server.AbstractServerAsyncTransport
        @NotNull
        public WebServiceContextDelegate getWebServiceContextDelegate(WSHTTPConnection con) {
            return con.getWebServiceContextDelegate();
        }
    }

    /* loaded from: rt.jar:com/sun/xml/internal/ws/transport/http/HttpAdapter$Oneway.class */
    static final class Oneway implements TransportBackChannel {
        WSHTTPConnection con;
        boolean closed;

        Oneway(WSHTTPConnection con) {
            this.con = con;
        }

        @Override // com.sun.xml.internal.ws.api.server.TransportBackChannel
        public void close() {
            if (!this.closed) {
                this.closed = true;
                if (this.con.getStatus() == 0) {
                    this.con.setStatus(202);
                }
                OutputStream output = null;
                try {
                    output = this.con.getOutput();
                } catch (IOException e2) {
                }
                if (HttpAdapter.dump || HttpAdapter.LOGGER.isLoggable(Level.FINER)) {
                    try {
                        ByteArrayBuffer buf = new ByteArrayBuffer();
                        HttpAdapter.dump(buf, "HTTP response " + this.con.getStatus(), this.con.getResponseHeaders());
                    } catch (Exception e3) {
                        throw new WebServiceException(e3.toString(), e3);
                    }
                }
                if (output != null) {
                    try {
                        output.close();
                    } catch (IOException e4) {
                        throw new WebServiceException(e4);
                    }
                }
                this.con.close();
            }
        }
    }

    /* loaded from: rt.jar:com/sun/xml/internal/ws/transport/http/HttpAdapter$HttpToolkit.class */
    final class HttpToolkit extends Adapter.Toolkit {
        HttpToolkit() {
            super();
        }

        public void handle(WSHTTPConnection con) throws IOException {
            Packet packet;
            boolean invoke = false;
            try {
                try {
                    packet = HttpAdapter.this.decodePacket(con, this.codec);
                    invoke = true;
                } catch (Exception e2) {
                    packet = new Packet();
                    if (e2 instanceof ExceptionHasMessage) {
                        HttpAdapter.LOGGER.log(Level.SEVERE, e2.getMessage(), (Throwable) e2);
                        packet.setMessage(((ExceptionHasMessage) e2).getFaultMessage());
                    } else if (e2 instanceof UnsupportedMediaException) {
                        HttpAdapter.LOGGER.log(Level.SEVERE, e2.getMessage(), (Throwable) e2);
                        con.setStatus(415);
                    } else {
                        HttpAdapter.LOGGER.log(Level.SEVERE, e2.getMessage(), (Throwable) e2);
                        con.setStatus(500);
                    }
                }
                if (invoke) {
                    try {
                        packet = this.head.process(packet, con.getWebServiceContextDelegate(), packet.transportBackChannel);
                    } catch (Throwable e3) {
                        HttpAdapter.LOGGER.log(Level.SEVERE, e3.getMessage(), e3);
                        if (!con.isClosed()) {
                            HttpAdapter.this.writeInternalServerError(con);
                        }
                        if (!con.isClosed()) {
                            if (HttpAdapter.LOGGER.isLoggable(Level.FINE)) {
                                HttpAdapter.LOGGER.log(Level.FINE, "Closing HTTP Connection with status: {0}", Integer.valueOf(con.getStatus()));
                            }
                            con.close();
                            return;
                        }
                        return;
                    }
                }
                HttpAdapter.this.encodePacket(packet, con, this.codec);
                if (!con.isClosed()) {
                    if (HttpAdapter.LOGGER.isLoggable(Level.FINE)) {
                        HttpAdapter.LOGGER.log(Level.FINE, "Closing HTTP Connection with status: {0}", Integer.valueOf(con.getStatus()));
                    }
                    con.close();
                }
            } catch (Throwable th) {
                if (!con.isClosed()) {
                    if (HttpAdapter.LOGGER.isLoggable(Level.FINE)) {
                        HttpAdapter.LOGGER.log(Level.FINE, "Closing HTTP Connection with status: {0}", Integer.valueOf(con.getStatus()));
                    }
                    con.close();
                }
                throw th;
            }
        }
    }

    private boolean isMetadataQuery(String query) {
        return query != null && (query.equals("WSDL") || query.startsWith("wsdl") || query.startsWith("xsd="));
    }

    public void publishWSDL(@NotNull WSHTTPConnection con) throws IOException {
        con.getInput().close();
        SDDocument doc = this.wsdls.get(con.getQueryString());
        if (doc == null) {
            writeNotFoundErrorPage(con, "Invalid Request");
            return;
        }
        con.setStatus(200);
        con.setContentTypeResponseHeader("text/xml;charset=utf-8");
        OutputStream os = con.getProtocol().contains(SerializerConstants.XMLVERSION11) ? con.getOutput() : new Http10OutputStream(con);
        PortAddressResolver portAddressResolver = getPortAddressResolver(con.getBaseAddress());
        DocumentAddressResolver resolver = getDocumentAddressResolver(portAddressResolver);
        doc.writeTo(portAddressResolver, resolver, os);
        os.close();
    }

    public PortAddressResolver getPortAddressResolver(String baseAddress) {
        return this.owner.createPortAddressResolver(baseAddress, this.endpoint.getImplementationClass());
    }

    public DocumentAddressResolver getDocumentAddressResolver(PortAddressResolver portAddressResolver) {
        final String address = portAddressResolver.getAddressFor(this.endpoint.getServiceName(), this.endpoint.getPortName().getLocalPart());
        if ($assertionsDisabled || address != null) {
            return new DocumentAddressResolver() { // from class: com.sun.xml.internal.ws.transport.http.HttpAdapter.3
                static final /* synthetic */ boolean $assertionsDisabled;

                static {
                    $assertionsDisabled = !HttpAdapter.class.desiredAssertionStatus();
                }

                @Override // com.sun.xml.internal.ws.api.server.DocumentAddressResolver
                public String getRelativeAddressFor(@NotNull SDDocument current, @NotNull SDDocument referenced) {
                    if ($assertionsDisabled || HttpAdapter.this.revWsdls.containsKey(referenced)) {
                        return address + '?' + ((String) HttpAdapter.this.revWsdls.get(referenced));
                    }
                    throw new AssertionError();
                }
            };
        }
        throw new AssertionError();
    }

    /* loaded from: rt.jar:com/sun/xml/internal/ws/transport/http/HttpAdapter$Http10OutputStream.class */
    private static final class Http10OutputStream extends ByteArrayBuffer {
        private final WSHTTPConnection con;

        Http10OutputStream(WSHTTPConnection con) {
            this.con = con;
        }

        @Override // com.sun.xml.internal.ws.util.ByteArrayBuffer, java.io.OutputStream, java.io.Closeable, java.lang.AutoCloseable
        public void close() throws IOException {
            super.close();
            this.con.setContentLengthResponseHeader(size());
            OutputStream os = this.con.getOutput();
            writeTo(os);
            os.close();
        }
    }

    private void writeNotFoundErrorPage(WSHTTPConnection con, String message) throws IOException {
        con.setStatus(404);
        con.setContentTypeResponseHeader("text/html; charset=utf-8");
        PrintWriter out = new PrintWriter(new OutputStreamWriter(con.getOutput(), "UTF-8"));
        out.println("<html>");
        out.println("<head><title>");
        out.println(WsservletMessages.SERVLET_HTML_TITLE());
        out.println("</title></head>");
        out.println("<body>");
        out.println(WsservletMessages.SERVLET_HTML_NOT_FOUND(message));
        out.println(FreeTextAnnotation.BODY_END);
        out.println("</html>");
        out.close();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void writeInternalServerError(WSHTTPConnection con) throws IOException {
        con.setStatus(500);
        con.getOutput().close();
    }

    /* loaded from: rt.jar:com/sun/xml/internal/ws/transport/http/HttpAdapter$DummyList.class */
    private static final class DummyList extends HttpAdapterList<HttpAdapter> {
        private DummyList() {
        }

        @Override // com.sun.xml.internal.ws.transport.http.HttpAdapterList
        protected HttpAdapter createHttpAdapter(String name, String urlPattern, WSEndpoint<?> endpoint) {
            return new HttpAdapter(endpoint, this, urlPattern);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void dump(ByteArrayBuffer buf, String caption, Map<String, List<String>> headers) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintWriter pw = new PrintWriter((OutputStream) baos, true);
        pw.println("---[" + caption + "]---");
        if (headers != null) {
            for (Map.Entry<String, List<String>> header : headers.entrySet()) {
                if (header.getValue().isEmpty()) {
                    pw.println(header.getValue());
                } else {
                    for (String value : header.getValue()) {
                        pw.println(header.getKey() + ": " + value);
                    }
                }
            }
        }
        if (buf.size() > dump_threshold) {
            byte[] b2 = buf.getRawData();
            baos.write(b2, 0, dump_threshold);
            pw.println();
            pw.println(WsservletMessages.MESSAGE_TOO_LONG(HttpAdapter.class.getName() + ".dumpTreshold"));
        } else {
            buf.writeTo(baos);
        }
        pw.println("--------------------");
        String msg = baos.toString();
        if (dump) {
            System.out.println(msg);
        }
        if (LOGGER.isLoggable(Level.FINER)) {
            LOGGER.log(Level.FINER, msg);
        }
    }

    private void writeWebServicesHtmlPage(WSHTTPConnection con) throws IOException {
        if (!publishStatusPage) {
            return;
        }
        con.getInput().close();
        con.setStatus(200);
        con.setContentTypeResponseHeader("text/html; charset=utf-8");
        PrintWriter out = new PrintWriter(new OutputStreamWriter(con.getOutput(), "UTF-8"));
        out.println("<html>");
        out.println("<head><title>");
        out.println(WsservletMessages.SERVLET_HTML_TITLE());
        out.println("</title></head>");
        out.println("<body>");
        out.println(WsservletMessages.SERVLET_HTML_TITLE_2());
        Module module = (Module) getEndpoint().getContainer().getSPI(Module.class);
        List<BoundEndpoint> endpoints = Collections.emptyList();
        if (module != null) {
            endpoints = module.getBoundEndpoints();
        }
        if (endpoints.isEmpty()) {
            out.println(WsservletMessages.SERVLET_HTML_NO_INFO_AVAILABLE());
        } else {
            out.println("<table width='100%' border='1'>");
            out.println("<tr>");
            out.println("<td>");
            out.println(WsservletMessages.SERVLET_HTML_COLUMN_HEADER_PORT_NAME());
            out.println("</td>");
            out.println("<td>");
            out.println(WsservletMessages.SERVLET_HTML_COLUMN_HEADER_INFORMATION());
            out.println("</td>");
            out.println("</tr>");
            for (BoundEndpoint a2 : endpoints) {
                String endpointAddress = a2.getAddress(con.getBaseAddress()).toString();
                out.println("<tr>");
                out.println("<td>");
                out.println(WsservletMessages.SERVLET_HTML_ENDPOINT_TABLE(a2.getEndpoint().getServiceName(), a2.getEndpoint().getPortName()));
                out.println("</td>");
                out.println("<td>");
                out.println(WsservletMessages.SERVLET_HTML_INFORMATION_TABLE(endpointAddress, a2.getEndpoint().getImplementationClass().getName()));
                out.println("</td>");
                out.println("</tr>");
            }
            out.println("</table>");
        }
        out.println(FreeTextAnnotation.BODY_END);
        out.println("</html>");
        out.close();
    }

    public static synchronized void setPublishStatus(boolean publish) {
        publishStatusPage = publish;
    }

    public static void setDump(boolean dumpMessages) {
        dump = dumpMessages;
    }
}
