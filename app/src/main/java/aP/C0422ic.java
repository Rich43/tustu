package aP;

import java.awt.Component;
import java.awt.Dimension;
import javax.swing.JScrollPane;

/* renamed from: aP.ic, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:aP/ic.class */
class C0422ic extends JScrollPane {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C0421ib f3721a;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public C0422ic(C0421ib c0421ib, Component component) {
        super(component);
        this.f3721a = c0421ib;
    }

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    public Dimension getPreferredSize() {
        return new Dimension(100, 100);
    }
}
