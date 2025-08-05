package com.efiAnalytics.ui;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/ui/cG.class */
class cG extends MouseAdapter {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ cF f11063a;

    cG(cF cFVar) {
        this.f11063a = cFVar;
    }

    @Override // java.awt.event.MouseAdapter, java.awt.event.MouseListener
    public void mouseClicked(MouseEvent mouseEvent) {
        if (this.f11063a.isEnabled()) {
            this.f11063a.a();
        }
    }
}
