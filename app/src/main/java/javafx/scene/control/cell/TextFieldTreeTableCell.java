package javafx.scene.control.cell;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeTableCell;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import javafx.util.StringConverter;
import javafx.util.converter.DefaultStringConverter;

/* loaded from: jfxrt.jar:javafx/scene/control/cell/TextFieldTreeTableCell.class */
public class TextFieldTreeTableCell<S, T> extends TreeTableCell<S, T> {
    private TextField textField;
    private ObjectProperty<StringConverter<T>> converter;

    public static <S> Callback<TreeTableColumn<S, String>, TreeTableCell<S, String>> forTreeTableColumn() {
        return forTreeTableColumn(new DefaultStringConverter());
    }

    public static <S, T> Callback<TreeTableColumn<S, T>, TreeTableCell<S, T>> forTreeTableColumn(StringConverter<T> converter) {
        return list -> {
            return new TextFieldTreeTableCell(converter);
        };
    }

    public TextFieldTreeTableCell() {
        this(null);
    }

    public TextFieldTreeTableCell(StringConverter<T> converter) {
        this.converter = new SimpleObjectProperty(this, "converter");
        getStyleClass().add("text-field-tree-table-cell");
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

    @Override // javafx.scene.control.TreeTableCell, javafx.scene.control.Cell
    public void startEdit() {
        if (!isEditable() || !getTreeTableView().isEditable() || !getTableColumn().isEditable()) {
            return;
        }
        super.startEdit();
        if (isEditing()) {
            if (this.textField == null) {
                this.textField = CellUtils.createTextField(this, getConverter());
            }
            CellUtils.startEdit(this, getConverter(), null, null, this.textField);
        }
    }

    @Override // javafx.scene.control.TreeTableCell, javafx.scene.control.Cell
    public void cancelEdit() {
        super.cancelEdit();
        CellUtils.cancelEdit(this, getConverter(), null);
    }

    @Override // javafx.scene.control.Cell
    public void updateItem(T item, boolean empty) {
        super.updateItem(item, empty);
        CellUtils.updateItem(this, getConverter(), (HBox) null, (Node) null, this.textField);
    }
}
