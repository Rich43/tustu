package com.sun.xml.internal.ws.api.message;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import com.sun.xml.internal.bind.api.Bridge;
import com.sun.xml.internal.ws.api.SOAPVersion;
import com.sun.xml.internal.ws.api.WSBinding;
import com.sun.xml.internal.ws.api.addressing.AddressingVersion;
import com.sun.xml.internal.ws.api.model.wsdl.WSDLPort;
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

/* loaded from: rt.jar:com/sun/xml/internal/ws/api/message/FilterMessageImpl.class */
public class FilterMessageImpl extends Message {
    private final Message delegate;

    protected FilterMessageImpl(Message delegate) {
        this.delegate = delegate;
    }

    @Override // com.sun.xml.internal.ws.api.message.Message
    public boolean hasHeaders() {
        return this.delegate.hasHeaders();
    }

    @Override // com.sun.xml.internal.ws.api.message.Message
    @NotNull
    public MessageHeaders getHeaders() {
        return this.delegate.getHeaders();
    }

    @Override // com.sun.xml.internal.ws.api.message.Message
    @NotNull
    public AttachmentSet getAttachments() {
        return this.delegate.getAttachments();
    }

    @Override // com.sun.xml.internal.ws.api.message.Message
    protected boolean hasAttachments() {
        return this.delegate.hasAttachments();
    }

    @Override // com.sun.xml.internal.ws.api.message.Message
    public boolean isOneWay(@NotNull WSDLPort port) {
        return this.delegate.isOneWay(port);
    }

    @Override // com.sun.xml.internal.ws.api.message.Message
    @Nullable
    public String getPayloadLocalPart() {
        return this.delegate.getPayloadLocalPart();
    }

    @Override // com.sun.xml.internal.ws.api.message.Message
    public String getPayloadNamespaceURI() {
        return this.delegate.getPayloadNamespaceURI();
    }

    @Override // com.sun.xml.internal.ws.api.message.Message
    public boolean hasPayload() {
        return this.delegate.hasPayload();
    }

    @Override // com.sun.xml.internal.ws.api.message.Message
    public boolean isFault() {
        return this.delegate.isFault();
    }

    @Override // com.sun.xml.internal.ws.api.message.Message
    @Nullable
    public QName getFirstDetailEntryName() {
        return this.delegate.getFirstDetailEntryName();
    }

    @Override // com.sun.xml.internal.ws.api.message.Message
    public Source readEnvelopeAsSource() {
        return this.delegate.readEnvelopeAsSource();
    }

    @Override // com.sun.xml.internal.ws.api.message.Message
    public Source readPayloadAsSource() {
        return this.delegate.readPayloadAsSource();
    }

    @Override // com.sun.xml.internal.ws.api.message.Message
    public SOAPMessage readAsSOAPMessage() throws SOAPException {
        return this.delegate.readAsSOAPMessage();
    }

    @Override // com.sun.xml.internal.ws.api.message.Message
    public SOAPMessage readAsSOAPMessage(Packet packet, boolean inbound) throws SOAPException {
        return this.delegate.readAsSOAPMessage(packet, inbound);
    }

    @Override // com.sun.xml.internal.ws.api.message.Message
    public <T> T readPayloadAsJAXB(Unmarshaller unmarshaller) throws JAXBException {
        return (T) this.delegate.readPayloadAsJAXB(unmarshaller);
    }

    @Override // com.sun.xml.internal.ws.api.message.Message
    public <T> T readPayloadAsJAXB(Bridge<T> bridge) throws JAXBException {
        return (T) this.delegate.readPayloadAsJAXB(bridge);
    }

    @Override // com.sun.xml.internal.ws.api.message.Message
    public <T> T readPayloadAsJAXB(XMLBridge<T> xMLBridge) throws JAXBException {
        return (T) this.delegate.readPayloadAsJAXB(xMLBridge);
    }

    @Override // com.sun.xml.internal.ws.api.message.Message
    public XMLStreamReader readPayload() throws XMLStreamException {
        return this.delegate.readPayload();
    }

    @Override // com.sun.xml.internal.ws.api.message.Message
    public void consume() {
        this.delegate.consume();
    }

    @Override // com.sun.xml.internal.ws.api.message.Message
    public void writePayloadTo(XMLStreamWriter sw) throws XMLStreamException {
        this.delegate.writePayloadTo(sw);
    }

    @Override // com.sun.xml.internal.ws.api.message.Message
    public void writeTo(XMLStreamWriter sw) throws XMLStreamException {
        this.delegate.writeTo(sw);
    }

    @Override // com.sun.xml.internal.ws.api.message.Message
    public void writeTo(ContentHandler contentHandler, ErrorHandler errorHandler) throws SAXException {
        this.delegate.writeTo(contentHandler, errorHandler);
    }

    @Override // com.sun.xml.internal.ws.api.message.Message
    public Message copy() {
        return this.delegate.copy();
    }

    @Override // com.sun.xml.internal.ws.api.message.Message
    @NotNull
    public String getID(@NotNull WSBinding binding) {
        return this.delegate.getID(binding);
    }

    @Override // com.sun.xml.internal.ws.api.message.Message
    @NotNull
    public String getID(AddressingVersion av2, SOAPVersion sv) {
        return this.delegate.getID(av2, sv);
    }

    @Override // com.sun.xml.internal.ws.api.message.Message
    public SOAPVersion getSOAPVersion() {
        return this.delegate.getSOAPVersion();
    }
}
