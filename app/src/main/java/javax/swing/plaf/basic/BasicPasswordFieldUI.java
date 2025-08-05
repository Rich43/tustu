package javax.swing.plaf.basic;

import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.JComponent;
import javax.swing.LookAndFeel;
import javax.swing.UIManager;
import javax.swing.plaf.ComponentUI;
import javax.swing.text.DefaultEditorKit;
import javax.swing.text.Element;
import javax.swing.text.PasswordView;
import javax.swing.text.View;

/* loaded from: rt.jar:javax/swing/plaf/basic/BasicPasswordFieldUI.class */
public class BasicPasswordFieldUI extends BasicTextFieldUI {
    public static ComponentUI createUI(JComponent jComponent) {
        return new BasicPasswordFieldUI();
    }

    @Override // javax.swing.plaf.basic.BasicTextFieldUI, javax.swing.plaf.basic.BasicTextUI
    protected String getPropertyPrefix() {
        return "PasswordField";
    }

    @Override // javax.swing.plaf.basic.BasicTextUI
    protected void installDefaults() {
        super.installDefaults();
        Character ch = (Character) UIManager.getDefaults().get(getPropertyPrefix() + ".echoChar");
        if (ch != null) {
            LookAndFeel.installProperty(getComponent(), "echoChar", ch);
        }
    }

    @Override // javax.swing.plaf.basic.BasicTextFieldUI, javax.swing.plaf.basic.BasicTextUI, javax.swing.text.ViewFactory
    public View create(Element element) {
        return new PasswordView(element);
    }

    @Override // javax.swing.plaf.basic.BasicTextUI
    ActionMap createActionMap() {
        Action action;
        ActionMap actionMapCreateActionMap = super.createActionMap();
        if (actionMapCreateActionMap.get(DefaultEditorKit.selectWordAction) != null && (action = actionMapCreateActionMap.get(DefaultEditorKit.selectLineAction)) != null) {
            actionMapCreateActionMap.remove(DefaultEditorKit.selectWordAction);
            actionMapCreateActionMap.put(DefaultEditorKit.selectWordAction, action);
        }
        return actionMapCreateActionMap;
    }
}
