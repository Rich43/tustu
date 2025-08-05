package com.sun.xml.internal.ws.message.saaj;

import com.sun.istack.internal.FragmentContentHandler;
import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import com.sun.istack.internal.XMLStreamException2;
import com.sun.xml.internal.bind.api.Bridge;
import com.sun.xml.internal.bind.unmarshaller.DOMScanner;
import com.sun.xml.internal.ws.api.SOAPVersion;
import com.sun.xml.internal.ws.api.message.Attachment;
import com.sun.xml.internal.ws.api.message.AttachmentEx;
import com.sun.xml.internal.ws.api.message.AttachmentSet;
import com.sun.xml.internal.ws.api.message.Header;
import com.sun.xml.internal.ws.api.message.HeaderList;
import com.sun.xml.internal.ws.api.message.Message;
import com.sun.xml.internal.ws.api.message.MessageHeaders;
import com.sun.xml.internal.ws.message.AttachmentUnmarshallerImpl;
import com.sun.xml.internal.ws.spi.db.XMLBridge;
import com.sun.xml.internal.ws.streaming.DOMStreamReader;
import com.sun.xml.internal.ws.util.ASCIIUtility;
import com.sun.xml.internal.ws.util.DOMUtil;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.activation.DataHandler;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.soap.AttachmentPart;
import javax.xml.soap.MimeHeader;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPHeaderElement;
import javax.xml.soap.SOAPMessage;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamSource;
import javax.xml.ws.WebServiceException;
import org.icepdf.core.util.PdfOps;
import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.xml.sax.ContentHandler;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;
import org.xml.sax.helpers.LocatorImpl;

/* loaded from: rt.jar:com/sun/xml/internal/ws/message/saaj/SAAJMessage.class */
public class SAAJMessage extends Message {
    private boolean parsedMessage;
    private boolean accessedMessage;
    private final SOAPMessage sm;
    private MessageHeaders headers;
    private List<Element> bodyParts;
    private Element payload;
    private String payloadLocalName;
    private String payloadNamespace;
    private SOAPVersion soapVersion;
    private NamedNodeMap bodyAttrs;
    private NamedNodeMap headerAttrs;
    private NamedNodeMap envelopeAttrs;
    private static final AttributesImpl EMPTY_ATTS = new AttributesImpl();
    private static final LocatorImpl NULL_LOCATOR = new LocatorImpl();
    private XMLStreamReader soapBodyFirstChildReader;
    private SOAPElement soapBodyFirstChild;

    public SAAJMessage(SOAPMessage sm) {
        this.sm = sm;
    }

    private SAAJMessage(MessageHeaders headers, AttachmentSet as2, SOAPMessage sm, SOAPVersion version) {
        this.sm = sm;
        parse();
        this.headers = headers == null ? new HeaderList(version) : headers;
        this.attachmentSet = as2;
    }

    private void parse() {
        if (!this.parsedMessage) {
            try {
                access();
                if (this.headers == null) {
                    this.headers = new HeaderList(getSOAPVersion());
                }
                SOAPHeader header = this.sm.getSOAPHeader();
                if (header != null) {
                    this.headerAttrs = header.getAttributes();
                    Iterator iter = header.examineAllHeaderElements();
                    while (iter.hasNext()) {
                        this.headers.add(new SAAJHeader((SOAPHeaderElement) iter.next()));
                    }
                }
                this.attachmentSet = new SAAJAttachmentSet(this.sm);
                this.parsedMessage = true;
            } catch (SOAPException e2) {
                throw new WebServiceException(e2);
            }
        }
    }

    protected void access() {
        if (!this.accessedMessage) {
            try {
                this.envelopeAttrs = this.sm.getSOAPPart().getEnvelope().getAttributes();
                Node body = this.sm.getSOAPBody();
                this.bodyAttrs = body.getAttributes();
                this.soapVersion = SOAPVersion.fromNsUri(body.getNamespaceURI());
                this.bodyParts = DOMUtil.getChildElements(body);
                this.payload = this.bodyParts.size() > 0 ? this.bodyParts.get(0) : null;
                if (this.payload != null) {
                    this.payloadLocalName = this.payload.getLocalName();
                    this.payloadNamespace = this.payload.getNamespaceURI();
                }
                this.accessedMessage = true;
            } catch (SOAPException e2) {
                throw new WebServiceException(e2);
            }
        }
    }

    @Override // com.sun.xml.internal.ws.api.message.Message
    public boolean hasHeaders() {
        parse();
        return this.headers.hasHeaders();
    }

    @Override // com.sun.xml.internal.ws.api.message.Message
    @NotNull
    public MessageHeaders getHeaders() {
        parse();
        return this.headers;
    }

    @Override // com.sun.xml.internal.ws.api.message.Message
    @NotNull
    public AttachmentSet getAttachments() {
        if (this.attachmentSet == null) {
            this.attachmentSet = new SAAJAttachmentSet(this.sm);
        }
        return this.attachmentSet;
    }

    @Override // com.sun.xml.internal.ws.api.message.Message
    protected boolean hasAttachments() {
        return !getAttachments().isEmpty();
    }

    @Override // com.sun.xml.internal.ws.api.message.Message
    @Nullable
    public String getPayloadLocalPart() {
        soapBodyFirstChild();
        return this.payloadLocalName;
    }

    @Override // com.sun.xml.internal.ws.api.message.Message
    public String getPayloadNamespaceURI() {
        soapBodyFirstChild();
        return this.payloadNamespace;
    }

    @Override // com.sun.xml.internal.ws.api.message.Message
    public boolean hasPayload() {
        return soapBodyFirstChild() != null;
    }

    private void addAttributes(Element e2, NamedNodeMap attrs) throws DOMException {
        if (attrs == null) {
            return;
        }
        String elPrefix = e2.getPrefix();
        for (int i2 = 0; i2 < attrs.getLength(); i2++) {
            Attr a2 = (Attr) attrs.item(i2);
            if ("xmlns".equals(a2.getPrefix()) || "xmlns".equals(a2.getLocalName())) {
                if ((elPrefix != null || !a2.getLocalName().equals("xmlns")) && (elPrefix == null || !"xmlns".equals(a2.getPrefix()) || !elPrefix.equals(a2.getLocalName()))) {
                    e2.setAttributeNS(a2.getNamespaceURI(), a2.getName(), a2.getValue());
                }
            } else {
                e2.setAttributeNS(a2.getNamespaceURI(), a2.getName(), a2.getValue());
            }
        }
    }

    @Override // com.sun.xml.internal.ws.api.message.Message
    public Source readEnvelopeAsSource() throws DOMException {
        try {
            if (!this.parsedMessage) {
                SOAPEnvelope se = this.sm.getSOAPPart().getEnvelope();
                return new DOMSource(se);
            }
            SOAPMessage msg = this.soapVersion.getMessageFactory().createMessage();
            addAttributes(msg.getSOAPPart().getEnvelope(), this.envelopeAttrs);
            SOAPBody newBody = msg.getSOAPPart().getEnvelope().getBody();
            addAttributes(newBody, this.bodyAttrs);
            for (Element part : this.bodyParts) {
                Node n2 = newBody.getOwnerDocument().importNode(part, true);
                newBody.appendChild(n2);
            }
            addAttributes(msg.getSOAPHeader(), this.headerAttrs);
            for (Header header : this.headers.asList()) {
                header.writeTo(msg);
            }
            SOAPEnvelope se2 = msg.getSOAPPart().getEnvelope();
            return new DOMSource(se2);
        } catch (SOAPException e2) {
            throw new WebServiceException(e2);
        }
    }

    @Override // com.sun.xml.internal.ws.api.message.Message
    public SOAPMessage readAsSOAPMessage() throws SOAPException, DOMException {
        if (!this.parsedMessage) {
            return this.sm;
        }
        SOAPMessage msg = this.soapVersion.getMessageFactory().createMessage();
        addAttributes(msg.getSOAPPart().getEnvelope(), this.envelopeAttrs);
        SOAPBody newBody = msg.getSOAPPart().getEnvelope().getBody();
        addAttributes(newBody, this.bodyAttrs);
        Iterator<Element> it = this.bodyParts.iterator();
        while (it.hasNext()) {
            Node n2 = newBody.getOwnerDocument().importNode(it.next(), true);
            newBody.appendChild(n2);
        }
        addAttributes(msg.getSOAPHeader(), this.headerAttrs);
        for (Header header : this.headers.asList()) {
            header.writeTo(msg);
        }
        for (Attachment att : getAttachments()) {
            AttachmentPart part = msg.createAttachmentPart();
            part.setDataHandler(att.asDataHandler());
            part.setContentId('<' + att.getContentId() + '>');
            addCustomMimeHeaders(att, part);
            msg.addAttachmentPart(part);
        }
        msg.saveChanges();
        return msg;
    }

    private void addCustomMimeHeaders(Attachment att, AttachmentPart part) {
        if (att instanceof AttachmentEx) {
            Iterator<AttachmentEx.MimeHeader> allMimeHeaders = ((AttachmentEx) att).getMimeHeaders();
            while (allMimeHeaders.hasNext()) {
                AttachmentEx.MimeHeader mh = allMimeHeaders.next();
                String name = mh.getName();
                if (!"Content-Type".equalsIgnoreCase(name) && !"Content-Id".equalsIgnoreCase(name)) {
                    part.addMimeHeader(name, mh.getValue());
                }
            }
        }
    }

    @Override // com.sun.xml.internal.ws.api.message.Message
    public Source readPayloadAsSource() {
        access();
        if (this.payload != null) {
            return new DOMSource(this.payload);
        }
        return null;
    }

    @Override // com.sun.xml.internal.ws.api.message.Message
    public <T> T readPayloadAsJAXB(Unmarshaller unmarshaller) throws JAXBException {
        access();
        if (this.payload != null) {
            if (hasAttachments()) {
                unmarshaller.setAttachmentUnmarshaller(new AttachmentUnmarshallerImpl(getAttachments()));
            }
            return (T) unmarshaller.unmarshal(this.payload);
        }
        return null;
    }

    @Override // com.sun.xml.internal.ws.api.message.Message
    public <T> T readPayloadAsJAXB(Bridge<T> bridge) throws JAXBException {
        access();
        if (this.payload != null) {
            return bridge.unmarshal(this.payload, hasAttachments() ? new AttachmentUnmarshallerImpl(getAttachments()) : null);
        }
        return null;
    }

    @Override // com.sun.xml.internal.ws.api.message.Message
    public <T> T readPayloadAsJAXB(XMLBridge<T> bridge) throws JAXBException {
        access();
        if (this.payload != null) {
            return bridge.unmarshal(this.payload, hasAttachments() ? new AttachmentUnmarshallerImpl(getAttachments()) : null);
        }
        return null;
    }

    @Override // com.sun.xml.internal.ws.api.message.Message
    public XMLStreamReader readPayload() throws XMLStreamException {
        return soapBodyFirstChildReader();
    }

    @Override // com.sun.xml.internal.ws.api.message.Message
    public void writePayloadTo(XMLStreamWriter sw) throws XMLStreamException {
        access();
        try {
            for (Element part : this.bodyParts) {
                DOMUtil.serializeNode(part, sw);
            }
        } catch (XMLStreamException e2) {
            throw new WebServiceException(e2);
        }
    }

    @Override // com.sun.xml.internal.ws.api.message.Message
    public void writeTo(XMLStreamWriter writer) throws XMLStreamException {
        try {
            writer.writeStartDocument();
            if (!this.parsedMessage) {
                DOMUtil.serializeNode(this.sm.getSOAPPart().getEnvelope(), writer);
            } else {
                SOAPEnvelope env = this.sm.getSOAPPart().getEnvelope();
                DOMUtil.writeTagWithAttributes(env, writer);
                if (hasHeaders()) {
                    if (env.getHeader() != null) {
                        DOMUtil.writeTagWithAttributes(env.getHeader(), writer);
                    } else {
                        writer.writeStartElement(env.getPrefix(), "Header", env.getNamespaceURI());
                    }
                    for (Header h2 : this.headers.asList()) {
                        h2.writeTo(writer);
                    }
                    writer.writeEndElement();
                }
                DOMUtil.serializeNode(this.sm.getSOAPBody(), writer);
                writer.writeEndElement();
            }
            writer.writeEndDocument();
            writer.flush();
        } catch (SOAPException ex) {
            throw new XMLStreamException2(ex);
        }
    }

    @Override // com.sun.xml.internal.ws.api.message.Message
    public void writeTo(ContentHandler contentHandler, ErrorHandler errorHandler) throws SAXException {
        String soapNsUri = this.soapVersion.nsUri;
        if (!this.parsedMessage) {
            DOMScanner ds = new DOMScanner();
            ds.setContentHandler(contentHandler);
            ds.scan((Document) this.sm.getSOAPPart());
            return;
        }
        contentHandler.setDocumentLocator(NULL_LOCATOR);
        contentHandler.startDocument();
        contentHandler.startPrefixMapping(PdfOps.S_TOKEN, soapNsUri);
        startPrefixMapping(contentHandler, this.envelopeAttrs, PdfOps.S_TOKEN);
        contentHandler.startElement(soapNsUri, "Envelope", "S:Envelope", getAttributes(this.envelopeAttrs));
        if (hasHeaders()) {
            startPrefixMapping(contentHandler, this.headerAttrs, PdfOps.S_TOKEN);
            contentHandler.startElement(soapNsUri, "Header", "S:Header", getAttributes(this.headerAttrs));
            MessageHeaders headers = getHeaders();
            for (Header h2 : headers.asList()) {
                h2.writeTo(contentHandler, errorHandler);
            }
            endPrefixMapping(contentHandler, this.headerAttrs, PdfOps.S_TOKEN);
            contentHandler.endElement(soapNsUri, "Header", "S:Header");
        }
        startPrefixMapping(contentHandler, this.bodyAttrs, PdfOps.S_TOKEN);
        contentHandler.startElement(soapNsUri, "Body", "S:Body", getAttributes(this.bodyAttrs));
        writePayloadTo(contentHandler, errorHandler, true);
        endPrefixMapping(contentHandler, this.bodyAttrs, PdfOps.S_TOKEN);
        contentHandler.endElement(soapNsUri, "Body", "S:Body");
        endPrefixMapping(contentHandler, this.envelopeAttrs, PdfOps.S_TOKEN);
        contentHandler.endElement(soapNsUri, "Envelope", "S:Envelope");
    }

    private AttributesImpl getAttributes(NamedNodeMap attrs) {
        AttributesImpl atts = new AttributesImpl();
        if (attrs == null) {
            return EMPTY_ATTS;
        }
        for (int i2 = 0; i2 < attrs.getLength(); i2++) {
            Attr a2 = (Attr) attrs.item(i2);
            if (!"xmlns".equals(a2.getPrefix()) && !"xmlns".equals(a2.getLocalName())) {
                atts.addAttribute(fixNull(a2.getNamespaceURI()), a2.getLocalName(), a2.getName(), a2.getSchemaTypeInfo().getTypeName(), a2.getValue());
            }
        }
        return atts;
    }

    private void startPrefixMapping(ContentHandler contentHandler, NamedNodeMap attrs, String excludePrefix) throws SAXException {
        if (attrs == null) {
            return;
        }
        for (int i2 = 0; i2 < attrs.getLength(); i2++) {
            Attr a2 = (Attr) attrs.item(i2);
            if (("xmlns".equals(a2.getPrefix()) || "xmlns".equals(a2.getLocalName())) && !fixNull(a2.getPrefix()).equals(excludePrefix)) {
                contentHandler.startPrefixMapping(fixNull(a2.getPrefix()), a2.getNamespaceURI());
            }
        }
    }

    private void endPrefixMapping(ContentHandler contentHandler, NamedNodeMap attrs, String excludePrefix) throws SAXException {
        if (attrs == null) {
            return;
        }
        for (int i2 = 0; i2 < attrs.getLength(); i2++) {
            Attr a2 = (Attr) attrs.item(i2);
            if (("xmlns".equals(a2.getPrefix()) || "xmlns".equals(a2.getLocalName())) && !fixNull(a2.getPrefix()).equals(excludePrefix)) {
                contentHandler.endPrefixMapping(fixNull(a2.getPrefix()));
            }
        }
    }

    private static String fixNull(String s2) {
        return s2 == null ? "" : s2;
    }

    private void writePayloadTo(ContentHandler contentHandler, ErrorHandler errorHandler, boolean fragment) throws SAXException {
        if (fragment) {
            contentHandler = new FragmentContentHandler(contentHandler);
        }
        DOMScanner ds = new DOMScanner();
        ds.setContentHandler(contentHandler);
        ds.scan(this.payload);
    }

    @Override // com.sun.xml.internal.ws.api.message.Message
    public Message copy() throws DOMException {
        try {
            if (!this.parsedMessage) {
                return new SAAJMessage(readAsSOAPMessage());
            }
            SOAPMessage msg = this.soapVersion.getMessageFactory().createMessage();
            SOAPBody newBody = msg.getSOAPPart().getEnvelope().getBody();
            for (Element part : this.bodyParts) {
                Node n2 = newBody.getOwnerDocument().importNode(part, true);
                newBody.appendChild(n2);
            }
            addAttributes(newBody, this.bodyAttrs);
            return new SAAJMessage(getHeaders(), getAttachments(), msg, this.soapVersion);
        } catch (SOAPException e2) {
            throw new WebServiceException(e2);
        }
    }

    /* loaded from: rt.jar:com/sun/xml/internal/ws/message/saaj/SAAJMessage$SAAJAttachment.class */
    protected static class SAAJAttachment implements AttachmentEx {

        /* renamed from: ap, reason: collision with root package name */
        final AttachmentPart f12088ap;
        String contentIdNoAngleBracket;

        public SAAJAttachment(AttachmentPart part) {
            this.f12088ap = part;
        }

        @Override // com.sun.xml.internal.ws.api.message.Attachment
        public String getContentId() {
            if (this.contentIdNoAngleBracket == null) {
                this.contentIdNoAngleBracket = this.f12088ap.getContentId();
                if (this.contentIdNoAngleBracket != null && this.contentIdNoAngleBracket.charAt(0) == '<') {
                    this.contentIdNoAngleBracket = this.contentIdNoAngleBracket.substring(1, this.contentIdNoAngleBracket.length() - 1);
                }
            }
            return this.contentIdNoAngleBracket;
        }

        @Override // com.sun.xml.internal.ws.api.message.Attachment
        public String getContentType() {
            return this.f12088ap.getContentType();
        }

        @Override // com.sun.xml.internal.ws.api.message.Attachment
        public byte[] asByteArray() {
            try {
                return this.f12088ap.getRawContentBytes();
            } catch (SOAPException e2) {
                throw new WebServiceException(e2);
            }
        }

        @Override // com.sun.xml.internal.ws.api.message.Attachment
        public DataHandler asDataHandler() {
            try {
                return this.f12088ap.getDataHandler();
            } catch (SOAPException e2) {
                throw new WebServiceException(e2);
            }
        }

        @Override // com.sun.xml.internal.ws.api.message.Attachment
        public Source asSource() {
            try {
                return new StreamSource(this.f12088ap.getRawContent());
            } catch (SOAPException e2) {
                throw new WebServiceException(e2);
            }
        }

        @Override // com.sun.xml.internal.ws.api.message.Attachment
        public InputStream asInputStream() {
            try {
                return this.f12088ap.getRawContent();
            } catch (SOAPException e2) {
                throw new WebServiceException(e2);
            }
        }

        @Override // com.sun.xml.internal.ws.api.message.Attachment
        public void writeTo(OutputStream os) throws IOException {
            try {
                ASCIIUtility.copyStream(this.f12088ap.getRawContent(), os);
            } catch (SOAPException e2) {
                throw new WebServiceException(e2);
            }
        }

        @Override // com.sun.xml.internal.ws.api.message.Attachment
        public void writeTo(SOAPMessage saaj) {
            saaj.addAttachmentPart(this.f12088ap);
        }

        AttachmentPart asAttachmentPart() {
            return this.f12088ap;
        }

        @Override // com.sun.xml.internal.ws.api.message.AttachmentEx
        public Iterator<AttachmentEx.MimeHeader> getMimeHeaders() {
            final Iterator it = this.f12088ap.getAllMimeHeaders();
            return new Iterator<AttachmentEx.MimeHeader>() { // from class: com.sun.xml.internal.ws.message.saaj.SAAJMessage.SAAJAttachment.1
                @Override // java.util.Iterator
                public boolean hasNext() {
                    return it.hasNext();
                }

                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.util.Iterator
                public AttachmentEx.MimeHeader next() {
                    final MimeHeader mh = (MimeHeader) it.next();
                    return new AttachmentEx.MimeHeader() { // from class: com.sun.xml.internal.ws.message.saaj.SAAJMessage.SAAJAttachment.1.1
                        @Override // com.sun.xml.internal.ws.api.message.AttachmentEx.MimeHeader
                        public String getName() {
                            return mh.getName();
                        }

                        @Override // com.sun.xml.internal.ws.api.message.AttachmentEx.MimeHeader
                        public String getValue() {
                            return mh.getValue();
                        }
                    };
                }

                @Override // java.util.Iterator
                public void remove() {
                    throw new UnsupportedOperationException();
                }
            };
        }
    }

    /* loaded from: rt.jar:com/sun/xml/internal/ws/message/saaj/SAAJMessage$SAAJAttachmentSet.class */
    protected static class SAAJAttachmentSet implements AttachmentSet {
        private Map<String, Attachment> attMap;
        private Iterator attIter;

        public SAAJAttachmentSet(SOAPMessage sm) {
            this.attIter = sm.getAttachments();
        }

        @Override // com.sun.xml.internal.ws.api.message.AttachmentSet
        public Attachment get(String contentId) {
            if (this.attMap == null) {
                if (!this.attIter.hasNext()) {
                    return null;
                }
                this.attMap = createAttachmentMap();
            }
            if (contentId.charAt(0) != '<') {
                return this.attMap.get('<' + contentId + '>');
            }
            return this.attMap.get(contentId);
        }

        @Override // com.sun.xml.internal.ws.api.message.AttachmentSet
        public boolean isEmpty() {
            if (this.attMap != null) {
                return this.attMap.isEmpty();
            }
            return !this.attIter.hasNext();
        }

        @Override // java.lang.Iterable, java.util.List
        public Iterator<Attachment> iterator() {
            if (this.attMap == null) {
                this.attMap = createAttachmentMap();
            }
            return this.attMap.values().iterator();
        }

        private Map<String, Attachment> createAttachmentMap() {
            HashMap<String, Attachment> map = new HashMap<>();
            while (this.attIter.hasNext()) {
                AttachmentPart ap2 = (AttachmentPart) this.attIter.next();
                map.put(ap2.getContentId(), new SAAJAttachment(ap2));
            }
            return map;
        }

        @Override // com.sun.xml.internal.ws.api.message.AttachmentSet
        public void add(Attachment att) {
            this.attMap.put('<' + att.getContentId() + '>', att);
        }
    }

    @Override // com.sun.xml.internal.ws.api.message.Message
    public SOAPVersion getSOAPVersion() {
        return this.soapVersion;
    }

    protected XMLStreamReader getXMLStreamReader(SOAPElement soapElement) {
        return null;
    }

    protected XMLStreamReader createXMLStreamReader(SOAPElement soapElement) {
        DOMStreamReader dss = new DOMStreamReader();
        dss.setCurrentNode(soapElement);
        return dss;
    }

    protected XMLStreamReader soapBodyFirstChildReader() {
        if (this.soapBodyFirstChildReader != null) {
            return this.soapBodyFirstChildReader;
        }
        soapBodyFirstChild();
        if (this.soapBodyFirstChild != null) {
            this.soapBodyFirstChildReader = getXMLStreamReader(this.soapBodyFirstChild);
            if (this.soapBodyFirstChildReader == null) {
                this.soapBodyFirstChildReader = createXMLStreamReader(this.soapBodyFirstChild);
            }
            if (this.soapBodyFirstChildReader.getEventType() == 7) {
                while (this.soapBodyFirstChildReader.getEventType() != 1) {
                    try {
                        this.soapBodyFirstChildReader.next();
                    } catch (XMLStreamException e2) {
                        throw new RuntimeException(e2);
                    }
                }
            }
            return this.soapBodyFirstChildReader;
        }
        this.payloadLocalName = null;
        this.payloadNamespace = null;
        return null;
    }

    SOAPElement soapBodyFirstChild() {
        if (this.soapBodyFirstChild != null) {
            return this.soapBodyFirstChild;
        }
        try {
            boolean foundElement = false;
            for (Node n2 = this.sm.getSOAPBody().getFirstChild(); n2 != null && !foundElement; n2 = n2.getNextSibling()) {
                if (n2.getNodeType() == 1) {
                    foundElement = true;
                    if (n2 instanceof SOAPElement) {
                        this.soapBodyFirstChild = (SOAPElement) n2;
                        this.payloadLocalName = this.soapBodyFirstChild.getLocalName();
                        this.payloadNamespace = this.soapBodyFirstChild.getNamespaceURI();
                        return this.soapBodyFirstChild;
                    }
                }
            }
            if (foundElement) {
                Iterator i2 = this.sm.getSOAPBody().getChildElements();
                while (i2.hasNext()) {
                    Object o2 = i2.next();
                    if (o2 instanceof SOAPElement) {
                        this.soapBodyFirstChild = (SOAPElement) o2;
                        this.payloadLocalName = this.soapBodyFirstChild.getLocalName();
                        this.payloadNamespace = this.soapBodyFirstChild.getNamespaceURI();
                        return this.soapBodyFirstChild;
                    }
                }
            }
            return this.soapBodyFirstChild;
        } catch (SOAPException e2) {
            throw new RuntimeException(e2);
        }
    }
}
