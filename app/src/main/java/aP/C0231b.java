package aP;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/* renamed from: aP.b, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:aP/b.class */
final class C0231b extends WindowAdapter {
    C0231b() {
    }

    @Override // java.awt.event.WindowAdapter, java.awt.event.WindowListener
    public void windowDeactivated(WindowEvent windowEvent) {
        if (windowEvent.getWindow().isShowing()) {
            return;
        }
        a(windowEvent);
    }

    @Override // java.awt.event.WindowAdapter, java.awt.event.WindowListener
    public void windowClosed(WindowEvent windowEvent) {
        a(windowEvent);
    }

    private void a(WindowEvent windowEvent) {
        windowEvent.getWindow().removeWindowListener(this);
        C0204a.f2793a = null;
    }
}
