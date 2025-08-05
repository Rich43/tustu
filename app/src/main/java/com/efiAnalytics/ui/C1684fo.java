package com.efiAnalytics.ui;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/* renamed from: com.efiAnalytics.ui.fo, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:com/efiAnalytics/ui/fo.class */
class C1684fo extends MouseAdapter {

    /* renamed from: a, reason: collision with root package name */
    boolean f11689a = false;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ C1683fn f11690b;

    C1684fo(C1683fn c1683fn) {
        this.f11690b = c1683fn;
    }

    @Override // java.awt.event.MouseAdapter, java.awt.event.MouseListener
    public void mousePressed(MouseEvent mouseEvent) {
        if (mouseEvent.getButton() == 1) {
            this.f11689a = true;
        }
    }

    @Override // java.awt.event.MouseAdapter, java.awt.event.MouseListener
    public void mouseReleased(MouseEvent mouseEvent) {
        if (mouseEvent.getButton() == 1 && this.f11689a) {
            this.f11690b.f11686f = !this.f11690b.f11686f;
            this.f11690b.f();
            this.f11690b.repaint();
            this.f11689a = false;
        }
    }
}
