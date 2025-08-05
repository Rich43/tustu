package javafx.beans.binding;

import com.sun.javafx.binding.StringFormatter;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.value.ObservableIntegerValue;
import javafx.beans.value.ObservableListValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/* loaded from: jfxrt.jar:javafx/beans/binding/ListExpression.class */
public abstract class ListExpression<E> implements ObservableListValue<E> {
    private static final ObservableList EMPTY_LIST = FXCollections.emptyObservableList();

    public abstract ReadOnlyIntegerProperty sizeProperty();

    public abstract ReadOnlyBooleanProperty emptyProperty();

    @Override // javafx.beans.value.ObservableValue
    /* renamed from: getValue */
    public ObservableList<E> getValue2() {
        return get();
    }

    public static <E> ListExpression<E> listExpression(final ObservableListValue<E> value) {
        if (value == null) {
            throw new NullPointerException("List must be specified.");
        }
        return value instanceof ListExpression ? (ListExpression) value : new ListBinding<E>() { // from class: javafx.beans.binding.ListExpression.1
            {
                super.bind(value);
            }

            @Override // javafx.beans.binding.ListBinding, javafx.beans.binding.Binding
            public void dispose() {
                super.unbind(value);
            }

            @Override // javafx.beans.binding.ListBinding
            protected ObservableList<E> computeValue() {
                return value.get();
            }

            @Override // javafx.beans.binding.ListBinding, javafx.beans.binding.Binding
            public ObservableList<ObservableListValue<E>> getDependencies() {
                return FXCollections.singletonObservableList(value);
            }
        };
    }

    public int getSize() {
        return size();
    }

    public ObjectBinding<E> valueAt(int index) {
        return Bindings.valueAt(this, index);
    }

    public ObjectBinding<E> valueAt(ObservableIntegerValue index) {
        return Bindings.valueAt((ObservableList) this, index);
    }

    public BooleanBinding isEqualTo(ObservableList<?> other) {
        return Bindings.equal(this, other);
    }

    public BooleanBinding isNotEqualTo(ObservableList<?> other) {
        return Bindings.notEqual(this, other);
    }

    public BooleanBinding isNull() {
        return Bindings.isNull(this);
    }

    public BooleanBinding isNotNull() {
        return Bindings.isNotNull(this);
    }

    public StringBinding asString() {
        return (StringBinding) StringFormatter.convert(this);
    }

    @Override // java.util.List, java.util.Collection, java.util.Set
    public int size() {
        ObservableList<E> list = get();
        return list == null ? EMPTY_LIST.size() : list.size();
    }

    @Override // java.util.List, java.util.Collection
    public boolean isEmpty() {
        ObservableList<E> list = get();
        return list == null ? EMPTY_LIST.isEmpty() : list.isEmpty();
    }

    @Override // java.util.List, java.util.Collection, java.util.Set
    public boolean contains(Object obj) {
        ObservableList<E> list = get();
        return list == null ? EMPTY_LIST.contains(obj) : list.contains(obj);
    }

    @Override // java.util.List
    public Iterator<E> iterator() {
        ObservableList<E> list = get();
        return list == null ? EMPTY_LIST.iterator() : list.iterator();
    }

    @Override // java.util.List
    public Object[] toArray() {
        ObservableList<E> list = get();
        return list == null ? EMPTY_LIST.toArray() : list.toArray();
    }

    @Override // java.util.List, java.util.Collection
    public <T> T[] toArray(T[] tArr) {
        ObservableList<E> observableList = get();
        return observableList == null ? (T[]) EMPTY_LIST.toArray(tArr) : (T[]) observableList.toArray(tArr);
    }

    @Override // java.util.List
    public boolean add(E element) {
        ObservableList<E> list = get();
        return list == null ? EMPTY_LIST.add(element) : list.add(element);
    }

    @Override // java.util.List, java.util.Collection, java.util.Set
    public boolean remove(Object obj) {
        ObservableList<E> list = get();
        return list == null ? EMPTY_LIST.remove(obj) : list.remove(obj);
    }

    @Override // java.util.List, java.util.Collection
    public boolean containsAll(Collection<?> objects) {
        ObservableList<E> list = get();
        return list == null ? EMPTY_LIST.contains(objects) : list.containsAll(objects);
    }

    @Override // java.util.List, java.util.Collection
    public boolean addAll(Collection<? extends E> elements) {
        ObservableList<E> list = get();
        return list == null ? EMPTY_LIST.addAll(elements) : list.addAll(elements);
    }

    @Override // java.util.List
    public boolean addAll(int i2, Collection<? extends E> elements) {
        ObservableList<E> list = get();
        return list == null ? EMPTY_LIST.addAll(i2, elements) : list.addAll(i2, elements);
    }

    @Override // java.util.List, java.util.Collection
    public boolean removeAll(Collection<?> objects) {
        ObservableList<E> list = get();
        return list == null ? EMPTY_LIST.removeAll(objects) : list.removeAll(objects);
    }

    @Override // java.util.List, java.util.Collection
    public boolean retainAll(Collection<?> objects) {
        ObservableList<E> list = get();
        return list == null ? EMPTY_LIST.retainAll(objects) : list.retainAll(objects);
    }

    @Override // java.util.List
    public void clear() {
        ObservableList<E> list = get();
        if (list == null) {
            EMPTY_LIST.clear();
        } else {
            list.clear();
        }
    }

    @Override // java.util.List
    public E get(int i2) {
        ObservableList<E> list = get();
        return list == null ? EMPTY_LIST.get(i2) : list.get(i2);
    }

    @Override // java.util.List
    public E set(int i2, E element) {
        ObservableList<E> list = get();
        return list == null ? EMPTY_LIST.set(i2, element) : list.set(i2, element);
    }

    @Override // java.util.List
    public void add(int i2, E element) {
        ObservableList<E> list = get();
        if (list == null) {
            EMPTY_LIST.add(i2, element);
        } else {
            list.add(i2, element);
        }
    }

    @Override // java.util.List
    public E remove(int i2) {
        ObservableList<E> list = get();
        return list == null ? EMPTY_LIST.remove(i2) : list.remove(i2);
    }

    @Override // java.util.List
    public int indexOf(Object obj) {
        ObservableList<E> list = get();
        return list == null ? EMPTY_LIST.indexOf(obj) : list.indexOf(obj);
    }

    @Override // java.util.List
    public int lastIndexOf(Object obj) {
        ObservableList<E> list = get();
        return list == null ? EMPTY_LIST.lastIndexOf(obj) : list.lastIndexOf(obj);
    }

    @Override // java.util.List
    public ListIterator<E> listIterator() {
        ObservableList<E> list = get();
        return list == null ? EMPTY_LIST.listIterator() : list.listIterator();
    }

    @Override // java.util.List
    public ListIterator<E> listIterator(int i2) {
        ObservableList<E> list = get();
        return list == null ? EMPTY_LIST.listIterator(i2) : list.listIterator(i2);
    }

    @Override // java.util.List
    public List<E> subList(int from, int to) {
        ObservableList<E> list = get();
        return list == null ? EMPTY_LIST.subList(from, to) : list.subList(from, to);
    }

    @Override // javafx.collections.ObservableList
    public boolean addAll(E... elements) {
        ObservableList<E> list = get();
        return list == null ? EMPTY_LIST.addAll(elements) : list.addAll(elements);
    }

    @Override // javafx.collections.ObservableList
    public boolean setAll(E... elements) {
        ObservableList<E> list = get();
        return list == null ? EMPTY_LIST.setAll(elements) : list.setAll(elements);
    }

    @Override // javafx.collections.ObservableList
    public boolean setAll(Collection<? extends E> elements) {
        ObservableList<E> list = get();
        return list == null ? EMPTY_LIST.setAll(elements) : list.setAll(elements);
    }

    @Override // javafx.collections.ObservableList
    public boolean removeAll(E... elements) {
        ObservableList<E> list = get();
        return list == null ? EMPTY_LIST.removeAll(elements) : list.removeAll(elements);
    }

    @Override // javafx.collections.ObservableList
    public boolean retainAll(E... elements) {
        ObservableList<E> list = get();
        return list == null ? EMPTY_LIST.retainAll(elements) : list.retainAll(elements);
    }

    @Override // javafx.collections.ObservableList
    public void remove(int from, int to) {
        ObservableList<E> list = get();
        if (list == null) {
            EMPTY_LIST.remove(from, to);
        } else {
            list.remove(from, to);
        }
    }
}
