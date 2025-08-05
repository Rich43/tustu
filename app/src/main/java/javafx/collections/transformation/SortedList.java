package javafx.collections.transformation;

import com.sun.javafx.collections.NonIterableChange;
import com.sun.javafx.collections.SortHelper;
import com.sun.javafx.collections.SourceAdapterChange;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import javafx.beans.NamedArg;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

/* loaded from: jfxrt.jar:javafx/collections/transformation/SortedList.class */
public final class SortedList<E> extends TransformationList<E, E> {
    private Comparator<Element<E>> elementComparator;
    private Element<E>[] sorted;
    private int[] perm;
    private int size;
    private final SortHelper helper;
    private final Element<E> tempElement;
    private ObjectProperty<Comparator<? super E>> comparator;

    public SortedList(@NamedArg("source") ObservableList<? extends E> source, @NamedArg("comparator") Comparator<? super E> comparator) {
        super(source);
        this.helper = new SortHelper();
        this.tempElement = new Element<>(null, -1);
        this.sorted = new Element[((source.size() * 3) / 2) + 1];
        this.perm = new int[this.sorted.length];
        this.size = source.size();
        for (int i2 = 0; i2 < this.size; i2++) {
            this.sorted[i2] = new Element<>(source.get(i2), i2);
            this.perm[i2] = i2;
        }
        if (comparator != null) {
            setComparator(comparator);
        }
    }

    public SortedList(@NamedArg("source") ObservableList<? extends E> source) {
        this(source, (Comparator) null);
    }

    @Override // javafx.collections.transformation.TransformationList
    protected void sourceChanged(ListChangeListener.Change<? extends E> c2) {
        if (this.elementComparator != null) {
            beginChange();
            while (c2.next()) {
                if (c2.wasPermutated()) {
                    updatePermutationIndexes(c2);
                } else if (c2.wasUpdated()) {
                    update(c2);
                } else {
                    addRemove(c2);
                }
            }
            endChange();
            return;
        }
        updateUnsorted(c2);
        fireChange(new SourceAdapterChange(this, c2));
    }

    public final ObjectProperty<Comparator<? super E>> comparatorProperty() {
        if (this.comparator == null) {
            this.comparator = new ObjectPropertyBase<Comparator<? super E>>() { // from class: javafx.collections.transformation.SortedList.1
                @Override // javafx.beans.property.ObjectPropertyBase
                protected void invalidated() {
                    Comparator<? super E> current = get();
                    SortedList.this.elementComparator = current != null ? new ElementComparator(current) : null;
                    SortedList.this.doSortWithPermutationChange();
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return SortedList.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "comparator";
                }
            };
        }
        return this.comparator;
    }

    public final Comparator<? super E> getComparator() {
        if (this.comparator == null) {
            return null;
        }
        return this.comparator.get();
    }

    public final void setComparator(Comparator<? super E> comparator) {
        comparatorProperty().set(comparator);
    }

    @Override // java.util.AbstractList, java.util.List
    public E get(int i2) {
        if (i2 >= this.size) {
            throw new IndexOutOfBoundsException();
        }
        return (E) ((Element) this.sorted[i2]).f12630e;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public int size() {
        return this.size;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void doSortWithPermutationChange() {
        if (this.elementComparator != null) {
            int[] perm = this.helper.sort(this.sorted, 0, this.size, this.elementComparator);
            for (int i2 = 0; i2 < this.size; i2++) {
                this.perm[((Element) this.sorted[i2]).index] = i2;
            }
            fireChange(new NonIterableChange.SimplePermutationChange(0, this.size, perm, this));
            return;
        }
        int[] perm2 = new int[this.size];
        int[] rperm = new int[this.size];
        for (int i3 = 0; i3 < this.size; i3++) {
            int i4 = i3;
            rperm[i3] = i4;
            perm2[i3] = i4;
        }
        boolean changed = false;
        int idx = 0;
        while (idx < this.size) {
            int otherIdx = ((Element) this.sorted[idx]).index;
            if (otherIdx == idx) {
                idx++;
            } else {
                Element<E> other = this.sorted[otherIdx];
                this.sorted[otherIdx] = this.sorted[idx];
                this.sorted[idx] = other;
                this.perm[idx] = idx;
                this.perm[otherIdx] = otherIdx;
                perm2[rperm[idx]] = otherIdx;
                perm2[rperm[otherIdx]] = idx;
                int tp = rperm[idx];
                rperm[idx] = rperm[otherIdx];
                rperm[otherIdx] = tp;
                changed = true;
            }
        }
        if (changed) {
            fireChange(new NonIterableChange.SimplePermutationChange(0, this.size, perm2, this));
        }
    }

    @Override // javafx.collections.transformation.TransformationList
    public int getSourceIndex(int index) {
        return ((Element) this.sorted[index]).index;
    }

    private void updatePermutationIndexes(ListChangeListener.Change<? extends E> change) {
        for (int i2 = 0; i2 < this.size; i2++) {
            int p2 = change.getPermutation(((Element) this.sorted[i2]).index);
            ((Element) this.sorted[i2]).index = p2;
            this.perm[p2] = i2;
        }
    }

    private void updateUnsorted(ListChangeListener.Change<? extends E> c2) {
        while (c2.next()) {
            if (c2.wasPermutated()) {
                Element[] sortedTmp = new Element[this.sorted.length];
                for (int i2 = 0; i2 < this.size; i2++) {
                    if (i2 >= c2.getFrom() && i2 < c2.getTo()) {
                        int p2 = c2.getPermutation(i2);
                        sortedTmp[p2] = this.sorted[i2];
                        sortedTmp[p2].index = p2;
                        this.perm[i2] = i2;
                    } else {
                        sortedTmp[i2] = this.sorted[i2];
                    }
                }
                this.sorted = sortedTmp;
            }
            if (c2.wasRemoved()) {
                int removedTo = c2.getFrom() + c2.getRemovedSize();
                System.arraycopy(this.sorted, removedTo, this.sorted, c2.getFrom(), this.size - removedTo);
                System.arraycopy(this.perm, removedTo, this.perm, c2.getFrom(), this.size - removedTo);
                this.size -= c2.getRemovedSize();
                updateIndices(removedTo, removedTo, -c2.getRemovedSize());
            }
            if (c2.wasAdded()) {
                ensureSize(this.size + c2.getAddedSize());
                updateIndices(c2.getFrom(), c2.getFrom(), c2.getAddedSize());
                System.arraycopy(this.sorted, c2.getFrom(), this.sorted, c2.getTo(), this.size - c2.getFrom());
                System.arraycopy(this.perm, c2.getFrom(), this.perm, c2.getTo(), this.size - c2.getFrom());
                this.size += c2.getAddedSize();
                for (int i3 = c2.getFrom(); i3 < c2.getTo(); i3++) {
                    this.sorted[i3] = new Element<>(c2.getList().get(i3), i3);
                    this.perm[i3] = i3;
                }
            }
        }
    }

    /* loaded from: jfxrt.jar:javafx/collections/transformation/SortedList$Element.class */
    private static class Element<E> {

        /* renamed from: e, reason: collision with root package name */
        private E f12630e;
        private int index;

        public Element(E e2, int index) {
            this.f12630e = e2;
            this.index = index;
        }
    }

    /* loaded from: jfxrt.jar:javafx/collections/transformation/SortedList$ElementComparator.class */
    private static class ElementComparator<E> implements Comparator<Element<E>> {
        private final Comparator<? super E> comparator;

        public ElementComparator(Comparator<? super E> comparator) {
            this.comparator = comparator;
        }

        @Override // java.util.Comparator
        public int compare(Element<E> element, Element<E> element2) {
            return this.comparator.compare((Object) ((Element) element).f12630e, (Object) ((Element) element2).f12630e);
        }
    }

    private void ensureSize(int size) {
        if (this.sorted.length < size) {
            Element<E>[] replacement = new Element[((size * 3) / 2) + 1];
            System.arraycopy(this.sorted, 0, replacement, 0, this.size);
            this.sorted = replacement;
            int[] replacementPerm = new int[((size * 3) / 2) + 1];
            System.arraycopy(this.perm, 0, replacementPerm, 0, this.size);
            this.perm = replacementPerm;
        }
    }

    private void updateIndices(int from, int viewFrom, int difference) {
        for (int i2 = 0; i2 < this.size; i2++) {
            if (((Element) this.sorted[i2]).index >= from) {
                ((Element) this.sorted[i2]).index += difference;
            }
            if (this.perm[i2] >= viewFrom) {
                int[] iArr = this.perm;
                int i3 = i2;
                iArr[i3] = iArr[i3] + difference;
            }
        }
    }

    private int findPosition(E e2) {
        if (this.sorted.length != 0) {
            ((Element) this.tempElement).f12630e = e2;
            int pos = Arrays.binarySearch(this.sorted, 0, this.size, this.tempElement, this.elementComparator);
            return pos;
        }
        return 0;
    }

    private void insertToMapping(E e2, int idx) {
        int pos = findPosition(e2);
        if (pos < 0) {
            pos ^= -1;
        }
        ensureSize(this.size + 1);
        updateIndices(idx, pos, 1);
        System.arraycopy(this.sorted, pos, this.sorted, pos + 1, this.size - pos);
        this.sorted[pos] = new Element<>(e2, idx);
        System.arraycopy(this.perm, idx, this.perm, idx + 1, this.size - idx);
        this.perm[idx] = pos;
        this.size++;
        nextAdd(pos, pos + 1);
    }

    private void setAllToMapping(List<? extends E> list, int to) {
        ensureSize(to);
        this.size = to;
        for (int i2 = 0; i2 < to; i2++) {
            this.sorted[i2] = new Element<>(list.get(i2), i2);
        }
        int[] perm = this.helper.sort(this.sorted, 0, this.size, this.elementComparator);
        System.arraycopy(perm, 0, this.perm, 0, this.size);
        nextAdd(0, this.size);
    }

    private void removeFromMapping(int idx, E e2) {
        int pos = this.perm[idx];
        System.arraycopy(this.sorted, pos + 1, this.sorted, pos, (this.size - pos) - 1);
        System.arraycopy(this.perm, idx + 1, this.perm, idx, (this.size - idx) - 1);
        this.size--;
        this.sorted[this.size] = null;
        updateIndices(idx + 1, pos, -1);
        nextRemove(pos, (int) e2);
    }

    private void removeAllFromMapping() {
        ArrayList arrayList = new ArrayList(this);
        for (int i2 = 0; i2 < this.size; i2++) {
            this.sorted[i2] = null;
        }
        this.size = 0;
        nextRemove(0, (List) arrayList);
    }

    private void update(ListChangeListener.Change<? extends E> c2) {
        int[] perm = this.helper.sort(this.sorted, 0, this.size, this.elementComparator);
        for (int i2 = 0; i2 < this.size; i2++) {
            this.perm[((Element) this.sorted[i2]).index] = i2;
        }
        nextPermutation(0, this.size, perm);
        int to = c2.getTo();
        for (int i3 = c2.getFrom(); i3 < to; i3++) {
            nextUpdate(this.perm[i3]);
        }
    }

    private void addRemove(ListChangeListener.Change<? extends E> c2) {
        if (c2.getFrom() == 0 && c2.getRemovedSize() == this.size) {
            removeAllFromMapping();
        } else {
            int sz = c2.getRemovedSize();
            for (int i2 = 0; i2 < sz; i2++) {
                removeFromMapping(c2.getFrom(), c2.getRemoved().get(i2));
            }
        }
        if (this.size == 0) {
            setAllToMapping(c2.getList(), c2.getTo());
            return;
        }
        int to = c2.getTo();
        for (int i3 = c2.getFrom(); i3 < to; i3++) {
            insertToMapping(c2.getList().get(i3), i3);
        }
    }
}
