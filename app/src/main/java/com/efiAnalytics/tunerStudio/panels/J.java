package com.efiAnalytics.tunerStudio.panels;

import G.C0050aj;
import G.C0068ba;
import G.C0087bt;
import G.C0130m;
import G.C0132o;
import G.aX;
import G.aY;
import G.bE;
import G.bF;
import G.da;
import aP.C0338f;
import bH.C0995c;
import bH.C0996d;
import bt.C1345d;
import com.efiAnalytics.tuningwidgets.panels.aL;
import com.efiAnalytics.tuningwidgets.panels.aM;
import com.efiAnalytics.ui.InterfaceC1565bc;
import com.efiAnalytics.ui.bV;
import com.efiAnalytics.ui.dM;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import jdk.internal.dynalink.CallSiteDescriptor;
import org.icepdf.core.util.PdfOps;
import r.C1798a;
import s.C1818g;
import sun.util.locale.LanguageTag;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/tunerStudio/panels/J.class */
public class J extends C1345d implements InterfaceC1565bc {

    /* renamed from: a, reason: collision with root package name */
    G.R f9945a;

    /* renamed from: b, reason: collision with root package name */
    aX f9946b;

    /* renamed from: e, reason: collision with root package name */
    JButton f9949e;

    /* renamed from: m, reason: collision with root package name */
    boolean f9957m;

    /* renamed from: c, reason: collision with root package name */
    JComboBox f9947c = new JComboBox();

    /* renamed from: d, reason: collision with root package name */
    JComboBox f9948d = new JComboBox();

    /* renamed from: f, reason: collision with root package name */
    HashMap f9950f = new HashMap();

    /* renamed from: g, reason: collision with root package name */
    JLabel f9951g = new JLabel("<html>" + C1818g.b("Select settings, click") + " <br>\"" + C1818g.b("Write to Controller") + "\"<br> &nbsp; </html>", 0);

    /* renamed from: h, reason: collision with root package name */
    FileWriter f9952h = null;

    /* renamed from: i, reason: collision with root package name */
    dM f9953i = new dM();

    /* renamed from: j, reason: collision with root package name */
    JPanel f9954j = new JPanel();

    /* renamed from: k, reason: collision with root package name */
    C0996d f9955k = new C0996d();

    /* renamed from: l, reason: collision with root package name */
    boolean f9956l = false;

    /* renamed from: n, reason: collision with root package name */
    boolean f9958n = true;

    public J(G.R r2, aX aXVar) {
        this.f9945a = null;
        this.f9946b = null;
        this.f9949e = null;
        this.f9957m = true;
        this.f9945a = r2;
        this.f9946b = aXVar;
        a((InterfaceC1565bc) this);
        setLayout(new BorderLayout());
        if (aXVar.O().size() > 0) {
            C0050aj c0050ajB = r2.e().b((String) aXVar.O().get(0));
            if (c0050ajB == null || c0050ajB.d() == null) {
                bH.C.c(C1818g.b("unable to load help topic ") + ((String) aXVar.O().get(0)));
            } else {
                JMenuBar jMenuBar = new JMenuBar();
                JMenu jMenu = new JMenu(C1818g.b("Help"));
                jMenuBar.add(jMenu);
                JMenuItem jMenuItem = new JMenuItem(c0050ajB.d());
                jMenuItem.addActionListener(new K(this));
                jMenu.add(jMenuItem);
                add("North", jMenuBar);
            }
        }
        JPanel jPanel = new JPanel();
        jPanel.setBorder(BorderFactory.createTitledBorder(C1818g.b(aXVar.M())));
        jPanel.setLayout(new BoxLayout(jPanel, 1));
        JPanel jPanel2 = new JPanel();
        jPanel2.setLayout(new BorderLayout(2, 2));
        if (aXVar.a() > 1) {
            jPanel.add(jPanel2);
        }
        if (aXVar.a() == 0) {
            bV.d(C1818g.b("Table identifier isn't defined in the ecu configuration file") + "\n" + C1818g.b("for reference table") + ": " + aXVar.aJ(), this);
            return;
        }
        if (aXVar.a() > 0) {
            jPanel2.setBorder(BorderFactory.createTitledBorder(C1818g.b("Sensor Table")));
            this.f9947c.setEditable(false);
            jPanel2.add(BorderLayout.CENTER, this.f9947c);
            Iterator itB = aXVar.b();
            while (itB.hasNext()) {
                this.f9947c.addItem(new S(this, (aY) itB.next()));
            }
            this.f9947c.addItemListener(new L(this));
        }
        if (aXVar.j() > 0) {
            JPanel jPanel3 = new JPanel();
            jPanel3.setBorder(BorderFactory.createTitledBorder(C1818g.b("Table Input Solution")));
            jPanel3.setLayout(new BorderLayout(2, 2));
            jPanel3.add("West", new JLabel(C1818g.b(aXVar.i())));
            jPanel3.add(BorderLayout.CENTER, this.f9948d);
            if (aXVar.j() > 1) {
                jPanel.add(jPanel3);
            }
            Iterator itF = aXVar.f();
            while (itF.hasNext()) {
                this.f9948d.addItem(new R(this, (C0068ba) itF.next()));
            }
            this.f9948d.addItemListener(new M(this));
        }
        this.f9954j.setLayout(new CardLayout());
        jPanel.add(this.f9954j);
        Iterator itG = aXVar.g();
        while (itG.hasNext()) {
            bE bEVar = (bE) itG.next();
            try {
                aL aLVarA = a(bEVar);
                this.f9954j.add(aLVarA, bEVar.g());
                this.f9950f.put(bEVar.g(), aLVarA);
            } catch (V.a e2) {
                bH.C.a(e2.getMessage(), e2, this);
            }
        }
        JPanel jPanel4 = new JPanel();
        jPanel4.setLayout(new GridLayout(0, 1));
        jPanel4.add(this.f9951g);
        this.f9951g.setMinimumSize(new Dimension(320, 60));
        this.f9951g.setPreferredSize(new Dimension(320, 60));
        jPanel.add(jPanel4);
        jPanel.add(this.f9953i);
        JPanel jPanel5 = new JPanel();
        jPanel5.setLayout(new FlowLayout(2));
        this.f9949e = new JButton(C1818g.b("Write to Controller"));
        this.f9949e.addActionListener(new N(this));
        jPanel5.add(this.f9949e);
        jPanel.add(jPanel5);
        add(BorderLayout.CENTER, jPanel);
        d();
        this.f9957m = r2.O().P() == -1 && r2.O().x() == r2.O().P();
        h();
        if (this.f9958n) {
            a();
        } else {
            bH.C.b("Reference table Panel cannot set selection as not connected to ECU directly.");
        }
    }

    public final void a() {
        byte[] bArrA;
        if (!this.f9958n || (bArrA = a(e())) == null) {
            return;
        }
        bH.Z z2 = new bH.Z();
        z2.a();
        C0068ba c0068baA = a(bArrA);
        z2.b();
        bH.C.c("Found solution:" + (c0068baA == null ? "No Match" : c0068baA.a()));
        bH.C.c(" Time to find solution:" + z2.c());
        a(c0068baA);
    }

    private byte[] a(String str) {
        da daVar = new da();
        try {
            C0130m c0130mA = C0130m.a(this.f9945a.O(), C0995c.d(str), this.f9946b.c() * this.f9946b.d());
            if (c0130mA == null) {
                return null;
            }
            c0130mA.c(this.f9957m);
            C0132o c0132oA = daVar.a(this.f9945a, c0130mA, 600);
            if (c0132oA.a() == 1) {
                return C0995c.a(c0132oA.e());
            }
            this.f9958n = false;
            return null;
        } catch (V.g e2) {
            bV.d(e2.getLocalizedMessage(), this);
            Logger.getLogger(J.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
            return null;
        }
    }

    private C0068ba a(byte[] bArr) {
        for (int i2 = 0; i2 < this.f9948d.getItemCount(); i2++) {
            C0068ba c0068baA = ((R) this.f9948d.getItemAt(i2)).a();
            byte[] bArrB = b(c0068baA);
            if (bArrB != null && C0995c.b(bArr, bArrB)) {
                bH.C.c("Found solution. Solution Checksum: " + C0995c.d(bArrB) + ", controller checksum: " + C0995c.d(bArr));
                return c0068baA;
            }
            if (bArrB != null) {
                bH.C.c("Solution " + c0068baA.a() + " doesn't match. Solution Checksum: " + C0995c.d(bArrB) + ", controller checksum: " + C0995c.d(bArr));
            } else {
                bH.C.c("Can not calculate solution for: " + c0068baA.a());
            }
        }
        return null;
    }

    private byte[] b(C0068ba c0068ba) {
        try {
            int[] iArrC = c(c0068ba);
            this.f9955k.reset();
            this.f9955k.update(C0995c.a(iArrC));
            return C0995c.a((int) this.f9955k.getValue(), new byte[4], true);
        } catch (Exception e2) {
            return null;
        }
    }

    public void a(C0068ba c0068ba) {
        if (c0068ba != null) {
            for (int i2 = 0; i2 < this.f9948d.getItemCount(); i2++) {
                if (((R) this.f9948d.getItemAt(i2)).a().equals(c0068ba)) {
                    this.f9948d.setSelectedIndex(i2);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void d() {
        C0068ba c0068baA;
        R r2 = (R) this.f9948d.getSelectedItem();
        if (r2 == null || (c0068baA = r2.a()) == null) {
            return;
        }
        if (this.f9950f.get(c0068baA.b()) != null) {
            ((CardLayout) this.f9954j.getLayout()).show(this.f9954j, c0068baA.b());
        }
        for (aL aLVar : this.f9950f.values()) {
            aLVar.setEnabled((c0068baA.b() == null || this.f9950f.get(c0068baA.b()) == null || !aLVar.equals(this.f9950f.get(c0068baA.b()))) ? false : true);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String e() {
        return ((S) this.f9947c.getSelectedItem()).a().a();
    }

    public void b() {
        if (!this.f9945a.C().q()) {
            bV.d(C1818g.b("Not currently Online!") + "\n" + C1818g.b("You must be connected to the controller to write this calibration table."), this);
            return;
        }
        this.f9956l = true;
        k();
        try {
            int[] iArrF = f();
            boolean z2 = this.f9945a.O().aw().length() > 1;
            int iAx = this.f9945a.O().ax();
            if (!this.f9945a.equals(G.T.a().c())) {
                int iG = G.T.a().c().O().G(0);
                iAx = iG < iAx ? iG : iAx;
            }
            if (z2) {
                ArrayList arrayList = new ArrayList();
                int iH = this.f9946b.h();
                int i2 = 0;
                byte[] bArrD = this.f9945a.O().e(this.f9945a.O().aw()).d();
                int[] iArrA = new int[2];
                while (i2 < iArrF.length) {
                    int length = iArrF.length - i2 < iAx ? iArrF.length - i2 : iAx;
                    int[] iArr = new int[length + 7];
                    iArr[0] = bArrD[0];
                    iArr[1] = bArrD[1];
                    iArr[2] = C0995c.d(e());
                    if (bArrD.length > 3) {
                        iH = C0995c.a(bArrD, 3, 2, true, false);
                    }
                    int[] iArrA2 = C0995c.a(i2 + iH, iArrA, true);
                    iArr[3] = iArrA2[0];
                    iArr[4] = iArrA2[1];
                    iArrA = C0995c.a(length, iArrA2, true);
                    iArr[5] = iArrA[0];
                    iArr[6] = iArrA[1];
                    System.arraycopy(iArrF, i2, iArr, 7, length);
                    C0130m c0130mA = C0130m.a(this.f9945a.O(), iArr);
                    c0130mA.a(this.f9945a.O().K());
                    arrayList.add(c0130mA);
                    c0130mA.a(600);
                    i2 += length;
                }
                C0130m c0130mA2 = C0130m.a(this.f9945a.O(), arrayList);
                O o2 = new O(this);
                if (arrayList.size() == 1) {
                    ((C0130m) arrayList.get(0)).b(o2);
                }
                c0130mA2.b(o2);
                this.f9945a.C().b(c0130mA2);
            } else {
                C0130m c0130mA3 = C0130m.a(this.f9945a.O(), e(), iArrF);
                c0130mA3.a(this.f9945a.O().K());
                P p2 = new P(this);
                c0130mA3.h(this.f9946b.d());
                c0130mA3.b(p2);
                this.f9945a.C().b(c0130mA3);
            }
        } catch (V.a e2) {
            bV.d(e2.getMessage(), this);
            c("Correct Errors.");
        } catch (V.g e3) {
            bV.d("Configuration Error trying to write reference table:\n" + e3.getMessage(), this);
            c("Correct Errors.");
        } catch (Exception e4) {
            bV.d("Application Error trying to write reference table.\nSee App Debug Log for more info", this);
            c("Contact Support.");
            e4.printStackTrace();
        }
        this.f9956l = false;
    }

    private int[] f() {
        return c(((R) this.f9948d.getSelectedItem()).a());
    }

    private int[] c(C0068ba c0068ba) throws V.a {
        double[] dArrA;
        String strB = c0068ba.b();
        if (c0068ba.b().trim().length() == 0) {
            throw new V.a("You must select a valid input solution to generate table.");
        }
        if (this.f9950f.get(strB) != null) {
            aL aLVar = (aL) this.f9950f.get(strB);
            b("//-- generated using refTable Generator:" + strB + "\n");
            b("//-- Table Identifier:" + e());
            b(aLVar.a() + "\n");
            b("//----------------------------------------------------------//\n");
            dArrA = aLVar.a(this.f9946b.c());
        } else {
            dArrA = new double[this.f9946b.c()];
            b("//-- generated using formula:" + strB + "\n");
            b("//-- Table Identifier:" + e());
            b("\n\n");
            for (int i2 = 0; i2 < dArrA.length; i2++) {
                String strA = strB;
                if (strA.indexOf("table(") != -1) {
                    strA = a(strA, i2);
                }
                String strB2 = bH.W.b(strA, "adcValue", "" + i2);
                try {
                    dArrA[i2] = bH.F.g(strB2);
                } catch (V.h e2) {
                    throw new V.a("Unable to evaluate " + strB2 + "\nfor solution: " + strB);
                }
            }
        }
        b("ADC \tValue \tMSVal \tvolts\n");
        double dC = this.f9946b.c(e());
        double d2 = this.f9946b.d(e());
        double dE = this.f9946b.e(e());
        int[] iArr = new int[dArrA.length * this.f9946b.d()];
        int i3 = 0;
        while (i3 < iArr.length) {
            if (dArrA[i3 / this.f9946b.d()] > d2 || dArrA[i3 / this.f9946b.d()] < dC) {
                dArrA[i3 / this.f9946b.d()] = dE;
            }
            int iRound = (int) Math.round(dArrA[i3 / this.f9946b.d()] * this.f9946b.k());
            if (this.f9946b.d() > 1) {
                if (iRound > Math.pow(2.0d, 8 * this.f9946b.d()) - 1.0d) {
                    iRound = (int) (Math.pow(2.0d, 8 * this.f9946b.d()) - 1.0d);
                }
            } else if (iRound > Math.pow(2.0d, 8 * this.f9946b.d())) {
                iRound = (int) (Math.pow(2.0d, 8 * this.f9946b.d()) - 1.0d);
            }
            b((i3 / this.f9946b.d()) + "\t" + bH.W.b(dArrA[i3 / this.f9946b.d()], 2) + "\t" + iRound + " \t" + bH.W.b((i3 * 5.0d) / iArr.length, 3) + "\n");
            for (byte b2 : C0995c.a(iRound, new byte[this.f9946b.d()], this.f9945a.O().e())) {
                int i4 = i3;
                i3++;
                iArr[i4] = b2;
            }
        }
        return iArr;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void g() {
        Q q2 = new Q(this);
        for (aL aLVar : this.f9950f.values()) {
            if (aLVar.isShowing()) {
                aLVar.a(q2);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void h() {
        Q q2 = new Q(this);
        Iterator it = this.f9950f.values().iterator();
        while (it.hasNext()) {
            ((aL) it.next()).b(q2);
        }
    }

    private void b(String str) {
        FileWriter fileWriterI;
        if (!this.f9956l || (fileWriterI = i()) == null) {
            return;
        }
        try {
            fileWriterI.write(str);
        } catch (IOException e2) {
            bH.C.c(str);
        }
    }

    private FileWriter i() {
        if (this.f9952h == null) {
            String str = this.f9945a.F() + File.separator + this.f9946b.aJ() + ".log";
            File file = new File(str);
            if (file.exists()) {
                file.delete();
            }
            try {
                this.f9952h = new FileWriter(str);
            } catch (IOException e2) {
                bH.C.a("Unable to open ReferenceTable log file:\n" + str);
                bH.C.a("Will writer to this log instead");
                e2.printStackTrace();
            }
        }
        return this.f9952h;
    }

    private void j() {
        if (this.f9952h != null) {
            try {
                this.f9952h.close();
                this.f9952h = null;
            } catch (IOException e2) {
                bH.C.c("Failed to close Reference Table log file");
            }
        }
    }

    private String a(String str, int i2) throws V.a {
        String strSubstring = str.substring(0, str.indexOf("table("));
        int iIndexOf = str.indexOf("(", str.indexOf("table")) + 1;
        int iIndexOf2 = str.indexOf(",", iIndexOf);
        String strB = bH.W.b(str.substring(iIndexOf, iIndexOf2).trim(), "adcValue", "" + i2);
        try {
            double dG = bH.F.g(strB);
            int iIndexOf3 = str.indexOf(")", iIndexOf2);
            String strTrim = str.substring(iIndexOf2 + 1, iIndexOf3).trim();
            String strF = G.T.a().c().F();
            String strB2 = bH.W.b(strTrim, PdfOps.DOUBLE_QUOTE__TOKEN, "");
            try {
                return (strSubstring + "" + bH.E.b(strF, strB2).a(dG)) + str.substring(iIndexOf3 + 1);
            } catch (IOException e2) {
                throw new V.a("Error loading inc Mapping File:" + strB2, e2);
            } catch (Exception e3) {
                throw new V.a("Error parsing inc Mapping File:" + strB2 + "\n\t" + e3.getMessage());
            }
        } catch (V.h e4) {
            throw new V.a("Unable to evaluate expression: " + strB + "\nin solution formula: " + str);
        }
    }

    private void k() throws IllegalArgumentException {
        this.f9949e.setEnabled(false);
        this.f9951g.setText("Writing to controller, Please wait...");
        Calendar calendar = Calendar.getInstance();
        b("//------------------------------------------------------------------------------\n");
        b("//--  Generated by " + C1798a.f13268b + " \n");
        b("//--  Date: " + (calendar.get(2) + 1) + LanguageTag.SEP + calendar.get(5) + LanguageTag.SEP + calendar.get(1) + "\n");
        b("//--  Time: " + calendar.get(11) + CallSiteDescriptor.TOKEN_DELIMITER + calendar.get(12) + "\n");
        b("//--  This file merely records what was sent to your Controller,        \n");
        b("//--  and may be deleted at any time.                                                    \n");
        b("//--  Selected type: " + this.f9947c.getSelectedItem() + " \n");
        b("//------------------------------------------------------------------------------\n");
        b("\n");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void c(String str) throws IllegalArgumentException {
        this.f9949e.setEnabled(true);
        this.f9951g.setText("<html>" + bH.W.b(str, "\n", "<br>") + "</html>");
        j();
    }

    public void c() {
        C0338f.a().a(this.f9945a, (String) this.f9946b.O().get(0), bV.a(this));
    }

    private aL a(bE bEVar) throws V.a {
        if (bEVar.g().equals("linearGenerator")) {
            return new com.efiAnalytics.tuningwidgets.panels.M((C0087bt) bEVar);
        }
        if (bEVar.g().equals("thermGenerator")) {
            return new aM((bF) bEVar);
        }
        if (bEVar.g().equals("fileBrowseGenerator")) {
            return new com.efiAnalytics.tuningwidgets.panels.G(bEVar.h());
        }
        throw new V.a("Table Generator type '" + bEVar.g() + "'\n not understood for table generator '" + bEVar.h() + "' and will not be displayed.");
    }

    @Override // com.efiAnalytics.ui.InterfaceC1565bc
    public void close() {
    }
}
