package com.efiAnalytics.ui;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/ui/I.class */
class I extends MouseAdapter {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1705w f10687a;

    I(C1705w c1705w) {
        this.f10687a = c1705w;
    }

    @Override // java.awt.event.MouseAdapter, java.awt.event.MouseListener
    public void mouseClicked(MouseEvent mouseEvent) throws NumberFormatException {
        if (this.f10687a.isEnabled() && mouseEvent.getClickCount() == 1 && mouseEvent.getButton() == 1) {
            this.f10687a.e(mouseEvent.getY() / this.f10687a.f11771j);
            mouseEvent.consume();
        }
    }
}
