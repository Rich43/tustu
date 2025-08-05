package com.efiAnalytics.ui;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/ui/K.class */
class K extends MouseAdapter {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1705w f10689a;

    K(C1705w c1705w) {
        this.f10689a = c1705w;
    }

    @Override // java.awt.event.MouseAdapter, java.awt.event.MouseListener
    public void mouseClicked(MouseEvent mouseEvent) throws NumberFormatException {
        if (this.f10689a.isEnabled() && mouseEvent.getClickCount() == 1 && mouseEvent.getButton() == 1) {
            this.f10689a.d(mouseEvent.getY() / this.f10689a.f11771j);
            mouseEvent.consume();
        }
    }
}
