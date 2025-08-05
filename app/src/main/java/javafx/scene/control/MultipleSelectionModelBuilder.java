package javafx.scene.control;

import java.util.Arrays;
import java.util.Collection;
import javafx.scene.control.MultipleSelectionModelBuilder;

@Deprecated
/* loaded from: jfxrt.jar:javafx/scene/control/MultipleSelectionModelBuilder.class */
public abstract class MultipleSelectionModelBuilder<T, B extends MultipleSelectionModelBuilder<T, B>> {
    private int __set;
    private Collection<? extends Integer> selectedIndices;
    private Collection<? extends T> selectedItems;
    private SelectionMode selectionMode;

    protected MultipleSelectionModelBuilder() {
    }

    public void applyTo(MultipleSelectionModel<T> x2) {
        int set = this.__set;
        if ((set & 1) != 0) {
            x2.getSelectedIndices().addAll(this.selectedIndices);
        }
        if ((set & 2) != 0) {
            x2.getSelectedItems().addAll(this.selectedItems);
        }
        if ((set & 4) != 0) {
            x2.setSelectionMode(this.selectionMode);
        }
    }

    public B selectedIndices(Collection<? extends Integer> x2) {
        this.selectedIndices = x2;
        this.__set |= 1;
        return this;
    }

    public B selectedIndices(Integer... numArr) {
        return (B) selectedIndices(Arrays.asList(numArr));
    }

    public B selectedItems(Collection<? extends T> x2) {
        this.selectedItems = x2;
        this.__set |= 2;
        return this;
    }

    public B selectedItems(T... tArr) {
        return (B) selectedItems(Arrays.asList(tArr));
    }

    public B selectionMode(SelectionMode x2) {
        this.selectionMode = x2;
        this.__set |= 4;
        return this;
    }
}
