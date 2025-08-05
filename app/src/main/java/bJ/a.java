package bJ;

import java.util.Arrays;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.stat.regression.OLSMultipleLinearRegression;

/* loaded from: TunerStudioMS.jar:bJ/a.class */
public abstract class a implements c {

    /* renamed from: a, reason: collision with root package name */
    RealMatrix f7103a = null;

    protected abstract double[] a(double d2);

    protected abstract boolean a();

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v4, types: [double[], double[][]] */
    @Override // bJ.c
    public void a(double[] dArr, double[] dArr2) throws MathIllegalArgumentException {
        if (dArr2.length != dArr.length) {
            throw new IllegalArgumentException(String.format("The numbers of y and x values must be equal (%d != %d)", Integer.valueOf(dArr.length), Integer.valueOf(dArr2.length)));
        }
        ?? r0 = new double[dArr2.length];
        for (int i2 = 0; i2 < dArr2.length; i2++) {
            r0[i2] = a(dArr2[i2]);
        }
        if (a()) {
            dArr = Arrays.copyOf(dArr, dArr.length);
            for (int i3 = 0; i3 < dArr2.length; i3++) {
                dArr[i3] = Math.log(dArr[i3]);
            }
        }
        OLSMultipleLinearRegression oLSMultipleLinearRegression = new OLSMultipleLinearRegression();
        oLSMultipleLinearRegression.setNoIntercept(true);
        oLSMultipleLinearRegression.newSampleData(dArr, r0);
        this.f7103a = MatrixUtils.createColumnRealMatrix(oLSMultipleLinearRegression.estimateRegressionParameters());
    }

    @Override // bJ.c
    public double b(double d2) {
        double dExp = this.f7103a.preMultiply(a(d2))[0];
        if (a()) {
            dExp = Math.exp(dExp);
        }
        return dExp;
    }
}
