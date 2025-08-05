package javafx.scene.control.cell;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.TreeTableCell;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import javafx.util.StringConverter;

/* loaded from: jfxrt.jar:javafx/scene/control/cell/ComboBoxTreeTableCell.class */
public class ComboBoxTreeTableCell<S, T> extends TreeTableCell<S, T> {
    private final ObservableList<T> items;
    private ComboBox<T> comboBox;
    private ObjectProperty<StringConverter<T>> converter;
    private BooleanProperty comboBoxEditable;

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
            return new ComboBoxTreeTableCell(converter, items);
        };
    }

    public ComboBoxTreeTableCell() {
        this(FXCollections.observableArrayList());
    }

    @SafeVarargs
    public ComboBoxTreeTableCell(T... items) {
        this(FXCollections.observableArrayList(items));
    }

    @SafeVarargs
    public ComboBoxTreeTableCell(StringConverter<T> converter, T... items) {
        this(converter, FXCollections.observableArrayList(items));
    }

    public ComboBoxTreeTableCell(ObservableList<T> items) {
        this((StringConverter) null, items);
    }

    public ComboBoxTreeTableCell(StringConverter<T> converter, ObservableList<T> items) {
        this.converter = new SimpleObjectProperty(this, "converter");
        this.comboBoxEditable = new SimpleBooleanProperty(this, "comboBoxEditable");
        getStyleClass().add("combo-box-tree-table-cell");
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

    public final BooleanProperty comboBoxEditableProperty() {
        return this.comboBoxEditable;
    }

    public final void setComboBoxEditable(boolean value) {
        comboBoxEditableProperty().set(value);
    }

    public final boolean isComboBoxEditable() {
        return comboBoxEditableProperty().get();
    }

    public ObservableList<T> getItems() {
        return this.items;
    }

    @Override // javafx.scene.control.TreeTableCell, javafx.scene.control.Cell
    public void startEdit() {
        if (!isEditable() || !getTreeTableView().isEditable() || !getTableColumn().isEditable()) {
            return;
        }
        if (this.comboBox == null) {
            this.comboBox = CellUtils.createComboBox(this, this.items, converterProperty());
            this.comboBox.editableProperty().bind(comboBoxEditableProperty());
        }
        this.comboBox.getSelectionModel().select((SingleSelectionModel<T>) getItem());
        super.startEdit();
        setText(null);
        setGraphic(this.comboBox);
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
        CellUtils.updateItem(this, getConverter(), (HBox) null, (Node) null, this.comboBox);
    }
}
