package bb;

import G.R;
import G.T;
import G.bS;
import W.ap;
import W.ar;
import aP.C0207ac;
import aP.aV;
import com.efiAnalytics.ui.InterfaceC1565bc;
import com.efiAnalytics.ui.eJ;
import com.efiAnalytics.ui.fS;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import jdk.internal.dynalink.CallSiteDescriptor;
import r.C1798a;
import r.C1806i;
import s.C1818g;

/* renamed from: bb.d, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bb/d.class */
public class C1039d extends JPanel implements A.o, aW.p, InterfaceC1565bc, fS {

    /* renamed from: m, reason: collision with root package name */
    private ae.p f7753m;

    /* renamed from: a, reason: collision with root package name */
    aW.a f7754a;

    /* renamed from: b, reason: collision with root package name */
    aV f7755b;

    /* renamed from: e, reason: collision with root package name */
    JButton f7759e;

    /* renamed from: f, reason: collision with root package name */
    JButton f7760f;

    /* renamed from: k, reason: collision with root package name */
    boolean f7765k;

    /* renamed from: n, reason: collision with root package name */
    private bS f7756n = null;

    /* renamed from: c, reason: collision with root package name */
    JLabel f7757c = new JLabel();

    /* renamed from: d, reason: collision with root package name */
    JLabel f7758d = new JLabel();

    /* renamed from: g, reason: collision with root package name */
    CardLayout f7761g = new CardLayout();

    /* renamed from: h, reason: collision with root package name */
    String f7762h = null;

    /* renamed from: i, reason: collision with root package name */
    List f7763i = null;

    /* renamed from: j, reason: collision with root package name */
    C1045j f7764j = null;

    /* renamed from: l, reason: collision with root package name */
    A.o f7766l = new C1044i(this);

    public C1039d(ae.p pVar, boolean z2) {
        this.f7755b = null;
        this.f7753m = pVar;
        this.f7765k = z2;
        setLayout(this.f7761g);
        add(j(), "Advanced Connection Settings");
        this.f7755b = new aV();
        this.f7755b.b(true);
        this.f7755b.setVisible(true);
        this.f7755b.a(this);
        this.f7755b.a(false);
        this.f7755b.setPreferredSize(new Dimension(eJ.a(520), eJ.a(320)));
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new BorderLayout());
        jPanel.add(this.f7755b, BorderLayout.CENTER);
        JPanel jPanel2 = new JPanel();
        jPanel2.setLayout(new FlowLayout(2));
        JButton jButton = new JButton(C1818g.b("Manually set Connection Settings"));
        jButton.addActionListener(new C1040e(this));
        if (z2) {
            jPanel2.add(jButton);
            jPanel.add(jPanel2, "South");
        }
        add(jPanel, "Detect Connection Settings");
        this.f7761g.show(this, "Detect Connection Settings");
        A.j.a().a(this.f7766l);
        new C1041f(this).start();
    }

    @Override // com.efiAnalytics.ui.InterfaceC1565bc
    public void close() {
        A.j.a().b(this.f7766l);
    }

    public void c() throws IllegalArgumentException {
        if (!this.f7765k) {
            d();
            return;
        }
        this.f7761g.show(this, "Advanced Connection Settings");
        this.f7755b.i();
        this.f7758d.setText(C1818g.b("Not Tested"));
    }

    public void d() {
        this.f7761g.show(this, "Detect Connection Settings");
        this.f7755b.b();
    }

    private JPanel j() throws IllegalArgumentException {
        JPanel jPanel = new JPanel();
        this.f7754a = new aW.a(aV.w.c(), i());
        this.f7754a.a(this);
        jPanel.setLayout(new BorderLayout());
        jPanel.add(BorderLayout.CENTER, this.f7754a);
        JPanel jPanel2 = new JPanel();
        jPanel2.setLayout(new GridLayout(0, 1, 5, 5));
        JPanel jPanel3 = new JPanel();
        jPanel3.setLayout(new BorderLayout());
        this.f7757c.setText(C1818g.b("Power on and Click Test"));
        this.f7759e = new JButton(C1818g.b("Auto Detect"));
        this.f7759e.addActionListener(new C1042g(this));
        jPanel3.add(this.f7759e, "East");
        JPanel jPanel4 = new JPanel();
        jPanel4.setLayout(new BorderLayout(4, 4));
        this.f7760f = new JButton(C1818g.b("Test"));
        this.f7760f.addActionListener(new C1043h(this));
        jPanel4.add(this.f7758d, BorderLayout.CENTER);
        jPanel4.add(this.f7760f, "East");
        jPanel2.add(jPanel4);
        jPanel2.add(jPanel3);
        jPanel.add("South", jPanel2);
        JPanel jPanel5 = new JPanel();
        jPanel5.setLayout(new FlowLayout(1));
        jPanel5.add(jPanel);
        return jPanel5;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void k() {
        R rC = T.a().c();
        if (rC != null) {
            if (rC.C() instanceof A.t) {
                A.f fVarA = ((A.t) rC.C()).a();
                i().a(fVarA);
                this.f7754a.a(fVarA);
                return;
            } else {
                if (rC.C() instanceof bQ.l) {
                    A.f fVarA2 = ((bQ.l) rC.C()).a();
                    i().a(fVarA2);
                    this.f7754a.a(fVarA2);
                    return;
                }
                return;
            }
        }
        A.v.a().a(new ar(C1798a.a().f13332an, "FirmwareLoader"));
        try {
            A.f fVarA3 = aV.w.c().a(C1798a.a().c("firmwareLoaderCiId", aV.w.c().b().a()), "DEFAULT_INSTANCE");
            A.v.a().b("FirmwareLoader", fVarA3);
            i().a(fVarA3);
            this.f7754a.a(fVarA3);
        } catch (IllegalAccessException e2) {
            Logger.getLogger(C1039d.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
        } catch (InstantiationException e3) {
            Logger.getLogger(C1039d.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e3);
        }
    }

    @Override // aW.p
    public void a(String str, String str2) {
        if (this.f7754a.b() == null || !str.equals("Driver")) {
            try {
                if (this.f7753m.a() != null) {
                    this.f7753m.a().a(str, str2);
                }
            } catch (A.s e2) {
                Logger.getLogger(C1039d.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
            }
        } else {
            C1798a.a().b("firmwareLoaderCiId", this.f7754a.b().h());
            this.f7753m.a(this.f7754a.b());
        }
        if (this.f7753m.a() != null) {
            ap apVarC = A.v.a().c();
            A.v.a().a(new ar(C1798a.a().f13332an, "FirmwareLoader"));
            A.v.a().a("FirmwareLoader", i().a());
            A.v.a().a(apVarC);
        }
    }

    public void e() {
        d();
    }

    public void f() throws IllegalArgumentException {
        A.x xVar = new A.x();
        A.f fVarB = this.f7754a.b();
        if (fVarB != null) {
            try {
                xVar.a((A.f) fVarB.getClass().newInstance());
                for (A.r rVar : xVar.d().l()) {
                    if (rVar.a() != 5) {
                        xVar.a(rVar.c(), fVarB.a(rVar.c()));
                    }
                }
                if (C1806i.a().a("HF-05[P54;'FD")) {
                    this.f7755b.a(xVar, "\\x02\\x00\\x00\\x00\\xFF\\x00");
                } else {
                    this.f7755b.b(xVar);
                }
                this.f7760f.setEnabled(false);
                this.f7758d.setText(C1818g.b("Testing") + ": " + fVarB.n());
                setCursor(Cursor.getPredefinedCursor(3));
                if (this.f7764j != null) {
                    this.f7764j.a();
                }
                this.f7764j = new C1045j(this);
                this.f7764j.start();
            } catch (IllegalAccessException e2) {
            } catch (InstantiationException e3) {
            }
        }
    }

    @Override // A.o
    public boolean a(String str, String str2, List list, bS bSVar) throws IllegalArgumentException {
        if (str2.contains(CallSiteDescriptor.TOKEN_DELIMITER)) {
            str2.substring(0, str2.indexOf(CallSiteDescriptor.TOKEN_DELIMITER));
            str2 = str2.substring(str2.indexOf(CallSiteDescriptor.TOKEN_DELIMITER) + 1);
        }
        try {
            A.f fVarA = aV.w.c().a(str2, "DEFAULT_INSTANCE");
            Iterator it = list.iterator();
            while (it.hasNext()) {
                A.c cVar = (A.c) it.next();
                try {
                    fVarA.a(cVar.a(), cVar.b());
                } catch (A.s e2) {
                    Logger.getLogger(C0207ac.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
                }
            }
            this.f7754a.a(fVarA);
            this.f7753m.a(fVarA);
        } catch (IllegalAccessException e3) {
            Logger.getLogger(C1039d.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e3);
        } catch (InstantiationException e4) {
            Logger.getLogger(C1039d.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e4);
        }
        this.f7756n = bSVar;
        this.f7762h = str2;
        this.f7763i = list;
        if (bSVar.e() != null) {
            this.f7757c.setText(bSVar.e().a());
            this.f7758d.setText(C1818g.b("Found") + ": " + bSVar.e().a());
        }
        if (this.f7764j != null) {
            this.f7764j.a();
        }
        this.f7760f.setEnabled(true);
        setCursor(Cursor.getDefaultCursor());
        return true;
    }

    @Override // A.o
    public void b(double d2) {
    }

    @Override // A.o
    public void a(String str) {
    }

    @Override // A.o
    public void a() throws IllegalArgumentException {
        this.f7758d.setText("Failed");
    }

    public bS g() {
        return this.f7756n;
    }

    public void h() {
        this.f7755b.i();
    }

    public ae.p i() {
        if (this.f7762h != null && this.f7763i != null && this.f7753m != null) {
            try {
                A.f fVarA = aV.w.c().a(this.f7762h, "DEFAULT_INSTANCE");
                for (A.c cVar : this.f7763i) {
                    try {
                        if (cVar.b() != null && !fVarA.a(cVar.a()).equals(cVar.b())) {
                            fVarA.a(cVar.a(), cVar.b());
                        }
                    } catch (A.s e2) {
                        Logger.getLogger(C0207ac.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
                    }
                }
                this.f7754a.a(fVarA);
                this.f7753m.a(fVarA);
            } catch (IllegalAccessException e3) {
                Logger.getLogger(C1039d.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e3);
            } catch (InstantiationException e4) {
                Logger.getLogger(C1039d.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e4);
            }
        }
        return this.f7753m;
    }

    @Override // A.o
    public void a(A.x xVar) {
    }

    @Override // com.efiAnalytics.ui.fS
    public boolean g_() {
        return true;
    }
}
