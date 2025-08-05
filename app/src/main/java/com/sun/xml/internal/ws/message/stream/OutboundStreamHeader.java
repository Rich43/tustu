package com.sun.xml.internal.ws.message.stream;

import com.sun.istack.internal.FinalArrayList;
import com.sun.istack.internal.NotNull;
import com.sun.xml.internal.stream.buffer.XMLStreamBuffer;
import com.sun.xml.internal.stream.buffer.XMLStreamBufferException;
import com.sun.xml.internal.ws.message.AbstractHeaderImpl;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPMessage;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.ws.WebServiceException;
import org.xml.sax.ContentHandler;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;

/* loaded from: rt.jar:com/sun/xml/internal/ws/message/stream/OutboundStreamHeader.class */
public final class OutboundStreamHeader extends AbstractHeaderImpl {
    private final XMLStreamBuffer infoset;
    private final String nsUri;
    private final String localName;
    private FinalArrayList<Attribute> attributes;
    private static final String TRUE_VALUE = "1";
    private static final String IS_REFERENCE_PARAMETER = "IsReferenceParameter";

    public OutboundStreamHeader(XMLStreamBuffer infoset, String nsUri, String localName) {
        this.infoset = infoset;
        this.nsUri = nsUri;
        this.localName = localName;
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
    public String getAttribute(String nsUri, String localName) {
        if (this.attributes == null) {
            parseAttributes();
        }
        for (int i2 = this.attributes.size() - 1; i2 >= 0; i2--) {
            Attribute a2 = this.attributes.get(i2);
            if (a2.localName.equals(localName) && a2.nsUri.equals(nsUri)) {
                return a2.value;
            }
        }
        return null;
    }

    private void parseAttributes() {
        try {
            XMLStreamReader reader = readHeader();
            this.attributes = new FinalArrayList<>();
            for (int i2 = 0; i2 < reader.getAttributeCount(); i2++) {
                String localName = reader.getAttributeLocalName(i2);
                String namespaceURI = reader.getAttributeNamespace(i2);
                String value = reader.getAttributeValue(i2);
                this.attributes.add(new Attribute(namespaceURI, localName, value));
            }
        } catch (XMLStreamException e2) {
            throw new WebServiceException("Unable to read the attributes for {" + this.nsUri + "}" + this.localName + " header", e2);
        }
    }

    @Override // com.sun.xml.internal.ws.api.message.Header
    public XMLStreamReader readHeader() throws XMLStreamException {
        return this.infoset.readAsXMLStreamReader();
    }

    @Override // com.sun.xml.internal.ws.api.message.Header
    public void writeTo(XMLStreamWriter w2) throws XMLStreamException {
        this.infoset.writeToXMLStreamWriter(w2, true);
    }

    @Override // com.sun.xml.internal.ws.api.message.Header
    public void writeTo(SOAPMessage saaj) throws SOAPException {
        try {
            SOAPHeader header = saaj.getSOAPHeader();
            if (header == null) {
                header = saaj.getSOAPPart().getEnvelope().addHeader();
            }
            this.infoset.writeTo(header);
        } catch (XMLStreamBufferException e2) {
            throw new SOAPException(e2);
        }
    }

    @Override // com.sun.xml.internal.ws.api.message.Header
    public void writeTo(ContentHandler contentHandler, ErrorHandler errorHandler) throws SAXException {
        this.infoset.writeTo(contentHandler, errorHandler);
    }

    /* loaded from: rt.jar:com/sun/xml/internal/ws/message/stream/OutboundStreamHeader$Attribute.class */
    static final class Attribute {
        final String nsUri;
        final String localName;
        final String value;

        public Attribute(String nsUri, String localName, String value) {
            this.nsUri = fixNull(nsUri);
            this.localName = localName;
            this.value = value;
        }

        private static String fixNull(String s2) {
            return s2 == null ? "" : s2;
        }
    }
}
