package bU;

import bH.C0995c;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/* loaded from: TunerStudioMS.jar:bU/C.class */
public class C implements bT.a {

    /* renamed from: a, reason: collision with root package name */
    Map f7626a = new HashMap();

    public C() {
        bV.g gVar = new bV.g();
        this.f7626a.put(Integer.valueOf(gVar.a()), gVar);
        bV.a aVar = new bV.a();
        this.f7626a.put(Integer.valueOf(aVar.a()), aVar);
        bV.c cVar = new bV.c();
        this.f7626a.put(Integer.valueOf(cVar.a()), cVar);
        bV.d dVar = new bV.d();
        this.f7626a.put(Integer.valueOf(dVar.a()), dVar);
        bV.e eVar = new bV.e();
        this.f7626a.put(Integer.valueOf(eVar.a()), eVar);
        bV.b bVar = new bV.b();
        this.f7626a.put(Integer.valueOf(bVar.a()), bVar);
        bV.f fVar = new bV.f();
        this.f7626a.put(Integer.valueOf(fVar.a()), fVar);
    }

    @Override // bT.a
    public void a(bT.o oVar, bN.t tVar) throws bT.h {
        if (tVar.a() != a()) {
            throw new bT.h("PID:USER_CMD Wrong handler! this handler is for 0x" + Integer.toHexString(a()).toUpperCase());
        }
        byte[] bArrC = tVar.c();
        try {
            int iA = C0995c.a(bArrC[0]);
            bV.h hVar = (bV.h) this.f7626a.get(Integer.valueOf(iA));
            if (hVar == null) {
                bN.l lVarB = bN.u.a().b();
                lVarB.a(254);
                bH.C.a("USER_COMMAND: No Sub handler for sub command code 0x" + Integer.toHexString(iA).toUpperCase());
                oVar.a(lVarB);
            } else {
                byte[] bArr = new byte[bArrC.length - 1];
                System.arraycopy(bArrC, 1, bArr, 0, bArr.length);
                ((bN.l) tVar).b(bArr);
                ((bN.l) tVar).a(iA);
                hVar.a(oVar, tVar);
            }
        } catch (bN.o e2) {
            Logger.getLogger(z.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
            throw new bT.h(e2.getLocalizedMessage());
        }
    }

    @Override // bT.a
    public int a() {
        return 241;
    }

    public bV.h a(int i2) {
        return (bV.h) this.f7626a.get(Integer.valueOf(i2));
    }
}
