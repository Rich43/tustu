package org.apache.commons.math3.special;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.util.MathArrays;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/special/BesselJ.class */
public class BesselJ implements UnivariateFunction {
    private static final double PI2 = 0.6366197723675814d;
    private static final double TOWPI1 = 6.28125d;
    private static final double TWOPI2 = 0.001935307179586477d;
    private static final double TWOPI = 6.283185307179586d;
    private static final double ENTEN = 1.0E308d;
    private static final double ENSIG = 1.0E16d;
    private static final double RTNSIG = 1.0E-4d;
    private static final double ENMTEN = 8.9E-308d;
    private static final double X_MIN = 0.0d;
    private static final double X_MAX = 10000.0d;
    private static final double[] FACT = {1.0d, 1.0d, 2.0d, 6.0d, 24.0d, 120.0d, 720.0d, 5040.0d, 40320.0d, 362880.0d, 3628800.0d, 3.99168E7d, 4.790016E8d, 6.2270208E9d, 8.71782912E10d, 1.307674368E12d, 2.0922789888E13d, 3.55687428096E14d, 6.402373705728E15d, 1.21645100408832E17d, 2.43290200817664E18d, 5.109094217170944E19d, 1.1240007277776077E21d, 2.585201673888498E22d, 6.204484017332394E23d};
    private final double order;

    public BesselJ(double order) {
        this.order = order;
    }

    @Override // org.apache.commons.math3.analysis.UnivariateFunction
    public double value(double x2) throws ConvergenceException, MathIllegalArgumentException {
        return value(this.order, x2);
    }

    public static double value(double order, double x2) throws ConvergenceException, MathIllegalArgumentException {
        int n2 = (int) order;
        double alpha = order - n2;
        int nb = n2 + 1;
        BesselJResult res = rjBesl(x2, alpha, nb);
        if (res.nVals >= nb) {
            return res.vals[n2];
        }
        if (res.nVals < 0) {
            throw new MathIllegalArgumentException(LocalizedFormats.BESSEL_FUNCTION_BAD_ARGUMENT, Double.valueOf(order), Double.valueOf(x2));
        }
        if (FastMath.abs(res.vals[res.nVals - 1]) >= 1.0E-100d) {
            throw new ConvergenceException(LocalizedFormats.BESSEL_FUNCTION_FAILED_CONVERGENCE, Double.valueOf(order), Double.valueOf(x2));
        }
        return res.vals[n2];
    }

    /* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/special/BesselJ$BesselJResult.class */
    public static class BesselJResult {
        private final double[] vals;
        private final int nVals;

        public BesselJResult(double[] b2, int n2) {
            this.vals = MathArrays.copyOf(b2, b2.length);
            this.nVals = n2;
        }

        public double[] getVals() {
            return MathArrays.copyOf(this.vals, this.vals.length);
        }

        public int getnVals() {
            return this.nVals;
        }
    }

    public static BesselJResult rjBesl(double x2, double alpha, int nb) {
        int ncalc;
        int nend;
        double pold;
        int m2;
        double[] b2 = new double[nb];
        int magx = (int) x2;
        if (nb > 0 && x2 >= 0.0d && x2 <= X_MAX && alpha >= 0.0d && alpha < 1.0d) {
            ncalc = nb;
            for (int i2 = 0; i2 < nb; i2++) {
                b2[i2] = 0.0d;
            }
            if (x2 < RTNSIG) {
                double tempa = 1.0d;
                double alpem = 1.0d + alpha;
                double halfx = 0.0d;
                if (x2 > ENMTEN) {
                    halfx = 0.5d * x2;
                }
                if (alpha != 0.0d) {
                    tempa = FastMath.pow(halfx, alpha) / (alpha * Gamma.gamma(alpha));
                }
                double tempb = 0.0d;
                if (x2 + 1.0d > 1.0d) {
                    tempb = (-halfx) * halfx;
                }
                b2[0] = tempa + ((tempa * tempb) / alpem);
                if (x2 != 0.0d && b2[0] == 0.0d) {
                    ncalc = 0;
                }
                if (nb != 1) {
                    if (x2 <= 0.0d) {
                        for (int n2 = 1; n2 < nb; n2++) {
                            b2[n2] = 0.0d;
                        }
                    } else {
                        double tempc = halfx;
                        double tover = tempb != 0.0d ? ENMTEN / tempb : 1.78E-307d / x2;
                        for (int n3 = 1; n3 < nb; n3++) {
                            double tempa2 = tempa / alpem;
                            alpem += 1.0d;
                            tempa = tempa2 * tempc;
                            if (tempa <= tover * alpem) {
                                tempa = 0.0d;
                            }
                            b2[n3] = tempa + ((tempa * tempb) / alpem);
                            if (b2[n3] == 0.0d && ncalc > n3) {
                                ncalc = n3;
                            }
                        }
                    }
                }
            } else if (x2 > 25.0d && nb <= magx + 1) {
                double xc = FastMath.sqrt(PI2 / x2);
                double mul = 0.125d / x2;
                double xin = mul * mul;
                if (x2 >= 130.0d) {
                    m2 = 4;
                } else if (x2 >= 35.0d) {
                    m2 = 8;
                } else {
                    m2 = 11;
                }
                double xm = 4.0d * m2;
                double t2 = (int) ((x2 / 6.283185307179586d) + 0.5d);
                double z2 = ((x2 - (t2 * TOWPI1)) - (t2 * TWOPI2)) - ((alpha + 0.5d) / PI2);
                double vsin = FastMath.sin(z2);
                double vcos = FastMath.cos(z2);
                double gnu2 = 2.0d * alpha;
                for (int i3 = 1; i3 <= 2; i3++) {
                    double s2 = ((xm - 1.0d) - gnu2) * ((xm - 1.0d) + gnu2) * xin * 0.5d;
                    double t3 = (gnu2 - (xm - 3.0d)) * (gnu2 + (xm - 3.0d));
                    double capp = (s2 * t3) / FACT[2 * m2];
                    double t1 = (gnu2 - (xm + 1.0d)) * (gnu2 + xm + 1.0d);
                    double capq = (s2 * t1) / FACT[(2 * m2) + 1];
                    double xk = xm;
                    int k2 = 2 * m2;
                    double t12 = t3;
                    for (int j2 = 2; j2 <= m2; j2++) {
                        xk -= 4.0d;
                        double s3 = ((xk - 1.0d) - gnu2) * ((xk - 1.0d) + gnu2);
                        double t4 = (gnu2 - (xk - 3.0d)) * (gnu2 + (xk - 3.0d));
                        capp = (capp + (1.0d / FACT[k2 - 2])) * s3 * t4 * xin;
                        capq = (capq + (1.0d / FACT[k2 - 1])) * s3 * t12 * xin;
                        k2 -= 2;
                        t12 = t4;
                    }
                    b2[i3 - 1] = xc * (((capp + 1.0d) * vcos) - ((((capq + 1.0d) * ((gnu2 * gnu2) - 1.0d)) * (0.125d / x2)) * vsin));
                    if (nb == 1) {
                        return new BesselJResult(MathArrays.copyOf(b2, b2.length), ncalc);
                    }
                    double t5 = vsin;
                    vsin = -vcos;
                    vcos = t5;
                    gnu2 += 2.0d;
                }
                if (nb > 2) {
                    double gnu3 = (2.0d * alpha) + 2.0d;
                    for (int j3 = 2; j3 < nb; j3++) {
                        b2[j3] = ((gnu3 * b2[j3 - 1]) / x2) - b2[j3 - 2];
                        gnu3 += 2.0d;
                    }
                }
            } else {
                int nbmx = nb - magx;
                int n4 = magx + 1;
                double en = 2.0d * (n4 + alpha);
                double plast = 1.0d;
                double p2 = en / x2;
                double test = 2.0E16d;
                boolean readyToInitialize = false;
                if (nbmx >= 3) {
                    int nstart = magx + 2;
                    int nend2 = nb - 1;
                    en = 2.0d * ((nstart - 1) + alpha);
                    int k3 = nstart;
                    while (true) {
                        if (k3 > nend2) {
                            break;
                        }
                        n4 = k3;
                        en += 2.0d;
                        double pold2 = plast;
                        plast = p2;
                        p2 = ((en * plast) / x2) - pold2;
                        if (p2 <= 1.0E292d) {
                            k3++;
                        } else {
                            double p3 = p2 / ENTEN;
                            plast /= ENTEN;
                            double psave = p3;
                            double psavel = plast;
                            int nstart2 = n4 + 1;
                            do {
                                n4++;
                                en += 2.0d;
                                pold = plast;
                                plast = p3;
                                p3 = ((en * plast) / x2) - pold;
                            } while (p3 <= 1.0d);
                            double tempb2 = en / x2;
                            test = ((pold * plast) * (0.5d - (0.5d / (tempb2 * tempb2)))) / ENSIG;
                            p2 = plast * ENTEN;
                            n4--;
                            en -= 2.0d;
                            nend2 = FastMath.min(nb, n4);
                            int l2 = nstart2;
                            while (true) {
                                if (l2 > nend2) {
                                    break;
                                }
                                double pold3 = psavel;
                                psavel = psave;
                                psave = ((en * psavel) / x2) - pold3;
                                if (psave * psavel <= test) {
                                    l2++;
                                } else {
                                    int i4 = l2 - 1;
                                    break;
                                }
                            }
                            ncalc = nend2;
                            readyToInitialize = true;
                        }
                    }
                    if (!readyToInitialize) {
                        n4 = nend2;
                        en = 2.0d * (n4 + alpha);
                        test = FastMath.max(test, FastMath.sqrt(plast * ENSIG) * FastMath.sqrt(2.0d * p2));
                    }
                }
                if (!readyToInitialize) {
                    do {
                        n4++;
                        en += 2.0d;
                        double pold4 = plast;
                        plast = p2;
                        p2 = ((en * plast) / x2) - pold4;
                    } while (p2 < test);
                }
                int n5 = n4 + 1;
                double en2 = en + 2.0d;
                double tempb3 = 0.0d;
                double tempa3 = 1.0d / p2;
                int m3 = (2 * n5) - (4 * (n5 / 2));
                double sum = 0.0d;
                double em = n5 / 2;
                double alpem2 = (em - 1.0d) + alpha;
                double alp2em = (2.0d * em) + alpha;
                if (m3 != 0) {
                    sum = ((tempa3 * alpem2) * alp2em) / em;
                }
                int nend3 = n5 - nb;
                boolean readyToNormalize = false;
                boolean calculatedB0 = false;
                for (int l3 = 1; l3 <= nend3; l3++) {
                    n5--;
                    en2 -= 2.0d;
                    double tempc2 = tempb3;
                    tempb3 = tempa3;
                    tempa3 = ((en2 * tempb3) / x2) - tempc2;
                    m3 = 2 - m3;
                    if (m3 != 0) {
                        em -= 1.0d;
                        double alp2em2 = (2.0d * em) + alpha;
                        if (n5 == 1) {
                            break;
                        }
                        double alpem3 = (em - 1.0d) + alpha;
                        if (alpem3 == 0.0d) {
                            alpem3 = 1.0d;
                        }
                        sum = ((sum + (tempa3 * alp2em2)) * alpem3) / em;
                    }
                }
                b2[n5 - 1] = tempa3;
                if (nend3 >= 0) {
                    if (nb <= 1) {
                        double alp2em3 = alpha;
                        if (alpha + 1.0d == 1.0d) {
                            alp2em3 = 1.0d;
                        }
                        sum += b2[0] * alp2em3;
                        readyToNormalize = true;
                    } else {
                        n5--;
                        en2 -= 2.0d;
                        b2[n5 - 1] = ((en2 * tempa3) / x2) - tempb3;
                        if (n5 == 1) {
                            calculatedB0 = true;
                        } else {
                            m3 = 2 - m3;
                            if (m3 != 0) {
                                em -= 1.0d;
                                double alp2em4 = (2.0d * em) + alpha;
                                double alpem4 = (em - 1.0d) + alpha;
                                if (alpem4 == 0.0d) {
                                    alpem4 = 1.0d;
                                }
                                sum = ((sum + (b2[n5 - 1] * alp2em4)) * alpem4) / em;
                            }
                        }
                    }
                }
                if (!readyToNormalize && !calculatedB0 && (nend = n5 - 2) != 0) {
                    for (int l4 = 1; l4 <= nend; l4++) {
                        n5--;
                        en2 -= 2.0d;
                        b2[n5 - 1] = ((en2 * b2[n5]) / x2) - b2[n5 + 1];
                        m3 = 2 - m3;
                        if (m3 != 0) {
                            em -= 1.0d;
                            double alp2em5 = (2.0d * em) + alpha;
                            double alpem5 = (em - 1.0d) + alpha;
                            if (alpem5 == 0.0d) {
                                alpem5 = 1.0d;
                            }
                            sum = ((sum + (b2[n5 - 1] * alp2em5)) * alpem5) / em;
                        }
                    }
                }
                if (!readyToNormalize) {
                    if (!calculatedB0) {
                        b2[0] = (((2.0d * (alpha + 1.0d)) * b2[1]) / x2) - b2[2];
                    }
                    double alp2em6 = (2.0d * (em - 1.0d)) + alpha;
                    if (alp2em6 == 0.0d) {
                        alp2em6 = 1.0d;
                    }
                    sum += b2[0] * alp2em6;
                }
                if (FastMath.abs(alpha) > 1.0E-16d) {
                    sum *= Gamma.gamma(alpha) * FastMath.pow(x2 * 0.5d, -alpha);
                }
                double tempa4 = 8.9E-308d;
                if (sum > 1.0d) {
                    tempa4 = ENMTEN * sum;
                }
                for (int n6 = 0; n6 < nb; n6++) {
                    if (FastMath.abs(b2[n6]) < tempa4) {
                        b2[n6] = 0.0d;
                    }
                    int i5 = n6;
                    b2[i5] = b2[i5] / sum;
                }
            }
        } else {
            if (b2.length > 0) {
                b2[0] = 0.0d;
            }
            ncalc = FastMath.min(nb, 0) - 1;
        }
        return new BesselJResult(MathArrays.copyOf(b2, b2.length), ncalc);
    }
}
