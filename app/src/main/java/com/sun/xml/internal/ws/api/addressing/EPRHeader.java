package com.sun.xml.internal.ws.api.addressing;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import com.sun.xml.internal.ws.message.AbstractHeaderImpl;
import com.sun.xml.internal.ws.util.xml.XmlUtil;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPMessage;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import org.w3c.dom.Node;
import org.xml.sax.ContentHandler;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;

/* loaded from: rt.jar:com/sun/xml/internal/ws/api/addressing/EPRHeader.class */
final class EPRHeader extends AbstractHeaderImpl {
    private final String nsUri;
    private final String localName;
    private final WSEndpointReference epr;

    EPRHeader(QName tagName, WSEndpointReference epr) {
        this.nsUri = tagName.getNamespaceURI();
        this.localName = tagName.getLocalPart();
        this.epr = epr;
    }

    @Override // com.sun.xml.internal.ws.api.message.Header
    @NotNull
    public String getNamespaceURI() {
        return this.nsUri;
    }

    @Override // com.sun.xml.internal.ws.api.message.Header
    @NotNull
    public String getLocalPart() {
        return this.localName;
    }

    @Override // com.sun.xml.internal.ws.api.message.Header
    @Nullable
    public String getAttribute(@NotNull String nsUri, @NotNull String localName) {
        try {
            XMLStreamReader sr = this.epr.read("EndpointReference");
            while (sr.getEventType() != 1) {
                sr.next();
            }
            return sr.getAttributeValue(nsUri, localName);
        } catch (XMLStreamException e2) {
            throw new AssertionError(e2);
        }
    }

    @Override // com.sun.xml.internal.ws.api.message.Header
    public XMLStreamReader readHeader() throws XMLStreamException {
        return this.epr.read(this.localName);
    }

    @Override // com.sun.xml.internal.ws.api.message.Header
    public void writeTo(XMLStreamWriter w2) throws XMLStreamException {
        this.epr.writeTo(this.localName, w2);
    }

    @Override // com.sun.xml.internal.ws.api.message.Header
    public void writeTo(SOAPMessage saaj) throws SOAPException {
        try {
            XmlUtil.newTransformer();
            SOAPHeader header = saaj.getSOAPHeader();
            if (header == null) {
                header = saaj.getSOAPPart().getEnvelope().addHeader();
            }
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            XMLStreamWriter w2 = XMLOutputFactory.newFactory().createXMLStreamWriter(baos);
            this.epr.writeTo(this.localName, w2);
            w2.flush();
            ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
            DocumentBuilderFactory fac = DocumentBuilderFactory.newInstance();
            fac.setNamespaceAware(true);
            Node eprNode = fac.newDocumentBuilder().parse(bais).getDocumentElement();
            Node eprNodeToAdd = header.getOwnerDocument().importNode(eprNode, true);
            header.appendChild(eprNodeToAdd);
        } catch (Exception e2) {
            throw new SOAPException(e2);
        }
    }

    @Override // com.sun.xml.internal.ws.api.message.Header
    public void writeTo(ContentHandler contentHandler, ErrorHandler errorHandler) throws SAXException {
        this.epr.writeTo(this.localName, contentHandler, errorHandler, true);
    }
}
