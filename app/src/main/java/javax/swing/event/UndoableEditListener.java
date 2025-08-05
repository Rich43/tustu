package javax.swing.event;

import java.util.EventListener;

/* loaded from: rt.jar:javax/swing/event/UndoableEditListener.class */
public interface UndoableEditListener extends EventListener {
    void undoableEditHappened(UndoableEditEvent undoableEditEvent);
}
