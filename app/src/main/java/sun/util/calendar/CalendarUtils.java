package sun.util.calendar;

/* loaded from: rt.jar:sun/util/calendar/CalendarUtils.class */
public class CalendarUtils {
    public static final boolean isGregorianLeapYear(int i2) {
        return i2 % 4 == 0 && (i2 % 100 != 0 || i2 % 400 == 0);
    }

    public static final boolean isJulianLeapYear(int i2) {
        return i2 % 4 == 0;
    }

    public static final long floorDivide(long j2, long j3) {
        return j2 >= 0 ? j2 / j3 : ((j2 + 1) / j3) - 1;
    }

    public static final int floorDivide(int i2, int i3) {
        return i2 >= 0 ? i2 / i3 : ((i2 + 1) / i3) - 1;
    }

    public static final int floorDivide(int i2, int i3, int[] iArr) {
        if (i2 >= 0) {
            iArr[0] = i2 % i3;
            return i2 / i3;
        }
        int i4 = ((i2 + 1) / i3) - 1;
        iArr[0] = i2 - (i4 * i3);
        return i4;
    }

    public static final int floorDivide(long j2, int i2, int[] iArr) {
        if (j2 >= 0) {
            iArr[0] = (int) (j2 % i2);
            return (int) (j2 / i2);
        }
        int i3 = (int) (((j2 + 1) / i2) - 1);
        iArr[0] = (int) (j2 - (i3 * i2));
        return i3;
    }

    public static final long mod(long j2, long j3) {
        return j2 - (j3 * floorDivide(j2, j3));
    }

    public static final int mod(int i2, int i3) {
        return i2 - (i3 * floorDivide(i2, i3));
    }

    public static final int amod(int i2, int i3) {
        int iMod = mod(i2, i3);
        return iMod == 0 ? i3 : iMod;
    }

    public static final long amod(long j2, long j3) {
        long jMod = mod(j2, j3);
        return jMod == 0 ? j3 : jMod;
    }

    public static final StringBuilder sprintf0d(StringBuilder sb, int i2, int i3) {
        long j2 = i2;
        if (j2 < 0) {
            sb.append('-');
            j2 = -j2;
            i3--;
        }
        int i4 = 10;
        for (int i5 = 2; i5 < i3; i5++) {
            i4 *= 10;
        }
        for (int i6 = 1; i6 < i3 && j2 < i4; i6++) {
            sb.append('0');
            i4 /= 10;
        }
        sb.append(j2);
        return sb;
    }

    public static final StringBuffer sprintf0d(StringBuffer stringBuffer, int i2, int i3) {
        long j2 = i2;
        if (j2 < 0) {
            stringBuffer.append('-');
            j2 = -j2;
            i3--;
        }
        int i4 = 10;
        for (int i5 = 2; i5 < i3; i5++) {
            i4 *= 10;
        }
        for (int i6 = 1; i6 < i3 && j2 < i4; i6++) {
            stringBuffer.append('0');
            i4 /= 10;
        }
        stringBuffer.append(j2);
        return stringBuffer;
    }
}
