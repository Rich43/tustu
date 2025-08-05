package com.sun.webkit.dom;

import com.sun.webkit.Disposer;
import com.sun.webkit.DisposerRecord;
import org.w3c.dom.DOMException;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;

/* loaded from: jfxrt.jar:com/sun/webkit/dom/EventTargetImpl.class */
public class EventTargetImpl implements EventTarget {
    private final long peer;

    /* JADX INFO: Access modifiers changed from: private */
    public static native void dispose(long j2);

    static native void addEventListenerImpl(long j2, String str, long j3, boolean z2);

    static native void removeEventListenerImpl(long j2, String str, long j3, boolean z2);

    static native boolean dispatchEventImpl(long j2, long j3);

    /* loaded from: jfxrt.jar:com/sun/webkit/dom/EventTargetImpl$SelfDisposer.class */
    private static class SelfDisposer implements DisposerRecord {
        private final long peer;

        SelfDisposer(long peer) {
            this.peer = peer;
        }

        @Override // com.sun.webkit.DisposerRecord
        public void dispose() {
            EventTargetImpl.dispose(this.peer);
        }
    }

    EventTargetImpl(long peer) {
        this.peer = peer;
        Disposer.addRecord(this, new SelfDisposer(peer));
    }

    static EventTarget create(long peer) {
        if (peer == 0) {
            return null;
        }
        return new EventTargetImpl(peer);
    }

    long getPeer() {
        return this.peer;
    }

    public boolean equals(Object that) {
        return (that instanceof EventTargetImpl) && this.peer == ((EventTargetImpl) that).peer;
    }

    public int hashCode() {
        long p2 = this.peer;
        return (int) (p2 ^ (p2 >> 17));
    }

    static long getPeer(EventTarget arg) {
        if (arg == null) {
            return 0L;
        }
        return ((EventTargetImpl) arg).getPeer();
    }

    static EventTarget getImpl(long peer) {
        return create(peer);
    }

    @Override // org.w3c.dom.events.EventTarget
    public void addEventListener(String type, EventListener listener, boolean useCapture) {
        addEventListenerImpl(getPeer(), type, EventListenerImpl.getPeer(listener), useCapture);
    }

    @Override // org.w3c.dom.events.EventTarget
    public void removeEventListener(String type, EventListener listener, boolean useCapture) {
        removeEventListenerImpl(getPeer(), type, EventListenerImpl.getPeer(listener), useCapture);
    }

    @Override // org.w3c.dom.events.EventTarget
    public boolean dispatchEvent(Event event) throws DOMException {
        return dispatchEventImpl(getPeer(), EventImpl.getPeer(event));
    }
}
