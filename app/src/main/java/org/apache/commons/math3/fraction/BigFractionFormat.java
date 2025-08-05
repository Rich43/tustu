package org.apache.commons.math3.fraction;

import java.io.Serializable;
import java.math.BigInteger;
import java.text.FieldPosition;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.Locale;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.MathParseException;
import org.apache.commons.math3.exception.util.LocalizedFormats;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/fraction/BigFractionFormat.class */
public class BigFractionFormat extends AbstractFormat implements Serializable {
    private static final long serialVersionUID = -2932167925527338976L;

    public BigFractionFormat() {
    }

    public BigFractionFormat(NumberFormat format) {
        super(format);
    }

    public BigFractionFormat(NumberFormat numeratorFormat, NumberFormat denominatorFormat) {
        super(numeratorFormat, denominatorFormat);
    }

    public static Locale[] getAvailableLocales() {
        return NumberFormat.getAvailableLocales();
    }

    public static String formatBigFraction(BigFraction f2) {
        return getImproperInstance().format(f2);
    }

    public static BigFractionFormat getImproperInstance() {
        return getImproperInstance(Locale.getDefault());
    }

    public static BigFractionFormat getImproperInstance(Locale locale) {
        return new BigFractionFormat(getDefaultNumberFormat(locale));
    }

    public static BigFractionFormat getProperInstance() {
        return getProperInstance(Locale.getDefault());
    }

    public static BigFractionFormat getProperInstance(Locale locale) {
        return new ProperBigFractionFormat(getDefaultNumberFormat(locale));
    }

    public StringBuffer format(BigFraction BigFraction, StringBuffer toAppendTo, FieldPosition pos) {
        pos.setBeginIndex(0);
        pos.setEndIndex(0);
        getNumeratorFormat().format(BigFraction.getNumerator(), toAppendTo, pos);
        toAppendTo.append(" / ");
        getDenominatorFormat().format(BigFraction.getDenominator(), toAppendTo, pos);
        return toAppendTo;
    }

    @Override // java.text.NumberFormat, java.text.Format
    public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos) {
        StringBuffer ret;
        if (obj instanceof BigFraction) {
            ret = format((BigFraction) obj, toAppendTo, pos);
        } else if (obj instanceof BigInteger) {
            ret = format(new BigFraction((BigInteger) obj), toAppendTo, pos);
        } else if (obj instanceof Number) {
            ret = format(new BigFraction(((Number) obj).doubleValue()), toAppendTo, pos);
        } else {
            throw new MathIllegalArgumentException(LocalizedFormats.CANNOT_FORMAT_OBJECT_TO_FRACTION, new Object[0]);
        }
        return ret;
    }

    @Override // java.text.NumberFormat
    public BigFraction parse(String source) throws MathParseException {
        ParsePosition parsePosition = new ParsePosition(0);
        BigFraction result = parse(source, parsePosition);
        if (parsePosition.getIndex() == 0) {
            throw new MathParseException(source, parsePosition.getErrorIndex(), BigFraction.class);
        }
        return result;
    }

    @Override // java.text.NumberFormat
    public BigFraction parse(String source, ParsePosition pos) {
        int initialIndex = pos.getIndex();
        parseAndIgnoreWhitespace(source, pos);
        BigInteger num = parseNextBigInteger(source, pos);
        if (num == null) {
            pos.setIndex(initialIndex);
            return null;
        }
        int startIndex = pos.getIndex();
        char c2 = parseNextCharacter(source, pos);
        switch (c2) {
            case 0:
                return new BigFraction(num);
            case '/':
                parseAndIgnoreWhitespace(source, pos);
                BigInteger den = parseNextBigInteger(source, pos);
                if (den == null) {
                    pos.setIndex(initialIndex);
                    return null;
                }
                return new BigFraction(num, den);
            default:
                pos.setIndex(initialIndex);
                pos.setErrorIndex(startIndex);
                return null;
        }
    }

    protected BigInteger parseNextBigInteger(String source, ParsePosition pos) {
        int start = pos.getIndex();
        int end = source.charAt(start) == '-' ? start + 1 : start;
        while (end < source.length() && Character.isDigit(source.charAt(end))) {
            end++;
        }
        try {
            BigInteger n2 = new BigInteger(source.substring(start, end));
            pos.setIndex(end);
            return n2;
        } catch (NumberFormatException e2) {
            pos.setErrorIndex(start);
            return null;
        }
    }
}
