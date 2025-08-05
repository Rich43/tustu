package br;

import G.C0079bl;
import G.InterfaceC0042ab;
import G.bL;
import G.dc;
import G.dk;
import G.dn;
import aP.gW;
import ai.C0516f;
import bs.C1265C;
import c.InterfaceC1386e;
import com.efiAnalytics.ui.InterfaceC1565bc;
import com.efiAnalytics.ui.bV;
import com.efiAnalytics.ui.cO;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.RenderingHints;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import n.InterfaceC1761a;
import r.C1798a;
import r.C1806i;
import s.C1818g;

/* renamed from: br.K, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:br/K.class */
public class C1233K extends n.n implements InterfaceC0042ab, aE.e, InterfaceC1761a {

    /* renamed from: a, reason: collision with root package name */
    G.R f8361a = null;

    /* renamed from: b, reason: collision with root package name */
    ArrayList f8362b = new ArrayList();

    /* renamed from: c, reason: collision with root package name */
    ArrayList f8363c = new ArrayList();

    /* renamed from: d, reason: collision with root package name */
    C0516f f8364d = null;

    /* renamed from: f, reason: collision with root package name */
    private Image f8365f = null;

    /* renamed from: e, reason: collision with root package name */
    O f8366e = null;

    public C1233K() {
        addChangeListener(new C1235M(this));
        setDoubleBuffered(false);
    }

    public void a(G.R r2) {
        k();
        removeAll();
        this.f8362b.clear();
        if (r2 == null) {
            return;
        }
        try {
            ArrayList arrayListA = C1232J.a().a(r2);
            if (arrayListA.isEmpty()) {
                return;
            }
            this.f8362b.addAll(arrayListA);
            int i2 = 0;
            aE.a aVarA = aE.a.A();
            Iterator it = arrayListA.iterator();
            while (it.hasNext()) {
                String str = (String) it.next();
                try {
                    dk dkVarA = C1232J.a().a(r2, str);
                    String strB = C1818g.b(bL.c(r2, str));
                    String strA = bL.a(r2, str);
                    C1245i c1245i = new C1245i(r2, dkVarA);
                    c1245i.a(aVarA);
                    this.f8363c.add(c1245i);
                    JPanel jPanel = new JPanel();
                    jPanel.setLayout(new GridLayout(1, 1));
                    jPanel.add(c1245i);
                    addTab(strB, jPanel);
                    if (strA != null && !strA.equals("")) {
                        a(i2 + "", new C1236N(this, r2, strA));
                    } else if (strA == null && dkVarA.d() != null && !dkVarA.d().isEmpty()) {
                        a(i2 + "", new C1236N(this, r2, dkVarA.d()));
                    }
                    i2++;
                } catch (Exception e2) {
                    bH.C.a("Definition Error! Unable to create VE Analyze for table name: " + str, e2, this);
                }
            }
            boolean zA = C1806i.a().a("skj98hg9843lkfd");
            try {
                ArrayList arrayListB = C1232J.a().b(r2);
                Iterator it2 = arrayListB.iterator();
                while (it2.hasNext()) {
                    String str2 = (String) it2.next();
                    try {
                        dc dcVarB = C1232J.a().b(r2, str2);
                        String strB2 = C1818g.b("Trim Table Analyze");
                        String strD = dcVarB.d();
                        C1245i c1245i2 = new C1245i(r2, dcVarB);
                        c1245i2.a(aVarA);
                        this.f8363c.add(c1245i2);
                        JPanel jPanel2 = new JPanel();
                        jPanel2.setLayout(new GridLayout(1, 1));
                        jPanel2.add(c1245i2);
                        addTab(strB2, jPanel2);
                        if (strD != null && !strD.equals("")) {
                            a(i2 + "", zA ? new C1236N(this, r2, strD) : new C1236N(this, r2, "0"));
                        }
                        i2++;
                    } catch (Exception e3) {
                        bH.C.a("Definition Error! Unable to create VE Analyze for table name: " + str2, e3, this);
                    }
                }
                this.f8362b.addAll(arrayListB);
                try {
                    ArrayList arrayListA2 = C1265C.a().a(r2);
                    Iterator it3 = arrayListA2.iterator();
                    while (it3.hasNext()) {
                        String str3 = (String) it3.next();
                        try {
                            dn dnVarA = C1265C.a().a(r2, str3);
                            String strB3 = C1818g.b(((C0079bl) r2.e().c(str3)).M());
                            String strA2 = bL.a(r2, str3);
                            bs.f fVar = new bs.f(r2, dnVarA);
                            fVar.a(aVarA);
                            this.f8363c.add(fVar);
                            JPanel jPanel3 = new JPanel();
                            jPanel3.setLayout(new GridLayout(1, 1));
                            jPanel3.add(fVar);
                            addTab(strB3, jPanel3);
                            if (strA2 != null && !strA2.equals("")) {
                                a(i2 + "", new C1236N(this, r2, strA2));
                            }
                            i2++;
                        } catch (Exception e4) {
                            bH.C.a("Unable to get WUE Analyze map for Curve name: " + str3, e4, this);
                        }
                    }
                    this.f8362b.addAll(arrayListA2);
                    e();
                    d();
                } catch (V.g e5) {
                    bH.C.a("Unable to get Wue Analyze Supported Curves.", e5, this);
                }
            } catch (V.g e6) {
                bH.C.a("Unable to get VE Analyze Supported tables.", e6, this);
            }
        } catch (V.g e7) {
            bH.C.a("Unable to get VE Analyze Supported tables.", e7, this);
        }
    }

    private void k() {
        Iterator it = this.f8363c.iterator();
        while (it.hasNext()) {
            try {
                ((InterfaceC1565bc) it.next()).close();
            } catch (Exception e2) {
                bH.C.b("Exception caught calling close listeners in VeAnalyzeTabs, caught and continuing.");
                e2.printStackTrace();
            }
        }
        C1242f.a().b();
    }

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    public void paint(Graphics graphics) {
        try {
            super.paint(graphics);
        } catch (Exception e2) {
            bH.C.b("Exception Caught in VeAnalyTabs painting, this was handled. it is just here for informational purposes.");
            e2.printStackTrace();
        }
        if (getComponentCount() == 0 && this.f8366e == null) {
            g();
        } else if (this.f8366e != null && getComponentCount() > 0) {
            h();
        }
        if (this.f8366e == null || !this.f8366e.f8372a) {
            return;
        }
        Graphics2D graphics2D = (Graphics2D) graphics;
        graphics2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        Image imageF = f();
        if (imageF != null) {
            int width = imageF.getWidth(null) / 2;
            int height = imageF.getHeight(null) / 2;
            graphics.drawImage(imageF, (getWidth() - width) / 2, (getHeight() - height) / 2, width, height, null);
        }
    }

    private void l() {
        if (this.f8364d == null) {
            this.f8364d = new C0516f();
            bV.d("The unregistered version of " + C1798a.f13268b + " does not \noffer " + gW.f3465p + "! Upgrade now to activate\nand watch " + C1798a.f13268b + " tune for you!\n\nSupporting all fueling models!\n\nWhether you are starting with a rough table or\nDialing in a table to perfection, Tune Analyze Live\nprovides you with the best tune by using your vehicles data.", this);
            addTab(C1818g.b("Upgrade Today!! For " + gW.f3465p + " and a whole lot more!"), this.f8364d);
        }
        try {
            this.f8364d.b("file:///" + new File(".").getAbsolutePath() + "/help/learnMore.html");
        } catch (V.a e2) {
            Logger.getLogger(C1233K.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
        }
    }

    @Override // n.InterfaceC1761a
    public boolean a() {
        if (!C1806i.a().a("-0ofdspok54sg")) {
            l();
            return true;
        }
        if (!this.f8362b.isEmpty()) {
            return true;
        }
        repaint();
        C1234L c1234l = new C1234L(this);
        if (SwingUtilities.isEventDispatchThread()) {
            c1234l.run();
        } else {
            try {
                SwingUtilities.invokeAndWait(c1234l);
            } catch (InterruptedException e2) {
                Logger.getLogger(C1233K.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
            } catch (InvocationTargetException e3) {
                Logger.getLogger(C1233K.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e3);
            }
        }
        C1798a.a().a("displayedVeAnalyzeWarning", false);
        return true;
    }

    public int c() {
        if (this.f8362b.isEmpty()) {
            a();
        }
        if (this.f8362b.isEmpty()) {
            return -1;
        }
        return this.f8362b.size() - 1;
    }

    @Override // aE.e
    public void a(aE.a aVar, G.R r2) {
        this.f8361a = r2;
        r2.h().a(this);
    }

    @Override // aE.e
    public void e_() {
        try {
            k();
            this.f8362b.clear();
            if (this.f8361a != null) {
                this.f8361a.h().b(this);
                this.f8361a = null;
            }
            this.f8363c.clear();
            removeAll();
            aS.l.a().b();
        } catch (Exception e2) {
        }
    }

    @Override // G.InterfaceC0042ab
    public void a(String str, int i2, int i3, int[] iArr) {
        d();
    }

    @Override // com.efiAnalytics.ui.eK
    public void d() {
        boolean zA;
        for (int i2 = 0; i2 < getTabCount(); i2++) {
            InterfaceC1386e interfaceC1386e = (InterfaceC1386e) i().get(i2 + "");
            if (interfaceC1386e != null && isEnabledAt(i2) != (zA = interfaceC1386e.a())) {
                setEnabledAt(i2, zA);
            }
        }
    }

    public void e() {
        for (int i2 = 0; i2 < getTabCount(); i2++) {
            InterfaceC1386e interfaceC1386e = (InterfaceC1386e) i().get(i2 + "");
            if (interfaceC1386e != null && interfaceC1386e.a()) {
                setSelectedIndex(i2);
                return;
            }
        }
    }

    @Override // aE.e
    public void a(aE.a aVar) {
    }

    protected Image f() {
        if (this.f8365f == null) {
            try {
                this.f8365f = cO.a().a(cO.f11107w);
            } catch (V.a e2) {
                Logger.getLogger(com.efiAnalytics.apps.ts.tuningViews.J.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
            }
        }
        return this.f8365f;
    }

    public void g() {
        h();
        this.f8366e = new O(this);
        this.f8366e.start();
    }

    public void h() {
        if (this.f8366e != null) {
            this.f8366e.a();
            this.f8366e = null;
        }
    }
}
