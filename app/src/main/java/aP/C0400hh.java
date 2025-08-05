package aP;

import java.awt.Component;
import java.awt.KeyEventDispatcher;
import java.awt.event.KeyEvent;

/* renamed from: aP.hh, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:aP/hh.class */
class C0400hh implements KeyEventDispatcher {

    /* renamed from: a, reason: collision with root package name */
    Component f3576a;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ C0394hb f3577b;

    public C0400hh(C0394hb c0394hb, Component component) {
        this.f3577b = c0394hb;
        this.f3576a = null;
        this.f3576a = component;
    }

    @Override // java.awt.KeyEventDispatcher
    public boolean dispatchKeyEvent(KeyEvent keyEvent) {
        if (!a(keyEvent) || keyEvent.getID() != 401 || keyEvent.getKeyCode() != 112) {
            return false;
        }
        if (this.f3577b.f3557b.O().size() > 0 || this.f3577b.f3561f.size() > 0) {
            return this.f3577b.c();
        }
        return false;
    }

    private boolean a(KeyEvent keyEvent) {
        Component component = keyEvent.getComponent();
        while (true) {
            Component component2 = component;
            if (component2 == null) {
                return false;
            }
            if (component2.equals(this.f3576a)) {
                return true;
            }
            component = component2.getParent();
        }
    }
}
