package com.sun.java.swing.plaf.motif;

import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;

/* loaded from: rt.jar:com/sun/java/swing/plaf/motif/MotifPopupMenuSeparatorUI.class */
public class MotifPopupMenuSeparatorUI extends MotifSeparatorUI {
    public static ComponentUI createUI(JComponent jComponent) {
        return new MotifPopupMenuSeparatorUI();
    }

    @Override // javax.swing.plaf.basic.BasicSeparatorUI, javax.swing.plaf.ComponentUI
    public void paint(Graphics graphics, JComponent jComponent) {
        Dimension size = jComponent.getSize();
        graphics.setColor(jComponent.getForeground());
        graphics.drawLine(0, 0, size.width, 0);
        graphics.setColor(jComponent.getBackground());
        graphics.drawLine(0, 1, size.width, 1);
    }

    @Override // javax.swing.plaf.basic.BasicSeparatorUI, javax.swing.plaf.ComponentUI
    public Dimension getPreferredSize(JComponent jComponent) {
        return new Dimension(0, 2);
    }
}
