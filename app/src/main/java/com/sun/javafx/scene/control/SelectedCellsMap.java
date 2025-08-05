package com.sun.javafx.scene.control;

import java.util.BitSet;
import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.scene.control.TablePositionBase;

/* loaded from: jfxrt.jar:com/sun/javafx/scene/control/SelectedCellsMap.class */
public abstract class SelectedCellsMap<T extends TablePositionBase> {
    private final ObservableList<T> selectedCells = FXCollections.observableArrayList();
    private final ObservableList<T> sortedSelectedCells = new SortedList(this.selectedCells, (o1, o2) -> {
        int result = o1.getRow() - o2.getRow();
        return result == 0 ? o1.getColumn() - o2.getColumn() : result;
    });
    private final Map<Integer, BitSet> selectedCellBitSetMap;

    public abstract boolean isCellSelectionEnabled();

    public SelectedCellsMap(ListChangeListener<T> listener) {
        this.sortedSelectedCells.addListener((ListChangeListener<? super T>) listener);
        this.selectedCellBitSetMap = new TreeMap((o1, o2) -> {
            return o1.compareTo(o2);
        });
    }

    public int size() {
        return this.selectedCells.size();
    }

    public T get(int i2) {
        if (i2 < 0) {
            return null;
        }
        return this.sortedSelectedCells.get(i2);
    }

    public void add(T tp) {
        BitSet bitset;
        int row = tp.getRow();
        int columnIndex = tp.getColumn();
        boolean isNewBitSet = false;
        if (!this.selectedCellBitSetMap.containsKey(Integer.valueOf(row))) {
            bitset = new BitSet();
            this.selectedCellBitSetMap.put(Integer.valueOf(row), bitset);
            isNewBitSet = true;
        } else {
            bitset = this.selectedCellBitSetMap.get(Integer.valueOf(row));
        }
        boolean cellSelectionModeEnabled = isCellSelectionEnabled();
        if (cellSelectionModeEnabled) {
            if (columnIndex >= 0) {
                boolean isAlreadySet = bitset.get(columnIndex);
                if (!isAlreadySet) {
                    bitset.set(columnIndex);
                    this.selectedCells.add(tp);
                    return;
                }
                return;
            }
            if (!this.selectedCells.contains(tp)) {
                this.selectedCells.add(tp);
                return;
            }
            return;
        }
        if (isNewBitSet) {
            if (columnIndex >= 0) {
                bitset.set(columnIndex);
            }
            this.selectedCells.add(tp);
        }
    }

    public void addAll(Collection<T> cells) {
        BitSet bitset;
        for (T tp : cells) {
            int row = tp.getRow();
            int columnIndex = tp.getColumn();
            if (!this.selectedCellBitSetMap.containsKey(Integer.valueOf(row))) {
                bitset = new BitSet();
                this.selectedCellBitSetMap.put(Integer.valueOf(row), bitset);
            } else {
                bitset = this.selectedCellBitSetMap.get(Integer.valueOf(row));
            }
            if (columnIndex >= 0) {
                bitset.set(columnIndex);
            }
        }
        this.selectedCells.addAll((Collection<? extends T>) cells);
    }

    public void setAll(Collection<T> cells) {
        BitSet bitset;
        this.selectedCellBitSetMap.clear();
        for (T tp : cells) {
            int row = tp.getRow();
            int columnIndex = tp.getColumn();
            if (!this.selectedCellBitSetMap.containsKey(Integer.valueOf(row))) {
                bitset = new BitSet();
                this.selectedCellBitSetMap.put(Integer.valueOf(row), bitset);
            } else {
                bitset = this.selectedCellBitSetMap.get(Integer.valueOf(row));
            }
            if (columnIndex >= 0) {
                bitset.set(columnIndex);
            }
        }
        this.selectedCells.setAll((Collection<? extends T>) cells);
    }

    public void remove(T tp) {
        int row = tp.getRow();
        int columnIndex = tp.getColumn();
        if (this.selectedCellBitSetMap.containsKey(Integer.valueOf(row))) {
            BitSet bitset = this.selectedCellBitSetMap.get(Integer.valueOf(row));
            if (columnIndex >= 0) {
                bitset.clear(columnIndex);
            }
            if (bitset.isEmpty()) {
                this.selectedCellBitSetMap.remove(Integer.valueOf(row));
            }
        }
        this.selectedCells.remove(tp);
    }

    public void clear() {
        this.selectedCellBitSetMap.clear();
        this.selectedCells.clear();
    }

    public boolean isSelected(int row, int columnIndex) {
        if (columnIndex < 0) {
            return this.selectedCellBitSetMap.containsKey(Integer.valueOf(row));
        }
        if (this.selectedCellBitSetMap.containsKey(Integer.valueOf(row))) {
            return this.selectedCellBitSetMap.get(Integer.valueOf(row)).get(columnIndex);
        }
        return false;
    }

    public int indexOf(T tp) {
        return this.sortedSelectedCells.indexOf(tp);
    }

    public boolean isEmpty() {
        return this.selectedCells.isEmpty();
    }

    public ObservableList<T> getSelectedCells() {
        return this.selectedCells;
    }
}
