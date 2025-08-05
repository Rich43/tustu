package bt;

import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;

/* loaded from: TunerStudioMS.jar:bt/aB.class */
class aB implements AdjustmentListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1303al f8742a;

    aB(C1303al c1303al) {
        this.f8742a = c1303al;
    }

    @Override // java.awt.event.AdjustmentListener
    public void adjustmentValueChanged(AdjustmentEvent adjustmentEvent) {
        adjustmentEvent.getAdjustable();
        if (adjustmentEvent.getValueIsAdjusting()) {
        }
        this.f8742a.f8867v.c(adjustmentEvent.getValue());
    }
}
