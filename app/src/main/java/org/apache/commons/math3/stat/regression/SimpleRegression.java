package org.apache.commons.math3.stat.regression;

import java.io.Serializable;
import org.apache.commons.math3.distribution.TDistribution;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NoDataException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.util.Precision;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/stat/regression/SimpleRegression.class */
public class SimpleRegression implements Serializable, UpdatingMultipleLinearRegression {
    private static final long serialVersionUID = -3004689053607543335L;
    private double sumX;
    private double sumXX;
    private double sumY;
    private double sumYY;
    private double sumXY;

    /* renamed from: n, reason: collision with root package name */
    private long f13111n;
    private double xbar;
    private double ybar;
    private final boolean hasIntercept;

    public SimpleRegression() {
        this(true);
    }

    public SimpleRegression(boolean includeIntercept) {
        this.sumX = 0.0d;
        this.sumXX = 0.0d;
        this.sumY = 0.0d;
        this.sumYY = 0.0d;
        this.sumXY = 0.0d;
        this.f13111n = 0L;
        this.xbar = 0.0d;
        this.ybar = 0.0d;
        this.hasIntercept = includeIntercept;
    }

    public void addData(double x2, double y2) {
        if (this.f13111n == 0) {
            this.xbar = x2;
            this.ybar = y2;
        } else if (this.hasIntercept) {
            double fact1 = 1.0d + this.f13111n;
            double fact2 = this.f13111n / (1.0d + this.f13111n);
            double dx = x2 - this.xbar;
            double dy = y2 - this.ybar;
            this.sumXX += dx * dx * fact2;
            this.sumYY += dy * dy * fact2;
            this.sumXY += dx * dy * fact2;
            this.xbar += dx / fact1;
            this.ybar += dy / fact1;
        }
        if (!this.hasIntercept) {
            this.sumXX += x2 * x2;
            this.sumYY += y2 * y2;
            this.sumXY += x2 * y2;
        }
        this.sumX += x2;
        this.sumY += y2;
        this.f13111n++;
    }

    public void append(SimpleRegression reg) {
        if (this.f13111n == 0) {
            this.xbar = reg.xbar;
            this.ybar = reg.ybar;
            this.sumXX = reg.sumXX;
            this.sumYY = reg.sumYY;
            this.sumXY = reg.sumXY;
        } else if (this.hasIntercept) {
            double fact1 = reg.f13111n / (reg.f13111n + this.f13111n);
            double fact2 = (this.f13111n * reg.f13111n) / (reg.f13111n + this.f13111n);
            double dx = reg.xbar - this.xbar;
            double dy = reg.ybar - this.ybar;
            this.sumXX += reg.sumXX + (dx * dx * fact2);
            this.sumYY += reg.sumYY + (dy * dy * fact2);
            this.sumXY += reg.sumXY + (dx * dy * fact2);
            this.xbar += dx * fact1;
            this.ybar += dy * fact1;
        } else {
            this.sumXX += reg.sumXX;
            this.sumYY += reg.sumYY;
            this.sumXY += reg.sumXY;
        }
        this.sumX += reg.sumX;
        this.sumY += reg.sumY;
        this.f13111n += reg.f13111n;
    }

    public void removeData(double x2, double y2) {
        if (this.f13111n > 0) {
            if (this.hasIntercept) {
                double fact1 = this.f13111n - 1.0d;
                double fact2 = this.f13111n / (this.f13111n - 1.0d);
                double dx = x2 - this.xbar;
                double dy = y2 - this.ybar;
                this.sumXX -= (dx * dx) * fact2;
                this.sumYY -= (dy * dy) * fact2;
                this.sumXY -= (dx * dy) * fact2;
                this.xbar -= dx / fact1;
                this.ybar -= dy / fact1;
            } else {
                double fact12 = this.f13111n - 1.0d;
                this.sumXX -= x2 * x2;
                this.sumYY -= y2 * y2;
                this.sumXY -= x2 * y2;
                this.xbar -= x2 / fact12;
                this.ybar -= y2 / fact12;
            }
            this.sumX -= x2;
            this.sumY -= y2;
            this.f13111n--;
        }
    }

    public void addData(double[][] data) throws ModelSpecificationException {
        for (int i2 = 0; i2 < data.length; i2++) {
            if (data[i2].length < 2) {
                throw new ModelSpecificationException(LocalizedFormats.INVALID_REGRESSION_OBSERVATION, Integer.valueOf(data[i2].length), 2);
            }
            addData(data[i2][0], data[i2][1]);
        }
    }

    @Override // org.apache.commons.math3.stat.regression.UpdatingMultipleLinearRegression
    public void addObservation(double[] x2, double y2) throws ModelSpecificationException {
        if (x2 == null || x2.length == 0) {
            LocalizedFormats localizedFormats = LocalizedFormats.INVALID_REGRESSION_OBSERVATION;
            Object[] objArr = new Object[2];
            objArr[0] = Integer.valueOf(x2 != null ? x2.length : 0);
            objArr[1] = 1;
            throw new ModelSpecificationException(localizedFormats, objArr);
        }
        addData(x2[0], y2);
    }

    @Override // org.apache.commons.math3.stat.regression.UpdatingMultipleLinearRegression
    public void addObservations(double[][] x2, double[] y2) throws ModelSpecificationException {
        if (x2 == null || y2 == null || x2.length != y2.length) {
            LocalizedFormats localizedFormats = LocalizedFormats.DIMENSIONS_MISMATCH_SIMPLE;
            Object[] objArr = new Object[2];
            objArr[0] = Integer.valueOf(x2 == null ? 0 : x2.length);
            objArr[1] = Integer.valueOf(y2 == null ? 0 : y2.length);
            throw new ModelSpecificationException(localizedFormats, objArr);
        }
        boolean obsOk = true;
        for (int i2 = 0; i2 < x2.length; i2++) {
            if (x2[i2] == null || x2[i2].length == 0) {
                obsOk = false;
            }
        }
        if (!obsOk) {
            throw new ModelSpecificationException(LocalizedFormats.NOT_ENOUGH_DATA_FOR_NUMBER_OF_PREDICTORS, 0, 1);
        }
        for (int i3 = 0; i3 < x2.length; i3++) {
            addData(x2[i3][0], y2[i3]);
        }
    }

    public void removeData(double[][] data) {
        for (int i2 = 0; i2 < data.length && this.f13111n > 0; i2++) {
            removeData(data[i2][0], data[i2][1]);
        }
    }

    @Override // org.apache.commons.math3.stat.regression.UpdatingMultipleLinearRegression
    public void clear() {
        this.sumX = 0.0d;
        this.sumXX = 0.0d;
        this.sumY = 0.0d;
        this.sumYY = 0.0d;
        this.sumXY = 0.0d;
        this.f13111n = 0L;
    }

    @Override // org.apache.commons.math3.stat.regression.UpdatingMultipleLinearRegression
    public long getN() {
        return this.f13111n;
    }

    public double predict(double x2) {
        double b1 = getSlope();
        if (this.hasIntercept) {
            return getIntercept(b1) + (b1 * x2);
        }
        return b1 * x2;
    }

    public double getIntercept() {
        if (this.hasIntercept) {
            return getIntercept(getSlope());
        }
        return 0.0d;
    }

    @Override // org.apache.commons.math3.stat.regression.UpdatingMultipleLinearRegression
    public boolean hasIntercept() {
        return this.hasIntercept;
    }

    public double getSlope() {
        if (this.f13111n < 2 || FastMath.abs(this.sumXX) < 4.9E-323d) {
            return Double.NaN;
        }
        return this.sumXY / this.sumXX;
    }

    public double getSumSquaredErrors() {
        return FastMath.max(0.0d, this.sumYY - ((this.sumXY * this.sumXY) / this.sumXX));
    }

    public double getTotalSumSquares() {
        if (this.f13111n < 2) {
            return Double.NaN;
        }
        return this.sumYY;
    }

    public double getXSumSquares() {
        if (this.f13111n < 2) {
            return Double.NaN;
        }
        return this.sumXX;
    }

    public double getSumOfCrossProducts() {
        return this.sumXY;
    }

    public double getRegressionSumSquares() {
        return getRegressionSumSquares(getSlope());
    }

    public double getMeanSquareError() {
        if (this.f13111n < 3) {
            return Double.NaN;
        }
        return this.hasIntercept ? getSumSquaredErrors() / (this.f13111n - 2) : getSumSquaredErrors() / (this.f13111n - 1);
    }

    public double getR() {
        double b1 = getSlope();
        double result = FastMath.sqrt(getRSquare());
        if (b1 < 0.0d) {
            result = -result;
        }
        return result;
    }

    public double getRSquare() {
        double ssto = getTotalSumSquares();
        return (ssto - getSumSquaredErrors()) / ssto;
    }

    public double getInterceptStdErr() {
        if (!this.hasIntercept) {
            return Double.NaN;
        }
        return FastMath.sqrt(getMeanSquareError() * ((1.0d / this.f13111n) + ((this.xbar * this.xbar) / this.sumXX)));
    }

    public double getSlopeStdErr() {
        return FastMath.sqrt(getMeanSquareError() / this.sumXX);
    }

    public double getSlopeConfidenceInterval() throws OutOfRangeException {
        return getSlopeConfidenceInterval(0.05d);
    }

    public double getSlopeConfidenceInterval(double alpha) throws OutOfRangeException {
        if (this.f13111n < 3) {
            return Double.NaN;
        }
        if (alpha >= 1.0d || alpha <= 0.0d) {
            throw new OutOfRangeException(LocalizedFormats.SIGNIFICANCE_LEVEL, Double.valueOf(alpha), 0, 1);
        }
        TDistribution distribution = new TDistribution(this.f13111n - 2);
        return getSlopeStdErr() * distribution.inverseCumulativeProbability(1.0d - (alpha / 2.0d));
    }

    public double getSignificance() {
        if (this.f13111n < 3) {
            return Double.NaN;
        }
        TDistribution distribution = new TDistribution(this.f13111n - 2);
        return 2.0d * (1.0d - distribution.cumulativeProbability(FastMath.abs(getSlope()) / getSlopeStdErr()));
    }

    private double getIntercept(double slope) {
        if (this.hasIntercept) {
            return (this.sumY - (slope * this.sumX)) / this.f13111n;
        }
        return 0.0d;
    }

    private double getRegressionSumSquares(double slope) {
        return slope * slope * this.sumXX;
    }

    /* JADX WARN: Type inference failed for: r3v11, types: [double[], double[][]] */
    /* JADX WARN: Type inference failed for: r3v22, types: [double[], double[][]] */
    /* JADX WARN: Type inference failed for: r3v3, types: [double[], double[][]] */
    /* JADX WARN: Type inference failed for: r3v37, types: [double[], double[][]] */
    @Override // org.apache.commons.math3.stat.regression.UpdatingMultipleLinearRegression
    public RegressionResults regress() throws NoDataException, ModelSpecificationException {
        if (!this.hasIntercept) {
            if (this.f13111n < 2) {
                throw new NoDataException(LocalizedFormats.NOT_ENOUGH_DATA_REGRESSION);
            }
            if (!Double.isNaN(this.sumXX)) {
                double[] vcv = {getMeanSquareError() / this.sumXX};
                double[] params = {this.sumXY / this.sumXX};
                return new RegressionResults(params, new double[]{vcv}, true, this.f13111n, 1, this.sumY, this.sumYY, getSumSquaredErrors(), false, false);
            }
            double[] vcv2 = {Double.NaN};
            double[] params2 = {Double.NaN};
            return new RegressionResults(params2, new double[]{vcv2}, true, this.f13111n, 1, Double.NaN, Double.NaN, Double.NaN, false, false);
        }
        if (this.f13111n < 3) {
            throw new NoDataException(LocalizedFormats.NOT_ENOUGH_DATA_REGRESSION);
        }
        if (FastMath.abs(this.sumXX) > Precision.SAFE_MIN) {
            double[] params3 = {getIntercept(), getSlope()};
            double mse = getMeanSquareError();
            double _syy = this.sumYY + ((this.sumY * this.sumY) / this.f13111n);
            double[] vcv3 = {mse * (((this.xbar * this.xbar) / this.sumXX) + (1.0d / this.f13111n)), ((-this.xbar) * mse) / this.sumXX, mse / this.sumXX};
            return new RegressionResults(params3, new double[]{vcv3}, true, this.f13111n, 2, this.sumY, _syy, getSumSquaredErrors(), true, false);
        }
        double[] params4 = {this.sumY / this.f13111n, Double.NaN};
        double[] vcv4 = {this.ybar / (this.f13111n - 1.0d), Double.NaN, Double.NaN};
        return new RegressionResults(params4, new double[]{vcv4}, true, this.f13111n, 1, this.sumY, this.sumYY, getSumSquaredErrors(), true, false);
    }

    /* JADX WARN: Type inference failed for: r3v12, types: [double[], double[][]] */
    /* JADX WARN: Type inference failed for: r3v18, types: [double[], double[][]] */
    /* JADX WARN: Type inference failed for: r3v25, types: [double[], double[][]] */
    @Override // org.apache.commons.math3.stat.regression.UpdatingMultipleLinearRegression
    public RegressionResults regress(int[] variablesToInclude) throws MathIllegalArgumentException {
        if (variablesToInclude == null || variablesToInclude.length == 0) {
            throw new MathIllegalArgumentException(LocalizedFormats.ARRAY_ZERO_LENGTH_OR_NULL_NOT_ALLOWED, new Object[0]);
        }
        if (variablesToInclude.length > 2 || (variablesToInclude.length > 1 && !this.hasIntercept)) {
            LocalizedFormats localizedFormats = LocalizedFormats.ARRAY_SIZE_EXCEEDS_MAX_VARIABLES;
            Object[] objArr = new Object[1];
            objArr[0] = Integer.valueOf((variablesToInclude.length <= 1 || this.hasIntercept) ? 2 : 1);
            throw new ModelSpecificationException(localizedFormats, objArr);
        }
        if (this.hasIntercept) {
            if (variablesToInclude.length == 2) {
                if (variablesToInclude[0] == 1) {
                    throw new ModelSpecificationException(LocalizedFormats.NOT_INCREASING_SEQUENCE, new Object[0]);
                }
                if (variablesToInclude[0] != 0) {
                    throw new OutOfRangeException(Integer.valueOf(variablesToInclude[0]), 0, 1);
                }
                if (variablesToInclude[1] != 1) {
                    throw new OutOfRangeException(Integer.valueOf(variablesToInclude[0]), 0, 1);
                }
                return regress();
            }
            if (variablesToInclude[0] != 1 && variablesToInclude[0] != 0) {
                throw new OutOfRangeException(Integer.valueOf(variablesToInclude[0]), 0, 1);
            }
            double _mean = (this.sumY * this.sumY) / this.f13111n;
            double _syy = this.sumYY + _mean;
            if (variablesToInclude[0] == 0) {
                double[] vcv = {this.sumYY / ((this.f13111n - 1) * this.f13111n)};
                double[] params = {this.ybar};
                return new RegressionResults(params, new double[]{vcv}, true, this.f13111n, 1, this.sumY, _syy + _mean, this.sumYY, true, false);
            }
            if (variablesToInclude[0] == 1) {
                double _sxx = this.sumXX + ((this.sumX * this.sumX) / this.f13111n);
                double _sxy = this.sumXY + ((this.sumX * this.sumY) / this.f13111n);
                double _sse = FastMath.max(0.0d, _syy - ((_sxy * _sxy) / _sxx));
                double _mse = _sse / (this.f13111n - 1);
                if (!Double.isNaN(_sxx)) {
                    double[] vcv2 = {_mse / _sxx};
                    double[] params2 = {_sxy / _sxx};
                    return new RegressionResults(params2, new double[]{vcv2}, true, this.f13111n, 1, this.sumY, _syy, _sse, false, false);
                }
                double[] vcv3 = {Double.NaN};
                double[] params3 = {Double.NaN};
                return new RegressionResults(params3, new double[]{vcv3}, true, this.f13111n, 1, Double.NaN, Double.NaN, Double.NaN, false, false);
            }
            return null;
        }
        if (variablesToInclude[0] != 0) {
            throw new OutOfRangeException(Integer.valueOf(variablesToInclude[0]), 0, 0);
        }
        return regress();
    }
}
