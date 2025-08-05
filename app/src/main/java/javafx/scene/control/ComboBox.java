package javafx.scene.control;

import com.sun.javafx.scene.control.skin.ComboBoxListViewSkin;
import com.sun.javafx.scene.control.skin.ComboBoxPopupControl;
import java.lang.ref.WeakReference;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.WeakInvalidationListener;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.WeakListChangeListener;
import javafx.scene.AccessibleAttribute;
import javafx.scene.AccessibleRole;
import javafx.scene.Node;
import javafx.util.Callback;
import javafx.util.StringConverter;
import javax.swing.JTree;

/* loaded from: jfxrt.jar:javafx/scene/control/ComboBox.class */
public class ComboBox<T> extends ComboBoxBase<T> {
    private ObjectProperty<ObservableList<T>> items;
    private ObjectProperty<StringConverter<T>> converter;
    private ObjectProperty<Callback<ListView<T>, ListCell<T>>> cellFactory;
    private ObjectProperty<ListCell<T>> buttonCell;
    private ObjectProperty<SingleSelectionModel<T>> selectionModel;
    private IntegerProperty visibleRowCount;
    private TextField textField;
    private ReadOnlyObjectWrapper<TextField> editor;
    private ObjectProperty<Node> placeholder;
    private ChangeListener<T> selectedItemListener;
    private static final String DEFAULT_STYLE_CLASS = "combo-box";
    private boolean wasSetAllCalled;
    private int previousItemCount;

    private static <T> StringConverter<T> defaultStringConverter() {
        return new StringConverter<T>() { // from class: javafx.scene.control.ComboBox.1
            @Override // javafx.util.StringConverter
            public String toString(T t2) {
                if (t2 == null) {
                    return null;
                }
                return t2.toString();
            }

            /* JADX WARN: Multi-variable type inference failed */
            @Override // javafx.util.StringConverter
            public T fromString(String str) {
                return str;
            }
        };
    }

    public ComboBox() {
        this(FXCollections.observableArrayList());
    }

    public ComboBox(ObservableList<T> items) {
        this.items = new SimpleObjectProperty(this, "items");
        this.converter = new SimpleObjectProperty(this, "converter", defaultStringConverter());
        this.cellFactory = new SimpleObjectProperty(this, "cellFactory");
        this.buttonCell = new SimpleObjectProperty(this, "buttonCell");
        this.selectionModel = new SimpleObjectProperty<SingleSelectionModel<T>>(this, "selectionModel") { // from class: javafx.scene.control.ComboBox.2
            private SingleSelectionModel<T> oldSM = null;

            @Override // javafx.beans.property.ObjectPropertyBase
            protected void invalidated() {
                if (this.oldSM != null) {
                    this.oldSM.selectedItemProperty().removeListener(ComboBox.this.selectedItemListener);
                }
                SingleSelectionModel<T> sm = get();
                this.oldSM = sm;
                if (sm != null) {
                    sm.selectedItemProperty().addListener(ComboBox.this.selectedItemListener);
                }
            }
        };
        this.visibleRowCount = new SimpleIntegerProperty(this, JTree.VISIBLE_ROW_COUNT_PROPERTY, 10);
        this.selectedItemListener = new ChangeListener<T>() { // from class: javafx.scene.control.ComboBox.3
            @Override // javafx.beans.value.ChangeListener
            public void changed(ObservableValue<? extends T> ov, T t2, T t1) {
                if (!ComboBox.this.wasSetAllCalled || t1 != null) {
                    ComboBox.this.updateValue(t1);
                }
                ComboBox.this.wasSetAllCalled = false;
            }
        };
        this.wasSetAllCalled = false;
        this.previousItemCount = -1;
        getStyleClass().add(DEFAULT_STYLE_CLASS);
        setAccessibleRole(AccessibleRole.COMBO_BOX);
        setItems(items);
        setSelectionModel(new ComboBoxSelectionModel(this));
        valueProperty().addListener((ov, t2, obj) -> {
            if (getItems() == null) {
                return;
            }
            SelectionModel<T> sm = getSelectionModel();
            int index = getItems().indexOf(obj);
            if (index == -1) {
                sm.setSelectedItem(obj);
                return;
            }
            T selectedItem = sm.getSelectedItem();
            if (selectedItem == null || !selectedItem.equals(getValue())) {
                sm.clearAndSelect(index);
            }
        });
        editableProperty().addListener(o2 -> {
            getSelectionModel().clearSelection();
        });
    }

    public final void setItems(ObservableList<T> value) {
        itemsProperty().set(value);
    }

    public final ObservableList<T> getItems() {
        return this.items.get();
    }

    public ObjectProperty<ObservableList<T>> itemsProperty() {
        return this.items;
    }

    public ObjectProperty<StringConverter<T>> converterProperty() {
        return this.converter;
    }

    public final void setConverter(StringConverter<T> value) {
        converterProperty().set(value);
    }

    public final StringConverter<T> getConverter() {
        return converterProperty().get();
    }

    public final void setCellFactory(Callback<ListView<T>, ListCell<T>> value) {
        cellFactoryProperty().set(value);
    }

    public final Callback<ListView<T>, ListCell<T>> getCellFactory() {
        return cellFactoryProperty().get();
    }

    public ObjectProperty<Callback<ListView<T>, ListCell<T>>> cellFactoryProperty() {
        return this.cellFactory;
    }

    public ObjectProperty<ListCell<T>> buttonCellProperty() {
        return this.buttonCell;
    }

    public final void setButtonCell(ListCell<T> value) {
        buttonCellProperty().set(value);
    }

    public final ListCell<T> getButtonCell() {
        return buttonCellProperty().get();
    }

    public final void setSelectionModel(SingleSelectionModel<T> value) {
        this.selectionModel.set(value);
    }

    public final SingleSelectionModel<T> getSelectionModel() {
        return this.selectionModel.get();
    }

    public final ObjectProperty<SingleSelectionModel<T>> selectionModelProperty() {
        return this.selectionModel;
    }

    public final void setVisibleRowCount(int value) {
        this.visibleRowCount.set(value);
    }

    public final int getVisibleRowCount() {
        return this.visibleRowCount.get();
    }

    public final IntegerProperty visibleRowCountProperty() {
        return this.visibleRowCount;
    }

    public final TextField getEditor() {
        return editorProperty().get();
    }

    public final ReadOnlyObjectProperty<TextField> editorProperty() {
        if (this.editor == null) {
            this.editor = new ReadOnlyObjectWrapper<>(this, "editor");
            this.textField = new ComboBoxPopupControl.FakeFocusTextField();
            this.editor.set(this.textField);
        }
        return this.editor.getReadOnlyProperty();
    }

    public final ObjectProperty<Node> placeholderProperty() {
        if (this.placeholder == null) {
            this.placeholder = new SimpleObjectProperty(this, "placeholder");
        }
        return this.placeholder;
    }

    public final void setPlaceholder(Node value) {
        placeholderProperty().set(value);
    }

    public final Node getPlaceholder() {
        if (this.placeholder == null) {
            return null;
        }
        return this.placeholder.get();
    }

    @Override // javafx.scene.control.Control
    protected Skin<?> createDefaultSkin() {
        return new ComboBoxListViewSkin(this);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateValue(T newValue) {
        if (!valueProperty().isBound()) {
            setValue(newValue);
        }
    }

    /* loaded from: jfxrt.jar:javafx/scene/control/ComboBox$ComboBoxSelectionModel.class */
    static class ComboBoxSelectionModel<T> extends SingleSelectionModel<T> {
        private final ComboBox<T> comboBox;
        private final InvalidationListener itemsObserver;
        private final ListChangeListener<T> itemsContentObserver = new ListChangeListener<T>() { // from class: javafx.scene.control.ComboBox.ComboBoxSelectionModel.2
            @Override // javafx.collections.ListChangeListener
            public void onChanged(ListChangeListener.Change<? extends T> c2) {
                int newIndex;
                if (ComboBoxSelectionModel.this.comboBox.getItems() == null || ComboBoxSelectionModel.this.comboBox.getItems().isEmpty()) {
                    ComboBoxSelectionModel.this.setSelectedIndex(-1);
                } else if (ComboBoxSelectionModel.this.getSelectedIndex() == -1 && ComboBoxSelectionModel.this.getSelectedItem() != null && (newIndex = ComboBoxSelectionModel.this.comboBox.getItems().indexOf(ComboBoxSelectionModel.this.getSelectedItem())) != -1) {
                    ComboBoxSelectionModel.this.setSelectedIndex(newIndex);
                }
                int shift = 0;
                while (c2.next()) {
                    ComboBoxSelectionModel.this.comboBox.wasSetAllCalled = ComboBoxSelectionModel.this.comboBox.previousItemCount == c2.getRemovedSize();
                    if (!c2.wasReplaced() && (c2.wasAdded() || c2.wasRemoved())) {
                        if (c2.getFrom() <= ComboBoxSelectionModel.this.getSelectedIndex() && ComboBoxSelectionModel.this.getSelectedIndex() != -1) {
                            shift += c2.wasAdded() ? c2.getAddedSize() : -c2.getRemovedSize();
                        }
                    }
                }
                if (shift != 0) {
                    ComboBoxSelectionModel.this.clearAndSelect(ComboBoxSelectionModel.this.getSelectedIndex() + shift);
                }
                ComboBoxSelectionModel.this.comboBox.previousItemCount = ComboBoxSelectionModel.this.getItemCount();
            }
        };
        private WeakListChangeListener<T> weakItemsContentObserver = new WeakListChangeListener<>(this.itemsContentObserver);

        public ComboBoxSelectionModel(ComboBox<T> cb) {
            if (cb == null) {
                throw new NullPointerException("ComboBox can not be null");
            }
            this.comboBox = cb;
            selectedIndexProperty().addListener(valueModel -> {
                setSelectedItem(getModelItem(getSelectedIndex()));
            });
            this.itemsObserver = new InvalidationListener() { // from class: javafx.scene.control.ComboBox.ComboBoxSelectionModel.1
                private WeakReference<ObservableList<T>> weakItemsRef;

                {
                    this.weakItemsRef = new WeakReference<>(ComboBoxSelectionModel.this.comboBox.getItems());
                }

                @Override // javafx.beans.InvalidationListener
                public void invalidated(Observable observable) {
                    ObservableList<T> oldItems = this.weakItemsRef.get();
                    this.weakItemsRef = new WeakReference<>(ComboBoxSelectionModel.this.comboBox.getItems());
                    ComboBoxSelectionModel.this.updateItemsObserver(oldItems, ComboBoxSelectionModel.this.comboBox.getItems());
                }
            };
            this.comboBox.itemsProperty().addListener(new WeakInvalidationListener(this.itemsObserver));
            if (this.comboBox.getItems() != null) {
                this.comboBox.getItems().addListener(this.weakItemsContentObserver);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void updateItemsObserver(ObservableList<T> oldList, ObservableList<T> newList) {
            T value;
            if (oldList != null) {
                oldList.removeListener(this.weakItemsContentObserver);
            }
            if (newList != null) {
                newList.addListener(this.weakItemsContentObserver);
            }
            int newValueIndex = -1;
            if (newList != null && (value = this.comboBox.getValue()) != null) {
                newValueIndex = newList.indexOf(value);
            }
            setSelectedIndex(newValueIndex);
        }

        @Override // javafx.scene.control.SingleSelectionModel
        protected T getModelItem(int index) {
            ObservableList<T> items = this.comboBox.getItems();
            if (items != null && index >= 0 && index < items.size()) {
                return items.get(index);
            }
            return null;
        }

        @Override // javafx.scene.control.SingleSelectionModel
        protected int getItemCount() {
            ObservableList<T> items = this.comboBox.getItems();
            if (items == null) {
                return 0;
            }
            return items.size();
        }
    }

    @Override // javafx.scene.control.ComboBoxBase, javafx.scene.control.Control, javafx.scene.Parent, javafx.scene.Node
    public Object queryAccessibleAttribute(AccessibleAttribute attribute, Object... parameters) {
        switch (attribute) {
            case TEXT:
                String accText = getAccessibleText();
                if (accText != null && !accText.isEmpty()) {
                    return accText;
                }
                Object title = super.queryAccessibleAttribute(attribute, parameters);
                if (title != null) {
                    return title;
                }
                StringConverter<T> converter = getConverter();
                if (converter == null) {
                    return getValue() != null ? getValue().toString() : "";
                }
                return converter.toString(getValue());
            default:
                return super.queryAccessibleAttribute(attribute, parameters);
        }
    }
}
