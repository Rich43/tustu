package com.efiAnalytics.apps.ts.tuningViews;

import com.efiAnalytics.ui.bV;
import java.util.ArrayList;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/apps/ts/tuningViews/A.class */
class A extends Thread {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ ArrayList f9668a;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ C1429b f9669b;

    /* renamed from: c, reason: collision with root package name */
    final /* synthetic */ z f9670c;

    A(z zVar, ArrayList arrayList, C1429b c1429b) {
        this.f9670c = zVar;
        this.f9668a = arrayList;
        this.f9669b = c1429b;
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        try {
            this.f9669b.a(G.a(this.f9668a));
        } catch (V.a e2) {
            bV.d("Failed to load Tuning Views:\n" + e2.getLocalizedMessage(), this.f9670c.f9882a);
        }
    }
}
