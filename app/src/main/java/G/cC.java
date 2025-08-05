package G;

import com.efiAnalytics.plugin.ecu.ControllerParameter;
import org.icepdf.core.util.PdfOps;

/* loaded from: TunerStudioMS.jar:G/cC.class */
public class cC {
    public static void a(R r2, aM aMVar, String str) {
        if (str != null && str.indexOf(PdfOps.DOUBLE_QUOTE__TOKEN) != -1) {
            try {
                if (aMVar.B()) {
                    r2.a(aMVar.aJ(), str);
                }
                return;
            } catch (V.g e2) {
                bH.C.b("Attempt to set Parameter value, but not valid for current firmware: " + e2.getMessage());
                return;
            } catch (Exception e3) {
                throw new V.i("Error setting value: " + aMVar.aJ() + " to value: " + str);
            }
        }
        if (aMVar != null && aMVar.i().equals("string")) {
            try {
                r2.a(aMVar.aJ(), str);
                return;
            } catch (V.g e4) {
                bH.C.b("Failed to set value for " + aMVar.aJ() + " value is not valid for current configuration: " + e4.getMessage());
                return;
            }
        }
        if (aMVar == null || str == null) {
            bH.C.c("A value was null for " + aMVar.aJ() + "??? This shouldn't happen.");
            return;
        }
        double[][] dArrB = new double[aMVar.a()][aMVar.m()];
        if (aMVar != null) {
            try {
                if (aMVar.B() && !aMVar.i().equals("string")) {
                    try {
                        dArrB = bH.W.a(dArrB, str);
                    } catch (Exception e5) {
                        if (!aMVar.aJ().startsWith("UNALLOCATED_SPACE")) {
                            throw new V.i("Invalid table data in Constant: " + aMVar.aJ() + ", not loading values.");
                        }
                    }
                    A aC2 = aMVar.c();
                    boolean z2 = false;
                    if (aMVar.i().equals(ControllerParameter.PARAM_CLASS_ARRAY) && dArrB.length > 0 && aC2.b() > 0 && (dArrB.length != aC2.b() || dArrB[0].length != aC2.a())) {
                        if (aMVar.O()) {
                            z2 = true;
                            bH.C.d("Unallocated memory changed, skip loading filler Constants.");
                        } else if (aMVar.C()) {
                            bH.C.b(aMVar.aJ() + " array size does not match the currently loaded configuration,\n\trescaled values to match configuration.");
                            dArrB = bH.H.a(dArrB, aC2.b(), aC2.a());
                        } else {
                            bH.C.b(aMVar.aJ() + " array size does not match the currently loaded configuration,\n\tnoSizeMutation set for parameter values transfered to new array size where possible.");
                            dArrB = bH.H.b(dArrB, aC2.b(), aC2.a());
                        }
                    }
                    if (!z2) {
                        r2.a(aMVar.aJ(), dArrB);
                    }
                }
            } catch (V.g e6) {
                bH.C.b("Failed to set value for " + aMVar.aJ() + " value is not valid for current configuration: " + e6.getMessage());
            } catch (V.j e7) {
                boolean z3 = false;
                boolean z4 = false;
                for (int i2 = 0; i2 < dArrB.length; i2++) {
                    try {
                        for (int i3 = 0; i3 < dArrB[0].length; i3++) {
                            if (dArrB[i2][i3] > aMVar.r()) {
                                if (!z3) {
                                    if (bL.i(r2, aMVar.aJ())) {
                                        bH.C.b(aMVar.aJ() + " row:" + i2 + ", col:" + i3 + ", One or more value higher than maximum: " + dArrB[i2][i3] + ", Set to the maximum value: " + aMVar.r());
                                    }
                                    z3 = true;
                                }
                                dArrB[i2][i3] = aMVar.r();
                            } else if (dArrB[i2][i3] < aMVar.a(i2)) {
                                if (!z4) {
                                    bH.C.b(aMVar.aJ() + " row:" + i2 + ", col:" + i3 + ", One or more value below minimum: " + dArrB[i2][i3] + ", Set to the minimum value: " + aMVar.a(i2));
                                    z4 = true;
                                }
                                dArrB[i2][i3] = aMVar.a(i2);
                            }
                        }
                    } catch (Exception e8) {
                        bH.C.b("Failed to set value for " + aMVar.aJ() + " value is not valid for current configuration: " + e8.getMessage());
                        return;
                    }
                }
                r2.a(aMVar.aJ(), dArrB);
            }
        }
    }

    public static String a(R r2, aM aMVar) {
        try {
            if (aMVar.i().equals(ControllerParameter.PARAM_CLASS_BITS)) {
                return aMVar.f(r2.p());
            }
            if (aMVar.i().equals("string")) {
                return aMVar.d(r2.p());
            }
            if (aMVar.i().equals(ControllerParameter.PARAM_CLASS_SCALAR) || aMVar.i().equals(bZ.f884e) || aMVar.i().equals(bZ.f883d)) {
                return (Math.round(aMVar.j(r2.p()) * 1.0E7d) / 1.0E7d) + "";
            }
            if (!aMVar.i().equals(ControllerParameter.PARAM_CLASS_ARRAY)) {
                return null;
            }
            String[][] strArr = new String[aMVar.a()][aMVar.m()];
            double[][] dArrI = aMVar.i(r2.p());
            for (int i2 = 0; i2 < dArrI.length; i2++) {
                for (int i3 = 0; i3 < dArrI[0].length; i3++) {
                    strArr[i2][i3] = "" + (Math.round(dArrI[i2][i3] * 1.0E7d) / 1.0E7d);
                }
            }
            return bH.W.a(strArr);
        } catch (V.g e2) {
            bH.C.a(e2.getLocalizedMessage());
            return null;
        }
    }

    public static String a(R r2) {
        StringBuilder sb = new StringBuilder();
        for (aM aMVar : r2.l()) {
            if (aMVar instanceof bZ) {
                sb.append(aMVar.aJ()).append("=").append(a(r2, aMVar)).append("~");
            }
        }
        return sb.toString();
    }
}
