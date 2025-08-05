package sun.awt;

import java.awt.AWTEvent;
import java.awt.Component;

/* loaded from: rt.jar:sun/awt/UngrabEvent.class */
public class UngrabEvent extends AWTEvent {
    private static final int UNGRAB_EVENT_ID = 1998;

    public UngrabEvent(Component component) {
        super(component, UNGRAB_EVENT_ID);
    }

    @Override // java.awt.AWTEvent, java.util.EventObject
    public String toString() {
        return "sun.awt.UngrabEvent[" + getSource() + "]";
    }
}
