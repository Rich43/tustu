package org.icepdf.ri.common;

import java.util.ArrayList;
import org.icepdf.core.Memento;
import org.icepdf.core.util.Defs;

/* loaded from: icepdf-viewer.jar:org/icepdf/ri/common/UndoCaretaker.class */
public class UndoCaretaker {
    private static int maxHistorySize = Defs.sysPropertyInt("org.icepdf.ri.viewer.undo.size", 25);
    private ArrayList<Memento> mementoStateHistory = new ArrayList<>(maxHistorySize);
    private int cursor = 0;

    public void undo() {
        if (isUndo()) {
            this.cursor--;
            Memento tmp = this.mementoStateHistory.get(this.cursor);
            tmp.restore();
        }
    }

    public boolean isUndo() {
        return this.mementoStateHistory.size() > 0 && this.cursor > 0;
    }

    public void redo() {
        if (isRedo()) {
            this.cursor++;
            Memento tmp = this.mementoStateHistory.get(this.cursor);
            tmp.restore();
        }
    }

    public boolean isRedo() {
        return this.cursor + 1 < this.mementoStateHistory.size();
    }

    public void addState(Memento previousState, Memento newState) {
        if (this.cursor >= maxHistorySize) {
            this.mementoStateHistory.remove(0);
            this.mementoStateHistory.remove(1);
            this.cursor = this.mementoStateHistory.size() - 1;
        }
        if (isRedo()) {
            int max = this.mementoStateHistory.size();
            for (int i2 = this.cursor + 1; i2 < max; i2++) {
                this.mementoStateHistory.remove(this.cursor + 1);
            }
        }
        if (this.mementoStateHistory.size() == 0) {
            this.mementoStateHistory.add(previousState);
            this.mementoStateHistory.add(newState);
            this.cursor = 1;
        } else {
            this.mementoStateHistory.set(this.cursor, previousState);
            this.mementoStateHistory.add(newState);
            this.cursor++;
        }
    }
}
