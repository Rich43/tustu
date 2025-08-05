package org.apache.commons.math3.ode.nonstiff;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.math3.FieldElement;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.fraction.BigFraction;
import org.apache.commons.math3.linear.Array2DRowFieldMatrix;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.ArrayFieldVector;
import org.apache.commons.math3.linear.FieldDecompositionSolver;
import org.apache.commons.math3.linear.FieldLUDecomposition;
import org.apache.commons.math3.linear.FieldMatrix;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.QRDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.SingularMatrixException;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/ode/nonstiff/AdamsNordsieckTransformer.class */
public class AdamsNordsieckTransformer {
    private static final Map<Integer, AdamsNordsieckTransformer> CACHE = new HashMap();
    private final Array2DRowRealMatrix update;
    private final double[] c1;

    private AdamsNordsieckTransformer(int n2) {
        int rows = n2 - 1;
        FieldMatrix<BigFraction> bigP = buildP(rows);
        FieldDecompositionSolver<BigFraction> pSolver = new FieldLUDecomposition(bigP).getSolver();
        BigFraction[] u2 = new BigFraction[rows];
        Arrays.fill(u2, BigFraction.ONE);
        BigFraction[] bigC1 = (BigFraction[]) pSolver.solve(new ArrayFieldVector((FieldElement[]) u2, false)).toArray();
        BigFraction[][] shiftedP = (BigFraction[][]) bigP.getData();
        for (int i2 = shiftedP.length - 1; i2 > 0; i2--) {
            shiftedP[i2] = shiftedP[i2 - 1];
        }
        shiftedP[0] = new BigFraction[rows];
        Arrays.fill(shiftedP[0], BigFraction.ZERO);
        this.update = MatrixUtils.bigFractionMatrixToRealMatrix(pSolver.solve(new Array2DRowFieldMatrix((FieldElement[][]) shiftedP, false)));
        this.c1 = new double[rows];
        for (int i3 = 0; i3 < rows; i3++) {
            this.c1[i3] = bigC1[i3].doubleValue();
        }
    }

    public static AdamsNordsieckTransformer getInstance(int nSteps) {
        AdamsNordsieckTransformer adamsNordsieckTransformer;
        synchronized (CACHE) {
            AdamsNordsieckTransformer t2 = CACHE.get(Integer.valueOf(nSteps));
            if (t2 == null) {
                t2 = new AdamsNordsieckTransformer(nSteps);
                CACHE.put(Integer.valueOf(nSteps), t2);
            }
            adamsNordsieckTransformer = t2;
        }
        return adamsNordsieckTransformer;
    }

    @Deprecated
    public int getNSteps() {
        return this.c1.length;
    }

    private FieldMatrix<BigFraction> buildP(int rows) {
        BigFraction[][] pData = new BigFraction[rows][rows];
        for (int i2 = 1; i2 <= pData.length; i2++) {
            BigFraction[] pI = pData[i2 - 1];
            int factor = -i2;
            int aj2 = factor;
            for (int j2 = 1; j2 <= pI.length; j2++) {
                pI[j2 - 1] = new BigFraction(aj2 * (j2 + 1));
                aj2 *= factor;
            }
        }
        return new Array2DRowFieldMatrix((FieldElement[][]) pData, false);
    }

    public Array2DRowRealMatrix initializeHighOrderDerivatives(double h2, double[] t2, double[][] y2, double[][] yDot) throws OutOfRangeException, SingularMatrixException {
        double[][] a2 = new double[this.c1.length + 1][this.c1.length + 1];
        double[][] b2 = new double[this.c1.length + 1][y2[0].length];
        double[] y0 = y2[0];
        double[] yDot0 = yDot[0];
        for (int i2 = 1; i2 < y2.length; i2++) {
            double di = t2[i2] - t2[0];
            double ratio = di / h2;
            double dikM1Ohk = 1.0d / h2;
            double[] aI2 = a2[(2 * i2) - 2];
            double[] aDotI = (2 * i2) - 1 < a2.length ? a2[(2 * i2) - 1] : null;
            for (int j2 = 0; j2 < aI2.length; j2++) {
                dikM1Ohk *= ratio;
                aI2[j2] = di * dikM1Ohk;
                if (aDotI != null) {
                    aDotI[j2] = (j2 + 2) * dikM1Ohk;
                }
            }
            double[] yI = y2[i2];
            double[] yDotI = yDot[i2];
            double[] bI2 = b2[(2 * i2) - 2];
            double[] bDotI = (2 * i2) - 1 < b2.length ? b2[(2 * i2) - 1] : null;
            for (int j3 = 0; j3 < yI.length; j3++) {
                bI2[j3] = (yI[j3] - y0[j3]) - (di * yDot0[j3]);
                if (bDotI != null) {
                    bDotI[j3] = yDotI[j3] - yDot0[j3];
                }
            }
        }
        QRDecomposition decomposition = new QRDecomposition(new Array2DRowRealMatrix(a2, false));
        RealMatrix x2 = decomposition.getSolver().solve(new Array2DRowRealMatrix(b2, false));
        Array2DRowRealMatrix truncatedX = new Array2DRowRealMatrix(x2.getRowDimension() - 1, x2.getColumnDimension());
        for (int i3 = 0; i3 < truncatedX.getRowDimension(); i3++) {
            for (int j4 = 0; j4 < truncatedX.getColumnDimension(); j4++) {
                truncatedX.setEntry(i3, j4, x2.getEntry(i3, j4));
            }
        }
        return truncatedX;
    }

    public Array2DRowRealMatrix updateHighOrderDerivativesPhase1(Array2DRowRealMatrix highOrder) {
        return this.update.multiply(highOrder);
    }

    public void updateHighOrderDerivativesPhase2(double[] start, double[] end, Array2DRowRealMatrix highOrder) {
        double[][] data = highOrder.getDataRef();
        for (int i2 = 0; i2 < data.length; i2++) {
            double[] dataI = data[i2];
            double c1I = this.c1[i2];
            for (int j2 = 0; j2 < dataI.length; j2++) {
                int i3 = j2;
                dataI[i3] = dataI[i3] + (c1I * (start[j2] - end[j2]));
            }
        }
    }
}
