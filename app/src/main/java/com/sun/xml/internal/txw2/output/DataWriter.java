package com.sun.xml.internal.txw2.output;

import java.io.Writer;
import java.util.Stack;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/* loaded from: rt.jar:com/sun/xml/internal/txw2/output/DataWriter.class */
public class DataWriter extends XMLWriter {
    private static final Object SEEN_NOTHING = new Object();
    private static final Object SEEN_ELEMENT = new Object();
    private static final Object SEEN_DATA = new Object();
    private Object state;
    private Stack stateStack;
    private String indentStep;
    private int depth;

    public DataWriter(Writer writer, String encoding, CharacterEscapeHandler _escapeHandler) {
        super(writer, encoding, _escapeHandler);
        this.state = SEEN_NOTHING;
        this.stateStack = new Stack();
        this.indentStep = "";
        this.depth = 0;
    }

    public DataWriter(Writer writer, String encoding) {
        this(writer, encoding, DumbEscapeHandler.theInstance);
    }

    public DataWriter(Writer writer) {
        this(writer, null, DumbEscapeHandler.theInstance);
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

    @Override // com.sun.xml.internal.txw2.output.XMLWriter
    public void reset() {
        this.depth = 0;
        this.state = SEEN_NOTHING;
        this.stateStack = new Stack();
        super.reset();
    }

    @Override // com.sun.xml.internal.txw2.output.XMLWriter, org.xml.sax.helpers.XMLFilterImpl, org.xml.sax.ContentHandler
    public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
        this.stateStack.push(SEEN_ELEMENT);
        this.state = SEEN_NOTHING;
        if (this.depth > 0) {
            super.characters("\n");
        }
        doIndent();
        super.startElement(uri, localName, qName, atts);
        this.depth++;
    }

    @Override // com.sun.xml.internal.txw2.output.XMLWriter, org.xml.sax.helpers.XMLFilterImpl, org.xml.sax.ContentHandler
    public void endElement(String uri, String localName, String qName) throws SAXException {
        this.depth--;
        if (this.state == SEEN_ELEMENT) {
            super.characters("\n");
            doIndent();
        }
        super.endElement(uri, localName, qName);
        this.state = this.stateStack.pop();
    }

    @Override // com.sun.xml.internal.txw2.output.XMLWriter, org.xml.sax.helpers.XMLFilterImpl, org.xml.sax.ContentHandler
    public void characters(char[] ch, int start, int length) throws SAXException {
        this.state = SEEN_DATA;
        super.characters(ch, start, length);
    }

    @Override // com.sun.xml.internal.txw2.output.XMLWriter, org.xml.sax.ext.LexicalHandler
    public void comment(char[] ch, int start, int length) throws SAXException {
        if (this.depth > 0) {
            super.characters("\n");
        }
        doIndent();
        super.comment(ch, start, length);
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
