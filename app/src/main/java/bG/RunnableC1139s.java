package bg;

/* renamed from: bg.s, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bg/s.class */
class RunnableC1139s implements Runnable {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1137q f8097a;

    RunnableC1139s(C1137q c1137q) {
        this.f8097a = c1137q;
    }

    @Override // java.lang.Runnable
    public void run() {
        this.f8097a.fireTableDataChanged();
    }
}
