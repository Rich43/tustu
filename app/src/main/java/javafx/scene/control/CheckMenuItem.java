package javafx.scene.control;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.BooleanPropertyBase;
import javafx.scene.Node;

/* loaded from: jfxrt.jar:javafx/scene/control/CheckMenuItem.class */
public class CheckMenuItem extends MenuItem {
    private BooleanProperty selected;
    private static final String DEFAULT_STYLE_CLASS = "check-menu-item";
    private static final String STYLE_CLASS_SELECTED = "selected";

    public CheckMenuItem() {
        this(null, null);
    }

    public CheckMenuItem(String text) {
        this(text, null);
    }

    public CheckMenuItem(String text, Node graphic) {
        super(text, graphic);
        getStyleClass().add(DEFAULT_STYLE_CLASS);
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
            this.selected = new BooleanPropertyBase() { // from class: javafx.scene.control.CheckMenuItem.1
                @Override // javafx.beans.property.BooleanPropertyBase
                protected void invalidated() {
                    get();
                    if (CheckMenuItem.this.isSelected()) {
                        CheckMenuItem.this.getStyleClass().add("selected");
                    } else {
                        CheckMenuItem.this.getStyleClass().remove("selected");
                    }
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return CheckMenuItem.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "selected";
                }
            };
        }
        return this.selected;
    }
}
