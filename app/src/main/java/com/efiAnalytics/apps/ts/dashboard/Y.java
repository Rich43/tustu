package com.efiAnalytics.apps.ts.dashboard;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager2;
import java.io.Serializable;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/apps/ts/dashboard/Y.class */
public class Y implements LayoutManager2, Serializable {
    @Override // java.awt.LayoutManager
    public void layoutContainer(Container container) {
        Component[] components = container.getComponents();
        for (int i2 = 0; i2 < components.length; i2++) {
            if (components[i2] instanceof AbstractC1420s) {
                AbstractC1420s abstractC1420s = (AbstractC1420s) components[i2];
                abstractC1420s.setBounds((int) Math.round(container.getWidth() * abstractC1420s.getRelativeX()), (int) Math.round(container.getHeight() * abstractC1420s.getRelativeY()), (int) Math.round(container.getWidth() * abstractC1420s.getRelativeWidth()), (int) Math.round(container.getHeight() * abstractC1420s.getRelativeHeight()));
                abstractC1420s.validate();
            }
        }
    }

    @Override // java.awt.LayoutManager
    public Dimension preferredLayoutSize(Container container) {
        return new Dimension(320, 260);
    }

    @Override // java.awt.LayoutManager
    public Dimension minimumLayoutSize(Container container) {
        return new Dimension(320, 240);
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
