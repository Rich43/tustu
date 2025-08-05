package javax.swing.plaf.metal;

import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;

/* loaded from: rt.jar:javax/swing/plaf/metal/MetalPopupMenuSeparatorUI.class */
public class MetalPopupMenuSeparatorUI extends MetalSeparatorUI {
    public static ComponentUI createUI(JComponent jComponent) {
        return new MetalPopupMenuSeparatorUI();
    }

    @Override // javax.swing.plaf.metal.MetalSeparatorUI, javax.swing.plaf.basic.BasicSeparatorUI, javax.swing.plaf.ComponentUI
    public void paint(Graphics graphics, JComponent jComponent) {
        Dimension size = jComponent.getSize();
        graphics.setColor(jComponent.getForeground());
        graphics.drawLine(0, 1, size.width, 1);
        graphics.setColor(jComponent.getBackground());
        graphics.drawLine(0, 2, size.width, 2);
        graphics.drawLine(0, 0, 0, 0);
        graphics.drawLine(0, 3, 0, 3);
    }

    @Override // javax.swing.plaf.metal.MetalSeparatorUI, javax.swing.plaf.basic.BasicSeparatorUI, javax.swing.plaf.ComponentUI
    public Dimension getPreferredSize(JComponent jComponent) {
        return new Dimension(0, 4);
    }
}
