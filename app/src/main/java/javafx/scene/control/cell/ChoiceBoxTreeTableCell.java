package javafx.scene.control.cell;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.TreeTableCell;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import javafx.util.StringConverter;

/* loaded from: jfxrt.jar:javafx/scene/control/cell/ChoiceBoxTreeTableCell.class */
public class ChoiceBoxTreeTableCell<S, T> extends TreeTableCell<S, T> {
    private final ObservableList<T> items;
    private ChoiceBox<T> choiceBox;
    private ObjectProperty<StringConverter<T>> converter;

    @SafeVarargs
    public static <S, T> Callback<TreeTableColumn<S, T>, TreeTableCell<S, T>> forTreeTableColumn(T... items) {
        return forTreeTableColumn((StringConverter) null, items);
    }

    @SafeVarargs
    public static <S, T> Callback<TreeTableColumn<S, T>, TreeTableCell<S, T>> forTreeTableColumn(StringConverter<T> converter, T... items) {
        return forTreeTableColumn(converter, FXCollections.observableArrayList(items));
    }

    public static <S, T> Callback<TreeTableColumn<S, T>, TreeTableCell<S, T>> forTreeTableColumn(ObservableList<T> items) {
        return forTreeTableColumn((StringConverter) null, items);
    }

    public static <S, T> Callback<TreeTableColumn<S, T>, TreeTableCell<S, T>> forTreeTableColumn(StringConverter<T> converter, ObservableList<T> items) {
        return list -> {
            return new ChoiceBoxTreeTableCell(converter, items);
        };
    }

    public ChoiceBoxTreeTableCell() {
        this(FXCollections.observableArrayList());
    }

    @SafeVarargs
    public ChoiceBoxTreeTableCell(T... items) {
        this(FXCollections.observableArrayList(items));
    }

    @SafeVarargs
    public ChoiceBoxTreeTableCell(StringConverter<T> converter, T... items) {
        this(converter, FXCollections.observableArrayList(items));
    }

    public ChoiceBoxTreeTableCell(ObservableList<T> items) {
        this((StringConverter) null, items);
    }

    public ChoiceBoxTreeTableCell(StringConverter<T> converter, ObservableList<T> items) {
        this.converter = new SimpleObjectProperty(this, "converter");
        getStyleClass().add("choice-box-tree-table-cell");
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

    @Override // javafx.scene.control.TreeTableCell, javafx.scene.control.Cell
    public void startEdit() {
        if (!isEditable() || !getTreeTableView().isEditable() || !getTableColumn().isEditable()) {
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

    @Override // javafx.scene.control.TreeTableCell, javafx.scene.control.Cell
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
