package com.efiAnalytics.tunerStudio.search;

/* renamed from: com.efiAnalytics.tunerStudio.search.a, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:com/efiAnalytics/tunerStudio/search/a.class */
class RunnableC1478a implements Runnable {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ f f10174a;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ ContinuousIpSearchPanel f10175b;

    RunnableC1478a(ContinuousIpSearchPanel continuousIpSearchPanel, f fVar) {
        this.f10175b = continuousIpSearchPanel;
        this.f10174a = fVar;
    }

    @Override // java.lang.Runnable
    public void run() {
        this.f10175b.f10163c.b(this.f10174a);
    }
}
