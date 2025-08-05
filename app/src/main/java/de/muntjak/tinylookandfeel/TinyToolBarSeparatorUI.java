package de.muntjak.tinylookandfeel;

import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JComponent;
import javax.swing.JSeparator;
import javax.swing.JToolBar;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicToolBarSeparatorUI;

/* loaded from: tinylaf.jar:de/muntjak/tinylookandfeel/TinyToolBarSeparatorUI.class */
public class TinyToolBarSeparatorUI extends BasicToolBarSeparatorUI {
    private static final int YQ_SIZE = 7;

    public static ComponentUI createUI(JComponent jComponent) {
        return new TinyToolBarSeparatorUI();
    }

    @Override // javax.swing.plaf.basic.BasicToolBarSeparatorUI, javax.swing.plaf.basic.BasicSeparatorUI
    protected void installDefaults(JSeparator jSeparator) {
    }

    @Override // javax.swing.plaf.basic.BasicSeparatorUI, javax.swing.plaf.ComponentUI
    public Dimension getMinimumSize(JComponent jComponent) {
        return ((JToolBar.Separator) jComponent).getOrientation() == 0 ? new Dimension(0, 1) : new Dimension(1, 0);
    }

    @Override // javax.swing.plaf.basic.BasicSeparatorUI, javax.swing.plaf.ComponentUI
    public Dimension getMaximumSize(JComponent jComponent) {
        JToolBar.Separator separator = (JToolBar.Separator) jComponent;
        Dimension separatorSize = separator.getSeparatorSize();
        return separator.getOrientation() == 0 ? separatorSize != null ? new Dimension(Short.MAX_VALUE, separatorSize.height) : new Dimension(Short.MAX_VALUE, 7) : separatorSize != null ? new Dimension(Short.MAX_VALUE, separatorSize.width) : new Dimension(7, Short.MAX_VALUE);
    }

    @Override // javax.swing.plaf.basic.BasicToolBarSeparatorUI, javax.swing.plaf.basic.BasicSeparatorUI, javax.swing.plaf.ComponentUI
    public Dimension getPreferredSize(JComponent jComponent) {
        JToolBar.Separator separator = (JToolBar.Separator) jComponent;
        Dimension separatorSize = separator.getSeparatorSize();
        return separatorSize != null ? separatorSize.getSize() : separator.getOrientation() == 0 ? new Dimension(0, 7) : new Dimension(7, 0);
    }

    @Override // javax.swing.plaf.basic.BasicToolBarSeparatorUI, javax.swing.plaf.basic.BasicSeparatorUI, javax.swing.plaf.ComponentUI
    public void paint(Graphics graphics, JComponent jComponent) {
        drawXpToolBarSeparator(graphics, jComponent);
    }

    protected void drawXpToolBarSeparator(Graphics graphics, JComponent jComponent) {
        JToolBar.Separator separator = (JToolBar.Separator) jComponent;
        if (separator.getOrientation() == 0) {
            int height = separator.getHeight() / 2;
            graphics.setColor(Theme.toolSeparatorColor.getColor());
            graphics.drawLine(0, height, separator.getWidth(), height);
        } else {
            int width = separator.getWidth() / 2;
            graphics.setColor(Theme.toolSeparatorColor.getColor());
            graphics.drawLine(width, 0, width, separator.getHeight());
        }
    }
}
