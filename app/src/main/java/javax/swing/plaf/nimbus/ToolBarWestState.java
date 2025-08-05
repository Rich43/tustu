package javax.swing.plaf.nimbus;

import javax.swing.JComponent;
import javax.swing.JToolBar;

/* loaded from: rt.jar:javax/swing/plaf/nimbus/ToolBarWestState.class */
class ToolBarWestState extends State {
    ToolBarWestState() {
        super("West");
    }

    @Override // javax.swing.plaf.nimbus.State
    protected boolean isInState(JComponent jComponent) {
        return (jComponent instanceof JToolBar) && NimbusLookAndFeel.resolveToolbarConstraint((JToolBar) jComponent) == "West";
    }
}
