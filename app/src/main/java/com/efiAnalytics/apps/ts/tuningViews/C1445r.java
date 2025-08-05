package com.efiAnalytics.apps.ts.tuningViews;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/* renamed from: com.efiAnalytics.apps.ts.tuningViews.r, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:com/efiAnalytics/apps/ts/tuningViews/r.class */
class C1445r implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ int f9805a;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ C1441n f9806b;

    C1445r(C1441n c1441n, int i2) {
        this.f9806b = c1441n;
        this.f9805a = i2;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        this.f9806b.b(this.f9806b.getTitleAt(this.f9805a));
    }
}
