package aM;

/* loaded from: TunerStudioMS.jar:aM/c.class */
class c implements Runnable {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ a f2622a;

    c(a aVar) {
        this.f2622a = aVar;
    }

    @Override // java.lang.Runnable
    public void run() {
        if (this.f2622a.f2616c != null) {
            this.f2622a.f2616c.dispose();
        }
    }
}
