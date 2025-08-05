package org.apache.commons.math3.linear;

import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.util.FastMath;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/linear/RRQRDecomposition.class */
public class RRQRDecomposition extends QRDecomposition {

    /* renamed from: p, reason: collision with root package name */
    private int[] f13025p;
    private RealMatrix cachedP;

    public RRQRDecomposition(RealMatrix matrix) {
        this(matrix, 0.0d);
    }

    public RRQRDecomposition(RealMatrix matrix, double threshold) {
        super(matrix, threshold);
    }

    @Override // org.apache.commons.math3.linear.QRDecomposition
    protected void decompose(double[][] qrt) {
        this.f13025p = new int[qrt.length];
        for (int i2 = 0; i2 < this.f13025p.length; i2++) {
            this.f13025p[i2] = i2;
        }
        super.decompose(qrt);
    }

    @Override // org.apache.commons.math3.linear.QRDecomposition
    protected void performHouseholderReflection(int minor, double[][] qrt) {
        double l2NormSquaredMax = 0.0d;
        int l2NormSquaredMaxIndex = minor;
        for (int i2 = minor; i2 < qrt.length; i2++) {
            double l2NormSquared = 0.0d;
            for (int j2 = 0; j2 < qrt[i2].length; j2++) {
                l2NormSquared += qrt[i2][j2] * qrt[i2][j2];
            }
            if (l2NormSquared > l2NormSquaredMax) {
                l2NormSquaredMax = l2NormSquared;
                l2NormSquaredMaxIndex = i2;
            }
        }
        if (l2NormSquaredMaxIndex != minor) {
            double[] tmp1 = qrt[minor];
            qrt[minor] = qrt[l2NormSquaredMaxIndex];
            qrt[l2NormSquaredMaxIndex] = tmp1;
            int tmp2 = this.f13025p[minor];
            this.f13025p[minor] = this.f13025p[l2NormSquaredMaxIndex];
            this.f13025p[l2NormSquaredMaxIndex] = tmp2;
        }
        super.performHouseholderReflection(minor, qrt);
    }

    public RealMatrix getP() throws OutOfRangeException {
        if (this.cachedP == null) {
            int n2 = this.f13025p.length;
            this.cachedP = MatrixUtils.createRealMatrix(n2, n2);
            for (int i2 = 0; i2 < n2; i2++) {
                this.cachedP.setEntry(this.f13025p[i2], i2, 1.0d);
            }
        }
        return this.cachedP;
    }

    public int getRank(double dropThreshold) {
        RealMatrix r2 = getR();
        int rows = r2.getRowDimension();
        int columns = r2.getColumnDimension();
        int rank = 1;
        double lastNorm = r2.getFrobeniusNorm();
        while (rank < FastMath.min(rows, columns)) {
            double thisNorm = r2.getSubMatrix(rank, rows - 1, rank, columns - 1).getFrobeniusNorm();
            if (thisNorm == 0.0d || (thisNorm / lastNorm) * lastNorm < dropThreshold) {
                break;
            }
            lastNorm = thisNorm;
            rank++;
        }
        return rank;
    }

    @Override // org.apache.commons.math3.linear.QRDecomposition
    public DecompositionSolver getSolver() {
        return new Solver(super.getSolver(), getP());
    }

    /* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/linear/RRQRDecomposition$Solver.class */
    private static class Solver implements DecompositionSolver {
        private final DecompositionSolver upper;

        /* renamed from: p, reason: collision with root package name */
        private RealMatrix f13026p;

        private Solver(DecompositionSolver upper, RealMatrix p2) {
            this.upper = upper;
            this.f13026p = p2;
        }

        @Override // org.apache.commons.math3.linear.DecompositionSolver
        public boolean isNonSingular() {
            return this.upper.isNonSingular();
        }

        @Override // org.apache.commons.math3.linear.DecompositionSolver
        public RealVector solve(RealVector b2) {
            return this.f13026p.operate(this.upper.solve(b2));
        }

        @Override // org.apache.commons.math3.linear.DecompositionSolver
        public RealMatrix solve(RealMatrix b2) {
            return this.f13026p.multiply(this.upper.solve(b2));
        }

        @Override // org.apache.commons.math3.linear.DecompositionSolver
        public RealMatrix getInverse() {
            return solve(MatrixUtils.createRealIdentityMatrix(this.f13026p.getRowDimension()));
        }
    }
}
