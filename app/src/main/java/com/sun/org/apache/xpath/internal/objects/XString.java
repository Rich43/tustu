package com.sun.org.apache.xpath.internal.objects;

import com.sun.org.apache.xml.internal.dtm.DTM;
import com.sun.org.apache.xml.internal.utils.WrappedRuntimeException;
import com.sun.org.apache.xml.internal.utils.XMLCharacterRecognizer;
import com.sun.org.apache.xml.internal.utils.XMLString;
import com.sun.org.apache.xml.internal.utils.XMLStringFactory;
import com.sun.org.apache.xpath.internal.ExpressionOwner;
import com.sun.org.apache.xpath.internal.XPathContext;
import com.sun.org.apache.xpath.internal.XPathVisitor;
import java.util.Locale;
import javax.xml.transform.TransformerException;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.ext.LexicalHandler;

/* loaded from: rt.jar:com/sun/org/apache/xpath/internal/objects/XString.class */
public class XString extends XObject implements XMLString {
    static final long serialVersionUID = 2020470518395094525L;
    public static final XString EMPTYSTRING = new XString("");

    protected XString(Object val) {
        super(val);
    }

    public XString(String val) {
        super(val);
    }

    @Override // com.sun.org.apache.xpath.internal.objects.XObject
    public int getType() {
        return 3;
    }

    @Override // com.sun.org.apache.xpath.internal.objects.XObject
    public String getTypeString() {
        return "#STRING";
    }

    @Override // com.sun.org.apache.xml.internal.utils.XMLString
    public boolean hasString() {
        return true;
    }

    @Override // com.sun.org.apache.xpath.internal.objects.XObject
    public double num() {
        return toDouble();
    }

    @Override // com.sun.org.apache.xml.internal.utils.XMLString
    public double toDouble() {
        XMLString s2 = trim();
        double result = Double.NaN;
        for (int i2 = 0; i2 < s2.length(); i2++) {
            char c2 = s2.charAt(i2);
            if (c2 != '-' && c2 != '.' && (c2 < '0' || c2 > '9')) {
                return Double.NaN;
            }
        }
        try {
            result = Double.parseDouble(s2.toString());
        } catch (NumberFormatException e2) {
        }
        return result;
    }

    @Override // com.sun.org.apache.xpath.internal.objects.XObject
    public boolean bool() {
        return str().length() > 0;
    }

    @Override // com.sun.org.apache.xpath.internal.objects.XObject
    public XMLString xstr() {
        return this;
    }

    @Override // com.sun.org.apache.xpath.internal.objects.XObject
    public String str() {
        return null != this.m_obj ? (String) this.m_obj : "";
    }

    @Override // com.sun.org.apache.xpath.internal.objects.XObject
    public int rtf(XPathContext support) {
        DTM frag = support.createDocumentFragment();
        frag.appendTextChild(str());
        return frag.getDocument();
    }

    @Override // com.sun.org.apache.xpath.internal.objects.XObject
    public void dispatchCharactersEvents(ContentHandler ch) throws SAXException {
        String str = str();
        ch.characters(str.toCharArray(), 0, str.length());
    }

    @Override // com.sun.org.apache.xml.internal.utils.XMLString
    public void dispatchAsComment(LexicalHandler lh) throws SAXException {
        String str = str();
        lh.comment(str.toCharArray(), 0, str.length());
    }

    @Override // com.sun.org.apache.xml.internal.utils.XMLString
    public int length() {
        return str().length();
    }

    @Override // com.sun.org.apache.xml.internal.utils.XMLString
    public char charAt(int index) {
        return str().charAt(index);
    }

    @Override // com.sun.org.apache.xml.internal.utils.XMLString
    public void getChars(int srcBegin, int srcEnd, char[] dst, int dstBegin) {
        str().getChars(srcBegin, srcEnd, dst, dstBegin);
    }

    @Override // com.sun.org.apache.xpath.internal.objects.XObject
    public boolean equals(XObject obj2) {
        int t2 = obj2.getType();
        try {
            if (4 == t2) {
                return obj2.equals((XObject) this);
            }
            if (1 == t2) {
                return obj2.bool() == bool();
            }
            if (2 == t2) {
                return obj2.num() == num();
            }
            return xstr().equals(obj2.xstr());
        } catch (TransformerException te) {
            throw new WrappedRuntimeException(te);
        }
    }

    @Override // com.sun.org.apache.xml.internal.utils.XMLString
    public boolean equals(String obj2) {
        return str().equals(obj2);
    }

    @Override // com.sun.org.apache.xml.internal.utils.XMLString
    public boolean equals(XMLString obj2) {
        if (obj2 != null) {
            if (!obj2.hasString()) {
                return obj2.equals(str());
            }
            return str().equals(obj2.toString());
        }
        return false;
    }

    @Override // com.sun.org.apache.xml.internal.utils.XMLString
    public boolean equals(Object obj2) {
        if (null == obj2) {
            return false;
        }
        if (obj2 instanceof XNodeSet) {
            return obj2.equals(this);
        }
        if (obj2 instanceof XNumber) {
            return obj2.equals(this);
        }
        return str().equals(obj2.toString());
    }

    @Override // com.sun.org.apache.xml.internal.utils.XMLString
    public boolean equalsIgnoreCase(String anotherString) {
        return str().equalsIgnoreCase(anotherString);
    }

    @Override // com.sun.org.apache.xml.internal.utils.XMLString
    public int compareTo(XMLString xstr) {
        int len1 = length();
        int len2 = xstr.length();
        int n2 = Math.min(len1, len2);
        int i2 = 0;
        int j2 = 0;
        while (true) {
            int i3 = n2;
            n2--;
            if (i3 != 0) {
                char c1 = charAt(i2);
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

    @Override // com.sun.org.apache.xml.internal.utils.XMLString
    public int compareToIgnoreCase(XMLString str) {
        throw new WrappedRuntimeException(new NoSuchMethodException("Java 1.2 method, not yet implemented"));
    }

    @Override // com.sun.org.apache.xml.internal.utils.XMLString
    public boolean startsWith(String prefix, int toffset) {
        return str().startsWith(prefix, toffset);
    }

    @Override // com.sun.org.apache.xml.internal.utils.XMLString
    public boolean startsWith(String prefix) {
        return startsWith(prefix, 0);
    }

    @Override // com.sun.org.apache.xml.internal.utils.XMLString
    public boolean startsWith(XMLString prefix, int toffset) {
        int to = toffset;
        int tlim = length();
        int po = 0;
        int pc = prefix.length();
        if (toffset < 0 || toffset > tlim - pc) {
            return false;
        }
        while (true) {
            pc--;
            if (pc >= 0) {
                if (charAt(to) != prefix.charAt(po)) {
                    return false;
                }
                to++;
                po++;
            } else {
                return true;
            }
        }
    }

    @Override // com.sun.org.apache.xml.internal.utils.XMLString
    public boolean startsWith(XMLString prefix) {
        return startsWith(prefix, 0);
    }

    @Override // com.sun.org.apache.xml.internal.utils.XMLString
    public boolean endsWith(String suffix) {
        return str().endsWith(suffix);
    }

    @Override // com.sun.org.apache.xml.internal.utils.XMLString
    public int hashCode() {
        return str().hashCode();
    }

    @Override // com.sun.org.apache.xml.internal.utils.XMLString
    public int indexOf(int ch) {
        return str().indexOf(ch);
    }

    @Override // com.sun.org.apache.xml.internal.utils.XMLString
    public int indexOf(int ch, int fromIndex) {
        return str().indexOf(ch, fromIndex);
    }

    @Override // com.sun.org.apache.xml.internal.utils.XMLString
    public int lastIndexOf(int ch) {
        return str().lastIndexOf(ch);
    }

    @Override // com.sun.org.apache.xml.internal.utils.XMLString
    public int lastIndexOf(int ch, int fromIndex) {
        return str().lastIndexOf(ch, fromIndex);
    }

    @Override // com.sun.org.apache.xml.internal.utils.XMLString
    public int indexOf(String str) {
        return str().indexOf(str);
    }

    @Override // com.sun.org.apache.xml.internal.utils.XMLString
    public int indexOf(XMLString str) {
        return str().indexOf(str.toString());
    }

    @Override // com.sun.org.apache.xml.internal.utils.XMLString
    public int indexOf(String str, int fromIndex) {
        return str().indexOf(str, fromIndex);
    }

    @Override // com.sun.org.apache.xml.internal.utils.XMLString
    public int lastIndexOf(String str) {
        return str().lastIndexOf(str);
    }

    @Override // com.sun.org.apache.xml.internal.utils.XMLString
    public int lastIndexOf(String str, int fromIndex) {
        return str().lastIndexOf(str, fromIndex);
    }

    @Override // com.sun.org.apache.xml.internal.utils.XMLString
    public XMLString substring(int beginIndex) {
        return new XString(str().substring(beginIndex));
    }

    @Override // com.sun.org.apache.xml.internal.utils.XMLString
    public XMLString substring(int beginIndex, int endIndex) {
        return new XString(str().substring(beginIndex, endIndex));
    }

    @Override // com.sun.org.apache.xml.internal.utils.XMLString
    public XMLString concat(String str) {
        return new XString(str().concat(str));
    }

    @Override // com.sun.org.apache.xml.internal.utils.XMLString
    public XMLString toLowerCase(Locale locale) {
        return new XString(str().toLowerCase(locale));
    }

    @Override // com.sun.org.apache.xml.internal.utils.XMLString
    public XMLString toLowerCase() {
        return new XString(str().toLowerCase());
    }

    @Override // com.sun.org.apache.xml.internal.utils.XMLString
    public XMLString toUpperCase(Locale locale) {
        return new XString(str().toUpperCase(locale));
    }

    @Override // com.sun.org.apache.xml.internal.utils.XMLString
    public XMLString toUpperCase() {
        return new XString(str().toUpperCase());
    }

    @Override // com.sun.org.apache.xml.internal.utils.XMLString
    public XMLString trim() {
        return new XString(str().trim());
    }

    private static boolean isSpace(char ch) {
        return XMLCharacterRecognizer.isWhiteSpace(ch);
    }

    @Override // com.sun.org.apache.xml.internal.utils.XMLString
    public XMLString fixWhiteSpace(boolean trimHead, boolean trimTail, boolean doublePunctuationSpaces) {
        int len = length();
        char[] buf = new char[len];
        getChars(0, len, buf, 0);
        boolean edit = false;
        int s2 = 0;
        while (s2 < len && !isSpace(buf[s2])) {
            s2++;
        }
        int d2 = s2;
        boolean pres = false;
        while (s2 < len) {
            char c2 = buf[s2];
            if (isSpace(c2)) {
                if (!pres) {
                    if (' ' != c2) {
                        edit = true;
                    }
                    int i2 = d2;
                    d2++;
                    buf[i2] = ' ';
                    if (doublePunctuationSpaces && s2 != 0) {
                        char prevChar = buf[s2 - 1];
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
            s2++;
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
        return edit ? xsf.newstr(new String(buf, start, d2 - start)) : this;
    }

    @Override // com.sun.org.apache.xpath.internal.objects.XObject, com.sun.org.apache.xpath.internal.XPathVisitable
    public void callVisitors(ExpressionOwner owner, XPathVisitor visitor) {
        visitor.visitStringLiteral(owner, this);
    }
}
