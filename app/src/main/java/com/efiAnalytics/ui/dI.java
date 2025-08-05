package com.efiAnalytics.ui;

import java.awt.Window;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/ui/dI.class */
public class dI {

    /* renamed from: c, reason: collision with root package name */
    private static dI f11330c = null;

    /* renamed from: a, reason: collision with root package name */
    dJ f11331a = null;

    /* renamed from: b, reason: collision with root package name */
    public List f11332b = new ArrayList();

    private dI() {
    }

    public static dI a() {
        if (f11330c == null) {
            f11330c = new dI();
        }
        return f11330c;
    }

    public void a(InterfaceC1663eu interfaceC1663eu) {
        this.f11332b.add(interfaceC1663eu);
    }

    public void b() {
        if (d()) {
            return;
        }
        c();
        this.f11331a = new dJ(this);
        this.f11331a.start();
        bH.C.d("Starting Prevent Sleep");
    }

    public void c() {
        if (this.f11331a != null) {
            this.f11331a.a(false);
            this.f11331a = null;
            bH.C.d("Stopping Prevent Sleep");
        }
    }

    public boolean d() {
        return this.f11331a != null && this.f11331a.a() && this.f11331a.isAlive();
    }

    private boolean e() {
        if (!this.f11332b.isEmpty()) {
            return true;
        }
        try {
            for (Window window : Window.getWindows()) {
                if (window.isVisible() && window.isActive()) {
                    return true;
                }
            }
            return false;
        } catch (Exception e2) {
            bH.C.a(e2);
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean f() {
        if (!e()) {
            return false;
        }
        Iterator it = this.f11332b.iterator();
        while (it.hasNext()) {
            if (!((InterfaceC1663eu) it.next()).a()) {
                return false;
            }
        }
        return true;
    }
}
