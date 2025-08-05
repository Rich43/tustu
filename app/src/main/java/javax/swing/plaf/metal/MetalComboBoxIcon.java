package javax.swing.plaf.metal;

import java.awt.Component;
import java.awt.Graphics;
import java.io.Serializable;
import javax.swing.Icon;
import javax.swing.JComponent;

/* loaded from: rt.jar:javax/swing/plaf/metal/MetalComboBoxIcon.class */
public class MetalComboBoxIcon implements Icon, Serializable {
    @Override // javax.swing.Icon
    public void paintIcon(Component component, Graphics graphics, int i2, int i3) {
        int iconWidth = getIconWidth();
        graphics.translate(i2, i3);
        graphics.setColor(((JComponent) component).isEnabled() ? MetalLookAndFeel.getControlInfo() : MetalLookAndFeel.getControlShadow());
        graphics.drawLine(0, 0, iconWidth - 1, 0);
        graphics.drawLine(1, 1, 1 + (iconWidth - 3), 1);
        graphics.drawLine(2, 2, 2 + (iconWidth - 5), 2);
        graphics.drawLine(3, 3, 3 + (iconWidth - 7), 3);
        graphics.drawLine(4, 4, 4 + (iconWidth - 9), 4);
        graphics.translate(-i2, -i3);
    }

    @Override // javax.swing.Icon
    public int getIconWidth() {
        return 10;
    }

    @Override // javax.swing.Icon
    public int getIconHeight() {
        return 5;
    }
}
