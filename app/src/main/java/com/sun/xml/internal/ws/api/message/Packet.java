package com.sun.xml.internal.ws.api.message;

import com.oracle.webservices.internal.api.message.BaseDistributedPropertySet;
import com.oracle.webservices.internal.api.message.BasePropertySet;
import com.oracle.webservices.internal.api.message.ContentType;
import com.oracle.webservices.internal.api.message.MessageContext;
import com.oracle.webservices.internal.api.message.PropertySet;
import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import com.sun.xml.internal.bind.marshaller.SAX2DOMEx;
import com.sun.xml.internal.ws.addressing.WsaPropertyBag;
import com.sun.xml.internal.ws.addressing.WsaTubeHelper;
import com.sun.xml.internal.ws.api.Component;
import com.sun.xml.internal.ws.api.DistributedPropertySet;
import com.sun.xml.internal.ws.api.EndpointAddress;
import com.sun.xml.internal.ws.api.SOAPVersion;
import com.sun.xml.internal.ws.api.WSBinding;
import com.sun.xml.internal.ws.api.addressing.AddressingVersion;
import com.sun.xml.internal.ws.api.addressing.WSEndpointReference;
import com.sun.xml.internal.ws.api.model.SEIModel;
import com.sun.xml.internal.ws.api.model.WSDLOperationMapping;
import com.sun.xml.internal.ws.api.model.wsdl.WSDLPort;
import com.sun.xml.internal.ws.api.pipe.Codec;
import com.sun.xml.internal.ws.api.server.TransportBackChannel;
import com.sun.xml.internal.ws.api.server.WSEndpoint;
import com.sun.xml.internal.ws.api.server.WebServiceContextDelegate;
import com.sun.xml.internal.ws.api.streaming.XMLStreamWriterFactory;
import com.sun.xml.internal.ws.client.BindingProviderProperties;
import com.sun.xml.internal.ws.client.ContentNegotiation;
import com.sun.xml.internal.ws.client.HandlerConfiguration;
import com.sun.xml.internal.ws.client.Stub;
import com.sun.xml.internal.ws.developer.JAXWSProperties;
import com.sun.xml.internal.ws.encoding.MtomCodec;
import com.sun.xml.internal.ws.message.RelatesToHeader;
import com.sun.xml.internal.ws.message.StringHeader;
import com.sun.xml.internal.ws.resources.AddressingMessages;
import com.sun.xml.internal.ws.util.DOMUtil;
import com.sun.xml.internal.ws.util.xml.XmlUtil;
import com.sun.xml.internal.ws.wsdl.DispatchException;
import com.sun.xml.internal.ws.wsdl.OperationDispatcher;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.channels.WritableByteChannel;
import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;
import javax.xml.namespace.QName;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.soap.MTOMFeature;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

/* loaded from: rt.jar:com/sun/xml/internal/ws/api/message/Packet.class */
public final class Packet extends BaseDistributedPropertySet implements MessageContext, MessageMetadata {
    private Message message;
    private WSDLOperationMapping wsdlOperationMapping;
    private QName wsdlOperation;
    public boolean wasTransportSecure;
    public static final String INBOUND_TRANSPORT_HEADERS = "com.sun.xml.internal.ws.api.message.packet.inbound.transport.headers";
    public static final String OUTBOUND_TRANSPORT_HEADERS = "com.sun.xml.internal.ws.api.message.packet.outbound.transport.headers";
    public static final String HA_INFO = "com.sun.xml.internal.ws.api.message.packet.hainfo";

    @PropertySet.Property({BindingProviderProperties.JAXWS_HANDLER_CONFIG})
    public HandlerConfiguration handlerConfig;

    @PropertySet.Property({BindingProviderProperties.JAXWS_CLIENT_HANDLE_PROPERTY})
    public BindingProvider proxy;
    public boolean isAdapterDeliversNonAnonymousResponse;
    public boolean packetTakesPriorityOverRequestContext;
    public EndpointAddress endpointAddress;
    public ContentNegotiation contentNegotiation;
    public String acceptableMimeTypes;
    public WebServiceContextDelegate webServiceContextDelegate;

    @Nullable
    public TransportBackChannel transportBackChannel;
    public Component component;

    @PropertySet.Property({JAXWSProperties.WSENDPOINT})
    public WSEndpoint endpoint;

    @PropertySet.Property({BindingProvider.SOAPACTION_URI_PROPERTY})
    public String soapAction;

    @PropertySet.Property({BindingProviderProperties.ONE_WAY_OPERATION})
    public Boolean expectReply;

    @Deprecated
    public Boolean isOneWay;
    public Boolean isSynchronousMEP;
    public Boolean nonNullAsyncHandlerGiven;
    private Boolean isRequestReplyMEP;
    private Set<String> handlerScopePropertyNames;
    public final Map<String, Object> invocationProperties;
    private static final BasePropertySet.PropertyMap model;
    private static final Logger LOGGER;
    public Codec codec;
    private ContentType contentType;
    private Boolean mtomRequest;
    private Boolean mtomAcceptable;
    private MTOMFeature mtomFeature;
    Boolean checkMtomAcceptable;
    private Boolean fastInfosetAcceptable;
    private State state;
    private boolean isFastInfosetDisabled;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !Packet.class.desiredAssertionStatus();
        model = parse(Packet.class);
        LOGGER = Logger.getLogger(Packet.class.getName());
    }

    public Packet(Message request) {
        this();
        this.message = request;
        if (this.message != null) {
            this.message.setMessageMedadata(this);
        }
    }

    public Packet() {
        this.wsdlOperationMapping = null;
        this.packetTakesPriorityOverRequestContext = false;
        this.codec = null;
        this.state = State.ServerRequest;
        this.invocationProperties = new HashMap();
    }

    private Packet(Packet that) {
        this.wsdlOperationMapping = null;
        this.packetTakesPriorityOverRequestContext = false;
        this.codec = null;
        this.state = State.ServerRequest;
        relatePackets(that, true);
        this.invocationProperties = that.invocationProperties;
    }

    public Packet copy(boolean copyMessage) {
        Packet copy = new Packet(this);
        if (copyMessage && this.message != null) {
            copy.message = this.message.copy();
        }
        if (copy.message != null) {
            copy.message.setMessageMedadata(copy);
        }
        return copy;
    }

    public Message getMessage() {
        if (this.message != null && !(this.message instanceof MessageWrapper)) {
            this.message = new MessageWrapper(this, this.message);
        }
        return this.message;
    }

    public Message getInternalMessage() {
        return this.message instanceof MessageWrapper ? ((MessageWrapper) this.message).delegate : this.message;
    }

    public WSBinding getBinding() {
        if (this.endpoint != null) {
            return this.endpoint.getBinding();
        }
        if (this.proxy != null) {
            return (WSBinding) this.proxy.getBinding();
        }
        return null;
    }

    public void setMessage(Message message) {
        this.message = message;
        if (message != null) {
            this.message.setMessageMedadata(this);
        }
    }

    @PropertySet.Property({javax.xml.ws.handler.MessageContext.WSDL_OPERATION})
    @Nullable
    public final QName getWSDLOperation() {
        if (this.wsdlOperation != null) {
            return this.wsdlOperation;
        }
        if (this.wsdlOperationMapping == null) {
            this.wsdlOperationMapping = getWSDLOperationMapping();
        }
        if (this.wsdlOperationMapping != null) {
            this.wsdlOperation = this.wsdlOperationMapping.getOperationName();
        }
        return this.wsdlOperation;
    }

    @Override // com.sun.xml.internal.ws.api.message.MessageMetadata
    public WSDLOperationMapping getWSDLOperationMapping() {
        if (this.wsdlOperationMapping != null) {
            return this.wsdlOperationMapping;
        }
        OperationDispatcher opDispatcher = null;
        if (this.endpoint != null) {
            opDispatcher = this.endpoint.getOperationDispatcher();
        } else if (this.proxy != null) {
            opDispatcher = ((Stub) this.proxy).getOperationDispatcher();
        }
        if (opDispatcher != null) {
            try {
                this.wsdlOperationMapping = opDispatcher.getWSDLOperationMapping(this);
            } catch (DispatchException e2) {
            }
        }
        return this.wsdlOperationMapping;
    }

    public void setWSDLOperation(QName wsdlOp) {
        this.wsdlOperation = wsdlOp;
    }

    @PropertySet.Property({BindingProvider.ENDPOINT_ADDRESS_PROPERTY})
    public String getEndPointAddressString() {
        if (this.endpointAddress == null) {
            return null;
        }
        return this.endpointAddress.toString();
    }

    public void setEndPointAddressString(String s2) {
        if (s2 == null) {
            this.endpointAddress = null;
        } else {
            this.endpointAddress = EndpointAddress.create(s2);
        }
    }

    @PropertySet.Property({ContentNegotiation.PROPERTY})
    public String getContentNegotiationString() {
        if (this.contentNegotiation != null) {
            return this.contentNegotiation.toString();
        }
        return null;
    }

    public void setContentNegotiationString(String s2) {
        if (s2 == null) {
            this.contentNegotiation = null;
            return;
        }
        try {
            this.contentNegotiation = ContentNegotiation.valueOf(s2);
        } catch (IllegalArgumentException e2) {
            this.contentNegotiation = ContentNegotiation.none;
        }
    }

    @PropertySet.Property({javax.xml.ws.handler.MessageContext.REFERENCE_PARAMETERS})
    @NotNull
    public List<Element> getReferenceParameters() {
        Message msg = getMessage();
        List<Element> refParams = new ArrayList<>();
        if (msg == null) {
            return refParams;
        }
        MessageHeaders hl = msg.getHeaders();
        for (Header h2 : hl.asList()) {
            String attr = h2.getAttribute(AddressingVersion.W3C.nsUri, "IsReferenceParameter");
            if (attr != null && (attr.equals("true") || attr.equals("1"))) {
                Document d2 = DOMUtil.createDom();
                SAX2DOMEx s2d = new SAX2DOMEx(d2);
                try {
                    h2.writeTo(s2d, XmlUtil.DRACONIAN_ERROR_HANDLER);
                    refParams.add((Element) d2.getLastChild());
                } catch (SAXException e2) {
                    throw new WebServiceException(e2);
                }
            }
        }
        return refParams;
    }

    @PropertySet.Property({JAXWSProperties.INBOUND_HEADER_LIST_PROPERTY})
    MessageHeaders getHeaderList() {
        Message msg = getMessage();
        if (msg == null) {
            return null;
        }
        return msg.getHeaders();
    }

    public TransportBackChannel keepTransportBackChannelOpen() {
        TransportBackChannel r2 = this.transportBackChannel;
        this.transportBackChannel = null;
        return r2;
    }

    public Boolean isRequestReplyMEP() {
        return this.isRequestReplyMEP;
    }

    public void setRequestReplyMEP(Boolean x2) {
        this.isRequestReplyMEP = x2;
    }

    public final Set<String> getHandlerScopePropertyNames(boolean readOnly) {
        Set<String> o2 = this.handlerScopePropertyNames;
        if (o2 == null) {
            if (readOnly) {
                return Collections.emptySet();
            }
            o2 = new HashSet();
            this.handlerScopePropertyNames = o2;
        }
        return o2;
    }

    public final Set<String> getApplicationScopePropertyNames(boolean readOnly) {
        if ($assertionsDisabled) {
            return new HashSet();
        }
        throw new AssertionError();
    }

    @Deprecated
    public Packet createResponse(Message msg) {
        Packet response = new Packet(this);
        response.setMessage(msg);
        return response;
    }

    public Packet createClientResponse(Message msg) {
        Packet response = new Packet(this);
        response.setMessage(msg);
        finishCreateRelateClientResponse(response);
        return response;
    }

    public Packet relateClientResponse(Packet response) {
        response.relatePackets(this, true);
        finishCreateRelateClientResponse(response);
        return response;
    }

    private void finishCreateRelateClientResponse(Packet response) {
        response.soapAction = null;
        response.setState(State.ClientResponse);
    }

    public Packet createServerResponse(@Nullable Message responseMessage, @Nullable WSDLPort wsdlPort, @Nullable SEIModel seiModel, @NotNull WSBinding binding) {
        Packet r2 = createClientResponse(responseMessage);
        return relateServerResponse(r2, wsdlPort, seiModel, binding);
    }

    public void copyPropertiesTo(@Nullable Packet response) {
        relatePackets(response, false);
    }

    private void relatePackets(@Nullable Packet packet, boolean isCopy) {
        Packet request;
        Packet response;
        if (!isCopy) {
            request = this;
            response = packet;
            response.soapAction = null;
            response.invocationProperties.putAll(request.invocationProperties);
            if (getState().equals(State.ServerRequest)) {
                response.setState(State.ServerResponse);
            }
        } else {
            request = packet;
            response = this;
            response.soapAction = request.soapAction;
            response.setState(request.getState());
        }
        request.copySatelliteInto((MessageContext) response);
        response.isAdapterDeliversNonAnonymousResponse = request.isAdapterDeliversNonAnonymousResponse;
        response.handlerConfig = request.handlerConfig;
        response.handlerScopePropertyNames = request.handlerScopePropertyNames;
        response.contentNegotiation = request.contentNegotiation;
        response.wasTransportSecure = request.wasTransportSecure;
        response.transportBackChannel = request.transportBackChannel;
        response.endpointAddress = request.endpointAddress;
        response.wsdlOperation = request.wsdlOperation;
        response.wsdlOperationMapping = request.wsdlOperationMapping;
        response.acceptableMimeTypes = request.acceptableMimeTypes;
        response.endpoint = request.endpoint;
        response.proxy = request.proxy;
        response.webServiceContextDelegate = request.webServiceContextDelegate;
        response.expectReply = request.expectReply;
        response.component = request.component;
        response.mtomAcceptable = request.mtomAcceptable;
        response.mtomRequest = request.mtomRequest;
    }

    public Packet relateServerResponse(@Nullable Packet r2, @Nullable WSDLPort wsdlPort, @Nullable SEIModel seiModel, @NotNull WSBinding binding) {
        relatePackets(r2, false);
        r2.setState(State.ServerResponse);
        AddressingVersion av2 = binding.getAddressingVersion();
        if (av2 == null) {
            return r2;
        }
        if (getMessage() == null) {
            return r2;
        }
        String inputAction = AddressingUtils.getAction(getMessage().getHeaders(), av2, binding.getSOAPVersion());
        if (inputAction == null) {
            return r2;
        }
        if (r2.getMessage() == null || (wsdlPort != null && getMessage().isOneWay(wsdlPort))) {
            return r2;
        }
        populateAddressingHeaders(binding, r2, wsdlPort, seiModel);
        return r2;
    }

    public Packet createServerResponse(@Nullable Message responseMessage, @NotNull AddressingVersion addressingVersion, @NotNull SOAPVersion soapVersion, @NotNull String action) {
        Packet responsePacket = createClientResponse(responseMessage);
        responsePacket.setState(State.ServerResponse);
        if (addressingVersion == null) {
            return responsePacket;
        }
        String inputAction = AddressingUtils.getAction(getMessage().getHeaders(), addressingVersion, soapVersion);
        if (inputAction == null) {
            return responsePacket;
        }
        populateAddressingHeaders(responsePacket, addressingVersion, soapVersion, action, false);
        return responsePacket;
    }

    public void setResponseMessage(@NotNull Packet request, @Nullable Message responseMessage, @NotNull AddressingVersion addressingVersion, @NotNull SOAPVersion soapVersion, @NotNull String action) {
        Packet temp = request.createServerResponse(responseMessage, addressingVersion, soapVersion, action);
        setMessage(temp.getMessage());
    }

    private void populateAddressingHeaders(Packet responsePacket, AddressingVersion av2, SOAPVersion sv, String action, boolean mustUnderstand) {
        if (av2 == null || responsePacket.getMessage() == null) {
            return;
        }
        MessageHeaders hl = responsePacket.getMessage().getHeaders();
        WsaPropertyBag wpb = (WsaPropertyBag) getSatellite(WsaPropertyBag.class);
        Message msg = getMessage();
        WSEndpointReference replyTo = null;
        Header replyToFromRequestMsg = AddressingUtils.getFirstHeader(msg.getHeaders(), av2.replyToTag, true, sv);
        Header replyToFromResponseMsg = hl.get(av2.toTag, false);
        boolean replaceToTag = true;
        if (replyToFromRequestMsg != null) {
            try {
                replyTo = replyToFromRequestMsg.readAsEPR(av2);
            } catch (XMLStreamException e2) {
                throw new WebServiceException(AddressingMessages.REPLY_TO_CANNOT_PARSE(), e2);
            }
        }
        if (replyToFromResponseMsg != null && replyTo == null) {
            replaceToTag = false;
        }
        if (replyTo == null) {
            replyTo = AddressingUtils.getReplyTo(msg.getHeaders(), av2, sv);
        }
        if (AddressingUtils.getAction(responsePacket.getMessage().getHeaders(), av2, sv) == null) {
            hl.add(new StringHeader(av2.actionTag, action, sv, mustUnderstand));
        }
        if (responsePacket.getMessage().getHeaders().get(av2.messageIDTag, false) == null) {
            String newID = Message.generateMessageID();
            hl.add(new StringHeader(av2.messageIDTag, newID));
        }
        String mid = null;
        if (wpb != null) {
            mid = wpb.getMessageID();
        }
        if (mid == null) {
            mid = AddressingUtils.getMessageID(msg.getHeaders(), av2, sv);
        }
        if (mid != null) {
            hl.addOrReplace(new RelatesToHeader(av2.relatesToTag, mid));
        }
        WSEndpointReference refpEPR = null;
        if (responsePacket.getMessage().isFault()) {
            if (wpb != null) {
                refpEPR = wpb.getFaultToFromRequest();
            }
            if (refpEPR == null) {
                refpEPR = AddressingUtils.getFaultTo(msg.getHeaders(), av2, sv);
            }
            if (refpEPR == null) {
                refpEPR = replyTo;
            }
        } else {
            refpEPR = replyTo;
        }
        if (replaceToTag && refpEPR != null) {
            hl.addOrReplace(new StringHeader(av2.toTag, refpEPR.getAddress()));
            refpEPR.addReferenceParametersToList(hl);
        }
    }

    private void populateAddressingHeaders(WSBinding binding, Packet responsePacket, WSDLPort wsdlPort, SEIModel seiModel) {
        String outputAction;
        AddressingVersion addressingVersion = binding.getAddressingVersion();
        if (addressingVersion == null) {
            return;
        }
        WsaTubeHelper wsaHelper = addressingVersion.getWsaHelper(wsdlPort, seiModel, binding);
        if (responsePacket.getMessage().isFault()) {
            outputAction = wsaHelper.getFaultAction(this, responsePacket);
        } else {
            outputAction = wsaHelper.getOutputAction(this);
        }
        String action = outputAction;
        if (action == null) {
            LOGGER.info("WSA headers are not added as value for wsa:Action cannot be resolved for this message");
        } else {
            populateAddressingHeaders(responsePacket, addressingVersion, binding.getSOAPVersion(), action, AddressingVersion.isRequired(binding));
        }
    }

    public String toShortString() {
        return super.toString();
    }

    public String toString() {
        String content;
        StringBuilder buf = new StringBuilder();
        buf.append(super.toString());
        try {
            Message msg = getMessage();
            if (msg != null) {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                XMLStreamWriter xmlWriter = XMLStreamWriterFactory.create(baos, "UTF-8");
                msg.copy().writeTo(xmlWriter);
                xmlWriter.flush();
                xmlWriter.close();
                baos.flush();
                XMLStreamWriterFactory.recycle(xmlWriter);
                byte[] bytes = baos.toByteArray();
                content = new String(bytes, "UTF-8");
            } else {
                content = "<none>";
            }
            buf.append(" Content: ").append(content);
            return buf.toString();
        } catch (Throwable t2) {
            throw new WebServiceException(t2);
        }
    }

    @Override // com.oracle.webservices.internal.api.message.BasePropertySet
    protected BasePropertySet.PropertyMap getPropertyMap() {
        return model;
    }

    public Map<String, Object> asMapIncludingInvocationProperties() {
        final Map<String, Object> asMap = asMap();
        return new AbstractMap<String, Object>() { // from class: com.sun.xml.internal.ws.api.message.Packet.1
            @Override // java.util.AbstractMap, java.util.Map
            public Object get(Object key) {
                Object o2 = asMap.get(key);
                if (o2 != null) {
                    return o2;
                }
                return Packet.this.invocationProperties.get(key);
            }

            @Override // java.util.AbstractMap, java.util.Map
            public int size() {
                return asMap.size() + Packet.this.invocationProperties.size();
            }

            @Override // java.util.AbstractMap, java.util.Map
            public boolean containsKey(Object key) {
                if (asMap.containsKey(key)) {
                    return true;
                }
                return Packet.this.invocationProperties.containsKey(key);
            }

            @Override // java.util.AbstractMap, java.util.Map
            public Set<Map.Entry<String, Object>> entrySet() {
                final Set<Map.Entry<String, Object>> asMapEntries = asMap.entrySet();
                final Set<Map.Entry<String, Object>> ipEntries = Packet.this.invocationProperties.entrySet();
                return new AbstractSet<Map.Entry<String, Object>>() { // from class: com.sun.xml.internal.ws.api.message.Packet.1.1
                    @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.List
                    public Iterator<Map.Entry<String, Object>> iterator() {
                        final Iterator<Map.Entry<String, Object>> asMapIt = asMapEntries.iterator();
                        final Iterator<Map.Entry<String, Object>> ipIt = ipEntries.iterator();
                        return new Iterator<Map.Entry<String, Object>>() { // from class: com.sun.xml.internal.ws.api.message.Packet.1.1.1
                            @Override // java.util.Iterator
                            public boolean hasNext() {
                                return asMapIt.hasNext() || ipIt.hasNext();
                            }

                            /* JADX WARN: Can't rename method to resolve collision */
                            @Override // java.util.Iterator
                            public Map.Entry<String, Object> next() {
                                if (asMapIt.hasNext()) {
                                    return (Map.Entry) asMapIt.next();
                                }
                                return (Map.Entry) ipIt.next();
                            }

                            @Override // java.util.Iterator
                            public void remove() {
                                throw new UnsupportedOperationException();
                            }
                        };
                    }

                    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
                    public int size() {
                        return asMap.size() + Packet.this.invocationProperties.size();
                    }
                };
            }

            @Override // java.util.AbstractMap, java.util.Map
            public Object put(String key, Object value) {
                if (Packet.this.supports(key)) {
                    return asMap.put(key, value);
                }
                return Packet.this.invocationProperties.put(key, value);
            }

            @Override // java.util.AbstractMap, java.util.Map
            public void clear() {
                asMap.clear();
                Packet.this.invocationProperties.clear();
            }

            @Override // java.util.AbstractMap, java.util.Map
            public Object remove(Object key) {
                if (Packet.this.supports(key)) {
                    return asMap.remove(key);
                }
                return Packet.this.invocationProperties.remove(key);
            }
        };
    }

    @Override // com.oracle.webservices.internal.api.message.MessageContext
    public SOAPMessage getSOAPMessage() throws SOAPException {
        return getAsSOAPMessage();
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.oracle.webservices.internal.api.message.MessageContext
    public SOAPMessage getAsSOAPMessage() throws SOAPException {
        Message message = getMessage();
        if (message == 0) {
            return null;
        }
        if (message instanceof MessageWritable) {
            ((MessageWritable) message).setMTOMConfiguration(this.mtomFeature);
        }
        return message.readAsSOAPMessage(this, getState().isInbound());
    }

    public Codec getCodec() {
        if (this.codec != null) {
            return this.codec;
        }
        if (this.endpoint != null) {
            this.codec = this.endpoint.createCodec();
        }
        WSBinding wsb = getBinding();
        if (wsb != null) {
            this.codec = wsb.getBindingId().createEncoder(wsb);
        }
        return this.codec;
    }

    @Override // com.oracle.webservices.internal.api.message.MessageContext
    public ContentType writeTo(OutputStream out) throws IOException {
        Object internalMessage = getInternalMessage();
        if (internalMessage instanceof MessageWritable) {
            ((MessageWritable) internalMessage).setMTOMConfiguration(this.mtomFeature);
            return ((MessageWritable) internalMessage).writeTo(out);
        }
        return getCodec().encode(this, out);
    }

    public ContentType writeTo(WritableByteChannel buffer) {
        return getCodec().encode(this, buffer);
    }

    public Boolean getMtomRequest() {
        return this.mtomRequest;
    }

    public void setMtomRequest(Boolean mtomRequest) {
        this.mtomRequest = mtomRequest;
    }

    public Boolean getMtomAcceptable() {
        return this.mtomAcceptable;
    }

    public void checkMtomAcceptable() {
        if (this.checkMtomAcceptable == null) {
            if (this.acceptableMimeTypes == null || this.isFastInfosetDisabled) {
                this.checkMtomAcceptable = false;
            } else {
                this.checkMtomAcceptable = Boolean.valueOf(this.acceptableMimeTypes.indexOf(MtomCodec.XOP_XML_MIME_TYPE) != -1);
            }
        }
        this.mtomAcceptable = this.checkMtomAcceptable;
    }

    public Boolean getFastInfosetAcceptable(String fiMimeType) {
        if (this.fastInfosetAcceptable == null) {
            if (this.acceptableMimeTypes == null || this.isFastInfosetDisabled) {
                this.fastInfosetAcceptable = false;
            } else {
                this.fastInfosetAcceptable = Boolean.valueOf(this.acceptableMimeTypes.indexOf(fiMimeType) != -1);
            }
        }
        return this.fastInfosetAcceptable;
    }

    public void setMtomFeature(MTOMFeature mtomFeature) {
        this.mtomFeature = mtomFeature;
    }

    public MTOMFeature getMtomFeature() {
        WSBinding binding = getBinding();
        if (binding != null) {
            return (MTOMFeature) binding.getFeature(MTOMFeature.class);
        }
        return this.mtomFeature;
    }

    @Override // com.oracle.webservices.internal.api.message.MessageContext
    public ContentType getContentType() {
        if (this.contentType == null) {
            this.contentType = getInternalContentType();
        }
        if (this.contentType == null) {
            this.contentType = getCodec().getStaticContentType(this);
        }
        if (this.contentType == null) {
        }
        return this.contentType;
    }

    public ContentType getInternalContentType() {
        Object internalMessage = getInternalMessage();
        if (internalMessage instanceof MessageWritable) {
            return ((MessageWritable) internalMessage).getContentType();
        }
        return this.contentType;
    }

    public void setContentType(ContentType contentType) {
        this.contentType = contentType;
    }

    /* loaded from: rt.jar:com/sun/xml/internal/ws/api/message/Packet$Status.class */
    public enum Status {
        Request,
        Response,
        Unknown;

        public boolean isRequest() {
            return Request.equals(this);
        }

        public boolean isResponse() {
            return Response.equals(this);
        }
    }

    /* loaded from: rt.jar:com/sun/xml/internal/ws/api/message/Packet$State.class */
    public enum State {
        ServerRequest(true),
        ClientRequest(false),
        ServerResponse(false),
        ClientResponse(true);

        private boolean inbound;

        State(boolean inbound) {
            this.inbound = inbound;
        }

        public boolean isInbound() {
            return this.inbound;
        }
    }

    public State getState() {
        return this.state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public boolean shouldUseMtom() {
        if (getState().isInbound()) {
            return isMtomContentType();
        }
        return shouldUseMtomOutbound();
    }

    private boolean shouldUseMtomOutbound() {
        MTOMFeature myMtomFeature = getMtomFeature();
        if (myMtomFeature != null && myMtomFeature.isEnabled()) {
            if (getMtomAcceptable() == null && getMtomRequest() == null) {
                return true;
            }
            if (getMtomAcceptable() != null && getMtomAcceptable().booleanValue() && getState().equals(State.ServerResponse)) {
                return true;
            }
            if (getMtomRequest() != null && getMtomRequest().booleanValue() && getState().equals(State.ServerResponse)) {
                return true;
            }
            if (getMtomRequest() != null && getMtomRequest().booleanValue() && getState().equals(State.ClientRequest)) {
                return true;
            }
            return false;
        }
        return false;
    }

    private boolean isMtomContentType() {
        return getInternalContentType() != null && getInternalContentType().getContentType().contains(MtomCodec.XOP_XML_MIME_TYPE);
    }

    public void addSatellite(@NotNull com.sun.xml.internal.ws.api.PropertySet satellite) {
        super.addSatellite((PropertySet) satellite);
    }

    public void addSatellite(@NotNull Class keyClass, @NotNull com.sun.xml.internal.ws.api.PropertySet satellite) {
        super.addSatellite((Class<? extends PropertySet>) keyClass, (PropertySet) satellite);
    }

    public void copySatelliteInto(@NotNull DistributedPropertySet r2) {
        super.copySatelliteInto((com.oracle.webservices.internal.api.message.DistributedPropertySet) r2);
    }

    public void removeSatellite(com.sun.xml.internal.ws.api.PropertySet satellite) {
        super.removeSatellite((PropertySet) satellite);
    }

    public void setFastInfosetDisabled(boolean b2) {
        this.isFastInfosetDisabled = b2;
    }
}
