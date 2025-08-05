package com.efiAnalytics.apps.ts.dashboard;

import java.awt.Component;
import java.awt.Container;
import java.awt.KeyEventDispatcher;
import java.awt.Window;
import java.awt.event.KeyEvent;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/apps/ts/dashboard/T.class */
public class T implements KeyEventDispatcher {

    /* renamed from: a, reason: collision with root package name */
    C1425x f9407a;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ C1425x f9408b;

    T(C1425x c1425x, C1425x c1425x2) {
        this.f9408b = c1425x;
        this.f9407a = null;
        this.f9407a = c1425x2;
    }

    @Override // java.awt.KeyEventDispatcher
    public boolean dispatchKeyEvent(KeyEvent keyEvent) {
        if (!this.f9408b.L() || !a(keyEvent.getSource()) || keyEvent.getID() != 401) {
            return false;
        }
        switch (keyEvent.getKeyCode()) {
            case 9:
                if (keyEvent.getModifiers() != 64 && keyEvent.getModifiers() != 1) {
                    this.f9408b.Y();
                    break;
                } else {
                    this.f9408b.Z();
                    break;
                }
                break;
            case 37:
                if (keyEvent.getModifiers() != 1) {
                    this.f9408b.Q();
                    break;
                } else {
                    this.f9408b.U();
                    break;
                }
            case 38:
                if (keyEvent.getModifiers() != 1) {
                    this.f9408b.R();
                    break;
                } else {
                    this.f9408b.W();
                    break;
                }
            case 39:
                if (keyEvent.getModifiers() != 64 && keyEvent.getModifiers() != 1) {
                    this.f9408b.P();
                    break;
                } else {
                    this.f9408b.T();
                    break;
                }
            case 40:
                if (keyEvent.getModifiers() != 1) {
                    this.f9408b.S();
                    break;
                } else {
                    this.f9408b.V();
                    break;
                }
            case 127:
                this.f9408b.a();
                break;
            default:
                if (this.f9408b.L()) {
                    if (keyEvent.getModifiers() == 2 && keyEvent.getKeyCode() == 67) {
                        this.f9408b.ap();
                    }
                    if (keyEvent.getModifiers() == 2 && keyEvent.getKeyCode() == 86) {
                        this.f9408b.aq();
                    }
                    if (keyEvent.getModifiers() == 2 && keyEvent.getKeyCode() == 65) {
                        this.f9408b.w();
                        break;
                    }
                }
                break;
        }
        return true;
    }

    private boolean a(Object obj) {
        Container parent = this.f9408b.getParent();
        if (this.f9407a.equals(obj)) {
            return true;
        }
        while (parent != null) {
            if (parent.equals(obj)) {
                return true;
            }
            if (this.f9408b.f9570an != null && parent.equals(this.f9408b.f9570an) && this.f9408b.f9592aF.contains(obj)) {
                return true;
            }
            parent = parent.getParent();
            if (parent instanceof Window) {
                return false;
            }
        }
        for (Component component : this.f9408b.getComponents()) {
            if (obj.equals(component)) {
                return true;
            }
        }
        return false;
    }
}
