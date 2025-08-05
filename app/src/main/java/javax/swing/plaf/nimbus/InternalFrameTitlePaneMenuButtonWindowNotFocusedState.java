package javax.swing.plaf.nimbus;

import java.awt.Container;
import javax.swing.JComponent;
import javax.swing.JInternalFrame;

/* loaded from: rt.jar:javax/swing/plaf/nimbus/InternalFrameTitlePaneMenuButtonWindowNotFocusedState.class */
class InternalFrameTitlePaneMenuButtonWindowNotFocusedState extends State {
    InternalFrameTitlePaneMenuButtonWindowNotFocusedState() {
        super("WindowNotFocused");
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
        return (container instanceof JInternalFrame) && !((JInternalFrame) container).isSelected();
    }
}
