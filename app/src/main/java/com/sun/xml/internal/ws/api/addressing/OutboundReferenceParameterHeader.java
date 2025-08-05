package com.sun.xml.internal.ws.api.addressing;

import com.sun.istack.internal.FinalArrayList;
import com.sun.istack.internal.NotNull;
import com.sun.xml.internal.stream.buffer.XMLStreamBuffer;
import com.sun.xml.internal.stream.buffer.XMLStreamBufferException;
import com.sun.xml.internal.ws.message.AbstractHeaderImpl;
import com.sun.xml.internal.ws.util.xml.XMLStreamWriterFilter;
import javax.xml.namespace.QName;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPMessage;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.stream.util.StreamReaderDelegate;
import javax.xml.ws.WebServiceException;
import jdk.internal.dynalink.CallSiteDescriptor;
import org.w3c.dom.DOMException;
import org.w3c.dom.Element;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;
import org.xml.sax.helpers.XMLFilterImpl;

/* loaded from: rt.jar:com/sun/xml/internal/ws/api/addressing/OutboundReferenceParameterHeader.class */
final class OutboundReferenceParameterHeader extends AbstractHeaderImpl {
    private final XMLStreamBuffer infoset;
    private final String nsUri;
    private final String localName;
    private FinalArrayList<Attribute> attributes;
    private static final String TRUE_VALUE = "1";
    private static final String IS_REFERENCE_PARAMETER = "IsReferenceParameter";

    OutboundReferenceParameterHeader(XMLStreamBuffer infoset, String nsUri, String localName) {
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
            reader.nextTag();
            this.attributes = new FinalArrayList<>();
            boolean refParamAttrWritten = false;
            for (int i2 = 0; i2 < reader.getAttributeCount(); i2++) {
                String attrLocalName = reader.getAttributeLocalName(i2);
                String namespaceURI = reader.getAttributeNamespace(i2);
                String value = reader.getAttributeValue(i2);
                if (namespaceURI.equals(AddressingVersion.W3C.nsUri) && attrLocalName.equals("IS_REFERENCE_PARAMETER")) {
                    refParamAttrWritten = true;
                }
                this.attributes.add(new Attribute(namespaceURI, attrLocalName, value));
            }
            if (!refParamAttrWritten) {
                this.attributes.add(new Attribute(AddressingVersion.W3C.nsUri, IS_REFERENCE_PARAMETER, "1"));
            }
        } catch (XMLStreamException e2) {
            throw new WebServiceException("Unable to read the attributes for {" + this.nsUri + "}" + this.localName + " header", e2);
        }
    }

    @Override // com.sun.xml.internal.ws.api.message.Header
    public XMLStreamReader readHeader() throws XMLStreamException {
        return new StreamReaderDelegate(this.infoset.readAsXMLStreamReader()) { // from class: com.sun.xml.internal.ws.api.addressing.OutboundReferenceParameterHeader.1
            int state = 0;

            @Override // javax.xml.stream.util.StreamReaderDelegate, javax.xml.stream.XMLStreamReader
            public int next() throws XMLStreamException {
                return check(super.next());
            }

            @Override // javax.xml.stream.util.StreamReaderDelegate, javax.xml.stream.XMLStreamReader
            public int nextTag() throws XMLStreamException {
                return check(super.nextTag());
            }

            private int check(int type) {
                switch (this.state) {
                    case 0:
                        if (type == 1) {
                            this.state = 1;
                            break;
                        }
                        break;
                    case 1:
                        this.state = 2;
                        break;
                }
                return type;
            }

            @Override // javax.xml.stream.util.StreamReaderDelegate, javax.xml.stream.XMLStreamReader
            public int getAttributeCount() {
                if (this.state == 1) {
                    return super.getAttributeCount() + 1;
                }
                return super.getAttributeCount();
            }

            @Override // javax.xml.stream.util.StreamReaderDelegate, javax.xml.stream.XMLStreamReader
            public String getAttributeLocalName(int index) {
                if (this.state == 1 && index == super.getAttributeCount()) {
                    return OutboundReferenceParameterHeader.IS_REFERENCE_PARAMETER;
                }
                return super.getAttributeLocalName(index);
            }

            @Override // javax.xml.stream.util.StreamReaderDelegate, javax.xml.stream.XMLStreamReader
            public String getAttributeNamespace(int index) {
                if (this.state == 1 && index == super.getAttributeCount()) {
                    return AddressingVersion.W3C.nsUri;
                }
                return super.getAttributeNamespace(index);
            }

            @Override // javax.xml.stream.util.StreamReaderDelegate, javax.xml.stream.XMLStreamReader
            public String getAttributePrefix(int index) {
                if (this.state == 1 && index == super.getAttributeCount()) {
                    return "wsa";
                }
                return super.getAttributePrefix(index);
            }

            @Override // javax.xml.stream.util.StreamReaderDelegate, javax.xml.stream.XMLStreamReader
            public String getAttributeType(int index) {
                if (this.state == 1 && index == super.getAttributeCount()) {
                    return "CDATA";
                }
                return super.getAttributeType(index);
            }

            @Override // javax.xml.stream.util.StreamReaderDelegate, javax.xml.stream.XMLStreamReader
            public String getAttributeValue(int index) {
                if (this.state == 1 && index == super.getAttributeCount()) {
                    return "1";
                }
                return super.getAttributeValue(index);
            }

            @Override // javax.xml.stream.util.StreamReaderDelegate, javax.xml.stream.XMLStreamReader
            public QName getAttributeName(int index) {
                if (this.state == 1 && index == super.getAttributeCount()) {
                    return new QName(AddressingVersion.W3C.nsUri, OutboundReferenceParameterHeader.IS_REFERENCE_PARAMETER, "wsa");
                }
                return super.getAttributeName(index);
            }

            @Override // javax.xml.stream.util.StreamReaderDelegate, javax.xml.stream.XMLStreamReader
            public String getAttributeValue(String namespaceUri, String localName) {
                if (this.state == 1 && localName.equals(OutboundReferenceParameterHeader.IS_REFERENCE_PARAMETER) && namespaceUri.equals(AddressingVersion.W3C.nsUri)) {
                    return "1";
                }
                return super.getAttributeValue(namespaceUri, localName);
            }
        };
    }

    @Override // com.sun.xml.internal.ws.api.message.Header
    public void writeTo(XMLStreamWriter w2) throws XMLStreamException {
        this.infoset.writeToXMLStreamWriter(new XMLStreamWriterFilter(w2) { // from class: com.sun.xml.internal.ws.api.addressing.OutboundReferenceParameterHeader.2
            private boolean root = true;
            private boolean onRootEl = true;

            @Override // com.sun.xml.internal.ws.util.xml.XMLStreamWriterFilter, javax.xml.stream.XMLStreamWriter
            public void writeStartElement(String localName) throws XMLStreamException {
                super.writeStartElement(localName);
                writeAddedAttribute();
            }

            private void writeAddedAttribute() throws XMLStreamException {
                if (!this.root) {
                    this.onRootEl = false;
                    return;
                }
                this.root = false;
                writeNamespace("wsa", AddressingVersion.W3C.nsUri);
                super.writeAttribute("wsa", AddressingVersion.W3C.nsUri, OutboundReferenceParameterHeader.IS_REFERENCE_PARAMETER, "1");
            }

            @Override // com.sun.xml.internal.ws.util.xml.XMLStreamWriterFilter, javax.xml.stream.XMLStreamWriter
            public void writeStartElement(String namespaceURI, String localName) throws XMLStreamException {
                super.writeStartElement(namespaceURI, localName);
                writeAddedAttribute();
            }

            @Override // com.sun.xml.internal.ws.util.xml.XMLStreamWriterFilter, javax.xml.stream.XMLStreamWriter
            public void writeStartElement(String prefix, String localName, String namespaceURI) throws XMLStreamException {
                boolean prefixDeclared = isPrefixDeclared(prefix, namespaceURI);
                super.writeStartElement(prefix, localName, namespaceURI);
                if (!prefixDeclared && !prefix.equals("")) {
                    super.writeNamespace(prefix, namespaceURI);
                }
                writeAddedAttribute();
            }

            @Override // com.sun.xml.internal.ws.util.xml.XMLStreamWriterFilter, javax.xml.stream.XMLStreamWriter
            public void writeNamespace(String prefix, String namespaceURI) throws XMLStreamException {
                if (!isPrefixDeclared(prefix, namespaceURI)) {
                    super.writeNamespace(prefix, namespaceURI);
                }
            }

            @Override // com.sun.xml.internal.ws.util.xml.XMLStreamWriterFilter, javax.xml.stream.XMLStreamWriter
            public void writeAttribute(String prefix, String namespaceURI, String localName, String value) throws XMLStreamException {
                if (this.onRootEl && namespaceURI.equals(AddressingVersion.W3C.nsUri) && localName.equals(OutboundReferenceParameterHeader.IS_REFERENCE_PARAMETER)) {
                    return;
                }
                this.writer.writeAttribute(prefix, namespaceURI, localName, value);
            }

            @Override // com.sun.xml.internal.ws.util.xml.XMLStreamWriterFilter, javax.xml.stream.XMLStreamWriter
            public void writeAttribute(String namespaceURI, String localName, String value) throws XMLStreamException {
                this.writer.writeAttribute(namespaceURI, localName, value);
            }

            private boolean isPrefixDeclared(String prefix, String namespaceURI) {
                return namespaceURI.equals(getNamespaceContext().getNamespaceURI(prefix));
            }
        }, true);
    }

    @Override // com.sun.xml.internal.ws.api.message.Header
    public void writeTo(SOAPMessage saaj) throws SOAPException, DOMException {
        try {
            SOAPHeader header = saaj.getSOAPHeader();
            if (header == null) {
                header = saaj.getSOAPPart().getEnvelope().addHeader();
            }
            Element node = (Element) this.infoset.writeTo(header);
            node.setAttributeNS(AddressingVersion.W3C.nsUri, AddressingVersion.W3C.getPrefix() + CallSiteDescriptor.TOKEN_DELIMITER + IS_REFERENCE_PARAMETER, "1");
        } catch (XMLStreamBufferException e2) {
            throw new SOAPException(e2);
        }
    }

    @Override // com.sun.xml.internal.ws.api.message.Header
    public void writeTo(ContentHandler contentHandler, ErrorHandler errorHandler) throws SAXException {
        this.infoset.writeTo(new XMLFilterImpl(contentHandler) { // from class: com.sun.xml.internal.ws.api.addressing.OutboundReferenceParameterHeader.1Filter
            private int depth = 0;

            {
                setContentHandler(contentHandler);
            }

            @Override // org.xml.sax.helpers.XMLFilterImpl, org.xml.sax.ContentHandler
            public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
                int i2 = this.depth;
                this.depth = i2 + 1;
                if (i2 == 0) {
                    super.startPrefixMapping("wsa", AddressingVersion.W3C.nsUri);
                    if (atts.getIndex(AddressingVersion.W3C.nsUri, OutboundReferenceParameterHeader.IS_REFERENCE_PARAMETER) == -1) {
                        AttributesImpl atts2 = new AttributesImpl(atts);
                        atts2.addAttribute(AddressingVersion.W3C.nsUri, OutboundReferenceParameterHeader.IS_REFERENCE_PARAMETER, "wsa:IsReferenceParameter", "CDATA", "1");
                        atts = atts2;
                    }
                }
                super.startElement(uri, localName, qName, atts);
            }

            @Override // org.xml.sax.helpers.XMLFilterImpl, org.xml.sax.ContentHandler
            public void endElement(String uri, String localName, String qName) throws SAXException {
                super.endElement(uri, localName, qName);
                int i2 = this.depth - 1;
                this.depth = i2;
                if (i2 == 0) {
                    super.endPrefixMapping("wsa");
                }
            }
        }, errorHandler);
    }

    /* loaded from: rt.jar:com/sun/xml/internal/ws/api/addressing/OutboundReferenceParameterHeader$Attribute.class */
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
