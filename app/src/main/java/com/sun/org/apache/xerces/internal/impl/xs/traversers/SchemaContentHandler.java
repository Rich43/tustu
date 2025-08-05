package com.sun.org.apache.xerces.internal.impl.xs.traversers;

import com.sun.org.apache.xerces.internal.impl.xs.opti.SchemaDOMParser;
import com.sun.org.apache.xerces.internal.util.NamespaceSupport;
import com.sun.org.apache.xerces.internal.util.SAXLocatorWrapper;
import com.sun.org.apache.xerces.internal.util.SymbolTable;
import com.sun.org.apache.xerces.internal.util.XMLAttributesImpl;
import com.sun.org.apache.xerces.internal.util.XMLStringBuffer;
import com.sun.org.apache.xerces.internal.util.XMLSymbols;
import com.sun.org.apache.xerces.internal.xni.NamespaceContext;
import com.sun.org.apache.xerces.internal.xni.QName;
import com.sun.org.apache.xerces.internal.xni.XMLString;
import com.sun.org.apache.xerces.internal.xni.XNIException;
import com.sun.org.apache.xerces.internal.xni.parser.XMLParseException;
import org.w3c.dom.Document;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.LocatorImpl;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/impl/xs/traversers/SchemaContentHandler.class */
final class SchemaContentHandler implements ContentHandler {
    private SymbolTable fSymbolTable;
    private SchemaDOMParser fSchemaDOMParser;
    private boolean fNeedPushNSContext;
    private final SAXLocatorWrapper fSAXLocatorWrapper = new SAXLocatorWrapper();
    private NamespaceSupport fNamespaceContext = new NamespaceSupport();
    private boolean fNamespacePrefixes = false;
    private boolean fStringsInternalized = false;
    private final QName fElementQName = new QName();
    private final QName fAttributeQName = new QName();
    private final XMLAttributesImpl fAttributes = new XMLAttributesImpl();
    private final XMLString fTempString = new XMLString();
    private final XMLStringBuffer fStringBuffer = new XMLStringBuffer();

    public Document getDocument() {
        return this.fSchemaDOMParser.getDocument();
    }

    @Override // org.xml.sax.ContentHandler
    public void setDocumentLocator(Locator locator) {
        this.fSAXLocatorWrapper.setLocator(locator);
    }

    @Override // org.xml.sax.ContentHandler
    public void startDocument() throws SAXException {
        this.fNeedPushNSContext = true;
        this.fNamespaceContext.reset();
        try {
            this.fSchemaDOMParser.startDocument(this.fSAXLocatorWrapper, null, this.fNamespaceContext, null);
        } catch (XMLParseException e2) {
            convertToSAXParseException(e2);
        } catch (XNIException e3) {
            convertToSAXException(e3);
        }
    }

    @Override // org.xml.sax.ContentHandler
    public void endDocument() throws SAXException {
        this.fSAXLocatorWrapper.setLocator(null);
        try {
            this.fSchemaDOMParser.endDocument(null);
        } catch (XMLParseException e2) {
            convertToSAXParseException(e2);
        } catch (XNIException e3) {
            convertToSAXException(e3);
        }
    }

    @Override // org.xml.sax.ContentHandler
    public void startPrefixMapping(String prefix, String uri) throws SAXException {
        if (this.fNeedPushNSContext) {
            this.fNeedPushNSContext = false;
            this.fNamespaceContext.pushContext();
        }
        if (!this.fStringsInternalized) {
            prefix = prefix != null ? this.fSymbolTable.addSymbol(prefix) : XMLSymbols.EMPTY_STRING;
            uri = (uri == null || uri.length() <= 0) ? null : this.fSymbolTable.addSymbol(uri);
        } else {
            if (prefix == null) {
                prefix = XMLSymbols.EMPTY_STRING;
            }
            if (uri != null && uri.length() == 0) {
                uri = null;
            }
        }
        this.fNamespaceContext.declarePrefix(prefix, uri);
    }

    @Override // org.xml.sax.ContentHandler
    public void endPrefixMapping(String prefix) throws SAXException {
    }

    @Override // org.xml.sax.ContentHandler
    public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
        int prefixCount;
        if (this.fNeedPushNSContext) {
            this.fNamespaceContext.pushContext();
        }
        this.fNeedPushNSContext = true;
        fillQName(this.fElementQName, uri, localName, qName);
        fillXMLAttributes(atts);
        if (!this.fNamespacePrefixes && (prefixCount = this.fNamespaceContext.getDeclaredPrefixCount()) > 0) {
            addNamespaceDeclarations(prefixCount);
        }
        try {
            this.fSchemaDOMParser.startElement(this.fElementQName, this.fAttributes, null);
        } catch (XMLParseException e2) {
            convertToSAXParseException(e2);
        } catch (XNIException e3) {
            convertToSAXException(e3);
        }
    }

    @Override // org.xml.sax.ContentHandler
    public void endElement(String uri, String localName, String qName) throws SAXException {
        fillQName(this.fElementQName, uri, localName, qName);
        try {
            try {
                try {
                    this.fSchemaDOMParser.endElement(this.fElementQName, null);
                    this.fNamespaceContext.popContext();
                } catch (XMLParseException e2) {
                    convertToSAXParseException(e2);
                    this.fNamespaceContext.popContext();
                }
            } catch (XNIException e3) {
                convertToSAXException(e3);
                this.fNamespaceContext.popContext();
            }
        } catch (Throwable th) {
            this.fNamespaceContext.popContext();
            throw th;
        }
    }

    @Override // org.xml.sax.ContentHandler
    public void characters(char[] ch, int start, int length) throws SAXException {
        try {
            this.fTempString.setValues(ch, start, length);
            this.fSchemaDOMParser.characters(this.fTempString, null);
        } catch (XMLParseException e2) {
            convertToSAXParseException(e2);
        } catch (XNIException e3) {
            convertToSAXException(e3);
        }
    }

    @Override // org.xml.sax.ContentHandler
    public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {
        try {
            this.fTempString.setValues(ch, start, length);
            this.fSchemaDOMParser.ignorableWhitespace(this.fTempString, null);
        } catch (XMLParseException e2) {
            convertToSAXParseException(e2);
        } catch (XNIException e3) {
            convertToSAXException(e3);
        }
    }

    @Override // org.xml.sax.ContentHandler
    public void processingInstruction(String target, String data) throws SAXException {
        try {
            this.fTempString.setValues(data.toCharArray(), 0, data.length());
            this.fSchemaDOMParser.processingInstruction(target, this.fTempString, null);
        } catch (XMLParseException e2) {
            convertToSAXParseException(e2);
        } catch (XNIException e3) {
            convertToSAXException(e3);
        }
    }

    @Override // org.xml.sax.ContentHandler
    public void skippedEntity(String arg) throws SAXException {
    }

    private void fillQName(QName toFill, String uri, String localpart, String rawname) {
        if (!this.fStringsInternalized) {
            uri = (uri == null || uri.length() <= 0) ? null : this.fSymbolTable.addSymbol(uri);
            localpart = localpart != null ? this.fSymbolTable.addSymbol(localpart) : XMLSymbols.EMPTY_STRING;
            rawname = rawname != null ? this.fSymbolTable.addSymbol(rawname) : XMLSymbols.EMPTY_STRING;
        } else {
            if (uri != null && uri.length() == 0) {
                uri = null;
            }
            if (localpart == null) {
                localpart = XMLSymbols.EMPTY_STRING;
            }
            if (rawname == null) {
                rawname = XMLSymbols.EMPTY_STRING;
            }
        }
        String prefix = XMLSymbols.EMPTY_STRING;
        int prefixIdx = rawname.indexOf(58);
        if (prefixIdx != -1) {
            prefix = this.fSymbolTable.addSymbol(rawname.substring(0, prefixIdx));
            if (localpart == XMLSymbols.EMPTY_STRING) {
                localpart = this.fSymbolTable.addSymbol(rawname.substring(prefixIdx + 1));
            }
        } else if (localpart == XMLSymbols.EMPTY_STRING) {
            localpart = rawname;
        }
        toFill.setValues(prefix, localpart, rawname, uri);
    }

    private void fillXMLAttributes(Attributes atts) {
        this.fAttributes.removeAllAttributes();
        int attrCount = atts.getLength();
        for (int i2 = 0; i2 < attrCount; i2++) {
            fillQName(this.fAttributeQName, atts.getURI(i2), atts.getLocalName(i2), atts.getQName(i2));
            String type = atts.getType(i2);
            this.fAttributes.addAttributeNS(this.fAttributeQName, type != null ? type : XMLSymbols.fCDATASymbol, atts.getValue(i2));
            this.fAttributes.setSpecified(i2, true);
        }
    }

    private void addNamespaceDeclarations(int prefixCount) {
        String prefix;
        String localpart;
        String rawname;
        for (int i2 = 0; i2 < prefixCount; i2++) {
            String nsPrefix = this.fNamespaceContext.getDeclaredPrefixAt(i2);
            String nsURI = this.fNamespaceContext.getURI(nsPrefix);
            if (nsPrefix.length() > 0) {
                prefix = XMLSymbols.PREFIX_XMLNS;
                localpart = nsPrefix;
                this.fStringBuffer.clear();
                this.fStringBuffer.append(prefix);
                this.fStringBuffer.append(':');
                this.fStringBuffer.append(localpart);
                rawname = this.fSymbolTable.addSymbol(this.fStringBuffer.ch, this.fStringBuffer.offset, this.fStringBuffer.length);
            } else {
                prefix = XMLSymbols.EMPTY_STRING;
                localpart = XMLSymbols.PREFIX_XMLNS;
                rawname = XMLSymbols.PREFIX_XMLNS;
            }
            this.fAttributeQName.setValues(prefix, localpart, rawname, NamespaceContext.XMLNS_URI);
            this.fAttributes.addAttribute(this.fAttributeQName, XMLSymbols.fCDATASymbol, nsURI != null ? nsURI : XMLSymbols.EMPTY_STRING);
        }
    }

    public void reset(SchemaDOMParser schemaDOMParser, SymbolTable symbolTable, boolean namespacePrefixes, boolean stringsInternalized) {
        this.fSchemaDOMParser = schemaDOMParser;
        this.fSymbolTable = symbolTable;
        this.fNamespacePrefixes = namespacePrefixes;
        this.fStringsInternalized = stringsInternalized;
    }

    static void convertToSAXParseException(XMLParseException e2) throws SAXException {
        Exception ex = e2.getException();
        if (ex == null) {
            LocatorImpl locatorImpl = new LocatorImpl();
            locatorImpl.setPublicId(e2.getPublicId());
            locatorImpl.setSystemId(e2.getExpandedSystemId());
            locatorImpl.setLineNumber(e2.getLineNumber());
            locatorImpl.setColumnNumber(e2.getColumnNumber());
            throw new SAXParseException(e2.getMessage(), locatorImpl);
        }
        if (ex instanceof SAXException) {
            throw ((SAXException) ex);
        }
        throw new SAXException(ex);
    }

    static void convertToSAXException(XNIException e2) throws SAXException {
        Exception ex = e2.getException();
        if (ex == null) {
            throw new SAXException(e2.getMessage());
        }
        if (ex instanceof SAXException) {
            throw ((SAXException) ex);
        }
        throw new SAXException(ex);
    }
}
