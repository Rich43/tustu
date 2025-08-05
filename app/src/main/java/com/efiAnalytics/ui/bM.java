package com.efiAnalytics.ui;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/ui/bM.class */
class bM extends MouseAdapter {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1582bt f10887a;

    bM(C1582bt c1582bt) {
        this.f10887a = c1582bt;
    }

    @Override // java.awt.event.MouseAdapter, java.awt.event.MouseListener
    public void mouseClicked(MouseEvent mouseEvent) {
        if (mouseEvent.getButton() == 3) {
            this.f10887a.a(mouseEvent.getPoint());
        }
    }
}
