package sun.awt;

import java.awt.Component;
import java.awt.event.FocusEvent;

/* loaded from: rt.jar:sun/awt/CausedFocusEvent.class */
public class CausedFocusEvent extends FocusEvent {
    private final Cause cause;

    /* loaded from: rt.jar:sun/awt/CausedFocusEvent$Cause.class */
    public enum Cause {
        UNKNOWN,
        MOUSE_EVENT,
        TRAVERSAL,
        TRAVERSAL_UP,
        TRAVERSAL_DOWN,
        TRAVERSAL_FORWARD,
        TRAVERSAL_BACKWARD,
        MANUAL_REQUEST,
        AUTOMATIC_TRAVERSE,
        ROLLBACK,
        NATIVE_SYSTEM,
        ACTIVATION,
        CLEAR_GLOBAL_FOCUS_OWNER,
        RETARGETED
    }

    public Cause getCause() {
        return this.cause;
    }

    @Override // java.awt.AWTEvent, java.util.EventObject
    public String toString() {
        return "java.awt.FocusEvent[" + super.paramString() + ",cause=" + ((Object) this.cause) + "] on " + getSource();
    }

    public CausedFocusEvent(Component component, int i2, boolean z2, Component component2, Cause cause) {
        super(component, i2, z2, component2);
        this.cause = cause == null ? Cause.UNKNOWN : cause;
    }

    public static FocusEvent retarget(FocusEvent focusEvent, Component component) {
        if (focusEvent == null) {
            return null;
        }
        return new CausedFocusEvent(component, focusEvent.getID(), focusEvent.isTemporary(), focusEvent.getOppositeComponent(), focusEvent instanceof CausedFocusEvent ? ((CausedFocusEvent) focusEvent).getCause() : Cause.RETARGETED);
    }
}
