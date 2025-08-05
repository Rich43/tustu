package javafx.scene.control.cell;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import javafx.util.StringConverter;

/* loaded from: jfxrt.jar:javafx/scene/control/cell/ChoiceBoxTableCell.class */
public class ChoiceBoxTableCell<S, T> extends TableCell<S, T> {
    private final ObservableList<T> items;
    private ChoiceBox<T> choiceBox;
    private ObjectProperty<StringConverter<T>> converter;

    @SafeVarargs
    public static <S, T> Callback<TableColumn<S, T>, TableCell<S, T>> forTableColumn(T... items) {
        return forTableColumn((StringConverter) null, items);
    }

    @SafeVarargs
    public static <S, T> Callback<TableColumn<S, T>, TableCell<S, T>> forTableColumn(StringConverter<T> converter, T... items) {
        return forTableColumn(converter, FXCollections.observableArrayList(items));
    }

    public static <S, T> Callback<TableColumn<S, T>, TableCell<S, T>> forTableColumn(ObservableList<T> items) {
        return forTableColumn((StringConverter) null, items);
    }

    public static <S, T> Callback<TableColumn<S, T>, TableCell<S, T>> forTableColumn(StringConverter<T> converter, ObservableList<T> items) {
        return list -> {
            return new ChoiceBoxTableCell(converter, items);
        };
    }

    public ChoiceBoxTableCell() {
        this(FXCollections.observableArrayList());
    }

    @SafeVarargs
    public ChoiceBoxTableCell(T... items) {
        this(FXCollections.observableArrayList(items));
    }

    @SafeVarargs
    public ChoiceBoxTableCell(StringConverter<T> converter, T... items) {
        this(converter, FXCollections.observableArrayList(items));
    }

    public ChoiceBoxTableCell(ObservableList<T> items) {
        this((StringConverter) null, items);
    }

    public ChoiceBoxTableCell(StringConverter<T> converter, ObservableList<T> items) {
        this.converter = new SimpleObjectProperty(this, "converter");
        getStyleClass().add("choice-box-table-cell");
        this.items = items;
        setConverter(converter != null ? converter : CellUtils.defaultStringConverter());
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

    public ObservableList<T> getItems() {
        return this.items;
    }

    @Override // javafx.scene.control.TableCell, javafx.scene.control.Cell
    public void startEdit() {
        if (!isEditable() || !getTableView().isEditable() || !getTableColumn().isEditable()) {
            return;
        }
        if (this.choiceBox == null) {
            this.choiceBox = CellUtils.createChoiceBox(this, this.items, converterProperty());
        }
        this.choiceBox.getSelectionModel().select((SingleSelectionModel<T>) getItem());
        super.startEdit();
        setText(null);
        setGraphic(this.choiceBox);
    }

    @Override // javafx.scene.control.TableCell, javafx.scene.control.Cell
    public void cancelEdit() {
        super.cancelEdit();
        setText(getConverter().toString(getItem()));
        setGraphic(null);
    }

    @Override // javafx.scene.control.Cell
    public void updateItem(T item, boolean empty) {
        super.updateItem(item, empty);
        CellUtils.updateItem(this, getConverter(), (HBox) null, (Node) null, this.choiceBox);
    }
}
