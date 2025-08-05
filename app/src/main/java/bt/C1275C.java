package bt;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/* renamed from: bt.C, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bt/C.class */
public class C1275C extends KeyAdapter {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1273A f8655a;

    public C1275C(C1273A c1273a) {
        this.f8655a = c1273a;
    }

    @Override // java.awt.event.KeyAdapter, java.awt.event.KeyListener
    public void keyPressed(KeyEvent keyEvent) {
        if (keyEvent.getKeyCode() == 10) {
            this.f8655a.b();
        }
    }
}
