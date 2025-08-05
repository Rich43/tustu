package java.awt.event;

import java.awt.Window;
import sun.awt.AppContext;
import sun.awt.SunToolkit;

/* loaded from: rt.jar:java/awt/event/WindowEvent.class */
public class WindowEvent extends ComponentEvent {
    public static final int WINDOW_FIRST = 200;
    public static final int WINDOW_OPENED = 200;
    public static final int WINDOW_CLOSING = 201;
    public static final int WINDOW_CLOSED = 202;
    public static final int WINDOW_ICONIFIED = 203;
    public static final int WINDOW_DEICONIFIED = 204;
    public static final int WINDOW_ACTIVATED = 205;
    public static final int WINDOW_DEACTIVATED = 206;
    public static final int WINDOW_GAINED_FOCUS = 207;
    public static final int WINDOW_LOST_FOCUS = 208;
    public static final int WINDOW_STATE_CHANGED = 209;
    public static final int WINDOW_LAST = 209;
    transient Window opposite;
    int oldState;
    int newState;
    private static final long serialVersionUID = -1567959133147912127L;

    public WindowEvent(Window window, int i2, Window window2, int i3, int i4) {
        super(window, i2);
        this.opposite = window2;
        this.oldState = i3;
        this.newState = i4;
    }

    public WindowEvent(Window window, int i2, Window window2) {
        this(window, i2, window2, 0, 0);
    }

    public WindowEvent(Window window, int i2, int i3, int i4) {
        this(window, i2, null, i3, i4);
    }

    public WindowEvent(Window window, int i2) {
        this(window, i2, null, 0, 0);
    }

    public Window getWindow() {
        if (this.source instanceof Window) {
            return (Window) this.source;
        }
        return null;
    }

    public Window getOppositeWindow() {
        if (this.opposite != null && SunToolkit.targetToAppContext(this.opposite) == AppContext.getAppContext()) {
            return this.opposite;
        }
        return null;
    }

    public int getOldState() {
        return this.oldState;
    }

    public int getNewState() {
        return this.newState;
    }

    @Override // java.awt.event.ComponentEvent, java.awt.AWTEvent
    public String paramString() {
        String str;
        switch (this.id) {
            case 200:
                str = "WINDOW_OPENED";
                break;
            case 201:
                str = "WINDOW_CLOSING";
                break;
            case 202:
                str = "WINDOW_CLOSED";
                break;
            case 203:
                str = "WINDOW_ICONIFIED";
                break;
            case 204:
                str = "WINDOW_DEICONIFIED";
                break;
            case 205:
                str = "WINDOW_ACTIVATED";
                break;
            case 206:
                str = "WINDOW_DEACTIVATED";
                break;
            case 207:
                str = "WINDOW_GAINED_FOCUS";
                break;
            case 208:
                str = "WINDOW_LOST_FOCUS";
                break;
            case 209:
                str = "WINDOW_STATE_CHANGED";
                break;
            default:
                str = "unknown type";
                break;
        }
        return str + ",opposite=" + ((Object) getOppositeWindow()) + ",oldState=" + this.oldState + ",newState=" + this.newState;
    }
}
