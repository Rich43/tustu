package com.sun.xml.internal.stream.buffer.sax;

import com.sun.xml.internal.stream.buffer.AbstractCreator;
import com.sun.xml.internal.stream.buffer.MutableXMLStreamBuffer;
import java.io.IOException;
import java.io.InputStream;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.DTDHandler;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;
import org.xml.sax.ext.LexicalHandler;

/* loaded from: rt.jar:com/sun/xml/internal/stream/buffer/sax/SAXBufferCreator.class */
public class SAXBufferCreator extends AbstractCreator implements EntityResolver, DTDHandler, ContentHandler, ErrorHandler, LexicalHandler {
    protected String[] _namespaceAttributes;
    protected int _namespaceAttributesPtr;
    private int depth;

    public SAXBufferCreator() {
        this.depth = 0;
        this._namespaceAttributes = new String[32];
    }

    public SAXBufferCreator(MutableXMLStreamBuffer buffer) {
        this();
        setBuffer(buffer);
    }

    public MutableXMLStreamBuffer create(XMLReader reader, InputStream in) throws SAXException, IOException {
        return create(reader, in, null);
    }

    public MutableXMLStreamBuffer create(XMLReader reader, InputStream in, String systemId) throws SAXException, IOException {
        if (this._buffer == null) {
            createBuffer();
        }
        this._buffer.setSystemId(systemId);
        reader.setContentHandler(this);
        reader.setProperty("http://xml.org/sax/properties/lexical-handler", this);
        try {
            setHasInternedStrings(reader.getFeature("http://xml.org/sax/features/string-interning"));
        } catch (SAXException e2) {
        }
        if (systemId != null) {
            InputSource s2 = new InputSource(systemId);
            s2.setByteStream(in);
            reader.parse(s2);
        } else {
            reader.parse(new InputSource(in));
        }
        return getXMLStreamBuffer();
    }

    public void reset() {
        this._buffer = null;
        this._namespaceAttributesPtr = 0;
        this.depth = 0;
    }

    @Override // org.xml.sax.ContentHandler
    public void startDocument() throws SAXException {
        storeStructure(16);
    }

    @Override // org.xml.sax.ContentHandler
    public void endDocument() throws SAXException {
        storeStructure(144);
    }

    @Override // org.xml.sax.ContentHandler
    public void startPrefixMapping(String prefix, String uri) throws SAXException {
        cacheNamespaceAttribute(prefix, uri);
    }

    @Override // org.xml.sax.ContentHandler
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        storeQualifiedName(32, uri, localName, qName);
        if (this._namespaceAttributesPtr > 0) {
            storeNamespaceAttributes();
        }
        if (attributes.getLength() > 0) {
            storeAttributes(attributes);
        }
        this.depth++;
    }

    @Override // org.xml.sax.ContentHandler
    public void endElement(String uri, String localName, String qName) throws SAXException {
        storeStructure(144);
        int i2 = this.depth - 1;
        this.depth = i2;
        if (i2 == 0) {
            increaseTreeCount();
        }
    }

    @Override // org.xml.sax.ContentHandler
    public void characters(char[] ch, int start, int length) throws SAXException {
        storeContentCharacters(80, ch, start, length);
    }

    @Override // org.xml.sax.ContentHandler
    public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {
        characters(ch, start, length);
    }

    @Override // org.xml.sax.ContentHandler
    public void processingInstruction(String target, String data) throws SAXException {
        storeStructure(112);
        storeStructureString(target);
        storeStructureString(data);
    }

    @Override // org.xml.sax.ext.LexicalHandler
    public void comment(char[] ch, int start, int length) throws SAXException {
        storeContentCharacters(96, ch, start, length);
    }

    private void cacheNamespaceAttribute(String prefix, String uri) {
        String[] strArr = this._namespaceAttributes;
        int i2 = this._namespaceAttributesPtr;
        this._namespaceAttributesPtr = i2 + 1;
        strArr[i2] = prefix;
        String[] strArr2 = this._namespaceAttributes;
        int i3 = this._namespaceAttributesPtr;
        this._namespaceAttributesPtr = i3 + 1;
        strArr2[i3] = uri;
        if (this._namespaceAttributesPtr == this._namespaceAttributes.length) {
            String[] namespaceAttributes = new String[this._namespaceAttributesPtr * 2];
            System.arraycopy(this._namespaceAttributes, 0, namespaceAttributes, 0, this._namespaceAttributesPtr);
            this._namespaceAttributes = namespaceAttributes;
        }
    }

    private void storeNamespaceAttributes() {
        for (int i2 = 0; i2 < this._namespaceAttributesPtr; i2 += 2) {
            int item = 64;
            if (this._namespaceAttributes[i2].length() > 0) {
                item = 64 | 1;
                storeStructureString(this._namespaceAttributes[i2]);
            }
            if (this._namespaceAttributes[i2 + 1].length() > 0) {
                item |= 2;
                storeStructureString(this._namespaceAttributes[i2 + 1]);
            }
            storeStructure(item);
        }
        this._namespaceAttributesPtr = 0;
    }

    private void storeAttributes(Attributes attributes) {
        for (int i2 = 0; i2 < attributes.getLength(); i2++) {
            if (!attributes.getQName(i2).startsWith("xmlns")) {
                storeQualifiedName(48, attributes.getURI(i2), attributes.getLocalName(i2), attributes.getQName(i2));
                storeStructureString(attributes.getType(i2));
                storeContentString(attributes.getValue(i2));
            }
        }
    }

    private void storeQualifiedName(int item, String uri, String localName, String qName) {
        if (uri.length() > 0) {
            item |= 2;
            storeStructureString(uri);
        }
        storeStructureString(localName);
        if (qName.indexOf(58) >= 0) {
            item |= 4;
            storeStructureString(qName);
        }
        storeStructure(item);
    }

    @Override // org.xml.sax.EntityResolver
    public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
        return null;
    }

    @Override // org.xml.sax.DTDHandler
    public void notationDecl(String name, String publicId, String systemId) throws SAXException {
    }

    @Override // org.xml.sax.DTDHandler
    public void unparsedEntityDecl(String name, String publicId, String systemId, String notationName) throws SAXException {
    }

    @Override // org.xml.sax.ContentHandler
    public void setDocumentLocator(Locator locator) {
    }

    @Override // org.xml.sax.ContentHandler
    public void endPrefixMapping(String prefix) throws SAXException {
    }

    @Override // org.xml.sax.ContentHandler
    public void skippedEntity(String name) throws SAXException {
    }

    @Override // org.xml.sax.ext.LexicalHandler
    public void startDTD(String name, String publicId, String systemId) throws SAXException {
    }

    @Override // org.xml.sax.ext.LexicalHandler
    public void endDTD() throws SAXException {
    }

    @Override // org.xml.sax.ext.LexicalHandler
    public void startEntity(String name) throws SAXException {
    }

    @Override // org.xml.sax.ext.LexicalHandler
    public void endEntity(String name) throws SAXException {
    }

    @Override // org.xml.sax.ext.LexicalHandler
    public void startCDATA() throws SAXException {
    }

    @Override // org.xml.sax.ext.LexicalHandler
    public void endCDATA() throws SAXException {
    }

    @Override // org.xml.sax.ErrorHandler
    public void warning(SAXParseException e2) throws SAXException {
    }

    @Override // org.xml.sax.ErrorHandler
    public void error(SAXParseException e2) throws SAXException {
    }

    @Override // org.xml.sax.ErrorHandler
    public void fatalError(SAXParseException e2) throws SAXException {
        throw e2;
    }
}
