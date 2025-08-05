package aP;

import java.util.ArrayList;
import java.util.Iterator;
import s.C1818g;

/* loaded from: TunerStudioMS.jar:aP/hH.class */
class hH implements u.g {

    /* renamed from: a, reason: collision with root package name */
    G.R f3525a;

    /* renamed from: b, reason: collision with root package name */
    ArrayList f3526b;

    /* renamed from: c, reason: collision with root package name */
    final /* synthetic */ hC f3527c;

    hH(hC hCVar, G.R r2, ArrayList arrayList) {
        this.f3527c = hCVar;
        this.f3525a = r2;
        this.f3526b = arrayList;
    }

    @Override // u.g
    public String a() {
        return C1818g.b("Save Delta Changes");
    }

    @Override // u.g
    public String b() {
        return "<html>" + C1818g.b("Saves a partial tune containing only the parameters that have changed.") + "<br>" + C1818g.b("The values on the left will be saved.");
    }

    @Override // u.g
    public boolean c() {
        return false;
    }

    @Override // u.g
    public boolean d() {
        ArrayList arrayList = new ArrayList();
        Iterator it = this.f3526b.iterator();
        while (it.hasNext()) {
            arrayList.add(((G.aM) it.next()).aJ());
        }
        C0338f.a().a(this.f3525a, "diff", arrayList);
        return false;
    }
}
