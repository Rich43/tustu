package de.muntjak.tinylookandfeel;

import java.awt.Graphics;
import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicPasswordFieldUI;
import javax.swing.text.JTextComponent;

/* loaded from: tinylaf.jar:de/muntjak/tinylookandfeel/TinyPasswordFieldUI.class */
public class TinyPasswordFieldUI extends BasicPasswordFieldUI {
    public static ComponentUI createUI(JComponent jComponent) {
        return new TinyPasswordFieldUI();
    }

    @Override // javax.swing.plaf.basic.BasicTextUI
    protected void paintBackground(Graphics graphics) {
        JTextComponent component = getComponent();
        if (!component.isEnabled()) {
            graphics.setColor(Theme.textDisabledBgColor.getColor());
        } else if (component.isEditable()) {
            graphics.setColor(component.getBackground());
        } else {
            graphics.setColor(Theme.textNonEditableBgColor.getColor());
        }
        graphics.fillRect(0, 0, component.getWidth(), component.getHeight());
    }
}
