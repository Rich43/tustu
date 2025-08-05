package com.efiAnalytics.ui;

import java.awt.Component;
import java.awt.KeyEventDispatcher;
import java.awt.Window;
import java.awt.event.KeyEvent;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/ui/dH.class */
class dH implements KeyEventDispatcher {

    /* renamed from: a, reason: collision with root package name */
    Component f11328a;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ dF f11329b;

    dH(dF dFVar, Component component) {
        this.f11329b = dFVar;
        this.f11328a = null;
        this.f11328a = component;
    }

    @Override // java.awt.KeyEventDispatcher
    public boolean dispatchKeyEvent(KeyEvent keyEvent) {
        if (!b(keyEvent) || keyEvent.getID() != 401) {
            return false;
        }
        switch (keyEvent.getKeyCode()) {
            case 27:
                if (a(keyEvent)) {
                    return false;
                }
                this.f11329b.k();
                this.f11329b.close();
                return true;
            default:
                return false;
        }
    }

    private boolean a(KeyEvent keyEvent) {
        return (keyEvent.getSource() instanceof Cdo) && (((Component) keyEvent.getSource()).getParent() instanceof BinTableView);
    }

    private boolean b(KeyEvent keyEvent) {
        Component component = keyEvent.getComponent();
        while (true) {
            Component component2 = component;
            if (component2 == null) {
                return false;
            }
            if (component2.equals(this.f11328a)) {
                return true;
            }
            if (component2 instanceof Window) {
                return false;
            }
            component = component2.getParent();
        }
    }
}
