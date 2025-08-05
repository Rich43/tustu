package javax.swing;

import java.awt.ItemSelectable;
import java.awt.event.ActionListener;
import java.awt.event.ItemListener;
import javax.swing.event.ChangeListener;

/* loaded from: rt.jar:javax/swing/ButtonModel.class */
public interface ButtonModel extends ItemSelectable {
    boolean isArmed();

    boolean isSelected();

    boolean isEnabled();

    boolean isPressed();

    boolean isRollover();

    void setArmed(boolean z2);

    void setSelected(boolean z2);

    void setEnabled(boolean z2);

    void setPressed(boolean z2);

    void setRollover(boolean z2);

    void setMnemonic(int i2);

    int getMnemonic();

    void setActionCommand(String str);

    String getActionCommand();

    void setGroup(ButtonGroup buttonGroup);

    void addActionListener(ActionListener actionListener);

    void removeActionListener(ActionListener actionListener);

    @Override // java.awt.ItemSelectable
    void addItemListener(ItemListener itemListener);

    @Override // java.awt.ItemSelectable
    void removeItemListener(ItemListener itemListener);

    void addChangeListener(ChangeListener changeListener);

    void removeChangeListener(ChangeListener changeListener);
}
