package sun.misc;

import java.util.Arrays;
import sun.misc.FloatingDecimal;

/* loaded from: rt.jar:sun/misc/FormattedFloatingDecimal.class */
public class FormattedFloatingDecimal {
    private int decExponentRounded;
    private char[] mantissa;
    private char[] exponent;
    private static final ThreadLocal<Object> threadLocalCharBuffer;
    static final /* synthetic */ boolean $assertionsDisabled;

    /* loaded from: rt.jar:sun/misc/FormattedFloatingDecimal$Form.class */
    public enum Form {
        SCIENTIFIC,
        COMPATIBLE,
        DECIMAL_FLOAT,
        GENERAL
    }

    static {
        $assertionsDisabled = !FormattedFloatingDecimal.class.desiredAssertionStatus();
        threadLocalCharBuffer = new ThreadLocal<Object>() { // from class: sun.misc.FormattedFloatingDecimal.1
            @Override // java.lang.ThreadLocal
            protected Object initialValue() {
                return new char[20];
            }
        };
    }

    public static FormattedFloatingDecimal valueOf(double d2, int i2, Form form) {
        return new FormattedFloatingDecimal(i2, form, FloatingDecimal.getBinaryToASCIIConverter(d2, form == Form.COMPATIBLE));
    }

    private static char[] getBuffer() {
        return (char[]) threadLocalCharBuffer.get();
    }

    private FormattedFloatingDecimal(int i2, Form form, FloatingDecimal.BinaryToASCIIConverter binaryToASCIIConverter) {
        if (binaryToASCIIConverter.isExceptional()) {
            this.mantissa = binaryToASCIIConverter.toJavaFormatString().toCharArray();
            this.exponent = null;
            return;
        }
        char[] buffer = getBuffer();
        int digits = binaryToASCIIConverter.getDigits(buffer);
        int decimalExponent = binaryToASCIIConverter.getDecimalExponent();
        boolean zIsNegative = binaryToASCIIConverter.isNegative();
        switch (form) {
            case COMPATIBLE:
                this.decExponentRounded = decimalExponent;
                fillCompatible(i2, buffer, digits, decimalExponent, zIsNegative);
                return;
            case DECIMAL_FLOAT:
                int iApplyPrecision = applyPrecision(decimalExponent, buffer, digits, decimalExponent + i2);
                fillDecimal(i2, buffer, digits, iApplyPrecision, zIsNegative);
                this.decExponentRounded = iApplyPrecision;
                return;
            case SCIENTIFIC:
                int iApplyPrecision2 = applyPrecision(decimalExponent, buffer, digits, i2 + 1);
                fillScientific(i2, buffer, digits, iApplyPrecision2, zIsNegative);
                this.decExponentRounded = iApplyPrecision2;
                return;
            case GENERAL:
                int iApplyPrecision3 = applyPrecision(decimalExponent, buffer, digits, i2);
                if (iApplyPrecision3 - 1 < -4 || iApplyPrecision3 - 1 >= i2) {
                    fillScientific(i2 - 1, buffer, digits, iApplyPrecision3, zIsNegative);
                } else {
                    fillDecimal(i2 - iApplyPrecision3, buffer, digits, iApplyPrecision3, zIsNegative);
                }
                this.decExponentRounded = iApplyPrecision3;
                return;
            default:
                if (!$assertionsDisabled) {
                    throw new AssertionError();
                }
                return;
        }
    }

    public int getExponentRounded() {
        return this.decExponentRounded - 1;
    }

    public char[] getMantissa() {
        return this.mantissa;
    }

    public char[] getExponent() {
        return this.exponent;
    }

    private static int applyPrecision(int i2, char[] cArr, int i3, int i4) {
        if (i4 >= i3 || i4 < 0) {
            return i2;
        }
        if (i4 == 0) {
            if (cArr[0] >= '5') {
                cArr[0] = '1';
                Arrays.fill(cArr, 1, i3, '0');
                return i2 + 1;
            }
            Arrays.fill(cArr, 0, i3, '0');
            return i2;
        }
        if (cArr[i4] >= '5') {
            int i5 = i4 - 1;
            char c2 = cArr[i5];
            if (c2 == '9') {
                while (c2 == '9' && i5 > 0) {
                    i5--;
                    c2 = cArr[i5];
                }
                if (c2 == '9') {
                    cArr[0] = '1';
                    Arrays.fill(cArr, 1, i3, '0');
                    return i2 + 1;
                }
            }
            cArr[i5] = (char) (c2 + 1);
            Arrays.fill(cArr, i5 + 1, i3, '0');
        } else {
            Arrays.fill(cArr, i4, i3, '0');
        }
        return i2;
    }

    private void fillCompatible(int i2, char[] cArr, int i3, int i4, boolean z2) {
        int i5;
        int i6;
        int i7 = z2 ? 1 : 0;
        if (i4 > 0 && i4 < 8) {
            if (i3 < i4) {
                int i8 = i4 - i3;
                this.mantissa = create(z2, i3 + i8 + 2);
                System.arraycopy(cArr, 0, this.mantissa, i7, i3);
                Arrays.fill(this.mantissa, i7 + i3, i7 + i3 + i8, '0');
                this.mantissa[i7 + i3 + i8] = '.';
                this.mantissa[i7 + i3 + i8 + 1] = '0';
                return;
            }
            if (i4 < i3) {
                int iMin = Math.min(i3 - i4, i2);
                this.mantissa = create(z2, i4 + 1 + iMin);
                System.arraycopy(cArr, 0, this.mantissa, i7, i4);
                this.mantissa[i7 + i4] = '.';
                System.arraycopy(cArr, i4, this.mantissa, i7 + i4 + 1, iMin);
                return;
            }
            this.mantissa = create(z2, i3 + 2);
            System.arraycopy(cArr, 0, this.mantissa, i7, i3);
            this.mantissa[i7 + i3] = '.';
            this.mantissa[i7 + i3 + 1] = '0';
            return;
        }
        if (i4 <= 0 && i4 > -3) {
            int iMax = Math.max(0, Math.min(-i4, i2));
            int iMax2 = Math.max(0, Math.min(i3, i2 + i4));
            if (iMax > 0) {
                this.mantissa = create(z2, iMax + 2 + iMax2);
                this.mantissa[i7] = '0';
                this.mantissa[i7 + 1] = '.';
                Arrays.fill(this.mantissa, i7 + 2, i7 + 2 + iMax, '0');
                if (iMax2 > 0) {
                    System.arraycopy(cArr, 0, this.mantissa, i7 + 2 + iMax, iMax2);
                    return;
                }
                return;
            }
            if (iMax2 > 0) {
                this.mantissa = create(z2, iMax + 2 + iMax2);
                this.mantissa[i7] = '0';
                this.mantissa[i7 + 1] = '.';
                System.arraycopy(cArr, 0, this.mantissa, i7 + 2, iMax2);
                return;
            }
            this.mantissa = create(z2, 1);
            this.mantissa[i7] = '0';
            return;
        }
        if (i3 > 1) {
            this.mantissa = create(z2, i3 + 1);
            this.mantissa[i7] = cArr[0];
            this.mantissa[i7 + 1] = '.';
            System.arraycopy(cArr, 1, this.mantissa, i7 + 2, i3 - 1);
        } else {
            this.mantissa = create(z2, 3);
            this.mantissa[i7] = cArr[0];
            this.mantissa[i7 + 1] = '.';
            this.mantissa[i7 + 2] = '0';
        }
        boolean z3 = i4 <= 0;
        if (z3) {
            i5 = (-i4) + 1;
            i6 = 1;
        } else {
            i5 = i4 - 1;
            i6 = 0;
        }
        if (i5 <= 9) {
            this.exponent = create(z3, 1);
            this.exponent[i6] = (char) (i5 + 48);
        } else if (i5 <= 99) {
            this.exponent = create(z3, 2);
            this.exponent[i6] = (char) ((i5 / 10) + 48);
            this.exponent[i6 + 1] = (char) ((i5 % 10) + 48);
        } else {
            this.exponent = create(z3, 3);
            this.exponent[i6] = (char) ((i5 / 100) + 48);
            int i9 = i5 % 100;
            this.exponent[i6 + 1] = (char) ((i9 / 10) + 48);
            this.exponent[i6 + 2] = (char) ((i9 % 10) + 48);
        }
    }

    private static char[] create(boolean z2, int i2) {
        if (z2) {
            char[] cArr = new char[i2 + 1];
            cArr[0] = '-';
            return cArr;
        }
        return new char[i2];
    }

    private void fillDecimal(int i2, char[] cArr, int i3, int i4, boolean z2) {
        int i5 = z2 ? 1 : 0;
        if (i4 > 0) {
            if (i3 < i4) {
                this.mantissa = create(z2, i4);
                System.arraycopy(cArr, 0, this.mantissa, i5, i3);
                Arrays.fill(this.mantissa, i5 + i3, i5 + i4, '0');
                return;
            }
            int iMin = Math.min(i3 - i4, i2);
            this.mantissa = create(z2, i4 + (iMin > 0 ? iMin + 1 : 0));
            System.arraycopy(cArr, 0, this.mantissa, i5, i4);
            if (iMin > 0) {
                this.mantissa[i5 + i4] = '.';
                System.arraycopy(cArr, i4, this.mantissa, i5 + i4 + 1, iMin);
                return;
            }
            return;
        }
        if (i4 <= 0) {
            int iMax = Math.max(0, Math.min(-i4, i2));
            int iMax2 = Math.max(0, Math.min(i3, i2 + i4));
            if (iMax > 0) {
                this.mantissa = create(z2, iMax + 2 + iMax2);
                this.mantissa[i5] = '0';
                this.mantissa[i5 + 1] = '.';
                Arrays.fill(this.mantissa, i5 + 2, i5 + 2 + iMax, '0');
                if (iMax2 > 0) {
                    System.arraycopy(cArr, 0, this.mantissa, i5 + 2 + iMax, iMax2);
                    return;
                }
                return;
            }
            if (iMax2 > 0) {
                this.mantissa = create(z2, iMax + 2 + iMax2);
                this.mantissa[i5] = '0';
                this.mantissa[i5 + 1] = '.';
                System.arraycopy(cArr, 0, this.mantissa, i5 + 2, iMax2);
                return;
            }
            this.mantissa = create(z2, 1);
            this.mantissa[i5] = '0';
        }
    }

    private void fillScientific(int i2, char[] cArr, int i3, int i4, boolean z2) {
        char c2;
        int i5;
        int i6 = z2 ? 1 : 0;
        int iMax = Math.max(0, Math.min(i3 - 1, i2));
        if (iMax > 0) {
            this.mantissa = create(z2, iMax + 2);
            this.mantissa[i6] = cArr[0];
            this.mantissa[i6 + 1] = '.';
            System.arraycopy(cArr, 1, this.mantissa, i6 + 2, iMax);
        } else {
            this.mantissa = create(z2, 1);
            this.mantissa[i6] = cArr[0];
        }
        if (i4 <= 0) {
            c2 = '-';
            i5 = (-i4) + 1;
        } else {
            c2 = '+';
            i5 = i4 - 1;
        }
        if (i5 <= 9) {
            this.exponent = new char[]{c2, '0', (char) (i5 + 48)};
        } else if (i5 <= 99) {
            this.exponent = new char[]{c2, (char) ((i5 / 10) + 48), (char) ((i5 % 10) + 48)};
        } else {
            int i7 = i5 % 100;
            this.exponent = new char[]{c2, (char) ((i5 / 100) + 48), (char) ((i7 / 10) + 48), (char) ((i7 % 10) + 48)};
        }
    }
}
