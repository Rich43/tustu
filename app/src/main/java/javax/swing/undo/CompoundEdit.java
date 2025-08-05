package javax.swing.undo;

import java.util.Enumeration;
import java.util.Vector;

/* loaded from: rt.jar:javax/swing/undo/CompoundEdit.class */
public class CompoundEdit extends AbstractUndoableEdit {
    boolean inProgress = true;
    protected Vector<UndoableEdit> edits = new Vector<>();

    @Override // javax.swing.undo.AbstractUndoableEdit, javax.swing.undo.UndoableEdit
    public void undo() throws CannotUndoException {
        super.undo();
        int size = this.edits.size();
        while (true) {
            int i2 = size;
            size--;
            if (i2 > 0) {
                this.edits.elementAt(size).undo();
            } else {
                return;
            }
        }
    }

    @Override // javax.swing.undo.AbstractUndoableEdit, javax.swing.undo.UndoableEdit
    public void redo() throws CannotRedoException {
        super.redo();
        Enumeration<UndoableEdit> enumerationElements = this.edits.elements();
        while (enumerationElements.hasMoreElements()) {
            enumerationElements.nextElement2().redo();
        }
    }

    protected UndoableEdit lastEdit() {
        int size = this.edits.size();
        if (size > 0) {
            return this.edits.elementAt(size - 1);
        }
        return null;
    }

    @Override // javax.swing.undo.AbstractUndoableEdit, javax.swing.undo.UndoableEdit
    public void die() {
        for (int size = this.edits.size() - 1; size >= 0; size--) {
            this.edits.elementAt(size).die();
        }
        super.die();
    }

    @Override // javax.swing.undo.AbstractUndoableEdit, javax.swing.undo.UndoableEdit
    public boolean addEdit(UndoableEdit undoableEdit) {
        if (!this.inProgress) {
            return false;
        }
        UndoableEdit undoableEditLastEdit = lastEdit();
        if (undoableEditLastEdit == null) {
            this.edits.addElement(undoableEdit);
            return true;
        }
        if (!undoableEditLastEdit.addEdit(undoableEdit)) {
            if (undoableEdit.replaceEdit(undoableEditLastEdit)) {
                this.edits.removeElementAt(this.edits.size() - 1);
            }
            this.edits.addElement(undoableEdit);
            return true;
        }
        return true;
    }

    public void end() {
        this.inProgress = false;
    }

    @Override // javax.swing.undo.AbstractUndoableEdit, javax.swing.undo.UndoableEdit
    public boolean canUndo() {
        return !isInProgress() && super.canUndo();
    }

    @Override // javax.swing.undo.AbstractUndoableEdit, javax.swing.undo.UndoableEdit
    public boolean canRedo() {
        return !isInProgress() && super.canRedo();
    }

    public boolean isInProgress() {
        return this.inProgress;
    }

    @Override // javax.swing.undo.AbstractUndoableEdit, javax.swing.undo.UndoableEdit
    public boolean isSignificant() {
        Enumeration<UndoableEdit> enumerationElements = this.edits.elements();
        while (enumerationElements.hasMoreElements()) {
            if (enumerationElements.nextElement2().isSignificant()) {
                return true;
            }
        }
        return false;
    }

    @Override // javax.swing.undo.AbstractUndoableEdit, javax.swing.undo.UndoableEdit
    public String getPresentationName() {
        UndoableEdit undoableEditLastEdit = lastEdit();
        if (undoableEditLastEdit != null) {
            return undoableEditLastEdit.getPresentationName();
        }
        return super.getPresentationName();
    }

    @Override // javax.swing.undo.AbstractUndoableEdit, javax.swing.undo.UndoableEdit
    public String getUndoPresentationName() {
        UndoableEdit undoableEditLastEdit = lastEdit();
        if (undoableEditLastEdit != null) {
            return undoableEditLastEdit.getUndoPresentationName();
        }
        return super.getUndoPresentationName();
    }

    @Override // javax.swing.undo.AbstractUndoableEdit, javax.swing.undo.UndoableEdit
    public String getRedoPresentationName() {
        UndoableEdit undoableEditLastEdit = lastEdit();
        if (undoableEditLastEdit != null) {
            return undoableEditLastEdit.getRedoPresentationName();
        }
        return super.getRedoPresentationName();
    }

    @Override // javax.swing.undo.AbstractUndoableEdit
    public String toString() {
        return super.toString() + " inProgress: " + this.inProgress + " edits: " + ((Object) this.edits);
    }
}
