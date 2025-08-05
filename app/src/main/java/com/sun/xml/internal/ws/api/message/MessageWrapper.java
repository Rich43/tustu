package com.sun.xml.internal.ws.api.message;

import com.sun.istack.internal.NotNull;
import com.sun.xml.internal.bind.api.Bridge;
import com.sun.xml.internal.ws.api.SOAPVersion;
import com.sun.xml.internal.ws.api.WSBinding;
import com.sun.xml.internal.ws.api.addressing.AddressingVersion;
import com.sun.xml.internal.ws.api.model.wsdl.WSDLPort;
import com.sun.xml.internal.ws.message.saaj.SAAJMessage;
import com.sun.xml.internal.ws.message.stream.StreamMessage;
import com.sun.xml.internal.ws.spi.db.XMLBridge;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.namespace.QName;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.transform.Source;
import org.xml.sax.ContentHandler;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;

/* loaded from: rt.jar:com/sun/xml/internal/ws/api/message/MessageWrapper.class */
class MessageWrapper extends StreamMessage {
    Packet packet;
    Message delegate;
    StreamMessage streamDelegate;

    @Override // com.sun.xml.internal.ws.message.stream.StreamMessage, com.sun.xml.internal.ws.message.AbstractMessageImpl
    public void writePayloadTo(ContentHandler contentHandler, ErrorHandler errorHandler, boolean fragment) throws SAXException {
        this.streamDelegate.writePayloadTo(contentHandler, errorHandler, fragment);
    }

    @Override // com.sun.xml.internal.ws.message.stream.StreamMessage
    public String getBodyPrologue() {
        return this.streamDelegate.getBodyPrologue();
    }

    @Override // com.sun.xml.internal.ws.message.stream.StreamMessage
    public String getBodyEpilogue() {
        return this.streamDelegate.getBodyEpilogue();
    }

    MessageWrapper(Packet p2, Message m2) {
        super(m2.getSOAPVersion());
        this.packet = p2;
        this.delegate = m2;
        this.streamDelegate = m2 instanceof StreamMessage ? (StreamMessage) m2 : null;
        setMessageMedadata(p2);
    }

    public int hashCode() {
        return this.delegate.hashCode();
    }

    public boolean equals(Object obj) {
        return this.delegate.equals(obj);
    }

    @Override // com.sun.xml.internal.ws.message.stream.StreamMessage, com.sun.xml.internal.ws.api.message.Message
    public boolean hasHeaders() {
        return this.delegate.hasHeaders();
    }

    @Override // com.sun.xml.internal.ws.api.message.Message
    public AttachmentSet getAttachments() {
        return this.delegate.getAttachments();
    }

    public String toString() {
        return this.delegate.toString();
    }

    @Override // com.sun.xml.internal.ws.api.message.Message
    public boolean isOneWay(WSDLPort port) {
        return this.delegate.isOneWay(port);
    }

    @Override // com.sun.xml.internal.ws.message.stream.StreamMessage, com.sun.xml.internal.ws.api.message.Message
    public String getPayloadLocalPart() {
        return this.delegate.getPayloadLocalPart();
    }

    @Override // com.sun.xml.internal.ws.message.stream.StreamMessage, com.sun.xml.internal.ws.api.message.Message
    public String getPayloadNamespaceURI() {
        return this.delegate.getPayloadNamespaceURI();
    }

    @Override // com.sun.xml.internal.ws.message.stream.StreamMessage, com.sun.xml.internal.ws.api.message.Message
    public boolean hasPayload() {
        return this.delegate.hasPayload();
    }

    @Override // com.sun.xml.internal.ws.api.message.Message
    public boolean isFault() {
        return this.delegate.isFault();
    }

    @Override // com.sun.xml.internal.ws.api.message.Message
    public QName getFirstDetailEntryName() {
        return this.delegate.getFirstDetailEntryName();
    }

    @Override // com.sun.xml.internal.ws.message.AbstractMessageImpl, com.sun.xml.internal.ws.api.message.Message
    public Source readEnvelopeAsSource() {
        return this.delegate.readEnvelopeAsSource();
    }

    @Override // com.sun.xml.internal.ws.message.stream.StreamMessage, com.sun.xml.internal.ws.api.message.Message
    public Source readPayloadAsSource() {
        return this.delegate.readPayloadAsSource();
    }

    @Override // com.sun.xml.internal.ws.message.AbstractMessageImpl, com.sun.xml.internal.ws.api.message.Message
    public SOAPMessage readAsSOAPMessage() throws SOAPException {
        if (!(this.delegate instanceof SAAJMessage)) {
            this.delegate = toSAAJ(this.packet, null);
        }
        return this.delegate.readAsSOAPMessage();
    }

    @Override // com.sun.xml.internal.ws.message.AbstractMessageImpl, com.sun.xml.internal.ws.api.message.Message
    public SOAPMessage readAsSOAPMessage(Packet p2, boolean inbound) throws SOAPException {
        if (!(this.delegate instanceof SAAJMessage)) {
            this.delegate = toSAAJ(p2, Boolean.valueOf(inbound));
        }
        return this.delegate.readAsSOAPMessage();
    }

    @Override // com.sun.xml.internal.ws.message.stream.StreamMessage, com.sun.xml.internal.ws.message.AbstractMessageImpl, com.sun.xml.internal.ws.api.message.Message
    public Object readPayloadAsJAXB(Unmarshaller unmarshaller) throws JAXBException {
        return this.delegate.readPayloadAsJAXB(unmarshaller);
    }

    @Override // com.sun.xml.internal.ws.message.stream.StreamMessage, com.sun.xml.internal.ws.message.AbstractMessageImpl, com.sun.xml.internal.ws.api.message.Message
    public <T> T readPayloadAsJAXB(Bridge<T> bridge) throws JAXBException {
        return (T) this.delegate.readPayloadAsJAXB(bridge);
    }

    @Override // com.sun.xml.internal.ws.message.stream.StreamMessage, com.sun.xml.internal.ws.message.AbstractMessageImpl, com.sun.xml.internal.ws.api.message.Message
    public <T> T readPayloadAsJAXB(XMLBridge<T> xMLBridge) throws JAXBException {
        return (T) this.delegate.readPayloadAsJAXB(xMLBridge);
    }

    @Override // com.sun.xml.internal.ws.message.stream.StreamMessage, com.sun.xml.internal.ws.api.message.Message
    public XMLStreamReader readPayload() {
        try {
            return this.delegate.readPayload();
        } catch (XMLStreamException e2) {
            e2.printStackTrace();
            return null;
        }
    }

    @Override // com.sun.xml.internal.ws.message.stream.StreamMessage, com.sun.xml.internal.ws.api.message.Message
    public void consume() {
        this.delegate.consume();
    }

    @Override // com.sun.xml.internal.ws.message.stream.StreamMessage, com.sun.xml.internal.ws.api.message.Message
    public void writePayloadTo(XMLStreamWriter sw) throws XMLStreamException {
        this.delegate.writePayloadTo(sw);
    }

    @Override // com.sun.xml.internal.ws.message.stream.StreamMessage, com.sun.xml.internal.ws.message.AbstractMessageImpl, com.sun.xml.internal.ws.api.message.Message
    public void writeTo(XMLStreamWriter sw) throws XMLStreamException {
        this.delegate.writeTo(sw);
    }

    @Override // com.sun.xml.internal.ws.message.stream.StreamMessage, com.sun.xml.internal.ws.message.AbstractMessageImpl, com.sun.xml.internal.ws.api.message.Message
    public void writeTo(ContentHandler contentHandler, ErrorHandler errorHandler) throws SAXException {
        this.delegate.writeTo(contentHandler, errorHandler);
    }

    @Override // com.sun.xml.internal.ws.message.stream.StreamMessage, com.sun.xml.internal.ws.api.message.Message
    public Message copy() {
        return this.delegate.copy();
    }

    @Override // com.sun.xml.internal.ws.api.message.Message
    public String getID(WSBinding binding) {
        return this.delegate.getID(binding);
    }

    @Override // com.sun.xml.internal.ws.api.message.Message
    public String getID(AddressingVersion av2, SOAPVersion sv) {
        return this.delegate.getID(av2, sv);
    }

    @Override // com.sun.xml.internal.ws.message.AbstractMessageImpl, com.sun.xml.internal.ws.api.message.Message
    public SOAPVersion getSOAPVersion() {
        return this.delegate.getSOAPVersion();
    }

    @Override // com.sun.xml.internal.ws.message.stream.StreamMessage, com.sun.xml.internal.ws.api.message.Message
    @NotNull
    public MessageHeaders getHeaders() {
        return this.delegate.getHeaders();
    }

    @Override // com.sun.xml.internal.ws.api.message.Message
    public void setMessageMedadata(MessageMetadata metadata) {
        super.setMessageMedadata(metadata);
        this.delegate.setMessageMedadata(metadata);
    }
}
