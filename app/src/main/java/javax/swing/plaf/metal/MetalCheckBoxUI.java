package javax.swing.plaf.metal;

import javax.swing.AbstractButton;
import javax.swing.JComponent;
import javax.swing.UIManager;
import javax.swing.plaf.ComponentUI;
import sun.awt.AppContext;

/* loaded from: rt.jar:javax/swing/plaf/metal/MetalCheckBoxUI.class */
public class MetalCheckBoxUI extends MetalRadioButtonUI {
    private static final Object METAL_CHECK_BOX_UI_KEY = new Object();
    private static final String propertyPrefix = "CheckBox.";
    private boolean defaults_initialized = false;

    public static ComponentUI createUI(JComponent jComponent) {
        AppContext appContext = AppContext.getAppContext();
        MetalCheckBoxUI metalCheckBoxUI = (MetalCheckBoxUI) appContext.get(METAL_CHECK_BOX_UI_KEY);
        if (metalCheckBoxUI == null) {
            metalCheckBoxUI = new MetalCheckBoxUI();
            appContext.put(METAL_CHECK_BOX_UI_KEY, metalCheckBoxUI);
        }
        return metalCheckBoxUI;
    }

    @Override // javax.swing.plaf.basic.BasicRadioButtonUI, javax.swing.plaf.basic.BasicToggleButtonUI, javax.swing.plaf.basic.BasicButtonUI
    public String getPropertyPrefix() {
        return propertyPrefix;
    }

    @Override // javax.swing.plaf.metal.MetalRadioButtonUI, javax.swing.plaf.basic.BasicRadioButtonUI, javax.swing.plaf.basic.BasicButtonUI
    public void installDefaults(AbstractButton abstractButton) {
        super.installDefaults(abstractButton);
        if (!this.defaults_initialized) {
            this.icon = UIManager.getIcon(getPropertyPrefix() + "icon");
            this.defaults_initialized = true;
        }
    }

    @Override // javax.swing.plaf.metal.MetalRadioButtonUI, javax.swing.plaf.basic.BasicRadioButtonUI, javax.swing.plaf.basic.BasicButtonUI
    protected void uninstallDefaults(AbstractButton abstractButton) {
        super.uninstallDefaults(abstractButton);
        this.defaults_initialized = false;
    }
}
