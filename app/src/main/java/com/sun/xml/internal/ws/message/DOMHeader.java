package com.sun.xml.internal.ws.message;

import com.sun.xml.internal.bind.api.Bridge;
import com.sun.xml.internal.bind.unmarshaller.DOMScanner;
import com.sun.xml.internal.ws.streaming.DOMStreamReader;
import com.sun.xml.internal.ws.util.DOMUtil;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPMessage;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import org.w3c.dom.DOMException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.ContentHandler;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;

/* loaded from: rt.jar:com/sun/xml/internal/ws/message/DOMHeader.class */
public class DOMHeader<N extends Element> extends AbstractHeaderImpl {
    protected final N node;
    private final String nsUri;
    private final String localName;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !DOMHeader.class.desiredAssertionStatus();
    }

    public DOMHeader(N node) {
        if (!$assertionsDisabled && node == null) {
            throw new AssertionError();
        }
        this.node = node;
        this.nsUri = fixNull(node.getNamespaceURI());
        this.localName = node.getLocalName();
    }

    @Override // com.sun.xml.internal.ws.api.message.Header
    public String getNamespaceURI() {
        return this.nsUri;
    }

    @Override // com.sun.xml.internal.ws.api.message.Header
    public String getLocalPart() {
        return this.localName;
    }

    @Override // com.sun.xml.internal.ws.api.message.Header
    public XMLStreamReader readHeader() throws XMLStreamException {
        DOMStreamReader r2 = new DOMStreamReader(this.node);
        r2.nextTag();
        return r2;
    }

    @Override // com.sun.xml.internal.ws.message.AbstractHeaderImpl, com.sun.xml.internal.ws.api.message.Header
    public <T> T readAsJAXB(Unmarshaller unmarshaller) throws JAXBException {
        return (T) unmarshaller.unmarshal(this.node);
    }

    @Override // com.sun.xml.internal.ws.message.AbstractHeaderImpl, com.sun.xml.internal.ws.api.message.Header
    public <T> T readAsJAXB(Bridge<T> bridge) throws JAXBException {
        return bridge.unmarshal(this.node);
    }

    @Override // com.sun.xml.internal.ws.api.message.Header
    public void writeTo(XMLStreamWriter w2) throws XMLStreamException {
        DOMUtil.serializeNode(this.node, w2);
    }

    private static String fixNull(String s2) {
        return s2 != null ? s2 : "";
    }

    @Override // com.sun.xml.internal.ws.api.message.Header
    public void writeTo(ContentHandler contentHandler, ErrorHandler errorHandler) throws SAXException {
        DOMScanner ds = new DOMScanner();
        ds.setContentHandler(contentHandler);
        ds.scan((Element) this.node);
    }

    @Override // com.sun.xml.internal.ws.api.message.Header
    public String getAttribute(String nsUri, String localName) {
        if (nsUri.length() == 0) {
            nsUri = null;
        }
        return this.node.getAttributeNS(nsUri, localName);
    }

    @Override // com.sun.xml.internal.ws.api.message.Header
    public void writeTo(SOAPMessage saaj) throws SOAPException, DOMException {
        SOAPHeader header = saaj.getSOAPHeader();
        if (header == null) {
            header = saaj.getSOAPPart().getEnvelope().addHeader();
        }
        Node clone = header.getOwnerDocument().importNode(this.node, true);
        header.appendChild(clone);
    }

    @Override // com.sun.xml.internal.ws.message.AbstractHeaderImpl, com.sun.xml.internal.ws.api.message.Header
    public String getStringContent() {
        return this.node.getTextContent();
    }

    public N getWrappedNode() {
        return this.node;
    }

    public int hashCode() {
        return getWrappedNode().hashCode();
    }

    public boolean equals(Object obj) {
        if (obj instanceof DOMHeader) {
            return getWrappedNode().equals(((DOMHeader) obj).getWrappedNode());
        }
        return false;
    }
}
