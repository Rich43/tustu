package javax.swing.plaf.metal;

import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JComponent;
import javax.swing.JSeparator;
import javax.swing.LookAndFeel;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicSeparatorUI;

/* loaded from: rt.jar:javax/swing/plaf/metal/MetalSeparatorUI.class */
public class MetalSeparatorUI extends BasicSeparatorUI {
    public static ComponentUI createUI(JComponent jComponent) {
        return new MetalSeparatorUI();
    }

    @Override // javax.swing.plaf.basic.BasicSeparatorUI
    protected void installDefaults(JSeparator jSeparator) {
        LookAndFeel.installColors(jSeparator, "Separator.background", "Separator.foreground");
    }

    @Override // javax.swing.plaf.basic.BasicSeparatorUI, javax.swing.plaf.ComponentUI
    public void paint(Graphics graphics, JComponent jComponent) {
        Dimension size = jComponent.getSize();
        if (((JSeparator) jComponent).getOrientation() == 1) {
            graphics.setColor(jComponent.getForeground());
            graphics.drawLine(0, 0, 0, size.height);
            graphics.setColor(jComponent.getBackground());
            graphics.drawLine(1, 0, 1, size.height);
            return;
        }
        graphics.setColor(jComponent.getForeground());
        graphics.drawLine(0, 0, size.width, 0);
        graphics.setColor(jComponent.getBackground());
        graphics.drawLine(0, 1, size.width, 1);
    }

    @Override // javax.swing.plaf.basic.BasicSeparatorUI, javax.swing.plaf.ComponentUI
    public Dimension getPreferredSize(JComponent jComponent) {
        if (((JSeparator) jComponent).getOrientation() == 1) {
            return new Dimension(2, 0);
        }
        return new Dimension(0, 2);
    }
}
