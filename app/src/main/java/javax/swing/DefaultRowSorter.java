package javax.swing;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.swing.RowFilter;
import javax.swing.RowSorter;

/* loaded from: rt.jar:javax/swing/DefaultRowSorter.class */
public abstract class DefaultRowSorter<M, I> extends RowSorter<M> {
    private boolean sortsOnUpdates;
    private Row[] viewToModel;
    private int[] modelToView;
    private Comparator[] comparators;
    private boolean[] isSortable;
    private RowSorter.SortKey[] cachedSortKeys;
    private Comparator[] sortComparators;
    private RowFilter<? super M, ? super I> filter;
    private DefaultRowSorter<M, I>.FilterEntry filterEntry;
    private boolean[] useToString;
    private boolean sorted;
    private ModelWrapper<M, I> modelWrapper;
    private int modelRowCount;
    private List<RowSorter.SortKey> sortKeys = Collections.emptyList();
    private int maxSortKeys = 3;

    protected final void setModelWrapper(ModelWrapper<M, I> modelWrapper) {
        if (modelWrapper == null) {
            throw new IllegalArgumentException("modelWrapper most be non-null");
        }
        ModelWrapper<M, I> modelWrapper2 = this.modelWrapper;
        this.modelWrapper = modelWrapper;
        if (modelWrapper2 != null) {
            modelStructureChanged();
        } else {
            this.modelRowCount = getModelWrapper().getRowCount();
        }
    }

    protected final ModelWrapper<M, I> getModelWrapper() {
        return this.modelWrapper;
    }

    @Override // javax.swing.RowSorter
    public final M getModel() {
        return getModelWrapper().getModel();
    }

    public void setSortable(int i2, boolean z2) {
        checkColumn(i2);
        if (this.isSortable == null) {
            this.isSortable = new boolean[getModelWrapper().getColumnCount()];
            for (int length = this.isSortable.length - 1; length >= 0; length--) {
                this.isSortable[length] = true;
            }
        }
        this.isSortable[i2] = z2;
    }

    public boolean isSortable(int i2) {
        checkColumn(i2);
        if (this.isSortable == null) {
            return true;
        }
        return this.isSortable[i2];
    }

    @Override // javax.swing.RowSorter
    public void setSortKeys(List<? extends RowSorter.SortKey> list) {
        List<RowSorter.SortKey> list2 = this.sortKeys;
        if (list != null && list.size() > 0) {
            int columnCount = getModelWrapper().getColumnCount();
            for (RowSorter.SortKey sortKey : list) {
                if (sortKey == null || sortKey.getColumn() < 0 || sortKey.getColumn() >= columnCount) {
                    throw new IllegalArgumentException("Invalid SortKey");
                }
            }
            this.sortKeys = Collections.unmodifiableList(new ArrayList(list));
        } else {
            this.sortKeys = Collections.emptyList();
        }
        if (!this.sortKeys.equals(list2)) {
            fireSortOrderChanged();
            if (this.viewToModel == null) {
                sort();
            } else {
                sortExistingData();
            }
        }
    }

    @Override // javax.swing.RowSorter
    public List<? extends RowSorter.SortKey> getSortKeys() {
        return this.sortKeys;
    }

    public void setMaxSortKeys(int i2) {
        if (i2 < 1) {
            throw new IllegalArgumentException("Invalid max");
        }
        this.maxSortKeys = i2;
    }

    public int getMaxSortKeys() {
        return this.maxSortKeys;
    }

    public void setSortsOnUpdates(boolean z2) {
        this.sortsOnUpdates = z2;
    }

    public boolean getSortsOnUpdates() {
        return this.sortsOnUpdates;
    }

    public void setRowFilter(RowFilter<? super M, ? super I> rowFilter) {
        this.filter = rowFilter;
        sort();
    }

    public RowFilter<? super M, ? super I> getRowFilter() {
        return this.filter;
    }

    @Override // javax.swing.RowSorter
    public void toggleSortOrder(int i2) {
        checkColumn(i2);
        if (isSortable(i2)) {
            List<? extends RowSorter.SortKey> arrayList = new ArrayList(getSortKeys());
            int size = arrayList.size() - 1;
            while (size >= 0 && arrayList.get(size).getColumn() != i2) {
                size--;
            }
            if (size == -1) {
                arrayList.add(0, new RowSorter.SortKey(i2, SortOrder.ASCENDING));
            } else if (size == 0) {
                arrayList.set(0, toggle(arrayList.get(0)));
            } else {
                arrayList.remove(size);
                arrayList.add(0, new RowSorter.SortKey(i2, SortOrder.ASCENDING));
            }
            if (arrayList.size() > getMaxSortKeys()) {
                arrayList = arrayList.subList(0, getMaxSortKeys());
            }
            setSortKeys(arrayList);
        }
    }

    private RowSorter.SortKey toggle(RowSorter.SortKey sortKey) {
        if (sortKey.getSortOrder() == SortOrder.ASCENDING) {
            return new RowSorter.SortKey(sortKey.getColumn(), SortOrder.DESCENDING);
        }
        return new RowSorter.SortKey(sortKey.getColumn(), SortOrder.ASCENDING);
    }

    @Override // javax.swing.RowSorter
    public int convertRowIndexToView(int i2) {
        if (this.modelToView == null) {
            if (i2 < 0 || i2 >= getModelWrapper().getRowCount()) {
                throw new IndexOutOfBoundsException("Invalid index");
            }
            return i2;
        }
        return this.modelToView[i2];
    }

    @Override // javax.swing.RowSorter
    public int convertRowIndexToModel(int i2) {
        if (this.viewToModel == null) {
            if (i2 < 0 || i2 >= getModelWrapper().getRowCount()) {
                throw new IndexOutOfBoundsException("Invalid index");
            }
            return i2;
        }
        return this.viewToModel[i2].modelIndex;
    }

    private boolean isUnsorted() {
        List<? extends RowSorter.SortKey> sortKeys = getSortKeys();
        return sortKeys.size() == 0 || sortKeys.get(0).getSortOrder() == SortOrder.UNSORTED;
    }

    private void sortExistingData() {
        int[] viewToModelAsInts = getViewToModelAsInts(this.viewToModel);
        updateUseToString();
        cacheSortKeys(getSortKeys());
        if (isUnsorted()) {
            if (getRowFilter() == null) {
                this.viewToModel = null;
                this.modelToView = null;
            } else {
                int i2 = 0;
                for (int i3 = 0; i3 < this.modelToView.length; i3++) {
                    if (this.modelToView[i3] != -1) {
                        this.viewToModel[i2].modelIndex = i3;
                        int i4 = i2;
                        i2++;
                        this.modelToView[i3] = i4;
                    }
                }
            }
        } else {
            Arrays.sort(this.viewToModel);
            setModelToViewFromViewToModel(false);
        }
        fireRowSorterChanged(viewToModelAsInts);
    }

    public void sort() {
        this.sorted = true;
        int[] viewToModelAsInts = getViewToModelAsInts(this.viewToModel);
        updateUseToString();
        if (isUnsorted()) {
            this.cachedSortKeys = new RowSorter.SortKey[0];
            if (getRowFilter() == null) {
                if (this.viewToModel != null) {
                    this.viewToModel = null;
                    this.modelToView = null;
                } else {
                    return;
                }
            } else {
                initializeFilteredMapping();
            }
        } else {
            cacheSortKeys(getSortKeys());
            if (getRowFilter() != null) {
                initializeFilteredMapping();
            } else {
                createModelToView(getModelWrapper().getRowCount());
                createViewToModel(getModelWrapper().getRowCount());
            }
            Arrays.sort(this.viewToModel);
            setModelToViewFromViewToModel(false);
        }
        fireRowSorterChanged(viewToModelAsInts);
    }

    private void updateUseToString() {
        int columnCount = getModelWrapper().getColumnCount();
        if (this.useToString == null || this.useToString.length != columnCount) {
            this.useToString = new boolean[columnCount];
        }
        while (true) {
            columnCount--;
            if (columnCount >= 0) {
                this.useToString[columnCount] = useToString(columnCount);
            } else {
                return;
            }
        }
    }

    private void initializeFilteredMapping() {
        int rowCount = getModelWrapper().getRowCount();
        int i2 = 0;
        createModelToView(rowCount);
        for (int i3 = 0; i3 < rowCount; i3++) {
            if (include(i3)) {
                this.modelToView[i3] = i3 - i2;
            } else {
                this.modelToView[i3] = -1;
                i2++;
            }
        }
        createViewToModel(rowCount - i2);
        int i4 = 0;
        for (int i5 = 0; i5 < rowCount; i5++) {
            if (this.modelToView[i5] != -1) {
                int i6 = i4;
                i4++;
                this.viewToModel[i6].modelIndex = i5;
            }
        }
    }

    private void createModelToView(int i2) {
        if (this.modelToView == null || this.modelToView.length != i2) {
            this.modelToView = new int[i2];
        }
    }

    private void createViewToModel(int i2) {
        int iMin = 0;
        if (this.viewToModel != null) {
            iMin = Math.min(i2, this.viewToModel.length);
            if (this.viewToModel.length != i2) {
                Row[] rowArr = this.viewToModel;
                this.viewToModel = new Row[i2];
                System.arraycopy(rowArr, 0, this.viewToModel, 0, iMin);
            }
        } else {
            this.viewToModel = new Row[i2];
        }
        for (int i3 = 0; i3 < iMin; i3++) {
            this.viewToModel[i3].modelIndex = i3;
        }
        for (int i4 = iMin; i4 < i2; i4++) {
            this.viewToModel[i4] = new Row(this, i4);
        }
    }

    private void cacheSortKeys(List<? extends RowSorter.SortKey> list) {
        int size = list.size();
        this.sortComparators = new Comparator[size];
        for (int i2 = 0; i2 < size; i2++) {
            this.sortComparators[i2] = getComparator0(list.get(i2).getColumn());
        }
        this.cachedSortKeys = (RowSorter.SortKey[]) list.toArray(new RowSorter.SortKey[size]);
    }

    protected boolean useToString(int i2) {
        return getComparator(i2) == null;
    }

    private void setModelToViewFromViewToModel(boolean z2) {
        if (z2) {
            for (int length = this.modelToView.length - 1; length >= 0; length--) {
                this.modelToView[length] = -1;
            }
        }
        for (int length2 = this.viewToModel.length - 1; length2 >= 0; length2--) {
            this.modelToView[this.viewToModel[length2].modelIndex] = length2;
        }
    }

    private int[] getViewToModelAsInts(Row[] rowArr) {
        if (rowArr != null) {
            int[] iArr = new int[rowArr.length];
            for (int length = rowArr.length - 1; length >= 0; length--) {
                iArr[length] = rowArr[length].modelIndex;
            }
            return iArr;
        }
        return new int[0];
    }

    public void setComparator(int i2, Comparator<?> comparator) {
        checkColumn(i2);
        if (this.comparators == null) {
            this.comparators = new Comparator[getModelWrapper().getColumnCount()];
        }
        this.comparators[i2] = comparator;
    }

    public Comparator<?> getComparator(int i2) {
        checkColumn(i2);
        if (this.comparators != null) {
            return this.comparators[i2];
        }
        return null;
    }

    private Comparator getComparator0(int i2) {
        Comparator<?> comparator = getComparator(i2);
        if (comparator != null) {
            return comparator;
        }
        return Collator.getInstance();
    }

    private RowFilter.Entry<M, I> getFilterEntry(int i2) {
        if (this.filterEntry == null) {
            this.filterEntry = new FilterEntry();
        }
        this.filterEntry.modelIndex = i2;
        return this.filterEntry;
    }

    @Override // javax.swing.RowSorter
    public int getViewRowCount() {
        if (this.viewToModel != null) {
            return this.viewToModel.length;
        }
        return getModelWrapper().getRowCount();
    }

    @Override // javax.swing.RowSorter
    public int getModelRowCount() {
        return getModelWrapper().getRowCount();
    }

    private void allChanged() {
        this.modelToView = null;
        this.viewToModel = null;
        this.comparators = null;
        this.isSortable = null;
        if (isUnsorted()) {
            sort();
        } else {
            setSortKeys(null);
        }
    }

    @Override // javax.swing.RowSorter
    public void modelStructureChanged() {
        allChanged();
        this.modelRowCount = getModelWrapper().getRowCount();
    }

    @Override // javax.swing.RowSorter
    public void allRowsChanged() {
        this.modelRowCount = getModelWrapper().getRowCount();
        sort();
    }

    @Override // javax.swing.RowSorter
    public void rowsInserted(int i2, int i3) {
        checkAgainstModel(i2, i3);
        int rowCount = getModelWrapper().getRowCount();
        if (i3 >= rowCount) {
            throw new IndexOutOfBoundsException("Invalid range");
        }
        this.modelRowCount = rowCount;
        if (shouldOptimizeChange(i2, i3)) {
            rowsInserted0(i2, i3);
        }
    }

    @Override // javax.swing.RowSorter
    public void rowsDeleted(int i2, int i3) {
        checkAgainstModel(i2, i3);
        if (i2 >= this.modelRowCount || i3 >= this.modelRowCount) {
            throw new IndexOutOfBoundsException("Invalid range");
        }
        this.modelRowCount = getModelWrapper().getRowCount();
        if (shouldOptimizeChange(i2, i3)) {
            rowsDeleted0(i2, i3);
        }
    }

    @Override // javax.swing.RowSorter
    public void rowsUpdated(int i2, int i3) {
        checkAgainstModel(i2, i3);
        if (i2 >= this.modelRowCount || i3 >= this.modelRowCount) {
            throw new IndexOutOfBoundsException("Invalid range");
        }
        if (getSortsOnUpdates()) {
            if (shouldOptimizeChange(i2, i3)) {
                rowsUpdated0(i2, i3);
                return;
            }
            return;
        }
        this.sorted = false;
    }

    @Override // javax.swing.RowSorter
    public void rowsUpdated(int i2, int i3, int i4) {
        checkColumn(i4);
        rowsUpdated(i2, i3);
    }

    private void checkAgainstModel(int i2, int i3) {
        if (i2 > i3 || i2 < 0 || i3 < 0 || i2 > this.modelRowCount) {
            throw new IndexOutOfBoundsException("Invalid range");
        }
    }

    private boolean include(int i2) {
        RowFilter<? super M, ? super I> rowFilter = getRowFilter();
        if (rowFilter != null) {
            return rowFilter.include(getFilterEntry(i2));
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int compare(int i2, int i3) {
        Object valueAt;
        Object valueAt2;
        int iCompare;
        for (int i4 = 0; i4 < this.cachedSortKeys.length; i4++) {
            int column = this.cachedSortKeys[i4].getColumn();
            SortOrder sortOrder = this.cachedSortKeys[i4].getSortOrder();
            if (sortOrder == SortOrder.UNSORTED) {
                iCompare = i2 - i3;
            } else {
                if (this.useToString[column]) {
                    valueAt = getModelWrapper().getStringValueAt(i2, column);
                    valueAt2 = getModelWrapper().getStringValueAt(i3, column);
                } else {
                    valueAt = getModelWrapper().getValueAt(i2, column);
                    valueAt2 = getModelWrapper().getValueAt(i3, column);
                }
                if (valueAt == null) {
                    if (valueAt2 == null) {
                        iCompare = 0;
                    } else {
                        iCompare = -1;
                    }
                } else if (valueAt2 == null) {
                    iCompare = 1;
                } else {
                    iCompare = this.sortComparators[i4].compare(valueAt, valueAt2);
                }
                if (sortOrder == SortOrder.DESCENDING) {
                    iCompare *= -1;
                }
            }
            if (iCompare != 0) {
                return iCompare;
            }
        }
        return i2 - i3;
    }

    private boolean isTransformed() {
        return this.viewToModel != null;
    }

    private void insertInOrder(List<Row> list, Row[] rowArr) {
        int i2 = 0;
        int size = list.size();
        for (int i3 = 0; i3 < size; i3++) {
            int iBinarySearch = Arrays.binarySearch(rowArr, list.get(i3));
            if (iBinarySearch < 0) {
                iBinarySearch = (-1) - iBinarySearch;
            }
            System.arraycopy(rowArr, i2, this.viewToModel, i2 + i3, iBinarySearch - i2);
            this.viewToModel[iBinarySearch + i3] = list.get(i3);
            i2 = iBinarySearch;
        }
        System.arraycopy(rowArr, i2, this.viewToModel, i2 + size, rowArr.length - i2);
    }

    private boolean shouldOptimizeChange(int i2, int i3) {
        if (!isTransformed()) {
            return false;
        }
        if (!this.sorted || i3 - i2 > this.viewToModel.length / 10) {
            sort();
            return false;
        }
        return true;
    }

    private void rowsInserted0(int i2, int i3) {
        int[] viewToModelAsInts = getViewToModelAsInts(this.viewToModel);
        int i4 = (i3 - i2) + 1;
        ArrayList arrayList = new ArrayList(i4);
        for (int i5 = i2; i5 <= i3; i5++) {
            if (include(i5)) {
                arrayList.add(new Row(this, i5));
            }
        }
        for (int length = this.modelToView.length - 1; length >= i2; length--) {
            int i6 = this.modelToView[length];
            if (i6 != -1) {
                this.viewToModel[i6].modelIndex += i4;
            }
        }
        if (arrayList.size() > 0) {
            Collections.sort(arrayList);
            Row[] rowArr = this.viewToModel;
            this.viewToModel = new Row[this.viewToModel.length + arrayList.size()];
            insertInOrder(arrayList, rowArr);
        }
        createModelToView(getModelWrapper().getRowCount());
        setModelToViewFromViewToModel(true);
        fireRowSorterChanged(viewToModelAsInts);
    }

    private void rowsDeleted0(int i2, int i3) {
        int[] viewToModelAsInts = getViewToModelAsInts(this.viewToModel);
        int i4 = 0;
        for (int i5 = i2; i5 <= i3; i5++) {
            int i6 = this.modelToView[i5];
            if (i6 != -1) {
                i4++;
                this.viewToModel[i6] = null;
            }
        }
        int i7 = (i3 - i2) + 1;
        for (int length = this.modelToView.length - 1; length > i3; length--) {
            int i8 = this.modelToView[length];
            if (i8 != -1) {
                this.viewToModel[i8].modelIndex -= i7;
            }
        }
        if (i4 > 0) {
            Row[] rowArr = new Row[this.viewToModel.length - i4];
            int i9 = 0;
            int i10 = 0;
            for (int i11 = 0; i11 < this.viewToModel.length; i11++) {
                if (this.viewToModel[i11] == null) {
                    System.arraycopy(this.viewToModel, i10, rowArr, i9, i11 - i10);
                    i9 += i11 - i10;
                    i10 = i11 + 1;
                }
            }
            System.arraycopy(this.viewToModel, i10, rowArr, i9, this.viewToModel.length - i10);
            this.viewToModel = rowArr;
        }
        createModelToView(getModelWrapper().getRowCount());
        setModelToViewFromViewToModel(true);
        fireRowSorterChanged(viewToModelAsInts);
    }

    private void rowsUpdated0(int i2, int i3) {
        int[] viewToModelAsInts = getViewToModelAsInts(this.viewToModel);
        int i4 = (i3 - i2) + 1;
        if (getRowFilter() == null) {
            Row[] rowArr = new Row[i4];
            int i5 = 0;
            int i6 = i2;
            while (i6 <= i3) {
                rowArr[i5] = this.viewToModel[this.modelToView[i6]];
                i6++;
                i5++;
            }
            Arrays.sort(rowArr);
            Row[] rowArr2 = new Row[this.viewToModel.length - i4];
            int i7 = 0;
            for (int i8 = 0; i8 < this.viewToModel.length; i8++) {
                int i9 = this.viewToModel[i8].modelIndex;
                if (i9 < i2 || i9 > i3) {
                    int i10 = i7;
                    i7++;
                    rowArr2[i10] = this.viewToModel[i8];
                }
            }
            insertInOrder(Arrays.asList(rowArr), rowArr2);
            setModelToViewFromViewToModel(false);
        } else {
            ArrayList arrayList = new ArrayList(i4);
            int i11 = 0;
            int i12 = 0;
            int i13 = 0;
            for (int i14 = i2; i14 <= i3; i14++) {
                if (this.modelToView[i14] == -1) {
                    if (include(i14)) {
                        arrayList.add(new Row(this, i14));
                        i11++;
                    }
                } else {
                    if (!include(i14)) {
                        i12++;
                    } else {
                        arrayList.add(this.viewToModel[this.modelToView[i14]]);
                    }
                    this.modelToView[i14] = -2;
                    i13++;
                }
            }
            Collections.sort(arrayList);
            Row[] rowArr3 = new Row[this.viewToModel.length - i13];
            int i15 = 0;
            for (int i16 = 0; i16 < this.viewToModel.length; i16++) {
                if (this.modelToView[this.viewToModel[i16].modelIndex] != -2) {
                    int i17 = i15;
                    i15++;
                    rowArr3[i17] = this.viewToModel[i16];
                }
            }
            if (i11 != i12) {
                this.viewToModel = new Row[(this.viewToModel.length + i11) - i12];
            }
            insertInOrder(arrayList, rowArr3);
            setModelToViewFromViewToModel(true);
        }
        fireRowSorterChanged(viewToModelAsInts);
    }

    private void checkColumn(int i2) {
        if (i2 < 0 || i2 >= getModelWrapper().getColumnCount()) {
            throw new IndexOutOfBoundsException("column beyond range of TableModel");
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: rt.jar:javax/swing/DefaultRowSorter$ModelWrapper.class */
    public static abstract class ModelWrapper<M, I> {
        public abstract M getModel();

        public abstract int getColumnCount();

        public abstract int getRowCount();

        public abstract Object getValueAt(int i2, int i3);

        public abstract I getIdentifier(int i2);

        protected ModelWrapper() {
        }

        public String getStringValueAt(int i2, int i3) {
            String string;
            Object valueAt = getValueAt(i2, i3);
            if (valueAt == null || (string = valueAt.toString()) == null) {
                return "";
            }
            return string;
        }
    }

    /* loaded from: rt.jar:javax/swing/DefaultRowSorter$FilterEntry.class */
    private class FilterEntry extends RowFilter.Entry<M, I> {
        int modelIndex;

        private FilterEntry() {
        }

        @Override // javax.swing.RowFilter.Entry
        public M getModel() {
            return DefaultRowSorter.this.getModelWrapper().getModel();
        }

        @Override // javax.swing.RowFilter.Entry
        public int getValueCount() {
            return DefaultRowSorter.this.getModelWrapper().getColumnCount();
        }

        @Override // javax.swing.RowFilter.Entry
        public Object getValue(int i2) {
            return DefaultRowSorter.this.getModelWrapper().getValueAt(this.modelIndex, i2);
        }

        @Override // javax.swing.RowFilter.Entry
        public String getStringValue(int i2) {
            return DefaultRowSorter.this.getModelWrapper().getStringValueAt(this.modelIndex, i2);
        }

        @Override // javax.swing.RowFilter.Entry
        public I getIdentifier() {
            return DefaultRowSorter.this.getModelWrapper().getIdentifier(this.modelIndex);
        }
    }

    /* loaded from: rt.jar:javax/swing/DefaultRowSorter$Row.class */
    private static class Row implements Comparable<Row> {
        private DefaultRowSorter sorter;
        int modelIndex;

        public Row(DefaultRowSorter defaultRowSorter, int i2) {
            this.sorter = defaultRowSorter;
            this.modelIndex = i2;
        }

        @Override // java.lang.Comparable
        public int compareTo(Row row) {
            return this.sorter.compare(this.modelIndex, row.modelIndex);
        }
    }
}
