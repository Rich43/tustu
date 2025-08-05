package S;

import G.R;
import G.S;
import G.T;
import W.ap;
import bH.C;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import jdk.internal.dynalink.CallSiteDescriptor;

/* loaded from: TunerStudioMS.jar:S/b.class */
public class b implements S {

    /* renamed from: b, reason: collision with root package name */
    private final U.a f1816b = new U.a();

    /* renamed from: c, reason: collision with root package name */
    private ap f1817c = null;

    /* renamed from: d, reason: collision with root package name */
    private final List f1818d = new ArrayList();

    /* renamed from: e, reason: collision with root package name */
    private final HashMap f1819e = new HashMap();

    /* renamed from: a, reason: collision with root package name */
    List f1820a = new ArrayList();

    /* renamed from: f, reason: collision with root package name */
    private static b f1821f = null;

    private b() {
    }

    public static b a() {
        if (f1821f == null) {
            f1821f = new b();
        }
        return f1821f;
    }

    public j a(String str, String str2) {
        a aVarB = b(str, str2);
        if (aVarB == null || !(aVarB instanceof j)) {
            j jVar = new j();
            jVar.g(str2);
            if (this.f1817c != null) {
                jVar.h(str);
            }
            this.f1819e.put(c(str, str2), jVar);
            aVarB = jVar;
        }
        return (j) aVarB;
    }

    public a b(String str, String str2) {
        a aVarA = (a) this.f1819e.get(c(str, str2));
        if (aVarA == null) {
            Iterator it = this.f1818d.iterator();
            while (it.hasNext()) {
                aVarA = ((g) it.next()).a(str, str2);
                if (aVarA != null) {
                    break;
                }
            }
            if (aVarA == null) {
                aVarA = this.f1816b.a(str, str2);
            }
            if (aVarA != null) {
                this.f1819e.put(c(str, str2), aVarA);
            }
            if (aVarA instanceof k) {
                ((k) aVarA).d_();
            }
        }
        return aVarA;
    }

    private String[] c() {
        ArrayList arrayList = new ArrayList();
        for (g gVar : this.f1818d) {
            if (gVar instanceof l) {
                arrayList.addAll(((l) gVar).c());
            }
        }
        return (String[]) arrayList.toArray(new String[arrayList.size()]);
    }

    private String c(String str, String str2) {
        return ((T.a().c() == null || !T.a().c().c().equals(str)) ? str + "." : "") + str2;
    }

    public void a(g gVar) {
        this.f1818d.add(gVar);
    }

    public ap b() {
        return this.f1817c;
    }

    public void a(ap apVar) {
        this.f1817c = apVar;
        this.f1816b.a(apVar);
    }

    public void a(String str) {
        b(str);
        this.f1819e.clear();
    }

    public void b(String str) {
        if (this.f1817c == null) {
            C.d("No Persistor set, not saving active triggers.");
            return;
        }
        StringBuilder sb = new StringBuilder();
        Iterator it = e.a().a(str).iterator();
        while (it.hasNext()) {
            sb.append(((a) it.next()).a()).append(CallSiteDescriptor.OPERATOR_DELIMITER);
        }
        this.f1817c.a(c(str, "ActiveTriggers"), sb.toString());
    }

    protected void c(String str) {
        if (this.f1817c == null) {
            C.d("No Persistor set, not activating persisted triggers.");
            return;
        }
        String strC = c(str, "ActiveTriggers");
        String str2 = "";
        String[] strArrC = c();
        if (strArrC != null) {
            for (String str3 : strArrC) {
                str2 = str2 + str3 + CallSiteDescriptor.OPERATOR_DELIMITER;
            }
        }
        String[] strArrSplit = this.f1817c.b(strC, str2).split("\\|");
        for (int i2 = 0; i2 < strArrSplit.length; i2++) {
            if (!strArrSplit[i2].equals("")) {
                a aVarB = b(str, strArrSplit[i2]);
                if (aVarB == null || !aVarB.c()) {
                    C.d("EventTrigger: " + str + "." + strArrSplit[i2] + " not active.");
                } else {
                    e.a().a(str, aVarB);
                    C.d("EventTrigger: " + str + "." + strArrSplit[i2] + " loaded and activated.");
                }
            }
        }
        d(str);
    }

    private void d(String str) {
        Iterator it = this.f1820a.iterator();
        while (it.hasNext()) {
            ((m) it.next()).a(str);
        }
    }

    public void a(m mVar) {
        if (this.f1820a.contains(mVar)) {
            return;
        }
        this.f1820a.add(mVar);
    }

    @Override // G.S
    public void a(R r2) {
        new c(this, r2).start();
    }

    @Override // G.S
    public void b(R r2) {
    }

    @Override // G.S
    public void c(R r2) {
    }
}
