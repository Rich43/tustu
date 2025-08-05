package javafx.scene.control;

/* loaded from: jfxrt.jar:javafx/scene/control/SingleSelectionModel.class */
public abstract class SingleSelectionModel<T> extends SelectionModel<T> {
    protected abstract T getModelItem(int i2);

    protected abstract int getItemCount();

    @Override // javafx.scene.control.SelectionModel
    public void clearSelection() {
        updateSelectedIndex(-1);
    }

    @Override // javafx.scene.control.SelectionModel
    public void clearSelection(int index) {
        if (getSelectedIndex() == index) {
            clearSelection();
        }
    }

    @Override // javafx.scene.control.SelectionModel
    public boolean isEmpty() {
        return getItemCount() == 0 || getSelectedIndex() == -1;
    }

    @Override // javafx.scene.control.SelectionModel
    public boolean isSelected(int index) {
        return getSelectedIndex() == index;
    }

    @Override // javafx.scene.control.SelectionModel
    public void clearAndSelect(int index) {
        select(index);
    }

    @Override // javafx.scene.control.SelectionModel
    public void select(T obj) {
        if (obj == null) {
            setSelectedIndex(-1);
            setSelectedItem(null);
            return;
        }
        int itemCount = getItemCount();
        for (int i2 = 0; i2 < itemCount; i2++) {
            T value = getModelItem(i2);
            if (value != null && value.equals(obj)) {
                select(i2);
                return;
            }
        }
        setSelectedItem(obj);
    }

    @Override // javafx.scene.control.SelectionModel
    public void select(int index) {
        if (index == -1) {
            clearSelection();
            return;
        }
        int itemCount = getItemCount();
        if (itemCount == 0 || index < 0 || index >= itemCount) {
            return;
        }
        updateSelectedIndex(index);
    }

    @Override // javafx.scene.control.SelectionModel
    public void selectPrevious() {
        if (getSelectedIndex() == 0) {
            return;
        }
        select(getSelectedIndex() - 1);
    }

    @Override // javafx.scene.control.SelectionModel
    public void selectNext() {
        select(getSelectedIndex() + 1);
    }

    @Override // javafx.scene.control.SelectionModel
    public void selectFirst() {
        if (getItemCount() > 0) {
            select(0);
        }
    }

    @Override // javafx.scene.control.SelectionModel
    public void selectLast() {
        int numItems = getItemCount();
        if (numItems > 0 && getSelectedIndex() < numItems - 1) {
            select(numItems - 1);
        }
    }

    private void updateSelectedIndex(int newIndex) {
        int currentIndex = getSelectedIndex();
        T selectedItem = getSelectedItem();
        setSelectedIndex(newIndex);
        if (currentIndex != -1 || selectedItem == null || newIndex != -1) {
            setSelectedItem(getModelItem(getSelectedIndex()));
        }
    }
}
