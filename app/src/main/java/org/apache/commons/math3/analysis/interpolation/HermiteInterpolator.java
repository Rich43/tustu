package org.apache.commons.math3.analysis.interpolation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.math3.analysis.differentiation.DerivativeStructure;
import org.apache.commons.math3.analysis.differentiation.UnivariateDifferentiableVectorFunction;
import org.apache.commons.math3.analysis.polynomials.PolynomialFunction;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.exception.NoDataException;
import org.apache.commons.math3.exception.ZeroException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.util.CombinatoricsUtils;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/analysis/interpolation/HermiteInterpolator.class */
public class HermiteInterpolator implements UnivariateDifferentiableVectorFunction {
    private final List<Double> abscissae = new ArrayList();
    private final List<double[]> topDiagonal = new ArrayList();
    private final List<double[]> bottomDiagonal = new ArrayList();

    /* JADX WARN: Multi-variable type inference failed */
    public void addSamplePoint(double x2, double[]... value) throws ZeroException, MathArithmeticException {
        for (int i2 = 0; i2 < value.length; i2++) {
            double[] y2 = (double[]) value[i2].clone();
            if (i2 > 1) {
                double inv = 1.0d / CombinatoricsUtils.factorial(i2);
                for (int j2 = 0; j2 < y2.length; j2++) {
                    int i3 = j2;
                    y2[i3] = y2[i3] * inv;
                }
            }
            int n2 = this.abscissae.size();
            this.bottomDiagonal.add(n2 - i2, y2);
            double[] bottom0 = y2;
            for (int j3 = i2; j3 < n2; j3++) {
                double[] bottom1 = this.bottomDiagonal.get(n2 - (j3 + 1));
                double inv2 = 1.0d / (x2 - this.abscissae.get(n2 - (j3 + 1)).doubleValue());
                if (Double.isInfinite(inv2)) {
                    throw new ZeroException(LocalizedFormats.DUPLICATED_ABSCISSA_DIVISION_BY_ZERO, Double.valueOf(x2));
                }
                for (int k2 = 0; k2 < y2.length; k2++) {
                    bottom1[k2] = inv2 * (bottom0[k2] - bottom1[k2]);
                }
                bottom0 = bottom1;
            }
            this.topDiagonal.add(bottom0.clone());
            this.abscissae.add(Double.valueOf(x2));
        }
    }

    public PolynomialFunction[] getPolynomials() throws NoDataException {
        checkInterpolation();
        PolynomialFunction zero = polynomial(0.0d);
        PolynomialFunction[] polynomials = new PolynomialFunction[this.topDiagonal.get(0).length];
        for (int i2 = 0; i2 < polynomials.length; i2++) {
            polynomials[i2] = zero;
        }
        PolynomialFunction coeff = polynomial(1.0d);
        for (int i3 = 0; i3 < this.topDiagonal.size(); i3++) {
            double[] tdi = this.topDiagonal.get(i3);
            for (int k2 = 0; k2 < polynomials.length; k2++) {
                polynomials[k2] = polynomials[k2].add(coeff.multiply(polynomial(tdi[k2])));
            }
            coeff = coeff.multiply(polynomial(-this.abscissae.get(i3).doubleValue(), 1.0d));
        }
        return polynomials;
    }

    @Override // org.apache.commons.math3.analysis.UnivariateVectorFunction
    public double[] value(double x2) throws NoDataException {
        checkInterpolation();
        double[] value = new double[this.topDiagonal.get(0).length];
        double valueCoeff = 1.0d;
        for (int i2 = 0; i2 < this.topDiagonal.size(); i2++) {
            double[] dividedDifference = this.topDiagonal.get(i2);
            for (int k2 = 0; k2 < value.length; k2++) {
                int i3 = k2;
                value[i3] = value[i3] + (dividedDifference[k2] * valueCoeff);
            }
            double deltaX = x2 - this.abscissae.get(i2).doubleValue();
            valueCoeff *= deltaX;
        }
        return value;
    }

    @Override // org.apache.commons.math3.analysis.differentiation.UnivariateDifferentiableVectorFunction
    public DerivativeStructure[] value(DerivativeStructure x2) throws NoDataException, DimensionMismatchException {
        checkInterpolation();
        DerivativeStructure[] value = new DerivativeStructure[this.topDiagonal.get(0).length];
        Arrays.fill(value, x2.getField2().getZero());
        DerivativeStructure valueCoeff = x2.getField2().getOne();
        for (int i2 = 0; i2 < this.topDiagonal.size(); i2++) {
            double[] dividedDifference = this.topDiagonal.get(i2);
            for (int k2 = 0; k2 < value.length; k2++) {
                value[k2] = value[k2].add(valueCoeff.multiply(dividedDifference[k2]));
            }
            DerivativeStructure deltaX = x2.subtract(this.abscissae.get(i2).doubleValue());
            valueCoeff = valueCoeff.multiply(deltaX);
        }
        return value;
    }

    private void checkInterpolation() throws NoDataException {
        if (this.abscissae.isEmpty()) {
            throw new NoDataException(LocalizedFormats.EMPTY_INTERPOLATION_SAMPLE);
        }
    }

    private PolynomialFunction polynomial(double... c2) {
        return new PolynomialFunction(c2);
    }
}
