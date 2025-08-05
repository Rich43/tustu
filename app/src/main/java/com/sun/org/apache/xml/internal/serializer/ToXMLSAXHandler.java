package com.sun.org.apache.xml.internal.serializer;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.util.Properties;
import javax.xml.transform.Result;
import jdk.internal.dynalink.CallSiteDescriptor;
import org.w3c.dom.Node;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.ext.LexicalHandler;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/serializer/ToXMLSAXHandler.class */
public final class ToXMLSAXHandler extends ToSAXHandler {
    protected boolean m_escapeSetting;

    public ToXMLSAXHandler() {
        this.m_escapeSetting = true;
        this.m_prefixMap = new NamespaceMappings();
        initCDATA();
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
        String localName = getLocalName(this.m_elemContext.m_elementName);
        String uri = getNamespaceURI(this.m_elemContext.m_elementName, true);
        if (this.m_needToCallStartDocument) {
            startDocumentInternal();
        }
        this.m_saxHandler.startElement(uri, localName, this.m_elemContext.m_elementName, this.m_attributes);
        this.m_attributes.clear();
        if (this.m_state != null) {
            this.m_state.setCurrentNode(null);
        }
    }

    @Override // com.sun.org.apache.xml.internal.serializer.ToSAXHandler
    public void closeCDATA() throws SAXException {
        if (this.m_lexHandler != null && this.m_cdataTagOpen) {
            this.m_lexHandler.endCDATA();
        }
        this.m_cdataTagOpen = false;
    }

    @Override // org.xml.sax.ContentHandler
    public void endElement(String namespaceURI, String localName, String qName) throws SAXException {
        flushPending();
        if (namespaceURI == null) {
            if (this.m_elemContext.m_elementURI != null) {
                namespaceURI = this.m_elemContext.m_elementURI;
            } else {
                namespaceURI = getNamespaceURI(qName, true);
            }
        }
        if (localName == null) {
            if (this.m_elemContext.m_elementLocalName != null) {
                localName = this.m_elemContext.m_elementLocalName;
            } else {
                localName = getLocalName(qName);
            }
        }
        this.m_saxHandler.endElement(namespaceURI, localName, qName);
        if (this.m_tracer != null) {
            super.fireEndElem(qName);
        }
        this.m_prefixMap.popNamespaces(this.m_elemContext.m_currentElemDepth, this.m_saxHandler);
        this.m_elemContext = this.m_elemContext.m_prev;
    }

    @Override // org.xml.sax.ContentHandler
    public void endPrefixMapping(String prefix) throws SAXException {
    }

    @Override // org.xml.sax.ContentHandler
    public void ignorableWhitespace(char[] arg0, int arg1, int arg2) throws SAXException {
        this.m_saxHandler.ignorableWhitespace(arg0, arg1, arg2);
    }

    @Override // com.sun.org.apache.xml.internal.serializer.SerializerBase, org.xml.sax.ContentHandler
    public void setDocumentLocator(Locator arg0) {
        super.setDocumentLocator(arg0);
        this.m_saxHandler.setDocumentLocator(arg0);
    }

    @Override // org.xml.sax.ContentHandler
    public void skippedEntity(String arg0) throws SAXException {
        this.m_saxHandler.skippedEntity(arg0);
    }

    @Override // org.xml.sax.ContentHandler
    public void startPrefixMapping(String prefix, String uri) throws SAXException {
        startPrefixMapping(prefix, uri, true);
    }

    @Override // com.sun.org.apache.xml.internal.serializer.ExtendedContentHandler
    public boolean startPrefixMapping(String prefix, String uri, boolean shouldFlush) throws SAXException {
        int pushDepth;
        if (shouldFlush) {
            flushPending();
            pushDepth = this.m_elemContext.m_currentElemDepth + 1;
        } else {
            pushDepth = this.m_elemContext.m_currentElemDepth;
        }
        boolean pushed = this.m_prefixMap.pushNamespace(prefix, uri, pushDepth);
        if (pushed) {
            this.m_saxHandler.startPrefixMapping(prefix, uri);
            if (getShouldOutputNSAttr()) {
                if ("".equals(prefix)) {
                    addAttributeAlways("http://www.w3.org/2000/xmlns/", "xmlns", "xmlns", "CDATA", uri, false);
                } else if (!"".equals(uri)) {
                    String name = "xmlns:" + prefix;
                    addAttributeAlways("http://www.w3.org/2000/xmlns/", prefix, name, "CDATA", uri, false);
                }
            }
        }
        return pushed;
    }

    @Override // org.xml.sax.ext.LexicalHandler
    public void comment(char[] arg0, int arg1, int arg2) throws SAXException {
        flushPending();
        if (this.m_lexHandler != null) {
            this.m_lexHandler.comment(arg0, arg1, arg2);
        }
        if (this.m_tracer != null) {
            super.fireCommentEvent(arg0, arg1, arg2);
        }
    }

    @Override // org.xml.sax.ext.LexicalHandler
    public void endCDATA() throws SAXException {
    }

    @Override // org.xml.sax.ext.LexicalHandler
    public void endDTD() throws SAXException {
        if (this.m_lexHandler != null) {
            this.m_lexHandler.endDTD();
        }
    }

    @Override // org.xml.sax.ext.LexicalHandler
    public void startEntity(String arg0) throws SAXException {
        if (this.m_lexHandler != null) {
            this.m_lexHandler.startEntity(arg0);
        }
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

    public ToXMLSAXHandler(ContentHandler handler, String encoding) {
        super(handler, encoding);
        this.m_escapeSetting = true;
        initCDATA();
        this.m_prefixMap = new NamespaceMappings();
    }

    public ToXMLSAXHandler(ContentHandler handler, LexicalHandler lex, String encoding) {
        super(handler, lex, encoding);
        this.m_escapeSetting = true;
        initCDATA();
        this.m_prefixMap = new NamespaceMappings();
    }

    @Override // com.sun.org.apache.xml.internal.serializer.ToSAXHandler, com.sun.org.apache.xml.internal.serializer.ExtendedContentHandler
    public void startElement(String elementNamespaceURI, String elementLocalName, String elementName) throws SAXException {
        startElement(elementNamespaceURI, elementLocalName, elementName, null);
    }

    @Override // com.sun.org.apache.xml.internal.serializer.ToSAXHandler, com.sun.org.apache.xml.internal.serializer.ExtendedContentHandler
    public void startElement(String elementName) throws SAXException {
        startElement(null, null, elementName, null);
    }

    @Override // org.xml.sax.ContentHandler
    public void characters(char[] ch, int off, int len) throws SAXException {
        if (this.m_needToCallStartDocument) {
            startDocumentInternal();
            this.m_needToCallStartDocument = false;
        }
        if (this.m_elemContext.m_startTagOpen) {
            closeStartTag();
            this.m_elemContext.m_startTagOpen = false;
        }
        if (this.m_elemContext.m_isCdataSection && !this.m_cdataTagOpen && this.m_lexHandler != null) {
            this.m_lexHandler.startCDATA();
            this.m_cdataTagOpen = true;
        }
        this.m_saxHandler.characters(ch, off, len);
        if (this.m_tracer != null) {
            fireCharEvent(ch, off, len);
        }
    }

    @Override // com.sun.org.apache.xml.internal.serializer.ExtendedContentHandler
    public void endElement(String elemName) throws SAXException {
        endElement(null, null, elemName);
    }

    @Override // com.sun.org.apache.xml.internal.serializer.SerializerBase, com.sun.org.apache.xml.internal.serializer.ExtendedContentHandler
    public void namespaceAfterStartElement(String prefix, String uri) throws SAXException {
        startPrefixMapping(prefix, uri, false);
    }

    @Override // com.sun.org.apache.xml.internal.serializer.ToSAXHandler, org.xml.sax.ContentHandler
    public void processingInstruction(String target, String data) throws SAXException {
        flushPending();
        this.m_saxHandler.processingInstruction(target, data);
        if (this.m_tracer != null) {
            super.fireEscapingEvent(target, data);
        }
    }

    protected boolean popNamespace(String prefix) {
        try {
            if (this.m_prefixMap.popNamespace(prefix)) {
                this.m_saxHandler.endPrefixMapping(prefix);
                return true;
            }
            return false;
        } catch (SAXException e2) {
            return false;
        }
    }

    @Override // org.xml.sax.ext.LexicalHandler
    public void startCDATA() throws SAXException {
        if (!this.m_cdataTagOpen) {
            flushPending();
            if (this.m_lexHandler != null) {
                this.m_lexHandler.startCDATA();
                this.m_cdataTagOpen = true;
            }
        }
    }

    @Override // com.sun.org.apache.xml.internal.serializer.ToSAXHandler, org.xml.sax.ContentHandler
    public void startElement(String namespaceURI, String localName, String name, Attributes atts) throws SAXException {
        flushPending();
        super.startElement(namespaceURI, localName, name, atts);
        if (this.m_needToOutputDocTypeDecl) {
            String doctypeSystem = getDoctypeSystem();
            if (doctypeSystem != null && this.m_lexHandler != null) {
                String doctypePublic = getDoctypePublic();
                if (doctypeSystem != null) {
                    this.m_lexHandler.startDTD(name, doctypePublic, doctypeSystem);
                }
            }
            this.m_needToOutputDocTypeDecl = false;
        }
        this.m_elemContext = this.m_elemContext.push(namespaceURI, localName, name);
        if (namespaceURI != null) {
            ensurePrefixIsDeclared(namespaceURI, name);
        }
        if (atts != null) {
            addAttributes(atts);
        }
        this.m_elemContext.m_isCdataSection = isCdataSection();
    }

    private void ensurePrefixIsDeclared(String ns, String rawName) throws SAXException {
        if (ns != null && ns.length() > 0) {
            int index = rawName.indexOf(CallSiteDescriptor.TOKEN_DELIMITER);
            boolean no_prefix = index < 0;
            String prefix = no_prefix ? "" : rawName.substring(0, index);
            if (null != prefix) {
                String foundURI = this.m_prefixMap.lookupNamespace(prefix);
                if (null == foundURI || !foundURI.equals(ns)) {
                    startPrefixMapping(prefix, ns, false);
                    if (getShouldOutputNSAttr()) {
                        addAttributeAlways("http://www.w3.org/2000/xmlns/", no_prefix ? "xmlns" : prefix, no_prefix ? "xmlns" : "xmlns:" + prefix, "CDATA", ns, false);
                    }
                }
            }
        }
    }

    @Override // com.sun.org.apache.xml.internal.serializer.SerializerBase, com.sun.org.apache.xml.internal.serializer.ExtendedContentHandler
    public void addAttribute(String uri, String localName, String rawName, String type, String value, boolean XSLAttribute) throws SAXException {
        if (this.m_elemContext.m_startTagOpen) {
            ensurePrefixIsDeclared(uri, rawName);
            addAttributeAlways(uri, localName, rawName, type, value, false);
        }
    }

    @Override // com.sun.org.apache.xml.internal.serializer.ToSAXHandler, com.sun.org.apache.xml.internal.serializer.SerializerBase, com.sun.org.apache.xml.internal.serializer.Serializer
    public boolean reset() {
        boolean wasReset = false;
        if (super.reset()) {
            resetToXMLSAXHandler();
            wasReset = true;
        }
        return wasReset;
    }

    private void resetToXMLSAXHandler() {
        this.m_escapeSetting = true;
    }
}
