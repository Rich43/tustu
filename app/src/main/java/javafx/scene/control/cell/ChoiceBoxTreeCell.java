package javafx.scene.control.cell;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import javafx.util.StringConverter;

/* loaded from: jfxrt.jar:javafx/scene/control/cell/ChoiceBoxTreeCell.class */
public class ChoiceBoxTreeCell<T> extends DefaultTreeCell<T> {
    private final ObservableList<T> items;
    private ChoiceBox<T> choiceBox;
    private HBox hbox;
    private ObjectProperty<StringConverter<T>> converter;

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
            return new ChoiceBoxTreeCell(converter, items);
        };
    }

    public ChoiceBoxTreeCell() {
        this(FXCollections.observableArrayList());
    }

    @SafeVarargs
    public ChoiceBoxTreeCell(T... items) {
        this(FXCollections.observableArrayList(items));
    }

    @SafeVarargs
    public ChoiceBoxTreeCell(StringConverter<T> converter, T... items) {
        this(converter, FXCollections.observableArrayList(items));
    }

    public ChoiceBoxTreeCell(ObservableList<T> items) {
        this((StringConverter) null, items);
    }

    public ChoiceBoxTreeCell(StringConverter<T> converter, ObservableList<T> items) {
        this.converter = new SimpleObjectProperty(this, "converter");
        getStyleClass().add("choice-box-tree-cell");
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

    @Override // javafx.scene.control.TreeCell, javafx.scene.control.Cell
    public void startEdit() {
        TreeItem<T> treeItem;
        if (!isEditable() || !getTreeView().isEditable() || (treeItem = getTreeItem()) == null) {
            return;
        }
        if (this.choiceBox == null) {
            this.choiceBox = CellUtils.createChoiceBox(this, this.items, converterProperty());
        }
        if (this.hbox == null) {
            this.hbox = new HBox(CellUtils.TREE_VIEW_HBOX_GRAPHIC_PADDING);
        }
        this.choiceBox.getSelectionModel().select((SingleSelectionModel<T>) treeItem.getValue());
        super.startEdit();
        if (isEditing()) {
            setText(null);
            Node graphic = getTreeItemGraphic();
            if (graphic != null) {
                this.hbox.getChildren().setAll(graphic, this.choiceBox);
                setGraphic(this.hbox);
            } else {
                setGraphic(this.choiceBox);
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
        CellUtils.updateItem(this, getConverter(), this.hbox, getTreeItemGraphic(), this.choiceBox);
    }

    private Node getTreeItemGraphic() {
        TreeItem<T> treeItem = getTreeItem();
        if (treeItem == null) {
            return null;
        }
        return treeItem.getGraphic();
    }
}
