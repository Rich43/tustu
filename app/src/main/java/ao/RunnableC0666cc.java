package ao;

import au.C0861c;
import h.C1737b;
import i.C1743c;

/* renamed from: ao.cc, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:ao/cc.class */
class RunnableC0666cc implements Runnable {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ bP f5485a;

    RunnableC0666cc(bP bPVar) {
        this.f5485a = bPVar;
    }

    @Override // java.lang.Runnable
    public void run() {
        if (C1737b.a().a("triggerLogViewer")) {
            this.f5485a.f5360n = new C0861c();
            this.f5485a.f5359m.add("Ignition Log Viewer", this.f5485a.f5360n);
        }
        this.f5485a.f5361o = new C0718eb();
        C1743c.a().a(this.f5485a.f5361o);
        this.f5485a.f5359m.add("Scatter Plots", this.f5485a.f5361o);
        this.f5485a.f5359m.a("Scatter Plots", C1737b.a().a("scatterPlots"));
        this.f5485a.f5362p = new C0729em();
        C1743c.a().a(this.f5485a.f5362p);
        boolean zA = C1737b.a().a("tableGenerator");
        this.f5485a.f5359m.addTab("Histogram / Table Generator", null, this.f5485a.f5362p, zA ? null : "MLV HD feature");
        this.f5485a.f5359m.a("Histogram / Table Generator", zA);
        if (C1737b.a().a("customTuneGenerator")) {
            this.f5485a.f5359m.addTab("Reference Values", null, new C0755fl(), zA ? null : "MLV HD feature");
            this.f5485a.f5359m.a("Reference Values", C1737b.a().a("customTuneGenerator"));
        }
    }
}
