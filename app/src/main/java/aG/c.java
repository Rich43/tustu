package aG;

import G.C0126i;
import G.R;
import G.aI;
import bH.C;

/* loaded from: TunerStudioMS.jar:aG/c.class */
public class c {
    public static boolean a(R r2) {
        return a(r2, "replayExtendedEnabled");
    }

    public static boolean b(R r2) {
        return a(r2, "replayOriginalEnabled");
    }

    private static boolean a(R r2, String str) {
        try {
            return C0126i.a((aI) r2, str) != 0.0d;
        } catch (V.g e2) {
            C.d("Unable to get value for '" + str + "' Assumed to be disabled.");
            return false;
        }
    }
}
