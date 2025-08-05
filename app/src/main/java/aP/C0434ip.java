package aP;

import java.util.logging.Level;
import java.util.logging.Logger;

/* renamed from: aP.ip, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:aP/ip.class */
class C0434ip extends Thread {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C0431im f3742a;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    C0434ip(C0431im c0431im) {
        super("SalesLabelRotation");
        this.f3742a = c0431im;
        setDaemon(true);
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() throws IllegalArgumentException {
        while (true) {
            this.f3742a.f3738c = (int) Math.round(Math.random() * (this.f3742a.f3736a.length - 1));
            this.f3742a.f3737b.setText(this.f3742a.f3736a[this.f3742a.f3738c].f3743a);
            try {
                Thread.sleep(10000L);
            } catch (InterruptedException e2) {
                Logger.getLogger(C0431im.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
            }
        }
    }
}
