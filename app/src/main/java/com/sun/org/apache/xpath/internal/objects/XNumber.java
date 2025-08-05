package com.sun.org.apache.xpath.internal.objects;

import com.sun.org.apache.xalan.internal.templates.Constants;
import com.sun.org.apache.xml.internal.utils.WrappedRuntimeException;
import com.sun.org.apache.xpath.internal.ExpressionOwner;
import com.sun.org.apache.xpath.internal.XPathContext;
import com.sun.org.apache.xpath.internal.XPathVisitor;
import javax.xml.transform.TransformerException;
import sun.util.locale.LanguageTag;

/* loaded from: rt.jar:com/sun/org/apache/xpath/internal/objects/XNumber.class */
public class XNumber extends XObject {
    static final long serialVersionUID = -2720400709619020193L;
    double m_val;

    public XNumber(double d2) {
        this.m_val = d2;
    }

    public XNumber(Number num) {
        this.m_val = num.doubleValue();
        setObject(num);
    }

    @Override // com.sun.org.apache.xpath.internal.objects.XObject
    public int getType() {
        return 2;
    }

    @Override // com.sun.org.apache.xpath.internal.objects.XObject
    public String getTypeString() {
        return "#NUMBER";
    }

    @Override // com.sun.org.apache.xpath.internal.objects.XObject
    public double num() {
        return this.m_val;
    }

    @Override // com.sun.org.apache.xpath.internal.Expression
    public double num(XPathContext xctxt) throws TransformerException {
        return this.m_val;
    }

    @Override // com.sun.org.apache.xpath.internal.objects.XObject
    public boolean bool() {
        return (Double.isNaN(this.m_val) || this.m_val == 0.0d) ? false : true;
    }

    @Override // com.sun.org.apache.xpath.internal.objects.XObject
    public String str() throws NumberFormatException {
        String sign;
        if (Double.isNaN(this.m_val)) {
            return "NaN";
        }
        if (Double.isInfinite(this.m_val)) {
            if (this.m_val > 0.0d) {
                return Constants.ATTRVAL_INFINITY;
            }
            return "-Infinity";
        }
        double num = this.m_val;
        String s2 = Double.toString(num);
        int len = s2.length();
        if (s2.charAt(len - 2) == '.' && s2.charAt(len - 1) == '0') {
            String s3 = s2.substring(0, len - 2);
            if (s3.equals("-0")) {
                return "0";
            }
            return s3;
        }
        int e2 = s2.indexOf(69);
        if (e2 < 0) {
            if (s2.charAt(len - 1) == '0') {
                return s2.substring(0, len - 1);
            }
            return s2;
        }
        int exp = Integer.parseInt(s2.substring(e2 + 1));
        if (s2.charAt(0) == '-') {
            sign = LanguageTag.SEP;
            s2 = s2.substring(1);
            e2--;
        } else {
            sign = "";
        }
        int nDigits = e2 - 2;
        if (exp >= nDigits) {
            return sign + s2.substring(0, 1) + s2.substring(2, e2) + zeros(exp - nDigits);
        }
        while (s2.charAt(e2 - 1) == '0') {
            e2--;
        }
        if (exp > 0) {
            return sign + s2.substring(0, 1) + s2.substring(2, 2 + exp) + "." + s2.substring(2 + exp, e2);
        }
        return sign + "0." + zeros((-1) - exp) + s2.substring(0, 1) + s2.substring(2, e2);
    }

    private static String zeros(int n2) {
        if (n2 < 1) {
            return "";
        }
        char[] buf = new char[n2];
        for (int i2 = 0; i2 < n2; i2++) {
            buf[i2] = '0';
        }
        return new String(buf);
    }

    @Override // com.sun.org.apache.xpath.internal.objects.XObject
    public Object object() {
        if (null == this.m_obj) {
            setObject(new Double(this.m_val));
        }
        return this.m_obj;
    }

    @Override // com.sun.org.apache.xpath.internal.objects.XObject
    public boolean equals(XObject obj2) {
        int t2 = obj2.getType();
        try {
            if (t2 == 4) {
                return obj2.equals((XObject) this);
            }
            return t2 == 1 ? obj2.bool() == bool() : this.m_val == obj2.num();
        } catch (TransformerException te) {
            throw new WrappedRuntimeException(te);
        }
    }

    @Override // com.sun.org.apache.xpath.internal.Expression
    public boolean isStableNumber() {
        return true;
    }

    @Override // com.sun.org.apache.xpath.internal.objects.XObject, com.sun.org.apache.xpath.internal.XPathVisitable
    public void callVisitors(ExpressionOwner owner, XPathVisitor visitor) {
        visitor.visitNumberLiteral(owner, this);
    }
}
