package org.apache.commons.math3.optim.nonlinear.scalar.noderiv;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.exception.TooManyEvaluationsException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.EigenDecomposition;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.optim.ConvergenceChecker;
import org.apache.commons.math3.optim.OptimizationData;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.nonlinear.scalar.MultivariateOptimizer;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.util.MathArrays;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/optim/nonlinear/scalar/noderiv/CMAESOptimizer.class */
public class CMAESOptimizer extends MultivariateOptimizer {
    private int lambda;
    private final boolean isActiveCMA;
    private final int checkFeasableCount;
    private double[] inputSigma;
    private int dimension;
    private int diagonalOnly;
    private boolean isMinimize;
    private final boolean generateStatistics;
    private final int maxIterations;
    private final double stopFitness;
    private double stopTolUpX;
    private double stopTolX;
    private double stopTolFun;
    private double stopTolHistFun;
    private int mu;
    private double logMu2;
    private RealMatrix weights;
    private double mueff;
    private double sigma;
    private double cc;
    private double cs;
    private double damps;
    private double ccov1;
    private double ccovmu;
    private double chiN;
    private double ccov1Sep;
    private double ccovmuSep;
    private RealMatrix xmean;
    private RealMatrix pc;
    private RealMatrix ps;
    private double normps;

    /* renamed from: B, reason: collision with root package name */
    private RealMatrix f13071B;

    /* renamed from: D, reason: collision with root package name */
    private RealMatrix f13072D;
    private RealMatrix BD;
    private RealMatrix diagD;

    /* renamed from: C, reason: collision with root package name */
    private RealMatrix f13073C;
    private RealMatrix diagC;
    private int iterations;
    private double[] fitnessHistory;
    private int historySize;
    private final RandomGenerator random;
    private final List<Double> statisticsSigmaHistory;
    private final List<RealMatrix> statisticsMeanHistory;
    private final List<Double> statisticsFitnessHistory;
    private final List<RealMatrix> statisticsDHistory;

    public CMAESOptimizer(int maxIterations, double stopFitness, boolean isActiveCMA, int diagonalOnly, int checkFeasableCount, RandomGenerator random, boolean generateStatistics, ConvergenceChecker<PointValuePair> checker) {
        super(checker);
        this.isMinimize = true;
        this.statisticsSigmaHistory = new ArrayList();
        this.statisticsMeanHistory = new ArrayList();
        this.statisticsFitnessHistory = new ArrayList();
        this.statisticsDHistory = new ArrayList();
        this.maxIterations = maxIterations;
        this.stopFitness = stopFitness;
        this.isActiveCMA = isActiveCMA;
        this.diagonalOnly = diagonalOnly;
        this.checkFeasableCount = checkFeasableCount;
        this.random = random;
        this.generateStatistics = generateStatistics;
    }

    public List<Double> getStatisticsSigmaHistory() {
        return this.statisticsSigmaHistory;
    }

    public List<RealMatrix> getStatisticsMeanHistory() {
        return this.statisticsMeanHistory;
    }

    public List<Double> getStatisticsFitnessHistory() {
        return this.statisticsFitnessHistory;
    }

    public List<RealMatrix> getStatisticsDHistory() {
        return this.statisticsDHistory;
    }

    /* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/optim/nonlinear/scalar/noderiv/CMAESOptimizer$Sigma.class */
    public static class Sigma implements OptimizationData {
        private final double[] sigma;

        public Sigma(double[] s2) throws NotPositiveException {
            for (int i2 = 0; i2 < s2.length; i2++) {
                if (s2[i2] < 0.0d) {
                    throw new NotPositiveException(Double.valueOf(s2[i2]));
                }
            }
            this.sigma = (double[]) s2.clone();
        }

        public double[] getSigma() {
            return (double[]) this.sigma.clone();
        }
    }

    /* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/optim/nonlinear/scalar/noderiv/CMAESOptimizer$PopulationSize.class */
    public static class PopulationSize implements OptimizationData {
        private final int lambda;

        public PopulationSize(int size) throws NotStrictlyPositiveException {
            if (size <= 0) {
                throw new NotStrictlyPositiveException(Integer.valueOf(size));
            }
            this.lambda = size;
        }

        public int getPopulationSize() {
            return this.lambda;
        }
    }

    @Override // org.apache.commons.math3.optim.nonlinear.scalar.MultivariateOptimizer, org.apache.commons.math3.optim.BaseMultivariateOptimizer, org.apache.commons.math3.optim.BaseOptimizer
    public PointValuePair optimize(OptimizationData... optData) throws TooManyEvaluationsException, DimensionMismatchException {
        return super.optimize(optData);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Code restructure failed: missing block: B:101:0x03fd, code lost:
    
        r18 = r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:103:0x0418, code lost:
    
        if (r15 != r0[r0[(int) (0.1d + (r11.lambda / 4.0d))]]) goto L105;
     */
    /* JADX WARN: Code restructure failed: missing block: B:104:0x041b, code lost:
    
        r11.sigma *= org.apache.commons.math3.util.FastMath.exp(0.2d + (r11.cs / r11.damps));
     */
    /* JADX WARN: Code restructure failed: missing block: B:106:0x0439, code lost:
    
        if (r11.iterations <= 2) goto L110;
     */
    /* JADX WARN: Code restructure failed: missing block: B:108:0x044d, code lost:
    
        if ((org.apache.commons.math3.util.FastMath.max(r0, r0) - org.apache.commons.math3.util.FastMath.min(r0, r0)) != 0.0d) goto L110;
     */
    /* JADX WARN: Code restructure failed: missing block: B:109:0x0450, code lost:
    
        r11.sigma *= org.apache.commons.math3.util.FastMath.exp(0.2d + (r11.cs / r11.damps));
     */
    /* JADX WARN: Code restructure failed: missing block: B:110:0x0469, code lost:
    
        push(r11.fitnessHistory, r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:111:0x0476, code lost:
    
        if (r11.generateStatistics == false) goto L128;
     */
    /* JADX WARN: Code restructure failed: missing block: B:112:0x0479, code lost:
    
        r11.statisticsSigmaHistory.add(java.lang.Double.valueOf(r11.sigma));
        r11.statisticsFitnessHistory.add(java.lang.Double.valueOf(r0));
        r11.statisticsMeanHistory.add(r11.xmean.transpose());
        r11.statisticsDHistory.add(r11.diagD.transpose().scalarMultiply(100000.0d));
     */
    /* JADX WARN: Code restructure failed: missing block: B:113:0x04c7, code lost:
    
        r11.iterations++;
     */
    /* JADX WARN: Code restructure failed: missing block: B:77:0x0351, code lost:
    
        r0 = min(r11.fitnessHistory);
        r0 = max(r11.fitnessHistory);
     */
    /* JADX WARN: Code restructure failed: missing block: B:78:0x0368, code lost:
    
        if (r11.iterations <= 2) goto L82;
     */
    /* JADX WARN: Code restructure failed: missing block: B:80:0x037f, code lost:
    
        if ((org.apache.commons.math3.util.FastMath.max(r0, r0) - org.apache.commons.math3.util.FastMath.min(r0, r0)) >= r11.stopTolFun) goto L82;
     */
    /* JADX WARN: Code restructure failed: missing block: B:83:0x038e, code lost:
    
        if (r11.iterations <= r11.fitnessHistory.length) goto L87;
     */
    /* JADX WARN: Code restructure failed: missing block: B:85:0x039b, code lost:
    
        if ((r0 - r0) >= r11.stopTolHistFun) goto L87;
     */
    /* JADX WARN: Code restructure failed: missing block: B:88:0x03b4, code lost:
    
        if ((max(r11.diagD) / min(r11.diagD)) <= 1.0E7d) goto L90;
     */
    /* JADX WARN: Code restructure failed: missing block: B:91:0x03be, code lost:
    
        if (getConvergenceChecker() == null) goto L102;
     */
    /* JADX WARN: Code restructure failed: missing block: B:92:0x03c1, code lost:
    
        r2 = r0.getColumn(0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:93:0x03d1, code lost:
    
        if (r11.isMinimize == false) goto L95;
     */
    /* JADX WARN: Code restructure failed: missing block: B:94:0x03d4, code lost:
    
        r3 = r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:95:0x03d9, code lost:
    
        r3 = -r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:96:0x03dc, code lost:
    
        r0 = new org.apache.commons.math3.optim.PointValuePair(r2, r3);
     */
    /* JADX WARN: Code restructure failed: missing block: B:97:0x03e3, code lost:
    
        if (r18 == null) goto L101;
     */
    /* JADX WARN: Code restructure failed: missing block: B:99:0x03f7, code lost:
    
        if (getConvergenceChecker().converged(r11.iterations, r0, r18) == false) goto L101;
     */
    /* JADX WARN: Removed duplicated region for block: B:51:0x02aa A[PHI: r15 r17 r18
  0x02aa: PHI (r15v2 'bestValue' double) = (r15v1 'bestValue' double), (r15v3 'bestValue' double), (r15v3 'bestValue' double), (r15v3 'bestValue' double) binds: [B:39:0x0258, B:45:0x028b, B:47:0x0290, B:49:0x02a4] A[DONT_GENERATE, DONT_INLINE]
  0x02aa: PHI (r17v3 'optimum' org.apache.commons.math3.optim.PointValuePair) = 
  (r17v1 'optimum' org.apache.commons.math3.optim.PointValuePair)
  (r17v4 'optimum' org.apache.commons.math3.optim.PointValuePair)
  (r17v4 'optimum' org.apache.commons.math3.optim.PointValuePair)
  (r17v4 'optimum' org.apache.commons.math3.optim.PointValuePair)
 binds: [B:39:0x0258, B:45:0x028b, B:47:0x0290, B:49:0x02a4] A[DONT_GENERATE, DONT_INLINE]
  0x02aa: PHI (r18v2 'lastResult' org.apache.commons.math3.optim.PointValuePair) = 
  (r18v1 'lastResult' org.apache.commons.math3.optim.PointValuePair)
  (r18v5 'lastResult' org.apache.commons.math3.optim.PointValuePair)
  (r18v5 'lastResult' org.apache.commons.math3.optim.PointValuePair)
  (r18v5 'lastResult' org.apache.commons.math3.optim.PointValuePair)
 binds: [B:39:0x0258, B:45:0x028b, B:47:0x0290, B:49:0x02a4] A[DONT_GENERATE, DONT_INLINE]] */
    /* JADX WARN: Removed duplicated region for block: B:60:0x02cf  */
    @Override // org.apache.commons.math3.optim.BaseOptimizer
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public org.apache.commons.math3.optim.PointValuePair doOptimize() throws org.apache.commons.math3.exception.OutOfRangeException, org.apache.commons.math3.linear.MatrixDimensionMismatchException, org.apache.commons.math3.exception.DimensionMismatchException {
        /*
            Method dump skipped, instructions count: 1239
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.math3.optim.nonlinear.scalar.noderiv.CMAESOptimizer.doOptimize():org.apache.commons.math3.optim.PointValuePair");
    }

    @Override // org.apache.commons.math3.optim.nonlinear.scalar.MultivariateOptimizer, org.apache.commons.math3.optim.BaseMultivariateOptimizer, org.apache.commons.math3.optim.BaseOptimizer
    protected void parseOptimizationData(OptimizationData... optData) {
        super.parseOptimizationData(optData);
        for (OptimizationData data : optData) {
            if (data instanceof Sigma) {
                this.inputSigma = ((Sigma) data).getSigma();
            } else if (data instanceof PopulationSize) {
                this.lambda = ((PopulationSize) data).getPopulationSize();
            }
        }
        checkParameters();
    }

    private void checkParameters() {
        double[] init = getStartPoint();
        double[] lB = getLowerBound();
        double[] uB = getUpperBound();
        if (this.inputSigma != null) {
            if (this.inputSigma.length != init.length) {
                throw new DimensionMismatchException(this.inputSigma.length, init.length);
            }
            for (int i2 = 0; i2 < init.length; i2++) {
                if (this.inputSigma[i2] > uB[i2] - lB[i2]) {
                    throw new OutOfRangeException(Double.valueOf(this.inputSigma[i2]), 0, Double.valueOf(uB[i2] - lB[i2]));
                }
            }
        }
    }

    private void initializeCMA(double[] guess) throws OutOfRangeException {
        if (this.lambda <= 0) {
            throw new NotStrictlyPositiveException(Integer.valueOf(this.lambda));
        }
        double[][] sigmaArray = new double[guess.length][1];
        for (int i2 = 0; i2 < guess.length; i2++) {
            sigmaArray[i2][0] = this.inputSigma[i2];
        }
        RealMatrix insigma = new Array2DRowRealMatrix(sigmaArray, false);
        this.sigma = max(insigma);
        this.stopTolUpX = 1000.0d * max(insigma);
        this.stopTolX = 1.0E-11d * max(insigma);
        this.stopTolFun = 1.0E-12d;
        this.stopTolHistFun = 1.0E-13d;
        this.mu = this.lambda / 2;
        this.logMu2 = FastMath.log(this.mu + 0.5d);
        this.weights = log(sequence(1.0d, this.mu, 1.0d)).scalarMultiply(-1.0d).scalarAdd(this.logMu2);
        double sumw = 0.0d;
        double sumwq = 0.0d;
        for (int i3 = 0; i3 < this.mu; i3++) {
            double w2 = this.weights.getEntry(i3, 0);
            sumw += w2;
            sumwq += w2 * w2;
        }
        this.weights = this.weights.scalarMultiply(1.0d / sumw);
        this.mueff = (sumw * sumw) / sumwq;
        this.cc = (4.0d + (this.mueff / this.dimension)) / ((this.dimension + 4) + ((2.0d * this.mueff) / this.dimension));
        this.cs = (this.mueff + 2.0d) / ((this.dimension + this.mueff) + 3.0d);
        this.damps = ((1.0d + (2.0d * FastMath.max(0.0d, FastMath.sqrt((this.mueff - 1.0d) / (this.dimension + 1)) - 1.0d))) * FastMath.max(0.3d, 1.0d - (this.dimension / (1.0E-6d + this.maxIterations)))) + this.cs;
        this.ccov1 = 2.0d / (((this.dimension + 1.3d) * (this.dimension + 1.3d)) + this.mueff);
        this.ccovmu = FastMath.min(1.0d - this.ccov1, (2.0d * ((this.mueff - 2.0d) + (1.0d / this.mueff))) / (((this.dimension + 2) * (this.dimension + 2)) + this.mueff));
        this.ccov1Sep = FastMath.min(1.0d, (this.ccov1 * (this.dimension + 1.5d)) / 3.0d);
        this.ccovmuSep = FastMath.min(1.0d - this.ccov1, (this.ccovmu * (this.dimension + 1.5d)) / 3.0d);
        this.chiN = FastMath.sqrt(this.dimension) * ((1.0d - (1.0d / (4.0d * this.dimension))) + (1.0d / ((21.0d * this.dimension) * this.dimension)));
        this.xmean = MatrixUtils.createColumnRealMatrix(guess);
        this.diagD = insigma.scalarMultiply(1.0d / this.sigma);
        this.diagC = square(this.diagD);
        this.pc = zeros(this.dimension, 1);
        this.ps = zeros(this.dimension, 1);
        this.normps = this.ps.getFrobeniusNorm();
        this.f13071B = eye(this.dimension, this.dimension);
        this.f13072D = ones(this.dimension, 1);
        this.BD = times(this.f13071B, repmat(this.diagD.transpose(), this.dimension, 1));
        this.f13073C = this.f13071B.multiply(diag(square(this.f13072D)).multiply(this.f13071B.transpose()));
        this.historySize = 10 + ((int) ((30 * this.dimension) / this.lambda));
        this.fitnessHistory = new double[this.historySize];
        for (int i4 = 0; i4 < this.historySize; i4++) {
            this.fitnessHistory[i4] = Double.MAX_VALUE;
        }
    }

    private boolean updateEvolutionPaths(RealMatrix zmean, RealMatrix xold) {
        this.ps = this.ps.scalarMultiply(1.0d - this.cs).add(this.f13071B.multiply(zmean).scalarMultiply(FastMath.sqrt(this.cs * (2.0d - this.cs) * this.mueff)));
        this.normps = this.ps.getFrobeniusNorm();
        boolean hsig = (this.normps / FastMath.sqrt(1.0d - FastMath.pow(1.0d - this.cs, 2 * this.iterations))) / this.chiN < 1.4d + (2.0d / (((double) this.dimension) + 1.0d));
        this.pc = this.pc.scalarMultiply(1.0d - this.cc);
        if (hsig) {
            this.pc = this.pc.add(this.xmean.subtract(xold).scalarMultiply(FastMath.sqrt((this.cc * (2.0d - this.cc)) * this.mueff) / this.sigma));
        }
        return hsig;
    }

    private void updateCovarianceDiagonalOnly(boolean hsig, RealMatrix bestArz) {
        double oldFac = hsig ? 0.0d : this.ccov1Sep * this.cc * (2.0d - this.cc);
        this.diagC = this.diagC.scalarMultiply(oldFac + ((1.0d - this.ccov1Sep) - this.ccovmuSep)).add(square(this.pc).scalarMultiply(this.ccov1Sep)).add(times(this.diagC, square(bestArz).multiply(this.weights)).scalarMultiply(this.ccovmuSep));
        this.diagD = sqrt(this.diagC);
        if (this.diagonalOnly > 1 && this.iterations > this.diagonalOnly) {
            this.diagonalOnly = 0;
            this.f13071B = eye(this.dimension, this.dimension);
            this.BD = diag(this.diagD);
            this.f13073C = diag(this.diagC);
        }
    }

    private void updateCovariance(boolean hsig, RealMatrix bestArx, RealMatrix arz, int[] arindex, RealMatrix xold) throws OutOfRangeException, DimensionMismatchException {
        double negccov = 0.0d;
        if (this.ccov1 + this.ccovmu > 0.0d) {
            RealMatrix arpos = bestArx.subtract(repmat(xold, 1, this.mu)).scalarMultiply(1.0d / this.sigma);
            RealMatrix roneu = this.pc.multiply(this.pc.transpose()).scalarMultiply(this.ccov1);
            double oldFac = (hsig ? 0.0d : this.ccov1 * this.cc * (2.0d - this.cc)) + ((1.0d - this.ccov1) - this.ccovmu);
            if (this.isActiveCMA) {
                negccov = (((1.0d - this.ccovmu) * 0.25d) * this.mueff) / (FastMath.pow(this.dimension + 2, 1.5d) + (2.0d * this.mueff));
                int[] arReverseIndex = reverse(arindex);
                RealMatrix arzneg = selectColumns(arz, MathArrays.copyOf(arReverseIndex, this.mu));
                RealMatrix arnorms = sqrt(sumRows(square(arzneg)));
                int[] idxnorms = sortedIndices(arnorms.getRow(0));
                RealMatrix arnormsSorted = selectColumns(arnorms, idxnorms);
                int[] idxReverse = reverse(idxnorms);
                RealMatrix arnormsReverse = selectColumns(arnorms, idxReverse);
                RealMatrix arnorms2 = divide(arnormsReverse, arnormsSorted);
                int[] idxInv = inverse(idxnorms);
                RealMatrix arnormsInv = selectColumns(arnorms2, idxInv);
                double negcovMax = 0.33999999999999997d / square(arnormsInv).multiply(this.weights).getEntry(0, 0);
                if (negccov > negcovMax) {
                    negccov = negcovMax;
                }
                RealMatrix artmp = this.BD.multiply(times(arzneg, repmat(arnormsInv, this.dimension, 1)));
                RealMatrix Cneg = artmp.multiply(diag(this.weights)).multiply(artmp.transpose());
                this.f13073C = this.f13073C.scalarMultiply(oldFac + (0.5d * negccov)).add(roneu).add(arpos.scalarMultiply(this.ccovmu + (0.5d * negccov)).multiply(times(repmat(this.weights, 1, this.dimension), arpos.transpose()))).subtract(Cneg.scalarMultiply(negccov));
            } else {
                this.f13073C = this.f13073C.scalarMultiply(oldFac).add(roneu).add(arpos.scalarMultiply(this.ccovmu).multiply(times(repmat(this.weights, 1, this.dimension), arpos.transpose())));
            }
        }
        updateBD(negccov);
    }

    private void updateBD(double negccov) throws OutOfRangeException {
        if (this.ccov1 + this.ccovmu + negccov > 0.0d && (((this.iterations % 1.0d) / ((this.ccov1 + this.ccovmu) + negccov)) / this.dimension) / 10.0d < 1.0d) {
            this.f13073C = triu(this.f13073C, 0).add(triu(this.f13073C, 1).transpose());
            EigenDecomposition eig = new EigenDecomposition(this.f13073C);
            this.f13071B = eig.getV();
            this.f13072D = eig.getD();
            this.diagD = diag(this.f13072D);
            if (min(this.diagD) <= 0.0d) {
                for (int i2 = 0; i2 < this.dimension; i2++) {
                    if (this.diagD.getEntry(i2, 0) < 0.0d) {
                        this.diagD.setEntry(i2, 0, 0.0d);
                    }
                }
                double tfac = max(this.diagD) / 1.0E14d;
                this.f13073C = this.f13073C.add(eye(this.dimension, this.dimension).scalarMultiply(tfac));
                this.diagD = this.diagD.add(ones(this.dimension, 1).scalarMultiply(tfac));
            }
            if (max(this.diagD) > 1.0E14d * min(this.diagD)) {
                double tfac2 = (max(this.diagD) / 1.0E14d) - min(this.diagD);
                this.f13073C = this.f13073C.add(eye(this.dimension, this.dimension).scalarMultiply(tfac2));
                this.diagD = this.diagD.add(ones(this.dimension, 1).scalarMultiply(tfac2));
            }
            this.diagC = diag(this.f13073C);
            this.diagD = sqrt(this.diagD);
            this.BD = times(this.f13071B, repmat(this.diagD.transpose(), this.dimension, 1));
        }
    }

    private static void push(double[] vals, double val) {
        for (int i2 = vals.length - 1; i2 > 0; i2--) {
            vals[i2] = vals[i2 - 1];
        }
        vals[0] = val;
    }

    private int[] sortedIndices(double[] doubles) {
        DoubleIndex[] dis = new DoubleIndex[doubles.length];
        for (int i2 = 0; i2 < doubles.length; i2++) {
            dis[i2] = new DoubleIndex(doubles[i2], i2);
        }
        Arrays.sort(dis);
        int[] indices = new int[doubles.length];
        for (int i3 = 0; i3 < doubles.length; i3++) {
            indices[i3] = dis[i3].index;
        }
        return indices;
    }

    private double valueRange(ValuePenaltyPair[] vpPairs) {
        double max = Double.NEGATIVE_INFINITY;
        double min = Double.MAX_VALUE;
        for (ValuePenaltyPair vpPair : vpPairs) {
            if (vpPair.value > max) {
                max = vpPair.value;
            }
            if (vpPair.value < min) {
                min = vpPair.value;
            }
        }
        return max - min;
    }

    /* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/optim/nonlinear/scalar/noderiv/CMAESOptimizer$DoubleIndex.class */
    private static class DoubleIndex implements Comparable<DoubleIndex> {
        private final double value;
        private final int index;

        DoubleIndex(double value, int index) {
            this.value = value;
            this.index = index;
        }

        @Override // java.lang.Comparable
        public int compareTo(DoubleIndex o2) {
            return Double.compare(this.value, o2.value);
        }

        public boolean equals(Object other) {
            if (this == other) {
                return true;
            }
            return (other instanceof DoubleIndex) && Double.compare(this.value, ((DoubleIndex) other).value) == 0;
        }

        public int hashCode() {
            long bits = Double.doubleToLongBits(this.value);
            return (int) (((1438542 ^ (bits >>> 32)) ^ bits) & (-1));
        }
    }

    /* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/optim/nonlinear/scalar/noderiv/CMAESOptimizer$ValuePenaltyPair.class */
    private static class ValuePenaltyPair {
        private double value;
        private double penalty;

        ValuePenaltyPair(double value, double penalty) {
            this.value = value;
            this.penalty = penalty;
        }
    }

    /* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/optim/nonlinear/scalar/noderiv/CMAESOptimizer$FitnessFunction.class */
    private class FitnessFunction {
        private final boolean isRepairMode = true;

        FitnessFunction() {
        }

        public ValuePenaltyPair value(double[] point) {
            double value;
            double penalty = 0.0d;
            if (this.isRepairMode) {
                double[] repaired = repair(point);
                value = CMAESOptimizer.this.computeObjectiveValue(repaired);
                penalty = penalty(point, repaired);
            } else {
                value = CMAESOptimizer.this.computeObjectiveValue(point);
            }
            return new ValuePenaltyPair(CMAESOptimizer.this.isMinimize ? value : -value, CMAESOptimizer.this.isMinimize ? penalty : -penalty);
        }

        public boolean isFeasible(double[] x2) {
            double[] lB = CMAESOptimizer.this.getLowerBound();
            double[] uB = CMAESOptimizer.this.getUpperBound();
            for (int i2 = 0; i2 < x2.length; i2++) {
                if (x2[i2] < lB[i2] || x2[i2] > uB[i2]) {
                    return false;
                }
            }
            return true;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public double[] repair(double[] x2) {
            double[] lB = CMAESOptimizer.this.getLowerBound();
            double[] uB = CMAESOptimizer.this.getUpperBound();
            double[] repaired = new double[x2.length];
            for (int i2 = 0; i2 < x2.length; i2++) {
                if (x2[i2] < lB[i2]) {
                    repaired[i2] = lB[i2];
                } else if (x2[i2] > uB[i2]) {
                    repaired[i2] = uB[i2];
                } else {
                    repaired[i2] = x2[i2];
                }
            }
            return repaired;
        }

        private double penalty(double[] x2, double[] repaired) {
            double penalty = 0.0d;
            for (int i2 = 0; i2 < x2.length; i2++) {
                double diff = FastMath.abs(x2[i2] - repaired[i2]);
                penalty += diff;
            }
            return CMAESOptimizer.this.isMinimize ? penalty : -penalty;
        }
    }

    private static RealMatrix log(RealMatrix m2) {
        double[][] d2 = new double[m2.getRowDimension()][m2.getColumnDimension()];
        for (int r2 = 0; r2 < m2.getRowDimension(); r2++) {
            for (int c2 = 0; c2 < m2.getColumnDimension(); c2++) {
                d2[r2][c2] = FastMath.log(m2.getEntry(r2, c2));
            }
        }
        return new Array2DRowRealMatrix(d2, false);
    }

    private static RealMatrix sqrt(RealMatrix m2) {
        double[][] d2 = new double[m2.getRowDimension()][m2.getColumnDimension()];
        for (int r2 = 0; r2 < m2.getRowDimension(); r2++) {
            for (int c2 = 0; c2 < m2.getColumnDimension(); c2++) {
                d2[r2][c2] = FastMath.sqrt(m2.getEntry(r2, c2));
            }
        }
        return new Array2DRowRealMatrix(d2, false);
    }

    private static RealMatrix square(RealMatrix m2) throws OutOfRangeException {
        double[][] d2 = new double[m2.getRowDimension()][m2.getColumnDimension()];
        for (int r2 = 0; r2 < m2.getRowDimension(); r2++) {
            for (int c2 = 0; c2 < m2.getColumnDimension(); c2++) {
                double e2 = m2.getEntry(r2, c2);
                d2[r2][c2] = e2 * e2;
            }
        }
        return new Array2DRowRealMatrix(d2, false);
    }

    private static RealMatrix times(RealMatrix m2, RealMatrix n2) {
        double[][] d2 = new double[m2.getRowDimension()][m2.getColumnDimension()];
        for (int r2 = 0; r2 < m2.getRowDimension(); r2++) {
            for (int c2 = 0; c2 < m2.getColumnDimension(); c2++) {
                d2[r2][c2] = m2.getEntry(r2, c2) * n2.getEntry(r2, c2);
            }
        }
        return new Array2DRowRealMatrix(d2, false);
    }

    private static RealMatrix divide(RealMatrix m2, RealMatrix n2) {
        double[][] d2 = new double[m2.getRowDimension()][m2.getColumnDimension()];
        for (int r2 = 0; r2 < m2.getRowDimension(); r2++) {
            for (int c2 = 0; c2 < m2.getColumnDimension(); c2++) {
                d2[r2][c2] = m2.getEntry(r2, c2) / n2.getEntry(r2, c2);
            }
        }
        return new Array2DRowRealMatrix(d2, false);
    }

    private static RealMatrix selectColumns(RealMatrix m2, int[] cols) {
        double[][] d2 = new double[m2.getRowDimension()][cols.length];
        for (int r2 = 0; r2 < m2.getRowDimension(); r2++) {
            for (int c2 = 0; c2 < cols.length; c2++) {
                d2[r2][c2] = m2.getEntry(r2, cols[c2]);
            }
        }
        return new Array2DRowRealMatrix(d2, false);
    }

    private static RealMatrix triu(RealMatrix m2, int k2) {
        double[][] d2 = new double[m2.getRowDimension()][m2.getColumnDimension()];
        int r2 = 0;
        while (r2 < m2.getRowDimension()) {
            for (int c2 = 0; c2 < m2.getColumnDimension(); c2++) {
                d2[r2][c2] = r2 <= c2 - k2 ? m2.getEntry(r2, c2) : 0.0d;
            }
            r2++;
        }
        return new Array2DRowRealMatrix(d2, false);
    }

    private static RealMatrix sumRows(RealMatrix m2) {
        double[][] d2 = new double[1][m2.getColumnDimension()];
        for (int c2 = 0; c2 < m2.getColumnDimension(); c2++) {
            double sum = 0.0d;
            for (int r2 = 0; r2 < m2.getRowDimension(); r2++) {
                sum += m2.getEntry(r2, c2);
            }
            d2[0][c2] = sum;
        }
        return new Array2DRowRealMatrix(d2, false);
    }

    private static RealMatrix diag(RealMatrix m2) {
        if (m2.getColumnDimension() == 1) {
            double[][] d2 = new double[m2.getRowDimension()][m2.getRowDimension()];
            for (int i2 = 0; i2 < m2.getRowDimension(); i2++) {
                d2[i2][i2] = m2.getEntry(i2, 0);
            }
            return new Array2DRowRealMatrix(d2, false);
        }
        double[][] d3 = new double[m2.getRowDimension()][1];
        for (int i3 = 0; i3 < m2.getColumnDimension(); i3++) {
            d3[i3][0] = m2.getEntry(i3, i3);
        }
        return new Array2DRowRealMatrix(d3, false);
    }

    private static void copyColumn(RealMatrix m1, int col1, RealMatrix m2, int col2) throws OutOfRangeException {
        for (int i2 = 0; i2 < m1.getRowDimension(); i2++) {
            m2.setEntry(i2, col2, m1.getEntry(i2, col1));
        }
    }

    private static RealMatrix ones(int n2, int m2) {
        double[][] d2 = new double[n2][m2];
        for (int r2 = 0; r2 < n2; r2++) {
            Arrays.fill(d2[r2], 1.0d);
        }
        return new Array2DRowRealMatrix(d2, false);
    }

    private static RealMatrix eye(int n2, int m2) {
        double[][] d2 = new double[n2][m2];
        for (int r2 = 0; r2 < n2; r2++) {
            if (r2 < m2) {
                d2[r2][r2] = 1.0d;
            }
        }
        return new Array2DRowRealMatrix(d2, false);
    }

    private static RealMatrix zeros(int n2, int m2) {
        return new Array2DRowRealMatrix(n2, m2);
    }

    private static RealMatrix repmat(RealMatrix mat, int n2, int m2) {
        int rd = mat.getRowDimension();
        int cd = mat.getColumnDimension();
        double[][] d2 = new double[n2 * rd][m2 * cd];
        for (int r2 = 0; r2 < n2 * rd; r2++) {
            for (int c2 = 0; c2 < m2 * cd; c2++) {
                d2[r2][c2] = mat.getEntry(r2 % rd, c2 % cd);
            }
        }
        return new Array2DRowRealMatrix(d2, false);
    }

    private static RealMatrix sequence(double start, double end, double step) {
        int size = (int) (((end - start) / step) + 1.0d);
        double[][] d2 = new double[size][1];
        double value = start;
        for (int r2 = 0; r2 < size; r2++) {
            d2[r2][0] = value;
            value += step;
        }
        return new Array2DRowRealMatrix(d2, false);
    }

    private static double max(RealMatrix m2) throws OutOfRangeException {
        double max = -1.7976931348623157E308d;
        for (int r2 = 0; r2 < m2.getRowDimension(); r2++) {
            for (int c2 = 0; c2 < m2.getColumnDimension(); c2++) {
                double e2 = m2.getEntry(r2, c2);
                if (max < e2) {
                    max = e2;
                }
            }
        }
        return max;
    }

    private static double min(RealMatrix m2) throws OutOfRangeException {
        double min = Double.MAX_VALUE;
        for (int r2 = 0; r2 < m2.getRowDimension(); r2++) {
            for (int c2 = 0; c2 < m2.getColumnDimension(); c2++) {
                double e2 = m2.getEntry(r2, c2);
                if (min > e2) {
                    min = e2;
                }
            }
        }
        return min;
    }

    private static double max(double[] m2) {
        double max = -1.7976931348623157E308d;
        for (int r2 = 0; r2 < m2.length; r2++) {
            if (max < m2[r2]) {
                max = m2[r2];
            }
        }
        return max;
    }

    private static double min(double[] m2) {
        double min = Double.MAX_VALUE;
        for (int r2 = 0; r2 < m2.length; r2++) {
            if (min > m2[r2]) {
                min = m2[r2];
            }
        }
        return min;
    }

    private static int[] inverse(int[] indices) {
        int[] inverse = new int[indices.length];
        for (int i2 = 0; i2 < indices.length; i2++) {
            inverse[indices[i2]] = i2;
        }
        return inverse;
    }

    private static int[] reverse(int[] indices) {
        int[] reverse = new int[indices.length];
        for (int i2 = 0; i2 < indices.length; i2++) {
            reverse[i2] = indices[(indices.length - i2) - 1];
        }
        return reverse;
    }

    private double[] randn(int size) {
        double[] randn = new double[size];
        for (int i2 = 0; i2 < size; i2++) {
            randn[i2] = this.random.nextGaussian();
        }
        return randn;
    }

    private RealMatrix randn1(int size, int popSize) {
        double[][] d2 = new double[size][popSize];
        for (int r2 = 0; r2 < size; r2++) {
            for (int c2 = 0; c2 < popSize; c2++) {
                d2[r2][c2] = this.random.nextGaussian();
            }
        }
        return new Array2DRowRealMatrix(d2, false);
    }
}
