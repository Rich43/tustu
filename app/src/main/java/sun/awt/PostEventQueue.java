package sun.awt;

import java.awt.AWTEvent;
import java.awt.EventQueue;

/* compiled from: SunToolkit.java */
/* loaded from: rt.jar:sun/awt/PostEventQueue.class */
class PostEventQueue {
    private final EventQueue eventQueue;
    private EventQueueItem queueHead = null;
    private EventQueueItem queueTail = null;
    private Thread flushThread = null;

    PostEventQueue(EventQueue eventQueue) {
        this.eventQueue = eventQueue;
    }

    public void flush() {
        Thread threadCurrentThread = Thread.currentThread();
        try {
            synchronized (this) {
                if (threadCurrentThread == this.flushThread) {
                    return;
                }
                while (this.flushThread != null) {
                    wait();
                }
                if (this.queueHead == null) {
                    return;
                }
                this.flushThread = threadCurrentThread;
                this.queueTail = null;
                this.queueHead = null;
                for (EventQueueItem eventQueueItem = this.queueHead; eventQueueItem != null; eventQueueItem = eventQueueItem.next) {
                    try {
                        this.eventQueue.postEvent(eventQueueItem.event);
                    } catch (Throwable th) {
                        synchronized (this) {
                            this.flushThread = null;
                            notifyAll();
                            throw th;
                        }
                    }
                }
                synchronized (this) {
                    this.flushThread = null;
                    notifyAll();
                }
            }
        } catch (InterruptedException e2) {
            threadCurrentThread.interrupt();
        }
    }

    void postEvent(AWTEvent aWTEvent) {
        EventQueueItem eventQueueItem = new EventQueueItem(aWTEvent);
        synchronized (this) {
            if (this.queueHead == null) {
                this.queueTail = eventQueueItem;
                this.queueHead = eventQueueItem;
            } else {
                this.queueTail.next = eventQueueItem;
                this.queueTail = eventQueueItem;
            }
        }
        SunToolkit.wakeupEventQueue(this.eventQueue, aWTEvent.getSource() == AWTAutoShutdown.getInstance());
    }
}
