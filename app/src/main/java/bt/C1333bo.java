package bt;

import G.C0126i;
import bH.C1007o;
import c.InterfaceC1385d;
import com.efiAnalytics.plugin.ecu.ControllerParameter;
import com.efiAnalytics.ui.C1665ew;
import com.efiAnalytics.ui.InterfaceC1565bc;
import com.efiAnalytics.ui.eJ;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import r.C1798a;
import r.C1806i;
import s.C1818g;

/* renamed from: bt.bo, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bt/bo.class */
public class C1333bo extends C1348g implements G.aN, InterfaceC1349h, InterfaceC1385d, InterfaceC1565bc, ChangeListener {

    /* renamed from: a, reason: collision with root package name */
    protected G.bB f9040a;

    /* renamed from: b, reason: collision with root package name */
    protected G.R f9041b;

    /* renamed from: c, reason: collision with root package name */
    protected JSlider f9042c;

    /* renamed from: m, reason: collision with root package name */
    private double f9043m;

    /* renamed from: e, reason: collision with root package name */
    protected aZ f9045e;

    /* renamed from: g, reason: collision with root package name */
    boolean f9047g;

    /* renamed from: j, reason: collision with root package name */
    G.aM f9050j;

    /* renamed from: k, reason: collision with root package name */
    C1335bq f9051k;

    /* renamed from: l, reason: collision with root package name */
    static long f9052l = 0;

    /* renamed from: d, reason: collision with root package name */
    protected JLabel f9044d = new JLabel("", 0);

    /* renamed from: f, reason: collision with root package name */
    boolean f9046f = false;

    /* renamed from: h, reason: collision with root package name */
    C1665ew f9048h = null;

    /* renamed from: i, reason: collision with root package name */
    boolean f9049i = false;

    public C1333bo(G.R r2, G.bB bBVar) throws IllegalArgumentException {
        this.f9040a = null;
        this.f9041b = null;
        this.f9042c = null;
        this.f9043m = 1.0d;
        this.f9045e = null;
        this.f9047g = false;
        this.f9050j = null;
        this.f9051k = null;
        this.f9040a = bBVar;
        this.f9041b = r2;
        this.f9047g = bBVar.f();
        b_(bBVar.aH());
        setBorder(BorderFactory.createTitledBorder(""));
        setLayout(new BorderLayout());
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new BorderLayout(3, 3));
        String strL = this.f9040a.l();
        strL = strL != null ? C1818g.b(strL) : strL;
        if (strL != null && strL.length() == 0) {
            strL = " ";
        }
        this.f9045e = new aZ(strL);
        if (this.f9040a.d()) {
            this.f9045e.setBackground(Color.BLUE);
            this.f9045e.setForeground(Color.WHITE);
        } else if (this.f9040a.c()) {
            this.f9045e.setBackground(Color.RED);
            this.f9045e.setForeground(Color.WHITE);
        }
        this.f9045e.setHorizontalAlignment(0);
        jPanel.add("South", this.f9045e);
        this.f9044d.setMinimumSize(new Dimension(eJ.a(50), eJ.a(20)));
        this.f9044d.setPreferredSize(new Dimension(eJ.a(50), eJ.a(20)));
        if (this.f9040a.b() != null) {
            this.f9050j = r2.c(this.f9040a.b());
            if (bBVar != null && bBVar.b() != null && this.f9050j != null && !this.f9050j.i().equals(ControllerParameter.PARAM_CLASS_ARRAY)) {
                JPanel jPanel2 = new JPanel();
                jPanel2.setLayout(new GridLayout(1, 0, 2, 2));
                if (C1806i.a().a("lkjfgblkjgdoijre98u")) {
                    jPanel2.add(new C1291a(r2, bBVar.b()));
                }
                jPanel2.add(new C1353l(r2, bBVar.b()));
                jPanel.add("West", jPanel2);
            }
            if (this.f9050j != null) {
                String strO = this.f9050j.o();
                if (strO != null && !strO.equals("") && this.f9045e.getText() != null && !this.f9045e.getText().endsWith(strO)) {
                    this.f9045e.setText(strL + "(" + C1818g.b(strO) + ")");
                }
                int i2 = this.f9040a.k() == G.bB.f826b ? 1 : 0;
                this.f9043m = 1.0d / this.f9050j.A();
                int iQ = (int) (this.f9050j.q() * this.f9043m);
                this.f9042c = new JSlider(i2, iQ, (int) (this.f9050j.r() * this.f9043m), iQ);
                h();
                this.f9051k = new C1335bq(this);
                try {
                    C0126i.a(r2.c(), this.f9050j, this.f9051k);
                } catch (V.a e2) {
                    Logger.getLogger(C1333bo.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
                }
                this.f9042c.setPaintTicks(true);
                if (this.f9050j.i().equals(ControllerParameter.PARAM_CLASS_SCALAR)) {
                    c();
                    this.f9042c.addChangeListener(this);
                    JPanel jPanel3 = new JPanel();
                    jPanel3.setLayout(new BorderLayout(1, 1));
                    jPanel3.add(BorderLayout.CENTER, this.f9044d);
                    jPanel.add("East", jPanel3);
                    jPanel.add(BorderLayout.CENTER, this.f9042c);
                } else {
                    this.f9045e.setText("Unsupported data type: " + this.f9050j.i());
                }
            } else {
                this.f9045e.setEnabled(false);
                this.f9045e.setText(" ");
            }
            int iA = C1798a.a().a(C1798a.f13352aH, C1798a.a().o());
            String family = getFont().getFamily();
            if (iA > 12) {
                setFont(new Font(family, 1, iA));
            } else {
                setFont(new Font(family, 0, iA));
            }
        }
        if (this.f9042c != null) {
            this.f9042c.addFocusListener(new C1334bp(this));
        }
        add("North", jPanel);
    }

    @Override // javax.swing.JComponent, java.awt.Component
    public void requestFocus() {
        if (this.f9042c != null) {
            this.f9042c.requestFocus();
        }
    }

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    public void setFont(Font font) {
        super.setFont(font);
        if (this.f9042c != null) {
            this.f9042c.setFont(font);
        }
        if (this.f9045e != null) {
            this.f9045e.setFont(font);
        }
    }

    @Override // javax.swing.JComponent, java.awt.Component, c.InterfaceC1385d
    public void setEnabled(boolean z2) throws IllegalArgumentException {
        super.setEnabled(z2);
        a(this, z2);
        c();
        if (this.f9048h != null) {
        }
    }

    private void a(Container container, boolean z2) {
        Component[] components = container.getComponents();
        for (int i2 = 0; i2 < components.length; i2++) {
            components[i2].setEnabled(z2);
            if (components[i2] instanceof Container) {
                a((Container) components[i2], z2);
            }
        }
    }

    public void c() throws IllegalArgumentException {
        if (this.f9040a.b() != null) {
            G.aM aMVarC = b_().c(this.f9040a.b());
            if (this.f9042c != null) {
                try {
                    int iRound = (int) Math.round(aMVarC.i(b_().p())[this.f9040a.e()][this.f9040a.g()] * this.f9043m);
                    if (iRound != this.f9042c.getValue()) {
                        this.f9042c.setValue(iRound);
                    }
                } catch (V.g e2) {
                    bH.C.a(e2.getMessage(), e2, this);
                }
            }
            if (this.f9044d != null) {
                try {
                    String str = aMVarC.b(b_().p())[this.f9040a.e()][this.f9040a.g()];
                    if (this.f9044d.isEnabled()) {
                        this.f9044d.setText(str);
                    } else {
                        this.f9044d.setText("");
                    }
                } catch (V.g e3) {
                    bH.C.a(e3.getMessage(), e3, this);
                }
            }
        }
    }

    @Override // G.aN
    public void a(String str, String str2) throws IllegalArgumentException {
        c();
    }

    public int d() {
        return 0;
    }

    public void e() throws IllegalArgumentException {
        if (d() == 0) {
            a(this.f9042c.getValue() / this.f9043m);
        } else {
            f();
        }
    }

    private static void a(String str, Component component) {
        if (System.currentTimeMillis() - f9052l > 500) {
            f9052l = System.currentTimeMillis();
            com.efiAnalytics.ui.bV.d(str, component);
            f9052l = System.currentTimeMillis();
        }
    }

    public void a(double d2) throws IllegalArgumentException {
        G.aM aMVarC = b_().c(this.f9040a.b());
        try {
            if (aMVarC.i().equals(ControllerParameter.PARAM_CLASS_SCALAR)) {
                aMVarC.a(b_().p(), d2);
            } else if (!aMVarC.i().equals(ControllerParameter.PARAM_CLASS_ARRAY)) {
                bH.C.c("Didn't update it: " + ((Object) aMVarC));
            } else if (d2 > aMVarC.r()) {
                a(this.f9040a.b() + " value of " + d2 + " is too high.\nMust be less than : " + aMVarC.r(), this);
                f();
                c();
            } else if (d2 < aMVarC.q()) {
                a(this.f9040a.b() + " value of " + d2 + " is too low.\nMust be greater than : " + aMVarC.q(), this);
                f();
                c();
            } else {
                double[][] dArrI = aMVarC.i(b_().p());
                dArrI[this.f9040a.e()][this.f9040a.g()] = d2;
                aMVarC.a(b_().p(), dArrI);
            }
            if (this.f9047g) {
                b_().I();
            }
        } catch (V.g e2) {
            e2.printStackTrace();
            com.efiAnalytics.ui.bV.d("Failed to update " + this.f9040a.b() + "\n Error logged", this);
        } catch (V.j e3) {
            if (e3.a() == 2) {
                String str = this.f9040a.b() + " value of " + e3.b() + " is too low.\nMust be greater than : " + e3.c();
                if (e3.d() >= 0) {
                    str = "Col:" + (e3.e() + 1) + "\n" + str;
                }
                if (e3.d() >= 0) {
                    str = "Row:" + (e3.d() + 1) + "\n" + str;
                }
                com.efiAnalytics.ui.bV.d(str, this);
                a(e3.c());
                c();
            } else if (e3.a() == 1) {
                String str2 = this.f9040a.b() + " value of " + e3.b() + " is too high!.\nMust be less than : " + e3.c();
                if (e3.d() >= 0) {
                    str2 = "Col:" + (e3.e() + 1) + "\n" + str2;
                }
                if (e3.d() >= 0) {
                    str2 = "Row:" + (e3.d() + 1) + "\n" + str2;
                }
                com.efiAnalytics.ui.bV.d(str2, this);
                a(e3.c());
                c();
            }
            f();
        } catch (NumberFormatException e4) {
            com.efiAnalytics.ui.bV.d("Must be numeric", this);
            f();
        }
    }

    @Override // javax.swing.JComponent, java.awt.Container
    public Insets getInsets() {
        return new Insets(1, 1, 1, 1);
    }

    @Override // com.efiAnalytics.ui.InterfaceC1565bc
    public void close() {
        g();
        G.aR.a().a(this);
        if (this.f9051k != null) {
            G.aR.a().a(this.f9051k);
        }
    }

    public void f() {
        this.f9046f = true;
        new C1336br(this).start();
    }

    public void g() {
        this.f9046f = false;
    }

    @Override // javax.swing.JComponent, java.awt.Component
    public void setBackground(Color color) {
        if (this.f9042c != null) {
            this.f9042c.setBackground(color);
        }
    }

    @Override // c.InterfaceC1385d
    public G.R b_() {
        return this.f9041b;
    }

    @Override // javax.swing.event.ChangeListener
    public void stateChanged(ChangeEvent changeEvent) throws IllegalArgumentException {
        e();
    }

    @Override // bt.InterfaceC1349h
    public void a() {
        if (a_() != null) {
            try {
                setEnabled(C1007o.a(a_(), this.f9041b));
            } catch (Exception e2) {
                bH.C.a(e2.getMessage());
                e2.printStackTrace();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void h() {
        if (this.f9050j != null) {
            this.f9043m = 1.0d / this.f9050j.A();
            int iQ = (int) (this.f9050j.q() * this.f9043m);
            int iR = (int) (this.f9050j.r() * this.f9043m);
            this.f9042c.setMinimum(iQ);
            this.f9042c.setMaximum(iR);
            if ((iR - iQ) % 4 == 0) {
                this.f9042c.setMajorTickSpacing((iR - iQ) / 4);
            } else if ((iR - iQ) % 5 == 0) {
                this.f9042c.setMajorTickSpacing((iR - iQ) / 5);
            } else if ((iR - iQ) % 2 == 0) {
                this.f9042c.setMajorTickSpacing((iR - iQ) / 2);
            }
            if ((iR - iQ) % 20 == 0) {
                this.f9042c.setMinorTickSpacing((iR - iQ) / 20);
            } else if ((iR - iQ) % 10 == 0) {
                this.f9042c.setMinorTickSpacing((iR - iQ) / 10);
            }
        }
    }
}
