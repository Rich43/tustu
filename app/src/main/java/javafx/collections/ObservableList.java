package javafx.collections;

import java.text.Collator;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import javafx.beans.Observable;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;

/* loaded from: jfxrt.jar:javafx/collections/ObservableList.class */
public interface ObservableList<E> extends List<E>, Observable {
    void addListener(ListChangeListener<? super E> listChangeListener);

    void removeListener(ListChangeListener<? super E> listChangeListener);

    boolean addAll(E... eArr);

    boolean setAll(E... eArr);

    boolean setAll(Collection<? extends E> collection);

    boolean removeAll(E... eArr);

    boolean retainAll(E... eArr);

    void remove(int i2, int i3);

    default FilteredList<E> filtered(Predicate<E> predicate) {
        return new FilteredList<>(this, predicate);
    }

    default SortedList<E> sorted(Comparator<E> comparator) {
        return new SortedList<>(this, comparator);
    }

    default SortedList<E> sorted() {
        Comparator naturalOrder = new Comparator<E>() { // from class: javafx.collections.ObservableList.1
            @Override // java.util.Comparator
            public int compare(E o1, E o2) {
                if (o1 == null && o2 == null) {
                    return 0;
                }
                if (o1 == null) {
                    return -1;
                }
                if (o2 == null) {
                    return 1;
                }
                if (o1 instanceof Comparable) {
                    return ((Comparable) o1).compareTo(o2);
                }
                return Collator.getInstance().compare(o1.toString(), o2.toString());
            }
        };
        return sorted(naturalOrder);
    }
}
