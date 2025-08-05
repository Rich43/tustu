package javax.swing.plaf.nimbus;

import javax.swing.JComponent;
import javax.swing.JToolBar;

/* loaded from: rt.jar:javax/swing/plaf/nimbus/ToolBarEastState.class */
class ToolBarEastState extends State {
    ToolBarEastState() {
        super("East");
    }

    @Override // javax.swing.plaf.nimbus.State
    protected boolean isInState(JComponent jComponent) {
        return (jComponent instanceof JToolBar) && NimbusLookAndFeel.resolveToolbarConstraint((JToolBar) jComponent) == "East";
    }
}
