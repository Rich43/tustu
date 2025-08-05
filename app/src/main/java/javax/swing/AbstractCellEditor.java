package javax.swing;

import java.io.Serializable;
import java.util.EventObject;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.EventListenerList;

/* loaded from: rt.jar:javax/swing/AbstractCellEditor.class */
public abstract class AbstractCellEditor implements CellEditor, Serializable {
    protected EventListenerList listenerList = new EventListenerList();
    protected transient ChangeEvent changeEvent = null;

    @Override // javax.swing.CellEditor
    public boolean isCellEditable(EventObject eventObject) {
        return true;
    }

    @Override // javax.swing.CellEditor
    public boolean shouldSelectCell(EventObject eventObject) {
        return true;
    }

    @Override // javax.swing.CellEditor
    public boolean stopCellEditing() {
        fireEditingStopped();
        return true;
    }

    @Override // javax.swing.CellEditor
    public void cancelCellEditing() {
        fireEditingCanceled();
    }

    @Override // javax.swing.CellEditor
    public void addCellEditorListener(CellEditorListener cellEditorListener) {
        this.listenerList.add(CellEditorListener.class, cellEditorListener);
    }

    @Override // javax.swing.CellEditor
    public void removeCellEditorListener(CellEditorListener cellEditorListener) {
        this.listenerList.remove(CellEditorListener.class, cellEditorListener);
    }

    public CellEditorListener[] getCellEditorListeners() {
        return (CellEditorListener[]) this.listenerList.getListeners(CellEditorListener.class);
    }

    protected void fireEditingStopped() {
        Object[] listenerList = this.listenerList.getListenerList();
        for (int length = listenerList.length - 2; length >= 0; length -= 2) {
            if (listenerList[length] == CellEditorListener.class) {
                if (this.changeEvent == null) {
                    this.changeEvent = new ChangeEvent(this);
                }
                ((CellEditorListener) listenerList[length + 1]).editingStopped(this.changeEvent);
            }
        }
    }

    protected void fireEditingCanceled() {
        Object[] listenerList = this.listenerList.getListenerList();
        for (int length = listenerList.length - 2; length >= 0; length -= 2) {
            if (listenerList[length] == CellEditorListener.class) {
                if (this.changeEvent == null) {
                    this.changeEvent = new ChangeEvent(this);
                }
                ((CellEditorListener) listenerList[length + 1]).editingCanceled(this.changeEvent);
            }
        }
    }
}
