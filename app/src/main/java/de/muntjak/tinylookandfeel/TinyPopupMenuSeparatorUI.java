package de.muntjak.tinylookandfeel;

import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.metal.MetalSeparatorUI;

/* loaded from: tinylaf.jar:de/muntjak/tinylookandfeel/TinyPopupMenuSeparatorUI.class */
public class TinyPopupMenuSeparatorUI extends MetalSeparatorUI {
    private static final Dimension preferredSize = new Dimension(0, 3);

    public static ComponentUI createUI(JComponent jComponent) {
        return new TinyPopupMenuSeparatorUI();
    }

    @Override // javax.swing.plaf.metal.MetalSeparatorUI, javax.swing.plaf.basic.BasicSeparatorUI, javax.swing.plaf.ComponentUI
    public void paint(Graphics graphics, JComponent jComponent) {
        drawXpSeparator(graphics, jComponent.getSize());
    }

    private void drawXpSeparator(Graphics graphics, Dimension dimension) {
        graphics.setColor(Theme.menuPopupColor.getColor());
        graphics.fillRect(0, 0, dimension.width, dimension.height);
        graphics.setColor(Theme.menuSeparatorColor.getColor());
        graphics.drawLine(2, 1, dimension.width - 3, 1);
    }

    @Override // javax.swing.plaf.metal.MetalSeparatorUI, javax.swing.plaf.basic.BasicSeparatorUI, javax.swing.plaf.ComponentUI
    public Dimension getPreferredSize(JComponent jComponent) {
        return preferredSize;
    }
}
