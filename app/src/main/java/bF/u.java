package bF;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager;

/* loaded from: TunerStudioMS.jar:bF/u.class */
class u implements LayoutManager {

    /* renamed from: b, reason: collision with root package name */
    private int f6886b = 0;

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C0973d f6887a;

    u(C0973d c0973d) {
        this.f6887a = c0973d;
    }

    @Override // java.awt.LayoutManager
    public void addLayoutComponent(String str, Component component) {
    }

    @Override // java.awt.LayoutManager
    public void removeLayoutComponent(Component component) {
    }

    @Override // java.awt.LayoutManager
    public Dimension preferredLayoutSize(Container container) {
        return this.f6887a.f6854g.getPreferredSize();
    }

    @Override // java.awt.LayoutManager
    public Dimension minimumLayoutSize(Container container) {
        return this.f6887a.f6854g.getMinimumSize();
    }

    @Override // java.awt.LayoutManager
    public void layoutContainer(Container container) {
        Dimension preferredSize = this.f6887a.f6854g.getPreferredSize();
        this.f6887a.f6854g.setBounds(0, this.f6886b, preferredSize.width, preferredSize.height);
    }

    public void a(int i2) {
        this.f6886b = i2;
    }
}
