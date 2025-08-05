package com.efiAnalytics.tunerStudio.search;

/* renamed from: com.efiAnalytics.tunerStudio.search.c, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:com/efiAnalytics/tunerStudio/search/c.class */
class C1480c implements B.k {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ ContinuousIpSearchPanel f10178a;

    C1480c(ContinuousIpSearchPanel continuousIpSearchPanel) {
        this.f10178a = continuousIpSearchPanel;
    }

    @Override // B.k
    public void a(B.i iVar) {
        String key = this.f10178a.getKey(iVar);
        f fVar = (f) this.f10178a.f10169j.get(key);
        if (fVar == null) {
            fVar = new f();
            this.f10178a.f10169j.put(key, fVar);
            this.f10178a.devicesUpdated();
        }
        boolean z2 = fVar.f() == null || !fVar.f().equals(iVar);
        fVar.a(iVar);
        fVar.a(System.currentTimeMillis());
        this.f10178a.f10163c.b(fVar);
        if (z2) {
            this.f10178a.f10163c.a();
        }
    }
}
