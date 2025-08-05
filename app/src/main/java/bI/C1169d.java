package bi;

import G.de;

/* renamed from: bi.d, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bi/d.class */
class C1169d implements de {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1166a f8156a;

    C1169d(C1166a c1166a) {
        this.f8156a = c1166a;
    }

    @Override // G.de
    public void a(boolean z2) throws IllegalArgumentException {
        if (z2) {
            this.f8156a.f8148e.setText("High Speed");
        } else {
            this.f8156a.f8148e.setText("Standard Speed");
        }
    }
}
