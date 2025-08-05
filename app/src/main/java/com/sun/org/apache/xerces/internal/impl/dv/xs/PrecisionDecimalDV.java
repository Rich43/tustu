package com.sun.org.apache.xerces.internal.impl.dv.xs;

import com.sun.org.apache.xerces.internal.impl.dv.InvalidDatatypeValueException;
import com.sun.org.apache.xerces.internal.impl.dv.ValidationContext;
import sun.util.locale.LanguageTag;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/impl/dv/xs/PrecisionDecimalDV.class */
class PrecisionDecimalDV extends TypeValidator {
    PrecisionDecimalDV() {
    }

    /* loaded from: rt.jar:com/sun/org/apache/xerces/internal/impl/dv/xs/PrecisionDecimalDV$XPrecisionDecimal.class */
    static final class XPrecisionDecimal {
        int sign;
        String ivalue;
        private String canonical;
        int totalDigits = 0;
        int intDigits = 0;
        int fracDigits = 0;
        String fvalue = "";
        int pvalue = 0;

        XPrecisionDecimal(String content) throws NumberFormatException {
            this.sign = 1;
            this.ivalue = "";
            if (content.equals("NaN")) {
                this.ivalue = content;
                this.sign = 0;
            }
            if (content.equals("+INF") || content.equals("INF") || content.equals("-INF")) {
                this.ivalue = content.charAt(0) == '+' ? content.substring(1) : content;
            } else {
                initD(content);
            }
        }

        void initD(String content) throws NumberFormatException {
            int len = content.length();
            if (len == 0) {
                throw new NumberFormatException();
            }
            int intStart = 0;
            int fracStart = 0;
            int fracEnd = 0;
            if (content.charAt(0) == '+') {
                intStart = 1;
            } else if (content.charAt(0) == '-') {
                intStart = 1;
                this.sign = -1;
            }
            int actualIntStart = intStart;
            while (actualIntStart < len && content.charAt(actualIntStart) == '0') {
                actualIntStart++;
            }
            int intEnd = actualIntStart;
            while (intEnd < len && TypeValidator.isDigit(content.charAt(intEnd))) {
                intEnd++;
            }
            if (intEnd < len) {
                if (content.charAt(intEnd) != '.' && content.charAt(intEnd) != 'E' && content.charAt(intEnd) != 'e') {
                    throw new NumberFormatException();
                }
                if (content.charAt(intEnd) == '.') {
                    fracStart = intEnd + 1;
                    fracEnd = fracStart;
                    while (fracEnd < len && TypeValidator.isDigit(content.charAt(fracEnd))) {
                        fracEnd++;
                    }
                } else {
                    this.pvalue = Integer.parseInt(content.substring(intEnd + 1, len));
                }
            }
            if (intStart == intEnd && fracStart == fracEnd) {
                throw new NumberFormatException();
            }
            for (int fracPos = fracStart; fracPos < fracEnd; fracPos++) {
                if (!TypeValidator.isDigit(content.charAt(fracPos))) {
                    throw new NumberFormatException();
                }
            }
            this.intDigits = intEnd - actualIntStart;
            this.fracDigits = fracEnd - fracStart;
            if (this.intDigits > 0) {
                this.ivalue = content.substring(actualIntStart, intEnd);
            }
            if (this.fracDigits > 0) {
                this.fvalue = content.substring(fracStart, fracEnd);
                if (fracEnd < len) {
                    this.pvalue = Integer.parseInt(content.substring(fracEnd + 1, len));
                }
            }
            this.totalDigits = this.intDigits + this.fracDigits;
        }

        private static String canonicalToStringForHashCode(String ivalue, String fvalue, int sign, int pvalue) {
            if ("NaN".equals(ivalue)) {
                return "NaN";
            }
            if ("INF".equals(ivalue)) {
                return sign < 0 ? "-INF" : "INF";
            }
            StringBuilder builder = new StringBuilder();
            int ilen = ivalue.length();
            int flen0 = fvalue.length();
            int lastNonZero = flen0;
            while (lastNonZero > 0 && fvalue.charAt(lastNonZero - 1) == '0') {
                lastNonZero--;
            }
            int flen = lastNonZero;
            int exponent = pvalue;
            int iStart = 0;
            while (iStart < ilen && ivalue.charAt(iStart) == '0') {
                iStart++;
            }
            int fStart = 0;
            if (iStart < ivalue.length()) {
                builder.append(sign == -1 ? LanguageTag.SEP : "");
                builder.append(ivalue.charAt(iStart));
                iStart++;
            } else if (flen > 0) {
                int fStart2 = 0;
                while (fStart2 < flen && fvalue.charAt(fStart2) == '0') {
                    fStart2++;
                }
                if (fStart2 < flen) {
                    builder.append(sign == -1 ? LanguageTag.SEP : "");
                    builder.append(fvalue.charAt(fStart2));
                    fStart = fStart2 + 1;
                    exponent -= fStart;
                } else {
                    return "0";
                }
            } else {
                return "0";
            }
            if (iStart < ilen || fStart < flen) {
                builder.append('.');
            }
            while (iStart < ilen) {
                int i2 = iStart;
                iStart++;
                builder.append(ivalue.charAt(i2));
                exponent++;
            }
            while (fStart < flen) {
                int i3 = fStart;
                fStart++;
                builder.append(fvalue.charAt(i3));
            }
            if (exponent != 0) {
                builder.append("E").append(exponent);
            }
            return builder.toString();
        }

        public boolean equals(Object val) {
            if (val == this) {
                return true;
            }
            if (!(val instanceof XPrecisionDecimal)) {
                return false;
            }
            XPrecisionDecimal oval = (XPrecisionDecimal) val;
            return compareTo(oval) == 0;
        }

        public int hashCode() {
            return canonicalToStringForHashCode(this.ivalue, this.fvalue, this.sign, this.pvalue).hashCode();
        }

        private int compareFractionalPart(XPrecisionDecimal oval) {
            if (this.fvalue.equals(oval.fvalue)) {
                return 0;
            }
            StringBuffer temp1 = new StringBuffer(this.fvalue);
            StringBuffer temp2 = new StringBuffer(oval.fvalue);
            truncateTrailingZeros(temp1, temp2);
            return temp1.toString().compareTo(temp2.toString());
        }

        private void truncateTrailingZeros(StringBuffer fValue, StringBuffer otherFValue) {
            for (int i2 = fValue.length() - 1; i2 >= 0 && fValue.charAt(i2) == '0'; i2--) {
                fValue.deleteCharAt(i2);
            }
            for (int i3 = otherFValue.length() - 1; i3 >= 0 && otherFValue.charAt(i3) == '0'; i3--) {
                otherFValue.deleteCharAt(i3);
            }
        }

        public int compareTo(XPrecisionDecimal val) {
            if (this.sign == 0) {
                return 2;
            }
            if (this.ivalue.equals("INF") || val.ivalue.equals("INF")) {
                if (this.ivalue.equals(val.ivalue)) {
                    return 0;
                }
                if (this.ivalue.equals("INF")) {
                    return 1;
                }
                return -1;
            }
            if (this.ivalue.equals("-INF") || val.ivalue.equals("-INF")) {
                if (this.ivalue.equals(val.ivalue)) {
                    return 0;
                }
                if (this.ivalue.equals("-INF")) {
                    return -1;
                }
                return 1;
            }
            if (this.sign != val.sign) {
                return this.sign > val.sign ? 1 : -1;
            }
            return this.sign * compare(val);
        }

        private int compare(XPrecisionDecimal val) {
            if (this.pvalue != 0 || val.pvalue != 0) {
                if (this.pvalue == val.pvalue) {
                    return intComp(val);
                }
                if (this.intDigits + this.pvalue != val.intDigits + val.pvalue) {
                    return this.intDigits + this.pvalue > val.intDigits + val.pvalue ? 1 : -1;
                }
                if (this.pvalue > val.pvalue) {
                    int expDiff = this.pvalue - val.pvalue;
                    StringBuffer buffer = new StringBuffer(this.ivalue);
                    StringBuffer fbuffer = new StringBuffer(this.fvalue);
                    for (int i2 = 0; i2 < expDiff; i2++) {
                        if (i2 < this.fracDigits) {
                            buffer.append(this.fvalue.charAt(i2));
                            fbuffer.deleteCharAt(i2);
                        } else {
                            buffer.append('0');
                        }
                    }
                    return compareDecimal(buffer.toString(), val.ivalue, fbuffer.toString(), val.fvalue);
                }
                int expDiff2 = val.pvalue - this.pvalue;
                StringBuffer buffer2 = new StringBuffer(val.ivalue);
                StringBuffer fbuffer2 = new StringBuffer(val.fvalue);
                for (int i3 = 0; i3 < expDiff2; i3++) {
                    if (i3 < val.fracDigits) {
                        buffer2.append(val.fvalue.charAt(i3));
                        fbuffer2.deleteCharAt(i3);
                    } else {
                        buffer2.append('0');
                    }
                }
                return compareDecimal(this.ivalue, buffer2.toString(), this.fvalue, fbuffer2.toString());
            }
            return intComp(val);
        }

        private int intComp(XPrecisionDecimal val) {
            if (this.intDigits != val.intDigits) {
                return this.intDigits > val.intDigits ? 1 : -1;
            }
            return compareDecimal(this.ivalue, val.ivalue, this.fvalue, val.fvalue);
        }

        private int compareDecimal(String iValue, String fValue, String otherIValue, String otherFValue) {
            int ret = iValue.compareTo(otherIValue);
            if (ret != 0) {
                return ret > 0 ? 1 : -1;
            }
            if (fValue.equals(otherFValue)) {
                return 0;
            }
            StringBuffer temp1 = new StringBuffer(fValue);
            StringBuffer temp2 = new StringBuffer(otherFValue);
            truncateTrailingZeros(temp1, temp2);
            int ret2 = temp1.toString().compareTo(temp2.toString());
            if (ret2 == 0) {
                return 0;
            }
            return ret2 > 0 ? 1 : -1;
        }

        public synchronized String toString() {
            if (this.canonical == null) {
                makeCanonical();
            }
            return this.canonical;
        }

        private void makeCanonical() {
            this.canonical = "TBD by Working Group";
        }

        public boolean isIdentical(XPrecisionDecimal decimal) {
            if (this.ivalue.equals(decimal.ivalue) && (this.ivalue.equals("INF") || this.ivalue.equals("-INF") || this.ivalue.equals("NaN"))) {
                return true;
            }
            if (this.sign == decimal.sign && this.intDigits == decimal.intDigits && this.fracDigits == decimal.fracDigits && this.pvalue == decimal.pvalue && this.ivalue.equals(decimal.ivalue) && this.fvalue.equals(decimal.fvalue)) {
                return true;
            }
            return false;
        }
    }

    @Override // com.sun.org.apache.xerces.internal.impl.dv.xs.TypeValidator
    public short getAllowedFacets() {
        return (short) 4088;
    }

    @Override // com.sun.org.apache.xerces.internal.impl.dv.xs.TypeValidator
    public Object getActualValue(String content, ValidationContext context) throws InvalidDatatypeValueException {
        try {
            return new XPrecisionDecimal(content);
        } catch (NumberFormatException e2) {
            throw new InvalidDatatypeValueException("cvc-datatype-valid.1.2.1", new Object[]{content, "precisionDecimal"});
        }
    }

    @Override // com.sun.org.apache.xerces.internal.impl.dv.xs.TypeValidator
    public int compare(Object value1, Object value2) {
        return ((XPrecisionDecimal) value1).compareTo((XPrecisionDecimal) value2);
    }

    @Override // com.sun.org.apache.xerces.internal.impl.dv.xs.TypeValidator
    public int getFractionDigits(Object value) {
        return ((XPrecisionDecimal) value).fracDigits;
    }

    @Override // com.sun.org.apache.xerces.internal.impl.dv.xs.TypeValidator
    public int getTotalDigits(Object value) {
        return ((XPrecisionDecimal) value).totalDigits;
    }

    @Override // com.sun.org.apache.xerces.internal.impl.dv.xs.TypeValidator
    public boolean isIdentical(Object value1, Object value2) {
        if (!(value2 instanceof XPrecisionDecimal) || !(value1 instanceof XPrecisionDecimal)) {
            return false;
        }
        return ((XPrecisionDecimal) value1).isIdentical((XPrecisionDecimal) value2);
    }
}
