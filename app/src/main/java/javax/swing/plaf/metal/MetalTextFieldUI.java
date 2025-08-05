package javax.swing.plaf.metal;

import java.beans.PropertyChangeEvent;
import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicTextFieldUI;

/* loaded from: rt.jar:javax/swing/plaf/metal/MetalTextFieldUI.class */
public class MetalTextFieldUI extends BasicTextFieldUI {
    public static ComponentUI createUI(JComponent jComponent) {
        return new MetalTextFieldUI();
    }

    @Override // javax.swing.plaf.basic.BasicTextUI
    public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
        super.propertyChange(propertyChangeEvent);
    }
}
