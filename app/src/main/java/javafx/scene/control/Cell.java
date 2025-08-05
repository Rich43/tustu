package javafx.scene.control;

import com.sun.org.apache.xalan.internal.templates.Constants;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.css.PseudoClass;
import javafx.css.StyleableProperty;
import javax.swing.JInternalFrame;
import javax.swing.JTree;

/* loaded from: jfxrt.jar:javafx/scene/control/Cell.class */
public class Cell<T> extends Labeled {
    private ObjectProperty<T> item = new SimpleObjectProperty(this, "item");
    private ReadOnlyBooleanWrapper empty = new ReadOnlyBooleanWrapper(true) { // from class: javafx.scene.control.Cell.2
        @Override // javafx.beans.property.BooleanPropertyBase
        protected void invalidated() {
            boolean active = get();
            Cell.this.pseudoClassStateChanged(Cell.PSEUDO_CLASS_EMPTY, active);
            Cell.this.pseudoClassStateChanged(Cell.PSEUDO_CLASS_FILLED, !active);
        }

        @Override // javafx.beans.property.SimpleBooleanProperty, javafx.beans.property.ReadOnlyProperty
        public Object getBean() {
            return Cell.this;
        }

        @Override // javafx.beans.property.SimpleBooleanProperty, javafx.beans.property.ReadOnlyProperty
        public String getName() {
            return Constants.ELEMNAME_EMPTY_STRING;
        }
    };
    private ReadOnlyBooleanWrapper selected = new ReadOnlyBooleanWrapper() { // from class: javafx.scene.control.Cell.3
        @Override // javafx.beans.property.BooleanPropertyBase
        protected void invalidated() {
            Cell.this.pseudoClassStateChanged(Cell.PSEUDO_CLASS_SELECTED, get());
        }

        @Override // javafx.beans.property.SimpleBooleanProperty, javafx.beans.property.ReadOnlyProperty
        public Object getBean() {
            return Cell.this;
        }

        @Override // javafx.beans.property.SimpleBooleanProperty, javafx.beans.property.ReadOnlyProperty
        public String getName() {
            return JInternalFrame.IS_SELECTED_PROPERTY;
        }
    };
    private ReadOnlyBooleanWrapper editing;
    private BooleanProperty editable;
    private static final String DEFAULT_STYLE_CLASS = "cell";
    private static final PseudoClass PSEUDO_CLASS_SELECTED = PseudoClass.getPseudoClass(JInternalFrame.IS_SELECTED_PROPERTY);
    private static final PseudoClass PSEUDO_CLASS_FOCUSED = PseudoClass.getPseudoClass("focused");
    private static final PseudoClass PSEUDO_CLASS_EMPTY = PseudoClass.getPseudoClass(Constants.ELEMNAME_EMPTY_STRING);
    private static final PseudoClass PSEUDO_CLASS_FILLED = PseudoClass.getPseudoClass("filled");

    public Cell() {
        setText(null);
        ((StyleableProperty) focusTraversableProperty()).applyStyle(null, Boolean.FALSE);
        getStyleClass().addAll(DEFAULT_STYLE_CLASS);
        super.focusedProperty().addListener(new InvalidationListener() { // from class: javafx.scene.control.Cell.1
            @Override // javafx.beans.InvalidationListener
            public void invalidated(Observable property) {
                Cell.this.pseudoClassStateChanged(Cell.PSEUDO_CLASS_FOCUSED, Cell.this.isFocused());
                if (!Cell.this.isFocused() && Cell.this.isEditing()) {
                    Cell.this.cancelEdit();
                }
            }
        });
        pseudoClassStateChanged(PSEUDO_CLASS_EMPTY, true);
    }

    public final ObjectProperty<T> itemProperty() {
        return this.item;
    }

    public final void setItem(T value) {
        this.item.set(value);
    }

    public final T getItem() {
        return this.item.get();
    }

    public final ReadOnlyBooleanProperty emptyProperty() {
        return this.empty.getReadOnlyProperty();
    }

    private void setEmpty(boolean value) {
        this.empty.set(value);
    }

    public final boolean isEmpty() {
        return this.empty.get();
    }

    public final ReadOnlyBooleanProperty selectedProperty() {
        return this.selected.getReadOnlyProperty();
    }

    void setSelected(boolean value) {
        this.selected.set(value);
    }

    public final boolean isSelected() {
        return this.selected.get();
    }

    private void setEditing(boolean value) {
        editingPropertyImpl().set(value);
    }

    public final boolean isEditing() {
        if (this.editing == null) {
            return false;
        }
        return this.editing.get();
    }

    public final ReadOnlyBooleanProperty editingProperty() {
        return editingPropertyImpl().getReadOnlyProperty();
    }

    private ReadOnlyBooleanWrapper editingPropertyImpl() {
        if (this.editing == null) {
            this.editing = new ReadOnlyBooleanWrapper(this, "editing");
        }
        return this.editing;
    }

    public final void setEditable(boolean value) {
        editableProperty().set(value);
    }

    public final boolean isEditable() {
        if (this.editable == null) {
            return true;
        }
        return this.editable.get();
    }

    public final BooleanProperty editableProperty() {
        if (this.editable == null) {
            this.editable = new SimpleBooleanProperty(this, JTree.EDITABLE_PROPERTY, true);
        }
        return this.editable;
    }

    public void startEdit() {
        if (isEditable() && !isEditing() && !isEmpty()) {
            setEditing(true);
        }
    }

    public void cancelEdit() {
        if (isEditing()) {
            setEditing(false);
        }
    }

    public void commitEdit(T newValue) {
        if (isEditing()) {
            setEditing(false);
        }
    }

    protected void updateItem(T item, boolean empty) {
        setItem(item);
        setEmpty(empty);
        if (empty && isSelected()) {
            updateSelected(false);
        }
    }

    public void updateSelected(boolean selected) {
        if (selected && isEmpty()) {
            return;
        }
        setSelected(selected);
    }

    protected boolean isItemChanged(T oldItem, T newItem) {
        return oldItem != null ? !oldItem.equals(newItem) : newItem != null;
    }

    @Override // javafx.scene.control.Control, javafx.scene.Node
    @Deprecated
    protected Boolean impl_cssGetFocusTraversableInitialValue() {
        return Boolean.FALSE;
    }
}
