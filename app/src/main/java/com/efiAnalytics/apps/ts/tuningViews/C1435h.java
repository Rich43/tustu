package com.efiAnalytics.apps.ts.tuningViews;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager2;

/* renamed from: com.efiAnalytics.apps.ts.tuningViews.h, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:com/efiAnalytics/apps/ts/tuningViews/h.class */
public class C1435h implements LayoutManager2 {

    /* renamed from: a, reason: collision with root package name */
    TuneViewComponent f9780a;

    /* renamed from: b, reason: collision with root package name */
    C1434g f9781b;

    /* renamed from: c, reason: collision with root package name */
    Component f9782c = null;

    public C1435h(TuneViewComponent tuneViewComponent) {
        this.f9780a = tuneViewComponent;
        this.f9781b = new C1434g(tuneViewComponent);
        this.f9780a.add(this.f9781b);
    }

    @Override // java.awt.LayoutManager
    public void layoutContainer(Container container) {
        synchronized (container.getTreeLock()) {
            Insets insets = container.getInsets();
            int i2 = insets.top;
            int height = container.getHeight() - insets.bottom;
            int i3 = insets.left;
            int width = container.getWidth() - insets.right;
            if (this.f9782c != null) {
                this.f9782c.setBounds(i3, i2, width - i3, height - i2);
            }
            if (this.f9780a.isEditMode() && this.f9780a.isShieldedDuringEdit()) {
                this.f9781b.setBounds(i3, i2, width - i3, height - i2);
                if (this.f9780a.getBtnSelectEcuConfig() != null) {
                    Dimension preferredSize = this.f9780a.getBtnSelectEcuConfig().getPreferredSize();
                    this.f9780a.getBtnSelectEcuConfig().setBounds(this.f9780a.getWidth() - preferredSize.width, 0, preferredSize.width, preferredSize.height);
                }
            } else {
                this.f9781b.setBounds(0, 0, 0, 0);
            }
        }
    }

    @Override // java.awt.LayoutManager
    public Dimension preferredLayoutSize(Container container) {
        return this.f9780a.getPreferredSize();
    }

    @Override // java.awt.LayoutManager
    public Dimension minimumLayoutSize(Container container) {
        return this.f9780a.getMinimumSize();
    }

    @Override // java.awt.LayoutManager
    public void addLayoutComponent(String str, Component component) {
        if (this.f9780a.getBtnSelectEcuConfig() == null || !this.f9780a.getBtnSelectEcuConfig().equals(component)) {
            this.f9782c = component;
        }
    }

    @Override // java.awt.LayoutManager
    public void removeLayoutComponent(Component component) {
        if (this.f9782c == null || !this.f9782c.equals(component)) {
            return;
        }
        this.f9782c = null;
    }

    @Override // java.awt.LayoutManager2
    public void addLayoutComponent(Component component, Object obj) {
        if (this.f9780a.getBtnSelectEcuConfig() == null || !this.f9780a.getBtnSelectEcuConfig().equals(component)) {
            this.f9782c = component;
        }
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
