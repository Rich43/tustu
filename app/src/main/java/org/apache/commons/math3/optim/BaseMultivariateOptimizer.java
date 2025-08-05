package org.apache.commons.math3.optim;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/optim/BaseMultivariateOptimizer.class */
public abstract class BaseMultivariateOptimizer<PAIR> extends BaseOptimizer<PAIR> {
    private double[] start;
    private double[] lowerBound;
    private double[] upperBound;

    protected BaseMultivariateOptimizer(ConvergenceChecker<PAIR> checker) {
        super(checker);
    }

    @Override // org.apache.commons.math3.optim.BaseOptimizer
    public PAIR optimize(OptimizationData... optimizationDataArr) {
        return (PAIR) super.optimize(optimizationDataArr);
    }

    @Override // org.apache.commons.math3.optim.BaseOptimizer
    protected void parseOptimizationData(OptimizationData... optData) {
        super.parseOptimizationData(optData);
        for (OptimizationData data : optData) {
            if (data instanceof InitialGuess) {
                this.start = ((InitialGuess) data).getInitialGuess();
            } else if (data instanceof SimpleBounds) {
                SimpleBounds bounds = (SimpleBounds) data;
                this.lowerBound = bounds.getLower();
                this.upperBound = bounds.getUpper();
            }
        }
        checkParameters();
    }

    public double[] getStartPoint() {
        if (this.start == null) {
            return null;
        }
        return (double[]) this.start.clone();
    }

    public double[] getLowerBound() {
        if (this.lowerBound == null) {
            return null;
        }
        return (double[]) this.lowerBound.clone();
    }

    public double[] getUpperBound() {
        if (this.upperBound == null) {
            return null;
        }
        return (double[]) this.upperBound.clone();
    }

    private void checkParameters() {
        if (this.start != null) {
            int dim = this.start.length;
            if (this.lowerBound != null) {
                if (this.lowerBound.length != dim) {
                    throw new DimensionMismatchException(this.lowerBound.length, dim);
                }
                for (int i2 = 0; i2 < dim; i2++) {
                    double v2 = this.start[i2];
                    double lo = this.lowerBound[i2];
                    if (v2 < lo) {
                        throw new NumberIsTooSmallException(Double.valueOf(v2), Double.valueOf(lo), true);
                    }
                }
            }
            if (this.upperBound != null) {
                if (this.upperBound.length != dim) {
                    throw new DimensionMismatchException(this.upperBound.length, dim);
                }
                for (int i3 = 0; i3 < dim; i3++) {
                    double v3 = this.start[i3];
                    double hi = this.upperBound[i3];
                    if (v3 > hi) {
                        throw new NumberIsTooLargeException(Double.valueOf(v3), Double.valueOf(hi), true);
                    }
                }
            }
        }
    }
}
