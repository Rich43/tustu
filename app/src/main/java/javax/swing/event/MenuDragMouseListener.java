package javax.swing.event;

import java.util.EventListener;

/* loaded from: rt.jar:javax/swing/event/MenuDragMouseListener.class */
public interface MenuDragMouseListener extends EventListener {
    void menuDragMouseEntered(MenuDragMouseEvent menuDragMouseEvent);

    void menuDragMouseExited(MenuDragMouseEvent menuDragMouseEvent);

    void menuDragMouseDragged(MenuDragMouseEvent menuDragMouseEvent);

    void menuDragMouseReleased(MenuDragMouseEvent menuDragMouseEvent);
}
