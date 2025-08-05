package com.sun.org.apache.xalan.internal.xsltc.trax;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.ext.Locator2;

/* loaded from: rt.jar:com/sun/org/apache/xalan/internal/xsltc/trax/SAX2StAXStreamWriter.class */
public class SAX2StAXStreamWriter extends SAX2StAXBaseWriter {
    private XMLStreamWriter writer;
    private boolean needToCallStartDocument = false;

    public SAX2StAXStreamWriter() {
    }

    public SAX2StAXStreamWriter(XMLStreamWriter writer) {
        this.writer = writer;
    }

    public XMLStreamWriter getStreamWriter() {
        return this.writer;
    }

    public void setStreamWriter(XMLStreamWriter writer) {
        this.writer = writer;
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.trax.SAX2StAXBaseWriter, org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
    public void startDocument() throws SAXException {
        super.startDocument();
        this.needToCallStartDocument = true;
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.trax.SAX2StAXBaseWriter, org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
    public void endDocument() throws SAXException {
        try {
            this.writer.writeEndDocument();
            super.endDocument();
        } catch (XMLStreamException e2) {
            throw new SAXException(e2);
        }
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.trax.SAX2StAXBaseWriter, org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if (this.needToCallStartDocument) {
            try {
                if (this.docLocator == null) {
                    this.writer.writeStartDocument();
                } else {
                    try {
                        this.writer.writeStartDocument(((Locator2) this.docLocator).getXMLVersion());
                    } catch (ClassCastException e2) {
                        this.writer.writeStartDocument();
                    }
                }
                this.needToCallStartDocument = false;
            } catch (XMLStreamException e3) {
                throw new SAXException(e3);
            }
        }
        try {
            try {
                String[] qname = {null, null};
                parseQName(qName, qname);
                this.writer.writeStartElement(qName);
                int s2 = attributes.getLength();
                for (int i2 = 0; i2 < s2; i2++) {
                    parseQName(attributes.getQName(i2), qname);
                    String attrPrefix = qname[0];
                    String attrLocal = qname[1];
                    String attrQName = attributes.getQName(i2);
                    String attrValue = attributes.getValue(i2);
                    String attrURI = attributes.getURI(i2);
                    if ("xmlns".equals(attrPrefix) || "xmlns".equals(attrQName)) {
                        if (attrLocal.length() == 0) {
                            this.writer.setDefaultNamespace(attrValue);
                        } else {
                            this.writer.setPrefix(attrLocal, attrValue);
                        }
                        this.writer.writeNamespace(attrLocal, attrValue);
                    } else if (attrPrefix.length() > 0) {
                        this.writer.writeAttribute(attrPrefix, attrURI, attrLocal, attrValue);
                    } else {
                        this.writer.writeAttribute(attrQName, attrValue);
                    }
                }
            } catch (XMLStreamException e4) {
                throw new SAXException(e4);
            }
        } finally {
            super.startElement(uri, localName, qName, attributes);
        }
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.trax.SAX2StAXBaseWriter, org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
    public void endElement(String uri, String localName, String qName) throws SAXException {
        try {
            try {
                this.writer.writeEndElement();
                super.endElement(uri, localName, qName);
            } catch (XMLStreamException e2) {
                throw new SAXException(e2);
            }
        } catch (Throwable th) {
            super.endElement(uri, localName, qName);
            throw th;
        }
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.trax.SAX2StAXBaseWriter, org.xml.sax.ext.LexicalHandler
    public void comment(char[] ch, int start, int length) throws SAXException {
        super.comment(ch, start, length);
        try {
            this.writer.writeComment(new String(ch, start, length));
        } catch (XMLStreamException e2) {
            throw new SAXException(e2);
        }
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.trax.SAX2StAXBaseWriter, org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
    public void characters(char[] ch, int start, int length) throws SAXException {
        super.characters(ch, start, length);
        try {
            if (!this.isCDATA) {
                this.writer.writeCharacters(ch, start, length);
            }
        } catch (XMLStreamException e2) {
            throw new SAXException(e2);
        }
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.trax.SAX2StAXBaseWriter, org.xml.sax.ext.LexicalHandler
    public void endCDATA() throws SAXException {
        try {
            this.writer.writeCData(this.CDATABuffer.toString());
            super.endCDATA();
        } catch (XMLStreamException e2) {
            throw new SAXException(e2);
        }
    }

    @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
    public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {
        super.ignorableWhitespace(ch, start, length);
        try {
            this.writer.writeCharacters(ch, start, length);
        } catch (XMLStreamException e2) {
            throw new SAXException(e2);
        }
    }

    @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
    public void processingInstruction(String target, String data) throws SAXException {
        super.processingInstruction(target, data);
        try {
            this.writer.writeProcessingInstruction(target, data);
        } catch (XMLStreamException e2) {
            throw new SAXException(e2);
        }
    }
}
