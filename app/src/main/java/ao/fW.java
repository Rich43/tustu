package ao;

/* loaded from: TunerStudioMS.jar:ao/fW.class */
class fW extends Thread {

    /* renamed from: a, reason: collision with root package name */
    int f5722a;

    /* renamed from: b, reason: collision with root package name */
    int f5723b;

    /* renamed from: c, reason: collision with root package name */
    final /* synthetic */ C0764fu f5724c;

    public fW(C0764fu c0764fu, int i2, int i3) {
        this.f5724c = c0764fu;
        this.f5722a = i2;
        this.f5723b = i3;
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        bE.q qVarA = this.f5724c.i().a((bE.p) this.f5724c.f5845f, this.f5722a, this.f5723b);
        if (qVarA == null || C0804hg.a().r() == null) {
            return;
        }
        double dB = (this.f5724c.f5845f.b() - this.f5724c.f5845f.a()) * 0.01d;
        double d2 = (this.f5724c.f5845f.d() - this.f5724c.f5845f.c()) * 0.01d;
        C0804hg.a().p();
        int iE = this.f5724c.f5855y == 0 ? this.f5724c.f5845f.e() : this.f5724c.f5855y;
        for (int i2 = this.f5724c.f5856z; i2 < iE; i2++) {
            int i3 = i2;
            bE.q qVarA2 = this.f5724c.f5845f.a(i3);
            if (Math.abs(qVarA2.getX() - qVarA.getX()) < dB && Math.abs(qVarA2.getY() - qVarA.getY()) < d2) {
                C0804hg.a().c(i3);
                return;
            }
        }
    }
}
