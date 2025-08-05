package aP;

import java.awt.Component;
import java.awt.Dimension;
import javax.swing.JScrollPane;

/* renamed from: aP.hz, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:aP/hz.class */
class C0418hz extends JScrollPane {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C0416hx f3632a;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public C0418hz(C0416hx c0416hx, Component component) {
        super(component);
        this.f3632a = c0416hx;
    }

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    public Dimension getPreferredSize() {
        Dimension preferredSize = super.getPreferredSize();
        preferredSize.width += 25;
        return preferredSize;
    }
}
