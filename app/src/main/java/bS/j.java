package bs;

import java.awt.KeyEventDispatcher;
import java.awt.event.KeyEvent;

/* loaded from: TunerStudioMS.jar:bs/j.class */
class j implements KeyEventDispatcher {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ f f8603a;

    j(f fVar) {
        this.f8603a = fVar;
    }

    @Override // java.awt.KeyEventDispatcher
    public boolean dispatchKeyEvent(KeyEvent keyEvent) {
        if (keyEvent.getKeyCode() != 27) {
            return false;
        }
        this.f8603a.d();
        return true;
    }
}
