package com.efiAnalytics.apps.ts.tuningViews;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/* renamed from: com.efiAnalytics.apps.ts.tuningViews.u, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:com/efiAnalytics/apps/ts/tuningViews/u.class */
class C1448u implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ int f9868a;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ C1441n f9869b;

    C1448u(C1441n c1441n, int i2) {
        this.f9869b = c1441n;
        this.f9868a = i2;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        this.f9869b.a(this.f9869b.getTitleAt(this.f9868a), this.f9868a + 1);
    }
}
