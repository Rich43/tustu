package be;

import W.ak;
import bt.C1351j;
import com.efiAnalytics.ui.bV;
import com.efiAnalytics.ui.cO;
import com.efiAnalytics.ui.eJ;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Image;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import s.C1818g;

/* renamed from: be.w, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:be/w.class */
public class C1107w extends JPanel implements InterfaceC1099o {

    /* renamed from: c, reason: collision with root package name */
    by.d f8008c;

    /* renamed from: f, reason: collision with root package name */
    private G.R f8005f = null;

    /* renamed from: a, reason: collision with root package name */
    JTextField f8006a = new JTextField("", 15);

    /* renamed from: b, reason: collision with root package name */
    JTextField f8007b = new JTextField("", 3);

    /* renamed from: g, reason: collision with root package name */
    private S f8009g = null;

    /* renamed from: h, reason: collision with root package name */
    private S f8010h = null;

    /* renamed from: d, reason: collision with root package name */
    String f8011d = "";

    /* renamed from: e, reason: collision with root package name */
    C1098n f8012e = new C1098n();

    public C1107w(G.R r2) {
        setBorder(BorderFactory.createTitledBorder(C1818g.b("OutputChannel Expression Editor")));
        setLayout(new BorderLayout());
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new BorderLayout(10, 10));
        add(jPanel, "North");
        this.f8006a.setBorder(BorderFactory.createBevelBorder(1));
        this.f8006a.addKeyListener(this.f8012e);
        JPanel jPanel2 = new JPanel();
        jPanel2.setLayout(new FlowLayout(0));
        jPanel2.add(a("Channel Name", this.f8006a, C1818g.b("A unique name to reference this channel.") + " " + C1818g.b("This name will be added to the available Channel List and can be used by gauges, data log fields or other expressions.")));
        JPanel jPanel3 = new JPanel();
        jPanel3.setLayout(new BorderLayout());
        jPanel3.add(this.f8007b, "West");
        this.f8007b.addKeyListener(this.f8012e);
        this.f8007b.setBorder(BorderFactory.createBevelBorder(1));
        jPanel2.add(a("Units", jPanel3, null));
        jPanel2.add(new JLabel(" "));
        jPanel.add(jPanel2, BorderLayout.CENTER);
        Image imageA = null;
        try {
            imageA = cO.a().a(cO.f11120J, this);
        } catch (V.a e2) {
            Logger.getLogger(C1107w.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
        }
        JButton jButton = new JButton(new ImageIcon(imageA));
        jButton.setPreferredSize(new Dimension(eJ.a(28), eJ.a(28)));
        jPanel.add(jButton, "East");
        jButton.addActionListener(new x(this));
        JPanel jPanel4 = new JPanel();
        jPanel4.setLayout(new BorderLayout());
        jPanel4.add(a("Expression", new JLabel(), "<html>" + C1818g.b("Enter a mathematical expression based on other Channels or Setting Parameters")), "North");
        ArrayList arrayList = new ArrayList();
        arrayList.add(by.j.a(r2));
        if (!ak.b(aE.a.A().j())) {
            arrayList.add(by.j.b(r2));
        }
        this.f8008c = new by.d(arrayList, by.n.a(), C1818g.d());
        jPanel4.add(this.f8008c, BorderLayout.CENTER);
        add(jPanel4, BorderLayout.CENTER);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void g() {
        R r2;
        try {
            r2 = this.f8010h.a(this.f8008c.b());
        } catch (StackOverflowError e2) {
            r2 = new R();
            r2.c();
            r2.a("Circular Dependency!");
        }
        if (r2.a()) {
            bV.d(C1818g.b("Valid Expression!"), this);
        } else {
            bV.d(C1818g.b(r2.d()), this);
        }
    }

    private JPanel a(String str, Component component, String str2) {
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new BorderLayout(5, 5));
        Dimension dimension = new Dimension(eJ.a(100), eJ.a(20));
        JLabel jLabel = new JLabel(C1818g.b(str) + ": ");
        jLabel.setMinimumSize(dimension);
        jPanel.add(jLabel, "West");
        jPanel.add(component, BorderLayout.CENTER);
        if (str2 != null) {
            jPanel.add(new C1351j(C1818g.b(str2)), "East");
        }
        return jPanel;
    }

    public boolean a() {
        if (this.f8009g != null) {
            R rA = this.f8009g.a(this.f8006a.getText());
            if (!rA.a()) {
                bV.d(C1818g.b(rA.d() != null ? rA.d() : "Invalid Channel Name"), this);
                return false;
            }
        }
        if (this.f8010h == null) {
            return true;
        }
        R rA2 = this.f8010h.a(this.f8008c.b());
        if (rA2.a()) {
            return true;
        }
        bV.d(C1818g.b(rA2.d() != null ? rA2.d() : "Invalid Expression"), this);
        return false;
    }

    public void a(G.R r2) {
        this.f8005f = r2;
    }

    public String b() {
        return this.f8006a.getText();
    }

    public void a(String str, boolean z2) {
        this.f8006a.setText(str);
        this.f8006a.setEditable(z2);
    }

    public String d() {
        return this.f8008c.b();
    }

    public void a(String str) {
        this.f8008c.d(str);
    }

    public void a(S s2) {
        this.f8009g = s2;
    }

    public void b(S s2) {
        this.f8010h = s2;
    }

    public void b(String str) {
        this.f8007b.setText(str);
    }

    public String e() {
        return this.f8007b.getText();
    }

    @Override // be.InterfaceC1099o
    public boolean c() {
        return this.f8012e.a() || !this.f8011d.equals(this.f8008c.b());
    }

    public void f() {
        this.f8012e.b();
        this.f8011d = this.f8008c.b();
    }
}
