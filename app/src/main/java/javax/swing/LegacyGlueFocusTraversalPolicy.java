package javax.swing;

import java.awt.Component;
import java.awt.Container;
import java.awt.FocusTraversalPolicy;
import java.awt.Window;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;

/* loaded from: rt.jar:javax/swing/LegacyGlueFocusTraversalPolicy.class */
final class LegacyGlueFocusTraversalPolicy extends FocusTraversalPolicy implements Serializable {
    private transient FocusTraversalPolicy delegatePolicy;
    private transient DefaultFocusManager delegateManager;
    private HashMap<Component, Component> forwardMap = new HashMap<>();
    private HashMap<Component, Component> backwardMap = new HashMap<>();

    LegacyGlueFocusTraversalPolicy(FocusTraversalPolicy focusTraversalPolicy) {
        this.delegatePolicy = focusTraversalPolicy;
    }

    LegacyGlueFocusTraversalPolicy(DefaultFocusManager defaultFocusManager) {
        this.delegateManager = defaultFocusManager;
    }

    void setNextFocusableComponent(Component component, Component component2) {
        this.forwardMap.put(component, component2);
        this.backwardMap.put(component2, component);
    }

    void unsetNextFocusableComponent(Component component, Component component2) {
        this.forwardMap.remove(component);
        this.backwardMap.remove(component2);
    }

    @Override // java.awt.FocusTraversalPolicy
    public Component getComponentAfter(Container container, Component component) {
        Component component2 = component;
        HashSet hashSet = new HashSet();
        do {
            Component component3 = component2;
            component2 = this.forwardMap.get(component2);
            if (component2 == null) {
                if (this.delegatePolicy != null && component3.isFocusCycleRoot(container)) {
                    return this.delegatePolicy.getComponentAfter(container, component3);
                }
                if (this.delegateManager != null) {
                    return this.delegateManager.getComponentAfter(container, component);
                }
                return null;
            }
            if (hashSet.contains(component2)) {
                return null;
            }
            hashSet.add(component2);
        } while (!accept(component2));
        return component2;
    }

    @Override // java.awt.FocusTraversalPolicy
    public Component getComponentBefore(Container container, Component component) {
        Component component2 = component;
        HashSet hashSet = new HashSet();
        do {
            Component component3 = component2;
            component2 = this.backwardMap.get(component2);
            if (component2 == null) {
                if (this.delegatePolicy != null && component3.isFocusCycleRoot(container)) {
                    return this.delegatePolicy.getComponentBefore(container, component3);
                }
                if (this.delegateManager != null) {
                    return this.delegateManager.getComponentBefore(container, component);
                }
                return null;
            }
            if (hashSet.contains(component2)) {
                return null;
            }
            hashSet.add(component2);
        } while (!accept(component2));
        return component2;
    }

    @Override // java.awt.FocusTraversalPolicy
    public Component getFirstComponent(Container container) {
        if (this.delegatePolicy != null) {
            return this.delegatePolicy.getFirstComponent(container);
        }
        if (this.delegateManager != null) {
            return this.delegateManager.getFirstComponent(container);
        }
        return null;
    }

    @Override // java.awt.FocusTraversalPolicy
    public Component getLastComponent(Container container) {
        if (this.delegatePolicy != null) {
            return this.delegatePolicy.getLastComponent(container);
        }
        if (this.delegateManager != null) {
            return this.delegateManager.getLastComponent(container);
        }
        return null;
    }

    @Override // java.awt.FocusTraversalPolicy
    public Component getDefaultComponent(Container container) {
        if (this.delegatePolicy != null) {
            return this.delegatePolicy.getDefaultComponent(container);
        }
        return getFirstComponent(container);
    }

    private boolean accept(Component component) {
        if (!component.isVisible() || !component.isDisplayable() || !component.isFocusable() || !component.isEnabled()) {
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

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        objectOutputStream.defaultWriteObject();
        if (this.delegatePolicy instanceof Serializable) {
            objectOutputStream.writeObject(this.delegatePolicy);
        } else {
            objectOutputStream.writeObject(null);
        }
        if (this.delegateManager instanceof Serializable) {
            objectOutputStream.writeObject(this.delegateManager);
        } else {
            objectOutputStream.writeObject(null);
        }
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        this.delegatePolicy = (FocusTraversalPolicy) objectInputStream.readObject();
        this.delegateManager = (DefaultFocusManager) objectInputStream.readObject();
    }
}
