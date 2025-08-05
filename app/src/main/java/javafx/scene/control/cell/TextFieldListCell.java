package javafx.scene.control.cell;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Node;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import javafx.util.StringConverter;
import javafx.util.converter.DefaultStringConverter;

/* loaded from: jfxrt.jar:javafx/scene/control/cell/TextFieldListCell.class */
public class TextFieldListCell<T> extends ListCell<T> {
    private TextField textField;
    private ObjectProperty<StringConverter<T>> converter;

    public static Callback<ListView<String>, ListCell<String>> forListView() {
        return forListView(new DefaultStringConverter());
    }

    public static <T> Callback<ListView<T>, ListCell<T>> forListView(StringConverter<T> converter) {
        return list -> {
            return new TextFieldListCell(converter);
        };
    }

    public TextFieldListCell() {
        this(null);
    }

    public TextFieldListCell(StringConverter<T> converter) {
        this.converter = new SimpleObjectProperty(this, "converter");
        getStyleClass().add("text-field-list-cell");
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

    @Override // javafx.scene.control.ListCell, javafx.scene.control.Cell
    public void startEdit() {
        if (!isEditable() || !getListView().isEditable()) {
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

    @Override // javafx.scene.control.ListCell, javafx.scene.control.Cell
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
