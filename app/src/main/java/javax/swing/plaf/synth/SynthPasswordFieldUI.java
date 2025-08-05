package javax.swing.plaf.synth;

import java.awt.Graphics;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import javax.swing.plaf.ComponentUI;
import javax.swing.text.DefaultEditorKit;
import javax.swing.text.Element;
import javax.swing.text.PasswordView;
import javax.swing.text.View;

/* loaded from: rt.jar:javax/swing/plaf/synth/SynthPasswordFieldUI.class */
public class SynthPasswordFieldUI extends SynthTextFieldUI {
    public static ComponentUI createUI(JComponent jComponent) {
        return new SynthPasswordFieldUI();
    }

    @Override // javax.swing.plaf.basic.BasicTextFieldUI, javax.swing.plaf.basic.BasicTextUI
    protected String getPropertyPrefix() {
        return "PasswordField";
    }

    @Override // javax.swing.plaf.basic.BasicTextFieldUI, javax.swing.plaf.basic.BasicTextUI, javax.swing.text.ViewFactory
    public View create(Element element) {
        return new PasswordView(element);
    }

    @Override // javax.swing.plaf.synth.SynthTextFieldUI
    void paintBackground(SynthContext synthContext, Graphics graphics, JComponent jComponent) {
        synthContext.getPainter().paintPasswordFieldBackground(synthContext, graphics, 0, 0, jComponent.getWidth(), jComponent.getHeight());
    }

    @Override // javax.swing.plaf.synth.SynthTextFieldUI, javax.swing.plaf.synth.SynthUI
    public void paintBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
        synthContext.getPainter().paintPasswordFieldBorder(synthContext, graphics, i2, i3, i4, i5);
    }

    @Override // javax.swing.plaf.basic.BasicTextUI
    protected void installKeyboardActions() {
        Action action;
        super.installKeyboardActions();
        ActionMap uIActionMap = SwingUtilities.getUIActionMap(getComponent());
        if (uIActionMap != null && uIActionMap.get(DefaultEditorKit.selectWordAction) != null && (action = uIActionMap.get(DefaultEditorKit.selectLineAction)) != null) {
            uIActionMap.put(DefaultEditorKit.selectWordAction, action);
        }
    }
}
