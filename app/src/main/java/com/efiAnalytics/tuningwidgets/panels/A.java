package com.efiAnalytics.tuningwidgets.panels;

import bt.C1324bf;
import com.efiAnalytics.ui.C1685fp;
import com.efiAnalytics.ui.eJ;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import s.C1818g;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/tuningwidgets/panels/A.class */
public class A extends C1324bf {

    /* renamed from: a, reason: collision with root package name */
    G.R[] f10234a;

    /* renamed from: f, reason: collision with root package name */
    List f10240f;

    /* renamed from: h, reason: collision with root package name */
    JPanel f10242h;

    /* renamed from: k, reason: collision with root package name */
    private String f10235k = "";

    /* renamed from: b, reason: collision with root package name */
    DefaultListModel f10236b = new DefaultListModel();

    /* renamed from: c, reason: collision with root package name */
    JList f10237c = new JList(this.f10236b);

    /* renamed from: d, reason: collision with root package name */
    DefaultListModel f10238d = new DefaultListModel();

    /* renamed from: e, reason: collision with root package name */
    JList f10239e = new JList(this.f10238d);

    /* renamed from: g, reason: collision with root package name */
    List f10241g = new ArrayList();

    /* renamed from: i, reason: collision with root package name */
    JLabel f10243i = new JLabel(" ");

    /* renamed from: j, reason: collision with root package name */
    long f10244j = 0;

    /* renamed from: l, reason: collision with root package name */
    private boolean f10245l = false;

    public A() {
        this.f10234a = null;
        this.f10240f = new ArrayList();
        String[] strArrD = G.T.a().d();
        this.f10234a = new G.R[strArrD.length];
        for (int i2 = 0; i2 < strArrD.length; i2++) {
            this.f10234a[i2] = G.T.a().c(strArrD[i2]);
        }
        Iterator<E> it = ac.r.a(this.f10234a).iterator();
        while (it.hasNext()) {
            this.f10240f.add(new F(this, (ac.q) it.next()));
        }
        this.f10240f = a(this.f10240f);
        setLayout(new BorderLayout(eJ.a(12), eJ.a(12)));
        setBorder(BorderFactory.createTitledBorder(C1818g.b("Profile Data Log Fields")));
        int iA = eJ.a(180);
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new BorderLayout(eJ.a(5), eJ.a(5)));
        JScrollPane jScrollPane = new JScrollPane(this.f10237c);
        jScrollPane.setBorder(BorderFactory.createLoweredBevelBorder());
        jScrollPane.setPreferredSize(new Dimension(iA, iA));
        jPanel.add("North", new JLabel(C1818g.b("Not Logged"), 0));
        jPanel.add(BorderLayout.CENTER, jScrollPane);
        jPanel.add("West", new JLabel(""));
        add("West", jPanel);
        JPanel jPanel2 = new JPanel();
        jPanel2.setLayout(new BorderLayout(eJ.a(5), eJ.a(5)));
        JScrollPane jScrollPane2 = new JScrollPane(this.f10239e);
        jScrollPane2.setBorder(BorderFactory.createLoweredBevelBorder());
        jScrollPane2.setPreferredSize(new Dimension(iA, iA));
        jPanel2.add("North", new JLabel(C1818g.b("Logged Fields"), 0));
        jPanel2.add(BorderLayout.CENTER, jScrollPane2);
        jPanel2.add("East", new JLabel(""));
        add("East", jPanel2);
        new JPanel().setLayout(new FlowLayout());
        this.f10242h = new JPanel();
        Dimension dimensionA = eJ.a(60, 25);
        this.f10242h.setLayout(new GridLayout(0, 1, eJ.a(4), eJ.a(4)));
        this.f10242h.add(new JLabel(" "));
        JButton jButton = new JButton(">>");
        jButton.setPreferredSize(dimensionA);
        jButton.setMnemonic(65);
        jButton.setToolTipText(C1818g.b("Add All"));
        jButton.addActionListener(new B(this));
        this.f10242h.add(jButton);
        JButton jButton2 = new JButton(">");
        jButton2.setPreferredSize(dimensionA);
        jButton2.setMnemonic(160);
        jButton2.setToolTipText(C1818g.b("Add selected fields"));
        jButton2.addActionListener(new C(this));
        this.f10242h.add(jButton2);
        JButton jButton3 = new JButton("<");
        jButton3.setPreferredSize(dimensionA);
        jButton3.setMnemonic(153);
        jButton3.setToolTipText(C1818g.b("Remove selected fields"));
        jButton3.addActionListener(new D(this));
        this.f10242h.add(jButton3);
        JButton jButton4 = new JButton("<<");
        jButton4.setPreferredSize(dimensionA);
        jButton4.setToolTipText(C1818g.b("Remove All"));
        jButton4.setMnemonic(82);
        jButton4.addActionListener(new E(this));
        this.f10242h.add(jButton4);
        add(BorderLayout.CENTER, this.f10242h);
    }

    private boolean b(String str) {
        return str.equals("Time") || str.equals("Engine");
    }

    private void j() {
        this.f10236b.clear();
        this.f10238d.clear();
        for (F f2 : this.f10240f) {
            if (ac.r.a(G.T.a().c(f2.a().g()), f2.a().b())) {
                if (f2.a().a().contains("Oil")) {
                    bH.C.c(f2.a().a());
                }
                if (this.f10241g == null || (this.f10241g.contains(f2.a().a()) && !b(f2.a().a()))) {
                    this.f10236b.addElement(f2);
                } else {
                    this.f10238d.addElement(f2);
                }
            }
        }
    }

    public void c() {
        this.f10244j = System.currentTimeMillis();
        for (int i2 : this.f10237c.getSelectedIndices()) {
            if (this.f10241g.remove(((F) this.f10236b.getElementAt(i2)).a().a())) {
            }
        }
        j();
    }

    public void d() {
        this.f10241g.clear();
        j();
    }

    public void e() {
        int[] selectedIndices = this.f10239e.getSelectedIndices();
        if (selectedIndices == null) {
            return;
        }
        for (int length = selectedIndices.length - 1; length >= 0; length--) {
            F f2 = (F) this.f10238d.getElementAt(selectedIndices[length]);
            if (!b(f2.f10250b.a()) && !this.f10241g.contains(f2.a().a())) {
                this.f10241g.add(this.f10241g.size(), f2.a().a());
            }
        }
        j();
    }

    @Override // java.awt.Container
    public void removeAll() {
        for (F f2 : this.f10240f) {
            if (!b(f2.a().a())) {
                this.f10241g.add(f2.a().a());
            }
        }
        j();
    }

    public String f() {
        return this.f10235k;
    }

    public void a(String str, String[] strArr) {
        this.f10235k = str;
        this.f10241g.clear();
        for (String str2 : strArr) {
            String strTrim = str2.trim();
            if (!this.f10241g.contains(strTrim)) {
                this.f10241g.add(strTrim);
            }
        }
        j();
    }

    public void a(boolean z2) {
        C1685fp.a((Component) this.f10242h, z2);
    }

    public void g() {
        this.f10245l = true;
    }

    public void h() {
        this.f10245l = false;
    }

    public String[] i() {
        return (String[]) this.f10241g.toArray(new String[this.f10241g.size()]);
    }

    public List a(List list) {
        for (int i2 = 0; i2 < list.size(); i2++) {
            for (int i3 = i2 + 1; i3 < list.size(); i3++) {
                F f2 = (F) list.get(i2);
                F f3 = (F) list.get(i3);
                if (f2.c().toLowerCase().compareTo(f3.c().toLowerCase()) > 0) {
                    list.set(i2, f3);
                    list.set(i3, f2);
                }
            }
        }
        return list;
    }
}
