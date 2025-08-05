package com.efiAnalytics.tunerStudio.search;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.math3.optimization.direct.CMAESOptimizer;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/tunerStudio/search/p.class */
class p extends Thread {

    /* renamed from: a, reason: collision with root package name */
    int f10203a;

    /* renamed from: b, reason: collision with root package name */
    long f10204b;

    /* renamed from: c, reason: collision with root package name */
    long f10205c;

    /* renamed from: d, reason: collision with root package name */
    int f10206d;

    /* renamed from: e, reason: collision with root package name */
    final /* synthetic */ l f10207e;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    p(l lVar) {
        super("SearchBox");
        this.f10207e = lVar;
        this.f10203a = 600;
        this.f10204b = Long.MAX_VALUE;
        this.f10205c = 0L;
        this.f10206d = CMAESOptimizer.DEFAULT_MAXITERATIONS;
        setDaemon(true);
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public synchronized void run() {
        this.f10205c = System.currentTimeMillis();
        while (this.f10205c + this.f10206d > System.currentTimeMillis()) {
            while (this.f10204b >= System.currentTimeMillis()) {
                try {
                    long jCurrentTimeMillis = this.f10204b - System.currentTimeMillis();
                    if (jCurrentTimeMillis > 0) {
                        wait(jCurrentTimeMillis);
                    } else if (jCurrentTimeMillis == 0) {
                        wait(1L);
                    } else {
                        bH.C.c("waitTime < 0 ???");
                    }
                } catch (InterruptedException e2) {
                    Logger.getLogger(l.class.getName()).log(Level.INFO, "Search Thread error", (Throwable) e2);
                }
            }
            if (this.f10207e.f10198d.equals(this.f10207e.f10196b.getText())) {
                this.f10204b = Long.MAX_VALUE;
            } else {
                this.f10204b = Long.MAX_VALUE;
                this.f10207e.c();
            }
        }
    }

    public synchronized void a() {
        this.f10204b = System.currentTimeMillis() + this.f10203a;
        this.f10205c = System.currentTimeMillis();
        notify();
    }
}
