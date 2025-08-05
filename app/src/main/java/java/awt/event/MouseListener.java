package java.awt.event;

import java.util.EventListener;

/* loaded from: rt.jar:java/awt/event/MouseListener.class */
public interface MouseListener extends EventListener {
    void mouseClicked(MouseEvent mouseEvent);

    void mousePressed(MouseEvent mouseEvent);

    void mouseReleased(MouseEvent mouseEvent);

    void mouseEntered(MouseEvent mouseEvent);

    void mouseExited(MouseEvent mouseEvent);
}
