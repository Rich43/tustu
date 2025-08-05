package javax.swing.event;

import java.util.EventListener;

/* loaded from: rt.jar:javax/swing/event/CellEditorListener.class */
public interface CellEditorListener extends EventListener {
    void editingStopped(ChangeEvent changeEvent);

    void editingCanceled(ChangeEvent changeEvent);
}
