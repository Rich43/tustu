package com.sun.xml.internal.ws.message.source;

import com.sun.xml.internal.bind.api.Bridge;
import com.sun.xml.internal.ws.api.SOAPVersion;
import com.sun.xml.internal.ws.api.message.Message;
import com.sun.xml.internal.ws.api.message.MessageHeaders;
import com.sun.xml.internal.ws.api.message.Packet;
import com.sun.xml.internal.ws.api.pipe.Codecs;
import com.sun.xml.internal.ws.api.pipe.StreamSOAPCodec;
import com.sun.xml.internal.ws.spi.db.XMLBridge;
import com.sun.xml.internal.ws.streaming.SourceReaderFactory;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.transform.Source;
import org.xml.sax.ContentHandler;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;

/* loaded from: rt.jar:com/sun/xml/internal/ws/message/source/ProtocolSourceMessage.class */
public class ProtocolSourceMessage extends Message {
    private final Message sm;

    public ProtocolSourceMessage(Source source, SOAPVersion soapVersion) {
        XMLStreamReader reader = SourceReaderFactory.createSourceReader(source, true);
        StreamSOAPCodec codec = Codecs.createSOAPEnvelopeXmlCodec(soapVersion);
        this.sm = codec.decode(reader);
    }

    @Override // com.sun.xml.internal.ws.api.message.Message
    public boolean hasHeaders() {
        return this.sm.hasHeaders();
    }

    @Override // com.sun.xml.internal.ws.api.message.Message
    public String getPayloadLocalPart() {
        return this.sm.getPayloadLocalPart();
    }

    @Override // com.sun.xml.internal.ws.api.message.Message
    public String getPayloadNamespaceURI() {
        return this.sm.getPayloadNamespaceURI();
    }

    @Override // com.sun.xml.internal.ws.api.message.Message
    public boolean hasPayload() {
        return this.sm.hasPayload();
    }

    @Override // com.sun.xml.internal.ws.api.message.Message
    public Source readPayloadAsSource() {
        return this.sm.readPayloadAsSource();
    }

    @Override // com.sun.xml.internal.ws.api.message.Message
    public XMLStreamReader readPayload() throws XMLStreamException {
        return this.sm.readPayload();
    }

    @Override // com.sun.xml.internal.ws.api.message.Message
    public void writePayloadTo(XMLStreamWriter sw) throws XMLStreamException {
        this.sm.writePayloadTo(sw);
    }

    @Override // com.sun.xml.internal.ws.api.message.Message
    public void writeTo(XMLStreamWriter sw) throws XMLStreamException {
        this.sm.writeTo(sw);
    }

    @Override // com.sun.xml.internal.ws.api.message.Message
    public Message copy() {
        return this.sm.copy();
    }

    @Override // com.sun.xml.internal.ws.api.message.Message
    public Source readEnvelopeAsSource() {
        return this.sm.readEnvelopeAsSource();
    }

    @Override // com.sun.xml.internal.ws.api.message.Message
    public SOAPMessage readAsSOAPMessage() throws SOAPException {
        return this.sm.readAsSOAPMessage();
    }

    @Override // com.sun.xml.internal.ws.api.message.Message
    public SOAPMessage readAsSOAPMessage(Packet packet, boolean inbound) throws SOAPException {
        return this.sm.readAsSOAPMessage(packet, inbound);
    }

    @Override // com.sun.xml.internal.ws.api.message.Message
    public <T> T readPayloadAsJAXB(Unmarshaller unmarshaller) throws JAXBException {
        return (T) this.sm.readPayloadAsJAXB(unmarshaller);
    }

    @Override // com.sun.xml.internal.ws.api.message.Message
    public <T> T readPayloadAsJAXB(Bridge<T> bridge) throws JAXBException {
        return (T) this.sm.readPayloadAsJAXB(bridge);
    }

    @Override // com.sun.xml.internal.ws.api.message.Message
    public <T> T readPayloadAsJAXB(XMLBridge<T> xMLBridge) throws JAXBException {
        return (T) this.sm.readPayloadAsJAXB(xMLBridge);
    }

    @Override // com.sun.xml.internal.ws.api.message.Message
    public void writeTo(ContentHandler contentHandler, ErrorHandler errorHandler) throws SAXException {
        this.sm.writeTo(contentHandler, errorHandler);
    }

    @Override // com.sun.xml.internal.ws.api.message.Message
    public SOAPVersion getSOAPVersion() {
        return this.sm.getSOAPVersion();
    }

    @Override // com.sun.xml.internal.ws.api.message.Message
    public MessageHeaders getHeaders() {
        return this.sm.getHeaders();
    }
}
