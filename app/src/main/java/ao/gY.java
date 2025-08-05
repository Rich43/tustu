package ao;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/* loaded from: TunerStudioMS.jar:ao/gY.class */
class gY extends KeyAdapter {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ gS f5968a;

    gY(gS gSVar) {
        this.f5968a = gSVar;
    }

    @Override // java.awt.event.KeyAdapter, java.awt.event.KeyListener
    public void keyTyped(KeyEvent keyEvent) {
        if (keyEvent.getKeyChar() == ' ') {
            ((C0801hd) keyEvent.getSource()).setSelectedItem(" ");
        }
    }
}
