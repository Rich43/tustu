package bF;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;

/* loaded from: TunerStudioMS.jar:bF/s.class */
class s extends Thread {

    /* renamed from: a, reason: collision with root package name */
    int f6880a = 200;

    /* renamed from: b, reason: collision with root package name */
    long f6881b = System.currentTimeMillis() + this.f6880a;

    /* renamed from: c, reason: collision with root package name */
    int f6882c = 50;

    /* renamed from: d, reason: collision with root package name */
    boolean f6883d = true;

    /* renamed from: e, reason: collision with root package name */
    final /* synthetic */ C0973d f6884e;

    s(C0973d c0973d) {
        this.f6884e = c0973d;
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        while (this.f6883d) {
            if (System.currentTimeMillis() > this.f6881b) {
                this.f6884e.f6861m = null;
                this.f6883d = false;
                SwingUtilities.invokeLater(new t(this));
            } else {
                try {
                    Thread.sleep(this.f6882c);
                } catch (InterruptedException e2) {
                    Logger.getLogger(C0973d.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void a() {
        this.f6881b = System.currentTimeMillis() + this.f6880a;
    }
}
