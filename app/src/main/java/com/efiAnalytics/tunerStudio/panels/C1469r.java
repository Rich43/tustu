package com.efiAnalytics.tunerStudio.panels;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/* renamed from: com.efiAnalytics.tunerStudio.panels.r, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:com/efiAnalytics/tunerStudio/panels/r.class */
class C1469r implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1466o f10140a;

    C1469r(C1466o c1466o) {
        this.f10140a = c1466o;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        this.f10140a.f10137k.a(((bA.c) actionEvent.getSource()).getState());
        this.f10140a.f10137k.i();
    }
}
