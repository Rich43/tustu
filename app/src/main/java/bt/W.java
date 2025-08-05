package bt;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/* loaded from: TunerStudioMS.jar:bt/W.class */
class W implements PropertyChangeListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ U f8731a;

    W(U u2) {
        this.f8731a = u2;
    }

    @Override // java.beans.PropertyChangeListener
    public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
        this.f8731a.f8719j.a("tableTuningSplitPanePosition", this.f8731a.f8710a.getDividerLocation() + "");
    }
}
