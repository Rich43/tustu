package org.apache.commons.math3.fraction;

import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.util.LocalizedFormats;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/fraction/FractionConversionException.class */
public class FractionConversionException extends ConvergenceException {
    private static final long serialVersionUID = -4661812640132576263L;

    public FractionConversionException(double value, int maxIterations) {
        super(LocalizedFormats.FAILED_FRACTION_CONVERSION, Double.valueOf(value), Integer.valueOf(maxIterations));
    }

    public FractionConversionException(double value, long p2, long q2) {
        super(LocalizedFormats.FRACTION_CONVERSION_OVERFLOW, Double.valueOf(value), Long.valueOf(p2), Long.valueOf(q2));
    }
}
