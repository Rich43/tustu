package br;

import com.efiAnalytics.ui.eJ;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/* renamed from: br.k, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:br/k.class */
class C1247k implements PropertyChangeListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1245i f8477a;

    C1247k(C1245i c1245i) {
        this.f8477a = c1245i;
    }

    @Override // java.beans.PropertyChangeListener
    public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
        if (!this.f8477a.f8472m || this.f8477a.f8475p) {
            return;
        }
        this.f8477a.f8458d.a("horizontalSplitPos", eJ.b(this.f8477a.f8459e.getDividerLocation()) + "");
    }
}
