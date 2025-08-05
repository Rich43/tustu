package org.apache.commons.math3.analysis.function;

import java.util.Arrays;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NoDataException;
import org.apache.commons.math3.exception.NonMonotonicSequenceException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.util.MathArrays;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/analysis/function/StepFunction.class */
public class StepFunction implements UnivariateFunction {
    private final double[] abscissa;
    private final double[] ordinate;

    public StepFunction(double[] x2, double[] y2) throws NullArgumentException, NoDataException, NonMonotonicSequenceException, DimensionMismatchException {
        if (x2 == null || y2 == null) {
            throw new NullArgumentException();
        }
        if (x2.length == 0 || y2.length == 0) {
            throw new NoDataException();
        }
        if (y2.length != x2.length) {
            throw new DimensionMismatchException(y2.length, x2.length);
        }
        MathArrays.checkOrder(x2);
        this.abscissa = MathArrays.copyOf(x2);
        this.ordinate = MathArrays.copyOf(y2);
    }

    @Override // org.apache.commons.math3.analysis.UnivariateFunction
    public double value(double x2) {
        double fx;
        int index = Arrays.binarySearch(this.abscissa, x2);
        if (index < -1) {
            fx = this.ordinate[(-index) - 2];
        } else if (index >= 0) {
            fx = this.ordinate[index];
        } else {
            fx = this.ordinate[0];
        }
        return fx;
    }
}
