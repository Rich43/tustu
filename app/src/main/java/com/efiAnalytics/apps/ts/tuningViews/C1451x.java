package com.efiAnalytics.apps.ts.tuningViews;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/* renamed from: com.efiAnalytics.apps.ts.tuningViews.x, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:com/efiAnalytics/apps/ts/tuningViews/x.class */
class C1451x extends MouseAdapter {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1441n f9877a;

    C1451x(C1441n c1441n) {
        this.f9877a = c1441n;
    }

    @Override // java.awt.event.MouseAdapter, java.awt.event.MouseListener
    public void mouseClicked(MouseEvent mouseEvent) {
    }

    @Override // java.awt.event.MouseAdapter, java.awt.event.MouseListener
    public void mousePressed(MouseEvent mouseEvent) {
        if (mouseEvent.isPopupTrigger()) {
            this.f9877a.a(mouseEvent);
        }
    }

    @Override // java.awt.event.MouseAdapter, java.awt.event.MouseListener
    public void mouseReleased(MouseEvent mouseEvent) {
        if (mouseEvent.isPopupTrigger()) {
            this.f9877a.a(mouseEvent);
        }
    }
}
