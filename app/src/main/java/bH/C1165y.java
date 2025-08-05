package bh;

import G.C0043ac;
import G.C0048ah;
import G.R;
import G.T;
import bH.W;
import java.util.Iterator;
import sun.util.locale.LanguageTag;

/* renamed from: bh.y, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bh/y.class */
public class C1165y implements bB.b {
    @Override // bB.b
    public bB.r a(String str) {
        String strA = h.i.a("FIELD_MIN_MAX_" + str, (String) null);
        bB.a aVar = new bB.a();
        aVar.a(str);
        if (strA == null || strA.indexOf(";") == -1) {
            C0048ah c0048ahB = b(str);
            if (c0048ahB != null) {
                aVar.a(c0048ahB.a());
                aVar.b(c0048ahB.d());
                aVar.a(c0048ahB.l());
            } else {
                aVar.a(Double.NaN);
                aVar.b(Double.NaN);
                aVar.a(-1);
            }
        } else {
            try {
                String[] strArrSplit = strA.split(";");
                String str2 = strArrSplit[0];
                String str3 = strArrSplit.length > 1 ? strArrSplit[1] : "Auto";
                String str4 = strArrSplit.length > 2 ? strArrSplit[2] : "Auto";
                if (str2.contains("Auto")) {
                    aVar.a(Double.NaN);
                } else {
                    aVar.a(Double.parseDouble(str2));
                }
                if (str3.contains("Auto")) {
                    aVar.b(Double.NaN);
                } else {
                    aVar.b(Double.parseDouble(str3));
                }
                if (str4.contains("Auto")) {
                    aVar.a(-1);
                } else {
                    aVar.a(Integer.parseInt(str4));
                }
            } catch (Exception e2) {
            }
        }
        return aVar;
    }

    @Override // bB.b
    public bB.r a(bB.r rVar) {
        String strC = h.i.c("FIELD_MIN_MAX_" + rVar.e());
        if (strC != null) {
            try {
                bB.a aVar = new bB.a();
                aVar.a(rVar.e());
                String[] strArrSplit = strC.split(";");
                String str = strArrSplit[0];
                String str2 = strArrSplit.length > 1 ? strArrSplit[1] : "Auto";
                String str3 = strArrSplit.length > 2 ? strArrSplit[2] : "Auto";
                if (str.contains("Auto")) {
                    aVar.a(Double.NaN);
                } else {
                    aVar.a(Double.parseDouble(str));
                }
                if (str2.contains("Auto")) {
                    aVar.b(Double.NaN);
                } else {
                    aVar.b(Double.parseDouble(str2));
                }
                if (str3.contains("Auto")) {
                    aVar.a(-1);
                } else {
                    aVar.a(Integer.parseInt(str3));
                }
            } catch (Exception e2) {
                return null;
            }
        }
        bB.a aVar2 = new bB.a();
        aVar2.a(rVar.e());
        C0048ah c0048ahB = b(rVar.e());
        if (c0048ahB != null) {
            aVar2.a(c0048ahB.a());
            aVar2.b(c0048ahB.d());
            aVar2.a(c0048ahB.l());
        } else {
            aVar2.a(Double.NaN);
            aVar2.b(Double.NaN);
            aVar2.a(-1);
        }
        return aVar2;
    }

    private C0048ah b(String str) {
        C0043ac c0043acA;
        C0048ah c0048ahB;
        if (str.contains(".")) {
            String strSubstring = str.substring(0, str.indexOf(46));
            String strSubstring2 = str.substring(str.indexOf(46) + 1);
            R rC = T.a().c(strSubstring);
            if (rC != null && (c0043acA = a(rC, strSubstring2)) != null && (c0048ahB = b(rC, c0043acA.a())) != null) {
                return c0048ahB;
            }
        }
        R rC2 = T.a().c();
        C0043ac c0043acA2 = a(rC2, str);
        if (c0043acA2 == null || c0043acA2.a() == null || c0043acA2.a().isEmpty()) {
            return null;
        }
        return b(rC2, c0043acA2.a());
    }

    private C0043ac a(R r2, String str) {
        Iterator it = r2.g().iterator();
        while (it.hasNext()) {
            C0043ac c0043ac = (C0043ac) it.next();
            if (W.b(c0043ac.b(), LanguageTag.SEP, " ").equals(str)) {
                return c0043ac;
            }
        }
        return null;
    }

    private C0048ah b(R r2, String str) {
        Iterator itB = r2.B();
        while (itB.hasNext()) {
            C0048ah c0048ah = (C0048ah) itB.next();
            if (c0048ah.i().equals(str)) {
                return c0048ah;
            }
        }
        return null;
    }
}
