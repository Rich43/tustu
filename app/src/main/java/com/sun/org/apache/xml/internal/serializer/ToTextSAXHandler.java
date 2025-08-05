package com.sun.org.apache.xml.internal.serializer;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.util.Properties;
import org.w3c.dom.Node;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.ext.LexicalHandler;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/serializer/ToTextSAXHandler.class */
public final class ToTextSAXHandler extends ToSAXHandler {
    @Override // com.sun.org.apache.xml.internal.serializer.ExtendedContentHandler
    public void endElement(String elemName) throws SAXException {
        if (this.m_tracer != null) {
            super.fireEndElem(elemName);
        }
    }

    @Override // org.xml.sax.ContentHandler
    public void endElement(String arg0, String arg1, String arg2) throws SAXException {
        if (this.m_tracer != null) {
            super.fireEndElem(arg2);
        }
    }

    public ToTextSAXHandler(ContentHandler hdlr, LexicalHandler lex, String encoding) {
        super(hdlr, lex, encoding);
    }

    public ToTextSAXHandler(ContentHandler handler, String encoding) {
        super(handler, encoding);
    }

    @Override // org.xml.sax.ext.LexicalHandler
    public void comment(char[] ch, int start, int length) throws SAXException {
        if (this.m_tracer != null) {
            super.fireCommentEvent(ch, start, length);
        }
    }

    @Override // com.sun.org.apache.xml.internal.serializer.ToSAXHandler, com.sun.org.apache.xml.internal.serializer.SerializerBase, com.sun.org.apache.xml.internal.serializer.ExtendedLexicalHandler
    public void comment(String data) throws SAXException {
        int length = data.length();
        if (length > this.m_charsBuff.length) {
            this.m_charsBuff = new char[(length * 2) + 1];
        }
        data.getChars(0, length, this.m_charsBuff, 0);
        comment(this.m_charsBuff, 0, length);
    }

    @Override // com.sun.org.apache.xml.internal.serializer.Serializer
    public Properties getOutputFormat() {
        return null;
    }

    @Override // com.sun.org.apache.xml.internal.serializer.Serializer
    public OutputStream getOutputStream() {
        return null;
    }

    @Override // com.sun.org.apache.xml.internal.serializer.Serializer
    public Writer getWriter() {
        return null;
    }

    public void indent(int n2) throws SAXException {
    }

    @Override // com.sun.org.apache.xml.internal.serializer.ToSAXHandler, com.sun.org.apache.xml.internal.serializer.SerializerBase, com.sun.org.apache.xml.internal.serializer.Serializer
    public boolean reset() {
        return false;
    }

    @Override // com.sun.org.apache.xml.internal.serializer.SerializationHandler, com.sun.org.apache.xml.internal.serializer.DOMSerializer
    public void serialize(Node node) throws IOException {
    }

    @Override // com.sun.org.apache.xml.internal.serializer.SerializationHandler
    public boolean setEscaping(boolean escape) {
        return false;
    }

    @Override // com.sun.org.apache.xml.internal.serializer.SerializerBase, com.sun.org.apache.xml.internal.serializer.XSLOutputAttributes
    public void setIndent(boolean indent) {
    }

    @Override // com.sun.org.apache.xml.internal.serializer.Serializer
    public void setOutputFormat(Properties format) {
    }

    @Override // com.sun.org.apache.xml.internal.serializer.Serializer
    public void setOutputStream(OutputStream output) {
    }

    @Override // com.sun.org.apache.xml.internal.serializer.Serializer
    public void setWriter(Writer writer) {
    }

    @Override // com.sun.org.apache.xml.internal.serializer.SerializerBase, com.sun.org.apache.xml.internal.serializer.ExtendedContentHandler
    public void addAttribute(String uri, String localName, String rawName, String type, String value, boolean XSLAttribute) {
    }

    @Override // org.xml.sax.ext.DeclHandler
    public void attributeDecl(String arg0, String arg1, String arg2, String arg3, String arg4) throws SAXException {
    }

    @Override // org.xml.sax.ext.DeclHandler
    public void elementDecl(String arg0, String arg1) throws SAXException {
    }

    @Override // org.xml.sax.ext.DeclHandler
    public void externalEntityDecl(String arg0, String arg1, String arg2) throws SAXException {
    }

    @Override // org.xml.sax.ext.DeclHandler
    public void internalEntityDecl(String arg0, String arg1) throws SAXException {
    }

    @Override // org.xml.sax.ContentHandler
    public void endPrefixMapping(String arg0) throws SAXException {
    }

    @Override // org.xml.sax.ContentHandler
    public void ignorableWhitespace(char[] arg0, int arg1, int arg2) throws SAXException {
    }

    @Override // com.sun.org.apache.xml.internal.serializer.ToSAXHandler, org.xml.sax.ContentHandler
    public void processingInstruction(String arg0, String arg1) throws SAXException {
        if (this.m_tracer != null) {
            super.fireEscapingEvent(arg0, arg1);
        }
    }

    @Override // com.sun.org.apache.xml.internal.serializer.SerializerBase, org.xml.sax.ContentHandler
    public void setDocumentLocator(Locator arg0) {
        super.setDocumentLocator(arg0);
    }

    @Override // org.xml.sax.ContentHandler
    public void skippedEntity(String arg0) throws SAXException {
    }

    @Override // com.sun.org.apache.xml.internal.serializer.ToSAXHandler, org.xml.sax.ContentHandler
    public void startElement(String arg0, String arg1, String arg2, Attributes arg3) throws SAXException {
        flushPending();
        super.startElement(arg0, arg1, arg2, arg3);
    }

    @Override // org.xml.sax.ext.LexicalHandler
    public void endCDATA() throws SAXException {
    }

    @Override // org.xml.sax.ext.LexicalHandler
    public void endDTD() throws SAXException {
    }

    @Override // org.xml.sax.ext.LexicalHandler
    public void startCDATA() throws SAXException {
    }

    @Override // org.xml.sax.ext.LexicalHandler
    public void startEntity(String arg0) throws SAXException {
    }

    @Override // com.sun.org.apache.xml.internal.serializer.ToSAXHandler, com.sun.org.apache.xml.internal.serializer.ExtendedContentHandler
    public void startElement(String elementNamespaceURI, String elementLocalName, String elementName) throws SAXException {
        super.startElement(elementNamespaceURI, elementLocalName, elementName);
    }

    @Override // com.sun.org.apache.xml.internal.serializer.ToSAXHandler, com.sun.org.apache.xml.internal.serializer.ExtendedContentHandler
    public void startElement(String elementName) throws SAXException {
        super.startElement(elementName);
    }

    @Override // org.xml.sax.ContentHandler
    public void endDocument() throws SAXException {
        flushPending();
        this.m_saxHandler.endDocument();
        if (this.m_tracer != null) {
            super.fireEndDoc();
        }
    }

    @Override // com.sun.org.apache.xml.internal.serializer.ToSAXHandler, com.sun.org.apache.xml.internal.serializer.ExtendedContentHandler
    public void characters(String characters) throws SAXException {
        int length = characters.length();
        if (length > this.m_charsBuff.length) {
            this.m_charsBuff = new char[(length * 2) + 1];
        }
        characters.getChars(0, length, this.m_charsBuff, 0);
        this.m_saxHandler.characters(this.m_charsBuff, 0, length);
    }

    @Override // org.xml.sax.ContentHandler
    public void characters(char[] characters, int offset, int length) throws SAXException {
        this.m_saxHandler.characters(characters, offset, length);
        if (this.m_tracer != null) {
            super.fireCharEvent(characters, offset, length);
        }
    }

    @Override // com.sun.org.apache.xml.internal.serializer.SerializerBase, com.sun.org.apache.xml.internal.serializer.ExtendedContentHandler
    public void addAttribute(String name, String value) {
    }

    @Override // com.sun.org.apache.xml.internal.serializer.ExtendedContentHandler
    public boolean startPrefixMapping(String prefix, String uri, boolean shouldFlush) throws SAXException {
        return false;
    }

    @Override // org.xml.sax.ContentHandler
    public void startPrefixMapping(String prefix, String uri) throws SAXException {
    }

    @Override // com.sun.org.apache.xml.internal.serializer.SerializerBase, com.sun.org.apache.xml.internal.serializer.ExtendedContentHandler
    public void namespaceAfterStartElement(String prefix, String uri) throws SAXException {
    }
}
