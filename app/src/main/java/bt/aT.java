package bt;

import G.C0083bp;
import G.C0126i;
import bH.C1007o;
import com.efiAnalytics.plugin.ecu.ControllerParameter;
import com.efiAnalytics.ui.C1665ew;
import com.efiAnalytics.ui.InterfaceC1565bc;
import com.efiAnalytics.ui.cP;
import com.efiAnalytics.ui.eJ;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import org.icepdf.core.util.PdfOps;
import r.C1798a;
import r.C1806i;
import s.C1818g;

/* loaded from: TunerStudioMS.jar:bt/aT.class */
public class aT extends C1348g implements G.aN, InterfaceC1282J, bX, bY, InterfaceC1349h, InterfaceC1356o, InterfaceC1565bc, cP {

    /* renamed from: a, reason: collision with root package name */
    protected C0083bp f8777a;

    /* renamed from: b, reason: collision with root package name */
    protected G.R f8778b;

    /* renamed from: c, reason: collision with root package name */
    protected Component f8779c;

    /* renamed from: d, reason: collision with root package name */
    protected C1366y f8780d;

    /* renamed from: e, reason: collision with root package name */
    protected C1273A f8781e;

    /* renamed from: f, reason: collision with root package name */
    protected bI f8782f;

    /* renamed from: g, reason: collision with root package name */
    protected C1276D f8783g;

    /* renamed from: h, reason: collision with root package name */
    protected JLabel f8784h;

    /* renamed from: i, reason: collision with root package name */
    protected aZ f8785i;

    /* renamed from: k, reason: collision with root package name */
    boolean f8787k;

    /* renamed from: m, reason: collision with root package name */
    C1665ew f8789m;

    /* renamed from: o, reason: collision with root package name */
    KeyAdapter f8791o;

    /* renamed from: r, reason: collision with root package name */
    static long f8794r = 0;

    /* renamed from: j, reason: collision with root package name */
    boolean f8786j = false;

    /* renamed from: l, reason: collision with root package name */
    aX f8788l = new aX(this);

    /* renamed from: n, reason: collision with root package name */
    boolean f8790n = false;

    /* renamed from: p, reason: collision with root package name */
    FocusAdapter f8792p = new aU(this);

    /* renamed from: q, reason: collision with root package name */
    aY f8793q = null;

    /* renamed from: s, reason: collision with root package name */
    List f8795s = new ArrayList();

    /* renamed from: t, reason: collision with root package name */
    boolean f8796t = true;

    public aT(G.R r2, C0083bp c0083bp) throws IllegalArgumentException {
        this.f8777a = null;
        this.f8778b = null;
        this.f8779c = null;
        this.f8780d = null;
        this.f8781e = null;
        this.f8782f = null;
        this.f8783g = null;
        this.f8784h = null;
        this.f8785i = null;
        this.f8787k = false;
        this.f8789m = null;
        this.f8791o = null;
        this.f8777a = c0083bp;
        this.f8778b = r2;
        this.f8787k = c0083bp.f();
        setLayout(new BorderLayout());
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new BorderLayout(3, 3));
        String strL = this.f8777a.l();
        strL = strL != null ? C1818g.b(strL) : strL;
        if (strL != null && strL.length() == 0) {
            strL = " ";
        }
        try {
            this.f8785i = (aZ) q.h.a().a(aZ.class);
            this.f8785i.setText(strL);
        } catch (Exception e2) {
            this.f8785i = new aZ(strL);
            bH.C.b("Failed to get SettingsLabel from cache, creating...");
        }
        if (this.f8777a.d()) {
            this.f8785i.setOpaque(true);
            this.f8785i.setBackground(Color.BLUE);
            this.f8785i.setForeground(Color.WHITE);
        } else if (this.f8777a.c()) {
            this.f8785i.setOpaque(true);
            this.f8785i.setBackground(Color.RED);
            this.f8785i.setForeground(Color.WHITE);
        } else {
            this.f8785i.setForeground(UIManager.getColor("Label.foreground"));
        }
        jPanel.add(BorderLayout.CENTER, this.f8785i);
        try {
            C0126i.a(r2.c(), this.f8777a.i(), this.f8788l);
        } catch (V.a e3) {
            Logger.getLogger(aT.class.getName()).log(Level.WARNING, "Unable to monitor label changes for " + this.f8777a.l(), (Throwable) e3);
        }
        if (this.f8777a.b() != null) {
            G.aM aMVarC = r2.c(this.f8777a.b());
            if (aMVarC != null && aMVarC != null && !aMVarC.i().equals(ControllerParameter.PARAM_CLASS_ARRAY)) {
                JPanel jPanel2 = new JPanel();
                jPanel2.setLayout(new GridLayout(1, 0, 2, 2));
                if (C1806i.a().a("lkjfgblkjgdoijre98u")) {
                    jPanel2.add(new C1291a(r2, c0083bp.b()));
                }
                jPanel2.add(new C1353l(r2, c0083bp.b()));
                jPanel.add("West", jPanel2);
            }
            if (aMVarC != null) {
                String strO = aMVarC.o();
                if (strO != null && !strO.equals("") && this.f8785i.getText() != null && !this.f8785i.getText().endsWith(strO)) {
                    this.f8785i.setText(strL + "(" + C1818g.b(strO) + ")");
                }
                try {
                    C0126i.a(r2.c(), aMVarC.p(), this.f8788l);
                } catch (V.a e4) {
                    Logger.getLogger(aT.class.getName()).log(Level.WARNING, "Unable to monitor units changes for " + aMVarC.aJ(), (Throwable) e4);
                }
                if (c0083bp.h()) {
                    try {
                        this.f8784h = (JLabel) q.h.a().a(JLabel.class);
                        this.f8784h.setText("                             ");
                        this.f8784h.setHorizontalAlignment(0);
                    } catch (Exception e5) {
                        this.f8784h = new JLabel("                             ", 0);
                        bH.C.b("Cache Failed, creating new JLabel");
                    }
                    this.f8784h.setMinimumSize(eJ.a(1850, 20));
                    this.f8784h.setPreferredSize(eJ.a(180, 20));
                    jPanel.add("East", this.f8784h);
                    this.f8779c = this.f8784h;
                    f();
                } else if (aMVarC.i().equals(ControllerParameter.PARAM_CLASS_BITS)) {
                    try {
                        this.f8780d = (C1366y) q.h.a().a(C1366y.class);
                    } catch (Exception e6) {
                        this.f8780d = new C1366y();
                        bH.C.b("Cache Failed, creating new UI Component");
                    }
                    Iterator it = aMVarC.x().iterator();
                    while (it.hasNext()) {
                        String str = (String) it.next();
                        if (str.indexOf("INVALID") == -1) {
                            String strSubstring = str;
                            if (strSubstring.startsWith(PdfOps.DOUBLE_QUOTE__TOKEN) && strSubstring.endsWith(PdfOps.DOUBLE_QUOTE__TOKEN)) {
                                String strSubstring2 = strSubstring.substring(1);
                                strSubstring = strSubstring2.substring(0, strSubstring2.length() - 1);
                            }
                            this.f8780d.a(str, C1818g.b(strSubstring));
                        }
                    }
                    f();
                    jPanel.add("East", this.f8780d);
                    this.f8780d.a(this);
                    this.f8779c = this.f8780d;
                } else if (aMVarC.i().equals(ControllerParameter.PARAM_CLASS_SCALAR) || aMVarC.i().equals(ControllerParameter.PARAM_CLASS_BITS) || aMVarC.i().equals(ControllerParameter.PARAM_CLASS_ARRAY)) {
                    try {
                        this.f8781e = (C1273A) q.h.a().a(C1273A.class);
                        if (c0083bp.j()) {
                            this.f8781e.a(c0083bp.j());
                            this.f8781e.b(aMVarC.y() * 2);
                        } else {
                            this.f8781e.b(aMVarC.u());
                        }
                    } catch (Exception e7) {
                        this.f8781e = new C1273A();
                        bH.C.b("Failed to create UI Component, creating new.");
                    }
                    f();
                    this.f8781e.a(this);
                    JPanel jPanel3 = new JPanel();
                    jPanel3.setLayout(new BorderLayout(1, 1));
                    jPanel3.add(BorderLayout.CENTER, this.f8781e);
                    jPanel.add("East", jPanel3);
                    if (!aMVarC.i().equals(ControllerParameter.PARAM_CLASS_ARRAY)) {
                        this.f8789m = new C1665ew();
                        this.f8789m.a(this);
                        jPanel3.add("East", this.f8789m);
                        this.f8789m.setToolTipText("<html> <b>D</b> or DOWN Arrow to decrease value<br> <b>+</b> or UP Arrow to increase value.</html>");
                    }
                    this.f8791o = new aV(this);
                    this.f8781e.addKeyListener(this.f8791o);
                    this.f8779c = this.f8781e;
                    f();
                } else if (aMVarC.i().equals("string")) {
                    try {
                        this.f8782f = (bI) q.h.a().a(bI.class);
                    } catch (Exception e8) {
                        bH.C.b("Failed to create StringParameterText, creating new.");
                        this.f8782f = new bI();
                    }
                    this.f8782f.setText("");
                    this.f8782f.setColumns(aMVarC.u() > 0 ? aMVarC.u() : aMVarC.y() / 2);
                    this.f8782f.a(aMVarC.y());
                    this.f8782f.a(this);
                    JPanel jPanel4 = new JPanel();
                    jPanel4.setLayout(new BorderLayout(1, 1));
                    jPanel4.add(BorderLayout.CENTER, this.f8782f);
                    jPanel.add("East", jPanel4);
                    this.f8779c = this.f8782f;
                    f();
                } else {
                    try {
                        this.f8781e = (C1273A) q.h.a().a(C1273A.class);
                        this.f8781e.b(aMVarC.u());
                    } catch (Exception e9) {
                        this.f8781e = new C1273A();
                        bH.C.b("Failed to create ParameterText, creating new.");
                    }
                    jPanel.add("East", this.f8781e);
                }
            } else if (r2.g(this.f8777a.b()) != null) {
                this.f8783g = new C1276D(r2, c0083bp);
                jPanel.add("East", this.f8783g);
                this.f8779c = this.f8783g;
            } else {
                this.f8785i.setEnabled(false);
                this.f8785i.setText(" ");
            }
            int iA = C1798a.a().a(C1798a.f13352aH, C1798a.a().o());
            String family = getFont().getFamily();
            Font font = iA > 12 ? new Font(family, 1, iA) : new Font(family, 0, iA);
            setFont(font);
            this.f8785i.setFont(font);
            if (this.f8779c != null) {
                this.f8779c.setFont(font);
            }
            if (aMVarC != null) {
                try {
                    C0126i.a(r2.c(), aMVarC, this);
                } catch (V.a e10) {
                    Logger.getLogger(aT.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e10);
                }
            }
        } else if (c0083bp.i() != null && c0083bp.h()) {
            this.f8784h = new JLabel("                             ", 0);
            this.f8784h.setMinimumSize(new Dimension(1850, 20));
            this.f8784h.setPreferredSize(new Dimension(180, 20));
            jPanel.add("East", this.f8784h);
            this.f8779c = this.f8784h;
            f();
            int iA2 = C1798a.a().a(C1798a.f13352aH, C1798a.a().o());
            String family2 = getFont().getFamily();
            if (iA2 > 12) {
                setFont(new Font(family2, 1, iA2));
            } else {
                setFont(new Font(family2, 0, iA2));
            }
            JLabel jLabel = new JLabel(" ");
            jLabel.setMinimumSize(new Dimension(15, 15));
            jLabel.setPreferredSize(new Dimension(15, 15));
            jPanel.add("West", jLabel);
        }
        if (this.f8779c != null) {
            this.f8779c.addFocusListener(this.f8792p);
        }
        add("North", jPanel);
    }

    @Override // javax.swing.JComponent, java.awt.Component
    public void requestFocus() {
        if (this.f8779c != null) {
            this.f8779c.requestFocus();
        }
    }

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    public void setFont(Font font) {
        super.setFont(font);
        if (this.f8779c != null) {
            this.f8779c.setFont(font);
        }
        if (this.f8785i != null) {
            this.f8785i.setFont(font);
        }
    }

    @Override // bt.InterfaceC1349h
    public void a() {
        if (this.f8777a == null || a_() == null || this.f8779c == null) {
            return;
        }
        try {
            setEnabled(C1007o.a(a_(), n()));
        } catch (Exception e2) {
            if (!this.f8790n) {
                com.efiAnalytics.ui.bV.d("Invalid enable condition on field " + this.f8777a.b() + ":\n { " + a_() + " } ", this);
                this.f8790n = true;
            }
            bH.C.a(e2.getMessage());
        }
    }

    @Override // javax.swing.JComponent, java.awt.Component, c.InterfaceC1385d
    public void setEnabled(boolean z2) throws IllegalArgumentException {
        if (this.f8796t || z2 != isEnabled()) {
            super.setEnabled(z2);
            a(this, z2);
            f();
            if (this.f8789m != null) {
            }
            this.f8796t = false;
        }
    }

    protected void b(boolean z2) {
        super.setEnabled(z2);
        a(this, z2);
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

    public void c(boolean z2) {
        if (this.f8781e != null) {
            this.f8781e.setEditable(z2);
        } else if (this.f8782f != null) {
            this.f8782f.setEditable(z2);
        }
        if (this.f8789m != null) {
            this.f8789m.setVisible(z2);
        }
    }

    public boolean e() {
        return this.f8781e != null && this.f8781e.isEditable();
    }

    public synchronized void f() throws IllegalArgumentException {
        if (this.f8777a != null) {
            if (this.f8777a.b() != null || this.f8777a.h()) {
                G.aM aMVarC = n().c(this.f8777a.b());
                if (this.f8780d != null) {
                    try {
                        if (n().p().j() || this.f8780d.a(aMVarC.f(n().p())) || this.f8780d.getItemCount() <= 0) {
                            if (n().p().j()) {
                                String strF = aMVarC.f(n().p());
                                if (strF.equals("INVALID") || strF.equals("\"INVALID\"")) {
                                    this.f8780d.addItem(strF);
                                }
                                this.f8780d.a(strF);
                            }
                        } else if (isEnabled()) {
                            b(this.f8780d.a());
                        }
                        return;
                    } catch (Exception e2) {
                        bH.C.b(e2.getMessage());
                        return;
                    }
                }
                if (this.f8781e != null) {
                    try {
                        String str = aMVarC.b(n().p())[this.f8777a.e()][this.f8777a.g()];
                        if (this.f8781e.f()) {
                            str = "0x" + Integer.toHexString(Integer.parseInt(str)).toUpperCase();
                        } else {
                            this.f8781e.b(aMVarC.u());
                        }
                        if (!str.equals(this.f8781e.getText())) {
                            this.f8781e.setText(str);
                            this.f8781e.selectAll();
                        }
                        return;
                    } catch (V.g e3) {
                        bH.C.a(e3.getMessage(), e3, this);
                        return;
                    } catch (IndexOutOfBoundsException e4) {
                        bH.C.a("Error in configuration, accessing ivalid index or addressing out of page.", e4, this);
                        return;
                    }
                }
                if (this.f8782f != null) {
                    try {
                        String strD = aMVarC.d(n().p());
                        if (!strD.equals(this.f8782f.getText())) {
                            this.f8782f.setText(strD);
                            this.f8782f.selectAll();
                        }
                        return;
                    } catch (Exception e5) {
                        bH.C.a(e5.getMessage(), e5, this);
                        return;
                    }
                }
                if (this.f8784h != null) {
                    if (this.f8777a.i() != null && this.f8777a.h()) {
                        try {
                            this.f8784h.setText(this.f8777a.j() ? Integer.toHexString(Integer.parseInt(this.f8777a.i().a())).toUpperCase() : this.f8777a.i().a());
                            return;
                        } catch (V.g e6) {
                            bH.C.a("Unable to set Label:" + e6.getLocalizedMessage());
                            this.f8784h.setText("ERROR!");
                            return;
                        }
                    }
                    try {
                        String strSubstring = aMVarC.b(n().p())[this.f8777a.e()][this.f8777a.g()];
                        if (strSubstring.startsWith(PdfOps.DOUBLE_QUOTE__TOKEN) && strSubstring.endsWith(PdfOps.DOUBLE_QUOTE__TOKEN)) {
                            String strSubstring2 = strSubstring.substring(1);
                            strSubstring = strSubstring2.substring(0, strSubstring2.length() - 1);
                        }
                        String upperCase = this.f8777a.j() ? Integer.toHexString(Integer.parseInt(strSubstring)).toUpperCase() : strSubstring;
                        if (this.f8784h.isEnabled()) {
                            this.f8784h.setText(upperCase);
                        } else {
                            this.f8784h.setText("");
                        }
                    } catch (V.g e7) {
                        bH.C.a(e7.getMessage(), e7, this);
                    }
                }
            }
        }
    }

    @Override // G.aN
    public void a(String str, String str2) {
        SwingUtilities.invokeLater(new aW(this));
    }

    public int g() {
        if (this.f8781e == null) {
            return 0;
        }
        G.aM aMVarC = n().c(this.f8777a.b());
        double dA = this.f8781e.a();
        if (dA > aMVarC.r()) {
            return 1;
        }
        return dA < aMVarC.a(this.f8777a.e()) ? 2 : 0;
    }

    public void h() throws IllegalArgumentException {
        if (this.f8781e != null) {
            if (g() == 0) {
                b(this.f8781e.getText());
                return;
            } else {
                i();
                return;
            }
        }
        if (this.f8782f != null) {
            if (g() == 0) {
                b(this.f8782f.getText());
            } else {
                i();
            }
        }
    }

    private static void a(String str, Component component) {
        if (System.currentTimeMillis() - f8794r > 500) {
            f8794r = System.currentTimeMillis();
            com.efiAnalytics.ui.bV.d(str, component);
            f8794r = System.currentTimeMillis();
        }
    }

    @Override // bt.bX
    public void b(String str) throws IllegalArgumentException {
        G.bQ bQVar;
        String[] strArrB;
        String strB;
        G.bQ bQVar2;
        String[] strArrB2;
        String strB2;
        if (this.f8777a != null) {
            if (!C1007o.a(this.f8777a.aH(), n())) {
                return;
            }
            G.aM aMVarC = n().c(this.f8777a.b());
            try {
                if (aMVarC.i().equals(ControllerParameter.PARAM_CLASS_BITS)) {
                    aMVarC.a(n().p(), str);
                } else if (aMVarC.i().equals(ControllerParameter.PARAM_CLASS_SCALAR)) {
                    if (this.f8777a.j()) {
                        aMVarC.a(n().p(), Integer.parseInt(bH.W.b(str, "0x", ""), 16));
                    } else {
                        aMVarC.a(n().p(), Double.parseDouble(str));
                    }
                } else if (aMVarC.i().equals("string")) {
                    if (str.length() <= aMVarC.y()) {
                        aMVarC.a(n().p(), str);
                    } else {
                        com.efiAnalytics.ui.bV.d("String too long, Maximum Characters: " + aMVarC.y(), this.f8779c);
                        i();
                    }
                } else if (aMVarC.i().equals(ControllerParameter.PARAM_CLASS_ARRAY)) {
                    double d2 = Double.parseDouble(str);
                    if (d2 > aMVarC.r()) {
                        a(this.f8777a.b() + " " + C1818g.b("value") + " " + d2 + " " + C1818g.b("is too high.") + "\n" + C1818g.b("Must be less than:") + " " + aMVarC.r(), this);
                        i();
                        f();
                    } else if (d2 < aMVarC.a(this.f8777a.e())) {
                        a(this.f8777a.b() + " " + C1818g.b("value") + " " + d2 + " " + C1818g.b("is too low.") + "\n" + C1818g.b("Must be greater than:") + " " + aMVarC.a(this.f8777a.e()), this);
                        i();
                        f();
                    } else {
                        double[][] dArrI = aMVarC.i(n().p());
                        for (int i2 = 0; i2 < dArrI.length; i2++) {
                            for (int i3 = 0; i3 < dArrI[0].length; i3++) {
                                if (dArrI[i2][i3] < aMVarC.a(i2)) {
                                    dArrI[i2][i3] = aMVarC.a(i2);
                                } else if (dArrI[i2][i3] > aMVarC.r()) {
                                    dArrI[i2][i3] = aMVarC.r();
                                }
                            }
                        }
                        aMVarC.a(n().p(), d2, this.f8777a.e(), this.f8777a.g());
                        aMVarC.i(n().p());
                    }
                } else {
                    bH.C.c("Didn't update it: " + ((Object) aMVarC));
                }
                if (this.f8787k) {
                    n().I();
                }
            } catch (V.g e2) {
                e2.printStackTrace();
                com.efiAnalytics.ui.bV.d("Failed to update " + this.f8777a.b() + "\n Error logged", this);
            } catch (V.j e3) {
                if (e3.a() == 2) {
                    String str2 = ((this.f8785i == null || this.f8785i.getText().isEmpty()) ? this.f8777a.b() : this.f8785i.getText()) + " value of " + e3.b() + " is too low.\nMust be greater than : " + e3.c();
                    if (e3.d() >= 0) {
                        str2 = "Col:" + (e3.e() + 1) + "\n" + str2;
                    }
                    if (e3.d() >= 0) {
                        str2 = "Row:" + (e3.d() + 1) + "\n" + str2;
                    }
                    if ((aMVarC.t() instanceof G.bQ) && (strArrB2 = (bQVar2 = (G.bQ) aMVarC.t()).b()) != null && strArrB2.length == 1 && strArrB2[0].equals(bQVar2.c().trim()) && (strB2 = G.bL.b(this.f8778b, strArrB2[0])) != null) {
                        str2 = str2 + "\n\n" + C1818g.b("Note!") + "\n" + C1818g.b("This limit can be adjusted at:") + "\n" + strB2;
                    }
                    com.efiAnalytics.ui.bV.d(str2, this);
                    b(e3.c() + "");
                    f();
                } else if (e3.a() == 1) {
                    String str3 = ((this.f8785i == null || this.f8785i.getText().isEmpty()) ? this.f8777a.b() : this.f8785i.getText()) + " value of " + e3.b() + " is too high!.\nMust be less than : " + e3.c();
                    if (e3.d() >= 0) {
                        str3 = "Col:" + (e3.e() + 1) + "\n" + str3;
                    }
                    if (e3.d() >= 0) {
                        str3 = "Row:" + (e3.d() + 1) + "\n" + str3;
                    }
                    if ((aMVarC.s() instanceof G.bQ) && (strArrB = (bQVar = (G.bQ) aMVarC.s()).b()) != null && strArrB.length == 1 && strArrB[0].equals(bQVar.c().trim()) && (strB = G.bL.b(this.f8778b, strArrB[0])) != null) {
                        str3 = str3 + "\n\n" + C1818g.b("Note!") + "\n" + C1818g.b("This limit can be adjusted at:") + "\n" + strB;
                    }
                    com.efiAnalytics.ui.bV.d(str3, this);
                    b(e3.c() + "");
                    f();
                }
                i();
            } catch (NumberFormatException e4) {
                com.efiAnalytics.ui.bV.d("Must be numeric", this);
                i();
            }
        }
    }

    public void a(int i2, int i3) throws IllegalArgumentException {
        this.f8777a.a(i2);
        this.f8777a.b(i3);
        f();
    }

    @Override // javax.swing.JComponent, java.awt.Container
    public Insets getInsets() {
        return new Insets(1, 1, 1, 1);
    }

    @Override // com.efiAnalytics.ui.InterfaceC1565bc
    public void close() {
        j();
        G.aR.a().a(this);
        G.aR.a().a(this.f8788l);
        if (this.f8780d != null) {
            this.f8780d.b(this);
        }
        if (this.f8781e != null && this.f8791o != null) {
            this.f8781e.removeKeyListener(this.f8791o);
            this.f8781e.b(this);
        }
        if (this.f8782f != null) {
            this.f8782f.b(this);
        }
        if (this.f8779c != null) {
            this.f8779c.removeFocusListener(this.f8792p);
            q.h.a().a(this.f8779c);
            this.f8779c = null;
        }
        if (this.f8785i != null) {
            q.h.a().a(this.f8785i);
            this.f8785i = null;
        }
        if (this.f8783g != null) {
            this.f8783g.close();
        }
        this.f8777a = null;
        this.f8796t = true;
    }

    @Override // bt.InterfaceC1356o
    public void a(boolean z2) {
        if (z2) {
            this.f8785i.setOpaque(true);
            this.f8785i.setBackground(Color.YELLOW);
            this.f8785i.setForeground(Color.BLACK);
            i();
            return;
        }
        if (this.f8777a.d()) {
            this.f8785i.setOpaque(true);
            this.f8785i.setBackground(Color.BLUE);
            this.f8785i.setForeground(Color.WHITE);
        } else if (this.f8777a.c()) {
            this.f8785i.setOpaque(true);
            this.f8785i.setBackground(Color.RED);
            this.f8785i.setForeground(Color.WHITE);
        } else {
            this.f8785i.setOpaque(false);
            this.f8785i.setForeground(UIManager.getColor("Label.foreground"));
            this.f8785i.setBackground(UIManager.getColor("Label.background"));
        }
        j();
    }

    public void i() {
        if (this.f8779c != null) {
            this.f8786j = true;
            this.f8793q = new aY(this);
            this.f8793q.start();
        }
    }

    public void j() {
        this.f8786j = false;
        if (this.f8793q != null) {
            this.f8793q.a();
        }
    }

    public boolean k() {
        G.aM aMVarC;
        return (c() == null || (aMVarC = n().c(this.f8777a.b())) == null || aMVarC.B()) ? false : true;
    }

    @Override // bt.InterfaceC1356o
    public String c() {
        if (this.f8777a != null) {
            return this.f8777a.b();
        }
        return null;
    }

    @Override // javax.swing.JComponent, java.awt.Component
    public void setBackground(Color color) {
        if (this.f8779c != null) {
            this.f8779c.setBackground(color);
        }
    }

    @Override // javax.swing.JComponent, java.awt.Component
    public void setForeground(Color color) {
        if (this.f8779c != null) {
            this.f8779c.setForeground(color);
        }
    }

    @Override // com.efiAnalytics.ui.cP
    public void l() {
        if (isEnabled() && e()) {
            G.aM aMVarC = n().c(this.f8777a.b());
            try {
                if (aMVarC.i().equals(ControllerParameter.PARAM_CLASS_ARRAY)) {
                    double dA = aMVarC.i(n().p())[this.f8777a.e()][this.f8777a.g()] + aMVarC.A();
                    if (dA > aMVarC.r()) {
                        dA = aMVarC.r();
                    }
                    aMVarC.a(n().p(), dA, this.f8777a.e(), this.f8777a.g());
                } else {
                    aMVarC.l(n().h());
                }
            } catch (V.g e2) {
                e2.printStackTrace();
            } catch (V.j e3) {
            }
        }
    }

    @Override // com.efiAnalytics.ui.cP
    public void m() {
        if (isEnabled() && e()) {
            G.aM aMVarC = n().c(this.f8777a.b());
            try {
                if (aMVarC.i().equals(ControllerParameter.PARAM_CLASS_ARRAY)) {
                    double dA = aMVarC.i(n().p())[this.f8777a.e()][this.f8777a.g()] - aMVarC.A();
                    if (dA < aMVarC.a(this.f8777a.e())) {
                        dA = aMVarC.a(this.f8777a.e());
                    }
                    aMVarC.a(n().p(), dA, this.f8777a.e(), this.f8777a.g());
                } else {
                    aMVarC.m(n().h());
                }
            } catch (V.g e2) {
                e2.printStackTrace();
            } catch (V.j e3) {
            }
        }
    }

    public G.R n() {
        return this.f8778b;
    }

    public void a(FocusListener focusListener) {
        if (this.f8779c != null) {
            this.f8779c.addFocusListener(focusListener);
        } else {
            bH.C.a("There is no settingComp, can not add FocusListener");
        }
    }

    @Override // java.awt.Component
    public FocusListener[] getFocusListeners() {
        return this.f8779c != null ? this.f8779c.getFocusListeners() : super.getFocusListeners();
    }

    @Override // bt.bY
    public void b() {
        if (this.f8777a == null || this.f8777a.m() == null || this.f8777a.m().equals("")) {
            return;
        }
        boolean zA = true;
        try {
            zA = C1007o.a(this.f8777a.m(), n());
        } catch (V.g e2) {
            Logger.getLogger(aT.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
        }
        if (isVisible() && !zA) {
            setVisible(false);
            if (getParent() instanceof JPanel) {
                ((JPanel) getParent()).revalidate();
                return;
            }
            return;
        }
        if (isVisible() || !zA) {
            return;
        }
        setVisible(true);
        if (getParent() instanceof JPanel) {
            ((JPanel) getParent()).revalidate();
        }
    }

    public void a(int i2) {
        if (this.f8781e != null) {
            this.f8781e.b(i2);
        }
    }

    public void b(int i2) {
        if (this.f8781e != null) {
            this.f8781e.a(i2);
        }
    }

    @Override // bt.InterfaceC1282J
    public void a(InterfaceC1281I interfaceC1281I) {
        this.f8795s.add(interfaceC1281I);
    }

    @Override // bt.InterfaceC1282J
    public void b(InterfaceC1281I interfaceC1281I) {
        this.f8795s.remove(interfaceC1281I);
    }

    @Override // bt.InterfaceC1282J
    public String d() {
        return this.f8777a.b();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void c(String str) {
        Iterator it = this.f8795s.iterator();
        while (it.hasNext()) {
            ((InterfaceC1281I) it.next()).b(str);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void d(String str) {
        Iterator it = this.f8795s.iterator();
        while (it.hasNext()) {
            ((InterfaceC1281I) it.next()).a(str);
        }
    }
}
