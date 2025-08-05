package org.apache.commons.math3.linear;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/linear/RectangularCholeskyDecomposition.class */
public class RectangularCholeskyDecomposition {
    private final RealMatrix root;
    private int rank;

    public RectangularCholeskyDecomposition(RealMatrix matrix) throws NonPositiveDefiniteMatrixException {
        this(matrix, 0.0d);
    }

    /* JADX WARN: Incorrect condition in loop: B:8:0x0043 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public RectangularCholeskyDecomposition(org.apache.commons.math3.linear.RealMatrix r10, double r11) throws org.apache.commons.math3.exception.OutOfRangeException, org.apache.commons.math3.linear.NonPositiveDefiniteMatrixException {
        /*
            Method dump skipped, instructions count: 581
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.math3.linear.RectangularCholeskyDecomposition.<init>(org.apache.commons.math3.linear.RealMatrix, double):void");
    }

    public RealMatrix getRootMatrix() {
        return this.root;
    }

    public int getRank() {
        return this.rank;
    }
}
