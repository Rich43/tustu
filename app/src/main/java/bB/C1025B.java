package bb;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/* renamed from: bb.B, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bb/B.class */
class C1025B extends Thread {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1024A f7713a;

    C1025B(C1024A c1024a) {
        this.f7713a = c1024a;
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() throws IOException {
        try {
            Thread.sleep(250L);
            boolean zH = this.f7713a.f7712a.f7840b.O().H();
            this.f7713a.f7712a.f7840b.O().e(false);
            this.f7713a.f7712a.j();
            if (this.f7713a.f7712a.f7840b != null) {
                this.f7713a.f7712a.f7840b.C().b(this.f7713a.f7712a.f7845g);
            }
            Thread.sleep(200L);
            this.f7713a.f7712a.f7840b.O().e(zH);
        } catch (InterruptedException e2) {
            Logger.getLogger(x.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
        }
    }
}
