package org.xml.sax.helpers;

import java.io.IOException;
import java.util.Enumeration;
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
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;

/* loaded from: rt.jar:org/xml/sax/helpers/ParserAdapter.class */
public class ParserAdapter implements XMLReader, DocumentHandler {
    private static SecuritySupport ss = new SecuritySupport();
    private static final String FEATURES = "http://xml.org/sax/features/";
    private static final String NAMESPACES = "http://xml.org/sax/features/namespaces";
    private static final String NAMESPACE_PREFIXES = "http://xml.org/sax/features/namespace-prefixes";
    private static final String XMLNS_URIs = "http://xml.org/sax/features/xmlns-uris";
    private NamespaceSupport nsSupport;
    private AttributeListAdapter attAdapter;
    private boolean parsing;
    private String[] nameParts;
    private Parser parser;
    private AttributesImpl atts;
    private boolean namespaces;
    private boolean prefixes;
    private boolean uris;
    Locator locator;
    EntityResolver entityResolver;
    DTDHandler dtdHandler;
    ContentHandler contentHandler;
    ErrorHandler errorHandler;

    public ParserAdapter() throws SAXException {
        this.parsing = false;
        this.nameParts = new String[3];
        this.parser = null;
        this.atts = null;
        this.namespaces = true;
        this.prefixes = false;
        this.uris = false;
        this.entityResolver = null;
        this.dtdHandler = null;
        this.contentHandler = null;
        this.errorHandler = null;
        String driver = ss.getSystemProperty("org.xml.sax.parser");
        try {
            setup(ParserFactory.makeParser());
        } catch (ClassCastException e2) {
            throw new SAXException("SAX1 driver class " + driver + " does not implement org.xml.sax.Parser");
        } catch (ClassNotFoundException e1) {
            throw new SAXException("Cannot find SAX1 driver class " + driver, e1);
        } catch (IllegalAccessException e22) {
            throw new SAXException("SAX1 driver class " + driver + " found but cannot be loaded", e22);
        } catch (InstantiationException e3) {
            throw new SAXException("SAX1 driver class " + driver + " loaded but cannot be instantiated", e3);
        } catch (NullPointerException e4) {
            throw new SAXException("System property org.xml.sax.parser not specified");
        }
    }

    public ParserAdapter(Parser parser) {
        this.parsing = false;
        this.nameParts = new String[3];
        this.parser = null;
        this.atts = null;
        this.namespaces = true;
        this.prefixes = false;
        this.uris = false;
        this.entityResolver = null;
        this.dtdHandler = null;
        this.contentHandler = null;
        this.errorHandler = null;
        setup(parser);
    }

    private void setup(Parser parser) {
        if (parser == null) {
            throw new NullPointerException("Parser argument must not be null");
        }
        this.parser = parser;
        this.atts = new AttributesImpl();
        this.nsSupport = new NamespaceSupport();
        this.attAdapter = new AttributeListAdapter();
    }

    @Override // org.xml.sax.XMLReader
    public void setFeature(String name, boolean value) throws SAXNotRecognizedException, SAXNotSupportedException {
        if (name.equals("http://xml.org/sax/features/namespaces")) {
            checkNotParsing("feature", name);
            this.namespaces = value;
            if (!this.namespaces && !this.prefixes) {
                this.prefixes = true;
                return;
            }
            return;
        }
        if (name.equals("http://xml.org/sax/features/namespace-prefixes")) {
            checkNotParsing("feature", name);
            this.prefixes = value;
            if (!this.prefixes && !this.namespaces) {
                this.namespaces = true;
                return;
            }
            return;
        }
        if (name.equals(XMLNS_URIs)) {
            checkNotParsing("feature", name);
            this.uris = value;
            return;
        }
        throw new SAXNotRecognizedException("Feature: " + name);
    }

    @Override // org.xml.sax.XMLReader
    public boolean getFeature(String name) throws SAXNotRecognizedException, SAXNotSupportedException {
        if (name.equals("http://xml.org/sax/features/namespaces")) {
            return this.namespaces;
        }
        if (name.equals("http://xml.org/sax/features/namespace-prefixes")) {
            return this.prefixes;
        }
        if (name.equals(XMLNS_URIs)) {
            return this.uris;
        }
        throw new SAXNotRecognizedException("Feature: " + name);
    }

    @Override // org.xml.sax.XMLReader
    public void setProperty(String name, Object value) throws SAXNotRecognizedException, SAXNotSupportedException {
        throw new SAXNotRecognizedException("Property: " + name);
    }

    @Override // org.xml.sax.XMLReader
    public Object getProperty(String name) throws SAXNotRecognizedException, SAXNotSupportedException {
        throw new SAXNotRecognizedException("Property: " + name);
    }

    @Override // org.xml.sax.XMLReader
    public void setEntityResolver(EntityResolver resolver) {
        this.entityResolver = resolver;
    }

    @Override // org.xml.sax.XMLReader
    public EntityResolver getEntityResolver() {
        return this.entityResolver;
    }

    @Override // org.xml.sax.XMLReader
    public void setDTDHandler(DTDHandler handler) {
        this.dtdHandler = handler;
    }

    @Override // org.xml.sax.XMLReader
    public DTDHandler getDTDHandler() {
        return this.dtdHandler;
    }

    @Override // org.xml.sax.XMLReader
    public void setContentHandler(ContentHandler handler) {
        this.contentHandler = handler;
    }

    @Override // org.xml.sax.XMLReader
    public ContentHandler getContentHandler() {
        return this.contentHandler;
    }

    @Override // org.xml.sax.XMLReader
    public void setErrorHandler(ErrorHandler handler) {
        this.errorHandler = handler;
    }

    @Override // org.xml.sax.XMLReader
    public ErrorHandler getErrorHandler() {
        return this.errorHandler;
    }

    @Override // org.xml.sax.XMLReader
    public void parse(String systemId) throws SAXException, IOException {
        parse(new InputSource(systemId));
    }

    @Override // org.xml.sax.XMLReader
    public void parse(InputSource input) throws SAXException, IOException {
        if (this.parsing) {
            throw new SAXException("Parser is already in use");
        }
        setupParser();
        this.parsing = true;
        try {
            this.parser.parse(input);
            this.parsing = false;
        } finally {
            this.parsing = false;
        }
    }

    @Override // org.xml.sax.DocumentHandler
    public void setDocumentLocator(Locator locator) {
        this.locator = locator;
        if (this.contentHandler != null) {
            this.contentHandler.setDocumentLocator(locator);
        }
    }

    @Override // org.xml.sax.DocumentHandler
    public void startDocument() throws SAXException {
        if (this.contentHandler != null) {
            this.contentHandler.startDocument();
        }
    }

    @Override // org.xml.sax.DocumentHandler
    public void endDocument() throws SAXException {
        if (this.contentHandler != null) {
            this.contentHandler.endDocument();
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:71:0x0191 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    @Override // org.xml.sax.DocumentHandler
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void startElement(java.lang.String r8, org.xml.sax.AttributeList r9) throws org.xml.sax.SAXException {
        /*
            Method dump skipped, instructions count: 572
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: org.xml.sax.helpers.ParserAdapter.startElement(java.lang.String, org.xml.sax.AttributeList):void");
    }

    @Override // org.xml.sax.DocumentHandler
    public void endElement(String qName) throws SAXException {
        if (!this.namespaces) {
            if (this.contentHandler != null) {
                this.contentHandler.endElement("", "", qName.intern());
                return;
            }
            return;
        }
        String[] names = processName(qName, false, false);
        if (this.contentHandler != null) {
            this.contentHandler.endElement(names[0], names[1], names[2]);
            Enumeration prefixes = this.nsSupport.getDeclaredPrefixes();
            while (prefixes.hasMoreElements()) {
                String prefix = (String) prefixes.nextElement2();
                this.contentHandler.endPrefixMapping(prefix);
            }
        }
        this.nsSupport.popContext();
    }

    @Override // org.xml.sax.DocumentHandler
    public void characters(char[] ch, int start, int length) throws SAXException {
        if (this.contentHandler != null) {
            this.contentHandler.characters(ch, start, length);
        }
    }

    @Override // org.xml.sax.DocumentHandler
    public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {
        if (this.contentHandler != null) {
            this.contentHandler.ignorableWhitespace(ch, start, length);
        }
    }

    @Override // org.xml.sax.DocumentHandler
    public void processingInstruction(String target, String data) throws SAXException {
        if (this.contentHandler != null) {
            this.contentHandler.processingInstruction(target, data);
        }
    }

    private void setupParser() {
        if (!this.prefixes && !this.namespaces) {
            throw new IllegalStateException();
        }
        this.nsSupport.reset();
        if (this.uris) {
            this.nsSupport.setNamespaceDeclUris(true);
        }
        if (this.entityResolver != null) {
            this.parser.setEntityResolver(this.entityResolver);
        }
        if (this.dtdHandler != null) {
            this.parser.setDTDHandler(this.dtdHandler);
        }
        if (this.errorHandler != null) {
            this.parser.setErrorHandler(this.errorHandler);
        }
        this.parser.setDocumentHandler(this);
        this.locator = null;
    }

    private String[] processName(String qName, boolean isAttribute, boolean useException) throws SAXException {
        String[] parts = this.nsSupport.processName(qName, this.nameParts, isAttribute);
        if (parts == null) {
            if (useException) {
                throw makeException("Undeclared prefix: " + qName);
            }
            reportError("Undeclared prefix: " + qName);
            parts = new String[]{"", "", qName.intern()};
        }
        return parts;
    }

    void reportError(String message) throws SAXException {
        if (this.errorHandler != null) {
            this.errorHandler.error(makeException(message));
        }
    }

    private SAXParseException makeException(String message) {
        if (this.locator != null) {
            return new SAXParseException(message, this.locator);
        }
        return new SAXParseException(message, null, null, -1, -1);
    }

    private void checkNotParsing(String type, String name) throws SAXNotSupportedException {
        if (this.parsing) {
            throw new SAXNotSupportedException("Cannot change " + type + ' ' + name + " while parsing");
        }
    }

    /* loaded from: rt.jar:org/xml/sax/helpers/ParserAdapter$AttributeListAdapter.class */
    final class AttributeListAdapter implements Attributes {
        private AttributeList qAtts;

        AttributeListAdapter() {
        }

        void setAttributeList(AttributeList qAtts) {
            this.qAtts = qAtts;
        }

        @Override // org.xml.sax.Attributes
        public int getLength() {
            return this.qAtts.getLength();
        }

        @Override // org.xml.sax.Attributes
        public String getURI(int i2) {
            return "";
        }

        @Override // org.xml.sax.Attributes
        public String getLocalName(int i2) {
            return "";
        }

        @Override // org.xml.sax.Attributes
        public String getQName(int i2) {
            return this.qAtts.getName(i2).intern();
        }

        @Override // org.xml.sax.Attributes
        public String getType(int i2) {
            return this.qAtts.getType(i2).intern();
        }

        @Override // org.xml.sax.Attributes
        public String getValue(int i2) {
            return this.qAtts.getValue(i2);
        }

        @Override // org.xml.sax.Attributes
        public int getIndex(String uri, String localName) {
            return -1;
        }

        @Override // org.xml.sax.Attributes
        public int getIndex(String qName) {
            int max = ParserAdapter.this.atts.getLength();
            for (int i2 = 0; i2 < max; i2++) {
                if (this.qAtts.getName(i2).equals(qName)) {
                    return i2;
                }
            }
            return -1;
        }

        @Override // org.xml.sax.Attributes
        public String getType(String uri, String localName) {
            return null;
        }

        @Override // org.xml.sax.Attributes
        public String getType(String qName) {
            return this.qAtts.getType(qName).intern();
        }

        @Override // org.xml.sax.Attributes
        public String getValue(String uri, String localName) {
            return null;
        }

        @Override // org.xml.sax.Attributes
        public String getValue(String qName) {
            return this.qAtts.getValue(qName);
        }
    }
}
