package com.sun.org.apache.xpath.internal.objects;

import com.sun.org.apache.xalan.internal.res.XSLMessages;
import com.sun.org.apache.xml.internal.utils.FastStringBuffer;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.ext.LexicalHandler;

/* loaded from: rt.jar:com/sun/org/apache/xpath/internal/objects/XStringForChars.class */
public class XStringForChars extends XString {
    static final long serialVersionUID = -2235248887220850467L;
    int m_start;
    int m_length;
    protected String m_strCache;

    public XStringForChars(char[] val, int start, int length) {
        super(val);
        this.m_strCache = null;
        this.m_start = start;
        this.m_length = length;
        if (null == val) {
            throw new IllegalArgumentException(XSLMessages.createXPATHMessage("ER_FASTSTRINGBUFFER_CANNOT_BE_NULL", null));
        }
    }

    private XStringForChars(String val) {
        super(val);
        this.m_strCache = null;
        throw new IllegalArgumentException(XSLMessages.createXPATHMessage("ER_XSTRINGFORCHARS_CANNOT_TAKE_STRING", null));
    }

    public FastStringBuffer fsb() {
        throw new RuntimeException(XSLMessages.createXPATHMessage("ER_FSB_NOT_SUPPORTED_XSTRINGFORCHARS", null));
    }

    @Override // com.sun.org.apache.xpath.internal.objects.XObject
    public void appendToFsb(FastStringBuffer fsb) {
        fsb.append((char[]) this.m_obj, this.m_start, this.m_length);
    }

    @Override // com.sun.org.apache.xpath.internal.objects.XString, com.sun.org.apache.xml.internal.utils.XMLString
    public boolean hasString() {
        return null != this.m_strCache;
    }

    @Override // com.sun.org.apache.xpath.internal.objects.XString, com.sun.org.apache.xpath.internal.objects.XObject
    public String str() {
        if (null == this.m_strCache) {
            this.m_strCache = new String((char[]) this.m_obj, this.m_start, this.m_length);
        }
        return this.m_strCache;
    }

    @Override // com.sun.org.apache.xpath.internal.objects.XObject
    public Object object() {
        return str();
    }

    @Override // com.sun.org.apache.xpath.internal.objects.XString, com.sun.org.apache.xpath.internal.objects.XObject
    public void dispatchCharactersEvents(ContentHandler ch) throws SAXException {
        ch.characters((char[]) this.m_obj, this.m_start, this.m_length);
    }

    @Override // com.sun.org.apache.xpath.internal.objects.XString, com.sun.org.apache.xml.internal.utils.XMLString
    public void dispatchAsComment(LexicalHandler lh) throws SAXException {
        lh.comment((char[]) this.m_obj, this.m_start, this.m_length);
    }

    @Override // com.sun.org.apache.xpath.internal.objects.XString, com.sun.org.apache.xml.internal.utils.XMLString
    public int length() {
        return this.m_length;
    }

    @Override // com.sun.org.apache.xpath.internal.objects.XString, com.sun.org.apache.xml.internal.utils.XMLString
    public char charAt(int index) {
        return ((char[]) this.m_obj)[index + this.m_start];
    }

    @Override // com.sun.org.apache.xpath.internal.objects.XString, com.sun.org.apache.xml.internal.utils.XMLString
    public void getChars(int srcBegin, int srcEnd, char[] dst, int dstBegin) {
        System.arraycopy((char[]) this.m_obj, this.m_start + srcBegin, dst, dstBegin, srcEnd);
    }
}
