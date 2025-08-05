package javax.swing.plaf.basic;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JComponent;
import javax.swing.JSeparator;
import javax.swing.LookAndFeel;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.SeparatorUI;

/* loaded from: rt.jar:javax/swing/plaf/basic/BasicSeparatorUI.class */
public class BasicSeparatorUI extends SeparatorUI {
    protected Color shadow;
    protected Color highlight;

    public static ComponentUI createUI(JComponent jComponent) {
        return new BasicSeparatorUI();
    }

    @Override // javax.swing.plaf.ComponentUI
    public void installUI(JComponent jComponent) {
        installDefaults((JSeparator) jComponent);
        installListeners((JSeparator) jComponent);
    }

    @Override // javax.swing.plaf.ComponentUI
    public void uninstallUI(JComponent jComponent) {
        uninstallDefaults((JSeparator) jComponent);
        uninstallListeners((JSeparator) jComponent);
    }

    protected void installDefaults(JSeparator jSeparator) {
        LookAndFeel.installColors(jSeparator, "Separator.background", "Separator.foreground");
        LookAndFeel.installProperty(jSeparator, "opaque", Boolean.FALSE);
    }

    protected void uninstallDefaults(JSeparator jSeparator) {
    }

    protected void installListeners(JSeparator jSeparator) {
    }

    protected void uninstallListeners(JSeparator jSeparator) {
    }

    @Override // javax.swing.plaf.ComponentUI
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

    @Override // javax.swing.plaf.ComponentUI
    public Dimension getPreferredSize(JComponent jComponent) {
        if (((JSeparator) jComponent).getOrientation() == 1) {
            return new Dimension(2, 0);
        }
        return new Dimension(0, 2);
    }

    @Override // javax.swing.plaf.ComponentUI
    public Dimension getMinimumSize(JComponent jComponent) {
        return null;
    }

    @Override // javax.swing.plaf.ComponentUI
    public Dimension getMaximumSize(JComponent jComponent) {
        return null;
    }
}
