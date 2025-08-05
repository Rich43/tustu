package com.sun.org.apache.xerces.internal.util;

import com.sun.org.apache.xerces.internal.impl.xs.util.SimpleLocator;
import com.sun.org.apache.xerces.internal.jaxp.validation.WrappedSAXException;
import com.sun.org.apache.xerces.internal.xni.QName;
import com.sun.org.apache.xerces.internal.xni.XMLAttributes;
import com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler;
import com.sun.org.apache.xerces.internal.xni.XMLLocator;
import com.sun.org.apache.xerces.internal.xni.XMLString;
import com.sun.org.apache.xerces.internal.xni.XNIException;
import com.sun.org.apache.xerces.internal.xni.parser.XMLDocumentSource;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/util/SAX2XNI.class */
public class SAX2XNI implements ContentHandler, XMLDocumentSource {
    private XMLDocumentHandler fCore;
    private Locator locator;
    private final NamespaceSupport nsContext = new NamespaceSupport();
    private final SymbolTable symbolTable = new SymbolTable();
    private final XMLAttributes xa = new XMLAttributesImpl();

    public SAX2XNI(XMLDocumentHandler core) {
        this.fCore = core;
    }

    @Override // com.sun.org.apache.xerces.internal.xni.parser.XMLDocumentSource
    public void setDocumentHandler(XMLDocumentHandler handler) {
        this.fCore = handler;
    }

    @Override // com.sun.org.apache.xerces.internal.xni.parser.XMLDocumentSource
    public XMLDocumentHandler getDocumentHandler() {
        return this.fCore;
    }

    @Override // org.xml.sax.ContentHandler
    public void startDocument() throws SAXException, XNIException {
        XMLLocator xmlLocator;
        try {
            this.nsContext.reset();
            if (this.locator == null) {
                xmlLocator = new SimpleLocator(null, null, -1, -1);
            } else {
                xmlLocator = new LocatorWrapper(this.locator);
            }
            this.fCore.startDocument(xmlLocator, null, this.nsContext, null);
        } catch (WrappedSAXException e2) {
            throw e2.exception;
        }
    }

    @Override // org.xml.sax.ContentHandler
    public void endDocument() throws SAXException, XNIException {
        try {
            this.fCore.endDocument(null);
        } catch (WrappedSAXException e2) {
            throw e2.exception;
        }
    }

    @Override // org.xml.sax.ContentHandler
    public void startElement(String uri, String local, String qname, Attributes att) throws SAXException, XNIException {
        try {
            this.fCore.startElement(createQName(uri, local, qname), createAttributes(att), null);
        } catch (WrappedSAXException e2) {
            throw e2.exception;
        }
    }

    @Override // org.xml.sax.ContentHandler
    public void endElement(String uri, String local, String qname) throws SAXException, XNIException {
        try {
            this.fCore.endElement(createQName(uri, local, qname), null);
        } catch (WrappedSAXException e2) {
            throw e2.exception;
        }
    }

    @Override // org.xml.sax.ContentHandler
    public void characters(char[] buf, int offset, int len) throws SAXException, XNIException {
        try {
            this.fCore.characters(new XMLString(buf, offset, len), null);
        } catch (WrappedSAXException e2) {
            throw e2.exception;
        }
    }

    @Override // org.xml.sax.ContentHandler
    public void ignorableWhitespace(char[] buf, int offset, int len) throws SAXException, XNIException {
        try {
            this.fCore.ignorableWhitespace(new XMLString(buf, offset, len), null);
        } catch (WrappedSAXException e2) {
            throw e2.exception;
        }
    }

    @Override // org.xml.sax.ContentHandler
    public void startPrefixMapping(String prefix, String uri) {
        this.nsContext.pushContext();
        this.nsContext.declarePrefix(prefix, uri);
    }

    @Override // org.xml.sax.ContentHandler
    public void endPrefixMapping(String prefix) {
        this.nsContext.popContext();
    }

    @Override // org.xml.sax.ContentHandler
    public void processingInstruction(String target, String data) throws SAXException, XNIException {
        try {
            this.fCore.processingInstruction(symbolize(target), createXMLString(data), null);
        } catch (WrappedSAXException e2) {
            throw e2.exception;
        }
    }

    @Override // org.xml.sax.ContentHandler
    public void skippedEntity(String name) {
    }

    @Override // org.xml.sax.ContentHandler
    public void setDocumentLocator(Locator _loc) {
        this.locator = _loc;
    }

    private QName createQName(String uri, String local, String raw) {
        String prefix;
        int idx = raw.indexOf(58);
        if (local.length() == 0) {
            uri = "";
            if (idx < 0) {
                local = raw;
            } else {
                local = raw.substring(idx + 1);
            }
        }
        if (idx < 0) {
            prefix = null;
        } else {
            prefix = raw.substring(0, idx);
        }
        if (uri != null && uri.length() == 0) {
            uri = null;
        }
        return new QName(symbolize(prefix), symbolize(local), symbolize(raw), symbolize(uri));
    }

    private String symbolize(String s2) {
        if (s2 == null) {
            return null;
        }
        return this.symbolTable.addSymbol(s2);
    }

    private XMLString createXMLString(String str) {
        return new XMLString(str.toCharArray(), 0, str.length());
    }

    private XMLAttributes createAttributes(Attributes att) {
        this.xa.removeAllAttributes();
        int len = att.getLength();
        for (int i2 = 0; i2 < len; i2++) {
            this.xa.addAttribute(createQName(att.getURI(i2), att.getLocalName(i2), att.getQName(i2)), att.getType(i2), att.getValue(i2));
        }
        return this.xa;
    }
}
