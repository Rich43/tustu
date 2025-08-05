package org.apache.commons.math3.stat.correlation;

import org.apache.commons.math3.distribution.TDistribution;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.linear.BlockRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.stat.regression.SimpleRegression;
import org.apache.commons.math3.util.FastMath;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/stat/correlation/PearsonsCorrelation.class */
public class PearsonsCorrelation {
    private final RealMatrix correlationMatrix;
    private final int nObs;

    public PearsonsCorrelation() {
        this.correlationMatrix = null;
        this.nObs = 0;
    }

    public PearsonsCorrelation(double[][] data) {
        this(new BlockRealMatrix(data));
    }

    public PearsonsCorrelation(RealMatrix matrix) {
        this.nObs = matrix.getRowDimension();
        this.correlationMatrix = computeCorrelationMatrix(matrix);
    }

    public PearsonsCorrelation(Covariance covariance) {
        RealMatrix covarianceMatrix = covariance.getCovarianceMatrix();
        if (covarianceMatrix == null) {
            throw new NullArgumentException(LocalizedFormats.COVARIANCE_MATRIX, new Object[0]);
        }
        this.nObs = covariance.getN();
        this.correlationMatrix = covarianceToCorrelation(covarianceMatrix);
    }

    public PearsonsCorrelation(RealMatrix covarianceMatrix, int numberOfObservations) {
        this.nObs = numberOfObservations;
        this.correlationMatrix = covarianceToCorrelation(covarianceMatrix);
    }

    public RealMatrix getCorrelationMatrix() {
        return this.correlationMatrix;
    }

    public RealMatrix getCorrelationStandardErrors() throws OutOfRangeException {
        int nVars = this.correlationMatrix.getColumnDimension();
        double[][] out = new double[nVars][nVars];
        for (int i2 = 0; i2 < nVars; i2++) {
            for (int j2 = 0; j2 < nVars; j2++) {
                double r2 = this.correlationMatrix.getEntry(i2, j2);
                out[i2][j2] = FastMath.sqrt((1.0d - (r2 * r2)) / (this.nObs - 2));
            }
        }
        return new BlockRealMatrix(out);
    }

    public RealMatrix getCorrelationPValues() throws OutOfRangeException {
        TDistribution tDistribution = new TDistribution(this.nObs - 2);
        int nVars = this.correlationMatrix.getColumnDimension();
        double[][] out = new double[nVars][nVars];
        for (int i2 = 0; i2 < nVars; i2++) {
            for (int j2 = 0; j2 < nVars; j2++) {
                if (i2 == j2) {
                    out[i2][j2] = 0.0d;
                } else {
                    double r2 = this.correlationMatrix.getEntry(i2, j2);
                    double t2 = FastMath.abs(r2 * FastMath.sqrt((this.nObs - 2) / (1.0d - (r2 * r2))));
                    out[i2][j2] = 2.0d * tDistribution.cumulativeProbability(-t2);
                }
            }
        }
        return new BlockRealMatrix(out);
    }

    public RealMatrix computeCorrelationMatrix(RealMatrix matrix) throws OutOfRangeException {
        checkSufficientData(matrix);
        int nVars = matrix.getColumnDimension();
        RealMatrix outMatrix = new BlockRealMatrix(nVars, nVars);
        for (int i2 = 0; i2 < nVars; i2++) {
            for (int j2 = 0; j2 < i2; j2++) {
                double corr = correlation(matrix.getColumn(i2), matrix.getColumn(j2));
                outMatrix.setEntry(i2, j2, corr);
                outMatrix.setEntry(j2, i2, corr);
            }
            outMatrix.setEntry(i2, i2, 1.0d);
        }
        return outMatrix;
    }

    public RealMatrix computeCorrelationMatrix(double[][] data) {
        return computeCorrelationMatrix(new BlockRealMatrix(data));
    }

    public double correlation(double[] xArray, double[] yArray) {
        SimpleRegression regression = new SimpleRegression();
        if (xArray.length != yArray.length) {
            throw new DimensionMismatchException(xArray.length, yArray.length);
        }
        if (xArray.length < 2) {
            throw new MathIllegalArgumentException(LocalizedFormats.INSUFFICIENT_DIMENSION, Integer.valueOf(xArray.length), 2);
        }
        for (int i2 = 0; i2 < xArray.length; i2++) {
            regression.addData(xArray[i2], yArray[i2]);
        }
        return regression.getR();
    }

    public RealMatrix covarianceToCorrelation(RealMatrix covarianceMatrix) throws OutOfRangeException {
        int nVars = covarianceMatrix.getColumnDimension();
        RealMatrix outMatrix = new BlockRealMatrix(nVars, nVars);
        for (int i2 = 0; i2 < nVars; i2++) {
            double sigma = FastMath.sqrt(covarianceMatrix.getEntry(i2, i2));
            outMatrix.setEntry(i2, i2, 1.0d);
            for (int j2 = 0; j2 < i2; j2++) {
                double entry = covarianceMatrix.getEntry(i2, j2) / (sigma * FastMath.sqrt(covarianceMatrix.getEntry(j2, j2)));
                outMatrix.setEntry(i2, j2, entry);
                outMatrix.setEntry(j2, i2, entry);
            }
        }
        return outMatrix;
    }

    private void checkSufficientData(RealMatrix matrix) {
        int nRows = matrix.getRowDimension();
        int nCols = matrix.getColumnDimension();
        if (nRows < 2 || nCols < 2) {
            throw new MathIllegalArgumentException(LocalizedFormats.INSUFFICIENT_ROWS_AND_COLUMNS, Integer.valueOf(nRows), Integer.valueOf(nCols));
        }
    }
}
