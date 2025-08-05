package javax.swing.plaf.synth;

import java.awt.Insets;
import java.awt.Rectangle;
import javax.swing.JComboBox;
import javax.swing.plaf.ComboBoxUI;
import javax.swing.plaf.basic.BasicComboPopup;

/* loaded from: rt.jar:javax/swing/plaf/synth/SynthComboPopup.class */
class SynthComboPopup extends BasicComboPopup {
    public SynthComboPopup(JComboBox jComboBox) {
        super(jComboBox);
    }

    @Override // javax.swing.plaf.basic.BasicComboPopup
    protected void configureList() {
        this.list.setFont(this.comboBox.getFont());
        this.list.setCellRenderer(this.comboBox.getRenderer());
        this.list.setFocusable(false);
        this.list.setSelectionMode(0);
        int selectedIndex = this.comboBox.getSelectedIndex();
        if (selectedIndex == -1) {
            this.list.clearSelection();
        } else {
            this.list.setSelectedIndex(selectedIndex);
            this.list.ensureIndexIsVisible(selectedIndex);
        }
        installListListeners();
    }

    @Override // javax.swing.plaf.basic.BasicComboPopup
    protected Rectangle computePopupBounds(int i2, int i3, int i4, int i5) {
        ComboBoxUI ui = this.comboBox.getUI();
        if (ui instanceof SynthComboBoxUI) {
            SynthComboBoxUI synthComboBoxUI = (SynthComboBoxUI) ui;
            if (synthComboBoxUI.popupInsets != null) {
                Insets insets = synthComboBoxUI.popupInsets;
                return super.computePopupBounds(i2 + insets.left, i3 + insets.top, (i4 - insets.left) - insets.right, (i5 - insets.top) - insets.bottom);
            }
        }
        return super.computePopupBounds(i2, i3, i4, i5);
    }
}
