package com.sun.org.apache.xerces.internal.impl.dv.xs;

import com.sun.org.apache.xerces.internal.impl.dv.InvalidDatatypeValueException;
import com.sun.org.apache.xerces.internal.impl.dv.ValidationContext;
import com.sun.org.apache.xerces.internal.impl.xs.SchemaSymbols;
import com.sun.org.apache.xerces.internal.xs.datatypes.XSFloat;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/impl/dv/xs/FloatDV.class */
public class FloatDV extends TypeValidator {
    @Override // com.sun.org.apache.xerces.internal.impl.dv.xs.TypeValidator
    public short getAllowedFacets() {
        return (short) 2552;
    }

    @Override // com.sun.org.apache.xerces.internal.impl.dv.xs.TypeValidator
    public Object getActualValue(String content, ValidationContext context) throws InvalidDatatypeValueException {
        try {
            return new XFloat(content);
        } catch (NumberFormatException e2) {
            throw new InvalidDatatypeValueException("cvc-datatype-valid.1.2.1", new Object[]{content, SchemaSymbols.ATTVAL_FLOAT});
        }
    }

    @Override // com.sun.org.apache.xerces.internal.impl.dv.xs.TypeValidator
    public int compare(Object value1, Object value2) {
        return ((XFloat) value1).compareTo((XFloat) value2);
    }

    @Override // com.sun.org.apache.xerces.internal.impl.dv.xs.TypeValidator
    public boolean isIdentical(Object value1, Object value2) {
        if (value2 instanceof XFloat) {
            return ((XFloat) value1).isIdentical((XFloat) value2);
        }
        return false;
    }

    /* loaded from: rt.jar:com/sun/org/apache/xerces/internal/impl/dv/xs/FloatDV$XFloat.class */
    private static final class XFloat implements XSFloat {
        private final float value;
        private String canonical;

        public XFloat(String s2) throws NumberFormatException {
            if (DoubleDV.isPossibleFP(s2)) {
                this.value = Float.parseFloat(s2);
                return;
            }
            if (s2.equals("INF")) {
                this.value = Float.POSITIVE_INFINITY;
            } else if (s2.equals("-INF")) {
                this.value = Float.NEGATIVE_INFINITY;
            } else {
                if (s2.equals("NaN")) {
                    this.value = Float.NaN;
                    return;
                }
                throw new NumberFormatException(s2);
            }
        }

        public boolean equals(Object val) {
            if (val == this) {
                return true;
            }
            if (!(val instanceof XFloat)) {
                return false;
            }
            XFloat oval = (XFloat) val;
            if (this.value == oval.value) {
                return true;
            }
            if (this.value != this.value && oval.value != oval.value) {
                return true;
            }
            return false;
        }

        public int hashCode() {
            if (this.value == 0.0f) {
                return 0;
            }
            return Float.floatToIntBits(this.value);
        }

        public boolean isIdentical(XFloat val) {
            if (val == this) {
                return true;
            }
            if (this.value == val.value) {
                return this.value != 0.0f || Float.floatToIntBits(this.value) == Float.floatToIntBits(val.value);
            }
            if (this.value != this.value && val.value != val.value) {
                return true;
            }
            return false;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public int compareTo(XFloat val) {
            float oval = val.value;
            if (this.value < oval) {
                return -1;
            }
            if (this.value > oval) {
                return 1;
            }
            if (this.value == oval) {
                return 0;
            }
            if (this.value != this.value && oval != oval) {
                return 0;
            }
            return 2;
        }

        public synchronized String toString() {
            int len;
            if (this.canonical == null) {
                if (this.value == Float.POSITIVE_INFINITY) {
                    this.canonical = "INF";
                } else if (this.value == Float.NEGATIVE_INFINITY) {
                    this.canonical = "-INF";
                } else if (this.value != this.value) {
                    this.canonical = "NaN";
                } else if (this.value == 0.0f) {
                    this.canonical = "0.0E1";
                } else {
                    this.canonical = Float.toString(this.value);
                    if (this.canonical.indexOf(69) == -1) {
                        int len2 = this.canonical.length();
                        char[] chars = new char[len2 + 3];
                        this.canonical.getChars(0, len2, chars, 0);
                        int edp = chars[0] == '-' ? 2 : 1;
                        if (this.value >= 1.0f || this.value <= -1.0f) {
                            int dp = this.canonical.indexOf(46);
                            for (int i2 = dp; i2 > edp; i2--) {
                                chars[i2] = chars[i2 - 1];
                            }
                            chars[edp] = '.';
                            while (chars[len2 - 1] == '0') {
                                len2--;
                            }
                            if (chars[len2 - 1] == '.') {
                                len2++;
                            }
                            int i3 = len2;
                            int len3 = len2 + 1;
                            chars[i3] = 'E';
                            int shift = dp - edp;
                            len = len3 + 1;
                            chars[len3] = (char) (shift + 48);
                        } else {
                            int nzp = edp + 1;
                            while (chars[nzp] == '0') {
                                nzp++;
                            }
                            chars[edp - 1] = chars[nzp];
                            chars[edp] = '.';
                            int i4 = nzp + 1;
                            int j2 = edp + 1;
                            while (i4 < len2) {
                                chars[j2] = chars[i4];
                                i4++;
                                j2++;
                            }
                            int len4 = len2 - (nzp - edp);
                            if (len4 == edp + 1) {
                                len4++;
                                chars[len4] = '0';
                            }
                            int i5 = len4;
                            int len5 = len4 + 1;
                            chars[i5] = 'E';
                            int len6 = len5 + 1;
                            chars[len5] = '-';
                            int shift2 = nzp - edp;
                            len = len6 + 1;
                            chars[len6] = (char) (shift2 + 48);
                        }
                        this.canonical = new String(chars, 0, len);
                    }
                }
            }
            return this.canonical;
        }

        @Override // com.sun.org.apache.xerces.internal.xs.datatypes.XSFloat
        public float getValue() {
            return this.value;
        }
    }
}
