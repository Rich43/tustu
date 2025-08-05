package sun.security.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Date;
import java.util.TimeZone;
import sun.util.calendar.CalendarDate;
import sun.util.calendar.CalendarSystem;
import sun.util.calendar.Gregorian;

/* loaded from: rt.jar:sun/security/util/DerInputBuffer.class */
class DerInputBuffer extends ByteArrayInputStream implements Cloneable {
    boolean allowBER;

    DerInputBuffer(byte[] bArr) {
        this(bArr, true);
    }

    DerInputBuffer(byte[] bArr, boolean z2) {
        super(bArr);
        this.allowBER = true;
        this.allowBER = z2;
    }

    DerInputBuffer(byte[] bArr, int i2, int i3, boolean z2) {
        super(bArr, i2, i3);
        this.allowBER = true;
        this.allowBER = z2;
    }

    DerInputBuffer dup() {
        try {
            DerInputBuffer derInputBuffer = (DerInputBuffer) clone();
            derInputBuffer.mark(Integer.MAX_VALUE);
            return derInputBuffer;
        } catch (CloneNotSupportedException e2) {
            throw new IllegalArgumentException(e2.toString());
        }
    }

    byte[] toByteArray() {
        int iAvailable = available();
        if (iAvailable <= 0) {
            return null;
        }
        byte[] bArr = new byte[iAvailable];
        System.arraycopy(this.buf, this.pos, bArr, 0, iAvailable);
        return bArr;
    }

    int peek() throws IOException {
        if (this.pos >= this.count) {
            throw new IOException("out of data");
        }
        return this.buf[this.pos];
    }

    public boolean equals(Object obj) {
        if (obj instanceof DerInputBuffer) {
            return equals((DerInputBuffer) obj);
        }
        return false;
    }

    boolean equals(DerInputBuffer derInputBuffer) {
        if (this == derInputBuffer) {
            return true;
        }
        int iAvailable = available();
        if (derInputBuffer.available() != iAvailable) {
            return false;
        }
        for (int i2 = 0; i2 < iAvailable; i2++) {
            if (this.buf[this.pos + i2] != derInputBuffer.buf[derInputBuffer.pos + i2]) {
                return false;
            }
        }
        return true;
    }

    public int hashCode() {
        int i2 = 0;
        int iAvailable = available();
        int i3 = this.pos;
        for (int i4 = 0; i4 < iAvailable; i4++) {
            i2 += this.buf[i3 + i4] * i4;
        }
        return i2;
    }

    void truncate(int i2) throws IOException {
        if (i2 > available()) {
            throw new IOException("insufficient data");
        }
        this.count = this.pos + i2;
    }

    BigInteger getBigInteger(int i2, boolean z2) throws IOException {
        if (i2 > available()) {
            throw new IOException("short read of integer");
        }
        if (i2 == 0) {
            throw new IOException("Invalid encoding: zero length Int value");
        }
        byte[] bArr = new byte[i2];
        System.arraycopy(this.buf, this.pos, bArr, 0, i2);
        skip(i2);
        if (!this.allowBER && i2 >= 2 && bArr[0] == 0 && bArr[1] >= 0) {
            throw new IOException("Invalid encoding: redundant leading 0s");
        }
        if (z2) {
            return new BigInteger(1, bArr);
        }
        return new BigInteger(bArr);
    }

    public int getInteger(int i2) throws IOException {
        BigInteger bigInteger = getBigInteger(i2, false);
        if (bigInteger.compareTo(BigInteger.valueOf(-2147483648L)) < 0) {
            throw new IOException("Integer below minimum valid value");
        }
        if (bigInteger.compareTo(BigInteger.valueOf(2147483647L)) > 0) {
            throw new IOException("Integer exceeds maximum valid value");
        }
        return bigInteger.intValue();
    }

    private static int checkPaddedBits(int i2, byte[] bArr, int i3, int i4, boolean z2) throws IOException {
        if (i2 < 0 || i2 > 7) {
            throw new IOException("Invalid number of padding bits");
        }
        int i5 = ((i4 - i3) << 3) - i2;
        if (i5 < 0) {
            throw new IOException("Not enough bytes in BitString");
        }
        if (!z2 && i2 != 0 && (bArr[i4 - 1] & (255 >>> (8 - i2))) != 0) {
            throw new IOException("Invalid value of padding bits");
        }
        return i5;
    }

    public byte[] getBitString(int i2) throws IOException {
        if (i2 > available()) {
            throw new IOException("short read of bit string");
        }
        if (i2 == 0) {
            throw new IOException("Invalid encoding: zero length bit string");
        }
        int i3 = this.pos;
        int i4 = i3 + i2;
        skip(i2);
        int i5 = i3 + 1;
        byte b2 = this.buf[i3];
        checkPaddedBits(b2, this.buf, i5, i4, this.allowBER);
        byte[] bArr = new byte[i2 - 1];
        System.arraycopy(this.buf, i5, bArr, 0, i2 - 1);
        if (this.allowBER && b2 != 0) {
            int length = bArr.length - 1;
            bArr[length] = (byte) (bArr[length] & (255 << b2));
        }
        return bArr;
    }

    byte[] getBitString() throws IOException {
        return getBitString(available());
    }

    BitArray getUnalignedBitString() throws IOException {
        return getUnalignedBitString(available());
    }

    BitArray getUnalignedBitString(int i2) throws IOException {
        if (i2 > available()) {
            throw new IOException("short read of bit string");
        }
        if (i2 == 0) {
            throw new IOException("Invalid encoding: zero length bit string");
        }
        if (this.pos >= this.count) {
            return null;
        }
        int i3 = this.pos;
        int i4 = i3 + i2;
        this.pos = this.count;
        int i5 = i3 + 1;
        return new BitArray(checkPaddedBits(this.buf[i3], this.buf, i5, i4, this.allowBER), this.buf, i5);
    }

    public Date getUTCTime(int i2) throws IOException {
        if (i2 > available()) {
            throw new IOException("short read of DER UTC Time");
        }
        if (i2 < 11 || i2 > 17) {
            throw new IOException("DER UTC Time length error");
        }
        return getTime(i2, false);
    }

    public Date getGeneralizedTime(int i2) throws IOException {
        if (i2 > available()) {
            throw new IOException("short read of DER Generalized Time");
        }
        if (i2 < 13) {
            throw new IOException("DER Generalized Time length error");
        }
        return getTime(i2, true);
    }

    private Date getTime(int i2, boolean z2) throws IOException {
        String str;
        int digit;
        int digit2;
        if (z2) {
            str = "Generalized";
            byte[] bArr = this.buf;
            int i3 = this.pos;
            this.pos = i3 + 1;
            int digit3 = 1000 * toDigit(bArr[i3], str);
            byte[] bArr2 = this.buf;
            int i4 = this.pos;
            this.pos = i4 + 1;
            int digit4 = digit3 + (100 * toDigit(bArr2[i4], str));
            byte[] bArr3 = this.buf;
            int i5 = this.pos;
            this.pos = i5 + 1;
            int digit5 = digit4 + (10 * toDigit(bArr3[i5], str));
            byte[] bArr4 = this.buf;
            int i6 = this.pos;
            this.pos = i6 + 1;
            digit = digit5 + toDigit(bArr4[i6], str);
            i2 -= 2;
        } else {
            str = "UTC";
            byte[] bArr5 = this.buf;
            int i7 = this.pos;
            this.pos = i7 + 1;
            int digit6 = 10 * toDigit(bArr5[i7], str);
            byte[] bArr6 = this.buf;
            int i8 = this.pos;
            this.pos = i8 + 1;
            int digit7 = digit6 + toDigit(bArr6[i8], str);
            if (digit7 < 50) {
                digit = digit7 + 2000;
            } else {
                digit = digit7 + 1900;
            }
        }
        byte[] bArr7 = this.buf;
        int i9 = this.pos;
        this.pos = i9 + 1;
        int digit8 = 10 * toDigit(bArr7[i9], str);
        byte[] bArr8 = this.buf;
        int i10 = this.pos;
        this.pos = i10 + 1;
        int digit9 = digit8 + toDigit(bArr8[i10], str);
        byte[] bArr9 = this.buf;
        int i11 = this.pos;
        this.pos = i11 + 1;
        int digit10 = 10 * toDigit(bArr9[i11], str);
        byte[] bArr10 = this.buf;
        int i12 = this.pos;
        this.pos = i12 + 1;
        int digit11 = digit10 + toDigit(bArr10[i12], str);
        byte[] bArr11 = this.buf;
        int i13 = this.pos;
        this.pos = i13 + 1;
        int digit12 = 10 * toDigit(bArr11[i13], str);
        byte[] bArr12 = this.buf;
        int i14 = this.pos;
        this.pos = i14 + 1;
        int digit13 = digit12 + toDigit(bArr12[i14], str);
        byte[] bArr13 = this.buf;
        int i15 = this.pos;
        this.pos = i15 + 1;
        int digit14 = 10 * toDigit(bArr13[i15], str);
        byte[] bArr14 = this.buf;
        int i16 = this.pos;
        this.pos = i16 + 1;
        int digit15 = digit14 + toDigit(bArr14[i16], str);
        int i17 = i2 - 10;
        int i18 = 0;
        if (i17 > 2) {
            byte[] bArr15 = this.buf;
            int i19 = this.pos;
            this.pos = i19 + 1;
            int digit16 = 10 * toDigit(bArr15[i19], str);
            byte[] bArr16 = this.buf;
            int i20 = this.pos;
            this.pos = i20 + 1;
            digit2 = digit16 + toDigit(bArr16[i20], str);
            i17 -= 2;
            if (z2 && (this.buf[this.pos] == 46 || this.buf[this.pos] == 44)) {
                i17--;
                if (i17 == 0) {
                    throw new IOException("Parse " + str + " time, empty fractional part");
                }
                this.pos++;
                int i21 = 0;
                while (this.buf[this.pos] != 90 && this.buf[this.pos] != 43 && this.buf[this.pos] != 45) {
                    int digit17 = toDigit(this.buf[this.pos], str);
                    i21++;
                    i17--;
                    if (i17 == 0) {
                        throw new IOException("Parse " + str + " time, invalid fractional part");
                    }
                    this.pos++;
                    switch (i21) {
                        case 1:
                            i18 += 100 * digit17;
                            break;
                        case 2:
                            i18 += 10 * digit17;
                            break;
                        case 3:
                            i18 += digit17;
                            break;
                    }
                }
                if (i21 == 0) {
                    throw new IOException("Parse " + str + " time, empty fractional part");
                }
            }
        } else {
            digit2 = 0;
        }
        if (digit9 == 0 || digit11 == 0 || digit9 > 12 || digit11 > 31 || digit13 >= 24 || digit15 >= 60 || digit2 >= 60) {
            throw new IOException("Parse " + str + " time, invalid format");
        }
        Gregorian gregorianCalendar = CalendarSystem.getGregorianCalendar();
        CalendarDate calendarDateNewCalendarDate = gregorianCalendar.newCalendarDate((TimeZone) null);
        calendarDateNewCalendarDate.setDate(digit, digit9, digit11);
        calendarDateNewCalendarDate.setTimeOfDay(digit13, digit15, digit2, i18);
        long time = gregorianCalendar.getTime(calendarDateNewCalendarDate);
        if (i17 != 1 && i17 != 5) {
            throw new IOException("Parse " + str + " time, invalid offset");
        }
        byte[] bArr17 = this.buf;
        int i22 = this.pos;
        this.pos = i22 + 1;
        switch (bArr17[i22]) {
            case 43:
                if (i17 != 5) {
                    throw new IOException("Parse " + str + " time, invalid offset");
                }
                byte[] bArr18 = this.buf;
                int i23 = this.pos;
                this.pos = i23 + 1;
                int digit18 = 10 * toDigit(bArr18[i23], str);
                byte[] bArr19 = this.buf;
                int i24 = this.pos;
                this.pos = i24 + 1;
                int digit19 = digit18 + toDigit(bArr19[i24], str);
                byte[] bArr20 = this.buf;
                int i25 = this.pos;
                this.pos = i25 + 1;
                int digit20 = 10 * toDigit(bArr20[i25], str);
                byte[] bArr21 = this.buf;
                int i26 = this.pos;
                this.pos = i26 + 1;
                int digit21 = digit20 + toDigit(bArr21[i26], str);
                if (digit19 >= 24 || digit21 >= 60) {
                    throw new IOException("Parse " + str + " time, +hhmm");
                }
                time -= (((digit19 * 60) + digit21) * 60) * 1000;
                break;
                break;
            case 45:
                if (i17 != 5) {
                    throw new IOException("Parse " + str + " time, invalid offset");
                }
                byte[] bArr22 = this.buf;
                int i27 = this.pos;
                this.pos = i27 + 1;
                int digit22 = 10 * toDigit(bArr22[i27], str);
                byte[] bArr23 = this.buf;
                int i28 = this.pos;
                this.pos = i28 + 1;
                int digit23 = digit22 + toDigit(bArr23[i28], str);
                byte[] bArr24 = this.buf;
                int i29 = this.pos;
                this.pos = i29 + 1;
                int digit24 = 10 * toDigit(bArr24[i29], str);
                byte[] bArr25 = this.buf;
                int i30 = this.pos;
                this.pos = i30 + 1;
                int digit25 = digit24 + toDigit(bArr25[i30], str);
                if (digit23 >= 24 || digit25 >= 60) {
                    throw new IOException("Parse " + str + " time, -hhmm");
                }
                time += ((digit23 * 60) + digit25) * 60 * 1000;
                break;
                break;
            case 90:
                if (i17 != 1) {
                    throw new IOException("Parse " + str + " time, invalid format");
                }
                break;
            default:
                throw new IOException("Parse " + str + " time, garbage offset");
        }
        return new Date(time);
    }

    private static int toDigit(byte b2, String str) throws IOException {
        if (b2 < 48 || b2 > 57) {
            throw new IOException("Parse " + str + " time, invalid format");
        }
        return b2 - 48;
    }
}
