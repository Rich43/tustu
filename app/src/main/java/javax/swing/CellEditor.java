package javax.swing;

import java.util.EventObject;
import javax.swing.event.CellEditorListener;

/* loaded from: rt.jar:javax/swing/CellEditor.class */
public interface CellEditor {
    Object getCellEditorValue();

    boolean isCellEditable(EventObject eventObject);

    boolean shouldSelectCell(EventObject eventObject);

    boolean stopCellEditing();

    void cancelCellEditing();

    void addCellEditorListener(CellEditorListener cellEditorListener);

    void removeCellEditorListener(CellEditorListener cellEditorListener);
}
