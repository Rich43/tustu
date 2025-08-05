package I;

import G.C0126i;
import G.R;
import G.T;
import G.aH;
import G.dh;
import W.am;
import bH.C;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/* loaded from: TunerStudioMS.jar:I/k.class */
public class k {

    /* renamed from: d, reason: collision with root package name */
    private static k f1383d = null;

    /* renamed from: c, reason: collision with root package name */
    private am f1382c = null;

    /* renamed from: a, reason: collision with root package name */
    Map f1384a = new HashMap();

    /* renamed from: b, reason: collision with root package name */
    Thread f1385b = null;

    private k() {
        new m(this).start();
    }

    public static k a() {
        if (f1383d == null) {
            f1383d = new k();
        }
        return f1383d;
    }

    public void a(am amVar) {
        this.f1382c = amVar;
    }

    private String c(String str, String str2) {
        return (T.a().c() == null || T.a().c().c().equals(str)) ? "_" + str2 : str + "_" + str2;
    }

    public void b() {
        this.f1384a.clear();
    }

    private List a(String str) {
        List arrayList = (List) this.f1384a.get(str);
        if (arrayList == null) {
            arrayList = new ArrayList();
            this.f1384a.put(str, arrayList);
        }
        return arrayList;
    }

    public void a(String str, String str2) {
        if (str == null || str.isEmpty()) {
            str = T.a().c().c();
        }
        aH aHVarG = T.a().c(str).g(str2);
        if (aHVarG == null || !aHVarG.b().equals("formula")) {
            return;
        }
        aHVarG.k();
        this.f1385b = Thread.currentThread();
        C0126i.b(str, str2);
        aHVarG.o();
        this.f1385b = null;
    }

    public double b(String str, String str2) {
        double d2;
        if (this.f1385b != null && this.f1385b.equals(Thread.currentThread())) {
            return 0.0d;
        }
        String strB = this.f1382c.b(c(str, str2), "0");
        try {
            d2 = Double.parseDouble(strB);
        } catch (Exception e2) {
            C.d("Bad stored value for key: " + str2 + " = " + strB + ", using 0");
            d2 = 0.0d;
        }
        if (Double.isNaN(d2)) {
            C.d("Stored value for key: " + str2 + " = NaN, using 0");
            d2 = 0.0d;
        }
        if (Double.isInfinite(d2)) {
            C.d("Stored value for key: " + str2 + " = Infinity, using 0");
            d2 = 0.0d;
        }
        return d2;
    }

    public void a(String str, String str2, dh dhVar) {
        l lVar = new l(this, str, str2, dhVar);
        List listA = a(str);
        if (listA.contains(str2)) {
            C.b("2 persisteAccumulate Entries with same key! : " + str2);
        }
        listA.add(lVar);
    }

    public void a(String str, boolean z2) {
        if (this.f1382c == null) {
            C.d("Persistor not set, not storing persistent accumulated values.");
            return;
        }
        List<l> list = (List) this.f1384a.get(str);
        if (z2) {
            str = "";
        }
        for (l lVar : list) {
            this.f1382c.a(c(str, lVar.b()), Double.toString(lVar.c().a()));
        }
    }

    public void c() {
        R rC = T.a().c();
        boolean z2 = false;
        Iterator it = this.f1384a.values().iterator();
        while (it.hasNext()) {
            for (l lVar : (List) it.next()) {
                String strA = (rC == null || !rC.c().equals(lVar.a())) ? lVar.a() : "";
                double dA = lVar.c().a();
                if (Double.isNaN(lVar.d()) || dA != lVar.d()) {
                    this.f1382c.a(c(strA, lVar.b()), Double.toString(dA));
                    lVar.a(dA);
                    z2 = true;
                }
            }
        }
        if (this.f1382c == null || !z2) {
            return;
        }
        this.f1382c.a();
    }
}
