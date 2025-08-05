package javax.swing.plaf.nimbus;

import javax.swing.JComboBox;
import javax.swing.JComponent;

/* loaded from: rt.jar:javax/swing/plaf/nimbus/ComboBoxEditableState.class */
class ComboBoxEditableState extends State {
    ComboBoxEditableState() {
        super("Editable");
    }

    @Override // javax.swing.plaf.nimbus.State
    protected boolean isInState(JComponent jComponent) {
        return (jComponent instanceof JComboBox) && ((JComboBox) jComponent).isEditable();
    }
}
