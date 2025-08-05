package javax.swing.event;

import java.util.EventObject;
import javax.swing.undo.UndoableEdit;

/* loaded from: rt.jar:javax/swing/event/UndoableEditEvent.class */
public class UndoableEditEvent extends EventObject {
    private UndoableEdit myEdit;

    public UndoableEditEvent(Object obj, UndoableEdit undoableEdit) {
        super(obj);
        this.myEdit = undoableEdit;
    }

    public UndoableEdit getEdit() {
        return this.myEdit;
    }
}
