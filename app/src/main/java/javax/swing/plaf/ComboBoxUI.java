package javax.swing.plaf;

import javax.swing.JComboBox;

/* loaded from: rt.jar:javax/swing/plaf/ComboBoxUI.class */
public abstract class ComboBoxUI extends ComponentUI {
    public abstract void setPopupVisible(JComboBox jComboBox, boolean z2);

    public abstract boolean isPopupVisible(JComboBox jComboBox);

    public abstract boolean isFocusTraversable(JComboBox jComboBox);
}
