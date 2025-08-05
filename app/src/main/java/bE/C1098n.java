package be;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/* renamed from: be.n, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:be/n.class */
class C1098n implements ActionListener, KeyListener {

    /* renamed from: a, reason: collision with root package name */
    boolean f7990a = false;

    C1098n() {
    }

    @Override // java.awt.event.KeyListener
    public void keyTyped(KeyEvent keyEvent) {
        this.f7990a = true;
    }

    @Override // java.awt.event.KeyListener
    public void keyPressed(KeyEvent keyEvent) {
    }

    @Override // java.awt.event.KeyListener
    public void keyReleased(KeyEvent keyEvent) {
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        this.f7990a = true;
    }

    public boolean a() {
        return this.f7990a;
    }

    public void b() {
        this.f7990a = false;
    }
}
