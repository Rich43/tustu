package bh;

/* renamed from: bh.j, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bh/j.class */
class RunnableC1150j implements Runnable {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1142b f8113a;

    RunnableC1150j(C1142b c1142b) {
        this.f8113a = c1142b;
    }

    @Override // java.lang.Runnable
    public void run() {
        this.f8113a.r().validate();
        this.f8113a.f8098n.doLayout();
        this.f8113a.f5143b.doLayout();
    }
}
