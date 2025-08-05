package org.apache.commons.math3.stat.regression;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.SingularMatrixException;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/stat/regression/GLSMultipleLinearRegression.class */
public class GLSMultipleLinearRegression extends AbstractMultipleLinearRegression {
    private RealMatrix Omega;
    private RealMatrix OmegaInverse;

    public void newSampleData(double[] y2, double[][] x2, double[][] covariance) {
        validateSampleData(x2, y2);
        newYSampleData(y2);
        newXSampleData(x2);
        validateCovarianceData(x2, covariance);
        newCovarianceData(covariance);
    }

    protected void newCovarianceData(double[][] omega) {
        this.Omega = new Array2DRowRealMatrix(omega);
        this.OmegaInverse = null;
    }

    protected RealMatrix getOmegaInverse() {
        if (this.OmegaInverse == null) {
            this.OmegaInverse = new LUDecomposition(this.Omega).getSolver().getInverse();
        }
        return this.OmegaInverse;
    }

    @Override // org.apache.commons.math3.stat.regression.AbstractMultipleLinearRegression
    protected RealVector calculateBeta() throws DimensionMismatchException, SingularMatrixException {
        RealMatrix OI = getOmegaInverse();
        RealMatrix XT = getX().transpose();
        RealMatrix XTOIX = XT.multiply(OI).multiply(getX());
        RealMatrix inverse = new LUDecomposition(XTOIX).getSolver().getInverse();
        return inverse.multiply(XT).multiply(OI).operate(getY());
    }

    @Override // org.apache.commons.math3.stat.regression.AbstractMultipleLinearRegression
    protected RealMatrix calculateBetaVariance() throws DimensionMismatchException {
        RealMatrix OI = getOmegaInverse();
        RealMatrix XTOIX = getX().transpose().multiply(OI).multiply(getX());
        return new LUDecomposition(XTOIX).getSolver().getInverse();
    }

    @Override // org.apache.commons.math3.stat.regression.AbstractMultipleLinearRegression
    protected double calculateErrorVariance() throws DimensionMismatchException {
        RealVector residuals = calculateResiduals();
        double t2 = residuals.dotProduct(getOmegaInverse().operate(residuals));
        return t2 / (getX().getRowDimension() - getX().getColumnDimension());
    }
}
