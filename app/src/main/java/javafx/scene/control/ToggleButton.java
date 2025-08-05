package javafx.scene.control;

import com.sun.javafx.scene.control.skin.ToggleButtonSkin;
import com.sun.javafx.scene.traversal.ParentTraversalEngine;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.BooleanPropertyBase;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.beans.value.ChangeListener;
import javafx.css.PseudoClass;
import javafx.css.StyleableProperty;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.AccessibleAttribute;
import javafx.scene.AccessibleRole;
import javafx.scene.Node;
import javax.swing.JInternalFrame;

/* loaded from: jfxrt.jar:javafx/scene/control/ToggleButton.class */
public class ToggleButton extends ButtonBase implements Toggle {
    private BooleanProperty selected;
    private ObjectProperty<ToggleGroup> toggleGroup;
    private static final String DEFAULT_STYLE_CLASS = "toggle-button";
    private static final PseudoClass PSEUDO_CLASS_SELECTED = PseudoClass.getPseudoClass(JInternalFrame.IS_SELECTED_PROPERTY);

    public ToggleButton() {
        initialize();
    }

    public ToggleButton(String text) {
        setText(text);
        initialize();
    }

    public ToggleButton(String text, Node graphic) {
        setText(text);
        setGraphic(graphic);
        initialize();
    }

    private void initialize() {
        getStyleClass().setAll(DEFAULT_STYLE_CLASS);
        setAccessibleRole(AccessibleRole.TOGGLE_BUTTON);
        ((StyleableProperty) alignmentProperty()).applyStyle(null, Pos.CENTER);
        setMnemonicParsing(true);
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
            this.selected = new BooleanPropertyBase() { // from class: javafx.scene.control.ToggleButton.1
                @Override // javafx.beans.property.BooleanPropertyBase
                protected void invalidated() {
                    boolean selected = get();
                    ToggleGroup tg = ToggleButton.this.getToggleGroup();
                    ToggleButton.this.pseudoClassStateChanged(ToggleButton.PSEUDO_CLASS_SELECTED, selected);
                    ToggleButton.this.notifyAccessibleAttributeChanged(AccessibleAttribute.SELECTED);
                    if (tg != null) {
                        if (selected) {
                            tg.selectToggle(ToggleButton.this);
                        } else if (tg.getSelectedToggle() == ToggleButton.this) {
                            tg.clearSelectedToggle();
                        }
                    }
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return ToggleButton.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return JInternalFrame.IS_SELECTED_PROPERTY;
                }
            };
        }
        return this.selected;
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
            this.toggleGroup = new ObjectPropertyBase<ToggleGroup>() { // from class: javafx.scene.control.ToggleButton.2
                private ToggleGroup old;
                private ChangeListener<Toggle> listener = (o2, oV, nV) -> {
                    ToggleButton.this.getImpl_traversalEngine().setOverriddenFocusTraversability(nV != null ? Boolean.valueOf(ToggleButton.this.isSelected()) : null);
                };

                @Override // javafx.beans.property.ObjectPropertyBase
                protected void invalidated() {
                    ToggleGroup tg = get();
                    if (tg != null && !tg.getToggles().contains(ToggleButton.this)) {
                        if (this.old != null) {
                            this.old.getToggles().remove(ToggleButton.this);
                        }
                        tg.getToggles().add(ToggleButton.this);
                        ParentTraversalEngine parentTraversalEngine = new ParentTraversalEngine(ToggleButton.this);
                        ToggleButton.this.setImpl_traversalEngine(parentTraversalEngine);
                        parentTraversalEngine.setOverriddenFocusTraversability(tg.getSelectedToggle() != null ? Boolean.valueOf(ToggleButton.this.isSelected()) : null);
                        tg.selectedToggleProperty().addListener(this.listener);
                    } else if (tg == null) {
                        this.old.selectedToggleProperty().removeListener(this.listener);
                        this.old.getToggles().remove(ToggleButton.this);
                        ToggleButton.this.setImpl_traversalEngine(null);
                    }
                    this.old = tg;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return ToggleButton.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "toggleGroup";
                }
            };
        }
        return this.toggleGroup;
    }

    @Override // javafx.scene.control.ButtonBase
    public void fire() {
        if (!isDisabled()) {
            setSelected(!isSelected());
            fireEvent(new ActionEvent());
        }
    }

    @Override // javafx.scene.control.Control
    protected Skin<?> createDefaultSkin() {
        return new ToggleButtonSkin(this);
    }

    @Override // javafx.scene.control.Labeled
    @Deprecated
    protected Pos impl_cssGetAlignmentInitialValue() {
        return Pos.CENTER;
    }

    @Override // javafx.scene.control.Control, javafx.scene.Parent, javafx.scene.Node
    public Object queryAccessibleAttribute(AccessibleAttribute attribute, Object... parameters) {
        switch (attribute) {
            case SELECTED:
                return Boolean.valueOf(isSelected());
            default:
                return super.queryAccessibleAttribute(attribute, parameters);
        }
    }
}
