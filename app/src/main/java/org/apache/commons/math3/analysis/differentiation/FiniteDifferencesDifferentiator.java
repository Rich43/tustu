package org.apache.commons.math3.analysis.differentiation;

import java.io.Serializable;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.UnivariateMatrixFunction;
import org.apache.commons.math3.analysis.UnivariateVectorFunction;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.util.FastMath;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/analysis/differentiation/FiniteDifferencesDifferentiator.class */
public class FiniteDifferencesDifferentiator implements UnivariateFunctionDifferentiator, UnivariateVectorFunctionDifferentiator, UnivariateMatrixFunctionDifferentiator, Serializable {
    private static final long serialVersionUID = 20120917;
    private final int nbPoints;
    private final double stepSize;
    private final double halfSampleSpan;
    private final double tMin;
    private final double tMax;

    public FiniteDifferencesDifferentiator(int nbPoints, double stepSize) throws NumberIsTooSmallException {
        this(nbPoints, stepSize, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
    }

    public FiniteDifferencesDifferentiator(int nbPoints, double stepSize, double tLower, double tUpper) throws NumberIsTooSmallException, NumberIsTooLargeException {
        if (nbPoints <= 1) {
            throw new NumberIsTooSmallException(Double.valueOf(stepSize), 1, false);
        }
        this.nbPoints = nbPoints;
        if (stepSize <= 0.0d) {
            throw new NotPositiveException(Double.valueOf(stepSize));
        }
        this.stepSize = stepSize;
        this.halfSampleSpan = 0.5d * stepSize * (nbPoints - 1);
        if (2.0d * this.halfSampleSpan >= tUpper - tLower) {
            throw new NumberIsTooLargeException(Double.valueOf(2.0d * this.halfSampleSpan), Double.valueOf(tUpper - tLower), false);
        }
        double safety = FastMath.ulp(this.halfSampleSpan);
        this.tMin = tLower + this.halfSampleSpan + safety;
        this.tMax = (tUpper - this.halfSampleSpan) - safety;
    }

    public int getNbPoints() {
        return this.nbPoints;
    }

    public double getStepSize() {
        return this.stepSize;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public DerivativeStructure evaluate(DerivativeStructure t2, double t0, double[] y2) throws DimensionMismatchException, NumberIsTooLargeException {
        DerivativeStructure derivativeStructureMultiply;
        double[] top = new double[this.nbPoints];
        double[] bottom = new double[this.nbPoints];
        for (int i2 = 0; i2 < this.nbPoints; i2++) {
            bottom[i2] = y2[i2];
            for (int j2 = 1; j2 <= i2; j2++) {
                bottom[i2 - j2] = (bottom[(i2 - j2) + 1] - bottom[i2 - j2]) / (j2 * this.stepSize);
            }
            top[i2] = bottom[0];
        }
        int order = t2.getOrder();
        int parameters = t2.getFreeParameters();
        double[] derivatives = t2.getAllDerivatives();
        double dt0 = t2.getValue() - t0;
        DerivativeStructure interpolation = new DerivativeStructure(parameters, order, 0.0d);
        DerivativeStructure monomial = null;
        for (int i3 = 0; i3 < this.nbPoints; i3++) {
            if (i3 == 0) {
                derivativeStructureMultiply = new DerivativeStructure(parameters, order, 1.0d);
            } else {
                derivatives[0] = dt0 - ((i3 - 1) * this.stepSize);
                DerivativeStructure deltaX = new DerivativeStructure(parameters, order, derivatives);
                derivativeStructureMultiply = monomial.multiply(deltaX);
            }
            monomial = derivativeStructureMultiply;
            interpolation = interpolation.add(monomial.multiply(top[i3]));
        }
        return interpolation;
    }

    @Override // org.apache.commons.math3.analysis.differentiation.UnivariateFunctionDifferentiator
    public UnivariateDifferentiableFunction differentiate(final UnivariateFunction function) {
        return new UnivariateDifferentiableFunction() { // from class: org.apache.commons.math3.analysis.differentiation.FiniteDifferencesDifferentiator.1
            @Override // org.apache.commons.math3.analysis.UnivariateFunction
            public double value(double x2) throws MathIllegalArgumentException {
                return function.value(x2);
            }

            @Override // org.apache.commons.math3.analysis.differentiation.UnivariateDifferentiableFunction
            public DerivativeStructure value(DerivativeStructure t2) throws MathIllegalArgumentException {
                if (t2.getOrder() < FiniteDifferencesDifferentiator.this.nbPoints) {
                    double t0 = FastMath.max(FastMath.min(t2.getValue(), FiniteDifferencesDifferentiator.this.tMax), FiniteDifferencesDifferentiator.this.tMin) - FiniteDifferencesDifferentiator.this.halfSampleSpan;
                    double[] y2 = new double[FiniteDifferencesDifferentiator.this.nbPoints];
                    for (int i2 = 0; i2 < FiniteDifferencesDifferentiator.this.nbPoints; i2++) {
                        y2[i2] = function.value(t0 + (i2 * FiniteDifferencesDifferentiator.this.stepSize));
                    }
                    return FiniteDifferencesDifferentiator.this.evaluate(t2, t0, y2);
                }
                throw new NumberIsTooLargeException(Integer.valueOf(t2.getOrder()), Integer.valueOf(FiniteDifferencesDifferentiator.this.nbPoints), false);
            }
        };
    }

    @Override // org.apache.commons.math3.analysis.differentiation.UnivariateVectorFunctionDifferentiator
    public UnivariateDifferentiableVectorFunction differentiate(final UnivariateVectorFunction function) {
        return new UnivariateDifferentiableVectorFunction() { // from class: org.apache.commons.math3.analysis.differentiation.FiniteDifferencesDifferentiator.2
            @Override // org.apache.commons.math3.analysis.UnivariateVectorFunction
            public double[] value(double x2) throws MathIllegalArgumentException {
                return function.value(x2);
            }

            @Override // org.apache.commons.math3.analysis.differentiation.UnivariateDifferentiableVectorFunction
            public DerivativeStructure[] value(DerivativeStructure t2) throws MathIllegalArgumentException {
                if (t2.getOrder() < FiniteDifferencesDifferentiator.this.nbPoints) {
                    double t0 = FastMath.max(FastMath.min(t2.getValue(), FiniteDifferencesDifferentiator.this.tMax), FiniteDifferencesDifferentiator.this.tMin) - FiniteDifferencesDifferentiator.this.halfSampleSpan;
                    double[][] y2 = (double[][]) null;
                    for (int i2 = 0; i2 < FiniteDifferencesDifferentiator.this.nbPoints; i2++) {
                        double[] v2 = function.value(t0 + (i2 * FiniteDifferencesDifferentiator.this.stepSize));
                        if (i2 == 0) {
                            y2 = new double[v2.length][FiniteDifferencesDifferentiator.this.nbPoints];
                        }
                        for (int j2 = 0; j2 < v2.length; j2++) {
                            y2[j2][i2] = v2[j2];
                        }
                    }
                    DerivativeStructure[] value = new DerivativeStructure[y2.length];
                    for (int j3 = 0; j3 < value.length; j3++) {
                        value[j3] = FiniteDifferencesDifferentiator.this.evaluate(t2, t0, y2[j3]);
                    }
                    return value;
                }
                throw new NumberIsTooLargeException(Integer.valueOf(t2.getOrder()), Integer.valueOf(FiniteDifferencesDifferentiator.this.nbPoints), false);
            }
        };
    }

    @Override // org.apache.commons.math3.analysis.differentiation.UnivariateMatrixFunctionDifferentiator
    public UnivariateDifferentiableMatrixFunction differentiate(final UnivariateMatrixFunction function) {
        return new UnivariateDifferentiableMatrixFunction() { // from class: org.apache.commons.math3.analysis.differentiation.FiniteDifferencesDifferentiator.3
            @Override // org.apache.commons.math3.analysis.UnivariateMatrixFunction
            public double[][] value(double x2) throws MathIllegalArgumentException {
                return function.value(x2);
            }

            @Override // org.apache.commons.math3.analysis.differentiation.UnivariateDifferentiableMatrixFunction
            public DerivativeStructure[][] value(DerivativeStructure t2) throws MathIllegalArgumentException {
                if (t2.getOrder() < FiniteDifferencesDifferentiator.this.nbPoints) {
                    double t0 = FastMath.max(FastMath.min(t2.getValue(), FiniteDifferencesDifferentiator.this.tMax), FiniteDifferencesDifferentiator.this.tMin) - FiniteDifferencesDifferentiator.this.halfSampleSpan;
                    double[][][] y2 = (double[][][]) null;
                    for (int i2 = 0; i2 < FiniteDifferencesDifferentiator.this.nbPoints; i2++) {
                        double[][] v2 = function.value(t0 + (i2 * FiniteDifferencesDifferentiator.this.stepSize));
                        if (i2 == 0) {
                            y2 = new double[v2.length][v2[0].length][FiniteDifferencesDifferentiator.this.nbPoints];
                        }
                        for (int j2 = 0; j2 < v2.length; j2++) {
                            for (int k2 = 0; k2 < v2[j2].length; k2++) {
                                y2[j2][k2][i2] = v2[j2][k2];
                            }
                        }
                    }
                    DerivativeStructure[][] value = new DerivativeStructure[y2.length][y2[0].length];
                    for (int j3 = 0; j3 < value.length; j3++) {
                        for (int k3 = 0; k3 < y2[j3].length; k3++) {
                            value[j3][k3] = FiniteDifferencesDifferentiator.this.evaluate(t2, t0, y2[j3][k3]);
                        }
                    }
                    return value;
                }
                throw new NumberIsTooLargeException(Integer.valueOf(t2.getOrder()), Integer.valueOf(FiniteDifferencesDifferentiator.this.nbPoints), false);
            }
        };
    }
}
