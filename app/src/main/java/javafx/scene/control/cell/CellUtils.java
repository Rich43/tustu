package javafx.scene.control.cell;

import javafx.beans.property.ObjectProperty;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Cell;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.util.StringConverter;

/* loaded from: jfxrt.jar:javafx/scene/control/cell/CellUtils.class */
class CellUtils {
    static int TREE_VIEW_HBOX_GRAPHIC_PADDING = 3;
    private static final StringConverter<?> defaultStringConverter = new StringConverter<Object>() { // from class: javafx.scene.control.cell.CellUtils.1
        @Override // javafx.util.StringConverter
        public String toString(Object t2) {
            if (t2 == null) {
                return null;
            }
            return t2.toString();
        }

        @Override // javafx.util.StringConverter
        public Object fromString(String string) {
            return string;
        }
    };
    private static final StringConverter<?> defaultTreeItemStringConverter = new StringConverter<TreeItem<?>>() { // from class: javafx.scene.control.cell.CellUtils.2
        @Override // javafx.util.StringConverter
        public String toString(TreeItem<?> treeItem) {
            return (treeItem == null || treeItem.getValue() == null) ? "" : treeItem.getValue().toString();
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // javafx.util.StringConverter
        public TreeItem<?> fromString(String string) {
            return new TreeItem<>(string);
        }
    };

    CellUtils() {
    }

    static <T> StringConverter<T> defaultStringConverter() {
        return (StringConverter<T>) defaultStringConverter;
    }

    static <T> StringConverter<TreeItem<T>> defaultTreeItemStringConverter() {
        return (StringConverter<TreeItem<T>>) defaultTreeItemStringConverter;
    }

    private static <T> String getItemText(Cell<T> cell, StringConverter<T> converter) {
        if (converter == null) {
            return cell.getItem() == null ? "" : cell.getItem().toString();
        }
        return converter.toString(cell.getItem());
    }

    static Node getGraphic(TreeItem<?> treeItem) {
        if (treeItem == null) {
            return null;
        }
        return treeItem.getGraphic();
    }

    static <T> void updateItem(Cell<T> cell, StringConverter<T> converter, ChoiceBox<T> choiceBox) {
        updateItem(cell, converter, (HBox) null, (Node) null, choiceBox);
    }

    static <T> void updateItem(Cell<T> cell, StringConverter<T> converter, HBox hbox, Node graphic, ChoiceBox<T> choiceBox) {
        if (cell.isEmpty()) {
            cell.setText(null);
            cell.setGraphic(null);
            return;
        }
        if (cell.isEditing()) {
            if (choiceBox != null) {
                choiceBox.getSelectionModel().select((SingleSelectionModel<T>) cell.getItem());
            }
            cell.setText(null);
            if (graphic != null) {
                hbox.getChildren().setAll(graphic, choiceBox);
                cell.setGraphic(hbox);
                return;
            } else {
                cell.setGraphic(choiceBox);
                return;
            }
        }
        cell.setText(getItemText(cell, converter));
        cell.setGraphic(graphic);
    }

    static <T> ChoiceBox<T> createChoiceBox(Cell<T> cell, ObservableList<T> items, ObjectProperty<StringConverter<T>> converter) {
        ChoiceBox<T> choiceBox = new ChoiceBox<>(items);
        choiceBox.setMaxWidth(Double.MAX_VALUE);
        choiceBox.converterProperty().bind(converter);
        choiceBox.getSelectionModel().selectedItemProperty().addListener((ov, oldValue, newValue) -> {
            if (cell.isEditing()) {
                cell.commitEdit(newValue);
            }
        });
        return choiceBox;
    }

    static <T> void updateItem(Cell<T> cell, StringConverter<T> converter, TextField textField) {
        updateItem(cell, converter, (HBox) null, (Node) null, textField);
    }

    static <T> void updateItem(Cell<T> cell, StringConverter<T> converter, HBox hbox, Node graphic, TextField textField) {
        if (cell.isEmpty()) {
            cell.setText(null);
            cell.setGraphic(null);
            return;
        }
        if (cell.isEditing()) {
            if (textField != null) {
                textField.setText(getItemText(cell, converter));
            }
            cell.setText(null);
            if (graphic != null) {
                hbox.getChildren().setAll(graphic, textField);
                cell.setGraphic(hbox);
                return;
            } else {
                cell.setGraphic(textField);
                return;
            }
        }
        cell.setText(getItemText(cell, converter));
        cell.setGraphic(graphic);
    }

    static <T> void startEdit(Cell<T> cell, StringConverter<T> converter, HBox hbox, Node graphic, TextField textField) {
        if (textField != null) {
            textField.setText(getItemText(cell, converter));
        }
        cell.setText(null);
        if (graphic != null) {
            hbox.getChildren().setAll(graphic, textField);
            cell.setGraphic(hbox);
        } else {
            cell.setGraphic(textField);
        }
        textField.selectAll();
        textField.requestFocus();
    }

    static <T> void cancelEdit(Cell<T> cell, StringConverter<T> converter, Node graphic) {
        cell.setText(getItemText(cell, converter));
        cell.setGraphic(graphic);
    }

    static <T> TextField createTextField(Cell<T> cell, StringConverter<T> converter) {
        TextField textField = new TextField(getItemText(cell, converter));
        textField.setOnAction(event -> {
            if (converter == null) {
                throw new IllegalStateException("Attempting to convert text input into Object, but provided StringConverter is null. Be sure to set a StringConverter in your cell factory.");
            }
            cell.commitEdit(converter.fromString(textField.getText()));
            event.consume();
        });
        textField.setOnKeyReleased(t2 -> {
            if (t2.getCode() == KeyCode.ESCAPE) {
                cell.cancelEdit();
                t2.consume();
            }
        });
        return textField;
    }

    static <T> void updateItem(Cell<T> cell, StringConverter<T> converter, ComboBox<T> comboBox) {
        updateItem(cell, converter, (HBox) null, (Node) null, comboBox);
    }

    static <T> void updateItem(Cell<T> cell, StringConverter<T> converter, HBox hbox, Node graphic, ComboBox<T> comboBox) {
        if (cell.isEmpty()) {
            cell.setText(null);
            cell.setGraphic(null);
            return;
        }
        if (cell.isEditing()) {
            if (comboBox != null) {
                comboBox.getSelectionModel().select((SingleSelectionModel<T>) cell.getItem());
            }
            cell.setText(null);
            if (graphic != null) {
                hbox.getChildren().setAll(graphic, comboBox);
                cell.setGraphic(hbox);
                return;
            } else {
                cell.setGraphic(comboBox);
                return;
            }
        }
        cell.setText(getItemText(cell, converter));
        cell.setGraphic(graphic);
    }

    static <T> ComboBox<T> createComboBox(Cell<T> cell, ObservableList<T> items, ObjectProperty<StringConverter<T>> converter) {
        ComboBox<T> comboBox = new ComboBox<>(items);
        comboBox.converterProperty().bind(converter);
        comboBox.setMaxWidth(Double.MAX_VALUE);
        comboBox.getSelectionModel().selectedItemProperty().addListener((ov, oldValue, newValue) -> {
            if (cell.isEditing()) {
                cell.commitEdit(newValue);
            }
        });
        return comboBox;
    }
}
