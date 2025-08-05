package org.apache.commons.math3.complex;

import java.text.FieldPosition;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.Locale;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.MathParseException;
import org.apache.commons.math3.exception.NoDataException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.util.CompositeFormat;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/complex/ComplexFormat.class */
public class ComplexFormat {
    private static final String DEFAULT_IMAGINARY_CHARACTER = "i";
    private final String imaginaryCharacter;
    private final NumberFormat imaginaryFormat;
    private final NumberFormat realFormat;

    public ComplexFormat() {
        this.imaginaryCharacter = "i";
        this.imaginaryFormat = CompositeFormat.getDefaultNumberFormat();
        this.realFormat = this.imaginaryFormat;
    }

    public ComplexFormat(NumberFormat format) throws NullArgumentException {
        if (format == null) {
            throw new NullArgumentException(LocalizedFormats.IMAGINARY_FORMAT, new Object[0]);
        }
        this.imaginaryCharacter = "i";
        this.imaginaryFormat = format;
        this.realFormat = format;
    }

    public ComplexFormat(NumberFormat realFormat, NumberFormat imaginaryFormat) throws NullArgumentException {
        if (imaginaryFormat == null) {
            throw new NullArgumentException(LocalizedFormats.IMAGINARY_FORMAT, new Object[0]);
        }
        if (realFormat == null) {
            throw new NullArgumentException(LocalizedFormats.REAL_FORMAT, new Object[0]);
        }
        this.imaginaryCharacter = "i";
        this.imaginaryFormat = imaginaryFormat;
        this.realFormat = realFormat;
    }

    public ComplexFormat(String imaginaryCharacter) throws NullArgumentException, NoDataException {
        this(imaginaryCharacter, CompositeFormat.getDefaultNumberFormat());
    }

    public ComplexFormat(String imaginaryCharacter, NumberFormat format) throws NullArgumentException, NoDataException {
        this(imaginaryCharacter, format, format);
    }

    public ComplexFormat(String imaginaryCharacter, NumberFormat realFormat, NumberFormat imaginaryFormat) throws NullArgumentException, NoDataException {
        if (imaginaryCharacter == null) {
            throw new NullArgumentException();
        }
        if (imaginaryCharacter.length() == 0) {
            throw new NoDataException();
        }
        if (imaginaryFormat == null) {
            throw new NullArgumentException(LocalizedFormats.IMAGINARY_FORMAT, new Object[0]);
        }
        if (realFormat == null) {
            throw new NullArgumentException(LocalizedFormats.REAL_FORMAT, new Object[0]);
        }
        this.imaginaryCharacter = imaginaryCharacter;
        this.imaginaryFormat = imaginaryFormat;
        this.realFormat = realFormat;
    }

    public static Locale[] getAvailableLocales() {
        return NumberFormat.getAvailableLocales();
    }

    public String format(Complex c2) {
        return format(c2, new StringBuffer(), new FieldPosition(0)).toString();
    }

    public String format(Double c2) {
        return format(new Complex(c2.doubleValue(), 0.0d), new StringBuffer(), new FieldPosition(0)).toString();
    }

    public StringBuffer format(Complex complex, StringBuffer toAppendTo, FieldPosition pos) {
        pos.setBeginIndex(0);
        pos.setEndIndex(0);
        double re = complex.getReal();
        CompositeFormat.formatDouble(re, getRealFormat(), toAppendTo, pos);
        double im = complex.getImaginary();
        if (im < 0.0d) {
            toAppendTo.append(" - ");
            StringBuffer imAppendTo = formatImaginary(-im, new StringBuffer(), pos);
            toAppendTo.append(imAppendTo);
            toAppendTo.append(getImaginaryCharacter());
        } else if (im > 0.0d || Double.isNaN(im)) {
            toAppendTo.append(" + ");
            StringBuffer imAppendTo2 = formatImaginary(im, new StringBuffer(), pos);
            toAppendTo.append(imAppendTo2);
            toAppendTo.append(getImaginaryCharacter());
        }
        return toAppendTo;
    }

    private StringBuffer formatImaginary(double absIm, StringBuffer toAppendTo, FieldPosition pos) {
        pos.setBeginIndex(0);
        pos.setEndIndex(0);
        CompositeFormat.formatDouble(absIm, getImaginaryFormat(), toAppendTo, pos);
        if (toAppendTo.toString().equals("1")) {
            toAppendTo.setLength(0);
        }
        return toAppendTo;
    }

    public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos) throws MathIllegalArgumentException {
        StringBuffer ret;
        if (obj instanceof Complex) {
            ret = format((Complex) obj, toAppendTo, pos);
        } else if (obj instanceof Number) {
            ret = format(new Complex(((Number) obj).doubleValue(), 0.0d), toAppendTo, pos);
        } else {
            throw new MathIllegalArgumentException(LocalizedFormats.CANNOT_FORMAT_INSTANCE_AS_COMPLEX, obj.getClass().getName());
        }
        return ret;
    }

    public String getImaginaryCharacter() {
        return this.imaginaryCharacter;
    }

    public NumberFormat getImaginaryFormat() {
        return this.imaginaryFormat;
    }

    public static ComplexFormat getInstance() {
        return getInstance(Locale.getDefault());
    }

    public static ComplexFormat getInstance(Locale locale) {
        NumberFormat f2 = CompositeFormat.getDefaultNumberFormat(locale);
        return new ComplexFormat(f2);
    }

    public static ComplexFormat getInstance(String imaginaryCharacter, Locale locale) throws NullArgumentException, NoDataException {
        NumberFormat f2 = CompositeFormat.getDefaultNumberFormat(locale);
        return new ComplexFormat(imaginaryCharacter, f2);
    }

    public NumberFormat getRealFormat() {
        return this.realFormat;
    }

    public Complex parse(String source) throws MathParseException {
        ParsePosition parsePosition = new ParsePosition(0);
        Complex result = parse(source, parsePosition);
        if (parsePosition.getIndex() == 0) {
            throw new MathParseException(source, parsePosition.getErrorIndex(), Complex.class);
        }
        return result;
    }

    public Complex parse(String source, ParsePosition pos) {
        int sign;
        int initialIndex = pos.getIndex();
        CompositeFormat.parseAndIgnoreWhitespace(source, pos);
        Number re = CompositeFormat.parseNumber(source, getRealFormat(), pos);
        if (re == null) {
            pos.setIndex(initialIndex);
            return null;
        }
        int startIndex = pos.getIndex();
        char c2 = CompositeFormat.parseNextCharacter(source, pos);
        switch (c2) {
            case 0:
                return new Complex(re.doubleValue(), 0.0d);
            case '+':
                sign = 1;
                break;
            case '-':
                sign = -1;
                break;
            default:
                pos.setIndex(initialIndex);
                pos.setErrorIndex(startIndex);
                return null;
        }
        CompositeFormat.parseAndIgnoreWhitespace(source, pos);
        Number im = CompositeFormat.parseNumber(source, getRealFormat(), pos);
        if (im == null) {
            pos.setIndex(initialIndex);
            return null;
        }
        if (!CompositeFormat.parseFixedstring(source, getImaginaryCharacter(), pos)) {
            return null;
        }
        return new Complex(re.doubleValue(), im.doubleValue() * sign);
    }
}
