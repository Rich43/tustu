package org.apache.commons.math3.distribution.fitting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.math3.distribution.MixtureMultivariateNormalDistribution;
import org.apache.commons.math3.distribution.MultivariateNormalDistribution;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.SingularMatrixException;
import org.apache.commons.math3.stat.correlation.Covariance;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.util.MathArrays;
import org.apache.commons.math3.util.Pair;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/distribution/fitting/MultivariateNormalMixtureExpectationMaximization.class */
public class MultivariateNormalMixtureExpectationMaximization {
    private static final int DEFAULT_MAX_ITERATIONS = 1000;
    private static final double DEFAULT_THRESHOLD = 1.0E-5d;
    private final double[][] data;
    private MixtureMultivariateNormalDistribution fittedModel;
    private double logLikelihood = 0.0d;

    public MultivariateNormalMixtureExpectationMaximization(double[][] data) throws NumberIsTooSmallException, DimensionMismatchException {
        if (data.length < 1) {
            throw new NotStrictlyPositiveException(Integer.valueOf(data.length));
        }
        this.data = new double[data.length][data[0].length];
        for (int i2 = 0; i2 < data.length; i2++) {
            if (data[i2].length != data[0].length) {
                throw new DimensionMismatchException(data[i2].length, data[0].length);
            }
            if (data[i2].length < 2) {
                throw new NumberIsTooSmallException(LocalizedFormats.NUMBER_TOO_SMALL, Integer.valueOf(data[i2].length), 2, true);
            }
            this.data[i2] = MathArrays.copyOf(data[i2], data[i2].length);
        }
    }

    public void fit(MixtureMultivariateNormalDistribution initialMixture, int maxIterations, double threshold) throws NotStrictlyPositiveException, SingularMatrixException, DimensionMismatchException {
        if (maxIterations < 1) {
            throw new NotStrictlyPositiveException(Integer.valueOf(maxIterations));
        }
        if (threshold < Double.MIN_VALUE) {
            throw new NotStrictlyPositiveException(Double.valueOf(threshold));
        }
        int n2 = this.data.length;
        int numCols = this.data[0].length;
        int k2 = initialMixture.getComponents().size();
        int numMeanColumns = initialMixture.getComponents().get(0).getSecond().getMeans().length;
        if (numMeanColumns != numCols) {
            throw new DimensionMismatchException(numMeanColumns, numCols);
        }
        int numIterations = 0;
        double previousLogLikelihood = 0.0d;
        this.logLikelihood = Double.NEGATIVE_INFINITY;
        this.fittedModel = new MixtureMultivariateNormalDistribution(initialMixture.getComponents());
        while (true) {
            int i2 = numIterations;
            numIterations++;
            if (i2 > maxIterations || FastMath.abs(previousLogLikelihood - this.logLikelihood) <= threshold) {
                break;
            }
            previousLogLikelihood = this.logLikelihood;
            double sumLogLikelihood = 0.0d;
            List<Pair<Double, MultivariateNormalDistribution>> components = this.fittedModel.getComponents();
            double[] weights = new double[k2];
            MultivariateNormalDistribution[] mvns = new MultivariateNormalDistribution[k2];
            for (int j2 = 0; j2 < k2; j2++) {
                weights[j2] = components.get(j2).getFirst().doubleValue();
                mvns[j2] = components.get(j2).getSecond();
            }
            double[][] gamma = new double[n2][k2];
            double[] gammaSums = new double[k2];
            double[][] gammaDataProdSums = new double[k2][numCols];
            for (int i3 = 0; i3 < n2; i3++) {
                double rowDensity = this.fittedModel.density(this.data[i3]);
                sumLogLikelihood += FastMath.log(rowDensity);
                for (int j3 = 0; j3 < k2; j3++) {
                    gamma[i3][j3] = (weights[j3] * mvns[j3].density(this.data[i3])) / rowDensity;
                    int i4 = j3;
                    gammaSums[i4] = gammaSums[i4] + gamma[i3][j3];
                    for (int col = 0; col < numCols; col++) {
                        double[] dArr = gammaDataProdSums[j3];
                        int i5 = col;
                        dArr[i5] = dArr[i5] + (gamma[i3][j3] * this.data[i3][col]);
                    }
                }
            }
            this.logLikelihood = sumLogLikelihood / n2;
            double[] newWeights = new double[k2];
            double[][] newMeans = new double[k2][numCols];
            for (int j4 = 0; j4 < k2; j4++) {
                newWeights[j4] = gammaSums[j4] / n2;
                for (int col2 = 0; col2 < numCols; col2++) {
                    newMeans[j4][col2] = gammaDataProdSums[j4][col2] / gammaSums[j4];
                }
            }
            RealMatrix[] newCovMats = new RealMatrix[k2];
            for (int j5 = 0; j5 < k2; j5++) {
                newCovMats[j5] = new Array2DRowRealMatrix(numCols, numCols);
            }
            for (int i6 = 0; i6 < n2; i6++) {
                for (int j6 = 0; j6 < k2; j6++) {
                    RealMatrix vec = new Array2DRowRealMatrix(MathArrays.ebeSubtract(this.data[i6], newMeans[j6]));
                    RealMatrix dataCov = vec.multiply(vec.transpose()).scalarMultiply(gamma[i6][j6]);
                    newCovMats[j6] = newCovMats[j6].add(dataCov);
                }
            }
            double[][][] newCovMatArrays = new double[k2][numCols][numCols];
            for (int j7 = 0; j7 < k2; j7++) {
                newCovMats[j7] = newCovMats[j7].scalarMultiply(1.0d / gammaSums[j7]);
                newCovMatArrays[j7] = newCovMats[j7].getData();
            }
            this.fittedModel = new MixtureMultivariateNormalDistribution(newWeights, newMeans, newCovMatArrays);
        }
        if (FastMath.abs(previousLogLikelihood - this.logLikelihood) > threshold) {
            throw new ConvergenceException();
        }
    }

    public void fit(MixtureMultivariateNormalDistribution initialMixture) throws NotStrictlyPositiveException, SingularMatrixException, DimensionMismatchException {
        fit(initialMixture, 1000, DEFAULT_THRESHOLD);
    }

    public static MixtureMultivariateNormalDistribution estimate(double[][] data, int numComponents) throws NotStrictlyPositiveException, DimensionMismatchException {
        if (data.length < 2) {
            throw new NotStrictlyPositiveException(Integer.valueOf(data.length));
        }
        if (numComponents < 2) {
            throw new NumberIsTooSmallException(Integer.valueOf(numComponents), 2, true);
        }
        if (numComponents > data.length) {
            throw new NumberIsTooLargeException(Integer.valueOf(numComponents), Integer.valueOf(data.length), true);
        }
        int numRows = data.length;
        int numCols = data[0].length;
        DataRow[] sortedData = new DataRow[numRows];
        for (int i2 = 0; i2 < numRows; i2++) {
            sortedData[i2] = new DataRow(data[i2]);
        }
        Arrays.sort(sortedData);
        double weight = 1.0d / numComponents;
        List<Pair<Double, MultivariateNormalDistribution>> components = new ArrayList<>(numComponents);
        for (int binIndex = 0; binIndex < numComponents; binIndex++) {
            int minIndex = (binIndex * numRows) / numComponents;
            int maxIndex = ((binIndex + 1) * numRows) / numComponents;
            int numBinRows = maxIndex - minIndex;
            double[][] binData = new double[numBinRows][numCols];
            double[] columnMeans = new double[numCols];
            int i3 = minIndex;
            int iBin = 0;
            while (i3 < maxIndex) {
                for (int j2 = 0; j2 < numCols; j2++) {
                    double val = sortedData[i3].getRow()[j2];
                    int i4 = j2;
                    columnMeans[i4] = columnMeans[i4] + val;
                    binData[iBin][j2] = val;
                }
                i3++;
                iBin++;
            }
            MathArrays.scaleInPlace(1.0d / numBinRows, columnMeans);
            double[][] covMat = new Covariance(binData).getCovarianceMatrix().getData();
            MultivariateNormalDistribution mvn = new MultivariateNormalDistribution(columnMeans, covMat);
            components.add(new Pair<>(Double.valueOf(weight), mvn));
        }
        return new MixtureMultivariateNormalDistribution(components);
    }

    public double getLogLikelihood() {
        return this.logLikelihood;
    }

    public MixtureMultivariateNormalDistribution getFittedModel() {
        return new MixtureMultivariateNormalDistribution(this.fittedModel.getComponents());
    }

    /* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/distribution/fitting/MultivariateNormalMixtureExpectationMaximization$DataRow.class */
    private static class DataRow implements Comparable<DataRow> {
        private final double[] row;
        private Double mean;

        DataRow(double[] data) {
            this.row = data;
            this.mean = Double.valueOf(0.0d);
            for (double d2 : data) {
                this.mean = Double.valueOf(this.mean.doubleValue() + d2);
            }
            this.mean = Double.valueOf(this.mean.doubleValue() / data.length);
        }

        @Override // java.lang.Comparable
        public int compareTo(DataRow other) {
            return this.mean.compareTo(other.mean);
        }

        public boolean equals(Object other) {
            if (this == other) {
                return true;
            }
            if (other instanceof DataRow) {
                return MathArrays.equals(this.row, ((DataRow) other).row);
            }
            return false;
        }

        public int hashCode() {
            return Arrays.hashCode(this.row);
        }

        public double[] getRow() {
            return this.row;
        }
    }
}
