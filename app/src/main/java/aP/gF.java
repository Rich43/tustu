package aP;

/* loaded from: TunerStudioMS.jar:aP/gF.class */
class gF implements Runnable {

    /* renamed from: b, reason: collision with root package name */
    private bA.f f3408b;

    /* renamed from: c, reason: collision with root package name */
    private boolean f3409c;

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C0308dx f3410a;

    private gF(C0308dx c0308dx) {
        this.f3410a = c0308dx;
        this.f3408b = null;
        this.f3409c = true;
    }

    @Override // java.lang.Runnable
    public void run() {
        this.f3408b.setEnabled(this.f3409c);
        this.f3410a.f3273k.add(this);
    }

    public void a(bA.f fVar) {
        this.f3408b = fVar;
    }

    public void a(boolean z2) {
        this.f3409c = z2;
    }

    /* synthetic */ gF(C0308dx c0308dx, C0309dy c0309dy) {
        this(c0308dx);
    }
}
