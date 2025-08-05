package javax.swing;

import java.awt.Component;
import java.awt.FocusTraversalPolicy;

/* loaded from: rt.jar:javax/swing/InternalFrameFocusTraversalPolicy.class */
public abstract class InternalFrameFocusTraversalPolicy extends FocusTraversalPolicy {
    public Component getInitialComponent(JInternalFrame jInternalFrame) {
        return getDefaultComponent(jInternalFrame);
    }
}
