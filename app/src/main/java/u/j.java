package u;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JComponent;

/* loaded from: TunerStudioMS.jar:u/j.class */
class j extends JComponent implements KeyListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ h f13996a;

    public j(h hVar) {
        this.f13996a = hVar;
        setOpaque(false);
        addKeyListener(this);
        setFocusTraversalKeysEnabled(false);
    }

    public void a() {
        setVisible(true);
        requestFocusInWindow();
    }

    @Override // java.awt.event.KeyListener
    public void keyPressed(KeyEvent keyEvent) {
        keyEvent.consume();
    }

    @Override // java.awt.event.KeyListener
    public void keyTyped(KeyEvent keyEvent) {
        keyEvent.consume();
    }

    @Override // java.awt.event.KeyListener
    public void keyReleased(KeyEvent keyEvent) {
        keyEvent.consume();
    }
}
