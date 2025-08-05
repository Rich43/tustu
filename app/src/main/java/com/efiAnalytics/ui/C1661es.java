package com.efiAnalytics.ui;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/* renamed from: com.efiAnalytics.ui.es, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:com/efiAnalytics/ui/es.class */
class C1661es extends MouseAdapter {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1658ep f11611a;

    C1661es(C1658ep c1658ep) {
        this.f11611a = c1658ep;
    }

    @Override // java.awt.event.MouseAdapter, java.awt.event.MouseListener
    public void mousePressed(MouseEvent mouseEvent) {
        this.f11611a.c();
    }
}
