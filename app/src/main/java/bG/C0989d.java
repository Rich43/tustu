package bG;

import W.ap;
import com.efiAnalytics.ui.Cdo;
import com.efiAnalytics.ui.bV;
import com.sun.org.apache.xerces.internal.xinclude.XIncludeHandler;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Window;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

/* renamed from: bG.d, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bG/d.class */
public class C0989d extends JDialog {

    /* renamed from: a, reason: collision with root package name */
    Dimension f6928a;

    /* renamed from: b, reason: collision with root package name */
    Cdo f6929b;

    /* renamed from: c, reason: collision with root package name */
    Cdo f6930c;

    /* renamed from: d, reason: collision with root package name */
    Cdo f6931d;

    /* renamed from: g, reason: collision with root package name */
    private boolean f6932g;

    /* renamed from: e, reason: collision with root package name */
    ap f6933e;

    /* renamed from: f, reason: collision with root package name */
    String f6934f;

    public C0989d(Window window, ap apVar) {
        super(window, "Set Base Teeth");
        this.f6928a = null;
        this.f6932g = false;
        this.f6933e = null;
        this.f6934f = "<html><body>These are just the starting point settings for your wheel.<br>Starting with a symetrical n-m wheel, you will then be able to<br>Add/Remove/Move and resize teeth.</body></html>";
        super.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
        this.f6933e = apVar;
        setLayout(new BorderLayout());
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new BorderLayout());
        add(BorderLayout.CENTER, jPanel);
        jPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JPanel jPanel2 = new JPanel();
        jPanel2.setLayout(new GridLayout(0, 1));
        jPanel.add(BorderLayout.CENTER, jPanel2);
        jPanel.add(new JLabel(this.f6934f), "North");
        C0990e c0990e = new C0990e(this);
        C0991f c0991f = new C0991f(this);
        this.f6929b = new Cdo();
        this.f6929b.b(0);
        this.f6929b.addFocusListener(c0990e);
        this.f6929b.addKeyListener(c0991f);
        this.f6929b.a(b("TotalTrigTeeth", 36.0d));
        a(jPanel2, "Base number of teeth including missing.", this.f6929b);
        this.f6930c = new Cdo("0");
        this.f6930c.b(0);
        this.f6930c.addFocusListener(c0990e);
        this.f6930c.addKeyListener(c0991f);
        this.f6930c.a(b("MissingTeeth", 1.0d));
        a(jPanel2, "Number of missing Teeth.", this.f6930c);
        this.f6931d = new Cdo("0");
        this.f6931d.b(1);
        this.f6931d.addFocusListener(c0990e);
        this.f6931d.addKeyListener(c0991f);
        this.f6931d.a(b("ToothWidth", 0.0d));
        a(jPanel2, "Teeth Width in Degrees. (0 for auto)", this.f6931d);
        JPanel jPanel3 = new JPanel();
        jPanel3.setLayout(new FlowLayout(2));
        JButton jButton = new JButton(XIncludeHandler.HTTP_ACCEPT);
        jButton.addActionListener(new C0992g(this));
        jPanel3.add(jButton);
        JButton jButton2 = new JButton("Cancel");
        jButton2.addActionListener(new h(this));
        jPanel3.add(jButton2);
        jPanel.add(jPanel3, "South");
        pack();
        setResizable(false);
    }

    public int a() {
        return (int) this.f6929b.e();
    }

    public int b() {
        return (int) this.f6930c.e();
    }

    public double c() {
        return this.f6931d.e();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void e() {
        double dE = this.f6929b.e();
        if (Double.isNaN(dE) || dE <= 0.0d) {
            bV.d("Number of Teeth must have a positive value.", this);
            return;
        }
        if (this.f6930c.e() < 0.0d) {
            this.f6930c.a(0.0d);
        }
        a("TotalTrigTeeth", this.f6929b.e());
        a("MissingTeeth", this.f6930c.e());
        a("ToothWidth", this.f6931d.e());
        this.f6932g = true;
        dispose();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void f() {
        dispose();
    }

    private void a(JPanel jPanel, String str, Component component) {
        JPanel jPanel2 = new JPanel();
        jPanel.add(jPanel2);
        jPanel2.setLayout(new BorderLayout());
        JLabel jLabel = new JLabel(str, 4);
        jPanel2.add(jLabel, BorderLayout.CENTER);
        if (this.f6928a == null) {
            this.f6928a = jLabel.getPreferredSize();
        }
        if (jLabel.getPreferredSize().width > this.f6928a.width) {
            this.f6928a.width += 10;
        }
        jLabel.setPreferredSize(this.f6928a);
        jPanel2.add(component, "East");
    }

    public boolean d() {
        return this.f6932g;
    }

    private void a(String str, double d2) {
        if (this.f6933e != null) {
            this.f6933e.a(str, "" + d2);
        }
    }

    private double b(String str, double d2) {
        double d3 = d2;
        if (this.f6933e != null) {
            try {
                d3 = Double.parseDouble(this.f6933e.b(str, "" + d2));
            } catch (Exception e2) {
            }
        }
        return d3;
    }
}
