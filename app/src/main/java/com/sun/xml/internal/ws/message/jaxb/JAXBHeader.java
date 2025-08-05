package com.sun.xml.internal.ws.message.jaxb;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.XMLStreamException2;
import com.sun.xml.internal.bind.api.Bridge;
import com.sun.xml.internal.stream.buffer.MutableXMLStreamBuffer;
import com.sun.xml.internal.stream.buffer.XMLStreamBuffer;
import com.sun.xml.internal.ws.message.AbstractHeaderImpl;
import com.sun.xml.internal.ws.message.RootElementSniffer;
import com.sun.xml.internal.ws.spi.db.BindingContext;
import com.sun.xml.internal.ws.spi.db.XMLBridge;
import com.sun.xml.internal.ws.streaming.XMLStreamWriterUtil;
import java.io.OutputStream;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.attachment.AttachmentMarshaller;
import javax.xml.bind.attachment.AttachmentUnmarshaller;
import javax.xml.bind.util.JAXBResult;
import javax.xml.namespace.QName;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPMessage;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.transform.Result;
import org.w3c.dom.Node;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/* loaded from: rt.jar:com/sun/xml/internal/ws/message/jaxb/JAXBHeader.class */
public final class JAXBHeader extends AbstractHeaderImpl {
    private final Object jaxbObject;
    private final XMLBridge bridge;
    private String nsUri;
    private String localName;
    private Attributes atts;
    private XMLStreamBuffer infoset;

    public JAXBHeader(BindingContext context, Object jaxbObject) {
        this.jaxbObject = jaxbObject;
        this.bridge = context.createFragmentBridge();
        if (jaxbObject instanceof JAXBElement) {
            JAXBElement e2 = (JAXBElement) jaxbObject;
            this.nsUri = e2.getName().getNamespaceURI();
            this.localName = e2.getName().getLocalPart();
        }
    }

    public JAXBHeader(XMLBridge bridge, Object jaxbObject) {
        this.jaxbObject = jaxbObject;
        this.bridge = bridge;
        QName tagName = bridge.getTypeInfo().tagName;
        this.nsUri = tagName.getNamespaceURI();
        this.localName = tagName.getLocalPart();
    }

    private void parse() {
        RootElementSniffer sniffer = new RootElementSniffer();
        try {
            this.bridge.marshal((XMLBridge) this.jaxbObject, (ContentHandler) sniffer, (AttachmentMarshaller) null);
        } catch (JAXBException e2) {
            this.nsUri = sniffer.getNsUri();
            this.localName = sniffer.getLocalName();
            this.atts = sniffer.getAttributes();
        }
    }

    @Override // com.sun.xml.internal.ws.api.message.Header
    @NotNull
    public String getNamespaceURI() {
        if (this.nsUri == null) {
            parse();
        }
        return this.nsUri;
    }

    @Override // com.sun.xml.internal.ws.api.message.Header
    @NotNull
    public String getLocalPart() {
        if (this.localName == null) {
            parse();
        }
        return this.localName;
    }

    @Override // com.sun.xml.internal.ws.api.message.Header
    public String getAttribute(String nsUri, String localName) {
        if (this.atts == null) {
            parse();
        }
        return this.atts.getValue(nsUri, localName);
    }

    @Override // com.sun.xml.internal.ws.api.message.Header
    public XMLStreamReader readHeader() throws XMLStreamException {
        if (this.infoset == null) {
            MutableXMLStreamBuffer buffer = new MutableXMLStreamBuffer();
            writeTo(buffer.createFromXMLStreamWriter());
            this.infoset = buffer;
        }
        return this.infoset.readAsXMLStreamReader();
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.sun.xml.internal.ws.message.AbstractHeaderImpl, com.sun.xml.internal.ws.api.message.Header
    public <T> T readAsJAXB(Unmarshaller unmarshaller) throws JAXBException {
        try {
            JAXBResult jAXBResult = new JAXBResult(unmarshaller);
            jAXBResult.getHandler().startDocument();
            this.bridge.marshal((XMLBridge) this.jaxbObject, (Result) jAXBResult);
            jAXBResult.getHandler().endDocument();
            return (T) jAXBResult.getResult();
        } catch (SAXException e2) {
            throw new JAXBException(e2);
        }
    }

    @Override // com.sun.xml.internal.ws.message.AbstractHeaderImpl, com.sun.xml.internal.ws.api.message.Header
    public <T> T readAsJAXB(Bridge<T> bridge) throws JAXBException {
        return bridge.unmarshal(new JAXBBridgeSource(this.bridge, this.jaxbObject));
    }

    @Override // com.sun.xml.internal.ws.message.AbstractHeaderImpl, com.sun.xml.internal.ws.api.message.Header
    public <T> T readAsJAXB(XMLBridge<T> bond) throws JAXBException {
        return bond.unmarshal(new JAXBBridgeSource(this.bridge, this.jaxbObject), (AttachmentUnmarshaller) null);
    }

    @Override // com.sun.xml.internal.ws.api.message.Header
    public void writeTo(XMLStreamWriter sw) throws XMLStreamException {
        try {
            String encoding = XMLStreamWriterUtil.getEncoding(sw);
            OutputStream os = this.bridge.supportOutputStream() ? XMLStreamWriterUtil.getOutputStream(sw) : null;
            if (os != null && encoding != null && encoding.equalsIgnoreCase("utf-8")) {
                this.bridge.marshal(this.jaxbObject, os, sw.getNamespaceContext(), null);
            } else {
                this.bridge.marshal((XMLBridge) this.jaxbObject, sw, (AttachmentMarshaller) null);
            }
        } catch (JAXBException e2) {
            throw new XMLStreamException2(e2);
        }
    }

    @Override // com.sun.xml.internal.ws.api.message.Header
    public void writeTo(SOAPMessage saaj) throws SOAPException {
        try {
            SOAPHeader header = saaj.getSOAPHeader();
            if (header == null) {
                header = saaj.getSOAPPart().getEnvelope().addHeader();
            }
            this.bridge.marshal((XMLBridge) this.jaxbObject, (Node) header);
        } catch (JAXBException e2) {
            throw new SOAPException(e2);
        }
    }

    @Override // com.sun.xml.internal.ws.api.message.Header
    public void writeTo(ContentHandler contentHandler, ErrorHandler errorHandler) throws SAXException {
        try {
            this.bridge.marshal((XMLBridge) this.jaxbObject, contentHandler, (AttachmentMarshaller) null);
        } catch (JAXBException e2) {
            SAXParseException x2 = new SAXParseException(e2.getMessage(), null, null, -1, -1, e2);
            errorHandler.fatalError(x2);
            throw x2;
        }
    }
}
