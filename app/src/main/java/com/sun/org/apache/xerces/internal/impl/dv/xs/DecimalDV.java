package com.sun.org.apache.xerces.internal.impl.dv.xs;

import com.sun.corba.se.impl.util.Version;
import com.sun.org.apache.xerces.internal.impl.dv.InvalidDatatypeValueException;
import com.sun.org.apache.xerces.internal.impl.dv.ValidationContext;
import com.sun.org.apache.xerces.internal.impl.xs.SchemaSymbols;
import com.sun.org.apache.xerces.internal.xs.datatypes.XSDecimal;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Objects;
import sun.util.locale.LanguageTag;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/impl/dv/xs/DecimalDV.class */
public class DecimalDV extends TypeValidator {
    @Override // com.sun.org.apache.xerces.internal.impl.dv.xs.TypeValidator
    public final short getAllowedFacets() {
        return (short) 4088;
    }

    @Override // com.sun.org.apache.xerces.internal.impl.dv.xs.TypeValidator
    public Object getActualValue(String content, ValidationContext context) throws InvalidDatatypeValueException {
        try {
            return new XDecimal(content);
        } catch (NumberFormatException e2) {
            throw new InvalidDatatypeValueException("cvc-datatype-valid.1.2.1", new Object[]{content, SchemaSymbols.ATTVAL_DECIMAL});
        }
    }

    @Override // com.sun.org.apache.xerces.internal.impl.dv.xs.TypeValidator
    public final int compare(Object value1, Object value2) {
        return ((XDecimal) value1).compareTo((XDecimal) value2);
    }

    @Override // com.sun.org.apache.xerces.internal.impl.dv.xs.TypeValidator
    public final int getTotalDigits(Object value) {
        return ((XDecimal) value).totalDigits;
    }

    @Override // com.sun.org.apache.xerces.internal.impl.dv.xs.TypeValidator
    public final int getFractionDigits(Object value) {
        return ((XDecimal) value).fracDigits;
    }

    /* loaded from: rt.jar:com/sun/org/apache/xerces/internal/impl/dv/xs/DecimalDV$XDecimal.class */
    static final class XDecimal implements XSDecimal {
        int sign = 1;
        int totalDigits = 0;
        int intDigits = 0;
        int fracDigits = 0;
        String ivalue = "";
        String fvalue = "";
        boolean integer = false;
        private String canonical;

        XDecimal(String content) throws NumberFormatException {
            initD(content);
        }

        XDecimal(String content, boolean integer) throws NumberFormatException {
            if (integer) {
                initI(content);
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
                if (content.charAt(intEnd) != '.') {
                    throw new NumberFormatException();
                }
                fracStart = intEnd + 1;
                fracEnd = len;
            }
            if (intStart == intEnd && fracStart == fracEnd) {
                throw new NumberFormatException();
            }
            while (fracEnd > fracStart && content.charAt(fracEnd - 1) == '0') {
                fracEnd--;
            }
            for (int fracPos = fracStart; fracPos < fracEnd; fracPos++) {
                if (!TypeValidator.isDigit(content.charAt(fracPos))) {
                    throw new NumberFormatException();
                }
            }
            this.intDigits = intEnd - actualIntStart;
            this.fracDigits = fracEnd - fracStart;
            this.totalDigits = this.intDigits + this.fracDigits;
            if (this.intDigits > 0) {
                this.ivalue = content.substring(actualIntStart, intEnd);
                if (this.fracDigits > 0) {
                    this.fvalue = content.substring(fracStart, fracEnd);
                    return;
                }
                return;
            }
            if (this.fracDigits > 0) {
                this.fvalue = content.substring(fracStart, fracEnd);
            } else {
                this.sign = 0;
            }
        }

        void initI(String content) throws NumberFormatException {
            int len = content.length();
            if (len == 0) {
                throw new NumberFormatException();
            }
            int intStart = 0;
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
                throw new NumberFormatException();
            }
            if (intStart == intEnd) {
                throw new NumberFormatException();
            }
            this.intDigits = intEnd - actualIntStart;
            this.fracDigits = 0;
            this.totalDigits = this.intDigits;
            if (this.intDigits > 0) {
                this.ivalue = content.substring(actualIntStart, intEnd);
            } else {
                this.sign = 0;
            }
            this.integer = true;
        }

        public boolean equals(Object val) {
            if (val == this) {
                return true;
            }
            if (!(val instanceof XDecimal)) {
                return false;
            }
            XDecimal oval = (XDecimal) val;
            if (this.sign != oval.sign) {
                return false;
            }
            if (this.sign == 0) {
                return true;
            }
            return this.intDigits == oval.intDigits && this.fracDigits == oval.fracDigits && this.ivalue.equals(oval.ivalue) && this.fvalue.equals(oval.fvalue);
        }

        public int hashCode() {
            int hash = (17 * 7) + this.sign;
            return this.sign == 0 ? hash : (17 * ((17 * ((17 * ((17 * hash) + this.intDigits)) + this.fracDigits)) + Objects.hashCode(this.ivalue))) + Objects.hashCode(this.fvalue);
        }

        public int compareTo(XDecimal val) {
            if (this.sign != val.sign) {
                return this.sign > val.sign ? 1 : -1;
            }
            if (this.sign == 0) {
                return 0;
            }
            return this.sign * intComp(val);
        }

        private int intComp(XDecimal val) {
            if (this.intDigits != val.intDigits) {
                return this.intDigits > val.intDigits ? 1 : -1;
            }
            int ret = this.ivalue.compareTo(val.ivalue);
            if (ret != 0) {
                return ret > 0 ? 1 : -1;
            }
            int ret2 = this.fvalue.compareTo(val.fvalue);
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
            if (this.sign == 0) {
                if (this.integer) {
                    this.canonical = "0";
                    return;
                } else {
                    this.canonical = Version.BUILD;
                    return;
                }
            }
            if (this.integer && this.sign > 0) {
                this.canonical = this.ivalue;
                return;
            }
            StringBuilder buffer = new StringBuilder(this.totalDigits + 3);
            if (this.sign == -1) {
                buffer.append('-');
            }
            if (this.intDigits != 0) {
                buffer.append(this.ivalue);
            } else {
                buffer.append('0');
            }
            if (!this.integer) {
                buffer.append('.');
                if (this.fracDigits != 0) {
                    buffer.append(this.fvalue);
                } else {
                    buffer.append('0');
                }
            }
            this.canonical = buffer.toString();
        }

        @Override // com.sun.org.apache.xerces.internal.xs.datatypes.XSDecimal
        public BigDecimal getBigDecimal() {
            if (this.sign == 0) {
                return new BigDecimal(BigInteger.ZERO);
            }
            return new BigDecimal(toString());
        }

        @Override // com.sun.org.apache.xerces.internal.xs.datatypes.XSDecimal
        public BigInteger getBigInteger() throws NumberFormatException {
            if (this.fracDigits != 0) {
                throw new NumberFormatException();
            }
            if (this.sign == 0) {
                return BigInteger.ZERO;
            }
            if (this.sign == 1) {
                return new BigInteger(this.ivalue);
            }
            return new BigInteger(LanguageTag.SEP + this.ivalue);
        }

        @Override // com.sun.org.apache.xerces.internal.xs.datatypes.XSDecimal
        public long getLong() throws NumberFormatException {
            if (this.fracDigits != 0) {
                throw new NumberFormatException();
            }
            if (this.sign == 0) {
                return 0L;
            }
            if (this.sign == 1) {
                return Long.parseLong(this.ivalue);
            }
            return Long.parseLong(LanguageTag.SEP + this.ivalue);
        }

        @Override // com.sun.org.apache.xerces.internal.xs.datatypes.XSDecimal
        public int getInt() throws NumberFormatException {
            if (this.fracDigits != 0) {
                throw new NumberFormatException();
            }
            if (this.sign == 0) {
                return 0;
            }
            if (this.sign == 1) {
                return Integer.parseInt(this.ivalue);
            }
            return Integer.parseInt(LanguageTag.SEP + this.ivalue);
        }

        @Override // com.sun.org.apache.xerces.internal.xs.datatypes.XSDecimal
        public short getShort() throws NumberFormatException {
            if (this.fracDigits != 0) {
                throw new NumberFormatException();
            }
            if (this.sign == 0) {
                return (short) 0;
            }
            if (this.sign == 1) {
                return Short.parseShort(this.ivalue);
            }
            return Short.parseShort(LanguageTag.SEP + this.ivalue);
        }

        @Override // com.sun.org.apache.xerces.internal.xs.datatypes.XSDecimal
        public byte getByte() throws NumberFormatException {
            if (this.fracDigits != 0) {
                throw new NumberFormatException();
            }
            if (this.sign == 0) {
                return (byte) 0;
            }
            if (this.sign == 1) {
                return Byte.parseByte(this.ivalue);
            }
            return Byte.parseByte(LanguageTag.SEP + this.ivalue);
        }
    }
}
