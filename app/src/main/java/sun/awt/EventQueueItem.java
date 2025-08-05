package sun.awt;

import java.awt.AWTEvent;

/* loaded from: rt.jar:sun/awt/EventQueueItem.class */
public class EventQueueItem {
    public AWTEvent event;
    public EventQueueItem next;

    public EventQueueItem(AWTEvent aWTEvent) {
        this.event = aWTEvent;
    }
}
