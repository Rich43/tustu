package javafx.scene.control;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.css.PseudoClass;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.AccessibleAction;
import javafx.scene.Node;

/* loaded from: jfxrt.jar:javafx/scene/control/ButtonBase.class */
public abstract class ButtonBase extends Labeled {
    private ReadOnlyBooleanWrapper armed;
    private ObjectProperty<EventHandler<ActionEvent>> onAction;
    private static final PseudoClass ARMED_PSEUDOCLASS_STATE = PseudoClass.getPseudoClass("armed");

    public abstract void fire();

    public ButtonBase() {
        this.armed = new ReadOnlyBooleanWrapper() { // from class: javafx.scene.control.ButtonBase.1
            @Override // javafx.beans.property.BooleanPropertyBase
            protected void invalidated() {
                ButtonBase.this.pseudoClassStateChanged(ButtonBase.ARMED_PSEUDOCLASS_STATE, get());
            }

            @Override // javafx.beans.property.SimpleBooleanProperty, javafx.beans.property.ReadOnlyProperty
            public Object getBean() {
                return ButtonBase.this;
            }

            @Override // javafx.beans.property.SimpleBooleanProperty, javafx.beans.property.ReadOnlyProperty
            public String getName() {
                return "armed";
            }
        };
        this.onAction = new ObjectPropertyBase<EventHandler<ActionEvent>>() { // from class: javafx.scene.control.ButtonBase.2
            @Override // javafx.beans.property.ObjectPropertyBase
            protected void invalidated() {
                ButtonBase.this.setEventHandler(ActionEvent.ACTION, get());
            }

            @Override // javafx.beans.property.ReadOnlyProperty
            public Object getBean() {
                return ButtonBase.this;
            }

            @Override // javafx.beans.property.ReadOnlyProperty
            public String getName() {
                return "onAction";
            }
        };
    }

    public ButtonBase(String text) {
        super(text);
        this.armed = new ReadOnlyBooleanWrapper() { // from class: javafx.scene.control.ButtonBase.1
            @Override // javafx.beans.property.BooleanPropertyBase
            protected void invalidated() {
                ButtonBase.this.pseudoClassStateChanged(ButtonBase.ARMED_PSEUDOCLASS_STATE, get());
            }

            @Override // javafx.beans.property.SimpleBooleanProperty, javafx.beans.property.ReadOnlyProperty
            public Object getBean() {
                return ButtonBase.this;
            }

            @Override // javafx.beans.property.SimpleBooleanProperty, javafx.beans.property.ReadOnlyProperty
            public String getName() {
                return "armed";
            }
        };
        this.onAction = new ObjectPropertyBase<EventHandler<ActionEvent>>() { // from class: javafx.scene.control.ButtonBase.2
            @Override // javafx.beans.property.ObjectPropertyBase
            protected void invalidated() {
                ButtonBase.this.setEventHandler(ActionEvent.ACTION, get());
            }

            @Override // javafx.beans.property.ReadOnlyProperty
            public Object getBean() {
                return ButtonBase.this;
            }

            @Override // javafx.beans.property.ReadOnlyProperty
            public String getName() {
                return "onAction";
            }
        };
    }

    public ButtonBase(String text, Node graphic) {
        super(text, graphic);
        this.armed = new ReadOnlyBooleanWrapper() { // from class: javafx.scene.control.ButtonBase.1
            @Override // javafx.beans.property.BooleanPropertyBase
            protected void invalidated() {
                ButtonBase.this.pseudoClassStateChanged(ButtonBase.ARMED_PSEUDOCLASS_STATE, get());
            }

            @Override // javafx.beans.property.SimpleBooleanProperty, javafx.beans.property.ReadOnlyProperty
            public Object getBean() {
                return ButtonBase.this;
            }

            @Override // javafx.beans.property.SimpleBooleanProperty, javafx.beans.property.ReadOnlyProperty
            public String getName() {
                return "armed";
            }
        };
        this.onAction = new ObjectPropertyBase<EventHandler<ActionEvent>>() { // from class: javafx.scene.control.ButtonBase.2
            @Override // javafx.beans.property.ObjectPropertyBase
            protected void invalidated() {
                ButtonBase.this.setEventHandler(ActionEvent.ACTION, get());
            }

            @Override // javafx.beans.property.ReadOnlyProperty
            public Object getBean() {
                return ButtonBase.this;
            }

            @Override // javafx.beans.property.ReadOnlyProperty
            public String getName() {
                return "onAction";
            }
        };
    }

    public final ReadOnlyBooleanProperty armedProperty() {
        return this.armed.getReadOnlyProperty();
    }

    private void setArmed(boolean value) {
        this.armed.set(value);
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

    public void arm() {
        setArmed(true);
    }

    public void disarm() {
        setArmed(false);
    }

    @Override // javafx.scene.control.Control, javafx.scene.Node
    public void executeAccessibleAction(AccessibleAction action, Object... parameters) {
        switch (action) {
            case FIRE:
                fire();
                break;
            default:
                super.executeAccessibleAction(action, new Object[0]);
                break;
        }
    }
}
