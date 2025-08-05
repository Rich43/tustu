package javax.swing.plaf.basic;

import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;

/* loaded from: rt.jar:javax/swing/plaf/basic/BasicFormattedTextFieldUI.class */
public class BasicFormattedTextFieldUI extends BasicTextFieldUI {
    public static ComponentUI createUI(JComponent jComponent) {
        return new BasicFormattedTextFieldUI();
    }

    @Override // javax.swing.plaf.basic.BasicTextFieldUI, javax.swing.plaf.basic.BasicTextUI
    protected String getPropertyPrefix() {
        return "FormattedTextField";
    }
}
