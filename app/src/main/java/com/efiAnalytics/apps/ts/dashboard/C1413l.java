package com.efiAnalytics.apps.ts.dashboard;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/* renamed from: com.efiAnalytics.apps.ts.dashboard.l, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:com/efiAnalytics/apps/ts/dashboard/l.class */
class C1413l extends MouseAdapter {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1408g f9507a;

    C1413l(C1408g c1408g) {
        this.f9507a = c1408g;
    }

    @Override // java.awt.event.MouseAdapter, java.awt.event.MouseListener
    public void mouseClicked(MouseEvent mouseEvent) {
        this.f9507a.f9500c.a(((C1415n) mouseEvent.getSource()).f9509a);
    }
}
