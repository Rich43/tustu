package javafx.scene.control;

import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;

/* loaded from: jfxrt.jar:javafx/scene/control/FocusModel.class */
public abstract class FocusModel<T> {
    private ReadOnlyIntegerWrapper focusedIndex = new ReadOnlyIntegerWrapper(this, "focusedIndex", -1);
    private ReadOnlyObjectWrapper<T> focusedItem = new ReadOnlyObjectWrapper<>(this, "focusedItem");

    protected abstract int getItemCount();

    protected abstract T getModelItem(int i2);

    public FocusModel() {
        focusedIndexProperty().addListener(valueModel -> {
            setFocusedItem(getModelItem(getFocusedIndex()));
        });
    }

    public final ReadOnlyIntegerProperty focusedIndexProperty() {
        return this.focusedIndex.getReadOnlyProperty();
    }

    public final int getFocusedIndex() {
        return this.focusedIndex.get();
    }

    final void setFocusedIndex(int value) {
        this.focusedIndex.set(value);
    }

    public final ReadOnlyObjectProperty<T> focusedItemProperty() {
        return this.focusedItem.getReadOnlyProperty();
    }

    public final T getFocusedItem() {
        return focusedItemProperty().get();
    }

    final void setFocusedItem(T value) {
        this.focusedItem.set(value);
    }

    public boolean isFocused(int index) {
        return index >= 0 && index < getItemCount() && getFocusedIndex() == index;
    }

    public void focus(int index) {
        if (index < 0 || index >= getItemCount()) {
            setFocusedIndex(-1);
            return;
        }
        int oldFocusIndex = getFocusedIndex();
        setFocusedIndex(index);
        if (oldFocusIndex == index) {
            setFocusedItem(getModelItem(index));
        }
    }

    public void focusPrevious() {
        if (getFocusedIndex() == -1) {
            focus(0);
        } else if (getFocusedIndex() > 0) {
            focus(getFocusedIndex() - 1);
        }
    }

    public void focusNext() {
        if (getFocusedIndex() == -1) {
            focus(0);
        } else if (getFocusedIndex() != getItemCount() - 1) {
            focus(getFocusedIndex() + 1);
        }
    }
}
