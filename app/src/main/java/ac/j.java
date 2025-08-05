package ac;

/* loaded from: TunerStudioMS.jar:ac/j.class */
class j extends Thread {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ h f4221a;

    j(h hVar) {
        this.f4221a = hVar;
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        try {
            sleep(1000L);
            if (this.f4221a.f4203h && this.f4221a.f4213n) {
                this.f4221a.m();
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }
}
