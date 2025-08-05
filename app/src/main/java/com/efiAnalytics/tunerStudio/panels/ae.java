package com.efiAnalytics.tunerStudio.panels;

import W.C0184j;
import W.C0188n;
import java.util.Iterator;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/tunerStudio/panels/ae.class */
class ae implements U {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ TriggerLoggerPanel f10060a;

    ae(TriggerLoggerPanel triggerLoggerPanel) {
        this.f10060a = triggerLoggerPanel;
    }

    @Override // com.efiAnalytics.tunerStudio.panels.U
    public void a(String str, double d2, double d3) {
        Iterator it = this.f10060a.f9980ah.iterator();
        while (it.hasNext()) {
            C0184j c0184jA = ((C0188n) it.next()).a(str);
            if (c0184jA != null) {
                float f2 = Double.isNaN(d2) ? Float.NaN : (float) d2;
                c0184jA.f(Double.isNaN(d3) ? Float.NaN : (float) d3);
                c0184jA.g(f2);
            }
        }
        this.f10060a.f9982f.z();
        this.f10060a.i();
    }
}
