package com.efiAnalytics.ui;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.io.Serializable;
import java.util.ArrayList;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/ui/cR.class */
public class cR implements LayoutManager, Serializable {

    /* renamed from: a, reason: collision with root package name */
    int f11154a;

    /* renamed from: b, reason: collision with root package name */
    int f11155b;

    /* renamed from: c, reason: collision with root package name */
    int f11156c;

    /* renamed from: d, reason: collision with root package name */
    int f11157d;

    public cR() {
        this(1, 0, 0, 0);
    }

    public cR(int i2, int i3, int i4, int i5) {
        if (i2 == 0 && i3 == 0) {
            throw new IllegalArgumentException("rows and cols cannot both be zero");
        }
        this.f11156c = i2;
        this.f11157d = i3;
        this.f11154a = i4;
        this.f11155b = i5;
    }

    public void a(int i2) {
        if (i2 == 0 && this.f11157d == 0) {
            throw new IllegalArgumentException("rows and cols cannot both be zero");
        }
        this.f11156c = i2;
    }

    public void b(int i2) {
        if (i2 == 0 && this.f11156c == 0) {
            throw new IllegalArgumentException("rows and cols cannot both be zero");
        }
        this.f11157d = i2;
    }

    @Override // java.awt.LayoutManager
    public void addLayoutComponent(String str, Component component) {
    }

    @Override // java.awt.LayoutManager
    public void removeLayoutComponent(Component component) {
    }

    @Override // java.awt.LayoutManager
    public Dimension preferredLayoutSize(Container container) {
        Dimension dimension;
        synchronized (container.getTreeLock()) {
            Insets insets = container.getInsets();
            int componentCount = container.getComponentCount();
            int i2 = this.f11156c;
            int i3 = this.f11157d;
            if (i2 > 0) {
                i3 = ((componentCount + i2) - 1) / i2;
            } else {
                i2 = ((componentCount + i3) - 1) / i3;
            }
            int i4 = 0;
            int i5 = 0;
            for (int i6 = 0; i6 < componentCount; i6++) {
                Dimension preferredSize = container.getComponent(i6).getPreferredSize();
                if (i4 < preferredSize.width) {
                    i4 = preferredSize.width;
                }
                if (i5 < preferredSize.height) {
                    i5 = preferredSize.height;
                }
            }
            dimension = new Dimension(insets.left + insets.right + (i3 * i4) + ((i3 - 1) * this.f11154a), insets.top + insets.bottom + (i2 * i5) + ((i2 - 1) * this.f11155b));
        }
        return dimension;
    }

    @Override // java.awt.LayoutManager
    public Dimension minimumLayoutSize(Container container) {
        Dimension dimension;
        synchronized (container.getTreeLock()) {
            Insets insets = container.getInsets();
            int componentCount = container.getComponentCount();
            int i2 = this.f11156c;
            int i3 = this.f11157d;
            if (i2 > 0) {
                i3 = ((componentCount + i2) - 1) / i2;
            } else {
                i2 = ((componentCount + i3) - 1) / i3;
            }
            int i4 = 0;
            int i5 = 0;
            for (int i6 = 0; i6 < componentCount; i6++) {
                Dimension minimumSize = container.getComponent(i6).getMinimumSize();
                if (i4 < minimumSize.width) {
                    i4 = minimumSize.width;
                }
                if (i5 < minimumSize.height) {
                    i5 = minimumSize.height;
                }
            }
            dimension = new Dimension(insets.left + insets.right + (i3 * i4) + ((i3 - 1) * this.f11154a), insets.top + insets.bottom + (i2 * i5) + ((i2 - 1) * this.f11155b));
        }
        return dimension;
    }

    private ArrayList a(Container container) {
        Component[] components = container.getComponents();
        ArrayList arrayList = new ArrayList();
        for (Component component : components) {
            if (component.isVisible()) {
                arrayList.add(component);
            }
        }
        return arrayList;
    }

    @Override // java.awt.LayoutManager
    public void layoutContainer(Container container) {
        synchronized (container.getTreeLock()) {
            Insets insets = container.getInsets();
            ArrayList arrayListA = a(container);
            int size = arrayListA.size();
            int i2 = this.f11156c;
            int i3 = this.f11157d;
            boolean zIsLeftToRight = container.getComponentOrientation().isLeftToRight();
            if (size == 0) {
                return;
            }
            if (i2 > 0) {
                i3 = ((size + i2) - 1) / i2;
            } else {
                i2 = ((size + i3) - 1) / i3;
            }
            int i4 = (i3 - 1) * this.f11154a;
            int width = container.getWidth() - (insets.left + insets.right);
            int i5 = (width - i4) / i3;
            int i6 = (width - ((i5 * i3) + i4)) / 2;
            int i7 = (i2 - 1) * this.f11155b;
            int height = container.getHeight() - (insets.top + insets.bottom);
            int i8 = (height - i7) / i2;
            int i9 = (height - ((i8 * i2) + i7)) / 2;
            if (zIsLeftToRight) {
                int i10 = 0;
                int i11 = insets.left + i6;
                while (i10 < i3) {
                    int i12 = 0;
                    int i13 = insets.top + i9;
                    while (i12 < i2) {
                        int i14 = (i12 * i3) + i10;
                        if (i14 < size) {
                            ((Component) arrayListA.get(i14)).setBounds(i11, i13, i5, i8);
                        }
                        i12++;
                        i13 += i8 + this.f11155b;
                    }
                    i10++;
                    i11 += i5 + this.f11154a;
                }
            } else {
                int i15 = 0;
                int width2 = ((container.getWidth() - insets.right) - i5) - i6;
                while (i15 < i3) {
                    int i16 = 0;
                    int i17 = insets.top + i9;
                    while (i16 < i2) {
                        int i18 = (i16 * i3) + i15;
                        if (i18 < size) {
                            ((Component) arrayListA.get(i18)).setBounds(width2, i17, i5, i8);
                        }
                        i16++;
                        i17 += i8 + this.f11155b;
                    }
                    i15++;
                    width2 -= i5 + this.f11154a;
                }
            }
        }
    }

    public String toString() {
        return getClass().getName() + "[hgap=" + this.f11154a + ",vgap=" + this.f11155b + ",rows=" + this.f11156c + ",cols=" + this.f11157d + "]";
    }
}
