package java.awt.event;

import java.awt.Component;
import java.awt.GraphicsEnvironment;
import java.awt.IllegalComponentStateException;
import java.awt.Point;
import java.awt.Toolkit;
import java.io.IOException;
import java.io.ObjectInputStream;
import org.slf4j.Marker;
import sun.awt.AWTAccessor;
import sun.awt.SunToolkit;

/* loaded from: rt.jar:java/awt/event/MouseEvent.class */
public class MouseEvent extends InputEvent {
    public static final int MOUSE_FIRST = 500;
    public static final int MOUSE_LAST = 507;
    public static final int MOUSE_CLICKED = 500;
    public static final int MOUSE_PRESSED = 501;
    public static final int MOUSE_RELEASED = 502;
    public static final int MOUSE_MOVED = 503;
    public static final int MOUSE_ENTERED = 504;
    public static final int MOUSE_EXITED = 505;
    public static final int MOUSE_DRAGGED = 506;
    public static final int MOUSE_WHEEL = 507;
    public static final int NOBUTTON = 0;
    public static final int BUTTON1 = 1;
    public static final int BUTTON2 = 2;
    public static final int BUTTON3 = 3;

    /* renamed from: x, reason: collision with root package name */
    int f12376x;

    /* renamed from: y, reason: collision with root package name */
    int f12377y;
    private int xAbs;
    private int yAbs;
    int clickCount;
    private boolean causedByTouchEvent;
    int button;
    boolean popupTrigger;
    private static final long serialVersionUID = -991214153494842848L;
    private static int cachedNumberOfButtons;
    private transient boolean shouldExcludeButtonFromExtModifiers;

    private static native void initIDs();

    static {
        NativeLibLoader.loadLibraries();
        if (!GraphicsEnvironment.isHeadless()) {
            initIDs();
        }
        Toolkit defaultToolkit = Toolkit.getDefaultToolkit();
        if (defaultToolkit instanceof SunToolkit) {
            cachedNumberOfButtons = ((SunToolkit) defaultToolkit).getNumberOfButtons();
        } else {
            cachedNumberOfButtons = 3;
        }
        AWTAccessor.setMouseEventAccessor(new AWTAccessor.MouseEventAccessor() { // from class: java.awt.event.MouseEvent.1
            @Override // sun.awt.AWTAccessor.MouseEventAccessor
            public boolean isCausedByTouchEvent(MouseEvent mouseEvent) {
                return mouseEvent.causedByTouchEvent;
            }

            @Override // sun.awt.AWTAccessor.MouseEventAccessor
            public void setCausedByTouchEvent(MouseEvent mouseEvent, boolean z2) {
                mouseEvent.causedByTouchEvent = z2;
            }
        });
    }

    public Point getLocationOnScreen() {
        return new Point(this.xAbs, this.yAbs);
    }

    public int getXOnScreen() {
        return this.xAbs;
    }

    public int getYOnScreen() {
        return this.yAbs;
    }

    public MouseEvent(Component component, int i2, long j2, int i3, int i4, int i5, int i6, boolean z2, int i7) {
        this(component, i2, j2, i3, i4, i5, 0, 0, i6, z2, i7);
        new Point(0, 0);
        try {
            Point locationOnScreen = component.getLocationOnScreen();
            this.xAbs = locationOnScreen.f12370x + i4;
            this.yAbs = locationOnScreen.f12371y + i5;
        } catch (IllegalComponentStateException e2) {
            this.xAbs = 0;
            this.yAbs = 0;
        }
    }

    public MouseEvent(Component component, int i2, long j2, int i3, int i4, int i5, int i6, boolean z2) {
        this(component, i2, j2, i3, i4, i5, i6, z2, 0);
    }

    @Override // java.awt.event.InputEvent
    public int getModifiersEx() {
        int maskForButton = this.modifiers;
        if (this.shouldExcludeButtonFromExtModifiers) {
            maskForButton &= InputEvent.getMaskForButton(getButton()) ^ (-1);
        }
        return maskForButton & (-64);
    }

    public MouseEvent(Component component, int i2, long j2, int i3, int i4, int i5, int i6, int i7, int i8, boolean z2, int i9) {
        super(component, i2, j2, i3);
        this.popupTrigger = false;
        this.shouldExcludeButtonFromExtModifiers = false;
        this.f12376x = i4;
        this.f12377y = i5;
        this.xAbs = i6;
        this.yAbs = i7;
        this.clickCount = i8;
        this.popupTrigger = z2;
        if (i9 < 0) {
            throw new IllegalArgumentException("Invalid button value :" + i9);
        }
        if (i9 > 3) {
            if (!Toolkit.getDefaultToolkit().areExtraMouseButtonsEnabled()) {
                throw new IllegalArgumentException("Extra mouse events are disabled " + i9);
            }
            if (i9 > cachedNumberOfButtons) {
                throw new IllegalArgumentException("Nonexistent button " + i9);
            }
            if (getModifiersEx() != 0 && (i2 == 502 || i2 == 500)) {
                this.shouldExcludeButtonFromExtModifiers = true;
            }
        }
        this.button = i9;
        if (getModifiers() != 0 && getModifiersEx() == 0) {
            setNewModifiers();
            return;
        }
        if (getModifiers() == 0) {
            if ((getModifiersEx() != 0 || i9 != 0) && i9 <= 3) {
                setOldModifiers();
            }
        }
    }

    public int getX() {
        return this.f12376x;
    }

    public int getY() {
        return this.f12377y;
    }

    public Point getPoint() {
        int i2;
        int i3;
        synchronized (this) {
            i2 = this.f12376x;
            i3 = this.f12377y;
        }
        return new Point(i2, i3);
    }

    public synchronized void translatePoint(int i2, int i3) {
        this.f12376x += i2;
        this.f12377y += i3;
    }

    public int getClickCount() {
        return this.clickCount;
    }

    public int getButton() {
        return this.button;
    }

    public boolean isPopupTrigger() {
        return this.popupTrigger;
    }

    public static String getMouseModifiersText(int i2) {
        StringBuilder sb = new StringBuilder();
        if ((i2 & 8) != 0) {
            sb.append(Toolkit.getProperty("AWT.alt", "Alt"));
            sb.append(Marker.ANY_NON_NULL_MARKER);
        }
        if ((i2 & 4) != 0) {
            sb.append(Toolkit.getProperty("AWT.meta", "Meta"));
            sb.append(Marker.ANY_NON_NULL_MARKER);
        }
        if ((i2 & 2) != 0) {
            sb.append(Toolkit.getProperty("AWT.control", "Ctrl"));
            sb.append(Marker.ANY_NON_NULL_MARKER);
        }
        if ((i2 & 1) != 0) {
            sb.append(Toolkit.getProperty("AWT.shift", "Shift"));
            sb.append(Marker.ANY_NON_NULL_MARKER);
        }
        if ((i2 & 32) != 0) {
            sb.append(Toolkit.getProperty("AWT.altGraph", "Alt Graph"));
            sb.append(Marker.ANY_NON_NULL_MARKER);
        }
        if ((i2 & 16) != 0) {
            sb.append(Toolkit.getProperty("AWT.button1", "Button1"));
            sb.append(Marker.ANY_NON_NULL_MARKER);
        }
        if ((i2 & 8) != 0) {
            sb.append(Toolkit.getProperty("AWT.button2", "Button2"));
            sb.append(Marker.ANY_NON_NULL_MARKER);
        }
        if ((i2 & 4) != 0) {
            sb.append(Toolkit.getProperty("AWT.button3", "Button3"));
            sb.append(Marker.ANY_NON_NULL_MARKER);
        }
        for (int i3 = 1; i3 <= cachedNumberOfButtons; i3++) {
            if ((i2 & InputEvent.getMaskForButton(i3)) != 0 && sb.indexOf(Toolkit.getProperty("AWT.button" + i3, "Button" + i3)) == -1) {
                sb.append(Toolkit.getProperty("AWT.button" + i3, "Button" + i3));
                sb.append(Marker.ANY_NON_NULL_MARKER);
            }
        }
        if (sb.length() > 0) {
            sb.setLength(sb.length() - 1);
        }
        return sb.toString();
    }

    @Override // java.awt.event.ComponentEvent, java.awt.AWTEvent
    public String paramString() {
        StringBuilder sb = new StringBuilder(80);
        switch (this.id) {
            case 500:
                sb.append("MOUSE_CLICKED");
                break;
            case 501:
                sb.append("MOUSE_PRESSED");
                break;
            case 502:
                sb.append("MOUSE_RELEASED");
                break;
            case 503:
                sb.append("MOUSE_MOVED");
                break;
            case 504:
                sb.append("MOUSE_ENTERED");
                break;
            case 505:
                sb.append("MOUSE_EXITED");
                break;
            case 506:
                sb.append("MOUSE_DRAGGED");
                break;
            case 507:
                sb.append("MOUSE_WHEEL");
                break;
            default:
                sb.append("unknown type");
                break;
        }
        sb.append(",(").append(this.f12376x).append(",").append(this.f12377y).append(")");
        sb.append(",absolute(").append(this.xAbs).append(",").append(this.yAbs).append(")");
        if (this.id != 506 && this.id != 503) {
            sb.append(",button=").append(getButton());
        }
        if (getModifiers() != 0) {
            sb.append(",modifiers=").append(getMouseModifiersText(this.modifiers));
        }
        if (getModifiersEx() != 0) {
            sb.append(",extModifiers=").append(getModifiersExText(getModifiersEx()));
        }
        sb.append(",clickCount=").append(this.clickCount);
        return sb.toString();
    }

    private void setNewModifiers() {
        if ((this.modifiers & 16) != 0) {
            this.modifiers |= 1024;
        }
        if ((this.modifiers & 8) != 0) {
            this.modifiers |= 2048;
        }
        if ((this.modifiers & 4) != 0) {
            this.modifiers |= 4096;
        }
        if (this.id == 501 || this.id == 502 || this.id == 500) {
            if ((this.modifiers & 16) != 0) {
                this.button = 1;
                this.modifiers &= -13;
                if (this.id != 501) {
                    this.modifiers &= -1025;
                }
            } else if ((this.modifiers & 8) != 0) {
                this.button = 2;
                this.modifiers &= -21;
                if (this.id != 501) {
                    this.modifiers &= -2049;
                }
            } else if ((this.modifiers & 4) != 0) {
                this.button = 3;
                this.modifiers &= -25;
                if (this.id != 501) {
                    this.modifiers &= -4097;
                }
            }
        }
        if ((this.modifiers & 8) != 0) {
            this.modifiers |= 512;
        }
        if ((this.modifiers & 4) != 0) {
            this.modifiers |= 256;
        }
        if ((this.modifiers & 1) != 0) {
            this.modifiers |= 64;
        }
        if ((this.modifiers & 2) != 0) {
            this.modifiers |= 128;
        }
        if ((this.modifiers & 32) != 0) {
            this.modifiers |= 8192;
        }
    }

    private void setOldModifiers() {
        if (this.id == 501 || this.id == 502 || this.id == 500) {
            switch (this.button) {
                case 1:
                    this.modifiers |= 16;
                    break;
                case 2:
                    this.modifiers |= 8;
                    break;
                case 3:
                    this.modifiers |= 4;
                    break;
            }
        } else {
            if ((this.modifiers & 1024) != 0) {
                this.modifiers |= 16;
            }
            if ((this.modifiers & 2048) != 0) {
                this.modifiers |= 8;
            }
            if ((this.modifiers & 4096) != 0) {
                this.modifiers |= 4;
            }
        }
        if ((this.modifiers & 512) != 0) {
            this.modifiers |= 8;
        }
        if ((this.modifiers & 256) != 0) {
            this.modifiers |= 4;
        }
        if ((this.modifiers & 64) != 0) {
            this.modifiers |= 1;
        }
        if ((this.modifiers & 128) != 0) {
            this.modifiers |= 2;
        }
        if ((this.modifiers & 8192) != 0) {
            this.modifiers |= 32;
        }
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        if (getModifiers() != 0 && getModifiersEx() == 0) {
            setNewModifiers();
        }
    }
}
