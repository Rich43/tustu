package sun.awt;

import java.awt.AWTEvent;
import java.awt.EventQueue;

/* loaded from: rt.jar:sun/awt/EventQueueDelegate.class */
public class EventQueueDelegate {
    private static final Object EVENT_QUEUE_DELEGATE_KEY = new StringBuilder("EventQueueDelegate.Delegate");

    /* loaded from: rt.jar:sun/awt/EventQueueDelegate$Delegate.class */
    public interface Delegate {
        AWTEvent getNextEvent(EventQueue eventQueue) throws InterruptedException;

        Object beforeDispatch(AWTEvent aWTEvent) throws InterruptedException;

        void afterDispatch(AWTEvent aWTEvent, Object obj) throws InterruptedException;
    }

    public static void setDelegate(Delegate delegate) {
        AppContext.getAppContext().put(EVENT_QUEUE_DELEGATE_KEY, delegate);
    }

    public static Delegate getDelegate() {
        return (Delegate) AppContext.getAppContext().get(EVENT_QUEUE_DELEGATE_KEY);
    }
}
