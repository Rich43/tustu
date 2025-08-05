package com.efiAnalytics.tuningwidgets.panels;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/* renamed from: com.efiAnalytics.tuningwidgets.panels.d, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:com/efiAnalytics/tuningwidgets/panels/d.class */
class C1506d implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1483a f10442a;

    C1506d(C1483a c1483a) {
        this.f10442a = c1483a;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        if (this.f10442a.f10326f) {
            return;
        }
        this.f10442a.e((String) this.f10442a.f10321a.getSelectedItem());
        this.f10442a.b();
    }
}
