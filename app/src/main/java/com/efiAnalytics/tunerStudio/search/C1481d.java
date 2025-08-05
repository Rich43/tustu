package com.efiAnalytics.tunerStudio.search;

import java.util.logging.Level;
import java.util.logging.Logger;

/* renamed from: com.efiAnalytics.tunerStudio.search.d, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:com/efiAnalytics/tunerStudio/search/d.class */
class C1481d extends Thread {

    /* renamed from: a, reason: collision with root package name */
    Object f10179a = new Object();

    /* renamed from: b, reason: collision with root package name */
    boolean f10180b = false;

    /* renamed from: c, reason: collision with root package name */
    final /* synthetic */ ContinuousIpSearchPanel f10181c;

    C1481d(ContinuousIpSearchPanel continuousIpSearchPanel) {
        this.f10181c = continuousIpSearchPanel;
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        while (!this.f10180b) {
            this.f10181c.updateDeviceStatus();
            try {
                Thread.sleep(100L);
            } catch (InterruptedException e2) {
                Logger.getLogger(ContinuousIpSearchPanel.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void a() {
        this.f10180b = false;
    }
}
