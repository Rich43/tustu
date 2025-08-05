package aP;

import G.C0129l;
import G.C0130m;
import G.C0132o;
import G.InterfaceC0131n;
import com.sun.org.apache.xerces.internal.xinclude.XIncludeHandler;
import com.sun.xml.internal.ws.model.RuntimeModeler;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Window;
import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import jdk.internal.dynalink.CallSiteDescriptor;
import r.C1798a;
import r.C1807j;
import s.C1818g;
import z.C1899c;
import z.C1900d;
import z.C1901e;

/* renamed from: aP.ac, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:aP/ac.class */
public class C0207ac extends JPanel implements A.o, InterfaceC0131n, aW.p {

    /* renamed from: a, reason: collision with root package name */
    JDialog f2883a;

    /* renamed from: b, reason: collision with root package name */
    JComboBox f2884b;

    /* renamed from: c, reason: collision with root package name */
    JComboBox f2885c;

    /* renamed from: d, reason: collision with root package name */
    JComboBox f2886d;

    /* renamed from: e, reason: collision with root package name */
    JComboBox f2887e;

    /* renamed from: f, reason: collision with root package name */
    JLabel f2888f;

    /* renamed from: g, reason: collision with root package name */
    JButton f2889g;

    /* renamed from: h, reason: collision with root package name */
    JButton f2890h;

    /* renamed from: i, reason: collision with root package name */
    G.R f2891i;

    /* renamed from: j, reason: collision with root package name */
    z.i f2892j;

    /* renamed from: k, reason: collision with root package name */
    String f2893k;

    /* renamed from: l, reason: collision with root package name */
    String f2894l;

    /* renamed from: m, reason: collision with root package name */
    String f2895m;

    /* renamed from: n, reason: collision with root package name */
    aW.a f2896n;

    /* renamed from: o, reason: collision with root package name */
    G.J f2897o;

    /* renamed from: p, reason: collision with root package name */
    W.ap f2898p;

    /* renamed from: q, reason: collision with root package name */
    aV f2899q;

    /* renamed from: r, reason: collision with root package name */
    JPanel f2900r;

    /* renamed from: s, reason: collision with root package name */
    JPanel f2901s;

    /* renamed from: u, reason: collision with root package name */
    private String f2902u;

    /* renamed from: v, reason: collision with root package name */
    private static String f2903v = "LegacyComSettings";

    /* renamed from: w, reason: collision with root package name */
    private static String f2904w = "MegaSquirtComSettings";

    /* renamed from: t, reason: collision with root package name */
    boolean f2905t;

    public C0207ac() {
        this(null);
    }

    public C0207ac(G.R r2) throws IllegalArgumentException {
        this.f2883a = null;
        this.f2884b = null;
        this.f2885c = null;
        this.f2886d = null;
        this.f2887e = null;
        this.f2888f = new JLabel();
        this.f2889g = new JButton(C1818g.b("Test Port"));
        this.f2890h = new JButton(C1818g.b("Detect"));
        this.f2891i = null;
        this.f2892j = new z.i();
        this.f2893k = "";
        this.f2894l = "";
        this.f2895m = "";
        this.f2897o = null;
        this.f2898p = new W.ar(new Properties(), "TempCommSettings");
        this.f2899q = null;
        this.f2900r = null;
        this.f2901s = null;
        this.f2902u = C1818g.b("Scanning Ports") + "...";
        this.f2905t = false;
        this.f2891i = r2;
        setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(0), C1818g.b("Communication Settings")));
        setLayout(new BorderLayout());
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new BorderLayout(15, 15));
        JPanel jPanel2 = new JPanel();
        jPanel2.setLayout(new GridLayout(1, 2));
        JPanel jPanel3 = new JPanel();
        jPanel3.setLayout(new GridLayout(0, 1));
        JPanel jPanel4 = new JPanel();
        JPanel jPanel5 = new JPanel();
        jPanel5.setLayout(new BorderLayout());
        jPanel4.setLayout(new GridLayout(0, 1, 2, 2));
        JLabel jLabel = new JLabel();
        jLabel.setText(C1818g.b("Configuration") + CallSiteDescriptor.TOKEN_DELIMITER);
        if (0 != 0) {
            jPanel3.add(jLabel);
        }
        this.f2884b = new JComboBox();
        k();
        G.T tA = G.T.a();
        if (tA.c() != null) {
            this.f2884b.setSelectedItem(tA.c().c());
        }
        this.f2884b.addActionListener(new C0208ad(this));
        jPanel5.add("North", this.f2884b);
        if (0 != 0 && this.f2884b.getItemCount() > 0) {
            jPanel4.add(jPanel5);
        }
        JPanel jPanel6 = new JPanel();
        int i2 = 0;
        jPanel6.setLayout(new BorderLayout());
        JLabel jLabel2 = new JLabel();
        jLabel2.setText(C1818g.b("Driver") + CallSiteDescriptor.TOKEN_DELIMITER);
        this.f2885c = new JComboBox();
        this.f2885c.setEditable(false);
        String str = C1798a.f13371ba;
        Iterator itB = C1899c.a().b();
        while (itB.hasNext()) {
            C1900d c1900d = (C1900d) itB.next();
            C0217am c0217am = new C0217am(this, c1900d);
            this.f2885c.addItem(c0217am);
            i2++;
            if (c1900d.a().equals(str)) {
                c0217am.a(c1900d.b() + " (" + C1818g.b(Action.DEFAULT) + ")");
                this.f2885c.setSelectedItem(c0217am);
            }
        }
        this.f2885c.addActionListener(new C0209ae(this));
        jPanel6.add("South", this.f2885c);
        if (i2 > 1) {
            jPanel3.add(jLabel2);
            jPanel4.add(jPanel6);
        }
        jPanel2.add(jPanel3);
        jPanel2.add(jPanel4);
        jPanel.add("North", jPanel2);
        JPanel jPanel7 = new JPanel();
        jPanel7.setLayout(new GridLayout(0, 1));
        JPanel jPanel8 = new JPanel();
        jPanel8.setLayout(new GridLayout(0, 1, 2, 2));
        this.f2900r = new JPanel();
        this.f2900r.setLayout(new CardLayout());
        this.f2901s = new JPanel();
        this.f2901s.setLayout(new GridLayout(1, 1));
        this.f2900r.add(this.f2901s, f2904w);
        JPanel jPanel9 = new JPanel();
        jPanel9.setLayout(new BorderLayout());
        JPanel jPanel10 = new JPanel();
        jPanel10.setLayout(new FlowLayout(1));
        jPanel10.setBorder(BorderFactory.createTitledBorder(C1818g.b("Connection Type")));
        jPanel10.add(new JLabel(C1818g.b("RS232 Serial Port")));
        jPanel9.add("North", jPanel10);
        JPanel jPanel11 = new JPanel();
        jPanel11.setLayout(new BorderLayout());
        jPanel11.setBorder(BorderFactory.createTitledBorder(C1818g.b("Connection Settings")));
        JPanel jPanel12 = new JPanel();
        jPanel12.setLayout(new GridLayout(0, 2, 2, 2));
        JPanel jPanel13 = new JPanel();
        jPanel13.setLayout(new BorderLayout());
        JLabel jLabel3 = new JLabel();
        jLabel3.setText(C1818g.b(RuntimeModeler.PORT) + CallSiteDescriptor.TOKEN_DELIMITER);
        jPanel7.add(jLabel3);
        this.f2887e = new JComboBox();
        this.f2887e.setEditable(true);
        this.f2887e.addItem(this.f2902u);
        new C0218an(this).start();
        this.f2887e.addActionListener(new C0210af(this));
        jPanel13.add("South", this.f2887e);
        jPanel8.add(jPanel13);
        JPanel jPanel14 = new JPanel();
        jPanel14.setLayout(new BorderLayout());
        JLabel jLabel4 = new JLabel();
        jLabel4.setText(C1818g.b("Baud Rate") + CallSiteDescriptor.TOKEN_DELIMITER);
        jPanel7.add(jLabel4);
        this.f2886d = new JComboBox();
        for (String str2 : this.f2892j.c()) {
            this.f2886d.addItem(str2);
        }
        if (this.f2891i == null || aE.a.A() == null) {
            this.f2886d.setSelectedItem(aD.a.f2297c);
        } else {
            this.f2886d.setSelectedItem(aE.a.A().m(this.f2891i.c()));
        }
        this.f2886d.addActionListener(new C0211ag(this));
        jPanel14.add("North", this.f2886d);
        jPanel8.add(jPanel14);
        jPanel12.add(jPanel7);
        jPanel12.add(jPanel8);
        JPanel jPanel15 = new JPanel();
        jPanel15.setLayout(new BorderLayout());
        jPanel15.add("North", jPanel12);
        jPanel11.add(BorderLayout.CENTER, jPanel15);
        jPanel9.add(BorderLayout.CENTER, jPanel11);
        this.f2900r.add(jPanel9, f2903v);
        jPanel.add(BorderLayout.CENTER, this.f2900r);
        add(BorderLayout.CENTER, jPanel);
        JPanel jPanel16 = new JPanel();
        jPanel16.setLayout(new GridLayout(1, 2));
        jPanel16.add(this.f2888f);
        JPanel jPanel17 = new JPanel();
        jPanel16.add(jPanel17);
        this.f2889g.addActionListener(new C0212ah(this));
        jPanel17.add(this.f2889g);
        jPanel17.add(this.f2890h);
        this.f2890h.addActionListener(new C0213ai(this));
        a(false);
        add("South", jPanel16);
        ((CardLayout) this.f2900r.getLayout()).show(this.f2900r, f2904w);
        try {
            b((String) this.f2884b.getSelectedItem());
        } catch (V.a e2) {
            Logger.getLogger(C0207ac.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
        }
        A.d.a().a(true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void m() throws IllegalArgumentException {
        C0217am c0217am = (C0217am) this.f2885c.getSelectedItem();
        if (c0217am.a().a().equals(C1899c.f14084e)) {
            this.f2887e.setEnabled(false);
        } else {
            this.f2887e.setEnabled(true);
        }
        B.i iVarD = null;
        if (aE.a.A() != null) {
            iVarD = C1807j.d(new File(aE.a.A().t()));
        }
        this.f2897o = C1899c.a().a(this.f2891i, c0217am.a().a(), aV.w.c(), iVarD, null);
        if (this.f2897o instanceof A.g) {
            this.f2896n.a(this.f2896n.b());
            ((CardLayout) this.f2900r.getLayout()).show(this.f2900r, f2904w);
        } else {
            ((CardLayout) this.f2900r.getLayout()).show(this.f2900r, f2903v);
        }
        n();
    }

    private void n() throws IllegalArgumentException {
        this.f2888f.setText(C1818g.b("Not tested"));
    }

    public void b() {
        setCursor(Cursor.getPredefinedCursor(3));
        this.f2889g.setEnabled(false);
        String[] strArrA = this.f2892j.a();
        this.f2887e.removeAllItems();
        for (String str : strArrA) {
            this.f2887e.addItem(str);
        }
        if (this.f2891i != null && aE.a.A() != null) {
            this.f2887e.setSelectedItem(aE.a.A().n(this.f2891i.c()));
        } else if (com.efiAnalytics.ui.bV.d()) {
            this.f2887e.setSelectedItem("COM1");
        } else if (this.f2887e.getItemCount() > 0) {
            this.f2887e.setSelectedIndex(0);
        }
        this.f2889g.setEnabled(true);
        setCursor(Cursor.getDefaultCursor());
    }

    public void b(String str) throws V.a, IllegalArgumentException {
        if (str == null) {
            return;
        }
        G.R rC = G.T.a().c(str);
        if (rC == null) {
            throw new V.a("Ecu Configuration " + str + " not currently loaded.");
        }
        a(rC);
    }

    public void a(G.R r2) throws V.a, IllegalArgumentException {
        if (r2 == null) {
            throw new V.a("Ecu Configuration not valid.");
        }
        this.f2891i = r2;
        C0214aj c0214aj = new C0214aj(this, r2);
        this.f2905t = true;
        c0214aj.start();
        a(this.f2884b, this.f2891i.c());
        a(this.f2886d, this.f2891i.O().r() + "");
        a(this.f2887e, this.f2891i.O().s());
        this.f2893k = this.f2891i.O().s();
        this.f2894l = this.f2891i.O().r() + "";
        if (r2.C() != null) {
            this.f2895m = r2.C().n();
        } else {
            this.f2895m = C1798a.f13347aC;
        }
        this.f2896n = new aW.a(aV.w.c(), A.v.a().a(r2));
        this.f2901s.removeAll();
        this.f2901s.add(this.f2896n);
        this.f2896n.a(this);
        m();
        if (r2.C() instanceof A.g) {
            A.f fVarA = ((A.g) r2.C()).a();
            if (fVarA != null) {
                a(fVarA);
            } else if (aV.w.c().b() != null) {
                try {
                    fVarA = aV.w.c().b().c(r2.c());
                } catch (IllegalAccessException e2) {
                    Logger.getLogger(C0207ac.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
                } catch (InstantiationException e3) {
                    Logger.getLogger(C0207ac.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e3);
                }
            }
            this.f2896n.a(fVarA);
        }
    }

    private void a(JComboBox jComboBox, String str) {
        jComboBox.setSelectedItem(str);
    }

    public void c() {
        new C0219ao(this, this).start();
    }

    public String d() {
        return (String) this.f2886d.getSelectedItem();
    }

    public String f() {
        return (String) this.f2887e.getSelectedItem();
    }

    public String g() {
        return ((C0217am) this.f2885c.getSelectedItem()).a().a();
    }

    public void c(String str) {
        for (int i2 = 0; i2 < this.f2885c.getItemCount(); i2++) {
            Object itemAt = this.f2885c.getItemAt(i2);
            if (itemAt != null && (itemAt instanceof C0217am)) {
                C0217am c0217am = (C0217am) this.f2885c.getItemAt(i2);
                if (str.equals(c0217am.a().a())) {
                    this.f2885c.setSelectedItem(c0217am);
                    this.f2895m = str;
                    return;
                }
            }
        }
    }

    public void h() {
        if (this.f2891i != null && (this.f2891i.C() instanceof A.g)) {
            b(((A.g) this.f2891i.C()).a());
        }
        if (this.f2891i != null && this.f2905t) {
            try {
                this.f2891i.C().d();
            } catch (C0129l e2) {
                Logger.getLogger(C0207ac.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
            }
        }
        i();
    }

    public synchronized void i() {
        if (this.f2883a != null) {
            this.f2883a.dispose();
        }
        A.j.a().b(this);
        A.d.a().a(false);
    }

    public void a(Component component) {
        this.f2883a = new JDialog(com.efiAnalytics.ui.bV.a(component), C1818g.b("Communication Settings"));
        this.f2883a.add(BorderLayout.CENTER, this);
        JButton jButton = new JButton(C1818g.b("Cancel"));
        jButton.addActionListener(new C0215ak(this));
        JButton jButton2 = new JButton(C1818g.b(XIncludeHandler.HTTP_ACCEPT));
        jButton2.addActionListener(new C0216al(this));
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new FlowLayout(2));
        if (com.efiAnalytics.ui.bV.d()) {
            jPanel.add(jButton2);
            jPanel.add(jButton);
        } else {
            jPanel.add(jButton);
            jPanel.add(jButton2);
        }
        this.f2883a.add("South", jPanel);
        this.f2883a.pack();
        com.efiAnalytics.ui.bV.a((Window) com.efiAnalytics.ui.bV.a(component), (Component) this.f2883a);
        this.f2883a.setVisible(true);
        validate();
        this.f2883a.pack();
        this.f2883a.setResizable(false);
    }

    public void a(boolean z2) {
        this.f2890h.setEnabled(z2);
        this.f2890h.setVisible(z2);
    }

    public void j() {
        a(aE.a.A());
    }

    public void a(aE.a aVar) {
        B.i iVarD;
        this.f2891i.O().j((String) this.f2886d.getSelectedItem());
        this.f2891i.O().k((String) this.f2887e.getSelectedItem());
        String strG = g();
        if (aVar != null && aVar.u().equals(this.f2884b.getSelectedItem())) {
            aVar.a((String) this.f2884b.getSelectedItem(), (String) this.f2886d.getSelectedItem());
            aVar.b((String) this.f2884b.getSelectedItem(), (String) this.f2887e.getSelectedItem());
            aVar.c(this.f2891i.c(), strG);
        }
        if (this.f2891i.C() instanceof C1901e) {
            C1901e c1901e = (C1901e) this.f2891i.C();
            String strF = c1901e.f();
            String strG2 = c1901e.g();
            if (!this.f2886d.getSelectedItem().equals(strF) || !strG2.endsWith((String) this.f2887e.getSelectedItem())) {
                this.f2891i.C().c();
            }
        }
        if (this.f2897o instanceof bQ.l) {
            try {
                A.f fVarB = this.f2896n.b();
                bQ.j.a().a(this.f2891i.c(), fVarB.h());
                for (A.r rVar : fVarB.l()) {
                    fVarB.a(rVar.c(), this.f2896n.a(rVar.c()));
                    if (rVar.c().equals("Baud Rate")) {
                        aVar.a((String) this.f2884b.getSelectedItem(), this.f2896n.a(rVar.c()).toString());
                        this.f2891i.O().j(this.f2896n.a(rVar.c()).toString());
                    }
                }
                bQ.j.a().a(this.f2891i.c(), fVarB);
                A.p.a((A.h) this.f2897o, fVarB);
                if ((fVarB instanceof B.a) && aE.a.A() != null && (iVarD = C1807j.d(new File(aE.a.A().t()))) != null) {
                    iVarD.d(((B.a) fVarB).a());
                    C1807j.a(new File(aE.a.A().t()), iVarD);
                }
            } catch (A.s e2) {
                com.efiAnalytics.ui.bV.d(e2.getMessage(), this);
                Logger.getLogger(C0207ac.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
            } catch (C0129l e3) {
                com.efiAnalytics.ui.bV.d(e3.getMessage(), this);
                Logger.getLogger(C0207ac.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e3);
            }
        }
        if (this.f2897o instanceof A.h) {
            try {
                A.f fVarB2 = this.f2896n.b();
                A.v.a().a(this.f2891i.c(), fVarB2.h());
                for (A.r rVar2 : fVarB2.l()) {
                    fVarB2.a(rVar2.c(), this.f2896n.a(rVar2.c()));
                    if (rVar2.c().equals("Baud Rate")) {
                        aVar.a((String) this.f2884b.getSelectedItem(), this.f2896n.a(rVar2.c()).toString());
                        this.f2891i.O().j(this.f2896n.a(rVar2.c()).toString());
                    }
                }
                A.v.a().a(this.f2891i.c(), fVarB2);
                A.p.a((A.h) this.f2897o, fVarB2);
            } catch (A.s e4) {
                com.efiAnalytics.ui.bV.d(e4.getMessage(), this);
                Logger.getLogger(C0207ac.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e4);
            } catch (C0129l e5) {
                com.efiAnalytics.ui.bV.d(e5.getMessage(), this);
                Logger.getLogger(C0207ac.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e5);
            }
        }
        if (this.f2897o == null || this.f2897o.equals(this.f2891i.C())) {
            return;
        }
        this.f2891i.C().c();
        this.f2891i.b(this.f2897o);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Multi-variable type inference failed */
    public synchronized void o() throws IllegalArgumentException {
        boolean zG = this.f2891i.O().G();
        this.f2891i.O().d(true);
        if (this.f2897o instanceof C1901e) {
            if (1 == 0) {
                C1901e c1901e = (C1901e) this.f2897o;
                setCursor(Cursor.getPredefinedCursor(3));
                this.f2889g.setEnabled(false);
                C0130m c0130mA = C0130m.a((String) this.f2887e.getSelectedItem(), (String) this.f2886d.getSelectedItem());
                c0130mA.b(this);
                this.f2888f.setText("");
                c1901e.b(c0130mA);
                c1901e.c();
                c1901e.l();
                if (this.f2888f.getText().equals("")) {
                    this.f2888f.setText(C1818g.b("Failed") + "!");
                }
                this.f2889g.setEnabled(true);
                setCursor(Cursor.getDefaultCursor());
            } else {
                A.f fVarA = null;
                try {
                    try {
                        try {
                            try {
                                try {
                                    fVarA = aV.w.c().a(aD.a.f2298d, this.f2891i.c());
                                    fVarA.a("Baud Rate", this.f2886d.getSelectedItem());
                                    fVarA.a("Com Port", this.f2887e.getSelectedItem());
                                    G.J jA = C1899c.a().a(this.f2891i, C1899c.f14084e, aV.w.c(), aE.a.A() != null ? C1807j.d(new File(aE.a.A().t())) : null, null);
                                    A.p.a((A.h) jA, fVarA);
                                    setCursor(Cursor.getPredefinedCursor(3));
                                    this.f2889g.setEnabled(false);
                                    C0130m c0130mA2 = C0130m.a((String) this.f2887e.getSelectedItem(), (String) this.f2886d.getSelectedItem());
                                    c0130mA2.b(this);
                                    this.f2888f.setText("");
                                    jA.b(c0130mA2);
                                    if (this.f2888f.getText().equals("")) {
                                        this.f2888f.setText(C1818g.b("Failed") + "!");
                                    }
                                    this.f2889g.setEnabled(true);
                                    setCursor(Cursor.getDefaultCursor());
                                    if (fVarA != null) {
                                        try {
                                            fVarA.g();
                                        } catch (Exception e2) {
                                        }
                                    }
                                } catch (InstantiationException e3) {
                                    com.efiAnalytics.ui.bV.d(C1818g.b("Application Error testing port. Your port may work fine, try it without testing."), this);
                                    Logger.getLogger(C0207ac.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e3);
                                    if (fVarA != null) {
                                        try {
                                            fVarA.g();
                                        } catch (Exception e4) {
                                        }
                                    }
                                }
                            } catch (IllegalAccessException e5) {
                                com.efiAnalytics.ui.bV.d(C1818g.b("Application Error testing port. Your port may work fine, try it without testing."), this);
                                Logger.getLogger(C0207ac.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e5);
                                if (fVarA != null) {
                                    try {
                                        fVarA.g();
                                    } catch (Exception e6) {
                                    }
                                }
                            }
                        } catch (C0129l e7) {
                            com.efiAnalytics.ui.bV.d(e7.getMessage(), this);
                            Logger.getLogger(C0207ac.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e7);
                            if (fVarA != null) {
                                try {
                                    fVarA.g();
                                } catch (Exception e8) {
                                }
                            }
                        }
                    } catch (A.s e9) {
                        com.efiAnalytics.ui.bV.d(e9.getMessage(), this);
                        Logger.getLogger(C0207ac.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e9);
                        if (fVarA != null) {
                            try {
                                fVarA.g();
                            } catch (Exception e10) {
                            }
                        }
                    }
                } catch (Throwable th) {
                    if (fVarA != null) {
                        try {
                            fVarA.g();
                        } catch (Exception e11) {
                        }
                    }
                    throw th;
                }
            }
        } else if (this.f2897o instanceof A.g) {
            A.f fVarB = null;
            try {
                try {
                    fVarB = this.f2896n.b();
                    for (A.r rVar : fVarB.l()) {
                        fVarB.a(rVar.c(), this.f2896n.a(rVar.c()));
                    }
                    A.p.a((A.h) this.f2897o, fVarB);
                    setCursor(Cursor.getPredefinedCursor(3));
                    this.f2889g.setEnabled(false);
                    C0130m c0130mA3 = C0130m.a((String) this.f2887e.getSelectedItem(), (String) this.f2886d.getSelectedItem());
                    c0130mA3.b(this);
                    this.f2888f.setText("");
                    this.f2897o.b(c0130mA3);
                    if (this.f2888f.getText().equals("")) {
                        this.f2888f.setText(C1818g.b("Failed") + "!");
                    }
                    this.f2889g.setEnabled(true);
                    setCursor(Cursor.getDefaultCursor());
                    if (fVarB != null) {
                        try {
                            fVarB.g();
                        } catch (Exception e12) {
                        }
                    }
                } catch (A.s e13) {
                    com.efiAnalytics.ui.bV.d(e13.getMessage(), this);
                    Logger.getLogger(C0207ac.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e13);
                    if (fVarB != null) {
                        try {
                            fVarB.g();
                        } catch (Exception e14) {
                        }
                    }
                } catch (C0129l e15) {
                    com.efiAnalytics.ui.bV.d(e15.getMessage(), this);
                    Logger.getLogger(C0207ac.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e15);
                    if (fVarB != null) {
                        try {
                            fVarB.g();
                        } catch (Exception e16) {
                        }
                    }
                }
            } catch (Throwable th2) {
                if (fVarB != null) {
                    try {
                        fVarB.g();
                    } catch (Exception e17) {
                    }
                }
                throw th2;
            }
        }
        this.f2891i.O().d(zG);
    }

    @Override // G.InterfaceC0131n
    public void a(double d2) {
    }

    @Override // G.InterfaceC0131n
    public synchronized void a(C0132o c0132o) throws IllegalArgumentException {
        G.aB.a().e();
        if (c0132o.a() == 1) {
            this.f2888f.setText(C1818g.b("Successful") + "!!!");
        } else {
            this.f2888f.setText(C1818g.b("Failed") + "!");
            this.f2888f.repaint();
        }
        this.f2889g.setEnabled(true);
        setCursor(Cursor.getDefaultCursor());
        notify();
    }

    @Override // A.o
    public boolean a(String str, String str2, List list, G.bS bSVar) {
        try {
            if (!ae.o.a(bSVar)) {
                com.efiAnalytics.ui.bV.d(C1818g.b("A Controller was found on") + " " + str + ".\n" + C1818g.b("However, there appears to be no Firmware loaded.") + "\n" + C1818g.b("Please check help for information on getting and installing firmware.") + "\n\n" + C1818g.b("You must have firmware installed to connect."), this);
            }
            if (str2.contains(CallSiteDescriptor.TOKEN_DELIMITER)) {
                String strSubstring = str2.substring(0, str2.indexOf(CallSiteDescriptor.TOKEN_DELIMITER));
                if (!C1899c.a().a(strSubstring)) {
                    com.efiAnalytics.ui.bV.d(C1818g.b("The Controller found requires a driver that is not available in this edition of the application."), this);
                    return false;
                }
                str2 = str2.substring(str2.indexOf(CallSiteDescriptor.TOKEN_DELIMITER) + 1, str2.length());
                c(strSubstring);
            }
            A.f fVarA = aV.w.c().a(str2, this.f2891i.c());
            Iterator it = list.iterator();
            while (it.hasNext()) {
                A.c cVar = (A.c) it.next();
                try {
                    fVarA.a(cVar.a(), cVar.b());
                } catch (A.s e2) {
                    Logger.getLogger(C0207ac.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
                }
            }
            this.f2896n.a(fVarA);
        } catch (IllegalAccessException e3) {
            Logger.getLogger(C0207ac.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e3);
        } catch (InstantiationException e4) {
            Logger.getLogger(C0207ac.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e4);
        }
        if (!str2.equals(aD.a.f2298d)) {
            return true;
        }
        c(C1899c.f14080a);
        Iterator it2 = list.iterator();
        while (it2.hasNext()) {
            A.c cVar2 = (A.c) it2.next();
            if (cVar2.a().equals("Baud Rate")) {
                a(this.f2886d, cVar2.b().toString());
            } else if (cVar2.a().equals("Com Port")) {
                a(this.f2887e, cVar2.b().toString());
            } else if (cVar2.a().equals("2nd Com Port")) {
                a(this.f2887e, cVar2.b().toString());
            }
        }
        return true;
    }

    @Override // A.o
    public void b(double d2) {
    }

    @Override // A.o
    public void a() {
    }

    public void k() {
        this.f2884b.removeAllItems();
        G.T tA = G.T.a();
        String[] strArrD = tA.d();
        if (this.f2891i == null) {
            this.f2891i = tA.c();
        }
        for (String str : strArrD) {
            this.f2884b.addItem(str);
        }
    }

    @Override // G.InterfaceC0131n
    public void e() {
        G.aB.a().d();
    }

    public void l() {
        if (this.f2899q != null) {
            this.f2899q.e();
            this.f2899q = null;
        }
        this.f2899q = new aV(com.efiAnalytics.ui.bV.b(this));
        this.f2899q.setVisible(true);
        this.f2899q.a(this);
    }

    @Override // A.o
    public void a(String str) {
    }

    private void a(A.f fVar) {
        try {
            a(fVar, this.f2898p);
        } catch (Exception e2) {
            Logger.getLogger(C0207ac.class.getName()).log(Level.WARNING, "Failed to capture ControllerInterface setting", (Throwable) e2);
        }
        A.f fVarB = this.f2896n.b(fVar.h());
        if (fVarB != null) {
            b(fVarB, this.f2898p);
        }
    }

    private void b(A.f fVar) {
        b(fVar, this.f2898p);
    }

    private W.ap a(A.f fVar, W.ap apVar) {
        String strH = fVar.h();
        for (A.r rVar : fVar.l()) {
            Object objA = fVar.a(rVar.c());
            if (objA != null) {
                apVar.a(strH + rVar.c(), objA.toString());
            } else {
                apVar.a(rVar.c(), "");
            }
        }
        return apVar;
    }

    private void b(A.f fVar, W.ap apVar) {
        String strH = fVar.h();
        for (A.r rVar : fVar.l()) {
            Object objA = fVar.a(rVar.c());
            try {
                fVar.a(rVar.c(), apVar.b(strH + rVar.c(), (objA != null ? objA : "").toString()));
            } catch (Exception e2) {
                Logger.getLogger(C0207ac.class.getName()).log(Level.WARNING, "Unable to set ControllerInterface setting", (Throwable) e2);
            }
        }
    }

    @Override // aW.p
    public void a(String str, String str2) throws IllegalArgumentException {
        n();
    }

    @Override // A.o
    public void a(A.x xVar) {
    }
}
