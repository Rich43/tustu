package L;

/* renamed from: L.z, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:L/z.class */
public class C0169z {
    public double a(double d2, double d3, double d4, double d5) {
        Double dA = a(Double.valueOf(d4 - d2));
        Double dA2 = a(Double.valueOf(d5 - d3));
        Double dValueOf = Double.valueOf((Math.sin(dA.doubleValue() / 2.0d) * Math.sin(dA.doubleValue() / 2.0d)) + (Math.cos(a(Double.valueOf(d2)).doubleValue()) * Math.cos(a(Double.valueOf(d4)).doubleValue()) * Math.sin(dA2.doubleValue() / 2.0d) * Math.sin(dA2.doubleValue() / 2.0d)));
        return Double.valueOf(6371.0d * Double.valueOf(2.0d * Math.atan2(Math.sqrt(dValueOf.doubleValue()), Math.sqrt(1.0d - dValueOf.doubleValue()))).doubleValue()).doubleValue();
    }

    private static Double a(Double d2) {
        return Double.valueOf((d2.doubleValue() * 3.141592653589793d) / 180.0d);
    }
}
