package com.sun.org.apache.xalan.internal.xsltc.trax;

import java.util.Vector;
import javax.xml.stream.Location;
import javax.xml.stream.XMLReporter;
import javax.xml.stream.XMLStreamException;
import org.xml.sax.Attributes;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.ext.LexicalHandler;
import org.xml.sax.helpers.DefaultHandler;

/* loaded from: rt.jar:com/sun/org/apache/xalan/internal/xsltc/trax/SAX2StAXBaseWriter.class */
public abstract class SAX2StAXBaseWriter extends DefaultHandler implements LexicalHandler {
    protected boolean isCDATA;
    protected StringBuffer CDATABuffer;
    protected Vector namespaces;
    protected Locator docLocator;
    protected XMLReporter reporter;

    public SAX2StAXBaseWriter() {
    }

    public SAX2StAXBaseWriter(XMLReporter reporter) {
        this.reporter = reporter;
    }

    public void setXMLReporter(XMLReporter reporter) {
        this.reporter = reporter;
    }

    @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
    public void setDocumentLocator(Locator locator) {
        this.docLocator = locator;
    }

    public Location getCurrentLocation() {
        if (this.docLocator != null) {
            return new SAXLocation(this.docLocator);
        }
        return null;
    }

    @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ErrorHandler
    public void error(SAXParseException e2) throws SAXException {
        reportException("ERROR", e2);
    }

    @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ErrorHandler
    public void fatalError(SAXParseException e2) throws SAXException {
        reportException("FATAL", e2);
    }

    @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ErrorHandler
    public void warning(SAXParseException e2) throws SAXException {
        reportException("WARNING", e2);
    }

    @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
    public void startDocument() throws SAXException {
        this.namespaces = new Vector(2);
    }

    @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
    public void endDocument() throws SAXException {
        this.namespaces = null;
    }

    @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        this.namespaces = null;
    }

    @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
    public void endElement(String uri, String localName, String qName) throws SAXException {
        this.namespaces = null;
    }

    @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
    public void startPrefixMapping(String prefix, String uri) throws SAXException {
        if (prefix == null) {
            prefix = "";
        } else if (prefix.equals("xml")) {
            return;
        }
        if (this.namespaces == null) {
            this.namespaces = new Vector(2);
        }
        this.namespaces.addElement(prefix);
        this.namespaces.addElement(uri);
    }

    @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
    public void endPrefixMapping(String prefix) throws SAXException {
    }

    @Override // org.xml.sax.ext.LexicalHandler
    public void startCDATA() throws SAXException {
        this.isCDATA = true;
        if (this.CDATABuffer == null) {
            this.CDATABuffer = new StringBuffer();
        } else {
            this.CDATABuffer.setLength(0);
        }
    }

    @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
    public void characters(char[] ch, int start, int length) throws SAXException {
        if (this.isCDATA) {
            this.CDATABuffer.append(ch, start, length);
        }
    }

    @Override // org.xml.sax.ext.LexicalHandler
    public void endCDATA() throws SAXException {
        this.isCDATA = false;
        this.CDATABuffer.setLength(0);
    }

    @Override // org.xml.sax.ext.LexicalHandler
    public void comment(char[] ch, int start, int length) throws SAXException {
    }

    @Override // org.xml.sax.ext.LexicalHandler
    public void endDTD() throws SAXException {
    }

    @Override // org.xml.sax.ext.LexicalHandler
    public void endEntity(String name) throws SAXException {
    }

    @Override // org.xml.sax.ext.LexicalHandler
    public void startDTD(String name, String publicId, String systemId) throws SAXException {
    }

    @Override // org.xml.sax.ext.LexicalHandler
    public void startEntity(String name) throws SAXException {
    }

    protected void reportException(String type, SAXException e2) throws SAXException {
        if (this.reporter != null) {
            try {
                this.reporter.report(e2.getMessage(), type, e2, getCurrentLocation());
            } catch (XMLStreamException e1) {
                throw new SAXException(e1);
            }
        }
    }

    public static final void parseQName(String qName, String[] results) {
        String prefix;
        String local;
        int idx = qName.indexOf(58);
        if (idx >= 0) {
            prefix = qName.substring(0, idx);
            local = qName.substring(idx + 1);
        } else {
            prefix = "";
            local = qName;
        }
        results[0] = prefix;
        results[1] = local;
    }

    /* loaded from: rt.jar:com/sun/org/apache/xalan/internal/xsltc/trax/SAX2StAXBaseWriter$SAXLocation.class */
    private static final class SAXLocation implements Location {
        private int lineNumber;
        private int columnNumber;
        private String publicId;
        private String systemId;

        private SAXLocation(Locator locator) {
            this.lineNumber = locator.getLineNumber();
            this.columnNumber = locator.getColumnNumber();
            this.publicId = locator.getPublicId();
            this.systemId = locator.getSystemId();
        }

        @Override // javax.xml.stream.Location
        public int getLineNumber() {
            return this.lineNumber;
        }

        @Override // javax.xml.stream.Location
        public int getColumnNumber() {
            return this.columnNumber;
        }

        @Override // javax.xml.stream.Location
        public int getCharacterOffset() {
            return -1;
        }

        @Override // javax.xml.stream.Location
        public String getPublicId() {
            return this.publicId;
        }

        @Override // javax.xml.stream.Location
        public String getSystemId() {
            return this.systemId;
        }
    }
}
