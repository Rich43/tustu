package ak;

import jdk.internal.dynalink.CallSiteDescriptor;
import org.icepdf.core.util.PdfOps;

/* loaded from: TunerStudioMS.jar:ak/R.class */
public class R extends C0546f {
    public R(String str, boolean z2) {
        super(str, z2);
    }

    @Override // ak.C0546f
    protected boolean c(String str, String str2) {
        try {
            if (str.startsWith(PdfOps.DOUBLE_QUOTE__TOKEN)) {
                str = str.substring(1);
            }
            String strSubstring = str.substring(0, str.indexOf(str2));
            if (strSubstring.trim().length() != 0 && !strSubstring.contains(CallSiteDescriptor.TOKEN_DELIMITER) && !strSubstring.contains("/")) {
                Double.parseDouble(strSubstring);
                return true;
            }
            int i2 = 0;
            aD aDVar = new aD(str, str2);
            int iC = aDVar.c();
            aDVar.b().trim();
            for (int i3 = 1; i3 < iC; i3++) {
                String strTrim = aDVar.b().trim();
                if (strTrim.contains(",")) {
                    strTrim = bH.W.b(strTrim, ",", ".");
                }
                if (strTrim.length() > 0) {
                    try {
                        Double.parseDouble(strTrim);
                    } catch (Exception e2) {
                        if (!bH.W.a(f4844z, strTrim) && !bH.W.a(f4843y, strTrim) && !strTrim.equals("N/A")) {
                            i2++;
                        }
                    }
                }
            }
            if (iC <= 6 || i2 / iC >= 0.3f) {
                return i2 == 0;
            }
            if (!strSubstring.contains(CallSiteDescriptor.TOKEN_DELIMITER)) {
                return true;
            }
            this.f4840v = true;
            return true;
        } catch (Exception e3) {
            return false;
        }
    }

    @Override // ak.C0546f, W.V
    public String i() {
        return W.X.f1959a;
    }
}
