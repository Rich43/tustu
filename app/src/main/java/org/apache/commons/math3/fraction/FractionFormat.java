package org.apache.commons.math3.fraction;

import java.text.FieldPosition;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.Locale;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.MathParseException;
import org.apache.commons.math3.exception.util.LocalizedFormats;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/fraction/FractionFormat.class */
public class FractionFormat extends AbstractFormat {
    private static final long serialVersionUID = 3008655719530972611L;

    public FractionFormat() {
    }

    public FractionFormat(NumberFormat format) {
        super(format);
    }

    public FractionFormat(NumberFormat numeratorFormat, NumberFormat denominatorFormat) {
        super(numeratorFormat, denominatorFormat);
    }

    public static Locale[] getAvailableLocales() {
        return NumberFormat.getAvailableLocales();
    }

    public static String formatFraction(Fraction f2) {
        return getImproperInstance().format(f2);
    }

    public static FractionFormat getImproperInstance() {
        return getImproperInstance(Locale.getDefault());
    }

    public static FractionFormat getImproperInstance(Locale locale) {
        return new FractionFormat(getDefaultNumberFormat(locale));
    }

    public static FractionFormat getProperInstance() {
        return getProperInstance(Locale.getDefault());
    }

    public static FractionFormat getProperInstance(Locale locale) {
        return new ProperFractionFormat(getDefaultNumberFormat(locale));
    }

    protected static NumberFormat getDefaultNumberFormat() {
        return getDefaultNumberFormat(Locale.getDefault());
    }

    public StringBuffer format(Fraction fraction, StringBuffer toAppendTo, FieldPosition pos) {
        pos.setBeginIndex(0);
        pos.setEndIndex(0);
        getNumeratorFormat().format(fraction.getNumerator(), toAppendTo, pos);
        toAppendTo.append(" / ");
        getDenominatorFormat().format(fraction.getDenominator(), toAppendTo, pos);
        return toAppendTo;
    }

    @Override // java.text.NumberFormat, java.text.Format
    public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos) throws FractionConversionException, MathIllegalArgumentException {
        StringBuffer ret;
        if (obj instanceof Fraction) {
            ret = format((Fraction) obj, toAppendTo, pos);
        } else if (obj instanceof Number) {
            ret = format(new Fraction(((Number) obj).doubleValue()), toAppendTo, pos);
        } else {
            throw new MathIllegalArgumentException(LocalizedFormats.CANNOT_FORMAT_OBJECT_TO_FRACTION, new Object[0]);
        }
        return ret;
    }

    @Override // java.text.NumberFormat
    public Fraction parse(String source) throws MathParseException {
        ParsePosition parsePosition = new ParsePosition(0);
        Fraction result = parse(source, parsePosition);
        if (parsePosition.getIndex() == 0) {
            throw new MathParseException(source, parsePosition.getErrorIndex(), Fraction.class);
        }
        return result;
    }

    @Override // java.text.NumberFormat
    public Fraction parse(String source, ParsePosition pos) {
        int initialIndex = pos.getIndex();
        parseAndIgnoreWhitespace(source, pos);
        Number num = getNumeratorFormat().parse(source, pos);
        if (num == null) {
            pos.setIndex(initialIndex);
            return null;
        }
        int startIndex = pos.getIndex();
        char c2 = parseNextCharacter(source, pos);
        switch (c2) {
            case 0:
                return new Fraction(num.intValue(), 1);
            case '/':
                parseAndIgnoreWhitespace(source, pos);
                Number den = getDenominatorFormat().parse(source, pos);
                if (den == null) {
                    pos.setIndex(initialIndex);
                    return null;
                }
                return new Fraction(num.intValue(), den.intValue());
            default:
                pos.setIndex(initialIndex);
                pos.setErrorIndex(startIndex);
                return null;
        }
    }
}
