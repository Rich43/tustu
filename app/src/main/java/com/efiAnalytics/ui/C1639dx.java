package com.efiAnalytics.ui;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;

/* renamed from: com.efiAnalytics.ui.dx, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:com/efiAnalytics/ui/dx.class */
class C1639dx extends FlowLayout {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1634ds f11455a;

    C1639dx(C1634ds c1634ds) {
        this.f11455a = c1634ds;
    }

    @Override // java.awt.FlowLayout, java.awt.LayoutManager
    public void layoutContainer(Container container) {
        Component component = container.getComponent(0);
        if (component != null) {
            Dimension preferredSize = component.getPreferredSize();
            Dimension size = container.getSize();
            int i2 = (size.width - preferredSize.width) / 2;
            int i3 = (size.height - preferredSize.height) / 2;
            int i4 = size.width > preferredSize.width ? preferredSize.width : size.width;
            int i5 = size.height > preferredSize.height ? preferredSize.height : size.height;
            if (i3 < 1) {
                i3 = 0;
            }
            if (i2 < 1) {
                i2 = 0;
            }
            component.setBounds(i2, i3, i4, i5);
        }
    }
}
