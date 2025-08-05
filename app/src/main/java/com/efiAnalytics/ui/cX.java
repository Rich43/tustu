package com.efiAnalytics.ui;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/ui/cX.class */
class cX extends Thread {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ cT f11167a;

    cX(cT cTVar) {
        this.f11167a = cTVar;
        setDaemon(true);
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        while (this.f11167a.f11163c.f11160f > 0) {
            this.f11167a.f11162b.setText(this.f11167a.e());
            try {
                Thread.sleep(1000L);
            } catch (InterruptedException e2) {
            }
            cS.c(this.f11167a.f11163c);
        }
        this.f11167a.f11162b.setText(this.f11167a.e());
        this.f11167a.f11162b.setEnabled(true);
        if (this.f11167a.f11161a) {
            this.f11167a.dispose();
        }
    }
}
