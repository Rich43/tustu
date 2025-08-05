package org.apache.commons.math3.optimization.direct;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.math3.analysis.MultivariateFunction;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.EigenDecomposition;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.MultivariateOptimizer;
import org.apache.commons.math3.optimization.OptimizationData;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.SimpleValueChecker;
import org.apache.commons.math3.random.MersenneTwister;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.util.MathArrays;

@Deprecated
/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/optimization/direct/CMAESOptimizer.class */
public class CMAESOptimizer extends BaseAbstractMultivariateSimpleBoundsOptimizer<MultivariateFunction> implements MultivariateOptimizer {
    public static final int DEFAULT_CHECKFEASABLECOUNT = 0;
    public static final double DEFAULT_STOPFITNESS = 0.0d;
    public static final boolean DEFAULT_ISACTIVECMA = true;
    public static final int DEFAULT_MAXITERATIONS = 30000;
    public static final int DEFAULT_DIAGONALONLY = 0;
    public static final RandomGenerator DEFAULT_RANDOMGENERATOR = new MersenneTwister();
    private int lambda;
    private boolean isActiveCMA;
    private int checkFeasableCount;
    private double[] inputSigma;
    private int dimension;
    private int diagonalOnly;
    private boolean isMinimize;
    private boolean generateStatistics;
    private int maxIterations;
    private double stopFitness;
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
    private RealMatrix f13074B;

    /* renamed from: D, reason: collision with root package name */
    private RealMatrix f13075D;
    private RealMatrix BD;
    private RealMatrix diagD;

    /* renamed from: C, reason: collision with root package name */
    private RealMatrix f13076C;
    private RealMatrix diagC;
    private int iterations;
    private double[] fitnessHistory;
    private int historySize;
    private RandomGenerator random;
    private List<Double> statisticsSigmaHistory;
    private List<RealMatrix> statisticsMeanHistory;
    private List<Double> statisticsFitnessHistory;
    private List<RealMatrix> statisticsDHistory;

    @Deprecated
    public CMAESOptimizer() {
        this(0);
    }

    @Deprecated
    public CMAESOptimizer(int lambda) {
        this(lambda, null, DEFAULT_MAXITERATIONS, 0.0d, true, 0, 0, DEFAULT_RANDOMGENERATOR, false, null);
    }

    @Deprecated
    public CMAESOptimizer(int lambda, double[] inputSigma) {
        this(lambda, inputSigma, DEFAULT_MAXITERATIONS, 0.0d, true, 0, 0, DEFAULT_RANDOMGENERATOR, false);
    }

    @Deprecated
    public CMAESOptimizer(int lambda, double[] inputSigma, int maxIterations, double stopFitness, boolean isActiveCMA, int diagonalOnly, int checkFeasableCount, RandomGenerator random, boolean generateStatistics) {
        this(lambda, inputSigma, maxIterations, stopFitness, isActiveCMA, diagonalOnly, checkFeasableCount, random, generateStatistics, new SimpleValueChecker());
    }

    @Deprecated
    public CMAESOptimizer(int lambda, double[] inputSigma, int maxIterations, double stopFitness, boolean isActiveCMA, int diagonalOnly, int checkFeasableCount, RandomGenerator random, boolean generateStatistics, ConvergenceChecker<PointValuePair> checker) {
        super(checker);
        this.diagonalOnly = 0;
        this.isMinimize = true;
        this.generateStatistics = false;
        this.statisticsSigmaHistory = new ArrayList();
        this.statisticsMeanHistory = new ArrayList();
        this.statisticsFitnessHistory = new ArrayList();
        this.statisticsDHistory = new ArrayList();
        this.lambda = lambda;
        this.inputSigma = inputSigma == null ? null : (double[]) inputSigma.clone();
        this.maxIterations = maxIterations;
        this.stopFitness = stopFitness;
        this.isActiveCMA = isActiveCMA;
        this.diagonalOnly = diagonalOnly;
        this.checkFeasableCount = checkFeasableCount;
        this.random = random;
        this.generateStatistics = generateStatistics;
    }

    public CMAESOptimizer(int maxIterations, double stopFitness, boolean isActiveCMA, int diagonalOnly, int checkFeasableCount, RandomGenerator random, boolean generateStatistics, ConvergenceChecker<PointValuePair> checker) {
        super(checker);
        this.diagonalOnly = 0;
        this.isMinimize = true;
        this.generateStatistics = false;
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

    /* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/optimization/direct/CMAESOptimizer$Sigma.class */
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

    /* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/optimization/direct/CMAESOptimizer$PopulationSize.class */
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

    @Override // org.apache.commons.math3.optimization.direct.BaseAbstractMultivariateOptimizer
    protected PointValuePair optimizeInternal(int maxEval, MultivariateFunction f2, GoalType goalType, OptimizationData... optData) {
        parseOptimizationData(optData);
        return super.optimizeInternal(maxEval, (int) f2, goalType, optData);
    }

    /* JADX WARN: Code restructure failed: missing block: B:100:0x03cf, code lost:
    
        r11.sigma *= org.apache.commons.math3.util.FastMath.exp(0.2d + (r11.cs / r11.damps));
     */
    /* JADX WARN: Code restructure failed: missing block: B:102:0x03ed, code lost:
    
        if (r11.iterations <= 2) goto L106;
     */
    /* JADX WARN: Code restructure failed: missing block: B:104:0x0401, code lost:
    
        if ((org.apache.commons.math3.util.FastMath.max(r0, r0) - org.apache.commons.math3.util.FastMath.min(r0, r0)) != 0.0d) goto L106;
     */
    /* JADX WARN: Code restructure failed: missing block: B:105:0x0404, code lost:
    
        r11.sigma *= org.apache.commons.math3.util.FastMath.exp(0.2d + (r11.cs / r11.damps));
     */
    /* JADX WARN: Code restructure failed: missing block: B:106:0x041d, code lost:
    
        push(r11.fitnessHistory, r0);
        r0.setValueRange(r0 - r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:107:0x0433, code lost:
    
        if (r11.generateStatistics == false) goto L124;
     */
    /* JADX WARN: Code restructure failed: missing block: B:108:0x0436, code lost:
    
        r11.statisticsSigmaHistory.add(java.lang.Double.valueOf(r11.sigma));
        r11.statisticsFitnessHistory.add(java.lang.Double.valueOf(r0));
        r11.statisticsMeanHistory.add(r11.xmean.transpose());
        r11.statisticsDHistory.add(r11.diagD.transpose().scalarMultiply(100000.0d));
     */
    /* JADX WARN: Code restructure failed: missing block: B:109:0x0484, code lost:
    
        r11.iterations++;
     */
    /* JADX WARN: Code restructure failed: missing block: B:73:0x0306, code lost:
    
        r0 = min(r11.fitnessHistory);
        r0 = max(r11.fitnessHistory);
     */
    /* JADX WARN: Code restructure failed: missing block: B:74:0x031d, code lost:
    
        if (r11.iterations <= 2) goto L78;
     */
    /* JADX WARN: Code restructure failed: missing block: B:76:0x0334, code lost:
    
        if ((org.apache.commons.math3.util.FastMath.max(r0, r0) - org.apache.commons.math3.util.FastMath.min(r0, r0)) >= r11.stopTolFun) goto L78;
     */
    /* JADX WARN: Code restructure failed: missing block: B:79:0x0343, code lost:
    
        if (r11.iterations <= r11.fitnessHistory.length) goto L83;
     */
    /* JADX WARN: Code restructure failed: missing block: B:81:0x0350, code lost:
    
        if ((r0 - r0) >= r11.stopTolHistFun) goto L83;
     */
    /* JADX WARN: Code restructure failed: missing block: B:84:0x0369, code lost:
    
        if ((max(r11.diagD) / min(r11.diagD)) <= 1.0E7d) goto L86;
     */
    /* JADX WARN: Code restructure failed: missing block: B:87:0x0373, code lost:
    
        if (getConvergenceChecker() == null) goto L98;
     */
    /* JADX WARN: Code restructure failed: missing block: B:88:0x0376, code lost:
    
        r2 = r0.getColumn(0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:89:0x0386, code lost:
    
        if (r11.isMinimize == false) goto L91;
     */
    /* JADX WARN: Code restructure failed: missing block: B:90:0x0389, code lost:
    
        r3 = r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:91:0x038e, code lost:
    
        r3 = -r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:92:0x0391, code lost:
    
        r0 = new org.apache.commons.math3.optimization.PointValuePair(r2, r3);
     */
    /* JADX WARN: Code restructure failed: missing block: B:93:0x0398, code lost:
    
        if (r17 == null) goto L97;
     */
    /* JADX WARN: Code restructure failed: missing block: B:95:0x03ac, code lost:
    
        if (getConvergenceChecker().converged(r11.iterations, r0, r17) == false) goto L97;
     */
    /* JADX WARN: Code restructure failed: missing block: B:97:0x03b2, code lost:
    
        r17 = r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:99:0x03cc, code lost:
    
        if (r14 != r0[r0[(int) (0.1d + (r11.lambda / 4.0d))]]) goto L101;
     */
    /* JADX WARN: Removed duplicated region for block: B:47:0x025f A[PHI: r14 r16 r17
  0x025f: PHI (r14v2 'bestValue' double) = (r14v1 'bestValue' double), (r14v3 'bestValue' double), (r14v3 'bestValue' double), (r14v3 'bestValue' double) binds: [B:35:0x020e, B:41:0x0240, B:43:0x0245, B:45:0x0259] A[DONT_GENERATE, DONT_INLINE]
  0x025f: PHI (r16v3 'optimum' org.apache.commons.math3.optimization.PointValuePair) = 
  (r16v1 'optimum' org.apache.commons.math3.optimization.PointValuePair)
  (r16v4 'optimum' org.apache.commons.math3.optimization.PointValuePair)
  (r16v4 'optimum' org.apache.commons.math3.optimization.PointValuePair)
  (r16v4 'optimum' org.apache.commons.math3.optimization.PointValuePair)
 binds: [B:35:0x020e, B:41:0x0240, B:43:0x0245, B:45:0x0259] A[DONT_GENERATE, DONT_INLINE]
  0x025f: PHI (r17v2 'lastResult' org.apache.commons.math3.optimization.PointValuePair) = 
  (r17v1 'lastResult' org.apache.commons.math3.optimization.PointValuePair)
  (r17v5 'lastResult' org.apache.commons.math3.optimization.PointValuePair)
  (r17v5 'lastResult' org.apache.commons.math3.optimization.PointValuePair)
  (r17v5 'lastResult' org.apache.commons.math3.optimization.PointValuePair)
 binds: [B:35:0x020e, B:41:0x0240, B:43:0x0245, B:45:0x0259] A[DONT_GENERATE, DONT_INLINE]] */
    /* JADX WARN: Removed duplicated region for block: B:56:0x0284  */
    @Override // org.apache.commons.math3.optimization.direct.BaseAbstractMultivariateOptimizer
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    protected org.apache.commons.math3.optimization.PointValuePair doOptimize() throws org.apache.commons.math3.exception.OutOfRangeException, org.apache.commons.math3.linear.MatrixDimensionMismatchException, org.apache.commons.math3.exception.DimensionMismatchException {
        /*
            Method dump skipped, instructions count: 1172
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.math3.optimization.direct.CMAESOptimizer.doOptimize():org.apache.commons.math3.optimization.PointValuePair");
    }

    private void parseOptimizationData(OptimizationData... optData) {
        for (OptimizationData data : optData) {
            if (data instanceof Sigma) {
                this.inputSigma = ((Sigma) data).getSigma();
            } else if (data instanceof PopulationSize) {
                this.lambda = ((PopulationSize) data).getPopulationSize();
            }
        }
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
                if (this.inputSigma[i2] < 0.0d) {
                    throw new NotPositiveException(Double.valueOf(this.inputSigma[i2]));
                }
                if (this.inputSigma[i2] > uB[i2] - lB[i2]) {
                    throw new OutOfRangeException(Double.valueOf(this.inputSigma[i2]), 0, Double.valueOf(uB[i2] - lB[i2]));
                }
            }
        }
    }

    private void initializeCMA(double[] guess) throws OutOfRangeException {
        if (this.lambda <= 0) {
            this.lambda = 4 + ((int) (3.0d * FastMath.log(this.dimension)));
        }
        double[][] sigmaArray = new double[guess.length][1];
        for (int i2 = 0; i2 < guess.length; i2++) {
            sigmaArray[i2][0] = this.inputSigma == null ? 0.3d : this.inputSigma[i2];
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
        this.f13074B = eye(this.dimension, this.dimension);
        this.f13075D = ones(this.dimension, 1);
        this.BD = times(this.f13074B, repmat(this.diagD.transpose(), this.dimension, 1));
        this.f13076C = this.f13074B.multiply(diag(square(this.f13075D)).multiply(this.f13074B.transpose()));
        this.historySize = 10 + ((int) ((30 * this.dimension) / this.lambda));
        this.fitnessHistory = new double[this.historySize];
        for (int i4 = 0; i4 < this.historySize; i4++) {
            this.fitnessHistory[i4] = Double.MAX_VALUE;
        }
    }

    private boolean updateEvolutionPaths(RealMatrix zmean, RealMatrix xold) {
        this.ps = this.ps.scalarMultiply(1.0d - this.cs).add(this.f13074B.multiply(zmean).scalarMultiply(FastMath.sqrt(this.cs * (2.0d - this.cs) * this.mueff)));
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
            this.f13074B = eye(this.dimension, this.dimension);
            this.BD = diag(this.diagD);
            this.f13076C = diag(this.diagC);
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
                this.f13076C = this.f13076C.scalarMultiply(oldFac + (0.5d * negccov)).add(roneu).add(arpos.scalarMultiply(this.ccovmu + (0.5d * negccov)).multiply(times(repmat(this.weights, 1, this.dimension), arpos.transpose()))).subtract(Cneg.scalarMultiply(negccov));
            } else {
                this.f13076C = this.f13076C.scalarMultiply(oldFac).add(roneu).add(arpos.scalarMultiply(this.ccovmu).multiply(times(repmat(this.weights, 1, this.dimension), arpos.transpose())));
            }
        }
        updateBD(negccov);
    }

    private void updateBD(double negccov) throws OutOfRangeException {
        if (this.ccov1 + this.ccovmu + negccov > 0.0d && (((this.iterations % 1.0d) / ((this.ccov1 + this.ccovmu) + negccov)) / this.dimension) / 10.0d < 1.0d) {
            this.f13076C = triu(this.f13076C, 0).add(triu(this.f13076C, 1).transpose());
            EigenDecomposition eig = new EigenDecomposition(this.f13076C);
            this.f13074B = eig.getV();
            this.f13075D = eig.getD();
            this.diagD = diag(this.f13075D);
            if (min(this.diagD) <= 0.0d) {
                for (int i2 = 0; i2 < this.dimension; i2++) {
                    if (this.diagD.getEntry(i2, 0) < 0.0d) {
                        this.diagD.setEntry(i2, 0, 0.0d);
                    }
                }
                double tfac = max(this.diagD) / 1.0E14d;
                this.f13076C = this.f13076C.add(eye(this.dimension, this.dimension).scalarMultiply(tfac));
                this.diagD = this.diagD.add(ones(this.dimension, 1).scalarMultiply(tfac));
            }
            if (max(this.diagD) > 1.0E14d * min(this.diagD)) {
                double tfac2 = (max(this.diagD) / 1.0E14d) - min(this.diagD);
                this.f13076C = this.f13076C.add(eye(this.dimension, this.dimension).scalarMultiply(tfac2));
                this.diagD = this.diagD.add(ones(this.dimension, 1).scalarMultiply(tfac2));
            }
            this.diagC = diag(this.f13076C);
            this.diagD = sqrt(this.diagD);
            this.BD = times(this.f13074B, repmat(this.diagD.transpose(), this.dimension, 1));
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

    /* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/optimization/direct/CMAESOptimizer$DoubleIndex.class */
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

    /* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/optimization/direct/CMAESOptimizer$FitnessFunction.class */
    private class FitnessFunction {
        private double valueRange = 1.0d;
        private final boolean isRepairMode = true;

        FitnessFunction() {
        }

        public double value(double[] point) {
            double value;
            if (this.isRepairMode) {
                double[] repaired = repair(point);
                value = CMAESOptimizer.this.computeObjectiveValue(repaired) + penalty(point, repaired);
            } else {
                value = CMAESOptimizer.this.computeObjectiveValue(point);
            }
            return CMAESOptimizer.this.isMinimize ? value : -value;
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

        public void setValueRange(double valueRange) {
            this.valueRange = valueRange;
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
                penalty += diff * this.valueRange;
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
