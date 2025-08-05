package javax.swing;

import java.awt.Component;
import java.awt.Container;
import java.awt.FocusTraversalPolicy;

/* loaded from: rt.jar:javax/swing/DefaultFocusManager.class */
public class DefaultFocusManager extends FocusManager {
    final FocusTraversalPolicy gluePolicy = new LegacyGlueFocusTraversalPolicy(this);
    private final FocusTraversalPolicy layoutPolicy = new LegacyLayoutFocusTraversalPolicy(this);
    private final LayoutComparator comparator = new LayoutComparator();

    public DefaultFocusManager() {
        setDefaultFocusTraversalPolicy(this.gluePolicy);
    }

    public Component getComponentAfter(Container container, Component component) {
        Container focusCycleRootAncestor = container.isFocusCycleRoot() ? container : container.getFocusCycleRootAncestor();
        if (focusCycleRootAncestor != null) {
            FocusTraversalPolicy focusTraversalPolicy = focusCycleRootAncestor.getFocusTraversalPolicy();
            if (focusTraversalPolicy != this.gluePolicy) {
                return focusTraversalPolicy.getComponentAfter(focusCycleRootAncestor, component);
            }
            this.comparator.setComponentOrientation(focusCycleRootAncestor.getComponentOrientation());
            return this.layoutPolicy.getComponentAfter(focusCycleRootAncestor, component);
        }
        return null;
    }

    public Component getComponentBefore(Container container, Component component) {
        Container focusCycleRootAncestor = container.isFocusCycleRoot() ? container : container.getFocusCycleRootAncestor();
        if (focusCycleRootAncestor != null) {
            FocusTraversalPolicy focusTraversalPolicy = focusCycleRootAncestor.getFocusTraversalPolicy();
            if (focusTraversalPolicy != this.gluePolicy) {
                return focusTraversalPolicy.getComponentBefore(focusCycleRootAncestor, component);
            }
            this.comparator.setComponentOrientation(focusCycleRootAncestor.getComponentOrientation());
            return this.layoutPolicy.getComponentBefore(focusCycleRootAncestor, component);
        }
        return null;
    }

    public Component getFirstComponent(Container container) {
        Container focusCycleRootAncestor = container.isFocusCycleRoot() ? container : container.getFocusCycleRootAncestor();
        if (focusCycleRootAncestor != null) {
            FocusTraversalPolicy focusTraversalPolicy = focusCycleRootAncestor.getFocusTraversalPolicy();
            if (focusTraversalPolicy != this.gluePolicy) {
                return focusTraversalPolicy.getFirstComponent(focusCycleRootAncestor);
            }
            this.comparator.setComponentOrientation(focusCycleRootAncestor.getComponentOrientation());
            return this.layoutPolicy.getFirstComponent(focusCycleRootAncestor);
        }
        return null;
    }

    public Component getLastComponent(Container container) {
        Container focusCycleRootAncestor = container.isFocusCycleRoot() ? container : container.getFocusCycleRootAncestor();
        if (focusCycleRootAncestor != null) {
            FocusTraversalPolicy focusTraversalPolicy = focusCycleRootAncestor.getFocusTraversalPolicy();
            if (focusTraversalPolicy != this.gluePolicy) {
                return focusTraversalPolicy.getLastComponent(focusCycleRootAncestor);
            }
            this.comparator.setComponentOrientation(focusCycleRootAncestor.getComponentOrientation());
            return this.layoutPolicy.getLastComponent(focusCycleRootAncestor);
        }
        return null;
    }

    public boolean compareTabOrder(Component component, Component component2) {
        return this.comparator.compare(component, component2) < 0;
    }
}
