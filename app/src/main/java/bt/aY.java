package bt;

import java.awt.Color;

/* loaded from: TunerStudioMS.jar:bt/aY.class */
class aY extends Thread {

    /* renamed from: a, reason: collision with root package name */
    Color f8801a;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ aT f8802b;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public aY(aT aTVar) {
        super("WarningBlink");
        this.f8802b = aTVar;
        this.f8801a = null;
        setDaemon(true);
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        this.f8801a = this.f8802b.f8779c.getBackground();
        Color foreground = this.f8802b.f8779c.getForeground();
        boolean z2 = (foreground.getRed() + foreground.getGreen()) + foreground.getBlue() > 400;
        if (this.f8802b.f8779c instanceof C1273A) {
            ((C1273A) this.f8802b.f8779c).setOpaque(true);
        }
        while (this.f8802b.f8786j) {
            a(Color.yellow);
            if (z2) {
                this.f8802b.f8779c.setForeground(Color.black);
            }
            try {
                Thread.sleep(500L);
                a(this.f8801a);
                if (z2) {
                    this.f8802b.f8779c.setForeground(foreground);
                }
            } catch (Exception e2) {
            }
            try {
                Thread.sleep(500L);
            } catch (Exception e3) {
            }
        }
        try {
            a(this.f8801a);
            this.f8802b.f8779c.setForeground(foreground);
            if (this.f8802b.f8779c instanceof C1273A) {
                ((C1273A) this.f8802b.f8779c).setOpaque(true);
            }
        } catch (NullPointerException e4) {
        }
    }

    private void a(Color color) {
        if (!(this.f8802b.f8779c instanceof C1273A)) {
            this.f8802b.setBackground(color);
        } else {
            this.f8802b.f8779c.setBackground(color);
            ((C1273A) this.f8802b.f8779c).setOpaque(false);
        }
    }

    protected void a() {
        a(this.f8801a);
    }

    @Override // java.lang.Thread
    public void start() {
        super.start();
    }
}
