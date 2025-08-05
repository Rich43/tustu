package com.efiAnalytics.ui;

import java.awt.Component;
import java.awt.Graphics;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;

/* renamed from: com.efiAnalytics.ui.m, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:com/efiAnalytics/ui/m.class */
class C1695m extends DefaultListCellRenderer {

    /* renamed from: a, reason: collision with root package name */
    boolean f11728a = false;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ C1642e f11729b;

    C1695m(C1642e c1642e) {
        this.f11729b = c1642e;
    }

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    public void paint(Graphics graphics) {
        super.paint(graphics);
        if (this.f11729b.f11468g == null || !this.f11728a) {
            return;
        }
        int iA = eJ.a(9);
        int width = getWidth() - eJ.a(12);
        int height = (getHeight() - iA) / 2;
        graphics.setColor(this.f11729b.f11468g);
        graphics.fillRect(width, height, iA, iA);
    }

    @Override // javax.swing.DefaultListCellRenderer, javax.swing.ListCellRenderer
    public Component getListCellRendererComponent(JList jList, Object obj, int i2, boolean z2, boolean z3) {
        JList.DropLocation dropLocation = jList.getDropLocation();
        if (dropLocation == null || dropLocation.isInsert() || dropLocation.getIndex() == i2) {
        }
        this.f11728a = i2 < 0;
        return super.getListCellRendererComponent(jList, obj, i2, z2, z3);
    }
}
