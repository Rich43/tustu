package javax.swing.event;

import java.util.EventObject;

/* loaded from: rt.jar:javax/swing/event/ListSelectionEvent.class */
public class ListSelectionEvent extends EventObject {
    private int firstIndex;
    private int lastIndex;
    private boolean isAdjusting;

    public ListSelectionEvent(Object obj, int i2, int i3, boolean z2) {
        super(obj);
        this.firstIndex = i2;
        this.lastIndex = i3;
        this.isAdjusting = z2;
    }

    public int getFirstIndex() {
        return this.firstIndex;
    }

    public int getLastIndex() {
        return this.lastIndex;
    }

    public boolean getValueIsAdjusting() {
        return this.isAdjusting;
    }

    @Override // java.util.EventObject
    public String toString() {
        return getClass().getName() + "[" + (" source=" + getSource() + " firstIndex= " + this.firstIndex + " lastIndex= " + this.lastIndex + " isAdjusting= " + this.isAdjusting + " ") + "]";
    }
}
