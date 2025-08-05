package com.sun.xml.internal.ws.transport.http.client;

import com.sun.glass.ui.Clipboard;
import com.sun.istack.internal.NotNull;
import com.sun.org.apache.xerces.internal.xinclude.XIncludeHandler;
import com.sun.xml.internal.ws.api.SOAPVersion;
import com.sun.xml.internal.ws.api.WSBinding;
import com.sun.xml.internal.ws.api.ha.StickyFeature;
import com.sun.xml.internal.ws.api.message.Packet;
import com.sun.xml.internal.ws.api.pipe.Codec;
import com.sun.xml.internal.ws.api.pipe.ContentType;
import com.sun.xml.internal.ws.api.pipe.NextAction;
import com.sun.xml.internal.ws.api.pipe.TubeCloner;
import com.sun.xml.internal.ws.api.pipe.helper.AbstractTubeImpl;
import com.sun.xml.internal.ws.client.ClientTransportException;
import com.sun.xml.internal.ws.developer.HttpConfigFeature;
import com.sun.xml.internal.ws.resources.ClientMessages;
import com.sun.xml.internal.ws.resources.WsservletMessages;
import com.sun.xml.internal.ws.transport.Headers;
import com.sun.xml.internal.ws.transport.http.HttpAdapter;
import com.sun.xml.internal.ws.util.ByteArrayBuffer;
import com.sun.xml.internal.ws.util.RuntimeVersion;
import com.sun.xml.internal.ws.util.StreamUtils;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.CookieHandler;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.DatatypeConverter;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.WebServiceFeature;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.soap.SOAPBinding;
import jdk.internal.dynalink.CallSiteDescriptor;

/* loaded from: rt.jar:com/sun/xml/internal/ws/transport/http/client/HttpTransportPipe.class */
public class HttpTransportPipe extends AbstractTubeImpl {
    private static final List<String> USER_AGENT = Collections.singletonList(RuntimeVersion.VERSION.toString());
    private static final Logger LOGGER = Logger.getLogger(HttpTransportPipe.class.getName());
    public static boolean dump;
    private final Codec codec;
    private final WSBinding binding;
    private final CookieHandler cookieJar;
    private final boolean sticky;

    static {
        boolean b2;
        try {
            b2 = Boolean.getBoolean(HttpTransportPipe.class.getName() + ".dump");
        } catch (Throwable th) {
            b2 = false;
        }
        dump = b2;
    }

    public HttpTransportPipe(Codec codec, WSBinding binding) {
        this.codec = codec;
        this.binding = binding;
        this.sticky = isSticky(binding);
        HttpConfigFeature configFeature = (HttpConfigFeature) binding.getFeature(HttpConfigFeature.class);
        this.cookieJar = (configFeature == null ? new HttpConfigFeature() : configFeature).getCookieHandler();
    }

    private static boolean isSticky(WSBinding binding) {
        boolean tSticky = false;
        WebServiceFeature[] features = binding.getFeatures().toArray();
        int length = features.length;
        int i2 = 0;
        while (true) {
            if (i2 >= length) {
                break;
            }
            WebServiceFeature f2 = features[i2];
            if (!(f2 instanceof StickyFeature)) {
                i2++;
            } else {
                tSticky = true;
                break;
            }
        }
        return tSticky;
    }

    private HttpTransportPipe(HttpTransportPipe that, TubeCloner cloner) {
        this(that.codec.copy(), that.binding);
        cloner.add(that, this);
    }

    @Override // com.sun.xml.internal.ws.api.pipe.Tube
    public NextAction processException(@NotNull Throwable t2) {
        return doThrow(t2);
    }

    @Override // com.sun.xml.internal.ws.api.pipe.Tube
    public NextAction processRequest(@NotNull Packet request) {
        return doReturnWith(process(request));
    }

    @Override // com.sun.xml.internal.ws.api.pipe.Tube
    public NextAction processResponse(@NotNull Packet response) {
        return doReturnWith(response);
    }

    protected HttpClientTransport getTransport(Packet request, Map<String, List<String>> reqHeaders) {
        return new HttpClientTransport(request, reqHeaders);
    }

    @Override // com.sun.xml.internal.ws.api.pipe.helper.AbstractTubeImpl, com.sun.xml.internal.ws.api.pipe.Pipe
    public Packet process(Packet request) {
        try {
            Map<String, List<String>> reqHeaders = new Headers();
            Map<String, List<String>> userHeaders = (Map) request.invocationProperties.get(MessageContext.HTTP_REQUEST_HEADERS);
            boolean addUserAgent = true;
            if (userHeaders != null) {
                reqHeaders.putAll(userHeaders);
                if (userHeaders.get("User-Agent") != null) {
                    addUserAgent = false;
                }
            }
            if (addUserAgent) {
                reqHeaders.put("User-Agent", USER_AGENT);
            }
            addBasicAuth(request, reqHeaders);
            addCookies(request, reqHeaders);
            HttpClientTransport con = getTransport(request, reqHeaders);
            request.addSatellite(new HttpResponseProperties(con));
            ContentType ct = this.codec.getStaticContentType(request);
            if (ct == null) {
                ByteArrayBuffer buf = new ByteArrayBuffer();
                ContentType ct2 = this.codec.encode(request, buf);
                reqHeaders.put("Content-Length", Collections.singletonList(Integer.toString(buf.size())));
                reqHeaders.put("Content-Type", Collections.singletonList(ct2.getContentType()));
                if (ct2.getAcceptHeader() != null) {
                    reqHeaders.put(XIncludeHandler.HTTP_ACCEPT, Collections.singletonList(ct2.getAcceptHeader()));
                }
                if (this.binding instanceof SOAPBinding) {
                    writeSOAPAction(reqHeaders, ct2.getSOAPActionHeader());
                }
                if (dump || LOGGER.isLoggable(Level.FINER)) {
                    dump(buf, "HTTP request", reqHeaders);
                }
                buf.writeTo(con.getOutput());
            } else {
                reqHeaders.put("Content-Type", Collections.singletonList(ct.getContentType()));
                if (ct.getAcceptHeader() != null) {
                    reqHeaders.put(XIncludeHandler.HTTP_ACCEPT, Collections.singletonList(ct.getAcceptHeader()));
                }
                if (this.binding instanceof SOAPBinding) {
                    writeSOAPAction(reqHeaders, ct.getSOAPActionHeader());
                }
                if (dump || LOGGER.isLoggable(Level.FINER)) {
                    ByteArrayBuffer buf2 = new ByteArrayBuffer();
                    this.codec.encode(request, buf2);
                    dump(buf2, "HTTP request - " + ((Object) request.endpointAddress), reqHeaders);
                    OutputStream out = con.getOutput();
                    if (out != null) {
                        buf2.writeTo(out);
                    }
                } else {
                    OutputStream os = con.getOutput();
                    if (os != null) {
                        this.codec.encode(request, os);
                    }
                }
            }
            con.closeOutput();
            return createResponsePacket(request, con);
        } catch (WebServiceException wex) {
            throw wex;
        } catch (Exception ex) {
            throw new WebServiceException(ex);
        }
    }

    private Packet createResponsePacket(Packet request, HttpClientTransport con) throws IOException {
        con.readResponseCodeAndMessage();
        recordCookies(request, con);
        InputStream responseStream = con.getInput();
        if (dump || LOGGER.isLoggable(Level.FINER)) {
            ByteArrayBuffer buf = new ByteArrayBuffer();
            if (responseStream != null) {
                buf.write(responseStream);
                responseStream.close();
            }
            dump(buf, "HTTP response - " + ((Object) request.endpointAddress) + " - " + con.statusCode, con.getHeaders());
            responseStream = buf.newInputStream();
        }
        int cl = con.contentLength;
        InputStream tempIn = null;
        if (cl == -1) {
            tempIn = StreamUtils.hasSomeData(responseStream);
            if (tempIn != null) {
                responseStream = tempIn;
            }
        }
        if ((cl == 0 || (cl == -1 && tempIn == null)) && responseStream != null) {
            responseStream.close();
            responseStream = null;
        }
        checkStatusCode(responseStream, con);
        Packet reply = request.createClientResponse(null);
        reply.wasTransportSecure = con.isSecure();
        if (responseStream != null) {
            String contentType = con.getContentType();
            if (contentType != null && contentType.contains(Clipboard.HTML_TYPE) && (this.binding instanceof SOAPBinding)) {
                throw new ClientTransportException(ClientMessages.localizableHTTP_STATUS_CODE(Integer.valueOf(con.statusCode), con.statusMessage));
            }
            this.codec.decode(responseStream, contentType, reply);
        }
        return reply;
    }

    private void checkStatusCode(InputStream in, HttpClientTransport con) throws IOException {
        int statusCode = con.statusCode;
        String statusMessage = con.statusMessage;
        if (this.binding instanceof SOAPBinding) {
            if (this.binding.getSOAPVersion() == SOAPVersion.SOAP_12) {
                if (statusCode == 200 || statusCode == 202 || isErrorCode(statusCode)) {
                    if (isErrorCode(statusCode) && in == null) {
                        throw new ClientTransportException(ClientMessages.localizableHTTP_STATUS_CODE(Integer.valueOf(statusCode), statusMessage));
                    }
                    return;
                }
            } else if (statusCode == 200 || statusCode == 202 || statusCode == 500) {
                if (statusCode == 500 && in == null) {
                    throw new ClientTransportException(ClientMessages.localizableHTTP_STATUS_CODE(Integer.valueOf(statusCode), statusMessage));
                }
                return;
            }
            if (in != null) {
                in.close();
            }
            throw new ClientTransportException(ClientMessages.localizableHTTP_STATUS_CODE(Integer.valueOf(statusCode), statusMessage));
        }
    }

    private boolean isErrorCode(int code) {
        return code == 500 || code == 400;
    }

    private void addCookies(Packet context, Map<String, List<String>> reqHeaders) throws IOException {
        Boolean shouldMaintainSessionProperty = (Boolean) context.invocationProperties.get(BindingProvider.SESSION_MAINTAIN_PROPERTY);
        if (shouldMaintainSessionProperty != null && !shouldMaintainSessionProperty.booleanValue()) {
            return;
        }
        if (this.sticky || (shouldMaintainSessionProperty != null && shouldMaintainSessionProperty.booleanValue())) {
            Map<String, List<String>> rememberedCookies = this.cookieJar.get(context.endpointAddress.getURI(), reqHeaders);
            processCookieHeaders(reqHeaders, rememberedCookies, "Cookie");
            processCookieHeaders(reqHeaders, rememberedCookies, "Cookie2");
        }
    }

    private void processCookieHeaders(Map<String, List<String>> requestHeaders, Map<String, List<String>> rememberedCookies, String cookieHeader) {
        List<String> jarCookies = rememberedCookies.get(cookieHeader);
        if (jarCookies != null && !jarCookies.isEmpty()) {
            List<String> resultCookies = mergeUserCookies(jarCookies, requestHeaders.get(cookieHeader));
            requestHeaders.put(cookieHeader, resultCookies);
        }
    }

    private List<String> mergeUserCookies(List<String> rememberedCookies, List<String> userCookies) {
        if (userCookies == null || userCookies.isEmpty()) {
            return rememberedCookies;
        }
        Map<String, String> map = new HashMap<>();
        cookieListToMap(rememberedCookies, map);
        cookieListToMap(userCookies, map);
        return new ArrayList(map.values());
    }

    private void cookieListToMap(List<String> cookieList, Map<String, String> targetMap) {
        for (String cookie : cookieList) {
            int index = cookie.indexOf("=");
            String cookieName = cookie.substring(0, index);
            targetMap.put(cookieName, cookie);
        }
    }

    private void recordCookies(Packet context, HttpClientTransport con) throws IOException {
        Boolean shouldMaintainSessionProperty = (Boolean) context.invocationProperties.get(BindingProvider.SESSION_MAINTAIN_PROPERTY);
        if (shouldMaintainSessionProperty != null && !shouldMaintainSessionProperty.booleanValue()) {
            return;
        }
        if (this.sticky || (shouldMaintainSessionProperty != null && shouldMaintainSessionProperty.booleanValue())) {
            this.cookieJar.put(context.endpointAddress.getURI(), con.getHeaders());
        }
    }

    private void addBasicAuth(Packet context, Map<String, List<String>> reqHeaders) {
        String pw;
        String user = (String) context.invocationProperties.get(BindingProvider.USERNAME_PROPERTY);
        if (user != null && (pw = (String) context.invocationProperties.get(BindingProvider.PASSWORD_PROPERTY)) != null) {
            String creds = DatatypeConverter.printBase64Binary((user + CallSiteDescriptor.TOKEN_DELIMITER + pw).getBytes());
            reqHeaders.put("Authorization", Collections.singletonList(new StringBuilder().append("Basic ").append(creds).toString()));
        }
    }

    private void writeSOAPAction(Map<String, List<String>> reqHeaders, String soapAction) {
        if (SOAPVersion.SOAP_12.equals(this.binding.getSOAPVersion())) {
            return;
        }
        if (soapAction != null) {
            reqHeaders.put("SOAPAction", Collections.singletonList(soapAction));
        } else {
            reqHeaders.put("SOAPAction", Collections.singletonList("\"\""));
        }
    }

    @Override // com.sun.xml.internal.ws.api.pipe.Tube, com.sun.xml.internal.ws.api.pipe.Pipe
    public void preDestroy() {
    }

    @Override // com.sun.xml.internal.ws.api.pipe.helper.AbstractTubeImpl, com.sun.xml.internal.ws.api.pipe.Tube
    public HttpTransportPipe copy(TubeCloner cloner) {
        return new HttpTransportPipe(this, cloner);
    }

    private void dump(ByteArrayBuffer buf, String caption, Map<String, List<String>> headers) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintWriter pw = new PrintWriter((OutputStream) baos, true);
        pw.println("---[" + caption + "]---");
        for (Map.Entry<String, List<String>> header : headers.entrySet()) {
            if (header.getValue().isEmpty()) {
                pw.println(header.getValue());
            } else {
                for (String value : header.getValue()) {
                    pw.println(header.getKey() + ": " + value);
                }
            }
        }
        if (buf.size() > HttpAdapter.dump_threshold) {
            byte[] b2 = buf.getRawData();
            baos.write(b2, 0, HttpAdapter.dump_threshold);
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
}
