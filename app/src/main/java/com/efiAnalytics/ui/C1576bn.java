package com.efiAnalytics.ui;

import java.awt.Component;
import java.awt.KeyEventDispatcher;
import java.awt.event.KeyEvent;

/* renamed from: com.efiAnalytics.ui.bn, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:com/efiAnalytics/ui/bn.class */
class C1576bn implements KeyEventDispatcher {

    /* renamed from: a, reason: collision with root package name */
    Component f11009a;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ C1571bi f11010b;

    public C1576bn(C1571bi c1571bi, Component component) {
        this.f11010b = c1571bi;
        this.f11009a = null;
        this.f11009a = component;
    }

    @Override // java.awt.KeyEventDispatcher
    public boolean dispatchKeyEvent(KeyEvent keyEvent) {
        if (!a(keyEvent) || keyEvent.getID() != 401 || keyEvent.getKeyCode() != 27) {
            return false;
        }
        this.f11010b.b();
        return true;
    }

    private boolean a(KeyEvent keyEvent) {
        Component component = keyEvent.getComponent();
        while (true) {
            Component component2 = component;
            if (component2 == null) {
                return false;
            }
            if (component2.equals(this.f11009a)) {
                return true;
            }
            component = component2.getParent();
        }
    }
}
