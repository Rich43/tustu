package javafx.scene.control.cell;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import javafx.util.StringConverter;
import javafx.util.converter.DefaultStringConverter;

/* loaded from: jfxrt.jar:javafx/scene/control/cell/TextFieldTreeCell.class */
public class TextFieldTreeCell<T> extends DefaultTreeCell<T> {
    private TextField textField;
    private HBox hbox;
    private ObjectProperty<StringConverter<T>> converter;

    public static Callback<TreeView<String>, TreeCell<String>> forTreeView() {
        return forTreeView(new DefaultStringConverter());
    }

    public static <T> Callback<TreeView<T>, TreeCell<T>> forTreeView(StringConverter<T> converter) {
        return list -> {
            return new TextFieldTreeCell(converter);
        };
    }

    public TextFieldTreeCell() {
        this(null);
    }

    public TextFieldTreeCell(StringConverter<T> converter) {
        this.converter = new SimpleObjectProperty(this, "converter");
        getStyleClass().add("text-field-tree-cell");
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

    @Override // javafx.scene.control.TreeCell, javafx.scene.control.Cell
    public void startEdit() {
        if (!isEditable() || !getTreeView().isEditable()) {
            return;
        }
        super.startEdit();
        if (isEditing()) {
            StringConverter<T> converter = getConverter();
            if (this.textField == null) {
                this.textField = CellUtils.createTextField(this, converter);
            }
            if (this.hbox == null) {
                this.hbox = new HBox(CellUtils.TREE_VIEW_HBOX_GRAPHIC_PADDING);
            }
            CellUtils.startEdit(this, converter, this.hbox, getTreeItemGraphic(), this.textField);
        }
    }

    @Override // javafx.scene.control.TreeCell, javafx.scene.control.Cell
    public void cancelEdit() {
        super.cancelEdit();
        CellUtils.cancelEdit(this, getConverter(), getTreeItemGraphic());
    }

    @Override // javafx.scene.control.cell.DefaultTreeCell, javafx.scene.control.Cell
    public void updateItem(T item, boolean empty) {
        super.updateItem(item, empty);
        CellUtils.updateItem(this, getConverter(), this.hbox, getTreeItemGraphic(), this.textField);
    }

    private Node getTreeItemGraphic() {
        TreeItem<T> treeItem = getTreeItem();
        if (treeItem == null) {
            return null;
        }
        return treeItem.getGraphic();
    }
}
