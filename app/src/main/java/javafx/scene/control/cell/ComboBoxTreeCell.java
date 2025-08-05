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
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import javafx.util.StringConverter;

/* loaded from: jfxrt.jar:javafx/scene/control/cell/ComboBoxTreeCell.class */
public class ComboBoxTreeCell<T> extends DefaultTreeCell<T> {
    private final ObservableList<T> items;
    private ComboBox<T> comboBox;
    private HBox hbox;
    private ObjectProperty<StringConverter<T>> converter;
    private BooleanProperty comboBoxEditable;

    @SafeVarargs
    public static <T> Callback<TreeView<T>, TreeCell<T>> forTreeView(T... items) {
        return forTreeView(FXCollections.observableArrayList(items));
    }

    public static <T> Callback<TreeView<T>, TreeCell<T>> forTreeView(ObservableList<T> items) {
        return forTreeView((StringConverter) null, items);
    }

    @SafeVarargs
    public static <T> Callback<TreeView<T>, TreeCell<T>> forTreeView(StringConverter<T> converter, T... items) {
        return forTreeView(converter, FXCollections.observableArrayList(items));
    }

    public static <T> Callback<TreeView<T>, TreeCell<T>> forTreeView(StringConverter<T> converter, ObservableList<T> items) {
        return list -> {
            return new ComboBoxTreeCell(converter, items);
        };
    }

    public ComboBoxTreeCell() {
        this(FXCollections.observableArrayList());
    }

    @SafeVarargs
    public ComboBoxTreeCell(T... items) {
        this(FXCollections.observableArrayList(items));
    }

    @SafeVarargs
    public ComboBoxTreeCell(StringConverter<T> converter, T... items) {
        this(converter, FXCollections.observableArrayList(items));
    }

    public ComboBoxTreeCell(ObservableList<T> items) {
        this((StringConverter) null, items);
    }

    public ComboBoxTreeCell(StringConverter<T> converter, ObservableList<T> items) {
        this.converter = new SimpleObjectProperty(this, "converter");
        this.comboBoxEditable = new SimpleBooleanProperty(this, "comboBoxEditable");
        getStyleClass().add("combo-box-tree-cell");
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

    @Override // javafx.scene.control.TreeCell, javafx.scene.control.Cell
    public void startEdit() {
        TreeItem<T> treeItem;
        if (!isEditable() || !getTreeView().isEditable() || (treeItem = getTreeItem()) == null) {
            return;
        }
        if (this.comboBox == null) {
            this.comboBox = CellUtils.createComboBox(this, this.items, converterProperty());
            this.comboBox.editableProperty().bind(comboBoxEditableProperty());
        }
        if (this.hbox == null) {
            this.hbox = new HBox(CellUtils.TREE_VIEW_HBOX_GRAPHIC_PADDING);
        }
        this.comboBox.getSelectionModel().select((SingleSelectionModel<T>) treeItem.getValue());
        super.startEdit();
        if (isEditing()) {
            setText(null);
            Node graphic = CellUtils.getGraphic(treeItem);
            if (graphic != null) {
                this.hbox.getChildren().setAll(graphic, this.comboBox);
                setGraphic(this.hbox);
            } else {
                setGraphic(this.comboBox);
            }
        }
    }

    @Override // javafx.scene.control.TreeCell, javafx.scene.control.Cell
    public void cancelEdit() {
        super.cancelEdit();
        setText(getConverter().toString(getItem()));
        setGraphic(null);
    }

    @Override // javafx.scene.control.cell.DefaultTreeCell, javafx.scene.control.Cell
    public void updateItem(T item, boolean empty) {
        super.updateItem(item, empty);
        Node graphic = CellUtils.getGraphic(getTreeItem());
        CellUtils.updateItem(this, getConverter(), this.hbox, graphic, this.comboBox);
    }
}
