package com.efiAnalytics.apps.ts.tuningViews;

import com.efiAnalytics.ui.bV;
import java.awt.Component;
import java.util.ArrayList;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/apps/ts/tuningViews/O.class */
class O extends Thread {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ ArrayList f9734a;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ C1429b f9735b;

    /* renamed from: c, reason: collision with root package name */
    final /* synthetic */ Component f9736c;

    /* renamed from: d, reason: collision with root package name */
    final /* synthetic */ J f9737d;

    O(J j2, ArrayList arrayList, C1429b c1429b, Component component) {
        this.f9737d = j2;
        this.f9734a = arrayList;
        this.f9735b = c1429b;
        this.f9736c = component;
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        try {
            this.f9735b.a(G.a(this.f9734a));
        } catch (V.a e2) {
            bV.d("Failed to load Tuning Views:\n" + e2.getLocalizedMessage(), this.f9736c);
        }
    }
}
