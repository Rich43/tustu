package com.efiAnalytics.apps.ts.tuningViews;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/* renamed from: com.efiAnalytics.apps.ts.tuningViews.s, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:com/efiAnalytics/apps/ts/tuningViews/s.class */
class C1446s implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ int f9807a;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ C1441n f9808b;

    C1446s(C1441n c1441n, int i2) {
        this.f9808b = c1441n;
        this.f9807a = i2;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        J jA = this.f9808b.a(this.f9808b.getTitleAt(this.f9807a));
        jA.j();
        if (jA.w() == null || jA.w().isEmpty()) {
            jA.b(false);
        } else {
            jA.h();
        }
    }
}
