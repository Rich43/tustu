package javax.swing.event;

import java.util.EventListener;

/* loaded from: rt.jar:javax/swing/event/MenuKeyListener.class */
public interface MenuKeyListener extends EventListener {
    void menuKeyTyped(MenuKeyEvent menuKeyEvent);

    void menuKeyPressed(MenuKeyEvent menuKeyEvent);

    void menuKeyReleased(MenuKeyEvent menuKeyEvent);
}
