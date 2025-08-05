package bt;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/* loaded from: TunerStudioMS.jar:bt/bL.class */
public class bL extends KeyAdapter {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ bI f8915a;

    public bL(bI bIVar) {
        this.f8915a = bIVar;
    }

    @Override // java.awt.event.KeyAdapter, java.awt.event.KeyListener
    public void keyPressed(KeyEvent keyEvent) {
        if (keyEvent.getKeyCode() == 10) {
            this.f8915a.b();
        }
    }
}
