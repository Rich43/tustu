package com.efiAnalytics.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/* renamed from: com.efiAnalytics.ui.ei, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:com/efiAnalytics/ui/ei.class */
public class C1651ei extends JPanel implements ListSelectionListener {

    /* renamed from: d, reason: collision with root package name */
    JButton f11581d;

    /* renamed from: e, reason: collision with root package name */
    JButton f11582e;

    /* renamed from: f, reason: collision with root package name */
    JButton f11583f;

    /* renamed from: g, reason: collision with root package name */
    JButton f11584g;

    /* renamed from: j, reason: collision with root package name */
    public static String f11587j = "Add Item";

    /* renamed from: k, reason: collision with root package name */
    public static String f11588k = "Delete Item";

    /* renamed from: l, reason: collision with root package name */
    public static String f11589l = "Raise Item Index";

    /* renamed from: m, reason: collision with root package name */
    public static String f11590m = "Lower Item Index";

    /* renamed from: a, reason: collision with root package name */
    JList f11578a = new JList();

    /* renamed from: b, reason: collision with root package name */
    C1656en f11579b = new C1656en(this);

    /* renamed from: c, reason: collision with root package name */
    int f11580c = 19;

    /* renamed from: h, reason: collision with root package name */
    Object f11585h = null;

    /* renamed from: i, reason: collision with root package name */
    ArrayList f11586i = new ArrayList();

    public C1651ei() {
        this.f11578a.setVisibleRowCount(4);
        this.f11578a.setModel(this.f11579b);
        this.f11578a.addListSelectionListener(this);
        setLayout(new BorderLayout(4, 4));
        JScrollPane jScrollPane = new JScrollPane();
        jScrollPane.getViewport().setView(this.f11578a);
        jScrollPane.setBorder(BorderFactory.createLoweredBevelBorder());
        add(BorderLayout.CENTER, jScrollPane);
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new GridLayout(0, 1, 2, 2));
        this.f11583f = new JButton(null, new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getResource("resources/plus.gif"))));
        this.f11583f.setToolTipText(f11587j);
        this.f11583f.addActionListener(new C1652ej(this));
        this.f11583f.setPreferredSize(new Dimension(this.f11580c, this.f11580c));
        jPanel.add(this.f11583f);
        this.f11584g = new JButton(null, new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getResource("resources/delete.gif"))));
        this.f11584g.setToolTipText(f11588k);
        this.f11584g.addActionListener(new C1653ek(this));
        this.f11584g.setPreferredSize(new Dimension(this.f11580c, this.f11580c));
        jPanel.add(this.f11584g);
        this.f11581d = new JButton(null, new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getResource("resources/up.gif"))));
        this.f11581d.setToolTipText(f11589l);
        this.f11581d.addActionListener(new C1654el(this));
        this.f11581d.setPreferredSize(new Dimension(this.f11580c, this.f11580c));
        jPanel.add(this.f11581d);
        this.f11582e = new JButton(null, new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getResource("resources/down.gif"))));
        this.f11582e.setToolTipText(f11590m);
        this.f11582e.addActionListener(new C1655em(this));
        this.f11582e.setPreferredSize(new Dimension(this.f11580c, this.f11580c));
        jPanel.add(this.f11582e);
        add("East", jPanel);
        f();
    }

    private void f() {
        this.f11581d.setEnabled(this.f11578a.getSelectedValue() != null && this.f11578a.getSelectedIndex() > 0);
        this.f11582e.setEnabled(this.f11578a.getSelectedValue() != null && this.f11578a.getSelectedIndex() < this.f11579b.getSize() - 1);
        this.f11584g.setEnabled(this.f11578a.getSelectedValue() != null);
    }

    public void a(Object obj) {
        this.f11579b.add(this.f11579b.getSize(), obj);
    }

    public void b(Object obj) {
        this.f11578a.setSelectedValue(obj, true);
    }

    public void a(int i2) {
        this.f11579b.remove(i2);
        f();
    }

    public Object[] a() {
        return this.f11579b.toArray();
    }

    protected void b() {
        g();
        f();
    }

    protected void c() {
        if (h()) {
            this.f11579b.remove(this.f11578a.getSelectedIndex());
            f();
        }
    }

    protected void d() {
        if (this.f11578a.getSelectedValue() == null || this.f11578a.getSelectedIndex() <= 0 || !a(this.f11578a.getSelectedValue(), this.f11578a.getSelectedIndex() - 1, this.f11578a.getSelectedIndex())) {
            return;
        }
        Object obj = this.f11579b.get(this.f11578a.getSelectedIndex());
        int selectedIndex = this.f11578a.getSelectedIndex();
        this.f11579b.remove(this.f11578a.getSelectedIndex());
        this.f11579b.insertElementAt(obj, selectedIndex - 1);
        this.f11578a.setSelectedValue(obj, true);
        f();
    }

    protected void e() {
        if (this.f11578a.getSelectedValue() == null || this.f11578a.getSelectedIndex() >= this.f11579b.getSize() - 1 || !a(this.f11578a.getSelectedValue(), this.f11578a.getSelectedIndex() - 1, this.f11578a.getSelectedIndex())) {
            return;
        }
        Object obj = this.f11579b.get(this.f11578a.getSelectedIndex());
        int selectedIndex = this.f11578a.getSelectedIndex();
        this.f11579b.remove(this.f11578a.getSelectedIndex());
        this.f11579b.insertElementAt(obj, selectedIndex + 1);
        this.f11578a.setSelectedValue(obj, true);
        f();
    }

    private void g() {
        Iterator it = this.f11586i.iterator();
        while (it.hasNext()) {
            ((InterfaceC1650eh) it.next()).a((DefaultListModel) this.f11579b);
        }
    }

    private boolean h() {
        if (this.f11578a.getSelectedValue() == null) {
            return false;
        }
        Iterator it = this.f11586i.iterator();
        while (it.hasNext()) {
            if (!((InterfaceC1650eh) it.next()).a(this.f11578a.getSelectedValue(), this.f11578a.getSelectedIndex())) {
                return false;
            }
        }
        return true;
    }

    private boolean a(Object obj, int i2, int i3) {
        Iterator it = this.f11586i.iterator();
        while (it.hasNext()) {
            if (!((InterfaceC1650eh) it.next()).a(obj, i2, i3)) {
                return false;
            }
        }
        return true;
    }

    private void c(Object obj) {
        Iterator it = this.f11586i.iterator();
        while (it.hasNext()) {
            ((InterfaceC1650eh) it.next()).a(obj);
        }
    }

    private void d(Object obj) {
        Iterator it = this.f11586i.iterator();
        while (it.hasNext()) {
            ((InterfaceC1650eh) it.next()).b(obj);
        }
    }

    public void a(InterfaceC1650eh interfaceC1650eh) {
        this.f11586i.add(interfaceC1650eh);
    }

    @Override // javax.swing.event.ListSelectionListener
    public void valueChanged(ListSelectionEvent listSelectionEvent) {
        if (this.f11578a.getSelectedValue() == null) {
            int firstIndex = listSelectionEvent.getFirstIndex();
            if (firstIndex < this.f11579b.getSize()) {
                d(this.f11579b.getElementAt(firstIndex));
            } else {
                d(null);
            }
            this.f11585h = null;
        } else {
            Object selectedValue = this.f11578a.getSelectedValue();
            if (listSelectionEvent.getValueIsAdjusting()) {
                if (this.f11585h != null) {
                    d(this.f11585h);
                }
                c(selectedValue);
                this.f11585h = selectedValue;
            }
        }
        f();
    }
}
