package javax.swing.plaf.basic;

import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;
import sun.awt.AppContext;

/* loaded from: rt.jar:javax/swing/plaf/basic/BasicCheckBoxUI.class */
public class BasicCheckBoxUI extends BasicRadioButtonUI {
    private static final Object BASIC_CHECK_BOX_UI_KEY = new Object();
    private static final String propertyPrefix = "CheckBox.";

    public static ComponentUI createUI(JComponent jComponent) {
        AppContext appContext = AppContext.getAppContext();
        BasicCheckBoxUI basicCheckBoxUI = (BasicCheckBoxUI) appContext.get(BASIC_CHECK_BOX_UI_KEY);
        if (basicCheckBoxUI == null) {
            basicCheckBoxUI = new BasicCheckBoxUI();
            appContext.put(BASIC_CHECK_BOX_UI_KEY, basicCheckBoxUI);
        }
        return basicCheckBoxUI;
    }

    @Override // javax.swing.plaf.basic.BasicRadioButtonUI, javax.swing.plaf.basic.BasicToggleButtonUI, javax.swing.plaf.basic.BasicButtonUI
    public String getPropertyPrefix() {
        return propertyPrefix;
    }
}
