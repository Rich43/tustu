package com.sun.xml.internal.bind.marshaller;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;
import org.xml.sax.helpers.XMLFilterImpl;

/* loaded from: rt.jar:com/sun/xml/internal/bind/marshaller/XMLWriter.class */
public class XMLWriter extends XMLFilterImpl {
    private final HashMap<String, String> locallyDeclaredPrefix;
    private final Attributes EMPTY_ATTS;
    private int elementLevel;
    private Writer output;
    private String encoding;
    private boolean writeXmlDecl;
    private String header;
    private final CharacterEscapeHandler escapeHandler;
    private boolean startTagIsClosed;

    public XMLWriter(Writer writer, String encoding, CharacterEscapeHandler _escapeHandler) {
        this.locallyDeclaredPrefix = new HashMap<>();
        this.EMPTY_ATTS = new AttributesImpl();
        this.elementLevel = 0;
        this.writeXmlDecl = true;
        this.header = null;
        this.startTagIsClosed = true;
        init(writer, encoding);
        this.escapeHandler = _escapeHandler;
    }

    public XMLWriter(Writer writer, String encoding) {
        this(writer, encoding, DumbEscapeHandler.theInstance);
    }

    private void init(Writer writer, String encoding) {
        setOutput(writer, encoding);
    }

    public void reset() {
        this.elementLevel = 0;
        this.startTagIsClosed = true;
    }

    public void flush() throws IOException {
        this.output.flush();
    }

    public void setOutput(Writer writer, String _encoding) {
        if (writer == null) {
            this.output = new OutputStreamWriter(System.out);
        } else {
            this.output = writer;
        }
        this.encoding = _encoding;
    }

    public void setXmlDecl(boolean _writeXmlDecl) {
        this.writeXmlDecl = _writeXmlDecl;
    }

    public void setHeader(String _header) {
        this.header = _header;
    }

    @Override // org.xml.sax.helpers.XMLFilterImpl, org.xml.sax.ContentHandler
    public void startPrefixMapping(String prefix, String uri) throws SAXException {
        this.locallyDeclaredPrefix.put(prefix, uri);
    }

    @Override // org.xml.sax.helpers.XMLFilterImpl, org.xml.sax.ContentHandler
    public void startDocument() throws SAXException {
        try {
            reset();
            if (this.writeXmlDecl) {
                String e2 = "";
                if (this.encoding != null) {
                    e2 = " encoding=\"" + this.encoding + '\"';
                }
                writeXmlDecl("<?xml version=\"1.0\"" + e2 + " standalone=\"yes\"?>");
            }
            if (this.header != null) {
                write(this.header);
            }
            super.startDocument();
        } catch (IOException e3) {
            throw new SAXException(e3);
        }
    }

    protected void writeXmlDecl(String decl) throws IOException {
        write(decl);
    }

    @Override // org.xml.sax.helpers.XMLFilterImpl, org.xml.sax.ContentHandler
    public void endDocument() throws SAXException {
        try {
            super.endDocument();
            flush();
        } catch (IOException e2) {
            throw new SAXException(e2);
        }
    }

    @Override // org.xml.sax.helpers.XMLFilterImpl, org.xml.sax.ContentHandler
    public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
        try {
            if (!this.startTagIsClosed) {
                write(">");
            }
            this.elementLevel++;
            write('<');
            write(qName);
            writeAttributes(atts);
            if (!this.locallyDeclaredPrefix.isEmpty()) {
                for (Map.Entry<String, String> e2 : this.locallyDeclaredPrefix.entrySet()) {
                    String p2 = e2.getKey();
                    String u2 = e2.getValue();
                    if (u2 == null) {
                        u2 = "";
                    }
                    write(' ');
                    if ("".equals(p2)) {
                        write("xmlns=\"");
                    } else {
                        write("xmlns:");
                        write(p2);
                        write("=\"");
                    }
                    char[] ch = u2.toCharArray();
                    writeEsc(ch, 0, ch.length, true);
                    write('\"');
                }
                this.locallyDeclaredPrefix.clear();
            }
            super.startElement(uri, localName, qName, atts);
            this.startTagIsClosed = false;
        } catch (IOException e3) {
            throw new SAXException(e3);
        }
    }

    @Override // org.xml.sax.helpers.XMLFilterImpl, org.xml.sax.ContentHandler
    public void endElement(String uri, String localName, String qName) throws SAXException {
        try {
            if (this.startTagIsClosed) {
                write("</");
                write(qName);
                write('>');
            } else {
                write("/>");
                this.startTagIsClosed = true;
            }
            super.endElement(uri, localName, qName);
            this.elementLevel--;
        } catch (IOException e2) {
            throw new SAXException(e2);
        }
    }

    @Override // org.xml.sax.helpers.XMLFilterImpl, org.xml.sax.ContentHandler
    public void characters(char[] ch, int start, int len) throws SAXException {
        try {
            if (!this.startTagIsClosed) {
                write('>');
                this.startTagIsClosed = true;
            }
            writeEsc(ch, start, len, false);
            super.characters(ch, start, len);
        } catch (IOException e2) {
            throw new SAXException(e2);
        }
    }

    @Override // org.xml.sax.helpers.XMLFilterImpl, org.xml.sax.ContentHandler
    public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {
        try {
            writeEsc(ch, start, length, false);
            super.ignorableWhitespace(ch, start, length);
        } catch (IOException e2) {
            throw new SAXException(e2);
        }
    }

    @Override // org.xml.sax.helpers.XMLFilterImpl, org.xml.sax.ContentHandler
    public void processingInstruction(String target, String data) throws SAXException {
        try {
            if (!this.startTagIsClosed) {
                write('>');
                this.startTagIsClosed = true;
            }
            write("<?");
            write(target);
            write(' ');
            write(data);
            write("?>");
            if (this.elementLevel < 1) {
                write('\n');
            }
            super.processingInstruction(target, data);
        } catch (IOException e2) {
            throw new SAXException(e2);
        }
    }

    public void startElement(String uri, String localName) throws SAXException {
        startElement(uri, localName, "", this.EMPTY_ATTS);
    }

    public void startElement(String localName) throws SAXException {
        startElement("", localName, "", this.EMPTY_ATTS);
    }

    public void endElement(String uri, String localName) throws SAXException {
        endElement(uri, localName, "");
    }

    public void endElement(String localName) throws SAXException {
        endElement("", localName, "");
    }

    public void dataElement(String uri, String localName, String qName, Attributes atts, String content) throws SAXException {
        startElement(uri, localName, qName, atts);
        characters(content);
        endElement(uri, localName, qName);
    }

    public void dataElement(String uri, String localName, String content) throws SAXException {
        dataElement(uri, localName, "", this.EMPTY_ATTS, content);
    }

    public void dataElement(String localName, String content) throws SAXException {
        dataElement("", localName, "", this.EMPTY_ATTS, content);
    }

    public void characters(String data) throws SAXException {
        try {
            if (!this.startTagIsClosed) {
                write('>');
                this.startTagIsClosed = true;
            }
            char[] ch = data.toCharArray();
            characters(ch, 0, ch.length);
        } catch (IOException e2) {
            throw new SAXException(e2);
        }
    }

    protected final void write(char c2) throws IOException {
        this.output.write(c2);
    }

    protected final void write(String s2) throws IOException {
        this.output.write(s2);
    }

    private void writeAttributes(Attributes atts) throws IOException {
        int len = atts.getLength();
        for (int i2 = 0; i2 < len; i2++) {
            char[] ch = atts.getValue(i2).toCharArray();
            write(' ');
            write(atts.getQName(i2));
            write("=\"");
            writeEsc(ch, 0, ch.length, true);
            write('\"');
        }
    }

    private void writeEsc(char[] ch, int start, int length, boolean isAttVal) throws IOException {
        this.escapeHandler.escape(ch, start, length, isAttVal, this.output);
    }
}
