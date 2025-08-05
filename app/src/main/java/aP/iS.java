package aP;

/* loaded from: TunerStudioMS.jar:aP/iS.class */
class iS implements Runnable {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ String f3683a;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ iR f3684b;

    iS(iR iRVar, String str) {
        this.f3684b = iRVar;
        this.f3683a = str;
    }

    @Override // java.lang.Runnable
    public void run() {
        this.f3684b.f3680c.setText(this.f3683a);
    }
}
