package com.sun.webkit.event;

/* loaded from: jfxrt.jar:com/sun/webkit/event/WCChangeEvent.class */
public final class WCChangeEvent {
    private final Object source;

    public WCChangeEvent(Object source) {
        if (source == null) {
            throw new IllegalArgumentException("null source");
        }
        this.source = source;
    }

    public Object getSource() {
        return this.source;
    }

    public String toString() {
        return getClass().getName() + "[source=" + this.source + "]";
    }
}
