package bt;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/* loaded from: TunerStudioMS.jar:bt/aV.class */
class aV extends KeyAdapter {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ aT f8798a;

    aV(aT aTVar) {
        this.f8798a = aTVar;
    }

    @Override // java.awt.event.KeyAdapter, java.awt.event.KeyListener
    public void keyPressed(KeyEvent keyEvent) {
        if (keyEvent.getKeyCode() == 38 || keyEvent.getKeyCode() == 61 || keyEvent.getKeyCode() == 521) {
            if (keyEvent.isShiftDown()) {
                this.f8798a.l();
                this.f8798a.l();
                this.f8798a.l();
                this.f8798a.l();
                this.f8798a.l();
            } else {
                this.f8798a.l();
            }
            keyEvent.consume();
        }
        if (keyEvent.getKeyCode() == 40 || keyEvent.getKeyCode() == 68) {
            if (keyEvent.isShiftDown()) {
                this.f8798a.m();
                this.f8798a.m();
                this.f8798a.m();
                this.f8798a.m();
                this.f8798a.m();
            } else {
                this.f8798a.m();
            }
            keyEvent.consume();
        }
    }
}
