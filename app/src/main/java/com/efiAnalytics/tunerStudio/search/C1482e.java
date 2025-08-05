package com.efiAnalytics.tunerStudio.search;

import java.util.logging.Level;
import java.util.logging.Logger;

/* renamed from: com.efiAnalytics.tunerStudio.search.e, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:com/efiAnalytics/tunerStudio/search/e.class */
class C1482e extends Thread {

    /* renamed from: a, reason: collision with root package name */
    boolean f10182a = false;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ ContinuousIpSearchPanel f10183b;

    C1482e(ContinuousIpSearchPanel continuousIpSearchPanel) {
        this.f10183b = continuousIpSearchPanel;
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        this.f10183b.loadRecentProjects();
        this.f10183b.devicesUpdated();
        B.g.a().a(this.f10183b.f10164d);
        while (!this.f10182a) {
            try {
                Thread.sleep(this.f10183b.f10165f / 4);
            } catch (InterruptedException e2) {
                Logger.getLogger(ContinuousIpSearchPanel.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
            }
            this.f10183b.removeExpired();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void a() {
        this.f10182a = false;
    }
}
