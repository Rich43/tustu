package javax.swing.plaf.basic;

import java.awt.Component;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.LookAndFeel;
import javax.swing.border.AbstractBorder;
import javax.swing.border.Border;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.PanelUI;

/* loaded from: rt.jar:javax/swing/plaf/basic/BasicPanelUI.class */
public class BasicPanelUI extends PanelUI {
    private static PanelUI panelUI;

    public static ComponentUI createUI(JComponent jComponent) {
        if (panelUI == null) {
            panelUI = new BasicPanelUI();
        }
        return panelUI;
    }

    @Override // javax.swing.plaf.ComponentUI
    public void installUI(JComponent jComponent) {
        JPanel jPanel = (JPanel) jComponent;
        super.installUI(jPanel);
        installDefaults(jPanel);
    }

    @Override // javax.swing.plaf.ComponentUI
    public void uninstallUI(JComponent jComponent) {
        uninstallDefaults((JPanel) jComponent);
        super.uninstallUI(jComponent);
    }

    protected void installDefaults(JPanel jPanel) {
        LookAndFeel.installColorsAndFont(jPanel, "Panel.background", "Panel.foreground", "Panel.font");
        LookAndFeel.installBorder(jPanel, "Panel.border");
        LookAndFeel.installProperty(jPanel, "opaque", Boolean.TRUE);
    }

    protected void uninstallDefaults(JPanel jPanel) {
        LookAndFeel.uninstallBorder(jPanel);
    }

    @Override // javax.swing.plaf.ComponentUI
    public int getBaseline(JComponent jComponent, int i2, int i3) {
        super.getBaseline(jComponent, i2, i3);
        Border border = jComponent.getBorder();
        if (border instanceof AbstractBorder) {
            return ((AbstractBorder) border).getBaseline(jComponent, i2, i3);
        }
        return -1;
    }

    @Override // javax.swing.plaf.ComponentUI
    public Component.BaselineResizeBehavior getBaselineResizeBehavior(JComponent jComponent) {
        super.getBaselineResizeBehavior(jComponent);
        Border border = jComponent.getBorder();
        if (border instanceof AbstractBorder) {
            return ((AbstractBorder) border).getBaselineResizeBehavior(jComponent);
        }
        return Component.BaselineResizeBehavior.OTHER;
    }
}
