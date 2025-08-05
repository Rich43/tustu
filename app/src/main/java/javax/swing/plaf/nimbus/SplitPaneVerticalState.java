package javax.swing.plaf.nimbus;

import javax.swing.JComponent;
import javax.swing.JSplitPane;

/* loaded from: rt.jar:javax/swing/plaf/nimbus/SplitPaneVerticalState.class */
class SplitPaneVerticalState extends State {
    SplitPaneVerticalState() {
        super("Vertical");
    }

    @Override // javax.swing.plaf.nimbus.State
    protected boolean isInState(JComponent jComponent) {
        return (jComponent instanceof JSplitPane) && ((JSplitPane) jComponent).getOrientation() == 1;
    }
}
