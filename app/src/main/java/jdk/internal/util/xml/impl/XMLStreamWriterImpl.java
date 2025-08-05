package jdk.internal.util.xml.impl;

import com.sun.org.apache.xml.internal.serializer.SerializerConstants;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.nio.charset.UnsupportedCharsetException;
import jdk.internal.util.xml.XMLStreamException;
import jdk.internal.util.xml.XMLStreamWriter;

/* loaded from: rt.jar:jdk/internal/util/xml/impl/XMLStreamWriterImpl.class */
public class XMLStreamWriterImpl implements XMLStreamWriter {
    static final int STATE_XML_DECL = 1;
    static final int STATE_PROLOG = 2;
    static final int STATE_DTD_DECL = 3;
    static final int STATE_ELEMENT = 4;
    static final int ELEMENT_STARTTAG_OPEN = 10;
    static final int ELEMENT_STARTTAG_CLOSE = 11;
    static final int ELEMENT_ENDTAG_OPEN = 12;
    static final int ELEMENT_ENDTAG_CLOSE = 13;
    public static final char CLOSE_START_TAG = '>';
    public static final char OPEN_START_TAG = '<';
    public static final String OPEN_END_TAG = "</";
    public static final char CLOSE_END_TAG = '>';
    public static final String START_CDATA = "<![CDATA[";
    public static final String END_CDATA = "]]>";
    public static final String CLOSE_EMPTY_ELEMENT = "/>";
    public static final String ENCODING_PREFIX = "&#x";
    public static final char SPACE = ' ';
    public static final char AMPERSAND = '&';
    public static final char DOUBLEQUOT = '\"';
    public static final char SEMICOLON = ';';
    private int _state;
    private Element _currentEle;
    private XMLWriter _writer;
    private String _encoding;
    boolean _escapeCharacters;
    private boolean _doIndent;
    private char[] _lineSep;

    public XMLStreamWriterImpl(OutputStream outputStream) throws XMLStreamException {
        this(outputStream, "UTF-8");
    }

    public XMLStreamWriterImpl(OutputStream outputStream, String str) throws XMLStreamException {
        this._state = 0;
        this._escapeCharacters = true;
        this._doIndent = true;
        this._lineSep = System.getProperty("line.separator").toCharArray();
        Charset charset = null;
        if (str == null) {
            this._encoding = "UTF-8";
        } else {
            try {
                charset = getCharset(str);
                this._encoding = str;
            } catch (UnsupportedEncodingException e2) {
                throw new XMLStreamException(e2);
            }
        }
        this._writer = new XMLWriter(outputStream, str, charset);
    }

    @Override // jdk.internal.util.xml.XMLStreamWriter
    public void writeStartDocument() throws XMLStreamException {
        writeStartDocument(this._encoding, "1.0");
    }

    @Override // jdk.internal.util.xml.XMLStreamWriter
    public void writeStartDocument(String str) throws XMLStreamException {
        writeStartDocument(this._encoding, str, null);
    }

    @Override // jdk.internal.util.xml.XMLStreamWriter
    public void writeStartDocument(String str, String str2) throws XMLStreamException {
        writeStartDocument(str, str2, null);
    }

    public void writeStartDocument(String str, String str2, String str3) throws XMLStreamException {
        if (this._state > 0) {
            throw new XMLStreamException("XML declaration must be as the first line in the XML document.");
        }
        this._state = 1;
        String str4 = str;
        if (str4 == null) {
            str4 = this._encoding;
        } else {
            try {
                getCharset(str);
            } catch (UnsupportedEncodingException e2) {
                throw new XMLStreamException(e2);
            }
        }
        if (str2 == null) {
            str2 = "1.0";
        }
        this._writer.write("<?xml version=\"");
        this._writer.write(str2);
        this._writer.write(34);
        if (str4 != null) {
            this._writer.write(" encoding=\"");
            this._writer.write(str4);
            this._writer.write(34);
        }
        if (str3 != null) {
            this._writer.write(" standalone=\"");
            this._writer.write(str3);
            this._writer.write(34);
        }
        this._writer.write("?>");
        writeLineSeparator();
    }

    @Override // jdk.internal.util.xml.XMLStreamWriter
    public void writeDTD(String str) throws XMLStreamException {
        if (this._currentEle != null && this._currentEle.getState() == 10) {
            closeStartTag();
        }
        this._writer.write(str);
        writeLineSeparator();
    }

    @Override // jdk.internal.util.xml.XMLStreamWriter
    public void writeStartElement(String str) throws XMLStreamException {
        if (str == null || str.length() == 0) {
            throw new XMLStreamException("Local Name cannot be null or empty");
        }
        this._state = 4;
        if (this._currentEle != null && this._currentEle.getState() == 10) {
            closeStartTag();
        }
        this._currentEle = new Element(this._currentEle, str, false);
        openStartTag();
        this._writer.write(str);
    }

    @Override // jdk.internal.util.xml.XMLStreamWriter
    public void writeEmptyElement(String str) throws XMLStreamException {
        if (this._currentEle != null && this._currentEle.getState() == 10) {
            closeStartTag();
        }
        this._currentEle = new Element(this._currentEle, str, true);
        openStartTag();
        this._writer.write(str);
    }

    @Override // jdk.internal.util.xml.XMLStreamWriter
    public void writeAttribute(String str, String str2) throws XMLStreamException {
        if (this._currentEle.getState() != 10) {
            throw new XMLStreamException("Attribute not associated with any element");
        }
        this._writer.write(32);
        this._writer.write(str);
        this._writer.write("=\"");
        writeXMLContent(str2, true, true);
        this._writer.write(34);
    }

    @Override // jdk.internal.util.xml.XMLStreamWriter
    public void writeEndDocument() throws XMLStreamException {
        if (this._currentEle != null && this._currentEle.getState() == 10) {
            closeStartTag();
        }
        while (this._currentEle != null) {
            if (!this._currentEle.isEmpty()) {
                this._writer.write("</");
                this._writer.write(this._currentEle.getLocalName());
                this._writer.write(62);
            }
            this._currentEle = this._currentEle.getParent();
        }
    }

    @Override // jdk.internal.util.xml.XMLStreamWriter
    public void writeEndElement() throws XMLStreamException {
        if (this._currentEle != null && this._currentEle.getState() == 10) {
            closeStartTag();
        }
        if (this._currentEle == null) {
            throw new XMLStreamException("No element was found to write");
        }
        if (this._currentEle.isEmpty()) {
            return;
        }
        this._writer.write("</");
        this._writer.write(this._currentEle.getLocalName());
        this._writer.write(62);
        writeLineSeparator();
        this._currentEle = this._currentEle.getParent();
    }

    @Override // jdk.internal.util.xml.XMLStreamWriter
    public void writeCData(String str) throws XMLStreamException {
        if (str == null) {
            throw new XMLStreamException("cdata cannot be null");
        }
        if (this._currentEle != null && this._currentEle.getState() == 10) {
            closeStartTag();
        }
        this._writer.write("<![CDATA[");
        this._writer.write(str);
        this._writer.write("]]>");
    }

    @Override // jdk.internal.util.xml.XMLStreamWriter
    public void writeCharacters(String str) throws XMLStreamException {
        if (this._currentEle != null && this._currentEle.getState() == 10) {
            closeStartTag();
        }
        writeXMLContent(str);
    }

    @Override // jdk.internal.util.xml.XMLStreamWriter
    public void writeCharacters(char[] cArr, int i2, int i3) throws XMLStreamException {
        if (this._currentEle != null && this._currentEle.getState() == 10) {
            closeStartTag();
        }
        writeXMLContent(cArr, i2, i3, this._escapeCharacters);
    }

    @Override // jdk.internal.util.xml.XMLStreamWriter
    public void close() throws XMLStreamException {
        if (this._writer != null) {
            this._writer.close();
        }
        this._writer = null;
        this._currentEle = null;
        this._state = 0;
    }

    @Override // jdk.internal.util.xml.XMLStreamWriter
    public void flush() throws XMLStreamException {
        if (this._writer != null) {
            this._writer.flush();
        }
    }

    public void setDoIndent(boolean z2) {
        this._doIndent = z2;
    }

    private void writeXMLContent(char[] cArr, int i2, int i3, boolean z2) throws XMLStreamException {
        if (!z2) {
            this._writer.write(cArr, i2, i3);
            return;
        }
        int i4 = i2;
        int i5 = i2 + i3;
        for (int i6 = i2; i6 < i5; i6++) {
            char c2 = cArr[i6];
            if (!this._writer.canEncode(c2)) {
                this._writer.write(cArr, i4, i6 - i4);
                this._writer.write(ENCODING_PREFIX);
                this._writer.write(Integer.toHexString(c2));
                this._writer.write(59);
                i4 = i6 + 1;
            } else {
                switch (c2) {
                    case '&':
                        this._writer.write(cArr, i4, i6 - i4);
                        this._writer.write(SerializerConstants.ENTITY_AMP);
                        i4 = i6 + 1;
                        break;
                    case '<':
                        this._writer.write(cArr, i4, i6 - i4);
                        this._writer.write(SerializerConstants.ENTITY_LT);
                        i4 = i6 + 1;
                        break;
                    case '>':
                        this._writer.write(cArr, i4, i6 - i4);
                        this._writer.write(SerializerConstants.ENTITY_GT);
                        i4 = i6 + 1;
                        break;
                }
            }
        }
        this._writer.write(cArr, i4, i5 - i4);
    }

    private void writeXMLContent(String str) throws XMLStreamException {
        if (str != null && str.length() > 0) {
            writeXMLContent(str, this._escapeCharacters, false);
        }
    }

    private void writeXMLContent(String str, boolean z2, boolean z3) throws XMLStreamException {
        if (!z2) {
            this._writer.write(str);
            return;
        }
        int i2 = 0;
        int length = str.length();
        for (int i3 = 0; i3 < length; i3++) {
            char cCharAt = str.charAt(i3);
            if (!this._writer.canEncode(cCharAt)) {
                this._writer.write(str, i2, i3 - i2);
                this._writer.write(ENCODING_PREFIX);
                this._writer.write(Integer.toHexString(cCharAt));
                this._writer.write(59);
                i2 = i3 + 1;
            } else {
                switch (cCharAt) {
                    case '\"':
                        this._writer.write(str, i2, i3 - i2);
                        if (z3) {
                            this._writer.write(SerializerConstants.ENTITY_QUOT);
                        } else {
                            this._writer.write(34);
                        }
                        i2 = i3 + 1;
                        break;
                    case '&':
                        this._writer.write(str, i2, i3 - i2);
                        this._writer.write(SerializerConstants.ENTITY_AMP);
                        i2 = i3 + 1;
                        break;
                    case '<':
                        this._writer.write(str, i2, i3 - i2);
                        this._writer.write(SerializerConstants.ENTITY_LT);
                        i2 = i3 + 1;
                        break;
                    case '>':
                        this._writer.write(str, i2, i3 - i2);
                        this._writer.write(SerializerConstants.ENTITY_GT);
                        i2 = i3 + 1;
                        break;
                }
            }
        }
        this._writer.write(str, i2, length - i2);
    }

    private void openStartTag() throws XMLStreamException {
        this._currentEle.setState(10);
        this._writer.write(60);
    }

    private void closeStartTag() throws XMLStreamException {
        if (this._currentEle.isEmpty()) {
            this._writer.write("/>");
        } else {
            this._writer.write(62);
        }
        if (this._currentEle.getParent() == null) {
            writeLineSeparator();
        }
        this._currentEle.setState(11);
    }

    private void writeLineSeparator() throws XMLStreamException {
        if (this._doIndent) {
            this._writer.write(this._lineSep, 0, this._lineSep.length);
        }
    }

    private Charset getCharset(String str) throws UnsupportedEncodingException {
        if (str.equalsIgnoreCase("UTF-32")) {
            throw new UnsupportedEncodingException("The basic XMLWriter does not support " + str);
        }
        try {
            return Charset.forName(str);
        } catch (IllegalCharsetNameException | UnsupportedCharsetException e2) {
            throw new UnsupportedEncodingException(str);
        }
    }

    /* loaded from: rt.jar:jdk/internal/util/xml/impl/XMLStreamWriterImpl$Element.class */
    protected class Element {
        protected Element _parent;
        protected short _Depth;
        boolean _isEmptyElement;
        String _localpart;
        int _state;

        public Element() {
            this._isEmptyElement = false;
        }

        public Element(Element element, String str, boolean z2) {
            this._isEmptyElement = false;
            this._parent = element;
            this._localpart = str;
            this._isEmptyElement = z2;
        }

        public Element getParent() {
            return this._parent;
        }

        public String getLocalName() {
            return this._localpart;
        }

        public int getState() {
            return this._state;
        }

        public void setState(int i2) {
            this._state = i2;
        }

        public boolean isEmpty() {
            return this._isEmptyElement;
        }
    }
}
