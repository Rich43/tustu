package ao;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/* renamed from: ao.o, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:ao/o.class */
class C0817o extends KeyAdapter {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C0815m f6158a;

    C0817o(C0815m c0815m) {
        this.f6158a = c0815m;
    }

    @Override // java.awt.event.KeyAdapter, java.awt.event.KeyListener
    public void keyPressed(KeyEvent keyEvent) {
        if (keyEvent.getKeyChar() == '\n') {
            this.f6158a.a(this.f6158a.f6151a.getText());
        }
    }
}
