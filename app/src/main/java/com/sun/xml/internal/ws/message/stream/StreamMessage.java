package com.sun.xml.internal.ws.message.stream;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import com.sun.istack.internal.XMLStreamReaderToContentHandler;
import com.sun.xml.internal.bind.api.Bridge;
import com.sun.xml.internal.stream.buffer.MutableXMLStreamBuffer;
import com.sun.xml.internal.stream.buffer.XMLStreamBuffer;
import com.sun.xml.internal.stream.buffer.XMLStreamBufferMark;
import com.sun.xml.internal.stream.buffer.stax.StreamReaderBufferCreator;
import com.sun.xml.internal.ws.api.SOAPVersion;
import com.sun.xml.internal.ws.api.message.AttachmentSet;
import com.sun.xml.internal.ws.api.message.Header;
import com.sun.xml.internal.ws.api.message.HeaderList;
import com.sun.xml.internal.ws.api.message.Message;
import com.sun.xml.internal.ws.api.message.MessageHeaders;
import com.sun.xml.internal.ws.api.message.StreamingSOAP;
import com.sun.xml.internal.ws.api.streaming.XMLStreamReaderFactory;
import com.sun.xml.internal.ws.encoding.TagInfoset;
import com.sun.xml.internal.ws.message.AbstractMessageImpl;
import com.sun.xml.internal.ws.message.AttachmentUnmarshallerImpl;
import com.sun.xml.internal.ws.protocol.soap.VersionMismatchException;
import com.sun.xml.internal.ws.spi.db.XMLBridge;
import com.sun.xml.internal.ws.streaming.XMLStreamReaderUtil;
import com.sun.xml.internal.ws.util.xml.DummyLocation;
import com.sun.xml.internal.ws.util.xml.StAXSource;
import com.sun.xml.internal.ws.util.xml.XMLReaderComposite;
import com.sun.xml.internal.ws.util.xml.XMLStreamReaderToXMLStreamWriter;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.Location;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.transform.Source;
import javax.xml.ws.WebServiceException;
import org.xml.sax.ContentHandler;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.NamespaceSupport;

/* loaded from: rt.jar:com/sun/xml/internal/ws/message/stream/StreamMessage.class */
public class StreamMessage extends AbstractMessageImpl implements StreamingSOAP {

    @NotNull
    private XMLStreamReader reader;

    @Nullable
    private MessageHeaders headers;
    private String bodyPrologue;
    private String bodyEpilogue;
    private String payloadLocalName;
    private String payloadNamespaceURI;
    private Throwable consumedAt;
    private XMLStreamReader envelopeReader;
    private static final String SOAP_ENVELOPE = "Envelope";
    private static final String SOAP_HEADER = "Header";
    private static final String SOAP_BODY = "Body";
    static final StreamHeaderDecoder SOAP12StreamHeaderDecoder;
    static final StreamHeaderDecoder SOAP11StreamHeaderDecoder;
    static final /* synthetic */ boolean $assertionsDisabled;

    /* loaded from: rt.jar:com/sun/xml/internal/ws/message/stream/StreamMessage$StreamHeaderDecoder.class */
    protected interface StreamHeaderDecoder {
        Header decodeHeader(XMLStreamReader xMLStreamReader, XMLStreamBuffer xMLStreamBuffer);
    }

    static {
        $assertionsDisabled = !StreamMessage.class.desiredAssertionStatus();
        SOAP12StreamHeaderDecoder = new StreamHeaderDecoder() { // from class: com.sun.xml.internal.ws.message.stream.StreamMessage.1
            @Override // com.sun.xml.internal.ws.message.stream.StreamMessage.StreamHeaderDecoder
            public Header decodeHeader(XMLStreamReader reader, XMLStreamBuffer mark) {
                return new StreamHeader12(reader, mark);
            }
        };
        SOAP11StreamHeaderDecoder = new StreamHeaderDecoder() { // from class: com.sun.xml.internal.ws.message.stream.StreamMessage.2
            @Override // com.sun.xml.internal.ws.message.stream.StreamMessage.StreamHeaderDecoder
            public Header decodeHeader(XMLStreamReader reader, XMLStreamBuffer mark) {
                return new StreamHeader11(reader, mark);
            }
        };
    }

    public StreamMessage(SOAPVersion v2) {
        super(v2);
        this.bodyPrologue = null;
        this.bodyEpilogue = null;
        this.payloadLocalName = null;
        this.payloadNamespaceURI = null;
    }

    public StreamMessage(SOAPVersion v2, @NotNull XMLStreamReader envelope, @NotNull AttachmentSet attachments) {
        super(v2);
        this.bodyPrologue = null;
        this.bodyEpilogue = null;
        this.envelopeReader = envelope;
        this.attachmentSet = attachments;
    }

    @Override // com.sun.xml.internal.ws.api.message.StreamingSOAP
    public XMLStreamReader readEnvelope() {
        if (this.envelopeReader == null) {
            List<XMLStreamReader> hReaders = new ArrayList<>();
            XMLReaderComposite.ElemInfo envElem = new XMLReaderComposite.ElemInfo(this.envelopeTag, null);
            XMLReaderComposite.ElemInfo hdrElem = this.headerTag != null ? new XMLReaderComposite.ElemInfo(this.headerTag, envElem) : null;
            XMLReaderComposite.ElemInfo bdyElem = new XMLReaderComposite.ElemInfo(this.bodyTag, envElem);
            for (Header h2 : getHeaders().asList()) {
                try {
                    hReaders.add(h2.readHeader());
                } catch (XMLStreamException e2) {
                    throw new RuntimeException(e2);
                }
            }
            XMLStreamReader soapHeader = hdrElem != null ? new XMLReaderComposite(hdrElem, (XMLStreamReader[]) hReaders.toArray(new XMLStreamReader[hReaders.size()])) : null;
            XMLStreamReader[] payload = {readPayload()};
            XMLStreamReader soapBody = new XMLReaderComposite(bdyElem, payload);
            XMLStreamReader[] soapContent = soapHeader != null ? new XMLStreamReader[]{soapHeader, soapBody} : new XMLStreamReader[]{soapBody};
            return new XMLReaderComposite(envElem, soapContent);
        }
        return this.envelopeReader;
    }

    public StreamMessage(@Nullable MessageHeaders headers, @NotNull AttachmentSet attachmentSet, @NotNull XMLStreamReader reader, @NotNull SOAPVersion soapVersion) {
        super(soapVersion);
        this.bodyPrologue = null;
        this.bodyEpilogue = null;
        init(headers, attachmentSet, reader, soapVersion);
    }

    private void init(@Nullable MessageHeaders headers, @NotNull AttachmentSet attachmentSet, @NotNull XMLStreamReader reader, @NotNull SOAPVersion soapVersion) {
        this.headers = headers;
        this.attachmentSet = attachmentSet;
        this.reader = reader;
        if (reader.getEventType() == 7) {
            XMLStreamReaderUtil.nextElementContent(reader);
        }
        if (reader.getEventType() == 2) {
            String body = reader.getLocalName();
            String nsUri = reader.getNamespaceURI();
            if (!$assertionsDisabled && body == null) {
                throw new AssertionError();
            }
            if (!$assertionsDisabled && nsUri == null) {
                throw new AssertionError();
            }
            if (body.equals("Body") && nsUri.equals(soapVersion.nsUri)) {
                this.payloadLocalName = null;
                this.payloadNamespaceURI = null;
            } else {
                throw new WebServiceException("Malformed stream: {" + nsUri + "}" + body);
            }
        } else {
            this.payloadLocalName = reader.getLocalName();
            this.payloadNamespaceURI = reader.getNamespaceURI();
        }
        int base = soapVersion.ordinal() * 3;
        this.envelopeTag = DEFAULT_TAGS.get(base);
        this.headerTag = DEFAULT_TAGS.get(base + 1);
        this.bodyTag = DEFAULT_TAGS.get(base + 2);
    }

    public StreamMessage(@NotNull TagInfoset envelopeTag, @Nullable TagInfoset headerTag, @NotNull AttachmentSet attachmentSet, @Nullable MessageHeaders headers, @NotNull TagInfoset bodyTag, @NotNull XMLStreamReader reader, @NotNull SOAPVersion soapVersion) {
        this(envelopeTag, headerTag, attachmentSet, headers, null, bodyTag, null, reader, soapVersion);
    }

    public StreamMessage(@NotNull TagInfoset envelopeTag, @Nullable TagInfoset headerTag, @NotNull AttachmentSet attachmentSet, @Nullable MessageHeaders headers, @Nullable String bodyPrologue, @NotNull TagInfoset bodyTag, @Nullable String bodyEpilogue, @NotNull XMLStreamReader reader, @NotNull SOAPVersion soapVersion) {
        super(soapVersion);
        this.bodyPrologue = null;
        this.bodyEpilogue = null;
        init(envelopeTag, headerTag, attachmentSet, headers, bodyPrologue, bodyTag, bodyEpilogue, reader, soapVersion);
    }

    private void init(@NotNull TagInfoset envelopeTag, @Nullable TagInfoset headerTag, @NotNull AttachmentSet attachmentSet, @Nullable MessageHeaders headers, @Nullable String bodyPrologue, @NotNull TagInfoset bodyTag, @Nullable String bodyEpilogue, @NotNull XMLStreamReader reader, @NotNull SOAPVersion soapVersion) {
        init(headers, attachmentSet, reader, soapVersion);
        if (envelopeTag == null) {
            throw new IllegalArgumentException("EnvelopeTag TagInfoset cannot be null");
        }
        if (bodyTag == null) {
            throw new IllegalArgumentException("BodyTag TagInfoset cannot be null");
        }
        this.envelopeTag = envelopeTag;
        this.headerTag = headerTag;
        this.bodyTag = bodyTag;
        this.bodyPrologue = bodyPrologue;
        this.bodyEpilogue = bodyEpilogue;
    }

    @Override // com.sun.xml.internal.ws.api.message.Message
    public boolean hasHeaders() {
        if (this.envelopeReader != null) {
            readEnvelope(this);
        }
        return this.headers != null && this.headers.hasHeaders();
    }

    @Override // com.sun.xml.internal.ws.api.message.Message
    public MessageHeaders getHeaders() {
        if (this.envelopeReader != null) {
            readEnvelope(this);
        }
        if (this.headers == null) {
            this.headers = new HeaderList(getSOAPVersion());
        }
        return this.headers;
    }

    @Override // com.sun.xml.internal.ws.api.message.Message
    public String getPayloadLocalPart() {
        if (this.envelopeReader != null) {
            readEnvelope(this);
        }
        return this.payloadLocalName;
    }

    @Override // com.sun.xml.internal.ws.api.message.Message
    public String getPayloadNamespaceURI() {
        if (this.envelopeReader != null) {
            readEnvelope(this);
        }
        return this.payloadNamespaceURI;
    }

    @Override // com.sun.xml.internal.ws.api.message.Message
    public boolean hasPayload() {
        if (this.envelopeReader != null) {
            readEnvelope(this);
        }
        return this.payloadLocalName != null;
    }

    @Override // com.sun.xml.internal.ws.api.message.Message
    public Source readPayloadAsSource() {
        if (hasPayload()) {
            if ($assertionsDisabled || unconsumed()) {
                return new StAXSource(this.reader, true, getInscopeNamespaces());
            }
            throw new AssertionError();
        }
        return null;
    }

    private String[] getInscopeNamespaces() {
        NamespaceSupport nss = new NamespaceSupport();
        nss.pushContext();
        for (int i2 = 0; i2 < this.envelopeTag.ns.length; i2 += 2) {
            nss.declarePrefix(this.envelopeTag.ns[i2], this.envelopeTag.ns[i2 + 1]);
        }
        nss.pushContext();
        for (int i3 = 0; i3 < this.bodyTag.ns.length; i3 += 2) {
            nss.declarePrefix(this.bodyTag.ns[i3], this.bodyTag.ns[i3 + 1]);
        }
        List<String> inscope = new ArrayList<>();
        Enumeration en = nss.getPrefixes();
        while (en.hasMoreElements()) {
            String prefix = (String) en.nextElement2();
            inscope.add(prefix);
            inscope.add(nss.getURI(prefix));
        }
        return (String[]) inscope.toArray(new String[inscope.size()]);
    }

    @Override // com.sun.xml.internal.ws.message.AbstractMessageImpl, com.sun.xml.internal.ws.api.message.Message
    public Object readPayloadAsJAXB(Unmarshaller unmarshaller) throws JAXBException {
        if (!hasPayload()) {
            return null;
        }
        if (!$assertionsDisabled && !unconsumed()) {
            throw new AssertionError();
        }
        if (hasAttachments()) {
            unmarshaller.setAttachmentUnmarshaller(new AttachmentUnmarshallerImpl(getAttachments()));
        }
        try {
            return unmarshaller.unmarshal(this.reader);
        } finally {
            unmarshaller.setAttachmentUnmarshaller(null);
            XMLStreamReaderUtil.readRest(this.reader);
            XMLStreamReaderUtil.close(this.reader);
            XMLStreamReaderFactory.recycle(this.reader);
        }
    }

    @Override // com.sun.xml.internal.ws.message.AbstractMessageImpl, com.sun.xml.internal.ws.api.message.Message
    public <T> T readPayloadAsJAXB(Bridge<T> bridge) throws JAXBException {
        if (!hasPayload()) {
            return null;
        }
        if (!$assertionsDisabled && !unconsumed()) {
            throw new AssertionError();
        }
        T r2 = bridge.unmarshal(this.reader, hasAttachments() ? new AttachmentUnmarshallerImpl(getAttachments()) : null);
        XMLStreamReaderUtil.readRest(this.reader);
        XMLStreamReaderUtil.close(this.reader);
        XMLStreamReaderFactory.recycle(this.reader);
        return r2;
    }

    @Override // com.sun.xml.internal.ws.message.AbstractMessageImpl, com.sun.xml.internal.ws.api.message.Message
    public <T> T readPayloadAsJAXB(XMLBridge<T> bridge) throws JAXBException {
        if (!hasPayload()) {
            return null;
        }
        if (!$assertionsDisabled && !unconsumed()) {
            throw new AssertionError();
        }
        T r2 = bridge.unmarshal(this.reader, hasAttachments() ? new AttachmentUnmarshallerImpl(getAttachments()) : null);
        XMLStreamReaderUtil.readRest(this.reader);
        XMLStreamReaderUtil.close(this.reader);
        XMLStreamReaderFactory.recycle(this.reader);
        return r2;
    }

    @Override // com.sun.xml.internal.ws.api.message.Message
    public void consume() {
        if (!$assertionsDisabled && !unconsumed()) {
            throw new AssertionError();
        }
        XMLStreamReaderUtil.readRest(this.reader);
        XMLStreamReaderUtil.close(this.reader);
        XMLStreamReaderFactory.recycle(this.reader);
    }

    @Override // com.sun.xml.internal.ws.api.message.Message
    public XMLStreamReader readPayload() {
        if (!hasPayload()) {
            return null;
        }
        if ($assertionsDisabled || unconsumed()) {
            return this.reader;
        }
        throw new AssertionError();
    }

    @Override // com.sun.xml.internal.ws.api.message.Message
    public void writePayloadTo(XMLStreamWriter writer) throws XMLStreamException {
        if (this.envelopeReader != null) {
            readEnvelope(this);
        }
        if (!$assertionsDisabled && !unconsumed()) {
            throw new AssertionError();
        }
        if (this.payloadLocalName == null) {
            return;
        }
        if (this.bodyPrologue != null) {
            writer.writeCharacters(this.bodyPrologue);
        }
        XMLStreamReaderToXMLStreamWriter conv = new XMLStreamReaderToXMLStreamWriter();
        while (this.reader.getEventType() != 8) {
            String name = this.reader.getLocalName();
            String nsUri = this.reader.getNamespaceURI();
            if (this.reader.getEventType() == 2) {
                if (isBodyElement(name, nsUri)) {
                    break;
                }
                String whiteSpaces = XMLStreamReaderUtil.nextWhiteSpaceContent(this.reader);
                if (whiteSpaces != null) {
                    this.bodyEpilogue = whiteSpaces;
                    writer.writeCharacters(whiteSpaces);
                }
            } else {
                conv.bridge(this.reader, writer);
            }
        }
        XMLStreamReaderUtil.readRest(this.reader);
        XMLStreamReaderUtil.close(this.reader);
        XMLStreamReaderFactory.recycle(this.reader);
    }

    private boolean isBodyElement(String name, String nsUri) {
        return name.equals("Body") && nsUri.equals(this.soapVersion.nsUri);
    }

    @Override // com.sun.xml.internal.ws.message.AbstractMessageImpl, com.sun.xml.internal.ws.api.message.Message
    public void writeTo(XMLStreamWriter sw) throws XMLStreamException {
        if (this.envelopeReader != null) {
            readEnvelope(this);
        }
        writeEnvelope(sw);
    }

    private void writeEnvelope(XMLStreamWriter writer) throws XMLStreamException {
        if (this.envelopeReader != null) {
            readEnvelope(this);
        }
        writer.writeStartDocument();
        this.envelopeTag.writeStart(writer);
        MessageHeaders hl = getHeaders();
        if (hl.hasHeaders() && this.headerTag == null) {
            this.headerTag = new TagInfoset(this.envelopeTag.nsUri, "Header", this.envelopeTag.prefix, EMPTY_ATTS, new String[0]);
        }
        if (this.headerTag != null) {
            this.headerTag.writeStart(writer);
            if (hl.hasHeaders()) {
                for (Header h2 : hl.asList()) {
                    h2.writeTo(writer);
                }
            }
            writer.writeEndElement();
        }
        this.bodyTag.writeStart(writer);
        if (hasPayload()) {
            writePayloadTo(writer);
        }
        writer.writeEndElement();
        writer.writeEndElement();
        writer.writeEndDocument();
    }

    @Override // com.sun.xml.internal.ws.message.AbstractMessageImpl
    public void writePayloadTo(ContentHandler contentHandler, ErrorHandler errorHandler, boolean fragment) throws SAXException {
        if (this.envelopeReader != null) {
            readEnvelope(this);
        }
        if (!$assertionsDisabled && !unconsumed()) {
            throw new AssertionError();
        }
        try {
            if (this.payloadLocalName == null) {
                return;
            }
            if (this.bodyPrologue != null) {
                char[] chars = this.bodyPrologue.toCharArray();
                contentHandler.characters(chars, 0, chars.length);
            }
            XMLStreamReaderToContentHandler conv = new XMLStreamReaderToContentHandler(this.reader, contentHandler, true, fragment, getInscopeNamespaces());
            while (this.reader.getEventType() != 8) {
                String name = this.reader.getLocalName();
                String nsUri = this.reader.getNamespaceURI();
                if (this.reader.getEventType() == 2) {
                    if (isBodyElement(name, nsUri)) {
                        break;
                    }
                    String whiteSpaces = XMLStreamReaderUtil.nextWhiteSpaceContent(this.reader);
                    if (whiteSpaces != null) {
                        this.bodyEpilogue = whiteSpaces;
                        char[] chars2 = whiteSpaces.toCharArray();
                        contentHandler.characters(chars2, 0, chars2.length);
                    }
                } else {
                    conv.bridge();
                }
            }
            XMLStreamReaderUtil.readRest(this.reader);
            XMLStreamReaderUtil.close(this.reader);
            XMLStreamReaderFactory.recycle(this.reader);
        } catch (XMLStreamException e2) {
            Location loc = e2.getLocation();
            if (loc == null) {
                loc = DummyLocation.INSTANCE;
            }
            SAXParseException x2 = new SAXParseException(e2.getMessage(), loc.getPublicId(), loc.getSystemId(), loc.getLineNumber(), loc.getColumnNumber(), e2);
            errorHandler.error(x2);
        }
    }

    @Override // com.sun.xml.internal.ws.api.message.Message
    public Message copy() {
        if (this.envelopeReader != null) {
            readEnvelope(this);
        }
        try {
            if (!$assertionsDisabled && !unconsumed()) {
                throw new AssertionError();
            }
            this.consumedAt = null;
            MutableXMLStreamBuffer xsb = new MutableXMLStreamBuffer();
            StreamReaderBufferCreator c2 = new StreamReaderBufferCreator(xsb);
            c2.storeElement(this.envelopeTag.nsUri, this.envelopeTag.localName, this.envelopeTag.prefix, this.envelopeTag.ns);
            c2.storeElement(this.bodyTag.nsUri, this.bodyTag.localName, this.bodyTag.prefix, this.bodyTag.ns);
            if (hasPayload()) {
                while (this.reader.getEventType() != 8) {
                    String name = this.reader.getLocalName();
                    String nsUri = this.reader.getNamespaceURI();
                    if (isBodyElement(name, nsUri) || this.reader.getEventType() == 8) {
                        break;
                    }
                    c2.create(this.reader);
                    if (this.reader.isWhiteSpace()) {
                        this.bodyEpilogue = XMLStreamReaderUtil.currentWhiteSpaceContent(this.reader);
                    } else {
                        this.bodyEpilogue = null;
                    }
                }
            }
            c2.storeEndElement();
            c2.storeEndElement();
            c2.storeEndElement();
            XMLStreamReaderUtil.readRest(this.reader);
            XMLStreamReaderUtil.close(this.reader);
            XMLStreamReaderFactory.recycle(this.reader);
            this.reader = xsb.readAsXMLStreamReader();
            XMLStreamReader clone = xsb.readAsXMLStreamReader();
            proceedToRootElement(this.reader);
            proceedToRootElement(clone);
            return new StreamMessage(this.envelopeTag, this.headerTag, this.attachmentSet, HeaderList.copy(this.headers), this.bodyPrologue, this.bodyTag, this.bodyEpilogue, clone, this.soapVersion);
        } catch (XMLStreamException e2) {
            throw new WebServiceException("Failed to copy a message", e2);
        }
    }

    private void proceedToRootElement(XMLStreamReader xsr) throws XMLStreamException {
        if (!$assertionsDisabled && xsr.getEventType() != 7) {
            throw new AssertionError();
        }
        xsr.nextTag();
        xsr.nextTag();
        xsr.nextTag();
        if (!$assertionsDisabled && xsr.getEventType() != 1 && xsr.getEventType() != 2) {
            throw new AssertionError();
        }
    }

    @Override // com.sun.xml.internal.ws.message.AbstractMessageImpl, com.sun.xml.internal.ws.api.message.Message
    public void writeTo(ContentHandler contentHandler, ErrorHandler errorHandler) throws SAXException {
        if (this.envelopeReader != null) {
            readEnvelope(this);
        }
        contentHandler.setDocumentLocator(NULL_LOCATOR);
        contentHandler.startDocument();
        this.envelopeTag.writeStart(contentHandler);
        if (hasHeaders() && this.headerTag == null) {
            this.headerTag = new TagInfoset(this.envelopeTag.nsUri, "Header", this.envelopeTag.prefix, EMPTY_ATTS, new String[0]);
        }
        if (this.headerTag != null) {
            this.headerTag.writeStart(contentHandler);
            if (hasHeaders()) {
                MessageHeaders headers = getHeaders();
                for (Header h2 : headers.asList()) {
                    h2.writeTo(contentHandler, errorHandler);
                }
            }
            this.headerTag.writeEnd(contentHandler);
        }
        this.bodyTag.writeStart(contentHandler);
        writePayloadTo(contentHandler, errorHandler, true);
        this.bodyTag.writeEnd(contentHandler);
        this.envelopeTag.writeEnd(contentHandler);
        contentHandler.endDocument();
    }

    private boolean unconsumed() {
        if (this.payloadLocalName == null) {
            return true;
        }
        if (this.reader.getEventType() != 1) {
            AssertionError error = new AssertionError((Object) "StreamMessage has been already consumed. See the nested exception for where it's consumed");
            error.initCause(this.consumedAt);
            throw error;
        }
        this.consumedAt = new Exception().fillInStackTrace();
        return true;
    }

    public String getBodyPrologue() {
        if (this.envelopeReader != null) {
            readEnvelope(this);
        }
        return this.bodyPrologue;
    }

    public String getBodyEpilogue() {
        if (this.envelopeReader != null) {
            readEnvelope(this);
        }
        return this.bodyEpilogue;
    }

    public XMLStreamReader getReader() {
        if (this.envelopeReader != null) {
            readEnvelope(this);
        }
        if ($assertionsDisabled || unconsumed()) {
            return this.reader;
        }
        throw new AssertionError();
    }

    private static void readEnvelope(StreamMessage message) {
        if (message.envelopeReader == null) {
            return;
        }
        XMLStreamReader reader = message.envelopeReader;
        message.envelopeReader = null;
        SOAPVersion soapVersion = message.soapVersion;
        if (reader.getEventType() != 1) {
            XMLStreamReaderUtil.nextElementContent(reader);
        }
        XMLStreamReaderUtil.verifyReaderState(reader, 1);
        if ("Envelope".equals(reader.getLocalName()) && !soapVersion.nsUri.equals(reader.getNamespaceURI())) {
            throw new VersionMismatchException(soapVersion, soapVersion.nsUri, reader.getNamespaceURI());
        }
        XMLStreamReaderUtil.verifyTag(reader, soapVersion.nsUri, "Envelope");
        TagInfoset envelopeTag = new TagInfoset(reader);
        Map<String, String> namespaces = new HashMap<>();
        for (int i2 = 0; i2 < reader.getNamespaceCount(); i2++) {
            namespaces.put(reader.getNamespacePrefix(i2), reader.getNamespaceURI(i2));
        }
        XMLStreamReaderUtil.nextElementContent(reader);
        XMLStreamReaderUtil.verifyReaderState(reader, 1);
        HeaderList headers = null;
        TagInfoset headerTag = null;
        if (reader.getLocalName().equals("Header") && reader.getNamespaceURI().equals(soapVersion.nsUri)) {
            headerTag = new TagInfoset(reader);
            for (int i3 = 0; i3 < reader.getNamespaceCount(); i3++) {
                namespaces.put(reader.getNamespacePrefix(i3), reader.getNamespaceURI(i3));
            }
            XMLStreamReaderUtil.nextElementContent(reader);
            if (reader.getEventType() == 1) {
                headers = new HeaderList(soapVersion);
                try {
                    StreamHeaderDecoder headerDecoder = SOAPVersion.SOAP_11.equals(soapVersion) ? SOAP11StreamHeaderDecoder : SOAP12StreamHeaderDecoder;
                    cacheHeaders(reader, namespaces, headers, headerDecoder);
                } catch (XMLStreamException e2) {
                    throw new WebServiceException(e2);
                }
            }
            XMLStreamReaderUtil.nextElementContent(reader);
        }
        XMLStreamReaderUtil.verifyTag(reader, soapVersion.nsUri, "Body");
        TagInfoset bodyTag = new TagInfoset(reader);
        String bodyPrologue = XMLStreamReaderUtil.nextWhiteSpaceContent(reader);
        message.init(envelopeTag, headerTag, message.attachmentSet, headers, bodyPrologue, bodyTag, null, reader, soapVersion);
    }

    private static XMLStreamBuffer cacheHeaders(XMLStreamReader reader, Map<String, String> namespaces, HeaderList headers, StreamHeaderDecoder headerDecoder) throws XMLStreamException {
        MutableXMLStreamBuffer buffer = createXMLStreamBuffer();
        StreamReaderBufferCreator creator = new StreamReaderBufferCreator();
        creator.setXMLStreamBuffer(buffer);
        while (reader.getEventType() == 1) {
            Map<String, String> headerBlockNamespaces = namespaces;
            if (reader.getNamespaceCount() > 0) {
                headerBlockNamespaces = new HashMap(namespaces);
                for (int i2 = 0; i2 < reader.getNamespaceCount(); i2++) {
                    headerBlockNamespaces.put(reader.getNamespacePrefix(i2), reader.getNamespaceURI(i2));
                }
            }
            XMLStreamBuffer mark = new XMLStreamBufferMark(headerBlockNamespaces, creator);
            headers.add(headerDecoder.decodeHeader(reader, mark));
            creator.createElementFragment(reader, false);
            if (reader.getEventType() != 1 && reader.getEventType() != 2) {
                XMLStreamReaderUtil.nextElementContent(reader);
            }
        }
        return buffer;
    }

    private static MutableXMLStreamBuffer createXMLStreamBuffer() {
        return new MutableXMLStreamBuffer();
    }
}
