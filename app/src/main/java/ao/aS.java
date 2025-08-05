package ao;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JSplitPane;

/* loaded from: TunerStudioMS.jar:ao/aS.class */
class aS implements PropertyChangeListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ aQ f5159a;

    aS(aQ aQVar) {
        this.f5159a = aQVar;
    }

    @Override // java.beans.PropertyChangeListener
    public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
        String propertyName = propertyChangeEvent.getPropertyName();
        if (this.f5159a.f5141n.r() == null || !propertyName.equals(JSplitPane.DIVIDER_LOCATION_PROPERTY)) {
            return;
        }
        double dividerLocation = this.f5159a.f5152p.getDividerLocation() / (this.f5159a.f5152p.getWidth() - this.f5159a.f5152p.getDividerSize());
        if (this.f5159a.f5156l) {
            h.i.c(h.i.f12303X, "" + dividerLocation);
        }
        if (dividerLocation > 0.0d && dividerLocation < 0.88d && !h.i.a("showTuningConsole", h.i.f12269p)) {
            this.f5159a.f5141n.b(true);
        } else {
            if (dividerLocation <= 0.97d || this.f5159a.e() == null || !h.i.a("showTuningConsole", h.i.f12269p)) {
                return;
            }
            this.f5159a.f5141n.b(false);
            this.f5159a.u();
        }
    }
}
