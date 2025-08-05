package aP;

import ao.C0804hg;
import ap.C0830b;
import ap.C0831c;
import i.C1746f;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JPanel;
import n.InterfaceC1761a;

/* renamed from: aP.dd, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:aP/dd.class */
public class C0289dd extends JPanel implements aE.e, InterfaceC1761a {

    /* renamed from: a, reason: collision with root package name */
    G.R f3214a = null;

    /* renamed from: b, reason: collision with root package name */
    bZ f3215b = null;

    /* renamed from: c, reason: collision with root package name */
    aE.a f3216c = null;

    /* renamed from: f, reason: collision with root package name */
    private Image f3217f = null;

    /* renamed from: d, reason: collision with root package name */
    C0292dg f3218d = null;

    /* renamed from: e, reason: collision with root package name */
    public static String f3219e = "Log File Review";

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    public void paint(Graphics graphics) {
        super.paint(graphics);
        if (this.f3218d == null || !this.f3218d.f3223a) {
            return;
        }
        Graphics2D graphics2D = (Graphics2D) graphics;
        graphics2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        Image imageC = c();
        if (imageC != null) {
            int width = imageC.getWidth(null) / 2;
            int height = imageC.getHeight(null) / 2;
            graphics.drawImage(imageC, (getWidth() - width) / 2, (getHeight() - height) / 2, width, height, null);
        }
    }

    protected Image c() {
        if (this.f3217f == null) {
            try {
                this.f3217f = com.efiAnalytics.ui.cO.a().a(com.efiAnalytics.ui.cO.f11107w);
            } catch (V.a e2) {
                Logger.getLogger(com.efiAnalytics.apps.ts.tuningViews.J.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
            }
        }
        return this.f3217f;
    }

    public void d() {
        e();
        this.f3218d = new C0292dg(this);
        this.f3218d.start();
    }

    public void e() {
        if (this.f3218d != null) {
            this.f3218d.a();
            this.f3218d = null;
        }
    }

    @Override // aE.e
    public void a(aE.a aVar, G.R r2) {
        this.f3214a = r2;
        this.f3216c = aVar;
        if (this.f3215b != null) {
            this.f3215b.a(aVar, r2);
        }
    }

    @Override // aE.e
    public void e_() {
        this.f3214a = null;
        this.f3216c = null;
        if (this.f3215b != null) {
            this.f3215b.e_();
        }
    }

    @Override // n.InterfaceC1761a
    public boolean a() {
        boolean z2 = this.f3215b == null;
        if (z2) {
            try {
                d();
                C1746f.a().a(new C0831c());
                C1746f.a().a(new C0830b());
                C1746f.a().b();
                new C0291df(this, new RunnableC0290de(this)).start();
            } catch (Exception e2) {
                e2.printStackTrace();
                return false;
            }
        }
        bH.C.c("Activate Logging Tabs");
        if (!z2 || !ac.r.a() || C0804hg.a().r() == null) {
            return true;
        }
        C0804hg.a().e();
        C0804hg.a().c(C0804hg.a().r().d() - 1);
        return true;
    }

    @Override // aE.e
    public void a(aE.a aVar) {
    }
}
