package javafx.scene.control;

import com.sun.javafx.binding.ExpressionHelper;
import com.sun.javafx.css.converters.EnumConverter;
import com.sun.javafx.css.converters.SizeConverter;
import com.sun.javafx.scene.control.skin.TextFieldSkin;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javafx.beans.InvalidationListener;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.beans.value.ChangeListener;
import javafx.css.CssMetaData;
import javafx.css.Styleable;
import javafx.css.StyleableIntegerProperty;
import javafx.css.StyleableObjectProperty;
import javafx.css.StyleableProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.AccessibleRole;
import javafx.scene.control.TextInputControl;

/* loaded from: jfxrt.jar:javafx/scene/control/TextField.class */
public class TextField extends TextInputControl {
    public static final int DEFAULT_PREF_COLUMN_COUNT = 12;
    private IntegerProperty prefColumnCount;
    private ObjectProperty<EventHandler<ActionEvent>> onAction;
    private ObjectProperty<Pos> alignment;

    /* loaded from: jfxrt.jar:javafx/scene/control/TextField$TextFieldContent.class */
    private static final class TextFieldContent implements TextInputControl.Content {
        private ExpressionHelper<String> helper;
        private StringBuilder characters;

        private TextFieldContent() {
            this.helper = null;
            this.characters = new StringBuilder();
        }

        @Override // javafx.scene.control.TextInputControl.Content
        public String get(int start, int end) {
            return this.characters.substring(start, end);
        }

        @Override // javafx.scene.control.TextInputControl.Content
        public void insert(int index, String text, boolean notifyListeners) {
            String text2 = TextInputControl.filterInput(text, true, true);
            if (!text2.isEmpty()) {
                this.characters.insert(index, text2);
                if (notifyListeners) {
                    ExpressionHelper.fireValueChangedEvent(this.helper);
                }
            }
        }

        @Override // javafx.scene.control.TextInputControl.Content
        public void delete(int start, int end, boolean notifyListeners) {
            if (end > start) {
                this.characters.delete(start, end);
                if (notifyListeners) {
                    ExpressionHelper.fireValueChangedEvent(this.helper);
                }
            }
        }

        @Override // javafx.scene.control.TextInputControl.Content
        public int length() {
            return this.characters.length();
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // javafx.beans.value.ObservableObjectValue
        public String get() {
            return this.characters.toString();
        }

        @Override // javafx.beans.value.ObservableValue
        public void addListener(ChangeListener<? super String> changeListener) {
            this.helper = ExpressionHelper.addListener(this.helper, this, changeListener);
        }

        @Override // javafx.beans.value.ObservableValue
        public void removeListener(ChangeListener<? super String> changeListener) {
            this.helper = ExpressionHelper.removeListener(this.helper, changeListener);
        }

        @Override // javafx.beans.value.ObservableValue
        /* renamed from: getValue */
        public String getValue2() {
            return get();
        }

        @Override // javafx.beans.Observable
        public void addListener(InvalidationListener listener) {
            this.helper = ExpressionHelper.addListener(this.helper, this, listener);
        }

        @Override // javafx.beans.Observable
        public void removeListener(InvalidationListener listener) {
            this.helper = ExpressionHelper.removeListener(this.helper, listener);
        }
    }

    public TextField() {
        this("");
    }

    public TextField(String text) {
        super(new TextFieldContent());
        this.prefColumnCount = new StyleableIntegerProperty(12) { // from class: javafx.scene.control.TextField.1
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

            @Override // javafx.css.StyleableProperty
            public CssMetaData<? extends Styleable, Number> getCssMetaData() {
                return StyleableProperties.PREF_COLUMN_COUNT;
            }

            @Override // javafx.beans.property.ReadOnlyProperty
            public Object getBean() {
                return TextField.this;
            }

            @Override // javafx.beans.property.ReadOnlyProperty
            public String getName() {
                return "prefColumnCount";
            }
        };
        this.onAction = new ObjectPropertyBase<EventHandler<ActionEvent>>() { // from class: javafx.scene.control.TextField.2
            @Override // javafx.beans.property.ObjectPropertyBase
            protected void invalidated() {
                TextField.this.setEventHandler(ActionEvent.ACTION, get());
            }

            @Override // javafx.beans.property.ReadOnlyProperty
            public Object getBean() {
                return TextField.this;
            }

            @Override // javafx.beans.property.ReadOnlyProperty
            public String getName() {
                return "onAction";
            }
        };
        getStyleClass().add("text-field");
        setAccessibleRole(AccessibleRole.TEXT_FIELD);
        setText(text);
    }

    public CharSequence getCharacters() {
        return ((TextFieldContent) getContent()).characters;
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

    public final ObjectProperty<Pos> alignmentProperty() {
        if (this.alignment == null) {
            this.alignment = new StyleableObjectProperty<Pos>(Pos.CENTER_LEFT) { // from class: javafx.scene.control.TextField.3
                @Override // javafx.css.StyleableProperty
                public CssMetaData<TextField, Pos> getCssMetaData() {
                    return StyleableProperties.ALIGNMENT;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return TextField.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "alignment";
                }
            };
        }
        return this.alignment;
    }

    public final void setAlignment(Pos value) {
        alignmentProperty().set(value);
    }

    public final Pos getAlignment() {
        return this.alignment == null ? Pos.CENTER_LEFT : this.alignment.get();
    }

    @Override // javafx.scene.control.Control
    protected Skin<?> createDefaultSkin() {
        return new TextFieldSkin(this);
    }

    /* loaded from: jfxrt.jar:javafx/scene/control/TextField$StyleableProperties.class */
    private static class StyleableProperties {
        private static final CssMetaData<TextField, Pos> ALIGNMENT = new CssMetaData<TextField, Pos>("-fx-alignment", new EnumConverter(Pos.class), Pos.CENTER_LEFT) { // from class: javafx.scene.control.TextField.StyleableProperties.1
            @Override // javafx.css.CssMetaData
            public boolean isSettable(TextField n2) {
                return n2.alignment == null || !n2.alignment.isBound();
            }

            @Override // javafx.css.CssMetaData
            public StyleableProperty<Pos> getStyleableProperty(TextField n2) {
                return (StyleableProperty) n2.alignmentProperty();
            }
        };
        private static final CssMetaData<TextField, Number> PREF_COLUMN_COUNT = new CssMetaData<TextField, Number>("-fx-pref-column-count", SizeConverter.getInstance(), 12) { // from class: javafx.scene.control.TextField.StyleableProperties.2
            @Override // javafx.css.CssMetaData
            public boolean isSettable(TextField n2) {
                return n2.prefColumnCount == null || !n2.prefColumnCount.isBound();
            }

            @Override // javafx.css.CssMetaData
            public StyleableProperty<Number> getStyleableProperty(TextField n2) {
                return (StyleableProperty) n2.prefColumnCountProperty();
            }
        };
        private static final List<CssMetaData<? extends Styleable, ?>> STYLEABLES;

        private StyleableProperties() {
        }

        static {
            List<CssMetaData<? extends Styleable, ?>> styleables = new ArrayList<>(TextInputControl.getClassCssMetaData());
            styleables.add(ALIGNMENT);
            styleables.add(PREF_COLUMN_COUNT);
            STYLEABLES = Collections.unmodifiableList(styleables);
        }
    }

    public static List<CssMetaData<? extends Styleable, ?>> getClassCssMetaData() {
        return StyleableProperties.STYLEABLES;
    }

    @Override // javafx.scene.control.TextInputControl, javafx.scene.control.Control
    public List<CssMetaData<? extends Styleable, ?>> getControlCssMetaData() {
        return getClassCssMetaData();
    }
}
