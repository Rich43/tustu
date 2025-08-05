package com.efiAnalytics.tuningwidgets.panels;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/* renamed from: com.efiAnalytics.tuningwidgets.panels.e, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:com/efiAnalytics/tuningwidgets/panels/e.class */
class C1507e implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1483a f10443a;

    C1507e(C1483a c1483a) {
        this.f10443a = c1483a;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        if (this.f10443a.f10326f) {
            return;
        }
        this.f10443a.f((String) this.f10443a.f10322b.getSelectedItem());
        this.f10443a.b();
    }
}
