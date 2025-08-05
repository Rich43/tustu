package com.efiAnalytics.ui;

import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JComboBox;
import javax.swing.ListCellRenderer;

/* renamed from: com.efiAnalytics.ui.de, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:com/efiAnalytics/ui/de.class */
public class C1621de extends JComboBox {

    /* renamed from: a, reason: collision with root package name */
    C1626dj f11414a = null;

    /* renamed from: b, reason: collision with root package name */
    Color f11415b = null;

    public C1621de() {
        super.setDoubleBuffered(false);
        super.addMouseListener(new C1622df(this));
        super.addFocusListener(new C1623dg(this));
        super.addKeyListener(new C1624dh(this));
    }

    public void a(String str) {
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
        if (getItemAt(i2) == null) {
            return null;
        }
        return getItemAt(i2).toString();
    }

    public void a(boolean z2) {
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

    @Override // javax.swing.JComponent, java.awt.Component
    public void repaint(long j2, int i2, int i3, int i4, int i5) {
        super.repaint(j2, i2, i3, i4, i5);
    }

    @Override // java.awt.Component
    public void repaint(int i2, int i3, int i4, int i5) {
        super.repaint(i2, i3, i4, i5);
    }

    @Override // javax.swing.JComboBox
    public ListCellRenderer getRenderer() {
        if (this.f11414a == null) {
            this.f11414a = new C1626dj(this);
        }
        return this.f11414a;
    }

    public boolean c(String str) {
        for (int i2 = 0; i2 < getItemCount(); i2++) {
            if (a(i2).equals(str)) {
                return true;
            }
        }
        return false;
    }
}
