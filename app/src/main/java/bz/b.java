package bz;

import bH.C;
import com.efiAnalytics.apps.ts.dashboard.C1425x;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager;

/* loaded from: TunerStudioMS.jar:bz/b.class */
public class b implements LayoutManager {
    private C1425x a(Container container) {
        if (container.getComponentCount() > 0 && (container.getComponent(0) instanceof C1425x)) {
            return (C1425x) container.getComponent(0);
        }
        C1425x c1425x = null;
        Component[] components = container.getComponents();
        int length = components.length;
        int i2 = 0;
        while (true) {
            if (i2 >= length) {
                break;
            }
            Component component = components[i2];
            if (component instanceof C1425x) {
                c1425x = (C1425x) component;
                break;
            }
            i2++;
        }
        return c1425x;
    }

    @Override // java.awt.LayoutManager
    public void addLayoutComponent(String str, Component component) {
    }

    @Override // java.awt.LayoutManager
    public void removeLayoutComponent(Component component) {
    }

    @Override // java.awt.LayoutManager
    public Dimension preferredLayoutSize(Container container) {
        C1425x c1425xA = a(container);
        return c1425xA == null ? new Dimension(1, 1) : c1425xA.getPreferredSize();
    }

    @Override // java.awt.LayoutManager
    public Dimension minimumLayoutSize(Container container) {
        C1425x c1425xA = a(container);
        return c1425xA == null ? new Dimension(1, 1) : c1425xA.getMinimumSize();
    }

    @Override // java.awt.LayoutManager
    public void layoutContainer(Container container) {
        Insets insets = container.getInsets();
        int width = insets.left;
        int height = insets.top;
        int width2 = (container.getWidth() - insets.left) - insets.right;
        int height2 = (container.getHeight() - insets.top) - insets.bottom;
        C1425x c1425xA = a(container);
        if (c1425xA == null) {
            if (container.getComponentCount() > 0) {
                C.b("The Parent of a Container using ClusterHolderLayout must only have a GaugeCluster.");
                return;
            }
            return;
        }
        if (c1425xA.G() && c1425xA.I() > 0.0d && c1425xA.H() > 0.0d) {
            double d2 = width2 / height2;
            double dH = c1425xA.H() / c1425xA.I();
            if (d2 > dH) {
                width2 = (int) Math.round(dH * height2);
                width = (container.getWidth() - width2) / 2;
            } else {
                height2 = (int) Math.round(width2 / dH);
                height = (container.getHeight() - height2) / 2;
            }
        }
        c1425xA.setBounds(width, height, width2, height2);
    }
}
