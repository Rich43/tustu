package javafx.scene.control.cell;

import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;
import javafx.util.StringConverter;

/* loaded from: jfxrt.jar:javafx/scene/control/cell/CheckBoxTableCell.class */
public class CheckBoxTableCell<S, T> extends TableCell<S, T> {
    private final CheckBox checkBox;
    private boolean showLabel;
    private ObservableValue<Boolean> booleanProperty;
    private ObjectProperty<StringConverter<T>> converter;
    private ObjectProperty<Callback<Integer, ObservableValue<Boolean>>> selectedStateCallback;

    public static <S> Callback<TableColumn<S, Boolean>, TableCell<S, Boolean>> forTableColumn(TableColumn<S, Boolean> column) {
        return forTableColumn((Callback<Integer, ObservableValue<Boolean>>) null, (StringConverter) null);
    }

    public static <S, T> Callback<TableColumn<S, T>, TableCell<S, T>> forTableColumn(Callback<Integer, ObservableValue<Boolean>> getSelectedProperty) {
        return forTableColumn(getSelectedProperty, (StringConverter) null);
    }

    public static <S, T> Callback<TableColumn<S, T>, TableCell<S, T>> forTableColumn(Callback<Integer, ObservableValue<Boolean>> getSelectedProperty, boolean showLabel) {
        StringConverter<T> converter = !showLabel ? null : CellUtils.defaultStringConverter();
        return forTableColumn(getSelectedProperty, converter);
    }

    public static <S, T> Callback<TableColumn<S, T>, TableCell<S, T>> forTableColumn(Callback<Integer, ObservableValue<Boolean>> getSelectedProperty, StringConverter<T> converter) {
        return list -> {
            return new CheckBoxTableCell(getSelectedProperty, converter);
        };
    }

    public CheckBoxTableCell() {
        this(null, null);
    }

    public CheckBoxTableCell(Callback<Integer, ObservableValue<Boolean>> getSelectedProperty) {
        this(getSelectedProperty, null);
    }

    public CheckBoxTableCell(Callback<Integer, ObservableValue<Boolean>> getSelectedProperty, StringConverter<T> converter) {
        this.converter = new SimpleObjectProperty<StringConverter<T>>(this, "converter") { // from class: javafx.scene.control.cell.CheckBoxTableCell.1
            @Override // javafx.beans.property.ObjectPropertyBase
            protected void invalidated() {
                CheckBoxTableCell.this.updateShowLabel();
            }
        };
        this.selectedStateCallback = new SimpleObjectProperty(this, "selectedStateCallback");
        getStyleClass().add("check-box-table-cell");
        this.checkBox = new CheckBox();
        setGraphic(null);
        setSelectedStateCallback(getSelectedProperty);
        setConverter(converter);
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

    public final ObjectProperty<Callback<Integer, ObservableValue<Boolean>>> selectedStateCallbackProperty() {
        return this.selectedStateCallback;
    }

    public final void setSelectedStateCallback(Callback<Integer, ObservableValue<Boolean>> value) {
        selectedStateCallbackProperty().set(value);
    }

    public final Callback<Integer, ObservableValue<Boolean>> getSelectedStateCallback() {
        return selectedStateCallbackProperty().get();
    }

    @Override // javafx.scene.control.Cell
    public void updateItem(T item, boolean empty) {
        super.updateItem(item, empty);
        if (empty) {
            setText(null);
            setGraphic(null);
            return;
        }
        StringConverter<T> c2 = getConverter();
        if (this.showLabel) {
            setText(c2.toString(item));
        }
        setGraphic(this.checkBox);
        if (this.booleanProperty instanceof BooleanProperty) {
            this.checkBox.selectedProperty().unbindBidirectional((BooleanProperty) this.booleanProperty);
        }
        ObservableValue selectedProperty = getSelectedProperty();
        if (selectedProperty instanceof BooleanProperty) {
            this.booleanProperty = selectedProperty;
            this.checkBox.selectedProperty().bindBidirectional((BooleanProperty) this.booleanProperty);
        }
        this.checkBox.disableProperty().bind(Bindings.not(getTableView().editableProperty().and(getTableColumn().editableProperty()).and(editableProperty())));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateShowLabel() {
        this.showLabel = this.converter != null;
        this.checkBox.setAlignment(this.showLabel ? Pos.CENTER_LEFT : Pos.CENTER);
    }

    private ObservableValue<?> getSelectedProperty() {
        if (getSelectedStateCallback() != null) {
            return getSelectedStateCallback().call(Integer.valueOf(getIndex()));
        }
        return getTableColumn().getCellObservableValue(getIndex());
    }
}
