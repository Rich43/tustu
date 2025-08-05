package javax.swing.plaf.nimbus;

import javax.swing.JComponent;
import javax.swing.JSplitPane;

/* loaded from: rt.jar:javax/swing/plaf/nimbus/SplitPaneDividerVerticalState.class */
class SplitPaneDividerVerticalState extends State {
    SplitPaneDividerVerticalState() {
        super("Vertical");
    }

    @Override // javax.swing.plaf.nimbus.State
    protected boolean isInState(JComponent jComponent) {
        return (jComponent instanceof JSplitPane) && ((JSplitPane) jComponent).getOrientation() == 1;
    }
}
