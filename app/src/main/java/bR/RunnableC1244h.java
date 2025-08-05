package br;

import aP.cZ;

/* renamed from: br.h, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:br/h.class */
class RunnableC1244h implements Runnable {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ int f8453a;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ C1242f f8454b;

    RunnableC1244h(C1242f c1242f, int i2) {
        this.f8454b = c1242f;
        this.f8453a = i2;
    }

    @Override // java.lang.Runnable
    public void run() {
        cZ.a().o().a();
        try {
            ((al) this.f8454b.f8450a.get(this.f8453a)).a(false);
        } catch (Exception e2) {
            bH.C.a("Failed to stop AutoTune for table index: " + this.f8453a + "\nError: " + e2.getLocalizedMessage());
        }
    }
}
