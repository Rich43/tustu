package javafx.scene.control;

import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;

/* loaded from: jfxrt.jar:javafx/scene/control/SelectionModel.class */
public abstract class SelectionModel<T> {
    private ReadOnlyIntegerWrapper selectedIndex = new ReadOnlyIntegerWrapper(this, "selectedIndex", -1);
    private ReadOnlyObjectWrapper<T> selectedItem = new ReadOnlyObjectWrapper<>(this, "selectedItem");

    public abstract void clearAndSelect(int i2);

    public abstract void select(int i2);

    public abstract void select(T t2);

    public abstract void clearSelection(int i2);

    public abstract void clearSelection();

    public abstract boolean isSelected(int i2);

    public abstract boolean isEmpty();

    public abstract void selectPrevious();

    public abstract void selectNext();

    public abstract void selectFirst();

    public abstract void selectLast();

    public final ReadOnlyIntegerProperty selectedIndexProperty() {
        return this.selectedIndex.getReadOnlyProperty();
    }

    protected final void setSelectedIndex(int value) {
        this.selectedIndex.set(value);
    }

    public final int getSelectedIndex() {
        return selectedIndexProperty().get();
    }

    public final ReadOnlyObjectProperty<T> selectedItemProperty() {
        return this.selectedItem.getReadOnlyProperty();
    }

    protected final void setSelectedItem(T value) {
        this.selectedItem.set(value);
    }

    public final T getSelectedItem() {
        return selectedItemProperty().get();
    }
}
