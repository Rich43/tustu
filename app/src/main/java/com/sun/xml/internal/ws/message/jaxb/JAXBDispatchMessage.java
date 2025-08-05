package com.sun.xml.internal.ws.message.jaxb;

import com.sun.xml.internal.ws.api.SOAPVersion;
import com.sun.xml.internal.ws.api.message.Message;
import com.sun.xml.internal.ws.api.message.MessageHeaders;
import com.sun.xml.internal.ws.message.AbstractMessageImpl;
import com.sun.xml.internal.ws.message.PayloadElementSniffer;
import com.sun.xml.internal.ws.spi.db.BindingContext;
import com.sun.xml.internal.ws.spi.db.XMLBridge;
import com.sun.xml.internal.ws.streaming.MtomStreamWriter;
import com.sun.xml.internal.ws.streaming.XMLStreamWriterUtil;
import java.io.OutputStream;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.attachment.AttachmentMarshaller;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.transform.Source;
import javax.xml.ws.WebServiceException;
import org.xml.sax.ContentHandler;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;

/* loaded from: rt.jar:com/sun/xml/internal/ws/message/jaxb/JAXBDispatchMessage.class */
public class JAXBDispatchMessage extends AbstractMessageImpl {
    private final Object jaxbObject;
    private final XMLBridge bridge;
    private final JAXBContext rawContext;
    private QName payloadQName;

    private JAXBDispatchMessage(JAXBDispatchMessage that) {
        super(that);
        this.jaxbObject = that.jaxbObject;
        this.rawContext = that.rawContext;
        this.bridge = that.bridge;
    }

    public JAXBDispatchMessage(JAXBContext rawContext, Object jaxbObject, SOAPVersion soapVersion) {
        super(soapVersion);
        this.bridge = null;
        this.rawContext = rawContext;
        this.jaxbObject = jaxbObject;
    }

    public JAXBDispatchMessage(BindingContext context, Object jaxbObject, SOAPVersion soapVersion) {
        super(soapVersion);
        this.bridge = context.createFragmentBridge();
        this.rawContext = null;
        this.jaxbObject = jaxbObject;
    }

    @Override // com.sun.xml.internal.ws.message.AbstractMessageImpl
    protected void writePayloadTo(ContentHandler contentHandler, ErrorHandler errorHandler, boolean fragment) throws SAXException {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.xml.internal.ws.api.message.Message
    public boolean hasHeaders() {
        return false;
    }

    @Override // com.sun.xml.internal.ws.api.message.Message
    public MessageHeaders getHeaders() {
        return null;
    }

    @Override // com.sun.xml.internal.ws.api.message.Message
    public String getPayloadLocalPart() {
        if (this.payloadQName == null) {
            readPayloadElement();
        }
        return this.payloadQName.getLocalPart();
    }

    @Override // com.sun.xml.internal.ws.api.message.Message
    public String getPayloadNamespaceURI() {
        if (this.payloadQName == null) {
            readPayloadElement();
        }
        return this.payloadQName.getNamespaceURI();
    }

    private void readPayloadElement() {
        PayloadElementSniffer sniffer = new PayloadElementSniffer();
        try {
            if (this.rawContext != null) {
                Marshaller m2 = this.rawContext.createMarshaller();
                m2.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.FALSE);
                m2.marshal(this.jaxbObject, sniffer);
            } else {
                this.bridge.marshal((XMLBridge) this.jaxbObject, (ContentHandler) sniffer, (AttachmentMarshaller) null);
            }
        } catch (JAXBException e2) {
            this.payloadQName = sniffer.getPayloadQName();
        }
    }

    @Override // com.sun.xml.internal.ws.api.message.Message
    public boolean hasPayload() {
        return true;
    }

    @Override // com.sun.xml.internal.ws.api.message.Message
    public Source readPayloadAsSource() {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.xml.internal.ws.api.message.Message
    public XMLStreamReader readPayload() throws XMLStreamException {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.xml.internal.ws.api.message.Message
    public void writePayloadTo(XMLStreamWriter sw) throws XMLStreamException {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.xml.internal.ws.api.message.Message
    public Message copy() {
        return new JAXBDispatchMessage(this);
    }

    @Override // com.sun.xml.internal.ws.message.AbstractMessageImpl, com.sun.xml.internal.ws.api.message.Message
    public void writeTo(XMLStreamWriter sw) throws XMLStreamException {
        try {
            AttachmentMarshaller am2 = sw instanceof MtomStreamWriter ? ((MtomStreamWriter) sw).getAttachmentMarshaller() : new AttachmentMarshallerImpl(this.attachmentSet);
            String encoding = XMLStreamWriterUtil.getEncoding(sw);
            OutputStream os = this.bridge.supportOutputStream() ? XMLStreamWriterUtil.getOutputStream(sw) : null;
            if (this.rawContext != null) {
                Marshaller m2 = this.rawContext.createMarshaller();
                m2.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.FALSE);
                m2.setAttachmentMarshaller(am2);
                if (os != null) {
                    m2.marshal(this.jaxbObject, os);
                } else {
                    m2.marshal(this.jaxbObject, sw);
                }
            } else if (os != null && encoding != null && encoding.equalsIgnoreCase("utf-8")) {
                this.bridge.marshal(this.jaxbObject, os, sw.getNamespaceContext(), am2);
            } else {
                this.bridge.marshal((XMLBridge) this.jaxbObject, sw, am2);
            }
        } catch (JAXBException e2) {
            throw new WebServiceException(e2);
        }
    }
}
