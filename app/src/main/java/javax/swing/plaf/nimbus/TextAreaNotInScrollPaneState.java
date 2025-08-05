package javax.swing.plaf.nimbus;

import javax.swing.JComponent;
import javax.swing.JViewport;

/* loaded from: rt.jar:javax/swing/plaf/nimbus/TextAreaNotInScrollPaneState.class */
class TextAreaNotInScrollPaneState extends State {
    TextAreaNotInScrollPaneState() {
        super("NotInScrollPane");
    }

    @Override // javax.swing.plaf.nimbus.State
    protected boolean isInState(JComponent jComponent) {
        return !(jComponent.getParent() instanceof JViewport);
    }
}
