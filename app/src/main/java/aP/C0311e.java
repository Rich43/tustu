package aP;

import aR.C0471a;
import aR.C0472b;
import bv.C1369a;
import d.InterfaceC1709a;
import d.InterfaceC1711c;
import e.C1713a;
import e.C1714b;
import e.C1715c;
import e.C1716d;
import e.C1718f;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import r.C1806i;

/* renamed from: aP.e, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:aP/e.class */
public class C0311e implements d.f {

    /* renamed from: a, reason: collision with root package name */
    Map f3280a = new HashMap();

    public C0311e() {
        a();
    }

    private void a() {
        C1369a c1369a = new C1369a();
        this.f3280a.put(c1369a.a(), c1369a);
        aR.j jVar = new aR.j();
        this.f3280a.put(jVar.a(), jVar);
        aR.k kVar = new aR.k();
        this.f3280a.put(kVar.a(), kVar);
        aR.e eVar = new aR.e();
        this.f3280a.put(eVar.a(), eVar);
        aR.r rVar = new aR.r();
        this.f3280a.put(rVar.a(), rVar);
        C0471a c0471a = new C0471a();
        this.f3280a.put(c0471a.a(), c0471a);
        aR.q qVar = new aR.q();
        this.f3280a.put(qVar.a(), qVar);
        aR.p pVar = new aR.p();
        this.f3280a.put(pVar.a(), pVar);
        aR.o oVar = new aR.o();
        this.f3280a.put(oVar.a(), oVar);
        if (C1806i.a().a("09BDPO;L,;l;ldpo;l5 ")) {
            aR.s sVar = new aR.s();
            this.f3280a.put(sVar.a(), sVar);
            C1713a c1713a = new C1713a();
            this.f3280a.put(c1713a.a(), c1713a);
            C0472b c0472b = new C0472b();
            this.f3280a.put(c0472b.a(), c0472b);
            aR.x xVar = new aR.x();
            this.f3280a.put(xVar.a(), xVar);
            aR.z zVar = new aR.z();
            this.f3280a.put(zVar.a(), zVar);
            aR.C c2 = new aR.C();
            this.f3280a.put(c2.a(), c2);
            aR.i iVar = new aR.i();
            this.f3280a.put(iVar.a(), iVar);
            aR.h hVar = new aR.h();
            this.f3280a.put(hVar.a(), hVar);
        }
        if (C1806i.a().a("poij  fdsz poi9ure895 ms7(")) {
            C1714b c1714b = new C1714b();
            this.f3280a.put(c1714b.a(), c1714b);
            aR.w wVar = new aR.w();
            this.f3280a.put(wVar.a(), wVar);
            aR.y yVar = new aR.y();
            this.f3280a.put(yVar.a(), yVar);
            aR.A a2 = new aR.A();
            this.f3280a.put(a2.a(), a2);
            aR.g gVar = new aR.g();
            this.f3280a.put(gVar.a(), gVar);
            aR.l lVar = new aR.l();
            this.f3280a.put(lVar.a(), lVar);
            aR.f fVar = new aR.f();
            this.f3280a.put(fVar.a(), fVar);
            aR.v vVar = new aR.v();
            this.f3280a.put(vVar.a(), vVar);
            C1715c c1715c = new C1715c();
            this.f3280a.put(c1715c.a(), c1715c);
            C1716d c1716d = new C1716d();
            this.f3280a.put(c1716d.a(), c1716d);
            aR.u uVar = new aR.u();
            this.f3280a.put(uVar.a(), uVar);
            aR.m mVar = new aR.m();
            this.f3280a.put(mVar.a(), mVar);
            aR.n nVar = new aR.n();
            this.f3280a.put(nVar.a(), nVar);
        }
        if (C1806i.a().a("98ua7h9uh432987 432")) {
            C1718f c1718f = new C1718f();
            this.f3280a.put(c1718f.a(), c1718f);
        }
    }

    @Override // d.f
    public InterfaceC1711c a(String str) {
        InterfaceC1711c interfaceC1711c = (InterfaceC1711c) this.f3280a.get(str);
        if (interfaceC1711c == null) {
        }
        return interfaceC1711c;
    }

    @Override // d.f
    public Collection a(InterfaceC1709a interfaceC1709a) {
        ArrayList arrayList = new ArrayList();
        for (InterfaceC1711c interfaceC1711c : this.f3280a.values()) {
            if (interfaceC1709a == null || interfaceC1709a.a(interfaceC1711c)) {
                arrayList.add(interfaceC1711c);
            }
        }
        return arrayList;
    }
}
