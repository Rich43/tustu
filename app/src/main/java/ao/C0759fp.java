package ao;

import com.sun.media.jfxmedia.MetadataParser;
import i.InterfaceC1741a;
import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Window;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.icepdf.core.util.PdfOps;
import sun.util.locale.LanguageTag;

/* renamed from: ao.fp, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:ao/fp.class */
public class C0759fp extends JDialog implements bE.k, bE.l, InterfaceC1741a {

    /* renamed from: k, reason: collision with root package name */
    private bE.m f5811k;

    /* renamed from: a, reason: collision with root package name */
    C0717ea f5812a;

    /* renamed from: b, reason: collision with root package name */
    JLabel f5813b;

    /* renamed from: c, reason: collision with root package name */
    JLabel f5814c;

    /* renamed from: d, reason: collision with root package name */
    com.efiAnalytics.ui.fE f5815d;

    /* renamed from: e, reason: collision with root package name */
    int f5816e;

    /* renamed from: f, reason: collision with root package name */
    int f5817f;

    /* renamed from: g, reason: collision with root package name */
    int f5818g;

    /* renamed from: h, reason: collision with root package name */
    int f5819h;

    /* renamed from: i, reason: collision with root package name */
    float f5820i;

    /* renamed from: j, reason: collision with root package name */
    String f5821j;

    /* renamed from: l, reason: collision with root package name */
    private C0764fu f5822l;

    public C0759fp(Window window, C0764fu c0764fu) {
        super(window, "Scatter Plot popout");
        this.f5811k = new bE.m();
        this.f5812a = null;
        this.f5813b = new JLabel(" ", 0);
        this.f5814c = new JLabel(" ", 0);
        this.f5815d = new com.efiAnalytics.ui.fE();
        this.f5816e = 1;
        this.f5817f = com.efiAnalytics.ui.eJ.a(10);
        this.f5818g = 100;
        this.f5819h = 100;
        this.f5820i = 1.0f;
        this.f5821j = "ScatterPlotPopout_";
        this.f5822l = c0764fu;
        setLayout(new BorderLayout());
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new BorderLayout());
        jPanel.add(BorderLayout.CENTER, this.f5811k);
        jPanel.add("North", this.f5813b);
        jPanel.add("West", this.f5815d);
        jPanel.add("South", this.f5814c);
        this.f5813b.setOpaque(true);
        this.f5815d.setOpaque(true);
        this.f5814c.setOpaque(true);
        this.f5813b.setBackground(Color.BLACK);
        this.f5815d.setBackground(Color.BLACK);
        this.f5814c.setBackground(Color.BLACK);
        this.f5813b.setForeground(Color.WHITE);
        this.f5815d.setForeground(Color.WHITE);
        this.f5814c.setForeground(Color.WHITE);
        this.f5813b.setFont(new Font("Times", 1, com.efiAnalytics.ui.eJ.a(18)));
        add(BorderLayout.CENTER, jPanel);
        C0804hg.a().a(this);
        f();
        addComponentListener(new C0760fq(this));
        this.f5811k.a(this);
    }

    public void a(C0717ea c0717ea) {
        if (this.f5811k.b(0) != null) {
            this.f5811k.b(0).b(this);
        }
        c0717ea.a(this);
        this.f5812a = c0717ea;
        this.f5811k.a(c0717ea, 0);
        a();
    }

    public void a(int i2, int i3) {
        this.f5816e = i2;
        this.f5817f = i3;
    }

    public void b(int i2, int i3) {
        this.f5818g = i2;
        this.f5819h = i3;
        e();
    }

    private void e() {
        this.f5820i = (float) Math.min(this.f5811k.getHeight() / this.f5819h, this.f5811k.getWidth() / this.f5818g);
        this.f5811k.e(Math.round(this.f5817f * this.f5820i));
        this.f5811k.d(Math.round(this.f5816e * this.f5820i));
    }

    @Override // bE.l
    public void a() throws IllegalArgumentException {
        b();
        this.f5811k.d();
        this.f5811k.repaint();
    }

    public void b(int i2) {
        if (this.f5812a == null || i2 <= this.f5812a.f() || this.f5812a.f5608a == null || i2 >= this.f5812a.e() || this.f5812a.f5609b == null || i2 >= this.f5812a.f5609b.i()) {
            this.f5811k.l();
            return;
        }
        bE.q qVarA = this.f5812a.a(i2);
        this.f5811k.a(qVarA.getX(), qVarA.getY());
        this.f5811k.repaint();
    }

    @Override // i.InterfaceC1741a
    public void a(int i2) {
        b(i2);
    }

    protected void b() throws IllegalArgumentException {
        if (this.f5812a.f5608a == null || this.f5812a.f5609b == null || this.f5812a.f5608a.a().length() <= 0 || this.f5812a.f5609b.a().length() <= 0) {
            return;
        }
        this.f5813b.setText((this.f5812a.f5610c == null || this.f5812a.f5610c.a().trim().length() <= 0) ? this.f5812a.f5609b.a() + " vs " + this.f5812a.f5608a.a() : this.f5812a.f5609b.a() + " vs " + this.f5812a.f5608a.a() + " vs " + this.f5812a.f5610c.a());
        this.f5814c.setText(this.f5812a.f5608a.a());
        this.f5815d.setText(this.f5812a.f5609b.a());
        this.f5811k.a(this.f5812a.f5608a.a());
        this.f5811k.b(this.f5812a.f5609b.a());
        if (this.f5812a.f5610c == null || this.f5812a.f5610c.a() == null) {
            this.f5811k.c("");
        } else {
            this.f5811k.c(this.f5812a.f5610c.a());
        }
    }

    private void f() {
        bP bPVarB = C0645bi.a().b();
        setBounds(h.i.b(this.f5821j + LanguageTag.PRIVATEUSE, bPVarB.getX() + 10), h.i.b(this.f5821j + PdfOps.y_TOKEN, bPVarB.getY() + com.efiAnalytics.ui.eJ.a(40)), h.i.b(this.f5821j + MetadataParser.WIDTH_TAG_NAME, com.efiAnalytics.ui.eJ.a(400)), h.i.b(this.f5821j + MetadataParser.HEIGHT_TAG_NAME, com.efiAnalytics.ui.eJ.a(400)));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void g() {
        h.i.c(this.f5821j + LanguageTag.PRIVATEUSE, "" + getX());
        h.i.c(this.f5821j + PdfOps.y_TOKEN, "" + getY());
        h.i.c(this.f5821j + MetadataParser.WIDTH_TAG_NAME, "" + getWidth());
        h.i.c(this.f5821j + MetadataParser.HEIGHT_TAG_NAME, "" + getHeight());
    }

    @Override // java.awt.Window, java.awt.Container, java.awt.Component
    public void processEvent(AWTEvent aWTEvent) {
        if ((aWTEvent.getID() == 101 || aWTEvent.getID() == 100) && !bH.I.b()) {
            g();
            e();
        }
        if (aWTEvent.getID() == 201 && bH.I.b()) {
            g();
        }
        super.processEvent(aWTEvent);
    }

    public bE.m c() {
        return this.f5811k;
    }

    @Override // bE.k
    public void a(double d2, double d3, double d4, double d5) {
        this.f5822l.a(d2, d3, d4, d5);
    }

    @Override // java.awt.Component
    /* renamed from: d, reason: merged with bridge method [inline-methods] */
    public C0764fu getParent() {
        return this.f5822l;
    }

    public void a(C0764fu c0764fu) {
        this.f5822l = c0764fu;
    }
}
