package com.sun.java.swing;

import java.applet.Applet;
import java.awt.AWTEvent;
import java.awt.Component;
import java.awt.Container;
import java.awt.EventQueue;
import java.awt.Window;
import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.Callable;
import javafx.fxml.FXMLLoader;
import javax.swing.JComponent;
import javax.swing.RepaintManager;
import sun.awt.AppContext;
import sun.awt.EventQueueDelegate;
import sun.awt.SunToolkit;

/* loaded from: rt.jar:com/sun/java/swing/SwingUtilities3.class */
public class SwingUtilities3 {
    private static final Object DELEGATE_REPAINT_MANAGER_KEY;
    private static final Map<Container, Boolean> vsyncedMap;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !SwingUtilities3.class.desiredAssertionStatus();
        DELEGATE_REPAINT_MANAGER_KEY = new StringBuilder("DelegateRepaintManagerKey");
        vsyncedMap = Collections.synchronizedMap(new WeakHashMap());
    }

    public static void setDelegateRepaintManager(JComponent jComponent, RepaintManager repaintManager) {
        AppContext.getAppContext().put(DELEGATE_REPAINT_MANAGER_KEY, Boolean.TRUE);
        jComponent.putClientProperty(DELEGATE_REPAINT_MANAGER_KEY, repaintManager);
    }

    public static void setVsyncRequested(Container container, boolean z2) {
        if (!$assertionsDisabled && !(container instanceof Applet) && !(container instanceof Window)) {
            throw new AssertionError();
        }
        if (z2) {
            vsyncedMap.put(container, Boolean.TRUE);
        } else {
            vsyncedMap.remove(container);
        }
    }

    public static boolean isVsyncRequested(Container container) {
        if ($assertionsDisabled || (container instanceof Applet) || (container instanceof Window)) {
            return Boolean.TRUE == vsyncedMap.get(container);
        }
        throw new AssertionError();
    }

    public static RepaintManager getDelegateRepaintManager(Component component) {
        RepaintManager repaintManager = null;
        if (Boolean.TRUE == SunToolkit.targetToAppContext(component).get(DELEGATE_REPAINT_MANAGER_KEY)) {
            while (repaintManager == null && component != null) {
                while (component != null && !(component instanceof JComponent)) {
                    component = component.getParent();
                }
                if (component != null) {
                    repaintManager = (RepaintManager) ((JComponent) component).getClientProperty(DELEGATE_REPAINT_MANAGER_KEY);
                    component = component.getParent();
                }
            }
        }
        return repaintManager;
    }

    public static void setEventQueueDelegate(Map<String, Map<String, Object>> map) {
        EventQueueDelegate.setDelegate(new EventQueueDelegateFromMap(map));
    }

    /* loaded from: rt.jar:com/sun/java/swing/SwingUtilities3$EventQueueDelegateFromMap.class */
    private static class EventQueueDelegateFromMap implements EventQueueDelegate.Delegate {
        private final AWTEvent[] afterDispatchEventArgument;
        private final Object[] afterDispatchHandleArgument;
        private final Callable<Void> afterDispatchCallable;
        private final AWTEvent[] beforeDispatchEventArgument;
        private final Callable<Object> beforeDispatchCallable;
        private final EventQueue[] getNextEventEventQueueArgument;
        private final Callable<AWTEvent> getNextEventCallable;

        public EventQueueDelegateFromMap(Map<String, Map<String, Object>> map) {
            Map<String, Object> map2 = map.get("afterDispatch");
            this.afterDispatchEventArgument = (AWTEvent[]) map2.get(FXMLLoader.EVENT_KEY);
            this.afterDispatchHandleArgument = (Object[]) map2.get("handle");
            this.afterDispatchCallable = (Callable) map2.get("method");
            Map<String, Object> map3 = map.get("beforeDispatch");
            this.beforeDispatchEventArgument = (AWTEvent[]) map3.get(FXMLLoader.EVENT_KEY);
            this.beforeDispatchCallable = (Callable) map3.get("method");
            Map<String, Object> map4 = map.get("getNextEvent");
            this.getNextEventEventQueueArgument = (EventQueue[]) map4.get("eventQueue");
            this.getNextEventCallable = (Callable) map4.get("method");
        }

        @Override // sun.awt.EventQueueDelegate.Delegate
        public void afterDispatch(AWTEvent aWTEvent, Object obj) throws InterruptedException {
            this.afterDispatchEventArgument[0] = aWTEvent;
            this.afterDispatchHandleArgument[0] = obj;
            try {
                this.afterDispatchCallable.call();
            } catch (InterruptedException e2) {
                throw e2;
            } catch (RuntimeException e3) {
                throw e3;
            } catch (Exception e4) {
                throw new RuntimeException(e4);
            }
        }

        @Override // sun.awt.EventQueueDelegate.Delegate
        public Object beforeDispatch(AWTEvent aWTEvent) throws InterruptedException {
            this.beforeDispatchEventArgument[0] = aWTEvent;
            try {
                return this.beforeDispatchCallable.call();
            } catch (InterruptedException e2) {
                throw e2;
            } catch (RuntimeException e3) {
                throw e3;
            } catch (Exception e4) {
                throw new RuntimeException(e4);
            }
        }

        @Override // sun.awt.EventQueueDelegate.Delegate
        public AWTEvent getNextEvent(EventQueue eventQueue) throws InterruptedException {
            this.getNextEventEventQueueArgument[0] = eventQueue;
            try {
                return this.getNextEventCallable.call();
            } catch (InterruptedException e2) {
                throw e2;
            } catch (RuntimeException e3) {
                throw e3;
            } catch (Exception e4) {
                throw new RuntimeException(e4);
            }
        }
    }
}
