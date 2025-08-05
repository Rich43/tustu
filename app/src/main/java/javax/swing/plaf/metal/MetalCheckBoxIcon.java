package javax.swing.plaf.metal;

import java.awt.Component;
import java.awt.Graphics;
import java.io.Serializable;
import javax.swing.ButtonModel;
import javax.swing.Icon;
import javax.swing.JCheckBox;
import javax.swing.plaf.UIResource;

/* loaded from: rt.jar:javax/swing/plaf/metal/MetalCheckBoxIcon.class */
public class MetalCheckBoxIcon implements Icon, UIResource, Serializable {
    protected int getControlSize() {
        return 13;
    }

    @Override // javax.swing.Icon
    public void paintIcon(Component component, Graphics graphics, int i2, int i3) {
        JCheckBox jCheckBox = (JCheckBox) component;
        ButtonModel model = jCheckBox.getModel();
        int controlSize = getControlSize();
        boolean zIsSelected = model.isSelected();
        if (model.isEnabled()) {
            if (jCheckBox.isBorderPaintedFlat()) {
                graphics.setColor(MetalLookAndFeel.getControlDarkShadow());
                graphics.drawRect(i2 + 1, i3, controlSize - 1, controlSize - 1);
            }
            if (model.isPressed() && model.isArmed()) {
                if (jCheckBox.isBorderPaintedFlat()) {
                    graphics.setColor(MetalLookAndFeel.getControlShadow());
                    graphics.fillRect(i2 + 2, i3 + 1, controlSize - 2, controlSize - 2);
                } else {
                    graphics.setColor(MetalLookAndFeel.getControlShadow());
                    graphics.fillRect(i2, i3, controlSize - 1, controlSize - 1);
                    MetalUtils.drawPressed3DBorder(graphics, i2, i3, controlSize, controlSize);
                }
            } else if (!jCheckBox.isBorderPaintedFlat()) {
                MetalUtils.drawFlush3DBorder(graphics, i2, i3, controlSize, controlSize);
            }
            graphics.setColor(MetalLookAndFeel.getControlInfo());
        } else {
            graphics.setColor(MetalLookAndFeel.getControlShadow());
            graphics.drawRect(i2, i3, controlSize - 1, controlSize - 1);
        }
        if (zIsSelected) {
            if (jCheckBox.isBorderPaintedFlat()) {
                i2++;
            }
            drawCheck(component, graphics, i2, i3);
        }
    }

    protected void drawCheck(Component component, Graphics graphics, int i2, int i3) {
        int controlSize = getControlSize();
        graphics.fillRect(i2 + 3, i3 + 5, 2, controlSize - 8);
        graphics.drawLine(i2 + (controlSize - 4), i3 + 3, i2 + 5, i3 + (controlSize - 6));
        graphics.drawLine(i2 + (controlSize - 4), i3 + 4, i2 + 5, i3 + (controlSize - 5));
    }

    @Override // javax.swing.Icon
    public int getIconWidth() {
        return getControlSize();
    }

    @Override // javax.swing.Icon
    public int getIconHeight() {
        return getControlSize();
    }
}
