package ao;

import W.C0188n;

/* loaded from: TunerStudioMS.jar:ao/aY.class */
class aY implements Runnable {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ long f5165a;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ C0188n f5166b;

    /* renamed from: c, reason: collision with root package name */
    final /* synthetic */ aQ f5167c;

    aY(aQ aQVar, long j2, C0188n c0188n) {
        this.f5167c = aQVar;
        this.f5165a = j2;
        this.f5166b = c0188n;
    }

    @Override // java.lang.Runnable
    public void run() {
        bH.C.d("$$$$$$$$$$$$$$$$$$   Time to call: " + (System.currentTimeMillis() - this.f5165a));
        this.f5167c.c(this.f5166b);
        bH.C.d("$$$$$$$$$$$$$$$$$$   Time finished call: " + (System.currentTimeMillis() - this.f5165a));
    }
}
