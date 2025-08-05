package javafx.scene.control.cell;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import javafx.util.StringConverter;

/* loaded from: jfxrt.jar:javafx/scene/control/cell/ChoiceBoxListCell.class */
public class ChoiceBoxListCell<T> extends ListCell<T> {
    private final ObservableList<T> items;
    private ChoiceBox<T> choiceBox;
    private ObjectProperty<StringConverter<T>> converter;

    @SafeVarargs
    public static <T> Callback<ListView<T>, ListCell<T>> forListView(T... items) {
        return forListView(FXCollections.observableArrayList(items));
    }

    @SafeVarargs
    public static <T> Callback<ListView<T>, ListCell<T>> forListView(StringConverter<T> converter, T... items) {
        return forListView(converter, FXCollections.observableArrayList(items));
    }

    public static <T> Callback<ListView<T>, ListCell<T>> forListView(ObservableList<T> items) {
        return forListView((StringConverter) null, items);
    }

    public static <T> Callback<ListView<T>, ListCell<T>> forListView(StringConverter<T> converter, ObservableList<T> items) {
        return list -> {
            return new ChoiceBoxListCell(converter, items);
        };
    }

    public ChoiceBoxListCell() {
        this(FXCollections.observableArrayList());
    }

    @SafeVarargs
    public ChoiceBoxListCell(T... items) {
        this(FXCollections.observableArrayList(items));
    }

    @SafeVarargs
    public ChoiceBoxListCell(StringConverter<T> converter, T... items) {
        this(converter, FXCollections.observableArrayList(items));
    }

    public ChoiceBoxListCell(ObservableList<T> items) {
        this((StringConverter) null, items);
    }

    public ChoiceBoxListCell(StringConverter<T> converter, ObservableList<T> items) {
        this.converter = new SimpleObjectProperty(this, "converter");
        getStyleClass().add("choice-box-list-cell");
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

    @Override // javafx.scene.control.ListCell, javafx.scene.control.Cell
    public void startEdit() {
        if (!isEditable() || !getListView().isEditable()) {
            return;
        }
        if (this.choiceBox == null) {
            this.choiceBox = CellUtils.createChoiceBox(this, this.items, converterProperty());
        }
        this.choiceBox.getSelectionModel().select((SingleSelectionModel<T>) getItem());
        super.startEdit();
        if (isEditing()) {
            setText(null);
            setGraphic(this.choiceBox);
        }
    }

    @Override // javafx.scene.control.ListCell, javafx.scene.control.Cell
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
