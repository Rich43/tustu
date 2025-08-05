package java.awt;

import java.io.Serializable;
import java.util.ArrayList;
import sun.util.logging.PlatformLogger;

/* loaded from: rt.jar:java/awt/ContainerOrderFocusTraversalPolicy.class */
public class ContainerOrderFocusTraversalPolicy extends FocusTraversalPolicy implements Serializable {
    private static final PlatformLogger log = PlatformLogger.getLogger("java.awt.ContainerOrderFocusTraversalPolicy");
    private static final long serialVersionUID = 486933713763926351L;
    private transient Container cachedRoot;
    private transient java.util.List<Component> cachedCycle;
    private final int FORWARD_TRAVERSAL = 0;
    private final int BACKWARD_TRAVERSAL = 1;
    private boolean implicitDownCycleTraversal = true;

    private java.util.List<Component> getFocusTraversalCycle(Container container) {
        ArrayList arrayList = new ArrayList();
        enumerateCycle(container, arrayList);
        return arrayList;
    }

    private int getComponentIndex(java.util.List<Component> list, Component component) {
        return list.indexOf(component);
    }

    /* JADX WARN: Removed duplicated region for block: B:17:0x0055  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private void enumerateCycle(java.awt.Container r5, java.util.List<java.awt.Component> r6) {
        /*
            r4 = this;
            r0 = r5
            boolean r0 = r0.isVisible()
            if (r0 == 0) goto Le
            r0 = r5
            boolean r0 = r0.isDisplayable()
            if (r0 != 0) goto Lf
        Le:
            return
        Lf:
            r0 = r6
            r1 = r5
            boolean r0 = r0.add(r1)
            r0 = r5
            java.awt.Component[] r0 = r0.getComponents()
            r7 = r0
            r0 = 0
            r8 = r0
        L1f:
            r0 = r8
            r1 = r7
            int r1 = r1.length
            if (r0 >= r1) goto L64
            r0 = r7
            r1 = r8
            r0 = r0[r1]
            r9 = r0
            r0 = r9
            boolean r0 = r0 instanceof java.awt.Container
            if (r0 == 0) goto L55
            r0 = r9
            java.awt.Container r0 = (java.awt.Container) r0
            r10 = r0
            r0 = r10
            boolean r0 = r0.isFocusCycleRoot()
            if (r0 != 0) goto L55
            r0 = r10
            boolean r0 = r0.isFocusTraversalPolicyProvider()
            if (r0 != 0) goto L55
            r0 = r4
            r1 = r10
            r2 = r6
            r0.enumerateCycle(r1, r2)
            goto L5e
        L55:
            r0 = r6
            r1 = r9
            boolean r0 = r0.add(r1)
        L5e:
            int r8 = r8 + 1
            goto L1f
        L64:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: java.awt.ContainerOrderFocusTraversalPolicy.enumerateCycle(java.awt.Container, java.util.List):void");
    }

    private Container getTopmostProvider(Container container, Component component) {
        Container parent = component.getParent();
        Container container2 = null;
        while (parent != container && parent != null) {
            if (parent.isFocusTraversalPolicyProvider()) {
                container2 = parent;
            }
            parent = parent.getParent();
        }
        if (parent == null) {
            return null;
        }
        return container2;
    }

    private Component getComponentDownCycle(Component component, int i2) {
        Component lastComponent;
        Component defaultComponent = null;
        if (component instanceof Container) {
            Container container = (Container) component;
            if (container.isFocusCycleRoot()) {
                if (getImplicitDownCycleTraversal()) {
                    defaultComponent = container.getFocusTraversalPolicy().getDefaultComponent(container);
                    if (defaultComponent != null && log.isLoggable(PlatformLogger.Level.FINE)) {
                        log.fine("### Transfered focus down-cycle to " + ((Object) defaultComponent) + " in the focus cycle root " + ((Object) container));
                    }
                } else {
                    return null;
                }
            } else if (container.isFocusTraversalPolicyProvider()) {
                if (i2 == 0) {
                    lastComponent = container.getFocusTraversalPolicy().getDefaultComponent(container);
                } else {
                    lastComponent = container.getFocusTraversalPolicy().getLastComponent(container);
                }
                defaultComponent = lastComponent;
                if (defaultComponent != null && log.isLoggable(PlatformLogger.Level.FINE)) {
                    log.fine("### Transfered focus to " + ((Object) defaultComponent) + " in the FTP provider " + ((Object) container));
                }
            }
        }
        return defaultComponent;
    }

    @Override // java.awt.FocusTraversalPolicy
    public Component getComponentAfter(Container container, Component component) {
        if (log.isLoggable(PlatformLogger.Level.FINE)) {
            log.fine("### Searching in " + ((Object) container) + " for component after " + ((Object) component));
        }
        if (container == null || component == null) {
            throw new IllegalArgumentException("aContainer and aComponent cannot be null");
        }
        if (!container.isFocusTraversalPolicyProvider() && !container.isFocusCycleRoot()) {
            throw new IllegalArgumentException("aContainer should be focus cycle root or focus traversal policy provider");
        }
        if (container.isFocusCycleRoot() && !component.isFocusCycleRoot(container)) {
            throw new IllegalArgumentException("aContainer is not a focus cycle root of aComponent");
        }
        synchronized (container.getTreeLock()) {
            if (!container.isVisible() || !container.isDisplayable()) {
                return null;
            }
            Component componentDownCycle = getComponentDownCycle(component, 0);
            if (componentDownCycle != null) {
                return componentDownCycle;
            }
            Container topmostProvider = getTopmostProvider(container, component);
            if (topmostProvider != null) {
                if (log.isLoggable(PlatformLogger.Level.FINE)) {
                    log.fine("### Asking FTP " + ((Object) topmostProvider) + " for component after " + ((Object) component));
                }
                Component componentAfter = topmostProvider.getFocusTraversalPolicy().getComponentAfter(topmostProvider, component);
                if (componentAfter != null) {
                    if (log.isLoggable(PlatformLogger.Level.FINE)) {
                        log.fine("### FTP returned " + ((Object) componentAfter));
                    }
                    return componentAfter;
                }
                component = topmostProvider;
            }
            java.util.List<Component> focusTraversalCycle = getFocusTraversalCycle(container);
            if (log.isLoggable(PlatformLogger.Level.FINE)) {
                log.fine("### Cycle is " + ((Object) focusTraversalCycle) + ", component is " + ((Object) component));
            }
            int componentIndex = getComponentIndex(focusTraversalCycle, component);
            if (componentIndex < 0) {
                if (log.isLoggable(PlatformLogger.Level.FINE)) {
                    log.fine("### Didn't find component " + ((Object) component) + " in a cycle " + ((Object) container));
                }
                return getFirstComponent(container);
            }
            for (int i2 = componentIndex + 1; i2 < focusTraversalCycle.size(); i2++) {
                Component component2 = focusTraversalCycle.get(i2);
                if (accept(component2)) {
                    return component2;
                }
                Component componentDownCycle2 = getComponentDownCycle(component2, 0);
                if (componentDownCycle2 != null) {
                    return componentDownCycle2;
                }
            }
            if (container.isFocusCycleRoot()) {
                this.cachedRoot = container;
                this.cachedCycle = focusTraversalCycle;
                Component firstComponent = getFirstComponent(container);
                this.cachedRoot = null;
                this.cachedCycle = null;
                return firstComponent;
            }
            return null;
        }
    }

    @Override // java.awt.FocusTraversalPolicy
    public Component getComponentBefore(Container container, Component component) {
        Component componentDownCycle;
        if (container == null || component == null) {
            throw new IllegalArgumentException("aContainer and aComponent cannot be null");
        }
        if (!container.isFocusTraversalPolicyProvider() && !container.isFocusCycleRoot()) {
            throw new IllegalArgumentException("aContainer should be focus cycle root or focus traversal policy provider");
        }
        if (container.isFocusCycleRoot() && !component.isFocusCycleRoot(container)) {
            throw new IllegalArgumentException("aContainer is not a focus cycle root of aComponent");
        }
        synchronized (container.getTreeLock()) {
            if (!container.isVisible() || !container.isDisplayable()) {
                return null;
            }
            Container topmostProvider = getTopmostProvider(container, component);
            if (topmostProvider != null) {
                if (log.isLoggable(PlatformLogger.Level.FINE)) {
                    log.fine("### Asking FTP " + ((Object) topmostProvider) + " for component after " + ((Object) component));
                }
                Component componentBefore = topmostProvider.getFocusTraversalPolicy().getComponentBefore(topmostProvider, component);
                if (componentBefore != null) {
                    if (log.isLoggable(PlatformLogger.Level.FINE)) {
                        log.fine("### FTP returned " + ((Object) componentBefore));
                    }
                    return componentBefore;
                }
                component = topmostProvider;
                if (accept(component)) {
                    return component;
                }
            }
            java.util.List<Component> focusTraversalCycle = getFocusTraversalCycle(container);
            if (log.isLoggable(PlatformLogger.Level.FINE)) {
                log.fine("### Cycle is " + ((Object) focusTraversalCycle) + ", component is " + ((Object) component));
            }
            int componentIndex = getComponentIndex(focusTraversalCycle, component);
            if (componentIndex < 0) {
                if (log.isLoggable(PlatformLogger.Level.FINE)) {
                    log.fine("### Didn't find component " + ((Object) component) + " in a cycle " + ((Object) container));
                }
                return getLastComponent(container);
            }
            for (int i2 = componentIndex - 1; i2 >= 0; i2--) {
                Component component2 = focusTraversalCycle.get(i2);
                if (component2 != container && (componentDownCycle = getComponentDownCycle(component2, 1)) != null) {
                    return componentDownCycle;
                }
                if (accept(component2)) {
                    return component2;
                }
            }
            if (container.isFocusCycleRoot()) {
                this.cachedRoot = container;
                this.cachedCycle = focusTraversalCycle;
                Component lastComponent = getLastComponent(container);
                this.cachedRoot = null;
                this.cachedCycle = null;
                return lastComponent;
            }
            return null;
        }
    }

    @Override // java.awt.FocusTraversalPolicy
    public Component getFirstComponent(Container container) {
        java.util.List<Component> focusTraversalCycle;
        Component componentDownCycle;
        if (log.isLoggable(PlatformLogger.Level.FINE)) {
            log.fine("### Getting first component in " + ((Object) container));
        }
        if (container == null) {
            throw new IllegalArgumentException("aContainer cannot be null");
        }
        synchronized (container.getTreeLock()) {
            if (!container.isVisible() || !container.isDisplayable()) {
                return null;
            }
            if (this.cachedRoot == container) {
                focusTraversalCycle = this.cachedCycle;
            } else {
                focusTraversalCycle = getFocusTraversalCycle(container);
            }
            if (focusTraversalCycle.size() == 0) {
                if (log.isLoggable(PlatformLogger.Level.FINE)) {
                    log.fine("### Cycle is empty");
                }
                return null;
            }
            if (log.isLoggable(PlatformLogger.Level.FINE)) {
                log.fine("### Cycle is " + ((Object) focusTraversalCycle));
            }
            for (Component component : focusTraversalCycle) {
                if (accept(component)) {
                    return component;
                }
                if (component != container && (componentDownCycle = getComponentDownCycle(component, 0)) != null) {
                    return componentDownCycle;
                }
            }
            return null;
        }
    }

    @Override // java.awt.FocusTraversalPolicy
    public Component getLastComponent(Container container) {
        java.util.List<Component> focusTraversalCycle;
        Component lastComponent;
        if (log.isLoggable(PlatformLogger.Level.FINE)) {
            log.fine("### Getting last component in " + ((Object) container));
        }
        if (container == null) {
            throw new IllegalArgumentException("aContainer cannot be null");
        }
        synchronized (container.getTreeLock()) {
            if (!container.isVisible() || !container.isDisplayable()) {
                return null;
            }
            if (this.cachedRoot == container) {
                focusTraversalCycle = this.cachedCycle;
            } else {
                focusTraversalCycle = getFocusTraversalCycle(container);
            }
            if (focusTraversalCycle.size() == 0) {
                if (log.isLoggable(PlatformLogger.Level.FINE)) {
                    log.fine("### Cycle is empty");
                }
                return null;
            }
            if (log.isLoggable(PlatformLogger.Level.FINE)) {
                log.fine("### Cycle is " + ((Object) focusTraversalCycle));
            }
            for (int size = focusTraversalCycle.size() - 1; size >= 0; size--) {
                Component component = focusTraversalCycle.get(size);
                if (accept(component)) {
                    return component;
                }
                if ((component instanceof Container) && component != container) {
                    Container container2 = (Container) component;
                    if (container2.isFocusTraversalPolicyProvider() && (lastComponent = container2.getFocusTraversalPolicy().getLastComponent(container2)) != null) {
                        return lastComponent;
                    }
                }
            }
            return null;
        }
    }

    @Override // java.awt.FocusTraversalPolicy
    public Component getDefaultComponent(Container container) {
        return getFirstComponent(container);
    }

    public void setImplicitDownCycleTraversal(boolean z2) {
        this.implicitDownCycleTraversal = z2;
    }

    public boolean getImplicitDownCycleTraversal() {
        return this.implicitDownCycleTraversal;
    }

    protected boolean accept(Component component) {
        if (!component.canBeFocusOwner()) {
            return false;
        }
        if (!(component instanceof Window)) {
            Container parent = component.getParent();
            while (true) {
                Container container = parent;
                if (container != null) {
                    if (!container.isEnabled() && !container.isLightweight()) {
                        return false;
                    }
                    if (!(container instanceof Window)) {
                        parent = container.getParent();
                    } else {
                        return true;
                    }
                } else {
                    return true;
                }
            }
        } else {
            return true;
        }
    }
}
