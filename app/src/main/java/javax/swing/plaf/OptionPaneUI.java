package javax.swing.plaf;

import javax.swing.JOptionPane;

/* loaded from: rt.jar:javax/swing/plaf/OptionPaneUI.class */
public abstract class OptionPaneUI extends ComponentUI {
    public abstract void selectInitialValue(JOptionPane jOptionPane);

    public abstract boolean containsCustomComponents(JOptionPane jOptionPane);
}
