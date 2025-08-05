package javax.swing.undo;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

/* loaded from: rt.jar:javax/swing/undo/StateEdit.class */
public class StateEdit extends AbstractUndoableEdit {
    protected static final String RCSID = "$Id: StateEdit.java,v 1.6 1997/10/01 20:05:51 sandipc Exp $";
    protected StateEditable object;
    protected Hashtable<Object, Object> preState;
    protected Hashtable<Object, Object> postState;
    protected String undoRedoName;

    public StateEdit(StateEditable stateEditable) {
        init(stateEditable, null);
    }

    public StateEdit(StateEditable stateEditable, String str) {
        init(stateEditable, str);
    }

    protected void init(StateEditable stateEditable, String str) {
        this.object = stateEditable;
        this.preState = new Hashtable<>(11);
        this.object.storeState(this.preState);
        this.postState = null;
        this.undoRedoName = str;
    }

    public void end() {
        this.postState = new Hashtable<>(11);
        this.object.storeState(this.postState);
        removeRedundantState();
    }

    @Override // javax.swing.undo.AbstractUndoableEdit, javax.swing.undo.UndoableEdit
    public void undo() throws CannotUndoException {
        super.undo();
        this.object.restoreState(this.preState);
    }

    @Override // javax.swing.undo.AbstractUndoableEdit, javax.swing.undo.UndoableEdit
    public void redo() throws CannotRedoException {
        super.redo();
        this.object.restoreState(this.postState);
    }

    @Override // javax.swing.undo.AbstractUndoableEdit, javax.swing.undo.UndoableEdit
    public String getPresentationName() {
        return this.undoRedoName;
    }

    protected void removeRedundantState() {
        Vector vector = new Vector();
        Enumeration<Object> enumerationKeys = this.preState.keys();
        while (enumerationKeys.hasMoreElements()) {
            Object objNextElement2 = enumerationKeys.nextElement2();
            if (this.postState.containsKey(objNextElement2) && this.postState.get(objNextElement2).equals(this.preState.get(objNextElement2))) {
                vector.addElement(objNextElement2);
            }
        }
        for (int size = vector.size() - 1; size >= 0; size--) {
            Object objElementAt = vector.elementAt(size);
            this.preState.remove(objElementAt);
            this.postState.remove(objElementAt);
        }
    }
}
