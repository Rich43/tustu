package G;

import com.efiAnalytics.plugin.ecu.ControllerParameter;
import org.apache.commons.math3.geometry.VectorFormat;
import org.icepdf.core.util.PdfOps;

/* loaded from: TunerStudioMS.jar:G/cY.class */
public class cY {

    /* renamed from: a, reason: collision with root package name */
    private static cY f1063a = null;

    private cY() {
    }

    public static cY a() {
        if (f1063a == null) {
            f1063a = new cY();
        }
        return f1063a;
    }

    public cZ a(cX cXVar, String str) {
        return a(null, cXVar, str);
    }

    public cZ a(R r2, String str) {
        return a(r2, null, str);
    }

    private cZ a(R r2, cX cXVar, String str) throws NumberFormatException, V.g {
        if (str.trim().startsWith(VectorFormat.DEFAULT_PREFIX)) {
            str = bH.W.b(bH.W.b(str, VectorFormat.DEFAULT_PREFIX, ""), "}", "");
        }
        String strTrim = str.trim();
        if (strTrim.indexOf("bitStringValue(") > -1) {
            String strI = bH.W.i(str.substring(0, str.indexOf("bitStringValue(")));
            String strSubstring = str.substring(str.indexOf("bitStringValue("), str.lastIndexOf(")") + 1);
            String strI2 = bH.W.i(str.substring(str.lastIndexOf(")") + 1, str.length()));
            String strSubstring2 = strSubstring.substring("bitStringValue(".length());
            String strSubstring3 = strSubstring2.substring(0, strSubstring2.lastIndexOf(")"));
            String[] strArr = new String[2];
            try {
                strArr[0] = strSubstring3.substring(0, strSubstring3.indexOf(",")).trim();
                strArr[1] = strSubstring3.substring(strSubstring3.indexOf(",") + 1).trim();
                if (r2 != null) {
                    aM aMVarC = r2.c(strArr[0]);
                    if (aMVarC == null) {
                        throw new V.g("bitStringValue([bitConstantName], [expression]): Invalid bitConstantName: " + strArr[0] + " not found");
                    }
                    if (!aMVarC.i().equals(ControllerParameter.PARAM_CLASS_BITS)) {
                        throw new V.g("bitStringValue([bitConstantName], [expression]): Invalid bitConstantName: " + strArr[0] + " is not paramClass bits");
                    }
                }
                String strB = bH.W.b(bH.W.b(strArr[1], VectorFormat.DEFAULT_PREFIX, ""), "}", "");
                C0121d c0121d = r2 == null ? new C0121d(cXVar, strArr[0], strB) : new C0121d(new C0133p(r2.c()), strArr[0], strB);
                if (!strI.trim().isEmpty()) {
                    c0121d.a(strI);
                }
                if (!strI2.trim().isEmpty()) {
                    c0121d.b(strI2);
                }
                return c0121d;
            } catch (Exception e2) {
                throw new V.g("bitStringValue([bitConstantName], [expression]): Invalid Expression");
            }
        }
        if (str.indexOf("$stringValue(") != -1) {
            String strI3 = bH.W.i(str.substring(0, str.indexOf("$stringValue(")));
            String strSubstring4 = str.substring(str.indexOf("$stringValue("), str.lastIndexOf(")") + 1);
            String strI4 = bH.W.i(str.substring(str.lastIndexOf(")") + 1, str.length()));
            String strSubstring5 = strSubstring4.substring("$stringValue(".length());
            String[] strArrC = bH.W.c(strSubstring5.substring(0, strSubstring5.lastIndexOf(")")), ",");
            String strSubstring6 = strArrC[0];
            int i2 = -1;
            if (strSubstring6.indexOf("[") > -1) {
                i2 = Integer.parseInt(strSubstring6.substring(strSubstring6.indexOf("[") + 1, strSubstring6.indexOf("]")));
                strSubstring6 = strSubstring6.substring(0, strSubstring6.indexOf("["));
            }
            aM aMVarC2 = null;
            if (r2 != null) {
                aMVarC2 = r2.c(strSubstring6);
                if (aMVarC2 == null) {
                    throw new V.g("stringValue([StringConstantName]): Invalid String Constant Name: " + strArrC[0] + " not found");
                }
            }
            cA cAVar = r2 != null ? new cA(r2, aMVarC2) : new cA(cXVar, strSubstring6);
            cAVar.a(i2);
            cAVar.a(strI3);
            cAVar.b(strI4);
            return cAVar;
        }
        if (!strTrim.startsWith("stringValue(")) {
            if (str.indexOf("$getWorkingDirPath(") != -1) {
                String strI5 = bH.W.i(str.substring(0, str.indexOf("$getWorkingDirPath(")));
                str.substring(str.indexOf("$getWorkingDirPath("), str.lastIndexOf(")") + 1);
                String strI6 = bH.W.i(str.substring(str.lastIndexOf(")") + 1, str.length()));
                dl dlVar = new dl(r2);
                dlVar.a(strI5);
                dlVar.b(strI6);
                return dlVar;
            }
            if (str.indexOf("$getProjectsDirPath(") == -1) {
                if (str.trim().startsWith(PdfOps.DOUBLE_QUOTE__TOKEN)) {
                    str = bH.W.i(str);
                }
                return new C0094c(str);
            }
            String strI7 = bH.W.i(str.substring(0, str.indexOf("$getProjectsDirPath(")));
            str.substring(str.indexOf("$getProjectsDirPath("), str.lastIndexOf(")") + 1);
            String strI8 = bH.W.i(str.substring(str.lastIndexOf(")") + 1, str.length()));
            cI cIVar = new cI(r2);
            cIVar.a(strI7);
            cIVar.b(strI8);
            return cIVar;
        }
        String strTrim2 = bH.W.i(str.substring(0, str.indexOf("stringValue("))).trim();
        String strTrim3 = str.substring(str.indexOf("stringValue("), str.lastIndexOf(")") + 1).trim();
        String strTrim4 = bH.W.i(str.substring(str.lastIndexOf(")") + 1, str.length())).trim();
        String strSubstring7 = strTrim3.substring("stringValue(".length());
        String[] strArrC2 = bH.W.c(strSubstring7.substring(0, strSubstring7.lastIndexOf(")")), ",");
        String strSubstring8 = strArrC2[0];
        int i3 = -1;
        if (strSubstring8.indexOf("[") > -1) {
            i3 = Integer.parseInt(strSubstring8.substring(strSubstring8.indexOf("[") + 1, strSubstring8.indexOf("]")));
            strSubstring8 = strSubstring8.substring(0, strSubstring8.indexOf("["));
        }
        aM aMVarC3 = null;
        if (r2 != null) {
            aMVarC3 = r2.c(strSubstring8);
            if (aMVarC3 == null) {
                throw new V.g("stringValue([StringConstantName]): Invalid String Constant Name: " + strArrC2[0] + " not found");
            }
        }
        cA cAVar2 = r2 != null ? new cA(r2, aMVarC3) : new cA(cXVar, strSubstring8);
        cAVar2.a(i3);
        cAVar2.a(strTrim2);
        cAVar2.b(strTrim4);
        return cAVar2;
    }
}
