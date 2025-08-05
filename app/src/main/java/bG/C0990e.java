package bG;

import com.efiAnalytics.ui.Cdo;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

/* renamed from: bG.e, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bG/e.class */
class C0990e implements FocusListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C0989d f6935a;

    C0990e(C0989d c0989d) {
        this.f6935a = c0989d;
    }

    @Override // java.awt.event.FocusListener
    public void focusGained(FocusEvent focusEvent) {
        ((Cdo) focusEvent.getSource()).selectAll();
    }

    @Override // java.awt.event.FocusListener
    public void focusLost(FocusEvent focusEvent) {
    }
}
