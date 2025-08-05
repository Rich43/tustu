package javax.swing.event;

import java.util.EventListener;

/* loaded from: rt.jar:javax/swing/event/AncestorListener.class */
public interface AncestorListener extends EventListener {
    void ancestorAdded(AncestorEvent ancestorEvent);

    void ancestorRemoved(AncestorEvent ancestorEvent);

    void ancestorMoved(AncestorEvent ancestorEvent);
}
