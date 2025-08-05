package bF;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/* loaded from: TunerStudioMS.jar:bF/E.class */
class E extends KeyAdapter {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ D f6811a;

    E(D d2) {
        this.f6811a = d2;
    }

    @Override // java.awt.event.KeyAdapter, java.awt.event.KeyListener
    public void keyPressed(KeyEvent keyEvent) {
        this.f6811a.a(keyEvent);
    }
}
