package com.sun.org.apache.xml.internal.serializer;

import com.sun.org.apache.xml.internal.serializer.utils.Utils;
import java.io.IOException;
import java.io.Writer;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/serializer/ToTextStream.class */
public final class ToTextStream extends ToStream {
    @Override // com.sun.org.apache.xml.internal.serializer.SerializerBase
    protected void startDocumentInternal() throws SAXException {
        super.startDocumentInternal();
        this.m_needToCallStartDocument = false;
    }

    @Override // org.xml.sax.ContentHandler
    public void endDocument() throws SAXException {
        flushPending();
        flushWriter();
        if (this.m_tracer != null) {
            super.fireEndDoc();
        }
    }

    @Override // com.sun.org.apache.xml.internal.serializer.ToStream, org.xml.sax.ContentHandler
    public void startElement(String namespaceURI, String localName, String name, Attributes atts) throws SAXException {
        if (this.m_tracer != null) {
            super.fireStartElem(name);
            firePseudoAttributes();
        }
    }

    @Override // com.sun.org.apache.xml.internal.serializer.ToStream, org.xml.sax.ContentHandler
    public void endElement(String namespaceURI, String localName, String name) throws SAXException {
        if (this.m_tracer != null) {
            super.fireEndElem(name);
        }
    }

    @Override // com.sun.org.apache.xml.internal.serializer.ToStream, org.xml.sax.ContentHandler
    public void characters(char[] ch, int start, int length) throws SAXException {
        flushPending();
        try {
            if (inTemporaryOutputState()) {
                this.m_writer.write(ch, start, length);
            } else {
                writeNormalizedChars(ch, start, length, this.m_lineSepUse);
            }
            if (this.m_tracer != null) {
                super.fireCharEvent(ch, start, length);
            }
        } catch (IOException ioe) {
            throw new SAXException(ioe);
        }
    }

    @Override // com.sun.org.apache.xml.internal.serializer.ToStream
    public void charactersRaw(char[] ch, int start, int length) throws SAXException {
        try {
            writeNormalizedChars(ch, start, length, this.m_lineSepUse);
        } catch (IOException ioe) {
            throw new SAXException(ioe);
        }
    }

    void writeNormalizedChars(char[] ch, int start, int length, boolean useLineSep) throws SAXException, IOException {
        String encoding = getEncoding();
        Writer writer = this.m_writer;
        int end = start + length;
        int i2 = start;
        while (i2 < end) {
            char c2 = ch[i2];
            if ('\n' == c2 && useLineSep) {
                writer.write(this.m_lineSep, 0, this.m_lineSepLen);
            } else if (this.m_encodingInfo.isInEncoding(c2)) {
                writer.write(c2);
            } else if (Encodings.isHighUTF16Surrogate(c2) || Encodings.isLowUTF16Surrogate(c2)) {
                int codePoint = writeUTF16Surrogate(c2, ch, i2, end);
                if (codePoint >= 0) {
                    if (Encodings.isHighUTF16Surrogate(c2)) {
                        i2++;
                    }
                    if (codePoint > 0) {
                        String integralValue = Integer.toString(codePoint);
                        String msg = Utils.messages.createMessage("ER_ILLEGAL_CHARACTER", new Object[]{integralValue, encoding});
                        System.err.println(msg);
                    }
                }
            } else if (encoding != null) {
                writer.write(38);
                writer.write(35);
                writer.write(Integer.toString(c2));
                writer.write(59);
                String integralValue2 = Integer.toString(c2);
                String msg2 = Utils.messages.createMessage("ER_ILLEGAL_CHARACTER", new Object[]{integralValue2, encoding});
                System.err.println(msg2);
            } else {
                writer.write(c2);
            }
            i2++;
        }
    }

    @Override // com.sun.org.apache.xml.internal.serializer.ToStream
    public void cdata(char[] ch, int start, int length) throws SAXException {
        try {
            writeNormalizedChars(ch, start, length, this.m_lineSepUse);
            if (this.m_tracer != null) {
                super.fireCDATAEvent(ch, start, length);
            }
        } catch (IOException ioe) {
            throw new SAXException(ioe);
        }
    }

    @Override // com.sun.org.apache.xml.internal.serializer.ToStream, org.xml.sax.ContentHandler
    public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {
        try {
            writeNormalizedChars(ch, start, length, this.m_lineSepUse);
        } catch (IOException ioe) {
            throw new SAXException(ioe);
        }
    }

    @Override // org.xml.sax.ContentHandler
    public void processingInstruction(String target, String data) throws SAXException {
        flushPending();
        if (this.m_tracer != null) {
            super.fireEscapingEvent(target, data);
        }
    }

    @Override // com.sun.org.apache.xml.internal.serializer.SerializerBase, com.sun.org.apache.xml.internal.serializer.ExtendedLexicalHandler
    public void comment(String data) throws SAXException {
        int length = data.length();
        if (length > this.m_charsBuff.length) {
            this.m_charsBuff = new char[(length * 2) + 1];
        }
        data.getChars(0, length, this.m_charsBuff, 0);
        comment(this.m_charsBuff, 0, length);
    }

    @Override // com.sun.org.apache.xml.internal.serializer.ToStream, org.xml.sax.ext.LexicalHandler
    public void comment(char[] ch, int start, int length) throws SAXException {
        flushPending();
        if (this.m_tracer != null) {
            super.fireCommentEvent(ch, start, length);
        }
    }

    @Override // com.sun.org.apache.xml.internal.serializer.SerializerBase, com.sun.org.apache.xml.internal.serializer.ExtendedContentHandler
    public void entityReference(String name) throws SAXException {
        if (this.m_tracer != null) {
            super.fireEntityReference(name);
        }
    }

    @Override // com.sun.org.apache.xml.internal.serializer.SerializerBase, com.sun.org.apache.xml.internal.serializer.ExtendedContentHandler
    public void addAttribute(String uri, String localName, String rawName, String type, String value, boolean XSLAttribute) {
    }

    @Override // com.sun.org.apache.xml.internal.serializer.ToStream, org.xml.sax.ext.LexicalHandler
    public void endCDATA() throws SAXException {
    }

    @Override // com.sun.org.apache.xml.internal.serializer.ToStream, com.sun.org.apache.xml.internal.serializer.ExtendedContentHandler
    public void endElement(String elemName) throws SAXException {
        if (this.m_tracer != null) {
            super.fireEndElem(elemName);
        }
    }

    @Override // com.sun.org.apache.xml.internal.serializer.ToStream, com.sun.org.apache.xml.internal.serializer.ExtendedContentHandler
    public void startElement(String elementNamespaceURI, String elementLocalName, String elementName) throws SAXException {
        if (this.m_needToCallStartDocument) {
            startDocumentInternal();
        }
        if (this.m_tracer != null) {
            super.fireStartElem(elementName);
            firePseudoAttributes();
        }
    }

    @Override // com.sun.org.apache.xml.internal.serializer.ToStream, com.sun.org.apache.xml.internal.serializer.ExtendedContentHandler
    public void characters(String characters) throws SAXException {
        int length = characters.length();
        if (length > this.m_charsBuff.length) {
            this.m_charsBuff = new char[(length * 2) + 1];
        }
        characters.getChars(0, length, this.m_charsBuff, 0);
        characters(this.m_charsBuff, 0, length);
    }

    @Override // com.sun.org.apache.xml.internal.serializer.SerializerBase, com.sun.org.apache.xml.internal.serializer.ExtendedContentHandler
    public void addAttribute(String name, String value) {
    }

    @Override // com.sun.org.apache.xml.internal.serializer.ExtendedContentHandler
    public void addUniqueAttribute(String qName, String value, int flags) throws SAXException {
    }

    @Override // com.sun.org.apache.xml.internal.serializer.ToStream, com.sun.org.apache.xml.internal.serializer.ExtendedContentHandler
    public boolean startPrefixMapping(String prefix, String uri, boolean shouldFlush) throws SAXException {
        return false;
    }

    @Override // com.sun.org.apache.xml.internal.serializer.ToStream, org.xml.sax.ContentHandler
    public void startPrefixMapping(String prefix, String uri) throws SAXException {
    }

    @Override // com.sun.org.apache.xml.internal.serializer.SerializerBase, com.sun.org.apache.xml.internal.serializer.ExtendedContentHandler
    public void namespaceAfterStartElement(String prefix, String uri) throws SAXException {
    }

    @Override // com.sun.org.apache.xml.internal.serializer.ToStream, com.sun.org.apache.xml.internal.serializer.SerializationHandler
    public void flushPending() throws SAXException {
        if (this.m_needToCallStartDocument) {
            startDocumentInternal();
            this.m_needToCallStartDocument = false;
        }
    }
}
