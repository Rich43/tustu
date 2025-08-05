package com.efiAnalytics.tuningwidgets.panels;

import G.C0069bb;
import G.C0070bc;
import bH.C1007o;
import bt.C1324bf;
import com.efiAnalytics.ui.bV;
import com.sun.org.apache.xerces.internal.impl.xs.SchemaSymbols;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import s.C1818g;

/* renamed from: com.efiAnalytics.tuningwidgets.panels.ae, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:com/efiAnalytics/tuningwidgets/panels/ae.class */
public class C1488ae extends C1324bf implements G.aN {

    /* renamed from: a, reason: collision with root package name */
    G.R f10388a;

    /* renamed from: b, reason: collision with root package name */
    G.aM f10389b;

    /* renamed from: g, reason: collision with root package name */
    String f10394g;

    /* renamed from: j, reason: collision with root package name */
    int f10397j;

    /* renamed from: c, reason: collision with root package name */
    DefaultListModel f10390c = new DefaultListModel();

    /* renamed from: d, reason: collision with root package name */
    JList f10391d = new JList(this.f10390c);

    /* renamed from: e, reason: collision with root package name */
    DefaultListModel f10392e = new DefaultListModel();

    /* renamed from: f, reason: collision with root package name */
    JList f10393f = new JList(this.f10392e);

    /* renamed from: h, reason: collision with root package name */
    JLabel f10395h = new JLabel(" ");

    /* renamed from: i, reason: collision with root package name */
    long f10396i = 0;

    public C1488ae(G.R r2, C0070bc c0070bc) {
        this.f10388a = null;
        this.f10389b = null;
        this.f10394g = "52";
        this.f10397j = 0;
        this.f10388a = r2;
        this.f10389b = this.f10388a.c(c0070bc.d());
        this.f10394g = c0070bc.a();
        this.f10397j = c0070bc.c();
        setLayout(new BorderLayout(12, 12));
        setBorder(BorderFactory.createTitledBorder(C1818g.b(c0070bc.M())));
        this.f10391d.addListSelectionListener(new C1489af(this));
        this.f10393f.addListSelectionListener(new C1490ag(this));
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new BorderLayout(5, 5));
        JScrollPane jScrollPane = new JScrollPane(this.f10391d);
        jScrollPane.setBorder(BorderFactory.createLoweredBevelBorder());
        jScrollPane.setPreferredSize(new Dimension(150, 150));
        jPanel.add("North", new JLabel(C1818g.b("Available Replay Fields"), 0));
        jPanel.add(BorderLayout.CENTER, jScrollPane);
        jPanel.add("West", new JLabel(""));
        add("West", jPanel);
        JPanel jPanel2 = new JPanel();
        jPanel2.setLayout(new BorderLayout(5, 5));
        JScrollPane jScrollPane2 = new JScrollPane(this.f10393f);
        jScrollPane2.setBorder(BorderFactory.createLoweredBevelBorder());
        jScrollPane2.setPreferredSize(new Dimension(150, 150));
        jPanel2.add("North", new JLabel(C1818g.b("Active Replay Fields"), 0));
        jPanel2.add(BorderLayout.CENTER, jScrollPane2);
        jPanel2.add("East", new JLabel(""));
        add("East", jPanel2);
        new JPanel().setLayout(new FlowLayout());
        JPanel jPanel3 = new JPanel();
        Dimension dimension = new Dimension(60, 25);
        jPanel3.setLayout(new GridLayout(0, 1, 4, 4));
        jPanel3.add(new JLabel(" "));
        JButton jButton = new JButton(">>");
        jButton.setPreferredSize(dimension);
        jButton.setMnemonic(65);
        jButton.setToolTipText(C1818g.b("Add All"));
        jButton.addActionListener(new C1491ah(this));
        jPanel3.add(jButton);
        JButton jButton2 = new JButton(">");
        jButton2.setPreferredSize(dimension);
        jButton2.setMnemonic(160);
        jButton2.setToolTipText(C1818g.b("Add selected fields"));
        jButton2.addActionListener(new C1492ai(this));
        jPanel3.add(jButton2);
        JButton jButton3 = new JButton("<");
        jButton3.setPreferredSize(dimension);
        jButton3.setMnemonic(153);
        jButton3.setToolTipText(C1818g.b("Remove selected fields"));
        jButton3.addActionListener(new C1493aj(this));
        jPanel3.add(jButton3);
        JButton jButton4 = new JButton("<<");
        jButton4.setPreferredSize(dimension);
        jButton4.setToolTipText(C1818g.b("Remove All"));
        jButton4.setMnemonic(82);
        jButton4.addActionListener(new C1494ak(this));
        jPanel3.add(jButton4);
        add(BorderLayout.CENTER, jPanel3);
        JPanel jPanel4 = new JPanel();
        jPanel4.setLayout(new GridLayout(0, 1));
        add("South", jPanel4);
        try {
            G.aR.a().a(this.f10388a.c(), this.f10389b.aJ(), this);
        } catch (V.a e2) {
            Logger.getLogger(C1488ae.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
        }
        c();
    }

    public void c() {
        this.f10390c.clear();
        this.f10392e.clear();
        try {
            ArrayList arrayListG = g();
            Iterator it = this.f10388a.t().iterator();
            while (it.hasNext()) {
                C0069bb c0069bb = (C0069bb) it.next();
                if (b(c0069bb.aH())) {
                    C1496am c1496am = new C1496am(this, c0069bb);
                    if (c1496am.a(arrayListG)) {
                        this.f10392e.addElement(c1496am);
                    } else {
                        this.f10390c.addElement(c1496am);
                    }
                }
            }
        } catch (V.g e2) {
            bH.C.a("Settings Error!", e2, this);
        } catch (Exception e3) {
            bH.C.a("An error occured opening the DataLogField Selector.\nThis is most likely caused by a settings error.\nCheck the log file during project opening for errors.", e3, this);
        }
    }

    private boolean b(String str) {
        if (str == null || str.equals("")) {
            return true;
        }
        try {
            return C1007o.a(str, this.f10388a);
        } catch (Exception e2) {
            bH.C.c(e2.getMessage());
            return true;
        }
    }

    public void d() {
        try {
            this.f10396i = System.currentTimeMillis();
            Object[] selectedValues = this.f10391d.getSelectedValues();
            for (int i2 = 0; i2 < selectedValues.length; i2++) {
                if (a((C1496am) selectedValues[i2])) {
                    this.f10392e.addElement(selectedValues[i2]);
                }
            }
        } finally {
            h();
            c();
        }
    }

    public void e() {
        try {
            this.f10396i = System.currentTimeMillis();
            Object[] array = this.f10390c.toArray();
            for (int i2 = 0; i2 < array.length; i2++) {
                if (a((C1496am) array[i2])) {
                    this.f10392e.addElement(array[i2]);
                }
            }
        } finally {
            h();
            c();
        }
    }

    public void f() {
        int[] selectedIndices = this.f10393f.getSelectedIndices();
        this.f10396i = System.currentTimeMillis();
        if (selectedIndices == null) {
            return;
        }
        for (int length = selectedIndices.length - 1; length >= 0; length--) {
            this.f10392e.remove(selectedIndices[length]);
        }
        h();
        c();
    }

    @Override // java.awt.Container
    public void removeAll() {
        this.f10392e.clear();
        this.f10396i = System.currentTimeMillis();
        h();
        c();
    }

    public ArrayList g() throws V.g {
        ArrayList arrayList = new ArrayList();
        String[] strArrV = this.f10388a.v();
        double[][] dArrI = this.f10389b.i(this.f10388a.h());
        for (String str : strArrV) {
            C0069bb c0069bbH = this.f10388a.h(str);
            for (int i2 = 0; i2 < this.f10389b.b(); i2++) {
                if (dArrI[i2][0] == c0069bbH.y() && !arrayList.contains(c0069bbH)) {
                    arrayList.add(c0069bbH);
                }
            }
        }
        return arrayList;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void b(C1496am c1496am) throws IllegalArgumentException {
        if (c1496am == null) {
            return;
        }
        this.f10395h.setText((c1496am.toString() + " - ") + c1496am.a().aJ());
    }

    public boolean a(C1496am c1496am) {
        return true;
    }

    public void h() {
        try {
            ArrayList arrayListI = i();
            if (arrayListI.size() > this.f10389b.b()) {
                bV.d("A maximum of " + this.f10389b.b() + " Output Channels can be logged.\nYour current selection of fields requires " + arrayListI.size() + " Output Channels\nOnly the fields based on the first " + this.f10389b.b() + " OutputChannels will be added.", this);
            }
            int iB = (int) C1007o.b(this.f10394g, this.f10388a);
            int iL = 0;
            Iterator it = arrayListI.iterator();
            while (it.hasNext()) {
                iL += ((C0069bb) it.next()).l();
                if (iL > iB) {
                    it.remove();
                    if (!it.hasNext()) {
                        bV.d("The selected Data Log fields require " + iL + " bytes\nThe selected Log Data Block size only supports " + iB + " bytes\nThe fields that do not fit have been removed.\nTo log the removed fields, others must be removed.", this);
                    }
                }
            }
            double[][] dArrI = this.f10389b.i(this.f10388a.h());
            for (int i2 = 0; i2 < dArrI.length; i2++) {
                if (i2 < arrayListI.size()) {
                    dArrI[i2][0] = ((C0069bb) arrayListI.get(i2)).y();
                } else {
                    dArrI[i2][0] = this.f10397j;
                }
            }
            this.f10389b.a(this.f10388a.h(), dArrI);
        } catch (V.g e2) {
            bH.C.a("Unable to save selected fields.", e2, this);
        } catch (V.j e3) {
            bH.C.a("Unable to save selected fields. Invalid offset and length values.", e3, this);
        }
    }

    private boolean a(ArrayList arrayList, C0069bb c0069bb) {
        Iterator it = arrayList.iterator();
        while (it.hasNext()) {
            C0069bb c0069bb2 = (C0069bb) it.next();
            if (c0069bb2.y() == c0069bb.y() && c0069bb2.l() == c0069bb.l()) {
                return true;
            }
        }
        return false;
    }

    private ArrayList i() {
        ArrayList arrayList = new ArrayList();
        for (int i2 = 0; i2 < this.f10392e.getSize(); i2++) {
            C0069bb c0069bbA = ((C1496am) this.f10392e.get(i2)).a();
            if (!c0069bbA.aJ().equals(SchemaSymbols.ATTVAL_TIME) && !a(arrayList, c0069bbA)) {
                arrayList.add(c0069bbA);
            }
        }
        for (int i3 = 0; i3 < arrayList.size(); i3++) {
            for (int i4 = i3 + 1; i4 < arrayList.size(); i4++) {
                C0069bb c0069bb = (C0069bb) arrayList.get(i3);
                C0069bb c0069bb2 = (C0069bb) arrayList.get(i4);
                if (c0069bb.y() > c0069bb2.y()) {
                    arrayList.set(i3, c0069bb2);
                    arrayList.set(i4, c0069bb);
                }
            }
        }
        return arrayList;
    }

    @Override // G.aN
    public void a(String str, String str2) {
        if (System.currentTimeMillis() - this.f10396i > 500) {
            this.f10396i = System.currentTimeMillis();
            new C1495al(this).start();
        }
    }
}
