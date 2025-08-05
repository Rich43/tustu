package bt;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

/* renamed from: bt.be, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bt/be.class */
class C1323be implements FocusListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1320bb f9008a;

    C1323be(C1320bb c1320bb) {
        this.f9008a = c1320bb;
    }

    @Override // java.awt.event.FocusListener
    public void focusGained(FocusEvent focusEvent) {
        this.f9008a.d();
    }

    @Override // java.awt.event.FocusListener
    public void focusLost(FocusEvent focusEvent) {
    }
}
