package com.efiAnalytics.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/ui/eW.class */
class eW implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ eV f11566a;

    eW(eV eVVar) {
        this.f11566a = eVVar;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        Iterator it = this.f11566a.f11564b.iterator();
        while (it.hasNext()) {
            ((eX) it.next()).a(this.f11566a.f11562a, this.f11566a.f11563c);
        }
    }
}
