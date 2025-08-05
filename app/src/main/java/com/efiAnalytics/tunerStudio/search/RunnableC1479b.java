package com.efiAnalytics.tunerStudio.search;

/* renamed from: com.efiAnalytics.tunerStudio.search.b, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:com/efiAnalytics/tunerStudio/search/b.class */
class RunnableC1479b implements Runnable {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ f f10176a;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ ContinuousIpSearchPanel f10177b;

    RunnableC1479b(ContinuousIpSearchPanel continuousIpSearchPanel, f fVar) {
        this.f10177b = continuousIpSearchPanel;
        this.f10176a = fVar;
    }

    @Override // java.lang.Runnable
    public void run() {
        this.f10177b.f10163c.a(this.f10176a);
    }
}
