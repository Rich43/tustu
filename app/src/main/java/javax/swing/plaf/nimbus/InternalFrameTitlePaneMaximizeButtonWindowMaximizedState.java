package javax.swing.plaf.nimbus;

import java.awt.Container;
import javax.swing.JComponent;
import javax.swing.JInternalFrame;

/* loaded from: rt.jar:javax/swing/plaf/nimbus/InternalFrameTitlePaneMaximizeButtonWindowMaximizedState.class */
class InternalFrameTitlePaneMaximizeButtonWindowMaximizedState extends State {
    InternalFrameTitlePaneMaximizeButtonWindowMaximizedState() {
        super("WindowMaximized");
    }

    @Override // javax.swing.plaf.nimbus.State
    protected boolean isInState(JComponent jComponent) {
        Container container;
        Container parent = jComponent;
        while (true) {
            container = parent;
            if (container.getParent() == null || (container instanceof JInternalFrame)) {
                break;
            }
            parent = container.getParent();
        }
        if (container instanceof JInternalFrame) {
            return ((JInternalFrame) container).isMaximum();
        }
        return false;
    }
}
