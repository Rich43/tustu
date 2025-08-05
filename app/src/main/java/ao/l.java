package aO;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/* loaded from: TunerStudioMS.jar:aO/l.class */
class l implements PropertyChangeListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ k f2704a;

    l(k kVar) {
        this.f2704a = kVar;
    }

    @Override // java.beans.PropertyChangeListener
    public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
        if (this.f2704a.f2683G == null) {
            return;
        }
        this.f2704a.f2683G.a(k.f2691w, this.f2704a.f2671g.getDividerLocation() + "");
    }
}
