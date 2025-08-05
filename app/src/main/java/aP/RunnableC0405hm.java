package aP;

/* renamed from: aP.hm, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:aP/hm.class */
class RunnableC0405hm implements Runnable {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ String f3588a;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ C0404hl f3589b;

    RunnableC0405hm(C0404hl c0404hl, String str) {
        this.f3589b = c0404hl;
        this.f3588a = str;
    }

    @Override // java.lang.Runnable
    public void run() throws IllegalArgumentException {
        this.f3589b.e(this.f3588a);
    }
}
