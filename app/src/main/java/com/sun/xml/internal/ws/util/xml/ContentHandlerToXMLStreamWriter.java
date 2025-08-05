package com.sun.xml.internal.ws.util.xml;

import java.util.Stack;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import org.xml.sax.Attributes;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/* loaded from: rt.jar:com/sun/xml/internal/ws/util/xml/ContentHandlerToXMLStreamWriter.class */
public class ContentHandlerToXMLStreamWriter extends DefaultHandler {
    private final XMLStreamWriter staxWriter;
    private final Stack prefixBindings = new Stack();

    public ContentHandlerToXMLStreamWriter(XMLStreamWriter staxCore) {
        this.staxWriter = staxCore;
    }

    @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
    public void endDocument() throws SAXException {
        try {
            this.staxWriter.writeEndDocument();
            this.staxWriter.flush();
        } catch (XMLStreamException e2) {
            throw new SAXException(e2);
        }
    }

    @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
    public void startDocument() throws SAXException {
        try {
            this.staxWriter.writeStartDocument();
        } catch (XMLStreamException e2) {
            throw new SAXException(e2);
        }
    }

    @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
    public void characters(char[] ch, int start, int length) throws SAXException {
        try {
            this.staxWriter.writeCharacters(ch, start, length);
        } catch (XMLStreamException e2) {
            throw new SAXException(e2);
        }
    }

    @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
    public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {
        characters(ch, start, length);
    }

    @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
    public void endPrefixMapping(String prefix) throws SAXException {
    }

    @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
    public void skippedEntity(String name) throws SAXException {
        try {
            this.staxWriter.writeEntityRef(name);
        } catch (XMLStreamException e2) {
            throw new SAXException(e2);
        }
    }

    @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
    public void setDocumentLocator(Locator locator) {
    }

    @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
    public void processingInstruction(String target, String data) throws SAXException {
        try {
            this.staxWriter.writeProcessingInstruction(target, data);
        } catch (XMLStreamException e2) {
            throw new SAXException(e2);
        }
    }

    @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
    public void startPrefixMapping(String prefix, String uri) throws SAXException {
        if (prefix == null) {
            prefix = "";
        }
        if (prefix.equals("xml")) {
            return;
        }
        this.prefixBindings.add(prefix);
        this.prefixBindings.add(uri);
    }

    @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
    public void endElement(String namespaceURI, String localName, String qName) throws SAXException {
        try {
            this.staxWriter.writeEndElement();
        } catch (XMLStreamException e2) {
            throw new SAXException(e2);
        }
    }

    @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
    public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {
        try {
            this.staxWriter.writeStartElement(getPrefix(qName), localName, namespaceURI);
            while (this.prefixBindings.size() != 0) {
                String uri = (String) this.prefixBindings.pop();
                String prefix = (String) this.prefixBindings.pop();
                if (prefix.length() == 0) {
                    this.staxWriter.setDefaultNamespace(uri);
                } else {
                    this.staxWriter.setPrefix(prefix, uri);
                }
                this.staxWriter.writeNamespace(prefix, uri);
            }
            writeAttributes(atts);
        } catch (XMLStreamException e2) {
            throw new SAXException(e2);
        }
    }

    private void writeAttributes(Attributes atts) throws XMLStreamException {
        for (int i2 = 0; i2 < atts.getLength(); i2++) {
            String prefix = getPrefix(atts.getQName(i2));
            if (!prefix.equals("xmlns")) {
                this.staxWriter.writeAttribute(prefix, atts.getURI(i2), atts.getLocalName(i2), atts.getValue(i2));
            }
        }
    }

    private String getPrefix(String qName) {
        int idx = qName.indexOf(58);
        if (idx == -1) {
            return "";
        }
        return qName.substring(0, idx);
    }
}
