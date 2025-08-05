package bt;

import java.awt.Color;

/* renamed from: bt.br, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bt/br.class */
class C1336br extends Thread {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1333bo f9055a;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    C1336br(C1333bo c1333bo) {
        super("SliderWarnBlink");
        this.f9055a = c1333bo;
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        Color background = this.f9055a.f9042c.getBackground();
        while (this.f9055a.f9046f && this.f9055a.isVisible()) {
            this.f9055a.f9042c.setBackground(Color.yellow);
            try {
                Thread.sleep(500L);
            } catch (Exception e2) {
            }
            this.f9055a.f9042c.setBackground(background);
            try {
                Thread.sleep(500L);
            } catch (Exception e3) {
            }
        }
        this.f9055a.f9042c.setBackground(background);
    }

    @Override // java.lang.Thread
    public void start() {
        super.start();
    }
}
