package org.apache.commons.math3.linear;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.function.Sqrt;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.util.MathArrays;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/linear/JacobiPreconditioner.class */
public class JacobiPreconditioner extends RealLinearOperator {
    private final ArrayRealVector diag;

    public JacobiPreconditioner(double[] diag, boolean deep) {
        this.diag = new ArrayRealVector(diag, deep);
    }

    public static JacobiPreconditioner create(RealLinearOperator a2) throws OutOfRangeException, NonSquareOperatorException {
        int n2 = a2.getColumnDimension();
        if (a2.getRowDimension() != n2) {
            throw new NonSquareOperatorException(a2.getRowDimension(), n2);
        }
        double[] diag = new double[n2];
        if (a2 instanceof AbstractRealMatrix) {
            AbstractRealMatrix m2 = (AbstractRealMatrix) a2;
            for (int i2 = 0; i2 < n2; i2++) {
                diag[i2] = m2.getEntry(i2, i2);
            }
        } else {
            ArrayRealVector x2 = new ArrayRealVector(n2);
            for (int i3 = 0; i3 < n2; i3++) {
                x2.set(0.0d);
                x2.setEntry(i3, 1.0d);
                diag[i3] = a2.operate(x2).getEntry(i3);
            }
        }
        return new JacobiPreconditioner(diag, false);
    }

    @Override // org.apache.commons.math3.linear.RealLinearOperator, org.apache.commons.math3.linear.AnyMatrix
    public int getColumnDimension() {
        return this.diag.getDimension();
    }

    @Override // org.apache.commons.math3.linear.RealLinearOperator, org.apache.commons.math3.linear.AnyMatrix
    public int getRowDimension() {
        return this.diag.getDimension();
    }

    @Override // org.apache.commons.math3.linear.RealLinearOperator, org.apache.commons.math3.linear.RealMatrix
    public RealVector operate(RealVector x2) {
        return new ArrayRealVector(MathArrays.ebeDivide(x2.toArray(), this.diag.toArray()), false);
    }

    public RealLinearOperator sqrt() {
        final RealVector sqrtDiag = this.diag.map((UnivariateFunction) new Sqrt());
        return new RealLinearOperator() { // from class: org.apache.commons.math3.linear.JacobiPreconditioner.1
            @Override // org.apache.commons.math3.linear.RealLinearOperator, org.apache.commons.math3.linear.RealMatrix
            public RealVector operate(RealVector x2) {
                return new ArrayRealVector(MathArrays.ebeDivide(x2.toArray(), sqrtDiag.toArray()), false);
            }

            @Override // org.apache.commons.math3.linear.RealLinearOperator, org.apache.commons.math3.linear.AnyMatrix
            public int getRowDimension() {
                return sqrtDiag.getDimension();
            }

            @Override // org.apache.commons.math3.linear.RealLinearOperator, org.apache.commons.math3.linear.AnyMatrix
            public int getColumnDimension() {
                return sqrtDiag.getDimension();
            }
        };
    }
}
