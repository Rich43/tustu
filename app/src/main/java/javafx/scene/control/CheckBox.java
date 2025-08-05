package javafx.scene.control;

import com.sun.javafx.scene.control.skin.CheckBoxSkin;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.BooleanPropertyBase;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.css.PseudoClass;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.AccessibleAttribute;
import javafx.scene.AccessibleRole;
import javax.swing.JInternalFrame;

/* loaded from: jfxrt.jar:javafx/scene/control/CheckBox.class */
public class CheckBox extends ButtonBase {
    private BooleanProperty indeterminate;
    private BooleanProperty selected;
    private BooleanProperty allowIndeterminate;
    private static final String DEFAULT_STYLE_CLASS = "check-box";
    private static final PseudoClass PSEUDO_CLASS_DETERMINATE = PseudoClass.getPseudoClass("determinate");
    private static final PseudoClass PSEUDO_CLASS_INDETERMINATE = PseudoClass.getPseudoClass("indeterminate");
    private static final PseudoClass PSEUDO_CLASS_SELECTED = PseudoClass.getPseudoClass(JInternalFrame.IS_SELECTED_PROPERTY);

    public CheckBox() {
        initialize();
    }

    public CheckBox(String text) {
        setText(text);
        initialize();
    }

    private void initialize() {
        getStyleClass().setAll(DEFAULT_STYLE_CLASS);
        setAccessibleRole(AccessibleRole.CHECK_BOX);
        setAlignment(Pos.CENTER_LEFT);
        setMnemonicParsing(true);
        pseudoClassStateChanged(PSEUDO_CLASS_DETERMINATE, true);
    }

    public final void setIndeterminate(boolean value) {
        indeterminateProperty().set(value);
    }

    public final boolean isIndeterminate() {
        if (this.indeterminate == null) {
            return false;
        }
        return this.indeterminate.get();
    }

    public final BooleanProperty indeterminateProperty() {
        if (this.indeterminate == null) {
            this.indeterminate = new BooleanPropertyBase(false) { // from class: javafx.scene.control.CheckBox.1
                @Override // javafx.beans.property.BooleanPropertyBase
                protected void invalidated() {
                    boolean active = get();
                    CheckBox.this.pseudoClassStateChanged(CheckBox.PSEUDO_CLASS_DETERMINATE, !active);
                    CheckBox.this.pseudoClassStateChanged(CheckBox.PSEUDO_CLASS_INDETERMINATE, active);
                    CheckBox.this.notifyAccessibleAttributeChanged(AccessibleAttribute.INDETERMINATE);
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return CheckBox.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "indeterminate";
                }
            };
        }
        return this.indeterminate;
    }

    public final void setSelected(boolean value) {
        selectedProperty().set(value);
    }

    public final boolean isSelected() {
        if (this.selected == null) {
            return false;
        }
        return this.selected.get();
    }

    public final BooleanProperty selectedProperty() {
        if (this.selected == null) {
            this.selected = new BooleanPropertyBase() { // from class: javafx.scene.control.CheckBox.2
                @Override // javafx.beans.property.BooleanPropertyBase
                protected void invalidated() {
                    Boolean v2 = Boolean.valueOf(get());
                    CheckBox.this.pseudoClassStateChanged(CheckBox.PSEUDO_CLASS_SELECTED, v2.booleanValue());
                    CheckBox.this.notifyAccessibleAttributeChanged(AccessibleAttribute.SELECTED);
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return CheckBox.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return JInternalFrame.IS_SELECTED_PROPERTY;
                }
            };
        }
        return this.selected;
    }

    public final void setAllowIndeterminate(boolean value) {
        allowIndeterminateProperty().set(value);
    }

    public final boolean isAllowIndeterminate() {
        if (this.allowIndeterminate == null) {
            return false;
        }
        return this.allowIndeterminate.get();
    }

    public final BooleanProperty allowIndeterminateProperty() {
        if (this.allowIndeterminate == null) {
            this.allowIndeterminate = new SimpleBooleanProperty(this, "allowIndeterminate");
        }
        return this.allowIndeterminate;
    }

    @Override // javafx.scene.control.ButtonBase
    public void fire() {
        if (!isDisabled()) {
            if (isAllowIndeterminate()) {
                if (!isSelected() && !isIndeterminate()) {
                    setIndeterminate(true);
                } else if (isSelected() && !isIndeterminate()) {
                    setSelected(false);
                } else if (isIndeterminate()) {
                    setSelected(true);
                    setIndeterminate(false);
                }
            } else {
                setSelected(!isSelected());
                setIndeterminate(false);
            }
            fireEvent(new ActionEvent());
        }
    }

    @Override // javafx.scene.control.Control
    protected Skin<?> createDefaultSkin() {
        return new CheckBoxSkin(this);
    }

    @Override // javafx.scene.control.Control, javafx.scene.Parent, javafx.scene.Node
    public Object queryAccessibleAttribute(AccessibleAttribute attribute, Object... parameters) {
        switch (attribute) {
            case SELECTED:
                return Boolean.valueOf(isSelected());
            case INDETERMINATE:
                return Boolean.valueOf(isIndeterminate());
            default:
                return super.queryAccessibleAttribute(attribute, parameters);
        }
    }
}
