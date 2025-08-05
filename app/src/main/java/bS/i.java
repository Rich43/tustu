package bs;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/* loaded from: TunerStudioMS.jar:bs/i.class */
class i implements PropertyChangeListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ f f8602a;

    i(f fVar) {
        this.f8602a = fVar;
    }

    @Override // java.beans.PropertyChangeListener
    public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
        if (!this.f8602a.f8596m || this.f8602a.f8598o) {
            return;
        }
        this.f8602a.i();
    }
}
