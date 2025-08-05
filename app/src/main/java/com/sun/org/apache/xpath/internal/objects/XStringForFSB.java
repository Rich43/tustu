package com.sun.org.apache.xpath.internal.objects;

import com.sun.org.apache.xalan.internal.res.XSLMessages;
import com.sun.org.apache.xml.internal.utils.FastStringBuffer;
import com.sun.org.apache.xml.internal.utils.XMLCharacterRecognizer;
import com.sun.org.apache.xml.internal.utils.XMLString;
import com.sun.org.apache.xml.internal.utils.XMLStringFactory;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.ext.LexicalHandler;

/* loaded from: rt.jar:com/sun/org/apache/xpath/internal/objects/XStringForFSB.class */
public class XStringForFSB extends XString {
    static final long serialVersionUID = -1533039186550674548L;
    int m_start;
    int m_length;
    protected String m_strCache;
    protected int m_hash;

    public XStringForFSB(FastStringBuffer val, int start, int length) {
        super(val);
        this.m_strCache = null;
        this.m_hash = 0;
        this.m_start = start;
        this.m_length = length;
        if (null == val) {
            throw new IllegalArgumentException(XSLMessages.createXPATHMessage("ER_FASTSTRINGBUFFER_CANNOT_BE_NULL", null));
        }
    }

    private XStringForFSB(String val) {
        super(val);
        this.m_strCache = null;
        this.m_hash = 0;
        throw new IllegalArgumentException(XSLMessages.createXPATHMessage("ER_FSB_CANNOT_TAKE_STRING", null));
    }

    public FastStringBuffer fsb() {
        return (FastStringBuffer) this.m_obj;
    }

    @Override // com.sun.org.apache.xpath.internal.objects.XObject
    public void appendToFsb(FastStringBuffer fsb) {
        fsb.append(str());
    }

    @Override // com.sun.org.apache.xpath.internal.objects.XString, com.sun.org.apache.xml.internal.utils.XMLString
    public boolean hasString() {
        return null != this.m_strCache;
    }

    @Override // com.sun.org.apache.xpath.internal.objects.XObject
    public Object object() {
        return str();
    }

    @Override // com.sun.org.apache.xpath.internal.objects.XString, com.sun.org.apache.xpath.internal.objects.XObject
    public String str() {
        if (null == this.m_strCache) {
            this.m_strCache = fsb().getString(this.m_start, this.m_length);
        }
        return this.m_strCache;
    }

    @Override // com.sun.org.apache.xpath.internal.objects.XString, com.sun.org.apache.xpath.internal.objects.XObject
    public void dispatchCharactersEvents(ContentHandler ch) throws SAXException {
        fsb().sendSAXcharacters(ch, this.m_start, this.m_length);
    }

    @Override // com.sun.org.apache.xpath.internal.objects.XString, com.sun.org.apache.xml.internal.utils.XMLString
    public void dispatchAsComment(LexicalHandler lh) throws SAXException {
        fsb().sendSAXComment(lh, this.m_start, this.m_length);
    }

    @Override // com.sun.org.apache.xpath.internal.objects.XString, com.sun.org.apache.xml.internal.utils.XMLString
    public int length() {
        return this.m_length;
    }

    @Override // com.sun.org.apache.xpath.internal.objects.XString, com.sun.org.apache.xml.internal.utils.XMLString
    public char charAt(int index) {
        return fsb().charAt(this.m_start + index);
    }

    @Override // com.sun.org.apache.xpath.internal.objects.XString, com.sun.org.apache.xml.internal.utils.XMLString
    public void getChars(int srcBegin, int srcEnd, char[] dst, int dstBegin) {
        int n2 = srcEnd - srcBegin;
        if (n2 > this.m_length) {
            n2 = this.m_length;
        }
        if (n2 > dst.length - dstBegin) {
            n2 = dst.length - dstBegin;
        }
        int end = srcBegin + this.m_start + n2;
        int d2 = dstBegin;
        FastStringBuffer fsb = fsb();
        for (int i2 = srcBegin + this.m_start; i2 < end; i2++) {
            int i3 = d2;
            d2++;
            dst[i3] = fsb.charAt(i2);
        }
    }

    @Override // com.sun.org.apache.xpath.internal.objects.XString, com.sun.org.apache.xml.internal.utils.XMLString
    public boolean equals(XMLString obj2) {
        if (this == obj2) {
            return true;
        }
        int n2 = this.m_length;
        if (n2 == obj2.length()) {
            FastStringBuffer fsb = fsb();
            int i2 = this.m_start;
            int j2 = 0;
            while (true) {
                int i3 = n2;
                n2--;
                if (i3 != 0) {
                    if (fsb.charAt(i2) != obj2.charAt(j2)) {
                        return false;
                    }
                    i2++;
                    j2++;
                } else {
                    return true;
                }
            }
        } else {
            return false;
        }
    }

    @Override // com.sun.org.apache.xpath.internal.objects.XString, com.sun.org.apache.xpath.internal.objects.XObject
    public boolean equals(XObject obj2) {
        if (this == obj2) {
            return true;
        }
        if (obj2.getType() == 2) {
            return obj2.equals((XObject) this);
        }
        String str = obj2.str();
        int n2 = this.m_length;
        if (n2 == str.length()) {
            FastStringBuffer fsb = fsb();
            int i2 = this.m_start;
            int j2 = 0;
            while (true) {
                int i3 = n2;
                n2--;
                if (i3 != 0) {
                    if (fsb.charAt(i2) != str.charAt(j2)) {
                        return false;
                    }
                    i2++;
                    j2++;
                } else {
                    return true;
                }
            }
        } else {
            return false;
        }
    }

    @Override // com.sun.org.apache.xpath.internal.objects.XString, com.sun.org.apache.xml.internal.utils.XMLString
    public boolean equals(String anotherString) {
        int n2 = this.m_length;
        if (n2 == anotherString.length()) {
            FastStringBuffer fsb = fsb();
            int i2 = this.m_start;
            int j2 = 0;
            while (true) {
                int i3 = n2;
                n2--;
                if (i3 != 0) {
                    if (fsb.charAt(i2) != anotherString.charAt(j2)) {
                        return false;
                    }
                    i2++;
                    j2++;
                } else {
                    return true;
                }
            }
        } else {
            return false;
        }
    }

    @Override // com.sun.org.apache.xpath.internal.objects.XString, com.sun.org.apache.xml.internal.utils.XMLString
    public boolean equals(Object obj2) {
        if (null == obj2) {
            return false;
        }
        if (obj2 instanceof XNumber) {
            return obj2.equals(this);
        }
        if (obj2 instanceof XNodeSet) {
            return obj2.equals(this);
        }
        if (obj2 instanceof XStringForFSB) {
            return equals((XMLString) obj2);
        }
        return equals(obj2.toString());
    }

    @Override // com.sun.org.apache.xpath.internal.objects.XString, com.sun.org.apache.xml.internal.utils.XMLString
    public boolean equalsIgnoreCase(String anotherString) {
        if (this.m_length == anotherString.length()) {
            return str().equalsIgnoreCase(anotherString);
        }
        return false;
    }

    @Override // com.sun.org.apache.xpath.internal.objects.XString, com.sun.org.apache.xml.internal.utils.XMLString
    public int compareTo(XMLString xstr) {
        int len1 = this.m_length;
        int len2 = xstr.length();
        int n2 = Math.min(len1, len2);
        FastStringBuffer fsb = fsb();
        int i2 = this.m_start;
        int j2 = 0;
        while (true) {
            int i3 = n2;
            n2--;
            if (i3 != 0) {
                char c1 = fsb.charAt(i2);
                char c2 = xstr.charAt(j2);
                if (c1 != c2) {
                    return c1 - c2;
                }
                i2++;
                j2++;
            } else {
                return len1 - len2;
            }
        }
    }

    @Override // com.sun.org.apache.xpath.internal.objects.XString, com.sun.org.apache.xml.internal.utils.XMLString
    public int compareToIgnoreCase(XMLString xstr) {
        int len1 = this.m_length;
        int len2 = xstr.length();
        int n2 = Math.min(len1, len2);
        FastStringBuffer fsb = fsb();
        int i2 = this.m_start;
        int j2 = 0;
        while (true) {
            int i3 = n2;
            n2--;
            if (i3 != 0) {
                char c1 = Character.toLowerCase(fsb.charAt(i2));
                char c2 = Character.toLowerCase(xstr.charAt(j2));
                if (c1 != c2) {
                    return c1 - c2;
                }
                i2++;
                j2++;
            } else {
                return len1 - len2;
            }
        }
    }

    @Override // com.sun.org.apache.xpath.internal.objects.XString, com.sun.org.apache.xml.internal.utils.XMLString
    public int hashCode() {
        return super.hashCode();
    }

    @Override // com.sun.org.apache.xpath.internal.objects.XString, com.sun.org.apache.xml.internal.utils.XMLString
    public boolean startsWith(XMLString prefix, int toffset) {
        FastStringBuffer fsb = fsb();
        int to = this.m_start + toffset;
        int i2 = this.m_start + this.m_length;
        int po = 0;
        int pc = prefix.length();
        if (toffset < 0 || toffset > this.m_length - pc) {
            return false;
        }
        while (true) {
            pc--;
            if (pc >= 0) {
                if (fsb.charAt(to) != prefix.charAt(po)) {
                    return false;
                }
                to++;
                po++;
            } else {
                return true;
            }
        }
    }

    @Override // com.sun.org.apache.xpath.internal.objects.XString, com.sun.org.apache.xml.internal.utils.XMLString
    public boolean startsWith(XMLString prefix) {
        return startsWith(prefix, 0);
    }

    @Override // com.sun.org.apache.xpath.internal.objects.XString, com.sun.org.apache.xml.internal.utils.XMLString
    public int indexOf(int ch) {
        return indexOf(ch, 0);
    }

    @Override // com.sun.org.apache.xpath.internal.objects.XString, com.sun.org.apache.xml.internal.utils.XMLString
    public int indexOf(int ch, int fromIndex) {
        int max = this.m_start + this.m_length;
        FastStringBuffer fsb = fsb();
        if (fromIndex < 0) {
            fromIndex = 0;
        } else if (fromIndex >= this.m_length) {
            return -1;
        }
        for (int i2 = this.m_start + fromIndex; i2 < max; i2++) {
            if (fsb.charAt(i2) == ch) {
                return i2 - this.m_start;
            }
        }
        return -1;
    }

    @Override // com.sun.org.apache.xpath.internal.objects.XString, com.sun.org.apache.xml.internal.utils.XMLString
    public XMLString substring(int beginIndex) {
        int len = this.m_length - beginIndex;
        if (len <= 0) {
            return XString.EMPTYSTRING;
        }
        int start = this.m_start + beginIndex;
        return new XStringForFSB(fsb(), start, len);
    }

    @Override // com.sun.org.apache.xpath.internal.objects.XString, com.sun.org.apache.xml.internal.utils.XMLString
    public XMLString substring(int beginIndex, int endIndex) {
        int len = endIndex - beginIndex;
        if (len > this.m_length) {
            len = this.m_length;
        }
        if (len <= 0) {
            return XString.EMPTYSTRING;
        }
        int start = this.m_start + beginIndex;
        return new XStringForFSB(fsb(), start, len);
    }

    @Override // com.sun.org.apache.xpath.internal.objects.XString, com.sun.org.apache.xml.internal.utils.XMLString
    public XMLString concat(String str) {
        return new XString(str().concat(str));
    }

    @Override // com.sun.org.apache.xpath.internal.objects.XString, com.sun.org.apache.xml.internal.utils.XMLString
    public XMLString trim() {
        return fixWhiteSpace(true, true, false);
    }

    private static boolean isSpace(char ch) {
        return XMLCharacterRecognizer.isWhiteSpace(ch);
    }

    @Override // com.sun.org.apache.xpath.internal.objects.XString, com.sun.org.apache.xml.internal.utils.XMLString
    public XMLString fixWhiteSpace(boolean trimHead, boolean trimTail, boolean doublePunctuationSpaces) {
        int end = this.m_length + this.m_start;
        char[] buf = new char[this.m_length];
        FastStringBuffer fsb = fsb();
        boolean edit = false;
        int d2 = 0;
        boolean pres = false;
        for (int s2 = this.m_start; s2 < end; s2++) {
            char c2 = fsb.charAt(s2);
            if (isSpace(c2)) {
                if (!pres) {
                    if (' ' != c2) {
                        edit = true;
                    }
                    int i2 = d2;
                    d2++;
                    buf[i2] = ' ';
                    if (doublePunctuationSpaces && d2 != 0) {
                        char prevChar = buf[d2 - 1];
                        if (prevChar != '.' && prevChar != '!' && prevChar != '?') {
                            pres = true;
                        }
                    } else {
                        pres = true;
                    }
                } else {
                    edit = true;
                    pres = true;
                }
            } else {
                int i3 = d2;
                d2++;
                buf[i3] = c2;
                pres = false;
            }
        }
        if (trimTail && 1 <= d2 && ' ' == buf[d2 - 1]) {
            edit = true;
            d2--;
        }
        int start = 0;
        if (trimHead && 0 < d2 && ' ' == buf[0]) {
            edit = true;
            start = 0 + 1;
        }
        XMLStringFactory xsf = XMLStringFactoryImpl.getFactory();
        return edit ? xsf.newstr(buf, start, d2 - start) : this;
    }

    @Override // com.sun.org.apache.xpath.internal.objects.XString, com.sun.org.apache.xml.internal.utils.XMLString
    public double toDouble() {
        char c2;
        if (this.m_length == 0) {
            return Double.NaN;
        }
        String valueString = fsb().getString(this.m_start, this.m_length);
        int i2 = 0;
        while (i2 < this.m_length && XMLCharacterRecognizer.isWhiteSpace(valueString.charAt(i2))) {
            i2++;
        }
        if (i2 == this.m_length) {
            return Double.NaN;
        }
        if (valueString.charAt(i2) == '-') {
            i2++;
        }
        while (i2 < this.m_length && ((c2 = valueString.charAt(i2)) == '.' || (c2 >= '0' && c2 <= '9'))) {
            i2++;
        }
        while (i2 < this.m_length && XMLCharacterRecognizer.isWhiteSpace(valueString.charAt(i2))) {
            i2++;
        }
        if (i2 != this.m_length) {
            return Double.NaN;
        }
        try {
            return Double.parseDouble(valueString);
        } catch (NumberFormatException e2) {
            return Double.NaN;
        }
    }
}
