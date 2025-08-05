package com.sun.javafx.scene.control.skin;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.IntegerPropertyBase;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.StringPropertyBase;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Control;
import javax.swing.JTree;

/* loaded from: jfxrt.jar:com/sun/javafx/scene/control/skin/InputField.class */
abstract class InputField extends Control {
    public static final int DEFAULT_PREF_COLUMN_COUNT = 12;
    private BooleanProperty editable = new SimpleBooleanProperty(this, JTree.EDITABLE_PROPERTY, true);
    private StringProperty promptText = new StringPropertyBase("") { // from class: com.sun.javafx.scene.control.skin.InputField.1
        @Override // javafx.beans.property.StringPropertyBase
        protected void invalidated() {
            String txt = get();
            if (txt != null && txt.contains("\n")) {
                set(txt.replace("\n", ""));
            }
        }

        @Override // javafx.beans.property.ReadOnlyProperty
        public Object getBean() {
            return InputField.this;
        }

        @Override // javafx.beans.property.ReadOnlyProperty
        public String getName() {
            return "promptText";
        }
    };
    private IntegerProperty prefColumnCount = new IntegerPropertyBase(12) { // from class: com.sun.javafx.scene.control.skin.InputField.2
        private int oldValue = get();

        @Override // javafx.beans.property.IntegerPropertyBase
        protected void invalidated() {
            int value = get();
            if (value < 0) {
                if (isBound()) {
                    unbind();
                }
                set(this.oldValue);
                throw new IllegalArgumentException("value cannot be negative.");
            }
            this.oldValue = value;
        }

        @Override // javafx.beans.property.ReadOnlyProperty
        public Object getBean() {
            return InputField.this;
        }

        @Override // javafx.beans.property.ReadOnlyProperty
        public String getName() {
            return "prefColumnCount";
        }
    };
    private ObjectProperty<EventHandler<ActionEvent>> onAction = new ObjectPropertyBase<EventHandler<ActionEvent>>() { // from class: com.sun.javafx.scene.control.skin.InputField.3
        @Override // javafx.beans.property.ObjectPropertyBase
        protected void invalidated() {
            InputField.this.setEventHandler(ActionEvent.ACTION, get());
        }

        @Override // javafx.beans.property.ReadOnlyProperty
        public Object getBean() {
            return InputField.this;
        }

        @Override // javafx.beans.property.ReadOnlyProperty
        public String getName() {
            return "onAction";
        }
    };

    public final boolean isEditable() {
        return this.editable.getValue2().booleanValue();
    }

    public final void setEditable(boolean value) {
        this.editable.setValue(Boolean.valueOf(value));
    }

    public final BooleanProperty editableProperty() {
        return this.editable;
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

    public final IntegerProperty prefColumnCountProperty() {
        return this.prefColumnCount;
    }

    public final int getPrefColumnCount() {
        return this.prefColumnCount.getValue2().intValue();
    }

    public final void setPrefColumnCount(int value) {
        this.prefColumnCount.setValue((Number) Integer.valueOf(value));
    }

    public final ObjectProperty<EventHandler<ActionEvent>> onActionProperty() {
        return this.onAction;
    }

    public final EventHandler<ActionEvent> getOnAction() {
        return onActionProperty().get();
    }

    public final void setOnAction(EventHandler<ActionEvent> value) {
        onActionProperty().set(value);
    }

    public InputField() {
        getStyleClass().setAll("input-field");
    }
}
