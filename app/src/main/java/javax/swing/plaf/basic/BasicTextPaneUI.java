package javax.swing.plaf.basic;

import java.beans.PropertyChangeEvent;
import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;

/* loaded from: rt.jar:javax/swing/plaf/basic/BasicTextPaneUI.class */
public class BasicTextPaneUI extends BasicEditorPaneUI {
    public static ComponentUI createUI(JComponent jComponent) {
        return new BasicTextPaneUI();
    }

    @Override // javax.swing.plaf.basic.BasicEditorPaneUI, javax.swing.plaf.basic.BasicTextUI
    protected String getPropertyPrefix() {
        return "TextPane";
    }

    @Override // javax.swing.plaf.basic.BasicEditorPaneUI, javax.swing.plaf.basic.BasicTextUI, javax.swing.plaf.ComponentUI
    public void installUI(JComponent jComponent) {
        super.installUI(jComponent);
    }

    @Override // javax.swing.plaf.basic.BasicEditorPaneUI, javax.swing.plaf.basic.BasicTextUI
    protected void propertyChange(PropertyChangeEvent propertyChangeEvent) {
        super.propertyChange(propertyChangeEvent);
    }
}
