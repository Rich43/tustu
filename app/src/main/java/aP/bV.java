package aP;

import com.efiAnalytics.apps.ts.dashboard.C1425x;

/* loaded from: TunerStudioMS.jar:aP/bV.class */
class bV implements Runnable {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1425x f3015a;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ C1425x f3016b;

    /* renamed from: c, reason: collision with root package name */
    final /* synthetic */ bT f3017c;

    bV(bT bTVar, C1425x c1425x, C1425x c1425x2) {
        this.f3017c = bTVar;
        this.f3015a = c1425x;
        this.f3016b = c1425x2;
    }

    @Override // java.lang.Runnable
    public void run() {
        this.f3016b.a(this.f3015a.v());
    }
}
