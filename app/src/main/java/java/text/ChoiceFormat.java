package java.text;

import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.util.Arrays;
import sun.misc.DoubleConsts;

/* loaded from: rt.jar:java/text/ChoiceFormat.class */
public class ChoiceFormat extends NumberFormat {
    private static final long serialVersionUID = 1795184449645032964L;
    private double[] choiceLimits;
    private String[] choiceFormats;
    static final long SIGN = Long.MIN_VALUE;
    static final long EXPONENT = 9218868437227405312L;
    static final long POSITIVEINFINITY = 9218868437227405312L;

    public void applyPattern(String str) {
        StringBuffer[] stringBufferArr = new StringBuffer[2];
        for (int i2 = 0; i2 < stringBufferArr.length; i2++) {
            stringBufferArr[i2] = new StringBuffer();
        }
        double[] dArrDoubleArraySize = new double[30];
        String[] strArrDoubleArraySize = new String[30];
        int i3 = 0;
        boolean z2 = false;
        double dDoubleValue = 0.0d;
        double d2 = Double.NaN;
        boolean z3 = false;
        int i4 = 0;
        while (i4 < str.length()) {
            char cCharAt = str.charAt(i4);
            if (cCharAt == '\'') {
                if (i4 + 1 < str.length() && str.charAt(i4 + 1) == cCharAt) {
                    stringBufferArr[z2 ? 1 : 0].append(cCharAt);
                    i4++;
                } else {
                    z3 = !z3;
                }
            } else if (z3) {
                stringBufferArr[z2 ? 1 : 0].append(cCharAt);
            } else if (cCharAt == '<' || cCharAt == '#' || cCharAt == 8804) {
                if (stringBufferArr[0].length() == 0) {
                    throw new IllegalArgumentException();
                }
                try {
                    String string = stringBufferArr[0].toString();
                    if (string.equals("∞")) {
                        dDoubleValue = Double.POSITIVE_INFINITY;
                    } else if (string.equals("-∞")) {
                        dDoubleValue = Double.NEGATIVE_INFINITY;
                    } else {
                        dDoubleValue = Double.valueOf(stringBufferArr[0].toString()).doubleValue();
                    }
                    if (cCharAt == '<' && dDoubleValue != Double.POSITIVE_INFINITY && dDoubleValue != Double.NEGATIVE_INFINITY) {
                        dDoubleValue = nextDouble(dDoubleValue);
                    }
                    if (dDoubleValue <= d2) {
                        throw new IllegalArgumentException();
                    }
                    stringBufferArr[0].setLength(0);
                    z2 = true;
                } catch (Exception e2) {
                    throw new IllegalArgumentException();
                }
            } else if (cCharAt == '|') {
                if (i3 == dArrDoubleArraySize.length) {
                    dArrDoubleArraySize = doubleArraySize(dArrDoubleArraySize);
                    strArrDoubleArraySize = doubleArraySize(strArrDoubleArraySize);
                }
                dArrDoubleArraySize[i3] = dDoubleValue;
                strArrDoubleArraySize[i3] = stringBufferArr[1].toString();
                i3++;
                d2 = dDoubleValue;
                stringBufferArr[1].setLength(0);
                z2 = false;
            } else {
                stringBufferArr[z2 ? 1 : 0].append(cCharAt);
            }
            i4++;
            z2 = z2;
        }
        if (z2) {
            if (i3 == dArrDoubleArraySize.length) {
                dArrDoubleArraySize = doubleArraySize(dArrDoubleArraySize);
                strArrDoubleArraySize = doubleArraySize(strArrDoubleArraySize);
            }
            dArrDoubleArraySize[i3] = dDoubleValue;
            strArrDoubleArraySize[i3] = stringBufferArr[1].toString();
            i3++;
        }
        this.choiceLimits = new double[i3];
        System.arraycopy(dArrDoubleArraySize, 0, this.choiceLimits, 0, i3);
        this.choiceFormats = new String[i3];
        System.arraycopy(strArrDoubleArraySize, 0, this.choiceFormats, 0, i3);
    }

    public String toPattern() {
        StringBuffer stringBuffer = new StringBuffer();
        for (int i2 = 0; i2 < this.choiceLimits.length; i2++) {
            if (i2 != 0) {
                stringBuffer.append('|');
            }
            double dPreviousDouble = previousDouble(this.choiceLimits[i2]);
            if (Math.abs(Math.IEEEremainder(this.choiceLimits[i2], 1.0d)) < Math.abs(Math.IEEEremainder(dPreviousDouble, 1.0d))) {
                stringBuffer.append("" + this.choiceLimits[i2]);
                stringBuffer.append('#');
            } else {
                if (this.choiceLimits[i2] == Double.POSITIVE_INFINITY) {
                    stringBuffer.append("∞");
                } else if (this.choiceLimits[i2] == Double.NEGATIVE_INFINITY) {
                    stringBuffer.append("-∞");
                } else {
                    stringBuffer.append("" + dPreviousDouble);
                }
                stringBuffer.append('<');
            }
            String str = this.choiceFormats[i2];
            boolean z2 = str.indexOf(60) >= 0 || str.indexOf(35) >= 0 || str.indexOf(8804) >= 0 || str.indexOf(124) >= 0;
            if (z2) {
                stringBuffer.append('\'');
            }
            if (str.indexOf(39) < 0) {
                stringBuffer.append(str);
            } else {
                for (int i3 = 0; i3 < str.length(); i3++) {
                    char cCharAt = str.charAt(i3);
                    stringBuffer.append(cCharAt);
                    if (cCharAt == '\'') {
                        stringBuffer.append(cCharAt);
                    }
                }
            }
            if (z2) {
                stringBuffer.append('\'');
            }
        }
        return stringBuffer.toString();
    }

    public ChoiceFormat(String str) {
        applyPattern(str);
    }

    public ChoiceFormat(double[] dArr, String[] strArr) {
        setChoices(dArr, strArr);
    }

    public void setChoices(double[] dArr, String[] strArr) {
        if (dArr.length != strArr.length) {
            throw new IllegalArgumentException("Array and limit arrays must be of the same length.");
        }
        this.choiceLimits = Arrays.copyOf(dArr, dArr.length);
        this.choiceFormats = (String[]) Arrays.copyOf(strArr, strArr.length);
    }

    public double[] getLimits() {
        return Arrays.copyOf(this.choiceLimits, this.choiceLimits.length);
    }

    public Object[] getFormats() {
        return Arrays.copyOf(this.choiceFormats, this.choiceFormats.length);
    }

    @Override // java.text.NumberFormat
    public StringBuffer format(long j2, StringBuffer stringBuffer, FieldPosition fieldPosition) {
        return format(j2, stringBuffer, fieldPosition);
    }

    @Override // java.text.NumberFormat
    public StringBuffer format(double d2, StringBuffer stringBuffer, FieldPosition fieldPosition) {
        int i2 = 0;
        while (i2 < this.choiceLimits.length && d2 >= this.choiceLimits[i2]) {
            i2++;
        }
        int i3 = i2 - 1;
        if (i3 < 0) {
            i3 = 0;
        }
        return stringBuffer.append(this.choiceFormats[i3]);
    }

    @Override // java.text.NumberFormat
    public Number parse(String str, ParsePosition parsePosition) {
        int i2 = parsePosition.index;
        int i3 = i2;
        double d2 = Double.NaN;
        for (int i4 = 0; i4 < this.choiceFormats.length; i4++) {
            String str2 = this.choiceFormats[i4];
            if (str.regionMatches(i2, str2, 0, str2.length())) {
                parsePosition.index = i2 + str2.length();
                double d3 = this.choiceLimits[i4];
                if (parsePosition.index > i3) {
                    i3 = parsePosition.index;
                    d2 = d3;
                    if (i3 == str.length()) {
                        break;
                    }
                } else {
                    continue;
                }
            }
        }
        parsePosition.index = i3;
        if (parsePosition.index == i2) {
            parsePosition.errorIndex = i3;
        }
        return new Double(d2);
    }

    public static final double nextDouble(double d2) {
        return nextDouble(d2, true);
    }

    public static final double previousDouble(double d2) {
        return nextDouble(d2, false);
    }

    @Override // java.text.NumberFormat, java.text.Format
    public Object clone() {
        ChoiceFormat choiceFormat = (ChoiceFormat) super.clone();
        choiceFormat.choiceLimits = (double[]) this.choiceLimits.clone();
        choiceFormat.choiceFormats = (String[]) this.choiceFormats.clone();
        return choiceFormat;
    }

    @Override // java.text.NumberFormat
    public int hashCode() {
        int length = this.choiceLimits.length;
        if (this.choiceFormats.length > 0) {
            length ^= this.choiceFormats[this.choiceFormats.length - 1].hashCode();
        }
        return length;
    }

    @Override // java.text.NumberFormat
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        ChoiceFormat choiceFormat = (ChoiceFormat) obj;
        return Arrays.equals(this.choiceLimits, choiceFormat.choiceLimits) && Arrays.equals(this.choiceFormats, choiceFormat.choiceFormats);
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        if (this.choiceLimits.length != this.choiceFormats.length) {
            throw new InvalidObjectException("limits and format arrays of different length.");
        }
    }

    public static double nextDouble(double d2, boolean z2) {
        if (Double.isNaN(d2)) {
            return d2;
        }
        if (d2 == 0.0d) {
            double dLongBitsToDouble = Double.longBitsToDouble(1L);
            if (z2) {
                return dLongBitsToDouble;
            }
            return -dLongBitsToDouble;
        }
        long jDoubleToLongBits = Double.doubleToLongBits(d2);
        long j2 = jDoubleToLongBits & Long.MAX_VALUE;
        if ((jDoubleToLongBits > 0) != z2) {
            j2--;
        } else if (j2 != DoubleConsts.EXP_BIT_MASK) {
            j2++;
        }
        return Double.longBitsToDouble(j2 | (jDoubleToLongBits & Long.MIN_VALUE));
    }

    private static double[] doubleArraySize(double[] dArr) {
        int length = dArr.length;
        double[] dArr2 = new double[length * 2];
        System.arraycopy(dArr, 0, dArr2, 0, length);
        return dArr2;
    }

    private String[] doubleArraySize(String[] strArr) {
        int length = strArr.length;
        String[] strArr2 = new String[length * 2];
        System.arraycopy(strArr, 0, strArr2, 0, length);
        return strArr2;
    }
}
