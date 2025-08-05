package aj;

import bH.C;

/* loaded from: TunerStudioMS.jar:aj/m.class */
class m extends Thread {

    /* renamed from: a, reason: collision with root package name */
    boolean f4578a;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ k f4579b;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    m(k kVar) {
        super("UDP_LoggerReceiveThread_" + Math.random());
        this.f4579b = kVar;
        this.f4578a = true;
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        while (this.f4578a) {
            try {
                this.f4579b.o();
            } catch (Exception e2) {
                C.c("UDP Read Thread Stopping: " + e2.getLocalizedMessage());
                this.f4578a = false;
            }
        }
        this.f4579b.f4574r = null;
    }

    public void a() {
        this.f4578a = false;
        this.f4579b.f4572y.clear();
    }
}
