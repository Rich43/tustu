package de.muntjak.tinylookandfeel;

import javax.swing.JButton;
import javax.swing.plaf.ButtonUI;

/* loaded from: tinylaf.jar:de/muntjak/tinylookandfeel/SpecialUIButton.class */
public class SpecialUIButton extends JButton {
    public SpecialUIButton(ButtonUI buttonUI) {
        this.ui = buttonUI;
        buttonUI.installUI(this);
    }

    @Override // javax.swing.AbstractButton
    public void setUI(ButtonUI buttonUI) {
    }
}
