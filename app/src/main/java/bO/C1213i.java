package bo;

import bH.W;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/* renamed from: bo.i, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bo/i.class */
class C1213i implements KeyListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1206b f8313a;

    C1213i(C1206b c1206b) {
        this.f8313a = c1206b;
    }

    @Override // java.awt.event.KeyListener
    public void keyTyped(KeyEvent keyEvent) {
    }

    @Override // java.awt.event.KeyListener
    public void keyPressed(KeyEvent keyEvent) {
    }

    @Override // java.awt.event.KeyListener
    public void keyReleased(KeyEvent keyEvent) {
        double dE = this.f8313a.f8286l.e();
        if (Double.isNaN(dE)) {
            this.f8313a.f8285k.setText("");
        } else {
            this.f8313a.f8285k.setText(W.b(dE / 16.38706d, 0));
        }
    }
}
