package ao;

import W.C0184j;
import i.InterfaceC1741a;
import java.awt.Dimension;
import java.awt.GridLayout;
import javax.swing.JPanel;
import org.apache.commons.net.nntp.NNTPReply;

/* renamed from: ao.K, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:ao/K.class */
public class C0593K extends JPanel implements InterfaceC1741a {

    /* renamed from: a, reason: collision with root package name */
    C0620ak f5088a;

    /* renamed from: b, reason: collision with root package name */
    C0620ak f5089b;

    /* renamed from: c, reason: collision with root package name */
    C0620ak f5090c;

    /* renamed from: d, reason: collision with root package name */
    C0620ak f5091d;

    /* renamed from: e, reason: collision with root package name */
    C0620ak f5092e;

    /* renamed from: f, reason: collision with root package name */
    C0620ak f5093f;

    /* renamed from: g, reason: collision with root package name */
    C0620ak f5094g;

    /* renamed from: h, reason: collision with root package name */
    C0620ak f5095h;

    /* renamed from: i, reason: collision with root package name */
    C0184j f5096i = null;

    public C0593K() {
        this.f5088a = null;
        this.f5089b = null;
        this.f5090c = null;
        this.f5091d = null;
        this.f5092e = null;
        this.f5093f = null;
        this.f5094g = null;
        this.f5095h = null;
        setLayout(new GridLayout(1, 0, 2, 2));
        this.f5089b = new C0620ak();
        this.f5089b.c(false);
        this.f5089b.a(2);
        this.f5089b.b(true);
        add(this.f5089b);
        this.f5090c = new C0620ak();
        this.f5090c.c(false);
        this.f5090c.a(4);
        this.f5090c.b(true);
        add(this.f5090c);
        this.f5091d = new C0620ak();
        this.f5091d.c(false);
        this.f5091d.a(8);
        this.f5091d.b(true);
        add(this.f5091d);
        this.f5088a = new C0620ak();
        this.f5088a.c(false);
        this.f5088a.a(1);
        this.f5088a.b(true);
        add(this.f5088a);
        this.f5092e = new C0620ak();
        this.f5092e.c(false);
        this.f5092e.a(16);
        this.f5092e.b(true);
        add(this.f5092e);
        this.f5093f = new C0620ak();
        this.f5093f.c(false);
        this.f5093f.a(32);
        this.f5093f.b(true);
        add(this.f5093f);
        this.f5094g = new C0620ak();
        this.f5094g.c(false);
        this.f5094g.a(64);
        this.f5094g.b(true);
        add(this.f5094g);
        this.f5095h = new C0620ak();
        this.f5095h.c(false);
        this.f5095h.a(128);
        this.f5095h.b(true);
        add(this.f5095h);
    }

    public void a(C0184j c0184j) {
        this.f5089b.a(c0184j);
        this.f5089b.a("Crank");
        this.f5090c.a(c0184j);
        this.f5090c.a("ASE");
        this.f5091d.a(c0184j);
        this.f5091d.a("Warm");
        this.f5088a.a(c0184j);
        this.f5088a.a("Run");
        this.f5092e.a(c0184j);
        this.f5092e.a("TP AE");
        this.f5093f.a(c0184j);
        this.f5093f.a("TP DE");
        this.f5094g.a(c0184j);
        this.f5094g.a("MAP AE");
        this.f5095h.a(c0184j);
        this.f5095h.a("MAP DE");
    }

    @Override // i.InterfaceC1741a
    public void a(int i2) {
        if (this.f5089b.b() != null) {
            this.f5089b.b(i2);
            this.f5090c.b(i2);
            this.f5091d.b(i2);
            this.f5088a.b(i2);
            this.f5092e.b(i2);
            this.f5093f.b(i2);
            this.f5094g.b(i2);
            this.f5095h.b(i2);
        }
    }

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    public Dimension getPreferredSize() {
        return new Dimension(com.efiAnalytics.ui.eJ.a(NNTPReply.AUTHENTICATION_REQUIRED), (int) (this.f5089b.getPreferredSize().getHeight() + 2.0d));
    }

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    public Dimension getMinimumSize() {
        return getPreferredSize();
    }
}
