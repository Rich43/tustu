package com.sun.java.swing.plaf.motif;

import javax.swing.AbstractButton;
import javax.swing.JComponent;
import javax.swing.UIManager;
import javax.swing.plaf.ComponentUI;
import sun.awt.AppContext;

/* loaded from: rt.jar:com/sun/java/swing/plaf/motif/MotifCheckBoxUI.class */
public class MotifCheckBoxUI extends MotifRadioButtonUI {
    private static final Object MOTIF_CHECK_BOX_UI_KEY = new Object();
    private static final String propertyPrefix = "CheckBox.";
    private boolean defaults_initialized = false;

    public static ComponentUI createUI(JComponent jComponent) {
        AppContext appContext = AppContext.getAppContext();
        MotifCheckBoxUI motifCheckBoxUI = (MotifCheckBoxUI) appContext.get(MOTIF_CHECK_BOX_UI_KEY);
        if (motifCheckBoxUI == null) {
            motifCheckBoxUI = new MotifCheckBoxUI();
            appContext.put(MOTIF_CHECK_BOX_UI_KEY, motifCheckBoxUI);
        }
        return motifCheckBoxUI;
    }

    @Override // javax.swing.plaf.basic.BasicRadioButtonUI, javax.swing.plaf.basic.BasicToggleButtonUI, javax.swing.plaf.basic.BasicButtonUI
    public String getPropertyPrefix() {
        return propertyPrefix;
    }

    @Override // com.sun.java.swing.plaf.motif.MotifRadioButtonUI, javax.swing.plaf.basic.BasicRadioButtonUI, javax.swing.plaf.basic.BasicButtonUI
    public void installDefaults(AbstractButton abstractButton) {
        super.installDefaults(abstractButton);
        if (!this.defaults_initialized) {
            this.icon = UIManager.getIcon(getPropertyPrefix() + "icon");
            this.defaults_initialized = true;
        }
    }

    @Override // com.sun.java.swing.plaf.motif.MotifRadioButtonUI, javax.swing.plaf.basic.BasicRadioButtonUI, javax.swing.plaf.basic.BasicButtonUI
    protected void uninstallDefaults(AbstractButton abstractButton) {
        super.uninstallDefaults(abstractButton);
        this.defaults_initialized = false;
    }
}
