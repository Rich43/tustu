package aP;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/* loaded from: TunerStudioMS.jar:aP/aM.class */
class aM extends KeyAdapter {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ aJ f2833a;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ aL f2834b;

    aM(aL aLVar, aJ aJVar) {
        this.f2834b = aLVar;
        this.f2833a = aJVar;
    }

    @Override // java.awt.event.KeyAdapter, java.awt.event.KeyListener
    public void keyTyped(KeyEvent keyEvent) {
        try {
            Double.parseDouble(this.f2834b.f2830a.getText() + keyEvent.getKeyChar());
        } catch (NumberFormatException e2) {
            keyEvent.consume();
        }
    }
}
