package k;

import L.V;
import W.C0184j;
import W.C0188n;
import ax.C0890F;
import ax.C0892H;
import ax.Q;
import ax.U;
import bH.C;
import bH.W;
import h.h;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.icepdf.core.util.PdfOps;
import org.slf4j.Marker;
import sun.util.locale.LanguageTag;

/* renamed from: k.a, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:k/a.class */
public class C1753a extends Q {

    /* renamed from: a, reason: collision with root package name */
    String[] f12891a = null;

    /* renamed from: b, reason: collision with root package name */
    Map f12892b = new HashMap();

    /* renamed from: c, reason: collision with root package name */
    String f12893c;

    public C1753a(String str) {
        String str2;
        this.f12893c = null;
        this.f12893c = str;
        int i2 = 0;
        String strA = W.a(W.a(W.b(str, "Math.", ""), " AND ", " && "), " OR ", " || ");
        while (true) {
            String str3 = strA;
            if (str3.indexOf("[") == -1) {
                super.a(d(str3));
                return;
            }
            String strSubstring = str3.substring(str3.indexOf("]") + 1);
            String strSubstring2 = str3.substring(str3.lastIndexOf("[", str3.indexOf("]")), str3.indexOf("]") + 1);
            String strSubstring3 = str3.substring(0, str3.lastIndexOf("[", str3.indexOf("]")));
            C1754b c1754bC = c(strSubstring2);
            if (c1754bC.b() != 0) {
                int i3 = i2;
                i2++;
                str2 = "TimeShiftVariableAlias" + i3;
            } else {
                int i4 = i2;
                i2++;
                str2 = "TimeShiftVariableAlias" + i4;
            }
            String str4 = str2;
            this.f12892b.put(str4, c1754bC);
            strA = new StringBuffer(strSubstring3).append(str4).append(strSubstring).toString();
        }
    }

    public double a(C0188n c0188n, int i2) {
        float fA;
        String[] strArrA = a();
        for (int i3 = 0; i3 < strArrA.length; i3++) {
            try {
                String str = strArrA[i3];
                if (!str.startsWith("TimeShiftVariableAlias") || this.f12892b.get(str) == null) {
                    fA = a(str, c0188n, i2, 0);
                } else {
                    C1754b c1754b = (C1754b) this.f12892b.get(str);
                    fA = a(c1754b.a(), c0188n, i2, c1754b.b());
                }
                super.a(strArrA[i3], fA);
            } catch (C0890F e2) {
                C.c("Error setting variable '" + strArrA[i3] + "' in formula:\n" + super.c());
                Logger.getLogger(C1753a.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
            } catch (C0892H e3) {
                C.c("Error setting variable '" + strArrA[i3] + "' in formula:\n" + super.c());
                Logger.getLogger(C1753a.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e3);
            }
        }
        return super.d();
    }

    @Override // ax.Q
    public String[] a() {
        if (this.f12891a == null || this.f12891a.length == 0) {
            this.f12891a = super.a();
        }
        return this.f12891a;
    }

    public String[] b() {
        ArrayList arrayList = new ArrayList();
        Iterator it = this.f12892b.values().iterator();
        while (it.hasNext()) {
            arrayList.add(((C1754b) it.next()).a());
        }
        return (String[]) arrayList.toArray(new String[arrayList.size()]);
    }

    protected float a(String str, C0188n c0188n, int i2, int i3) throws U {
        C0184j c0184jA = c0188n.a(str);
        if (c0184jA == null) {
            throw new U("Field: " + str + " not found in currently loaded data set.\n this field is required to evaluate expression:\n" + this.f12893c + "\n\nPlease make sure any Option fields required for this expression are turned on.");
        }
        if (c0184jA.v() <= 0) {
            return Float.NaN;
        }
        if (i2 + i3 < 0) {
            return c0184jA.c(0);
        }
        if (i2 + i3 >= 0 && i2 + i3 > c0184jA.i() - 1) {
            return c0184jA.c(c0184jA.i() - 1);
        }
        if (i2 + i3 >= 0) {
            return c0184jA.c(i2 + i3);
        }
        return 0.0f;
    }

    private C1754b c(String str) {
        int i2 = 0;
        C1754b c1754b = new C1754b(this);
        if (str.startsWith("[") && str.endsWith("]")) {
            str = str.substring(1, str.length() - 1);
            int iIndexOf = str.indexOf(LanguageTag.SEP);
            if (iIndexOf > 0) {
                String strSubstring = str.substring(iIndexOf + 1);
                str = str.substring(0, iIndexOf);
                i2 = 0 - Integer.parseInt(strSubstring);
            } else if (str.indexOf(Marker.ANY_NON_NULL_MARKER) > 0) {
                int iIndexOf2 = str.indexOf(Marker.ANY_NON_NULL_MARKER);
                String strSubstring2 = str.substring(iIndexOf2 + 1);
                str = str.substring(0, iIndexOf2);
                i2 = 0 + Integer.parseInt(strSubstring2);
            }
        }
        c1754b.a(str);
        c1754b.a(i2);
        return c1754b;
    }

    private String d(String str) throws C1755c {
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
            String absolutePath = h.c().getAbsolutePath();
            String strB = W.b(strTrim2, PdfOps.DOUBLE_QUOTE__TOKEN, "");
            try {
                str = strSubstring + " table(" + strTrim + ", " + ((Object) V.a().a(absolutePath, strB)) + " ) " + str.substring(iIndexOf4 + 1);
                iIndexOf = str.indexOf("table(", iIndexOf + 6);
            } catch (FileNotFoundException e2) {
                C1755c c1755c = new C1755c(e2.getLocalizedMessage(), e2);
                c1755c.a(new File(strB));
                throw c1755c;
            }
        }
        return str;
    }

    @Override // ax.Q
    public String c() {
        return this.f12893c;
    }
}
