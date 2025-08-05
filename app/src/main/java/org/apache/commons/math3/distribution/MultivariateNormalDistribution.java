package org.apache.commons.math3.distribution;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.EigenDecomposition;
import org.apache.commons.math3.linear.NonPositiveDefiniteMatrixException;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.SingularMatrixException;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.util.MathArrays;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/distribution/MultivariateNormalDistribution.class */
public class MultivariateNormalDistribution extends AbstractMultivariateRealDistribution {
    private final double[] means;
    private final RealMatrix covarianceMatrix;
    private final RealMatrix covarianceMatrixInverse;
    private final double covarianceMatrixDeterminant;
    private final RealMatrix samplingMatrix;

    public MultivariateNormalDistribution(double[] means, double[][] covariances) throws NonPositiveDefiniteMatrixException, SingularMatrixException, DimensionMismatchException {
        this(new Well19937c(), means, covariances);
    }

    public MultivariateNormalDistribution(RandomGenerator rng, double[] means, double[][] covariances) throws OutOfRangeException, NonPositiveDefiniteMatrixException, SingularMatrixException, DimensionMismatchException {
        super(rng, means.length);
        int dim = means.length;
        if (covariances.length != dim) {
            throw new DimensionMismatchException(covariances.length, dim);
        }
        for (int i2 = 0; i2 < dim; i2++) {
            if (dim != covariances[i2].length) {
                throw new DimensionMismatchException(covariances[i2].length, dim);
            }
        }
        this.means = MathArrays.copyOf(means);
        this.covarianceMatrix = new Array2DRowRealMatrix(covariances);
        EigenDecomposition covMatDec = new EigenDecomposition(this.covarianceMatrix);
        this.covarianceMatrixInverse = covMatDec.getSolver().getInverse();
        this.covarianceMatrixDeterminant = covMatDec.getDeterminant();
        double[] covMatEigenvalues = covMatDec.getRealEigenvalues();
        for (int i3 = 0; i3 < covMatEigenvalues.length; i3++) {
            if (covMatEigenvalues[i3] < 0.0d) {
                throw new NonPositiveDefiniteMatrixException(covMatEigenvalues[i3], i3, 0.0d);
            }
        }
        Array2DRowRealMatrix covMatEigenvectors = new Array2DRowRealMatrix(dim, dim);
        for (int v2 = 0; v2 < dim; v2++) {
            double[] evec = covMatDec.getEigenvector(v2).toArray();
            covMatEigenvectors.setColumn(v2, evec);
        }
        RealMatrix tmpMatrix = covMatEigenvectors.transpose();
        for (int row = 0; row < dim; row++) {
            double factor = FastMath.sqrt(covMatEigenvalues[row]);
            for (int col = 0; col < dim; col++) {
                tmpMatrix.multiplyEntry(row, col, factor);
            }
        }
        this.samplingMatrix = covMatEigenvectors.multiply(tmpMatrix);
    }

    public double[] getMeans() {
        return MathArrays.copyOf(this.means);
    }

    public RealMatrix getCovariances() {
        return this.covarianceMatrix.copy();
    }

    @Override // org.apache.commons.math3.distribution.MultivariateRealDistribution
    public double density(double[] vals) throws DimensionMismatchException {
        int dim = getDimension();
        if (vals.length != dim) {
            throw new DimensionMismatchException(vals.length, dim);
        }
        return FastMath.pow(6.283185307179586d, (-0.5d) * dim) * FastMath.pow(this.covarianceMatrixDeterminant, -0.5d) * getExponentTerm(vals);
    }

    public double[] getStandardDeviations() {
        int dim = getDimension();
        double[] std = new double[dim];
        double[][] s2 = this.covarianceMatrix.getData();
        for (int i2 = 0; i2 < dim; i2++) {
            std[i2] = FastMath.sqrt(s2[i2][i2]);
        }
        return std;
    }

    @Override // org.apache.commons.math3.distribution.AbstractMultivariateRealDistribution, org.apache.commons.math3.distribution.MultivariateRealDistribution
    public double[] sample() throws DimensionMismatchException {
        int dim = getDimension();
        double[] normalVals = new double[dim];
        for (int i2 = 0; i2 < dim; i2++) {
            normalVals[i2] = this.random.nextGaussian();
        }
        double[] vals = this.samplingMatrix.operate(normalVals);
        for (int i3 = 0; i3 < dim; i3++) {
            int i4 = i3;
            vals[i4] = vals[i4] + this.means[i3];
        }
        return vals;
    }

    private double getExponentTerm(double[] values) throws DimensionMismatchException {
        double[] centered = new double[values.length];
        for (int i2 = 0; i2 < centered.length; i2++) {
            centered[i2] = values[i2] - getMeans()[i2];
        }
        double[] preMultiplied = this.covarianceMatrixInverse.preMultiply(centered);
        double sum = 0.0d;
        for (int i3 = 0; i3 < preMultiplied.length; i3++) {
            sum += preMultiplied[i3] * centered[i3];
        }
        return FastMath.exp((-0.5d) * sum);
    }
}
