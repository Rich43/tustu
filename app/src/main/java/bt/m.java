package bT;

import java.util.Iterator;

/* loaded from: TunerStudioMS.jar:bT/m.class */
class m implements A.e {

    /* renamed from: a, reason: collision with root package name */
    r f7599a;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ l f7600b;

    m(l lVar, r rVar) {
        this.f7600b = lVar;
        this.f7599a = rVar;
        l.a(lVar);
    }

    @Override // A.e
    public void a() {
        n nVar = null;
        l.b(this.f7600b);
        Iterator it = this.f7600b.f7596c.iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            n nVar2 = (n) it.next();
            if (nVar2.b().equals(this.f7599a)) {
                nVar = nVar2;
                break;
            }
        }
        if (nVar != null) {
            nVar.a();
            this.f7600b.f7596c.remove(nVar);
        }
    }

    @Override // A.e
    public void b() {
    }

    @Override // A.e
    public void c() {
    }

    @Override // A.e
    public void d() {
    }

    @Override // A.e
    public void e() {
    }
}
