package be;

import java.awt.Component;
import javax.swing.JPanel;

/* loaded from: TunerStudioMS.jar:be/W.class */
class W extends JPanel {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ T f7950a;

    W(T t2) {
        this.f7950a = t2;
    }

    @Override // javax.swing.JComponent, java.awt.Component
    public Component.BaselineResizeBehavior getBaselineResizeBehavior() {
        return Component.BaselineResizeBehavior.CONSTANT_ASCENT;
    }

    @Override // javax.swing.JComponent, java.awt.Component
    public int getBaseline(int i2, int i3) {
        return 0;
    }
}
