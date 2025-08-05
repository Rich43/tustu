package javax.swing.event;

import java.util.EventListener;

/* loaded from: rt.jar:javax/swing/event/ListDataListener.class */
public interface ListDataListener extends EventListener {
    void intervalAdded(ListDataEvent listDataEvent);

    void intervalRemoved(ListDataEvent listDataEvent);

    void contentsChanged(ListDataEvent listDataEvent);
}
