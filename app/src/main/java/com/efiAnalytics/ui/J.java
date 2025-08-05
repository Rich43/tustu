package com.efiAnalytics.ui;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/ui/J.class */
class J extends MouseAdapter {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1705w f10688a;

    J(C1705w c1705w) {
        this.f10688a = c1705w;
    }

    @Override // java.awt.event.MouseAdapter, java.awt.event.MouseListener
    public void mousePressed(MouseEvent mouseEvent) {
        if (mouseEvent.getButton() == 3) {
            this.f10688a.s();
        } else {
            this.f10688a.f11762a.w();
        }
    }
}
