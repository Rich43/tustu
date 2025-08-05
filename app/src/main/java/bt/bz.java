package bt;

import G.C0126i;
import bG.C0986a;
import bG.C0988c;
import bH.C1007o;
import c.InterfaceC1385d;
import com.efiAnalytics.ui.InterfaceC1565bc;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import r.C1806i;
import r.C1807j;
import s.C1818g;

/* loaded from: TunerStudioMS.jar:bt/bz.class */
public class bz extends C1348g implements G.aN, InterfaceC1349h, InterfaceC1385d, InterfaceC1565bc {

    /* renamed from: a, reason: collision with root package name */
    G.R f9075a;

    /* renamed from: b, reason: collision with root package name */
    bG.m f9076b;

    /* renamed from: j, reason: collision with root package name */
    G.bI f9084j;

    /* renamed from: k, reason: collision with root package name */
    G.aN f9085k;

    /* renamed from: m, reason: collision with root package name */
    bG.q f9087m;

    /* renamed from: c, reason: collision with root package name */
    G.bK f9077c = null;

    /* renamed from: d, reason: collision with root package name */
    G.aM f9078d = null;

    /* renamed from: e, reason: collision with root package name */
    G.aM f9079e = null;

    /* renamed from: f, reason: collision with root package name */
    G.aM f9080f = null;

    /* renamed from: g, reason: collision with root package name */
    G.aM f9081g = null;

    /* renamed from: h, reason: collision with root package name */
    bG.G f9082h = null;

    /* renamed from: i, reason: collision with root package name */
    bG.l f9083i = new C0986a(60, 2);

    /* renamed from: l, reason: collision with root package name */
    JLabel f9086l = new JLabel("-------");

    public bz(G.R r2, G.bI bIVar) throws IllegalArgumentException {
        this.f9075a = null;
        this.f9076b = null;
        this.f9087m = null;
        this.f9075a = r2;
        this.f9084j = bIVar;
        setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(0), bIVar.M()));
        this.f9076b = new bG.m();
        this.f9076b.setName(bIVar.aJ());
        this.f9076b.a(new bA(this));
        setLayout(new BorderLayout());
        if (bIVar.b() != null) {
            setPreferredSize(new Dimension(bIVar.b().a(), bIVar.b().b()));
        }
        add(BorderLayout.CENTER, this.f9076b);
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new BorderLayout());
        add("North", jPanel);
        if (bIVar.c() && C1806i.a().a("09PO;L'RE4")) {
            this.f9087m = new bG.q(this.f9076b);
            add("East", this.f9087m);
            this.f9076b.a(true);
            JMenuBar jMenuBar = new JMenuBar();
            jPanel.add("North", jMenuBar);
            jMenuBar.add(c());
            jMenuBar.add(this.f9087m.a());
        } else if (bIVar.c()) {
            JMenuBar jMenuBar2 = new JMenuBar();
            jMenuBar2.add(c());
            jPanel.add("North", jMenuBar2);
        }
        JPanel jPanel2 = new JPanel();
        jPanel2.setLayout(new GridLayout(0, 1));
        JPanel jPanel3 = new JPanel();
        jPanel3.setLayout(new BorderLayout());
        jPanel3.add("West", new JLabel(C1818g.b("Teeth: sensor to missing tooth @ TDC") + ": ", 4));
        jPanel3.add(BorderLayout.CENTER, this.f9086l);
        jPanel2.add(jPanel3);
        jPanel.add("South", jPanel2);
        f();
        this.f9085k = new bB(this);
        Iterator it = bIVar.a().iterator();
        while (it.hasNext()) {
            try {
                C0126i.a(this.f9075a, ((G.bJ) it.next()).a(), this.f9085k);
            } catch (V.a e2) {
                com.efiAnalytics.ui.bV.d(e2.getLocalizedMessage(), this);
                Logger.getLogger(bz.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
            }
        }
    }

    private JMenu c() {
        JMenu jMenu = new JMenu(C1818g.b("File"));
        JMenuItem jMenuItem = new JMenuItem(C1818g.b("Load Wheel Pattern"));
        jMenuItem.addActionListener(new bC(this));
        jMenu.add(jMenuItem);
        JMenuItem jMenuItem2 = new JMenuItem(C1818g.b("Save Wheel Pattern"));
        jMenuItem2.addActionListener(new bD(this));
        jMenu.add(jMenuItem2);
        return jMenu;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void d() {
        String strA = com.efiAnalytics.ui.bV.a(this, "Save Wheel Pattern", new String[]{"wheel"}, "", C1807j.e().getAbsolutePath());
        if (strA == null || strA.isEmpty()) {
            return;
        }
        if (!strA.toLowerCase().endsWith(".wheel")) {
            strA = strA + ".wheel";
        }
        try {
            bG.F f2 = new bG.F();
            File file = new File(strA);
            C1807j.b(file);
            f2.a(file, this.f9076b.e().a());
        } catch (IOException e2) {
            com.efiAnalytics.ui.bV.d(C1818g.b("Unable to save wheel pattern.") + "\n" + e2.getLocalizedMessage(), this);
            Logger.getLogger(bz.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void e() {
        String strB = com.efiAnalytics.ui.bV.b(this, "Load Wheel Pattern", new String[]{"wheel"}, "", C1807j.e().getAbsolutePath());
        if (strB == null || strB.isEmpty() || !strB.toLowerCase().endsWith(".wheel")) {
            return;
        }
        try {
            bG.F f2 = new bG.F();
            File file = new File(strB);
            C1807j.b(file);
            List listA = f2.a(file);
            if (this.f9076b.e() instanceof bG.i) {
                ((bG.i) this.f9076b.e()).a(listA);
            }
        } catch (IOException e2) {
            com.efiAnalytics.ui.bV.d(C1818g.b("Unable to load wheel pattern.") + "\n" + e2.getLocalizedMessage(), this);
            Logger.getLogger(bz.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void f() throws IllegalArgumentException {
        Iterator it = this.f9084j.a().iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            G.bJ bJVar = (G.bJ) it.next();
            boolean z2 = false;
            try {
                z2 = bJVar.a() == null || bJVar.a().isEmpty() || C1007o.a(bJVar.a(), this.f9075a);
            } catch (V.g e2) {
                Logger.getLogger(bz.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
            }
            if (z2) {
                a(bJVar.b());
                break;
            }
        }
        g();
    }

    public void a(G.bK bKVar) {
        this.f9077c = bKVar;
        G.aR.a().a(this);
        if (bKVar instanceof P.b) {
            a((P.b) bKVar);
        } else if (bKVar instanceof P.a) {
            a((P.a) bKVar);
        } else {
            if (bKVar instanceof P.c) {
            }
        }
    }

    public void a(P.a aVar) {
        if (this.f9082h != null) {
            this.f9082h.c();
        }
        try {
            this.f9082h = new C0988c(this.f9075a, aVar, this.f9076b);
        } catch (V.g e2) {
            com.efiAnalytics.ui.bV.d(C1818g.b("Failed to set Wheel pattern") + "\n" + e2.getMessage(), this);
            Logger.getLogger(bz.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
        }
        if (aVar.d() != null) {
            this.f9078d = this.f9075a.c(aVar.d());
            try {
                G.aR.a().a(this.f9075a.c(), this.f9078d.aJ(), this);
            } catch (Exception e3) {
                bH.C.a("Failed to subscribe to parameter: " + aVar.d());
                Logger.getLogger(bz.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e3);
            }
        }
        if (aVar.c() != null) {
            this.f9081g = this.f9075a.c(aVar.c());
            try {
                G.aR.a().a(this.f9075a.c(), this.f9081g.aJ(), this);
            } catch (Exception e4) {
                bH.C.a("Failed to subscribe to parameter: " + aVar.c());
                Logger.getLogger(bz.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e4);
            }
        }
    }

    public void a(P.b bVar) {
        this.f9078d = this.f9075a.c(bVar.b());
        this.f9079e = this.f9075a.c(bVar.c());
        this.f9080f = this.f9075a.c(bVar.d());
        this.f9081g = this.f9075a.c(bVar.e());
        try {
            G.aR.a().a(this.f9075a.c(), this.f9078d.aJ(), this);
        } catch (Exception e2) {
            bH.C.a("Failed to subscribe to parameter: " + bVar.b());
            Logger.getLogger(bz.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
        }
        try {
            G.aR.a().a(this.f9075a.c(), this.f9079e.aJ(), this);
        } catch (Exception e3) {
            bH.C.a("Failed to subscribe to parameter: " + bVar.c());
            Logger.getLogger(bz.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e3);
        }
        try {
            G.aR.a().a(this.f9075a.c(), this.f9080f.aJ(), this);
        } catch (Exception e4) {
            bH.C.a("Failed to subscribe to parameter: " + bVar.d());
            Logger.getLogger(bz.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e4);
        }
        try {
            G.aR.a().a(this.f9075a.c(), this.f9081g.aJ(), this);
        } catch (Exception e5) {
            bH.C.a("Failed to subscribe to parameter: " + bVar.e());
            Logger.getLogger(bz.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e5);
        }
    }

    @Override // com.efiAnalytics.ui.InterfaceC1565bc
    public void close() {
        G.aR.a().a(this);
        G.aR.a().a(this.f9085k);
    }

    @Override // G.aN
    public void a(String str, String str2) throws IllegalArgumentException {
        if (this.f9078d != null && str2.equals(this.f9078d.aJ())) {
            h();
        }
        if (this.f9079e != null && str2.equals(this.f9079e.aJ())) {
            i();
        }
        if (this.f9080f != null && str2.equals(this.f9080f.aJ())) {
            i();
        }
        if (this.f9081g == null || !str2.equals(this.f9081g.aJ())) {
            return;
        }
        j();
    }

    private void g() throws IllegalArgumentException {
        h();
        i();
        j();
    }

    private void h() throws IllegalArgumentException {
        if (this.f9078d != null) {
            try {
                this.f9076b.a(this.f9078d.j(this.f9075a.h()));
                this.f9076b.repaint();
                this.f9086l.setText(this.f9076b.b() + "");
            } catch (V.g e2) {
                Logger.getLogger(bz.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
                bH.C.a("Failed to get Offset Angle", e2, this);
            }
        }
    }

    private void i() throws IllegalArgumentException {
        if (this.f9079e == null || this.f9080f == null) {
            return;
        }
        try {
            int iRound = (int) Math.round(this.f9079e.j(this.f9075a.h()));
            int iRound2 = (int) Math.round(this.f9080f.j(this.f9075a.h()));
            if (this.f9083i instanceof C0986a) {
                ((C0986a) this.f9083i).a(iRound, iRound2);
                this.f9076b.a(this.f9083i);
                this.f9086l.setText(this.f9076b.b() + "");
                this.f9076b.repaint();
            }
        } catch (V.g e2) {
            Logger.getLogger(bz.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
            bH.C.a("Failed to set Tooth Pattern", e2, this);
        }
    }

    private void j() throws IllegalArgumentException {
        if (this.f9081g != null) {
            try {
                if (this.f9081g != null) {
                    this.f9076b.a((int) Math.round(this.f9081g.j(this.f9075a.h())));
                    this.f9086l.setText(this.f9076b.b() + "");
                }
                this.f9076b.repaint();
            } catch (V.g e2) {
                Logger.getLogger(bz.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
                bH.C.a("Failed to set Input Capture Point on Trigger Wheel image", e2, this);
            }
        }
    }

    public void a(double d2) {
        if (this.f9078d != null) {
            if (d2 > this.f9078d.r()) {
                d2 = this.f9078d.r();
            }
            if (d2 < this.f9078d.q()) {
                d2 = this.f9078d.q();
            }
            try {
                this.f9078d.a(this.f9075a.p(), d2);
            } catch (V.g e2) {
                bH.C.a("Unable to set Offset Angle.", e2, this);
            } catch (V.j e3) {
                bH.C.a("Trigger Wheel Value out of bounds.", e3, this);
            }
        }
    }

    @Override // javax.swing.JComponent, java.awt.Component, c.InterfaceC1385d
    public void setEnabled(boolean z2) {
        this.f9076b.setEnabled(z2);
        super.setEnabled(z2);
    }

    @Override // bt.InterfaceC1349h
    public void a() {
        try {
            if (a_() != null) {
                setEnabled(C1007o.a(a_(), this.f9075a));
            }
        } catch (V.g e2) {
            Logger.getLogger(bz.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
        }
    }

    @Override // c.InterfaceC1385d
    public G.R b_() {
        return this.f9075a;
    }
}
