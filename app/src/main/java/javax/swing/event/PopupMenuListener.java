package javax.swing.event;

import java.util.EventListener;

/* loaded from: rt.jar:javax/swing/event/PopupMenuListener.class */
public interface PopupMenuListener extends EventListener {
    void popupMenuWillBecomeVisible(PopupMenuEvent popupMenuEvent);

    void popupMenuWillBecomeInvisible(PopupMenuEvent popupMenuEvent);

    void popupMenuCanceled(PopupMenuEvent popupMenuEvent);
}
