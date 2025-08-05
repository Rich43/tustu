package java.awt.event;

import java.awt.Component;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import java.util.Arrays;
import org.slf4j.Marker;
import sun.awt.AWTAccessor;
import sun.security.util.SecurityConstants;
import sun.util.logging.PlatformLogger;

/* loaded from: rt.jar:java/awt/event/InputEvent.class */
public abstract class InputEvent extends ComponentEvent {
    public static final int SHIFT_MASK = 1;
    public static final int CTRL_MASK = 2;
    public static final int META_MASK = 4;
    public static final int ALT_MASK = 8;
    public static final int ALT_GRAPH_MASK = 32;
    public static final int BUTTON1_MASK = 16;
    public static final int BUTTON2_MASK = 8;
    public static final int BUTTON3_MASK = 4;
    public static final int SHIFT_DOWN_MASK = 64;
    public static final int CTRL_DOWN_MASK = 128;
    public static final int META_DOWN_MASK = 256;
    public static final int ALT_DOWN_MASK = 512;
    public static final int BUTTON1_DOWN_MASK = 1024;
    public static final int BUTTON2_DOWN_MASK = 2048;
    public static final int BUTTON3_DOWN_MASK = 4096;
    public static final int ALT_GRAPH_DOWN_MASK = 8192;
    static final int FIRST_HIGH_BIT = Integer.MIN_VALUE;
    static final int JDK_1_3_MODIFIERS = 63;
    static final int HIGH_MODIFIERS = Integer.MIN_VALUE;
    long when;
    int modifiers;
    private transient boolean canAccessSystemClipboard;
    static final long serialVersionUID = -2482525981698309786L;
    private static final PlatformLogger logger = PlatformLogger.getLogger("java.awt.event.InputEvent");
    private static final int[] BUTTON_DOWN_MASK = {1024, 2048, 4096, 16384, 32768, 65536, 131072, 262144, 524288, 1048576, 2097152, 4194304, 8388608, 16777216, 33554432, 67108864, 134217728, 268435456, 536870912, 1073741824};

    private static native void initIDs();

    static {
        NativeLibLoader.loadLibraries();
        if (!GraphicsEnvironment.isHeadless()) {
            initIDs();
        }
        AWTAccessor.setInputEventAccessor(new AWTAccessor.InputEventAccessor() { // from class: java.awt.event.InputEvent.1
            @Override // sun.awt.AWTAccessor.InputEventAccessor
            public int[] getButtonDownMasks() {
                return InputEvent.getButtonDownMasks();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static int[] getButtonDownMasks() {
        return Arrays.copyOf(BUTTON_DOWN_MASK, BUTTON_DOWN_MASK.length);
    }

    public static int getMaskForButton(int i2) {
        if (i2 <= 0 || i2 > BUTTON_DOWN_MASK.length) {
            throw new IllegalArgumentException("button doesn't exist " + i2);
        }
        return BUTTON_DOWN_MASK[i2 - 1];
    }

    InputEvent(Component component, int i2, long j2, int i3) {
        super(component, i2);
        this.when = j2;
        this.modifiers = i3;
        this.canAccessSystemClipboard = canAccessSystemClipboard();
    }

    private boolean canAccessSystemClipboard() {
        boolean z2 = false;
        if (!GraphicsEnvironment.isHeadless()) {
            SecurityManager securityManager = System.getSecurityManager();
            if (securityManager != null) {
                try {
                    securityManager.checkPermission(SecurityConstants.AWT.ACCESS_CLIPBOARD_PERMISSION);
                    z2 = true;
                } catch (SecurityException e2) {
                    if (logger.isLoggable(PlatformLogger.Level.FINE)) {
                        logger.fine("InputEvent.canAccessSystemClipboard() got SecurityException ", e2);
                    }
                }
            } else {
                z2 = true;
            }
        }
        return z2;
    }

    public boolean isShiftDown() {
        return (this.modifiers & 1) != 0;
    }

    public boolean isControlDown() {
        return (this.modifiers & 2) != 0;
    }

    public boolean isMetaDown() {
        return (this.modifiers & 4) != 0;
    }

    public boolean isAltDown() {
        return (this.modifiers & 8) != 0;
    }

    public boolean isAltGraphDown() {
        return (this.modifiers & 32) != 0;
    }

    public long getWhen() {
        return this.when;
    }

    public int getModifiers() {
        return this.modifiers & (-2147483585);
    }

    public int getModifiersEx() {
        return this.modifiers & (-64);
    }

    @Override // java.awt.AWTEvent
    public void consume() {
        this.consumed = true;
    }

    @Override // java.awt.AWTEvent
    public boolean isConsumed() {
        return this.consumed;
    }

    public static String getModifiersExText(int i2) {
        StringBuilder sb = new StringBuilder();
        if ((i2 & 256) != 0) {
            sb.append(Toolkit.getProperty("AWT.meta", "Meta"));
            sb.append(Marker.ANY_NON_NULL_MARKER);
        }
        if ((i2 & 128) != 0) {
            sb.append(Toolkit.getProperty("AWT.control", "Ctrl"));
            sb.append(Marker.ANY_NON_NULL_MARKER);
        }
        if ((i2 & 512) != 0) {
            sb.append(Toolkit.getProperty("AWT.alt", "Alt"));
            sb.append(Marker.ANY_NON_NULL_MARKER);
        }
        if ((i2 & 64) != 0) {
            sb.append(Toolkit.getProperty("AWT.shift", "Shift"));
            sb.append(Marker.ANY_NON_NULL_MARKER);
        }
        if ((i2 & 8192) != 0) {
            sb.append(Toolkit.getProperty("AWT.altGraph", "Alt Graph"));
            sb.append(Marker.ANY_NON_NULL_MARKER);
        }
        int i3 = 1;
        for (int i4 : BUTTON_DOWN_MASK) {
            if ((i2 & i4) != 0) {
                sb.append(Toolkit.getProperty("AWT.button" + i3, "Button" + i3));
                sb.append(Marker.ANY_NON_NULL_MARKER);
            }
            i3++;
        }
        if (sb.length() > 0) {
            sb.setLength(sb.length() - 1);
        }
        return sb.toString();
    }
}
