package bo;

import com.efiAnalytics.ui.Cdo;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

/* renamed from: bo.l, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bo/l.class */
class C1216l implements FocusListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1206b f8318a;

    C1216l(C1206b c1206b) {
        this.f8318a = c1206b;
    }

    @Override // java.awt.event.FocusListener
    public void focusGained(FocusEvent focusEvent) {
        if (focusEvent.getSource() instanceof Cdo) {
            ((Cdo) focusEvent.getSource()).selectAll();
        }
    }

    @Override // java.awt.event.FocusListener
    public void focusLost(FocusEvent focusEvent) {
    }
}
