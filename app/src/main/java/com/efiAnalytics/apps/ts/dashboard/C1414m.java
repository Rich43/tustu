package com.efiAnalytics.apps.ts.dashboard;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/* renamed from: com.efiAnalytics.apps.ts.dashboard.m, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:com/efiAnalytics/apps/ts/dashboard/m.class */
class C1414m extends MouseAdapter {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1408g f9508a;

    C1414m(C1408g c1408g) {
        this.f9508a = c1408g;
    }

    @Override // java.awt.event.MouseAdapter, java.awt.event.MouseListener
    public void mouseClicked(MouseEvent mouseEvent) {
        this.f9508a.f9500c.a(((C1415n) mouseEvent.getSource()).f9509a);
    }
}
