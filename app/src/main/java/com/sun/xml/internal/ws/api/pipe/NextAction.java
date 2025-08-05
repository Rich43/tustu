package com.sun.xml.internal.ws.api.pipe;

import com.sun.xml.internal.ws.api.message.Packet;

/* loaded from: rt.jar:com/sun/xml/internal/ws/api/pipe/NextAction.class */
public final class NextAction {
    int kind;
    Tube next;
    Packet packet;
    Throwable throwable;
    Runnable onExitRunnable;
    static final int INVOKE = 0;
    static final int INVOKE_AND_FORGET = 1;
    static final int RETURN = 2;
    static final int THROW = 3;
    static final int SUSPEND = 4;
    static final int THROW_ABORT_RESPONSE = 5;
    static final int ABORT_RESPONSE = 6;
    static final int INVOKE_ASYNC = 7;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !NextAction.class.desiredAssertionStatus();
    }

    private void set(int k2, Tube v2, Packet p2, Throwable t2) {
        this.kind = k2;
        this.next = v2;
        this.packet = p2;
        this.throwable = t2;
    }

    public void invoke(Tube next, Packet p2) {
        set(0, next, p2, null);
    }

    public void invokeAndForget(Tube next, Packet p2) {
        set(1, next, p2, null);
    }

    public void returnWith(Packet response) {
        set(2, null, response, null);
    }

    public void throwException(Packet response, Throwable t2) {
        set(2, null, response, t2);
    }

    public void throwException(Throwable t2) {
        if (!$assertionsDisabled && !(t2 instanceof RuntimeException) && !(t2 instanceof Error)) {
            throw new AssertionError();
        }
        set(3, null, null, t2);
    }

    public void throwExceptionAbortResponse(Throwable t2) {
        set(5, null, null, t2);
    }

    public void abortResponse(Packet response) {
        set(6, null, response, null);
    }

    public void invokeAsync(Tube next, Packet p2) {
        set(7, next, p2, null);
    }

    public void suspend() {
        suspend(null, null);
    }

    public void suspend(Runnable onExitRunnable) {
        suspend(null, onExitRunnable);
    }

    public void suspend(Tube next) {
        suspend(next, null);
    }

    public void suspend(Tube next, Runnable onExitRunnable) {
        set(4, next, null, null);
        this.onExitRunnable = onExitRunnable;
    }

    public Tube getNext() {
        return this.next;
    }

    public void setNext(Tube next) {
        this.next = next;
    }

    public Packet getPacket() {
        return this.packet;
    }

    public Throwable getThrowable() {
        return this.throwable;
    }

    public String toString() {
        StringBuilder buf = new StringBuilder();
        buf.append(super.toString()).append(" [");
        buf.append("kind=").append(getKindString()).append(',');
        buf.append("next=").append((Object) this.next).append(',');
        buf.append("packet=").append(this.packet != null ? this.packet.toShortString() : null).append(',');
        buf.append("throwable=").append((Object) this.throwable).append(']');
        return buf.toString();
    }

    public String getKindString() {
        switch (this.kind) {
            case 0:
                return "INVOKE";
            case 1:
                return "INVOKE_AND_FORGET";
            case 2:
                return "RETURN";
            case 3:
                return "THROW";
            case 4:
                return "SUSPEND";
            case 5:
                return "THROW_ABORT_RESPONSE";
            case 6:
                return "ABORT_RESPONSE";
            case 7:
                return "INVOKE_ASYNC";
            default:
                throw new AssertionError(this.kind);
        }
    }
}
