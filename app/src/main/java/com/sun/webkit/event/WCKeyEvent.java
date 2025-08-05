package com.sun.webkit.event;

/* loaded from: jfxrt.jar:com/sun/webkit/event/WCKeyEvent.class */
public final class WCKeyEvent {
    public static final int KEY_TYPED = 0;
    public static final int KEY_PRESSED = 1;
    public static final int KEY_RELEASED = 2;
    public static final int VK_BACK = 8;
    public static final int VK_TAB = 9;
    public static final int VK_RETURN = 13;
    public static final int VK_ESCAPE = 27;
    public static final int VK_PRIOR = 33;
    public static final int VK_NEXT = 34;
    public static final int VK_END = 35;
    public static final int VK_HOME = 36;
    public static final int VK_LEFT = 37;
    public static final int VK_UP = 38;
    public static final int VK_RIGHT = 39;
    public static final int VK_DOWN = 40;
    public static final int VK_INSERT = 45;
    public static final int VK_DELETE = 46;
    public static final int VK_OEM_PERIOD = 190;
    private final int type;
    private final long when;
    private final String text;
    private final String keyIdentifier;
    private final int windowsVirtualKeyCode;
    private final boolean shift;
    private final boolean ctrl;
    private final boolean alt;
    private final boolean meta;

    public WCKeyEvent(int type, String text, String keyIdentifier, int windowsVirtualKeyCode, boolean shift, boolean ctrl, boolean alt, boolean meta, long when) {
        this.type = type;
        this.text = text;
        this.keyIdentifier = keyIdentifier;
        this.windowsVirtualKeyCode = windowsVirtualKeyCode;
        this.shift = shift;
        this.ctrl = ctrl;
        this.alt = alt;
        this.meta = meta;
        this.when = when;
    }

    public int getType() {
        return this.type;
    }

    public long getWhen() {
        return this.when;
    }

    public String getText() {
        return this.text;
    }

    public String getKeyIdentifier() {
        return this.keyIdentifier;
    }

    public int getWindowsVirtualKeyCode() {
        return this.windowsVirtualKeyCode;
    }

    public boolean isShiftDown() {
        return this.shift;
    }

    public boolean isCtrlDown() {
        return this.ctrl;
    }

    public boolean isAltDown() {
        return this.alt;
    }

    public boolean isMetaDown() {
        return this.meta;
    }

    public static boolean filterEvent(WCKeyEvent ke) {
        char kc;
        if (ke.getType() == 0) {
            String text = ke.getText();
            if (text == null || text.length() != 1 || (kc = text.charAt(0)) == '\b' || kc == '\n' || kc == '\t' || kc == 65535 || kc == 24 || kc == 27 || kc == 127) {
                return true;
            }
            return false;
        }
        return false;
    }
}
