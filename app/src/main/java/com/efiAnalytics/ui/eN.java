package com.efiAnalytics.ui;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/ui/eN.class */
class eN extends MouseAdapter {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ eM f11524a;

    eN(eM eMVar) {
        this.f11524a = eMVar;
    }

    @Override // java.awt.event.MouseAdapter, java.awt.event.MouseListener
    public void mouseClicked(MouseEvent mouseEvent) {
        if (this.f11524a.isEnabled()) {
            this.f11524a.requestFocus();
        }
    }
}
