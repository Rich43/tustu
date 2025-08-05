package java.awt.event;

import java.awt.Component;
import sun.awt.AppContext;
import sun.awt.SunToolkit;

/* loaded from: rt.jar:java/awt/event/FocusEvent.class */
public class FocusEvent extends ComponentEvent {
    public static final int FOCUS_FIRST = 1004;
    public static final int FOCUS_LAST = 1005;
    public static final int FOCUS_GAINED = 1004;
    public static final int FOCUS_LOST = 1005;
    boolean temporary;
    transient Component opposite;
    private static final long serialVersionUID = 523753786457416396L;

    public FocusEvent(Component component, int i2, boolean z2, Component component2) {
        super(component, i2);
        this.temporary = z2;
        this.opposite = component2;
    }

    public FocusEvent(Component component, int i2, boolean z2) {
        this(component, i2, z2, null);
    }

    public FocusEvent(Component component, int i2) {
        this(component, i2, false);
    }

    public boolean isTemporary() {
        return this.temporary;
    }

    public Component getOppositeComponent() {
        if (this.opposite != null && SunToolkit.targetToAppContext(this.opposite) == AppContext.getAppContext()) {
            return this.opposite;
        }
        return null;
    }

    @Override // java.awt.event.ComponentEvent, java.awt.AWTEvent
    public String paramString() {
        String str;
        switch (this.id) {
            case 1004:
                str = "FOCUS_GAINED";
                break;
            case 1005:
                str = "FOCUS_LOST";
                break;
            default:
                str = "unknown type";
                break;
        }
        return str + (this.temporary ? ",temporary" : ",permanent") + ",opposite=" + ((Object) getOppositeComponent());
    }
}
