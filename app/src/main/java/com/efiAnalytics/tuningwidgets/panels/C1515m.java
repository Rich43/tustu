package com.efiAnalytics.tuningwidgets.panels;

import javax.swing.JLabel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/* renamed from: com.efiAnalytics.tuningwidgets.panels.m, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:com/efiAnalytics/tuningwidgets/panels/m.class */
class C1515m implements ChangeListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1509g f10479a;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ JLabel f10480b;

    /* renamed from: c, reason: collision with root package name */
    final /* synthetic */ C1513k f10481c;

    C1515m(C1513k c1513k, C1509g c1509g, JLabel jLabel) {
        this.f10481c = c1513k;
        this.f10479a = c1509g;
        this.f10480b = jLabel;
    }

    @Override // javax.swing.event.ChangeListener
    public void stateChanged(ChangeEvent changeEvent) throws IllegalArgumentException {
        this.f10480b.setText(bH.W.a(this.f10481c.f10466e.getValue() + " s.", ' ', 8));
    }
}
