package javax.swing.plaf.nimbus;

import javax.swing.JComponent;
import javax.swing.JToolBar;

/* loaded from: rt.jar:javax/swing/plaf/nimbus/ToolBarNorthState.class */
class ToolBarNorthState extends State {
    ToolBarNorthState() {
        super("North");
    }

    @Override // javax.swing.plaf.nimbus.State
    protected boolean isInState(JComponent jComponent) {
        return (jComponent instanceof JToolBar) && NimbusLookAndFeel.resolveToolbarConstraint((JToolBar) jComponent) == "North";
    }
}
