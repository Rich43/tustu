package com.sun.webkit.event;

/* loaded from: jfxrt.jar:com/sun/webkit/event/WCMouseEvent.class */
public final class WCMouseEvent {
    public static final int MOUSE_PRESSED = 0;
    public static final int MOUSE_RELEASED = 1;
    public static final int MOUSE_MOVED = 2;
    public static final int MOUSE_DRAGGED = 3;
    public static final int MOUSE_WHEEL = 4;
    public static final int NOBUTTON = 0;
    public static final int BUTTON1 = 1;
    public static final int BUTTON2 = 2;
    public static final int BUTTON3 = 4;
    private final int id;
    private final long when;
    private final int button;
    private final int buttonMask;
    private final int clickCount;

    /* renamed from: x, reason: collision with root package name */
    private final int f12052x;

    /* renamed from: y, reason: collision with root package name */
    private final int f12053y;
    private final int screenX;
    private final int screenY;
    private final boolean shift;
    private final boolean control;
    private final boolean alt;
    private final boolean meta;
    private final boolean popupTrigger;

    public WCMouseEvent(int id, int button, int clickCount, int x2, int y2, int screenX, int screenY, long when, boolean shift, boolean control, boolean alt, boolean meta, boolean popupTrigger, int buttonMask) {
        this.id = id;
        this.button = button;
        this.clickCount = clickCount;
        this.f12052x = x2;
        this.f12053y = y2;
        this.screenX = screenX;
        this.screenY = screenY;
        this.when = when;
        this.shift = shift;
        this.control = control;
        this.alt = alt;
        this.meta = meta;
        this.popupTrigger = popupTrigger;
        this.buttonMask = buttonMask;
    }

    public WCMouseEvent(int id, int button, int clickCount, int x2, int y2, int screenX, int screenY, long when, boolean shift, boolean control, boolean alt, boolean meta, boolean popupTrigger) {
        this(id, button, clickCount, x2, y2, screenX, screenY, when, shift, control, alt, meta, popupTrigger, 0);
    }

    public int getID() {
        return this.id;
    }

    public long getWhen() {
        return this.when;
    }

    public int getButton() {
        return this.button;
    }

    public int getClickCount() {
        return this.clickCount;
    }

    public int getX() {
        return this.f12052x;
    }

    public int getY() {
        return this.f12053y;
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

    public boolean isPopupTrigger() {
        return this.popupTrigger;
    }

    public int getButtonMask() {
        return this.buttonMask;
    }
}
