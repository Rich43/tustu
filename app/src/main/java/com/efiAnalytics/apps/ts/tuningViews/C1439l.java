package com.efiAnalytics.apps.ts.tuningViews;

import com.efiAnalytics.ui.eJ;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager2;

/* renamed from: com.efiAnalytics.apps.ts.tuningViews.l, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:com/efiAnalytics/apps/ts/tuningViews/l.class */
public class C1439l implements LayoutManager2 {
    @Override // java.awt.LayoutManager
    public void layoutContainer(Container container) {
        Component[] components = container.getComponents();
        Insets insets = container.getInsets();
        double width = (container.getWidth() - insets.left) - insets.right;
        double height = (container.getHeight() - insets.top) - insets.bottom;
        for (int i2 = 0; i2 < components.length; i2++) {
            if (components[i2] instanceof TuneViewComponent) {
                TuneViewComponent tuneViewComponent = (TuneViewComponent) components[i2];
                tuneViewComponent.setBounds(insets.left + ((int) Math.round(width * tuneViewComponent.getRelativeX())), insets.top + ((int) Math.round(height * tuneViewComponent.getRelativeY())), (int) Math.round(width * tuneViewComponent.getRelativeWidth()), (int) Math.round(height * tuneViewComponent.getRelativeHeight()));
                tuneViewComponent.validate();
            }
        }
    }

    @Override // java.awt.LayoutManager
    public Dimension preferredLayoutSize(Container container) {
        return new Dimension(eJ.a(320), eJ.a(260));
    }

    @Override // java.awt.LayoutManager
    public Dimension minimumLayoutSize(Container container) {
        return new Dimension(eJ.a(320), eJ.a(240));
    }

    @Override // java.awt.LayoutManager
    public void addLayoutComponent(String str, Component component) {
    }

    @Override // java.awt.LayoutManager
    public void removeLayoutComponent(Component component) {
    }

    @Override // java.awt.LayoutManager2
    public void addLayoutComponent(Component component, Object obj) {
    }

    @Override // java.awt.LayoutManager2
    public Dimension maximumLayoutSize(Container container) {
        return new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE);
    }

    @Override // java.awt.LayoutManager2
    public float getLayoutAlignmentX(Container container) {
        return 0.5f;
    }

    @Override // java.awt.LayoutManager2
    public float getLayoutAlignmentY(Container container) {
        return 0.5f;
    }

    @Override // java.awt.LayoutManager2
    public void invalidateLayout(Container container) {
    }
}
