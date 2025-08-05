package javafx.scene.control.cell;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;
import javafx.util.StringConverter;

/* loaded from: jfxrt.jar:javafx/scene/control/cell/CheckBoxListCell.class */
public class CheckBoxListCell<T> extends ListCell<T> {
    private final CheckBox checkBox;
    private ObservableValue<Boolean> booleanProperty;
    private ObjectProperty<StringConverter<T>> converter;
    private ObjectProperty<Callback<T, ObservableValue<Boolean>>> selectedStateCallback;

    public static <T> Callback<ListView<T>, ListCell<T>> forListView(Callback<T, ObservableValue<Boolean>> getSelectedProperty) {
        return forListView(getSelectedProperty, CellUtils.defaultStringConverter());
    }

    public static <T> Callback<ListView<T>, ListCell<T>> forListView(Callback<T, ObservableValue<Boolean>> getSelectedProperty, StringConverter<T> converter) {
        return list -> {
            return new CheckBoxListCell(getSelectedProperty, converter);
        };
    }

    public CheckBoxListCell() {
        this(null);
    }

    public CheckBoxListCell(Callback<T, ObservableValue<Boolean>> getSelectedProperty) {
        this(getSelectedProperty, CellUtils.defaultStringConverter());
    }

    public CheckBoxListCell(Callback<T, ObservableValue<Boolean>> getSelectedProperty, StringConverter<T> converter) {
        this.converter = new SimpleObjectProperty(this, "converter");
        this.selectedStateCallback = new SimpleObjectProperty(this, "selectedStateCallback");
        getStyleClass().add("check-box-list-cell");
        setSelectedStateCallback(getSelectedProperty);
        setConverter(converter);
        this.checkBox = new CheckBox();
        setAlignment(Pos.CENTER_LEFT);
        setContentDisplay(ContentDisplay.LEFT);
        setGraphic(null);
    }

    public final ObjectProperty<StringConverter<T>> converterProperty() {
        return this.converter;
    }

    public final void setConverter(StringConverter<T> value) {
        converterProperty().set(value);
    }

    public final StringConverter<T> getConverter() {
        return converterProperty().get();
    }

    public final ObjectProperty<Callback<T, ObservableValue<Boolean>>> selectedStateCallbackProperty() {
        return this.selectedStateCallback;
    }

    public final void setSelectedStateCallback(Callback<T, ObservableValue<Boolean>> value) {
        selectedStateCallbackProperty().set(value);
    }

    public final Callback<T, ObservableValue<Boolean>> getSelectedStateCallback() {
        return selectedStateCallbackProperty().get();
    }

    @Override // javafx.scene.control.Cell
    public void updateItem(T item, boolean empty) {
        super.updateItem(item, empty);
        if (!empty) {
            StringConverter<T> c2 = getConverter();
            Callback<T, ObservableValue<Boolean>> callback = getSelectedStateCallback();
            if (callback == null) {
                throw new NullPointerException("The CheckBoxListCell selectedStateCallbackProperty can not be null");
            }
            setGraphic(this.checkBox);
            setText(c2 != null ? c2.toString(item) : item == null ? "" : item.toString());
            if (this.booleanProperty != null) {
                this.checkBox.selectedProperty().unbindBidirectional((BooleanProperty) this.booleanProperty);
            }
            this.booleanProperty = callback.call(item);
            if (this.booleanProperty != null) {
                this.checkBox.selectedProperty().bindBidirectional((BooleanProperty) this.booleanProperty);
                return;
            }
            return;
        }
        setGraphic(null);
        setText(null);
    }
}
