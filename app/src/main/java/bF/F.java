package bF;

/* loaded from: TunerStudioMS.jar:bF/F.class */
class F implements Runnable {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ int f6812a;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ int f6813b;

    /* renamed from: c, reason: collision with root package name */
    final /* synthetic */ D f6814c;

    F(D d2, int i2, int i3) {
        this.f6814c = d2;
        this.f6812a = i2;
        this.f6813b = i3;
    }

    @Override // java.lang.Runnable
    public void run() {
        this.f6814c.changeSelection(this.f6812a, this.f6813b, false, false);
    }
}
