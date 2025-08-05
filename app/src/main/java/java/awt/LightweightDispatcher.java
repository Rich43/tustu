package java.awt;

import java.awt.event.AWTEventListener;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.security.AccessController;
import java.security.PrivilegedAction;
import sun.awt.AWTAccessor;
import sun.awt.AppContext;
import sun.awt.SunToolkit;
import sun.awt.dnd.SunDropTargetEvent;
import sun.util.logging.PlatformLogger;

/* compiled from: Container.java */
/* loaded from: rt.jar:java/awt/LightweightDispatcher.class */
class LightweightDispatcher implements Serializable, AWTEventListener {
    private static final long serialVersionUID = 5184291520170872969L;
    private static final int LWD_MOUSE_DRAGGED_OVER = 1500;
    private static final PlatformLogger eventLog;
    private static final int BUTTONS_DOWN_MASK;
    private Container nativeContainer;
    private Component focus;
    private Cursor nativeCursor;
    private static final long PROXY_EVENT_MASK = 131132;
    private static final long MOUSE_MASK = 131120;
    static final /* synthetic */ boolean $assertionsDisabled;
    private transient boolean isMouseInNativeContainer = false;
    private transient boolean isMouseDTInNativeContainer = false;
    private transient WeakReference<Component> mouseEventTarget = new WeakReference<>(null);
    private transient WeakReference<Component> targetLastEntered = new WeakReference<>(null);
    private transient WeakReference<Component> targetLastEnteredDT = new WeakReference<>(null);
    private long eventMask = 0;

    static {
        $assertionsDisabled = !LightweightDispatcher.class.desiredAssertionStatus();
        eventLog = PlatformLogger.getLogger("java.awt.event.LightweightDispatcher");
        int i2 = 0;
        for (int i3 : AWTAccessor.getInputEventAccessor().getButtonDownMasks()) {
            i2 |= i3;
        }
        BUTTONS_DOWN_MASK = i2;
    }

    LightweightDispatcher(Container container) {
        this.nativeContainer = container;
    }

    void dispose() {
        stopListeningForOtherDrags();
        this.mouseEventTarget.clear();
        this.targetLastEntered.clear();
        this.targetLastEnteredDT.clear();
    }

    void enableEvents(long j2) {
        this.eventMask |= j2;
    }

    boolean dispatchEvent(AWTEvent aWTEvent) throws IllegalArgumentException {
        boolean zProcessMouseEvent = false;
        if (aWTEvent instanceof SunDropTargetEvent) {
            zProcessMouseEvent = processDropTargetEvent((SunDropTargetEvent) aWTEvent);
        } else {
            if ((aWTEvent instanceof MouseEvent) && (this.eventMask & MOUSE_MASK) != 0) {
                zProcessMouseEvent = processMouseEvent((MouseEvent) aWTEvent);
            }
            if (aWTEvent.getID() == 503) {
                this.nativeContainer.updateCursorImmediately();
            }
        }
        return zProcessMouseEvent;
    }

    private boolean isMouseGrab(MouseEvent mouseEvent) {
        int modifiersEx = mouseEvent.getModifiersEx();
        if (mouseEvent.getID() == 501 || mouseEvent.getID() == 502) {
            modifiersEx ^= InputEvent.getMaskForButton(mouseEvent.getButton());
        }
        return (modifiersEx & BUTTONS_DOWN_MASK) != 0;
    }

    private boolean processMouseEvent(MouseEvent mouseEvent) throws IllegalArgumentException {
        int id = mouseEvent.getID();
        Component mouseEventTarget = this.nativeContainer.getMouseEventTarget(mouseEvent.getX(), mouseEvent.getY(), true);
        trackMouseEnterExit(mouseEventTarget, mouseEvent);
        Component component = this.mouseEventTarget.get();
        if (!isMouseGrab(mouseEvent) && id != 500) {
            component = mouseEventTarget != this.nativeContainer ? mouseEventTarget : null;
            this.mouseEventTarget = new WeakReference<>(component);
        }
        if (component != null) {
            switch (id) {
                case 500:
                    if (mouseEventTarget == component) {
                        retargetMouseEvent(mouseEventTarget, id, mouseEvent);
                        break;
                    }
                    break;
                case 501:
                    retargetMouseEvent(component, id, mouseEvent);
                    break;
                case 502:
                    retargetMouseEvent(component, id, mouseEvent);
                    break;
                case 503:
                    retargetMouseEvent(component, id, mouseEvent);
                    break;
                case 506:
                    if (isMouseGrab(mouseEvent)) {
                        retargetMouseEvent(component, id, mouseEvent);
                        break;
                    }
                    break;
                case 507:
                    if (eventLog.isLoggable(PlatformLogger.Level.FINEST) && mouseEventTarget != null) {
                        eventLog.finest("retargeting mouse wheel to " + mouseEventTarget.getName() + ", " + ((Object) mouseEventTarget.getClass()));
                    }
                    retargetMouseEvent(mouseEventTarget, id, mouseEvent);
                    break;
            }
            if (id != 507) {
                mouseEvent.consume();
            }
        }
        return mouseEvent.isConsumed();
    }

    private boolean processDropTargetEvent(SunDropTargetEvent sunDropTargetEvent) throws IllegalArgumentException {
        int id = sunDropTargetEvent.getID();
        int x2 = sunDropTargetEvent.getX();
        int y2 = sunDropTargetEvent.getY();
        if (!this.nativeContainer.contains(x2, y2)) {
            Dimension size = this.nativeContainer.getSize();
            if (size.width <= x2) {
                x2 = size.width - 1;
            } else if (x2 < 0) {
                x2 = 0;
            }
            if (size.height <= y2) {
                y2 = size.height - 1;
            } else if (y2 < 0) {
                y2 = 0;
            }
        }
        Component dropTargetEventTarget = this.nativeContainer.getDropTargetEventTarget(x2, y2, true);
        trackMouseEnterExit(dropTargetEventTarget, sunDropTargetEvent);
        if (dropTargetEventTarget != this.nativeContainer && dropTargetEventTarget != null) {
            switch (id) {
                case 504:
                case 505:
                    break;
                default:
                    retargetMouseEvent(dropTargetEventTarget, id, sunDropTargetEvent);
                    sunDropTargetEvent.consume();
                    break;
            }
        }
        return sunDropTargetEvent.isConsumed();
    }

    private void trackDropTargetEnterExit(Component component, MouseEvent mouseEvent) {
        int id = mouseEvent.getID();
        if (id == 504 && this.isMouseDTInNativeContainer) {
            this.targetLastEnteredDT.clear();
        } else if (id == 504) {
            this.isMouseDTInNativeContainer = true;
        } else if (id == 505) {
            this.isMouseDTInNativeContainer = false;
        }
        this.targetLastEnteredDT = new WeakReference<>(retargetMouseEnterExit(component, mouseEvent, this.targetLastEnteredDT.get(), this.isMouseDTInNativeContainer));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void trackMouseEnterExit(Component component, MouseEvent mouseEvent) {
        if (mouseEvent instanceof SunDropTargetEvent) {
            trackDropTargetEnterExit(component, mouseEvent);
            return;
        }
        int id = mouseEvent.getID();
        if (id != 505 && id != 506 && id != LWD_MOUSE_DRAGGED_OVER && !this.isMouseInNativeContainer) {
            this.isMouseInNativeContainer = true;
            startListeningForOtherDrags();
        } else if (id == 505) {
            this.isMouseInNativeContainer = false;
            stopListeningForOtherDrags();
        }
        this.targetLastEntered = new WeakReference<>(retargetMouseEnterExit(component, mouseEvent, this.targetLastEntered.get(), this.isMouseInNativeContainer));
    }

    private Component retargetMouseEnterExit(Component component, MouseEvent mouseEvent, Component component2, boolean z2) throws IllegalArgumentException {
        int id = mouseEvent.getID();
        Component component3 = z2 ? component : null;
        if (component2 != component3) {
            if (component2 != null) {
                retargetMouseEvent(component2, 505, mouseEvent);
            }
            if (id == 505) {
                mouseEvent.consume();
            }
            if (component3 != null) {
                retargetMouseEvent(component3, 504, mouseEvent);
            }
            if (id == 504) {
                mouseEvent.consume();
            }
        }
        return component3;
    }

    private void startListeningForOtherDrags() {
        AccessController.doPrivileged(new PrivilegedAction<Object>() { // from class: java.awt.LightweightDispatcher.1
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public Object run2() {
                LightweightDispatcher.this.nativeContainer.getToolkit().addAWTEventListener(LightweightDispatcher.this, 48L);
                return null;
            }
        });
    }

    private void stopListeningForOtherDrags() {
        AccessController.doPrivileged(new PrivilegedAction<Object>() { // from class: java.awt.LightweightDispatcher.2
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public Object run2() {
                LightweightDispatcher.this.nativeContainer.getToolkit().removeAWTEventListener(LightweightDispatcher.this);
                return null;
            }
        });
    }

    @Override // java.awt.event.AWTEventListener
    public void eventDispatched(AWTEvent aWTEvent) {
        if (!((aWTEvent instanceof MouseEvent) && !(aWTEvent instanceof SunDropTargetEvent) && aWTEvent.id == 506 && aWTEvent.getSource() != this.nativeContainer)) {
            return;
        }
        MouseEvent mouseEvent = (MouseEvent) aWTEvent;
        synchronized (this.nativeContainer.getTreeLock()) {
            Component component = mouseEvent.getComponent();
            if (component.isShowing()) {
                Container parent_NoClientCode = this.nativeContainer;
                while (parent_NoClientCode != null && !(parent_NoClientCode instanceof Window)) {
                    parent_NoClientCode = parent_NoClientCode.getParent_NoClientCode();
                }
                if (parent_NoClientCode == null || ((Window) parent_NoClientCode).isModalBlocked()) {
                    return;
                }
                final MouseEvent mouseEvent2 = new MouseEvent(this.nativeContainer, LWD_MOUSE_DRAGGED_OVER, mouseEvent.getWhen(), mouseEvent.getModifiersEx() | mouseEvent.getModifiers(), mouseEvent.getX(), mouseEvent.getY(), mouseEvent.getXOnScreen(), mouseEvent.getYOnScreen(), mouseEvent.getClickCount(), mouseEvent.isPopupTrigger(), mouseEvent.getButton());
                AWTAccessor.MouseEventAccessor mouseEventAccessor = AWTAccessor.getMouseEventAccessor();
                mouseEventAccessor.setCausedByTouchEvent(mouseEvent2, mouseEventAccessor.isCausedByTouchEvent(mouseEvent));
                mouseEvent.copyPrivateDataInto(mouseEvent2);
                final Point locationOnScreen = component.getLocationOnScreen();
                if (AppContext.getAppContext() != this.nativeContainer.appContext) {
                    SunToolkit.executeOnEventHandlerThread(this.nativeContainer, new Runnable() { // from class: java.awt.LightweightDispatcher.3
                        @Override // java.lang.Runnable
                        public void run() {
                            if (LightweightDispatcher.this.nativeContainer.isShowing()) {
                                Point locationOnScreen2 = LightweightDispatcher.this.nativeContainer.getLocationOnScreen();
                                mouseEvent2.translatePoint(locationOnScreen.f12370x - locationOnScreen2.f12370x, locationOnScreen.f12371y - locationOnScreen2.f12371y);
                                LightweightDispatcher.this.trackMouseEnterExit(LightweightDispatcher.this.nativeContainer.getMouseEventTarget(mouseEvent2.getX(), mouseEvent2.getY(), true), mouseEvent2);
                            }
                        }
                    });
                } else if (this.nativeContainer.isShowing()) {
                    Point locationOnScreen2 = this.nativeContainer.getLocationOnScreen();
                    mouseEvent2.translatePoint(locationOnScreen.f12370x - locationOnScreen2.f12370x, locationOnScreen.f12371y - locationOnScreen2.f12371y);
                    trackMouseEnterExit(this.nativeContainer.getMouseEventTarget(mouseEvent2.getX(), mouseEvent2.getY(), true), mouseEvent2);
                }
            }
        }
    }

    void retargetMouseEvent(Component component, int i2, MouseEvent mouseEvent) throws IllegalArgumentException {
        Component component2;
        MouseEvent mouseEvent2;
        if (component == null) {
            return;
        }
        int x2 = mouseEvent.getX();
        int y2 = mouseEvent.getY();
        Component parent = component;
        while (true) {
            component2 = parent;
            if (component2 == null || component2 == this.nativeContainer) {
                break;
            }
            x2 -= component2.f12361x;
            y2 -= component2.f12362y;
            parent = component2.getParent();
        }
        if (component2 != null) {
            if (mouseEvent instanceof SunDropTargetEvent) {
                mouseEvent2 = new SunDropTargetEvent(component, i2, x2, y2, ((SunDropTargetEvent) mouseEvent).getDispatcher());
            } else if (i2 == 507) {
                mouseEvent2 = new MouseWheelEvent(component, i2, mouseEvent.getWhen(), mouseEvent.getModifiersEx() | mouseEvent.getModifiers(), x2, y2, mouseEvent.getXOnScreen(), mouseEvent.getYOnScreen(), mouseEvent.getClickCount(), mouseEvent.isPopupTrigger(), ((MouseWheelEvent) mouseEvent).getScrollType(), ((MouseWheelEvent) mouseEvent).getScrollAmount(), ((MouseWheelEvent) mouseEvent).getWheelRotation(), ((MouseWheelEvent) mouseEvent).getPreciseWheelRotation());
            } else {
                mouseEvent2 = new MouseEvent(component, i2, mouseEvent.getWhen(), mouseEvent.getModifiersEx() | mouseEvent.getModifiers(), x2, y2, mouseEvent.getXOnScreen(), mouseEvent.getYOnScreen(), mouseEvent.getClickCount(), mouseEvent.isPopupTrigger(), mouseEvent.getButton());
                AWTAccessor.MouseEventAccessor mouseEventAccessor = AWTAccessor.getMouseEventAccessor();
                mouseEventAccessor.setCausedByTouchEvent(mouseEvent2, mouseEventAccessor.isCausedByTouchEvent(mouseEvent));
            }
            mouseEvent.copyPrivateDataInto(mouseEvent2);
            if (component == this.nativeContainer) {
                ((Container) component).dispatchEventToSelf(mouseEvent2);
            } else {
                if (!$assertionsDisabled && AppContext.getAppContext() != component.appContext) {
                    throw new AssertionError();
                }
                if (this.nativeContainer.modalComp == null || ((Container) this.nativeContainer.modalComp).isAncestorOf(component)) {
                    component.dispatchEvent(mouseEvent2);
                } else {
                    mouseEvent.consume();
                }
            }
            if (i2 == 507 && mouseEvent2.isConsumed()) {
                mouseEvent.consume();
            }
        }
    }
}
