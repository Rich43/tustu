package com.efiAnalytics.tunerStudio.search;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/tunerStudio/search/y.class */
class y implements Runnable {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ w f10231a;

    y(w wVar) {
        this.f10231a = wVar;
    }

    @Override // java.lang.Runnable
    public void run() {
        this.f10231a.f10229a.a();
        this.f10231a.f10229a.f10210c.fireTableDataChanged();
    }
}
