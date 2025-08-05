package br;

import java.awt.Component;
import java.awt.Container;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import n.InterfaceC1761a;

/* renamed from: br.M, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:br/M.class */
class C1235M implements ChangeListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1233K f8368a;

    C1235M(C1233K c1233k) {
        this.f8368a = c1233k;
    }

    @Override // javax.swing.event.ChangeListener
    public void stateChanged(ChangeEvent changeEvent) {
        a(this.f8368a.getSelectedComponent());
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void a(Component component) {
        if (component instanceof InterfaceC1761a) {
            ((InterfaceC1761a) component).a();
        }
        if (component instanceof Container) {
            Container container = (Container) component;
            for (int i2 = 0; i2 < container.getComponentCount(); i2++) {
                a(container.getComponent(i2));
            }
        }
    }
}
