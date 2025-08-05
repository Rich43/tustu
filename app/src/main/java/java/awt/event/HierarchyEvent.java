package java.awt.event;

import java.awt.AWTEvent;
import java.awt.Component;
import java.awt.Container;

/* loaded from: rt.jar:java/awt/event/HierarchyEvent.class */
public class HierarchyEvent extends AWTEvent {
    private static final long serialVersionUID = -5337576970038043990L;
    public static final int HIERARCHY_FIRST = 1400;
    public static final int HIERARCHY_CHANGED = 1400;
    public static final int ANCESTOR_MOVED = 1401;
    public static final int ANCESTOR_RESIZED = 1402;
    public static final int HIERARCHY_LAST = 1402;
    public static final int PARENT_CHANGED = 1;
    public static final int DISPLAYABILITY_CHANGED = 2;
    public static final int SHOWING_CHANGED = 4;
    Component changed;
    Container changedParent;
    long changeFlags;

    public HierarchyEvent(Component component, int i2, Component component2, Container container) {
        super(component, i2);
        this.changed = component2;
        this.changedParent = container;
    }

    public HierarchyEvent(Component component, int i2, Component component2, Container container, long j2) {
        super(component, i2);
        this.changed = component2;
        this.changedParent = container;
        this.changeFlags = j2;
    }

    public Component getComponent() {
        if (this.source instanceof Component) {
            return (Component) this.source;
        }
        return null;
    }

    public Component getChanged() {
        return this.changed;
    }

    public Container getChangedParent() {
        return this.changedParent;
    }

    public long getChangeFlags() {
        return this.changeFlags;
    }

    @Override // java.awt.AWTEvent
    public String paramString() {
        String str;
        switch (this.id) {
            case 1400:
                String str2 = "HIERARCHY_CHANGED (";
                boolean z2 = true;
                if ((this.changeFlags & 1) != 0) {
                    z2 = false;
                    str2 = str2 + "PARENT_CHANGED";
                }
                if ((this.changeFlags & 2) != 0) {
                    if (z2) {
                        z2 = false;
                    } else {
                        str2 = str2 + ",";
                    }
                    str2 = str2 + "DISPLAYABILITY_CHANGED";
                }
                if ((this.changeFlags & 4) != 0) {
                    if (z2) {
                        z2 = false;
                    } else {
                        str2 = str2 + ",";
                    }
                    str2 = str2 + "SHOWING_CHANGED";
                }
                if (!z2) {
                    str2 = str2 + ",";
                }
                str = str2 + ((Object) this.changed) + "," + ((Object) this.changedParent) + ")";
                break;
            case ANCESTOR_MOVED /* 1401 */:
                str = "ANCESTOR_MOVED (" + ((Object) this.changed) + "," + ((Object) this.changedParent) + ")";
                break;
            case 1402:
                str = "ANCESTOR_RESIZED (" + ((Object) this.changed) + "," + ((Object) this.changedParent) + ")";
                break;
            default:
                str = "unknown type";
                break;
        }
        return str;
    }
}
