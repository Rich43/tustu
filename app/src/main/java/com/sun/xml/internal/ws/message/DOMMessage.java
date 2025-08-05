package com.sun.xml.internal.ws.message;

import com.sun.istack.internal.FragmentContentHandler;
import com.sun.xml.internal.bind.api.Bridge;
import com.sun.xml.internal.bind.unmarshaller.DOMScanner;
import com.sun.xml.internal.ws.api.SOAPVersion;
import com.sun.xml.internal.ws.api.message.AttachmentSet;
import com.sun.xml.internal.ws.api.message.HeaderList;
import com.sun.xml.internal.ws.api.message.Message;
import com.sun.xml.internal.ws.api.message.MessageHeaders;
import com.sun.xml.internal.ws.streaming.DOMStreamReader;
import com.sun.xml.internal.ws.util.DOMUtil;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMSource;
import javax.xml.ws.WebServiceException;
import org.w3c.dom.Element;
import org.xml.sax.ContentHandler;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;

/* loaded from: rt.jar:com/sun/xml/internal/ws/message/DOMMessage.class */
public final class DOMMessage extends AbstractMessageImpl {
    private MessageHeaders headers;
    private final Element payload;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !DOMMessage.class.desiredAssertionStatus();
    }

    public DOMMessage(SOAPVersion ver, Element payload) {
        this(ver, null, payload);
    }

    public DOMMessage(SOAPVersion ver, MessageHeaders headers, Element payload) {
        this(ver, headers, payload, null);
    }

    public DOMMessage(SOAPVersion ver, MessageHeaders headers, Element payload, AttachmentSet attachments) {
        super(ver);
        this.headers = headers;
        this.payload = payload;
        this.attachmentSet = attachments;
        if (!$assertionsDisabled && payload == null) {
            throw new AssertionError();
        }
    }

    private DOMMessage(DOMMessage that) {
        super(that);
        this.headers = HeaderList.copy(that.headers);
        this.payload = that.payload;
    }

    @Override // com.sun.xml.internal.ws.api.message.Message
    public boolean hasHeaders() {
        return getHeaders().hasHeaders();
    }

    @Override // com.sun.xml.internal.ws.api.message.Message
    public MessageHeaders getHeaders() {
        if (this.headers == null) {
            this.headers = new HeaderList(getSOAPVersion());
        }
        return this.headers;
    }

    @Override // com.sun.xml.internal.ws.api.message.Message
    public String getPayloadLocalPart() {
        return this.payload.getLocalName();
    }

    @Override // com.sun.xml.internal.ws.api.message.Message
    public String getPayloadNamespaceURI() {
        return this.payload.getNamespaceURI();
    }

    @Override // com.sun.xml.internal.ws.api.message.Message
    public boolean hasPayload() {
        return true;
    }

    @Override // com.sun.xml.internal.ws.api.message.Message
    public Source readPayloadAsSource() {
        return new DOMSource(this.payload);
    }

    @Override // com.sun.xml.internal.ws.message.AbstractMessageImpl, com.sun.xml.internal.ws.api.message.Message
    public <T> T readPayloadAsJAXB(Unmarshaller unmarshaller) throws JAXBException {
        if (hasAttachments()) {
            unmarshaller.setAttachmentUnmarshaller(new AttachmentUnmarshallerImpl(getAttachments()));
        }
        try {
            return (T) unmarshaller.unmarshal(this.payload);
        } finally {
            unmarshaller.setAttachmentUnmarshaller(null);
        }
    }

    @Override // com.sun.xml.internal.ws.message.AbstractMessageImpl, com.sun.xml.internal.ws.api.message.Message
    public <T> T readPayloadAsJAXB(Bridge<T> bridge) throws JAXBException {
        return bridge.unmarshal(this.payload, hasAttachments() ? new AttachmentUnmarshallerImpl(getAttachments()) : null);
    }

    @Override // com.sun.xml.internal.ws.api.message.Message
    public XMLStreamReader readPayload() throws XMLStreamException {
        DOMStreamReader dss = new DOMStreamReader();
        dss.setCurrentNode(this.payload);
        dss.nextTag();
        if ($assertionsDisabled || dss.getEventType() == 1) {
            return dss;
        }
        throw new AssertionError();
    }

    @Override // com.sun.xml.internal.ws.api.message.Message
    public void writePayloadTo(XMLStreamWriter sw) {
        try {
            if (this.payload != null) {
                DOMUtil.serializeNode(this.payload, sw);
            }
        } catch (XMLStreamException e2) {
            throw new WebServiceException(e2);
        }
    }

    @Override // com.sun.xml.internal.ws.message.AbstractMessageImpl
    protected void writePayloadTo(ContentHandler contentHandler, ErrorHandler errorHandler, boolean fragment) throws SAXException {
        if (fragment) {
            contentHandler = new FragmentContentHandler(contentHandler);
        }
        DOMScanner ds = new DOMScanner();
        ds.setContentHandler(contentHandler);
        ds.scan(this.payload);
    }

    @Override // com.sun.xml.internal.ws.api.message.Message
    public Message copy() {
        return new DOMMessage(this);
    }
}
