package com.efiAnalytics.ui;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/ui/fQ.class */
class fQ extends FlowLayout {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ fK f11658a;

    fQ(fK fKVar) {
        this.f11658a = fKVar;
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
