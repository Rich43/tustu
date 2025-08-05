package br;

import com.efiAnalytics.ui.bV;

/* renamed from: br.L, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:br/L.class */
class C1234L extends Thread {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1233K f8367a;

    C1234L(C1233K c1233k) {
        this.f8367a = c1233k;
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        try {
            this.f8367a.a(this.f8367a.f8361a);
        } catch (Exception e2) {
            bV.d("Error initializeing Tune Analyze Tabs, some tabs may be missing.", this.f8367a.f8364d);
        }
    }
}
