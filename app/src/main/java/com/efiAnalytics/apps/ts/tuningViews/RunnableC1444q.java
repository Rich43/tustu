package com.efiAnalytics.apps.ts.tuningViews;

import java.awt.HeadlessException;

/* renamed from: com.efiAnalytics.apps.ts.tuningViews.q, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:com/efiAnalytics/apps/ts/tuningViews/q.class */
class RunnableC1444q implements Runnable {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ J f9803a;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ C1441n f9804b;

    RunnableC1444q(C1441n c1441n, J j2) {
        this.f9804b = c1441n;
        this.f9803a = j2;
    }

    @Override // java.lang.Runnable
    public void run() throws HeadlessException {
        if (this.f9803a != null) {
            this.f9803a.t();
        }
    }
}
