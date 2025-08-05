package org.apache.commons.math3.stat.regression;

import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.commons.math3.linear.QRDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.SingularMatrixException;
import org.apache.commons.math3.stat.StatUtils;
import org.apache.commons.math3.stat.descriptive.moment.SecondMoment;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/stat/regression/OLSMultipleLinearRegression.class */
public class OLSMultipleLinearRegression extends AbstractMultipleLinearRegression {
    private QRDecomposition qr;
    private final double threshold;

    public OLSMultipleLinearRegression() {
        this(0.0d);
    }

    public OLSMultipleLinearRegression(double threshold) {
        this.qr = null;
        this.threshold = threshold;
    }

    public void newSampleData(double[] y2, double[][] x2) throws MathIllegalArgumentException {
        validateSampleData(x2, y2);
        newYSampleData(y2);
        newXSampleData(x2);
    }

    @Override // org.apache.commons.math3.stat.regression.AbstractMultipleLinearRegression
    public void newSampleData(double[] data, int nobs, int nvars) {
        super.newSampleData(data, nobs, nvars);
        this.qr = new QRDecomposition(getX(), this.threshold);
    }

    public RealMatrix calculateHat() {
        RealMatrix Q2 = this.qr.getQ();
        int p2 = this.qr.getR().getColumnDimension();
        int n2 = Q2.getColumnDimension();
        Array2DRowRealMatrix augI = new Array2DRowRealMatrix(n2, n2);
        double[][] augIData = augI.getDataRef();
        for (int i2 = 0; i2 < n2; i2++) {
            for (int j2 = 0; j2 < n2; j2++) {
                if (i2 == j2 && i2 < p2) {
                    augIData[i2][j2] = 1.0d;
                } else {
                    augIData[i2][j2] = 0.0d;
                }
            }
        }
        return Q2.multiply(augI).multiply(Q2.transpose());
    }

    public double calculateTotalSumOfSquares() {
        if (isNoIntercept()) {
            return StatUtils.sumSq(getY().toArray());
        }
        return new SecondMoment().evaluate(getY().toArray());
    }

    public double calculateResidualSumOfSquares() {
        RealVector residuals = calculateResiduals();
        return residuals.dotProduct(residuals);
    }

    public double calculateRSquared() {
        return 1.0d - (calculateResidualSumOfSquares() / calculateTotalSumOfSquares());
    }

    public double calculateAdjustedRSquared() {
        double n2 = getX().getRowDimension();
        if (isNoIntercept()) {
            return 1.0d - ((1.0d - calculateRSquared()) * (n2 / (n2 - getX().getColumnDimension())));
        }
        return 1.0d - ((calculateResidualSumOfSquares() * (n2 - 1.0d)) / (calculateTotalSumOfSquares() * (n2 - getX().getColumnDimension())));
    }

    @Override // org.apache.commons.math3.stat.regression.AbstractMultipleLinearRegression
    protected void newXSampleData(double[][] x2) {
        super.newXSampleData(x2);
        this.qr = new QRDecomposition(getX(), this.threshold);
    }

    @Override // org.apache.commons.math3.stat.regression.AbstractMultipleLinearRegression
    protected RealVector calculateBeta() {
        return this.qr.getSolver().solve(getY());
    }

    @Override // org.apache.commons.math3.stat.regression.AbstractMultipleLinearRegression
    protected RealMatrix calculateBetaVariance() throws NumberIsTooSmallException, OutOfRangeException, SingularMatrixException {
        int p2 = getX().getColumnDimension();
        RealMatrix Raug = this.qr.getR().getSubMatrix(0, p2 - 1, 0, p2 - 1);
        RealMatrix Rinv = new LUDecomposition(Raug).getSolver().getInverse();
        return Rinv.multiply(Rinv.transpose());
    }
}
