package com.efiAnalytics.apps.ts.tuningViews;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/* renamed from: com.efiAnalytics.apps.ts.tuningViews.t, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:com/efiAnalytics/apps/ts/tuningViews/t.class */
class C1447t implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ int f9809a;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ C1441n f9810b;

    C1447t(C1441n c1441n, int i2) {
        this.f9810b = c1441n;
        this.f9809a = i2;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        this.f9810b.a(this.f9810b.getTitleAt(this.f9809a), this.f9809a - 1);
    }
}
