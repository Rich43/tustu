package javax.swing.undo;

import java.io.Serializable;
import javax.swing.UIManager;

/* loaded from: rt.jar:javax/swing/undo/AbstractUndoableEdit.class */
public class AbstractUndoableEdit implements UndoableEdit, Serializable {
    protected static final String UndoName = "Undo";
    protected static final String RedoName = "Redo";
    boolean hasBeenDone = true;
    boolean alive = true;

    @Override // javax.swing.undo.UndoableEdit
    public void die() {
        this.alive = false;
    }

    @Override // javax.swing.undo.UndoableEdit
    public void undo() throws CannotUndoException {
        if (!canUndo()) {
            throw new CannotUndoException();
        }
        this.hasBeenDone = false;
    }

    @Override // javax.swing.undo.UndoableEdit
    public boolean canUndo() {
        return this.alive && this.hasBeenDone;
    }

    @Override // javax.swing.undo.UndoableEdit
    public void redo() throws CannotRedoException {
        if (!canRedo()) {
            throw new CannotRedoException();
        }
        this.hasBeenDone = true;
    }

    @Override // javax.swing.undo.UndoableEdit
    public boolean canRedo() {
        return this.alive && !this.hasBeenDone;
    }

    @Override // javax.swing.undo.UndoableEdit
    public boolean addEdit(UndoableEdit undoableEdit) {
        return false;
    }

    @Override // javax.swing.undo.UndoableEdit
    public boolean replaceEdit(UndoableEdit undoableEdit) {
        return false;
    }

    @Override // javax.swing.undo.UndoableEdit
    public boolean isSignificant() {
        return true;
    }

    @Override // javax.swing.undo.UndoableEdit
    public String getPresentationName() {
        return "";
    }

    @Override // javax.swing.undo.UndoableEdit
    public String getUndoPresentationName() {
        String string;
        String presentationName = getPresentationName();
        if (!"".equals(presentationName)) {
            string = UIManager.getString("AbstractUndoableEdit.undoText") + " " + presentationName;
        } else {
            string = UIManager.getString("AbstractUndoableEdit.undoText");
        }
        return string;
    }

    @Override // javax.swing.undo.UndoableEdit
    public String getRedoPresentationName() {
        String string;
        String presentationName = getPresentationName();
        if (!"".equals(presentationName)) {
            string = UIManager.getString("AbstractUndoableEdit.redoText") + " " + presentationName;
        } else {
            string = UIManager.getString("AbstractUndoableEdit.redoText");
        }
        return string;
    }

    public String toString() {
        return super.toString() + " hasBeenDone: " + this.hasBeenDone + " alive: " + this.alive;
    }
}
