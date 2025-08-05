package javax.swing.plaf.nimbus;

import java.awt.Container;
import javax.swing.JComboBox;
import javax.swing.JComponent;

/* loaded from: rt.jar:javax/swing/plaf/nimbus/ComboBoxArrowButtonEditableState.class */
class ComboBoxArrowButtonEditableState extends State {
    ComboBoxArrowButtonEditableState() {
        super("Editable");
    }

    @Override // javax.swing.plaf.nimbus.State
    protected boolean isInState(JComponent jComponent) {
        Container parent = jComponent.getParent();
        return (parent instanceof JComboBox) && ((JComboBox) parent).isEditable();
    }
}
