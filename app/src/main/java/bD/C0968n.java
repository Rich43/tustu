package bD;

/* renamed from: bD.n, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bD/n.class */
class C0968n extends Thread {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C0963i f6681a;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    C0968n(C0963i c0963i) {
        super("Delete");
        this.f6681a = c0963i;
        setDaemon(true);
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        try {
            this.f6681a.j();
        } finally {
            this.f6681a.setEnabled(true);
        }
    }
}
