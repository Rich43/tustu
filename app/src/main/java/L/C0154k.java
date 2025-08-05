package L;

import G.C0126i;
import G.aI;
import G.aM;
import java.io.FileNotFoundException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.icepdf.core.util.PdfOps;

/* renamed from: L.k, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:L/k.class */
public class C0154k extends ax.Q {

    /* renamed from: a, reason: collision with root package name */
    String f1666a = "";

    /* renamed from: b, reason: collision with root package name */
    aI f1667b;

    /* renamed from: c, reason: collision with root package name */
    static C0151h f1668c = null;

    /* renamed from: f, reason: collision with root package name */
    private static String f1669f = "array.";

    public C0154k(aI aIVar) {
        this.f1667b = null;
        this.f1667b = aIVar;
        if (f1668c != null) {
            f1668c.a(aIVar);
        } else {
            f1668c = new C0151h(aIVar);
            ax.Q.a(f1668c);
        }
    }

    @Override // ax.Q
    public void a(String str) {
        this.f1666a = str;
        if (str.contains("timeNow") && str.indexOf("timeNow(") == -1) {
            str = bH.W.b(str, "timeNow", "timeNow()");
        }
        try {
            String strC = c(str);
            if (strC.indexOf("tempCvt(") != -1) {
                strC = C0126i.a(strC, this.f1667b.d("CELSIUS") != null);
            }
            while (strC.contains(f1669f)) {
                String strSubstring = strC.substring(0, strC.indexOf(f1669f));
                int iIndexOf = strC.indexOf(f1669f) + f1669f.length();
                int iA = a(strC, iIndexOf);
                String strSubstring2 = strC.substring(iIndexOf, iA);
                aM aMVarC = this.f1667b.c(strSubstring2);
                if (aMVarC == null) {
                    throw new ax.U("unknown EcuParameter " + strSubstring2 + " in expresstion: " + this.f1666a);
                }
                strC = strSubstring + C0155l.a().a(aMVarC) + strC.substring(iA);
            }
            super.a(strC);
        } catch (FileNotFoundException e2) {
            Logger.getLogger(C0154k.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
            throw new ax.U(e2.getMessage());
        }
    }

    private String c(String str) throws FileNotFoundException {
        int iIndexOf = str.indexOf("table(");
        if (iIndexOf == -1) {
            return str;
        }
        while (iIndexOf != -1) {
            String strSubstring = str.substring(0, str.indexOf("table("));
            int iIndexOf2 = str.indexOf("(", str.indexOf("table")) + 1;
            int iIndexOf3 = str.indexOf(",", iIndexOf2);
            String strTrim = str.substring(iIndexOf2, iIndexOf3).trim();
            int iIndexOf4 = str.indexOf(")", iIndexOf3);
            String strTrim2 = str.substring(iIndexOf3 + 1, iIndexOf4).trim();
            str = strSubstring + " table(" + strTrim + ", " + ((Object) V.a().a(this.f1667b.K().F(), bH.W.b(strTrim2, PdfOps.DOUBLE_QUOTE__TOKEN, ""))) + " ) " + str.substring(iIndexOf4 + 1);
            iIndexOf = str.indexOf("table(", iIndexOf + 6);
        }
        return str;
    }

    private static int a(String str, int i2) {
        int i3 = 0;
        int i4 = 0;
        int i5 = i2;
        while (i5 < str.length() && (i3 == 0 || i3 != i4)) {
            if (str.charAt(i5) == '(') {
                i3++;
            }
            if (str.charAt(i5) == ')') {
                i4++;
            }
            if (str.charAt(i5) == ',') {
                break;
            }
            i5++;
        }
        return i5;
    }
}
