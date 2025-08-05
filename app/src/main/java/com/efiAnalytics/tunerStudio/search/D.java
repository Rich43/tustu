package com.efiAnalytics.tunerStudio.search;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/tunerStudio/search/D.class */
class D extends MouseAdapter {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C f10171a;

    public D(C c2) {
        this.f10171a = c2;
    }

    @Override // java.awt.event.MouseAdapter, java.awt.event.MouseListener
    public void mouseClicked(MouseEvent mouseEvent) {
        this.f10171a.a();
    }

    @Override // java.awt.event.MouseAdapter, java.awt.event.MouseListener
    public void mouseEntered(MouseEvent mouseEvent) {
        this.f10171a.a(true);
    }

    @Override // java.awt.event.MouseAdapter, java.awt.event.MouseListener
    public void mouseExited(MouseEvent mouseEvent) {
        this.f10171a.a(false);
    }
}
