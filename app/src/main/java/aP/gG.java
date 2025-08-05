package aP;

/* loaded from: TunerStudioMS.jar:aP/gG.class */
class gG implements Runnable {

    /* renamed from: b, reason: collision with root package name */
    private bA.f f3411b;

    /* renamed from: c, reason: collision with root package name */
    private boolean f3412c;

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C0308dx f3413a;

    private gG(C0308dx c0308dx) {
        this.f3413a = c0308dx;
        this.f3411b = null;
        this.f3412c = true;
    }

    @Override // java.lang.Runnable
    public void run() {
        this.f3411b.setVisible(this.f3412c);
        this.f3413a.f3274l.add(this);
    }

    public void a(bA.f fVar) {
        this.f3411b = fVar;
    }

    public void a(boolean z2) {
        this.f3412c = z2;
    }

    /* synthetic */ gG(C0308dx c0308dx, C0309dy c0309dy) {
        this(c0308dx);
    }
}
