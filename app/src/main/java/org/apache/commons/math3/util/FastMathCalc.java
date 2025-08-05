package org.apache.commons.math3.util;

import java.io.PrintStream;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.slf4j.Marker;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/util/FastMathCalc.class */
class FastMathCalc {
    private static final long HEX_40000000 = 1073741824;
    private static final double[] FACT = {1.0d, 1.0d, 2.0d, 6.0d, 24.0d, 120.0d, 720.0d, 5040.0d, 40320.0d, 362880.0d, 3628800.0d, 3.99168E7d, 4.790016E8d, 6.2270208E9d, 8.71782912E10d, 1.307674368E12d, 2.0922789888E13d, 3.55687428096E14d, 6.402373705728E15d, 1.21645100408832E17d};
    private static final double[][] LN_SPLIT_COEF = {new double[]{2.0d, 0.0d}, new double[]{0.6666666269302368d, 3.9736429850260626E-8d}, new double[]{0.3999999761581421d, 2.3841857910019882E-8d}, new double[]{0.2857142686843872d, 1.7029898543501842E-8d}, new double[]{0.2222222089767456d, 1.3245471311735498E-8d}, new double[]{0.1818181574344635d, 2.4384203044354907E-8d}, new double[]{0.1538461446762085d, 9.140260083262505E-9d}, new double[]{0.13333332538604736d, 9.220590270857665E-9d}, new double[]{0.11764700710773468d, 1.2393345855018391E-8d}, new double[]{0.10526403784751892d, 8.251545029714408E-9d}, new double[]{0.0952233225107193d, 1.2675934823758863E-8d}, new double[]{0.08713622391223907d, 1.1430250008909141E-8d}, new double[]{0.07842259109020233d, 2.404307984052299E-9d}, new double[]{0.08371849358081818d, 1.176342548272881E-8d}, new double[]{0.030589580535888672d, 1.2958646899018938E-9d}, new double[]{0.14982303977012634d, 1.225743062930824E-8d}};
    private static final String TABLE_START_DECL = "    {";
    private static final String TABLE_END_DECL = "    };";

    private FastMathCalc() {
    }

    private static void buildSinCosTables(double[] SINE_TABLE_A, double[] SINE_TABLE_B, double[] COSINE_TABLE_A, double[] COSINE_TABLE_B, int SINE_TABLE_LEN, double[] TANGENT_TABLE_A, double[] TANGENT_TABLE_B) {
        double[] result = new double[2];
        for (int i2 = 0; i2 < 7; i2++) {
            double x2 = i2 / 8.0d;
            slowSin(x2, result);
            SINE_TABLE_A[i2] = result[0];
            SINE_TABLE_B[i2] = result[1];
            slowCos(x2, result);
            COSINE_TABLE_A[i2] = result[0];
            COSINE_TABLE_B[i2] = result[1];
        }
        for (int i3 = 7; i3 < SINE_TABLE_LEN; i3++) {
            double[] xs = new double[2];
            double[] ys = new double[2];
            double[] as2 = new double[2];
            double[] bs2 = new double[2];
            double[] temps = new double[2];
            if ((i3 & 1) == 0) {
                xs[0] = SINE_TABLE_A[i3 / 2];
                xs[1] = SINE_TABLE_B[i3 / 2];
                ys[0] = COSINE_TABLE_A[i3 / 2];
                ys[1] = COSINE_TABLE_B[i3 / 2];
                splitMult(xs, ys, result);
                SINE_TABLE_A[i3] = result[0] * 2.0d;
                SINE_TABLE_B[i3] = result[1] * 2.0d;
                splitMult(ys, ys, as2);
                splitMult(xs, xs, temps);
                temps[0] = -temps[0];
                temps[1] = -temps[1];
                splitAdd(as2, temps, result);
                COSINE_TABLE_A[i3] = result[0];
                COSINE_TABLE_B[i3] = result[1];
            } else {
                xs[0] = SINE_TABLE_A[i3 / 2];
                xs[1] = SINE_TABLE_B[i3 / 2];
                ys[0] = COSINE_TABLE_A[i3 / 2];
                ys[1] = COSINE_TABLE_B[i3 / 2];
                as2[0] = SINE_TABLE_A[(i3 / 2) + 1];
                as2[1] = SINE_TABLE_B[(i3 / 2) + 1];
                bs2[0] = COSINE_TABLE_A[(i3 / 2) + 1];
                bs2[1] = COSINE_TABLE_B[(i3 / 2) + 1];
                splitMult(xs, bs2, temps);
                splitMult(ys, as2, result);
                splitAdd(result, temps, result);
                SINE_TABLE_A[i3] = result[0];
                SINE_TABLE_B[i3] = result[1];
                splitMult(ys, bs2, result);
                splitMult(xs, as2, temps);
                temps[0] = -temps[0];
                temps[1] = -temps[1];
                splitAdd(result, temps, result);
                COSINE_TABLE_A[i3] = result[0];
                COSINE_TABLE_B[i3] = result[1];
            }
        }
        for (int i4 = 0; i4 < SINE_TABLE_LEN; i4++) {
            double[] ys2 = new double[2];
            double[] as3 = {COSINE_TABLE_A[i4], COSINE_TABLE_B[i4]};
            splitReciprocal(as3, ys2);
            splitMult(new double[]{SINE_TABLE_A[i4], SINE_TABLE_B[i4]}, ys2, as3);
            TANGENT_TABLE_A[i4] = as3[0];
            TANGENT_TABLE_B[i4] = as3[1];
        }
    }

    static double slowCos(double x2, double[] result) {
        double[] xs = new double[2];
        double[] facts = new double[2];
        double[] as2 = new double[2];
        split(x2, xs);
        double[] ys = {0.0d, 0.0d};
        for (int i2 = FACT.length - 1; i2 >= 0; i2--) {
            splitMult(xs, ys, as2);
            ys[0] = as2[0];
            ys[1] = as2[1];
            if ((i2 & 1) == 0) {
                split(FACT[i2], as2);
                splitReciprocal(as2, facts);
                if ((i2 & 2) != 0) {
                    facts[0] = -facts[0];
                    facts[1] = -facts[1];
                }
                splitAdd(ys, facts, as2);
                ys[0] = as2[0];
                ys[1] = as2[1];
            }
        }
        if (result != null) {
            result[0] = ys[0];
            result[1] = ys[1];
        }
        return ys[0] + ys[1];
    }

    static double slowSin(double x2, double[] result) {
        double[] xs = new double[2];
        double[] facts = new double[2];
        double[] as2 = new double[2];
        split(x2, xs);
        double[] ys = {0.0d, 0.0d};
        for (int i2 = FACT.length - 1; i2 >= 0; i2--) {
            splitMult(xs, ys, as2);
            ys[0] = as2[0];
            ys[1] = as2[1];
            if ((i2 & 1) != 0) {
                split(FACT[i2], as2);
                splitReciprocal(as2, facts);
                if ((i2 & 2) != 0) {
                    facts[0] = -facts[0];
                    facts[1] = -facts[1];
                }
                splitAdd(ys, facts, as2);
                ys[0] = as2[0];
                ys[1] = as2[1];
            }
        }
        if (result != null) {
            result[0] = ys[0];
            result[1] = ys[1];
        }
        return ys[0] + ys[1];
    }

    static double slowexp(double x2, double[] result) {
        double[] xs = new double[2];
        double[] facts = new double[2];
        double[] as2 = new double[2];
        split(x2, xs);
        double[] ys = {0.0d, 0.0d};
        for (int i2 = FACT.length - 1; i2 >= 0; i2--) {
            splitMult(xs, ys, as2);
            ys[0] = as2[0];
            ys[1] = as2[1];
            split(FACT[i2], as2);
            splitReciprocal(as2, facts);
            splitAdd(ys, facts, as2);
            ys[0] = as2[0];
            ys[1] = as2[1];
        }
        if (result != null) {
            result[0] = ys[0];
            result[1] = ys[1];
        }
        return ys[0] + ys[1];
    }

    private static void split(double d2, double[] split) {
        if (d2 < 8.0E298d && d2 > -8.0E298d) {
            double a2 = d2 * 1.073741824E9d;
            split[0] = (d2 + a2) - a2;
            split[1] = d2 - split[0];
        } else {
            split[0] = ((d2 + (d2 * 9.313225746154785E-10d)) - d2) * 1.073741824E9d;
            split[1] = d2 - split[0];
        }
    }

    private static void resplit(double[] a2) {
        double c2 = a2[0] + a2[1];
        double d2 = -((c2 - a2[0]) - a2[1]);
        if (c2 < 8.0E298d && c2 > -8.0E298d) {
            double z2 = c2 * 1.073741824E9d;
            a2[0] = (c2 + z2) - z2;
            a2[1] = (c2 - a2[0]) + d2;
        } else {
            a2[0] = ((c2 + (c2 * 9.313225746154785E-10d)) - c2) * 1.073741824E9d;
            a2[1] = (c2 - a2[0]) + d2;
        }
    }

    private static void splitMult(double[] a2, double[] b2, double[] ans) {
        ans[0] = a2[0] * b2[0];
        ans[1] = (a2[0] * b2[1]) + (a2[1] * b2[0]) + (a2[1] * b2[1]);
        resplit(ans);
    }

    private static void splitAdd(double[] a2, double[] b2, double[] ans) {
        ans[0] = a2[0] + b2[0];
        ans[1] = a2[1] + b2[1];
        resplit(ans);
    }

    static void splitReciprocal(double[] in, double[] result) {
        if (in[0] == 0.0d) {
            in[0] = in[1];
            in[1] = 0.0d;
        }
        result[0] = 0.9999997615814209d / in[0];
        result[1] = ((2.384185791015625E-7d * in[0]) - (0.9999997615814209d * in[1])) / ((in[0] * in[0]) + (in[0] * in[1]));
        if (result[1] != result[1]) {
            result[1] = 0.0d;
        }
        resplit(result);
        for (int i2 = 0; i2 < 2; i2++) {
            double err = (((1.0d - (result[0] * in[0])) - (result[0] * in[1])) - (result[1] * in[0])) - (result[1] * in[1]);
            result[1] = result[1] + (err * (result[0] + result[1]));
        }
    }

    private static void quadMult(double[] a2, double[] b2, double[] result) {
        double[] xs = new double[2];
        double[] ys = new double[2];
        double[] zs = new double[2];
        split(a2[0], xs);
        split(b2[0], ys);
        splitMult(xs, ys, zs);
        result[0] = zs[0];
        result[1] = zs[1];
        split(b2[1], ys);
        splitMult(xs, ys, zs);
        double tmp = result[0] + zs[0];
        result[1] = result[1] - ((tmp - result[0]) - zs[0]);
        result[0] = tmp;
        double tmp2 = result[0] + zs[1];
        result[1] = result[1] - ((tmp2 - result[0]) - zs[1]);
        result[0] = tmp2;
        split(a2[1], xs);
        split(b2[0], ys);
        splitMult(xs, ys, zs);
        double tmp3 = result[0] + zs[0];
        result[1] = result[1] - ((tmp3 - result[0]) - zs[0]);
        result[0] = tmp3;
        double tmp4 = result[0] + zs[1];
        result[1] = result[1] - ((tmp4 - result[0]) - zs[1]);
        result[0] = tmp4;
        split(a2[1], xs);
        split(b2[1], ys);
        splitMult(xs, ys, zs);
        double tmp5 = result[0] + zs[0];
        result[1] = result[1] - ((tmp5 - result[0]) - zs[0]);
        result[0] = tmp5;
        double tmp6 = result[0] + zs[1];
        result[1] = result[1] - ((tmp6 - result[0]) - zs[1]);
        result[0] = tmp6;
    }

    static double expint(int p2, double[] result) {
        double[] as2 = new double[2];
        double[] ys = new double[2];
        double[] xs = {2.718281828459045d, 1.4456468917292502E-16d};
        split(1.0d, ys);
        while (p2 > 0) {
            if ((p2 & 1) != 0) {
                quadMult(ys, xs, as2);
                ys[0] = as2[0];
                ys[1] = as2[1];
            }
            quadMult(xs, xs, as2);
            xs[0] = as2[0];
            xs[1] = as2[1];
            p2 >>= 1;
        }
        if (result != null) {
            result[0] = ys[0];
            result[1] = ys[1];
            resplit(result);
        }
        return ys[0] + ys[1];
    }

    static double[] slowLog(double xi) {
        double[] x2 = new double[2];
        double[] x22 = new double[2];
        double[] a2 = new double[2];
        split(xi, x2);
        x2[0] = x2[0] + 1.0d;
        resplit(x2);
        splitReciprocal(x2, a2);
        x2[0] = x2[0] - 2.0d;
        resplit(x2);
        double[] y2 = {LN_SPLIT_COEF[LN_SPLIT_COEF.length - 1][0], LN_SPLIT_COEF[LN_SPLIT_COEF.length - 1][1]};
        splitMult(x2, a2, y2);
        x2[0] = y2[0];
        x2[1] = y2[1];
        splitMult(x2, x2, x22);
        for (int i2 = LN_SPLIT_COEF.length - 2; i2 >= 0; i2--) {
            splitMult(y2, x22, a2);
            y2[0] = a2[0];
            y2[1] = a2[1];
            splitAdd(y2, LN_SPLIT_COEF[i2], a2);
            y2[0] = a2[0];
            y2[1] = a2[1];
        }
        splitMult(y2, x2, a2);
        y2[0] = a2[0];
        y2[1] = a2[1];
        return y2;
    }

    static void printarray(PrintStream out, String name, int expectedLen, double[][] array2d) {
        out.println(name);
        checkLen(expectedLen, array2d.length);
        out.println("    { ");
        int i2 = 0;
        for (double[] array : array2d) {
            out.print("        {");
            for (double d2 : array) {
                out.printf("%-25.25s", format(d2));
            }
            int i3 = i2;
            i2++;
            out.println("}, // " + i3);
        }
        out.println(TABLE_END_DECL);
    }

    static void printarray(PrintStream out, String name, int expectedLen, double[] array) {
        out.println(name + "=");
        checkLen(expectedLen, array.length);
        out.println(TABLE_START_DECL);
        for (double d2 : array) {
            out.printf("        %s%n", format(d2));
        }
        out.println(TABLE_END_DECL);
    }

    static String format(double d2) {
        if (d2 != d2) {
            return "Double.NaN,";
        }
        return (d2 >= 0.0d ? Marker.ANY_NON_NULL_MARKER : "") + Double.toString(d2) + "d,";
    }

    private static void checkLen(int expectedLen, int actual) throws DimensionMismatchException {
        if (expectedLen != actual) {
            throw new DimensionMismatchException(actual, expectedLen);
        }
    }
}
