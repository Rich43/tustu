package java.awt;

import java.awt.event.ActionEvent;
import java.awt.event.AdjustmentEvent;
import java.awt.event.InputEvent;
import java.awt.event.ItemEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.peer.ComponentPeer;
import java.awt.peer.LightweightPeer;
import java.lang.reflect.Field;
import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.EventObject;
import sun.awt.AWTAccessor;
import sun.util.logging.PlatformLogger;

/* loaded from: rt.jar:java/awt/AWTEvent.class */
public abstract class AWTEvent extends EventObject {
    private byte[] bdata;
    protected int id;
    protected boolean consumed;
    private volatile transient AccessControlContext acc;
    transient boolean focusManagerIsDispatching;
    transient boolean isPosted;
    private transient boolean isSystemGenerated;
    public static final long COMPONENT_EVENT_MASK = 1;
    public static final long CONTAINER_EVENT_MASK = 2;
    public static final long FOCUS_EVENT_MASK = 4;
    public static final long KEY_EVENT_MASK = 8;
    public static final long MOUSE_EVENT_MASK = 16;
    public static final long MOUSE_MOTION_EVENT_MASK = 32;
    public static final long WINDOW_EVENT_MASK = 64;
    public static final long ACTION_EVENT_MASK = 128;
    public static final long ADJUSTMENT_EVENT_MASK = 256;
    public static final long ITEM_EVENT_MASK = 512;
    public static final long TEXT_EVENT_MASK = 1024;
    public static final long INPUT_METHOD_EVENT_MASK = 2048;
    static final long INPUT_METHODS_ENABLED_MASK = 4096;
    public static final long PAINT_EVENT_MASK = 8192;
    public static final long INVOCATION_EVENT_MASK = 16384;
    public static final long HIERARCHY_EVENT_MASK = 32768;
    public static final long HIERARCHY_BOUNDS_EVENT_MASK = 65536;
    public static final long MOUSE_WHEEL_EVENT_MASK = 131072;
    public static final long WINDOW_STATE_EVENT_MASK = 262144;
    public static final long WINDOW_FOCUS_EVENT_MASK = 524288;
    public static final int RESERVED_ID_MAX = 1999;
    private static final long serialVersionUID = -1825314779160409405L;
    private static final PlatformLogger log = PlatformLogger.getLogger("java.awt.AWTEvent");
    private static Field inputEvent_CanAccessSystemClipboard_Field = null;

    private static native void initIDs();

    private native void nativeSetSource(ComponentPeer componentPeer);

    static {
        Toolkit.loadLibraries();
        if (!GraphicsEnvironment.isHeadless()) {
            initIDs();
        }
        AWTAccessor.setAWTEventAccessor(new AWTAccessor.AWTEventAccessor() { // from class: java.awt.AWTEvent.1
            @Override // sun.awt.AWTAccessor.AWTEventAccessor
            public void setPosted(AWTEvent aWTEvent) {
                aWTEvent.isPosted = true;
            }

            @Override // sun.awt.AWTAccessor.AWTEventAccessor
            public void setSystemGenerated(AWTEvent aWTEvent) {
                aWTEvent.isSystemGenerated = true;
            }

            @Override // sun.awt.AWTAccessor.AWTEventAccessor
            public boolean isSystemGenerated(AWTEvent aWTEvent) {
                return aWTEvent.isSystemGenerated;
            }

            @Override // sun.awt.AWTAccessor.AWTEventAccessor
            public AccessControlContext getAccessControlContext(AWTEvent aWTEvent) {
                return aWTEvent.getAccessControlContext();
            }

            @Override // sun.awt.AWTAccessor.AWTEventAccessor
            public byte[] getBData(AWTEvent aWTEvent) {
                return aWTEvent.bdata;
            }

            @Override // sun.awt.AWTAccessor.AWTEventAccessor
            public void setBData(AWTEvent aWTEvent, byte[] bArr) {
                aWTEvent.bdata = bArr;
            }
        });
    }

    final AccessControlContext getAccessControlContext() {
        if (this.acc == null) {
            throw new SecurityException("AWTEvent is missing AccessControlContext");
        }
        return this.acc;
    }

    private static synchronized Field get_InputEvent_CanAccessSystemClipboard() {
        if (inputEvent_CanAccessSystemClipboard_Field == null) {
            inputEvent_CanAccessSystemClipboard_Field = (Field) AccessController.doPrivileged(new PrivilegedAction<Field>() { // from class: java.awt.AWTEvent.2
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.security.PrivilegedAction
                public Field run() {
                    try {
                        Field declaredField = InputEvent.class.getDeclaredField("canAccessSystemClipboard");
                        declaredField.setAccessible(true);
                        return declaredField;
                    } catch (NoSuchFieldException e2) {
                        if (AWTEvent.log.isLoggable(PlatformLogger.Level.FINE)) {
                            AWTEvent.log.fine("AWTEvent.get_InputEvent_CanAccessSystemClipboard() got NoSuchFieldException ", e2);
                            return null;
                        }
                        return null;
                    } catch (SecurityException e3) {
                        if (AWTEvent.log.isLoggable(PlatformLogger.Level.FINE)) {
                            AWTEvent.log.fine("AWTEvent.get_InputEvent_CanAccessSystemClipboard() got SecurityException ", e3);
                            return null;
                        }
                        return null;
                    }
                }
            });
        }
        return inputEvent_CanAccessSystemClipboard_Field;
    }

    public AWTEvent(Event event) {
        this(event.target, event.id);
    }

    public AWTEvent(Object obj, int i2) {
        super(obj);
        this.consumed = false;
        this.acc = AccessController.getContext();
        this.focusManagerIsDispatching = false;
        this.id = i2;
        switch (i2) {
            case 601:
            case 701:
            case 900:
            case 1001:
                this.consumed = true;
                break;
        }
    }

    public void setSource(Object obj) {
        ComponentPeer componentPeer;
        if (this.source == obj) {
            return;
        }
        Component component = null;
        if (obj instanceof Component) {
            Component component2 = (Component) obj;
            while (true) {
                component = component2;
                if (component == null || component.peer == null || !(component.peer instanceof LightweightPeer)) {
                    break;
                } else {
                    component2 = component.parent;
                }
            }
        }
        synchronized (this) {
            this.source = obj;
            if (component != null && (componentPeer = component.peer) != null) {
                nativeSetSource(componentPeer);
            }
        }
    }

    public int getID() {
        return this.id;
    }

    @Override // java.util.EventObject
    public String toString() {
        String name = null;
        if (this.source instanceof Component) {
            name = ((Component) this.source).getName();
        } else if (this.source instanceof MenuComponent) {
            name = ((MenuComponent) this.source).getName();
        }
        return getClass().getName() + "[" + paramString() + "] on " + (name != null ? name : this.source);
    }

    public String paramString() {
        return "";
    }

    protected void consume() {
        switch (this.id) {
            case 401:
            case 402:
            case 501:
            case 502:
            case 503:
            case 504:
            case 505:
            case 506:
            case 507:
            case 1100:
            case 1101:
                this.consumed = true;
                break;
        }
    }

    protected boolean isConsumed() {
        return this.consumed;
    }

    Event convertToOld() {
        int i2;
        int i3;
        Object objValueOf;
        String actionCommand;
        Object source = getSource();
        int i4 = this.id;
        switch (this.id) {
            case 100:
                if ((source instanceof Frame) || (source instanceof Dialog)) {
                    Point location = ((Component) source).getLocation();
                    return new Event(source, 0L, 205, location.f12370x, location.f12371y, 0, 0);
                }
                return null;
            case 201:
            case 203:
            case 204:
                return new Event(source, i4, null);
            case 401:
            case 402:
                KeyEvent keyEvent = (KeyEvent) this;
                if (keyEvent.isActionKey()) {
                    i4 = this.id == 401 ? 403 : 404;
                }
                int keyCode = keyEvent.getKeyCode();
                if (keyCode == 16 || keyCode == 17 || keyCode == 18) {
                    return null;
                }
                return new Event(source, keyEvent.getWhen(), i4, 0, 0, Event.getOldEventKey(keyEvent), keyEvent.getModifiers() & (-17));
            case 501:
            case 502:
            case 503:
            case 504:
            case 505:
            case 506:
                MouseEvent mouseEvent = (MouseEvent) this;
                Event event = new Event(source, mouseEvent.getWhen(), i4, mouseEvent.getX(), mouseEvent.getY(), 0, mouseEvent.getModifiers() & (-17));
                event.clickCount = mouseEvent.getClickCount();
                return event;
            case 601:
                AdjustmentEvent adjustmentEvent = (AdjustmentEvent) this;
                switch (adjustmentEvent.getAdjustmentType()) {
                    case 1:
                        i2 = 602;
                        break;
                    case 2:
                        i2 = 601;
                        break;
                    case 3:
                        i2 = 603;
                        break;
                    case 4:
                        i2 = 604;
                        break;
                    case 5:
                        if (adjustmentEvent.getValueIsAdjusting()) {
                            i2 = 605;
                            break;
                        } else {
                            i2 = 607;
                            break;
                        }
                    default:
                        return null;
                }
                return new Event(source, i2, Integer.valueOf(adjustmentEvent.getValue()));
            case 701:
                ItemEvent itemEvent = (ItemEvent) this;
                if (source instanceof List) {
                    i3 = itemEvent.getStateChange() == 1 ? 701 : Event.LIST_DESELECT;
                    objValueOf = itemEvent.getItem();
                } else {
                    i3 = 1001;
                    if (source instanceof Choice) {
                        objValueOf = itemEvent.getItem();
                    } else {
                        objValueOf = Boolean.valueOf(itemEvent.getStateChange() == 1);
                    }
                }
                return new Event(source, i3, objValueOf);
            case 1001:
                ActionEvent actionEvent = (ActionEvent) this;
                if (source instanceof Button) {
                    actionCommand = ((Button) source).getLabel();
                } else if (source instanceof MenuItem) {
                    actionCommand = ((MenuItem) source).getLabel();
                } else {
                    actionCommand = actionEvent.getActionCommand();
                }
                return new Event(source, 0L, i4, 0, 0, 0, actionEvent.getModifiers(), actionCommand);
            case 1004:
                return new Event(source, 1004, null);
            case 1005:
                return new Event(source, 1005, null);
            default:
                return null;
        }
    }

    void copyPrivateDataInto(AWTEvent aWTEvent) throws IllegalArgumentException {
        Field field;
        aWTEvent.bdata = this.bdata;
        if ((this instanceof InputEvent) && (aWTEvent instanceof InputEvent) && (field = get_InputEvent_CanAccessSystemClipboard()) != null) {
            try {
                field.setBoolean(aWTEvent, field.getBoolean(this));
            } catch (IllegalAccessException e2) {
                if (log.isLoggable(PlatformLogger.Level.FINE)) {
                    log.fine("AWTEvent.copyPrivateDataInto() got IllegalAccessException ", e2);
                }
            }
        }
        aWTEvent.isSystemGenerated = this.isSystemGenerated;
    }

    void dispatched() throws IllegalArgumentException {
        Field field;
        if ((this instanceof InputEvent) && (field = get_InputEvent_CanAccessSystemClipboard()) != null) {
            try {
                field.setBoolean(this, false);
            } catch (IllegalAccessException e2) {
                if (log.isLoggable(PlatformLogger.Level.FINE)) {
                    log.fine("AWTEvent.dispatched() got IllegalAccessException ", e2);
                }
            }
        }
    }
}
