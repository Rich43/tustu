package com.sun.webkit.event;

/* loaded from: jfxrt.jar:com/sun/webkit/event/WCMouseWheelEvent.class */
public final class WCMouseWheelEvent {
    private final long when;

    /* renamed from: x, reason: collision with root package name */
    private final int f12054x;

    /* renamed from: y, reason: collision with root package name */
    private final int f12055y;
    private final int screenX;
    private final int screenY;
    private final float deltaX;
    private final float deltaY;
    private final boolean shift;
    private final boolean control;
    private final boolean alt;
    private final boolean meta;

    public WCMouseWheelEvent(int x2, int y2, int screenX, int screenY, long when, boolean shift, boolean control, boolean alt, boolean meta, float deltaX, float deltaY) {
        this.f12054x = x2;
        this.f12055y = y2;
        this.screenX = screenX;
        this.screenY = screenY;
        this.when = when;
        this.shift = shift;
        this.control = control;
        this.alt = alt;
        this.meta = meta;
        this.deltaX = deltaX;
        this.deltaY = deltaY;
    }

    public long getWhen() {
        return this.when;
    }

    public int getX() {
        return this.f12054x;
    }

    public int getY() {
        return this.f12055y;
    }

    public int getScreenX() {
        return this.screenX;
    }

    public int getScreenY() {
        return this.screenY;
    }

    public boolean isShiftDown() {
        return this.shift;
    }

    public boolean isControlDown() {
        return this.control;
    }

    public boolean isAltDown() {
        return this.alt;
    }

    public boolean isMetaDown() {
        return this.meta;
    }

    public float getDeltaX() {
        return this.deltaX;
    }

    public float getDeltaY() {
        return this.deltaY;
    }
}
