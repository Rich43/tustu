package javax.swing.plaf.metal;

import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;
import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicProgressBarUI;

/* loaded from: rt.jar:javax/swing/plaf/metal/MetalProgressBarUI.class */
public class MetalProgressBarUI extends BasicProgressBarUI {
    private Rectangle innards;
    private Rectangle box;

    public static ComponentUI createUI(JComponent jComponent) {
        return new MetalProgressBarUI();
    }

    @Override // javax.swing.plaf.basic.BasicProgressBarUI
    public void paintDeterminate(Graphics graphics, JComponent jComponent) {
        super.paintDeterminate(graphics, jComponent);
        if ((graphics instanceof Graphics2D) && this.progressBar.isBorderPainted()) {
            Insets insets = this.progressBar.getInsets();
            int width = this.progressBar.getWidth() - (insets.left + insets.right);
            int height = this.progressBar.getHeight() - (insets.top + insets.bottom);
            int amountFull = getAmountFull(insets, width, height);
            boolean zIsLeftToRight = MetalUtils.isLeftToRight(jComponent);
            int i2 = insets.left;
            int i3 = insets.top;
            int i4 = (insets.left + width) - 1;
            int i5 = (insets.top + height) - 1;
            Graphics2D graphics2D = (Graphics2D) graphics;
            graphics2D.setStroke(new BasicStroke(1.0f));
            if (this.progressBar.getOrientation() == 0) {
                graphics2D.setColor(MetalLookAndFeel.getControlShadow());
                graphics2D.drawLine(i2, i3, i4, i3);
                if (amountFull > 0) {
                    graphics2D.setColor(MetalLookAndFeel.getPrimaryControlDarkShadow());
                    if (zIsLeftToRight) {
                        graphics2D.drawLine(i2, i3, (i2 + amountFull) - 1, i3);
                    } else {
                        graphics2D.drawLine(i4, i3, (i4 - amountFull) + 1, i3);
                        if (this.progressBar.getPercentComplete() != 1.0d) {
                            graphics2D.setColor(MetalLookAndFeel.getControlShadow());
                        }
                    }
                }
                graphics2D.drawLine(i2, i3, i2, i5);
                return;
            }
            graphics2D.setColor(MetalLookAndFeel.getControlShadow());
            graphics2D.drawLine(i2, i3, i2, i5);
            if (amountFull > 0) {
                graphics2D.setColor(MetalLookAndFeel.getPrimaryControlDarkShadow());
                graphics2D.drawLine(i2, i5, i2, (i5 - amountFull) + 1);
            }
            graphics2D.setColor(MetalLookAndFeel.getControlShadow());
            if (this.progressBar.getPercentComplete() == 1.0d) {
                graphics2D.setColor(MetalLookAndFeel.getPrimaryControlDarkShadow());
            }
            graphics2D.drawLine(i2, i3, i4, i3);
        }
    }

    @Override // javax.swing.plaf.basic.BasicProgressBarUI
    public void paintIndeterminate(Graphics graphics, JComponent jComponent) {
        super.paintIndeterminate(graphics, jComponent);
        if (!this.progressBar.isBorderPainted() || !(graphics instanceof Graphics2D)) {
            return;
        }
        Insets insets = this.progressBar.getInsets();
        int width = this.progressBar.getWidth() - (insets.left + insets.right);
        int height = this.progressBar.getHeight() - (insets.top + insets.bottom);
        getAmountFull(insets, width, height);
        MetalUtils.isLeftToRight(jComponent);
        Rectangle box = getBox(null);
        int i2 = insets.left;
        int i3 = insets.top;
        int i4 = (insets.left + width) - 1;
        int i5 = (insets.top + height) - 1;
        Graphics2D graphics2D = (Graphics2D) graphics;
        graphics2D.setStroke(new BasicStroke(1.0f));
        if (this.progressBar.getOrientation() == 0) {
            graphics2D.setColor(MetalLookAndFeel.getControlShadow());
            graphics2D.drawLine(i2, i3, i4, i3);
            graphics2D.drawLine(i2, i3, i2, i5);
            graphics2D.setColor(MetalLookAndFeel.getPrimaryControlDarkShadow());
            graphics2D.drawLine(box.f12372x, i3, (box.f12372x + box.width) - 1, i3);
            return;
        }
        graphics2D.setColor(MetalLookAndFeel.getControlShadow());
        graphics2D.drawLine(i2, i3, i2, i5);
        graphics2D.drawLine(i2, i3, i4, i3);
        graphics2D.setColor(MetalLookAndFeel.getPrimaryControlDarkShadow());
        graphics2D.drawLine(i2, box.f12373y, i2, (box.f12373y + box.height) - 1);
    }
}
