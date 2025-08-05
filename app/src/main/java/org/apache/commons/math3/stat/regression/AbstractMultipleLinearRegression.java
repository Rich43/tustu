package org.apache.commons.math3.stat.regression;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.InsufficientDataException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NoDataException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.NonSquareMatrixException;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.stat.descriptive.moment.Variance;
import org.apache.commons.math3.util.FastMath;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/stat/regression/AbstractMultipleLinearRegression.class */
public abstract class AbstractMultipleLinearRegression implements MultipleLinearRegression {
    private RealMatrix xMatrix;
    private RealVector yVector;
    private boolean noIntercept = false;

    protected abstract RealVector calculateBeta();

    protected abstract RealMatrix calculateBetaVariance();

    protected RealMatrix getX() {
        return this.xMatrix;
    }

    protected RealVector getY() {
        return this.yVector;
    }

    public boolean isNoIntercept() {
        return this.noIntercept;
    }

    public void setNoIntercept(boolean noIntercept) {
        this.noIntercept = noIntercept;
    }

    public void newSampleData(double[] data, int nobs, int nvars) {
        if (data == null) {
            throw new NullArgumentException();
        }
        if (data.length != nobs * (nvars + 1)) {
            throw new DimensionMismatchException(data.length, nobs * (nvars + 1));
        }
        if (nobs <= nvars) {
            throw new InsufficientDataException(LocalizedFormats.INSUFFICIENT_OBSERVED_POINTS_IN_SAMPLE, Integer.valueOf(nobs), Integer.valueOf(nvars + 1));
        }
        double[] y2 = new double[nobs];
        int cols = this.noIntercept ? nvars : nvars + 1;
        double[][] x2 = new double[nobs][cols];
        int pointer = 0;
        for (int i2 = 0; i2 < nobs; i2++) {
            int i3 = pointer;
            pointer++;
            y2[i2] = data[i3];
            if (!this.noIntercept) {
                x2[i2][0] = 1.0d;
            }
            for (int j2 = this.noIntercept ? 0 : 1; j2 < cols; j2++) {
                int i4 = pointer;
                pointer++;
                x2[i2][j2] = data[i4];
            }
        }
        this.xMatrix = new Array2DRowRealMatrix(x2);
        this.yVector = new ArrayRealVector(y2);
    }

    protected void newYSampleData(double[] y2) {
        if (y2 == null) {
            throw new NullArgumentException();
        }
        if (y2.length == 0) {
            throw new NoDataException();
        }
        this.yVector = new ArrayRealVector(y2);
    }

    protected void newXSampleData(double[][] x2) {
        if (x2 == null) {
            throw new NullArgumentException();
        }
        if (x2.length == 0) {
            throw new NoDataException();
        }
        if (this.noIntercept) {
            this.xMatrix = new Array2DRowRealMatrix(x2, true);
            return;
        }
        int nVars = x2[0].length;
        double[][] xAug = new double[x2.length][nVars + 1];
        for (int i2 = 0; i2 < x2.length; i2++) {
            if (x2[i2].length != nVars) {
                throw new DimensionMismatchException(x2[i2].length, nVars);
            }
            xAug[i2][0] = 1.0d;
            System.arraycopy(x2[i2], 0, xAug[i2], 1, nVars);
        }
        this.xMatrix = new Array2DRowRealMatrix(xAug, false);
    }

    protected void validateSampleData(double[][] x2, double[] y2) throws MathIllegalArgumentException {
        if (x2 == null || y2 == null) {
            throw new NullArgumentException();
        }
        if (x2.length != y2.length) {
            throw new DimensionMismatchException(y2.length, x2.length);
        }
        if (x2.length == 0) {
            throw new NoDataException();
        }
        if (x2[0].length + 1 > x2.length) {
            throw new MathIllegalArgumentException(LocalizedFormats.NOT_ENOUGH_DATA_FOR_NUMBER_OF_PREDICTORS, Integer.valueOf(x2.length), Integer.valueOf(x2[0].length));
        }
    }

    protected void validateCovarianceData(double[][] x2, double[][] covariance) {
        if (x2.length != covariance.length) {
            throw new DimensionMismatchException(x2.length, covariance.length);
        }
        if (covariance.length > 0 && covariance.length != covariance[0].length) {
            throw new NonSquareMatrixException(covariance.length, covariance[0].length);
        }
    }

    @Override // org.apache.commons.math3.stat.regression.MultipleLinearRegression
    public double[] estimateRegressionParameters() {
        RealVector b2 = calculateBeta();
        return b2.toArray();
    }

    @Override // org.apache.commons.math3.stat.regression.MultipleLinearRegression
    public double[] estimateResiduals() throws OutOfRangeException, DimensionMismatchException {
        RealVector b2 = calculateBeta();
        RealVector e2 = this.yVector.subtract(this.xMatrix.operate(b2));
        return e2.toArray();
    }

    @Override // org.apache.commons.math3.stat.regression.MultipleLinearRegression
    public double[][] estimateRegressionParametersVariance() {
        return calculateBetaVariance().getData();
    }

    @Override // org.apache.commons.math3.stat.regression.MultipleLinearRegression
    public double[] estimateRegressionParametersStandardErrors() {
        double[][] betaVariance = estimateRegressionParametersVariance();
        double sigma = calculateErrorVariance();
        int length = betaVariance[0].length;
        double[] result = new double[length];
        for (int i2 = 0; i2 < length; i2++) {
            result[i2] = FastMath.sqrt(sigma * betaVariance[i2][i2]);
        }
        return result;
    }

    @Override // org.apache.commons.math3.stat.regression.MultipleLinearRegression
    public double estimateRegressandVariance() {
        return calculateYVariance();
    }

    public double estimateErrorVariance() {
        return calculateErrorVariance();
    }

    public double estimateRegressionStandardError() {
        return FastMath.sqrt(estimateErrorVariance());
    }

    protected double calculateYVariance() {
        return new Variance().evaluate(this.yVector.toArray());
    }

    protected double calculateErrorVariance() {
        RealVector residuals = calculateResiduals();
        return residuals.dotProduct(residuals) / (this.xMatrix.getRowDimension() - this.xMatrix.getColumnDimension());
    }

    protected RealVector calculateResiduals() {
        RealVector b2 = calculateBeta();
        return this.yVector.subtract(this.xMatrix.operate(b2));
    }
}
