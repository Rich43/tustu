package com.efiAnalytics.tuningwidgets.portEditor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/tuningwidgets/portEditor/r.class */
class r implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ q f10592a;

    r(q qVar) {
        this.f10592a = qVar;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        this.f10592a.a((String) this.f10592a.f10585a.getSelectedItem());
    }
}
