package javax.swing.undo;

import java.util.Iterator;
import java.util.Vector;
import javax.swing.UIManager;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;

/* loaded from: rt.jar:javax/swing/undo/UndoManager.class */
public class UndoManager extends CompoundEdit implements UndoableEditListener {
    int indexOfNextAdd = 0;
    int limit = 100;

    public UndoManager() {
        this.edits.ensureCapacity(this.limit);
    }

    public synchronized int getLimit() {
        return this.limit;
    }

    public synchronized void discardAllEdits() {
        Iterator<UndoableEdit> it = this.edits.iterator();
        while (it.hasNext()) {
            it.next().die();
        }
        this.edits = new Vector<>();
        this.indexOfNextAdd = 0;
    }

    protected void trimForLimit() {
        int size;
        if (this.limit >= 0 && (size = this.edits.size()) > this.limit) {
            int i2 = this.limit / 2;
            int i3 = (this.indexOfNextAdd - 1) - i2;
            int i4 = (this.indexOfNextAdd - 1) + i2;
            if ((i4 - i3) + 1 > this.limit) {
                i3++;
            }
            if (i3 < 0) {
                i4 -= i3;
                i3 = 0;
            }
            if (i4 >= size) {
                int i5 = (size - i4) - 1;
                i4 += i5;
                i3 += i5;
            }
            trimEdits(i4 + 1, size - 1);
            trimEdits(0, i3 - 1);
        }
    }

    protected void trimEdits(int i2, int i3) {
        if (i2 <= i3) {
            for (int i4 = i3; i2 <= i4; i4--) {
                this.edits.elementAt(i4).die();
                this.edits.removeElementAt(i4);
            }
            if (this.indexOfNextAdd > i3) {
                this.indexOfNextAdd -= (i3 - i2) + 1;
            } else if (this.indexOfNextAdd >= i2) {
                this.indexOfNextAdd = i2;
            }
        }
    }

    public synchronized void setLimit(int i2) {
        if (!this.inProgress) {
            throw new RuntimeException("Attempt to call UndoManager.setLimit() after UndoManager.end() has been called");
        }
        this.limit = i2;
        trimForLimit();
    }

    protected UndoableEdit editToBeUndone() {
        int i2 = this.indexOfNextAdd;
        while (i2 > 0) {
            i2--;
            UndoableEdit undoableEditElementAt = this.edits.elementAt(i2);
            if (undoableEditElementAt.isSignificant()) {
                return undoableEditElementAt;
            }
        }
        return null;
    }

    protected UndoableEdit editToBeRedone() {
        int size = this.edits.size();
        int i2 = this.indexOfNextAdd;
        while (i2 < size) {
            int i3 = i2;
            i2++;
            UndoableEdit undoableEditElementAt = this.edits.elementAt(i3);
            if (undoableEditElementAt.isSignificant()) {
                return undoableEditElementAt;
            }
        }
        return null;
    }

    protected void undoTo(UndoableEdit undoableEdit) throws CannotUndoException {
        boolean z2 = false;
        while (!z2) {
            Vector<UndoableEdit> vector = this.edits;
            int i2 = this.indexOfNextAdd - 1;
            this.indexOfNextAdd = i2;
            UndoableEdit undoableEditElementAt = vector.elementAt(i2);
            undoableEditElementAt.undo();
            z2 = undoableEditElementAt == undoableEdit;
        }
    }

    protected void redoTo(UndoableEdit undoableEdit) throws CannotRedoException {
        boolean z2 = false;
        while (!z2) {
            Vector<UndoableEdit> vector = this.edits;
            int i2 = this.indexOfNextAdd;
            this.indexOfNextAdd = i2 + 1;
            UndoableEdit undoableEditElementAt = vector.elementAt(i2);
            undoableEditElementAt.redo();
            z2 = undoableEditElementAt == undoableEdit;
        }
    }

    public synchronized void undoOrRedo() throws CannotRedoException, CannotUndoException {
        if (this.indexOfNextAdd == this.edits.size()) {
            undo();
        } else {
            redo();
        }
    }

    public synchronized boolean canUndoOrRedo() {
        if (this.indexOfNextAdd == this.edits.size()) {
            return canUndo();
        }
        return canRedo();
    }

    @Override // javax.swing.undo.CompoundEdit, javax.swing.undo.AbstractUndoableEdit, javax.swing.undo.UndoableEdit
    public synchronized void undo() throws CannotUndoException {
        if (this.inProgress) {
            UndoableEdit undoableEditEditToBeUndone = editToBeUndone();
            if (undoableEditEditToBeUndone == null) {
                throw new CannotUndoException();
            }
            undoTo(undoableEditEditToBeUndone);
            return;
        }
        super.undo();
    }

    @Override // javax.swing.undo.CompoundEdit, javax.swing.undo.AbstractUndoableEdit, javax.swing.undo.UndoableEdit
    public synchronized boolean canUndo() {
        if (this.inProgress) {
            UndoableEdit undoableEditEditToBeUndone = editToBeUndone();
            return undoableEditEditToBeUndone != null && undoableEditEditToBeUndone.canUndo();
        }
        return super.canUndo();
    }

    @Override // javax.swing.undo.CompoundEdit, javax.swing.undo.AbstractUndoableEdit, javax.swing.undo.UndoableEdit
    public synchronized void redo() throws CannotRedoException {
        if (this.inProgress) {
            UndoableEdit undoableEditEditToBeRedone = editToBeRedone();
            if (undoableEditEditToBeRedone == null) {
                throw new CannotRedoException();
            }
            redoTo(undoableEditEditToBeRedone);
            return;
        }
        super.redo();
    }

    @Override // javax.swing.undo.CompoundEdit, javax.swing.undo.AbstractUndoableEdit, javax.swing.undo.UndoableEdit
    public synchronized boolean canRedo() {
        if (this.inProgress) {
            UndoableEdit undoableEditEditToBeRedone = editToBeRedone();
            return undoableEditEditToBeRedone != null && undoableEditEditToBeRedone.canRedo();
        }
        return super.canRedo();
    }

    @Override // javax.swing.undo.CompoundEdit, javax.swing.undo.AbstractUndoableEdit, javax.swing.undo.UndoableEdit
    public synchronized boolean addEdit(UndoableEdit undoableEdit) {
        trimEdits(this.indexOfNextAdd, this.edits.size() - 1);
        boolean zAddEdit = super.addEdit(undoableEdit);
        if (this.inProgress) {
            zAddEdit = true;
        }
        this.indexOfNextAdd = this.edits.size();
        trimForLimit();
        return zAddEdit;
    }

    @Override // javax.swing.undo.CompoundEdit
    public synchronized void end() {
        super.end();
        trimEdits(this.indexOfNextAdd, this.edits.size() - 1);
    }

    public synchronized String getUndoOrRedoPresentationName() {
        if (this.indexOfNextAdd == this.edits.size()) {
            return getUndoPresentationName();
        }
        return getRedoPresentationName();
    }

    @Override // javax.swing.undo.CompoundEdit, javax.swing.undo.AbstractUndoableEdit, javax.swing.undo.UndoableEdit
    public synchronized String getUndoPresentationName() {
        if (this.inProgress) {
            if (canUndo()) {
                return editToBeUndone().getUndoPresentationName();
            }
            return UIManager.getString("AbstractUndoableEdit.undoText");
        }
        return super.getUndoPresentationName();
    }

    @Override // javax.swing.undo.CompoundEdit, javax.swing.undo.AbstractUndoableEdit, javax.swing.undo.UndoableEdit
    public synchronized String getRedoPresentationName() {
        if (this.inProgress) {
            if (canRedo()) {
                return editToBeRedone().getRedoPresentationName();
            }
            return UIManager.getString("AbstractUndoableEdit.redoText");
        }
        return super.getRedoPresentationName();
    }

    @Override // javax.swing.event.UndoableEditListener
    public void undoableEditHappened(UndoableEditEvent undoableEditEvent) {
        addEdit(undoableEditEvent.getEdit());
    }

    @Override // javax.swing.undo.CompoundEdit, javax.swing.undo.AbstractUndoableEdit
    public String toString() {
        return super.toString() + " limit: " + this.limit + " indexOfNextAdd: " + this.indexOfNextAdd;
    }
}
