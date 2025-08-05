package bo;

import bH.C;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/* renamed from: bo.j, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bo/j.class */
class C1214j implements KeyListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1206b f8314a;

    C1214j(C1206b c1206b) {
        this.f8314a = c1206b;
    }

    @Override // java.awt.event.KeyListener
    public void keyTyped(KeyEvent keyEvent) {
        C.c("Typed");
    }

    @Override // java.awt.event.KeyListener
    public void keyPressed(KeyEvent keyEvent) {
        C.c("Pressed");
    }

    @Override // java.awt.event.KeyListener
    public void keyReleased(KeyEvent keyEvent) {
        C.c("Release");
    }
}
