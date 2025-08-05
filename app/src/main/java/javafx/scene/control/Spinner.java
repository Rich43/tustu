package javafx.scene.control;

import com.sun.javafx.scene.control.skin.ComboBoxPopupControl;
import com.sun.javafx.scene.control.skin.SpinnerSkin;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.TemporalUnit;
import javafx.beans.NamedArg;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.scene.AccessibleAction;
import javafx.scene.AccessibleAttribute;
import javafx.scene.AccessibleRole;
import javafx.scene.control.SpinnerValueFactory;
import javafx.util.StringConverter;
import javax.swing.JOptionPane;
import javax.swing.JTree;

/* loaded from: jfxrt.jar:javafx/scene/control/Spinner.class */
public class Spinner<T> extends Control {
    private static final String DEFAULT_STYLE_CLASS = "spinner";
    public static final String STYLE_CLASS_ARROWS_ON_RIGHT_HORIZONTAL = "arrows-on-right-horizontal";
    public static final String STYLE_CLASS_ARROWS_ON_LEFT_VERTICAL = "arrows-on-left-vertical";
    public static final String STYLE_CLASS_ARROWS_ON_LEFT_HORIZONTAL = "arrows-on-left-horizontal";
    public static final String STYLE_CLASS_SPLIT_ARROWS_VERTICAL = "split-arrows-vertical";
    public static final String STYLE_CLASS_SPLIT_ARROWS_HORIZONTAL = "split-arrows-horizontal";
    private ReadOnlyObjectWrapper<T> value;
    private ObjectProperty<SpinnerValueFactory<T>> valueFactory;
    private BooleanProperty editable;
    private TextField textField;
    private ReadOnlyObjectWrapper<TextField> editor;

    public Spinner() {
        this.value = new ReadOnlyObjectWrapper<>(this, "value");
        this.valueFactory = new SimpleObjectProperty<SpinnerValueFactory<T>>(this, "valueFactory") { // from class: javafx.scene.control.Spinner.1
            @Override // javafx.beans.property.ObjectPropertyBase
            protected void invalidated() {
                Spinner.this.value.unbind();
                SpinnerValueFactory<T> newFactory = get();
                if (newFactory != null) {
                    Spinner.this.value.bind(newFactory.valueProperty());
                }
            }
        };
        getStyleClass().add(DEFAULT_STYLE_CLASS);
        setAccessibleRole(AccessibleRole.SPINNER);
        getEditor().setOnAction(action -> {
            StringConverter<T> converter;
            String text = getEditor().getText();
            SpinnerValueFactory<T> valueFactory = getValueFactory();
            if (valueFactory != null && (converter = valueFactory.getConverter()) != null) {
                T value = converter.fromString(text);
                valueFactory.setValue(value);
            }
        });
        getEditor().editableProperty().bind(editableProperty());
        this.value.addListener((o2, oldValue, obj) -> {
            setText(obj);
        });
        getProperties().addListener(change -> {
            if (change.wasAdded() && change.getKey() == "FOCUSED") {
                setFocused(((Boolean) change.getValueAdded()).booleanValue());
                getProperties().remove("FOCUSED");
            }
        });
    }

    public Spinner(@NamedArg("min") int min, @NamedArg("max") int max, @NamedArg(JOptionPane.INITIAL_VALUE_PROPERTY) int initialValue) {
        this(new SpinnerValueFactory.IntegerSpinnerValueFactory(min, max, initialValue));
    }

    public Spinner(@NamedArg("min") int min, @NamedArg("max") int max, @NamedArg(JOptionPane.INITIAL_VALUE_PROPERTY) int initialValue, @NamedArg("amountToStepBy") int amountToStepBy) {
        this(new SpinnerValueFactory.IntegerSpinnerValueFactory(min, max, initialValue, amountToStepBy));
    }

    public Spinner(@NamedArg("min") double min, @NamedArg("max") double max, @NamedArg(JOptionPane.INITIAL_VALUE_PROPERTY) double initialValue) {
        this(new SpinnerValueFactory.DoubleSpinnerValueFactory(min, max, initialValue));
    }

    public Spinner(@NamedArg("min") double min, @NamedArg("max") double max, @NamedArg(JOptionPane.INITIAL_VALUE_PROPERTY) double initialValue, @NamedArg("amountToStepBy") double amountToStepBy) {
        this(new SpinnerValueFactory.DoubleSpinnerValueFactory(min, max, initialValue, amountToStepBy));
    }

    Spinner(@NamedArg("min") LocalDate min, @NamedArg("max") LocalDate max, @NamedArg(JOptionPane.INITIAL_VALUE_PROPERTY) LocalDate initialValue) {
        this(new SpinnerValueFactory.LocalDateSpinnerValueFactory(min, max, initialValue));
    }

    Spinner(@NamedArg("min") LocalDate min, @NamedArg("max") LocalDate max, @NamedArg(JOptionPane.INITIAL_VALUE_PROPERTY) LocalDate initialValue, @NamedArg("amountToStepBy") long amountToStepBy, @NamedArg("temporalUnit") TemporalUnit temporalUnit) {
        this(new SpinnerValueFactory.LocalDateSpinnerValueFactory(min, max, initialValue, amountToStepBy, temporalUnit));
    }

    Spinner(@NamedArg("min") LocalTime min, @NamedArg("max") LocalTime max, @NamedArg(JOptionPane.INITIAL_VALUE_PROPERTY) LocalTime initialValue) {
        this(new SpinnerValueFactory.LocalTimeSpinnerValueFactory(min, max, initialValue));
    }

    Spinner(@NamedArg("min") LocalTime min, @NamedArg("max") LocalTime max, @NamedArg(JOptionPane.INITIAL_VALUE_PROPERTY) LocalTime initialValue, @NamedArg("amountToStepBy") long amountToStepBy, @NamedArg("temporalUnit") TemporalUnit temporalUnit) {
        this(new SpinnerValueFactory.LocalTimeSpinnerValueFactory(min, max, initialValue, amountToStepBy, temporalUnit));
    }

    public Spinner(@NamedArg("items") ObservableList<T> items) {
        this(new SpinnerValueFactory.ListSpinnerValueFactory(items));
    }

    public Spinner(@NamedArg("valueFactory") SpinnerValueFactory<T> valueFactory) {
        this();
        setValueFactory(valueFactory);
    }

    public void increment() {
        increment(1);
    }

    public void increment(int steps) {
        SpinnerValueFactory<T> valueFactory = getValueFactory();
        if (valueFactory == null) {
            throw new IllegalStateException("Can't increment Spinner with a null SpinnerValueFactory");
        }
        commitEditorText();
        valueFactory.increment(steps);
    }

    public void decrement() {
        decrement(1);
    }

    public void decrement(int steps) {
        SpinnerValueFactory<T> valueFactory = getValueFactory();
        if (valueFactory == null) {
            throw new IllegalStateException("Can't decrement Spinner with a null SpinnerValueFactory");
        }
        commitEditorText();
        valueFactory.decrement(steps);
    }

    @Override // javafx.scene.control.Control
    protected Skin<?> createDefaultSkin() {
        return new SpinnerSkin(this);
    }

    public final T getValue() {
        return this.value.get();
    }

    public final ReadOnlyObjectProperty<T> valueProperty() {
        return this.value;
    }

    public final void setValueFactory(SpinnerValueFactory<T> value) {
        this.valueFactory.setValue(value);
    }

    public final SpinnerValueFactory<T> getValueFactory() {
        return this.valueFactory.get();
    }

    public final ObjectProperty<SpinnerValueFactory<T>> valueFactoryProperty() {
        return this.valueFactory;
    }

    public final void setEditable(boolean value) {
        editableProperty().set(value);
    }

    public final boolean isEditable() {
        if (this.editable == null) {
            return true;
        }
        return this.editable.get();
    }

    public final BooleanProperty editableProperty() {
        if (this.editable == null) {
            this.editable = new SimpleBooleanProperty(this, JTree.EDITABLE_PROPERTY, false);
        }
        return this.editable;
    }

    public final ReadOnlyObjectProperty<TextField> editorProperty() {
        if (this.editor == null) {
            this.editor = new ReadOnlyObjectWrapper<>(this, "editor");
            this.textField = new ComboBoxPopupControl.FakeFocusTextField();
            this.editor.set(this.textField);
        }
        return this.editor.getReadOnlyProperty();
    }

    public final TextField getEditor() {
        return editorProperty().get();
    }

    private void setText(T value) {
        StringConverter<T> converter;
        String text = null;
        SpinnerValueFactory<T> valueFactory = getValueFactory();
        if (valueFactory != null && (converter = valueFactory.getConverter()) != null) {
            text = converter.toString(value);
        }
        notifyAccessibleAttributeChanged(AccessibleAttribute.TEXT);
        if (text == null) {
            if (value == null) {
                getEditor().clear();
                return;
            }
            text = value.toString();
        }
        getEditor().setText(text);
    }

    static int wrapValue(int value, int min, int max) {
        if (max == 0) {
            throw new RuntimeException();
        }
        int r2 = value % max;
        if (r2 > min && max < min) {
            r2 = (r2 + max) - min;
        } else if (r2 < min && max > min) {
            r2 = (r2 + max) - min;
        }
        return r2;
    }

    static BigDecimal wrapValue(BigDecimal value, BigDecimal min, BigDecimal max) {
        if (max.doubleValue() == 0.0d) {
            throw new RuntimeException();
        }
        if (value.compareTo(min) < 0) {
            return max;
        }
        if (value.compareTo(max) > 0) {
            return min;
        }
        return value;
    }

    private void commitEditorText() {
        StringConverter<T> converter;
        if (isEditable()) {
            String text = getEditor().getText();
            SpinnerValueFactory<T> valueFactory = getValueFactory();
            if (valueFactory != null && (converter = valueFactory.getConverter()) != null) {
                T value = converter.fromString(text);
                valueFactory.setValue(value);
            }
        }
    }

    @Override // javafx.scene.control.Control, javafx.scene.Parent, javafx.scene.Node
    public Object queryAccessibleAttribute(AccessibleAttribute attribute, Object... parameters) {
        StringConverter<T> converter;
        switch (attribute) {
            case TEXT:
                T value = getValue();
                SpinnerValueFactory<T> factory = getValueFactory();
                if (factory == null || (converter = factory.getConverter()) == null) {
                    return value != null ? value.toString() : "";
                }
                return converter.toString(value);
            default:
                return super.queryAccessibleAttribute(attribute, parameters);
        }
    }

    @Override // javafx.scene.control.Control, javafx.scene.Node
    public void executeAccessibleAction(AccessibleAction action, Object... parameters) {
        switch (action) {
            case INCREMENT:
                increment();
                break;
            case DECREMENT:
                decrement();
                break;
            default:
                super.executeAccessibleAction(action, new Object[0]);
                break;
        }
    }
}
