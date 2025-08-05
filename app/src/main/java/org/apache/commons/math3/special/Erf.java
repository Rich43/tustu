package org.apache.commons.math3.special;

import org.apache.commons.math3.util.FastMath;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/special/Erf.class */
public class Erf {
    private static final double X_CRIT = 0.4769362762044697d;

    private Erf() {
    }

    public static double erf(double x2) {
        if (FastMath.abs(x2) > 40.0d) {
            return x2 > 0.0d ? 1.0d : -1.0d;
        }
        double ret = Gamma.regularizedGammaP(0.5d, x2 * x2, 1.0E-15d, 10000);
        return x2 < 0.0d ? -ret : ret;
    }

    public static double erfc(double x2) {
        if (FastMath.abs(x2) > 40.0d) {
            return x2 > 0.0d ? 0.0d : 2.0d;
        }
        double ret = Gamma.regularizedGammaQ(0.5d, x2 * x2, 1.0E-15d, 10000);
        return x2 < 0.0d ? 2.0d - ret : ret;
    }

    public static double erf(double x1, double x2) {
        if (x1 > x2) {
            return -erf(x2, x1);
        }
        return x1 < -0.4769362762044697d ? x2 < 0.0d ? erfc(-x2) - erfc(-x1) : erf(x2) - erf(x1) : (x2 <= X_CRIT || x1 <= 0.0d) ? erf(x2) - erf(x1) : erfc(x1) - erfc(x2);
    }

    public static double erfInv(double x2) {
        double p2;
        double w2 = -FastMath.log((1.0d - x2) * (1.0d + x2));
        if (w2 < 6.25d) {
            double w3 = w2 - 3.125d;
            double p3 = (-1.6850591381820166E-19d) + ((-3.64441206401782E-21d) * w3);
            p2 = 1.6536545626831027d + ((0.24015818242558962d + (((-0.006033670871430149d) + (((-7.40702534166267E-4d) + ((1.8673420803405714E-4d + (((-1.3882523362786469E-5d) + (((-1.3654692000834679E-6d) + ((4.2347877827932404E-7d + (((-2.9070369957882005E-8d) + (((-4.112633980346984E-9d) + ((1.0512122733215323E-9d + (((-5.415412054294628E-11d) + (((-1.2975133253453532E-11d) + ((2.6335093153082323E-12d + (((-8.151934197605472E-14d) + (((-4.054566272975207E-14d) + ((6.637638134358324E-15d + ((2.0972767875968562E-17d + (((-1.333171662854621E-16d) + ((1.1157877678025181E-17d + ((1.28584807152564E-18d + (p3 * w3)) * w3)) * w3)) * w3)) * w3)) * w3)) * w3)) * w3)) * w3)) * w3)) * w3)) * w3)) * w3)) * w3)) * w3)) * w3)) * w3)) * w3)) * w3)) * w3)) * w3);
        } else if (w2 < 16.0d) {
            double w4 = FastMath.sqrt(w2) - 3.25d;
            double p4 = 9.075656193888539E-8d + (2.2137376921775787E-9d * w4);
            p2 = 3.0838856104922208d + ((1.0052589676941592d + ((0.005370914553590064d + (((-0.003751208507569241d) + ((0.002491442096107851d + (((-0.0016882755560235047d) + ((9.532893797373805E-4d + (((-3.550375203628475E-4d) + ((2.4031110387097894E-5d + ((6.828485145957318E-5d + (((-4.7318229009055734E-5d) + ((1.2475304481671779E-5d + ((2.9234449089955446E-6d + (((-4.013867526981546E-6d) + ((1.5027403968909828E-6d + ((1.8239629214389228E-8d + (((-2.7517406297064545E-7d) + (p4 * w4)) * w4)) * w4)) * w4)) * w4)) * w4)) * w4)) * w4)) * w4)) * w4)) * w4)) * w4)) * w4)) * w4)) * w4)) * w4)) * w4);
        } else if (!Double.isInfinite(w2)) {
            double w5 = FastMath.sqrt(w2) - 5.0d;
            double p5 = (-2.555641816996525E-10d) + ((-2.7109920616438573E-11d) * w5);
            p2 = 4.849906401408584d + ((1.0103004648645344d + (((-1.3871931833623122E-4d) + (((-2.1503011930044477E-4d) + ((7.599527703001776E-5d + (((-1.968177810553167E-5d) + ((4.526062597223154E-6d + (((-9.9298272942317E-7d) + ((2.2900482228026655E-7d + (((-6.771199775845234E-8d) + ((2.914795345090108E-8d + (((-1.496002662714924E-8d) + ((7.61570120807834E-9d + (((-3.789465440126737E-9d) + ((1.5076572693500548E-9d + (p5 * w5)) * w5)) * w5)) * w5)) * w5)) * w5)) * w5)) * w5)) * w5)) * w5)) * w5)) * w5)) * w5)) * w5)) * w5);
        } else {
            p2 = Double.POSITIVE_INFINITY;
        }
        return p2 * x2;
    }

    public static double erfcInv(double x2) {
        return erfInv(1.0d - x2);
    }
}
