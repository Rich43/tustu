package br;

import aP.cZ;

/* renamed from: br.g, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:br/g.class */
class RunnableC1243g implements Runnable {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ int f8451a;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ C1242f f8452b;

    RunnableC1243g(C1242f c1242f, int i2) {
        this.f8452b = c1242f;
        this.f8451a = i2;
    }

    @Override // java.lang.Runnable
    public void run() {
        cZ.a().o().a();
        try {
            ((al) this.f8452b.f8450a.get(this.f8451a)).a(true);
        } catch (Exception e2) {
            bH.C.a("Failed to start AutoTune for table index: " + this.f8451a + "\nError: " + e2.getLocalizedMessage());
        }
    }
}
