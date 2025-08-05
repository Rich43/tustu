package java.awt.event;

import java.awt.AWTEvent;
import java.util.EventListenerProxy;

/* loaded from: rt.jar:java/awt/event/AWTEventListenerProxy.class */
public class AWTEventListenerProxy extends EventListenerProxy<AWTEventListener> implements AWTEventListener {
    private final long eventMask;

    public AWTEventListenerProxy(long j2, AWTEventListener aWTEventListener) {
        super(aWTEventListener);
        this.eventMask = j2;
    }

    @Override // java.awt.event.AWTEventListener
    public void eventDispatched(AWTEvent aWTEvent) {
        getListener().eventDispatched(aWTEvent);
    }

    public long getEventMask() {
        return this.eventMask;
    }
}
