package com.sun.xml.internal.ws.message.stream;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import com.sun.xml.internal.ws.api.SOAPVersion;
import com.sun.xml.internal.ws.api.message.AttachmentSet;
import com.sun.xml.internal.ws.api.message.Message;
import com.sun.xml.internal.ws.api.message.MessageHeaders;
import com.sun.xml.internal.ws.message.AbstractMessageImpl;
import com.sun.xml.internal.ws.message.AttachmentSetImpl;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.transform.Source;
import org.xml.sax.ContentHandler;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;

/* loaded from: rt.jar:com/sun/xml/internal/ws/message/stream/PayloadStreamReaderMessage.class */
public class PayloadStreamReaderMessage extends AbstractMessageImpl {
    private final StreamMessage message;

    public PayloadStreamReaderMessage(XMLStreamReader reader, SOAPVersion soapVer) {
        this(null, reader, new AttachmentSetImpl(), soapVer);
    }

    public PayloadStreamReaderMessage(@Nullable MessageHeaders headers, @NotNull XMLStreamReader reader, @NotNull AttachmentSet attSet, @NotNull SOAPVersion soapVersion) {
        super(soapVersion);
        this.message = new StreamMessage(headers, attSet, reader, soapVersion);
    }

    @Override // com.sun.xml.internal.ws.api.message.Message
    public boolean hasHeaders() {
        return this.message.hasHeaders();
    }

    @Override // com.sun.xml.internal.ws.api.message.Message
    public AttachmentSet getAttachments() {
        return this.message.getAttachments();
    }

    @Override // com.sun.xml.internal.ws.api.message.Message
    public String getPayloadLocalPart() {
        return this.message.getPayloadLocalPart();
    }

    @Override // com.sun.xml.internal.ws.api.message.Message
    public String getPayloadNamespaceURI() {
        return this.message.getPayloadNamespaceURI();
    }

    @Override // com.sun.xml.internal.ws.api.message.Message
    public boolean hasPayload() {
        return true;
    }

    @Override // com.sun.xml.internal.ws.api.message.Message
    public Source readPayloadAsSource() {
        return this.message.readPayloadAsSource();
    }

    @Override // com.sun.xml.internal.ws.api.message.Message
    public XMLStreamReader readPayload() throws XMLStreamException {
        return this.message.readPayload();
    }

    @Override // com.sun.xml.internal.ws.api.message.Message
    public void writePayloadTo(XMLStreamWriter sw) throws XMLStreamException {
        this.message.writePayloadTo(sw);
    }

    @Override // com.sun.xml.internal.ws.message.AbstractMessageImpl, com.sun.xml.internal.ws.api.message.Message
    public <T> T readPayloadAsJAXB(Unmarshaller unmarshaller) throws JAXBException {
        return (T) this.message.readPayloadAsJAXB(unmarshaller);
    }

    @Override // com.sun.xml.internal.ws.message.AbstractMessageImpl, com.sun.xml.internal.ws.api.message.Message
    public void writeTo(ContentHandler contentHandler, ErrorHandler errorHandler) throws SAXException {
        this.message.writeTo(contentHandler, errorHandler);
    }

    @Override // com.sun.xml.internal.ws.message.AbstractMessageImpl
    protected void writePayloadTo(ContentHandler contentHandler, ErrorHandler errorHandler, boolean fragment) throws SAXException {
        this.message.writePayloadTo(contentHandler, errorHandler, fragment);
    }

    @Override // com.sun.xml.internal.ws.api.message.Message
    public Message copy() {
        return this.message.copy();
    }

    @Override // com.sun.xml.internal.ws.api.message.Message
    @NotNull
    public MessageHeaders getHeaders() {
        return this.message.getHeaders();
    }
}
