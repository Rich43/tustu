package javax.swing.plaf.nimbus;

import javax.swing.JComponent;
import javax.swing.JInternalFrame;

/* loaded from: rt.jar:javax/swing/plaf/nimbus/InternalFrameTitlePaneWindowFocusedState.class */
class InternalFrameTitlePaneWindowFocusedState extends State {
    InternalFrameTitlePaneWindowFocusedState() {
        super("WindowFocused");
    }

    @Override // javax.swing.plaf.nimbus.State
    protected boolean isInState(JComponent jComponent) {
        return (jComponent instanceof JInternalFrame) && ((JInternalFrame) jComponent).isSelected();
    }
}
