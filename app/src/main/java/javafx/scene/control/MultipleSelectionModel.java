package javafx.scene.control;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.collections.ObservableList;
import javax.swing.tree.DefaultTreeSelectionModel;

/* loaded from: jfxrt.jar:javafx/scene/control/MultipleSelectionModel.class */
public abstract class MultipleSelectionModel<T> extends SelectionModel<T> {
    private ObjectProperty<SelectionMode> selectionMode;

    public abstract ObservableList<Integer> getSelectedIndices();

    public abstract ObservableList<T> getSelectedItems();

    public abstract void selectIndices(int i2, int... iArr);

    public abstract void selectAll();

    @Override // javafx.scene.control.SelectionModel
    public abstract void selectFirst();

    @Override // javafx.scene.control.SelectionModel
    public abstract void selectLast();

    public final void setSelectionMode(SelectionMode value) {
        selectionModeProperty().set(value);
    }

    public final SelectionMode getSelectionMode() {
        return this.selectionMode == null ? SelectionMode.SINGLE : this.selectionMode.get();
    }

    public final ObjectProperty<SelectionMode> selectionModeProperty() {
        if (this.selectionMode == null) {
            this.selectionMode = new ObjectPropertyBase<SelectionMode>(SelectionMode.SINGLE) { // from class: javafx.scene.control.MultipleSelectionModel.1
                @Override // javafx.beans.property.ObjectPropertyBase
                protected void invalidated() {
                    if (MultipleSelectionModel.this.getSelectionMode() == SelectionMode.SINGLE && !MultipleSelectionModel.this.isEmpty()) {
                        int lastIndex = MultipleSelectionModel.this.getSelectedIndex();
                        MultipleSelectionModel.this.clearSelection();
                        MultipleSelectionModel.this.select(lastIndex);
                    }
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return MultipleSelectionModel.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return DefaultTreeSelectionModel.SELECTION_MODE_PROPERTY;
                }
            };
        }
        return this.selectionMode;
    }

    public void selectRange(int start, int end) {
        int i2;
        int startValue;
        int i3;
        if (start == end) {
            return;
        }
        boolean asc = start < end;
        int low = asc ? start : end;
        int high = asc ? end : start;
        int arrayLength = (high - low) - 1;
        int[] indices = new int[arrayLength];
        int startValue2 = asc ? low : high;
        if (asc) {
            i2 = startValue2;
            startValue = startValue2 + 1;
        } else {
            i2 = startValue2;
            startValue = startValue2 - 1;
        }
        int firstVal = i2;
        for (int i4 = 0; i4 < arrayLength; i4++) {
            int i5 = i4;
            if (asc) {
                i3 = startValue;
                startValue++;
            } else {
                i3 = startValue;
                startValue--;
            }
            indices[i5] = i3;
        }
        selectIndices(firstVal, indices);
    }
}
