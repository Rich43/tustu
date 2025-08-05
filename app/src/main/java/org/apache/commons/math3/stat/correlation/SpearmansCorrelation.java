package org.apache.commons.math3.stat.correlation;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.linear.BlockRealMatrix;
import org.apache.commons.math3.linear.MatrixDimensionMismatchException;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.stat.ranking.NaNStrategy;
import org.apache.commons.math3.stat.ranking.NaturalRanking;
import org.apache.commons.math3.stat.ranking.RankingAlgorithm;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/stat/correlation/SpearmansCorrelation.class */
public class SpearmansCorrelation {
    private final RealMatrix data;
    private final RankingAlgorithm rankingAlgorithm;
    private final PearsonsCorrelation rankCorrelation;

    public SpearmansCorrelation() {
        this(new NaturalRanking());
    }

    public SpearmansCorrelation(RankingAlgorithm rankingAlgorithm) {
        this.data = null;
        this.rankingAlgorithm = rankingAlgorithm;
        this.rankCorrelation = null;
    }

    public SpearmansCorrelation(RealMatrix dataMatrix) {
        this(dataMatrix, new NaturalRanking());
    }

    public SpearmansCorrelation(RealMatrix dataMatrix, RankingAlgorithm rankingAlgorithm) {
        this.rankingAlgorithm = rankingAlgorithm;
        this.data = rankTransform(dataMatrix);
        this.rankCorrelation = new PearsonsCorrelation(this.data);
    }

    public RealMatrix getCorrelationMatrix() {
        return this.rankCorrelation.getCorrelationMatrix();
    }

    public PearsonsCorrelation getRankCorrelation() {
        return this.rankCorrelation;
    }

    public RealMatrix computeCorrelationMatrix(RealMatrix matrix) throws OutOfRangeException, MatrixDimensionMismatchException {
        RealMatrix matrixCopy = rankTransform(matrix);
        return new PearsonsCorrelation().computeCorrelationMatrix(matrixCopy);
    }

    public RealMatrix computeCorrelationMatrix(double[][] matrix) {
        return computeCorrelationMatrix(new BlockRealMatrix(matrix));
    }

    public double correlation(double[] xArray, double[] yArray) {
        if (xArray.length != yArray.length) {
            throw new DimensionMismatchException(xArray.length, yArray.length);
        }
        if (xArray.length < 2) {
            throw new MathIllegalArgumentException(LocalizedFormats.INSUFFICIENT_DIMENSION, Integer.valueOf(xArray.length), 2);
        }
        double[] x2 = xArray;
        double[] y2 = yArray;
        if ((this.rankingAlgorithm instanceof NaturalRanking) && NaNStrategy.REMOVED == ((NaturalRanking) this.rankingAlgorithm).getNanStrategy()) {
            Set<Integer> nanPositions = new HashSet<>();
            nanPositions.addAll(getNaNPositions(xArray));
            nanPositions.addAll(getNaNPositions(yArray));
            x2 = removeValues(xArray, nanPositions);
            y2 = removeValues(yArray, nanPositions);
        }
        return new PearsonsCorrelation().correlation(this.rankingAlgorithm.rank(x2), this.rankingAlgorithm.rank(y2));
    }

    private RealMatrix rankTransform(RealMatrix matrix) throws OutOfRangeException, MatrixDimensionMismatchException {
        RealMatrix transformed = null;
        if ((this.rankingAlgorithm instanceof NaturalRanking) && ((NaturalRanking) this.rankingAlgorithm).getNanStrategy() == NaNStrategy.REMOVED) {
            Set<Integer> nanPositions = new HashSet<>();
            for (int i2 = 0; i2 < matrix.getColumnDimension(); i2++) {
                nanPositions.addAll(getNaNPositions(matrix.getColumn(i2)));
            }
            if (!nanPositions.isEmpty()) {
                transformed = new BlockRealMatrix(matrix.getRowDimension() - nanPositions.size(), matrix.getColumnDimension());
                for (int i3 = 0; i3 < transformed.getColumnDimension(); i3++) {
                    transformed.setColumn(i3, removeValues(matrix.getColumn(i3), nanPositions));
                }
            }
        }
        if (transformed == null) {
            transformed = matrix.copy();
        }
        for (int i4 = 0; i4 < transformed.getColumnDimension(); i4++) {
            transformed.setColumn(i4, this.rankingAlgorithm.rank(transformed.getColumn(i4)));
        }
        return transformed;
    }

    private List<Integer> getNaNPositions(double[] input) {
        List<Integer> positions = new ArrayList<>();
        for (int i2 = 0; i2 < input.length; i2++) {
            if (Double.isNaN(input[i2])) {
                positions.add(Integer.valueOf(i2));
            }
        }
        return positions;
    }

    private double[] removeValues(double[] input, Set<Integer> indices) {
        if (indices.isEmpty()) {
            return input;
        }
        double[] result = new double[input.length - indices.size()];
        int j2 = 0;
        for (int i2 = 0; i2 < input.length; i2++) {
            if (!indices.contains(Integer.valueOf(i2))) {
                int i3 = j2;
                j2++;
                result[i3] = input[i2];
            }
        }
        return result;
    }
}
