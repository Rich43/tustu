package javafx.scene.control;

import com.sun.javafx.collections.MappingChange;
import com.sun.javafx.collections.NonIterableChange;
import com.sun.javafx.scene.control.ReadOnlyUnbackedObservableList;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collections;
import java.util.List;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.util.Callback;

/* loaded from: jfxrt.jar:javafx/scene/control/MultipleSelectionModelBase.class */
abstract class MultipleSelectionModelBase<T> extends MultipleSelectionModel<T> {
    final BitSet selectedIndices;
    final MultipleSelectionModelBase<T>.BitSetReadOnlyUnbackedObservableList selectedIndicesSeq;
    private final ReadOnlyUnbackedObservableList<T> selectedItemsSeq;
    ListChangeListener.Change selectedItemChange;
    private int atomicityCount = 0;

    protected abstract int getItemCount();

    protected abstract T getModelItem(int i2);

    protected abstract void focus(int i2);

    protected abstract int getFocusedIndex();

    public MultipleSelectionModelBase() {
        selectedIndexProperty().addListener(valueModel -> {
            setSelectedItem(getModelItem(getSelectedIndex()));
        });
        this.selectedIndices = new BitSet();
        this.selectedIndicesSeq = new BitSetReadOnlyUnbackedObservableList(this.selectedIndices);
        final MappingChange.Map<Integer, T> map = f2 -> {
            return getModelItem(f2.intValue());
        };
        this.selectedIndicesSeq.addListener(new ListChangeListener<Integer>() { // from class: javafx.scene.control.MultipleSelectionModelBase.1
            @Override // javafx.collections.ListChangeListener
            public void onChanged(ListChangeListener.Change<? extends Integer> c2) {
                boolean hasRealChangeOccurred;
                boolean z2 = false;
                while (true) {
                    hasRealChangeOccurred = z2;
                    if (!c2.next() || hasRealChangeOccurred) {
                        break;
                    } else {
                        z2 = c2.wasAdded() || c2.wasRemoved();
                    }
                }
                if (hasRealChangeOccurred) {
                    if (MultipleSelectionModelBase.this.selectedItemChange != null) {
                        MultipleSelectionModelBase.this.selectedItemsSeq.callObservers(MultipleSelectionModelBase.this.selectedItemChange);
                    } else {
                        c2.reset();
                        MultipleSelectionModelBase.this.selectedItemsSeq.callObservers(new MappingChange(c2, map, MultipleSelectionModelBase.this.selectedItemsSeq));
                    }
                }
                c2.reset();
            }
        });
        this.selectedItemsSeq = new ReadOnlyUnbackedObservableList<T>() { // from class: javafx.scene.control.MultipleSelectionModelBase.2
            @Override // com.sun.javafx.scene.control.ReadOnlyUnbackedObservableList, java.util.List
            public T get(int i2) {
                return (T) MultipleSelectionModelBase.this.getModelItem(MultipleSelectionModelBase.this.selectedIndicesSeq.get(i2).intValue());
            }

            @Override // com.sun.javafx.scene.control.ReadOnlyUnbackedObservableList, java.util.List, java.util.Collection, java.util.Set
            public int size() {
                return MultipleSelectionModelBase.this.selectedIndices.cardinality();
            }
        };
    }

    @Override // javafx.scene.control.MultipleSelectionModel
    public ObservableList<Integer> getSelectedIndices() {
        return this.selectedIndicesSeq;
    }

    @Override // javafx.scene.control.MultipleSelectionModel
    public ObservableList<T> getSelectedItems() {
        return this.selectedItemsSeq;
    }

    boolean isAtomic() {
        return this.atomicityCount > 0;
    }

    void startAtomic() {
        this.atomicityCount++;
    }

    void stopAtomic() {
        int i2 = this.atomicityCount - 1;
        this.atomicityCount = i2;
        this.atomicityCount = Math.max(0, i2);
    }

    /* loaded from: jfxrt.jar:javafx/scene/control/MultipleSelectionModelBase$ShiftParams.class */
    static class ShiftParams {
        private final int clearIndex;
        private final int setIndex;
        private final boolean selected;

        ShiftParams(int clearIndex, int setIndex, boolean selected) {
            this.clearIndex = clearIndex;
            this.setIndex = setIndex;
            this.selected = selected;
        }

        public final int getClearIndex() {
            return this.clearIndex;
        }

        public final int getSetIndex() {
            return this.setIndex;
        }

        public final boolean isSelected() {
            return this.selected;
        }
    }

    void shiftSelection(int position, int shift, Callback<ShiftParams, Void> callback) {
        int selectedIndicesCardinality;
        if (position < 0 || shift == 0 || (selectedIndicesCardinality = this.selectedIndices.cardinality()) == 0) {
            return;
        }
        int selectedIndicesSize = this.selectedIndices.size();
        int[] perm = new int[selectedIndicesSize];
        int idx = 0;
        boolean hasPermutated = false;
        if (shift > 0) {
            for (int i2 = selectedIndicesSize - 1; i2 >= position && i2 >= 0; i2--) {
                boolean selected = this.selectedIndices.get(i2);
                if (callback == null) {
                    this.selectedIndices.clear(i2);
                    this.selectedIndices.set(i2 + shift, selected);
                } else {
                    callback.call(new ShiftParams(i2, i2 + shift, selected));
                }
                if (selected) {
                    int i3 = idx;
                    idx++;
                    perm[i3] = i2 + 1;
                    hasPermutated = true;
                }
            }
            this.selectedIndices.clear(position);
        } else if (shift < 0) {
            for (int i4 = position; i4 < selectedIndicesSize; i4++) {
                if (i4 + shift >= 0 && i4 + 1 + shift >= position) {
                    boolean selected2 = this.selectedIndices.get(i4 + 1);
                    if (callback == null) {
                        this.selectedIndices.clear(i4 + 1);
                        this.selectedIndices.set(i4 + 1 + shift, selected2);
                    } else {
                        callback.call(new ShiftParams(i4 + 1, i4 + 1 + shift, selected2));
                    }
                    if (selected2) {
                        int i5 = idx;
                        idx++;
                        perm[i5] = i4;
                        hasPermutated = true;
                    }
                }
            }
        }
        int selectedIndex = getSelectedIndex();
        if (selectedIndex >= position && selectedIndex > -1) {
            int newSelectionLead = Math.max(0, selectedIndex + shift);
            setSelectedIndex(newSelectionLead);
            if (hasPermutated) {
                this.selectedIndices.set(newSelectionLead, true);
            } else {
                select(newSelectionLead);
            }
        }
        if (hasPermutated) {
            this.selectedIndicesSeq.callObservers(new NonIterableChange.SimplePermutationChange(0, selectedIndicesCardinality, perm, this.selectedIndicesSeq));
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v23, types: [javafx.collections.ListChangeListener$Change] */
    @Override // javafx.scene.control.SelectionModel
    public void clearAndSelect(int row) {
        NonIterableChange.GenericAddRemoveChange genericAddRemoveChange;
        if (row < 0 || row >= getItemCount()) {
            clearSelection();
            return;
        }
        boolean wasSelected = isSelected(row);
        if (wasSelected && getSelectedIndices().size() == 1 && getSelectedItem() == getModelItem(row)) {
            return;
        }
        BitSet selectedIndicesCopy = new BitSet();
        selectedIndicesCopy.or(this.selectedIndices);
        selectedIndicesCopy.clear(row);
        List<Integer> previousSelectedIndices = new BitSetReadOnlyUnbackedObservableList(selectedIndicesCopy);
        startAtomic();
        clearSelection();
        select(row);
        stopAtomic();
        if (wasSelected) {
            genericAddRemoveChange = ControlUtils.buildClearAndSelectChange(this.selectedIndicesSeq, previousSelectedIndices, row);
        } else {
            int changeIndex = this.selectedIndicesSeq.indexOf(Integer.valueOf(row));
            genericAddRemoveChange = new NonIterableChange.GenericAddRemoveChange(changeIndex, changeIndex + 1, previousSelectedIndices, this.selectedIndicesSeq);
        }
        this.selectedIndicesSeq.callObservers(genericAddRemoveChange);
    }

    @Override // javafx.scene.control.SelectionModel
    public void select(int row) {
        if (row == -1) {
            clearSelection();
            return;
        }
        if (row < 0 || row >= getItemCount()) {
            return;
        }
        boolean isSameRow = row == getSelectedIndex();
        T currentItem = getSelectedItem();
        T newItem = getModelItem(row);
        boolean isSameItem = newItem != null && newItem.equals(currentItem);
        boolean fireUpdatedItemEvent = isSameRow && !isSameItem;
        startAtomic();
        if (!this.selectedIndices.get(row)) {
            if (getSelectionMode() == SelectionMode.SINGLE) {
                quietClearSelection();
            }
            this.selectedIndices.set(row);
        }
        setSelectedIndex(row);
        focus(row);
        stopAtomic();
        if (!isAtomic()) {
            int changeIndex = Math.max(0, this.selectedIndicesSeq.indexOf(Integer.valueOf(row)));
            this.selectedIndicesSeq.callObservers(new NonIterableChange.SimpleAddChange(changeIndex, changeIndex + 1, this.selectedIndicesSeq));
        }
        if (fireUpdatedItemEvent) {
            setSelectedItem(newItem);
        }
    }

    @Override // javafx.scene.control.SelectionModel
    public void select(T obj) {
        if (obj == null && getSelectionMode() == SelectionMode.SINGLE) {
            clearSelection();
            return;
        }
        int max = getItemCount();
        for (int i2 = 0; i2 < max; i2++) {
            Object rowObj = getModelItem(i2);
            if (rowObj != null && rowObj.equals(obj)) {
                if (isSelected(i2)) {
                    return;
                }
                if (getSelectionMode() == SelectionMode.SINGLE) {
                    quietClearSelection();
                }
                select(i2);
                return;
            }
        }
        setSelectedIndex(-1);
        setSelectedItem(obj);
    }

    @Override // javafx.scene.control.MultipleSelectionModel
    public void selectIndices(int row, int... rows) {
        if (rows == null || rows.length == 0) {
            select(row);
            return;
        }
        int rowCount = getItemCount();
        if (getSelectionMode() == SelectionMode.SINGLE) {
            quietClearSelection();
            int i2 = rows.length - 1;
            while (true) {
                if (i2 < 0) {
                    break;
                }
                int index = rows[i2];
                if (index >= 0 && index < rowCount) {
                    this.selectedIndices.set(index);
                    select(index);
                    break;
                }
                i2--;
            }
            if (this.selectedIndices.isEmpty() && row > 0 && row < rowCount) {
                this.selectedIndices.set(row);
                select(row);
            }
            this.selectedIndicesSeq.callObservers(new NonIterableChange.SimpleAddChange(0, 1, this.selectedIndicesSeq));
            return;
        }
        List<Integer> actualSelectedRows = new ArrayList<>();
        int lastIndex = -1;
        if (row >= 0 && row < rowCount) {
            lastIndex = row;
            if (!this.selectedIndices.get(row)) {
                this.selectedIndices.set(row);
                actualSelectedRows.add(Integer.valueOf(row));
            }
        }
        for (int index2 : rows) {
            if (index2 >= 0 && index2 < rowCount) {
                lastIndex = index2;
                if (!this.selectedIndices.get(index2)) {
                    this.selectedIndices.set(index2);
                    actualSelectedRows.add(Integer.valueOf(index2));
                }
            }
        }
        if (lastIndex != -1) {
            setSelectedIndex(lastIndex);
            focus(lastIndex);
            setSelectedItem(getModelItem(lastIndex));
        }
        Collections.sort(actualSelectedRows);
        ListChangeListener.Change<Integer> change = createRangeChange(this.selectedIndicesSeq, actualSelectedRows, false);
        this.selectedIndicesSeq.callObservers(change);
    }

    static ListChangeListener.Change<Integer> createRangeChange(final ObservableList<Integer> list, final List<Integer> addedItems, final boolean splitChanges) {
        ListChangeListener.Change<Integer> change = new ListChangeListener.Change<Integer>(list) { // from class: javafx.scene.control.MultipleSelectionModelBase.3
            private final int addedSize;
            private final int[] EMPTY_PERM = new int[0];
            private boolean invalid = true;
            private int pos = 0;
            private int from = this.pos;
            private int to = this.pos;

            {
                this.addedSize = addedItems.size();
            }

            @Override // javafx.collections.ListChangeListener.Change
            public int getFrom() {
                checkState();
                return this.from;
            }

            @Override // javafx.collections.ListChangeListener.Change
            public int getTo() {
                checkState();
                return this.to;
            }

            @Override // javafx.collections.ListChangeListener.Change
            public List<Integer> getRemoved() {
                checkState();
                return Collections.emptyList();
            }

            @Override // javafx.collections.ListChangeListener.Change
            protected int[] getPermutation() {
                checkState();
                return this.EMPTY_PERM;
            }

            @Override // javafx.collections.ListChangeListener.Change
            public int getAddedSize() {
                return this.to - this.from;
            }

            @Override // javafx.collections.ListChangeListener.Change
            public boolean next() {
                if (this.pos >= this.addedSize) {
                    return false;
                }
                List list2 = addedItems;
                int i2 = this.pos;
                this.pos = i2 + 1;
                int startValue = ((Integer) list2.get(i2)).intValue();
                this.from = list.indexOf(Integer.valueOf(startValue));
                this.to = this.from + 1;
                int endValue = startValue;
                while (this.pos < this.addedSize) {
                    int previousEndValue = endValue;
                    List list3 = addedItems;
                    int i3 = this.pos;
                    this.pos = i3 + 1;
                    endValue = ((Integer) list3.get(i3)).intValue();
                    this.to++;
                    if (splitChanges && previousEndValue != endValue - 1) {
                        break;
                    }
                }
                if (!this.invalid) {
                    return splitChanges && this.pos < this.addedSize;
                }
                this.invalid = false;
                return true;
            }

            @Override // javafx.collections.ListChangeListener.Change
            public void reset() {
                this.invalid = true;
                this.pos = 0;
                this.to = 0;
                this.from = 0;
            }

            private void checkState() {
                if (this.invalid) {
                    throw new IllegalStateException("Invalid Change state: next() must be called before inspecting the Change.");
                }
            }
        };
        return change;
    }

    @Override // javafx.scene.control.MultipleSelectionModel
    public void selectAll() {
        if (getSelectionMode() != SelectionMode.SINGLE && getItemCount() > 0) {
            int rowCount = getItemCount();
            int focusedIndex = getFocusedIndex();
            clearSelection();
            this.selectedIndices.set(0, rowCount, true);
            this.selectedIndicesSeq.callObservers(new NonIterableChange.SimpleAddChange(0, rowCount, this.selectedIndicesSeq));
            if (focusedIndex == -1) {
                setSelectedIndex(rowCount - 1);
                focus(rowCount - 1);
            } else {
                setSelectedIndex(focusedIndex);
                focus(focusedIndex);
            }
        }
    }

    @Override // javafx.scene.control.MultipleSelectionModel, javafx.scene.control.SelectionModel
    public void selectFirst() {
        if (getSelectionMode() == SelectionMode.SINGLE) {
            quietClearSelection();
        }
        if (getItemCount() > 0) {
            select(0);
        }
    }

    @Override // javafx.scene.control.MultipleSelectionModel, javafx.scene.control.SelectionModel
    public void selectLast() {
        if (getSelectionMode() == SelectionMode.SINGLE) {
            quietClearSelection();
        }
        int numItems = getItemCount();
        if (numItems > 0 && getSelectedIndex() < numItems - 1) {
            select(numItems - 1);
        }
    }

    @Override // javafx.scene.control.SelectionModel
    public void clearSelection(int index) {
        if (index < 0) {
            return;
        }
        boolean wasEmpty = this.selectedIndices.isEmpty();
        this.selectedIndices.clear(index);
        if (!wasEmpty && this.selectedIndices.isEmpty()) {
            clearSelection();
        }
        if (!isAtomic()) {
            this.selectedIndicesSeq.callObservers(new NonIterableChange.GenericAddRemoveChange(index, index, Collections.singletonList(Integer.valueOf(index)), this.selectedIndicesSeq));
        }
    }

    @Override // javafx.scene.control.SelectionModel
    public void clearSelection() {
        List<Integer> removed = new BitSetReadOnlyUnbackedObservableList((BitSet) this.selectedIndices.clone());
        quietClearSelection();
        if (!isAtomic()) {
            setSelectedIndex(-1);
            focus(-1);
            this.selectedIndicesSeq.callObservers(new NonIterableChange.GenericAddRemoveChange(0, 0, removed, this.selectedIndicesSeq));
        }
    }

    private void quietClearSelection() {
        this.selectedIndices.clear();
    }

    @Override // javafx.scene.control.SelectionModel
    public boolean isSelected(int index) {
        if (index >= 0 && index < this.selectedIndices.length()) {
            return this.selectedIndices.get(index);
        }
        return false;
    }

    @Override // javafx.scene.control.SelectionModel
    public boolean isEmpty() {
        return this.selectedIndices.isEmpty();
    }

    @Override // javafx.scene.control.SelectionModel
    public void selectPrevious() {
        int focusIndex = getFocusedIndex();
        if (getSelectionMode() == SelectionMode.SINGLE) {
            quietClearSelection();
        }
        if (focusIndex == -1) {
            select(getItemCount() - 1);
        } else if (focusIndex > 0) {
            select(focusIndex - 1);
        }
    }

    @Override // javafx.scene.control.SelectionModel
    public void selectNext() {
        int focusIndex = getFocusedIndex();
        if (getSelectionMode() == SelectionMode.SINGLE) {
            quietClearSelection();
        }
        if (focusIndex == -1) {
            select(0);
        } else if (focusIndex != getItemCount() - 1) {
            select(focusIndex + 1);
        }
    }

    /* loaded from: jfxrt.jar:javafx/scene/control/MultipleSelectionModelBase$BitSetReadOnlyUnbackedObservableList.class */
    class BitSetReadOnlyUnbackedObservableList extends ReadOnlyUnbackedObservableList<Integer> {
        private final BitSet bitset;
        private int lastGetIndex = -1;
        private int lastGetValue = -1;

        public BitSetReadOnlyUnbackedObservableList(BitSet bitset) {
            this.bitset = bitset;
        }

        @Override // com.sun.javafx.scene.control.ReadOnlyUnbackedObservableList, java.util.List
        public Integer get(int index) {
            int itemCount = MultipleSelectionModelBase.this.getItemCount();
            if (index < 0 || index >= itemCount) {
                return -1;
            }
            if (index == this.lastGetIndex + 1 && this.lastGetValue < itemCount) {
                this.lastGetIndex++;
                this.lastGetValue = this.bitset.nextSetBit(this.lastGetValue + 1);
                return Integer.valueOf(this.lastGetValue);
            }
            if (index == this.lastGetIndex - 1 && this.lastGetValue > 0) {
                this.lastGetIndex--;
                this.lastGetValue = this.bitset.previousSetBit(this.lastGetValue - 1);
                return Integer.valueOf(this.lastGetValue);
            }
            this.lastGetIndex = 0;
            this.lastGetValue = this.bitset.nextSetBit(0);
            while (true) {
                if (this.lastGetValue >= 0 || this.lastGetIndex == index) {
                    if (this.lastGetIndex != index) {
                        this.lastGetIndex++;
                        this.lastGetValue = this.bitset.nextSetBit(this.lastGetValue + 1);
                    } else {
                        return Integer.valueOf(this.lastGetValue);
                    }
                } else {
                    return -1;
                }
            }
        }

        @Override // com.sun.javafx.scene.control.ReadOnlyUnbackedObservableList, java.util.List, java.util.Collection, java.util.Set
        public int size() {
            return this.bitset.cardinality();
        }

        @Override // com.sun.javafx.scene.control.ReadOnlyUnbackedObservableList, java.util.List, java.util.Collection, java.util.Set
        public boolean contains(Object o2) {
            if (o2 instanceof Number) {
                Number n2 = (Number) o2;
                int index = n2.intValue();
                return index >= 0 && index < this.bitset.length() && this.bitset.get(index);
            }
            return false;
        }

        public void reset() {
            this.lastGetIndex = -1;
            this.lastGetValue = -1;
        }
    }
}
