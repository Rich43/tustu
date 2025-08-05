package org.apache.commons.math3.stat.regression;

import java.io.Serializable;
import java.util.Arrays;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.util.MathArrays;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/stat/regression/RegressionResults.class */
public class RegressionResults implements Serializable {
    private static final int SSE_IDX = 0;
    private static final int SST_IDX = 1;
    private static final int RSQ_IDX = 2;
    private static final int MSE_IDX = 3;
    private static final int ADJRSQ_IDX = 4;
    private static final long serialVersionUID = 1;
    private final double[] parameters;
    private final double[][] varCovData;
    private final boolean isSymmetricVCD;
    private final int rank;
    private final long nobs;
    private final boolean containsConstant;
    private final double[] globalFitInfo;

    private RegressionResults() {
        this.parameters = null;
        this.varCovData = (double[][]) null;
        this.rank = -1;
        this.nobs = -1L;
        this.containsConstant = false;
        this.isSymmetricVCD = false;
        this.globalFitInfo = null;
    }

    /* JADX WARN: Type inference failed for: r1v19, types: [double[], double[][]] */
    public RegressionResults(double[] parameters, double[][] varcov, boolean isSymmetricCompressed, long nobs, int rank, double sumy, double sumysq, double sse, boolean containsConstant, boolean copyData) {
        if (copyData) {
            this.parameters = MathArrays.copyOf(parameters);
            this.varCovData = new double[varcov.length];
            for (int i2 = 0; i2 < varcov.length; i2++) {
                this.varCovData[i2] = MathArrays.copyOf(varcov[i2]);
            }
        } else {
            this.parameters = parameters;
            this.varCovData = varcov;
        }
        this.isSymmetricVCD = isSymmetricCompressed;
        this.nobs = nobs;
        this.rank = rank;
        this.containsConstant = containsConstant;
        this.globalFitInfo = new double[5];
        Arrays.fill(this.globalFitInfo, Double.NaN);
        if (rank > 0) {
            this.globalFitInfo[1] = containsConstant ? sumysq - ((sumy * sumy) / nobs) : sumysq;
        }
        this.globalFitInfo[0] = sse;
        this.globalFitInfo[3] = this.globalFitInfo[0] / (nobs - rank);
        this.globalFitInfo[2] = 1.0d - (this.globalFitInfo[0] / this.globalFitInfo[1]);
        if (!containsConstant) {
            this.globalFitInfo[4] = 1.0d - ((1.0d - this.globalFitInfo[2]) * (nobs / (nobs - rank)));
        } else {
            this.globalFitInfo[4] = 1.0d - ((sse * (nobs - 1.0d)) / (this.globalFitInfo[1] * (nobs - rank)));
        }
    }

    public double getParameterEstimate(int index) throws OutOfRangeException {
        if (this.parameters == null) {
            return Double.NaN;
        }
        if (index < 0 || index >= this.parameters.length) {
            throw new OutOfRangeException(Integer.valueOf(index), 0, Integer.valueOf(this.parameters.length - 1));
        }
        return this.parameters[index];
    }

    public double[] getParameterEstimates() {
        if (this.parameters == null) {
            return null;
        }
        return MathArrays.copyOf(this.parameters);
    }

    public double getStdErrorOfEstimate(int index) throws OutOfRangeException {
        if (this.parameters == null) {
            return Double.NaN;
        }
        if (index < 0 || index >= this.parameters.length) {
            throw new OutOfRangeException(Integer.valueOf(index), 0, Integer.valueOf(this.parameters.length - 1));
        }
        double var = getVcvElement(index, index);
        if (!Double.isNaN(var) && var > Double.MIN_VALUE) {
            return FastMath.sqrt(var);
        }
        return Double.NaN;
    }

    public double[] getStdErrorOfEstimates() {
        if (this.parameters == null) {
            return null;
        }
        double[] se = new double[this.parameters.length];
        for (int i2 = 0; i2 < this.parameters.length; i2++) {
            double var = getVcvElement(i2, i2);
            if (!Double.isNaN(var) && var > Double.MIN_VALUE) {
                se[i2] = FastMath.sqrt(var);
            } else {
                se[i2] = Double.NaN;
            }
        }
        return se;
    }

    public double getCovarianceOfParameters(int i2, int j2) throws OutOfRangeException {
        if (this.parameters == null) {
            return Double.NaN;
        }
        if (i2 < 0 || i2 >= this.parameters.length) {
            throw new OutOfRangeException(Integer.valueOf(i2), 0, Integer.valueOf(this.parameters.length - 1));
        }
        if (j2 < 0 || j2 >= this.parameters.length) {
            throw new OutOfRangeException(Integer.valueOf(j2), 0, Integer.valueOf(this.parameters.length - 1));
        }
        return getVcvElement(i2, j2);
    }

    public int getNumberOfParameters() {
        if (this.parameters == null) {
            return -1;
        }
        return this.parameters.length;
    }

    public long getN() {
        return this.nobs;
    }

    public double getTotalSumSquares() {
        return this.globalFitInfo[1];
    }

    public double getRegressionSumSquares() {
        return this.globalFitInfo[1] - this.globalFitInfo[0];
    }

    public double getErrorSumSquares() {
        return this.globalFitInfo[0];
    }

    public double getMeanSquareError() {
        return this.globalFitInfo[3];
    }

    public double getRSquared() {
        return this.globalFitInfo[2];
    }

    public double getAdjustedRSquared() {
        return this.globalFitInfo[4];
    }

    public boolean hasIntercept() {
        return this.containsConstant;
    }

    private double getVcvElement(int i2, int j2) {
        if (this.isSymmetricVCD) {
            if (this.varCovData.length > 1) {
                if (i2 == j2) {
                    return this.varCovData[i2][i2];
                }
                if (i2 >= this.varCovData[j2].length) {
                    return this.varCovData[i2][j2];
                }
                return this.varCovData[j2][i2];
            }
            if (i2 > j2) {
                return this.varCovData[0][(((i2 + 1) * i2) / 2) + j2];
            }
            return this.varCovData[0][(((j2 + 1) * j2) / 2) + i2];
        }
        return this.varCovData[i2][j2];
    }
}
