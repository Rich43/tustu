package ao;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager;

/* loaded from: TunerStudioMS.jar:ao/fS.class */
class fS implements LayoutManager {

    /* renamed from: a, reason: collision with root package name */
    Dimension f5717a = new Dimension(400, 400);

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ C0764fu f5718b;

    fS(C0764fu c0764fu) {
        this.f5718b = c0764fu;
    }

    @Override // java.awt.LayoutManager
    public void addLayoutComponent(String str, Component component) {
    }

    @Override // java.awt.LayoutManager
    public void layoutContainer(Container container) {
        this.f5718b.f5882t.setBounds(this.f5718b.i().getWidth() - 35, 0, 20, 20);
        this.f5718b.f5884v.setBounds(0, 0, 20, 20);
    }

    @Override // java.awt.LayoutManager
    public Dimension minimumLayoutSize(Container container) {
        return this.f5717a;
    }

    @Override // java.awt.LayoutManager
    public Dimension preferredLayoutSize(Container container) {
        return this.f5717a;
    }

    @Override // java.awt.LayoutManager
    public void removeLayoutComponent(Component component) {
    }
}
