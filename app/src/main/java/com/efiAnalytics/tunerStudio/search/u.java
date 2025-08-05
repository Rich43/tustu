package com.efiAnalytics.tunerStudio.search;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/tunerStudio/search/u.class */
class u implements Runnable {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ q f10227a;

    u(q qVar) {
        this.f10227a = qVar;
    }

    @Override // java.lang.Runnable
    public void run() {
        this.f10227a.f10210c.fireTableDataChanged();
        this.f10227a.removeEditor();
    }
}
