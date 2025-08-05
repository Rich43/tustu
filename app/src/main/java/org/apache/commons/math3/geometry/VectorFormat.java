package org.apache.commons.math3.geometry;

import java.text.FieldPosition;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.Locale;
import org.apache.commons.math3.exception.MathParseException;
import org.apache.commons.math3.geometry.Space;
import org.apache.commons.math3.util.CompositeFormat;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/geometry/VectorFormat.class */
public abstract class VectorFormat<S extends Space> {
    public static final String DEFAULT_PREFIX = "{";
    public static final String DEFAULT_SUFFIX = "}";
    public static final String DEFAULT_SEPARATOR = "; ";
    private final String prefix;
    private final String suffix;
    private final String separator;
    private final String trimmedPrefix;
    private final String trimmedSuffix;
    private final String trimmedSeparator;
    private final NumberFormat format;

    public abstract StringBuffer format(Vector<S> vector, StringBuffer stringBuffer, FieldPosition fieldPosition);

    public abstract Vector<S> parse(String str) throws MathParseException;

    public abstract Vector<S> parse(String str, ParsePosition parsePosition);

    protected VectorFormat() {
        this(DEFAULT_PREFIX, "}", DEFAULT_SEPARATOR, CompositeFormat.getDefaultNumberFormat());
    }

    protected VectorFormat(NumberFormat format) {
        this(DEFAULT_PREFIX, "}", DEFAULT_SEPARATOR, format);
    }

    protected VectorFormat(String prefix, String suffix, String separator) {
        this(prefix, suffix, separator, CompositeFormat.getDefaultNumberFormat());
    }

    protected VectorFormat(String prefix, String suffix, String separator, NumberFormat format) {
        this.prefix = prefix;
        this.suffix = suffix;
        this.separator = separator;
        this.trimmedPrefix = prefix.trim();
        this.trimmedSuffix = suffix.trim();
        this.trimmedSeparator = separator.trim();
        this.format = format;
    }

    public static Locale[] getAvailableLocales() {
        return NumberFormat.getAvailableLocales();
    }

    public String getPrefix() {
        return this.prefix;
    }

    public String getSuffix() {
        return this.suffix;
    }

    public String getSeparator() {
        return this.separator;
    }

    public NumberFormat getFormat() {
        return this.format;
    }

    public String format(Vector<S> vector) {
        return format(vector, new StringBuffer(), new FieldPosition(0)).toString();
    }

    protected StringBuffer format(StringBuffer toAppendTo, FieldPosition pos, double... coordinates) {
        pos.setBeginIndex(0);
        pos.setEndIndex(0);
        toAppendTo.append(this.prefix);
        for (int i2 = 0; i2 < coordinates.length; i2++) {
            if (i2 > 0) {
                toAppendTo.append(this.separator);
            }
            CompositeFormat.formatDouble(coordinates[i2], this.format, toAppendTo, pos);
        }
        toAppendTo.append(this.suffix);
        return toAppendTo;
    }

    protected double[] parseCoordinates(int dimension, String source, ParsePosition pos) {
        int initialIndex = pos.getIndex();
        double[] coordinates = new double[dimension];
        CompositeFormat.parseAndIgnoreWhitespace(source, pos);
        if (!CompositeFormat.parseFixedstring(source, this.trimmedPrefix, pos)) {
            return null;
        }
        for (int i2 = 0; i2 < dimension; i2++) {
            CompositeFormat.parseAndIgnoreWhitespace(source, pos);
            if (i2 > 0 && !CompositeFormat.parseFixedstring(source, this.trimmedSeparator, pos)) {
                return null;
            }
            CompositeFormat.parseAndIgnoreWhitespace(source, pos);
            Number c2 = CompositeFormat.parseNumber(source, this.format, pos);
            if (c2 == null) {
                pos.setIndex(initialIndex);
                return null;
            }
            coordinates[i2] = c2.doubleValue();
        }
        CompositeFormat.parseAndIgnoreWhitespace(source, pos);
        if (!CompositeFormat.parseFixedstring(source, this.trimmedSuffix, pos)) {
            return null;
        }
        return coordinates;
    }
}
