package javax.swing.plaf.metal;

import java.awt.Graphics;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.UIManager;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicLabelUI;
import sun.awt.AppContext;
import sun.swing.SwingUtilities2;

/* loaded from: rt.jar:javax/swing/plaf/metal/MetalLabelUI.class */
public class MetalLabelUI extends BasicLabelUI {
    protected static MetalLabelUI metalLabelUI = new MetalLabelUI();
    private static final Object METAL_LABEL_UI_KEY = new Object();

    public static ComponentUI createUI(JComponent jComponent) {
        if (System.getSecurityManager() != null) {
            AppContext appContext = AppContext.getAppContext();
            MetalLabelUI metalLabelUI2 = (MetalLabelUI) appContext.get(METAL_LABEL_UI_KEY);
            if (metalLabelUI2 == null) {
                metalLabelUI2 = new MetalLabelUI();
                appContext.put(METAL_LABEL_UI_KEY, metalLabelUI2);
            }
            return metalLabelUI2;
        }
        return metalLabelUI;
    }

    @Override // javax.swing.plaf.basic.BasicLabelUI
    protected void paintDisabledText(JLabel jLabel, Graphics graphics, String str, int i2, int i3) {
        int displayedMnemonicIndex = jLabel.getDisplayedMnemonicIndex();
        graphics.setColor(UIManager.getColor("Label.disabledForeground"));
        SwingUtilities2.drawStringUnderlineCharAt(jLabel, graphics, str, displayedMnemonicIndex, i2, i3);
    }
}
