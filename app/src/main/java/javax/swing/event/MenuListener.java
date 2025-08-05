package javax.swing.event;

import java.util.EventListener;

/* loaded from: rt.jar:javax/swing/event/MenuListener.class */
public interface MenuListener extends EventListener {
    void menuSelected(MenuEvent menuEvent);

    void menuDeselected(MenuEvent menuEvent);

    void menuCanceled(MenuEvent menuEvent);
}
