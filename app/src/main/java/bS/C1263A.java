package bs;

import java.util.logging.Level;
import java.util.logging.Logger;

/* renamed from: bs.A, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bs/A.class */
class C1263A extends bE.a implements bM.e {

    /* renamed from: a, reason: collision with root package name */
    int f8535a = 400;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ k f8536b;

    C1263A(k kVar) {
        this.f8536b = kVar;
    }

    @Override // bM.e
    public void a(bM.d dVar) {
        if (this.f8536b.f8605b.a(dVar.e()) < this.f8535a) {
            float fD = ((dVar.d() / dVar.c()) - 1.0f) * 100.0f;
            if (dVar.h() || this.f8536b.f8608e.e() >= 20000) {
                return;
            }
            float fB = 0.0f;
            try {
                fB = 100.0f - (((float) (this.f8536b.f8605b.b(dVar.e()) / this.f8536b.f8605b.a(dVar.e()))) * 100.0f);
            } catch (V.g e2) {
                Logger.getLogger(k.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
            }
            a(dVar.e(), fD, fB);
            this.f8536b.f8609f.a();
        }
    }
}
