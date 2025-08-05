package com.efiAnalytics.ui;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/* renamed from: com.efiAnalytics.ui.x, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:com/efiAnalytics/ui/x.class */
class C1706x extends MouseAdapter {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1705w f11794a;

    C1706x(C1705w c1705w) {
        this.f11794a = c1705w;
    }

    @Override // java.awt.event.MouseAdapter, java.awt.event.MouseListener
    public void mouseClicked(MouseEvent mouseEvent) throws NumberFormatException {
        if (this.f11794a.isEnabled() && mouseEvent.getClickCount() == 1) {
            this.f11794a.f(mouseEvent.getX() / this.f11794a.f11762a.i());
        }
    }
}
