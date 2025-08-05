package de.muntjak.tinylookandfeel;

import java.awt.Graphics;
import javax.swing.JComponent;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.metal.MetalTextFieldUI;
import javax.swing.text.JTextComponent;

/* loaded from: tinylaf.jar:de/muntjak/tinylookandfeel/TinyTextFieldUI.class */
public class TinyTextFieldUI extends MetalTextFieldUI {
    public static ComponentUI createUI(JComponent jComponent) {
        return new TinyTextFieldUI();
    }

    @Override // javax.swing.plaf.basic.BasicTextUI
    protected void paintBackground(Graphics graphics) {
        JTextComponent component = getComponent();
        if (component.isEnabled()) {
            if (!component.isEditable() && (component.getBackground() instanceof ColorUIResource)) {
                graphics.setColor(Theme.textNonEditableBgColor.getColor());
            } else {
                graphics.setColor(component.getBackground());
            }
            graphics.fillRect(0, 0, component.getWidth(), component.getHeight());
            return;
        }
        if (component.getBackground() instanceof ColorUIResource) {
            graphics.setColor(Theme.textDisabledBgColor.getColor());
        } else {
            graphics.setColor(component.getBackground());
        }
        graphics.fillRect(0, 0, component.getWidth(), component.getHeight());
        if (component.getBorder() == null) {
            return;
        }
        graphics.setColor(Theme.backColor.getColor());
        graphics.drawRect(1, 1, component.getWidth() - 3, component.getHeight() - 3);
        graphics.drawRect(2, 2, component.getWidth() - 5, component.getHeight() - 5);
    }
}
