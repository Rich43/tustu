package R;

import bH.C;

/* loaded from: TunerStudioMS.jar:R/f.class */
class f implements c {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ d f1795a;

    f(d dVar) {
        this.f1795a = dVar;
    }

    @Override // R.c
    public boolean a(l lVar) {
        try {
            j jVar = (j) lVar;
            new m().b(jVar.b(), jVar.c(), jVar.d(), jVar.e(), jVar.f(), jVar.g(), jVar.h(), jVar.i(), jVar.j());
            return true;
        } catch (Exception e2) {
            C.d("unable to process ProposedTranslation Message. Exception:" + e2.getMessage());
            return false;
        }
    }
}
