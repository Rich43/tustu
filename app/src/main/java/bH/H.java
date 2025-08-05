package bH;

import org.icepdf.core.util.PdfOps;

/* loaded from: TunerStudioMS.jar:bH/H.class */
public class H {
    public static double a(double d2) {
        if (d2 * 100000.0d <= 0.0d) {
            return 0.0d;
        }
        return ((Integer.parseInt((r0 + "").substring(0, 1)) + 1) * ((int) Math.pow(10.0d, (int) Math.log10(r0)))) / 100000.0d;
    }

    public static boolean a(String str) {
        if (str == null) {
            return false;
        }
        if (str.startsWith(".") || str.endsWith(".")) {
            str = "0" + str + "0";
        }
        if (str.startsWith("0x") || str.endsWith(PdfOps.h_TOKEN)) {
            try {
                Integer.parseInt(W.b(W.b(str, "0x", ""), PdfOps.h_TOKEN, ""), 16);
                return true;
            } catch (Exception e2) {
                return false;
            }
        }
        try {
            Double.parseDouble(str);
            return true;
        } catch (Exception e3) {
            return false;
        }
    }

    public static double b(String str) {
        return (str.startsWith("0x") || str.endsWith(PdfOps.h_TOKEN)) ? Integer.parseInt(W.b(W.b(str, "0x", ""), PdfOps.h_TOKEN, ""), 16) : str.startsWith("0b") ? Integer.parseInt(W.b(str, PdfOps.b_TOKEN, ""), 2) : Double.parseDouble(str);
    }

    public static double[][] a(double[][] dArr, int i2, int i3) {
        double[][] dArr2 = new double[i2][i3];
        if (i3 == 1 && dArr[0].length == 1) {
            for (int i4 = 0; i4 < dArr2.length; i4++) {
                dArr2[i4][0] = a(dArr, (i4 / (dArr2.length - 1)) * (dArr.length - 1), 0.0d);
            }
        } else if (i2 == 1 && dArr.length == 1) {
            for (int i5 = 0; i5 < dArr2[0].length; i5++) {
                dArr2[0][i5] = a(dArr, 0.0d, (i5 / (dArr2[0].length - 1)) * (dArr[0].length - 1));
            }
        } else {
            for (int i6 = 0; i6 < dArr2.length; i6++) {
                double length = (i6 / (dArr2.length - 1)) * (dArr.length - 1);
                for (int i7 = 0; i7 < dArr2[0].length; i7++) {
                    dArr2[i6][i7] = a(dArr, length, (i7 / (dArr2[0].length - 1)) * (dArr[0].length - 1));
                }
            }
        }
        return dArr2;
    }

    public static double a(double[][] dArr, double d2, double d3) {
        int i2 = (int) d2;
        int i3 = (int) d3;
        int i4 = i2 < dArr.length - 1 ? i2 + 1 : i2;
        int i5 = i3 < dArr[0].length - 1 ? i3 + 1 : i3;
        double d4 = d2 - i2;
        double d5 = d3 - i3;
        return (dArr[i2][i3] * (1.0d - d5) * (1.0d - d4)) + (dArr[i2][i5] * d5 * (1.0d - d4)) + (dArr[i4][i3] * (1.0d - d5) * d4) + (dArr[i4][i5] * d5 * d4);
    }

    public static double[][] b(double[][] dArr, int i2, int i3) {
        double[][] dArr2 = new double[i2][i3];
        for (int i4 = 0; i4 < dArr2.length && i4 < dArr.length; i4++) {
            for (int i5 = 0; i5 < dArr2[0].length && i5 < dArr[0].length; i5++) {
                dArr2[i4][i5] = dArr[i4][i5];
            }
        }
        return dArr2;
    }

    public static Double a(Object obj) {
        if (!(obj instanceof String)) {
            return (Double) obj;
        }
        try {
            return Double.valueOf((String) obj);
        } catch (Exception e2) {
            return null;
        }
    }
}
