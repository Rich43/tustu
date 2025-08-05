package de.muntjak.tinylookandfeel;

import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JComponent;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicMenuBarUI;

/* loaded from: tinylaf.jar:de/muntjak/tinylookandfeel/TinyMenuBarUI.class */
public class TinyMenuBarUI extends BasicMenuBarUI {
    private static final boolean DEBUG = true;
    private static final String CLOSE_OPENED_MENU_KEY = "closeOpenedMenu";

    public static ComponentUI createUI(JComponent jComponent) {
        return new TinyMenuBarUI();
    }

    @Override // javax.swing.plaf.ComponentUI
    public void paint(Graphics graphics, JComponent jComponent) {
        if (jComponent.isOpaque()) {
            Color background = jComponent.getBackground();
            if (background instanceof ColorUIResource) {
                background = Theme.menuBarColor.getColor();
            }
            graphics.setColor(background);
            graphics.fillRect(0, 0, jComponent.getWidth(), jComponent.getHeight());
        }
    }
}
