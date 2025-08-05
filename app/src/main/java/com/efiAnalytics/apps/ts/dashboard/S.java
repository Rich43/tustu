package com.efiAnalytics.apps.ts.dashboard;

import G.C0113cs;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/apps/ts/dashboard/S.class */
class S extends Thread {

    /* renamed from: a, reason: collision with root package name */
    boolean f9395a = true;

    /* renamed from: b, reason: collision with root package name */
    boolean f9396b = false;

    /* renamed from: c, reason: collision with root package name */
    final /* synthetic */ C1425x f9397c;

    S(C1425x c1425x) {
        this.f9397c = c1425x;
        StringBuilder sbAppend = new StringBuilder().append("GaugeCluster Demo ");
        int i2 = C1425x.f9665ai;
        C1425x.f9665ai = i2 + 1;
        setName(sbAppend.append(i2).toString());
        setDaemon(true);
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        while (true) {
            if (this.f9396b || !(this.f9397c.f9582ax || this.f9397c.f9583ay)) {
                break;
            }
            if (C0113cs.a().g(I.b.f1346a) > 0.0d) {
                this.f9397c.b(false);
                this.f9397c.f9582ax = false;
                this.f9397c.f9583ay = false;
                this.f9397c.f9586l = null;
                break;
            }
            AbstractC1420s[] abstractC1420sArrJ = this.f9397c.j();
            boolean z2 = false;
            if (this.f9397c.f9583ay) {
                this.f9397c.f9582ax = false;
            }
            for (AbstractC1420s abstractC1420s : abstractC1420sArrJ) {
                if (this.f9396b) {
                    break;
                }
                if (abstractC1420s instanceof Gauge) {
                    Gauge gauge = (Gauge) abstractC1420s;
                    double dMax = (gauge.max() - gauge.min()) / (100.0d - ((75.0d * (gauge.getValue() - gauge.min())) / (gauge.max() - gauge.min())));
                    if (!this.f9395a) {
                        double dMin = gauge.min();
                        if (this.f9397c.f9583ay && gauge.min() < 0.0d && gauge.max() > 0.0d) {
                            dMin = 0.0d;
                        }
                        if (gauge.getValue() <= gauge.max() && gauge.getValue() - dMax > dMin) {
                            gauge.setValue(gauge.getValue() - dMax);
                            z2 = true;
                        } else if (!this.f9397c.f9582ax) {
                            gauge.setValue(dMin);
                            gauge.f9317E = dMin;
                        }
                    } else if (gauge.getValue() + dMax < gauge.max()) {
                        gauge.setValue(gauge.getValue() + dMax);
                    } else {
                        gauge.setValue(gauge.max());
                        this.f9395a = false;
                        z2 = true;
                    }
                }
            }
            if (!z2) {
                if (this.f9397c.f9582ax) {
                    this.f9395a = true;
                } else {
                    this.f9397c.f9583ay = false;
                }
            }
            try {
                Thread.currentThread();
                Thread.sleep(30L);
            } catch (Exception e2) {
            }
        }
        this.f9397c.repaint();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void a() {
        this.f9396b = true;
    }
}
