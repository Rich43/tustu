package javafx.scene.control;

import javafx.scene.AccessibleAttribute;
import javafx.scene.AccessibleRole;

/* loaded from: jfxrt.jar:javafx/scene/control/PasswordField.class */
public class PasswordField extends TextField {
    public PasswordField() {
        getStyleClass().add("password-field");
        setAccessibleRole(AccessibleRole.PASSWORD_FIELD);
    }

    @Override // javafx.scene.control.TextInputControl
    public void cut() {
    }

    @Override // javafx.scene.control.TextInputControl
    public void copy() {
    }

    @Override // javafx.scene.control.TextInputControl, javafx.scene.control.Control, javafx.scene.Parent, javafx.scene.Node
    public Object queryAccessibleAttribute(AccessibleAttribute attribute, Object... parameters) {
        switch (attribute) {
            case TEXT:
                return null;
            default:
                return super.queryAccessibleAttribute(attribute, parameters);
        }
    }
}
