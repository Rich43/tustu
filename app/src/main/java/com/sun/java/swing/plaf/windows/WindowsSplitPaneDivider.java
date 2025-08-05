package com.sun.java.swing.plaf.windows;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.UIManager;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;

/* loaded from: rt.jar:com/sun/java/swing/plaf/windows/WindowsSplitPaneDivider.class */
public class WindowsSplitPaneDivider extends BasicSplitPaneDivider {
    public WindowsSplitPaneDivider(BasicSplitPaneUI basicSplitPaneUI) {
        super(basicSplitPaneUI);
    }

    @Override // javax.swing.plaf.basic.BasicSplitPaneDivider, java.awt.Container, java.awt.Component
    public void paint(Graphics graphics) {
        Color background;
        if (this.splitPane.hasFocus()) {
            background = UIManager.getColor("SplitPane.shadow");
        } else {
            background = getBackground();
        }
        Color color = background;
        Dimension size = getSize();
        if (color != null) {
            graphics.setColor(color);
            graphics.fillRect(0, 0, size.width, size.height);
        }
        super.paint(graphics);
    }
}
