package javafx.scene.control;

import com.sun.javafx.scene.control.skin.ButtonSkin;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.BooleanPropertyBase;
import javafx.css.PseudoClass;
import javafx.event.ActionEvent;
import javafx.scene.AccessibleRole;
import javafx.scene.Node;

/* loaded from: jfxrt.jar:javafx/scene/control/Button.class */
public class Button extends ButtonBase {
    private BooleanProperty defaultButton;
    private BooleanProperty cancelButton;
    private static final String DEFAULT_STYLE_CLASS = "button";
    private static final PseudoClass PSEUDO_CLASS_DEFAULT = PseudoClass.getPseudoClass("default");
    private static final PseudoClass PSEUDO_CLASS_CANCEL = PseudoClass.getPseudoClass("cancel");

    public Button() {
        initialize();
    }

    public Button(String text) {
        super(text);
        initialize();
    }

    public Button(String text, Node graphic) {
        super(text, graphic);
        initialize();
    }

    private void initialize() {
        getStyleClass().setAll("button");
        setAccessibleRole(AccessibleRole.BUTTON);
        setMnemonicParsing(true);
    }

    public final void setDefaultButton(boolean value) {
        defaultButtonProperty().set(value);
    }

    public final boolean isDefaultButton() {
        if (this.defaultButton == null) {
            return false;
        }
        return this.defaultButton.get();
    }

    public final BooleanProperty defaultButtonProperty() {
        if (this.defaultButton == null) {
            this.defaultButton = new BooleanPropertyBase(false) { // from class: javafx.scene.control.Button.1
                @Override // javafx.beans.property.BooleanPropertyBase
                protected void invalidated() {
                    Button.this.pseudoClassStateChanged(Button.PSEUDO_CLASS_DEFAULT, get());
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return Button.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "defaultButton";
                }
            };
        }
        return this.defaultButton;
    }

    public final void setCancelButton(boolean value) {
        cancelButtonProperty().set(value);
    }

    public final boolean isCancelButton() {
        if (this.cancelButton == null) {
            return false;
        }
        return this.cancelButton.get();
    }

    public final BooleanProperty cancelButtonProperty() {
        if (this.cancelButton == null) {
            this.cancelButton = new BooleanPropertyBase(false) { // from class: javafx.scene.control.Button.2
                @Override // javafx.beans.property.BooleanPropertyBase
                protected void invalidated() {
                    Button.this.pseudoClassStateChanged(Button.PSEUDO_CLASS_CANCEL, get());
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return Button.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "cancelButton";
                }
            };
        }
        return this.cancelButton;
    }

    @Override // javafx.scene.control.ButtonBase
    public void fire() {
        if (!isDisabled()) {
            fireEvent(new ActionEvent());
        }
    }

    @Override // javafx.scene.control.Control
    protected Skin<?> createDefaultSkin() {
        return new ButtonSkin(this);
    }
}
