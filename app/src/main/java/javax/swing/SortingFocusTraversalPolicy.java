package javax.swing;

import java.awt.Component;
import java.awt.Container;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.ListIterator;
import sun.security.action.GetPropertyAction;
import sun.util.logging.PlatformLogger;

/* loaded from: rt.jar:javax/swing/SortingFocusTraversalPolicy.class */
public class SortingFocusTraversalPolicy extends InternalFrameFocusTraversalPolicy {
    private Comparator<? super Component> comparator;
    private transient Container cachedRoot;
    private transient List<Component> cachedCycle;
    private static final Method legacyMergeSortMethod;
    private static final SwingContainerOrderFocusTraversalPolicy fitnessTestPolicy = new SwingContainerOrderFocusTraversalPolicy();
    private static final boolean legacySortingFTPEnabled = "true".equals(AccessController.doPrivileged(new GetPropertyAction("swing.legacySortingFTPEnabled", "true")));
    private boolean implicitDownCycleTraversal = true;
    private PlatformLogger log = PlatformLogger.getLogger("javax.swing.SortingFocusTraversalPolicy");
    private final int FORWARD_TRAVERSAL = 0;
    private final int BACKWARD_TRAVERSAL = 1;

    static {
        legacyMergeSortMethod = legacySortingFTPEnabled ? (Method) AccessController.doPrivileged(new PrivilegedAction<Method>() { // from class: javax.swing.SortingFocusTraversalPolicy.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public Method run2() {
                try {
                    Method declaredMethod = Class.forName("java.util.Arrays").getDeclaredMethod("legacyMergeSort", Object[].class, Comparator.class);
                    declaredMethod.setAccessible(true);
                    return declaredMethod;
                } catch (ClassNotFoundException | NoSuchMethodException e2) {
                    return null;
                }
            }
        }) : null;
    }

    protected SortingFocusTraversalPolicy() {
    }

    public SortingFocusTraversalPolicy(Comparator<? super Component> comparator) {
        this.comparator = comparator;
    }

    private List<Component> getFocusTraversalCycle(Container container) {
        ArrayList arrayList = new ArrayList();
        enumerateAndSortCycle(container, arrayList);
        return arrayList;
    }

    private int getComponentIndex(List<Component> list, Component component) {
        try {
            int iBinarySearch = Collections.binarySearch(list, component, this.comparator);
            if (iBinarySearch < 0) {
                iBinarySearch = list.indexOf(component);
            }
            return iBinarySearch;
        } catch (ClassCastException e2) {
            if (this.log.isLoggable(PlatformLogger.Level.FINE)) {
                this.log.fine("### During the binary search for " + ((Object) component) + " the exception occurred: ", e2);
                return -1;
            }
            return -1;
        }
    }

    private void enumerateAndSortCycle(Container container, List<Component> list) {
        if (container.isShowing()) {
            enumerateCycle(container, list);
            if (!legacySortingFTPEnabled || !legacySort(list, this.comparator)) {
                Collections.sort(list, this.comparator);
            }
        }
    }

    private boolean legacySort(List<Component> list, Comparator<? super Component> comparator) {
        if (legacyMergeSortMethod == null) {
            return false;
        }
        Object[] array = list.toArray();
        try {
            legacyMergeSortMethod.invoke(null, array, comparator);
            ListIterator<Component> listIterator = list.listIterator();
            for (Object obj : array) {
                listIterator.next();
                listIterator.set((Component) obj);
            }
            return true;
        } catch (IllegalAccessException | InvocationTargetException e2) {
            return false;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:21:0x0071  */
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
            r0 = r7
            r8 = r0
            r0 = r8
            int r0 = r0.length
            r9 = r0
            r0 = 0
            r10 = r0
        L27:
            r0 = r10
            r1 = r9
            if (r0 >= r1) goto L80
            r0 = r8
            r1 = r10
            r0 = r0[r1]
            r11 = r0
            r0 = r11
            boolean r0 = r0 instanceof java.awt.Container
            if (r0 == 0) goto L71
            r0 = r11
            java.awt.Container r0 = (java.awt.Container) r0
            r12 = r0
            r0 = r12
            boolean r0 = r0.isFocusCycleRoot()
            if (r0 != 0) goto L71
            r0 = r12
            boolean r0 = r0.isFocusTraversalPolicyProvider()
            if (r0 != 0) goto L71
            r0 = r12
            boolean r0 = r0 instanceof javax.swing.JComponent
            if (r0 == 0) goto L67
            r0 = r12
            javax.swing.JComponent r0 = (javax.swing.JComponent) r0
            boolean r0 = r0.isManagingFocus()
            if (r0 != 0) goto L71
        L67:
            r0 = r4
            r1 = r12
            r2 = r6
            r0.enumerateCycle(r1, r2)
            goto L7a
        L71:
            r0 = r6
            r1 = r11
            boolean r0 = r0.add(r1)
        L7a:
            int r10 = r10 + 1
            goto L27
        L80:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: javax.swing.SortingFocusTraversalPolicy.enumerateCycle(java.awt.Container, java.util.List):void");
    }

    Container getTopmostProvider(Container container, Component component) {
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
                    if (defaultComponent != null && this.log.isLoggable(PlatformLogger.Level.FINE)) {
                        this.log.fine("### Transfered focus down-cycle to " + ((Object) defaultComponent) + " in the focus cycle root " + ((Object) container));
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
                if (defaultComponent != null && this.log.isLoggable(PlatformLogger.Level.FINE)) {
                    this.log.fine("### Transfered focus to " + ((Object) defaultComponent) + " in the FTP provider " + ((Object) container));
                }
            }
        }
        return defaultComponent;
    }

    @Override // java.awt.FocusTraversalPolicy
    public Component getComponentAfter(Container container, Component component) {
        Component componentDownCycle;
        if (this.log.isLoggable(PlatformLogger.Level.FINE)) {
            this.log.fine("### Searching in " + ((Object) container) + " for component after " + ((Object) component));
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
        Component componentDownCycle2 = getComponentDownCycle(component, 0);
        if (componentDownCycle2 != null) {
            return componentDownCycle2;
        }
        Container topmostProvider = getTopmostProvider(container, component);
        if (topmostProvider != null) {
            if (this.log.isLoggable(PlatformLogger.Level.FINE)) {
                this.log.fine("### Asking FTP " + ((Object) topmostProvider) + " for component after " + ((Object) component));
            }
            Component componentAfter = topmostProvider.getFocusTraversalPolicy().getComponentAfter(topmostProvider, component);
            if (componentAfter != null) {
                if (this.log.isLoggable(PlatformLogger.Level.FINE)) {
                    this.log.fine("### FTP returned " + ((Object) componentAfter));
                }
                return componentAfter;
            }
            component = topmostProvider;
        }
        List<Component> focusTraversalCycle = getFocusTraversalCycle(container);
        if (this.log.isLoggable(PlatformLogger.Level.FINE)) {
            this.log.fine("### Cycle is " + ((Object) focusTraversalCycle) + ", component is " + ((Object) component));
        }
        int componentIndex = getComponentIndex(focusTraversalCycle, component);
        if (componentIndex < 0) {
            if (this.log.isLoggable(PlatformLogger.Level.FINE)) {
                this.log.fine("### Didn't find component " + ((Object) component) + " in a cycle " + ((Object) container));
            }
            return getFirstComponent(container);
        }
        do {
            componentIndex++;
            if (componentIndex < focusTraversalCycle.size()) {
                Component component2 = focusTraversalCycle.get(componentIndex);
                if (accept(component2)) {
                    return component2;
                }
                componentDownCycle = getComponentDownCycle(component2, 0);
            } else {
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
        } while (componentDownCycle == null);
        return componentDownCycle;
    }

    @Override // java.awt.FocusTraversalPolicy
    public Component getComponentBefore(Container container, Component component) {
        Component component2;
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
        Container topmostProvider = getTopmostProvider(container, component);
        if (topmostProvider != null) {
            if (this.log.isLoggable(PlatformLogger.Level.FINE)) {
                this.log.fine("### Asking FTP " + ((Object) topmostProvider) + " for component after " + ((Object) component));
            }
            Component componentBefore = topmostProvider.getFocusTraversalPolicy().getComponentBefore(topmostProvider, component);
            if (componentBefore != null) {
                if (this.log.isLoggable(PlatformLogger.Level.FINE)) {
                    this.log.fine("### FTP returned " + ((Object) componentBefore));
                }
                return componentBefore;
            }
            component = topmostProvider;
            if (accept(component)) {
                return component;
            }
        }
        List<Component> focusTraversalCycle = getFocusTraversalCycle(container);
        if (this.log.isLoggable(PlatformLogger.Level.FINE)) {
            this.log.fine("### Cycle is " + ((Object) focusTraversalCycle) + ", component is " + ((Object) component));
        }
        int componentIndex = getComponentIndex(focusTraversalCycle, component);
        if (componentIndex < 0) {
            if (this.log.isLoggable(PlatformLogger.Level.FINE)) {
                this.log.fine("### Didn't find component " + ((Object) component) + " in a cycle " + ((Object) container));
            }
            return getLastComponent(container);
        }
        do {
            componentIndex--;
            if (componentIndex >= 0) {
                component2 = focusTraversalCycle.get(componentIndex);
                if (component2 != container && (componentDownCycle = getComponentDownCycle(component2, 1)) != null) {
                    return componentDownCycle;
                }
            } else {
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
        } while (!accept(component2));
        return component2;
    }

    @Override // java.awt.FocusTraversalPolicy
    public Component getFirstComponent(Container container) {
        List<Component> focusTraversalCycle;
        Component componentDownCycle;
        if (this.log.isLoggable(PlatformLogger.Level.FINE)) {
            this.log.fine("### Getting first component in " + ((Object) container));
        }
        if (container == null) {
            throw new IllegalArgumentException("aContainer cannot be null");
        }
        if (this.cachedRoot == container) {
            focusTraversalCycle = this.cachedCycle;
        } else {
            focusTraversalCycle = getFocusTraversalCycle(container);
        }
        if (focusTraversalCycle.size() == 0) {
            if (this.log.isLoggable(PlatformLogger.Level.FINE)) {
                this.log.fine("### Cycle is empty");
                return null;
            }
            return null;
        }
        if (this.log.isLoggable(PlatformLogger.Level.FINE)) {
            this.log.fine("### Cycle is " + ((Object) focusTraversalCycle));
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

    @Override // java.awt.FocusTraversalPolicy
    public Component getLastComponent(Container container) {
        List<Component> focusTraversalCycle;
        Component lastComponent;
        if (this.log.isLoggable(PlatformLogger.Level.FINE)) {
            this.log.fine("### Getting last component in " + ((Object) container));
        }
        if (container == null) {
            throw new IllegalArgumentException("aContainer cannot be null");
        }
        if (this.cachedRoot == container) {
            focusTraversalCycle = this.cachedCycle;
        } else {
            focusTraversalCycle = getFocusTraversalCycle(container);
        }
        if (focusTraversalCycle.size() == 0) {
            if (this.log.isLoggable(PlatformLogger.Level.FINE)) {
                this.log.fine("### Cycle is empty");
                return null;
            }
            return null;
        }
        if (this.log.isLoggable(PlatformLogger.Level.FINE)) {
            this.log.fine("### Cycle is " + ((Object) focusTraversalCycle));
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

    protected void setComparator(Comparator<? super Component> comparator) {
        this.comparator = comparator;
    }

    protected Comparator<? super Component> getComparator() {
        return this.comparator;
    }

    protected boolean accept(Component component) {
        return fitnessTestPolicy.accept(component);
    }
}
