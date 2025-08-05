package bt;

import com.efiAnalytics.ui.C1582bt;
import com.efiAnalytics.ui.eM;
import java.awt.Component;
import java.awt.KeyEventDispatcher;
import java.awt.event.KeyEvent;

/* loaded from: TunerStudioMS.jar:bt/bM.class */
public class bM implements KeyEventDispatcher {

    /* renamed from: a, reason: collision with root package name */
    Component f8916a;

    /* renamed from: b, reason: collision with root package name */
    eM f8917b;

    /* renamed from: c, reason: collision with root package name */
    C1582bt f8918c;

    public bM(Component component, C1582bt c1582bt) {
        this.f8916a = null;
        this.f8917b = null;
        this.f8918c = null;
        this.f8916a = component;
        this.f8918c = c1582bt;
        this.f8917b = this.f8918c.h();
    }

    @Override // java.awt.KeyEventDispatcher
    public boolean dispatchKeyEvent(KeyEvent keyEvent) {
        if (!a(keyEvent) || keyEvent.getID() != 401) {
            return false;
        }
        if (((keyEvent.getModifiersEx() & 64) == 64 && keyEvent.getKeyCode() == 38) || (((keyEvent.getModifiersEx() & 64) == 64 && keyEvent.getKeyCode() == 39) || keyEvent.getKeyCode() == 521 || keyEvent.getKeyCode() == 81 || keyEvent.getKeyCode() == 160 || keyEvent.getKeyCode() == 46)) {
            double dL = this.f8918c.l();
            if ((keyEvent.getModifiersEx() & 128) == 128) {
                if (this.f8918c.k()) {
                    this.f8917b.a(dL, this.f8918c.i());
                } else {
                    this.f8917b.a(this.f8918c.i());
                }
            } else if (this.f8918c.k()) {
                this.f8917b.a(dL, this.f8918c.j());
            } else {
                this.f8917b.a(this.f8918c.j());
            }
            this.f8917b.repaint();
            keyEvent.consume();
            return true;
        }
        if (((keyEvent.getModifiersEx() & 64) == 64 && keyEvent.getKeyCode() == 40) || (((keyEvent.getModifiersEx() & 64) == 64 && keyEvent.getKeyCode() == 37) || keyEvent.getKeyCode() == 45 || keyEvent.getKeyCode() == 153 || keyEvent.getKeyCode() == 87 || keyEvent.getKeyCode() == 44)) {
            double dL2 = this.f8918c.l();
            if ((keyEvent.getModifiersEx() & 128) == 128) {
                if (this.f8918c.k()) {
                    this.f8917b.a(dL2, -this.f8918c.i());
                } else {
                    this.f8917b.b(this.f8918c.i());
                }
            } else if (this.f8918c.k()) {
                this.f8917b.a(dL2, -this.f8918c.j());
            } else {
                this.f8917b.b(this.f8918c.j());
            }
            this.f8917b.repaint();
            keyEvent.consume();
            return true;
        }
        switch (keyEvent.getKeyCode()) {
            case 37:
                this.f8917b.c();
                keyEvent.consume();
                return true;
            case 38:
                this.f8917b.d();
                keyEvent.consume();
                return true;
            case 39:
                this.f8917b.b();
                keyEvent.consume();
                return true;
            case 40:
                this.f8917b.e();
                keyEvent.consume();
                return true;
            case 70:
                this.f8918c.g();
                keyEvent.consume();
                return true;
            case 71:
                this.f8918c.f();
                keyEvent.consume();
                return true;
            case 74:
                this.f8918c.b(this.f8917b.m() - 10);
                keyEvent.consume();
                return true;
            case 75:
                this.f8918c.a(this.f8917b.l() - 10);
                keyEvent.consume();
                return true;
            case 77:
                this.f8918c.a(this.f8917b.l() + 10);
                keyEvent.consume();
                return true;
            case 78:
                this.f8918c.b(this.f8917b.m() + 10);
                keyEvent.consume();
                return true;
            case 90:
                this.f8918c.a();
                keyEvent.consume();
                return true;
            default:
                return false;
        }
    }

    private boolean a(KeyEvent keyEvent) {
        Component component = keyEvent.getComponent();
        while (true) {
            Component component2 = component;
            if (component2 == null) {
                return false;
            }
            if (component2.equals(this.f8916a)) {
                return true;
            }
            component = component2.getParent();
        }
    }
}
