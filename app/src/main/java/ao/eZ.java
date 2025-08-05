package ao;

import java.awt.HeadlessException;
import java.awt.KeyEventDispatcher;
import java.awt.event.KeyEvent;

/* loaded from: TunerStudioMS.jar:ao/eZ.class */
class eZ implements KeyEventDispatcher {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C0737eu f5606a;

    eZ(C0737eu c0737eu) {
        this.f5606a = c0737eu;
    }

    @Override // java.awt.KeyEventDispatcher
    public boolean dispatchKeyEvent(KeyEvent keyEvent) throws HeadlessException {
        if (keyEvent.getID() != 401 || keyEvent.getKeyCode() != 112) {
            return false;
        }
        this.f5606a.c();
        return true;
    }
}
