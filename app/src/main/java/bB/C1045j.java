package bb;

import java.awt.Color;
import javax.swing.UIManager;

/* renamed from: bb.j, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bb/j.class */
class C1045j extends Thread {

    /* renamed from: a, reason: collision with root package name */
    boolean f7772a = true;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ C1039d f7773b;

    C1045j(C1039d c1039d) {
        this.f7773b = c1039d;
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public synchronized void run() {
        while (this.f7772a) {
            try {
                wait(500L);
                this.f7773b.f7758d.setOpaque(true);
                this.f7773b.f7758d.setBackground(Color.yellow);
                this.f7773b.f7758d.setForeground(Color.black);
                this.f7773b.f7758d.repaint();
                if (this.f7772a) {
                    wait(1000L);
                }
                this.f7773b.f7758d.setOpaque(false);
                this.f7773b.f7758d.setBackground(UIManager.getColor("Label.background"));
                this.f7773b.f7758d.setForeground(UIManager.getColor("Label.foreground"));
                this.f7773b.f7758d.repaint();
            } catch (InterruptedException e2) {
                this.f7772a = false;
            }
        }
    }

    public synchronized void a() {
        this.f7772a = false;
        notify();
    }
}
