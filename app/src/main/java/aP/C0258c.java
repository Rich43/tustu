package aP;

import java.awt.Window;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/* renamed from: aP.c, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:aP/c.class */
final class C0258c implements PropertyChangeListener {

    /* renamed from: a, reason: collision with root package name */
    private long f3108a = 0;

    /* renamed from: b, reason: collision with root package name */
    private int f3109b = 100;

    C0258c() {
    }

    @Override // java.beans.PropertyChangeListener
    public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
        if (propertyChangeEvent.getNewValue() == null) {
            this.f3108a = System.currentTimeMillis();
        } else if (System.currentTimeMillis() - this.f3108a <= this.f3109b || C0204a.f2793a == null) {
            C0204a.f2793a = (Window) propertyChangeEvent.getNewValue();
        } else {
            this.f3108a = System.currentTimeMillis();
        }
    }
}
