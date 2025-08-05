package javafx.scene.control.cell;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Node;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import javafx.util.StringConverter;
import javafx.util.converter.DefaultStringConverter;

/* loaded from: jfxrt.jar:javafx/scene/control/cell/TextFieldTableCell.class */
public class TextFieldTableCell<S, T> extends TableCell<S, T> {
    private TextField textField;
    private ObjectProperty<StringConverter<T>> converter;

    public static <S> Callback<TableColumn<S, String>, TableCell<S, String>> forTableColumn() {
        return forTableColumn(new DefaultStringConverter());
    }

    public static <S, T> Callback<TableColumn<S, T>, TableCell<S, T>> forTableColumn(StringConverter<T> converter) {
        return list -> {
            return new TextFieldTableCell(converter);
        };
    }

    public TextFieldTableCell() {
        this(null);
    }

    public TextFieldTableCell(StringConverter<T> converter) {
        this.converter = new SimpleObjectProperty(this, "converter");
        getStyleClass().add("text-field-table-cell");
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

    @Override // javafx.scene.control.TableCell, javafx.scene.control.Cell
    public void startEdit() {
        if (!isEditable() || !getTableView().isEditable() || !getTableColumn().isEditable()) {
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

    @Override // javafx.scene.control.TableCell, javafx.scene.control.Cell
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
