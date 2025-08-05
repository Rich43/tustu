package com.efiAnalytics.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JComboBox;
import javax.swing.ListCellRenderer;
import javax.swing.SwingUtilities;

/* renamed from: com.efiAnalytics.ui.e, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:com/efiAnalytics/ui/e.class */
public class C1642e extends JComboBox {

    /* renamed from: a, reason: collision with root package name */
    private final InterfaceC1697o f11464a;

    /* renamed from: d, reason: collision with root package name */
    List f11465d = new ArrayList();

    /* renamed from: e, reason: collision with root package name */
    boolean f11466e = false;

    /* renamed from: f, reason: collision with root package name */
    C1695m f11467f = null;

    /* renamed from: g, reason: collision with root package name */
    Color f11468g = null;

    /* renamed from: h, reason: collision with root package name */
    C1696n f11469h = null;

    public C1642e() {
        super.setDoubleBuffered(false);
        this.f11464a = new C1694l(this);
        super.addMouseListener(new C1669f(this));
        super.addFocusListener(new C1689g(this));
        super.addPopupMenuListener(new C1690h(this));
        super.addKeyListener(new C1691i(this));
    }

    public void a(String str) {
        if (!this.f11466e) {
            this.f11465d.add(str.toString());
        }
        super.addItem(str);
    }

    public boolean b(String str) {
        for (int i2 = 0; i2 < getItemCount(); i2++) {
            if (((String) getItemAt(i2)).equals(str)) {
                setSelectedIndex(i2);
                return true;
            }
        }
        return false;
    }

    public String a(int i2) {
        return (String) getItemAt(i2);
    }

    public void a(boolean z2) {
    }

    public void a(Color color) {
        this.f11468g = color;
        repaint();
    }

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    public Dimension getPreferredSize() {
        Dimension preferredSize = super.getPreferredSize();
        preferredSize.width += 10;
        return preferredSize;
    }

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    public Dimension getMinimumSize() {
        Dimension minimumSize = super.getMinimumSize();
        minimumSize.width += 10;
        return minimumSize;
    }

    @Override // javax.swing.JComboBox
    public void setPopupVisible(boolean z2) {
        boolean z3 = false;
        if (super.isPopupVisible() && !z2) {
            z3 = true;
        }
        super.setPopupVisible(z2);
        if (z3) {
            a();
        }
    }

    @Override // javax.swing.JComponent, java.awt.Component
    public void repaint(long j2, int i2, int i3, int i4, int i5) {
        super.repaint(j2, i2, i3, i4, i5);
    }

    @Override // java.awt.Component
    public void repaint(int i2, int i3, int i4, int i5) {
        super.repaint(i2, i3, i4, i5);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void a() {
        c("");
    }

    public void c(String str) {
        if (isVisible() && isShowing()) {
            SwingUtilities.invokeLater(new RunnableC1693k(this, str));
        }
    }

    @Override // javax.swing.JComboBox
    public ListCellRenderer getRenderer() {
        if (this.f11467f == null) {
            this.f11467f = new C1695m(this);
        }
        return this.f11467f;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void c() {
        super.removeAllItems();
    }

    @Override // javax.swing.JComboBox
    public void addItem(Object obj) {
        if (!this.f11466e) {
            this.f11465d.add(obj.toString());
        }
        super.addItem(obj.toString());
    }

    @Override // javax.swing.JComboBox
    public void insertItemAt(Object obj, int i2) {
        if (!this.f11466e) {
            this.f11465d.add(i2, obj.toString());
        }
        super.insertItemAt(obj, i2);
    }

    @Override // javax.swing.JComboBox
    public void removeItem(Object obj) {
        if (!this.f11466e) {
            this.f11465d.remove(obj);
        }
        super.removeItem(obj);
    }

    @Override // javax.swing.JComboBox
    public void removeItemAt(int i2) {
        if (!this.f11466e) {
            this.f11465d.remove(i2);
        }
        super.removeItemAt(i2);
    }

    @Override // javax.swing.JComboBox
    public void removeAllItems() {
        if (!this.f11466e) {
            this.f11465d.clear();
        }
        super.removeAllItems();
    }

    public String[] b() {
        String[] strArr = new String[getItemCount()];
        for (int i2 = 0; i2 < strArr.length; i2++) {
            strArr[i2] = a(i2);
        }
        return strArr;
    }
}
