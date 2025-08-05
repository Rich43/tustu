package ao;

import com.efiAnalytics.ui.Cdo;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

/* renamed from: ao.gd, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:ao/gd.class */
class C0774gd implements FocusListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ fX f5973a;

    C0774gd(fX fXVar) {
        this.f5973a = fXVar;
    }

    @Override // java.awt.event.FocusListener
    public void focusGained(FocusEvent focusEvent) {
        ((Cdo) focusEvent.getSource()).selectAll();
    }

    @Override // java.awt.event.FocusListener
    public void focusLost(FocusEvent focusEvent) {
    }
}
