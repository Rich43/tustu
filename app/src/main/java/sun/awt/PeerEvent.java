package sun.awt;

import java.awt.event.InvocationEvent;

/* loaded from: rt.jar:sun/awt/PeerEvent.class */
public class PeerEvent extends InvocationEvent {
    public static final long PRIORITY_EVENT = 1;
    public static final long ULTIMATE_PRIORITY_EVENT = 2;
    public static final long LOW_PRIORITY_EVENT = 4;
    private long flags;

    public PeerEvent(Object obj, Runnable runnable, long j2) {
        this(obj, runnable, null, false, j2);
    }

    public PeerEvent(Object obj, Runnable runnable, Object obj2, boolean z2, long j2) {
        super(obj, runnable, obj2, z2);
        this.flags = j2;
    }

    public long getFlags() {
        return this.flags;
    }

    public PeerEvent coalesceEvents(PeerEvent peerEvent) {
        return null;
    }
}
