package bt;

import com.efiAnalytics.apps.ts.dashboard.Gauge;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager;

/* loaded from: TunerStudioMS.jar:bt/bH.class */
class bH implements LayoutManager {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ bG f8910a;

    bH(bG bGVar) {
        this.f8910a = bGVar;
    }

    @Override // java.awt.LayoutManager
    public void addLayoutComponent(String str, Component component) {
    }

    @Override // java.awt.LayoutManager
    public void removeLayoutComponent(Component component) {
    }

    @Override // java.awt.LayoutManager
    public Dimension preferredLayoutSize(Container container) {
        return container.getPreferredSize();
    }

    @Override // java.awt.LayoutManager
    public Dimension minimumLayoutSize(Container container) {
        return container.getMinimumSize();
    }

    @Override // java.awt.LayoutManager
    public void layoutContainer(Container container) {
        if (container.getComponentCount() <= 0 || !(container.getComponent(0) instanceof Gauge)) {
            return;
        }
        Gauge gauge = (Gauge) container.getComponent(0);
        double relativeWidth = (1.3333333730697632d * gauge.getRelativeWidth()) / gauge.getRelativeHeight();
        if (gauge.isShapeLockedToAspect()) {
            int iMin = Math.min(container.getHeight(), container.getWidth());
            gauge.setBounds((container.getWidth() - iMin) / 2, (container.getHeight() - iMin) / 2, iMin, iMin);
        } else if (relativeWidth > 1.0d) {
            int iRound = (int) Math.round(container.getHeight() / relativeWidth);
            gauge.setBounds(0, (container.getHeight() - iRound) / 2, container.getWidth(), iRound);
        } else {
            int iRound2 = (int) Math.round(container.getHeight() * relativeWidth);
            gauge.setBounds((container.getWidth() - iRound2) / 2, 0, iRound2, container.getHeight());
        }
    }
}
