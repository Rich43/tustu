package com.sun.xml.internal.ws.message;

import com.sun.istack.internal.NotNull;
import com.sun.xml.internal.ws.api.SOAPVersion;
import com.sun.xml.internal.ws.api.message.AttachmentSet;
import com.sun.xml.internal.ws.api.message.HeaderList;
import com.sun.xml.internal.ws.api.message.Message;
import com.sun.xml.internal.ws.api.message.MessageHeaders;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.transform.Source;
import org.xml.sax.ContentHandler;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;

/* loaded from: rt.jar:com/sun/xml/internal/ws/message/EmptyMessageImpl.class */
public class EmptyMessageImpl extends AbstractMessageImpl {
    private final MessageHeaders headers;
    private final AttachmentSet attachmentSet;

    public EmptyMessageImpl(SOAPVersion version) {
        super(version);
        this.headers = new HeaderList(version);
        this.attachmentSet = new AttachmentSetImpl();
    }

    public EmptyMessageImpl(MessageHeaders headers, @NotNull AttachmentSet attachmentSet, SOAPVersion version) {
        super(version);
        headers = headers == null ? new HeaderList(version) : headers;
        this.attachmentSet = attachmentSet;
        this.headers = headers;
    }

    private EmptyMessageImpl(EmptyMessageImpl that) {
        super(that);
        this.headers = new HeaderList(that.headers);
        this.attachmentSet = that.attachmentSet;
    }

    @Override // com.sun.xml.internal.ws.api.message.Message
    public boolean hasHeaders() {
        return this.headers.hasHeaders();
    }

    @Override // com.sun.xml.internal.ws.api.message.Message
    public MessageHeaders getHeaders() {
        return this.headers;
    }

    @Override // com.sun.xml.internal.ws.api.message.Message
    public String getPayloadLocalPart() {
        return null;
    }

    @Override // com.sun.xml.internal.ws.api.message.Message
    public String getPayloadNamespaceURI() {
        return null;
    }

    @Override // com.sun.xml.internal.ws.api.message.Message
    public boolean hasPayload() {
        return false;
    }

    @Override // com.sun.xml.internal.ws.api.message.Message
    public Source readPayloadAsSource() {
        return null;
    }

    @Override // com.sun.xml.internal.ws.api.message.Message
    public XMLStreamReader readPayload() throws XMLStreamException {
        return null;
    }

    @Override // com.sun.xml.internal.ws.api.message.Message
    public void writePayloadTo(XMLStreamWriter sw) throws XMLStreamException {
    }

    @Override // com.sun.xml.internal.ws.message.AbstractMessageImpl
    public void writePayloadTo(ContentHandler contentHandler, ErrorHandler errorHandler, boolean fragment) throws SAXException {
    }

    @Override // com.sun.xml.internal.ws.api.message.Message
    public Message copy() {
        return new EmptyMessageImpl(this);
    }
}
