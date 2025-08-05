package com.efiAnalytics.tuningwidgets.portEditor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/tuningwidgets/portEditor/t.class */
class t implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ s f10598a;

    t(s sVar) {
        this.f10598a = sVar;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        this.f10598a.a(this.f10598a.f10597e.getSelectedItem().toString());
    }
}
