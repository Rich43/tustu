package javafx.scene.control;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.css.PseudoClass;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.AccessibleAction;
import javafx.scene.AccessibleAttribute;
import javax.swing.JTree;

/* loaded from: jfxrt.jar:javafx/scene/control/ComboBoxBase.class */
public abstract class ComboBoxBase<T> extends Control {
    private ReadOnlyBooleanWrapper showing;
    private static final String DEFAULT_STYLE_CLASS = "combo-box-base";
    public static final EventType<Event> ON_SHOWING = new EventType<>(Event.ANY, "COMBO_BOX_BASE_ON_SHOWING");
    public static final EventType<Event> ON_SHOWN = new EventType<>(Event.ANY, "COMBO_BOX_BASE_ON_SHOWN");
    public static final EventType<Event> ON_HIDING = new EventType<>(Event.ANY, "COMBO_BOX_BASE_ON_HIDING");
    public static final EventType<Event> ON_HIDDEN = new EventType<>(Event.ANY, "COMBO_BOX_BASE_ON_HIDDEN");
    private static final PseudoClass PSEUDO_CLASS_EDITABLE = PseudoClass.getPseudoClass(JTree.EDITABLE_PROPERTY);
    private static final PseudoClass PSEUDO_CLASS_SHOWING = PseudoClass.getPseudoClass("showing");
    private static final PseudoClass PSEUDO_CLASS_ARMED = PseudoClass.getPseudoClass("armed");
    private ObjectProperty<T> value = new SimpleObjectProperty(this, "value");
    private BooleanProperty editable = new SimpleBooleanProperty(this, JTree.EDITABLE_PROPERTY, false) { // from class: javafx.scene.control.ComboBoxBase.1
        @Override // javafx.beans.property.BooleanPropertyBase
        protected void invalidated() {
            ComboBoxBase.this.pseudoClassStateChanged(ComboBoxBase.PSEUDO_CLASS_EDITABLE, get());
        }
    };
    private StringProperty promptText = new SimpleStringProperty(this, "promptText", "") { // from class: javafx.scene.control.ComboBoxBase.3
        @Override // javafx.beans.property.StringPropertyBase
        protected void invalidated() {
            String txt = get();
            if (txt != null && txt.contains("\n")) {
                set(txt.replace("\n", ""));
            }
        }
    };
    private BooleanProperty armed = new SimpleBooleanProperty(this, "armed", false) { // from class: javafx.scene.control.ComboBoxBase.4
        @Override // javafx.beans.property.BooleanPropertyBase
        protected void invalidated() {
            ComboBoxBase.this.pseudoClassStateChanged(ComboBoxBase.PSEUDO_CLASS_ARMED, get());
        }
    };
    private ObjectProperty<EventHandler<ActionEvent>> onAction = new ObjectPropertyBase<EventHandler<ActionEvent>>() { // from class: javafx.scene.control.ComboBoxBase.5
        @Override // javafx.beans.property.ObjectPropertyBase
        protected void invalidated() {
            ComboBoxBase.this.setEventHandler(ActionEvent.ACTION, get());
        }

        @Override // javafx.beans.property.ReadOnlyProperty
        public Object getBean() {
            return ComboBoxBase.this;
        }

        @Override // javafx.beans.property.ReadOnlyProperty
        public String getName() {
            return "onAction";
        }
    };
    private ObjectProperty<EventHandler<Event>> onShowing = new ObjectPropertyBase<EventHandler<Event>>() { // from class: javafx.scene.control.ComboBoxBase.6
        @Override // javafx.beans.property.ObjectPropertyBase
        protected void invalidated() {
            ComboBoxBase.this.setEventHandler(ComboBoxBase.ON_SHOWING, get());
        }

        @Override // javafx.beans.property.ReadOnlyProperty
        public Object getBean() {
            return ComboBoxBase.this;
        }

        @Override // javafx.beans.property.ReadOnlyProperty
        public String getName() {
            return "onShowing";
        }
    };
    private ObjectProperty<EventHandler<Event>> onShown = new ObjectPropertyBase<EventHandler<Event>>() { // from class: javafx.scene.control.ComboBoxBase.7
        @Override // javafx.beans.property.ObjectPropertyBase
        protected void invalidated() {
            ComboBoxBase.this.setEventHandler(ComboBoxBase.ON_SHOWN, get());
        }

        @Override // javafx.beans.property.ReadOnlyProperty
        public Object getBean() {
            return ComboBoxBase.this;
        }

        @Override // javafx.beans.property.ReadOnlyProperty
        public String getName() {
            return "onShown";
        }
    };
    private ObjectProperty<EventHandler<Event>> onHiding = new ObjectPropertyBase<EventHandler<Event>>() { // from class: javafx.scene.control.ComboBoxBase.8
        @Override // javafx.beans.property.ObjectPropertyBase
        protected void invalidated() {
            ComboBoxBase.this.setEventHandler(ComboBoxBase.ON_HIDING, get());
        }

        @Override // javafx.beans.property.ReadOnlyProperty
        public Object getBean() {
            return ComboBoxBase.this;
        }

        @Override // javafx.beans.property.ReadOnlyProperty
        public String getName() {
            return "onHiding";
        }
    };
    private ObjectProperty<EventHandler<Event>> onHidden = new ObjectPropertyBase<EventHandler<Event>>() { // from class: javafx.scene.control.ComboBoxBase.9
        @Override // javafx.beans.property.ObjectPropertyBase
        protected void invalidated() {
            ComboBoxBase.this.setEventHandler(ComboBoxBase.ON_HIDDEN, get());
        }

        @Override // javafx.beans.property.ReadOnlyProperty
        public Object getBean() {
            return ComboBoxBase.this;
        }

        @Override // javafx.beans.property.ReadOnlyProperty
        public String getName() {
            return "onHidden";
        }
    };

    public ComboBoxBase() {
        getStyleClass().add(DEFAULT_STYLE_CLASS);
        getProperties().addListener(change -> {
            if (change.wasAdded() && change.getKey() == "FOCUSED") {
                setFocused(((Boolean) change.getValueAdded()).booleanValue());
                getProperties().remove("FOCUSED");
            }
        });
    }

    public ObjectProperty<T> valueProperty() {
        return this.value;
    }

    public final void setValue(T value) {
        valueProperty().set(value);
    }

    public final T getValue() {
        return valueProperty().get();
    }

    public BooleanProperty editableProperty() {
        return this.editable;
    }

    public final void setEditable(boolean value) {
        editableProperty().set(value);
    }

    public final boolean isEditable() {
        return editableProperty().get();
    }

    public ReadOnlyBooleanProperty showingProperty() {
        return showingPropertyImpl().getReadOnlyProperty();
    }

    public final boolean isShowing() {
        return showingPropertyImpl().get();
    }

    private void setShowing(boolean value) {
        Event.fireEvent(this, value ? new Event(ON_SHOWING) : new Event(ON_HIDING));
        showingPropertyImpl().set(value);
        Event.fireEvent(this, value ? new Event(ON_SHOWN) : new Event(ON_HIDDEN));
    }

    private ReadOnlyBooleanWrapper showingPropertyImpl() {
        if (this.showing == null) {
            this.showing = new ReadOnlyBooleanWrapper(false) { // from class: javafx.scene.control.ComboBoxBase.2
                @Override // javafx.beans.property.BooleanPropertyBase
                protected void invalidated() {
                    ComboBoxBase.this.pseudoClassStateChanged(ComboBoxBase.PSEUDO_CLASS_SHOWING, get());
                    ComboBoxBase.this.notifyAccessibleAttributeChanged(AccessibleAttribute.EXPANDED);
                }

                @Override // javafx.beans.property.SimpleBooleanProperty, javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return ComboBoxBase.this;
                }

                @Override // javafx.beans.property.SimpleBooleanProperty, javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "showing";
                }
            };
        }
        return this.showing;
    }

    public final StringProperty promptTextProperty() {
        return this.promptText;
    }

    public final String getPromptText() {
        return this.promptText.get();
    }

    public final void setPromptText(String value) {
        this.promptText.set(value);
    }

    public BooleanProperty armedProperty() {
        return this.armed;
    }

    private final void setArmed(boolean value) {
        armedProperty().set(value);
    }

    public final boolean isArmed() {
        return armedProperty().get();
    }

    public final ObjectProperty<EventHandler<ActionEvent>> onActionProperty() {
        return this.onAction;
    }

    public final void setOnAction(EventHandler<ActionEvent> value) {
        onActionProperty().set(value);
    }

    public final EventHandler<ActionEvent> getOnAction() {
        return onActionProperty().get();
    }

    public final ObjectProperty<EventHandler<Event>> onShowingProperty() {
        return this.onShowing;
    }

    public final void setOnShowing(EventHandler<Event> value) {
        onShowingProperty().set(value);
    }

    public final EventHandler<Event> getOnShowing() {
        return onShowingProperty().get();
    }

    public final ObjectProperty<EventHandler<Event>> onShownProperty() {
        return this.onShown;
    }

    public final void setOnShown(EventHandler<Event> value) {
        onShownProperty().set(value);
    }

    public final EventHandler<Event> getOnShown() {
        return onShownProperty().get();
    }

    public final ObjectProperty<EventHandler<Event>> onHidingProperty() {
        return this.onHiding;
    }

    public final void setOnHiding(EventHandler<Event> value) {
        onHidingProperty().set(value);
    }

    public final EventHandler<Event> getOnHiding() {
        return onHidingProperty().get();
    }

    public final ObjectProperty<EventHandler<Event>> onHiddenProperty() {
        return this.onHidden;
    }

    public final void setOnHidden(EventHandler<Event> value) {
        onHiddenProperty().set(value);
    }

    public final EventHandler<Event> getOnHidden() {
        return onHiddenProperty().get();
    }

    public void show() {
        if (!isDisabled()) {
            setShowing(true);
        }
    }

    public void hide() {
        if (isShowing()) {
            setShowing(false);
        }
    }

    public void arm() {
        if (!armedProperty().isBound()) {
            setArmed(true);
        }
    }

    public void disarm() {
        if (!armedProperty().isBound()) {
            setArmed(false);
        }
    }

    @Override // javafx.scene.control.Control, javafx.scene.Parent, javafx.scene.Node
    public Object queryAccessibleAttribute(AccessibleAttribute attribute, Object... parameters) {
        switch (attribute) {
            case EXPANDED:
                return Boolean.valueOf(isShowing());
            case EDITABLE:
                return Boolean.valueOf(isEditable());
            default:
                return super.queryAccessibleAttribute(attribute, parameters);
        }
    }

    @Override // javafx.scene.control.Control, javafx.scene.Node
    public void executeAccessibleAction(AccessibleAction action, Object... parameters) {
        switch (action) {
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
