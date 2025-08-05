package javax.swing.undo;

import java.util.Enumeration;
import java.util.Vector;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;

/* loaded from: rt.jar:javax/swing/undo/UndoableEditSupport.class */
public class UndoableEditSupport {
    protected int updateLevel;
    protected CompoundEdit compoundEdit;
    protected Vector<UndoableEditListener> listeners;
    protected Object realSource;

    public UndoableEditSupport() {
        this(null);
    }

    public UndoableEditSupport(Object obj) {
        this.realSource = obj == null ? this : obj;
        this.updateLevel = 0;
        this.compoundEdit = null;
        this.listeners = new Vector<>();
    }

    public synchronized void addUndoableEditListener(UndoableEditListener undoableEditListener) {
        this.listeners.addElement(undoableEditListener);
    }

    public synchronized void removeUndoableEditListener(UndoableEditListener undoableEditListener) {
        this.listeners.removeElement(undoableEditListener);
    }

    public synchronized UndoableEditListener[] getUndoableEditListeners() {
        return (UndoableEditListener[]) this.listeners.toArray(new UndoableEditListener[0]);
    }

    protected void _postEdit(UndoableEdit undoableEdit) {
        UndoableEditEvent undoableEditEvent = new UndoableEditEvent(this.realSource, undoableEdit);
        Enumeration enumerationElements = ((Vector) this.listeners.clone()).elements();
        while (enumerationElements.hasMoreElements()) {
            ((UndoableEditListener) enumerationElements.nextElement2()).undoableEditHappened(undoableEditEvent);
        }
    }

    public synchronized void postEdit(UndoableEdit undoableEdit) {
        if (this.updateLevel == 0) {
            _postEdit(undoableEdit);
        } else {
            this.compoundEdit.addEdit(undoableEdit);
        }
    }

    public int getUpdateLevel() {
        return this.updateLevel;
    }

    public synchronized void beginUpdate() {
        if (this.updateLevel == 0) {
            this.compoundEdit = createCompoundEdit();
        }
        this.updateLevel++;
    }

    protected CompoundEdit createCompoundEdit() {
        return new CompoundEdit();
    }

    public synchronized void endUpdate() {
        this.updateLevel--;
        if (this.updateLevel == 0) {
            this.compoundEdit.end();
            _postEdit(this.compoundEdit);
            this.compoundEdit = null;
        }
    }

    public String toString() {
        return super.toString() + " updateLevel: " + this.updateLevel + " listeners: " + ((Object) this.listeners) + " compoundEdit: " + ((Object) this.compoundEdit);
    }
}
