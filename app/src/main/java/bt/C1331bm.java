package bt;

import java.awt.Color;
import java.awt.Component;
import javax.swing.UIManager;

/* renamed from: bt.bm, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bt/bm.class */
class C1331bm extends Thread {

    /* renamed from: a, reason: collision with root package name */
    Color f9031a;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ C1328bj f9032b;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public C1331bm(C1328bj c1328bj) {
        super("WarningBlink");
        this.f9032b = c1328bj;
        this.f9031a = null;
        setDaemon(true);
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        this.f9031a = UIManager.getColor("Label.background");
        Color color = UIManager.getColor("Label.foreground");
        boolean z2 = (color.getRed() + color.getGreen()) + color.getBlue() > 400;
        while (this.f9032b.f9028g) {
            a(Color.yellow);
            this.f9032b.f9023i.setBackground(Color.YELLOW);
            if (z2) {
                b(Color.BLACK);
            }
            try {
                Thread.sleep(500L);
                a(this.f9031a);
                if (z2) {
                    b(color);
                }
            } catch (Exception e2) {
            }
            try {
                Thread.sleep(500L);
            } catch (Exception e3) {
            }
        }
        try {
            a(this.f9031a);
            b(color);
        } catch (NullPointerException e4) {
        }
    }

    private void a(Color color) {
        this.f9032b.f9023i.setBackground(color);
    }

    protected void a() {
        a(this.f9031a);
    }

    private void b(Color color) {
        for (Component component : this.f9032b.f9023i.getComponents()) {
            component.setForeground(color);
        }
    }

    @Override // java.lang.Thread
    public void start() {
        super.start();
    }
}
