package ao;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JSplitPane;

/* loaded from: TunerStudioMS.jar:ao/aR.class */
class aR implements PropertyChangeListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ aQ f5158a;

    aR(aQ aQVar) {
        this.f5158a = aQVar;
    }

    @Override // java.beans.PropertyChangeListener
    public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
        if (this.f5158a.f5141n.r() == null || !JSplitPane.DIVIDER_LOCATION_PROPERTY.equals(propertyChangeEvent.getPropertyName())) {
            return;
        }
        double dividerLocation = this.f5158a.f5153q.getDividerLocation();
        double height = this.f5158a.getHeight();
        if (dividerLocation <= 0.0d || height <= 0.0d) {
            return;
        }
        double d2 = dividerLocation / height;
        h.i.c(h.i.f12302W, "" + d2);
        if (d2 < 0.88d && !h.i.e(h.i.f12293N, h.i.f12297R).equals(h.i.f12295P)) {
            this.f5158a.f5141n.c(true);
        } else if (this.f5158a.getHeight() - this.f5158a.f5153q.getDividerLocation() <= this.f5158a.f5143b.f6173e.getPreferredSize().height + this.f5158a.f5153q.getDividerSize()) {
            if (h.i.f12296Q.equals(h.i.e(h.i.f12293N, h.i.f12297R))) {
                this.f5158a.f5141n.c(false);
            }
            this.f5158a.t();
        }
    }
}
