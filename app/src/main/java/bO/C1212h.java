package bo;

import bH.W;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/* renamed from: bo.h, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bo/h.class */
class C1212h implements KeyListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1206b f8312a;

    C1212h(C1206b c1206b) {
        this.f8312a = c1206b;
    }

    @Override // java.awt.event.KeyListener
    public void keyTyped(KeyEvent keyEvent) {
    }

    @Override // java.awt.event.KeyListener
    public void keyPressed(KeyEvent keyEvent) {
    }

    @Override // java.awt.event.KeyListener
    public void keyReleased(KeyEvent keyEvent) {
        double dE = this.f8312a.f8285k.e();
        if (Double.isNaN(dE)) {
            this.f8312a.f8286l.setText("");
        } else {
            this.f8312a.f8286l.setText(W.b(dE * 16.38706d, 0));
        }
    }
}
