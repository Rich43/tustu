package com.sun.org.apache.xml.internal.resolver.readers;

import java.io.IOException;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/resolver/readers/SAXParserHandler.class */
public class SAXParserHandler extends DefaultHandler {
    private EntityResolver er = null;
    private ContentHandler ch = null;

    public void setEntityResolver(EntityResolver er) {
        this.er = er;
    }

    public void setContentHandler(ContentHandler ch) {
        this.ch = ch;
    }

    @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.EntityResolver
    public InputSource resolveEntity(String publicId, String systemId) throws SAXException {
        if (this.er != null) {
            try {
                return this.er.resolveEntity(publicId, systemId);
            } catch (IOException e2) {
                System.out.println("resolveEntity threw IOException!");
                return null;
            }
        }
        return null;
    }

    @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
    public void characters(char[] ch, int start, int length) throws SAXException {
        if (this.ch != null) {
            this.ch.characters(ch, start, length);
        }
    }

    @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
    public void endDocument() throws SAXException {
        if (this.ch != null) {
            this.ch.endDocument();
        }
    }

    @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
    public void endElement(String namespaceURI, String localName, String qName) throws SAXException {
        if (this.ch != null) {
            this.ch.endElement(namespaceURI, localName, qName);
        }
    }

    @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
    public void endPrefixMapping(String prefix) throws SAXException {
        if (this.ch != null) {
            this.ch.endPrefixMapping(prefix);
        }
    }

    @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
    public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {
        if (this.ch != null) {
            this.ch.ignorableWhitespace(ch, start, length);
        }
    }

    @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
    public void processingInstruction(String target, String data) throws SAXException {
        if (this.ch != null) {
            this.ch.processingInstruction(target, data);
        }
    }

    @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
    public void setDocumentLocator(Locator locator) {
        if (this.ch != null) {
            this.ch.setDocumentLocator(locator);
        }
    }

    @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
    public void skippedEntity(String name) throws SAXException {
        if (this.ch != null) {
            this.ch.skippedEntity(name);
        }
    }

    @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
    public void startDocument() throws SAXException {
        if (this.ch != null) {
            this.ch.startDocument();
        }
    }

    @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
    public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {
        if (this.ch != null) {
            this.ch.startElement(namespaceURI, localName, qName, atts);
        }
    }

    @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
    public void startPrefixMapping(String prefix, String uri) throws SAXException {
        if (this.ch != null) {
            this.ch.startPrefixMapping(prefix, uri);
        }
    }
}
