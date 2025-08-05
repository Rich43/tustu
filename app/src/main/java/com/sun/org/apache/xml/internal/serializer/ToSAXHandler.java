package com.sun.org.apache.xml.internal.serializer;

import java.util.ArrayList;
import org.w3c.dom.DOMException;
import org.w3c.dom.Node;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.ext.LexicalHandler;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/serializer/ToSAXHandler.class */
public abstract class ToSAXHandler extends SerializerBase {
    protected ContentHandler m_saxHandler;
    protected LexicalHandler m_lexHandler;
    private boolean m_shouldGenerateNSAttribute = true;
    protected TransformStateSetter m_state = null;

    public ToSAXHandler() {
    }

    public ToSAXHandler(ContentHandler hdlr, LexicalHandler lex, String encoding) {
        setContentHandler(hdlr);
        setLexHandler(lex);
        setEncoding(encoding);
    }

    public ToSAXHandler(ContentHandler handler, String encoding) {
        setContentHandler(handler);
        setEncoding(encoding);
    }

    @Override // com.sun.org.apache.xml.internal.serializer.SerializerBase
    protected void startDocumentInternal() throws SAXException {
        if (this.m_needToCallStartDocument) {
            super.startDocumentInternal();
            this.m_saxHandler.startDocument();
            this.m_needToCallStartDocument = false;
        }
    }

    @Override // org.xml.sax.ext.LexicalHandler
    public void startDTD(String arg0, String arg1, String arg2) throws SAXException {
    }

    @Override // com.sun.org.apache.xml.internal.serializer.ExtendedContentHandler
    public void characters(String characters) throws SAXException {
        int len = characters.length();
        if (len > this.m_charsBuff.length) {
            this.m_charsBuff = new char[(len * 2) + 1];
        }
        characters.getChars(0, len, this.m_charsBuff, 0);
        characters(this.m_charsBuff, 0, len);
    }

    @Override // com.sun.org.apache.xml.internal.serializer.SerializerBase, com.sun.org.apache.xml.internal.serializer.ExtendedLexicalHandler
    public void comment(String comment) throws SAXException {
        flushPending();
        if (this.m_lexHandler != null) {
            int len = comment.length();
            if (len > this.m_charsBuff.length) {
                this.m_charsBuff = new char[(len * 2) + 1];
            }
            comment.getChars(0, len, this.m_charsBuff, 0);
            this.m_lexHandler.comment(this.m_charsBuff, 0, len);
            if (this.m_tracer != null) {
                super.fireCommentEvent(this.m_charsBuff, 0, len);
            }
        }
    }

    @Override // org.xml.sax.ContentHandler
    public void processingInstruction(String target, String data) throws SAXException {
    }

    protected void closeStartTag() throws SAXException {
    }

    protected void closeCDATA() throws SAXException {
    }

    @Override // org.xml.sax.ContentHandler
    public void startElement(String arg0, String arg1, String arg2, Attributes arg3) throws SAXException {
        if (this.m_state != null) {
            this.m_state.resetState(getTransformer());
        }
        if (this.m_tracer != null) {
            super.fireStartElem(arg2);
        }
    }

    public void setLexHandler(LexicalHandler _lexHandler) {
        this.m_lexHandler = _lexHandler;
    }

    @Override // com.sun.org.apache.xml.internal.serializer.SerializationHandler
    public void setContentHandler(ContentHandler _saxHandler) {
        this.m_saxHandler = _saxHandler;
        if (this.m_lexHandler == null && (_saxHandler instanceof LexicalHandler)) {
            this.m_lexHandler = (LexicalHandler) _saxHandler;
        }
    }

    @Override // com.sun.org.apache.xml.internal.serializer.XSLOutputAttributes
    public void setCdataSectionElements(ArrayList<String> URI_and_localNames) {
    }

    public void setShouldOutputNSAttr(boolean doOutputNSAttr) {
        this.m_shouldGenerateNSAttribute = doOutputNSAttr;
    }

    boolean getShouldOutputNSAttr() {
        return this.m_shouldGenerateNSAttribute;
    }

    @Override // com.sun.org.apache.xml.internal.serializer.SerializationHandler
    public void flushPending() throws SAXException {
        if (this.m_needToCallStartDocument) {
            startDocumentInternal();
            this.m_needToCallStartDocument = false;
        }
        if (this.m_elemContext.m_startTagOpen) {
            closeStartTag();
            this.m_elemContext.m_startTagOpen = false;
        }
        if (this.m_cdataTagOpen) {
            closeCDATA();
            this.m_cdataTagOpen = false;
        }
    }

    public void setTransformState(TransformStateSetter ts) {
        this.m_state = ts;
    }

    @Override // com.sun.org.apache.xml.internal.serializer.ExtendedContentHandler
    public void startElement(String uri, String localName, String qName) throws SAXException {
        if (this.m_state != null) {
            this.m_state.resetState(getTransformer());
        }
        if (this.m_tracer != null) {
            super.fireStartElem(qName);
        }
    }

    @Override // com.sun.org.apache.xml.internal.serializer.ExtendedContentHandler
    public void startElement(String qName) throws SAXException {
        if (this.m_state != null) {
            this.m_state.resetState(getTransformer());
        }
        if (this.m_tracer != null) {
            super.fireStartElem(qName);
        }
    }

    @Override // com.sun.org.apache.xml.internal.serializer.SerializerBase, com.sun.org.apache.xml.internal.serializer.ExtendedContentHandler
    public void characters(Node node) throws DOMException, SAXException {
        if (this.m_state != null) {
            this.m_state.setCurrentNode(node);
        }
        String data = node.getNodeValue();
        if (data != null) {
            characters(data);
        }
    }

    @Override // com.sun.org.apache.xml.internal.serializer.SerializerBase, org.xml.sax.ErrorHandler
    public void fatalError(SAXParseException exc) throws SAXException {
        super.fatalError(exc);
        this.m_needToCallStartDocument = false;
        if (this.m_saxHandler instanceof ErrorHandler) {
            ((ErrorHandler) this.m_saxHandler).fatalError(exc);
        }
    }

    @Override // com.sun.org.apache.xml.internal.serializer.SerializerBase, org.xml.sax.ErrorHandler
    public void error(SAXParseException exc) throws SAXException {
        super.error(exc);
        if (this.m_saxHandler instanceof ErrorHandler) {
            ((ErrorHandler) this.m_saxHandler).error(exc);
        }
    }

    @Override // com.sun.org.apache.xml.internal.serializer.SerializerBase, org.xml.sax.ErrorHandler
    public void warning(SAXParseException exc) throws SAXException {
        super.warning(exc);
        if (this.m_saxHandler instanceof ErrorHandler) {
            ((ErrorHandler) this.m_saxHandler).warning(exc);
        }
    }

    @Override // com.sun.org.apache.xml.internal.serializer.SerializerBase, com.sun.org.apache.xml.internal.serializer.Serializer
    public boolean reset() {
        boolean wasReset = false;
        if (super.reset()) {
            resetToSAXHandler();
            wasReset = true;
        }
        return wasReset;
    }

    private void resetToSAXHandler() {
        this.m_lexHandler = null;
        this.m_saxHandler = null;
        this.m_state = null;
        this.m_shouldGenerateNSAttribute = false;
    }

    @Override // com.sun.org.apache.xml.internal.serializer.ExtendedContentHandler
    public void addUniqueAttribute(String qName, String value, int flags) throws SAXException {
        addAttribute(qName, value);
    }
}
