package javax.swing.plaf.nimbus;

import javax.swing.JComponent;
import javax.swing.JProgressBar;

/* loaded from: rt.jar:javax/swing/plaf/nimbus/ProgressBarFinishedState.class */
class ProgressBarFinishedState extends State {
    ProgressBarFinishedState() {
        super("Finished");
    }

    @Override // javax.swing.plaf.nimbus.State
    protected boolean isInState(JComponent jComponent) {
        return (jComponent instanceof JProgressBar) && ((JProgressBar) jComponent).getPercentComplete() == 1.0d;
    }
}
