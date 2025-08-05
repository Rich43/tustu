package bw;

import com.efiAnalytics.ui.Cdo;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/* renamed from: bw.c, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bw/c.class */
class C1372c implements KeyListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1371b f9164a;

    C1372c(C1371b c1371b) {
        this.f9164a = c1371b;
    }

    @Override // java.awt.event.KeyListener
    public void keyTyped(KeyEvent keyEvent) {
    }

    @Override // java.awt.event.KeyListener
    public void keyPressed(KeyEvent keyEvent) {
    }

    @Override // java.awt.event.KeyListener
    public void keyReleased(KeyEvent keyEvent) {
        ((Cdo) keyEvent.getSource()).d();
        this.f9164a.a();
    }
}
