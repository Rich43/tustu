package com.sun.org.apache.xml.internal.utils;

import java.util.Locale;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.ext.LexicalHandler;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/utils/XMLStringDefault.class */
public class XMLStringDefault implements XMLString {
    private String m_str;

    public XMLStringDefault(String str) {
        this.m_str = str;
    }

    @Override // com.sun.org.apache.xml.internal.utils.XMLString
    public void dispatchCharactersEvents(ContentHandler ch) throws SAXException {
    }

    @Override // com.sun.org.apache.xml.internal.utils.XMLString
    public void dispatchAsComment(LexicalHandler lh) throws SAXException {
    }

    @Override // com.sun.org.apache.xml.internal.utils.XMLString
    public XMLString fixWhiteSpace(boolean trimHead, boolean trimTail, boolean doublePunctuationSpaces) {
        return new XMLStringDefault(this.m_str.trim());
    }

    @Override // com.sun.org.apache.xml.internal.utils.XMLString
    public int length() {
        return this.m_str.length();
    }

    @Override // com.sun.org.apache.xml.internal.utils.XMLString
    public char charAt(int index) {
        return this.m_str.charAt(index);
    }

    @Override // com.sun.org.apache.xml.internal.utils.XMLString
    public void getChars(int srcBegin, int srcEnd, char[] dst, int dstBegin) {
        int destIndex = dstBegin;
        for (int i2 = srcBegin; i2 < srcEnd; i2++) {
            int i3 = destIndex;
            destIndex++;
            dst[i3] = this.m_str.charAt(i2);
        }
    }

    @Override // com.sun.org.apache.xml.internal.utils.XMLString
    public boolean equals(String obj2) {
        return this.m_str.equals(obj2);
    }

    @Override // com.sun.org.apache.xml.internal.utils.XMLString
    public boolean equals(XMLString anObject) {
        return this.m_str.equals(anObject.toString());
    }

    @Override // com.sun.org.apache.xml.internal.utils.XMLString
    public boolean equals(Object anObject) {
        return this.m_str.equals(anObject);
    }

    @Override // com.sun.org.apache.xml.internal.utils.XMLString
    public boolean equalsIgnoreCase(String anotherString) {
        return this.m_str.equalsIgnoreCase(anotherString);
    }

    @Override // com.sun.org.apache.xml.internal.utils.XMLString
    public int compareTo(XMLString anotherString) {
        return this.m_str.compareTo(anotherString.toString());
    }

    @Override // com.sun.org.apache.xml.internal.utils.XMLString
    public int compareToIgnoreCase(XMLString str) {
        return this.m_str.compareToIgnoreCase(str.toString());
    }

    @Override // com.sun.org.apache.xml.internal.utils.XMLString
    public boolean startsWith(String prefix, int toffset) {
        return this.m_str.startsWith(prefix, toffset);
    }

    @Override // com.sun.org.apache.xml.internal.utils.XMLString
    public boolean startsWith(XMLString prefix, int toffset) {
        return this.m_str.startsWith(prefix.toString(), toffset);
    }

    @Override // com.sun.org.apache.xml.internal.utils.XMLString
    public boolean startsWith(String prefix) {
        return this.m_str.startsWith(prefix);
    }

    @Override // com.sun.org.apache.xml.internal.utils.XMLString
    public boolean startsWith(XMLString prefix) {
        return this.m_str.startsWith(prefix.toString());
    }

    @Override // com.sun.org.apache.xml.internal.utils.XMLString
    public boolean endsWith(String suffix) {
        return this.m_str.endsWith(suffix);
    }

    @Override // com.sun.org.apache.xml.internal.utils.XMLString
    public int hashCode() {
        return this.m_str.hashCode();
    }

    @Override // com.sun.org.apache.xml.internal.utils.XMLString
    public int indexOf(int ch) {
        return this.m_str.indexOf(ch);
    }

    @Override // com.sun.org.apache.xml.internal.utils.XMLString
    public int indexOf(int ch, int fromIndex) {
        return this.m_str.indexOf(ch, fromIndex);
    }

    @Override // com.sun.org.apache.xml.internal.utils.XMLString
    public int lastIndexOf(int ch) {
        return this.m_str.lastIndexOf(ch);
    }

    @Override // com.sun.org.apache.xml.internal.utils.XMLString
    public int lastIndexOf(int ch, int fromIndex) {
        return this.m_str.lastIndexOf(ch, fromIndex);
    }

    @Override // com.sun.org.apache.xml.internal.utils.XMLString
    public int indexOf(String str) {
        return this.m_str.indexOf(str);
    }

    @Override // com.sun.org.apache.xml.internal.utils.XMLString
    public int indexOf(XMLString str) {
        return this.m_str.indexOf(str.toString());
    }

    @Override // com.sun.org.apache.xml.internal.utils.XMLString
    public int indexOf(String str, int fromIndex) {
        return this.m_str.indexOf(str, fromIndex);
    }

    @Override // com.sun.org.apache.xml.internal.utils.XMLString
    public int lastIndexOf(String str) {
        return this.m_str.lastIndexOf(str);
    }

    @Override // com.sun.org.apache.xml.internal.utils.XMLString
    public int lastIndexOf(String str, int fromIndex) {
        return this.m_str.lastIndexOf(str, fromIndex);
    }

    @Override // com.sun.org.apache.xml.internal.utils.XMLString
    public XMLString substring(int beginIndex) {
        return new XMLStringDefault(this.m_str.substring(beginIndex));
    }

    @Override // com.sun.org.apache.xml.internal.utils.XMLString
    public XMLString substring(int beginIndex, int endIndex) {
        return new XMLStringDefault(this.m_str.substring(beginIndex, endIndex));
    }

    @Override // com.sun.org.apache.xml.internal.utils.XMLString
    public XMLString concat(String str) {
        return new XMLStringDefault(this.m_str.concat(str));
    }

    @Override // com.sun.org.apache.xml.internal.utils.XMLString
    public XMLString toLowerCase(Locale locale) {
        return new XMLStringDefault(this.m_str.toLowerCase(locale));
    }

    @Override // com.sun.org.apache.xml.internal.utils.XMLString
    public XMLString toLowerCase() {
        return new XMLStringDefault(this.m_str.toLowerCase());
    }

    @Override // com.sun.org.apache.xml.internal.utils.XMLString
    public XMLString toUpperCase(Locale locale) {
        return new XMLStringDefault(this.m_str.toUpperCase(locale));
    }

    @Override // com.sun.org.apache.xml.internal.utils.XMLString
    public XMLString toUpperCase() {
        return new XMLStringDefault(this.m_str.toUpperCase());
    }

    @Override // com.sun.org.apache.xml.internal.utils.XMLString
    public XMLString trim() {
        return new XMLStringDefault(this.m_str.trim());
    }

    @Override // com.sun.org.apache.xml.internal.utils.XMLString
    public String toString() {
        return this.m_str;
    }

    @Override // com.sun.org.apache.xml.internal.utils.XMLString
    public boolean hasString() {
        return true;
    }

    @Override // com.sun.org.apache.xml.internal.utils.XMLString
    public double toDouble() {
        try {
            return Double.valueOf(this.m_str).doubleValue();
        } catch (NumberFormatException e2) {
            return Double.NaN;
        }
    }
}
