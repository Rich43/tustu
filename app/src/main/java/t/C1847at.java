package t;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JTextField;
import s.C1818g;

/* renamed from: t.at, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:t/at.class */
class C1847at extends KeyAdapter {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1846as f13806a;

    C1847at(C1846as c1846as) {
        this.f13806a = c1846as;
    }

    @Override // java.awt.event.KeyAdapter, java.awt.event.KeyListener
    public void keyReleased(KeyEvent keyEvent) {
        JTextField jTextField = (JTextField) keyEvent.getSource();
        if (this.f13806a.a(jTextField, C1818g.b("Label Text"))) {
            this.f13806a.c().s(jTextField.getText());
        }
    }
}
