package ao;

import W.C0188n;
import i.C1743c;
import i.InterfaceC1741a;
import i.InterfaceC1742b;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

/* renamed from: ao.bw, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:ao/bw.class */
public class C0659bw extends JPanel implements InterfaceC1741a, InterfaceC1742b {

    /* renamed from: b, reason: collision with root package name */
    JPanel f5446b;

    /* renamed from: d, reason: collision with root package name */
    C0804hg f5448d;

    /* renamed from: e, reason: collision with root package name */
    JLabel f5449e;

    /* renamed from: a, reason: collision with root package name */
    C0806hi f5445a = new C0806hi();

    /* renamed from: c, reason: collision with root package name */
    JPanel f5447c = new JPanel();

    /* renamed from: f, reason: collision with root package name */
    int f5450f = -1;

    public C0659bw(C0804hg c0804hg) {
        this.f5448d = null;
        this.f5448d = c0804hg;
        setLayout(new BorderLayout());
        this.f5446b = a(c0804hg);
        add(this.f5446b, "South");
        c0804hg.a(this.f5445a);
        C1743c.a().a(this.f5445a);
        Color background = getBackground();
        if (background.getBlue() < 50 && background.getGreen() < 50 && background.getRed() < 50) {
            this.f5445a.setBackground(Color.GRAY);
        }
        this.f5449e = new JLabel();
        this.f5449e.setHorizontalAlignment(0);
        this.f5449e.setBorder(BorderFactory.createLoweredBevelBorder());
        Dimension dimensionA = com.efiAnalytics.ui.eJ.a(110, 16);
        this.f5449e.setMinimumSize(dimensionA);
        this.f5449e.setPreferredSize(dimensionA);
    }

    private JPanel a(C0804hg c0804hg) {
        this.f5446b = new JPanel();
        this.f5446b.setLayout(new BorderLayout());
        this.f5446b.add(this.f5445a, BorderLayout.CENTER);
        this.f5447c.setLayout(new GridLayout(1, 0, 1, 1));
        this.f5446b.add(this.f5447c, "East");
        Color color = new Color(220, 220, 220);
        Color background = getBackground();
        if (background.getBlue() < 50 && background.getGreen() < 50 && background.getRed() < 50) {
            color = Color.GRAY;
        }
        Dimension dimension = new Dimension(com.efiAnalytics.ui.eJ.a(16), com.efiAnalytics.ui.eJ.a(16));
        C0618ai c0618ai = new C0618ai(null, com.efiAnalytics.ui.eJ.a(Toolkit.getDefaultToolkit().getImage(getClass().getResource("resources/stop.gif"))), null, dimension);
        c0618ai.setBackground(color);
        c0618ai.a(new C0660bx(this));
        c0618ai.setToolTipText("Stop Playback - CTRL+S");
        this.f5447c.add(c0618ai);
        C0618ai c0618ai2 = new C0618ai(null, com.efiAnalytics.ui.eJ.a(Toolkit.getDefaultToolkit().getImage(getClass().getResource("resources/pause.gif"))), null, dimension);
        c0618ai2.setBackground(color);
        c0618ai2.a(new bA(this));
        c0618ai2.setToolTipText("Pause Playback - CTRL+S");
        this.f5447c.add(c0618ai2);
        C0618ai c0618ai3 = new C0618ai(null, com.efiAnalytics.ui.eJ.a(Toolkit.getDefaultToolkit().getImage(getClass().getResource("resources/forward.gif"))), null, dimension);
        c0618ai3.setBackground(color);
        c0618ai3.a(new bB(this));
        c0618ai3.setToolTipText("Start Play back - CTRL+P");
        this.f5447c.add(c0618ai3);
        this.f5447c.add(new JPanel());
        C0618ai c0618ai4 = new C0618ai(null, com.efiAnalytics.ui.eJ.a(Toolkit.getDefaultToolkit().getImage(getClass().getResource("resources/zoomout.gif"))), null, dimension);
        c0618ai4.setBackground(color);
        c0618ai4.a(new bC(this));
        c0618ai4.setToolTipText("Zoom Out - Down Key");
        this.f5447c.add(c0618ai4);
        C0618ai c0618ai5 = new C0618ai(null, com.efiAnalytics.ui.eJ.a(Toolkit.getDefaultToolkit().getImage(getClass().getResource("resources/zoomin.gif"))), null, dimension);
        c0618ai5.setBackground(color);
        c0618ai5.a(new bD(this));
        c0618ai5.setToolTipText("Zoom In - Up key");
        this.f5447c.add(c0618ai5);
        C0618ai c0618ai6 = new C0618ai(null, com.efiAnalytics.ui.eJ.a(Toolkit.getDefaultToolkit().getImage(getClass().getResource("resources/home.gif"))), null, dimension);
        c0618ai6.setBackground(color);
        c0618ai6.a(new bE(this));
        c0618ai6.setToolTipText("Go to 1st record - Home Key");
        this.f5447c.add(c0618ai6);
        C0618ai c0618ai7 = new C0618ai(null, com.efiAnalytics.ui.eJ.a(Toolkit.getDefaultToolkit().getImage(getClass().getResource("resources/rewind.gif"))), null, dimension);
        c0618ai7.setBackground(color);
        c0618ai7.a(new bF(this));
        c0618ai7.setToolTipText("Page Back - Page Down");
        this.f5447c.add(c0618ai7);
        C0618ai c0618ai8 = new C0618ai(null, com.efiAnalytics.ui.eJ.a(Toolkit.getDefaultToolkit().getImage(getClass().getResource("resources/minus.gif"))), null, dimension);
        c0618ai8.setBackground(color);
        c0618ai8.a(new bG(this));
        c0618ai8.setToolTipText("1 record Back - CTRL+B or Left");
        this.f5447c.add(c0618ai8);
        C0618ai c0618ai9 = new C0618ai(null, com.efiAnalytics.ui.eJ.a(Toolkit.getDefaultToolkit().getImage(getClass().getResource("resources/plus.gif"))), null, dimension);
        c0618ai9.setBackground(color);
        c0618ai9.a(new bH(this));
        c0618ai9.setToolTipText("1 record Forward - CTRL+N or Right");
        this.f5447c.add(c0618ai9);
        C0618ai c0618ai10 = new C0618ai(null, com.efiAnalytics.ui.eJ.a(Toolkit.getDefaultToolkit().getImage(getClass().getResource("resources/fastforward.gif"))), null, dimension);
        c0618ai10.setBackground(color);
        c0618ai10.a(new C0661by(this));
        c0618ai10.setToolTipText("Page Forward - Page Up");
        this.f5447c.add(c0618ai10);
        C0618ai c0618ai11 = new C0618ai(null, com.efiAnalytics.ui.eJ.a(Toolkit.getDefaultToolkit().getImage(getClass().getResource("resources/end.gif"))), null, dimension);
        c0618ai11.setBackground(color);
        c0618ai11.a(new C0662bz(this));
        c0618ai11.setToolTipText("Jump to last record - End Key");
        this.f5447c.add(c0618ai11);
        return this.f5446b;
    }

    public void a(boolean z2) {
        if (z2) {
            this.f5446b.add("West", this.f5449e);
        } else {
            this.f5446b.remove(this.f5449e);
        }
        this.f5446b.validate();
    }

    public void a(InterfaceC0808hk interfaceC0808hk) {
        this.f5445a.a(interfaceC0808hk);
    }

    protected void c() {
        this.f5448d.l();
    }

    protected void d() {
        this.f5448d.m();
    }

    protected void e() {
        this.f5448d.w();
    }

    protected void f() {
        this.f5448d.z();
    }

    protected void g() {
        this.f5448d.o();
    }

    protected void h() {
        this.f5448d.n();
    }

    protected void i() {
        this.f5448d.u();
    }

    protected void j() {
        this.f5448d.v();
    }

    protected void k() {
        this.f5448d.e();
    }

    protected void l() {
        this.f5448d.j();
    }

    protected void m() {
        this.f5448d.k();
    }

    @Override // i.InterfaceC1742b
    public void a() {
    }

    @Override // i.InterfaceC1742b
    public void a(double d2) {
    }

    @Override // i.InterfaceC1742b
    public void a(C0188n c0188n) {
    }

    @Override // i.InterfaceC1742b
    public void b(C0188n c0188n) {
    }

    @Override // i.InterfaceC1742b
    public void b() {
    }

    @Override // i.InterfaceC1741a
    public void a(int i2) throws IllegalArgumentException {
        C0188n c0188nR = C0804hg.a().r();
        if (c0188nR == null) {
            this.f5450f = -1;
        } else {
            this.f5450f = c0188nR.d();
        }
        if (this.f5450f == -1) {
            this.f5449e.setText("");
        } else {
            this.f5449e.setText((i2 + 1) + " : " + this.f5450f);
        }
    }
}
