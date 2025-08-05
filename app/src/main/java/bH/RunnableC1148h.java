package bh;

/* renamed from: bh.h, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bh/h.class */
class RunnableC1148h implements Runnable {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1142b f8111a;

    RunnableC1148h(C1142b c1142b) {
        this.f8111a = c1142b;
    }

    @Override // java.lang.Runnable
    public void run() {
        this.f8111a.f8098n.a().doLayout();
        this.f8111a.f8098n.a().validate();
    }
}
