package java.awt;

import java.awt.event.KeyEvent;
import java.io.Serializable;

/* loaded from: rt.jar:java/awt/Event.class */
public class Event implements Serializable {
    private transient long data;
    public static final int SHIFT_MASK = 1;
    public static final int CTRL_MASK = 2;
    public static final int META_MASK = 4;
    public static final int ALT_MASK = 8;
    public static final int HOME = 1000;
    public static final int END = 1001;
    public static final int PGUP = 1002;
    public static final int PGDN = 1003;
    public static final int UP = 1004;
    public static final int DOWN = 1005;
    public static final int RIGHT = 1007;
    public static final int F1 = 1008;
    public static final int CAPS_LOCK = 1022;
    public static final int NUM_LOCK = 1023;
    public static final int PAUSE = 1024;
    public static final int ENTER = 10;
    public static final int BACK_SPACE = 8;
    public static final int TAB = 9;
    public static final int ESCAPE = 27;
    public static final int DELETE = 127;
    private static final int WINDOW_EVENT = 200;
    public static final int WINDOW_DESTROY = 201;
    public static final int WINDOW_EXPOSE = 202;
    public static final int WINDOW_ICONIFY = 203;
    public static final int WINDOW_DEICONIFY = 204;
    public static final int WINDOW_MOVED = 205;
    private static final int KEY_EVENT = 400;
    public static final int KEY_PRESS = 401;
    public static final int KEY_RELEASE = 402;
    public static final int KEY_ACTION = 403;
    public static final int KEY_ACTION_RELEASE = 404;
    private static final int MOUSE_EVENT = 500;
    public static final int MOUSE_DOWN = 501;
    public static final int MOUSE_UP = 502;
    public static final int MOUSE_MOVE = 503;
    public static final int MOUSE_ENTER = 504;
    public static final int MOUSE_EXIT = 505;
    public static final int MOUSE_DRAG = 506;
    private static final int SCROLL_EVENT = 600;
    public static final int SCROLL_LINE_UP = 601;
    public static final int SCROLL_LINE_DOWN = 602;
    public static final int SCROLL_PAGE_UP = 603;
    public static final int SCROLL_PAGE_DOWN = 604;
    public static final int SCROLL_ABSOLUTE = 605;
    public static final int SCROLL_BEGIN = 606;
    public static final int SCROLL_END = 607;
    private static final int LIST_EVENT = 700;
    public static final int LIST_SELECT = 701;
    public static final int LIST_DESELECT = 702;
    private static final int MISC_EVENT = 1000;
    public static final int ACTION_EVENT = 1001;
    public static final int LOAD_FILE = 1002;
    public static final int SAVE_FILE = 1003;
    public static final int GOT_FOCUS = 1004;
    public static final int LOST_FOCUS = 1005;
    public Object target;
    public long when;
    public int id;

    /* renamed from: x, reason: collision with root package name */
    public int f12363x;

    /* renamed from: y, reason: collision with root package name */
    public int f12364y;
    public int key;
    public int modifiers;
    public int clickCount;
    public Object arg;
    public Event evt;
    private boolean consumed;
    private static final long serialVersionUID = 5488922509400504703L;
    public static final int LEFT = 1006;
    public static final int F2 = 1009;
    public static final int F3 = 1010;
    public static final int F4 = 1011;
    public static final int F5 = 1012;
    public static final int F6 = 1013;
    public static final int F7 = 1014;
    public static final int F8 = 1015;
    public static final int F9 = 1016;
    public static final int F10 = 1017;
    public static final int F11 = 1018;
    public static final int F12 = 1019;
    public static final int PRINT_SCREEN = 1020;
    public static final int SCROLL_LOCK = 1021;
    public static final int INSERT = 1025;
    private static final int[][] actionKeyCodes = {new int[]{36, 1000}, new int[]{35, 1001}, new int[]{33, 1002}, new int[]{34, 1003}, new int[]{38, 1004}, new int[]{40, 1005}, new int[]{37, LEFT}, new int[]{39, 1007}, new int[]{112, 1008}, new int[]{113, F2}, new int[]{114, F3}, new int[]{115, F4}, new int[]{116, F5}, new int[]{117, F6}, new int[]{118, F7}, new int[]{119, F8}, new int[]{120, F9}, new int[]{121, F10}, new int[]{122, F11}, new int[]{123, F12}, new int[]{154, PRINT_SCREEN}, new int[]{145, SCROLL_LOCK}, new int[]{20, 1022}, new int[]{144, 1023}, new int[]{19, 1024}, new int[]{155, INSERT}};

    private static native void initIDs();

    /* JADX WARN: Type inference failed for: r0v1, types: [int[], int[][]] */
    static {
        Toolkit.loadLibraries();
        if (!GraphicsEnvironment.isHeadless()) {
            initIDs();
        }
    }

    public Event(Object obj, long j2, int i2, int i3, int i4, int i5, int i6, Object obj2) {
        this.consumed = false;
        this.target = obj;
        this.when = j2;
        this.id = i2;
        this.f12363x = i3;
        this.f12364y = i4;
        this.key = i5;
        this.modifiers = i6;
        this.arg = obj2;
        this.data = 0L;
        this.clickCount = 0;
        switch (i2) {
            case 201:
            case 203:
            case 204:
            case 205:
            case 601:
            case SCROLL_LINE_DOWN /* 602 */:
            case SCROLL_PAGE_UP /* 603 */:
            case SCROLL_PAGE_DOWN /* 604 */:
            case SCROLL_ABSOLUTE /* 605 */:
            case SCROLL_BEGIN /* 606 */:
            case SCROLL_END /* 607 */:
            case 701:
            case LIST_DESELECT /* 702 */:
            case 1001:
                this.consumed = true;
                break;
        }
    }

    public Event(Object obj, long j2, int i2, int i3, int i4, int i5, int i6) {
        this(obj, j2, i2, i3, i4, i5, i6, null);
    }

    public Event(Object obj, int i2, Object obj2) {
        this(obj, 0L, i2, 0, 0, 0, 0, obj2);
    }

    public void translate(int i2, int i3) {
        this.f12363x += i2;
        this.f12364y += i3;
    }

    public boolean shiftDown() {
        return (this.modifiers & 1) != 0;
    }

    public boolean controlDown() {
        return (this.modifiers & 2) != 0;
    }

    public boolean metaDown() {
        return (this.modifiers & 4) != 0;
    }

    void consume() {
        switch (this.id) {
            case 401:
            case 402:
            case 403:
            case 404:
                this.consumed = true;
                break;
        }
    }

    boolean isConsumed() {
        return this.consumed;
    }

    static int getOldEventKey(KeyEvent keyEvent) {
        int keyCode = keyEvent.getKeyCode();
        for (int i2 = 0; i2 < actionKeyCodes.length; i2++) {
            if (actionKeyCodes[i2][0] == keyCode) {
                return actionKeyCodes[i2][1];
            }
        }
        return keyEvent.getKeyChar();
    }

    char getKeyEventChar() {
        for (int i2 = 0; i2 < actionKeyCodes.length; i2++) {
            if (actionKeyCodes[i2][1] == this.key) {
                return (char) 65535;
            }
        }
        return (char) this.key;
    }

    protected String paramString() {
        String str = "id=" + this.id + ",x=" + this.f12363x + ",y=" + this.f12364y;
        if (this.key != 0) {
            str = str + ",key=" + this.key;
        }
        if (shiftDown()) {
            str = str + ",shift";
        }
        if (controlDown()) {
            str = str + ",control";
        }
        if (metaDown()) {
            str = str + ",meta";
        }
        if (this.target != null) {
            str = str + ",target=" + this.target;
        }
        if (this.arg != null) {
            str = str + ",arg=" + this.arg;
        }
        return str;
    }

    public String toString() {
        return getClass().getName() + "[" + paramString() + "]";
    }
}
