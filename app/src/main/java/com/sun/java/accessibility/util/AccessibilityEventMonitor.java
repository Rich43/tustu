package com.sun.java.accessibility.util;

import java.awt.Window;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.accessibility.Accessible;
import javax.accessibility.AccessibleContext;
import javax.accessibility.AccessibleRole;
import javax.accessibility.AccessibleState;
import javax.accessibility.AccessibleStateSet;
import jdk.Exported;

@Exported
/* loaded from: jaccess.jar:com/sun/java/accessibility/util/AccessibilityEventMonitor.class */
public class AccessibilityEventMonitor {
    protected static final AccessibilityListenerList listenerList = new AccessibilityListenerList();
    protected static final AccessibilityEventListener accessibilityListener = new AccessibilityEventListener();

    public static void addPropertyChangeListener(PropertyChangeListener propertyChangeListener) {
        if (listenerList.getListenerCount(PropertyChangeListener.class) == 0) {
            accessibilityListener.installListeners();
        }
        listenerList.add(PropertyChangeListener.class, propertyChangeListener);
    }

    public static void removePropertyChangeListener(PropertyChangeListener propertyChangeListener) {
        listenerList.remove(PropertyChangeListener.class, propertyChangeListener);
        if (listenerList.getListenerCount(PropertyChangeListener.class) == 0) {
            accessibilityListener.removeListeners();
        }
    }

    /* loaded from: jaccess.jar:com/sun/java/accessibility/util/AccessibilityEventMonitor$AccessibilityEventListener.class */
    static class AccessibilityEventListener implements TopLevelWindowListener, PropertyChangeListener {
        public AccessibilityEventListener() {
            EventQueueMonitor.addTopLevelWindowListener(this);
        }

        protected void installListeners() {
            Window[] topLevelWindows = EventQueueMonitor.getTopLevelWindows();
            if (topLevelWindows != null) {
                for (int i2 = 0; i2 < topLevelWindows.length; i2++) {
                    if (topLevelWindows[i2] instanceof Accessible) {
                        installListeners(topLevelWindows[i2]);
                    }
                }
            }
        }

        protected void installListeners(Accessible accessible) {
            installListeners(accessible.getAccessibleContext());
        }

        private void installListeners(AccessibleContext accessibleContext) {
            AccessibleRole accessibleRole;
            Accessible accessibleChild;
            AccessibleContext accessibleContext2;
            AccessibleRole accessibleRole2;
            if (accessibleContext != null && !accessibleContext.getAccessibleStateSet().contains(AccessibleState.TRANSIENT)) {
                accessibleContext.addPropertyChangeListener(this);
                if (accessibleContext.getAccessibleStateSet().contains(_AccessibleState.MANAGES_DESCENDANTS) || (accessibleRole = accessibleContext.getAccessibleRole()) == AccessibleRole.LIST || accessibleRole == AccessibleRole.TREE) {
                    return;
                }
                if (accessibleRole == AccessibleRole.TABLE && (accessibleChild = accessibleContext.getAccessibleChild(0)) != null && (accessibleContext2 = accessibleChild.getAccessibleContext()) != null && (accessibleRole2 = accessibleContext2.getAccessibleRole()) != null && accessibleRole2 != AccessibleRole.TABLE) {
                    return;
                }
                int accessibleChildrenCount = accessibleContext.getAccessibleChildrenCount();
                for (int i2 = 0; i2 < accessibleChildrenCount; i2++) {
                    Accessible accessibleChild2 = accessibleContext.getAccessibleChild(i2);
                    if (accessibleChild2 != null) {
                        installListeners(accessibleChild2);
                    }
                }
            }
        }

        protected void removeListeners() {
            Window[] topLevelWindows = EventQueueMonitor.getTopLevelWindows();
            if (topLevelWindows != null) {
                for (int i2 = 0; i2 < topLevelWindows.length; i2++) {
                    if (topLevelWindows[i2] instanceof Accessible) {
                        removeListeners(topLevelWindows[i2]);
                    }
                }
            }
        }

        protected void removeListeners(Accessible accessible) {
            removeListeners(accessible.getAccessibleContext());
        }

        private void removeListeners(AccessibleContext accessibleContext) {
            AccessibleRole accessibleRole;
            if (accessibleContext != null) {
                AccessibleStateSet accessibleStateSet = accessibleContext.getAccessibleStateSet();
                if (!accessibleStateSet.contains(AccessibleState.TRANSIENT)) {
                    accessibleContext.removePropertyChangeListener(this);
                    if (accessibleStateSet.contains(_AccessibleState.MANAGES_DESCENDANTS) || (accessibleRole = accessibleContext.getAccessibleRole()) == AccessibleRole.LIST || accessibleRole == AccessibleRole.TABLE || accessibleRole == AccessibleRole.TREE) {
                        return;
                    }
                    int accessibleChildrenCount = accessibleContext.getAccessibleChildrenCount();
                    for (int i2 = 0; i2 < accessibleChildrenCount; i2++) {
                        Accessible accessibleChild = accessibleContext.getAccessibleChild(i2);
                        if (accessibleChild != null) {
                            removeListeners(accessibleChild);
                        }
                    }
                }
            }
        }

        @Override // com.sun.java.accessibility.util.TopLevelWindowListener
        public void topLevelWindowCreated(Window window) {
            if (window instanceof Accessible) {
                installListeners(window);
            }
        }

        @Override // com.sun.java.accessibility.util.TopLevelWindowListener
        public void topLevelWindowDestroyed(Window window) {
            if (window instanceof Accessible) {
                removeListeners(window);
            }
        }

        @Override // java.beans.PropertyChangeListener
        public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
            Object[] listenerList = AccessibilityEventMonitor.listenerList.getListenerList();
            for (int length = listenerList.length - 2; length >= 0; length -= 2) {
                if (listenerList[length] == PropertyChangeListener.class) {
                    ((PropertyChangeListener) listenerList[length + 1]).propertyChange(propertyChangeEvent);
                }
            }
            if (propertyChangeEvent.getPropertyName().compareTo(AccessibleContext.ACCESSIBLE_CHILD_PROPERTY) == 0) {
                Object oldValue = propertyChangeEvent.getOldValue();
                Object newValue = propertyChangeEvent.getNewValue();
                if ((oldValue == null) ^ (newValue == null)) {
                    if (oldValue != null) {
                        if (oldValue instanceof Accessible) {
                            removeListeners(((Accessible) oldValue).getAccessibleContext());
                            return;
                        } else {
                            if (oldValue instanceof AccessibleContext) {
                                removeListeners((AccessibleContext) oldValue);
                                return;
                            }
                            return;
                        }
                    }
                    if (newValue != null) {
                        if (newValue instanceof Accessible) {
                            installListeners(((Accessible) newValue).getAccessibleContext());
                            return;
                        } else {
                            if (newValue instanceof AccessibleContext) {
                                installListeners((AccessibleContext) newValue);
                                return;
                            }
                            return;
                        }
                    }
                    return;
                }
                System.out.println("ERROR in usage of PropertyChangeEvents for: " + propertyChangeEvent.toString());
            }
        }
    }
}
