package com.sun.javafx.collections;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import javafx.beans.InvalidationListener;
import javafx.collections.ObservableSet;
import javafx.collections.SetChangeListener;

/* loaded from: jfxrt.jar:com/sun/javafx/collections/ObservableSetWrapper.class */
public class ObservableSetWrapper<E> implements ObservableSet<E> {
    private final Set<E> backingSet;
    private SetListenerHelper<E> listenerHelper;

    public ObservableSetWrapper(Set<E> set) {
        this.backingSet = set;
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/collections/ObservableSetWrapper$SimpleAddChange.class */
    private class SimpleAddChange extends SetChangeListener.Change<E> {
        private final E added;

        public SimpleAddChange(E added) {
            super(ObservableSetWrapper.this);
            this.added = added;
        }

        @Override // javafx.collections.SetChangeListener.Change
        public boolean wasAdded() {
            return true;
        }

        @Override // javafx.collections.SetChangeListener.Change
        public boolean wasRemoved() {
            return false;
        }

        @Override // javafx.collections.SetChangeListener.Change
        public E getElementAdded() {
            return this.added;
        }

        @Override // javafx.collections.SetChangeListener.Change
        public E getElementRemoved() {
            return null;
        }

        public String toString() {
            return "added " + ((Object) this.added);
        }
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/collections/ObservableSetWrapper$SimpleRemoveChange.class */
    private class SimpleRemoveChange extends SetChangeListener.Change<E> {
        private final E removed;

        public SimpleRemoveChange(E removed) {
            super(ObservableSetWrapper.this);
            this.removed = removed;
        }

        @Override // javafx.collections.SetChangeListener.Change
        public boolean wasAdded() {
            return false;
        }

        @Override // javafx.collections.SetChangeListener.Change
        public boolean wasRemoved() {
            return true;
        }

        @Override // javafx.collections.SetChangeListener.Change
        public E getElementAdded() {
            return null;
        }

        @Override // javafx.collections.SetChangeListener.Change
        public E getElementRemoved() {
            return this.removed;
        }

        public String toString() {
            return "removed " + ((Object) this.removed);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void callObservers(SetChangeListener.Change<E> change) {
        SetListenerHelper.fireValueChangedEvent(this.listenerHelper, change);
    }

    @Override // javafx.beans.Observable
    public void addListener(InvalidationListener listener) {
        this.listenerHelper = SetListenerHelper.addListener(this.listenerHelper, listener);
    }

    @Override // javafx.beans.Observable
    public void removeListener(InvalidationListener listener) {
        this.listenerHelper = SetListenerHelper.removeListener(this.listenerHelper, listener);
    }

    @Override // javafx.collections.ObservableSet
    public void addListener(SetChangeListener<? super E> observer) {
        this.listenerHelper = SetListenerHelper.addListener(this.listenerHelper, observer);
    }

    @Override // javafx.collections.ObservableSet
    public void removeListener(SetChangeListener<? super E> observer) {
        this.listenerHelper = SetListenerHelper.removeListener(this.listenerHelper, observer);
    }

    @Override // java.util.Set
    public int size() {
        return this.backingSet.size();
    }

    @Override // java.util.Set, java.util.Collection
    public boolean isEmpty() {
        return this.backingSet.isEmpty();
    }

    @Override // java.util.Set
    public boolean contains(Object o2) {
        return this.backingSet.contains(o2);
    }

    @Override // java.util.Set, java.util.Collection, java.lang.Iterable, java.util.List
    public Iterator iterator() {
        return new Iterator<E>() { // from class: com.sun.javafx.collections.ObservableSetWrapper.1
            private final Iterator<E> backingIt;
            private E lastElement;

            {
                this.backingIt = ObservableSetWrapper.this.backingSet.iterator();
            }

            @Override // java.util.Iterator
            public boolean hasNext() {
                return this.backingIt.hasNext();
            }

            @Override // java.util.Iterator
            public E next() {
                this.lastElement = this.backingIt.next();
                return this.lastElement;
            }

            @Override // java.util.Iterator
            public void remove() {
                this.backingIt.remove();
                ObservableSetWrapper.this.callObservers(new SimpleRemoveChange(this.lastElement));
            }
        };
    }

    @Override // java.util.Set, java.util.Collection, java.util.List
    public Object[] toArray() {
        return this.backingSet.toArray();
    }

    @Override // java.util.Set, java.util.Collection
    public <T> T[] toArray(T[] tArr) {
        return (T[]) this.backingSet.toArray(tArr);
    }

    @Override // java.util.Set, java.util.Collection, java.util.List
    public boolean add(E o2) {
        boolean ret = this.backingSet.add(o2);
        if (ret) {
            callObservers(new SimpleAddChange(o2));
        }
        return ret;
    }

    @Override // java.util.Set
    public boolean remove(Object o2) {
        boolean ret = this.backingSet.remove(o2);
        if (ret) {
            callObservers(new SimpleRemoveChange(o2));
        }
        return ret;
    }

    @Override // java.util.Set, java.util.Collection
    public boolean containsAll(Collection<?> c2) {
        return this.backingSet.containsAll(c2);
    }

    @Override // java.util.Set, java.util.Collection
    public boolean addAll(Collection<? extends E> c2) {
        boolean ret = false;
        for (E element : c2) {
            ret |= add(element);
        }
        return ret;
    }

    @Override // java.util.Set, java.util.Collection
    public boolean retainAll(Collection<?> c2) {
        return removeRetain(c2, false);
    }

    @Override // java.util.Set, java.util.Collection
    public boolean removeAll(Collection<?> c2) {
        return removeRetain(c2, true);
    }

    private boolean removeRetain(Collection<?> c2, boolean remove) {
        boolean removed = false;
        Iterator<E> i2 = this.backingSet.iterator();
        while (i2.hasNext()) {
            E element = i2.next();
            if (remove == c2.contains(element)) {
                removed = true;
                i2.remove();
                callObservers(new SimpleRemoveChange(element));
            }
        }
        return removed;
    }

    @Override // java.util.Set, java.util.Collection, java.util.List
    public void clear() {
        Iterator<E> i2 = this.backingSet.iterator();
        while (i2.hasNext()) {
            E element = i2.next();
            i2.remove();
            callObservers(new SimpleRemoveChange(element));
        }
    }

    public String toString() {
        return this.backingSet.toString();
    }

    @Override // java.util.Set, java.util.Collection, java.util.List
    public boolean equals(Object obj) {
        return this.backingSet.equals(obj);
    }

    @Override // java.util.Set, java.util.Collection, java.util.List
    public int hashCode() {
        return this.backingSet.hashCode();
    }
}
