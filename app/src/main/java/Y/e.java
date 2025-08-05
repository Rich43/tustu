package y;

/* loaded from: TunerStudioMS.jar:y/e.class */
class e extends Thread {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1895c f14050a;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    e(C1895c c1895c) {
        super("SenderThread");
        this.f14050a = c1895c;
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        C1895c.b();
    }
}
