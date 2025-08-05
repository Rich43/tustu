package aP;

import java.awt.Component;
import java.awt.Container;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import n.InterfaceC1761a;

/* loaded from: TunerStudioMS.jar:aP/iW.class */
public class iW implements ChangeListener {

    /* renamed from: a, reason: collision with root package name */
    n.n f3692a;

    /* renamed from: b, reason: collision with root package name */
    Component f3693b = null;

    public iW(n.n nVar) {
        this.f3692a = nVar;
    }

    @Override // javax.swing.event.ChangeListener
    public void stateChanged(ChangeEvent changeEvent) {
        Component selectedComponent = this.f3692a.getSelectedComponent();
        if (this.f3693b != null && !this.f3693b.equals(selectedComponent)) {
            b(this.f3693b);
        }
        this.f3693b = selectedComponent;
        a(selectedComponent);
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void a(Component component) {
        if ((component instanceof InterfaceC1761a) && !((InterfaceC1761a) component).a() && this.f3692a.getTabCount() > 0) {
            this.f3692a.setSelectedIndex(0);
        }
        if (component instanceof Container) {
            Container container = (Container) component;
            for (int i2 = 0; i2 < container.getComponentCount(); i2++) {
                a(container.getComponent(i2));
            }
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void b(Component component) {
        if (component instanceof n.g) {
            ((n.g) component).b();
        }
        if (component instanceof Container) {
            Container container = (Container) component;
            for (int i2 = 0; i2 < container.getComponentCount(); i2++) {
                b(container.getComponent(i2));
            }
        }
    }
}
