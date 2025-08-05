package com.sun.org.apache.xml.internal.serializer;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.util.Properties;
import javax.xml.transform.Result;
import org.w3c.dom.Node;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.ext.LexicalHandler;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/serializer/ToHTMLSAXHandler.class */
public final class ToHTMLSAXHandler extends ToSAXHandler {
    private boolean m_dtdHandled;
    protected boolean m_escapeSetting;

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

    @Override // com.sun.org.apache.xml.internal.serializer.SerializationHandler, com.sun.org.apache.xml.internal.serializer.DOMSerializer
    public void serialize(Node node) throws IOException {
    }

    @Override // com.sun.org.apache.xml.internal.serializer.SerializationHandler
    public boolean setEscaping(boolean escape) throws SAXException {
        boolean oldEscapeSetting = this.m_escapeSetting;
        this.m_escapeSetting = escape;
        if (escape) {
            processingInstruction(Result.PI_ENABLE_OUTPUT_ESCAPING, "");
        } else {
            processingInstruction(Result.PI_DISABLE_OUTPUT_ESCAPING, "");
        }
        return oldEscapeSetting;
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

    @Override // org.xml.sax.ext.DeclHandler
    public void attributeDecl(String eName, String aName, String type, String valueDefault, String value) throws SAXException {
    }

    @Override // org.xml.sax.ext.DeclHandler
    public void elementDecl(String name, String model) throws SAXException {
    }

    @Override // org.xml.sax.ext.DeclHandler
    public void externalEntityDecl(String arg0, String arg1, String arg2) throws SAXException {
    }

    @Override // org.xml.sax.ext.DeclHandler
    public void internalEntityDecl(String name, String value) throws SAXException {
    }

    @Override // org.xml.sax.ContentHandler
    public void endElement(String uri, String localName, String qName) throws SAXException {
        flushPending();
        this.m_saxHandler.endElement(uri, localName, qName);
        if (this.m_tracer != null) {
            super.fireEndElem(qName);
        }
    }

    @Override // org.xml.sax.ContentHandler
    public void endPrefixMapping(String prefix) throws SAXException {
    }

    @Override // org.xml.sax.ContentHandler
    public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {
    }

    @Override // com.sun.org.apache.xml.internal.serializer.ToSAXHandler, org.xml.sax.ContentHandler
    public void processingInstruction(String target, String data) throws SAXException {
        flushPending();
        this.m_saxHandler.processingInstruction(target, data);
        if (this.m_tracer != null) {
            super.fireEscapingEvent(target, data);
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
    public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {
        flushPending();
        super.startElement(namespaceURI, localName, qName, atts);
        this.m_saxHandler.startElement(namespaceURI, localName, qName, atts);
        this.m_elemContext.m_startTagOpen = false;
    }

    @Override // org.xml.sax.ext.LexicalHandler
    public void comment(char[] ch, int start, int length) throws SAXException {
        flushPending();
        if (this.m_lexHandler != null) {
            this.m_lexHandler.comment(ch, start, length);
        }
        if (this.m_tracer != null) {
            super.fireCommentEvent(ch, start, length);
        }
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

    @Override // org.xml.sax.ContentHandler
    public void endDocument() throws SAXException {
        flushPending();
        this.m_saxHandler.endDocument();
        if (this.m_tracer != null) {
            super.fireEndDoc();
        }
    }

    @Override // com.sun.org.apache.xml.internal.serializer.ToSAXHandler
    protected void closeStartTag() throws SAXException {
        this.m_elemContext.m_startTagOpen = false;
        this.m_saxHandler.startElement("", this.m_elemContext.m_elementName, this.m_elemContext.m_elementName, this.m_attributes);
        this.m_attributes.clear();
    }

    @Override // com.sun.org.apache.xml.internal.serializer.SerializerBase, com.sun.org.apache.xml.internal.serializer.SerializationHandler
    public void close() {
    }

    @Override // com.sun.org.apache.xml.internal.serializer.ToSAXHandler, com.sun.org.apache.xml.internal.serializer.ExtendedContentHandler
    public void characters(String chars) throws SAXException {
        int length = chars.length();
        if (length > this.m_charsBuff.length) {
            this.m_charsBuff = new char[(length * 2) + 1];
        }
        chars.getChars(0, length, this.m_charsBuff, 0);
        characters(this.m_charsBuff, 0, length);
    }

    public ToHTMLSAXHandler(ContentHandler handler, String encoding) {
        super(handler, encoding);
        this.m_dtdHandled = false;
        this.m_escapeSetting = true;
    }

    public ToHTMLSAXHandler(ContentHandler handler, LexicalHandler lex, String encoding) {
        super(handler, lex, encoding);
        this.m_dtdHandled = false;
        this.m_escapeSetting = true;
    }

    @Override // com.sun.org.apache.xml.internal.serializer.ToSAXHandler, com.sun.org.apache.xml.internal.serializer.ExtendedContentHandler
    public void startElement(String elementNamespaceURI, String elementLocalName, String elementName) throws SAXException {
        super.startElement(elementNamespaceURI, elementLocalName, elementName);
        flushPending();
        if (!this.m_dtdHandled) {
            String doctypeSystem = getDoctypeSystem();
            String doctypePublic = getDoctypePublic();
            if ((doctypeSystem != null || doctypePublic != null) && this.m_lexHandler != null) {
                this.m_lexHandler.startDTD(elementName, doctypePublic, doctypeSystem);
            }
            this.m_dtdHandled = true;
        }
        this.m_elemContext = this.m_elemContext.push(elementNamespaceURI, elementLocalName, elementName);
    }

    @Override // com.sun.org.apache.xml.internal.serializer.ToSAXHandler, com.sun.org.apache.xml.internal.serializer.ExtendedContentHandler
    public void startElement(String elementName) throws SAXException {
        startElement(null, null, elementName);
    }

    @Override // com.sun.org.apache.xml.internal.serializer.ExtendedContentHandler
    public void endElement(String elementName) throws SAXException {
        flushPending();
        this.m_saxHandler.endElement("", elementName, elementName);
        if (this.m_tracer != null) {
            super.fireEndElem(elementName);
        }
    }

    @Override // org.xml.sax.ContentHandler
    public void characters(char[] ch, int off, int len) throws SAXException {
        flushPending();
        this.m_saxHandler.characters(ch, off, len);
        if (this.m_tracer != null) {
            super.fireCharEvent(ch, off, len);
        }
    }

    @Override // com.sun.org.apache.xml.internal.serializer.ToSAXHandler, com.sun.org.apache.xml.internal.serializer.SerializationHandler
    public void flushPending() throws SAXException {
        if (this.m_needToCallStartDocument) {
            startDocumentInternal();
            this.m_needToCallStartDocument = false;
        }
        if (this.m_elemContext.m_startTagOpen) {
            closeStartTag();
            this.m_elemContext.m_startTagOpen = false;
        }
    }

    @Override // com.sun.org.apache.xml.internal.serializer.ExtendedContentHandler
    public boolean startPrefixMapping(String prefix, String uri, boolean shouldFlush) throws SAXException {
        if (shouldFlush) {
            flushPending();
        }
        this.m_saxHandler.startPrefixMapping(prefix, uri);
        return false;
    }

    @Override // org.xml.sax.ContentHandler
    public void startPrefixMapping(String prefix, String uri) throws SAXException {
        startPrefixMapping(prefix, uri, true);
    }

    @Override // com.sun.org.apache.xml.internal.serializer.SerializerBase, com.sun.org.apache.xml.internal.serializer.ExtendedContentHandler
    public void namespaceAfterStartElement(String prefix, String uri) throws SAXException {
        if (this.m_elemContext.m_elementURI == null) {
            String prefix1 = getPrefixPart(this.m_elemContext.m_elementName);
            if (prefix1 == null && "".equals(prefix)) {
                this.m_elemContext.m_elementURI = uri;
            }
        }
        startPrefixMapping(prefix, uri, false);
    }

    @Override // com.sun.org.apache.xml.internal.serializer.ToSAXHandler, com.sun.org.apache.xml.internal.serializer.SerializerBase, com.sun.org.apache.xml.internal.serializer.Serializer
    public boolean reset() {
        boolean wasReset = false;
        if (super.reset()) {
            resetToHTMLSAXHandler();
            wasReset = true;
        }
        return wasReset;
    }

    private void resetToHTMLSAXHandler() {
        this.m_escapeSetting = true;
    }
}
