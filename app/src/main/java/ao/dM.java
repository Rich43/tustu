package ao;

import java.awt.KeyEventDispatcher;
import java.awt.event.KeyEvent;

/* loaded from: TunerStudioMS.jar:ao/dM.class */
class dM implements KeyEventDispatcher {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ bP f5527a;

    dM(bP bPVar) {
        this.f5527a = bPVar;
    }

    @Override // java.awt.KeyEventDispatcher
    public boolean dispatchKeyEvent(KeyEvent keyEvent) {
        if (keyEvent.getID() != 401 || keyEvent.getKeyCode() != 112) {
            return false;
        }
        this.f5527a.u();
        return true;
    }
}
