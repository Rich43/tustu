package javafx.scene.control;

import com.sun.javafx.scene.control.skin.ChoiceBoxSkin;
import javafx.beans.DefaultProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.css.PseudoClass;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.AccessibleAction;
import javafx.scene.AccessibleAttribute;
import javafx.scene.AccessibleRole;
import javafx.util.StringConverter;

@DefaultProperty("items")
/* loaded from: jfxrt.jar:javafx/scene/control/ChoiceBox.class */
public class ChoiceBox<T> extends Control {
    private ObjectProperty<SingleSelectionModel<T>> selectionModel;
    private ChangeListener<T> selectedItemListener;
    private ReadOnlyBooleanWrapper showing;
    private ObjectProperty<ObservableList<T>> items;
    private final ListChangeListener<T> itemsListener;
    private ObjectProperty<StringConverter<T>> converter;
    private ObjectProperty<T> value;
    private ObjectProperty<EventHandler<ActionEvent>> onAction;
    private ObjectProperty<EventHandler<Event>> onShowing;
    private ObjectProperty<EventHandler<Event>> onShown;
    private ObjectProperty<EventHandler<Event>> onHiding;
    private ObjectProperty<EventHandler<Event>> onHidden;
    public static final EventType<Event> ON_SHOWING = new EventType<>(Event.ANY, "CHOICE_BOX_ON_SHOWING");
    public static final EventType<Event> ON_SHOWN = new EventType<>(Event.ANY, "CHOICE_BOX_ON_SHOWN");
    public static final EventType<Event> ON_HIDING = new EventType<>(Event.ANY, "CHOICE_BOX_ON_HIDING");
    public static final EventType<Event> ON_HIDDEN = new EventType<>(Event.ANY, "CHOICE_BOX_ON_HIDDEN");
    private static final PseudoClass SHOWING_PSEUDOCLASS_STATE = PseudoClass.getPseudoClass("showing");

    public ChoiceBox() {
        this(FXCollections.observableArrayList());
    }

    public ChoiceBox(ObservableList<T> items) {
        this.selectionModel = new SimpleObjectProperty<SingleSelectionModel<T>>(this, "selectionModel") { // from class: javafx.scene.control.ChoiceBox.1
            private SelectionModel<T> oldSM = null;

            @Override // javafx.beans.property.ObjectPropertyBase
            protected void invalidated() {
                if (this.oldSM != null) {
                    this.oldSM.selectedItemProperty().removeListener(ChoiceBox.this.selectedItemListener);
                }
                SelectionModel<T> sm = get();
                this.oldSM = sm;
                if (sm != null) {
                    sm.selectedItemProperty().addListener(ChoiceBox.this.selectedItemListener);
                }
            }
        };
        this.selectedItemListener = (ov, t2, obj) -> {
            if (!valueProperty().isBound()) {
                setValue(obj);
            }
        };
        this.showing = new ReadOnlyBooleanWrapper() { // from class: javafx.scene.control.ChoiceBox.2
            @Override // javafx.beans.property.BooleanPropertyBase
            protected void invalidated() {
                ChoiceBox.this.pseudoClassStateChanged(ChoiceBox.SHOWING_PSEUDOCLASS_STATE, get());
                ChoiceBox.this.notifyAccessibleAttributeChanged(AccessibleAttribute.EXPANDED);
            }

            @Override // javafx.beans.property.SimpleBooleanProperty, javafx.beans.property.ReadOnlyProperty
            public Object getBean() {
                return ChoiceBox.this;
            }

            @Override // javafx.beans.property.SimpleBooleanProperty, javafx.beans.property.ReadOnlyProperty
            public String getName() {
                return "showing";
            }
        };
        this.items = new ObjectPropertyBase<ObservableList<T>>() { // from class: javafx.scene.control.ChoiceBox.3
            ObservableList<T> old;

            @Override // javafx.beans.property.ObjectPropertyBase
            protected void invalidated() {
                ObservableList<T> newItems = get();
                if (this.old != newItems) {
                    if (this.old != null) {
                        this.old.removeListener(ChoiceBox.this.itemsListener);
                    }
                    if (newItems != null) {
                        newItems.addListener(ChoiceBox.this.itemsListener);
                    }
                    SingleSelectionModel<T> sm = ChoiceBox.this.getSelectionModel();
                    if (sm != null) {
                        if ((newItems == null || !newItems.isEmpty()) && sm.getSelectedIndex() == -1 && sm.getSelectedItem() != null) {
                            int newIndex = ChoiceBox.this.getItems().indexOf(sm.getSelectedItem());
                            if (newIndex != -1) {
                                sm.setSelectedIndex(newIndex);
                            }
                        } else {
                            sm.clearSelection();
                        }
                    }
                    this.old = newItems;
                }
            }

            @Override // javafx.beans.property.ReadOnlyProperty
            public Object getBean() {
                return ChoiceBox.this;
            }

            @Override // javafx.beans.property.ReadOnlyProperty
            public String getName() {
                return "items";
            }
        };
        this.itemsListener = c2 -> {
            SingleSelectionModel<T> sm = getSelectionModel();
            if (sm != null) {
                if (getItems() == null || getItems().isEmpty()) {
                    sm.clearSelection();
                } else {
                    int newIndex = getItems().indexOf(sm.getSelectedItem());
                    sm.setSelectedIndex(newIndex);
                }
            }
            if (sm != null) {
                T selectedItem = sm.getSelectedItem();
                while (c2.next()) {
                    if (selectedItem != null && c2.getRemoved().contains(selectedItem)) {
                        sm.clearSelection();
                        return;
                    }
                }
            }
        };
        this.converter = new SimpleObjectProperty(this, "converter", null);
        this.value = new SimpleObjectProperty<T>(this, "value") { // from class: javafx.scene.control.ChoiceBox.4
            /* JADX WARN: Multi-variable type inference failed */
            @Override // javafx.beans.property.ObjectPropertyBase
            protected void invalidated() {
                super.invalidated();
                ChoiceBox.this.fireEvent(new ActionEvent());
                SingleSelectionModel selectionModel = ChoiceBox.this.getSelectionModel();
                if (selectionModel != 0) {
                    selectionModel.select((SingleSelectionModel) super.getValue2());
                }
                ChoiceBox.this.notifyAccessibleAttributeChanged(AccessibleAttribute.TEXT);
            }
        };
        this.onAction = new ObjectPropertyBase<EventHandler<ActionEvent>>() { // from class: javafx.scene.control.ChoiceBox.5
            @Override // javafx.beans.property.ObjectPropertyBase
            protected void invalidated() {
                ChoiceBox.this.setEventHandler(ActionEvent.ACTION, get());
            }

            @Override // javafx.beans.property.ReadOnlyProperty
            public Object getBean() {
                return ChoiceBox.this;
            }

            @Override // javafx.beans.property.ReadOnlyProperty
            public String getName() {
                return "onAction";
            }
        };
        this.onShowing = new ObjectPropertyBase<EventHandler<Event>>() { // from class: javafx.scene.control.ChoiceBox.6
            @Override // javafx.beans.property.ObjectPropertyBase
            protected void invalidated() {
                ChoiceBox.this.setEventHandler(ChoiceBox.ON_SHOWING, get());
            }

            @Override // javafx.beans.property.ReadOnlyProperty
            public Object getBean() {
                return ChoiceBox.this;
            }

            @Override // javafx.beans.property.ReadOnlyProperty
            public String getName() {
                return "onShowing";
            }
        };
        this.onShown = new ObjectPropertyBase<EventHandler<Event>>() { // from class: javafx.scene.control.ChoiceBox.7
            @Override // javafx.beans.property.ObjectPropertyBase
            protected void invalidated() {
                ChoiceBox.this.setEventHandler(ChoiceBox.ON_SHOWN, get());
            }

            @Override // javafx.beans.property.ReadOnlyProperty
            public Object getBean() {
                return ChoiceBox.this;
            }

            @Override // javafx.beans.property.ReadOnlyProperty
            public String getName() {
                return "onShown";
            }
        };
        this.onHiding = new ObjectPropertyBase<EventHandler<Event>>() { // from class: javafx.scene.control.ChoiceBox.8
            @Override // javafx.beans.property.ObjectPropertyBase
            protected void invalidated() {
                ChoiceBox.this.setEventHandler(ChoiceBox.ON_HIDING, get());
            }

            @Override // javafx.beans.property.ReadOnlyProperty
            public Object getBean() {
                return ChoiceBox.this;
            }

            @Override // javafx.beans.property.ReadOnlyProperty
            public String getName() {
                return "onHiding";
            }
        };
        this.onHidden = new ObjectPropertyBase<EventHandler<Event>>() { // from class: javafx.scene.control.ChoiceBox.9
            @Override // javafx.beans.property.ObjectPropertyBase
            protected void invalidated() {
                ChoiceBox.this.setEventHandler(ChoiceBox.ON_HIDDEN, get());
            }

            @Override // javafx.beans.property.ReadOnlyProperty
            public Object getBean() {
                return ChoiceBox.this;
            }

            @Override // javafx.beans.property.ReadOnlyProperty
            public String getName() {
                return "onHidden";
            }
        };
        getStyleClass().setAll("choice-box");
        setAccessibleRole(AccessibleRole.COMBO_BOX);
        setItems(items);
        setSelectionModel(new ChoiceBoxSelectionModel(this));
        valueProperty().addListener((ov2, t3, t1) -> {
            int index;
            if (getItems() != null && (index = getItems().indexOf(t1)) > -1) {
                getSelectionModel().select(index);
            }
        });
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

    public final boolean isShowing() {
        return this.showing.get();
    }

    public final ReadOnlyBooleanProperty showingProperty() {
        return this.showing.getReadOnlyProperty();
    }

    private void setShowing(boolean value) {
        Event.fireEvent(this, value ? new Event(ComboBoxBase.ON_SHOWING) : new Event(ComboBoxBase.ON_HIDING));
        this.showing.set(value);
        Event.fireEvent(this, value ? new Event(ComboBoxBase.ON_SHOWN) : new Event(ComboBoxBase.ON_HIDDEN));
    }

    public final void setItems(ObservableList<T> value) {
        this.items.set(value);
    }

    public final ObservableList<T> getItems() {
        return this.items.get();
    }

    public final ObjectProperty<ObservableList<T>> itemsProperty() {
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

    public ObjectProperty<T> valueProperty() {
        return this.value;
    }

    public final void setValue(T value) {
        valueProperty().set(value);
    }

    public final T getValue() {
        return valueProperty().get();
    }

    public final ObjectProperty<EventHandler<ActionEvent>> onActionProperty() {
        return this.onAction;
    }

    public final void setOnAction(EventHandler<ActionEvent> value) {
        onActionProperty().set(value);
    }

    public final EventHandler<ActionEvent> getOnAction() {
        return onActionProperty().get();
    }

    public final ObjectProperty<EventHandler<Event>> onShowingProperty() {
        return this.onShowing;
    }

    public final void setOnShowing(EventHandler<Event> value) {
        onShowingProperty().set(value);
    }

    public final EventHandler<Event> getOnShowing() {
        return onShowingProperty().get();
    }

    public final ObjectProperty<EventHandler<Event>> onShownProperty() {
        return this.onShown;
    }

    public final void setOnShown(EventHandler<Event> value) {
        onShownProperty().set(value);
    }

    public final EventHandler<Event> getOnShown() {
        return onShownProperty().get();
    }

    public final ObjectProperty<EventHandler<Event>> onHidingProperty() {
        return this.onHiding;
    }

    public final void setOnHiding(EventHandler<Event> value) {
        onHidingProperty().set(value);
    }

    public final EventHandler<Event> getOnHiding() {
        return onHidingProperty().get();
    }

    public final ObjectProperty<EventHandler<Event>> onHiddenProperty() {
        return this.onHidden;
    }

    public final void setOnHidden(EventHandler<Event> value) {
        onHiddenProperty().set(value);
    }

    public final EventHandler<Event> getOnHidden() {
        return onHiddenProperty().get();
    }

    public void show() {
        if (!isDisabled()) {
            setShowing(true);
        }
    }

    public void hide() {
        setShowing(false);
    }

    @Override // javafx.scene.control.Control
    protected Skin<?> createDefaultSkin() {
        return new ChoiceBoxSkin(this);
    }

    /* loaded from: jfxrt.jar:javafx/scene/control/ChoiceBox$ChoiceBoxSelectionModel.class */
    static class ChoiceBoxSelectionModel<T> extends SingleSelectionModel<T> {
        private final ChoiceBox<T> choiceBox;

        public ChoiceBoxSelectionModel(ChoiceBox<T> cb) {
            if (cb == null) {
                throw new NullPointerException("ChoiceBox can not be null");
            }
            this.choiceBox = cb;
            ListChangeListener<? super T> listChangeListener = c2 -> {
                int newIndex;
                if (this.choiceBox.getItems() == null || this.choiceBox.getItems().isEmpty()) {
                    setSelectedIndex(-1);
                } else if (getSelectedIndex() == -1 && getSelectedItem() != null && (newIndex = this.choiceBox.getItems().indexOf(getSelectedItem())) != -1) {
                    setSelectedIndex(newIndex);
                }
            };
            if (this.choiceBox.getItems() != null) {
                this.choiceBox.getItems().addListener(listChangeListener);
            }
            ChangeListener<ObservableList<T>> itemsObserver = (valueModel, oldList, newList) -> {
                int newIndex;
                if (oldList != null) {
                    oldList.removeListener(listChangeListener);
                }
                if (newList != null) {
                    newList.addListener(listChangeListener);
                }
                setSelectedIndex(-1);
                if (getSelectedItem() != null && (newIndex = this.choiceBox.getItems().indexOf(getSelectedItem())) != -1) {
                    setSelectedIndex(newIndex);
                }
            };
            this.choiceBox.itemsProperty().addListener(itemsObserver);
        }

        @Override // javafx.scene.control.SingleSelectionModel
        protected T getModelItem(int index) {
            ObservableList<T> items = this.choiceBox.getItems();
            if (items != null && index >= 0 && index < items.size()) {
                return items.get(index);
            }
            return null;
        }

        @Override // javafx.scene.control.SingleSelectionModel
        protected int getItemCount() {
            ObservableList<T> items = this.choiceBox.getItems();
            if (items == null) {
                return 0;
            }
            return items.size();
        }

        @Override // javafx.scene.control.SingleSelectionModel, javafx.scene.control.SelectionModel
        public void select(int index) {
            T value = getModelItem(index);
            if (value instanceof Separator) {
                select(index + 1);
            } else {
                super.select(index);
            }
            if (this.choiceBox.isShowing()) {
                this.choiceBox.hide();
            }
        }
    }

    @Override // javafx.scene.control.Control, javafx.scene.Parent, javafx.scene.Node
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
            case EXPANDED:
                return Boolean.valueOf(isShowing());
            default:
                return super.queryAccessibleAttribute(attribute, parameters);
        }
    }

    @Override // javafx.scene.control.Control, javafx.scene.Node
    public void executeAccessibleAction(AccessibleAction action, Object... parameters) {
        switch (action) {
            case COLLAPSE:
                hide();
                break;
            case EXPAND:
                show();
                break;
            default:
                super.executeAccessibleAction(action, new Object[0]);
                break;
        }
    }
}
