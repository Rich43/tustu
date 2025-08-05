package javax.swing.undo;

/* loaded from: rt.jar:javax/swing/undo/UndoableEdit.class */
public interface UndoableEdit {
    void undo() throws CannotUndoException;

    boolean canUndo();

    void redo() throws CannotRedoException;

    boolean canRedo();

    void die();

    boolean addEdit(UndoableEdit undoableEdit);

    boolean replaceEdit(UndoableEdit undoableEdit);

    boolean isSignificant();

    String getPresentationName();

    String getUndoPresentationName();

    String getRedoPresentationName();
}
