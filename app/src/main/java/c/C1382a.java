package c;

import bH.C;
import org.icepdf.core.util.PdfOps;

/* renamed from: c.a, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:c/a.class */
public class C1382a {
    public static String a(String str, String str2) {
        C.d("Getting ECU name for signature: '" + str + PdfOps.SINGLE_QUOTE_TOKEN);
        if (str.toLowerCase().startsWith("rusefi")) {
            C.d("Looks like rusEFI");
            return "rusEFI";
        }
        if (str.toLowerCase().startsWith("speeduino")) {
            C.d("Looks like Speeduino");
            return "Speeduino";
        }
        if (!str.toLowerCase().startsWith("Trans")) {
            return str2;
        }
        C.d("Looks like MegaSquirt Trans Controller");
        return "MegaSquirt Trans Controller";
    }
}
