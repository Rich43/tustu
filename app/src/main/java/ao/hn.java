package ao;

import W.C0188n;
import com.efiAnalytics.ui.AbstractC1600ck;
import com.efiAnalytics.ui.C1705w;
import g.C1733k;
import h.C1737b;
import i.InterfaceC1741a;
import i.InterfaceC1742b;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Point;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/* loaded from: TunerStudioMS.jar:ao/hn.class */
public class hn extends JPanel implements W.B, hM, InterfaceC1741a, InterfaceC1742b {

    /* renamed from: a, reason: collision with root package name */
    hI f6110a;

    /* renamed from: b, reason: collision with root package name */
    hI f6111b;

    /* renamed from: c, reason: collision with root package name */
    hw f6114c;

    /* renamed from: d, reason: collision with root package name */
    hw f6115d;

    /* renamed from: k, reason: collision with root package name */
    JButton f6122k;

    /* renamed from: l, reason: collision with root package name */
    JButton f6123l;

    /* renamed from: m, reason: collision with root package name */
    JButton f6124m;

    /* renamed from: s, reason: collision with root package name */
    hx f6130s;

    /* renamed from: t, reason: collision with root package name */
    public static String f6131t = "VE Analyze";

    /* renamed from: u, reason: collision with root package name */
    private hF f6112u = null;

    /* renamed from: v, reason: collision with root package name */
    private File f6113v = null;

    /* renamed from: e, reason: collision with root package name */
    C0188n f6116e = null;

    /* renamed from: f, reason: collision with root package name */
    String f6117f = "";

    /* renamed from: g, reason: collision with root package name */
    String[] f6118g = {"MAP", "MAP"};

    /* renamed from: h, reason: collision with root package name */
    final int f6119h = 0;

    /* renamed from: i, reason: collision with root package name */
    final int f6120i = 1;

    /* renamed from: j, reason: collision with root package name */
    boolean f6121j = true;

    /* renamed from: n, reason: collision with root package name */
    C0737eu f6125n = null;

    /* renamed from: o, reason: collision with root package name */
    hv f6126o = new hv(this);

    /* renamed from: p, reason: collision with root package name */
    JScrollPane f6127p = new JScrollPane();

    /* renamed from: q, reason: collision with root package name */
    String f6128q = null;

    /* renamed from: r, reason: collision with root package name */
    boolean f6129r = false;

    public hn() throws NumberFormatException {
        this.f6110a = null;
        this.f6111b = null;
        this.f6114c = null;
        this.f6115d = null;
        this.f6122k = null;
        this.f6123l = null;
        this.f6124m = null;
        this.f6130s = null;
        this.f6130s = new hx(this);
        setLayout(new BorderLayout(2, 2));
        this.f6127p.setHorizontalScrollBarPolicy(31);
        this.f6127p.setVerticalScrollBarPolicy(22);
        this.f6127p.setViewportView(this.f6130s);
        add(BorderLayout.CENTER, this.f6127p);
        setName("Table Tuner");
        this.f6130s.setLayout(new BoxLayout(this.f6130s, 1));
        int iB = h.i.b("prefFontSize", com.efiAnalytics.ui.eJ.a(10));
        hy hyVar = new hy(this);
        this.f6110a = new hI();
        this.f6110a.a(hyVar);
        this.f6110a.h().a(new com.efiAnalytics.ui.dQ(h.i.f(), "tuningTable1"));
        this.f6110a.c(iB);
        this.f6130s.add(this.f6110a);
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new GridLayout(0, 1, com.efiAnalytics.ui.eJ.a(5), 0));
        JPanel jPanel2 = new JPanel();
        jPanel2.setLayout(new GridLayout(1, 0, com.efiAnalytics.ui.eJ.a(2), com.efiAnalytics.ui.eJ.a(3)));
        JButton jButton = new JButton("Open Tune");
        jButton.setToolTipText("Open Tune File");
        jButton.addActionListener(new ho(this));
        jPanel2.add(jButton);
        this.f6123l = new JButton("Save Tune");
        this.f6123l.setToolTipText("Save Tune File Changes");
        this.f6123l.addActionListener(new hp(this));
        jPanel2.add(this.f6123l);
        this.f6114c = new hw(this);
        this.f6114c.addItemListener(new hq(this));
        jPanel2.add(this.f6114c);
        jPanel.add(jPanel2);
        JPanel jPanel3 = new JPanel();
        jPanel3.setLayout(new GridLayout(1, 0, com.efiAnalytics.ui.eJ.a(2), com.efiAnalytics.ui.eJ.a(3)));
        this.f6122k = new JButton(f6131t);
        this.f6122k.addActionListener(new hr(this));
        jPanel3.add(this.f6122k);
        this.f6122k.setEnabled(C1737b.a().a("veAnalyze"));
        this.f6124m = new JButton("Save Tune As");
        this.f6124m.setToolTipText("Save Tune to a new file");
        this.f6124m.addActionListener(new hs(this));
        jPanel3.add(this.f6124m);
        this.f6115d = new hw(this);
        this.f6115d.addItemListener(new ht(this));
        jPanel3.add(this.f6115d);
        jPanel.add(jPanel3);
        this.f6130s.add(jPanel);
        this.f6111b = new hI();
        this.f6111b.a(hyVar);
        this.f6111b.h().a(new com.efiAnalytics.ui.dQ(h.i.f(), "tuningTable2"));
        this.f6111b.c(iB);
        this.f6130s.add(this.f6111b);
        JPanel jPanel4 = new JPanel();
        jPanel4.setLayout(new GridLayout(1, 1));
        jPanel4.add(this.f6126o);
        this.f6130s.add(jPanel4);
        if (0 == 0) {
            JPanel jPanel5 = new JPanel();
            jPanel5.setMinimumSize(new Dimension(com.efiAnalytics.ui.eJ.a(150), com.efiAnalytics.ui.eJ.a(200)));
            jPanel5.setPreferredSize(new Dimension(com.efiAnalytics.ui.eJ.a(150), com.efiAnalytics.ui.eJ.a(300)));
        } else {
            C0764fu c0764fu = new C0764fu();
            add(BorderLayout.CENTER, c0764fu);
            c0764fu.a(new com.efiAnalytics.ui.dQ(h.i.f12258e, "mainViewScatter1_"));
        }
        b(false);
    }

    public void a(Component component) {
        this.f6130s.remove(this.f6126o);
        this.f6130s.add(component);
        this.f6130s.add(this.f6126o);
    }

    public void a(boolean z2) {
        this.f6121j = z2;
        if (z2) {
            this.f6130s.remove(this.f6126o);
            this.f6130s.add(this.f6111b);
            this.f6130s.add(this.f6126o);
        } else {
            this.f6130s.remove(this.f6111b);
        }
        b(isVisible());
    }

    public JPanel c() {
        return this.f6130s;
    }

    public void b(boolean z2) {
        this.f6110a.setVisible(z2);
        this.f6114c.setVisible(z2);
        this.f6122k.setVisible(z2);
        this.f6126o.setVisible(z2);
        this.f6124m.setVisible(z2);
        this.f6123l.setVisible(z2);
        if (this.f6121j) {
            this.f6111b.setVisible(z2);
            this.f6115d.setVisible(z2);
        } else {
            this.f6111b.setVisible(false);
            this.f6115d.setVisible(false);
        }
    }

    public void d() {
        String strE = h.i.e("lastVeFile", "");
        if (strE.equals("") || !new File(strE).exists()) {
            return;
        }
        try {
            b(strE);
        } catch (Exception e2) {
            Logger.getLogger(hn.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
            com.efiAnalytics.ui.bV.d("Unable to open file:\n" + strE + "\n \nError Message:\n" + e2.getMessage(), this);
        }
    }

    private String n() {
        String strE = h.i.e("lastVeFile", "");
        if (strE.length() != 0) {
            return strE.indexOf(File.separator) != -1 ? strE.substring(0, strE.lastIndexOf(File.separator)) : strE;
        }
        File file = new File(h.i.e("lastFileDir", System.getProperty("user.home") + File.separator));
        if (file.exists() && file.getName().equals("DataLogs")) {
            file = file.getParentFile();
        }
        return file.getAbsolutePath();
    }

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    public Dimension getMinimumSize() {
        Dimension minimumSize = super.getMinimumSize();
        minimumSize.height = 200;
        return minimumSize;
    }

    public void a(int i2, String str) {
        hF hFVarM;
        if (str == null || (hFVarM = m()) == null) {
            return;
        }
        hI hIVar = i2 == 1 ? this.f6111b : this.f6110a;
        hH hHVarB = hFVarM.b(str);
        if (hHVarB == null) {
            return;
        }
        int iA = h.i.a("prefFontSize", com.efiAnalytics.ui.eJ.a(10));
        hIVar.a(hHVarB);
        hIVar.f().setToolTipText(hHVarB.v());
        hIVar.a("<html>" + str + "<br>" + this.f6117f + "</html>");
        hIVar.b(str);
        if (hFVarM.k(str)) {
            hIVar.b(true);
        }
        invalidate();
        hIVar.k();
        hIVar.c(iA);
        hIVar.setName(str);
        if (getParent() != null) {
            getParent().validate();
            getParent().doLayout();
        }
        doLayout();
        h.i.c(i2 == 1 ? "lastTable2" : "lastTable1", str);
    }

    @Override // java.awt.Component
    public void setBounds(int i2, int i3, int i4, int i5) {
        if (i5 < getMinimumSize().height) {
            super.setBounds(i2, i3, i4, getMinimumSize().height);
        } else {
            super.setBounds(i2, i3, i4, i5);
        }
    }

    @Override // java.awt.Component
    public void setSize(int i2, int i3) {
        if (i3 < getMinimumSize().height) {
            super.setSize(i2, getMinimumSize().height);
        } else {
            super.setSize(i2, i3);
        }
    }

    public void a(hF hFVar, hw hwVar) {
        hwVar.removeAll();
        if (hFVar != null) {
            hz.a(hFVar, hwVar, (String) null);
        }
    }

    public void e() {
        if (g()) {
            String[] strArrA = com.efiAnalytics.ui.bV.a((Component) this, "Open Tune settings file", o(), (String) null, n(), true, (AbstractC1600ck) null, true);
            if (strArrA == null || strArrA.length <= 0) {
                return;
            }
            this.f6117f = strArrA[0];
            try {
                b(this.f6117f);
            } catch (V.a e2) {
                Logger.getLogger(hn.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
                com.efiAnalytics.ui.bV.d("Unable to open file:\n" + this.f6117f + "\n \nError Message:\n" + e2.getMessage(), this);
            }
        }
    }

    public void f() {
        if (g() && c(this.f6116e)) {
            String strB = this.f6114c.b();
            if (strB.toLowerCase().indexOf("ve") == -1 && strB.indexOf("Fuel") == -1 && strB.indexOf("pwATable") == -1 && strB.indexOf("pwBTable") == -1) {
                C1733k.a("Please set top table to the VE table you wish to analyze.", this);
                return;
            }
            String strA = h.g.a().a(h.g.f12245d);
            if (this.f6116e.a(strA).f() >= 100.0d || C1733k.a("RPM Very low, You probably want to select RPM x 100 from the Options Menu.\nContinue anyway?", (Component) this, true)) {
                if (this.f6116e.a(strA).f() <= 20000.0d || C1733k.a("RPM Very High, You probably want to unselect RPM x 100 from the Options Menu.\nContinue anyway?", (Component) this, true)) {
                    String strB2 = "";
                    String strSubstring = strB.substring(strB.length() - 1);
                    for (int i2 = 0; i2 < this.f6114c.c(); i2++) {
                        String lowerCase = this.f6114c.b(i2).toLowerCase();
                        if ((lowerCase.toLowerCase().startsWith("afr") || lowerCase.toLowerCase().startsWith("lambda") || lowerCase.toLowerCase().startsWith("target afr") || lowerCase.toLowerCase().startsWith("target lambda")) && this.f6114c.b(i2).indexOf(strSubstring) != -1) {
                            strB2 = this.f6114c.b(i2);
                        }
                    }
                    hH hHVarB = null;
                    try {
                        if (!h.i.e("lastVeAnalysisAfrTable", "").equals("Default AFR")) {
                            hHVarB = m().b(strB2);
                        }
                    } catch (Exception e2) {
                    }
                    String strV = this.f6110a.g().v();
                    if (strV == null || strV.equals("") || this.f6116e.a(strV) == null) {
                        this.f6110a.g().d(h.i.a("yAxisField", h.g.a().a(h.g.f12251j)));
                    }
                    this.f6125n = new C0737eu(m(), (hH) this.f6110a.g(), hHVarB, this.f6116e, C1733k.a(this), "VE Analyze");
                    Point location = C1733k.a(this).getLocation();
                    this.f6125n.setLocation(location.f12370x + 100, location.f12371y + 50);
                    this.f6125n.a(this);
                    this.f6125n.setVisible(true);
                }
            }
        }
    }

    public boolean g() {
        String property = System.getProperty("java.specification.version");
        if (property == null || property.equals("")) {
            C1733k.a("Failed to detect Java Version, 1.4 or greater required for Tuning Console. Will try to load.", this);
            return true;
        }
        if (Double.parseDouble(property) >= 1.4d) {
            return true;
        }
        C1733k.a("Java Version " + property + " detected, \nJRE version 1.4 or greater required for Tuning Console. \nPlease upgrade JRE to use this feature. \nUpdated JRE can be found at http://www.java.com/en/download/", this);
        return false;
    }

    public void a(String str) {
        if (str != null && str.length() > 65) {
            str = str.substring(0, 10) + " .... " + str.substring(str.length() - (65 - 19), str.length());
        }
        this.f6126o.setText(str);
    }

    public boolean h() {
        return m() != null && m().b();
    }

    public void i() {
        this.f6110a.j();
        if (this.f6111b != null) {
            this.f6111b.j();
        }
    }

    @Override // W.B
    public void a(File file) {
        if (com.efiAnalytics.ui.bV.a("File " + file.getName() + " has been modified outside this application.\nReload the file now?", (Component) this, true)) {
            try {
                b(file.getAbsolutePath());
            } catch (V.a e2) {
                Logger.getLogger(hn.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
            }
        }
    }

    public void b(String str) {
        new hu(this, str, this).run();
    }

    public void j() {
        a("");
        b((hF) null);
        a((hF) null, this.f6114c);
        a((hF) null, this.f6115d);
        b(true);
        h.i.c("lastVeFile", "");
        if (this.f6125n != null && this.f6125n.isVisible()) {
            this.f6125n.setVisible(false);
            this.f6125n.dispose();
        }
        if (this.f6113v != null) {
            W.C.a().e(this.f6113v);
            this.f6113v = null;
        }
    }

    public void a(hF hFVar) {
        a(this.f6117f);
        b(hFVar);
        a(m(), this.f6114c);
        a(m(), this.f6115d);
        b(true);
        if (this.f6114c.c() > 0) {
            if (!this.f6114c.b(h.i.a("lastTable1", "veBins1")) && !this.f6114c.b("veTable1") && !this.f6114c.b("veBins1") && !this.f6114c.b("fuelTable")) {
                this.f6114c.a(0);
            }
            a(0, this.f6114c.b(this.f6114c.d()));
        }
        if (this.f6115d.c() > 1) {
            if (!this.f6115d.b(h.i.e("lastTable2", "advTable1")) && !this.f6115d.b("advanceTable") && !this.f6115d.b("advTable1") && !this.f6115d.b("afrTable1") && !this.f6115d.b("o2Table")) {
                this.f6115d.a(1);
            }
            a(1, this.f6115d.b(this.f6115d.d()));
        }
        h.i.c("lastVeFile", this.f6117f);
        if (this.f6125n != null && this.f6125n.isVisible()) {
            this.f6125n.setVisible(false);
            this.f6125n.dispose();
        }
        b(C0804hg.a().p());
        i();
        hFVar.a();
    }

    private String[] o() {
        return h.i.a("tuneFileExtensions", "msq;tune").split(";");
    }

    private boolean c(String str) {
        for (String str2 : o()) {
            if (str.toLowerCase().indexOf("." + str2) != -1) {
                return true;
            }
        }
        return false;
    }

    public void k() {
        String[] strArrO = o();
        String str = this.f6117f;
        String strA = C1733k.a((Component) this, "Save Tune Configuration file", strArrO, this.f6117f, false);
        if (strA == null || strA.equals("")) {
            return;
        }
        if (!c(strA)) {
            strA = (this.f6128q == null || this.f6128q.equals("")) ? strA + "." + strArrO[0] : strA + "." + this.f6128q;
        }
        this.f6117f = strA;
        if (m() == null) {
            C1733k.a("Unable to save this " + strArrO[0] + " file.\nIt is most likely corrupt.\nTry saving a new " + strArrO[0] + " from a supported tuning application.\nIf this problem persists please report it.", this);
            return;
        }
        try {
            m().j(this.f6117f);
            a(this.f6117f);
            a(0, this.f6114c.b(this.f6114c.d()));
            if (this.f6115d != null && this.f6115d.isVisible()) {
                a(1, this.f6115d.b(this.f6115d.d()));
            }
            h.i.c("lastVeFile", this.f6117f);
            com.efiAnalytics.ui.cS.a().a(this);
        } catch (V.h e2) {
            System.out.println("Error Saving File.");
            e2.printStackTrace();
            C1733k.a("Error saving msq file.\nSee log file for details.", this);
        }
    }

    public void l() {
        if (m() == null) {
            C1733k.a("No Tune Settings loaded.\nPlease open an valid Tune Settings file.", this);
            return;
        }
        try {
            W.C.a().c(this.f6113v);
            m().j(this.f6117f);
            W.C.a().d(this.f6113v);
            m().a();
            a(0, this.f6114c.b(this.f6114c.d()));
            if (this.f6115d != null && this.f6115d.isVisible()) {
                a(1, this.f6115d.b(this.f6115d.d()));
            }
            com.efiAnalytics.ui.cS.a().a(this);
        } catch (V.h e2) {
            System.out.println("Error Saving File.");
            e2.printStackTrace();
            C1733k.a("Error saving Tune Settings file.\nSee log file for details.", this);
        }
    }

    public void b(int i2) {
        if (this.f6116e == null || !isVisible() || this.f6110a == null || !this.f6110a.isVisible()) {
            return;
        }
        String string = "";
        String string2 = "";
        try {
            if (this.f6116e.a(this.f6110a.g().v()) == null && !this.f6129r) {
                String strA = h.i.a("yAxisField", "");
                if (strA.equals("") || strA.equals(this.f6110a.g().v())) {
                    C1733k.a("The Y Axis fields \"" + this.f6110a.g().v() + "\" not found in current data log.\nSet the correct Y axis field from \"Options\" Menu for table highlighting\nand VE Analysis to work correctly.", this);
                    this.f6129r = true;
                } else {
                    Iterator itC = m().c();
                    while (itC.hasNext()) {
                        hH hHVarB = m().b((String) itC.next());
                        if (this.f6116e.a(hHVarB.v()) == null) {
                            hHVarB.d(strA);
                        }
                    }
                }
            }
            String strA2 = h.g.a().a(h.g.f12245d);
            String strV = this.f6110a.g().v();
            if (this.f6116e.a(strV) == null) {
                strV = h.i.a("yAxisField", "");
            }
            String strV2 = this.f6111b.g().v();
            if (this.f6116e.a(strV2) == null) {
                strV2 = h.i.a("yAxisField", "MAP");
            }
            if (this.f6116e.a(strV2) == null) {
                strV2 = h.i.a("yAxisField", "Load");
            }
            if (this.f6116e.a(strA2) != null && this.f6116e.a(strV) != null) {
                string2 = Float.toString(this.f6116e.a(strA2).d(i2));
                this.f6110a.h().a(Float.toString(this.f6116e.a(strV).d(i2)), string2);
                int iJ = this.f6110a.h().J();
                String[] strArr = new String[iJ];
                String[] strArr2 = new String[iJ];
                for (int i3 = 0; i3 < iJ && i2 - i3 > 0; i3++) {
                    strArr[i3] = Float.toString(this.f6116e.a(strV).d(i2 - i3));
                    strArr2[i3] = Float.toString(this.f6116e.a(strA2).d(i2 - i3));
                }
                this.f6110a.h().a(strArr2, strArr);
                this.f6110a.h().repaint();
            }
            if (this.f6116e.a(strA2) != null && this.f6116e.a(strV2) != null) {
                string = Float.toString(this.f6116e.a(strV2).d(i2));
                this.f6111b.h().a(string, string2);
                int iJ2 = this.f6111b.h().J();
                String[] strArr3 = new String[iJ2];
                String[] strArr4 = new String[iJ2];
                for (int i4 = 0; i4 < iJ2 && i2 - i4 > 0; i4++) {
                    strArr3[i4] = Float.toString(this.f6116e.a(strV2).d(i2 - i4));
                    strArr4[i4] = Float.toString(this.f6116e.a(strA2).d(i2 - i4));
                }
                this.f6111b.h().a(strArr4, strArr3);
                this.f6111b.h().repaint();
            }
        } catch (Exception e2) {
            System.out.println("yVal=" + string + ", rpm=" + string2 + ", index=" + i2);
            e2.printStackTrace();
        }
    }

    @Override // ao.hM
    public void a(String str, hH hHVar) {
        hH hHVarB = m().b(str);
        for (int i2 = 0; i2 < hHVar.getRowCount(); i2++) {
            for (int i3 = 0; i3 < hHVar.getColumnCount(); i3++) {
                hHVarB.setValueAt(hHVar.getValueAt(i2, i3), i2, i3);
            }
        }
        hHVarB.C();
        this.f6110a.h().repaint();
        this.f6111b.h().repaint();
    }

    @Override // i.InterfaceC1742b
    public void a(double d2) {
    }

    @Override // i.InterfaceC1742b
    public void a(C0188n c0188n) {
        this.f6116e = c0188n;
        if (m() != null) {
            m().e();
        }
        if (this.f6125n == null || !this.f6125n.isShowing()) {
            return;
        }
        this.f6125n.b(false);
    }

    @Override // i.InterfaceC1742b
    public void a() {
    }

    @Override // i.InterfaceC1742b
    public void b(C0188n c0188n) {
    }

    @Override // i.InterfaceC1741a
    public void a(int i2) {
        if (this.f6125n != null && this.f6125n.isVisible()) {
            this.f6125n.a(i2);
        }
        if (isVisible()) {
            b(i2);
        }
    }

    @Override // i.InterfaceC1742b
    public void b() {
    }

    void c(int i2) {
        if (this.f6110a != null && this.f6114c.c() > 0) {
            this.f6110a.c(i2);
            String strB = this.f6114c.b();
            a(0, this.f6114c.b((this.f6114c.d() + 1) % this.f6114c.c()));
            a(0, strB);
        }
        if (this.f6111b == null || this.f6115d.c() <= 0) {
            return;
        }
        this.f6111b.c(i2);
        String strB2 = this.f6115d.b();
        a(1, this.f6115d.b((this.f6115d.d() + 1) % this.f6115d.c()));
        a(1, strB2);
    }

    private boolean c(C0188n c0188n) {
        String strA = h.g.a().a(h.g.f12244c);
        String strA2 = h.g.a().a(h.g.f12249h);
        String strA3 = h.g.a().a(h.g.f12250i);
        String strA4 = h.g.a().a(h.g.f12243b);
        String strA5 = h.g.a().a(h.g.f12245d);
        String strA6 = h.g.a().a(h.g.f12246e);
        String strV = this.f6110a.g().v();
        if (strV == null || strV.equals("") || c0188n.a(strV) == null) {
            strV = h.i.a("yAxisField", h.g.a().a(h.g.f12251j));
        }
        if (strA5 == null || c0188n.a(strA5) == null || strV == null || c0188n.a(strV) == null || ((strA2 == null || c0188n.a(strA2) == null) && ((strA4 == null || c0188n.a(strA4) == null) && (strA3 == null || c0188n.a(strA3) == null)))) {
            String str = "The following fields are required for VE Analyze to work best:\n" + strA5 + "\n" + strA2 + " or " + strA3 + "\n" + strV + "\n\nAlso to improve results please log these additional fields:\n" + strA6 + "\n" + strA + "\n";
            if (h.i.f12256c.startsWith(C1737b.f12225e)) {
                str = str + "dMAP_Corr";
            }
            com.efiAnalytics.ui.bV.d(str, this);
            return false;
        }
        if ((strA6 != null && c0188n.a(strA6) != null) || strV.equals("PW In1")) {
            return true;
        }
        com.efiAnalytics.ui.bV.d("The field used to determine the amount of EGO correction the ECU is applying \"" + strA6 + "\"\nWas not found in the currently loaded Log File. If you run VE Analyze it will assume\nthere is no EGO correction being applied by the ECU.\n\nTo correct this, either load a log file with the field \"" + strA6 + "\" \nOR\nCheck to make sure the correct \"Field Naming (ECU)\" option is selected \nunder the Options Menu. It may be another field name that is appropriate for your ECU", this);
        return true;
    }

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    public Dimension getPreferredSize() {
        return (!h.i.e(h.i.f12293N, h.i.f12297R).equals(h.i.f12295P) || this.f6110a == null) ? super.getPreferredSize() : this.f6110a.getPreferredSize();
    }

    private List a(List list, Container container) {
        for (Component component : container.getComponents()) {
            if (component instanceof C1705w) {
                list.add((C1705w) component);
            } else if (component instanceof Container) {
                a(list, (Container) component);
            }
        }
        return list;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public List p() {
        return a(new ArrayList(), this.f6130s);
    }

    public hF m() {
        return this.f6112u;
    }

    public void b(hF hFVar) {
        this.f6112u = hFVar;
    }
}
