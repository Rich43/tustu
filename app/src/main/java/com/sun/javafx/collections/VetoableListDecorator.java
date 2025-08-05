package com.sun.javafx.collections;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import javafx.beans.InvalidationListener;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

/* loaded from: jfxrt.jar:com/sun/javafx/collections/VetoableListDecorator.class */
public abstract class VetoableListDecorator<E> implements ObservableList<E> {
    private final ObservableList<E> list;
    private int modCount;
    private ListListenerHelper<E> helper;

    /* loaded from: jfxrt.jar:com/sun/javafx/collections/VetoableListDecorator$ModCountAccessor.class */
    private interface ModCountAccessor {
        int get();

        int incrementAndGet();

        int decrementAndGet();
    }

    protected abstract void onProposedChange(List<E> list, int... iArr);

    static /* synthetic */ int access$404(VetoableListDecorator x0) {
        int i2 = x0.modCount + 1;
        x0.modCount = i2;
        return i2;
    }

    static /* synthetic */ int access$406(VetoableListDecorator x0) {
        int i2 = x0.modCount - 1;
        x0.modCount = i2;
        return i2;
    }

    public VetoableListDecorator(ObservableList<E> decorated) {
        this.list = decorated;
        this.list.addListener(c2 -> {
            ListListenerHelper.fireValueChangedEvent(this.helper, new SourceAdapterChange(this, c2));
        });
    }

    @Override // javafx.collections.ObservableList
    public void addListener(ListChangeListener<? super E> listener) {
        this.helper = ListListenerHelper.addListener(this.helper, listener);
    }

    @Override // javafx.collections.ObservableList
    public void removeListener(ListChangeListener<? super E> listener) {
        this.helper = ListListenerHelper.removeListener(this.helper, listener);
    }

    @Override // javafx.beans.Observable
    public void addListener(InvalidationListener listener) {
        this.helper = ListListenerHelper.addListener(this.helper, listener);
    }

    @Override // javafx.beans.Observable
    public void removeListener(InvalidationListener listener) {
        this.helper = ListListenerHelper.removeListener(this.helper, listener);
    }

    @Override // javafx.collections.ObservableList
    public boolean addAll(E... elements) {
        return addAll(Arrays.asList(elements));
    }

    @Override // javafx.collections.ObservableList
    public boolean setAll(E... elements) {
        return setAll(Arrays.asList(elements));
    }

    @Override // javafx.collections.ObservableList
    public boolean setAll(Collection<? extends E> col) throws Exception {
        onProposedChange(Collections.unmodifiableList(new ArrayList(col)), 0, size());
        try {
            this.modCount++;
            this.list.setAll(col);
            return true;
        } catch (Exception e2) {
            this.modCount--;
            throw e2;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void removeFromList(List<E> backingList, int offset, Collection<?> col, boolean complement) {
        int[] toBeRemoved = new int[2];
        int pointer = -1;
        for (int i2 = 0; i2 < backingList.size(); i2++) {
            E el = backingList.get(i2);
            if (col.contains(el) ^ complement) {
                if (pointer == -1) {
                    toBeRemoved[pointer + 1] = offset + i2;
                    toBeRemoved[pointer + 2] = offset + i2 + 1;
                    pointer += 2;
                } else if (toBeRemoved[pointer - 1] == offset + i2) {
                    toBeRemoved[pointer - 1] = offset + i2 + 1;
                } else {
                    int[] tmp = new int[toBeRemoved.length + 2];
                    System.arraycopy(toBeRemoved, 0, tmp, 0, toBeRemoved.length);
                    toBeRemoved = tmp;
                    toBeRemoved[pointer + 1] = offset + i2;
                    toBeRemoved[pointer + 2] = offset + i2 + 1;
                    pointer += 2;
                }
            }
        }
        if (pointer != -1) {
            onProposedChange(Collections.emptyList(), toBeRemoved);
        }
    }

    @Override // javafx.collections.ObservableList
    public boolean removeAll(E... elements) {
        return removeAll(Arrays.asList(elements));
    }

    @Override // javafx.collections.ObservableList
    public boolean retainAll(E... elements) {
        return retainAll(Arrays.asList(elements));
    }

    @Override // javafx.collections.ObservableList
    public void remove(int from, int to) {
        onProposedChange(Collections.emptyList(), from, to);
        try {
            this.modCount++;
            this.list.remove(from, to);
        } catch (Exception e2) {
            this.modCount--;
        }
    }

    @Override // java.util.List, java.util.Collection, java.util.Set
    public int size() {
        return this.list.size();
    }

    @Override // java.util.List, java.util.Collection
    public boolean isEmpty() {
        return this.list.isEmpty();
    }

    @Override // java.util.List, java.util.Collection, java.util.Set
    public boolean contains(Object o2) {
        return this.list.contains(o2);
    }

    @Override // java.util.List
    public Iterator<E> iterator() {
        return new VetoableIteratorDecorator(new ModCountAccessorImpl(), this.list.iterator(), 0);
    }

    @Override // java.util.List
    public Object[] toArray() {
        return this.list.toArray();
    }

    @Override // java.util.List, java.util.Collection
    public <T> T[] toArray(T[] tArr) {
        return (T[]) this.list.toArray(tArr);
    }

    @Override // java.util.List
    public boolean add(E e2) throws Exception {
        onProposedChange(Collections.singletonList(e2), size(), size());
        try {
            this.modCount++;
            this.list.add(e2);
            return true;
        } catch (Exception ex) {
            this.modCount--;
            throw ex;
        }
    }

    @Override // java.util.List, java.util.Collection, java.util.Set
    public boolean remove(Object o2) throws Exception {
        int i2 = this.list.indexOf(o2);
        if (i2 != -1) {
            remove(i2);
            return true;
        }
        return false;
    }

    @Override // java.util.List, java.util.Collection
    public boolean containsAll(Collection<?> c2) {
        return this.list.containsAll(c2);
    }

    @Override // java.util.List, java.util.Collection
    public boolean addAll(Collection<? extends E> c2) throws Exception {
        onProposedChange(Collections.unmodifiableList(new ArrayList(c2)), size(), size());
        try {
            this.modCount++;
            boolean ret = this.list.addAll(c2);
            if (!ret) {
                this.modCount--;
            }
            return ret;
        } catch (Exception e2) {
            this.modCount--;
            throw e2;
        }
    }

    @Override // java.util.List
    public boolean addAll(int index, Collection<? extends E> c2) throws Exception {
        onProposedChange(Collections.unmodifiableList(new ArrayList(c2)), index, index);
        try {
            this.modCount++;
            boolean ret = this.list.addAll(index, c2);
            if (!ret) {
                this.modCount--;
            }
            return ret;
        } catch (Exception e2) {
            this.modCount--;
            throw e2;
        }
    }

    @Override // java.util.List, java.util.Collection
    public boolean removeAll(Collection<?> c2) throws Exception {
        removeFromList(this, 0, c2, false);
        try {
            this.modCount++;
            boolean ret = this.list.removeAll(c2);
            if (!ret) {
                this.modCount--;
            }
            return ret;
        } catch (Exception e2) {
            this.modCount--;
            throw e2;
        }
    }

    @Override // java.util.List, java.util.Collection
    public boolean retainAll(Collection<?> c2) throws Exception {
        removeFromList(this, 0, c2, true);
        try {
            this.modCount++;
            boolean ret = this.list.retainAll(c2);
            if (!ret) {
                this.modCount--;
            }
            return ret;
        } catch (Exception e2) {
            this.modCount--;
            throw e2;
        }
    }

    @Override // java.util.List
    public void clear() throws Exception {
        onProposedChange(Collections.emptyList(), 0, size());
        try {
            this.modCount++;
            this.list.clear();
        } catch (Exception e2) {
            this.modCount--;
            throw e2;
        }
    }

    @Override // java.util.List
    public E get(int index) {
        return this.list.get(index);
    }

    @Override // java.util.List
    public E set(int index, E element) {
        onProposedChange(Collections.singletonList(element), index, index + 1);
        return this.list.set(index, element);
    }

    @Override // java.util.List
    public void add(int index, E element) throws Exception {
        onProposedChange(Collections.singletonList(element), index, index);
        try {
            this.modCount++;
            this.list.add(index, element);
        } catch (Exception e2) {
            this.modCount--;
            throw e2;
        }
    }

    @Override // java.util.List
    public E remove(int index) throws Exception {
        onProposedChange(Collections.emptyList(), index, index + 1);
        try {
            this.modCount++;
            E ret = this.list.remove(index);
            return ret;
        } catch (Exception e2) {
            this.modCount--;
            throw e2;
        }
    }

    @Override // java.util.List
    public int indexOf(Object o2) {
        return this.list.indexOf(o2);
    }

    @Override // java.util.List
    public int lastIndexOf(Object o2) {
        return this.list.lastIndexOf(o2);
    }

    @Override // java.util.List
    public ListIterator<E> listIterator() {
        return new VetoableListIteratorDecorator(new ModCountAccessorImpl(), this.list.listIterator(), 0);
    }

    @Override // java.util.List
    public ListIterator<E> listIterator(int index) {
        return new VetoableListIteratorDecorator(new ModCountAccessorImpl(), this.list.listIterator(index), index);
    }

    @Override // java.util.List
    public List<E> subList(int fromIndex, int toIndex) {
        return new VetoableSubListDecorator(new ModCountAccessorImpl(), this.list.subList(fromIndex, toIndex), fromIndex);
    }

    public String toString() {
        return this.list.toString();
    }

    @Override // java.util.List
    public boolean equals(Object obj) {
        return this.list.equals(obj);
    }

    @Override // java.util.List
    public int hashCode() {
        return this.list.hashCode();
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/collections/VetoableListDecorator$VetoableSubListDecorator.class */
    private class VetoableSubListDecorator implements List<E> {
        private final List<E> subList;
        private final int offset;
        private final ModCountAccessor modCountAccessor;
        private int modCount;

        public VetoableSubListDecorator(ModCountAccessor modCountAccessor, List<E> subList, int offset) {
            this.modCountAccessor = modCountAccessor;
            this.modCount = modCountAccessor.get();
            this.subList = subList;
            this.offset = offset;
        }

        @Override // java.util.List, java.util.Collection, java.util.Set
        public int size() {
            checkForComodification();
            return this.subList.size();
        }

        @Override // java.util.List, java.util.Collection
        public boolean isEmpty() {
            checkForComodification();
            return this.subList.isEmpty();
        }

        @Override // java.util.List, java.util.Collection, java.util.Set
        public boolean contains(Object o2) {
            checkForComodification();
            return this.subList.contains(o2);
        }

        @Override // java.util.List
        public Iterator<E> iterator() {
            checkForComodification();
            return new VetoableIteratorDecorator(new ModCountAccessorImplSub(), this.subList.iterator(), this.offset);
        }

        @Override // java.util.List
        public Object[] toArray() {
            checkForComodification();
            return this.subList.toArray();
        }

        @Override // java.util.List, java.util.Collection
        public <T> T[] toArray(T[] tArr) {
            checkForComodification();
            return (T[]) this.subList.toArray(tArr);
        }

        @Override // java.util.List
        public boolean add(E e2) throws Exception {
            checkForComodification();
            VetoableListDecorator.this.onProposedChange(Collections.singletonList(e2), this.offset + size(), this.offset + size());
            try {
                incrementModCount();
                this.subList.add(e2);
                return true;
            } catch (Exception ex) {
                decrementModCount();
                throw ex;
            }
        }

        @Override // java.util.List, java.util.Collection, java.util.Set
        public boolean remove(Object o2) throws Exception {
            checkForComodification();
            int i2 = indexOf(o2);
            if (i2 != -1) {
                remove(i2);
                return true;
            }
            return false;
        }

        @Override // java.util.List, java.util.Collection
        public boolean containsAll(Collection<?> c2) {
            checkForComodification();
            return this.subList.containsAll(c2);
        }

        @Override // java.util.List, java.util.Collection
        public boolean addAll(Collection<? extends E> c2) throws Exception {
            checkForComodification();
            VetoableListDecorator.this.onProposedChange(Collections.unmodifiableList(new ArrayList(c2)), this.offset + size(), this.offset + size());
            try {
                incrementModCount();
                boolean res = this.subList.addAll(c2);
                if (!res) {
                    decrementModCount();
                }
                return res;
            } catch (Exception e2) {
                decrementModCount();
                throw e2;
            }
        }

        @Override // java.util.List
        public boolean addAll(int index, Collection<? extends E> c2) throws Exception {
            checkForComodification();
            VetoableListDecorator.this.onProposedChange(Collections.unmodifiableList(new ArrayList(c2)), this.offset + index, this.offset + index);
            try {
                incrementModCount();
                boolean res = this.subList.addAll(index, c2);
                if (!res) {
                    decrementModCount();
                }
                return res;
            } catch (Exception e2) {
                decrementModCount();
                throw e2;
            }
        }

        @Override // java.util.List, java.util.Collection
        public boolean removeAll(Collection<?> c2) throws Exception {
            checkForComodification();
            VetoableListDecorator.this.removeFromList(this, this.offset, c2, false);
            try {
                incrementModCount();
                boolean res = this.subList.removeAll(c2);
                if (!res) {
                    decrementModCount();
                }
                return res;
            } catch (Exception e2) {
                decrementModCount();
                throw e2;
            }
        }

        @Override // java.util.List, java.util.Collection
        public boolean retainAll(Collection<?> c2) throws Exception {
            checkForComodification();
            VetoableListDecorator.this.removeFromList(this, this.offset, c2, true);
            try {
                incrementModCount();
                boolean res = this.subList.retainAll(c2);
                if (!res) {
                    decrementModCount();
                }
                return res;
            } catch (Exception e2) {
                decrementModCount();
                throw e2;
            }
        }

        @Override // java.util.List
        public void clear() throws Exception {
            checkForComodification();
            VetoableListDecorator.this.onProposedChange(Collections.emptyList(), this.offset, this.offset + size());
            try {
                incrementModCount();
                this.subList.clear();
            } catch (Exception e2) {
                decrementModCount();
                throw e2;
            }
        }

        @Override // java.util.List
        public E get(int index) {
            checkForComodification();
            return this.subList.get(index);
        }

        @Override // java.util.List
        public E set(int index, E element) {
            checkForComodification();
            VetoableListDecorator.this.onProposedChange(Collections.singletonList(element), this.offset + index, this.offset + index + 1);
            return this.subList.set(index, element);
        }

        @Override // java.util.List
        public void add(int index, E element) throws Exception {
            checkForComodification();
            VetoableListDecorator.this.onProposedChange(Collections.singletonList(element), this.offset + index, this.offset + index);
            try {
                incrementModCount();
                this.subList.add(index, element);
            } catch (Exception e2) {
                decrementModCount();
                throw e2;
            }
        }

        @Override // java.util.List
        public E remove(int index) throws Exception {
            checkForComodification();
            VetoableListDecorator.this.onProposedChange(Collections.emptyList(), this.offset + index, this.offset + index + 1);
            try {
                incrementModCount();
                E res = this.subList.remove(index);
                return res;
            } catch (Exception e2) {
                decrementModCount();
                throw e2;
            }
        }

        @Override // java.util.List
        public int indexOf(Object o2) {
            checkForComodification();
            return this.subList.indexOf(o2);
        }

        @Override // java.util.List
        public int lastIndexOf(Object o2) {
            checkForComodification();
            return this.subList.lastIndexOf(o2);
        }

        @Override // java.util.List
        public ListIterator<E> listIterator() {
            checkForComodification();
            return new VetoableListIteratorDecorator(new ModCountAccessorImplSub(), this.subList.listIterator(), this.offset);
        }

        @Override // java.util.List
        public ListIterator<E> listIterator(int index) {
            checkForComodification();
            return new VetoableListIteratorDecorator(new ModCountAccessorImplSub(), this.subList.listIterator(index), this.offset + index);
        }

        @Override // java.util.List
        public List<E> subList(int fromIndex, int toIndex) {
            checkForComodification();
            return new VetoableSubListDecorator(new ModCountAccessorImplSub(), this.subList.subList(fromIndex, toIndex), this.offset + fromIndex);
        }

        public String toString() {
            checkForComodification();
            return this.subList.toString();
        }

        @Override // java.util.List
        public boolean equals(Object obj) {
            checkForComodification();
            return this.subList.equals(obj);
        }

        @Override // java.util.List
        public int hashCode() {
            checkForComodification();
            return this.subList.hashCode();
        }

        private void checkForComodification() {
            if (this.modCount != this.modCountAccessor.get()) {
                throw new ConcurrentModificationException();
            }
        }

        private void incrementModCount() {
            this.modCount = this.modCountAccessor.incrementAndGet();
        }

        private void decrementModCount() {
            this.modCount = this.modCountAccessor.decrementAndGet();
        }

        /* loaded from: jfxrt.jar:com/sun/javafx/collections/VetoableListDecorator$VetoableSubListDecorator$ModCountAccessorImplSub.class */
        private class ModCountAccessorImplSub implements ModCountAccessor {
            private ModCountAccessorImplSub() {
            }

            @Override // com.sun.javafx.collections.VetoableListDecorator.ModCountAccessor
            public int get() {
                return VetoableSubListDecorator.this.modCount;
            }

            @Override // com.sun.javafx.collections.VetoableListDecorator.ModCountAccessor
            public int incrementAndGet() {
                return VetoableSubListDecorator.this.modCount = VetoableSubListDecorator.this.modCountAccessor.incrementAndGet();
            }

            @Override // com.sun.javafx.collections.VetoableListDecorator.ModCountAccessor
            public int decrementAndGet() {
                return VetoableSubListDecorator.this.modCount = VetoableSubListDecorator.this.modCountAccessor.decrementAndGet();
            }
        }
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/collections/VetoableListDecorator$VetoableIteratorDecorator.class */
    private class VetoableIteratorDecorator implements Iterator<E> {
        private final Iterator<E> it;
        private final ModCountAccessor modCountAccessor;
        private int modCount;
        protected final int offset;
        protected int cursor;
        protected int lastReturned;

        public VetoableIteratorDecorator(ModCountAccessor modCountAccessor, Iterator<E> it, int offset) {
            this.modCountAccessor = modCountAccessor;
            this.modCount = modCountAccessor.get();
            this.it = it;
            this.offset = offset;
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            checkForComodification();
            return this.it.hasNext();
        }

        @Override // java.util.Iterator
        public E next() {
            checkForComodification();
            E e2 = this.it.next();
            int i2 = this.cursor;
            this.cursor = i2 + 1;
            this.lastReturned = i2;
            return e2;
        }

        @Override // java.util.Iterator
        public void remove() throws Exception {
            checkForComodification();
            if (this.lastReturned == -1) {
                throw new IllegalStateException();
            }
            VetoableListDecorator.this.onProposedChange(Collections.emptyList(), this.offset + this.lastReturned, this.offset + this.lastReturned + 1);
            try {
                incrementModCount();
                this.it.remove();
                this.lastReturned = -1;
                this.cursor--;
            } catch (Exception e2) {
                decrementModCount();
                throw e2;
            }
        }

        protected void checkForComodification() {
            if (this.modCount != this.modCountAccessor.get()) {
                throw new ConcurrentModificationException();
            }
        }

        protected void incrementModCount() {
            this.modCount = this.modCountAccessor.incrementAndGet();
        }

        protected void decrementModCount() {
            this.modCount = this.modCountAccessor.decrementAndGet();
        }
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/collections/VetoableListDecorator$VetoableListIteratorDecorator.class */
    private class VetoableListIteratorDecorator extends VetoableListDecorator<E>.VetoableIteratorDecorator implements ListIterator<E> {
        private final ListIterator<E> lit;

        public VetoableListIteratorDecorator(ModCountAccessor modCountAccessor, ListIterator<E> it, int offset) {
            super(modCountAccessor, it, offset);
            this.lit = it;
        }

        @Override // java.util.ListIterator
        public boolean hasPrevious() {
            checkForComodification();
            return this.lit.hasPrevious();
        }

        @Override // java.util.ListIterator
        public E previous() {
            checkForComodification();
            E e2 = this.lit.previous();
            int i2 = this.cursor - 1;
            this.cursor = i2;
            this.lastReturned = i2;
            return e2;
        }

        @Override // java.util.ListIterator
        public int nextIndex() {
            checkForComodification();
            return this.lit.nextIndex();
        }

        @Override // java.util.ListIterator
        public int previousIndex() {
            checkForComodification();
            return this.lit.previousIndex();
        }

        @Override // java.util.ListIterator
        public void set(E e2) {
            checkForComodification();
            if (this.lastReturned == -1) {
                throw new IllegalStateException();
            }
            VetoableListDecorator.this.onProposedChange(Collections.singletonList(e2), this.offset + this.lastReturned, this.offset + this.lastReturned + 1);
            this.lit.set(e2);
        }

        @Override // java.util.ListIterator
        public void add(E e2) throws Exception {
            checkForComodification();
            VetoableListDecorator.this.onProposedChange(Collections.singletonList(e2), this.offset + this.cursor, this.offset + this.cursor);
            try {
                incrementModCount();
                this.lit.add(e2);
                this.cursor++;
            } catch (Exception ex) {
                decrementModCount();
                throw ex;
            }
        }
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/collections/VetoableListDecorator$ModCountAccessorImpl.class */
    private class ModCountAccessorImpl implements ModCountAccessor {
        public ModCountAccessorImpl() {
        }

        @Override // com.sun.javafx.collections.VetoableListDecorator.ModCountAccessor
        public int get() {
            return VetoableListDecorator.this.modCount;
        }

        @Override // com.sun.javafx.collections.VetoableListDecorator.ModCountAccessor
        public int incrementAndGet() {
            return VetoableListDecorator.access$404(VetoableListDecorator.this);
        }

        @Override // com.sun.javafx.collections.VetoableListDecorator.ModCountAccessor
        public int decrementAndGet() {
            return VetoableListDecorator.access$406(VetoableListDecorator.this);
        }
    }
}
