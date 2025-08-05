package javafx.collections.transformation;

import com.sun.javafx.collections.NonIterableChange;
import com.sun.javafx.collections.SortHelper;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.function.Predicate;
import javafx.beans.NamedArg;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

/* loaded from: jfxrt.jar:javafx/collections/transformation/FilteredList.class */
public final class FilteredList<E> extends TransformationList<E, E> {
    private int[] filtered;
    private int size;
    private SortHelper helper;
    private static final Predicate ALWAYS_TRUE = t2 -> {
        return true;
    };
    private ObjectProperty<Predicate<? super E>> predicate;

    public FilteredList(@NamedArg("source") ObservableList<E> source, @NamedArg("predicate") Predicate<? super E> predicate) {
        super(source);
        this.filtered = new int[((source.size() * 3) / 2) + 1];
        if (predicate != null) {
            setPredicate(predicate);
            return;
        }
        this.size = 0;
        while (this.size < source.size()) {
            this.filtered[this.size] = this.size;
            this.size++;
        }
    }

    public FilteredList(@NamedArg("source") ObservableList<E> source) {
        this(source, null);
    }

    public final ObjectProperty<Predicate<? super E>> predicateProperty() {
        if (this.predicate == null) {
            this.predicate = new ObjectPropertyBase<Predicate<? super E>>() { // from class: javafx.collections.transformation.FilteredList.1
                @Override // javafx.beans.property.ObjectPropertyBase
                protected void invalidated() {
                    FilteredList.this.refilter();
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return FilteredList.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "predicate";
                }
            };
        }
        return this.predicate;
    }

    public final Predicate<? super E> getPredicate() {
        if (this.predicate == null) {
            return null;
        }
        return this.predicate.get();
    }

    public final void setPredicate(Predicate<? super E> predicate) {
        predicateProperty().set(predicate);
    }

    private Predicate<? super E> getPredicateImpl() {
        if (getPredicate() != null) {
            return getPredicate();
        }
        return ALWAYS_TRUE;
    }

    @Override // javafx.collections.transformation.TransformationList
    protected void sourceChanged(ListChangeListener.Change<? extends E> c2) {
        beginChange();
        while (c2.next()) {
            if (c2.wasPermutated()) {
                permutate(c2);
            } else if (c2.wasUpdated()) {
                update(c2);
            } else {
                addRemove(c2);
            }
        }
        endChange();
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public int size() {
        return this.size;
    }

    @Override // java.util.AbstractList, java.util.List
    public E get(int index) {
        if (index >= this.size) {
            throw new IndexOutOfBoundsException();
        }
        return getSource().get(this.filtered[index]);
    }

    @Override // javafx.collections.transformation.TransformationList
    public int getSourceIndex(int index) {
        if (index >= this.size) {
            throw new IndexOutOfBoundsException();
        }
        return this.filtered[index];
    }

    private SortHelper getSortHelper() {
        if (this.helper == null) {
            this.helper = new SortHelper();
        }
        return this.helper;
    }

    private int findPosition(int p2) {
        if (this.filtered.length == 0 || p2 == 0) {
            return 0;
        }
        int pos = Arrays.binarySearch(this.filtered, 0, this.size, p2);
        if (pos < 0) {
            pos ^= -1;
        }
        return pos;
    }

    private void ensureSize(int size) {
        if (this.filtered.length < size) {
            int[] replacement = new int[((size * 3) / 2) + 1];
            System.arraycopy(this.filtered, 0, replacement, 0, this.size);
            this.filtered = replacement;
        }
    }

    private void updateIndexes(int from, int delta) {
        for (int i2 = from; i2 < this.size; i2++) {
            int[] iArr = this.filtered;
            int i3 = i2;
            iArr[i3] = iArr[i3] + delta;
        }
    }

    private void permutate(ListChangeListener.Change<? extends E> c2) {
        int from = findPosition(c2.getFrom());
        int to = findPosition(c2.getTo());
        if (to > from) {
            for (int i2 = from; i2 < to; i2++) {
                this.filtered[i2] = c2.getPermutation(this.filtered[i2]);
            }
            int[] perm = getSortHelper().sort(this.filtered, from, to);
            nextPermutation(from, to, perm);
        }
    }

    private void addRemove(ListChangeListener.Change<? extends E> change) {
        Predicate<? super E> predicateImpl = getPredicateImpl();
        ensureSize(getSource().size());
        int iFindPosition = findPosition(change.getFrom());
        int iFindPosition2 = findPosition(change.getFrom() + change.getRemovedSize());
        for (int i2 = iFindPosition; i2 < iFindPosition2; i2++) {
            nextRemove(iFindPosition, (int) change.getRemoved().get(this.filtered[i2] - change.getFrom()));
        }
        updateIndexes(iFindPosition2, change.getAddedSize() - change.getRemovedSize());
        int i3 = iFindPosition;
        int from = change.getFrom();
        ListIterator<? extends E> listIterator = getSource().listIterator(from);
        while (i3 < iFindPosition2 && listIterator.nextIndex() < change.getTo()) {
            if (predicateImpl.test(listIterator.next())) {
                this.filtered[i3] = listIterator.previousIndex();
                nextAdd(i3, i3 + 1);
                i3++;
            }
        }
        if (i3 < iFindPosition2) {
            System.arraycopy(this.filtered, iFindPosition2, this.filtered, i3, this.size - iFindPosition2);
            this.size -= iFindPosition2 - i3;
            return;
        }
        while (listIterator.nextIndex() < change.getTo()) {
            if (predicateImpl.test(listIterator.next())) {
                System.arraycopy(this.filtered, i3, this.filtered, i3 + 1, this.size - i3);
                this.filtered[i3] = listIterator.previousIndex();
                nextAdd(i3, i3 + 1);
                i3++;
                this.size++;
            }
            from++;
        }
    }

    private void update(ListChangeListener.Change<? extends E> c2) {
        Predicate<? super E> pred = getPredicateImpl();
        ensureSize(getSource().size());
        int sourceFrom = c2.getFrom();
        int sourceTo = c2.getTo();
        int filterFrom = findPosition(sourceFrom);
        int filterTo = findPosition(sourceTo);
        ListIterator<E> listIterator = getSource().listIterator(sourceFrom);
        int pos = filterFrom;
        while (true) {
            if (pos < filterTo || sourceFrom < sourceTo) {
                E el = listIterator.next();
                if (pos < this.size && this.filtered[pos] == sourceFrom) {
                    if (!pred.test(el)) {
                        nextRemove(pos, (int) el);
                        System.arraycopy(this.filtered, pos + 1, this.filtered, pos, (this.size - pos) - 1);
                        this.size--;
                        filterTo--;
                    } else {
                        nextUpdate(pos);
                        pos++;
                    }
                } else if (pred.test(el)) {
                    nextAdd(pos, pos + 1);
                    System.arraycopy(this.filtered, pos, this.filtered, pos + 1, this.size - pos);
                    this.filtered[pos] = sourceFrom;
                    this.size++;
                    pos++;
                    filterTo++;
                }
                sourceFrom++;
            } else {
                return;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void refilter() {
        ensureSize(getSource().size());
        ArrayList arrayList = null;
        if (hasListeners()) {
            arrayList = new ArrayList(this);
        }
        this.size = 0;
        int i2 = 0;
        Predicate<? super E> predicateImpl = getPredicateImpl();
        Iterator<? extends E> it = getSource().iterator();
        while (it.hasNext()) {
            if (predicateImpl.test(it.next())) {
                int[] iArr = this.filtered;
                int i3 = this.size;
                this.size = i3 + 1;
                iArr[i3] = i2;
            }
            i2++;
        }
        if (hasListeners()) {
            fireChange(new NonIterableChange.GenericAddRemoveChange(0, this.size, arrayList, this));
        }
    }
}
