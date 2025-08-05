package com.sun.java.accessibility.util;

import java.awt.AWTEvent;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.AWTEventListener;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Vector;
import javax.accessibility.Accessible;
import javax.accessibility.AccessibleComponent;
import javax.accessibility.AccessibleContext;
import jdk.Exported;

@Exported
/* loaded from: jaccess.jar:com/sun/java/accessibility/util/EventQueueMonitor.class */
public class EventQueueMonitor implements AWTEventListener {
    static Vector topLevelWindows = new Vector();
    static Window topLevelWindowWithFocus = null;
    static Point currentMousePosition = null;
    static Component currentMouseComponent = null;
    static GUIInitializedListener guiInitializedListener = null;
    static TopLevelWindowListener topLevelWindowListener = null;
    static MouseMotionListener mouseMotionListener = null;
    static boolean guiInitialized = false;
    static EventQueueMonitorItem componentEventQueue = null;
    private static ComponentEvtDispatchThread cedt = null;
    static Object componentEventQueueLock = new Object();

    public EventQueueMonitor() {
        if (cedt == null) {
            cedt = new ComponentEvtDispatchThread("EventQueueMonitor-ComponentEvtDispatch");
            cedt.setDaemon(true);
            cedt.start();
        }
    }

    static void queueComponentEvent(ComponentEvent componentEvent) {
        synchronized (componentEventQueueLock) {
            EventQueueMonitorItem eventQueueMonitorItem = new EventQueueMonitorItem(componentEvent);
            if (componentEventQueue == null) {
                componentEventQueue = eventQueueMonitorItem;
            } else {
                EventQueueMonitorItem eventQueueMonitorItem2 = componentEventQueue;
                while (eventQueueMonitorItem2.next != null) {
                    eventQueueMonitorItem2 = eventQueueMonitorItem2.next;
                }
                eventQueueMonitorItem2.next = eventQueueMonitorItem;
            }
            componentEventQueueLock.notifyAll();
        }
    }

    public static void maybeInitialize() {
        if (cedt == null) {
            AccessController.doPrivileged(new PrivilegedAction() { // from class: com.sun.java.accessibility.util.EventQueueMonitor.1
                @Override // java.security.PrivilegedAction
                /* renamed from: run */
                public Object run2() {
                    try {
                        Toolkit.getDefaultToolkit().addAWTEventListener(new EventQueueMonitor(), 100L);
                        return null;
                    } catch (Exception e2) {
                        return null;
                    }
                }
            });
        }
    }

    @Override // java.awt.event.AWTEventListener
    public void eventDispatched(AWTEvent aWTEvent) {
        processEvent(aWTEvent);
    }

    static void maybeNotifyAssistiveTechnologies() {
        if (!guiInitialized) {
            guiInitialized = true;
            if (guiInitializedListener != null) {
                guiInitializedListener.guiInitialized();
            }
        }
    }

    static void addTopLevelWindow(Component component) {
        Container parent;
        if (component == null) {
            return;
        }
        if (!(component instanceof Window)) {
            addTopLevelWindow(component.getParent());
            return;
        }
        if ((component instanceof Dialog) || (component instanceof Window)) {
            parent = (Container) component;
        } else {
            parent = component.getParent();
            if (parent != null) {
                addTopLevelWindow(parent);
                return;
            }
        }
        if (parent == null) {
            parent = (Container) component;
        }
        synchronized (topLevelWindows) {
            if (parent != null) {
                if (!topLevelWindows.contains(parent)) {
                    topLevelWindows.addElement(parent);
                    if (topLevelWindowListener != null) {
                        topLevelWindowListener.topLevelWindowCreated((Window) parent);
                    }
                }
            }
        }
    }

    static void removeTopLevelWindow(Window window) {
        synchronized (topLevelWindows) {
            if (topLevelWindows.contains(window)) {
                topLevelWindows.removeElement(window);
                if (topLevelWindowListener != null) {
                    topLevelWindowListener.topLevelWindowDestroyed(window);
                }
            }
        }
    }

    static void updateCurrentMousePosition(MouseEvent mouseEvent) {
        Point point = currentMousePosition;
        try {
            Point point2 = mouseEvent.getPoint();
            currentMouseComponent = (Component) mouseEvent.getSource();
            currentMousePosition = currentMouseComponent.getLocationOnScreen();
            currentMousePosition.translate(point2.f12370x, point2.f12371y);
        } catch (Exception e2) {
            currentMousePosition = point;
        }
    }

    static void processEvent(AWTEvent aWTEvent) {
        switch (aWTEvent.getID()) {
            case 200:
                if (aWTEvent instanceof ComponentEvent) {
                    ComponentEvent componentEvent = (ComponentEvent) aWTEvent;
                    if (componentEvent.getComponent() instanceof Window) {
                        addTopLevelWindow(componentEvent.getComponent());
                        maybeNotifyAssistiveTechnologies();
                        break;
                    } else {
                        maybeNotifyAssistiveTechnologies();
                        addTopLevelWindow(componentEvent.getComponent());
                        break;
                    }
                }
                break;
            case 202:
                if (aWTEvent instanceof ComponentEvent) {
                    removeTopLevelWindow((Window) ((ComponentEvent) aWTEvent).getComponent());
                    break;
                }
                break;
            case 205:
                if (aWTEvent instanceof ComponentEvent) {
                    ComponentEvent componentEvent2 = (ComponentEvent) aWTEvent;
                    if (componentEvent2.getComponent() instanceof Window) {
                        addTopLevelWindow(componentEvent2.getComponent());
                        maybeNotifyAssistiveTechnologies();
                    } else {
                        maybeNotifyAssistiveTechnologies();
                        addTopLevelWindow(componentEvent2.getComponent());
                    }
                }
                queueComponentEvent((ComponentEvent) aWTEvent);
                break;
            case 206:
            case 503:
            case 506:
            case 1004:
                queueComponentEvent((ComponentEvent) aWTEvent);
                break;
        }
    }

    static synchronized Component getShowingComponentAt(Container container, int i2, int i3) {
        if (!container.contains(i2, i3)) {
            return null;
        }
        int componentCount = container.getComponentCount();
        for (int i4 = 0; i4 < componentCount; i4++) {
            Component component = container.getComponent(i4);
            if (component != null && component.isShowing()) {
                Point location = component.getLocation();
                if (component.contains(i2 - location.f12370x, i3 - location.f12371y)) {
                    return component;
                }
            }
        }
        return container;
    }

    static synchronized Component getComponentAt(Container container, Point point) {
        if (!container.isShowing()) {
            return null;
        }
        Point locationOnScreen = container.getLocationOnScreen();
        Point point2 = new Point(point.f12370x - locationOnScreen.f12370x, point.f12371y - locationOnScreen.f12371y);
        Component showingComponentAt = getShowingComponentAt(container, point2.f12370x, point2.f12371y);
        if (showingComponentAt != container && (showingComponentAt instanceof Container)) {
            return getComponentAt((Container) showingComponentAt, point);
        }
        return showingComponentAt;
    }

    public static Accessible getAccessibleAt(Point point) {
        AccessibleComponent accessibleComponent;
        Window topLevelWindowWithFocus2 = getTopLevelWindowWithFocus();
        Window[] topLevelWindows2 = getTopLevelWindows();
        Component componentAt = null;
        if (currentMousePosition == null) {
            return null;
        }
        if (currentMousePosition.equals(point) && (currentMouseComponent instanceof Container)) {
            componentAt = getComponentAt((Container) currentMouseComponent, point);
        }
        if (componentAt == null && topLevelWindowWithFocus2 != null) {
            componentAt = getComponentAt(topLevelWindowWithFocus2, point);
        }
        if (componentAt == null) {
            for (Window window : topLevelWindows2) {
                componentAt = getComponentAt(window, point);
                if (componentAt != null) {
                    break;
                }
            }
        }
        if (componentAt instanceof Accessible) {
            AccessibleContext accessibleContext = ((Accessible) componentAt).getAccessibleContext();
            if (accessibleContext != null && (accessibleComponent = accessibleContext.getAccessibleComponent()) != null && accessibleContext.getAccessibleChildrenCount() != 0) {
                Point locationOnScreen = accessibleComponent.getLocationOnScreen();
                locationOnScreen.move(point.f12370x - locationOnScreen.f12370x, point.f12371y - locationOnScreen.f12371y);
                return accessibleComponent.getAccessibleAt(locationOnScreen);
            }
            return (Accessible) componentAt;
        }
        return Translator.getAccessible(componentAt);
    }

    public static boolean isGUIInitialized() {
        maybeInitialize();
        return guiInitialized;
    }

    public static void addGUIInitializedListener(GUIInitializedListener gUIInitializedListener) {
        maybeInitialize();
        guiInitializedListener = GUIInitializedMulticaster.add(guiInitializedListener, gUIInitializedListener);
    }

    public static void removeGUIInitializedListener(GUIInitializedListener gUIInitializedListener) {
        guiInitializedListener = GUIInitializedMulticaster.remove(guiInitializedListener, gUIInitializedListener);
    }

    public static void addTopLevelWindowListener(TopLevelWindowListener topLevelWindowListener2) {
        topLevelWindowListener = TopLevelWindowMulticaster.add(topLevelWindowListener, topLevelWindowListener2);
    }

    public static void removeTopLevelWindowListener(TopLevelWindowListener topLevelWindowListener2) {
        topLevelWindowListener = TopLevelWindowMulticaster.remove(topLevelWindowListener, topLevelWindowListener2);
    }

    public static Point getCurrentMousePosition() {
        return currentMousePosition;
    }

    public static Window[] getTopLevelWindows() {
        synchronized (topLevelWindows) {
            int size = topLevelWindows.size();
            if (size > 0) {
                Window[] windowArr = new Window[size];
                for (int i2 = 0; i2 < size; i2++) {
                    windowArr[i2] = (Window) topLevelWindows.elementAt(i2);
                }
                return windowArr;
            }
            return new Window[0];
        }
    }

    public static Window getTopLevelWindowWithFocus() {
        return topLevelWindowWithFocus;
    }
}
