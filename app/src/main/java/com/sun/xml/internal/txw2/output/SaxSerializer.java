package com.sun.xml.internal.txw2.output;

import com.sun.xml.internal.txw2.TxwException;
import java.util.Stack;
import javax.xml.transform.sax.SAXResult;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.ext.LexicalHandler;
import org.xml.sax.helpers.AttributesImpl;

/* loaded from: rt.jar:com/sun/xml/internal/txw2/output/SaxSerializer.class */
public class SaxSerializer implements XmlSerializer {
    private final ContentHandler writer;
    private final LexicalHandler lexical;
    private final Stack<String> prefixBindings;
    private final Stack<String> elementBindings;
    private final AttributesImpl attrs;

    public SaxSerializer(ContentHandler handler) {
        this(handler, null, true);
    }

    public SaxSerializer(ContentHandler handler, LexicalHandler lex) {
        this(handler, lex, true);
    }

    public SaxSerializer(ContentHandler handler, LexicalHandler lex, boolean indenting) {
        this.prefixBindings = new Stack<>();
        this.elementBindings = new Stack<>();
        this.attrs = new AttributesImpl();
        if (!indenting) {
            this.writer = handler;
            this.lexical = lex;
        } else {
            IndentingXMLFilter indenter = new IndentingXMLFilter(handler, lex);
            this.writer = indenter;
            this.lexical = indenter;
        }
    }

    public SaxSerializer(SAXResult result) {
        this(result.getHandler(), result.getLexicalHandler());
    }

    @Override // com.sun.xml.internal.txw2.output.XmlSerializer
    public void startDocument() {
        try {
            this.writer.startDocument();
        } catch (SAXException e2) {
            throw new TxwException(e2);
        }
    }

    @Override // com.sun.xml.internal.txw2.output.XmlSerializer
    public void writeXmlns(String prefix, String uri) {
        if (prefix == null) {
            prefix = "";
        }
        if (prefix.equals("xml")) {
            return;
        }
        this.prefixBindings.add(uri);
        this.prefixBindings.add(prefix);
    }

    @Override // com.sun.xml.internal.txw2.output.XmlSerializer
    public void beginStartTag(String uri, String localName, String prefix) {
        this.elementBindings.add(getQName(prefix, localName));
        this.elementBindings.add(localName);
        this.elementBindings.add(uri);
    }

    @Override // com.sun.xml.internal.txw2.output.XmlSerializer
    public void writeAttribute(String uri, String localName, String prefix, StringBuilder value) {
        this.attrs.addAttribute(uri, localName, getQName(prefix, localName), "CDATA", value.toString());
    }

    @Override // com.sun.xml.internal.txw2.output.XmlSerializer
    public void endStartTag(String uri, String localName, String prefix) {
        while (this.prefixBindings.size() != 0) {
            try {
                this.writer.startPrefixMapping(this.prefixBindings.pop(), this.prefixBindings.pop());
            } catch (SAXException e2) {
                throw new TxwException(e2);
            }
        }
        this.writer.startElement(uri, localName, getQName(prefix, localName), this.attrs);
        this.attrs.clear();
    }

    @Override // com.sun.xml.internal.txw2.output.XmlSerializer
    public void endTag() {
        try {
            this.writer.endElement(this.elementBindings.pop(), this.elementBindings.pop(), this.elementBindings.pop());
        } catch (SAXException e2) {
            throw new TxwException(e2);
        }
    }

    @Override // com.sun.xml.internal.txw2.output.XmlSerializer
    public void text(StringBuilder text) {
        try {
            this.writer.characters(text.toString().toCharArray(), 0, text.length());
        } catch (SAXException e2) {
            throw new TxwException(e2);
        }
    }

    @Override // com.sun.xml.internal.txw2.output.XmlSerializer
    public void cdata(StringBuilder text) {
        if (this.lexical == null) {
            throw new UnsupportedOperationException("LexicalHandler is needed to write PCDATA");
        }
        try {
            this.lexical.startCDATA();
            text(text);
            this.lexical.endCDATA();
        } catch (SAXException e2) {
            throw new TxwException(e2);
        }
    }

    @Override // com.sun.xml.internal.txw2.output.XmlSerializer
    public void comment(StringBuilder comment) {
        try {
            if (this.lexical == null) {
                throw new UnsupportedOperationException("LexicalHandler is needed to write comments");
            }
            this.lexical.comment(comment.toString().toCharArray(), 0, comment.length());
        } catch (SAXException e2) {
            throw new TxwException(e2);
        }
    }

    @Override // com.sun.xml.internal.txw2.output.XmlSerializer
    public void endDocument() {
        try {
            this.writer.endDocument();
        } catch (SAXException e2) {
            throw new TxwException(e2);
        }
    }

    @Override // com.sun.xml.internal.txw2.output.XmlSerializer
    public void flush() {
    }

    private static String getQName(String prefix, String localName) {
        String qName;
        if (prefix == null || prefix.length() == 0) {
            qName = localName;
        } else {
            qName = prefix + ':' + localName;
        }
        return qName;
    }
}
