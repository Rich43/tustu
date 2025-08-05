package com.efiAnalytics.tunerStudio.panels;

import W.C0184j;
import W.C0188n;
import com.efiAnalytics.ui.bV;
import java.util.logging.Level;
import java.util.logging.Logger;
import r.C1810m;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/tunerStudio/panels/ax.class */
class ax extends Thread {

    /* renamed from: a, reason: collision with root package name */
    boolean f10101a = true;

    /* renamed from: b, reason: collision with root package name */
    boolean f10102b = false;

    /* renamed from: c, reason: collision with root package name */
    final /* synthetic */ TriggerLoggerPanel f10103c;

    ax(TriggerLoggerPanel triggerLoggerPanel) {
        this.f10103c = triggerLoggerPanel;
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        try {
            if (this.f10103c.f9980ah.c() == this.f10103c.f9980ah.size() - 1) {
                this.f10103c.f9980ah.f();
            }
            try {
                C0184j c0184jA = this.f10103c.f9980ah.a(this.f10103c.f9980ah.c()).a("Time");
                if (c0184jA == null || c0184jA.v() == 0) {
                    bV.d("No time Column, Playback unavailable.", this.f10103c.f10032Y);
                    if (!this.f10102b || this.f10103c.f9980ah.c() >= this.f10103c.f9980ah.size() - 3) {
                        this.f10103c.f10000x.b(true);
                    }
                    this.f10101a = false;
                    C1810m.a().a(false);
                    return;
                }
                long jCurrentTimeMillis = System.currentTimeMillis();
                C1810m.a().a(true);
                long jRound = Math.round(c0184jA.d(0));
                while (this.f10101a) {
                    long jCurrentTimeMillis2 = System.currentTimeMillis() - jCurrentTimeMillis;
                    int iV = c0184jA.v() - 1;
                    for (long jRound2 = Math.round(c0184jA.d(iV)) - jRound; this.f10101a && jCurrentTimeMillis2 < jRound2; jRound2 = Math.round(c0184jA.d(iV)) - jRound) {
                        try {
                            sleep(10L);
                        } catch (InterruptedException e2) {
                            Logger.getLogger(TriggerLoggerPanel.class.getName()).log(Level.INFO, (String) null, (Throwable) e2);
                        }
                        jCurrentTimeMillis2 = System.currentTimeMillis() - jCurrentTimeMillis;
                    }
                    C0188n c0188nA = this.f10103c.n().a();
                    if (c0188nA == null) {
                        this.f10101a = false;
                    } else {
                        this.f10103c.i(c0188nA);
                        this.f10103c.h();
                        c0184jA = c0188nA.a("Time");
                        if (c0184jA == null) {
                            this.f10101a = false;
                        }
                    }
                }
                if (!this.f10102b || this.f10103c.f9980ah.c() >= this.f10103c.f9980ah.size() - 3) {
                    this.f10103c.f10000x.b(true);
                }
                this.f10101a = false;
                C1810m.a().a(false);
            } catch (V.a e3) {
                bH.C.c("Tried playback with no valid start page.");
                if (!this.f10102b || this.f10103c.f9980ah.c() >= this.f10103c.f9980ah.size() - 3) {
                    this.f10103c.f10000x.b(true);
                }
                this.f10101a = false;
                C1810m.a().a(false);
            }
        } catch (Throwable th) {
            if (!this.f10102b || this.f10103c.f9980ah.c() >= this.f10103c.f9980ah.size() - 3) {
                this.f10103c.f10000x.b(true);
            }
            this.f10101a = false;
            C1810m.a().a(false);
            throw th;
        }
    }

    public void a() {
        this.f10101a = false;
        this.f10102b = true;
    }
}
