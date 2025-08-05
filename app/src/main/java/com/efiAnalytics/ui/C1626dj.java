package com.efiAnalytics.ui;

import java.awt.Component;
import java.awt.Graphics;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;

/* renamed from: com.efiAnalytics.ui.dj, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:com/efiAnalytics/ui/dj.class */
class C1626dj extends DefaultListCellRenderer {

    /* renamed from: a, reason: collision with root package name */
    boolean f11423a = false;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ C1621de f11424b;

    C1626dj(C1621de c1621de) {
        this.f11424b = c1621de;
    }

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    public void paint(Graphics graphics) {
        super.paint(graphics);
        if (this.f11424b.f11415b == null || !this.f11423a) {
            return;
        }
        int iA = eJ.a(9);
        int width = getWidth() - eJ.a(12);
        int height = (getHeight() - iA) / 2;
        graphics.setColor(this.f11424b.f11415b);
        graphics.fillRect(width, height, iA, iA);
    }

    @Override // javax.swing.DefaultListCellRenderer, javax.swing.ListCellRenderer
    public Component getListCellRendererComponent(JList jList, Object obj, int i2, boolean z2, boolean z3) {
        JList.DropLocation dropLocation = jList.getDropLocation();
        if (dropLocation == null || dropLocation.isInsert() || dropLocation.getIndex() == i2) {
        }
        this.f11423a = i2 < 0;
        return super.getListCellRendererComponent(jList, obj, i2, z2, z3);
    }
}
