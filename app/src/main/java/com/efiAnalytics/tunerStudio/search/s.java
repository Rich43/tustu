package com.efiAnalytics.tunerStudio.search;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/tunerStudio/search/s.class */
class s implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ q f10225a;

    s(q qVar) {
        this.f10225a = qVar;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        if (this.f10225a.getSelectedRow() >= 0) {
            this.f10225a.d((f) this.f10225a.f10209b.get(this.f10225a.getSelectedRow()));
        }
    }
}
