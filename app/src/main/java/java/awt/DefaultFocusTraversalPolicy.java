package java.awt;

import java.awt.peer.ComponentPeer;

/* loaded from: rt.jar:java/awt/DefaultFocusTraversalPolicy.class */
public class DefaultFocusTraversalPolicy extends ContainerOrderFocusTraversalPolicy {
    private static final long serialVersionUID = 8876966522510157497L;

    @Override // java.awt.ContainerOrderFocusTraversalPolicy
    protected boolean accept(Component component) {
        if (!component.isVisible() || !component.isDisplayable() || !component.isEnabled()) {
            return false;
        }
        if (!(component instanceof Window)) {
            Container parent = component.getParent();
            while (true) {
                Container container = parent;
                if (container == null) {
                    break;
                }
                if (!container.isEnabled() && !container.isLightweight()) {
                    return false;
                }
                if (container instanceof Window) {
                    break;
                }
                parent = container.getParent();
            }
        }
        boolean zIsFocusable = component.isFocusable();
        if (component.isFocusTraversableOverridden()) {
            return zIsFocusable;
        }
        ComponentPeer peer = component.getPeer();
        return peer != null && peer.isFocusable();
    }
}
