package bs;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/* loaded from: TunerStudioMS.jar:bs/h.class */
class h implements PropertyChangeListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ f f8601a;

    h(f fVar) {
        this.f8601a = fVar;
    }

    @Override // java.beans.PropertyChangeListener
    public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
        if (!this.f8601a.f8596m || this.f8601a.f8599p) {
            return;
        }
        this.f8601a.f8583d.a("horizontalSplitPos", this.f8601a.f8584e.getDividerLocation() + "");
    }
}
