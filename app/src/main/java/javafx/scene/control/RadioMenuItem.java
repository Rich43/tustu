package javafx.scene.control;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.BooleanPropertyBase;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.scene.Node;

/* loaded from: jfxrt.jar:javafx/scene/control/RadioMenuItem.class */
public class RadioMenuItem extends MenuItem implements Toggle {
    private ObjectProperty<ToggleGroup> toggleGroup;
    private BooleanProperty selected;
    private static final String DEFAULT_STYLE_CLASS = "radio-menu-item";
    private static final String STYLE_CLASS_SELECTED = "selected";

    public RadioMenuItem() {
        this(null, null);
    }

    public RadioMenuItem(String text) {
        this(text, null);
    }

    public RadioMenuItem(String text, Node graphic) {
        super(text, graphic);
        getStyleClass().add(DEFAULT_STYLE_CLASS);
    }

    @Override // javafx.scene.control.Toggle
    public final void setToggleGroup(ToggleGroup value) {
        toggleGroupProperty().set(value);
    }

    @Override // javafx.scene.control.Toggle
    public final ToggleGroup getToggleGroup() {
        if (this.toggleGroup == null) {
            return null;
        }
        return this.toggleGroup.get();
    }

    @Override // javafx.scene.control.Toggle
    public final ObjectProperty<ToggleGroup> toggleGroupProperty() {
        if (this.toggleGroup == null) {
            this.toggleGroup = new ObjectPropertyBase<ToggleGroup>() { // from class: javafx.scene.control.RadioMenuItem.1
                private ToggleGroup old;

                @Override // javafx.beans.property.ObjectPropertyBase
                protected void invalidated() {
                    if (this.old != null) {
                        this.old.getToggles().remove(RadioMenuItem.this);
                    }
                    this.old = get();
                    if (get() != null && !get().getToggles().contains(RadioMenuItem.this)) {
                        get().getToggles().add(RadioMenuItem.this);
                    }
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return RadioMenuItem.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "toggleGroup";
                }
            };
        }
        return this.toggleGroup;
    }

    @Override // javafx.scene.control.Toggle
    public final void setSelected(boolean value) {
        selectedProperty().set(value);
    }

    @Override // javafx.scene.control.Toggle
    public final boolean isSelected() {
        if (this.selected == null) {
            return false;
        }
        return this.selected.get();
    }

    @Override // javafx.scene.control.Toggle
    public final BooleanProperty selectedProperty() {
        if (this.selected == null) {
            this.selected = new BooleanPropertyBase() { // from class: javafx.scene.control.RadioMenuItem.2
                @Override // javafx.beans.property.BooleanPropertyBase
                protected void invalidated() {
                    if (RadioMenuItem.this.getToggleGroup() != null) {
                        if (get()) {
                            RadioMenuItem.this.getToggleGroup().selectToggle(RadioMenuItem.this);
                        } else if (RadioMenuItem.this.getToggleGroup().getSelectedToggle() == RadioMenuItem.this) {
                            RadioMenuItem.this.getToggleGroup().clearSelectedToggle();
                        }
                    }
                    if (RadioMenuItem.this.isSelected()) {
                        RadioMenuItem.this.getStyleClass().add("selected");
                    } else {
                        RadioMenuItem.this.getStyleClass().remove("selected");
                    }
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return RadioMenuItem.this;
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
