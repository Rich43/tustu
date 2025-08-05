package com.efiAnalytics.apps.ts.dashboard;

import com.efiAnalytics.ui.bV;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/apps/ts/dashboard/aJ.class */
class aJ extends Thread {

    /* renamed from: a, reason: collision with root package name */
    String f9447a;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ HtmlDisplay f9448b;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    aJ(HtmlDisplay htmlDisplay, String str) {
        super("DocLoader");
        this.f9448b = htmlDisplay;
        this.f9447a = str;
        setDaemon(true);
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        try {
            this.f9448b.setDocumentUrl(this.f9447a);
        } catch (V.a e2) {
            bV.d(e2.getMessage(), this.f9448b.f9350a);
        }
    }
}
