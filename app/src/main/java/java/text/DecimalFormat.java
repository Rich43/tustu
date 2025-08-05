package java.text;

import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.Format;
import java.text.NumberFormat;
import java.text.spi.NumberFormatProvider;
import java.util.ArrayList;
import java.util.Currency;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import sun.util.locale.LanguageTag;
import sun.util.locale.provider.LocaleProviderAdapter;
import sun.util.locale.provider.ResourceBundleBasedAdapter;

/* loaded from: rt.jar:java/text/DecimalFormat.class */
public class DecimalFormat extends NumberFormat {
    private transient BigInteger bigIntegerMultiplier;
    private transient BigDecimal bigDecimalMultiplier;
    private static final int STATUS_INFINITE = 0;
    private static final int STATUS_POSITIVE = 1;
    private static final int STATUS_LENGTH = 2;
    private String posPrefixPattern;
    private String posSuffixPattern;
    private String negPrefixPattern;
    private String negSuffixPattern;
    private DecimalFormatSymbols symbols;
    private boolean useExponentialNotation;
    private transient FieldPosition[] positivePrefixFieldPositions;
    private transient FieldPosition[] positiveSuffixFieldPositions;
    private transient FieldPosition[] negativePrefixFieldPositions;
    private transient FieldPosition[] negativeSuffixFieldPositions;
    private byte minExponentDigits;
    private transient FastPathData fastPathData;
    static final int currentSerialVersion = 4;
    private static final double MAX_INT_AS_DOUBLE = 2.147483647E9d;
    private static final char PATTERN_ZERO_DIGIT = '0';
    private static final char PATTERN_GROUPING_SEPARATOR = ',';
    private static final char PATTERN_DECIMAL_SEPARATOR = '.';
    private static final char PATTERN_PER_MILLE = 8240;
    private static final char PATTERN_PERCENT = '%';
    private static final char PATTERN_DIGIT = '#';
    private static final char PATTERN_SEPARATOR = ';';
    private static final String PATTERN_EXPONENT = "E";
    private static final char PATTERN_MINUS = '-';
    private static final char CURRENCY_SIGN = 164;
    private static final char QUOTE = '\'';
    private static FieldPosition[] EmptyFieldPositionArray;
    static final int DOUBLE_INTEGER_DIGITS = 309;
    static final int DOUBLE_FRACTION_DIGITS = 340;
    static final int MAXIMUM_INTEGER_DIGITS = Integer.MAX_VALUE;
    static final int MAXIMUM_FRACTION_DIGITS = Integer.MAX_VALUE;
    static final long serialVersionUID = 864413376551465018L;
    static final /* synthetic */ boolean $assertionsDisabled;
    private transient DigitList digitList = new DigitList();
    private String positivePrefix = "";
    private String positiveSuffix = "";
    private String negativePrefix = LanguageTag.SEP;
    private String negativeSuffix = "";
    private int multiplier = 1;
    private byte groupingSize = 3;
    private boolean decimalSeparatorAlwaysShown = false;
    private boolean parseBigDecimal = false;
    private transient boolean isCurrencyFormat = false;
    private int maximumIntegerDigits = super.getMaximumIntegerDigits();
    private int minimumIntegerDigits = super.getMinimumIntegerDigits();
    private int maximumFractionDigits = super.getMaximumFractionDigits();
    private int minimumFractionDigits = super.getMinimumFractionDigits();
    private RoundingMode roundingMode = RoundingMode.HALF_EVEN;
    private transient boolean isFastPath = false;
    private transient boolean fastPathCheckNeeded = true;
    private int serialVersionOnStream = 4;

    static {
        $assertionsDisabled = !DecimalFormat.class.desiredAssertionStatus();
        EmptyFieldPositionArray = new FieldPosition[0];
    }

    public DecimalFormat() {
        this.symbols = null;
        Locale locale = Locale.getDefault(Locale.Category.FORMAT);
        LocaleProviderAdapter adapter = LocaleProviderAdapter.getAdapter(NumberFormatProvider.class, locale);
        String[] numberPatterns = (adapter instanceof ResourceBundleBasedAdapter ? adapter : LocaleProviderAdapter.getResourceBundleBased()).getLocaleResources(locale).getNumberPatterns();
        this.symbols = DecimalFormatSymbols.getInstance(locale);
        applyPattern(numberPatterns[0], false);
    }

    public DecimalFormat(String str) {
        this.symbols = null;
        this.symbols = DecimalFormatSymbols.getInstance(Locale.getDefault(Locale.Category.FORMAT));
        applyPattern(str, false);
    }

    public DecimalFormat(String str, DecimalFormatSymbols decimalFormatSymbols) {
        this.symbols = null;
        this.symbols = (DecimalFormatSymbols) decimalFormatSymbols.clone();
        applyPattern(str, false);
    }

    @Override // java.text.NumberFormat, java.text.Format
    public final StringBuffer format(Object obj, StringBuffer stringBuffer, FieldPosition fieldPosition) {
        if ((obj instanceof Long) || (obj instanceof Integer) || (obj instanceof Short) || (obj instanceof Byte) || (obj instanceof AtomicInteger) || (obj instanceof AtomicLong) || ((obj instanceof BigInteger) && ((BigInteger) obj).bitLength() < 64)) {
            return format(((Number) obj).longValue(), stringBuffer, fieldPosition);
        }
        if (obj instanceof BigDecimal) {
            return format((BigDecimal) obj, stringBuffer, fieldPosition);
        }
        if (obj instanceof BigInteger) {
            return format((BigInteger) obj, stringBuffer, fieldPosition);
        }
        if (obj instanceof Number) {
            return format(((Number) obj).doubleValue(), stringBuffer, fieldPosition);
        }
        throw new IllegalArgumentException("Cannot format given Object as a Number");
    }

    @Override // java.text.NumberFormat
    public StringBuffer format(double d2, StringBuffer stringBuffer, FieldPosition fieldPosition) {
        String strFastFormat;
        boolean z2 = false;
        if (fieldPosition == DontCareFieldPosition.INSTANCE) {
            z2 = true;
        } else {
            fieldPosition.setBeginIndex(0);
            fieldPosition.setEndIndex(0);
        }
        if (z2 && (strFastFormat = fastFormat(d2)) != null) {
            stringBuffer.append(strFastFormat);
            return stringBuffer;
        }
        return format(d2, stringBuffer, fieldPosition.getFieldDelegate());
    }

    private StringBuffer format(double d2, StringBuffer stringBuffer, Format.FieldDelegate fieldDelegate) {
        StringBuffer stringBufferSubformat;
        if (Double.isNaN(d2) || (Double.isInfinite(d2) && this.multiplier == 0)) {
            int length = stringBuffer.length();
            stringBuffer.append(this.symbols.getNaN());
            fieldDelegate.formatted(0, NumberFormat.Field.INTEGER, NumberFormat.Field.INTEGER, length, stringBuffer.length(), stringBuffer);
            return stringBuffer;
        }
        boolean z2 = (d2 < 0.0d || (d2 == 0.0d && 1.0d / d2 < 0.0d)) ^ (this.multiplier < 0);
        if (this.multiplier != 1) {
            d2 *= this.multiplier;
        }
        if (Double.isInfinite(d2)) {
            if (z2) {
                append(stringBuffer, this.negativePrefix, fieldDelegate, getNegativePrefixFieldPositions(), NumberFormat.Field.SIGN);
            } else {
                append(stringBuffer, this.positivePrefix, fieldDelegate, getPositivePrefixFieldPositions(), NumberFormat.Field.SIGN);
            }
            int length2 = stringBuffer.length();
            stringBuffer.append(this.symbols.getInfinity());
            fieldDelegate.formatted(0, NumberFormat.Field.INTEGER, NumberFormat.Field.INTEGER, length2, stringBuffer.length(), stringBuffer);
            if (z2) {
                append(stringBuffer, this.negativeSuffix, fieldDelegate, getNegativeSuffixFieldPositions(), NumberFormat.Field.SIGN);
            } else {
                append(stringBuffer, this.positiveSuffix, fieldDelegate, getPositiveSuffixFieldPositions(), NumberFormat.Field.SIGN);
            }
            return stringBuffer;
        }
        if (z2) {
            d2 = -d2;
        }
        if (!$assertionsDisabled && (d2 < 0.0d || Double.isInfinite(d2))) {
            throw new AssertionError();
        }
        synchronized (this.digitList) {
            int maximumIntegerDigits = super.getMaximumIntegerDigits();
            int minimumIntegerDigits = super.getMinimumIntegerDigits();
            int maximumFractionDigits = super.getMaximumFractionDigits();
            int minimumFractionDigits = super.getMinimumFractionDigits();
            this.digitList.set(z2, d2, this.useExponentialNotation ? maximumIntegerDigits + maximumFractionDigits : maximumFractionDigits, !this.useExponentialNotation);
            stringBufferSubformat = subformat(stringBuffer, fieldDelegate, z2, false, maximumIntegerDigits, minimumIntegerDigits, maximumFractionDigits, minimumFractionDigits);
        }
        return stringBufferSubformat;
    }

    @Override // java.text.NumberFormat
    public StringBuffer format(long j2, StringBuffer stringBuffer, FieldPosition fieldPosition) {
        fieldPosition.setBeginIndex(0);
        fieldPosition.setEndIndex(0);
        return format(j2, stringBuffer, fieldPosition.getFieldDelegate());
    }

    private StringBuffer format(long j2, StringBuffer stringBuffer, Format.FieldDelegate fieldDelegate) {
        StringBuffer stringBufferSubformat;
        boolean z2 = j2 < 0;
        if (z2) {
            j2 = -j2;
        }
        boolean z3 = false;
        if (j2 < 0) {
            if (this.multiplier != 0) {
                z3 = true;
            }
        } else if (this.multiplier != 1 && this.multiplier != 0) {
            long j3 = Long.MAX_VALUE / this.multiplier;
            if (j3 < 0) {
                j3 = -j3;
            }
            z3 = j2 > j3;
        }
        if (z3) {
            if (z2) {
                j2 = -j2;
            }
            return format(BigInteger.valueOf(j2), stringBuffer, fieldDelegate, true);
        }
        long j4 = j2 * this.multiplier;
        if (j4 == 0) {
            z2 = false;
        } else if (this.multiplier < 0) {
            j4 = -j4;
            z2 = !z2;
        }
        synchronized (this.digitList) {
            int maximumIntegerDigits = super.getMaximumIntegerDigits();
            int minimumIntegerDigits = super.getMinimumIntegerDigits();
            int maximumFractionDigits = super.getMaximumFractionDigits();
            int minimumFractionDigits = super.getMinimumFractionDigits();
            this.digitList.set(z2, j4, this.useExponentialNotation ? maximumIntegerDigits + maximumFractionDigits : 0);
            stringBufferSubformat = subformat(stringBuffer, fieldDelegate, z2, true, maximumIntegerDigits, minimumIntegerDigits, maximumFractionDigits, minimumFractionDigits);
        }
        return stringBufferSubformat;
    }

    private StringBuffer format(BigDecimal bigDecimal, StringBuffer stringBuffer, FieldPosition fieldPosition) {
        fieldPosition.setBeginIndex(0);
        fieldPosition.setEndIndex(0);
        return format(bigDecimal, stringBuffer, fieldPosition.getFieldDelegate());
    }

    private StringBuffer format(BigDecimal bigDecimal, StringBuffer stringBuffer, Format.FieldDelegate fieldDelegate) {
        StringBuffer stringBufferSubformat;
        if (this.multiplier != 1) {
            bigDecimal = bigDecimal.multiply(getBigDecimalMultiplier());
        }
        boolean z2 = bigDecimal.signum() == -1;
        if (z2) {
            bigDecimal = bigDecimal.negate();
        }
        synchronized (this.digitList) {
            int maximumIntegerDigits = getMaximumIntegerDigits();
            int minimumIntegerDigits = getMinimumIntegerDigits();
            int maximumFractionDigits = getMaximumFractionDigits();
            int minimumFractionDigits = getMinimumFractionDigits();
            int i2 = maximumIntegerDigits + maximumFractionDigits;
            this.digitList.set(z2, bigDecimal, this.useExponentialNotation ? i2 < 0 ? Integer.MAX_VALUE : i2 : maximumFractionDigits, !this.useExponentialNotation);
            stringBufferSubformat = subformat(stringBuffer, fieldDelegate, z2, false, maximumIntegerDigits, minimumIntegerDigits, maximumFractionDigits, minimumFractionDigits);
        }
        return stringBufferSubformat;
    }

    private StringBuffer format(BigInteger bigInteger, StringBuffer stringBuffer, FieldPosition fieldPosition) {
        fieldPosition.setBeginIndex(0);
        fieldPosition.setEndIndex(0);
        return format(bigInteger, stringBuffer, fieldPosition.getFieldDelegate(), false);
    }

    private StringBuffer format(BigInteger bigInteger, StringBuffer stringBuffer, Format.FieldDelegate fieldDelegate, boolean z2) {
        int maximumIntegerDigits;
        int minimumIntegerDigits;
        int maximumFractionDigits;
        int minimumFractionDigits;
        int i2;
        StringBuffer stringBufferSubformat;
        if (this.multiplier != 1) {
            bigInteger = bigInteger.multiply(getBigIntegerMultiplier());
        }
        boolean z3 = bigInteger.signum() == -1;
        if (z3) {
            bigInteger = bigInteger.negate();
        }
        synchronized (this.digitList) {
            if (z2) {
                maximumIntegerDigits = super.getMaximumIntegerDigits();
                minimumIntegerDigits = super.getMinimumIntegerDigits();
                maximumFractionDigits = super.getMaximumFractionDigits();
                minimumFractionDigits = super.getMinimumFractionDigits();
                i2 = maximumIntegerDigits + maximumFractionDigits;
            } else {
                maximumIntegerDigits = getMaximumIntegerDigits();
                minimumIntegerDigits = getMinimumIntegerDigits();
                maximumFractionDigits = getMaximumFractionDigits();
                minimumFractionDigits = getMinimumFractionDigits();
                i2 = maximumIntegerDigits + maximumFractionDigits;
                if (i2 < 0) {
                    i2 = Integer.MAX_VALUE;
                }
            }
            this.digitList.set(z3, bigInteger, this.useExponentialNotation ? i2 : 0);
            stringBufferSubformat = subformat(stringBuffer, fieldDelegate, z3, true, maximumIntegerDigits, minimumIntegerDigits, maximumFractionDigits, minimumFractionDigits);
        }
        return stringBufferSubformat;
    }

    @Override // java.text.Format
    public AttributedCharacterIterator formatToCharacterIterator(Object obj) {
        CharacterIteratorFieldDelegate characterIteratorFieldDelegate = new CharacterIteratorFieldDelegate();
        StringBuffer stringBuffer = new StringBuffer();
        if ((obj instanceof Double) || (obj instanceof Float)) {
            format(((Number) obj).doubleValue(), stringBuffer, characterIteratorFieldDelegate);
        } else if ((obj instanceof Long) || (obj instanceof Integer) || (obj instanceof Short) || (obj instanceof Byte) || (obj instanceof AtomicInteger) || (obj instanceof AtomicLong)) {
            format(((Number) obj).longValue(), stringBuffer, (Format.FieldDelegate) characterIteratorFieldDelegate);
        } else if (obj instanceof BigDecimal) {
            format((BigDecimal) obj, stringBuffer, characterIteratorFieldDelegate);
        } else if (obj instanceof BigInteger) {
            format((BigInteger) obj, stringBuffer, characterIteratorFieldDelegate, false);
        } else {
            if (obj == null) {
                throw new NullPointerException("formatToCharacterIterator must be passed non-null object");
            }
            throw new IllegalArgumentException("Cannot format given Object as a Number");
        }
        return characterIteratorFieldDelegate.getIterator(stringBuffer.toString());
    }

    private boolean checkAndSetFastPathStatus() {
        boolean z2 = this.isFastPath;
        if (this.roundingMode == RoundingMode.HALF_EVEN && isGroupingUsed() && this.groupingSize == 3 && this.multiplier == 1 && !this.decimalSeparatorAlwaysShown && !this.useExponentialNotation) {
            this.isFastPath = this.minimumIntegerDigits == 1 && this.maximumIntegerDigits >= 10;
            if (this.isFastPath) {
                if (this.isCurrencyFormat) {
                    if (this.minimumFractionDigits != 2 || this.maximumFractionDigits != 2) {
                        this.isFastPath = false;
                    }
                } else if (this.minimumFractionDigits != 0 || this.maximumFractionDigits != 3) {
                    this.isFastPath = false;
                }
            }
        } else {
            this.isFastPath = false;
        }
        resetFastPathData(z2);
        this.fastPathCheckNeeded = false;
        return true;
    }

    private void resetFastPathData(boolean z2) {
        char decimalSeparator;
        if (!this.isFastPath) {
            if (z2) {
                this.fastPathData.fastPathContainer = null;
                this.fastPathData.charsPositiveSuffix = null;
                this.fastPathData.charsNegativeSuffix = null;
                this.fastPathData.charsPositivePrefix = null;
                this.fastPathData.charsNegativePrefix = null;
                return;
            }
            return;
        }
        if (this.fastPathData == null) {
            this.fastPathData = new FastPathData();
        }
        this.fastPathData.zeroDelta = this.symbols.getZeroDigit() - '0';
        this.fastPathData.groupingChar = this.symbols.getGroupingSeparator();
        this.fastPathData.fractionalMaxIntBound = this.isCurrencyFormat ? 99 : 999;
        this.fastPathData.fractionalScaleFactor = this.isCurrencyFormat ? 100.0d : 1000.0d;
        this.fastPathData.positiveAffixesRequired = (this.positivePrefix.length() == 0 && this.positiveSuffix.length() == 0) ? false : true;
        this.fastPathData.negativeAffixesRequired = (this.negativePrefix.length() == 0 && this.negativeSuffix.length() == 0) ? false : true;
        this.fastPathData.fastPathContainer = new char[Math.max(this.positivePrefix.length(), this.negativePrefix.length()) + 10 + 3 + 1 + this.maximumFractionDigits + Math.max(this.positiveSuffix.length(), this.negativeSuffix.length())];
        this.fastPathData.charsPositiveSuffix = this.positiveSuffix.toCharArray();
        this.fastPathData.charsNegativeSuffix = this.negativeSuffix.toCharArray();
        this.fastPathData.charsPositivePrefix = this.positivePrefix.toCharArray();
        this.fastPathData.charsNegativePrefix = this.negativePrefix.toCharArray();
        int iMax = 10 + 3 + Math.max(this.positivePrefix.length(), this.negativePrefix.length());
        this.fastPathData.integralLastIndex = iMax - 1;
        this.fastPathData.fractionalFirstIndex = iMax + 1;
        char[] cArr = this.fastPathData.fastPathContainer;
        if (this.isCurrencyFormat) {
            decimalSeparator = this.symbols.getMonetaryDecimalSeparator();
        } else {
            decimalSeparator = this.symbols.getDecimalSeparator();
        }
        cArr[iMax] = decimalSeparator;
    }

    private boolean exactRoundUp(double d2, int i2) {
        double d3;
        double d4;
        double d5;
        if (this.isCurrencyFormat) {
            d3 = d2 * 128.0d;
            d4 = -(d2 * 32.0d);
            d5 = d2 * 4.0d;
        } else {
            d3 = d2 * 1024.0d;
            d4 = -(d2 * 16.0d);
            d5 = -(d2 * 8.0d);
        }
        if (!$assertionsDisabled && (-d4) < Math.abs(d5)) {
            throw new AssertionError();
        }
        double d6 = d4 + d5;
        double d7 = d5 - (d6 - d4);
        if (!$assertionsDisabled && d3 < Math.abs(d6)) {
            throw new AssertionError();
        }
        double d8 = d3 + d6;
        double d9 = d7 + (d6 - (d8 - d3));
        if (!$assertionsDisabled && d8 < Math.abs(d9)) {
            throw new AssertionError();
        }
        double d10 = d9 - ((d8 + d9) - d8);
        if (d10 > 0.0d) {
            return true;
        }
        if (d10 >= 0.0d && (i2 & 1) != 0) {
            return true;
        }
        return false;
    }

    private void collectIntegralDigits(int i2, char[] cArr, int i3) {
        int i4 = i3;
        while (i2 > 999) {
            int i5 = i2 / 1000;
            int i6 = (i2 - (i5 << 10)) + (i5 << 4) + (i5 << 3);
            i2 = i5;
            int i7 = i4;
            int i8 = i4 - 1;
            cArr[i7] = DigitArrays.DigitOnes1000[i6];
            int i9 = i8 - 1;
            cArr[i8] = DigitArrays.DigitTens1000[i6];
            int i10 = i9 - 1;
            cArr[i9] = DigitArrays.DigitHundreds1000[i6];
            i4 = i10 - 1;
            cArr[i10] = this.fastPathData.groupingChar;
        }
        cArr[i4] = DigitArrays.DigitOnes1000[i2];
        if (i2 > 9) {
            i4--;
            cArr[i4] = DigitArrays.DigitTens1000[i2];
            if (i2 > 99) {
                i4--;
                cArr[i4] = DigitArrays.DigitHundreds1000[i2];
            }
        }
        this.fastPathData.firstUsedIndex = i4;
    }

    private void collectFractionalDigits(int i2, char[] cArr, int i3) {
        int i4;
        char c2 = DigitArrays.DigitOnes1000[i2];
        char c3 = DigitArrays.DigitTens1000[i2];
        if (this.isCurrencyFormat) {
            int i5 = i3 + 1;
            cArr[i3] = c3;
            i4 = i5 + 1;
            cArr[i5] = c2;
        } else if (i2 != 0) {
            i4 = i3 + 1;
            cArr[i3] = DigitArrays.DigitHundreds1000[i2];
            if (c2 != '0') {
                int i6 = i4 + 1;
                cArr[i4] = c3;
                i4 = i6 + 1;
                cArr[i6] = c2;
            } else if (c3 != '0') {
                i4++;
                cArr[i4] = c3;
            }
        } else {
            i4 = i3 - 1;
        }
        this.fastPathData.lastFreeIndex = i4;
    }

    private void addAffixes(char[] cArr, char[] cArr2, char[] cArr3) {
        int length = cArr2.length;
        int length2 = cArr3.length;
        if (length != 0) {
            prependPrefix(cArr2, length, cArr);
        }
        if (length2 != 0) {
            appendSuffix(cArr3, length2, cArr);
        }
    }

    private void prependPrefix(char[] cArr, int i2, char[] cArr2) {
        this.fastPathData.firstUsedIndex -= i2;
        int i3 = this.fastPathData.firstUsedIndex;
        if (i2 == 1) {
            cArr2[i3] = cArr[0];
            return;
        }
        if (i2 <= 4) {
            int i4 = (i3 + i2) - 1;
            cArr2[i3] = cArr[0];
            cArr2[i4] = cArr[i2 - 1];
            if (i2 > 2) {
                cArr2[i3 + 1] = cArr[1];
            }
            if (i2 == 4) {
                cArr2[i4 - 1] = cArr[2];
                return;
            }
            return;
        }
        System.arraycopy(cArr, 0, cArr2, i3, i2);
    }

    private void appendSuffix(char[] cArr, int i2, char[] cArr2) {
        int i3 = this.fastPathData.lastFreeIndex;
        if (i2 == 1) {
            cArr2[i3] = cArr[0];
        } else if (i2 <= 4) {
            int i4 = (i3 + i2) - 1;
            cArr2[i3] = cArr[0];
            cArr2[i4] = cArr[i2 - 1];
            if (i2 > 2) {
                cArr2[i3 + 1] = cArr[1];
            }
            if (i2 == 4) {
                cArr2[i4 - 1] = cArr[2];
            }
        } else {
            System.arraycopy(cArr, 0, cArr2, i3, i2);
        }
        this.fastPathData.lastFreeIndex += i2;
    }

    private void localizeDigits(char[] cArr) {
        int i2 = this.fastPathData.lastFreeIndex - this.fastPathData.fractionalFirstIndex;
        if (i2 < 0) {
            i2 = this.groupingSize;
        }
        for (int i3 = this.fastPathData.lastFreeIndex - 1; i3 >= this.fastPathData.firstUsedIndex; i3--) {
            if (i2 != 0) {
                int i4 = i3;
                cArr[i4] = (char) (cArr[i4] + this.fastPathData.zeroDelta);
                i2--;
            } else {
                i2 = this.groupingSize;
            }
        }
    }

    private void fastDoubleFormat(double d2, boolean z2) {
        boolean zExactRoundUp;
        char[] cArr = this.fastPathData.fastPathContainer;
        int i2 = (int) d2;
        double d3 = d2 - i2;
        double d4 = d3 * this.fastPathData.fractionalScaleFactor;
        int i3 = (int) d4;
        double d5 = d4 - i3;
        if (d5 >= 0.5d) {
            if (d5 == 0.5d) {
                zExactRoundUp = exactRoundUp(d3, i3);
            } else {
                zExactRoundUp = true;
            }
            if (zExactRoundUp) {
                if (i3 < this.fastPathData.fractionalMaxIntBound) {
                    i3++;
                } else {
                    i3 = 0;
                    i2++;
                }
            }
        }
        collectFractionalDigits(i3, cArr, this.fastPathData.fractionalFirstIndex);
        collectIntegralDigits(i2, cArr, this.fastPathData.integralLastIndex);
        if (this.fastPathData.zeroDelta != 0) {
            localizeDigits(cArr);
        }
        if (z2) {
            if (this.fastPathData.negativeAffixesRequired) {
                addAffixes(cArr, this.fastPathData.charsNegativePrefix, this.fastPathData.charsNegativeSuffix);
            }
        } else if (this.fastPathData.positiveAffixesRequired) {
            addAffixes(cArr, this.fastPathData.charsPositivePrefix, this.fastPathData.charsPositiveSuffix);
        }
    }

    @Override // java.text.NumberFormat
    String fastFormat(double d2) {
        boolean zCheckAndSetFastPathStatus = false;
        if (this.fastPathCheckNeeded) {
            zCheckAndSetFastPathStatus = checkAndSetFastPathStatus();
        }
        if (!this.isFastPath || !Double.isFinite(d2)) {
            return null;
        }
        boolean z2 = false;
        if (d2 < 0.0d) {
            z2 = true;
            d2 = -d2;
        } else if (d2 == 0.0d) {
            z2 = Math.copySign(1.0d, d2) == -1.0d;
            d2 = 0.0d;
        }
        if (d2 > MAX_INT_AS_DOUBLE) {
            return null;
        }
        if (!zCheckAndSetFastPathStatus) {
            resetFastPathData(this.isFastPath);
        }
        fastDoubleFormat(d2, z2);
        return new String(this.fastPathData.fastPathContainer, this.fastPathData.firstUsedIndex, this.fastPathData.lastFreeIndex - this.fastPathData.firstUsedIndex);
    }

    private StringBuffer subformat(StringBuffer stringBuffer, Format.FieldDelegate fieldDelegate, boolean z2, boolean z3, int i2, int i3, int i4, int i5) {
        char decimalSeparator;
        int i6;
        char zeroDigit = this.symbols.getZeroDigit();
        int i7 = zeroDigit - '0';
        char groupingSeparator = this.symbols.getGroupingSeparator();
        if (this.isCurrencyFormat) {
            decimalSeparator = this.symbols.getMonetaryDecimalSeparator();
        } else {
            decimalSeparator = this.symbols.getDecimalSeparator();
        }
        char c2 = decimalSeparator;
        if (this.digitList.isZero()) {
            this.digitList.decimalAt = 0;
        }
        if (z2) {
            append(stringBuffer, this.negativePrefix, fieldDelegate, getNegativePrefixFieldPositions(), NumberFormat.Field.SIGN);
        } else {
            append(stringBuffer, this.positivePrefix, fieldDelegate, getPositivePrefixFieldPositions(), NumberFormat.Field.SIGN);
        }
        if (this.useExponentialNotation) {
            int length = stringBuffer.length();
            int length2 = -1;
            int length3 = -1;
            int i8 = this.digitList.decimalAt;
            int i9 = i3;
            if (i2 > 1 && i2 > i3) {
                i6 = i8 >= 1 ? ((i8 - 1) / i2) * i2 : ((i8 - i2) / i2) * i2;
                i9 = 1;
            } else {
                i6 = i8 - i9;
            }
            int i10 = i3 + i5;
            if (i10 < 0) {
                i10 = Integer.MAX_VALUE;
            }
            int i11 = this.digitList.isZero() ? i9 : this.digitList.decimalAt - i6;
            if (i10 < i11) {
                i10 = i11;
            }
            int i12 = this.digitList.count;
            if (i10 > i12) {
                i12 = i10;
            }
            boolean z4 = false;
            int i13 = 0;
            while (i13 < i12) {
                if (i13 == i11) {
                    length2 = stringBuffer.length();
                    stringBuffer.append(c2);
                    z4 = true;
                    length3 = stringBuffer.length();
                }
                stringBuffer.append(i13 < this.digitList.count ? (char) (this.digitList.digits[i13] + i7) : zeroDigit);
                i13++;
            }
            if (this.decimalSeparatorAlwaysShown && i12 == i11) {
                length2 = stringBuffer.length();
                stringBuffer.append(c2);
                z4 = true;
                length3 = stringBuffer.length();
            }
            if (length2 == -1) {
                length2 = stringBuffer.length();
            }
            fieldDelegate.formatted(0, NumberFormat.Field.INTEGER, NumberFormat.Field.INTEGER, length, length2, stringBuffer);
            if (z4) {
                fieldDelegate.formatted(NumberFormat.Field.DECIMAL_SEPARATOR, NumberFormat.Field.DECIMAL_SEPARATOR, length2, length3, stringBuffer);
            }
            if (length3 == -1) {
                length3 = stringBuffer.length();
            }
            fieldDelegate.formatted(1, NumberFormat.Field.FRACTION, NumberFormat.Field.FRACTION, length3, stringBuffer.length(), stringBuffer);
            int length4 = stringBuffer.length();
            stringBuffer.append(this.symbols.getExponentSeparator());
            fieldDelegate.formatted(NumberFormat.Field.EXPONENT_SYMBOL, NumberFormat.Field.EXPONENT_SYMBOL, length4, stringBuffer.length(), stringBuffer);
            if (this.digitList.isZero()) {
                i6 = 0;
            }
            boolean z5 = i6 < 0;
            if (z5) {
                i6 = -i6;
                int length5 = stringBuffer.length();
                stringBuffer.append(this.symbols.getMinusSign());
                fieldDelegate.formatted(NumberFormat.Field.EXPONENT_SIGN, NumberFormat.Field.EXPONENT_SIGN, length5, stringBuffer.length(), stringBuffer);
            }
            this.digitList.set(z5, i6);
            int length6 = stringBuffer.length();
            for (int i14 = this.digitList.decimalAt; i14 < this.minExponentDigits; i14++) {
                stringBuffer.append(zeroDigit);
            }
            int i15 = 0;
            while (i15 < this.digitList.decimalAt) {
                stringBuffer.append(i15 < this.digitList.count ? (char) (this.digitList.digits[i15] + i7) : zeroDigit);
                i15++;
            }
            fieldDelegate.formatted(NumberFormat.Field.EXPONENT, NumberFormat.Field.EXPONENT, length6, stringBuffer.length(), stringBuffer);
        } else {
            int length7 = stringBuffer.length();
            int i16 = i3;
            int i17 = 0;
            if (this.digitList.decimalAt > 0 && i16 < this.digitList.decimalAt) {
                i16 = this.digitList.decimalAt;
            }
            if (i16 > i2) {
                i16 = i2;
                i17 = this.digitList.decimalAt - i16;
            }
            int length8 = stringBuffer.length();
            for (int i18 = i16 - 1; i18 >= 0; i18--) {
                if (i18 < this.digitList.decimalAt && i17 < this.digitList.count) {
                    int i19 = i17;
                    i17++;
                    stringBuffer.append((char) (this.digitList.digits[i19] + i7));
                } else {
                    stringBuffer.append(zeroDigit);
                }
                if (isGroupingUsed() && i18 > 0 && this.groupingSize != 0 && i18 % this.groupingSize == 0) {
                    int length9 = stringBuffer.length();
                    stringBuffer.append(groupingSeparator);
                    fieldDelegate.formatted(NumberFormat.Field.GROUPING_SEPARATOR, NumberFormat.Field.GROUPING_SEPARATOR, length9, stringBuffer.length(), stringBuffer);
                }
            }
            boolean z6 = i5 > 0 || (!z3 && i17 < this.digitList.count);
            if (!z6 && stringBuffer.length() == length8) {
                stringBuffer.append(zeroDigit);
            }
            fieldDelegate.formatted(0, NumberFormat.Field.INTEGER, NumberFormat.Field.INTEGER, length7, stringBuffer.length(), stringBuffer);
            int length10 = stringBuffer.length();
            if (this.decimalSeparatorAlwaysShown || z6) {
                stringBuffer.append(c2);
            }
            if (length10 != stringBuffer.length()) {
                fieldDelegate.formatted(NumberFormat.Field.DECIMAL_SEPARATOR, NumberFormat.Field.DECIMAL_SEPARATOR, length10, stringBuffer.length(), stringBuffer);
            }
            int length11 = stringBuffer.length();
            for (int i20 = 0; i20 < i4 && (i20 < i5 || (!z3 && i17 < this.digitList.count)); i20++) {
                if ((-1) - i20 > this.digitList.decimalAt - 1) {
                    stringBuffer.append(zeroDigit);
                } else if (!z3 && i17 < this.digitList.count) {
                    int i21 = i17;
                    i17++;
                    stringBuffer.append((char) (this.digitList.digits[i21] + i7));
                } else {
                    stringBuffer.append(zeroDigit);
                }
            }
            fieldDelegate.formatted(1, NumberFormat.Field.FRACTION, NumberFormat.Field.FRACTION, length11, stringBuffer.length(), stringBuffer);
        }
        if (z2) {
            append(stringBuffer, this.negativeSuffix, fieldDelegate, getNegativeSuffixFieldPositions(), NumberFormat.Field.SIGN);
        } else {
            append(stringBuffer, this.positiveSuffix, fieldDelegate, getPositiveSuffixFieldPositions(), NumberFormat.Field.SIGN);
        }
        return stringBuffer;
    }

    private void append(StringBuffer stringBuffer, String str, Format.FieldDelegate fieldDelegate, FieldPosition[] fieldPositionArr, Format.Field field) {
        int length = stringBuffer.length();
        if (str.length() > 0) {
            stringBuffer.append(str);
            for (FieldPosition fieldPosition : fieldPositionArr) {
                Format.Field fieldAttribute = fieldPosition.getFieldAttribute();
                if (fieldAttribute == NumberFormat.Field.SIGN) {
                    fieldAttribute = field;
                }
                fieldDelegate.formatted(fieldAttribute, fieldAttribute, length + fieldPosition.getBeginIndex(), length + fieldPosition.getEndIndex(), stringBuffer);
            }
        }
    }

    @Override // java.text.NumberFormat
    public Number parse(String str, ParsePosition parsePosition) {
        if (str.regionMatches(parsePosition.index, this.symbols.getNaN(), 0, this.symbols.getNaN().length())) {
            parsePosition.index += this.symbols.getNaN().length();
            return new Double(Double.NaN);
        }
        boolean[] zArr = new boolean[2];
        if (!subparse(str, parsePosition, this.positivePrefix, this.negativePrefix, this.digitList, false, zArr)) {
            return null;
        }
        if (zArr[0]) {
            if (zArr[1] == (this.multiplier >= 0)) {
                return new Double(Double.POSITIVE_INFINITY);
            }
            return new Double(Double.NEGATIVE_INFINITY);
        }
        if (this.multiplier == 0) {
            if (this.digitList.isZero()) {
                return new Double(Double.NaN);
            }
            if (zArr[1]) {
                return new Double(Double.POSITIVE_INFINITY);
            }
            return new Double(Double.NEGATIVE_INFINITY);
        }
        if (isParseBigDecimal()) {
            BigDecimal bigDecimal = this.digitList.getBigDecimal();
            if (this.multiplier != 1) {
                try {
                    bigDecimal = bigDecimal.divide(getBigDecimalMultiplier());
                } catch (ArithmeticException e2) {
                    bigDecimal = bigDecimal.divide(getBigDecimalMultiplier(), this.roundingMode);
                }
            }
            if (!zArr[1]) {
                bigDecimal = bigDecimal.negate();
            }
            return bigDecimal;
        }
        boolean z2 = true;
        boolean z3 = false;
        double d2 = 0.0d;
        long j2 = 0;
        if (this.digitList.fitsIntoLong(zArr[1], isParseIntegerOnly())) {
            z2 = false;
            j2 = this.digitList.getLong();
            if (j2 < 0) {
                z3 = true;
            }
        } else {
            d2 = this.digitList.getDouble();
        }
        if (this.multiplier != 1) {
            if (z2) {
                d2 /= this.multiplier;
            } else if (j2 % this.multiplier == 0) {
                j2 /= this.multiplier;
            } else {
                d2 = j2 / this.multiplier;
                z2 = true;
            }
        }
        if (!zArr[1] && !z3) {
            d2 = -d2;
            j2 = -j2;
        }
        if (this.multiplier != 1 && z2) {
            j2 = (long) d2;
            z2 = (d2 != ((double) j2) || (d2 == 0.0d && 1.0d / d2 < 0.0d)) && !isParseIntegerOnly();
        }
        return z2 ? new Double(d2) : new Long(j2);
    }

    private BigInteger getBigIntegerMultiplier() {
        if (this.bigIntegerMultiplier == null) {
            this.bigIntegerMultiplier = BigInteger.valueOf(this.multiplier);
        }
        return this.bigIntegerMultiplier;
    }

    private BigDecimal getBigDecimalMultiplier() {
        if (this.bigDecimalMultiplier == null) {
            this.bigDecimalMultiplier = new BigDecimal(this.multiplier);
        }
        return this.bigDecimalMultiplier;
    }

    private final boolean subparse(String str, ParsePosition parsePosition, String str2, String str3, DigitList digitList, boolean z2, boolean[] zArr) {
        int length;
        char decimalSeparator;
        int i2 = parsePosition.index;
        int i3 = parsePosition.index;
        boolean zRegionMatches = str.regionMatches(i2, str2, 0, str2.length());
        boolean zRegionMatches2 = str.regionMatches(i2, str3, 0, str3.length());
        if (zRegionMatches && zRegionMatches2) {
            if (str2.length() > str3.length()) {
                zRegionMatches2 = false;
            } else if (str2.length() < str3.length()) {
                zRegionMatches = false;
            }
        }
        if (zRegionMatches) {
            length = i2 + str2.length();
        } else if (zRegionMatches2) {
            length = i2 + str3.length();
        } else {
            parsePosition.errorIndex = i2;
            return false;
        }
        zArr[0] = false;
        if (!z2 && str.regionMatches(length, this.symbols.getInfinity(), 0, this.symbols.getInfinity().length())) {
            length += this.symbols.getInfinity().length();
            zArr[0] = true;
        } else {
            digitList.count = 0;
            digitList.decimalAt = 0;
            char zeroDigit = this.symbols.getZeroDigit();
            if (this.isCurrencyFormat) {
                decimalSeparator = this.symbols.getMonetaryDecimalSeparator();
            } else {
                decimalSeparator = this.symbols.getDecimalSeparator();
            }
            char c2 = decimalSeparator;
            char groupingSeparator = this.symbols.getGroupingSeparator();
            String exponentSeparator = this.symbols.getExponentSeparator();
            boolean z3 = false;
            boolean z4 = false;
            int i4 = 0;
            int i5 = 0;
            int i6 = -1;
            while (length < str.length()) {
                char cCharAt = str.charAt(length);
                int iDigit = cCharAt - zeroDigit;
                if (iDigit < 0 || iDigit > 9) {
                    iDigit = Character.digit(cCharAt, 10);
                }
                if (iDigit == 0) {
                    i6 = -1;
                    z4 = true;
                    if (digitList.count == 0) {
                        if (z3) {
                            digitList.decimalAt--;
                        }
                    } else {
                        i5++;
                        digitList.append((char) (iDigit + 48));
                    }
                } else if (iDigit > 0 && iDigit <= 9) {
                    z4 = true;
                    i5++;
                    digitList.append((char) (iDigit + 48));
                    i6 = -1;
                } else if (!z2 && cCharAt == c2) {
                    if (isParseIntegerOnly() || z3) {
                        break;
                    }
                    digitList.decimalAt = i5;
                    z3 = true;
                } else if (!z2 && cCharAt == groupingSeparator && isGroupingUsed()) {
                    if (z3) {
                        break;
                    }
                    i6 = length;
                } else if (!z2 && str.regionMatches(length, exponentSeparator, 0, exponentSeparator.length()) && 0 == 0) {
                    ParsePosition parsePosition2 = new ParsePosition(length + exponentSeparator.length());
                    boolean[] zArr2 = new boolean[2];
                    DigitList digitList2 = new DigitList();
                    if (subparse(str, parsePosition2, "", Character.toString(this.symbols.getMinusSign()), digitList2, true, zArr2) && digitList2.fitsIntoLong(zArr2[1], true)) {
                        length = parsePosition2.index;
                        i4 = (int) digitList2.getLong();
                        if (!zArr2[1]) {
                            i4 = -i4;
                        }
                    }
                }
                length++;
            }
            if (i6 != -1) {
                length = i6;
            }
            if (!z3) {
                digitList.decimalAt = i5;
            }
            digitList.decimalAt += i4;
            if (!z4 && i5 == 0) {
                parsePosition.index = i3;
                parsePosition.errorIndex = i3;
                return false;
            }
        }
        if (!z2) {
            if (zRegionMatches) {
                zRegionMatches = str.regionMatches(length, this.positiveSuffix, 0, this.positiveSuffix.length());
            }
            if (zRegionMatches2) {
                zRegionMatches2 = str.regionMatches(length, this.negativeSuffix, 0, this.negativeSuffix.length());
            }
            if (zRegionMatches && zRegionMatches2) {
                if (this.positiveSuffix.length() > this.negativeSuffix.length()) {
                    zRegionMatches2 = false;
                } else if (this.positiveSuffix.length() < this.negativeSuffix.length()) {
                    zRegionMatches = false;
                }
            }
            if (zRegionMatches == zRegionMatches2) {
                parsePosition.errorIndex = length;
                return false;
            }
            parsePosition.index = length + (zRegionMatches ? this.positiveSuffix.length() : this.negativeSuffix.length());
        } else {
            parsePosition.index = length;
        }
        zArr[1] = zRegionMatches;
        if (parsePosition.index == i3) {
            parsePosition.errorIndex = length;
            return false;
        }
        return true;
    }

    public DecimalFormatSymbols getDecimalFormatSymbols() {
        try {
            return (DecimalFormatSymbols) this.symbols.clone();
        } catch (Exception e2) {
            return null;
        }
    }

    public void setDecimalFormatSymbols(DecimalFormatSymbols decimalFormatSymbols) {
        try {
            this.symbols = (DecimalFormatSymbols) decimalFormatSymbols.clone();
            expandAffixes();
            this.fastPathCheckNeeded = true;
        } catch (Exception e2) {
        }
    }

    public String getPositivePrefix() {
        return this.positivePrefix;
    }

    public void setPositivePrefix(String str) {
        this.positivePrefix = str;
        this.posPrefixPattern = null;
        this.positivePrefixFieldPositions = null;
        this.fastPathCheckNeeded = true;
    }

    private FieldPosition[] getPositivePrefixFieldPositions() {
        if (this.positivePrefixFieldPositions == null) {
            if (this.posPrefixPattern != null) {
                this.positivePrefixFieldPositions = expandAffix(this.posPrefixPattern);
            } else {
                this.positivePrefixFieldPositions = EmptyFieldPositionArray;
            }
        }
        return this.positivePrefixFieldPositions;
    }

    public String getNegativePrefix() {
        return this.negativePrefix;
    }

    public void setNegativePrefix(String str) {
        this.negativePrefix = str;
        this.negPrefixPattern = null;
        this.fastPathCheckNeeded = true;
    }

    private FieldPosition[] getNegativePrefixFieldPositions() {
        if (this.negativePrefixFieldPositions == null) {
            if (this.negPrefixPattern != null) {
                this.negativePrefixFieldPositions = expandAffix(this.negPrefixPattern);
            } else {
                this.negativePrefixFieldPositions = EmptyFieldPositionArray;
            }
        }
        return this.negativePrefixFieldPositions;
    }

    public String getPositiveSuffix() {
        return this.positiveSuffix;
    }

    public void setPositiveSuffix(String str) {
        this.positiveSuffix = str;
        this.posSuffixPattern = null;
        this.fastPathCheckNeeded = true;
    }

    private FieldPosition[] getPositiveSuffixFieldPositions() {
        if (this.positiveSuffixFieldPositions == null) {
            if (this.posSuffixPattern != null) {
                this.positiveSuffixFieldPositions = expandAffix(this.posSuffixPattern);
            } else {
                this.positiveSuffixFieldPositions = EmptyFieldPositionArray;
            }
        }
        return this.positiveSuffixFieldPositions;
    }

    public String getNegativeSuffix() {
        return this.negativeSuffix;
    }

    public void setNegativeSuffix(String str) {
        this.negativeSuffix = str;
        this.negSuffixPattern = null;
        this.fastPathCheckNeeded = true;
    }

    private FieldPosition[] getNegativeSuffixFieldPositions() {
        if (this.negativeSuffixFieldPositions == null) {
            if (this.negSuffixPattern != null) {
                this.negativeSuffixFieldPositions = expandAffix(this.negSuffixPattern);
            } else {
                this.negativeSuffixFieldPositions = EmptyFieldPositionArray;
            }
        }
        return this.negativeSuffixFieldPositions;
    }

    public int getMultiplier() {
        return this.multiplier;
    }

    public void setMultiplier(int i2) {
        this.multiplier = i2;
        this.bigDecimalMultiplier = null;
        this.bigIntegerMultiplier = null;
        this.fastPathCheckNeeded = true;
    }

    @Override // java.text.NumberFormat
    public void setGroupingUsed(boolean z2) {
        super.setGroupingUsed(z2);
        this.fastPathCheckNeeded = true;
    }

    public int getGroupingSize() {
        return this.groupingSize;
    }

    public void setGroupingSize(int i2) {
        this.groupingSize = (byte) i2;
        this.fastPathCheckNeeded = true;
    }

    public boolean isDecimalSeparatorAlwaysShown() {
        return this.decimalSeparatorAlwaysShown;
    }

    public void setDecimalSeparatorAlwaysShown(boolean z2) {
        this.decimalSeparatorAlwaysShown = z2;
        this.fastPathCheckNeeded = true;
    }

    public boolean isParseBigDecimal() {
        return this.parseBigDecimal;
    }

    public void setParseBigDecimal(boolean z2) {
        this.parseBigDecimal = z2;
    }

    @Override // java.text.NumberFormat, java.text.Format
    public Object clone() {
        DecimalFormat decimalFormat = (DecimalFormat) super.clone();
        decimalFormat.symbols = (DecimalFormatSymbols) this.symbols.clone();
        decimalFormat.digitList = (DigitList) this.digitList.clone();
        decimalFormat.fastPathCheckNeeded = true;
        decimalFormat.isFastPath = false;
        decimalFormat.fastPathData = null;
        return decimalFormat;
    }

    @Override // java.text.NumberFormat
    public boolean equals(Object obj) {
        if (obj == null || !super.equals(obj)) {
            return false;
        }
        DecimalFormat decimalFormat = (DecimalFormat) obj;
        return ((this.posPrefixPattern == decimalFormat.posPrefixPattern && this.positivePrefix.equals(decimalFormat.positivePrefix)) || (this.posPrefixPattern != null && this.posPrefixPattern.equals(decimalFormat.posPrefixPattern))) && ((this.posSuffixPattern == decimalFormat.posSuffixPattern && this.positiveSuffix.equals(decimalFormat.positiveSuffix)) || (this.posSuffixPattern != null && this.posSuffixPattern.equals(decimalFormat.posSuffixPattern))) && (((this.negPrefixPattern == decimalFormat.negPrefixPattern && this.negativePrefix.equals(decimalFormat.negativePrefix)) || (this.negPrefixPattern != null && this.negPrefixPattern.equals(decimalFormat.negPrefixPattern))) && (((this.negSuffixPattern == decimalFormat.negSuffixPattern && this.negativeSuffix.equals(decimalFormat.negativeSuffix)) || (this.negSuffixPattern != null && this.negSuffixPattern.equals(decimalFormat.negSuffixPattern))) && this.multiplier == decimalFormat.multiplier && this.groupingSize == decimalFormat.groupingSize && this.decimalSeparatorAlwaysShown == decimalFormat.decimalSeparatorAlwaysShown && this.parseBigDecimal == decimalFormat.parseBigDecimal && this.useExponentialNotation == decimalFormat.useExponentialNotation && ((!this.useExponentialNotation || this.minExponentDigits == decimalFormat.minExponentDigits) && this.maximumIntegerDigits == decimalFormat.maximumIntegerDigits && this.minimumIntegerDigits == decimalFormat.minimumIntegerDigits && this.maximumFractionDigits == decimalFormat.maximumFractionDigits && this.minimumFractionDigits == decimalFormat.minimumFractionDigits && this.roundingMode == decimalFormat.roundingMode && this.symbols.equals(decimalFormat.symbols))));
    }

    @Override // java.text.NumberFormat
    public int hashCode() {
        return (super.hashCode() * 37) + this.positivePrefix.hashCode();
    }

    public String toPattern() {
        return toPattern(false);
    }

    public String toLocalizedPattern() {
        return toPattern(true);
    }

    private void expandAffixes() {
        StringBuffer stringBuffer = new StringBuffer();
        if (this.posPrefixPattern != null) {
            this.positivePrefix = expandAffix(this.posPrefixPattern, stringBuffer);
            this.positivePrefixFieldPositions = null;
        }
        if (this.posSuffixPattern != null) {
            this.positiveSuffix = expandAffix(this.posSuffixPattern, stringBuffer);
            this.positiveSuffixFieldPositions = null;
        }
        if (this.negPrefixPattern != null) {
            this.negativePrefix = expandAffix(this.negPrefixPattern, stringBuffer);
            this.negativePrefixFieldPositions = null;
        }
        if (this.negSuffixPattern != null) {
            this.negativeSuffix = expandAffix(this.negSuffixPattern, stringBuffer);
            this.negativeSuffixFieldPositions = null;
        }
    }

    private String expandAffix(String str, StringBuffer stringBuffer) {
        stringBuffer.setLength(0);
        int i2 = 0;
        while (i2 < str.length()) {
            int i3 = i2;
            i2++;
            char cCharAt = str.charAt(i3);
            if (cCharAt == '\'') {
                i2++;
                cCharAt = str.charAt(i2);
                switch (cCharAt) {
                    case '%':
                        cCharAt = this.symbols.getPercent();
                        break;
                    case '-':
                        cCharAt = this.symbols.getMinusSign();
                        break;
                    case 164:
                        if (i2 < str.length() && str.charAt(i2) == 164) {
                            i2++;
                            stringBuffer.append(this.symbols.getInternationalCurrencySymbol());
                        } else {
                            stringBuffer.append(this.symbols.getCurrencySymbol());
                            continue;
                        }
                        break;
                    case PATTERN_PER_MILLE /* 8240 */:
                        cCharAt = this.symbols.getPerMill();
                        break;
                }
            }
            stringBuffer.append(cCharAt);
        }
        return stringBuffer.toString();
    }

    private FieldPosition[] expandAffix(String str) {
        String currencySymbol;
        ArrayList arrayList = null;
        int length = 0;
        int i2 = 0;
        while (i2 < str.length()) {
            int i3 = i2;
            i2++;
            if (str.charAt(i3) == '\'') {
                int i4 = -1;
                NumberFormat.Field field = null;
                i2++;
                switch (str.charAt(i2)) {
                    case '%':
                        this.symbols.getPercent();
                        i4 = -1;
                        field = NumberFormat.Field.PERCENT;
                        break;
                    case '-':
                        this.symbols.getMinusSign();
                        i4 = -1;
                        field = NumberFormat.Field.SIGN;
                        break;
                    case 164:
                        if (i2 < str.length() && str.charAt(i2) == 164) {
                            i2++;
                            currencySymbol = this.symbols.getInternationalCurrencySymbol();
                        } else {
                            currencySymbol = this.symbols.getCurrencySymbol();
                        }
                        if (currencySymbol.length() > 0) {
                            if (arrayList == null) {
                                arrayList = new ArrayList(2);
                            }
                            FieldPosition fieldPosition = new FieldPosition(NumberFormat.Field.CURRENCY);
                            fieldPosition.setBeginIndex(length);
                            fieldPosition.setEndIndex(length + currencySymbol.length());
                            arrayList.add(fieldPosition);
                            length += currencySymbol.length();
                        } else {
                            continue;
                        }
                        break;
                    case PATTERN_PER_MILLE /* 8240 */:
                        this.symbols.getPerMill();
                        i4 = -1;
                        field = NumberFormat.Field.PERMILLE;
                        break;
                }
                if (field != null) {
                    if (arrayList == null) {
                        arrayList = new ArrayList(2);
                    }
                    FieldPosition fieldPosition2 = new FieldPosition(field, i4);
                    fieldPosition2.setBeginIndex(length);
                    fieldPosition2.setEndIndex(length + 1);
                    arrayList.add(fieldPosition2);
                }
            }
            length++;
        }
        if (arrayList != null) {
            return (FieldPosition[]) arrayList.toArray(EmptyFieldPositionArray);
        }
        return EmptyFieldPositionArray;
    }

    private void appendAffix(StringBuffer stringBuffer, String str, String str2, boolean z2) {
        if (str == null) {
            appendAffix(stringBuffer, str2, z2);
            return;
        }
        int i2 = 0;
        while (true) {
            int i3 = i2;
            if (i3 < str.length()) {
                int iIndexOf = str.indexOf(39, i3);
                if (iIndexOf < 0) {
                    appendAffix(stringBuffer, str.substring(i3), z2);
                    return;
                }
                if (iIndexOf > i3) {
                    appendAffix(stringBuffer, str.substring(i3, iIndexOf), z2);
                }
                int i4 = iIndexOf + 1;
                char cCharAt = str.charAt(i4);
                int i5 = i4 + 1;
                if (cCharAt == '\'') {
                    stringBuffer.append(cCharAt);
                } else if (cCharAt == 164 && i5 < str.length() && str.charAt(i5) == 164) {
                    i5++;
                    stringBuffer.append(cCharAt);
                } else if (z2) {
                    switch (cCharAt) {
                        case '%':
                            cCharAt = this.symbols.getPercent();
                            break;
                        case '-':
                            cCharAt = this.symbols.getMinusSign();
                            break;
                        case PATTERN_PER_MILLE /* 8240 */:
                            cCharAt = this.symbols.getPerMill();
                            break;
                    }
                }
                stringBuffer.append(cCharAt);
                i2 = i5;
            } else {
                return;
            }
        }
    }

    private void appendAffix(StringBuffer stringBuffer, String str, boolean z2) {
        boolean z3;
        if (z2) {
            z3 = str.indexOf(this.symbols.getZeroDigit()) >= 0 || str.indexOf(this.symbols.getGroupingSeparator()) >= 0 || str.indexOf(this.symbols.getDecimalSeparator()) >= 0 || str.indexOf(this.symbols.getPercent()) >= 0 || str.indexOf(this.symbols.getPerMill()) >= 0 || str.indexOf(this.symbols.getDigit()) >= 0 || str.indexOf(this.symbols.getPatternSeparator()) >= 0 || str.indexOf(this.symbols.getMinusSign()) >= 0 || str.indexOf(164) >= 0;
        } else {
            z3 = str.indexOf(48) >= 0 || str.indexOf(44) >= 0 || str.indexOf(46) >= 0 || str.indexOf(37) >= 0 || str.indexOf(PATTERN_PER_MILLE) >= 0 || str.indexOf(35) >= 0 || str.indexOf(59) >= 0 || str.indexOf(45) >= 0 || str.indexOf(164) >= 0;
        }
        if (z3) {
            stringBuffer.append('\'');
        }
        if (str.indexOf(39) < 0) {
            stringBuffer.append(str);
        } else {
            for (int i2 = 0; i2 < str.length(); i2++) {
                char cCharAt = str.charAt(i2);
                stringBuffer.append(cCharAt);
                if (cCharAt == '\'') {
                    stringBuffer.append(cCharAt);
                }
            }
        }
        if (z3) {
            stringBuffer.append('\'');
        }
    }

    private String toPattern(boolean z2) {
        int iMax;
        char digit;
        StringBuffer stringBuffer = new StringBuffer();
        for (int i2 = 1; i2 >= 0; i2--) {
            if (i2 == 1) {
                appendAffix(stringBuffer, this.posPrefixPattern, this.positivePrefix, z2);
            } else {
                appendAffix(stringBuffer, this.negPrefixPattern, this.negativePrefix, z2);
            }
            if (this.useExponentialNotation) {
                iMax = getMaximumIntegerDigits();
            } else {
                iMax = Math.max((int) this.groupingSize, getMinimumIntegerDigits()) + 1;
            }
            int i3 = iMax;
            for (int i4 = i3; i4 > 0; i4--) {
                if (i4 != i3 && isGroupingUsed() && this.groupingSize != 0 && i4 % this.groupingSize == 0) {
                    stringBuffer.append(z2 ? this.symbols.getGroupingSeparator() : ',');
                }
                if (i4 <= getMinimumIntegerDigits()) {
                    digit = z2 ? this.symbols.getZeroDigit() : '0';
                } else {
                    digit = z2 ? this.symbols.getDigit() : '#';
                }
                stringBuffer.append(digit);
            }
            if (getMaximumFractionDigits() > 0 || this.decimalSeparatorAlwaysShown) {
                stringBuffer.append(z2 ? this.symbols.getDecimalSeparator() : '.');
            }
            for (int i5 = 0; i5 < getMaximumFractionDigits(); i5++) {
                if (i5 < getMinimumFractionDigits()) {
                    stringBuffer.append(z2 ? this.symbols.getZeroDigit() : '0');
                } else {
                    stringBuffer.append(z2 ? this.symbols.getDigit() : '#');
                }
            }
            if (this.useExponentialNotation) {
                stringBuffer.append(z2 ? this.symbols.getExponentSeparator() : PATTERN_EXPONENT);
                for (int i6 = 0; i6 < this.minExponentDigits; i6++) {
                    stringBuffer.append(z2 ? this.symbols.getZeroDigit() : '0');
                }
            }
            if (i2 == 1) {
                appendAffix(stringBuffer, this.posSuffixPattern, this.positiveSuffix, z2);
                if (((this.negSuffixPattern == this.posSuffixPattern && this.negativeSuffix.equals(this.positiveSuffix)) || (this.negSuffixPattern != null && this.negSuffixPattern.equals(this.posSuffixPattern))) && ((this.negPrefixPattern != null && this.posPrefixPattern != null && this.negPrefixPattern.equals("'-" + this.posPrefixPattern)) || (this.negPrefixPattern == this.posPrefixPattern && this.negativePrefix.equals(this.symbols.getMinusSign() + this.positivePrefix)))) {
                    break;
                }
                stringBuffer.append(z2 ? this.symbols.getPatternSeparator() : ';');
            } else {
                appendAffix(stringBuffer, this.negSuffixPattern, this.negativeSuffix, z2);
            }
        }
        return stringBuffer.toString();
    }

    public void applyPattern(String str) {
        applyPattern(str, false);
    }

    public void applyLocalizedPattern(String str) {
        applyPattern(str, true);
    }

    /* JADX WARN: Removed duplicated region for block: B:89:0x02ce  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private void applyPattern(java.lang.String r7, boolean r8) {
        /*
            Method dump skipped, instructions count: 1591
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: java.text.DecimalFormat.applyPattern(java.lang.String, boolean):void");
    }

    @Override // java.text.NumberFormat
    public void setMaximumIntegerDigits(int i2) {
        this.maximumIntegerDigits = Math.min(Math.max(0, i2), Integer.MAX_VALUE);
        super.setMaximumIntegerDigits(this.maximumIntegerDigits > DOUBLE_INTEGER_DIGITS ? DOUBLE_INTEGER_DIGITS : this.maximumIntegerDigits);
        if (this.minimumIntegerDigits > this.maximumIntegerDigits) {
            this.minimumIntegerDigits = this.maximumIntegerDigits;
            super.setMinimumIntegerDigits(this.minimumIntegerDigits > DOUBLE_INTEGER_DIGITS ? DOUBLE_INTEGER_DIGITS : this.minimumIntegerDigits);
        }
        this.fastPathCheckNeeded = true;
    }

    @Override // java.text.NumberFormat
    public void setMinimumIntegerDigits(int i2) {
        this.minimumIntegerDigits = Math.min(Math.max(0, i2), Integer.MAX_VALUE);
        super.setMinimumIntegerDigits(this.minimumIntegerDigits > DOUBLE_INTEGER_DIGITS ? DOUBLE_INTEGER_DIGITS : this.minimumIntegerDigits);
        if (this.minimumIntegerDigits > this.maximumIntegerDigits) {
            this.maximumIntegerDigits = this.minimumIntegerDigits;
            super.setMaximumIntegerDigits(this.maximumIntegerDigits > DOUBLE_INTEGER_DIGITS ? DOUBLE_INTEGER_DIGITS : this.maximumIntegerDigits);
        }
        this.fastPathCheckNeeded = true;
    }

    @Override // java.text.NumberFormat
    public void setMaximumFractionDigits(int i2) {
        this.maximumFractionDigits = Math.min(Math.max(0, i2), Integer.MAX_VALUE);
        super.setMaximumFractionDigits(this.maximumFractionDigits > 340 ? 340 : this.maximumFractionDigits);
        if (this.minimumFractionDigits > this.maximumFractionDigits) {
            this.minimumFractionDigits = this.maximumFractionDigits;
            super.setMinimumFractionDigits(this.minimumFractionDigits > 340 ? 340 : this.minimumFractionDigits);
        }
        this.fastPathCheckNeeded = true;
    }

    @Override // java.text.NumberFormat
    public void setMinimumFractionDigits(int i2) {
        this.minimumFractionDigits = Math.min(Math.max(0, i2), Integer.MAX_VALUE);
        super.setMinimumFractionDigits(this.minimumFractionDigits > 340 ? 340 : this.minimumFractionDigits);
        if (this.minimumFractionDigits > this.maximumFractionDigits) {
            this.maximumFractionDigits = this.minimumFractionDigits;
            super.setMaximumFractionDigits(this.maximumFractionDigits > 340 ? 340 : this.maximumFractionDigits);
        }
        this.fastPathCheckNeeded = true;
    }

    @Override // java.text.NumberFormat
    public int getMaximumIntegerDigits() {
        return this.maximumIntegerDigits;
    }

    @Override // java.text.NumberFormat
    public int getMinimumIntegerDigits() {
        return this.minimumIntegerDigits;
    }

    @Override // java.text.NumberFormat
    public int getMaximumFractionDigits() {
        return this.maximumFractionDigits;
    }

    @Override // java.text.NumberFormat
    public int getMinimumFractionDigits() {
        return this.minimumFractionDigits;
    }

    @Override // java.text.NumberFormat
    public Currency getCurrency() {
        return this.symbols.getCurrency();
    }

    @Override // java.text.NumberFormat
    public void setCurrency(Currency currency) {
        if (currency != this.symbols.getCurrency()) {
            this.symbols.setCurrency(currency);
            if (this.isCurrencyFormat) {
                expandAffixes();
            }
        }
        this.fastPathCheckNeeded = true;
    }

    @Override // java.text.NumberFormat
    public RoundingMode getRoundingMode() {
        return this.roundingMode;
    }

    @Override // java.text.NumberFormat
    public void setRoundingMode(RoundingMode roundingMode) {
        if (roundingMode == null) {
            throw new NullPointerException();
        }
        this.roundingMode = roundingMode;
        this.digitList.setRoundingMode(roundingMode);
        this.fastPathCheckNeeded = true;
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        this.digitList = new DigitList();
        this.fastPathCheckNeeded = true;
        this.isFastPath = false;
        this.fastPathData = null;
        if (this.serialVersionOnStream < 4) {
            setRoundingMode(RoundingMode.HALF_EVEN);
        } else {
            setRoundingMode(getRoundingMode());
        }
        if (super.getMaximumIntegerDigits() > DOUBLE_INTEGER_DIGITS || super.getMaximumFractionDigits() > 340) {
            throw new InvalidObjectException("Digit count out of range");
        }
        if (this.serialVersionOnStream < 3) {
            setMaximumIntegerDigits(super.getMaximumIntegerDigits());
            setMinimumIntegerDigits(super.getMinimumIntegerDigits());
            setMaximumFractionDigits(super.getMaximumFractionDigits());
            setMinimumFractionDigits(super.getMinimumFractionDigits());
        }
        if (this.serialVersionOnStream < 1) {
            this.useExponentialNotation = false;
        }
        this.serialVersionOnStream = 4;
    }

    /* loaded from: rt.jar:java/text/DecimalFormat$FastPathData.class */
    private static class FastPathData {
        int lastFreeIndex;
        int firstUsedIndex;
        int zeroDelta;
        char groupingChar;
        int integralLastIndex;
        int fractionalFirstIndex;
        double fractionalScaleFactor;
        int fractionalMaxIntBound;
        char[] fastPathContainer;
        char[] charsPositivePrefix;
        char[] charsNegativePrefix;
        char[] charsPositiveSuffix;
        char[] charsNegativeSuffix;
        boolean positiveAffixesRequired;
        boolean negativeAffixesRequired;

        private FastPathData() {
            this.positiveAffixesRequired = true;
            this.negativeAffixesRequired = true;
        }
    }

    /* loaded from: rt.jar:java/text/DecimalFormat$DigitArrays.class */
    private static class DigitArrays {
        static final char[] DigitOnes1000 = new char[1000];
        static final char[] DigitTens1000 = new char[1000];
        static final char[] DigitHundreds1000 = new char[1000];

        private DigitArrays() {
        }

        static {
            int i2 = 0;
            int i3 = 0;
            char c2 = '0';
            char c3 = '0';
            char c4 = '0';
            for (int i4 = 0; i4 < 1000; i4++) {
                DigitOnes1000[i4] = c2;
                if (c2 == '9') {
                    c2 = '0';
                } else {
                    c2 = (char) (c2 + 1);
                }
                DigitTens1000[i4] = c3;
                if (i4 == i2 + 9) {
                    i2 += 10;
                    if (c3 == '9') {
                        c3 = '0';
                    } else {
                        c3 = (char) (c3 + 1);
                    }
                }
                DigitHundreds1000[i4] = c4;
                if (i4 == i3 + 99) {
                    c4 = (char) (c4 + 1);
                    i3 += 100;
                }
            }
        }
    }
}
