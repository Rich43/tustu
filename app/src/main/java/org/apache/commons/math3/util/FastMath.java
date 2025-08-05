package org.apache.commons.math3.util;

import java.io.PrintStream;
import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import sun.misc.FloatConsts;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/util/FastMath.class */
public class FastMath {
    public static final double PI = 3.141592653589793d;

    /* renamed from: E, reason: collision with root package name */
    public static final double f13120E = 2.718281828459045d;
    static final int EXP_INT_TABLE_MAX_INDEX = 750;
    static final int EXP_INT_TABLE_LEN = 1500;
    static final int LN_MANT_LEN = 1024;
    static final int EXP_FRAC_TABLE_LEN = 1025;
    private static final boolean RECOMPUTE_TABLES_AT_RUNTIME = false;
    private static final double LN_2_A = 0.6931470632553101d;
    private static final double LN_2_B = 1.1730463525082348E-7d;
    private static final int SINE_TABLE_LEN = 14;
    private static final long HEX_40000000 = 1073741824;
    private static final long MASK_30BITS = -1073741824;
    private static final int MASK_NON_SIGN_INT = Integer.MAX_VALUE;
    private static final long MASK_NON_SIGN_LONG = Long.MAX_VALUE;
    private static final long MASK_DOUBLE_EXPONENT = 9218868437227405312L;
    private static final long MASK_DOUBLE_MANTISSA = 4503599627370495L;
    private static final long IMPLICIT_HIGH_BIT = 4503599627370496L;
    private static final double TWO_POWER_52 = 4.503599627370496E15d;
    private static final double F_1_3 = 0.3333333333333333d;
    private static final double F_1_5 = 0.2d;
    private static final double F_1_7 = 0.14285714285714285d;
    private static final double F_1_9 = 0.1111111111111111d;
    private static final double F_1_11 = 0.09090909090909091d;
    private static final double F_1_13 = 0.07692307692307693d;
    private static final double F_1_15 = 0.06666666666666667d;
    private static final double F_1_17 = 0.058823529411764705d;
    private static final double F_15_16 = 0.9375d;
    private static final double F_13_14 = 0.9285714285714286d;
    private static final double F_11_12 = 0.9166666666666666d;
    private static final double F_9_10 = 0.9d;
    private static final double F_5_6 = 0.8333333333333334d;
    private static final double F_1_2 = 0.5d;
    private static final double LOG_MAX_VALUE = StrictMath.log(Double.MAX_VALUE);
    private static final double[][] LN_QUICK_COEF = {new double[]{1.0d, 5.669184079525E-24d}, new double[]{-0.25d, -0.25d}, new double[]{0.3333333134651184d, 1.986821492305628E-8d}, new double[]{-0.25d, -6.663542893624021E-14d}, new double[]{0.19999998807907104d, 1.1921056801463227E-8d}, new double[]{-0.1666666567325592d, -7.800414592973399E-9d}, new double[]{0.1428571343421936d, 5.650007086920087E-9d}, new double[]{-0.12502530217170715d, -7.44321345601866E-11d}, new double[]{0.11113807559013367d, 9.219544613762692E-9d}};
    private static final double[][] LN_HI_PREC_COEF = {new double[]{1.0d, -6.032174644509064E-23d}, new double[]{-0.25d, -0.25d}, new double[]{0.3333333134651184d, 1.9868161777724352E-8d}, new double[]{-0.2499999701976776d, -2.957007209750105E-8d}, new double[]{0.19999954104423523d, 1.5830993332061267E-10d}, new double[]{-0.16624879837036133d, -2.6033824355191673E-8d}};
    private static final double[] SINE_TABLE_A = {0.0d, 0.1246747374534607d, 0.24740394949913025d, 0.366272509098053d, 0.4794255495071411d, 0.5850973129272461d, 0.6816387176513672d, 0.7675435543060303d, 0.8414709568023682d, 0.902267575263977d, 0.9489846229553223d, 0.9808930158615112d, 0.9974949359893799d, 0.9985313415527344d};
    private static final double[] SINE_TABLE_B = {0.0d, -4.068233003401932E-9d, 9.755392680573412E-9d, 1.9987994582857286E-8d, -1.0902938113007961E-8d, -3.9986783938944604E-8d, 4.23719669792332E-8d, -5.207000323380292E-8d, 2.800552834259E-8d, 1.883511811213715E-8d, -3.5997360512765566E-9d, 4.116164446561962E-8d, 5.0614674548127384E-8d, -1.0129027912496858E-9d};
    private static final double[] COSINE_TABLE_A = {1.0d, 0.9921976327896118d, 0.9689123630523682d, 0.9305076599121094d, 0.8775825500488281d, 0.8109631538391113d, 0.7316888570785522d, 0.6409968137741089d, 0.5403022766113281d, 0.4311765432357788d, 0.3153223395347595d, 0.19454771280288696d, 0.07073719799518585d, -0.05417713522911072d};
    private static final double[] COSINE_TABLE_B = {0.0d, 3.4439717236742845E-8d, 5.865827662008209E-8d, -3.7999795083850525E-8d, 1.184154459111628E-8d, -3.43338934259355E-8d, 1.1795268640216787E-8d, 4.438921624363781E-8d, 2.925681159240093E-8d, -2.6437112632041807E-8d, 2.2860509143963117E-8d, -4.813899778443457E-9d, 3.6725170580355583E-9d, 2.0217439756338078E-10d};
    private static final double[] TANGENT_TABLE_A = {0.0d, 0.1256551444530487d, 0.25534194707870483d, 0.3936265707015991d, 0.5463024377822876d, 0.7214844226837158d, 0.9315965175628662d, 1.1974215507507324d, 1.5574076175689697d, 2.092571258544922d, 3.0095696449279785d, 5.041914939880371d, 14.101419448852539d, -18.430862426757812d};
    private static final double[] TANGENT_TABLE_B = {0.0d, -7.877917738262007E-9d, -2.5857668567479893E-8d, 5.2240336371356666E-9d, 5.206150291559893E-8d, 1.8307188599677033E-8d, -5.7618793749770706E-8d, 7.848361555046424E-8d, 1.0708593250394448E-7d, 1.7827257129423813E-8d, 2.893485277253286E-8d, 3.1660099222737955E-7d, 4.983191803254889E-7d, -3.356118100840571E-7d};
    private static final long[] RECIP_2PI = {2935890503282001226L, 9154082963658192752L, 3952090531849364496L, 9193070505571053912L, 7910884519577875640L, 113236205062349959L, 4577762542105553359L, -5034868814120038111L, 4208363204685324176L, 5648769086999809661L, 2819561105158720014L, -4035746434778044925L, -302932621132653753L, -2644281811660520851L, -3183605296591799669L, 6722166367014452318L, -3512299194304650054L, -7278142539171889152L};
    private static final long[] PI_O_4_BITS = {-3958705157555305932L, -4267615245585081135L};
    private static final double F_1_4 = 0.25d;
    private static final double F_3_4 = 0.75d;
    private static final double F_7_8 = 0.875d;
    private static final double[] EIGHTHS = {0.0d, 0.125d, F_1_4, 0.375d, 0.5d, 0.625d, F_3_4, F_7_8, 1.0d, 1.125d, 1.25d, 1.375d, 1.5d, 1.625d};
    private static final double[] CBRTTWO = {0.6299605249474366d, 0.7937005259840998d, 1.0d, 1.2599210498948732d, 1.5874010519681994d};

    private FastMath() {
    }

    private static double doubleHighPart(double d2) {
        if (d2 > (-Precision.SAFE_MIN) && d2 < Precision.SAFE_MIN) {
            return d2;
        }
        long xl = Double.doubleToRawLongBits(d2);
        return Double.longBitsToDouble(xl & MASK_30BITS);
    }

    public static double sqrt(double a2) {
        return Math.sqrt(a2);
    }

    public static double cosh(double x2) {
        if (x2 != x2) {
            return x2;
        }
        if (x2 > 20.0d) {
            if (x2 >= LOG_MAX_VALUE) {
                double t2 = exp(0.5d * x2);
                return 0.5d * t2 * t2;
            }
            return 0.5d * exp(x2);
        }
        if (x2 < -20.0d) {
            if (x2 <= (-LOG_MAX_VALUE)) {
                double t3 = exp((-0.5d) * x2);
                return 0.5d * t3 * t3;
            }
            return 0.5d * exp(-x2);
        }
        double[] hiPrec = new double[2];
        if (x2 < 0.0d) {
            x2 = -x2;
        }
        exp(x2, 0.0d, hiPrec);
        double ya = hiPrec[0] + hiPrec[1];
        double yb = -((ya - hiPrec[0]) - hiPrec[1]);
        double temp = ya * 1.073741824E9d;
        double yaa = (ya + temp) - temp;
        double yab = ya - yaa;
        double recip = 1.0d / ya;
        double temp2 = recip * 1.073741824E9d;
        double recipa = (recip + temp2) - temp2;
        double recipb = recip - recipa;
        double recipb2 = recipb + (((((1.0d - (yaa * recipa)) - (yaa * recipb)) - (yab * recipa)) - (yab * recipb)) * recip) + ((-yb) * recip * recip);
        double temp3 = ya + recipa;
        double yb2 = yb + (-((temp3 - ya) - recipa));
        double temp4 = temp3 + recipb2;
        double result = temp4 + yb2 + (-((temp4 - temp3) - recipb2));
        return result * 0.5d;
    }

    public static double sinh(double x2) {
        double result;
        boolean negate = false;
        if (x2 != x2) {
            return x2;
        }
        if (x2 > 20.0d) {
            if (x2 >= LOG_MAX_VALUE) {
                double t2 = exp(0.5d * x2);
                return 0.5d * t2 * t2;
            }
            return 0.5d * exp(x2);
        }
        if (x2 < -20.0d) {
            if (x2 <= (-LOG_MAX_VALUE)) {
                double t3 = exp((-0.5d) * x2);
                return (-0.5d) * t3 * t3;
            }
            return (-0.5d) * exp(-x2);
        }
        if (x2 == 0.0d) {
            return x2;
        }
        if (x2 < 0.0d) {
            x2 = -x2;
            negate = true;
        }
        if (x2 > F_1_4) {
            double[] hiPrec = new double[2];
            exp(x2, 0.0d, hiPrec);
            double ya = hiPrec[0] + hiPrec[1];
            double yb = -((ya - hiPrec[0]) - hiPrec[1]);
            double temp = ya * 1.073741824E9d;
            double yaa = (ya + temp) - temp;
            double yab = ya - yaa;
            double recip = 1.0d / ya;
            double temp2 = recip * 1.073741824E9d;
            double recipa = (recip + temp2) - temp2;
            double recipb = recip - recipa;
            double recipb2 = recipb + (((((1.0d - (yaa * recipa)) - (yaa * recipb)) - (yab * recipa)) - (yab * recipb)) * recip) + ((-yb) * recip * recip);
            double recipa2 = -recipa;
            double recipb3 = -recipb2;
            double temp3 = ya + recipa2;
            double yb2 = yb + (-((temp3 - ya) - recipa2));
            double temp4 = temp3 + recipb3;
            double result2 = temp4 + yb2 + (-((temp4 - temp3) - recipb3));
            result = result2 * 0.5d;
        } else {
            double[] hiPrec2 = new double[2];
            expm1(x2, hiPrec2);
            double ya2 = hiPrec2[0] + hiPrec2[1];
            double yb3 = -((ya2 - hiPrec2[0]) - hiPrec2[1]);
            double denom = 1.0d + ya2;
            double denomr = 1.0d / denom;
            double denomb = (-((denom - 1.0d) - ya2)) + yb3;
            double ratio = ya2 * denomr;
            double temp5 = ratio * 1.073741824E9d;
            double ra = (ratio + temp5) - temp5;
            double rb = ratio - ra;
            double temp6 = denom * 1.073741824E9d;
            double za = (denom + temp6) - temp6;
            double zb = denom - za;
            double rb2 = rb + (((((ya2 - (za * ra)) - (za * rb)) - (zb * ra)) - (zb * rb)) * denomr) + (yb3 * denomr) + ((-ya2) * denomb * denomr * denomr);
            double temp7 = ya2 + ra;
            double yb4 = yb3 + (-((temp7 - ya2) - ra));
            double temp8 = temp7 + rb2;
            double result3 = temp8 + yb4 + (-((temp8 - temp7) - rb2));
            result = result3 * 0.5d;
        }
        if (negate) {
            result = -result;
        }
        return result;
    }

    public static double tanh(double x2) {
        double result;
        boolean negate = false;
        if (x2 != x2) {
            return x2;
        }
        if (x2 > 20.0d) {
            return 1.0d;
        }
        if (x2 < -20.0d) {
            return -1.0d;
        }
        if (x2 == 0.0d) {
            return x2;
        }
        if (x2 < 0.0d) {
            x2 = -x2;
            negate = true;
        }
        if (x2 >= 0.5d) {
            double[] hiPrec = new double[2];
            exp(x2 * 2.0d, 0.0d, hiPrec);
            double ya = hiPrec[0] + hiPrec[1];
            double yb = -((ya - hiPrec[0]) - hiPrec[1]);
            double na = (-1.0d) + ya;
            double nb = -((na + 1.0d) - ya);
            double temp = na + yb;
            double nb2 = nb + (-((temp - na) - yb));
            double da = 1.0d + ya;
            double db = -((da - 1.0d) - ya);
            double temp2 = da + yb;
            double db2 = db + (-((temp2 - da) - yb));
            double temp3 = temp2 * 1.073741824E9d;
            double daa = (temp2 + temp3) - temp3;
            double dab = temp2 - daa;
            double ratio = temp / temp2;
            double temp4 = ratio * 1.073741824E9d;
            double ratioa = (ratio + temp4) - temp4;
            double ratiob = ratio - ratioa;
            result = ratioa + ratiob + (((((temp - (daa * ratioa)) - (daa * ratiob)) - (dab * ratioa)) - (dab * ratiob)) / temp2) + (nb2 / temp2) + ((((-db2) * temp) / temp2) / temp2);
        } else {
            double[] hiPrec2 = new double[2];
            expm1(x2 * 2.0d, hiPrec2);
            double ya2 = hiPrec2[0] + hiPrec2[1];
            double yb2 = -((ya2 - hiPrec2[0]) - hiPrec2[1]);
            double da2 = 2.0d + ya2;
            double db3 = -((da2 - 2.0d) - ya2);
            double temp5 = da2 + yb2;
            double db4 = db3 + (-((temp5 - da2) - yb2));
            double temp6 = temp5 * 1.073741824E9d;
            double daa2 = (temp5 + temp6) - temp6;
            double dab2 = temp5 - daa2;
            double ratio2 = ya2 / temp5;
            double temp7 = ratio2 * 1.073741824E9d;
            double ratioa2 = (ratio2 + temp7) - temp7;
            double ratiob2 = ratio2 - ratioa2;
            result = ratioa2 + ratiob2 + (((((ya2 - (daa2 * ratioa2)) - (daa2 * ratiob2)) - (dab2 * ratioa2)) - (dab2 * ratiob2)) / temp5) + (yb2 / temp5) + ((((-db4) * ya2) / temp5) / temp5);
        }
        if (negate) {
            result = -result;
        }
        return result;
    }

    public static double acosh(double a2) {
        return log(a2 + sqrt((a2 * a2) - 1.0d));
    }

    public static double asinh(double a2) {
        double absAsinh;
        boolean negative = false;
        if (a2 < 0.0d) {
            negative = true;
            a2 = -a2;
        }
        if (a2 > 0.167d) {
            absAsinh = log(sqrt((a2 * a2) + 1.0d) + a2);
        } else {
            double a22 = a2 * a2;
            if (a2 > 0.097d) {
                absAsinh = a2 * (1.0d - ((a22 * (F_1_3 - ((a22 * (F_1_5 - ((a22 * (F_1_7 - ((a22 * (F_1_9 - ((a22 * (F_1_11 - ((a22 * (F_1_13 - ((a22 * (F_1_15 - ((a22 * F_1_17) * F_15_16))) * F_13_14))) * F_11_12))) * F_9_10))) * F_7_8))) * F_5_6))) * F_3_4))) * 0.5d));
            } else if (a2 > 0.036d) {
                absAsinh = a2 * (1.0d - ((a22 * (F_1_3 - ((a22 * (F_1_5 - ((a22 * (F_1_7 - ((a22 * (F_1_9 - ((a22 * (F_1_11 - ((a22 * F_1_13) * F_11_12))) * F_9_10))) * F_7_8))) * F_5_6))) * F_3_4))) * 0.5d));
            } else if (a2 > 0.0036d) {
                absAsinh = a2 * (1.0d - ((a22 * (F_1_3 - ((a22 * (F_1_5 - ((a22 * (F_1_7 - ((a22 * F_1_9) * F_7_8))) * F_5_6))) * F_3_4))) * 0.5d));
            } else {
                absAsinh = a2 * (1.0d - ((a22 * (F_1_3 - ((a22 * F_1_5) * F_3_4))) * 0.5d));
            }
        }
        return negative ? -absAsinh : absAsinh;
    }

    public static double atanh(double a2) {
        double absAtanh;
        boolean negative = false;
        if (a2 < 0.0d) {
            negative = true;
            a2 = -a2;
        }
        if (a2 > 0.15d) {
            absAtanh = 0.5d * log((1.0d + a2) / (1.0d - a2));
        } else {
            double a22 = a2 * a2;
            if (a2 > 0.087d) {
                absAtanh = a2 * (1.0d + (a22 * (F_1_3 + (a22 * (F_1_5 + (a22 * (F_1_7 + (a22 * (F_1_9 + (a22 * (F_1_11 + (a22 * (F_1_13 + (a22 * (F_1_15 + (a22 * F_1_17))))))))))))))));
            } else if (a2 > 0.031d) {
                absAtanh = a2 * (1.0d + (a22 * (F_1_3 + (a22 * (F_1_5 + (a22 * (F_1_7 + (a22 * (F_1_9 + (a22 * (F_1_11 + (a22 * F_1_13))))))))))));
            } else if (a2 > 0.003d) {
                absAtanh = a2 * (1.0d + (a22 * (F_1_3 + (a22 * (F_1_5 + (a22 * (F_1_7 + (a22 * F_1_9))))))));
            } else {
                absAtanh = a2 * (1.0d + (a22 * (F_1_3 + (a22 * F_1_5))));
            }
        }
        return negative ? -absAtanh : absAtanh;
    }

    public static double signum(double a2) {
        if (a2 < 0.0d) {
            return -1.0d;
        }
        if (a2 > 0.0d) {
            return 1.0d;
        }
        return a2;
    }

    public static float signum(float a2) {
        if (a2 < 0.0f) {
            return -1.0f;
        }
        if (a2 > 0.0f) {
            return 1.0f;
        }
        return a2;
    }

    public static double nextUp(double a2) {
        return nextAfter(a2, Double.POSITIVE_INFINITY);
    }

    public static float nextUp(float a2) {
        return nextAfter(a2, Double.POSITIVE_INFINITY);
    }

    public static double nextDown(double a2) {
        return nextAfter(a2, Double.NEGATIVE_INFINITY);
    }

    public static float nextDown(float a2) {
        return nextAfter(a2, Double.NEGATIVE_INFINITY);
    }

    public static double random() {
        return Math.random();
    }

    public static double exp(double x2) {
        return exp(x2, 0.0d, null);
    }

    private static double exp(double x2, double extra, double[] hiPrec) {
        double result;
        int intVal = (int) x2;
        if (x2 < 0.0d) {
            if (x2 < -746.0d) {
                if (hiPrec != null) {
                    hiPrec[0] = 0.0d;
                    hiPrec[1] = 0.0d;
                    return 0.0d;
                }
                return 0.0d;
            }
            if (intVal < -709) {
                double result2 = exp(x2 + 40.19140625d, extra, hiPrec) / 2.8504009514401178E17d;
                if (hiPrec != null) {
                    hiPrec[0] = hiPrec[0] / 2.8504009514401178E17d;
                    hiPrec[1] = hiPrec[1] / 2.8504009514401178E17d;
                }
                return result2;
            }
            if (intVal == -709) {
                double result3 = exp(x2 + 1.494140625d, extra, hiPrec) / 4.455505956692757d;
                if (hiPrec != null) {
                    hiPrec[0] = hiPrec[0] / 4.455505956692757d;
                    hiPrec[1] = hiPrec[1] / 4.455505956692757d;
                }
                return result3;
            }
            intVal--;
        } else if (intVal > 709) {
            if (hiPrec != null) {
                hiPrec[0] = Double.POSITIVE_INFINITY;
                hiPrec[1] = 0.0d;
                return Double.POSITIVE_INFINITY;
            }
            return Double.POSITIVE_INFINITY;
        }
        double intPartA = ExpIntTable.EXP_INT_TABLE_A[EXP_INT_TABLE_MAX_INDEX + intVal];
        double intPartB = ExpIntTable.EXP_INT_TABLE_B[EXP_INT_TABLE_MAX_INDEX + intVal];
        int intFrac = (int) ((x2 - intVal) * 1024.0d);
        double fracPartA = ExpFracTable.EXP_FRAC_TABLE_A[intFrac];
        double fracPartB = ExpFracTable.EXP_FRAC_TABLE_B[intFrac];
        double epsilon = x2 - (intVal + (intFrac / 1024.0d));
        double z2 = (((((((0.04168701738764507d * epsilon) + 0.1666666505023083d) * epsilon) + 0.5000000000042687d) * epsilon) + 1.0d) * epsilon) - 3.940510424527919E-20d;
        double tempA = intPartA * fracPartA;
        double tempB = (intPartA * fracPartB) + (intPartB * fracPartA) + (intPartB * fracPartB);
        double tempC = tempB + tempA;
        if (tempC == Double.POSITIVE_INFINITY) {
            return Double.POSITIVE_INFINITY;
        }
        if (extra != 0.0d) {
            result = (tempC * extra * z2) + (tempC * extra) + (tempC * z2) + tempB + tempA;
        } else {
            result = (tempC * z2) + tempB + tempA;
        }
        if (hiPrec != null) {
            hiPrec[0] = tempA;
            hiPrec[1] = (tempC * extra * z2) + (tempC * extra) + (tempC * z2) + tempB;
        }
        return result;
    }

    public static double expm1(double x2) {
        return expm1(x2, null);
    }

    private static double expm1(double x2, double[] hiPrecOut) {
        if (x2 != x2 || x2 == 0.0d) {
            return x2;
        }
        if (x2 <= -1.0d || x2 >= 1.0d) {
            double[] hiPrec = new double[2];
            exp(x2, 0.0d, hiPrec);
            if (x2 > 0.0d) {
                return (-1.0d) + hiPrec[0] + hiPrec[1];
            }
            double ra = (-1.0d) + hiPrec[0];
            return ra + (-((ra + 1.0d) - hiPrec[0])) + hiPrec[1];
        }
        boolean negative = false;
        if (x2 < 0.0d) {
            x2 = -x2;
            negative = true;
        }
        int intFrac = (int) (x2 * 1024.0d);
        double tempA = ExpFracTable.EXP_FRAC_TABLE_A[intFrac] - 1.0d;
        double tempB = ExpFracTable.EXP_FRAC_TABLE_B[intFrac];
        double temp = tempA + tempB;
        double tempB2 = -((temp - tempA) - tempB);
        double temp2 = temp * 1.073741824E9d;
        double baseA = (temp + temp2) - temp2;
        double baseB = tempB2 + (temp - baseA);
        double epsilon = x2 - (intFrac / 1024.0d);
        double zb = ((((((0.008336750013465571d * epsilon) + 0.041666663879186654d) * epsilon) + 0.16666666666745392d) * epsilon) + 0.49999999999999994d) * epsilon * epsilon;
        double temp3 = epsilon + zb;
        double zb2 = -((temp3 - epsilon) - zb);
        double temp4 = temp3 * 1.073741824E9d;
        double temp5 = (temp3 + temp4) - temp4;
        double zb3 = zb2 + (temp3 - temp5);
        double ya = temp5 * baseA;
        double temp6 = ya + (temp5 * baseB);
        double yb = -((temp6 - ya) - (temp5 * baseB));
        double temp7 = temp6 + (zb3 * baseA);
        double yb2 = yb + (-((temp7 - temp6) - (zb3 * baseA)));
        double temp8 = temp7 + (zb3 * baseB);
        double yb3 = yb2 + (-((temp8 - temp7) - (zb3 * baseB)));
        double temp9 = temp8 + baseA;
        double yb4 = yb3 + (-((temp9 - baseA) - temp8));
        double temp10 = temp9 + temp5;
        double yb5 = yb4 + (-((temp10 - temp9) - temp5));
        double temp11 = temp10 + baseB;
        double yb6 = yb5 + (-((temp11 - temp10) - baseB));
        double temp12 = temp11 + zb3;
        double yb7 = yb6 + (-((temp12 - temp11) - zb3));
        double ya2 = temp12;
        if (negative) {
            double denom = 1.0d + ya2;
            double denomr = 1.0d / denom;
            double denomb = (-((denom - 1.0d) - ya2)) + yb7;
            double ratio = ya2 * denomr;
            double temp13 = ratio * 1.073741824E9d;
            double ra2 = (ratio + temp13) - temp13;
            double rb = ratio - ra2;
            double temp14 = denom * 1.073741824E9d;
            double za = (denom + temp14) - temp14;
            double zb4 = denom - za;
            double rb2 = rb + (((((ya2 - (za * ra2)) - (za * rb)) - (zb4 * ra2)) - (zb4 * rb)) * denomr) + (yb7 * denomr) + ((-ya2) * denomb * denomr * denomr);
            ya2 = -ra2;
            yb7 = -rb2;
        }
        if (hiPrecOut != null) {
            hiPrecOut[0] = ya2;
            hiPrecOut[1] = yb7;
        }
        return ya2 + yb7;
    }

    public static double log(double x2) {
        return log(x2, (double[]) null);
    }

    private static double log(double x2, double[] hiPrec) {
        double lnza;
        if (x2 == 0.0d) {
            return Double.NEGATIVE_INFINITY;
        }
        long bits = Double.doubleToRawLongBits(x2);
        if (((bits & Long.MIN_VALUE) != 0 || x2 != x2) && x2 != 0.0d) {
            if (hiPrec != null) {
                hiPrec[0] = Double.NaN;
                return Double.NaN;
            }
            return Double.NaN;
        }
        if (x2 == Double.POSITIVE_INFINITY) {
            if (hiPrec != null) {
                hiPrec[0] = Double.POSITIVE_INFINITY;
                return Double.POSITIVE_INFINITY;
            }
            return Double.POSITIVE_INFINITY;
        }
        int exp = ((int) (bits >> 52)) - 1023;
        if ((bits & 9218868437227405312L) == 0) {
            if (x2 == 0.0d) {
                if (hiPrec != null) {
                    hiPrec[0] = Double.NEGATIVE_INFINITY;
                    return Double.NEGATIVE_INFINITY;
                }
                return Double.NEGATIVE_INFINITY;
            }
            while (true) {
                bits <<= 1;
                if ((bits & IMPLICIT_HIGH_BIT) != 0) {
                    break;
                }
                exp--;
            }
        }
        if ((exp == -1 || exp == 0) && x2 < 1.01d && x2 > 0.99d && hiPrec == null) {
            double xa = x2 - 1.0d;
            double d2 = (xa - x2) + 1.0d;
            double tmp = xa * 1.073741824E9d;
            double aa2 = (xa + tmp) - tmp;
            double ab2 = xa - aa2;
            double[] lnCoef_last = LN_QUICK_COEF[LN_QUICK_COEF.length - 1];
            double ya = lnCoef_last[0];
            double yb = lnCoef_last[1];
            for (int i2 = LN_QUICK_COEF.length - 2; i2 >= 0; i2--) {
                double aa3 = ya * aa2;
                double ab3 = (ya * ab2) + (yb * aa2) + (yb * ab2);
                double tmp2 = aa3 * 1.073741824E9d;
                double ya2 = (aa3 + tmp2) - tmp2;
                double yb2 = (aa3 - ya2) + ab3;
                double[] lnCoef_i = LN_QUICK_COEF[i2];
                double aa4 = ya2 + lnCoef_i[0];
                double ab4 = yb2 + lnCoef_i[1];
                double tmp3 = aa4 * 1.073741824E9d;
                ya = (aa4 + tmp3) - tmp3;
                yb = (aa4 - ya) + ab4;
            }
            double aa5 = ya * aa2;
            double ab5 = (ya * ab2) + (yb * aa2) + (yb * ab2);
            double tmp4 = aa5 * 1.073741824E9d;
            double ya3 = (aa5 + tmp4) - tmp4;
            double yb3 = (aa5 - ya3) + ab5;
            return ya3 + yb3;
        }
        double[] lnm = lnMant.LN_MANT[(int) ((bits & 4499201580859392L) >> 42)];
        double epsilon = (bits & 4398046511103L) / (TWO_POWER_52 + (bits & 4499201580859392L));
        double lnzb = 0.0d;
        if (hiPrec != null) {
            double tmp5 = epsilon * 1.073741824E9d;
            double aa6 = (epsilon + tmp5) - tmp5;
            double ab6 = epsilon - aa6;
            double numer = bits & 4398046511103L;
            double denom = TWO_POWER_52 + (bits & 4499201580859392L);
            double xb = ab6 + (((numer - (aa6 * denom)) - (ab6 * denom)) / denom);
            double[] lnCoef_last2 = LN_HI_PREC_COEF[LN_HI_PREC_COEF.length - 1];
            double ya4 = lnCoef_last2[0];
            double yb4 = lnCoef_last2[1];
            for (int i3 = LN_HI_PREC_COEF.length - 2; i3 >= 0; i3--) {
                double aa7 = ya4 * aa6;
                double ab7 = (ya4 * xb) + (yb4 * aa6) + (yb4 * xb);
                double tmp6 = aa7 * 1.073741824E9d;
                double ya5 = (aa7 + tmp6) - tmp6;
                double yb5 = (aa7 - ya5) + ab7;
                double[] lnCoef_i2 = LN_HI_PREC_COEF[i3];
                double aa8 = ya5 + lnCoef_i2[0];
                double ab8 = yb5 + lnCoef_i2[1];
                double tmp7 = aa8 * 1.073741824E9d;
                ya4 = (aa8 + tmp7) - tmp7;
                yb4 = (aa8 - ya4) + ab8;
            }
            double aa9 = ya4 * aa6;
            double ab9 = (ya4 * xb) + (yb4 * aa6) + (yb4 * xb);
            lnza = aa9 + ab9;
            lnzb = -((lnza - aa9) - ab9);
        } else {
            double lnza2 = ((-0.16624882440418567d) * epsilon) + 0.19999954120254515d;
            lnza = ((((((((lnza2 * epsilon) - 0.2499999997677497d) * epsilon) + 0.3333333333332802d) * epsilon) - 0.5d) * epsilon) + 1.0d) * epsilon;
        }
        double a2 = LN_2_A * exp;
        double c2 = a2 + lnm[0];
        double d3 = -((c2 - a2) - lnm[0]);
        double b2 = 0.0d + d3;
        double c3 = c2 + lnza;
        double d4 = -((c3 - c2) - lnza);
        double b3 = b2 + d4;
        double c4 = c3 + (LN_2_B * exp);
        double d5 = -((c4 - c3) - (LN_2_B * exp));
        double b4 = b3 + d5;
        double c5 = c4 + lnm[1];
        double d6 = -((c5 - c4) - lnm[1]);
        double b5 = b4 + d6;
        double c6 = c5 + lnzb;
        double d7 = -((c6 - c5) - lnzb);
        double b6 = b5 + d7;
        if (hiPrec != null) {
            hiPrec[0] = c6;
            hiPrec[1] = b6;
        }
        return c6 + b6;
    }

    public static double log1p(double x2) {
        if (x2 == -1.0d) {
            return Double.NEGATIVE_INFINITY;
        }
        if (x2 == Double.POSITIVE_INFINITY) {
            return Double.POSITIVE_INFINITY;
        }
        if (x2 > 1.0E-6d || x2 < -1.0E-6d) {
            double xpa = 1.0d + x2;
            double xpb = -((xpa - 1.0d) - x2);
            double[] hiPrec = new double[2];
            double lores = log(xpa, hiPrec);
            if (Double.isInfinite(lores)) {
                return lores;
            }
            double fx1 = xpb / xpa;
            double epsilon = (0.5d * fx1) + 1.0d;
            return (epsilon * fx1) + hiPrec[1] + hiPrec[0];
        }
        double y2 = (((x2 * F_1_3) - 0.5d) * x2) + 1.0d;
        return y2 * x2;
    }

    public static double log10(double x2) {
        double[] hiPrec = new double[2];
        double lores = log(x2, hiPrec);
        if (Double.isInfinite(lores)) {
            return lores;
        }
        double tmp = hiPrec[0] * 1.073741824E9d;
        double lna = (hiPrec[0] + tmp) - tmp;
        double lnb = (hiPrec[0] - lna) + hiPrec[1];
        return (1.9699272335463627E-8d * lnb) + (1.9699272335463627E-8d * lna) + (0.4342944622039795d * lnb) + (0.4342944622039795d * lna);
    }

    public static double log(double base, double x2) {
        return log(x2) / log(base);
    }

    public static double pow(double x2, double y2) {
        if (y2 == 0.0d) {
            return 1.0d;
        }
        long yBits = Double.doubleToRawLongBits(y2);
        int yRawExp = (int) ((yBits & 9218868437227405312L) >> 52);
        long yRawMantissa = yBits & 4503599627370495L;
        long xBits = Double.doubleToRawLongBits(x2);
        int xRawExp = (int) ((xBits & 9218868437227405312L) >> 52);
        long xRawMantissa = xBits & 4503599627370495L;
        if (yRawExp > 1085) {
            if (yRawExp == 2047 && yRawMantissa != 0) {
                return Double.NaN;
            }
            if (xRawExp == 2047 && xRawMantissa != 0) {
                return Double.NaN;
            }
            if (xRawExp == 1023 && xRawMantissa == 0) {
                if (yRawExp == 2047) {
                    return Double.NaN;
                }
                return 1.0d;
            }
            if ((y2 > 0.0d) ^ (xRawExp < 1023)) {
                return Double.POSITIVE_INFINITY;
            }
            return 0.0d;
        }
        if (yRawExp >= 1023) {
            long yFullMantissa = IMPLICIT_HIGH_BIT | yRawMantissa;
            if (yRawExp < 1075) {
                long integralMask = (-1) << (1075 - yRawExp);
                if ((yFullMantissa & integralMask) == yFullMantissa) {
                    long l2 = yFullMantissa >> (1075 - yRawExp);
                    return pow(x2, y2 < 0.0d ? -l2 : l2);
                }
            } else {
                long l3 = yFullMantissa << (yRawExp - 1075);
                return pow(x2, y2 < 0.0d ? -l3 : l3);
            }
        }
        if (x2 == 0.0d) {
            return y2 < 0.0d ? Double.POSITIVE_INFINITY : 0.0d;
        }
        if (xRawExp == 2047) {
            if (xRawMantissa == 0) {
                return y2 < 0.0d ? 0.0d : Double.POSITIVE_INFINITY;
            }
            return Double.NaN;
        }
        if (x2 < 0.0d) {
            return Double.NaN;
        }
        double tmp = y2 * 1.073741824E9d;
        double ya = (y2 + tmp) - tmp;
        double yb = y2 - ya;
        double[] lns = new double[2];
        double lores = log(x2, lns);
        if (Double.isInfinite(lores)) {
            return lores;
        }
        double lna = lns[0];
        double lnb = lns[1];
        double tmp1 = lna * 1.073741824E9d;
        double tmp2 = (lna + tmp1) - tmp1;
        double lnb2 = lnb + (lna - tmp2);
        double aa2 = tmp2 * ya;
        double ab2 = (tmp2 * yb) + (lnb2 * ya) + (lnb2 * yb);
        double lna2 = aa2 + ab2;
        double lnb3 = -((lna2 - aa2) - ab2);
        double z2 = (0.008333333333333333d * lnb3) + 0.041666666666666664d;
        double result = exp(lna2, ((((((z2 * lnb3) + 0.16666666666666666d) * lnb3) + 0.5d) * lnb3) + 1.0d) * lnb3, null);
        return result;
    }

    public static double pow(double d2, int e2) {
        return pow(d2, e2);
    }

    public static double pow(double d2, long e2) {
        if (e2 == 0) {
            return 1.0d;
        }
        return e2 > 0 ? new Split(d2).pow(e2).full : new Split(d2).reciprocal().pow(-e2).full;
    }

    /* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/util/FastMath$Split.class */
    private static class Split {
        public static final Split NAN = new Split(Double.NaN, 0.0d);
        public static final Split POSITIVE_INFINITY = new Split(Double.POSITIVE_INFINITY, 0.0d);
        public static final Split NEGATIVE_INFINITY = new Split(Double.NEGATIVE_INFINITY, 0.0d);
        private final double full;
        private final double high;
        private final double low;

        Split(double x2) {
            this.full = x2;
            this.high = Double.longBitsToDouble(Double.doubleToRawLongBits(x2) & (-134217728));
            this.low = x2 - this.high;
        }

        Split(double high, double low) {
            this(high == 0.0d ? (low == 0.0d && Double.doubleToRawLongBits(high) == Long.MIN_VALUE) ? -0.0d : low : high + low, high, low);
        }

        Split(double full, double high, double low) {
            this.full = full;
            this.high = high;
            this.low = low;
        }

        public Split multiply(Split b2) {
            Split mulBasic = new Split(this.full * b2.full);
            double mulError = (this.low * b2.low) - (((mulBasic.full - (this.high * b2.high)) - (this.low * b2.high)) - (this.high * b2.low));
            return new Split(mulBasic.high, mulBasic.low + mulError);
        }

        public Split reciprocal() {
            double approximateInv = 1.0d / this.full;
            Split splitInv = new Split(approximateInv);
            Split product = multiply(splitInv);
            double error = (product.high - 1.0d) + product.low;
            return Double.isNaN(error) ? splitInv : new Split(splitInv.high, splitInv.low - (error / this.full));
        }

        /* JADX INFO: Access modifiers changed from: private */
        public Split pow(long e2) {
            Split result = new Split(1.0d);
            Split d2p = new Split(this.full, this.high, this.low);
            long j2 = e2;
            while (true) {
                long p2 = j2;
                if (p2 == 0) {
                    break;
                }
                if ((p2 & 1) != 0) {
                    result = result.multiply(d2p);
                }
                d2p = d2p.multiply(d2p);
                j2 = p2 >>> 1;
            }
            if (Double.isNaN(result.full)) {
                if (Double.isNaN(this.full)) {
                    return NAN;
                }
                if (FastMath.abs(this.full) < 1.0d) {
                    return new Split(FastMath.copySign(0.0d, this.full), 0.0d);
                }
                if (this.full < 0.0d && (e2 & 1) == 1) {
                    return NEGATIVE_INFINITY;
                }
                return POSITIVE_INFINITY;
            }
            return result;
        }
    }

    private static double polySine(double x2) {
        double x22 = x2 * x2;
        double p2 = (2.7553817452272217E-6d * x22) - 1.9841269659586505E-4d;
        return ((((p2 * x22) + 0.008333333333329196d) * x22) - 0.16666666666666666d) * x22 * x2;
    }

    private static double polyCosine(double x2) {
        double x22 = x2 * x2;
        double p2 = (2.479773539153719E-5d * x22) - 0.0013888888689039883d;
        return ((((p2 * x22) + 0.041666666666621166d) * x22) - 0.49999999999999994d) * x22;
    }

    private static double sinQ(double xa, double xb) {
        int idx = (int) ((xa * 8.0d) + 0.5d);
        double epsilon = xa - EIGHTHS[idx];
        double sintA = SINE_TABLE_A[idx];
        double sintB = SINE_TABLE_B[idx];
        double costA = COSINE_TABLE_A[idx];
        double costB = COSINE_TABLE_B[idx];
        double sinEpsB = polySine(epsilon);
        double cosEpsB = polyCosine(epsilon);
        double temp = epsilon * 1.073741824E9d;
        double temp2 = (epsilon + temp) - temp;
        double sinEpsB2 = sinEpsB + (epsilon - temp2);
        double c2 = 0.0d + sintA;
        double d2 = -((c2 - 0.0d) - sintA);
        double b2 = 0.0d + d2;
        double t2 = costA * temp2;
        double c3 = c2 + t2;
        double d3 = -((c3 - c2) - t2);
        double a2 = c3;
        double b3 = b2 + d3 + (sintA * cosEpsB) + (costA * sinEpsB2) + sintB + (costB * temp2) + (sintB * cosEpsB) + (costB * sinEpsB2);
        if (xb != 0.0d) {
            double t3 = (((costA + costB) * (1.0d + cosEpsB)) - ((sintA + sintB) * (temp2 + sinEpsB2))) * xb;
            double c4 = a2 + t3;
            double d4 = -((c4 - a2) - t3);
            a2 = c4;
            b3 += d4;
        }
        double result = a2 + b3;
        return result;
    }

    private static double cosQ(double xa, double xb) {
        double a2 = 1.5707963267948966d - xa;
        double b2 = -((a2 - 1.5707963267948966d) + xa);
        return sinQ(a2, b2 + (6.123233995736766E-17d - xb));
    }

    private static double tanQ(double xa, double xb, boolean cotanFlag) {
        int idx = (int) ((xa * 8.0d) + 0.5d);
        double epsilon = xa - EIGHTHS[idx];
        double sintA = SINE_TABLE_A[idx];
        double sintB = SINE_TABLE_B[idx];
        double costA = COSINE_TABLE_A[idx];
        double costB = COSINE_TABLE_B[idx];
        double sinEpsB = polySine(epsilon);
        double cosEpsB = polyCosine(epsilon);
        double temp = epsilon * 1.073741824E9d;
        double temp2 = (epsilon + temp) - temp;
        double sinEpsB2 = sinEpsB + (epsilon - temp2);
        double c2 = 0.0d + sintA;
        double d2 = -((c2 - 0.0d) - sintA);
        double b2 = 0.0d + d2;
        double t2 = costA * temp2;
        double c3 = c2 + t2;
        double d3 = -((c3 - c2) - t2);
        double b3 = b2 + d3 + (sintA * cosEpsB) + (costA * sinEpsB2) + sintB + (costB * temp2) + (sintB * cosEpsB) + (costB * sinEpsB2);
        double sina = c3 + b3;
        double sinb = -((sina - c3) - b3);
        double t3 = costA * 1.0d;
        double c4 = 0.0d + t3;
        double d4 = -((c4 - 0.0d) - t3);
        double b4 = 0.0d + d4;
        double t4 = (-sintA) * temp2;
        double c5 = c4 + t4;
        double d5 = -((c5 - c4) - t4);
        double b5 = ((b4 + d5) + (((costB * 1.0d) + (costA * cosEpsB)) + (costB * cosEpsB))) - (((sintB * temp2) + (sintA * sinEpsB2)) + (sintB * sinEpsB2));
        double cosa = c5 + b5;
        double cosb = -((cosa - c5) - b5);
        if (cotanFlag) {
            cosa = sina;
            sina = cosa;
            cosb = sinb;
            sinb = cosb;
        }
        double est = sina / cosa;
        double temp3 = est * 1.073741824E9d;
        double esta = (est + temp3) - temp3;
        double estb = est - esta;
        double temp4 = cosa * 1.073741824E9d;
        double cosaa = (cosa + temp4) - temp4;
        double cosab = cosa - cosaa;
        double err = (((((sina - (esta * cosaa)) - (esta * cosab)) - (estb * cosaa)) - (estb * cosab)) / cosa) + (sinb / cosa) + ((((-sina) * cosb) / cosa) / cosa);
        if (xb != 0.0d) {
            double xbadj = xb + (est * est * xb);
            if (cotanFlag) {
                xbadj = -xbadj;
            }
            err += xbadj;
        }
        return est + err;
    }

    private static void reducePayneHanek(double x2, double[] result) {
        long shpi0;
        long shpiA;
        long shpiB;
        long inbits = Double.doubleToRawLongBits(x2);
        int exponent = (((int) ((inbits >> 52) & 2047)) - 1023) + 1;
        long inbits2 = ((inbits & 4503599627370495L) | IMPLICIT_HIGH_BIT) << 11;
        int idx = exponent >> 6;
        int shift = exponent - (idx << 6);
        if (shift != 0) {
            long shpi02 = idx == 0 ? 0L : RECIP_2PI[idx - 1] << shift;
            shpi0 = shpi02 | (RECIP_2PI[idx] >>> (64 - shift));
            shpiA = (RECIP_2PI[idx] << shift) | (RECIP_2PI[idx + 1] >>> (64 - shift));
            shpiB = (RECIP_2PI[idx + 1] << shift) | (RECIP_2PI[idx + 2] >>> (64 - shift));
        } else {
            shpi0 = idx == 0 ? 0L : RECIP_2PI[idx - 1];
            shpiA = RECIP_2PI[idx];
            shpiB = RECIP_2PI[idx + 1];
        }
        long a2 = inbits2 >>> 32;
        long b2 = inbits2 & 4294967295L;
        long c2 = shpiA >>> 32;
        long d2 = shpiA & 4294967295L;
        long ac2 = a2 * c2;
        long bd2 = b2 * d2;
        long bc2 = b2 * c2;
        long ad2 = a2 * d2;
        long prodB = bd2 + (ad2 << 32);
        long prodA = ac2 + (ad2 >>> 32);
        boolean bita = (bd2 & Long.MIN_VALUE) != 0;
        boolean bitb = (ad2 & 2147483648L) != 0;
        boolean bitsum = (prodB & Long.MIN_VALUE) != 0;
        if ((bita && bitb) || ((bita || bitb) && !bitsum)) {
            prodA++;
        }
        boolean bita2 = (prodB & Long.MIN_VALUE) != 0;
        boolean bitb2 = (bc2 & 2147483648L) != 0;
        long prodB2 = prodB + (bc2 << 32);
        long prodA2 = prodA + (bc2 >>> 32);
        boolean bitsum2 = (prodB2 & Long.MIN_VALUE) != 0;
        if ((bita2 && bitb2) || ((bita2 || bitb2) && !bitsum2)) {
            prodA2++;
        }
        long c3 = shpiB >>> 32;
        long ac3 = a2 * c3;
        long ac4 = ac3 + (((b2 * c3) + (a2 * (shpiB & 4294967295L))) >>> 32);
        boolean bita3 = (prodB2 & Long.MIN_VALUE) != 0;
        boolean bitb3 = (ac4 & Long.MIN_VALUE) != 0;
        long prodB3 = prodB2 + ac4;
        boolean bitsum3 = (prodB3 & Long.MIN_VALUE) != 0;
        if ((bita3 && bitb3) || ((bita3 || bitb3) && !bitsum3)) {
            prodA2++;
        }
        long d3 = shpi0 & 4294967295L;
        long prodA3 = prodA2 + (b2 * d3) + (((b2 * (shpi0 >>> 32)) + (a2 * d3)) << 32);
        int intPart = (int) (prodA3 >>> 62);
        long prodA4 = (prodA3 << 2) | (prodB3 >>> 62);
        long prodB4 = prodB3 << 2;
        long a3 = prodA4 >>> 32;
        long b3 = prodA4 & 4294967295L;
        long c4 = PI_O_4_BITS[0] >>> 32;
        long d4 = PI_O_4_BITS[0] & 4294967295L;
        long ac5 = a3 * c4;
        long bd3 = b3 * d4;
        long bc3 = b3 * c4;
        long ad3 = a3 * d4;
        long prod2B = bd3 + (ad3 << 32);
        long prod2A = ac5 + (ad3 >>> 32);
        boolean bita4 = (bd3 & Long.MIN_VALUE) != 0;
        boolean bitb4 = (ad3 & 2147483648L) != 0;
        boolean bitsum4 = (prod2B & Long.MIN_VALUE) != 0;
        if ((bita4 && bitb4) || ((bita4 || bitb4) && !bitsum4)) {
            prod2A++;
        }
        boolean bita5 = (prod2B & Long.MIN_VALUE) != 0;
        boolean bitb5 = (bc3 & 2147483648L) != 0;
        long prod2B2 = prod2B + (bc3 << 32);
        long prod2A2 = prod2A + (bc3 >>> 32);
        boolean bitsum5 = (prod2B2 & Long.MIN_VALUE) != 0;
        if ((bita5 && bitb5) || ((bita5 || bitb5) && !bitsum5)) {
            prod2A2++;
        }
        long c5 = PI_O_4_BITS[1] >>> 32;
        long ac6 = a3 * c5;
        long ac7 = ac6 + (((b3 * c5) + (a3 * (PI_O_4_BITS[1] & 4294967295L))) >>> 32);
        boolean bita6 = (prod2B2 & Long.MIN_VALUE) != 0;
        boolean bitb6 = (ac7 & Long.MIN_VALUE) != 0;
        long prod2B3 = prod2B2 + ac7;
        boolean bitsum6 = (prod2B3 & Long.MIN_VALUE) != 0;
        if ((bita6 && bitb6) || ((bita6 || bitb6) && !bitsum6)) {
            prod2A2++;
        }
        long a4 = prodB4 >>> 32;
        long b4 = prodB4 & 4294967295L;
        long c6 = PI_O_4_BITS[0] >>> 32;
        long ac8 = a4 * c6;
        long ac9 = ac8 + (((b4 * c6) + (a4 * (PI_O_4_BITS[0] & 4294967295L))) >>> 32);
        boolean bita7 = (prod2B3 & Long.MIN_VALUE) != 0;
        boolean bitb7 = (ac9 & Long.MIN_VALUE) != 0;
        boolean bitsum7 = ((prod2B3 + ac9) & Long.MIN_VALUE) != 0;
        if ((bita7 && bitb7) || ((bita7 || bitb7) && !bitsum7)) {
            prod2A2++;
        }
        double tmpA = (prod2A2 >>> 12) / TWO_POWER_52;
        double tmpB = ((((prod2A2 & 4095) << 40) + (r0 >>> 24)) / TWO_POWER_52) / TWO_POWER_52;
        double sumA = tmpA + tmpB;
        double sumB = -((sumA - tmpA) - tmpB);
        result[0] = intPart;
        result[1] = sumA * 2.0d;
        result[2] = sumB * 2.0d;
    }

    public static double sin(double x2) {
        boolean negative = false;
        int quadrant = 0;
        double xb = 0.0d;
        double xa = x2;
        if (x2 < 0.0d) {
            negative = true;
            xa = -xa;
        }
        if (xa == 0.0d) {
            long bits = Double.doubleToRawLongBits(x2);
            if (bits < 0) {
                return -0.0d;
            }
            return 0.0d;
        }
        if (xa != xa || xa == Double.POSITIVE_INFINITY) {
            return Double.NaN;
        }
        if (xa > 3294198.0d) {
            double[] reduceResults = new double[3];
            reducePayneHanek(xa, reduceResults);
            quadrant = ((int) reduceResults[0]) & 3;
            xa = reduceResults[1];
            xb = reduceResults[2];
        } else if (xa > 1.5707963267948966d) {
            CodyWaite cw = new CodyWaite(xa);
            quadrant = cw.getK() & 3;
            xa = cw.getRemA();
            xb = cw.getRemB();
        }
        if (negative) {
            quadrant ^= 2;
        }
        switch (quadrant) {
            case 0:
                return sinQ(xa, xb);
            case 1:
                return cosQ(xa, xb);
            case 2:
                return -sinQ(xa, xb);
            case 3:
                return -cosQ(xa, xb);
            default:
                return Double.NaN;
        }
    }

    public static double cos(double x2) {
        int quadrant = 0;
        double xa = x2;
        if (x2 < 0.0d) {
            xa = -xa;
        }
        if (xa != xa || xa == Double.POSITIVE_INFINITY) {
            return Double.NaN;
        }
        double xb = 0.0d;
        if (xa > 3294198.0d) {
            double[] reduceResults = new double[3];
            reducePayneHanek(xa, reduceResults);
            quadrant = ((int) reduceResults[0]) & 3;
            xa = reduceResults[1];
            xb = reduceResults[2];
        } else if (xa > 1.5707963267948966d) {
            CodyWaite cw = new CodyWaite(xa);
            quadrant = cw.getK() & 3;
            xa = cw.getRemA();
            xb = cw.getRemB();
        }
        switch (quadrant) {
            case 0:
                return cosQ(xa, xb);
            case 1:
                return -sinQ(xa, xb);
            case 2:
                return -cosQ(xa, xb);
            case 3:
                return sinQ(xa, xb);
            default:
                return Double.NaN;
        }
    }

    public static double tan(double x2) {
        double result;
        boolean negative = false;
        int quadrant = 0;
        double xa = x2;
        if (x2 < 0.0d) {
            negative = true;
            xa = -xa;
        }
        if (xa == 0.0d) {
            long bits = Double.doubleToRawLongBits(x2);
            if (bits < 0) {
                return -0.0d;
            }
            return 0.0d;
        }
        if (xa != xa || xa == Double.POSITIVE_INFINITY) {
            return Double.NaN;
        }
        double xb = 0.0d;
        if (xa > 3294198.0d) {
            double[] reduceResults = new double[3];
            reducePayneHanek(xa, reduceResults);
            quadrant = ((int) reduceResults[0]) & 3;
            xa = reduceResults[1];
            xb = reduceResults[2];
        } else if (xa > 1.5707963267948966d) {
            CodyWaite cw = new CodyWaite(xa);
            quadrant = cw.getK() & 3;
            xa = cw.getRemA();
            xb = cw.getRemB();
        }
        if (xa > 1.5d) {
            double a2 = 1.5707963267948966d - xa;
            double b2 = (-((a2 - 1.5707963267948966d) + xa)) + (6.123233995736766E-17d - xb);
            xa = a2 + b2;
            xb = -((xa - a2) - b2);
            quadrant ^= 1;
            negative = !negative;
        }
        if ((quadrant & 1) == 0) {
            result = tanQ(xa, xb, false);
        } else {
            result = -tanQ(xa, xb, true);
        }
        if (negative) {
            result = -result;
        }
        return result;
    }

    public static double atan(double x2) {
        return atan(x2, 0.0d, false);
    }

    private static double atan(double xa, double xb, boolean leftPlane) {
        boolean negate;
        int idx;
        double ya;
        double yb;
        if (xa == 0.0d) {
            return leftPlane ? copySign(3.141592653589793d, xa) : xa;
        }
        if (xa < 0.0d) {
            xa = -xa;
            xb = -xb;
            negate = true;
        } else {
            negate = false;
        }
        if (xa > 1.633123935319537E16d) {
            return negate ^ leftPlane ? -1.5707963267948966d : 1.5707963267948966d;
        }
        if (xa < 1.0d) {
            idx = (int) (((((-1.7168146928204135d) * xa * xa) + 8.0d) * xa) + 0.5d);
        } else {
            double oneOverXa = 1.0d / xa;
            idx = (int) ((-((((-1.7168146928204135d) * oneOverXa * oneOverXa) + 8.0d) * oneOverXa)) + 13.07d);
        }
        double ttA = TANGENT_TABLE_A[idx];
        double ttB = TANGENT_TABLE_B[idx];
        double epsA = xa - ttA;
        double epsB = (-((epsA - xa) + ttA)) + (xb - ttB);
        double temp = epsA + epsB;
        double epsB2 = -((temp - epsA) - epsB);
        double temp2 = xa * 1.073741824E9d;
        double ya2 = (xa + temp2) - temp2;
        double yb2 = (xb + xa) - ya2;
        double xb2 = xb + yb2;
        if (idx == 0) {
            double denom = 1.0d / (1.0d + ((ya2 + xb2) * (ttA + ttB)));
            ya = temp * denom;
            yb = epsB2 * denom;
        } else {
            double temp22 = ya2 * ttA;
            double za = 1.0d + temp22;
            double zb = -((za - 1.0d) - temp22);
            double temp23 = (xb2 * ttA) + (ya2 * ttB);
            double temp3 = za + temp23;
            double zb2 = zb + (-((temp3 - za) - temp23)) + (xb2 * ttB);
            ya = temp / temp3;
            double temp4 = ya * 1.073741824E9d;
            double yaa = (ya + temp4) - temp4;
            double yab = ya - yaa;
            double temp5 = temp3 * 1.073741824E9d;
            double zaa = (temp3 + temp5) - temp5;
            double zab = temp3 - zaa;
            double yb3 = ((((temp - (yaa * zaa)) - (yaa * zab)) - (yab * zaa)) - (yab * zab)) / temp3;
            yb = yb3 + ((((-temp) * zb2) / temp3) / temp3) + (epsB2 / temp3);
        }
        double epsA2 = ya;
        double epsA22 = epsA2 * epsA2;
        double yb4 = (0.07490822288864472d * epsA22) - 0.09088450866185192d;
        double yb5 = ((((((((yb4 * epsA22) + 0.11111095942313305d) * epsA22) - 0.1428571423679182d) * epsA22) + 0.19999999999923582d) * epsA22) - 0.33333333333333287d) * epsA22 * epsA2;
        double temp6 = epsA2 + yb5;
        double yb6 = (-((temp6 - epsA2) - yb5)) + (yb / (1.0d + (epsA2 * epsA2)));
        double eighths = EIGHTHS[idx];
        double za2 = eighths + temp6;
        double zb3 = -((za2 - eighths) - temp6);
        double temp7 = za2 + yb6;
        double zb4 = zb3 + (-((temp7 - za2) - yb6));
        double result = temp7 + zb4;
        if (leftPlane) {
            double resultb = -((result - temp7) - zb4);
            double za3 = 3.141592653589793d - result;
            double zb5 = -((za3 - 3.141592653589793d) + result);
            result = za3 + zb5 + (1.2246467991473532E-16d - resultb);
        }
        if (negate ^ leftPlane) {
            result = -result;
        }
        return result;
    }

    public static double atan2(double y2, double x2) {
        if (x2 != x2 || y2 != y2) {
            return Double.NaN;
        }
        if (y2 == 0.0d) {
            double result = x2 * y2;
            double invx = 1.0d / x2;
            double invy = 1.0d / y2;
            if (invx == 0.0d) {
                if (x2 > 0.0d) {
                    return y2;
                }
                return copySign(3.141592653589793d, y2);
            }
            if (x2 >= 0.0d && invx >= 0.0d) {
                return result;
            }
            if (y2 < 0.0d || invy < 0.0d) {
                return -3.141592653589793d;
            }
            return 3.141592653589793d;
        }
        if (y2 == Double.POSITIVE_INFINITY) {
            if (x2 == Double.POSITIVE_INFINITY) {
                return 0.7853981633974483d;
            }
            if (x2 == Double.NEGATIVE_INFINITY) {
                return 2.356194490192345d;
            }
            return 1.5707963267948966d;
        }
        if (y2 == Double.NEGATIVE_INFINITY) {
            if (x2 == Double.POSITIVE_INFINITY) {
                return -0.7853981633974483d;
            }
            if (x2 == Double.NEGATIVE_INFINITY) {
                return -2.356194490192345d;
            }
            return -1.5707963267948966d;
        }
        if (x2 == Double.POSITIVE_INFINITY) {
            if (y2 > 0.0d || 1.0d / y2 > 0.0d) {
                return 0.0d;
            }
            if (y2 < 0.0d || 1.0d / y2 < 0.0d) {
                return -0.0d;
            }
        }
        if (x2 == Double.NEGATIVE_INFINITY) {
            if (y2 > 0.0d || 1.0d / y2 > 0.0d) {
                return 3.141592653589793d;
            }
            if (y2 < 0.0d || 1.0d / y2 < 0.0d) {
                return -3.141592653589793d;
            }
        }
        if (x2 == 0.0d) {
            if (y2 > 0.0d || 1.0d / y2 > 0.0d) {
                return 1.5707963267948966d;
            }
            if (y2 < 0.0d || 1.0d / y2 < 0.0d) {
                return -1.5707963267948966d;
            }
        }
        double r2 = y2 / x2;
        if (Double.isInfinite(r2)) {
            return atan(r2, 0.0d, x2 < 0.0d);
        }
        double ra = doubleHighPart(r2);
        double rb = r2 - ra;
        double xa = doubleHighPart(x2);
        double xb = x2 - xa;
        double rb2 = rb + (((((y2 - (ra * xa)) - (ra * xb)) - (rb * xa)) - (rb * xb)) / x2);
        double temp = ra + rb2;
        double rb3 = -((temp - ra) - rb2);
        double ra2 = temp;
        if (ra2 == 0.0d) {
            ra2 = copySign(0.0d, y2);
        }
        double result2 = atan(ra2, rb3, x2 < 0.0d);
        return result2;
    }

    public static double asin(double x2) {
        if (x2 != x2 || x2 > 1.0d || x2 < -1.0d) {
            return Double.NaN;
        }
        if (x2 == 1.0d) {
            return 1.5707963267948966d;
        }
        if (x2 == -1.0d) {
            return -1.5707963267948966d;
        }
        if (x2 == 0.0d) {
            return x2;
        }
        double temp = x2 * 1.073741824E9d;
        double xa = (x2 + temp) - temp;
        double xb = x2 - xa;
        double ya = xa * xa;
        double yb = (xa * xb * 2.0d) + (xb * xb);
        double ya2 = -ya;
        double yb2 = -yb;
        double za = 1.0d + ya2;
        double zb = -((za - 1.0d) - ya2);
        double temp2 = za + yb2;
        double zb2 = zb + (-((temp2 - za) - yb2));
        double y2 = sqrt(temp2);
        double temp3 = y2 * 1.073741824E9d;
        double ya3 = (y2 + temp3) - temp3;
        double yb3 = y2 - ya3;
        double yb4 = yb3 + ((((temp2 - (ya3 * ya3)) - ((2.0d * ya3) * yb3)) - (yb3 * yb3)) / (2.0d * y2));
        double dx = zb2 / (2.0d * y2);
        double r2 = x2 / y2;
        double temp4 = r2 * 1.073741824E9d;
        double ra = (r2 + temp4) - temp4;
        double rb = r2 - ra;
        double rb2 = rb + (((((x2 - (ra * ya3)) - (ra * yb4)) - (rb * ya3)) - (rb * yb4)) / y2) + ((((-x2) * dx) / y2) / y2);
        double temp5 = ra + rb2;
        return atan(temp5, -((temp5 - ra) - rb2), false);
    }

    public static double acos(double x2) {
        if (x2 != x2 || x2 > 1.0d || x2 < -1.0d) {
            return Double.NaN;
        }
        if (x2 == -1.0d) {
            return 3.141592653589793d;
        }
        if (x2 == 1.0d) {
            return 0.0d;
        }
        if (x2 == 0.0d) {
            return 1.5707963267948966d;
        }
        double temp = x2 * 1.073741824E9d;
        double xa = (x2 + temp) - temp;
        double xb = x2 - xa;
        double ya = xa * xa;
        double yb = (xa * xb * 2.0d) + (xb * xb);
        double ya2 = -ya;
        double yb2 = -yb;
        double za = 1.0d + ya2;
        double zb = -((za - 1.0d) - ya2);
        double temp2 = za + yb2;
        double zb2 = zb + (-((temp2 - za) - yb2));
        double y2 = sqrt(temp2);
        double temp3 = y2 * 1.073741824E9d;
        double ya3 = (y2 + temp3) - temp3;
        double yb3 = y2 - ya3;
        double yb4 = yb3 + ((((temp2 - (ya3 * ya3)) - ((2.0d * ya3) * yb3)) - (yb3 * yb3)) / (2.0d * y2)) + (zb2 / (2.0d * y2));
        double y3 = ya3 + yb4;
        double yb5 = -((y3 - ya3) - yb4);
        double r2 = y3 / x2;
        if (Double.isInfinite(r2)) {
            return 1.5707963267948966d;
        }
        double ra = doubleHighPart(r2);
        double rb = r2 - ra;
        double rb2 = rb + (((((y3 - (ra * xa)) - (ra * xb)) - (rb * xa)) - (rb * xb)) / x2) + (yb5 / x2);
        double temp4 = ra + rb2;
        return atan(temp4, -((temp4 - ra) - rb2), x2 < 0.0d);
    }

    public static double cbrt(double x2) {
        long inbits = Double.doubleToRawLongBits(x2);
        int exponent = ((int) ((inbits >> 52) & 2047)) - 1023;
        boolean subnormal = false;
        if (exponent == -1023) {
            if (x2 == 0.0d) {
                return x2;
            }
            subnormal = true;
            x2 *= 1.8014398509481984E16d;
            inbits = Double.doubleToRawLongBits(x2);
            exponent = ((int) ((inbits >> 52) & 2047)) - 1023;
        }
        if (exponent == 1024) {
            return x2;
        }
        int exp3 = exponent / 3;
        double p2 = Double.longBitsToDouble((inbits & Long.MIN_VALUE) | (((exp3 + 1023) & 2047) << 52));
        double mant = Double.longBitsToDouble((inbits & 4503599627370495L) | 4607182418800017408L);
        double est = ((-0.010714690733195933d) * mant) + 0.0875862700108075d;
        double est2 = ((((((est * mant) - 0.3058015757857271d) * mant) + 0.7249995199969751d) * mant) + 0.5039018405998233d) * CBRTTWO[(exponent % 3) + 2];
        double xs = x2 / ((p2 * p2) * p2);
        double est3 = est2 + ((xs - ((est2 * est2) * est2)) / ((3.0d * est2) * est2));
        double est4 = est3 + ((xs - ((est3 * est3) * est3)) / ((3.0d * est3) * est3));
        double temp = est4 * 1.073741824E9d;
        double ya = (est4 + temp) - temp;
        double yb = est4 - ya;
        double za = ya * ya;
        double zb = (ya * yb * 2.0d) + (yb * yb);
        double temp2 = za * 1.073741824E9d;
        double temp22 = (za + temp2) - temp2;
        double zb2 = zb + (za - temp22);
        double zb3 = (temp22 * yb) + (ya * zb2) + (zb2 * yb);
        double za2 = temp22 * ya;
        double na = xs - za2;
        double nb = -((na - xs) + za2);
        double est5 = (est4 + ((na + (nb - zb3)) / ((3.0d * est4) * est4))) * p2;
        if (subnormal) {
            est5 *= 3.814697265625E-6d;
        }
        return est5;
    }

    public static double toRadians(double x2) {
        if (Double.isInfinite(x2) || x2 == 0.0d) {
            return x2;
        }
        double xa = doubleHighPart(x2);
        double xb = x2 - xa;
        double result = (xb * 1.997844754509471E-9d) + (xb * 0.01745329052209854d) + (xa * 1.997844754509471E-9d) + (xa * 0.01745329052209854d);
        if (result == 0.0d) {
            result *= x2;
        }
        return result;
    }

    public static double toDegrees(double x2) {
        if (Double.isInfinite(x2) || x2 == 0.0d) {
            return x2;
        }
        double xa = doubleHighPart(x2);
        double xb = x2 - xa;
        return (xb * 3.145894820876798E-6d) + (xb * 57.2957763671875d) + (xa * 3.145894820876798E-6d) + (xa * 57.2957763671875d);
    }

    public static int abs(int x2) {
        int i2 = x2 >>> 31;
        return (x2 ^ ((i2 ^ (-1)) + 1)) + i2;
    }

    public static long abs(long x2) {
        long l2 = x2 >>> 63;
        return (x2 ^ ((l2 ^ (-1)) + 1)) + l2;
    }

    public static float abs(float x2) {
        return Float.intBitsToFloat(Integer.MAX_VALUE & Float.floatToRawIntBits(x2));
    }

    public static double abs(double x2) {
        return Double.longBitsToDouble(Long.MAX_VALUE & Double.doubleToRawLongBits(x2));
    }

    public static double ulp(double x2) {
        if (Double.isInfinite(x2)) {
            return Double.POSITIVE_INFINITY;
        }
        return abs(x2 - Double.longBitsToDouble(Double.doubleToRawLongBits(x2) ^ 1));
    }

    public static float ulp(float x2) {
        if (Float.isInfinite(x2)) {
            return Float.POSITIVE_INFINITY;
        }
        return abs(x2 - Float.intBitsToFloat(Float.floatToIntBits(x2) ^ 1));
    }

    public static double scalb(double d2, int n2) {
        if (n2 > -1023 && n2 < 1024) {
            return d2 * Double.longBitsToDouble((n2 + 1023) << 52);
        }
        if (Double.isNaN(d2) || Double.isInfinite(d2) || d2 == 0.0d) {
            return d2;
        }
        if (n2 < -2098) {
            return d2 > 0.0d ? 0.0d : -0.0d;
        }
        if (n2 > 2097) {
            return d2 > 0.0d ? Double.POSITIVE_INFINITY : Double.NEGATIVE_INFINITY;
        }
        long bits = Double.doubleToRawLongBits(d2);
        long sign = bits & Long.MIN_VALUE;
        int exponent = ((int) (bits >>> 52)) & 2047;
        long mantissa = bits & 4503599627370495L;
        int scaledExponent = exponent + n2;
        if (n2 < 0) {
            if (scaledExponent > 0) {
                return Double.longBitsToDouble(sign | (scaledExponent << 52) | mantissa);
            }
            if (scaledExponent <= -53) {
                return sign == 0 ? 0.0d : -0.0d;
            }
            long mantissa2 = mantissa | IMPLICIT_HIGH_BIT;
            long mostSignificantLostBit = mantissa2 & (1 << (-scaledExponent));
            long mantissa3 = mantissa2 >>> (1 - scaledExponent);
            if (mostSignificantLostBit != 0) {
                mantissa3++;
            }
            return Double.longBitsToDouble(sign | mantissa3);
        }
        if (exponent == 0) {
            while ((mantissa >>> 52) != 1) {
                mantissa <<= 1;
                scaledExponent--;
            }
            int scaledExponent2 = scaledExponent + 1;
            long mantissa4 = mantissa & 4503599627370495L;
            if (scaledExponent2 < 2047) {
                return Double.longBitsToDouble(sign | (scaledExponent2 << 52) | mantissa4);
            }
            return sign == 0 ? Double.POSITIVE_INFINITY : Double.NEGATIVE_INFINITY;
        }
        if (scaledExponent < 2047) {
            return Double.longBitsToDouble(sign | (scaledExponent << 52) | mantissa);
        }
        return sign == 0 ? Double.POSITIVE_INFINITY : Double.NEGATIVE_INFINITY;
    }

    public static float scalb(float f2, int n2) {
        if (n2 > -127 && n2 < 128) {
            return f2 * Float.intBitsToFloat((n2 + 127) << 23);
        }
        if (Float.isNaN(f2) || Float.isInfinite(f2) || f2 == 0.0f) {
            return f2;
        }
        if (n2 < -277) {
            return f2 > 0.0f ? 0.0f : -0.0f;
        }
        if (n2 > 276) {
            return f2 > 0.0f ? Float.POSITIVE_INFINITY : Float.NEGATIVE_INFINITY;
        }
        int bits = Float.floatToIntBits(f2);
        int sign = bits & Integer.MIN_VALUE;
        int exponent = (bits >>> 23) & 255;
        int mantissa = bits & FloatConsts.SIGNIF_BIT_MASK;
        int scaledExponent = exponent + n2;
        if (n2 < 0) {
            if (scaledExponent > 0) {
                return Float.intBitsToFloat(sign | (scaledExponent << 23) | mantissa);
            }
            if (scaledExponent <= -24) {
                return sign == 0 ? 0.0f : -0.0f;
            }
            int mantissa2 = mantissa | 8388608;
            int mostSignificantLostBit = mantissa2 & (1 << (-scaledExponent));
            int mantissa3 = mantissa2 >>> (1 - scaledExponent);
            if (mostSignificantLostBit != 0) {
                mantissa3++;
            }
            return Float.intBitsToFloat(sign | mantissa3);
        }
        if (exponent == 0) {
            while ((mantissa >>> 23) != 1) {
                mantissa <<= 1;
                scaledExponent--;
            }
            int scaledExponent2 = scaledExponent + 1;
            int mantissa4 = mantissa & FloatConsts.SIGNIF_BIT_MASK;
            if (scaledExponent2 < 255) {
                return Float.intBitsToFloat(sign | (scaledExponent2 << 23) | mantissa4);
            }
            return sign == 0 ? Float.POSITIVE_INFINITY : Float.NEGATIVE_INFINITY;
        }
        if (scaledExponent < 255) {
            return Float.intBitsToFloat(sign | (scaledExponent << 23) | mantissa);
        }
        return sign == 0 ? Float.POSITIVE_INFINITY : Float.NEGATIVE_INFINITY;
    }

    public static double nextAfter(double d2, double direction) {
        if (Double.isNaN(d2) || Double.isNaN(direction)) {
            return Double.NaN;
        }
        if (d2 == direction) {
            return direction;
        }
        if (Double.isInfinite(d2)) {
            return d2 < 0.0d ? -1.7976931348623157E308d : Double.MAX_VALUE;
        }
        if (d2 == 0.0d) {
            return direction < 0.0d ? -4.9E-324d : Double.MIN_VALUE;
        }
        long bits = Double.doubleToRawLongBits(d2);
        long sign = bits & Long.MIN_VALUE;
        if ((direction < d2) ^ (sign == 0)) {
            return Double.longBitsToDouble(sign | ((bits & Long.MAX_VALUE) + 1));
        }
        return Double.longBitsToDouble(sign | ((bits & Long.MAX_VALUE) - 1));
    }

    public static float nextAfter(float f2, double direction) {
        if (Double.isNaN(f2) || Double.isNaN(direction)) {
            return Float.NaN;
        }
        if (f2 == direction) {
            return (float) direction;
        }
        if (Float.isInfinite(f2)) {
            return f2 < 0.0f ? -3.4028235E38f : Float.MAX_VALUE;
        }
        if (f2 == 0.0f) {
            return direction < 0.0d ? -1.4E-45f : Float.MIN_VALUE;
        }
        int bits = Float.floatToIntBits(f2);
        int sign = bits & Integer.MIN_VALUE;
        if ((direction < ((double) f2)) ^ (sign == 0)) {
            return Float.intBitsToFloat(sign | ((bits & Integer.MAX_VALUE) + 1));
        }
        return Float.intBitsToFloat(sign | ((bits & Integer.MAX_VALUE) - 1));
    }

    public static double floor(double x2) {
        if (x2 != x2) {
            return x2;
        }
        if (x2 >= TWO_POWER_52 || x2 <= -4.503599627370496E15d) {
            return x2;
        }
        long y2 = (long) x2;
        if (x2 < 0.0d && y2 != x2) {
            y2--;
        }
        if (y2 == 0) {
            return x2 * y2;
        }
        return y2;
    }

    public static double ceil(double x2) {
        if (x2 != x2) {
            return x2;
        }
        double y2 = floor(x2);
        if (y2 == x2) {
            return y2;
        }
        double y3 = y2 + 1.0d;
        if (y3 == 0.0d) {
            return x2 * y3;
        }
        return y3;
    }

    public static double rint(double x2) {
        double y2 = floor(x2);
        double d2 = x2 - y2;
        if (d2 > 0.5d) {
            if (y2 == -1.0d) {
                return -0.0d;
            }
            return y2 + 1.0d;
        }
        if (d2 < 0.5d) {
            return y2;
        }
        long z2 = (long) y2;
        return (z2 & 1) == 0 ? y2 : y2 + 1.0d;
    }

    public static long round(double x2) {
        return (long) floor(x2 + 0.5d);
    }

    public static int round(float x2) {
        return (int) floor(x2 + 0.5f);
    }

    public static int min(int a2, int b2) {
        return a2 <= b2 ? a2 : b2;
    }

    public static long min(long a2, long b2) {
        return a2 <= b2 ? a2 : b2;
    }

    public static float min(float a2, float b2) {
        if (a2 > b2) {
            return b2;
        }
        if (a2 < b2) {
            return a2;
        }
        if (a2 != b2) {
            return Float.NaN;
        }
        int bits = Float.floatToRawIntBits(a2);
        if (bits == Integer.MIN_VALUE) {
            return a2;
        }
        return b2;
    }

    public static double min(double a2, double b2) {
        if (a2 > b2) {
            return b2;
        }
        if (a2 < b2) {
            return a2;
        }
        if (a2 != b2) {
            return Double.NaN;
        }
        long bits = Double.doubleToRawLongBits(a2);
        if (bits == Long.MIN_VALUE) {
            return a2;
        }
        return b2;
    }

    public static int max(int a2, int b2) {
        return a2 <= b2 ? b2 : a2;
    }

    public static long max(long a2, long b2) {
        return a2 <= b2 ? b2 : a2;
    }

    public static float max(float a2, float b2) {
        if (a2 > b2) {
            return a2;
        }
        if (a2 < b2) {
            return b2;
        }
        if (a2 != b2) {
            return Float.NaN;
        }
        int bits = Float.floatToRawIntBits(a2);
        if (bits == Integer.MIN_VALUE) {
            return b2;
        }
        return a2;
    }

    public static double max(double a2, double b2) {
        if (a2 > b2) {
            return a2;
        }
        if (a2 < b2) {
            return b2;
        }
        if (a2 != b2) {
            return Double.NaN;
        }
        long bits = Double.doubleToRawLongBits(a2);
        if (bits == Long.MIN_VALUE) {
            return b2;
        }
        return a2;
    }

    public static double hypot(double x2, double y2) {
        if (Double.isInfinite(x2) || Double.isInfinite(y2)) {
            return Double.POSITIVE_INFINITY;
        }
        if (Double.isNaN(x2) || Double.isNaN(y2)) {
            return Double.NaN;
        }
        int expX = getExponent(x2);
        int expY = getExponent(y2);
        if (expX > expY + 27) {
            return abs(x2);
        }
        if (expY > expX + 27) {
            return abs(y2);
        }
        int middleExp = (expX + expY) / 2;
        double scaledX = scalb(x2, -middleExp);
        double scaledY = scalb(y2, -middleExp);
        double scaledH = sqrt((scaledX * scaledX) + (scaledY * scaledY));
        return scalb(scaledH, middleExp);
    }

    public static double IEEEremainder(double dividend, double divisor) {
        return StrictMath.IEEEremainder(dividend, divisor);
    }

    public static int toIntExact(long n2) throws MathArithmeticException {
        if (n2 < -2147483648L || n2 > 2147483647L) {
            throw new MathArithmeticException(LocalizedFormats.OVERFLOW, new Object[0]);
        }
        return (int) n2;
    }

    public static int incrementExact(int n2) throws MathArithmeticException {
        if (n2 == Integer.MAX_VALUE) {
            throw new MathArithmeticException(LocalizedFormats.OVERFLOW_IN_ADDITION, Integer.valueOf(n2), 1);
        }
        return n2 + 1;
    }

    public static long incrementExact(long n2) throws MathArithmeticException {
        if (n2 == Long.MAX_VALUE) {
            throw new MathArithmeticException(LocalizedFormats.OVERFLOW_IN_ADDITION, Long.valueOf(n2), 1);
        }
        return n2 + 1;
    }

    public static int decrementExact(int n2) throws MathArithmeticException {
        if (n2 == Integer.MIN_VALUE) {
            throw new MathArithmeticException(LocalizedFormats.OVERFLOW_IN_SUBTRACTION, Integer.valueOf(n2), 1);
        }
        return n2 - 1;
    }

    public static long decrementExact(long n2) throws MathArithmeticException {
        if (n2 == Long.MIN_VALUE) {
            throw new MathArithmeticException(LocalizedFormats.OVERFLOW_IN_SUBTRACTION, Long.valueOf(n2), 1);
        }
        return n2 - 1;
    }

    public static int addExact(int a2, int b2) throws MathArithmeticException {
        int sum = a2 + b2;
        if ((a2 ^ b2) >= 0 && (sum ^ b2) < 0) {
            throw new MathArithmeticException(LocalizedFormats.OVERFLOW_IN_ADDITION, Integer.valueOf(a2), Integer.valueOf(b2));
        }
        return sum;
    }

    public static long addExact(long a2, long b2) throws MathArithmeticException {
        long sum = a2 + b2;
        if ((a2 ^ b2) >= 0 && (sum ^ b2) < 0) {
            throw new MathArithmeticException(LocalizedFormats.OVERFLOW_IN_ADDITION, Long.valueOf(a2), Long.valueOf(b2));
        }
        return sum;
    }

    public static int subtractExact(int a2, int b2) {
        int sub = a2 - b2;
        if ((a2 ^ b2) < 0 && (sub ^ b2) >= 0) {
            throw new MathArithmeticException(LocalizedFormats.OVERFLOW_IN_SUBTRACTION, Integer.valueOf(a2), Integer.valueOf(b2));
        }
        return sub;
    }

    public static long subtractExact(long a2, long b2) {
        long sub = a2 - b2;
        if ((a2 ^ b2) < 0 && (sub ^ b2) >= 0) {
            throw new MathArithmeticException(LocalizedFormats.OVERFLOW_IN_SUBTRACTION, Long.valueOf(a2), Long.valueOf(b2));
        }
        return sub;
    }

    public static int multiplyExact(int a2, int b2) {
        if ((b2 > 0 && (a2 > Integer.MAX_VALUE / b2 || a2 < Integer.MIN_VALUE / b2)) || ((b2 < -1 && (a2 > Integer.MIN_VALUE / b2 || a2 < Integer.MAX_VALUE / b2)) || (b2 == -1 && a2 == Integer.MIN_VALUE))) {
            throw new MathArithmeticException(LocalizedFormats.OVERFLOW_IN_MULTIPLICATION, Integer.valueOf(a2), Integer.valueOf(b2));
        }
        return a2 * b2;
    }

    public static long multiplyExact(long a2, long b2) {
        if ((b2 > 0 && (a2 > Long.MAX_VALUE / b2 || a2 < Long.MIN_VALUE / b2)) || ((b2 < -1 && (a2 > Long.MIN_VALUE / b2 || a2 < Long.MAX_VALUE / b2)) || (b2 == -1 && a2 == Long.MIN_VALUE))) {
            throw new MathArithmeticException(LocalizedFormats.OVERFLOW_IN_MULTIPLICATION, Long.valueOf(a2), Long.valueOf(b2));
        }
        return a2 * b2;
    }

    public static int floorDiv(int a2, int b2) throws MathArithmeticException {
        if (b2 == 0) {
            throw new MathArithmeticException(LocalizedFormats.ZERO_DENOMINATOR, new Object[0]);
        }
        int m2 = a2 % b2;
        if ((a2 ^ b2) >= 0 || m2 == 0) {
            return a2 / b2;
        }
        return (a2 / b2) - 1;
    }

    public static long floorDiv(long a2, long b2) throws MathArithmeticException {
        if (b2 == 0) {
            throw new MathArithmeticException(LocalizedFormats.ZERO_DENOMINATOR, new Object[0]);
        }
        long m2 = a2 % b2;
        if ((a2 ^ b2) >= 0 || m2 == 0) {
            return a2 / b2;
        }
        return (a2 / b2) - 1;
    }

    public static int floorMod(int a2, int b2) throws MathArithmeticException {
        if (b2 == 0) {
            throw new MathArithmeticException(LocalizedFormats.ZERO_DENOMINATOR, new Object[0]);
        }
        int m2 = a2 % b2;
        if ((a2 ^ b2) >= 0 || m2 == 0) {
            return m2;
        }
        return b2 + m2;
    }

    public static long floorMod(long a2, long b2) {
        if (b2 == 0) {
            throw new MathArithmeticException(LocalizedFormats.ZERO_DENOMINATOR, new Object[0]);
        }
        long m2 = a2 % b2;
        if ((a2 ^ b2) >= 0 || m2 == 0) {
            return m2;
        }
        return b2 + m2;
    }

    public static double copySign(double magnitude, double sign) {
        long m2 = Double.doubleToRawLongBits(magnitude);
        long s2 = Double.doubleToRawLongBits(sign);
        if ((m2 ^ s2) >= 0) {
            return magnitude;
        }
        return -magnitude;
    }

    public static float copySign(float magnitude, float sign) {
        int m2 = Float.floatToRawIntBits(magnitude);
        int s2 = Float.floatToRawIntBits(sign);
        if ((m2 ^ s2) >= 0) {
            return magnitude;
        }
        return -magnitude;
    }

    public static int getExponent(double d2) {
        return ((int) ((Double.doubleToRawLongBits(d2) >>> 52) & 2047)) - 1023;
    }

    public static int getExponent(float f2) {
        return ((Float.floatToRawIntBits(f2) >>> 23) & 255) - 127;
    }

    public static void main(String[] a2) {
        PrintStream out = System.out;
        FastMathCalc.printarray(out, "EXP_INT_TABLE_A", EXP_INT_TABLE_LEN, ExpIntTable.EXP_INT_TABLE_A);
        FastMathCalc.printarray(out, "EXP_INT_TABLE_B", EXP_INT_TABLE_LEN, ExpIntTable.EXP_INT_TABLE_B);
        FastMathCalc.printarray(out, "EXP_FRAC_TABLE_A", 1025, ExpFracTable.EXP_FRAC_TABLE_A);
        FastMathCalc.printarray(out, "EXP_FRAC_TABLE_B", 1025, ExpFracTable.EXP_FRAC_TABLE_B);
        FastMathCalc.printarray(out, "LN_MANT", 1024, lnMant.LN_MANT);
        FastMathCalc.printarray(out, "SINE_TABLE_A", 14, SINE_TABLE_A);
        FastMathCalc.printarray(out, "SINE_TABLE_B", 14, SINE_TABLE_B);
        FastMathCalc.printarray(out, "COSINE_TABLE_A", 14, COSINE_TABLE_A);
        FastMathCalc.printarray(out, "COSINE_TABLE_B", 14, COSINE_TABLE_B);
        FastMathCalc.printarray(out, "TANGENT_TABLE_A", 14, TANGENT_TABLE_A);
        FastMathCalc.printarray(out, "TANGENT_TABLE_B", 14, TANGENT_TABLE_B);
    }

    /* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/util/FastMath$ExpIntTable.class */
    private static class ExpIntTable {
        private static final double[] EXP_INT_TABLE_A = FastMathLiteralArrays.loadExpIntA();
        private static final double[] EXP_INT_TABLE_B = FastMathLiteralArrays.loadExpIntB();

        private ExpIntTable() {
        }
    }

    /* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/util/FastMath$ExpFracTable.class */
    private static class ExpFracTable {
        private static final double[] EXP_FRAC_TABLE_A = FastMathLiteralArrays.loadExpFracA();
        private static final double[] EXP_FRAC_TABLE_B = FastMathLiteralArrays.loadExpFracB();

        private ExpFracTable() {
        }
    }

    /* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/util/FastMath$lnMant.class */
    private static class lnMant {
        private static final double[][] LN_MANT = FastMathLiteralArrays.loadLnMant();

        private lnMant() {
        }
    }

    /* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/util/FastMath$CodyWaite.class */
    private static class CodyWaite {
        private final int finalK;
        private final double finalRemA;
        private final double finalRemB;

        CodyWaite(double xa) {
            int k2 = (int) (xa * 0.6366197723675814d);
            while (true) {
                double a2 = (-k2) * 1.570796251296997d;
                double remA = xa + a2;
                double remB = -((remA - xa) - a2);
                double a3 = (-k2) * 7.549789948768648E-8d;
                double remA2 = a3 + remA;
                double remB2 = remB + (-((remA2 - remA) - a3));
                double a4 = (-k2) * 6.123233995736766E-17d;
                double remA3 = a4 + remA2;
                double remB3 = remB2 + (-((remA3 - remA2) - a4));
                if (remA3 <= 0.0d) {
                    k2--;
                } else {
                    this.finalK = k2;
                    this.finalRemA = remA3;
                    this.finalRemB = remB3;
                    return;
                }
            }
        }

        int getK() {
            return this.finalK;
        }

        double getRemA() {
            return this.finalRemA;
        }

        double getRemB() {
            return this.finalRemB;
        }
    }
}
