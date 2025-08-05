package javafx.scene.control.cell;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.AccessibleAttribute;
import javafx.scene.AccessibleRole;
import javafx.scene.control.CheckBox;
import javafx.scene.control.CheckBoxTreeItem;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.util.Callback;
import javafx.util.StringConverter;

/* loaded from: jfxrt.jar:javafx/scene/control/cell/CheckBoxTreeCell.class */
public class CheckBoxTreeCell<T> extends DefaultTreeCell<T> {
    private final CheckBox checkBox;
    private ObservableValue<Boolean> booleanProperty;
    private BooleanProperty indeterminateProperty;
    private ObjectProperty<StringConverter<TreeItem<T>>> converter;
    private ObjectProperty<Callback<TreeItem<T>, ObservableValue<Boolean>>> selectedStateCallback;

    public static <T> Callback<TreeView<T>, TreeCell<T>> forTreeView() {
        Callback<TreeItem<T>, ObservableValue<Boolean>> getSelectedProperty = item -> {
            if (item instanceof CheckBoxTreeItem) {
                return ((CheckBoxTreeItem) item).selectedProperty();
            }
            return null;
        };
        return forTreeView(getSelectedProperty, CellUtils.defaultTreeItemStringConverter());
    }

    public static <T> Callback<TreeView<T>, TreeCell<T>> forTreeView(Callback<TreeItem<T>, ObservableValue<Boolean>> getSelectedProperty) {
        return forTreeView(getSelectedProperty, CellUtils.defaultTreeItemStringConverter());
    }

    public static <T> Callback<TreeView<T>, TreeCell<T>> forTreeView(Callback<TreeItem<T>, ObservableValue<Boolean>> getSelectedProperty, StringConverter<TreeItem<T>> converter) {
        return tree -> {
            return new CheckBoxTreeCell(getSelectedProperty, converter);
        };
    }

    public CheckBoxTreeCell() {
        this(item -> {
            if (item instanceof CheckBoxTreeItem) {
                return ((CheckBoxTreeItem) item).selectedProperty();
            }
            return null;
        });
    }

    public CheckBoxTreeCell(Callback<TreeItem<T>, ObservableValue<Boolean>> getSelectedProperty) {
        this(getSelectedProperty, CellUtils.defaultTreeItemStringConverter(), null);
    }

    public CheckBoxTreeCell(Callback<TreeItem<T>, ObservableValue<Boolean>> getSelectedProperty, StringConverter<TreeItem<T>> converter) {
        this(getSelectedProperty, converter, null);
    }

    private CheckBoxTreeCell(Callback<TreeItem<T>, ObservableValue<Boolean>> getSelectedProperty, StringConverter<TreeItem<T>> converter, Callback<TreeItem<T>, ObservableValue<Boolean>> getIndeterminateProperty) {
        this.converter = new SimpleObjectProperty(this, "converter");
        this.selectedStateCallback = new SimpleObjectProperty(this, "selectedStateCallback");
        getStyleClass().add("check-box-tree-cell");
        setSelectedStateCallback(getSelectedProperty);
        setConverter(converter);
        this.checkBox = new CheckBox();
        this.checkBox.setAllowIndeterminate(false);
        setGraphic(null);
        setAccessibleRole(AccessibleRole.CHECK_BOX_TREE_ITEM);
    }

    public final ObjectProperty<StringConverter<TreeItem<T>>> converterProperty() {
        return this.converter;
    }

    public final void setConverter(StringConverter<TreeItem<T>> value) {
        converterProperty().set(value);
    }

    public final StringConverter<TreeItem<T>> getConverter() {
        return converterProperty().get();
    }

    public final ObjectProperty<Callback<TreeItem<T>, ObservableValue<Boolean>>> selectedStateCallbackProperty() {
        return this.selectedStateCallback;
    }

    public final void setSelectedStateCallback(Callback<TreeItem<T>, ObservableValue<Boolean>> value) {
        selectedStateCallbackProperty().set(value);
    }

    public final Callback<TreeItem<T>, ObservableValue<Boolean>> getSelectedStateCallback() {
        return selectedStateCallbackProperty().get();
    }

    @Override // javafx.scene.control.cell.DefaultTreeCell, javafx.scene.control.Cell
    public void updateItem(T item, boolean empty) {
        super.updateItem(item, empty);
        if (empty) {
            setText(null);
            setGraphic(null);
            return;
        }
        StringConverter<TreeItem<T>> c2 = getConverter();
        TreeItem<T> treeItem = getTreeItem();
        setText(c2 != null ? c2.toString(treeItem) : treeItem == null ? "" : treeItem.toString());
        this.checkBox.setGraphic(treeItem == null ? null : treeItem.getGraphic());
        setGraphic(this.checkBox);
        if (this.booleanProperty != null) {
            this.checkBox.selectedProperty().unbindBidirectional((BooleanProperty) this.booleanProperty);
        }
        if (this.indeterminateProperty != null) {
            this.checkBox.indeterminateProperty().unbindBidirectional(this.indeterminateProperty);
        }
        if (treeItem instanceof CheckBoxTreeItem) {
            CheckBoxTreeItem<T> cbti = (CheckBoxTreeItem) treeItem;
            this.booleanProperty = cbti.selectedProperty();
            this.checkBox.selectedProperty().bindBidirectional((BooleanProperty) this.booleanProperty);
            this.indeterminateProperty = cbti.indeterminateProperty();
            this.checkBox.indeterminateProperty().bindBidirectional(this.indeterminateProperty);
            return;
        }
        Callback<TreeItem<T>, ObservableValue<Boolean>> callback = getSelectedStateCallback();
        if (callback == null) {
            throw new NullPointerException("The CheckBoxTreeCell selectedStateCallbackProperty can not be null");
        }
        this.booleanProperty = callback.call(treeItem);
        if (this.booleanProperty != null) {
            this.checkBox.selectedProperty().bindBidirectional((BooleanProperty) this.booleanProperty);
        }
    }

    @Override // javafx.scene.control.cell.DefaultTreeCell
    void updateDisplay(T item, boolean empty) {
    }

    @Override // javafx.scene.control.TreeCell, javafx.scene.control.Control, javafx.scene.Parent, javafx.scene.Node
    public Object queryAccessibleAttribute(AccessibleAttribute attribute, Object... parameters) {
        switch (attribute) {
            case TOGGLE_STATE:
                if (this.checkBox.isIndeterminate()) {
                    return AccessibleAttribute.ToggleState.INDETERMINATE;
                }
                if (this.checkBox.isSelected()) {
                    return AccessibleAttribute.ToggleState.CHECKED;
                }
                return AccessibleAttribute.ToggleState.UNCHECKED;
            default:
                return super.queryAccessibleAttribute(attribute, parameters);
        }
    }
}
