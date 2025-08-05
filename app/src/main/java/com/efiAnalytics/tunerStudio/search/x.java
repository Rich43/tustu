package com.efiAnalytics.tunerStudio.search;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/tunerStudio/search/x.class */
class x implements Runnable {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ w f10230a;

    x(w wVar) {
        this.f10230a = wVar;
    }

    @Override // java.lang.Runnable
    public void run() {
        this.f10230a.f10229a.a();
        this.f10230a.f10229a.f10210c.fireTableDataChanged();
    }
}
