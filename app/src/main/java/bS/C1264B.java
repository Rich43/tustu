package bs;

import java.util.logging.Level;
import java.util.logging.Logger;

/* renamed from: bs.B, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bs/B.class */
class C1264B extends bE.a implements bM.e {

    /* renamed from: a, reason: collision with root package name */
    int f8537a = 400;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ k f8538b;

    C1264B(k kVar) {
        this.f8538b = kVar;
    }

    @Override // bM.e
    public void a(bM.d dVar) {
        if (this.f8538b.f8605b.a(dVar.e()) < this.f8537a) {
            float fD = dVar.d() - dVar.c();
            if (dVar.h() || this.f8538b.f8608e.e() >= 20000) {
                return;
            }
            float fB = 0.0f;
            try {
                fB = (float) this.f8538b.f8605b.b(dVar.e());
            } catch (V.g e2) {
                Logger.getLogger(k.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
            }
            a(dVar.e(), fB, fD);
            this.f8538b.f8612i.a();
        }
    }
}
