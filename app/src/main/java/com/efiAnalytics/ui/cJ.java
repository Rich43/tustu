package com.efiAnalytics.ui;

import java.awt.HeadlessException;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/ui/cJ.class */
class cJ extends MouseAdapter {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ cI f11068a;

    cJ(cI cIVar) {
        this.f11068a = cIVar;
    }

    @Override // java.awt.event.MouseAdapter, java.awt.event.MouseListener
    public void mouseClicked(MouseEvent mouseEvent) throws HeadlessException {
        if (this.f11068a.isEnabled()) {
            this.f11068a.a();
        }
    }
}
