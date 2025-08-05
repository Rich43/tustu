package org.xml.sax.helpers;

import java.io.IOException;
import java.util.Locale;
import org.xml.sax.AttributeList;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.DTDHandler;
import org.xml.sax.DocumentHandler;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.Parser;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.XMLReader;

/* loaded from: rt.jar:org/xml/sax/helpers/XMLReaderAdapter.class */
public class XMLReaderAdapter implements Parser, ContentHandler {
    XMLReader xmlReader;
    DocumentHandler documentHandler;
    AttributesAdapter qAtts;

    public XMLReaderAdapter() throws SAXException {
        setup(XMLReaderFactory.createXMLReader());
    }

    public XMLReaderAdapter(XMLReader xmlReader) {
        setup(xmlReader);
    }

    private void setup(XMLReader xmlReader) {
        if (xmlReader == null) {
            throw new NullPointerException("XMLReader must not be null");
        }
        this.xmlReader = xmlReader;
        this.qAtts = new AttributesAdapter();
    }

    @Override // org.xml.sax.Parser
    public void setLocale(Locale locale) throws SAXException {
        throw new SAXNotSupportedException("setLocale not supported");
    }

    @Override // org.xml.sax.Parser, org.xml.sax.XMLReader
    public void setEntityResolver(EntityResolver resolver) {
        this.xmlReader.setEntityResolver(resolver);
    }

    @Override // org.xml.sax.Parser, org.xml.sax.XMLReader
    public void setDTDHandler(DTDHandler handler) {
        this.xmlReader.setDTDHandler(handler);
    }

    @Override // org.xml.sax.Parser
    public void setDocumentHandler(DocumentHandler handler) {
        this.documentHandler = handler;
    }

    @Override // org.xml.sax.Parser, org.xml.sax.XMLReader
    public void setErrorHandler(ErrorHandler handler) {
        this.xmlReader.setErrorHandler(handler);
    }

    @Override // org.xml.sax.Parser, org.xml.sax.XMLReader
    public void parse(String systemId) throws SAXException, IOException {
        parse(new InputSource(systemId));
    }

    @Override // org.xml.sax.Parser, org.xml.sax.XMLReader
    public void parse(InputSource input) throws SAXException, IOException {
        setupXMLReader();
        this.xmlReader.parse(input);
    }

    private void setupXMLReader() throws SAXException {
        this.xmlReader.setFeature("http://xml.org/sax/features/namespace-prefixes", true);
        try {
            this.xmlReader.setFeature("http://xml.org/sax/features/namespaces", false);
        } catch (SAXException e2) {
        }
        this.xmlReader.setContentHandler(this);
    }

    @Override // org.xml.sax.ContentHandler
    public void setDocumentLocator(Locator locator) {
        if (this.documentHandler != null) {
            this.documentHandler.setDocumentLocator(locator);
        }
    }

    @Override // org.xml.sax.ContentHandler
    public void startDocument() throws SAXException {
        if (this.documentHandler != null) {
            this.documentHandler.startDocument();
        }
    }

    @Override // org.xml.sax.ContentHandler
    public void endDocument() throws SAXException {
        if (this.documentHandler != null) {
            this.documentHandler.endDocument();
        }
    }

    @Override // org.xml.sax.ContentHandler
    public void startPrefixMapping(String prefix, String uri) {
    }

    @Override // org.xml.sax.ContentHandler
    public void endPrefixMapping(String prefix) {
    }

    @Override // org.xml.sax.ContentHandler
    public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
        if (this.documentHandler != null) {
            this.qAtts.setAttributes(atts);
            this.documentHandler.startElement(qName, this.qAtts);
        }
    }

    @Override // org.xml.sax.ContentHandler
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (this.documentHandler != null) {
            this.documentHandler.endElement(qName);
        }
    }

    @Override // org.xml.sax.ContentHandler
    public void characters(char[] ch, int start, int length) throws SAXException {
        if (this.documentHandler != null) {
            this.documentHandler.characters(ch, start, length);
        }
    }

    @Override // org.xml.sax.ContentHandler
    public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {
        if (this.documentHandler != null) {
            this.documentHandler.ignorableWhitespace(ch, start, length);
        }
    }

    @Override // org.xml.sax.ContentHandler
    public void processingInstruction(String target, String data) throws SAXException {
        if (this.documentHandler != null) {
            this.documentHandler.processingInstruction(target, data);
        }
    }

    @Override // org.xml.sax.ContentHandler
    public void skippedEntity(String name) throws SAXException {
    }

    /* loaded from: rt.jar:org/xml/sax/helpers/XMLReaderAdapter$AttributesAdapter.class */
    final class AttributesAdapter implements AttributeList {
        private Attributes attributes;

        AttributesAdapter() {
        }

        void setAttributes(Attributes attributes) {
            this.attributes = attributes;
        }

        @Override // org.xml.sax.AttributeList
        public int getLength() {
            return this.attributes.getLength();
        }

        @Override // org.xml.sax.AttributeList
        public String getName(int i2) {
            return this.attributes.getQName(i2);
        }

        @Override // org.xml.sax.AttributeList
        public String getType(int i2) {
            return this.attributes.getType(i2);
        }

        @Override // org.xml.sax.AttributeList
        public String getValue(int i2) {
            return this.attributes.getValue(i2);
        }

        @Override // org.xml.sax.AttributeList
        public String getType(String qName) {
            return this.attributes.getType(qName);
        }

        @Override // org.xml.sax.AttributeList
        public String getValue(String qName) {
            return this.attributes.getValue(qName);
        }
    }
}
