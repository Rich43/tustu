package aP;

import G.C0113cs;
import G.InterfaceC0109co;
import com.efiAnalytics.ui.InterfaceC1565bc;
import com.sun.org.apache.xerces.internal.xinclude.XIncludeHandler;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Window;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import s.C1818g;

/* loaded from: TunerStudioMS.jar:aP/aF.class */
public class aF extends JPanel implements G.aG, InterfaceC0109co, InterfaceC1565bc {

    /* renamed from: a, reason: collision with root package name */
    JDialog f2805a = null;

    /* renamed from: b, reason: collision with root package name */
    JTextField f2806b = new JTextField("", 4);

    /* renamed from: c, reason: collision with root package name */
    JTextField f2807c = new JTextField("", 4);

    /* renamed from: d, reason: collision with root package name */
    com.efiAnalytics.ui.dM f2808d = new com.efiAnalytics.ui.dM();

    /* renamed from: j, reason: collision with root package name */
    private G.R f2809j = null;

    /* renamed from: k, reason: collision with root package name */
    private String f2810k = "0";

    /* renamed from: e, reason: collision with root package name */
    int f2811e = 255;

    /* renamed from: f, reason: collision with root package name */
    int f2812f = -1;

    /* renamed from: g, reason: collision with root package name */
    int f2813g = -1;

    /* renamed from: h, reason: collision with root package name */
    JButton f2814h;

    /* renamed from: i, reason: collision with root package name */
    JButton f2815i;

    public aF() {
        setBorder(BorderFactory.createTitledBorder(C1818g.b("Calibrate Throttle")));
        FlowLayout flowLayout = new FlowLayout(2);
        flowLayout.setHgap(10);
        setLayout(new BoxLayout(this, 1));
        JPanel jPanel = new JPanel();
        jPanel.setLayout(flowLayout);
        JLabel jLabel = new JLabel(C1818g.b("Closed throttle ADC count"));
        jLabel.setMinimumSize(new Dimension(180, 20));
        jPanel.add(jLabel);
        jPanel.add(this.f2806b);
        JButton jButtonB = b("min");
        this.f2814h = jButtonB;
        jPanel.add(jButtonB);
        add(jPanel);
        JPanel jPanel2 = new JPanel();
        jPanel2.setLayout(flowLayout);
        JLabel jLabel2 = new JLabel(C1818g.b("Full throttle ADC count"));
        jLabel2.setMinimumSize(new Dimension(180, 20));
        jPanel2.add(jLabel2);
        jPanel2.add(this.f2807c);
        JButton jButtonB2 = b("max");
        this.f2815i = jButtonB2;
        jPanel2.add(jButtonB2);
        add(jPanel2);
        this.f2808d.a(0.0d);
        add(this.f2808d);
    }

    private JButton b(String str) {
        JButton jButton = new JButton(C1818g.b("Get Current"));
        jButton.setActionCommand(str);
        jButton.addActionListener(new aG(this));
        return jButton;
    }

    private void a(boolean z2) {
        if (this.f2809j != null) {
            this.f2814h.setEnabled(z2);
            this.f2815i.setEnabled(z2);
        }
    }

    public String a() {
        if (b() == null || !b().C().q()) {
            com.efiAnalytics.ui.bV.d("Connection unavailable.", this);
        }
        return this.f2810k;
    }

    public G.R b() {
        return this.f2809j;
    }

    public void a(G.R r2) {
        this.f2809j = r2;
        String strC = r2.c();
        C0113cs c0113csA = C0113cs.a();
        c0113csA.a(this);
        try {
            c0113csA.a(strC, r2.G().a(), this);
        } catch (Exception e2) {
            com.efiAnalytics.ui.bV.d("Unable to register TP ADC OutputChannel " + r2.G().a(), this);
        }
        try {
            G.aH aHVarG = r2.g(r2.G().a());
            this.f2811e = (int) aHVarG.m();
            String strB = r2.G().b();
            String strC2 = r2.G().c();
            G.aM aMVarC = r2.c(strB);
            G.aM aMVarC2 = r2.c(strC2);
            if (aMVarC2 != null) {
                this.f2811e = (int) aMVarC2.r();
            }
            if (aMVarC != null && aMVarC2 != null) {
                this.f2812f = (int) aMVarC.j(r2.h());
                this.f2806b.setText("" + this.f2812f);
                this.f2813g = (int) aMVarC2.j(r2.h());
                this.f2807c.setText("" + this.f2813g);
                if (aHVarG.k() != null && !aHVarG.k().equals("")) {
                    aMVarC.a(r2.p(), 0.0d);
                    aMVarC2.a(r2.p(), 1023.0d);
                    r2.I();
                }
            }
            a(r2.R());
            r2.C().a(this);
        } catch (Exception e3) {
            bH.C.c("Unable to get Max tp ADC, using default:" + this.f2811e);
        }
    }

    public void c() throws NumberFormatException {
        if (b() == null) {
            com.efiAnalytics.ui.bV.d("No EcuConfiguration set. Can not save calibration.", this);
            return;
        }
        aU.d dVarA = aU.e.a(this.f2809j);
        if (dVarA == null) {
            com.efiAnalytics.ui.bV.d("No Throttle Calibration Writer set. Can not save calibration.", this);
            return;
        }
        dVarA.a(this.f2809j, Integer.parseInt(this.f2806b.getText()), Integer.parseInt(this.f2807c.getText()));
    }

    public boolean d() {
        String str = "";
        try {
            int i2 = Integer.parseInt(this.f2807c.getText());
            int i3 = Integer.parseInt(this.f2806b.getText());
            if (i3 == i2) {
                str = str + C1818g.b("Min and Max can not be the same!") + "\n";
            } else if (i3 > i2 && str.length() == 0 && !com.efiAnalytics.ui.bV.a(C1818g.b("Min is greater than than Max!") + "\n" + C1818g.b("Are you sure you want to use these values?"), (Component) this, true)) {
                return false;
            }
            if (i3 < 0) {
                str = str + C1818g.b("Min should be greater than 0!") + "\n";
            }
            if (i2 > this.f2811e) {
                str = str + "Max ADC should not be more than " + this.f2811e + "!\n";
            }
            if (str.equals("")) {
                return true;
            }
            com.efiAnalytics.ui.bV.d(str, this);
            return false;
        } catch (NumberFormatException e2) {
            com.efiAnalytics.ui.bV.d(C1818g.b("TP ADC Min and Max must be numeric!"), this);
            return false;
        }
    }

    public boolean e() throws NumberFormatException {
        if (!d()) {
            return false;
        }
        try {
            c();
        } catch (V.a e2) {
            bH.C.a("Unable to save Throttle Calibration.", e2, this);
        }
        C0113cs.a().a(this);
        return true;
    }

    @Override // com.efiAnalytics.ui.InterfaceC1565bc
    public void close() {
        if (this.f2805a != null) {
            this.f2805a.dispose();
        }
        if (this.f2809j != null) {
            this.f2809j.C().b(this);
        }
    }

    @Override // G.InterfaceC0109co
    public void setCurrentOutputChannelValue(String str, double d2) {
        this.f2808d.a(d2 / this.f2811e);
        this.f2810k = bH.W.b(d2, 0);
    }

    public void a(Component component) {
        this.f2805a = new JDialog(com.efiAnalytics.ui.bV.a(component), C1818g.b("Calibrate Throttle Position Sensor"));
        this.f2805a.add(BorderLayout.CENTER, this);
        JButton jButton = new JButton(C1818g.b("Cancel"));
        jButton.addActionListener(new aH(this));
        JButton jButton2 = new JButton(C1818g.b(XIncludeHandler.HTTP_ACCEPT));
        jButton2.addActionListener(new aI(this));
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new FlowLayout(2));
        if (com.efiAnalytics.ui.bV.d()) {
            jPanel.add(jButton2);
            jPanel.add(jButton);
        } else {
            jPanel.add(jButton);
            jPanel.add(jButton2);
        }
        this.f2805a.add("South", jPanel);
        this.f2805a.pack();
        com.efiAnalytics.ui.bV.a((Window) com.efiAnalytics.ui.bV.a(component), (Component) this.f2805a);
        this.f2805a.setVisible(true);
        validate();
        this.f2805a.pack();
        this.f2805a.setResizable(false);
    }

    @Override // G.aG
    public boolean a(String str, G.bS bSVar) {
        a(true);
        return true;
    }

    @Override // G.aG
    public void a(String str) {
        a(false);
    }
}
