package aP;

import ao.C0804hg;

/* renamed from: aP.ce, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:aP/ce.class */
class RunnableC0263ce implements Runnable {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ bZ f3156a;

    RunnableC0263ce(bZ bZVar) {
        this.f3156a = bZVar;
    }

    @Override // java.lang.Runnable
    public void run() {
        this.f3156a.f3043r.setEnabled(!ac.r.a());
        this.f3156a.f3044s.setEnabled(ac.r.a());
        this.f3156a.f3046u.setEnabled((C0804hg.a().r() == null || C0804hg.a().r().isEmpty()) ? false : true);
    }
}
