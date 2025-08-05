package com.efiAnalytics.ui;

import java.awt.Component;
import java.awt.Dimension;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/* renamed from: com.efiAnalytics.ui.fl, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:com/efiAnalytics/ui/fl.class */
public class C1681fl extends JPanel {

    /* renamed from: a, reason: collision with root package name */
    bz.a f11678a = new bz.a(0, 5, eJ.a(6), eJ.a(6));

    /* renamed from: b, reason: collision with root package name */
    int f11679b = 160;

    /* renamed from: c, reason: collision with root package name */
    int f11680c = -1;

    public C1681fl() {
        String str;
        setLayout(this.f11678a);
        this.f11678a.a(false);
        str = "Y Axis Selectors";
        setBorder(BorderFactory.createTitledBorder(bV.a() != null ? bV.a().a(str) : "Y Axis Selectors"));
    }

    @Override // java.awt.Component
    public void setSize(int i2, int i3) {
        a(i2);
        super.setSize(i2, i3);
    }

    @Override // java.awt.Component
    public void setBounds(int i2, int i3, int i4, int i5) {
        a(i4);
        super.setBounds(i2, i3, i4, i5);
    }

    private int a() {
        int i2;
        int i3 = 30;
        Component[] components = getComponents();
        for (int i4 = 0; i4 < components.length; i4++) {
            if ((components[i4] instanceof C1683fn) && i3 < (i2 = ((C1683fn) components[i4]).getMinimumSize().width)) {
                i3 = i2;
            }
        }
        return i3 + (i3 > eJ.a(120) ? 0 : eJ.a(20));
    }

    public void a(int i2) {
        if (getComponentCount() > 0) {
            int iA = i2 / a();
            if (iA < 2) {
                iA = 2;
            }
            this.f11678a.b(iA);
            this.f11678a.a(0);
            if (iA != this.f11680c && getParent() != null) {
                SwingUtilities.invokeLater(new RunnableC1682fm(this));
            }
            this.f11680c = iA;
        }
    }

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    public Dimension getMinimumSize() {
        return getPreferredSize();
    }

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    public Dimension getPreferredSize() {
        Dimension preferredSize = super.getPreferredSize();
        preferredSize.width = 200;
        return preferredSize;
    }
}
