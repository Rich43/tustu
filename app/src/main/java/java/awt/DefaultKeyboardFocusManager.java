package java.awt;

import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.peer.ComponentPeer;
import java.awt.peer.LightweightPeer;
import java.lang.ref.WeakReference;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Set;
import sun.awt.AWTAccessor;
import sun.awt.AppContext;
import sun.awt.CausedFocusEvent;
import sun.awt.SunToolkit;
import sun.awt.TimedWindowEvent;
import sun.util.logging.PlatformLogger;

/* loaded from: rt.jar:java/awt/DefaultKeyboardFocusManager.class */
public class DefaultKeyboardFocusManager extends KeyboardFocusManager {
    private static final PlatformLogger focusLog = PlatformLogger.getLogger("java.awt.focus.DefaultKeyboardFocusManager");
    private static final WeakReference<Window> NULL_WINDOW_WR = new WeakReference<>(null);
    private static final WeakReference<Component> NULL_COMPONENT_WR = new WeakReference<>(null);
    private int inSendMessage;
    private boolean consumeNextKeyTyped;
    private Component restoreFocusTo;
    private WeakReference<Window> realOppositeWindowWR = NULL_WINDOW_WR;
    private WeakReference<Component> realOppositeComponentWR = NULL_COMPONENT_WR;
    private LinkedList<KeyEvent> enqueuedKeyEvents = new LinkedList<>();
    private LinkedList<TypeAheadMarker> typeAheadMarkers = new LinkedList<>();

    static /* synthetic */ int access$108(DefaultKeyboardFocusManager defaultKeyboardFocusManager) {
        int i2 = defaultKeyboardFocusManager.inSendMessage;
        defaultKeyboardFocusManager.inSendMessage = i2 + 1;
        return i2;
    }

    static /* synthetic */ int access$110(DefaultKeyboardFocusManager defaultKeyboardFocusManager) {
        int i2 = defaultKeyboardFocusManager.inSendMessage;
        defaultKeyboardFocusManager.inSendMessage = i2 - 1;
        return i2;
    }

    static {
        AWTAccessor.setDefaultKeyboardFocusManagerAccessor(new AWTAccessor.DefaultKeyboardFocusManagerAccessor() { // from class: java.awt.DefaultKeyboardFocusManager.1
            @Override // sun.awt.AWTAccessor.DefaultKeyboardFocusManagerAccessor
            public void consumeNextKeyTyped(DefaultKeyboardFocusManager defaultKeyboardFocusManager, KeyEvent keyEvent) {
                defaultKeyboardFocusManager.consumeNextKeyTyped(keyEvent);
            }
        });
    }

    /* loaded from: rt.jar:java/awt/DefaultKeyboardFocusManager$TypeAheadMarker.class */
    private static class TypeAheadMarker {
        long after;
        Component untilFocused;

        TypeAheadMarker(long j2, Component component) {
            this.after = j2;
            this.untilFocused = component;
        }

        public String toString() {
            return ">>> Marker after " + this.after + " on " + ((Object) this.untilFocused);
        }
    }

    private Window getOwningFrameDialog(Window window) {
        while (window != null && !(window instanceof Frame) && !(window instanceof Dialog)) {
            window = (Window) window.getParent();
        }
        return window;
    }

    private void restoreFocus(FocusEvent focusEvent, Window window) {
        Component component = this.realOppositeComponentWR.get();
        Component component2 = focusEvent.getComponent();
        if (window == null || !restoreFocus(window, component2, false)) {
            if (component == null || !doRestoreFocus(component, component2, false)) {
                if (focusEvent.getOppositeComponent() == null || !doRestoreFocus(focusEvent.getOppositeComponent(), component2, false)) {
                    clearGlobalFocusOwnerPriv();
                }
            }
        }
    }

    private void restoreFocus(WindowEvent windowEvent) {
        Window window = this.realOppositeWindowWR.get();
        if (window == null || !restoreFocus(window, null, false)) {
            if (windowEvent.getOppositeWindow() == null || !restoreFocus(windowEvent.getOppositeWindow(), null, false)) {
                clearGlobalFocusOwnerPriv();
            }
        }
    }

    private boolean restoreFocus(Window window, Component component, boolean z2) {
        this.restoreFocusTo = null;
        Component mostRecentFocusOwner = KeyboardFocusManager.getMostRecentFocusOwner(window);
        if (mostRecentFocusOwner != null && mostRecentFocusOwner != component) {
            if (getHeavyweight(window) != getNativeFocusOwner()) {
                if (!mostRecentFocusOwner.isShowing() || !mostRecentFocusOwner.canBeFocusOwner()) {
                    mostRecentFocusOwner = mostRecentFocusOwner.getNextFocusCandidate();
                }
                if (mostRecentFocusOwner != null && mostRecentFocusOwner != component) {
                    if (!mostRecentFocusOwner.requestFocus(false, CausedFocusEvent.Cause.ROLLBACK)) {
                        this.restoreFocusTo = mostRecentFocusOwner;
                        return true;
                    }
                    return true;
                }
            } else if (doRestoreFocus(mostRecentFocusOwner, component, false)) {
                return true;
            }
        }
        if (z2) {
            clearGlobalFocusOwnerPriv();
            return true;
        }
        return false;
    }

    private boolean restoreFocus(Component component, boolean z2) {
        return doRestoreFocus(component, null, z2);
    }

    private boolean doRestoreFocus(Component component, Component component2, boolean z2) {
        boolean z3 = true;
        if (component != component2 && component.isShowing() && component.canBeFocusOwner()) {
            boolean zRequestFocus = component.requestFocus(false, CausedFocusEvent.Cause.ROLLBACK);
            z3 = zRequestFocus;
            if (zRequestFocus) {
                return true;
            }
        }
        if (!z3 && getGlobalFocusedWindow() != SunToolkit.getContainingWindow(component)) {
            this.restoreFocusTo = component;
            return true;
        }
        Component nextFocusCandidate = component.getNextFocusCandidate();
        if (nextFocusCandidate != null && nextFocusCandidate != component2 && nextFocusCandidate.requestFocusInWindow(CausedFocusEvent.Cause.ROLLBACK)) {
            return true;
        }
        if (z2) {
            clearGlobalFocusOwnerPriv();
            return true;
        }
        return false;
    }

    /* loaded from: rt.jar:java/awt/DefaultKeyboardFocusManager$DefaultKeyboardFocusManagerSentEvent.class */
    private static class DefaultKeyboardFocusManagerSentEvent extends SentEvent {
        private static final long serialVersionUID = -2924743257508701758L;

        public DefaultKeyboardFocusManagerSentEvent(AWTEvent aWTEvent, AppContext appContext) {
            super(aWTEvent, appContext);
        }

        @Override // java.awt.SentEvent, java.awt.ActiveEvent
        public final void dispatch() {
            KeyboardFocusManager currentKeyboardFocusManager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
            DefaultKeyboardFocusManager defaultKeyboardFocusManager = currentKeyboardFocusManager instanceof DefaultKeyboardFocusManager ? (DefaultKeyboardFocusManager) currentKeyboardFocusManager : null;
            if (defaultKeyboardFocusManager != null) {
                synchronized (defaultKeyboardFocusManager) {
                    DefaultKeyboardFocusManager.access$108(defaultKeyboardFocusManager);
                }
            }
            super.dispatch();
            if (defaultKeyboardFocusManager != null) {
                synchronized (defaultKeyboardFocusManager) {
                    DefaultKeyboardFocusManager.access$110(defaultKeyboardFocusManager);
                }
            }
        }
    }

    static boolean sendMessage(Component component, AWTEvent aWTEvent) {
        aWTEvent.isPosted = true;
        AppContext appContext = AppContext.getAppContext();
        final AppContext appContext2 = component.appContext;
        final DefaultKeyboardFocusManagerSentEvent defaultKeyboardFocusManagerSentEvent = new DefaultKeyboardFocusManagerSentEvent(aWTEvent, appContext);
        if (appContext == appContext2) {
            defaultKeyboardFocusManagerSentEvent.dispatch();
        } else {
            if (appContext2.isDisposed()) {
                return false;
            }
            SunToolkit.postEvent(appContext2, defaultKeyboardFocusManagerSentEvent);
            if (EventQueue.isDispatchThread()) {
                ((EventDispatchThread) Thread.currentThread()).pumpEvents(1007, new Conditional() { // from class: java.awt.DefaultKeyboardFocusManager.2
                    @Override // java.awt.Conditional
                    public boolean evaluate() {
                        return (defaultKeyboardFocusManagerSentEvent.dispatched || appContext2.isDisposed()) ? false : true;
                    }
                });
            } else {
                synchronized (defaultKeyboardFocusManagerSentEvent) {
                    while (!defaultKeyboardFocusManagerSentEvent.dispatched && !appContext2.isDisposed()) {
                        try {
                            defaultKeyboardFocusManagerSentEvent.wait(1000L);
                        } catch (InterruptedException e2) {
                        }
                    }
                }
            }
        }
        return defaultKeyboardFocusManagerSentEvent.dispatched;
    }

    private boolean repostIfFollowsKeyEvents(WindowEvent windowEvent) {
        Window containingWindow;
        if (!(windowEvent instanceof TimedWindowEvent)) {
            return false;
        }
        long when = ((TimedWindowEvent) windowEvent).getWhen();
        synchronized (this) {
            KeyEvent first = this.enqueuedKeyEvents.isEmpty() ? null : this.enqueuedKeyEvents.getFirst();
            if (first != null && when >= first.getWhen()) {
                TypeAheadMarker first2 = this.typeAheadMarkers.isEmpty() ? null : this.typeAheadMarkers.getFirst();
                if (first2 != null && (containingWindow = first2.untilFocused.getContainingWindow()) != null && containingWindow.isFocused()) {
                    SunToolkit.postEvent(AppContext.getAppContext(), new SequencedEvent(windowEvent));
                    return true;
                }
            }
            return false;
        }
    }

    @Override // java.awt.KeyboardFocusManager
    public boolean dispatchEvent(AWTEvent aWTEvent) {
        Component temporaryLostComponent;
        if (focusLog.isLoggable(PlatformLogger.Level.FINE) && ((aWTEvent instanceof WindowEvent) || (aWTEvent instanceof FocusEvent))) {
            focusLog.fine("" + ((Object) aWTEvent));
        }
        switch (aWTEvent.getID()) {
            case 205:
                WindowEvent windowEvent = (WindowEvent) aWTEvent;
                Window globalActiveWindow = getGlobalActiveWindow();
                Window window = windowEvent.getWindow();
                if (globalActiveWindow != window) {
                    if (globalActiveWindow != null) {
                        if (!sendMessage(globalActiveWindow, new WindowEvent(globalActiveWindow, 206, window))) {
                            setGlobalActiveWindow(null);
                        }
                        if (getGlobalActiveWindow() != null) {
                            return true;
                        }
                    }
                    setGlobalActiveWindow(window);
                    if (window == getGlobalActiveWindow()) {
                        return typeAheadAssertions(window, windowEvent);
                    }
                    return true;
                }
                return true;
            case 206:
                AWTEvent aWTEvent2 = (WindowEvent) aWTEvent;
                Window globalActiveWindow2 = getGlobalActiveWindow();
                if (globalActiveWindow2 != null && globalActiveWindow2 == aWTEvent.getSource()) {
                    setGlobalActiveWindow(null);
                    if (getGlobalActiveWindow() == null) {
                        aWTEvent2.setSource(globalActiveWindow2);
                        return typeAheadAssertions(globalActiveWindow2, aWTEvent2);
                    }
                    return true;
                }
                return true;
            case 207:
                if (!repostIfFollowsKeyEvents((WindowEvent) aWTEvent)) {
                    WindowEvent windowEvent2 = (WindowEvent) aWTEvent;
                    Window globalFocusedWindow = getGlobalFocusedWindow();
                    Window window2 = windowEvent2.getWindow();
                    if (window2 != globalFocusedWindow) {
                        if (!window2.isFocusableWindow() || !window2.isVisible() || !window2.isDisplayable()) {
                            restoreFocus(windowEvent2);
                            return true;
                        }
                        if (globalFocusedWindow != null && !sendMessage(globalFocusedWindow, new WindowEvent(globalFocusedWindow, 208, window2))) {
                            setGlobalFocusOwner(null);
                            setGlobalFocusedWindow(null);
                        }
                        Window owningFrameDialog = getOwningFrameDialog(window2);
                        Window globalActiveWindow3 = getGlobalActiveWindow();
                        if (owningFrameDialog != globalActiveWindow3) {
                            sendMessage(owningFrameDialog, new WindowEvent(owningFrameDialog, 205, globalActiveWindow3));
                            if (owningFrameDialog != getGlobalActiveWindow()) {
                                restoreFocus(windowEvent2);
                                return true;
                            }
                        }
                        setGlobalFocusedWindow(window2);
                        if (window2 != getGlobalFocusedWindow()) {
                            restoreFocus(windowEvent2);
                            return true;
                        }
                        if (this.inSendMessage == 0) {
                            Component mostRecentFocusOwner = KeyboardFocusManager.getMostRecentFocusOwner(window2);
                            boolean z2 = this.restoreFocusTo != null && mostRecentFocusOwner == this.restoreFocusTo;
                            if (mostRecentFocusOwner == null && window2.isFocusableWindow()) {
                                mostRecentFocusOwner = window2.getFocusTraversalPolicy().getInitialComponent(window2);
                            }
                            synchronized (KeyboardFocusManager.class) {
                                temporaryLostComponent = window2.setTemporaryLostComponent(null);
                            }
                            if (focusLog.isLoggable(PlatformLogger.Level.FINER)) {
                                focusLog.finer("tempLost {0}, toFocus {1}", temporaryLostComponent, mostRecentFocusOwner);
                            }
                            if (temporaryLostComponent != null) {
                                temporaryLostComponent.requestFocusInWindow((z2 && temporaryLostComponent == mostRecentFocusOwner) ? CausedFocusEvent.Cause.ROLLBACK : CausedFocusEvent.Cause.ACTIVATION);
                            }
                            if (mostRecentFocusOwner != null && mostRecentFocusOwner != temporaryLostComponent) {
                                mostRecentFocusOwner.requestFocusInWindow(CausedFocusEvent.Cause.ACTIVATION);
                            }
                        }
                        this.restoreFocusTo = null;
                        Window window3 = this.realOppositeWindowWR.get();
                        if (window3 != windowEvent2.getOppositeWindow()) {
                            windowEvent2 = new WindowEvent(window2, 207, window3);
                        }
                        return typeAheadAssertions(window2, windowEvent2);
                    }
                    return true;
                }
                return true;
            case 208:
                if (!repostIfFollowsKeyEvents((WindowEvent) aWTEvent)) {
                    WindowEvent windowEvent3 = (WindowEvent) aWTEvent;
                    Window globalFocusedWindow2 = getGlobalFocusedWindow();
                    Window window4 = windowEvent3.getWindow();
                    Window globalActiveWindow4 = getGlobalActiveWindow();
                    Window oppositeWindow = windowEvent3.getOppositeWindow();
                    if (focusLog.isLoggable(PlatformLogger.Level.FINE)) {
                        focusLog.fine("Active {0}, Current focused {1}, losing focus {2} opposite {3}", globalActiveWindow4, globalFocusedWindow2, window4, oppositeWindow);
                    }
                    if (globalFocusedWindow2 != null) {
                        if (this.inSendMessage != 0 || window4 != globalActiveWindow4 || oppositeWindow != globalFocusedWindow2) {
                            Component globalFocusOwner = getGlobalFocusOwner();
                            if (globalFocusOwner != null) {
                                Component temporaryLostComponent2 = null;
                                if (oppositeWindow != null) {
                                    temporaryLostComponent2 = oppositeWindow.getTemporaryLostComponent();
                                    if (temporaryLostComponent2 == null) {
                                        temporaryLostComponent2 = oppositeWindow.getMostRecentFocusOwner();
                                    }
                                }
                                if (temporaryLostComponent2 == null) {
                                    temporaryLostComponent2 = oppositeWindow;
                                }
                                sendMessage(globalFocusOwner, new CausedFocusEvent(globalFocusOwner, 1005, true, temporaryLostComponent2, CausedFocusEvent.Cause.ACTIVATION));
                            }
                            setGlobalFocusedWindow(null);
                            if (getGlobalFocusedWindow() != null) {
                                restoreFocus(globalFocusedWindow2, null, true);
                                return true;
                            }
                            windowEvent3.setSource(globalFocusedWindow2);
                            this.realOppositeWindowWR = oppositeWindow != null ? new WeakReference<>(globalFocusedWindow2) : NULL_WINDOW_WR;
                            typeAheadAssertions(globalFocusedWindow2, windowEvent3);
                            if (oppositeWindow == null && globalActiveWindow4 != null) {
                                sendMessage(globalActiveWindow4, new WindowEvent(globalActiveWindow4, 206, null));
                                if (getGlobalActiveWindow() != null) {
                                    restoreFocus(globalFocusedWindow2, null, true);
                                    return true;
                                }
                                return true;
                            }
                            return true;
                        }
                        return true;
                    }
                    return true;
                }
                return true;
            case 400:
            case 401:
            case 402:
                return typeAheadAssertions(null, aWTEvent);
            case 1004:
                this.restoreFocusTo = null;
                FocusEvent causedFocusEvent = (FocusEvent) aWTEvent;
                CausedFocusEvent.Cause cause = causedFocusEvent instanceof CausedFocusEvent ? ((CausedFocusEvent) causedFocusEvent).getCause() : CausedFocusEvent.Cause.UNKNOWN;
                Component globalFocusOwner2 = getGlobalFocusOwner();
                Component component = causedFocusEvent.getComponent();
                if (globalFocusOwner2 == component) {
                    if (focusLog.isLoggable(PlatformLogger.Level.FINE)) {
                        focusLog.fine("Skipping {0} because focus owner is the same", aWTEvent);
                    }
                    dequeueKeyEvents(-1L, component);
                    return true;
                }
                if (globalFocusOwner2 != null && !sendMessage(globalFocusOwner2, new CausedFocusEvent(globalFocusOwner2, 1005, causedFocusEvent.isTemporary(), component, cause))) {
                    setGlobalFocusOwner(null);
                    if (!causedFocusEvent.isTemporary()) {
                        setGlobalPermanentFocusOwner(null);
                    }
                }
                Window containingWindow = SunToolkit.getContainingWindow(component);
                Window globalFocusedWindow3 = getGlobalFocusedWindow();
                if (containingWindow != null && containingWindow != globalFocusedWindow3) {
                    sendMessage(containingWindow, new WindowEvent(containingWindow, 207, globalFocusedWindow3));
                    if (containingWindow != getGlobalFocusedWindow()) {
                        dequeueKeyEvents(-1L, component);
                        return true;
                    }
                }
                if (!component.isFocusable() || !component.isShowing() || (!component.isEnabled() && !cause.equals(CausedFocusEvent.Cause.UNKNOWN))) {
                    dequeueKeyEvents(-1L, component);
                    if (KeyboardFocusManager.isAutoFocusTransferEnabled()) {
                        if (containingWindow == null) {
                            restoreFocus(causedFocusEvent, globalFocusedWindow3);
                        } else {
                            restoreFocus(causedFocusEvent, containingWindow);
                        }
                        setMostRecentFocusOwner(containingWindow, null);
                        return true;
                    }
                    return true;
                }
                setGlobalFocusOwner(component);
                if (component != getGlobalFocusOwner()) {
                    dequeueKeyEvents(-1L, component);
                    if (KeyboardFocusManager.isAutoFocusTransferEnabled()) {
                        restoreFocus(causedFocusEvent, containingWindow);
                        return true;
                    }
                    return true;
                }
                if (!causedFocusEvent.isTemporary()) {
                    setGlobalPermanentFocusOwner(component);
                    if (component != getGlobalPermanentFocusOwner()) {
                        dequeueKeyEvents(-1L, component);
                        if (KeyboardFocusManager.isAutoFocusTransferEnabled()) {
                            restoreFocus(causedFocusEvent, containingWindow);
                            return true;
                        }
                        return true;
                    }
                }
                setNativeFocusOwner(getHeavyweight(component));
                Component component2 = this.realOppositeComponentWR.get();
                if (component2 != null && component2 != causedFocusEvent.getOppositeComponent()) {
                    causedFocusEvent = new CausedFocusEvent(component, 1004, causedFocusEvent.isTemporary(), component2, cause);
                    causedFocusEvent.isPosted = true;
                }
                return typeAheadAssertions(component, causedFocusEvent);
            case 1005:
                FocusEvent focusEvent = (FocusEvent) aWTEvent;
                Component globalFocusOwner3 = getGlobalFocusOwner();
                if (globalFocusOwner3 == null) {
                    if (focusLog.isLoggable(PlatformLogger.Level.FINE)) {
                        focusLog.fine("Skipping {0} because focus owner is null", aWTEvent);
                        return true;
                    }
                    return true;
                }
                if (globalFocusOwner3 == focusEvent.getOppositeComponent()) {
                    if (focusLog.isLoggable(PlatformLogger.Level.FINE)) {
                        focusLog.fine("Skipping {0} because current focus owner is equal to opposite", aWTEvent);
                        return true;
                    }
                    return true;
                }
                setGlobalFocusOwner(null);
                if (getGlobalFocusOwner() != null) {
                    restoreFocus(globalFocusOwner3, true);
                    return true;
                }
                if (!focusEvent.isTemporary()) {
                    setGlobalPermanentFocusOwner(null);
                    if (getGlobalPermanentFocusOwner() != null) {
                        restoreFocus(globalFocusOwner3, true);
                        return true;
                    }
                } else {
                    Window containingWindow2 = globalFocusOwner3.getContainingWindow();
                    if (containingWindow2 != null) {
                        containingWindow2.setTemporaryLostComponent(globalFocusOwner3);
                    }
                }
                setNativeFocusOwner(null);
                focusEvent.setSource(globalFocusOwner3);
                this.realOppositeComponentWR = focusEvent.getOppositeComponent() != null ? new WeakReference<>(globalFocusOwner3) : NULL_COMPONENT_WR;
                return typeAheadAssertions(globalFocusOwner3, focusEvent);
            default:
                return false;
        }
    }

    @Override // java.awt.KeyboardFocusManager, java.awt.KeyEventDispatcher
    public boolean dispatchKeyEvent(KeyEvent keyEvent) {
        Container nativeContainer;
        Component component;
        Component focusOwner = keyEvent.isPosted ? getFocusOwner() : keyEvent.getComponent();
        if (focusOwner != null && focusOwner.isShowing() && focusOwner.canBeFocusOwner() && !keyEvent.isConsumed() && (component = keyEvent.getComponent()) != null && component.isEnabled()) {
            redispatchEvent(component, keyEvent);
        }
        boolean zPostProcessKeyEvent = false;
        java.util.List<KeyEventPostProcessor> keyEventPostProcessors = getKeyEventPostProcessors();
        if (keyEventPostProcessors != null) {
            Iterator<KeyEventPostProcessor> it = keyEventPostProcessors.iterator();
            while (!zPostProcessKeyEvent && it.hasNext()) {
                zPostProcessKeyEvent = it.next().postProcessKeyEvent(keyEvent);
            }
        }
        if (!zPostProcessKeyEvent) {
            postProcessKeyEvent(keyEvent);
        }
        Component component2 = keyEvent.getComponent();
        ComponentPeer peer = component2.getPeer();
        if ((peer == null || (peer instanceof LightweightPeer)) && (nativeContainer = component2.getNativeContainer()) != null) {
            peer = nativeContainer.getPeer();
        }
        if (peer != null) {
            peer.handleEvent(keyEvent);
            return true;
        }
        return true;
    }

    @Override // java.awt.KeyboardFocusManager, java.awt.KeyEventPostProcessor
    public boolean postProcessKeyEvent(KeyEvent keyEvent) {
        if (!keyEvent.isConsumed()) {
            Component component = keyEvent.getComponent();
            Container container = (Container) (component instanceof Container ? component : component.getParent());
            if (container != null) {
                container.postProcessKeyEvent(keyEvent);
                return true;
            }
            return true;
        }
        return true;
    }

    private void pumpApprovedKeyEvents() {
        KeyEvent first;
        do {
            first = null;
            synchronized (this) {
                if (this.enqueuedKeyEvents.size() != 0) {
                    first = this.enqueuedKeyEvents.getFirst();
                    if (this.typeAheadMarkers.size() != 0 && first.getWhen() > this.typeAheadMarkers.getFirst().after) {
                        first = null;
                    }
                    if (first != null) {
                        if (focusLog.isLoggable(PlatformLogger.Level.FINER)) {
                            focusLog.finer("Pumping approved event {0}", first);
                        }
                        this.enqueuedKeyEvents.removeFirst();
                    }
                }
            }
            if (first != null) {
                preDispatchKeyEvent(first);
            }
        } while (first != null);
    }

    void dumpMarkers() {
        if (focusLog.isLoggable(PlatformLogger.Level.FINEST)) {
            focusLog.finest(">>> Markers dump, time: {0}", Long.valueOf(System.currentTimeMillis()));
            synchronized (this) {
                if (this.typeAheadMarkers.size() != 0) {
                    Iterator<TypeAheadMarker> it = this.typeAheadMarkers.iterator();
                    while (it.hasNext()) {
                        focusLog.finest("    {0}", it.next());
                    }
                }
            }
        }
    }

    private boolean typeAheadAssertions(Component component, AWTEvent aWTEvent) {
        pumpApprovedKeyEvents();
        switch (aWTEvent.getID()) {
            case 400:
            case 401:
            case 402:
                KeyEvent keyEvent = (KeyEvent) aWTEvent;
                synchronized (this) {
                    if (aWTEvent.isPosted && this.typeAheadMarkers.size() != 0) {
                        TypeAheadMarker first = this.typeAheadMarkers.getFirst();
                        if (keyEvent.getWhen() > first.after) {
                            if (focusLog.isLoggable(PlatformLogger.Level.FINER)) {
                                focusLog.finer("Storing event {0} because of marker {1}", keyEvent, first);
                            }
                            this.enqueuedKeyEvents.addLast(keyEvent);
                            return true;
                        }
                    }
                    return preDispatchKeyEvent(keyEvent);
                }
            case 1004:
                if (focusLog.isLoggable(PlatformLogger.Level.FINEST)) {
                    focusLog.finest("Markers before FOCUS_GAINED on {0}", component);
                }
                dumpMarkers();
                synchronized (this) {
                    boolean z2 = false;
                    if (hasMarker(component)) {
                        Iterator<TypeAheadMarker> it = this.typeAheadMarkers.iterator();
                        while (it.hasNext()) {
                            if (it.next().untilFocused == component) {
                                z2 = true;
                            } else if (z2) {
                            }
                            it.remove();
                        }
                    } else if (focusLog.isLoggable(PlatformLogger.Level.FINER)) {
                        focusLog.finer("Event without marker {0}", aWTEvent);
                    }
                }
                focusLog.finest("Markers after FOCUS_GAINED");
                dumpMarkers();
                redispatchEvent(component, aWTEvent);
                pumpApprovedKeyEvents();
                return true;
            default:
                redispatchEvent(component, aWTEvent);
                return true;
        }
    }

    private boolean hasMarker(Component component) {
        Iterator<TypeAheadMarker> it = this.typeAheadMarkers.iterator();
        while (it.hasNext()) {
            if (it.next().untilFocused == component) {
                return true;
            }
        }
        return false;
    }

    @Override // java.awt.KeyboardFocusManager
    void clearMarkers() {
        synchronized (this) {
            this.typeAheadMarkers.clear();
        }
    }

    private boolean preDispatchKeyEvent(KeyEvent keyEvent) {
        ComponentPeer peer;
        if (keyEvent.isPosted) {
            Component focusOwner = getFocusOwner();
            keyEvent.setSource(focusOwner != null ? focusOwner : getFocusedWindow());
        }
        if (keyEvent.getSource() == null) {
            return true;
        }
        EventQueue.setCurrentEventAndMostRecentTime(keyEvent);
        if (KeyboardFocusManager.isProxyActive(keyEvent)) {
            Container nativeContainer = ((Component) keyEvent.getSource()).getNativeContainer();
            if (nativeContainer != null && (peer = nativeContainer.getPeer()) != null) {
                peer.handleEvent(keyEvent);
                keyEvent.consume();
                return true;
            }
            return true;
        }
        java.util.List<KeyEventDispatcher> keyEventDispatchers = getKeyEventDispatchers();
        if (keyEventDispatchers != null) {
            Iterator<KeyEventDispatcher> it = keyEventDispatchers.iterator();
            while (it.hasNext()) {
                if (it.next().dispatchKeyEvent(keyEvent)) {
                    return true;
                }
            }
        }
        return dispatchKeyEvent(keyEvent);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void consumeNextKeyTyped(KeyEvent keyEvent) {
        this.consumeNextKeyTyped = true;
    }

    private void consumeTraversalKey(KeyEvent keyEvent) {
        keyEvent.consume();
        this.consumeNextKeyTyped = keyEvent.getID() == 401 && !keyEvent.isActionKey();
    }

    private boolean consumeProcessedKeyEvent(KeyEvent keyEvent) {
        if (keyEvent.getID() == 400 && this.consumeNextKeyTyped) {
            keyEvent.consume();
            this.consumeNextKeyTyped = false;
            return true;
        }
        return false;
    }

    @Override // java.awt.KeyboardFocusManager
    public void processKeyEvent(Component component, KeyEvent keyEvent) {
        if (!consumeProcessedKeyEvent(keyEvent) && keyEvent.getID() != 400 && component.getFocusTraversalKeysEnabled() && !keyEvent.isConsumed()) {
            AWTKeyStroke aWTKeyStrokeForEvent = AWTKeyStroke.getAWTKeyStrokeForEvent(keyEvent);
            AWTKeyStroke aWTKeyStroke = AWTKeyStroke.getAWTKeyStroke(aWTKeyStrokeForEvent.getKeyCode(), aWTKeyStrokeForEvent.getModifiers(), !aWTKeyStrokeForEvent.isOnKeyRelease());
            Set<AWTKeyStroke> focusTraversalKeys = component.getFocusTraversalKeys(0);
            boolean zContains = focusTraversalKeys.contains(aWTKeyStrokeForEvent);
            boolean zContains2 = focusTraversalKeys.contains(aWTKeyStroke);
            if (zContains || zContains2) {
                consumeTraversalKey(keyEvent);
                if (zContains) {
                    focusNextComponent(component);
                    return;
                }
                return;
            }
            if (keyEvent.getID() == 401) {
                this.consumeNextKeyTyped = false;
            }
            Set<AWTKeyStroke> focusTraversalKeys2 = component.getFocusTraversalKeys(1);
            boolean zContains3 = focusTraversalKeys2.contains(aWTKeyStrokeForEvent);
            boolean zContains4 = focusTraversalKeys2.contains(aWTKeyStroke);
            if (zContains3 || zContains4) {
                consumeTraversalKey(keyEvent);
                if (zContains3) {
                    focusPreviousComponent(component);
                    return;
                }
                return;
            }
            Set<AWTKeyStroke> focusTraversalKeys3 = component.getFocusTraversalKeys(2);
            boolean zContains5 = focusTraversalKeys3.contains(aWTKeyStrokeForEvent);
            boolean zContains6 = focusTraversalKeys3.contains(aWTKeyStroke);
            if (zContains5 || zContains6) {
                consumeTraversalKey(keyEvent);
                if (zContains5) {
                    upFocusCycle(component);
                    return;
                }
                return;
            }
            if (!(component instanceof Container) || !((Container) component).isFocusCycleRoot()) {
                return;
            }
            Set<AWTKeyStroke> focusTraversalKeys4 = component.getFocusTraversalKeys(3);
            boolean zContains7 = focusTraversalKeys4.contains(aWTKeyStrokeForEvent);
            boolean zContains8 = focusTraversalKeys4.contains(aWTKeyStroke);
            if (zContains7 || zContains8) {
                consumeTraversalKey(keyEvent);
                if (zContains7) {
                    downFocusCycle((Container) component);
                }
            }
        }
    }

    @Override // java.awt.KeyboardFocusManager
    protected synchronized void enqueueKeyEvents(long j2, Component component) {
        if (component == null) {
            return;
        }
        if (focusLog.isLoggable(PlatformLogger.Level.FINER)) {
            focusLog.finer("Enqueue at {0} for {1}", Long.valueOf(j2), component);
        }
        int i2 = 0;
        int size = this.typeAheadMarkers.size();
        ListIterator<TypeAheadMarker> listIterator = this.typeAheadMarkers.listIterator(size);
        while (true) {
            if (size <= 0) {
                break;
            }
            if (listIterator.previous().after > j2) {
                size--;
            } else {
                i2 = size;
                break;
            }
        }
        this.typeAheadMarkers.add(i2, new TypeAheadMarker(j2, component));
    }

    @Override // java.awt.KeyboardFocusManager
    protected synchronized void dequeueKeyEvents(long j2, Component component) {
        if (component == null) {
            return;
        }
        if (focusLog.isLoggable(PlatformLogger.Level.FINER)) {
            focusLog.finer("Dequeue at {0} for {1}", Long.valueOf(j2), component);
        }
        ListIterator<TypeAheadMarker> listIterator = this.typeAheadMarkers.listIterator(j2 >= 0 ? this.typeAheadMarkers.size() : 0);
        if (j2 < 0) {
            while (listIterator.hasNext()) {
                if (listIterator.next().untilFocused == component) {
                    listIterator.remove();
                    return;
                }
            }
            return;
        }
        while (listIterator.hasPrevious()) {
            TypeAheadMarker typeAheadMarkerPrevious = listIterator.previous();
            if (typeAheadMarkerPrevious.untilFocused == component && typeAheadMarkerPrevious.after == j2) {
                listIterator.remove();
                return;
            }
        }
    }

    @Override // java.awt.KeyboardFocusManager
    protected synchronized void discardKeyEvents(Component component) {
        boolean z2;
        if (component == null) {
            return;
        }
        long j2 = -1;
        Iterator<TypeAheadMarker> it = this.typeAheadMarkers.iterator();
        while (it.hasNext()) {
            TypeAheadMarker next = it.next();
            Component parent = next.untilFocused;
            boolean z3 = parent == component;
            while (true) {
                z2 = z3;
                if (z2 || parent == null || (parent instanceof Window)) {
                    break;
                }
                parent = parent.getParent();
                z3 = parent == component;
            }
            if (z2) {
                if (j2 < 0) {
                    j2 = next.after;
                }
                it.remove();
            } else if (j2 >= 0) {
                purgeStampedEvents(j2, next.after);
                j2 = -1;
            }
        }
        purgeStampedEvents(j2, -1L);
    }

    private void purgeStampedEvents(long j2, long j3) {
        if (j2 < 0) {
            return;
        }
        Iterator<KeyEvent> it = this.enqueuedKeyEvents.iterator();
        while (it.hasNext()) {
            long when = it.next().getWhen();
            if (j2 < when && (j3 < 0 || when <= j3)) {
                it.remove();
            }
            if (j3 >= 0 && when > j3) {
                return;
            }
        }
    }

    @Override // java.awt.KeyboardFocusManager
    public void focusPreviousComponent(Component component) {
        if (component != null) {
            component.transferFocusBackward();
        }
    }

    @Override // java.awt.KeyboardFocusManager
    public void focusNextComponent(Component component) {
        if (component != null) {
            component.transferFocus();
        }
    }

    @Override // java.awt.KeyboardFocusManager
    public void upFocusCycle(Component component) {
        if (component != null) {
            component.transferFocusUpCycle();
        }
    }

    @Override // java.awt.KeyboardFocusManager
    public void downFocusCycle(Container container) {
        if (container != null && container.isFocusCycleRoot()) {
            container.transferFocusDownCycle();
        }
    }
}
