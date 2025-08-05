package K;

import G.R;
import G.aM;

/* loaded from: TunerStudioMS.jar:K/d.class */
public class d {
    public static double a(double[][] dArr, double[][] dArr2, double d2) {
        double dA = a(dArr, d2);
        int iFloor = (int) Math.floor(dA);
        double d3 = dA - iFloor;
        if (d3 == 0.0d) {
            return dArr2[iFloor][0];
        }
        return (dArr2[iFloor][0] * (1.0d - d3)) + (dArr2[(int) Math.ceil(dA)][0] * d3);
    }

    public static double a(double[][] dArr, double d2) {
        double d3 = -1.0d;
        int i2 = 0;
        while (true) {
            if (i2 >= dArr.length) {
                break;
            }
            double d4 = dArr[i2][0];
            if (i2 == 0 && d4 >= d2) {
                d3 = i2;
                break;
            }
            if (i2 == dArr.length - 1 && d2 >= d4) {
                d3 = i2;
                break;
            }
            if (d4 > d2) {
                double d5 = dArr[i2 - 1][0];
                d3 = (i2 - 1) + ((d2 - d5) / (d4 - d5));
                break;
            }
            i2++;
        }
        return d3;
    }

    public static double[] a(R r2, aM aMVar) throws V.g {
        double[] dArr = new double[aMVar.b()];
        double[][] dArrI = aMVar.i(r2.p());
        for (int i2 = 0; i2 < dArr.length; i2++) {
            dArr[i2] = dArrI[i2][0];
        }
        return dArr;
    }
}
