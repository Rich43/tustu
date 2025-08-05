package javax.swing.event;

import java.util.EventObject;

/* loaded from: rt.jar:javax/swing/event/ListDataEvent.class */
public class ListDataEvent extends EventObject {
    public static final int CONTENTS_CHANGED = 0;
    public static final int INTERVAL_ADDED = 1;
    public static final int INTERVAL_REMOVED = 2;
    private int type;
    private int index0;
    private int index1;

    public int getType() {
        return this.type;
    }

    public int getIndex0() {
        return this.index0;
    }

    public int getIndex1() {
        return this.index1;
    }

    public ListDataEvent(Object obj, int i2, int i3, int i4) {
        super(obj);
        this.type = i2;
        this.index0 = Math.min(i3, i4);
        this.index1 = Math.max(i3, i4);
    }

    @Override // java.util.EventObject
    public String toString() {
        return getClass().getName() + "[type=" + this.type + ",index0=" + this.index0 + ",index1=" + this.index1 + "]";
    }
}
