package java.text;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import sun.misc.FloatingDecimal;

/* loaded from: rt.jar:java/text/DigitList.class */
final class DigitList implements Cloneable {
    public static final int MAX_COUNT = 19;
    private char[] data;
    private static final char[] LONG_MIN_REP;
    private StringBuffer tempBuffer;
    static final /* synthetic */ boolean $assertionsDisabled;
    public int decimalAt = 0;
    public int count = 0;
    public char[] digits = new char[19];
    private RoundingMode roundingMode = RoundingMode.HALF_EVEN;
    private boolean isNegative = false;

    DigitList() {
    }

    static {
        $assertionsDisabled = !DigitList.class.desiredAssertionStatus();
        LONG_MIN_REP = "9223372036854775808".toCharArray();
    }

    boolean isZero() {
        for (int i2 = 0; i2 < this.count; i2++) {
            if (this.digits[i2] != '0') {
                return false;
            }
        }
        return true;
    }

    void setRoundingMode(RoundingMode roundingMode) {
        this.roundingMode = roundingMode;
    }

    public void clear() {
        this.decimalAt = 0;
        this.count = 0;
    }

    public void append(char c2) {
        if (this.count == this.digits.length) {
            char[] cArr = new char[this.count + 100];
            System.arraycopy(this.digits, 0, cArr, 0, this.count);
            this.digits = cArr;
        }
        char[] cArr2 = this.digits;
        int i2 = this.count;
        this.count = i2 + 1;
        cArr2[i2] = c2;
    }

    public final double getDouble() {
        if (this.count == 0) {
            return 0.0d;
        }
        StringBuffer stringBuffer = getStringBuffer();
        stringBuffer.append('.');
        stringBuffer.append(this.digits, 0, this.count);
        stringBuffer.append('E');
        stringBuffer.append(this.decimalAt);
        return Double.parseDouble(stringBuffer.toString());
    }

    public final long getLong() {
        if (this.count == 0) {
            return 0L;
        }
        if (isLongMIN_VALUE()) {
            return Long.MIN_VALUE;
        }
        StringBuffer stringBuffer = getStringBuffer();
        stringBuffer.append(this.digits, 0, this.count);
        for (int i2 = this.count; i2 < this.decimalAt; i2++) {
            stringBuffer.append('0');
        }
        return Long.parseLong(stringBuffer.toString());
    }

    public final BigDecimal getBigDecimal() {
        if (this.count == 0) {
            if (this.decimalAt == 0) {
                return BigDecimal.ZERO;
            }
            return new BigDecimal("0E" + this.decimalAt);
        }
        if (this.decimalAt == this.count) {
            return new BigDecimal(this.digits, 0, this.count);
        }
        return new BigDecimal(this.digits, 0, this.count).scaleByPowerOfTen(this.decimalAt - this.count);
    }

    boolean fitsIntoLong(boolean z2, boolean z3) {
        while (this.count > 0 && this.digits[this.count - 1] == '0') {
            this.count--;
        }
        if (this.count == 0) {
            return z2 || z3;
        }
        if (this.decimalAt < this.count || this.decimalAt > 19) {
            return false;
        }
        if (this.decimalAt < 19) {
            return true;
        }
        for (int i2 = 0; i2 < this.count; i2++) {
            char c2 = this.digits[i2];
            char c3 = LONG_MIN_REP[i2];
            if (c2 > c3) {
                return false;
            }
            if (c2 < c3) {
                return true;
            }
        }
        return this.count < this.decimalAt || !z2;
    }

    final void set(boolean z2, double d2, int i2) {
        set(z2, d2, i2, true);
    }

    final void set(boolean z2, double d2, int i2, boolean z3) {
        FloatingDecimal.BinaryToASCIIConverter binaryToASCIIConverter = FloatingDecimal.getBinaryToASCIIConverter(d2);
        boolean zDigitsRoundedUp = binaryToASCIIConverter.digitsRoundedUp();
        boolean zDecimalDigitsExact = binaryToASCIIConverter.decimalDigitsExact();
        if (!$assertionsDisabled && binaryToASCIIConverter.isExceptional()) {
            throw new AssertionError();
        }
        set(z2, binaryToASCIIConverter.toJavaFormatString(), zDigitsRoundedUp, zDecimalDigitsExact, i2, z3);
    }

    private void set(boolean z2, String str, boolean z3, boolean z4, int i2, boolean z5) {
        this.isNegative = z2;
        int length = str.length();
        char[] dataChars = getDataChars(length);
        str.getChars(0, length, dataChars, 0);
        this.decimalAt = -1;
        this.count = 0;
        int i3 = 0;
        int i4 = 0;
        boolean z6 = false;
        int i5 = 0;
        while (i5 < length) {
            int i6 = i5;
            i5++;
            char c2 = dataChars[i6];
            if (c2 == '.') {
                this.decimalAt = this.count;
            } else {
                if (c2 == 'e' || c2 == 'E') {
                    i3 = parseInt(dataChars, i5, length);
                    break;
                }
                if (!z6) {
                    z6 = c2 != '0';
                    if (!z6 && this.decimalAt != -1) {
                        i4++;
                    }
                }
                if (z6) {
                    char[] cArr = this.digits;
                    int i7 = this.count;
                    this.count = i7 + 1;
                    cArr[i7] = c2;
                }
            }
        }
        if (this.decimalAt == -1) {
            this.decimalAt = this.count;
        }
        if (z6) {
            this.decimalAt += i3 - i4;
        }
        if (z5) {
            if ((-this.decimalAt) > i2) {
                this.count = 0;
                return;
            }
            if ((-this.decimalAt) == i2) {
                if (shouldRoundUp(0, z3, z4)) {
                    this.count = 1;
                    this.decimalAt++;
                    this.digits[0] = '1';
                    return;
                }
                this.count = 0;
                return;
            }
        }
        while (this.count > 1 && this.digits[this.count - 1] == '0') {
            this.count--;
        }
        round(z5 ? i2 + this.decimalAt : i2, z3, z4);
    }

    private final void round(int i2, boolean z2, boolean z3) {
        if (i2 >= 0 && i2 < this.count) {
            if (shouldRoundUp(i2, z2, z3)) {
                while (true) {
                    i2--;
                    if (i2 < 0) {
                        this.digits[0] = '1';
                        this.decimalAt++;
                        i2 = 0;
                        break;
                    } else {
                        char[] cArr = this.digits;
                        cArr[i2] = (char) (cArr[i2] + 1);
                        if (this.digits[i2] <= '9') {
                            break;
                        }
                    }
                }
                i2++;
            }
            this.count = i2;
            while (this.count > 1 && this.digits[this.count - 1] == '0') {
                this.count--;
            }
        }
    }

    private boolean shouldRoundUp(int i2, boolean z2, boolean z3) {
        if (i2 < this.count) {
            switch (this.roundingMode) {
                case UP:
                    for (int i3 = i2; i3 < this.count; i3++) {
                        if (this.digits[i3] != '0') {
                            return true;
                        }
                    }
                    return false;
                case DOWN:
                    return false;
                case CEILING:
                    for (int i4 = i2; i4 < this.count; i4++) {
                        if (this.digits[i4] != '0') {
                            return !this.isNegative;
                        }
                    }
                    return false;
                case FLOOR:
                    for (int i5 = i2; i5 < this.count; i5++) {
                        if (this.digits[i5] != '0') {
                            return this.isNegative;
                        }
                    }
                    return false;
                case HALF_UP:
                case HALF_DOWN:
                    if (this.digits[i2] > '5') {
                        return true;
                    }
                    if (this.digits[i2] == '5') {
                        if (i2 != this.count - 1) {
                            return true;
                        }
                        return z3 ? this.roundingMode == RoundingMode.HALF_UP : !z2;
                    }
                    return false;
                case HALF_EVEN:
                    if (this.digits[i2] > '5') {
                        return true;
                    }
                    if (this.digits[i2] == '5') {
                        if (i2 == this.count - 1) {
                            if (z2) {
                                return false;
                            }
                            if (z3) {
                                return i2 > 0 && this.digits[i2 - 1] % 2 != 0;
                            }
                            return true;
                        }
                        for (int i6 = i2 + 1; i6 < this.count; i6++) {
                            if (this.digits[i6] != '0') {
                                return true;
                            }
                        }
                        return false;
                    }
                    return false;
                case UNNECESSARY:
                    for (int i7 = i2; i7 < this.count; i7++) {
                        if (this.digits[i7] != '0') {
                            throw new ArithmeticException("Rounding needed with the rounding mode being set to RoundingMode.UNNECESSARY");
                        }
                    }
                    return false;
                default:
                    if ($assertionsDisabled) {
                        return false;
                    }
                    throw new AssertionError();
            }
        }
        return false;
    }

    final void set(boolean z2, long j2) {
        set(z2, j2, 0);
    }

    final void set(boolean z2, long j2, int i2) {
        this.isNegative = z2;
        if (j2 > 0) {
            int i3 = 19;
            while (j2 > 0) {
                i3--;
                this.digits[i3] = (char) (48 + (j2 % 10));
                j2 /= 10;
            }
            this.decimalAt = 19 - i3;
            int i4 = 18;
            while (this.digits[i4] == '0') {
                i4--;
            }
            this.count = (i4 - i3) + 1;
            System.arraycopy(this.digits, i3, this.digits, 0, this.count);
        } else if (j2 == Long.MIN_VALUE) {
            this.count = 19;
            this.decimalAt = 19;
            System.arraycopy(LONG_MIN_REP, 0, this.digits, 0, this.count);
        } else {
            this.count = 0;
            this.decimalAt = 0;
        }
        if (i2 > 0) {
            round(i2, false, true);
        }
    }

    final void set(boolean z2, BigDecimal bigDecimal, int i2, boolean z3) {
        String string = bigDecimal.toString();
        extendDigits(string.length());
        set(z2, string, false, true, i2, z3);
    }

    final void set(boolean z2, BigInteger bigInteger, int i2) {
        this.isNegative = z2;
        String string = bigInteger.toString();
        int length = string.length();
        extendDigits(length);
        string.getChars(0, length, this.digits, 0);
        this.decimalAt = length;
        int i3 = length - 1;
        while (i3 >= 0 && this.digits[i3] == '0') {
            i3--;
        }
        this.count = i3 + 1;
        if (i2 > 0) {
            round(i2, false, true);
        }
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof DigitList)) {
            return false;
        }
        DigitList digitList = (DigitList) obj;
        if (this.count != digitList.count || this.decimalAt != digitList.decimalAt) {
            return false;
        }
        for (int i2 = 0; i2 < this.count; i2++) {
            if (this.digits[i2] != digitList.digits[i2]) {
                return false;
            }
        }
        return true;
    }

    public int hashCode() {
        int i2 = this.decimalAt;
        for (int i3 = 0; i3 < this.count; i3++) {
            i2 = (i2 * 37) + this.digits[i3];
        }
        return i2;
    }

    public Object clone() {
        try {
            DigitList digitList = (DigitList) super.clone();
            char[] cArr = new char[this.digits.length];
            System.arraycopy(this.digits, 0, cArr, 0, this.digits.length);
            digitList.digits = cArr;
            digitList.tempBuffer = null;
            return digitList;
        } catch (CloneNotSupportedException e2) {
            throw new InternalError(e2);
        }
    }

    private boolean isLongMIN_VALUE() {
        if (this.decimalAt != this.count || this.count != 19) {
            return false;
        }
        for (int i2 = 0; i2 < this.count; i2++) {
            if (this.digits[i2] != LONG_MIN_REP[i2]) {
                return false;
            }
        }
        return true;
    }

    private static final int parseInt(char[] cArr, int i2, int i3) {
        int i4;
        boolean z2 = true;
        char c2 = cArr[i2];
        if (c2 == '-') {
            z2 = false;
            i2++;
        } else if (c2 == '+') {
            i2++;
        }
        int i5 = 0;
        while (true) {
            i4 = i5;
            if (i2 >= i3) {
                break;
            }
            int i6 = i2;
            i2++;
            char c3 = cArr[i6];
            if (c3 < '0' || c3 > '9') {
                break;
            }
            i5 = (i4 * 10) + (c3 - '0');
        }
        return z2 ? i4 : -i4;
    }

    public String toString() {
        if (isZero()) {
            return "0";
        }
        StringBuffer stringBuffer = getStringBuffer();
        stringBuffer.append("0.");
        stringBuffer.append(this.digits, 0, this.count);
        stringBuffer.append("x10^");
        stringBuffer.append(this.decimalAt);
        return stringBuffer.toString();
    }

    private StringBuffer getStringBuffer() {
        if (this.tempBuffer == null) {
            this.tempBuffer = new StringBuffer(19);
        } else {
            this.tempBuffer.setLength(0);
        }
        return this.tempBuffer;
    }

    private void extendDigits(int i2) {
        if (i2 > this.digits.length) {
            this.digits = new char[i2];
        }
    }

    private final char[] getDataChars(int i2) {
        if (this.data == null || this.data.length < i2) {
            this.data = new char[i2];
        }
        return this.data;
    }
}
