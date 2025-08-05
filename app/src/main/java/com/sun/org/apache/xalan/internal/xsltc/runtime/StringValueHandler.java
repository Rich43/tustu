package com.sun.org.apache.xalan.internal.xsltc.runtime;

import com.sun.org.apache.xml.internal.serializer.EmptySerializer;
import org.xml.sax.SAXException;

/* loaded from: rt.jar:com/sun/org/apache/xalan/internal/xsltc/runtime/StringValueHandler.class */
public final class StringValueHandler extends EmptySerializer {
    private static final String EMPTY_STR = "";
    private StringBuilder _buffer = new StringBuilder();
    private String _str = null;
    private boolean m_escaping = false;
    private int _nestedLevel = 0;

    @Override // com.sun.org.apache.xml.internal.serializer.EmptySerializer, org.xml.sax.ContentHandler
    public void characters(char[] ch, int off, int len) throws SAXException {
        if (this._nestedLevel > 0) {
            return;
        }
        if (this._str != null) {
            this._buffer.append(this._str);
            this._str = null;
        }
        this._buffer.append(ch, off, len);
    }

    public String getValue() {
        if (this._buffer.length() != 0) {
            String result = this._buffer.toString();
            this._buffer.setLength(0);
            return result;
        }
        String result2 = this._str;
        this._str = null;
        return result2 != null ? result2 : "";
    }

    @Override // com.sun.org.apache.xml.internal.serializer.EmptySerializer, com.sun.org.apache.xml.internal.serializer.ExtendedContentHandler
    public void characters(String characters) throws SAXException {
        if (this._nestedLevel > 0) {
            return;
        }
        if (this._str == null && this._buffer.length() == 0) {
            this._str = characters;
            return;
        }
        if (this._str != null) {
            this._buffer.append(this._str);
            this._str = null;
        }
        this._buffer.append(characters);
    }

    @Override // com.sun.org.apache.xml.internal.serializer.EmptySerializer, com.sun.org.apache.xml.internal.serializer.ExtendedContentHandler
    public void startElement(String qname) throws SAXException {
        this._nestedLevel++;
    }

    @Override // com.sun.org.apache.xml.internal.serializer.EmptySerializer, com.sun.org.apache.xml.internal.serializer.ExtendedContentHandler
    public void endElement(String qname) throws SAXException {
        this._nestedLevel--;
    }

    @Override // com.sun.org.apache.xml.internal.serializer.EmptySerializer, com.sun.org.apache.xml.internal.serializer.SerializationHandler
    public boolean setEscaping(boolean bool) {
        boolean z2 = this.m_escaping;
        this.m_escaping = bool;
        return bool;
    }

    public String getValueOfPI() {
        String value = getValue();
        if (value.indexOf("?>") > 0) {
            int n2 = value.length();
            StringBuilder valueOfPI = new StringBuilder();
            int i2 = 0;
            while (i2 < n2) {
                int i3 = i2;
                i2++;
                char ch = value.charAt(i3);
                if (ch == '?' && value.charAt(i2) == '>') {
                    valueOfPI.append("? >");
                    i2++;
                } else {
                    valueOfPI.append(ch);
                }
            }
            return valueOfPI.toString();
        }
        return value;
    }
}
