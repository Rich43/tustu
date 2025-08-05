package aO;

import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;

/* loaded from: TunerStudioMS.jar:aO/r.class */
class r implements AdjustmentListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ k f2714a;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ q f2715b;

    r(q qVar, k kVar) {
        this.f2715b = qVar;
        this.f2714a = kVar;
    }

    @Override // java.awt.event.AdjustmentListener
    public void adjustmentValueChanged(AdjustmentEvent adjustmentEvent) {
        this.f2715b.f2713d.e();
    }
}
