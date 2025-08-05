package b;

/* renamed from: b.a, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:b/a.class */
public class C0954a {
    public static double a(double d2, double d3) {
        double dAbs;
        if (Math.abs(d2) > Math.abs(d3)) {
            double d4 = d3 / d2;
            dAbs = Math.abs(d2) * Math.sqrt(1.0d + (d4 * d4));
        } else if (d3 != 0.0d) {
            double d5 = d2 / d3;
            dAbs = Math.abs(d3) * Math.sqrt(1.0d + (d5 * d5));
        } else {
            dAbs = 0.0d;
        }
        return dAbs;
    }
}
