package com.sun.xml.internal.ws.message.jaxb;

import com.sun.istack.internal.FragmentContentHandler;
import com.sun.xml.internal.stream.buffer.MutableXMLStreamBuffer;
import com.sun.xml.internal.stream.buffer.XMLStreamBuffer;
import com.sun.xml.internal.stream.buffer.XMLStreamBufferResult;
import com.sun.xml.internal.ws.api.SOAPVersion;
import com.sun.xml.internal.ws.api.message.AttachmentSet;
import com.sun.xml.internal.ws.api.message.Header;
import com.sun.xml.internal.ws.api.message.HeaderList;
import com.sun.xml.internal.ws.api.message.Message;
import com.sun.xml.internal.ws.api.message.MessageHeaders;
import com.sun.xml.internal.ws.api.message.StreamingSOAP;
import com.sun.xml.internal.ws.message.AbstractMessageImpl;
import com.sun.xml.internal.ws.message.AttachmentSetImpl;
import com.sun.xml.internal.ws.message.RootElementSniffer;
import com.sun.xml.internal.ws.message.stream.StreamMessage;
import com.sun.xml.internal.ws.spi.db.BindingContext;
import com.sun.xml.internal.ws.spi.db.BindingContextFactory;
import com.sun.xml.internal.ws.spi.db.XMLBridge;
import com.sun.xml.internal.ws.streaming.MtomStreamWriter;
import com.sun.xml.internal.ws.streaming.XMLStreamReaderUtil;
import com.sun.xml.internal.ws.streaming.XMLStreamWriterUtil;
import com.sun.xml.internal.ws.util.xml.XMLReaderComposite;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.attachment.AttachmentMarshaller;
import javax.xml.bind.util.JAXBResult;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.ws.WebServiceException;
import org.xml.sax.ContentHandler;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;

/* loaded from: rt.jar:com/sun/xml/internal/ws/message/jaxb/JAXBMessage.class */
public final class JAXBMessage extends AbstractMessageImpl implements StreamingSOAP {
    private MessageHeaders headers;
    private final Object jaxbObject;
    private final XMLBridge bridge;
    private final JAXBContext rawContext;
    private String nsUri;
    private String localName;
    private XMLStreamBuffer infoset;

    public static Message create(BindingContext context, Object jaxbObject, SOAPVersion soapVersion, MessageHeaders headers, AttachmentSet attachments) {
        if (!context.hasSwaRef()) {
            return new JAXBMessage(context, jaxbObject, soapVersion, headers, attachments);
        }
        try {
            MutableXMLStreamBuffer xsb = new MutableXMLStreamBuffer();
            Marshaller m2 = context.createMarshaller();
            AttachmentMarshallerImpl am2 = new AttachmentMarshallerImpl(attachments);
            m2.setAttachmentMarshaller(am2);
            am2.cleanup();
            m2.marshal(jaxbObject, xsb.createFromXMLStreamWriter());
            return new StreamMessage(headers, attachments, xsb.readAsXMLStreamReader(), soapVersion);
        } catch (JAXBException e2) {
            throw new WebServiceException(e2);
        } catch (XMLStreamException e3) {
            throw new WebServiceException(e3);
        }
    }

    public static Message create(BindingContext context, Object jaxbObject, SOAPVersion soapVersion) {
        return create(context, jaxbObject, soapVersion, null, null);
    }

    public static Message create(JAXBContext context, Object jaxbObject, SOAPVersion soapVersion) {
        return create(BindingContextFactory.create(context), jaxbObject, soapVersion, null, null);
    }

    public static Message createRaw(JAXBContext context, Object jaxbObject, SOAPVersion soapVersion) {
        return new JAXBMessage(context, jaxbObject, soapVersion, (MessageHeaders) null, (AttachmentSet) null);
    }

    private JAXBMessage(BindingContext context, Object jaxbObject, SOAPVersion soapVer, MessageHeaders headers, AttachmentSet attachments) {
        super(soapVer);
        this.bridge = context.createFragmentBridge();
        this.rawContext = null;
        this.jaxbObject = jaxbObject;
        this.headers = headers;
        this.attachmentSet = attachments;
    }

    private JAXBMessage(JAXBContext rawContext, Object jaxbObject, SOAPVersion soapVer, MessageHeaders headers, AttachmentSet attachments) {
        super(soapVer);
        this.rawContext = rawContext;
        this.bridge = null;
        this.jaxbObject = jaxbObject;
        this.headers = headers;
        this.attachmentSet = attachments;
    }

    public static Message create(XMLBridge bridge, Object jaxbObject, SOAPVersion soapVer) {
        if (!bridge.context().hasSwaRef()) {
            return new JAXBMessage(bridge, jaxbObject, soapVer);
        }
        try {
            MutableXMLStreamBuffer xsb = new MutableXMLStreamBuffer();
            AttachmentSetImpl attachments = new AttachmentSetImpl();
            AttachmentMarshallerImpl am2 = new AttachmentMarshallerImpl(attachments);
            bridge.marshal((XMLBridge) jaxbObject, xsb.createFromXMLStreamWriter(), (AttachmentMarshaller) am2);
            am2.cleanup();
            return new StreamMessage(null, attachments, xsb.readAsXMLStreamReader(), soapVer);
        } catch (JAXBException e2) {
            throw new WebServiceException(e2);
        } catch (XMLStreamException e3) {
            throw new WebServiceException(e3);
        }
    }

    private JAXBMessage(XMLBridge bridge, Object jaxbObject, SOAPVersion soapVer) {
        super(soapVer);
        this.bridge = bridge;
        this.rawContext = null;
        this.jaxbObject = jaxbObject;
        QName tagName = bridge.getTypeInfo().tagName;
        this.nsUri = tagName.getNamespaceURI();
        this.localName = tagName.getLocalPart();
        this.attachmentSet = new AttachmentSetImpl();
    }

    public JAXBMessage(JAXBMessage that) {
        super(that);
        this.headers = that.headers;
        if (this.headers != null) {
            this.headers = new HeaderList(this.headers);
        }
        this.attachmentSet = that.attachmentSet;
        this.jaxbObject = that.jaxbObject;
        this.bridge = that.bridge;
        this.rawContext = that.rawContext;
    }

    @Override // com.sun.xml.internal.ws.api.message.Message
    public boolean hasHeaders() {
        return this.headers != null && this.headers.hasHeaders();
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
        if (this.localName == null) {
            sniff();
        }
        return this.localName;
    }

    @Override // com.sun.xml.internal.ws.api.message.Message
    public String getPayloadNamespaceURI() {
        if (this.nsUri == null) {
            sniff();
        }
        return this.nsUri;
    }

    @Override // com.sun.xml.internal.ws.api.message.Message
    public boolean hasPayload() {
        return true;
    }

    private void sniff() {
        RootElementSniffer sniffer = new RootElementSniffer(false);
        try {
            if (this.rawContext != null) {
                Marshaller m2 = this.rawContext.createMarshaller();
                m2.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
                m2.marshal(this.jaxbObject, sniffer);
            } else {
                this.bridge.marshal((XMLBridge) this.jaxbObject, (ContentHandler) sniffer, (AttachmentMarshaller) null);
            }
        } catch (JAXBException e2) {
            this.nsUri = sniffer.getNsUri();
            this.localName = sniffer.getLocalName();
        }
    }

    @Override // com.sun.xml.internal.ws.api.message.Message
    public Source readPayloadAsSource() {
        return new JAXBBridgeSource(this.bridge, this.jaxbObject);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.sun.xml.internal.ws.message.AbstractMessageImpl, com.sun.xml.internal.ws.api.message.Message
    public <T> T readPayloadAsJAXB(Unmarshaller unmarshaller) throws JAXBException {
        JAXBResult jAXBResult = new JAXBResult(unmarshaller);
        try {
            jAXBResult.getHandler().startDocument();
            if (this.rawContext != null) {
                Marshaller marshallerCreateMarshaller = this.rawContext.createMarshaller();
                marshallerCreateMarshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
                marshallerCreateMarshaller.marshal(this.jaxbObject, jAXBResult);
            } else {
                this.bridge.marshal((XMLBridge) this.jaxbObject, (Result) jAXBResult);
            }
            jAXBResult.getHandler().endDocument();
            return (T) jAXBResult.getResult();
        } catch (SAXException e2) {
            throw new JAXBException(e2);
        }
    }

    @Override // com.sun.xml.internal.ws.api.message.Message
    public XMLStreamReader readPayload() throws XMLStreamException {
        try {
            if (this.infoset == null) {
                if (this.rawContext != null) {
                    XMLStreamBufferResult sbr = new XMLStreamBufferResult();
                    Marshaller m2 = this.rawContext.createMarshaller();
                    m2.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
                    m2.marshal(this.jaxbObject, sbr);
                    this.infoset = sbr.getXMLStreamBuffer();
                } else {
                    MutableXMLStreamBuffer buffer = new MutableXMLStreamBuffer();
                    writePayloadTo(buffer.createFromXMLStreamWriter());
                    this.infoset = buffer;
                }
            }
            XMLStreamReader reader = this.infoset.readAsXMLStreamReader();
            if (reader.getEventType() == 7) {
                XMLStreamReaderUtil.nextElementContent(reader);
            }
            return reader;
        } catch (JAXBException e2) {
            throw new WebServiceException(e2);
        }
    }

    @Override // com.sun.xml.internal.ws.message.AbstractMessageImpl
    protected void writePayloadTo(ContentHandler contentHandler, ErrorHandler errorHandler, boolean fragment) throws SAXException {
        if (fragment) {
            try {
                contentHandler = new FragmentContentHandler(contentHandler);
            } catch (JAXBException e2) {
                throw new WebServiceException(e2.getMessage(), e2);
            }
        }
        AttachmentMarshallerImpl am2 = new AttachmentMarshallerImpl(this.attachmentSet);
        if (this.rawContext != null) {
            Marshaller m2 = this.rawContext.createMarshaller();
            m2.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
            m2.setAttachmentMarshaller(am2);
            m2.marshal(this.jaxbObject, contentHandler);
        } else {
            this.bridge.marshal((XMLBridge) this.jaxbObject, contentHandler, (AttachmentMarshaller) am2);
        }
        am2.cleanup();
    }

    @Override // com.sun.xml.internal.ws.api.message.Message
    public void writePayloadTo(XMLStreamWriter sw) throws XMLStreamException {
        try {
            AttachmentMarshaller am2 = sw instanceof MtomStreamWriter ? ((MtomStreamWriter) sw).getAttachmentMarshaller() : new AttachmentMarshallerImpl(this.attachmentSet);
            String encoding = XMLStreamWriterUtil.getEncoding(sw);
            OutputStream os = this.bridge.supportOutputStream() ? XMLStreamWriterUtil.getOutputStream(sw) : null;
            if (this.rawContext != null) {
                Marshaller m2 = this.rawContext.createMarshaller();
                m2.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
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

    @Override // com.sun.xml.internal.ws.api.message.Message
    public Message copy() {
        return new JAXBMessage(this);
    }

    @Override // com.sun.xml.internal.ws.api.message.StreamingSOAP
    public XMLStreamReader readEnvelope() {
        int base = this.soapVersion.ordinal() * 3;
        this.envelopeTag = DEFAULT_TAGS.get(base);
        this.bodyTag = DEFAULT_TAGS.get(base + 2);
        List<XMLStreamReader> hReaders = new ArrayList<>();
        XMLReaderComposite.ElemInfo envElem = new XMLReaderComposite.ElemInfo(this.envelopeTag, null);
        XMLReaderComposite.ElemInfo bdyElem = new XMLReaderComposite.ElemInfo(this.bodyTag, envElem);
        for (Header h2 : getHeaders().asList()) {
            try {
                hReaders.add(h2.readHeader());
            } catch (XMLStreamException e2) {
                throw new RuntimeException(e2);
            }
        }
        XMLStreamReader soapHeader = null;
        if (hReaders.size() > 0) {
            this.headerTag = DEFAULT_TAGS.get(base + 1);
            XMLReaderComposite.ElemInfo hdrElem = new XMLReaderComposite.ElemInfo(this.headerTag, envElem);
            soapHeader = new XMLReaderComposite(hdrElem, (XMLStreamReader[]) hReaders.toArray(new XMLStreamReader[hReaders.size()]));
        }
        try {
            XMLStreamReader payload = readPayload();
            XMLStreamReader soapBody = new XMLReaderComposite(bdyElem, new XMLStreamReader[]{payload});
            XMLStreamReader[] soapContent = soapHeader != null ? new XMLStreamReader[]{soapHeader, soapBody} : new XMLStreamReader[]{soapBody};
            return new XMLReaderComposite(envElem, soapContent);
        } catch (XMLStreamException e3) {
            throw new RuntimeException(e3);
        }
    }
}
