package org.apache.commons.math3.geometry.euclidean.oned;

import java.text.FieldPosition;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.Locale;
import org.apache.commons.math3.exception.MathParseException;
import org.apache.commons.math3.geometry.Vector;
import org.apache.commons.math3.geometry.VectorFormat;
import org.apache.commons.math3.util.CompositeFormat;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/geometry/euclidean/oned/Vector1DFormat.class */
public class Vector1DFormat extends VectorFormat<Euclidean1D> {
    public Vector1DFormat() {
        super(VectorFormat.DEFAULT_PREFIX, "}", VectorFormat.DEFAULT_SEPARATOR, CompositeFormat.getDefaultNumberFormat());
    }

    public Vector1DFormat(NumberFormat format) {
        super(VectorFormat.DEFAULT_PREFIX, "}", VectorFormat.DEFAULT_SEPARATOR, format);
    }

    public Vector1DFormat(String prefix, String suffix) {
        super(prefix, suffix, VectorFormat.DEFAULT_SEPARATOR, CompositeFormat.getDefaultNumberFormat());
    }

    public Vector1DFormat(String prefix, String suffix, NumberFormat format) {
        super(prefix, suffix, VectorFormat.DEFAULT_SEPARATOR, format);
    }

    public static Vector1DFormat getInstance() {
        return getInstance(Locale.getDefault());
    }

    public static Vector1DFormat getInstance(Locale locale) {
        return new Vector1DFormat(CompositeFormat.getDefaultNumberFormat(locale));
    }

    @Override // org.apache.commons.math3.geometry.VectorFormat
    public StringBuffer format(Vector<Euclidean1D> vector, StringBuffer toAppendTo, FieldPosition pos) {
        Vector1D p1 = (Vector1D) vector;
        return format(toAppendTo, pos, p1.getX());
    }

    @Override // org.apache.commons.math3.geometry.VectorFormat
    public Vector1D parse(String source) throws MathParseException {
        ParsePosition parsePosition = new ParsePosition(0);
        Vector1D result = parse(source, parsePosition);
        if (parsePosition.getIndex() == 0) {
            throw new MathParseException(source, parsePosition.getErrorIndex(), Vector1D.class);
        }
        return result;
    }

    @Override // org.apache.commons.math3.geometry.VectorFormat
    public Vector1D parse(String source, ParsePosition pos) {
        double[] coordinates = parseCoordinates(1, source, pos);
        if (coordinates == null) {
            return null;
        }
        return new Vector1D(coordinates[0]);
    }
}
