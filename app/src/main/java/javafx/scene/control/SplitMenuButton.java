package javafx.scene.control;

import com.sun.javafx.scene.control.skin.SplitMenuButtonSkin;
import javafx.event.ActionEvent;
import javafx.scene.AccessibleAction;
import javafx.scene.AccessibleAttribute;
import javafx.scene.AccessibleRole;

/* loaded from: jfxrt.jar:javafx/scene/control/SplitMenuButton.class */
public class SplitMenuButton extends MenuButton {
    private static final String DEFAULT_STYLE_CLASS = "split-menu-button";

    public SplitMenuButton() {
        this((MenuItem[]) null);
    }

    public SplitMenuButton(MenuItem... items) {
        if (items != null) {
            getItems().addAll(items);
        }
        getStyleClass().setAll(DEFAULT_STYLE_CLASS);
        setAccessibleRole(AccessibleRole.SPLIT_MENU_BUTTON);
        setMnemonicParsing(true);
    }

    @Override // javafx.scene.control.MenuButton, javafx.scene.control.ButtonBase
    public void fire() {
        if (!isDisabled()) {
            fireEvent(new ActionEvent());
        }
    }

    @Override // javafx.scene.control.MenuButton, javafx.scene.control.Control
    protected Skin<?> createDefaultSkin() {
        return new SplitMenuButtonSkin(this);
    }

    @Override // javafx.scene.control.Control, javafx.scene.Parent, javafx.scene.Node
    public Object queryAccessibleAttribute(AccessibleAttribute attribute, Object... parameters) {
        switch (attribute) {
            case EXPANDED:
                return Boolean.valueOf(isShowing());
            default:
                return super.queryAccessibleAttribute(attribute, parameters);
        }
    }

    @Override // javafx.scene.control.MenuButton, javafx.scene.control.ButtonBase, javafx.scene.control.Control, javafx.scene.Node
    public void executeAccessibleAction(AccessibleAction action, Object... parameters) {
        switch (action) {
            case FIRE:
                fire();
                break;
            case EXPAND:
                show();
                break;
            case COLLAPSE:
                hide();
                break;
            default:
                super.executeAccessibleAction(action, new Object[0]);
                break;
        }
    }
}
