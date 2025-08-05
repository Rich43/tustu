package javafx.scene.control;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.ObservableList;

/* loaded from: jfxrt.jar:javafx/scene/control/TableSelectionModel.class */
public abstract class TableSelectionModel<T> extends MultipleSelectionModelBase<T> {
    private BooleanProperty cellSelectionEnabled = new SimpleBooleanProperty(this, "cellSelectionEnabled");

    public abstract boolean isSelected(int i2, TableColumnBase<T, ?> tableColumnBase);

    public abstract void select(int i2, TableColumnBase<T, ?> tableColumnBase);

    public abstract void clearAndSelect(int i2, TableColumnBase<T, ?> tableColumnBase);

    public abstract void clearSelection(int i2, TableColumnBase<T, ?> tableColumnBase);

    public abstract void selectLeftCell();

    public abstract void selectRightCell();

    public abstract void selectAboveCell();

    public abstract void selectBelowCell();

    public abstract void selectRange(int i2, TableColumnBase<T, ?> tableColumnBase, int i3, TableColumnBase<T, ?> tableColumnBase2);

    @Override // javafx.scene.control.MultipleSelectionModelBase, javafx.scene.control.SelectionModel
    public /* bridge */ /* synthetic */ void selectNext() {
        super.selectNext();
    }

    @Override // javafx.scene.control.MultipleSelectionModelBase, javafx.scene.control.SelectionModel
    public /* bridge */ /* synthetic */ void selectPrevious() {
        super.selectPrevious();
    }

    @Override // javafx.scene.control.MultipleSelectionModelBase, javafx.scene.control.SelectionModel
    public /* bridge */ /* synthetic */ boolean isEmpty() {
        return super.isEmpty();
    }

    @Override // javafx.scene.control.MultipleSelectionModelBase, javafx.scene.control.SelectionModel
    public /* bridge */ /* synthetic */ boolean isSelected(int i2) {
        return super.isSelected(i2);
    }

    @Override // javafx.scene.control.MultipleSelectionModelBase, javafx.scene.control.SelectionModel
    public /* bridge */ /* synthetic */ void clearSelection() {
        super.clearSelection();
    }

    @Override // javafx.scene.control.MultipleSelectionModelBase, javafx.scene.control.SelectionModel
    public /* bridge */ /* synthetic */ void clearSelection(int i2) {
        super.clearSelection(i2);
    }

    @Override // javafx.scene.control.MultipleSelectionModelBase, javafx.scene.control.MultipleSelectionModel, javafx.scene.control.SelectionModel
    public /* bridge */ /* synthetic */ void selectLast() {
        super.selectLast();
    }

    @Override // javafx.scene.control.MultipleSelectionModelBase, javafx.scene.control.MultipleSelectionModel, javafx.scene.control.SelectionModel
    public /* bridge */ /* synthetic */ void selectFirst() {
        super.selectFirst();
    }

    @Override // javafx.scene.control.MultipleSelectionModelBase, javafx.scene.control.MultipleSelectionModel
    public /* bridge */ /* synthetic */ void selectAll() {
        super.selectAll();
    }

    @Override // javafx.scene.control.MultipleSelectionModelBase, javafx.scene.control.MultipleSelectionModel
    public /* bridge */ /* synthetic */ void selectIndices(int i2, int[] iArr) {
        super.selectIndices(i2, iArr);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // javafx.scene.control.MultipleSelectionModelBase, javafx.scene.control.SelectionModel
    public /* bridge */ /* synthetic */ void select(Object obj) {
        super.select((TableSelectionModel<T>) obj);
    }

    @Override // javafx.scene.control.MultipleSelectionModelBase, javafx.scene.control.SelectionModel
    public /* bridge */ /* synthetic */ void select(int i2) {
        super.select(i2);
    }

    @Override // javafx.scene.control.MultipleSelectionModelBase, javafx.scene.control.SelectionModel
    public /* bridge */ /* synthetic */ void clearAndSelect(int i2) {
        super.clearAndSelect(i2);
    }

    @Override // javafx.scene.control.MultipleSelectionModelBase, javafx.scene.control.MultipleSelectionModel
    public /* bridge */ /* synthetic */ ObservableList getSelectedItems() {
        return super.getSelectedItems();
    }

    @Override // javafx.scene.control.MultipleSelectionModelBase, javafx.scene.control.MultipleSelectionModel
    public /* bridge */ /* synthetic */ ObservableList getSelectedIndices() {
        return super.getSelectedIndices();
    }

    public final BooleanProperty cellSelectionEnabledProperty() {
        return this.cellSelectionEnabled;
    }

    public final void setCellSelectionEnabled(boolean value) {
        cellSelectionEnabledProperty().set(value);
    }

    public final boolean isCellSelectionEnabled() {
        if (this.cellSelectionEnabled == null) {
            return false;
        }
        return this.cellSelectionEnabled.get();
    }
}
