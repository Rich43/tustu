package com.sun.webkit.dom;

import com.sun.webkit.Disposer;
import com.sun.webkit.DisposerRecord;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;

/* loaded from: jfxrt.jar:com/sun/webkit/dom/EventListenerImpl.class */
final class EventListenerImpl implements EventListener {
    private static final Map<EventListener, Long> EL2peer = new WeakHashMap();
    private static final Map<Long, WeakReference<EventListener>> peer2EL = new HashMap();
    private final EventListener eventListener;
    private final long jsPeer;

    private native long twkCreatePeer();

    private static native void twkDispatchEvent(long j2, long j3);

    /* JADX INFO: Access modifiers changed from: private */
    public static native void twkDisposeJSPeer(long j2);

    /* loaded from: jfxrt.jar:com/sun/webkit/dom/EventListenerImpl$SelfDisposer.class */
    private static final class SelfDisposer implements DisposerRecord {
        private final long peer;

        private SelfDisposer(long peer) {
            this.peer = peer;
        }

        @Override // com.sun.webkit.DisposerRecord
        public void dispose() {
            EventListenerImpl.dispose(this.peer);
            EventListenerImpl.twkDisposeJSPeer(this.peer);
        }
    }

    static long getPeer(EventListener eventListener) {
        if (eventListener == null) {
            return 0L;
        }
        Long peer = EL2peer.get(eventListener);
        if (peer != null) {
            return peer.longValue();
        }
        EventListenerImpl eli = new EventListenerImpl(eventListener, 0L);
        Long peer2 = Long.valueOf(eli.twkCreatePeer());
        EL2peer.put(eventListener, peer2);
        peer2EL.put(peer2, new WeakReference<>(eventListener));
        return peer2.longValue();
    }

    private static EventListener getELfromPeer(long peer) {
        WeakReference<EventListener> wr = peer2EL.get(Long.valueOf(peer));
        if (wr == null) {
            return null;
        }
        return wr.get();
    }

    static EventListener getImpl(long peer) {
        if (peer == 0) {
            return null;
        }
        EventListener ev = getELfromPeer(peer);
        if (ev != null) {
            twkDisposeJSPeer(peer);
            return ev;
        }
        EventListener el = new EventListenerImpl(null, peer);
        EL2peer.put(el, Long.valueOf(peer));
        peer2EL.put(Long.valueOf(peer), new WeakReference<>(el));
        Disposer.addRecord(el, new SelfDisposer(peer));
        return el;
    }

    @Override // org.w3c.dom.events.EventListener
    public void handleEvent(Event evt) {
        if (this.jsPeer != 0 && (evt instanceof EventImpl)) {
            twkDispatchEvent(this.jsPeer, ((EventImpl) evt).getPeer());
        }
    }

    private EventListenerImpl(EventListener eventListener, long jsPeer) {
        this.eventListener = eventListener;
        this.jsPeer = jsPeer;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void dispose(long peer) {
        EventListener ev = getELfromPeer(peer);
        if (ev != null) {
            EL2peer.remove(ev);
        }
        peer2EL.remove(Long.valueOf(peer));
    }

    private void fwkHandleEvent(long eventPeer) {
        this.eventListener.handleEvent(EventImpl.getImpl(eventPeer));
    }
}
