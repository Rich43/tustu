package sun.awt;

import java.awt.Canvas;
import java.awt.Component;
import java.awt.Panel;
import java.awt.Scrollbar;
import java.awt.Window;
import java.awt.peer.ComponentPeer;
import java.awt.peer.KeyboardFocusManagerPeer;
import sun.awt.AWTAccessor;
import sun.awt.CausedFocusEvent;
import sun.util.logging.PlatformLogger;

/* loaded from: rt.jar:sun/awt/KeyboardFocusManagerPeerImpl.class */
public abstract class KeyboardFocusManagerPeerImpl implements KeyboardFocusManagerPeer {
    private static final PlatformLogger focusLog = PlatformLogger.getLogger("sun.awt.focus.KeyboardFocusManagerPeerImpl");
    public static final int SNFH_FAILURE = 0;
    public static final int SNFH_SUCCESS_HANDLED = 1;
    public static final int SNFH_SUCCESS_PROCEED = 2;

    /* loaded from: rt.jar:sun/awt/KeyboardFocusManagerPeerImpl$KfmAccessor.class */
    private static class KfmAccessor {
        private static AWTAccessor.KeyboardFocusManagerAccessor instance = AWTAccessor.getKeyboardFocusManagerAccessor();

        private KfmAccessor() {
        }
    }

    @Override // java.awt.peer.KeyboardFocusManagerPeer
    public void clearGlobalFocusOwner(Window window) {
        if (window != null) {
            Component focusOwner = window.getFocusOwner();
            if (focusLog.isLoggable(PlatformLogger.Level.FINE)) {
                focusLog.fine("Clearing global focus owner " + ((Object) focusOwner));
            }
            if (focusOwner != null) {
                SunToolkit.postPriorityEvent(new CausedFocusEvent(focusOwner, 1005, false, null, CausedFocusEvent.Cause.CLEAR_GLOBAL_FOCUS_OWNER));
            }
        }
    }

    public static boolean shouldFocusOnClick(Component component) {
        boolean zIsFocusable;
        if ((component instanceof Canvas) || (component instanceof Scrollbar)) {
            zIsFocusable = true;
        } else if (component instanceof Panel) {
            zIsFocusable = ((Panel) component).getComponentCount() == 0;
        } else {
            ComponentPeer peer = component != null ? component.getPeer() : null;
            zIsFocusable = peer != null ? peer.isFocusable() : false;
        }
        return zIsFocusable && AWTAccessor.getComponentAccessor().canBeFocusOwner(component);
    }

    public static boolean deliverFocus(Component component, Component component2, boolean z2, boolean z3, long j2, CausedFocusEvent.Cause cause, Component component3) {
        if (component == null) {
            component = component2;
        }
        Component component4 = component3;
        if (component4 != null && component4.getPeer() == null) {
            component4 = null;
        }
        if (component4 != null) {
            CausedFocusEvent causedFocusEvent = new CausedFocusEvent(component4, 1005, false, component, cause);
            if (focusLog.isLoggable(PlatformLogger.Level.FINER)) {
                focusLog.finer("Posting focus event: " + ((Object) causedFocusEvent));
            }
            SunToolkit.postEvent(SunToolkit.targetToAppContext(component4), causedFocusEvent);
        }
        CausedFocusEvent causedFocusEvent2 = new CausedFocusEvent(component, 1004, false, component4, cause);
        if (focusLog.isLoggable(PlatformLogger.Level.FINER)) {
            focusLog.finer("Posting focus event: " + ((Object) causedFocusEvent2));
        }
        SunToolkit.postEvent(SunToolkit.targetToAppContext(component), causedFocusEvent2);
        return true;
    }

    public static boolean requestFocusFor(Component component, CausedFocusEvent.Cause cause) {
        return AWTAccessor.getComponentAccessor().requestFocus(component, cause);
    }

    public static int shouldNativelyFocusHeavyweight(Component component, Component component2, boolean z2, boolean z3, long j2, CausedFocusEvent.Cause cause) {
        return KfmAccessor.instance.shouldNativelyFocusHeavyweight(component, component2, z2, z3, j2, cause);
    }

    public static void removeLastFocusRequest(Component component) {
        KfmAccessor.instance.removeLastFocusRequest(component);
    }

    public static boolean processSynchronousLightweightTransfer(Component component, Component component2, boolean z2, boolean z3, long j2) {
        return KfmAccessor.instance.processSynchronousLightweightTransfer(component, component2, z2, z3, j2);
    }
}
