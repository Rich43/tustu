package com.efiAnalytics.tuningwidgets.panels;

import G.C0043ac;
import G.C0047ag;
import G.C0052al;
import G.C0126i;
import G.bQ;
import G.dh;
import bH.C1007o;
import bt.C1324bf;
import com.efiAnalytics.plugin.ecu.ControllerParameter;
import com.efiAnalytics.ui.InterfaceC1565bc;
import com.efiAnalytics.ui.bV;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import jdk.internal.dynalink.CallSiteDescriptor;
import s.C1818g;

/* renamed from: com.efiAnalytics.tuningwidgets.panels.n, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:com/efiAnalytics/tuningwidgets/panels/n.class */
public class C1516n extends C1324bf implements G.aN, InterfaceC1565bc {

    /* renamed from: a, reason: collision with root package name */
    G.R f10482a;

    /* renamed from: b, reason: collision with root package name */
    G.aM f10483b;

    /* renamed from: c, reason: collision with root package name */
    G.aM f10484c;

    /* renamed from: h, reason: collision with root package name */
    dh f10489h;

    /* renamed from: m, reason: collision with root package name */
    int f10494m;

    /* renamed from: d, reason: collision with root package name */
    DefaultListModel f10485d = new DefaultListModel();

    /* renamed from: e, reason: collision with root package name */
    JList f10486e = new JList(this.f10485d);

    /* renamed from: f, reason: collision with root package name */
    DefaultListModel f10487f = new DefaultListModel();

    /* renamed from: g, reason: collision with root package name */
    JList f10488g = new JList(this.f10487f);

    /* renamed from: i, reason: collision with root package name */
    JLabel f10490i = new JLabel(" ");

    /* renamed from: j, reason: collision with root package name */
    JLabel f10491j = new JLabel(" ");

    /* renamed from: k, reason: collision with root package name */
    JLabel f10492k = new JLabel(" ");

    /* renamed from: l, reason: collision with root package name */
    long f10493l = 0;

    public C1516n(G.R r2, C0047ag c0047ag) {
        this.f10482a = null;
        this.f10483b = null;
        this.f10484c = null;
        this.f10489h = new G.B(55.0d);
        this.f10494m = 0;
        this.f10482a = r2;
        this.f10483b = this.f10482a.c(c0047ag.a());
        this.f10484c = this.f10482a.c(c0047ag.b());
        this.f10489h = c0047ag.c();
        if (this.f10489h instanceof bQ) {
            C0126i.h(((bQ) this.f10489h).c(), r2);
            try {
                C0126i.a(r2, ((bQ) this.f10489h).c(), new C1517o(this));
            } catch (V.a e2) {
                Logger.getLogger(C1516n.class.getName()).log(Level.WARNING, "Failed to subscribe listener to log record size limit expression", (Throwable) e2);
            }
        }
        this.f10494m = c0047ag.d();
        setLayout(new BorderLayout(12, 12));
        setBorder(BorderFactory.createTitledBorder(C1818g.b(c0047ag.M())));
        this.f10486e.addListSelectionListener(new C1520r(this));
        this.f10488g.addListSelectionListener(new C1521s(this));
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new BorderLayout(5, 5));
        JScrollPane jScrollPane = new JScrollPane(this.f10486e);
        jScrollPane.setBorder(BorderFactory.createLoweredBevelBorder());
        jScrollPane.setPreferredSize(new Dimension(150, 150));
        jPanel.add("North", new JLabel(C1818g.b("Available Datalog Fields"), 0));
        jPanel.add(BorderLayout.CENTER, jScrollPane);
        jPanel.add("West", new JLabel(""));
        add("West", jPanel);
        JPanel jPanel2 = new JPanel();
        jPanel2.setLayout(new BorderLayout(5, 5));
        JScrollPane jScrollPane2 = new JScrollPane(this.f10488g);
        jScrollPane2.setBorder(BorderFactory.createLoweredBevelBorder());
        jScrollPane2.setPreferredSize(new Dimension(150, 150));
        jPanel2.add("North", new JLabel(C1818g.b("Selected Datalog Fields"), 0));
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
        jButton.addActionListener(new C1522t(this));
        jPanel3.add(jButton);
        JButton jButton2 = new JButton(">");
        jButton2.setPreferredSize(dimension);
        jButton2.setMnemonic(160);
        jButton2.setToolTipText(C1818g.b("Add selected fields"));
        jButton2.addActionListener(new C1523u(this));
        jPanel3.add(jButton2);
        JButton jButton3 = new JButton("<");
        jButton3.setPreferredSize(dimension);
        jButton3.setMnemonic(153);
        jButton3.setToolTipText(C1818g.b("Remove selected fields"));
        jButton3.addActionListener(new C1524v(this));
        jPanel3.add(jButton3);
        JButton jButton4 = new JButton("<<");
        jButton4.setPreferredSize(dimension);
        jButton4.setToolTipText(C1818g.b("Remove All"));
        jButton4.setMnemonic(82);
        jButton4.addActionListener(new C1525w(this));
        jPanel3.add(jButton4);
        add(BorderLayout.CENTER, jPanel3);
        JPanel jPanel4 = new JPanel();
        jPanel4.setLayout(new GridLayout(0, 1));
        jPanel4.add(new JLabel(C1818g.b("Required Output Channels") + CallSiteDescriptor.TOKEN_DELIMITER));
        jPanel4.add(this.f10490i);
        JPanel jPanel5 = new JPanel();
        jPanel5.setLayout(new GridLayout(1, 0));
        jPanel4.add(jPanel5);
        jPanel5.add(this.f10491j);
        jPanel5.add(this.f10492k);
        add("South", jPanel4);
        try {
            G.aR.a().a(this.f10482a.c(), this.f10483b.aJ(), this);
        } catch (V.a e3) {
            Logger.getLogger(C1516n.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e3);
        }
        c();
    }

    public void c() {
        long jCurrentTimeMillis = System.currentTimeMillis();
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        try {
            ArrayList arrayListG = g();
            Iterator it = this.f10482a.g().iterator();
            while (it.hasNext()) {
                C0043ac c0043ac = (C0043ac) it.next();
                if (b(c0043ac.aH())) {
                    G.aH aHVarG = this.f10482a.g(c0043ac.a());
                    if (aHVarG.b().equals(ControllerParameter.PARAM_CLASS_BITS)) {
                        aHVarG = C0126i.a(this.f10482a, aHVarG.a());
                    }
                    ArrayList arrayListA = C0126i.a(this.f10482a, aHVarG);
                    C1528z c1528z = new C1528z(this, c0043ac, arrayListA);
                    c1528z.a(aHVarG != null && aHVarG.b().equals("formula"));
                    if (!c0043ac.a().equals("dataLogTime") && (arrayListA.isEmpty() || aHVarG.w())) {
                        bH.C.c("skipping: " + c0043ac.b());
                    } else if (c1528z.a(arrayListG) || c0043ac.a().equals("dataLogTime") || c0043ac.a().equals("engine")) {
                        arrayList2.add(c1528z);
                    } else {
                        arrayList.add(c1528z);
                    }
                }
            }
            Iterator it2 = this.f10482a.f().iterator();
            while (it2.hasNext()) {
                C0052al c0052al = (C0052al) it2.next();
                ArrayList arrayList3 = new ArrayList();
                arrayList3.add(c0052al);
                C1528z c1528z2 = new C1528z(this, c0052al, arrayList3);
                if (c1528z2.a(arrayListG)) {
                    arrayList2.add(c1528z2);
                } else {
                    arrayList.add(c1528z2);
                }
            }
        } catch (V.g e2) {
            bH.C.a("Settings Error!", e2, this);
        } catch (Exception e3) {
            bH.C.a("An error occured opening the DataLogField Selector.\nThis is most likely caused by a settings error.\nCheck the log file during project opening for errors.", e3, this);
        }
        C1526x c1526x = new C1526x(this);
        Collections.sort(arrayList, c1526x);
        Collections.sort(arrayList2, c1526x);
        this.f10485d.clear();
        this.f10487f.clear();
        Iterator<E> it3 = arrayList.iterator();
        while (it3.hasNext()) {
            this.f10485d.addElement((C1528z) it3.next());
        }
        Iterator<E> it4 = arrayList2.iterator();
        while (it4.hasNext()) {
            this.f10487f.addElement((C1528z) it4.next());
        }
        bH.C.c("Time in updateSelections():" + (System.currentTimeMillis() - jCurrentTimeMillis) + "ms.");
    }

    private boolean b(String str) {
        if (str == null || str.equals("")) {
            return true;
        }
        try {
            return C1007o.a(str, this.f10482a);
        } catch (Exception e2) {
            bH.C.c(e2.getMessage());
            return true;
        }
    }

    public void d() {
        try {
            this.f10493l = System.currentTimeMillis();
            for (C1528z c1528z : this.f10486e.getSelectedValuesList()) {
                if (a(c1528z)) {
                    this.f10487f.addElement(c1528z);
                }
            }
        } finally {
            i();
            c();
        }
    }

    public void e() {
        try {
            this.f10493l = System.currentTimeMillis();
            Object[] array = this.f10485d.toArray();
            for (int i2 = 0; i2 < array.length; i2++) {
                if (a((C1528z) array[i2])) {
                    this.f10487f.addElement((C1528z) array[i2]);
                }
            }
        } finally {
            i();
            c();
        }
    }

    public void f() {
        int[] selectedIndices = this.f10488g.getSelectedIndices();
        this.f10493l = System.currentTimeMillis();
        if (selectedIndices == null) {
            return;
        }
        if (selectedIndices.length == 1 && ((C1528z) this.f10488g.getModel().getElementAt(selectedIndices[0])).f10508b == null) {
            C1528z c1528z = (C1528z) this.f10488g.getModel().getElementAt(selectedIndices[0]);
            G.aH aHVarG = this.f10482a.g(c1528z.f10507a.a());
            List<C1528z> listA = a((List) c1528z.a());
            if (aHVarG != null && aHVarG.b().equals("formula")) {
                StringBuilder sb = new StringBuilder();
                sb.append(C1818g.b("This field requires no SD record space because it is generated from fields that are already logged")).append(".\n").append(C1818g.b("This field will be removed if any of the following are removed")).append(":\n");
                for (C1528z c1528z2 : listA) {
                    if (!c1528z2.equals(c1528z) && !c1528z2.b()) {
                        sb.append("\t").append(c1528z2.toString()).append("\n");
                    }
                }
                sb.append("\n");
                sb.append(C1818g.b("Do you want to remove all the listed Fields?"));
                if (!bV.a(sb.toString(), (Component) this, true)) {
                    return;
                }
            } else if (listA.size() > 1) {
                StringBuilder sb2 = new StringBuilder();
                sb2.append(C1818g.b("Other selected fields are dependent on this field")).append(".\n").append(C1818g.b("If this is removed, those fields will also be removed")).append(".\n").append(C1818g.b("Removing")).append(": \n\t").append(c1528z.toString()).append("\n").append(C1818g.b("Also to be removed")).append(": \n");
                for (C1528z c1528z3 : listA) {
                    if (!c1528z3.equals(c1528z)) {
                        sb2.append("\t").append(c1528z3.toString()).append("\n");
                    }
                }
                sb2.append("\n");
                sb2.append(C1818g.b("Are you sure you want to remove all the listed Fields?"));
                if (!bV.a(sb2.toString(), (Component) this, true)) {
                    return;
                }
            }
            Iterator it = listA.iterator();
            while (it.hasNext()) {
                b((C1528z) it.next());
            }
        } else {
            this.f10488g.clearSelection();
            for (int length = selectedIndices.length - 1; length >= 0; length--) {
                this.f10487f.remove(selectedIndices[length]);
            }
        }
        i();
        c();
    }

    private void b(C1528z c1528z) {
        for (int i2 = 0; i2 < this.f10487f.getSize(); i2++) {
            if (((C1528z) this.f10487f.getElementAt(i2)).equals(c1528z)) {
                this.f10487f.remove(i2);
                return;
            }
        }
    }

    private List a(List list) {
        ArrayList arrayList = new ArrayList();
        for (int i2 = 0; i2 < this.f10488g.getModel().getSize(); i2++) {
            C1528z c1528z = (C1528z) this.f10488g.getModel().getElementAt(i2);
            if (c1528z.b(list)) {
                arrayList.add(c1528z);
            }
        }
        return arrayList;
    }

    @Override // java.awt.Container
    public void removeAll() {
        this.f10487f.clear();
        this.f10493l = System.currentTimeMillis();
        i();
        c();
    }

    public ArrayList g() throws V.g {
        ArrayList arrayList = new ArrayList();
        String[] strArrS = this.f10482a.s();
        double[][] dArrI = this.f10483b.i(this.f10482a.h());
        for (String str : strArrS) {
            G.aH aHVarG = this.f10482a.g(str);
            for (int i2 = 0; i2 < this.f10483b.b(); i2++) {
                if (!aHVarG.b().equals("formula") && dArrI[i2][0] == aHVarG.a() && !arrayList.contains(aHVarG)) {
                    arrayList.add(aHVarG);
                }
            }
        }
        Iterator it = this.f10482a.f().iterator();
        while (it.hasNext()) {
            C0052al c0052al = (C0052al) it.next();
            for (int i3 = 0; i3 < this.f10483b.b(); i3++) {
                if (dArrI[i3][0] == c0052al.a() && !arrayList.contains(c0052al)) {
                    arrayList.add(c0052al);
                }
            }
        }
        return arrayList;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void a(C1528z c1528z, boolean z2) throws IllegalArgumentException {
        if (c1528z == null) {
            return;
        }
        try {
            String str = c1528z.toString() + " - ";
            ArrayList arrayListA = c1528z.a();
            G.aH aHVarG = c1528z.f10508b != null ? c1528z.f10508b : this.f10482a.g(c1528z.f10507a.a());
            ArrayList arrayListG = g();
            int iL = 0;
            int iL2 = 0;
            for (int i2 = 0; i2 < arrayListA.size(); i2++) {
                str = str + ((G.aH) arrayListA.get(i2)).aJ();
                if (i2 < arrayListA.size() - 1) {
                    str = str + ", ";
                }
                if (!arrayListG.contains(arrayListA.get(i2))) {
                    iL += ((G.aH) arrayListA.get(i2)).l();
                }
                iL2 += ((G.aH) arrayListA.get(i2)).l();
            }
            if (iL == 0 || aHVarG == null || aHVarG.b().equals("formula")) {
                this.f10490i.setText(str);
            } else {
                this.f10490i.setText(str);
            }
            if (!z2) {
                this.f10491j.setText(C1818g.b("Record Bytes Required") + ": " + iL);
            } else if (aHVarG == null || !aHVarG.b().equals("formula")) {
                this.f10491j.setText(C1818g.b("Record Bytes Used") + ": " + aHVarG.l());
            } else {
                this.f10491j.setText(C1818g.b("0 Bytes.") + " " + C1818g.b("Underlying Channels") + ": " + iL2);
            }
            int iJ = j();
            int iA = (int) this.f10489h.a();
            this.f10492k.setText(C1818g.b("Available Bytes") + ": " + (iA - iJ) + " " + C1818g.b("of") + " " + iA);
        } catch (V.g e2) {
            e2.printStackTrace();
        }
    }

    protected boolean h() {
        ArrayList arrayListQ = q();
        ArrayList arrayList = new ArrayList();
        int iA = (int) this.f10489h.a();
        int iL = 0;
        Iterator it = arrayListQ.iterator();
        while (it.hasNext()) {
            G.aH aHVar = (G.aH) it.next();
            iL += aHVar.l();
            if (iL > iA) {
                arrayList.add(aHVar);
            }
        }
        if (arrayList.isEmpty()) {
            return true;
        }
        List listA = a((List) arrayList);
        String str = C1818g.b("This change will not allow all of your selected fields to be logged.") + "\n" + C1818g.b("The following fields will be removed") + ":\n";
        Iterator it2 = listA.iterator();
        while (it2.hasNext()) {
            str = str + ((C1528z) it2.next()).toString() + "\n";
        }
        if (!bV.a(str + "\n" + C1818g.b("Are you sure you wish to continue?"), (Component) this, true)) {
            try {
                this.f10482a.h().c();
                return true;
            } catch (V.g e2) {
                Logger.getLogger(C1516n.class.getName()).log(Level.WARNING, "Failed to undo log size constraint.", (Throwable) e2);
                return true;
            }
        }
        Iterator it3 = listA.iterator();
        while (it3.hasNext()) {
            b((C1528z) it3.next());
        }
        i();
        c();
        return false;
    }

    protected boolean a(C1528z c1528z) throws V.a {
        if (c1528z.f10508b != null) {
            return true;
        }
        ArrayList arrayListQ = q();
        ArrayList arrayList = new ArrayList(c1528z.a());
        arrayList.addAll(arrayListQ);
        if (arrayList.size() > this.f10483b.b()) {
            throw new V.a("A maximum of " + this.f10483b.b() + " Output Channels can be logged.\nYour current selection of fields requires more \nOutput Channels than supported by the firmware.");
        }
        int iA = (int) this.f10489h.a();
        int iL = 0;
        Iterator it = arrayList.iterator();
        while (it.hasNext()) {
            iL += ((G.aH) it.next()).l();
            if (iL > iA) {
                it.remove();
                if (!it.hasNext()) {
                    throw new V.a("The selected Data Log fields require " + iL + " bytes\nThe selected Log Data Block size only supports " + iA + " bytes\nField(s) not added.");
                }
            }
        }
        return true;
    }

    private int j() {
        int iL = 0;
        Iterator it = q().iterator();
        while (it.hasNext()) {
            iL += ((G.aH) it.next()).l();
        }
        return iL;
    }

    public void i() {
        try {
            ArrayList arrayListQ = q();
            if (arrayListQ.size() > this.f10483b.b()) {
                bV.d("A maximum of " + this.f10483b.b() + " Output Channels can be logged.\nYour current selection of fields requires " + arrayListQ.size() + " Output Channels\nOnly the fields based on the first " + this.f10483b.b() + " OutputChannels will be added.", this);
            }
            int iA = (int) this.f10489h.a();
            int iL = 0;
            Iterator it = arrayListQ.iterator();
            while (it.hasNext()) {
                iL += ((G.aH) it.next()).l();
                if (iL > iA) {
                    it.remove();
                    if (!it.hasNext()) {
                        bV.d("The selected Data Log fields require " + iL + " bytes\nThe selected Log Data Block size only supports " + iA + " bytes\nThe fields that do not fit have been removed.\nTo log the removed fields, others must be removed.", this);
                    }
                }
            }
            double[][] dArrI = this.f10483b.i(this.f10482a.h());
            double[][] dArrI2 = this.f10484c.i(this.f10482a.h());
            String str = "Writing Offsets:\n";
            for (int i2 = 0; i2 < dArrI.length; i2++) {
                if (i2 < arrayListQ.size()) {
                    G.aH aHVar = (G.aH) arrayListQ.get(i2);
                    dArrI[i2][0] = aHVar.a();
                    dArrI2[i2][0] = aHVar.l();
                    str = str + aHVar.a() + ", len=" + aHVar.l() + "\n";
                } else {
                    dArrI[i2][0] = this.f10494m;
                    dArrI2[i2][0] = 0.0d;
                }
            }
            bH.C.c(str);
            this.f10483b.a(this.f10482a.h(), dArrI);
            this.f10484c.a(this.f10482a.h(), dArrI2);
        } catch (V.g e2) {
            bH.C.a("Unable to save selected fields.", e2, this);
        } catch (V.j e3) {
            bH.C.a("Unable to save selected fields. Invalid offset and length values.", e3, this);
        }
    }

    private boolean a(ArrayList arrayList, G.aH aHVar) {
        Iterator it = arrayList.iterator();
        while (it.hasNext()) {
            G.aH aHVar2 = (G.aH) it.next();
            if (aHVar2.a() >= aHVar.a() && aHVar2.a() + aHVar2.l() <= aHVar.a() + aHVar.l()) {
                return true;
            }
        }
        return false;
    }

    @Override // bt.C1324bf, com.efiAnalytics.ui.InterfaceC1565bc
    public void close() {
        G.aR.a().a(this);
        super.close();
    }

    private ArrayList q() {
        ArrayList arrayList = new ArrayList();
        for (int i2 = 0; i2 < this.f10487f.getSize(); i2++) {
            Iterator it = ((C1528z) this.f10487f.get(i2)).a().iterator();
            while (it.hasNext()) {
                G.aH aHVar = (G.aH) it.next();
                if (!aHVar.aJ().equals("dataLogTime") && !a(arrayList, aHVar)) {
                    arrayList.add(aHVar);
                }
            }
        }
        ArrayList arrayListA = C0126i.a(this.f10482a, arrayList);
        for (int i3 = 0; i3 < arrayListA.size(); i3++) {
            for (int i4 = i3 + 1; i4 < arrayListA.size(); i4++) {
                G.aH aHVar2 = (G.aH) arrayListA.get(i3);
                G.aH aHVar3 = (G.aH) arrayListA.get(i4);
                if (aHVar2.a() > aHVar3.a()) {
                    arrayListA.set(i3, aHVar3);
                    arrayListA.set(i4, aHVar2);
                }
            }
        }
        return arrayListA;
    }

    @Override // G.aN
    public void a(String str, String str2) {
        if (System.currentTimeMillis() - this.f10493l > 500) {
            this.f10493l = System.currentTimeMillis();
            new C1519q(this, new RunnableC1527y(this)).start();
        }
    }
}
