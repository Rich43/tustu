package com.sun.xml.internal.txw2.output;

import java.util.Stack;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.ext.LexicalHandler;
import org.xml.sax.helpers.XMLFilterImpl;

/* loaded from: rt.jar:com/sun/xml/internal/txw2/output/IndentingXMLFilter.class */
public class IndentingXMLFilter extends XMLFilterImpl implements LexicalHandler {
    private LexicalHandler lexical;
    private static final char[] NEWLINE = {'\n'};
    private static final Object SEEN_NOTHING = new Object();
    private static final Object SEEN_ELEMENT = new Object();
    private static final Object SEEN_DATA = new Object();
    private Object state = SEEN_NOTHING;
    private Stack<Object> stateStack = new Stack<>();
    private String indentStep = "";
    private int depth = 0;

    public IndentingXMLFilter() {
    }

    public IndentingXMLFilter(ContentHandler handler) {
        setContentHandler(handler);
    }

    public IndentingXMLFilter(ContentHandler handler, LexicalHandler lexical) {
        setContentHandler(handler);
        setLexicalHandler(lexical);
    }

    public LexicalHandler getLexicalHandler() {
        return this.lexical;
    }

    public void setLexicalHandler(LexicalHandler lexical) {
        this.lexical = lexical;
    }

    public int getIndentStep() {
        return this.indentStep.length();
    }

    public void setIndentStep(int indentStep) {
        StringBuilder s2 = new StringBuilder();
        while (indentStep > 0) {
            s2.append(' ');
            indentStep--;
        }
        setIndentStep(s2.toString());
    }

    public void setIndentStep(String s2) {
        this.indentStep = s2;
    }

    @Override // org.xml.sax.helpers.XMLFilterImpl, org.xml.sax.ContentHandler
    public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
        this.stateStack.push(SEEN_ELEMENT);
        this.state = SEEN_NOTHING;
        if (this.depth > 0) {
            writeNewLine();
        }
        doIndent();
        super.startElement(uri, localName, qName, atts);
        this.depth++;
    }

    private void writeNewLine() throws SAXException {
        super.characters(NEWLINE, 0, NEWLINE.length);
    }

    @Override // org.xml.sax.helpers.XMLFilterImpl, org.xml.sax.ContentHandler
    public void endElement(String uri, String localName, String qName) throws SAXException {
        this.depth--;
        if (this.state == SEEN_ELEMENT) {
            writeNewLine();
            doIndent();
        }
        super.endElement(uri, localName, qName);
        this.state = this.stateStack.pop();
    }

    @Override // org.xml.sax.helpers.XMLFilterImpl, org.xml.sax.ContentHandler
    public void characters(char[] ch, int start, int length) throws SAXException {
        this.state = SEEN_DATA;
        super.characters(ch, start, length);
    }

    @Override // org.xml.sax.ext.LexicalHandler
    public void comment(char[] ch, int start, int length) throws SAXException {
        if (this.depth > 0) {
            writeNewLine();
        }
        doIndent();
        if (this.lexical != null) {
            this.lexical.comment(ch, start, length);
        }
    }

    @Override // org.xml.sax.ext.LexicalHandler
    public void startDTD(String name, String publicId, String systemId) throws SAXException {
        if (this.lexical != null) {
            this.lexical.startDTD(name, publicId, systemId);
        }
    }

    @Override // org.xml.sax.ext.LexicalHandler
    public void endDTD() throws SAXException {
        if (this.lexical != null) {
            this.lexical.endDTD();
        }
    }

    @Override // org.xml.sax.ext.LexicalHandler
    public void startEntity(String name) throws SAXException {
        if (this.lexical != null) {
            this.lexical.startEntity(name);
        }
    }

    @Override // org.xml.sax.ext.LexicalHandler
    public void endEntity(String name) throws SAXException {
        if (this.lexical != null) {
            this.lexical.endEntity(name);
        }
    }

    @Override // org.xml.sax.ext.LexicalHandler
    public void startCDATA() throws SAXException {
        if (this.lexical != null) {
            this.lexical.startCDATA();
        }
    }

    @Override // org.xml.sax.ext.LexicalHandler
    public void endCDATA() throws SAXException {
        if (this.lexical != null) {
            this.lexical.endCDATA();
        }
    }

    private void doIndent() throws SAXException {
        if (this.depth > 0) {
            char[] ch = this.indentStep.toCharArray();
            for (int i2 = 0; i2 < this.depth; i2++) {
                characters(ch, 0, ch.length);
            }
        }
    }
}
