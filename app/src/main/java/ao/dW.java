package ao;

/* loaded from: TunerStudioMS.jar:ao/dW.class */
class dW extends Thread {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ bP f5544a;

    dW(bP bPVar) {
        this.f5544a = bPVar;
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        int i2 = 0;
        while (true) {
            if (this.f5544a.f5347a.n().r() != null && this.f5544a.f5347a.n().r().d() >= 50 && this.f5544a.f5347a.f()) {
                break;
            }
            try {
                Thread.sleep(100L);
                if (i2 > 600) {
                    this.f5544a.y();
                    return;
                }
                i2++;
            } catch (Exception e2) {
                System.out.println("breaking weird Thread Exception");
                this.f5544a.f5347a.n().c(this.f5544a.f5347a.n().r().d() - 10);
                this.f5544a.f5347a.n().a(1.0d, false);
                this.f5544a.f5347a.n().e();
                return;
            }
        }
    }
}
