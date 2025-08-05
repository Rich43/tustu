package br;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/* renamed from: br.l, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:br/l.class */
class C1248l implements PropertyChangeListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1245i f8478a;

    C1248l(C1245i c1245i) {
        this.f8478a = c1245i;
    }

    @Override // java.beans.PropertyChangeListener
    public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
        if (!this.f8478a.f8472m || this.f8478a.f8474o) {
            return;
        }
        this.f8478a.i();
    }
}
