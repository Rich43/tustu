package javax.swing.plaf.nimbus;

import javax.swing.JComponent;
import javax.swing.JProgressBar;

/* loaded from: rt.jar:javax/swing/plaf/nimbus/ProgressBarIndeterminateState.class */
class ProgressBarIndeterminateState extends State {
    ProgressBarIndeterminateState() {
        super("Indeterminate");
    }

    @Override // javax.swing.plaf.nimbus.State
    protected boolean isInState(JComponent jComponent) {
        return (jComponent instanceof JProgressBar) && ((JProgressBar) jComponent).isIndeterminate();
    }
}
