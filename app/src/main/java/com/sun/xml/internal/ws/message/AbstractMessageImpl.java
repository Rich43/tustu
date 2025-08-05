package com.sun.xml.internal.ws.message;

import com.sun.istack.internal.NotNull;
import com.sun.xml.internal.bind.api.Bridge;
import com.sun.xml.internal.ws.api.SOAPVersion;
import com.sun.xml.internal.ws.api.message.Header;
import com.sun.xml.internal.ws.api.message.Message;
import com.sun.xml.internal.ws.api.message.MessageHeaders;
import com.sun.xml.internal.ws.api.message.MessageWritable;
import com.sun.xml.internal.ws.api.message.Packet;
import com.sun.xml.internal.ws.api.message.saaj.SAAJFactory;
import com.sun.xml.internal.ws.encoding.TagInfoset;
import com.sun.xml.internal.ws.message.saaj.SAAJMessage;
import com.sun.xml.internal.ws.spi.db.XMLBridge;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.transform.Source;
import javax.xml.transform.sax.SAXSource;
import org.icepdf.core.util.PdfOps;
import org.xml.sax.ContentHandler;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;
import org.xml.sax.helpers.LocatorImpl;

/* loaded from: rt.jar:com/sun/xml/internal/ws/message/AbstractMessageImpl.class */
public abstract class AbstractMessageImpl extends Message {
    protected final SOAPVersion soapVersion;

    @NotNull
    protected TagInfoset envelopeTag;

    @NotNull
    protected TagInfoset headerTag;

    @NotNull
    protected TagInfoset bodyTag;
    protected static final List<TagInfoset> DEFAULT_TAGS;
    protected static final LocatorImpl NULL_LOCATOR = new LocatorImpl();
    protected static final AttributesImpl EMPTY_ATTS = new AttributesImpl();

    protected abstract void writePayloadTo(ContentHandler contentHandler, ErrorHandler errorHandler, boolean z2) throws SAXException;

    static {
        List<TagInfoset> tagList = new ArrayList<>();
        create(SOAPVersion.SOAP_11, tagList);
        create(SOAPVersion.SOAP_12, tagList);
        DEFAULT_TAGS = Collections.unmodifiableList(tagList);
    }

    static void create(SOAPVersion v2, List c2) {
        int base = v2.ordinal() * 3;
        c2.add(base, new TagInfoset(v2.nsUri, "Envelope", PdfOps.S_TOKEN, EMPTY_ATTS, PdfOps.S_TOKEN, v2.nsUri));
        c2.add(base + 1, new TagInfoset(v2.nsUri, "Header", PdfOps.S_TOKEN, EMPTY_ATTS, new String[0]));
        c2.add(base + 2, new TagInfoset(v2.nsUri, "Body", PdfOps.S_TOKEN, EMPTY_ATTS, new String[0]));
    }

    protected AbstractMessageImpl(SOAPVersion soapVersion) {
        this.soapVersion = soapVersion;
    }

    @Override // com.sun.xml.internal.ws.api.message.Message
    public SOAPVersion getSOAPVersion() {
        return this.soapVersion;
    }

    protected AbstractMessageImpl(AbstractMessageImpl that) {
        this.soapVersion = that.soapVersion;
    }

    @Override // com.sun.xml.internal.ws.api.message.Message
    public Source readEnvelopeAsSource() {
        return new SAXSource(new XMLReaderImpl(this), XMLReaderImpl.THE_SOURCE);
    }

    @Override // com.sun.xml.internal.ws.api.message.Message
    public <T> T readPayloadAsJAXB(Unmarshaller unmarshaller) throws JAXBException {
        if (hasAttachments()) {
            unmarshaller.setAttachmentUnmarshaller(new AttachmentUnmarshallerImpl(getAttachments()));
        }
        try {
            return (T) unmarshaller.unmarshal(readPayloadAsSource());
        } finally {
            unmarshaller.setAttachmentUnmarshaller(null);
        }
    }

    @Override // com.sun.xml.internal.ws.api.message.Message
    public <T> T readPayloadAsJAXB(Bridge<T> bridge) throws JAXBException {
        return bridge.unmarshal(readPayloadAsSource(), hasAttachments() ? new AttachmentUnmarshallerImpl(getAttachments()) : null);
    }

    @Override // com.sun.xml.internal.ws.api.message.Message
    public <T> T readPayloadAsJAXB(XMLBridge<T> bridge) throws JAXBException {
        return bridge.unmarshal(readPayloadAsSource(), hasAttachments() ? new AttachmentUnmarshallerImpl(getAttachments()) : null);
    }

    @Override // com.sun.xml.internal.ws.api.message.Message
    public void writeTo(XMLStreamWriter w2) throws XMLStreamException {
        String soapNsUri = this.soapVersion.nsUri;
        w2.writeStartDocument();
        w2.writeStartElement(PdfOps.S_TOKEN, "Envelope", soapNsUri);
        w2.writeNamespace(PdfOps.S_TOKEN, soapNsUri);
        if (hasHeaders()) {
            w2.writeStartElement(PdfOps.S_TOKEN, "Header", soapNsUri);
            MessageHeaders headers = getHeaders();
            for (Header h2 : headers.asList()) {
                h2.writeTo(w2);
            }
            w2.writeEndElement();
        }
        w2.writeStartElement(PdfOps.S_TOKEN, "Body", soapNsUri);
        writePayloadTo(w2);
        w2.writeEndElement();
        w2.writeEndElement();
        w2.writeEndDocument();
    }

    @Override // com.sun.xml.internal.ws.api.message.Message
    public void writeTo(ContentHandler contentHandler, ErrorHandler errorHandler) throws SAXException {
        String soapNsUri = this.soapVersion.nsUri;
        contentHandler.setDocumentLocator(NULL_LOCATOR);
        contentHandler.startDocument();
        contentHandler.startPrefixMapping(PdfOps.S_TOKEN, soapNsUri);
        contentHandler.startElement(soapNsUri, "Envelope", "S:Envelope", EMPTY_ATTS);
        if (hasHeaders()) {
            contentHandler.startElement(soapNsUri, "Header", "S:Header", EMPTY_ATTS);
            MessageHeaders headers = getHeaders();
            for (Header h2 : headers.asList()) {
                h2.writeTo(contentHandler, errorHandler);
            }
            contentHandler.endElement(soapNsUri, "Header", "S:Header");
        }
        contentHandler.startElement(soapNsUri, "Body", "S:Body", EMPTY_ATTS);
        writePayloadTo(contentHandler, errorHandler, true);
        contentHandler.endElement(soapNsUri, "Body", "S:Body");
        contentHandler.endElement(soapNsUri, "Envelope", "S:Envelope");
    }

    /* JADX WARN: Multi-variable type inference failed */
    public Message toSAAJ(Packet p2, Boolean inbound) throws SOAPException {
        SAAJMessage sAAJMessage = SAAJFactory.read(p2);
        if (sAAJMessage instanceof MessageWritable) {
            ((MessageWritable) sAAJMessage).setMTOMConfiguration(p2.getMtomFeature());
        }
        if (inbound != null) {
            transportHeaders(p2, inbound.booleanValue(), sAAJMessage.readAsSOAPMessage());
        }
        return sAAJMessage;
    }

    @Override // com.sun.xml.internal.ws.api.message.Message
    public SOAPMessage readAsSOAPMessage() throws SOAPException {
        return SAAJFactory.read(this.soapVersion, this);
    }

    @Override // com.sun.xml.internal.ws.api.message.Message
    public SOAPMessage readAsSOAPMessage(Packet packet, boolean inbound) throws SOAPException {
        SOAPMessage msg = SAAJFactory.read(this.soapVersion, this, packet);
        transportHeaders(packet, inbound, msg);
        return msg;
    }

    private void transportHeaders(Packet packet, boolean inbound, SOAPMessage msg) throws SOAPException {
        Map<String, List<String>> headers = getTransportHeaders(packet, inbound);
        if (headers != null) {
            addSOAPMimeHeaders(msg.getMimeHeaders(), headers);
        }
        if (msg.saveRequired()) {
            msg.saveChanges();
        }
    }
}
