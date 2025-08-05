package com.sun.org.apache.xalan.internal.xsltc.dom;

import com.sun.org.apache.xalan.internal.templates.Constants;
import com.sun.org.apache.xalan.internal.xsltc.DOM;
import com.sun.org.apache.xalan.internal.xsltc.Translet;
import com.sun.org.apache.xml.internal.dtm.DTMAxisIterator;
import java.util.Vector;
import org.icepdf.core.util.PdfOps;
import sun.util.locale.LanguageTag;

/* loaded from: rt.jar:com/sun/org/apache/xalan/internal/xsltc/dom/NodeCounter.class */
public abstract class NodeCounter {
    public static final int END = -1;
    public final DOM _document;
    public final DTMAxisIterator _iterator;
    public final Translet _translet;
    protected String _format;
    protected String _lang;
    protected String _letterValue;
    protected String _groupSep;
    protected int _groupSize;
    private static final String[] Thousands = {"", PdfOps.m_TOKEN, "mm", "mmm"};
    private static final String[] Hundreds = {"", PdfOps.c_TOKEN, "cc", "ccc", "cd", PdfOps.d_TOKEN, "dc", "dcc", "dccc", PdfOps.cm_TOKEN};
    private static final String[] Tens = {"", LanguageTag.PRIVATEUSE, "xx", "xxx", "xl", PdfOps.l_TOKEN, "lx", "lxx", "lxxx", "xc"};
    private static final String[] Ones = {"", PdfOps.i_TOKEN, "ii", "iii", "iv", PdfOps.v_TOKEN, "vi", "vii", "viii", "ix"};
    protected boolean _hasFrom;
    protected int _node = -1;
    protected int _nodeType = -1;
    protected double _value = -2.147483648E9d;
    private boolean _separFirst = true;
    private boolean _separLast = false;
    private Vector _separToks = new Vector();
    private Vector _formatToks = new Vector();
    private int _nSepars = 0;
    private int _nFormats = 0;
    private StringBuilder _tempBuffer = new StringBuilder();

    public abstract NodeCounter setStartNode(int i2);

    public abstract String getCounter();

    protected NodeCounter(Translet translet, DOM document, DTMAxisIterator iterator) {
        this._translet = translet;
        this._document = document;
        this._iterator = iterator;
    }

    protected NodeCounter(Translet translet, DOM document, DTMAxisIterator iterator, boolean hasFrom) {
        this._translet = translet;
        this._document = document;
        this._iterator = iterator;
        this._hasFrom = hasFrom;
    }

    public NodeCounter setValue(double value) {
        this._value = value;
        return this;
    }

    protected void setFormatting(String format, String lang, String letterValue, String groupSep, String groupSize) {
        this._lang = lang;
        this._groupSep = groupSep;
        this._letterValue = letterValue;
        this._groupSize = parseStringToAnInt(groupSize);
        setTokens(format);
    }

    private int parseStringToAnInt(String s2) {
        int limit;
        int result;
        if (s2 == null) {
            return 0;
        }
        int result2 = 0;
        boolean negative = false;
        int i2 = 0;
        int max = s2.length();
        if (max > 0) {
            if (s2.charAt(0) == '-') {
                negative = true;
                limit = Integer.MIN_VALUE;
                i2 = 0 + 1;
            } else {
                limit = -2147483647;
            }
            int multmin = limit / 10;
            if (i2 < max) {
                int i3 = i2;
                i2++;
                int digit = Character.digit(s2.charAt(i3), 10);
                if (digit < 0) {
                    return 0;
                }
                result2 = -digit;
            }
            while (i2 < max) {
                int i4 = i2;
                i2++;
                int digit2 = Character.digit(s2.charAt(i4), 10);
                if (digit2 < 0 || result2 < multmin || (result = result2 * 10) < limit + digit2) {
                    return 0;
                }
                result2 = result - digit2;
            }
            if (negative) {
                if (i2 > 1) {
                    return result2;
                }
                return 0;
            }
            return -result2;
        }
        return 0;
    }

    private final void setTokens(String format) {
        if (this._format != null && format.equals(this._format)) {
            return;
        }
        this._format = format;
        int length = this._format.length();
        boolean isFirst = true;
        this._separFirst = true;
        this._separLast = false;
        this._nSepars = 0;
        this._nFormats = 0;
        this._separToks.clear();
        this._formatToks.clear();
        int i2 = 0;
        while (i2 < length) {
            char c2 = format.charAt(i2);
            int j2 = i2;
            while (Character.isLetterOrDigit(c2)) {
                i2++;
                if (i2 == length) {
                    break;
                } else {
                    c2 = format.charAt(i2);
                }
            }
            if (i2 > j2) {
                if (isFirst) {
                    this._separToks.addElement(".");
                    this._separFirst = false;
                    isFirst = false;
                }
                this._formatToks.addElement(format.substring(j2, i2));
            }
            if (i2 == length) {
                break;
            }
            char c3 = format.charAt(i2);
            int j3 = i2;
            while (!Character.isLetterOrDigit(c3)) {
                i2++;
                if (i2 == length) {
                    break;
                }
                c3 = format.charAt(i2);
                isFirst = false;
            }
            if (i2 > j3) {
                this._separToks.addElement(format.substring(j3, i2));
            }
        }
        this._nSepars = this._separToks.size();
        this._nFormats = this._formatToks.size();
        if (this._nSepars > this._nFormats) {
            this._separLast = true;
        }
        if (this._separFirst) {
            this._nSepars--;
        }
        if (this._separLast) {
            this._nSepars--;
        }
        if (this._nSepars == 0) {
            this._separToks.insertElementAt(".", 1);
            this._nSepars++;
        }
        if (this._separFirst) {
            this._nSepars++;
        }
    }

    public NodeCounter setDefaultFormatting() {
        setFormatting("1", "en", Constants.ATTRVAL_ALPHABETIC, null, null);
        return this;
    }

    public String getCounter(String format, String lang, String letterValue, String groupSep, String groupSize) {
        setFormatting(format, lang, letterValue, groupSep, groupSize);
        return getCounter();
    }

    public boolean matchesCount(int node) {
        return this._nodeType == this._document.getExpandedTypeID(node);
    }

    public boolean matchesFrom(int node) {
        return false;
    }

    protected String formatNumbers(int value) {
        return formatNumbers(new int[]{value});
    }

    protected String formatNumbers(int[] values) {
        boolean isEmpty = true;
        for (int i2 : values) {
            if (i2 != Integer.MIN_VALUE) {
                isEmpty = false;
            }
        }
        if (isEmpty) {
            return "";
        }
        boolean isFirst = true;
        int t2 = 0;
        int s2 = 1;
        this._tempBuffer.setLength(0);
        StringBuilder buffer = this._tempBuffer;
        if (this._separFirst) {
            buffer.append((String) this._separToks.elementAt(0));
        }
        for (int value : values) {
            if (value != Integer.MIN_VALUE) {
                if (!isFirst) {
                    int i3 = s2;
                    s2++;
                    buffer.append((String) this._separToks.elementAt(i3));
                }
                int i4 = t2;
                t2++;
                formatValue(value, (String) this._formatToks.elementAt(i4), buffer);
                if (t2 == this._nFormats) {
                    t2--;
                }
                if (s2 >= this._nSepars) {
                    s2--;
                }
                isFirst = false;
            }
        }
        if (this._separLast) {
            buffer.append((String) this._separToks.lastElement());
        }
        return buffer.toString();
    }

    private void formatValue(int value, String format, StringBuilder buffer) {
        char c2 = format.charAt(0);
        if (Character.isDigit(c2)) {
            char zero = (char) (c2 - Character.getNumericValue(c2));
            StringBuilder temp = buffer;
            if (this._groupSize > 0) {
                temp = new StringBuilder();
            }
            String s2 = "";
            int i2 = value;
            while (true) {
                int n2 = i2;
                if (n2 <= 0) {
                    break;
                }
                s2 = ((char) (zero + (n2 % 10))) + s2;
                i2 = n2 / 10;
            }
            for (int i3 = 0; i3 < format.length() - s2.length(); i3++) {
                temp.append(zero);
            }
            temp.append(s2);
            if (this._groupSize > 0) {
                for (int i4 = 0; i4 < temp.length(); i4++) {
                    if (i4 != 0 && (temp.length() - i4) % this._groupSize == 0) {
                        buffer.append(this._groupSep);
                    }
                    buffer.append(temp.charAt(i4));
                }
                return;
            }
            return;
        }
        if (c2 == 'i' && !this._letterValue.equals(Constants.ATTRVAL_ALPHABETIC)) {
            buffer.append(romanValue(value));
            return;
        }
        if (c2 == 'I' && !this._letterValue.equals(Constants.ATTRVAL_ALPHABETIC)) {
            buffer.append(romanValue(value).toUpperCase());
            return;
        }
        int max = c2;
        if (c2 >= 945 && c2 <= 969) {
            max = 969;
        } else {
            while (Character.isLetterOrDigit((char) (max + 1))) {
                max++;
            }
        }
        buffer.append(alphaValue(value, c2, max));
    }

    private String alphaValue(int value, int min, int max) {
        if (value <= 0) {
            return "" + value;
        }
        int range = (max - min) + 1;
        char last = (char) (((value - 1) % range) + min);
        if (value > range) {
            return alphaValue((value - 1) / range, min, max) + last;
        }
        return "" + last;
    }

    private String romanValue(int n2) {
        if (n2 <= 0 || n2 > 4000) {
            return "" + n2;
        }
        return Thousands[n2 / 1000] + Hundreds[(n2 / 100) % 10] + Tens[(n2 / 10) % 10] + Ones[n2 % 10];
    }
}
