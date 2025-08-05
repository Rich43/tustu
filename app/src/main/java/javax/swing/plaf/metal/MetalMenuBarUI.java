package javax.swing.plaf.metal;

import java.awt.Graphics;
import javax.swing.JComponent;
import javax.swing.JMenuBar;
import javax.swing.JToolBar;
import javax.swing.UIManager;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.basic.BasicMenuBarUI;

/* loaded from: rt.jar:javax/swing/plaf/metal/MetalMenuBarUI.class */
public class MetalMenuBarUI extends BasicMenuBarUI {
    public static ComponentUI createUI(JComponent jComponent) {
        if (jComponent == null) {
            throw new NullPointerException("Must pass in a non-null component");
        }
        return new MetalMenuBarUI();
    }

    @Override // javax.swing.plaf.basic.BasicMenuBarUI, javax.swing.plaf.ComponentUI
    public void installUI(JComponent jComponent) {
        super.installUI(jComponent);
        MetalToolBarUI.register(jComponent);
    }

    @Override // javax.swing.plaf.basic.BasicMenuBarUI, javax.swing.plaf.ComponentUI
    public void uninstallUI(JComponent jComponent) {
        super.uninstallUI(jComponent);
        MetalToolBarUI.unregister(jComponent);
    }

    @Override // javax.swing.plaf.ComponentUI
    public void update(Graphics graphics, JComponent jComponent) {
        boolean zIsOpaque = jComponent.isOpaque();
        if (graphics == null) {
            throw new NullPointerException("Graphics must be non-null");
        }
        if (zIsOpaque && (jComponent.getBackground() instanceof UIResource) && UIManager.get("MenuBar.gradient") != null) {
            if (MetalToolBarUI.doesMenuBarBorderToolBar((JMenuBar) jComponent)) {
                JToolBar jToolBar = (JToolBar) MetalToolBarUI.findRegisteredComponentOfType(jComponent, JToolBar.class);
                if (jToolBar.isOpaque() && (jToolBar.getBackground() instanceof UIResource)) {
                    MetalUtils.drawGradient(jComponent, graphics, "MenuBar.gradient", 0, 0, jComponent.getWidth(), jComponent.getHeight() + jToolBar.getHeight(), true);
                    paint(graphics, jComponent);
                    return;
                }
            }
            MetalUtils.drawGradient(jComponent, graphics, "MenuBar.gradient", 0, 0, jComponent.getWidth(), jComponent.getHeight(), true);
            paint(graphics, jComponent);
            return;
        }
        super.update(graphics, jComponent);
    }
}
