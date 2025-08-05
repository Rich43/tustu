package com.sun.webkit.dom;

import com.sun.webkit.Disposer;
import com.sun.webkit.DisposerRecord;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventTarget;

/* loaded from: jfxrt.jar:com/sun/webkit/dom/EventImpl.class */
public class EventImpl implements Event {
    private final long peer;
    private static final int TYPE_WheelEvent = 1;
    private static final int TYPE_MouseEvent = 2;
    private static final int TYPE_KeyboardEvent = 3;
    private static final int TYPE_UIEvent = 4;
    private static final int TYPE_MutationEvent = 5;
    public static final int NONE = 0;
    public static final int CAPTURING_PHASE = 1;
    public static final int AT_TARGET = 2;
    public static final int BUBBLING_PHASE = 3;
    public static final int MOUSEDOWN = 1;
    public static final int MOUSEUP = 2;
    public static final int MOUSEOVER = 4;
    public static final int MOUSEOUT = 8;
    public static final int MOUSEMOVE = 16;
    public static final int MOUSEDRAG = 32;
    public static final int CLICK = 64;
    public static final int DBLCLICK = 128;
    public static final int KEYDOWN = 256;
    public static final int KEYUP = 512;
    public static final int KEYPRESS = 1024;
    public static final int DRAGDROP = 2048;
    public static final int FOCUS = 4096;
    public static final int BLUR = 8192;
    public static final int SELECT = 16384;
    public static final int CHANGE = 32768;

    /* JADX INFO: Access modifiers changed from: private */
    public static native void dispose(long j2);

    private static native int getCPPTypeImpl(long j2);

    static native String getTypeImpl(long j2);

    static native long getTargetImpl(long j2);

    static native long getCurrentTargetImpl(long j2);

    static native short getEventPhaseImpl(long j2);

    static native boolean getBubblesImpl(long j2);

    static native boolean getCancelableImpl(long j2);

    static native long getTimeStampImpl(long j2);

    static native boolean getDefaultPreventedImpl(long j2);

    static native boolean getIsTrustedImpl(long j2);

    static native long getSrcElementImpl(long j2);

    static native boolean getReturnValueImpl(long j2);

    static native void setReturnValueImpl(long j2, boolean z2);

    static native boolean getCancelBubbleImpl(long j2);

    static native void setCancelBubbleImpl(long j2, boolean z2);

    static native void stopPropagationImpl(long j2);

    static native void preventDefaultImpl(long j2);

    static native void initEventImpl(long j2, String str, boolean z2, boolean z3);

    static native void stopImmediatePropagationImpl(long j2);

    /* loaded from: jfxrt.jar:com/sun/webkit/dom/EventImpl$SelfDisposer.class */
    private static class SelfDisposer implements DisposerRecord {
        private final long peer;

        SelfDisposer(long peer) {
            this.peer = peer;
        }

        @Override // com.sun.webkit.DisposerRecord
        public void dispose() {
            EventImpl.dispose(this.peer);
        }
    }

    EventImpl(long peer) {
        this.peer = peer;
        Disposer.addRecord(this, new SelfDisposer(peer));
    }

    static Event create(long peer) {
        if (peer == 0) {
            return null;
        }
        switch (getCPPTypeImpl(peer)) {
            case 1:
                return new WheelEventImpl(peer);
            case 2:
                return new MouseEventImpl(peer);
            case 3:
                return new KeyboardEventImpl(peer);
            case 4:
                return new UIEventImpl(peer);
            case 5:
                return new MutationEventImpl(peer);
            default:
                return new EventImpl(peer);
        }
    }

    long getPeer() {
        return this.peer;
    }

    public boolean equals(Object that) {
        return (that instanceof EventImpl) && this.peer == ((EventImpl) that).peer;
    }

    public int hashCode() {
        long p2 = this.peer;
        return (int) (p2 ^ (p2 >> 17));
    }

    static long getPeer(Event arg) {
        if (arg == null) {
            return 0L;
        }
        return ((EventImpl) arg).getPeer();
    }

    static Event getImpl(long peer) {
        return create(peer);
    }

    @Override // org.w3c.dom.events.Event
    public String getType() {
        return getTypeImpl(getPeer());
    }

    @Override // org.w3c.dom.events.Event
    public EventTarget getTarget() {
        return (EventTarget) NodeImpl.getImpl(getTargetImpl(getPeer()));
    }

    @Override // org.w3c.dom.events.Event
    public EventTarget getCurrentTarget() {
        return (EventTarget) NodeImpl.getImpl(getCurrentTargetImpl(getPeer()));
    }

    @Override // org.w3c.dom.events.Event
    public short getEventPhase() {
        return getEventPhaseImpl(getPeer());
    }

    @Override // org.w3c.dom.events.Event
    public boolean getBubbles() {
        return getBubblesImpl(getPeer());
    }

    @Override // org.w3c.dom.events.Event
    public boolean getCancelable() {
        return getCancelableImpl(getPeer());
    }

    @Override // org.w3c.dom.events.Event
    public long getTimeStamp() {
        return getTimeStampImpl(getPeer());
    }

    public boolean getDefaultPrevented() {
        return getDefaultPreventedImpl(getPeer());
    }

    public boolean getIsTrusted() {
        return getIsTrustedImpl(getPeer());
    }

    public EventTarget getSrcElement() {
        return (EventTarget) NodeImpl.getImpl(getSrcElementImpl(getPeer()));
    }

    public boolean getReturnValue() {
        return getReturnValueImpl(getPeer());
    }

    public void setReturnValue(boolean value) {
        setReturnValueImpl(getPeer(), value);
    }

    public boolean getCancelBubble() {
        return getCancelBubbleImpl(getPeer());
    }

    public void setCancelBubble(boolean value) {
        setCancelBubbleImpl(getPeer(), value);
    }

    @Override // org.w3c.dom.events.Event
    public void stopPropagation() {
        stopPropagationImpl(getPeer());
    }

    @Override // org.w3c.dom.events.Event
    public void preventDefault() {
        preventDefaultImpl(getPeer());
    }

    @Override // org.w3c.dom.events.Event
    public void initEvent(String eventTypeArg, boolean canBubbleArg, boolean cancelableArg) {
        initEventImpl(getPeer(), eventTypeArg, canBubbleArg, cancelableArg);
    }

    public void stopImmediatePropagation() {
        stopImmediatePropagationImpl(getPeer());
    }
}
