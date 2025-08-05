package ao;

import sun.java2d.marlin.MarlinConst;

/* renamed from: ao.bb, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:ao/bb.class */
class C0638bb extends Thread {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ aQ f5393a;

    C0638bb(aQ aQVar) {
        this.f5393a = aQVar;
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        while (this.f5393a.f5141n.r() == null) {
            try {
                Thread.currentThread();
                Thread.sleep(MarlinConst.statDump);
            } catch (InterruptedException e2) {
            }
            this.f5393a.o();
        }
    }
}
