package de.muntjak.tinylookandfeel;

import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JComponent;
import javax.swing.JSeparator;
import javax.swing.LookAndFeel;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicSeparatorUI;

/* loaded from: tinylaf.jar:de/muntjak/tinylookandfeel/TinySeparatorUI.class */
public class TinySeparatorUI extends BasicSeparatorUI {
    protected static final Dimension vertDimension = new Dimension(0, 2);
    protected static final Dimension horzDimension = new Dimension(2, 0);

    public static ComponentUI createUI(JComponent jComponent) {
        return new TinySeparatorUI();
    }

    @Override // javax.swing.plaf.basic.BasicSeparatorUI
    protected void installDefaults(JSeparator jSeparator) {
        LookAndFeel.installColors(jSeparator, "Separator.background", "Separator.foreground");
    }

    @Override // javax.swing.plaf.basic.BasicSeparatorUI, javax.swing.plaf.ComponentUI
    public void paint(Graphics graphics, JComponent jComponent) {
        drawXpSeparator(graphics, jComponent);
    }

    protected void drawXpSeparator(Graphics graphics, JComponent jComponent) {
        Dimension size = jComponent.getSize();
        graphics.setColor(jComponent.getBackground());
        if (((JSeparator) jComponent).getOrientation() == 1) {
            graphics.drawLine(0, 0, 0, size.height);
        } else {
            graphics.drawLine(0, 0, size.width, 0);
        }
    }

    @Override // javax.swing.plaf.basic.BasicSeparatorUI, javax.swing.plaf.ComponentUI
    public Dimension getPreferredSize(JComponent jComponent) {
        return ((JSeparator) jComponent).getOrientation() == 1 ? horzDimension : vertDimension;
    }

    @Override // javax.swing.plaf.basic.BasicSeparatorUI, javax.swing.plaf.ComponentUI
    public Dimension getMinimumSize(JComponent jComponent) {
        return getPreferredSize(jComponent);
    }
}
