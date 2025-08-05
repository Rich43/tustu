package com.efiAnalytics.tunerStudio.search;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/tunerStudio/search/t.class */
class t implements Runnable {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ q f10226a;

    t(q qVar) {
        this.f10226a = qVar;
    }

    @Override // java.lang.Runnable
    public void run() {
        this.f10226a.f10210c.fireTableDataChanged();
        this.f10226a.removeEditor();
    }
}
