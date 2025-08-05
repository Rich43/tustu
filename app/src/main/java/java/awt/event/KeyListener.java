package java.awt.event;

import java.util.EventListener;

/* loaded from: rt.jar:java/awt/event/KeyListener.class */
public interface KeyListener extends EventListener {
    void keyTyped(KeyEvent keyEvent);

    void keyPressed(KeyEvent keyEvent);

    void keyReleased(KeyEvent keyEvent);
}
