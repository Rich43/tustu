package G;

import org.apache.commons.math3.geometry.VectorFormat;

/* loaded from: TunerStudioMS.jar:G/di.class */
public class di {
    public static dh a(double d2) {
        return new B(d2);
    }

    public static dh a(aI aIVar, String str) {
        if (!bH.H.a(str)) {
            return new bQ(aIVar, bH.W.b(bH.W.b(str, VectorFormat.DEFAULT_PREFIX, ""), "}", ""));
        }
        try {
            return new B(str.startsWith("0x") ? bH.W.g(str) : Double.parseDouble(str));
        } catch (NumberFormatException e2) {
            throw new V.g("Non-Numeric Value.");
        }
    }

    public static dh a(cX cXVar, String str) {
        if (bH.H.a(str)) {
            return new B(Double.parseDouble(str));
        }
        String strB = bH.W.b(bH.W.b(str, VectorFormat.DEFAULT_PREFIX, ""), "}", "");
        return strB.isEmpty() ? new B(Double.NaN) : new bQ(cXVar, strB);
    }
}
