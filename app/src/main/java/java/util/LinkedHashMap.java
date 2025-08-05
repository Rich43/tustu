package java.util;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;

/* loaded from: rt.jar:java/util/LinkedHashMap.class */
public class LinkedHashMap<K, V> extends HashMap<K, V> implements Map<K, V> {
    private static final long serialVersionUID = 3801124242820219131L;
    transient Entry<K, V> head;
    transient Entry<K, V> tail;
    final boolean accessOrder;

    /* loaded from: rt.jar:java/util/LinkedHashMap$Entry.class */
    static class Entry<K, V> extends HashMap.Node<K, V> {
        Entry<K, V> before;
        Entry<K, V> after;

        Entry(int i2, K k2, V v2, HashMap.Node<K, V> node) {
            super(i2, k2, v2, node);
        }
    }

    private void linkNodeLast(Entry<K, V> entry) {
        Entry<K, V> entry2 = this.tail;
        this.tail = entry;
        if (entry2 == null) {
            this.head = entry;
        } else {
            entry.before = entry2;
            entry2.after = entry;
        }
    }

    private void transferLinks(Entry<K, V> entry, Entry<K, V> entry2) {
        Entry<K, V> entry3 = entry.before;
        entry2.before = entry3;
        Entry<K, V> entry4 = entry.after;
        entry2.after = entry4;
        if (entry3 == null) {
            this.head = entry2;
        } else {
            entry3.after = entry2;
        }
        if (entry4 == null) {
            this.tail = entry2;
        } else {
            entry4.before = entry2;
        }
    }

    @Override // java.util.HashMap
    void reinitialize() {
        super.reinitialize();
        this.tail = null;
        this.head = null;
    }

    @Override // java.util.HashMap
    HashMap.Node<K, V> newNode(int i2, K k2, V v2, HashMap.Node<K, V> node) {
        Entry<K, V> entry = new Entry<>(i2, k2, v2, node);
        linkNodeLast(entry);
        return entry;
    }

    @Override // java.util.HashMap
    HashMap.Node<K, V> replacementNode(HashMap.Node<K, V> node, HashMap.Node<K, V> node2) {
        Entry<K, V> entry = (Entry) node;
        Entry<K, V> entry2 = new Entry<>(entry.hash, entry.key, entry.value, node2);
        transferLinks(entry, entry2);
        return entry2;
    }

    @Override // java.util.HashMap
    HashMap.TreeNode<K, V> newTreeNode(int i2, K k2, V v2, HashMap.Node<K, V> node) {
        HashMap.TreeNode<K, V> treeNode = new HashMap.TreeNode<>(i2, k2, v2, node);
        linkNodeLast(treeNode);
        return treeNode;
    }

    @Override // java.util.HashMap
    HashMap.TreeNode<K, V> replacementTreeNode(HashMap.Node<K, V> node, HashMap.Node<K, V> node2) {
        Entry<K, V> entry = (Entry) node;
        HashMap.TreeNode<K, V> treeNode = new HashMap.TreeNode<>(entry.hash, entry.key, entry.value, node2);
        transferLinks(entry, treeNode);
        return treeNode;
    }

    @Override // java.util.HashMap
    void afterNodeRemoval(HashMap.Node<K, V> node) {
        Entry entry = (Entry) node;
        Entry<K, V> entry2 = entry.before;
        Entry<K, V> entry3 = entry.after;
        entry.after = null;
        entry.before = null;
        if (entry2 == null) {
            this.head = entry3;
        } else {
            entry2.after = entry3;
        }
        if (entry3 == null) {
            this.tail = entry2;
        } else {
            entry3.before = entry2;
        }
    }

    @Override // java.util.HashMap
    void afterNodeInsertion(boolean z2) {
        Entry<K, V> entry;
        if (z2 && (entry = this.head) != null && removeEldestEntry(entry)) {
            K k2 = entry.key;
            removeNode(hash(k2), k2, null, false, true);
        }
    }

    @Override // java.util.HashMap
    void afterNodeAccess(HashMap.Node<K, V> node) {
        if (this.accessOrder) {
            Entry<K, V> entry = this.tail;
            Entry<K, V> entry2 = entry;
            if (entry != node) {
                Entry<K, V> entry3 = (Entry) node;
                Entry<K, V> entry4 = entry3.before;
                Entry<K, V> entry5 = entry3.after;
                entry3.after = null;
                if (entry4 == null) {
                    this.head = entry5;
                } else {
                    entry4.after = entry5;
                }
                if (entry5 != null) {
                    entry5.before = entry4;
                } else {
                    entry2 = entry4;
                }
                if (entry2 == null) {
                    this.head = entry3;
                } else {
                    entry3.before = entry2;
                    entry2.after = entry3;
                }
                this.tail = entry3;
                this.modCount++;
            }
        }
    }

    @Override // java.util.HashMap
    void internalWriteEntries(ObjectOutputStream objectOutputStream) throws IOException {
        Entry<K, V> entry = this.head;
        while (true) {
            Entry<K, V> entry2 = entry;
            if (entry2 != null) {
                objectOutputStream.writeObject(entry2.key);
                objectOutputStream.writeObject(entry2.value);
                entry = entry2.after;
            } else {
                return;
            }
        }
    }

    public LinkedHashMap(int i2, float f2) {
        super(i2, f2);
        this.accessOrder = false;
    }

    public LinkedHashMap(int i2) {
        super(i2);
        this.accessOrder = false;
    }

    public LinkedHashMap() {
        this.accessOrder = false;
    }

    public LinkedHashMap(Map<? extends K, ? extends V> map) {
        this.accessOrder = false;
        putMapEntries(map, false);
    }

    public LinkedHashMap(int i2, float f2, boolean z2) {
        super(i2, f2);
        this.accessOrder = z2;
    }

    @Override // java.util.HashMap, java.util.AbstractMap, java.util.Map
    public boolean containsValue(Object obj) {
        Entry<K, V> entry = this.head;
        while (true) {
            Entry<K, V> entry2 = entry;
            if (entry2 != null) {
                V v2 = entry2.value;
                if (v2 == obj) {
                    return true;
                }
                if (obj == null || !obj.equals(v2)) {
                    entry = entry2.after;
                } else {
                    return true;
                }
            } else {
                return false;
            }
        }
    }

    @Override // java.util.HashMap, java.util.AbstractMap, java.util.Map
    public V get(Object obj) {
        HashMap.Node<K, V> node = getNode(hash(obj), obj);
        if (node == null) {
            return null;
        }
        if (this.accessOrder) {
            afterNodeAccess(node);
        }
        return node.value;
    }

    @Override // java.util.HashMap, java.util.Map
    public V getOrDefault(Object obj, V v2) {
        HashMap.Node<K, V> node = getNode(hash(obj), obj);
        if (node == null) {
            return v2;
        }
        if (this.accessOrder) {
            afterNodeAccess(node);
        }
        return node.value;
    }

    @Override // java.util.HashMap, java.util.AbstractMap, java.util.Map
    public void clear() {
        super.clear();
        this.tail = null;
        this.head = null;
    }

    protected boolean removeEldestEntry(Map.Entry<K, V> entry) {
        return false;
    }

    @Override // java.util.HashMap, java.util.AbstractMap, java.util.Map
    public Set<K> keySet() {
        Set<K> linkedKeySet = this.keySet;
        if (linkedKeySet == null) {
            linkedKeySet = new LinkedKeySet();
            this.keySet = linkedKeySet;
        }
        return linkedKeySet;
    }

    /* loaded from: rt.jar:java/util/LinkedHashMap$LinkedKeySet.class */
    final class LinkedKeySet extends AbstractSet<K> {
        LinkedKeySet() {
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public final int size() {
            return LinkedHashMap.this.size;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
        public final void clear() {
            LinkedHashMap.this.clear();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.List
        public final Iterator<K> iterator() {
            return new LinkedKeyIterator();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public final boolean contains(Object obj) {
            return LinkedHashMap.this.containsKey(obj);
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public final boolean remove(Object obj) {
            return LinkedHashMap.this.removeNode(HashMap.hash(obj), obj, null, false, true) != null;
        }

        @Override // java.util.Collection, java.lang.Iterable
        public final Spliterator<K> spliterator() {
            return Spliterators.spliterator(this, 81);
        }

        @Override // java.lang.Iterable
        public final void forEach(Consumer<? super K> consumer) {
            if (consumer == null) {
                throw new NullPointerException();
            }
            int i2 = LinkedHashMap.this.modCount;
            Entry<K, V> entry = LinkedHashMap.this.head;
            while (true) {
                Entry<K, V> entry2 = entry;
                if (entry2 == null) {
                    break;
                }
                consumer.accept(entry2.key);
                entry = entry2.after;
            }
            if (LinkedHashMap.this.modCount != i2) {
                throw new ConcurrentModificationException();
            }
        }
    }

    @Override // java.util.HashMap, java.util.AbstractMap, java.util.Map
    public Collection<V> values() {
        Collection<V> linkedValues = this.values;
        if (linkedValues == null) {
            linkedValues = new LinkedValues();
            this.values = linkedValues;
        }
        return linkedValues;
    }

    /* loaded from: rt.jar:java/util/LinkedHashMap$LinkedValues.class */
    final class LinkedValues extends AbstractCollection<V> {
        LinkedValues() {
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public final int size() {
            return LinkedHashMap.this.size;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
        public final void clear() {
            LinkedHashMap.this.clear();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.List
        public final Iterator<V> iterator() {
            return new LinkedValueIterator();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public final boolean contains(Object obj) {
            return LinkedHashMap.this.containsValue(obj);
        }

        @Override // java.util.Collection, java.lang.Iterable
        public final Spliterator<V> spliterator() {
            return Spliterators.spliterator(this, 80);
        }

        @Override // java.lang.Iterable
        public final void forEach(Consumer<? super V> consumer) {
            if (consumer == null) {
                throw new NullPointerException();
            }
            int i2 = LinkedHashMap.this.modCount;
            Entry<K, V> entry = LinkedHashMap.this.head;
            while (true) {
                Entry<K, V> entry2 = entry;
                if (entry2 == null) {
                    break;
                }
                consumer.accept(entry2.value);
                entry = entry2.after;
            }
            if (LinkedHashMap.this.modCount != i2) {
                throw new ConcurrentModificationException();
            }
        }
    }

    @Override // java.util.HashMap, java.util.AbstractMap, java.util.Map
    public Set<Map.Entry<K, V>> entrySet() {
        Set<Map.Entry<K, V>> set = this.entrySet;
        if (set != null) {
            return set;
        }
        LinkedEntrySet linkedEntrySet = new LinkedEntrySet();
        this.entrySet = linkedEntrySet;
        return linkedEntrySet;
    }

    /* loaded from: rt.jar:java/util/LinkedHashMap$LinkedEntrySet.class */
    final class LinkedEntrySet extends AbstractSet<Map.Entry<K, V>> {
        LinkedEntrySet() {
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public final int size() {
            return LinkedHashMap.this.size;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
        public final void clear() {
            LinkedHashMap.this.clear();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.List
        public final Iterator<Map.Entry<K, V>> iterator() {
            return new LinkedEntryIterator();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public final boolean contains(Object obj) {
            if (!(obj instanceof Map.Entry)) {
                return false;
            }
            Map.Entry entry = (Map.Entry) obj;
            Object key = entry.getKey();
            HashMap.Node<K, V> node = LinkedHashMap.this.getNode(HashMap.hash(key), key);
            return node != null && node.equals(entry);
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public final boolean remove(Object obj) {
            if (obj instanceof Map.Entry) {
                Map.Entry entry = (Map.Entry) obj;
                Object key = entry.getKey();
                return LinkedHashMap.this.removeNode(HashMap.hash(key), key, entry.getValue(), true, true) != null;
            }
            return false;
        }

        @Override // java.util.Collection, java.lang.Iterable
        public final Spliterator<Map.Entry<K, V>> spliterator() {
            return Spliterators.spliterator(this, 81);
        }

        @Override // java.lang.Iterable
        public final void forEach(Consumer<? super Map.Entry<K, V>> consumer) {
            if (consumer == null) {
                throw new NullPointerException();
            }
            int i2 = LinkedHashMap.this.modCount;
            Entry<K, V> entry = LinkedHashMap.this.head;
            while (true) {
                Entry<K, V> entry2 = entry;
                if (entry2 == null) {
                    break;
                }
                consumer.accept(entry2);
                entry = entry2.after;
            }
            if (LinkedHashMap.this.modCount != i2) {
                throw new ConcurrentModificationException();
            }
        }
    }

    @Override // java.util.HashMap, java.util.Map
    public void forEach(BiConsumer<? super K, ? super V> biConsumer) {
        if (biConsumer == null) {
            throw new NullPointerException();
        }
        int i2 = this.modCount;
        Entry<K, V> entry = this.head;
        while (true) {
            Entry<K, V> entry2 = entry;
            if (entry2 == null) {
                break;
            }
            biConsumer.accept(entry2.key, entry2.value);
            entry = entry2.after;
        }
        if (this.modCount != i2) {
            throw new ConcurrentModificationException();
        }
    }

    @Override // java.util.HashMap, java.util.Map
    public void replaceAll(BiFunction<? super K, ? super V, ? extends V> biFunction) {
        if (biFunction == null) {
            throw new NullPointerException();
        }
        int i2 = this.modCount;
        Entry<K, V> entry = this.head;
        while (true) {
            Entry<K, V> entry2 = entry;
            if (entry2 == null) {
                break;
            }
            entry2.value = biFunction.apply(entry2.key, entry2.value);
            entry = entry2.after;
        }
        if (this.modCount != i2) {
            throw new ConcurrentModificationException();
        }
    }

    /* loaded from: rt.jar:java/util/LinkedHashMap$LinkedHashIterator.class */
    abstract class LinkedHashIterator {
        Entry<K, V> next;
        Entry<K, V> current = null;
        int expectedModCount;

        LinkedHashIterator() {
            this.next = LinkedHashMap.this.head;
            this.expectedModCount = LinkedHashMap.this.modCount;
        }

        public final boolean hasNext() {
            return this.next != null;
        }

        final Entry<K, V> nextNode() {
            Entry<K, V> entry = this.next;
            if (LinkedHashMap.this.modCount != this.expectedModCount) {
                throw new ConcurrentModificationException();
            }
            if (entry == null) {
                throw new NoSuchElementException();
            }
            this.current = entry;
            this.next = entry.after;
            return entry;
        }

        public final void remove() {
            Entry<K, V> entry = this.current;
            if (entry == null) {
                throw new IllegalStateException();
            }
            if (LinkedHashMap.this.modCount != this.expectedModCount) {
                throw new ConcurrentModificationException();
            }
            this.current = null;
            K k2 = entry.key;
            LinkedHashMap.this.removeNode(HashMap.hash(k2), k2, null, false, false);
            this.expectedModCount = LinkedHashMap.this.modCount;
        }
    }

    /* loaded from: rt.jar:java/util/LinkedHashMap$LinkedKeyIterator.class */
    final class LinkedKeyIterator extends LinkedHashMap<K, V>.LinkedHashIterator implements Iterator<K> {
        LinkedKeyIterator() {
            super();
        }

        @Override // java.util.Iterator
        public final K next() {
            return nextNode().getKey();
        }
    }

    /* loaded from: rt.jar:java/util/LinkedHashMap$LinkedValueIterator.class */
    final class LinkedValueIterator extends LinkedHashMap<K, V>.LinkedHashIterator implements Iterator<V> {
        LinkedValueIterator() {
            super();
        }

        @Override // java.util.Iterator
        public final V next() {
            return nextNode().value;
        }
    }

    /* loaded from: rt.jar:java/util/LinkedHashMap$LinkedEntryIterator.class */
    final class LinkedEntryIterator extends LinkedHashMap<K, V>.LinkedHashIterator implements Iterator<Map.Entry<K, V>> {
        LinkedEntryIterator() {
            super();
        }

        @Override // java.util.Iterator
        public final Map.Entry<K, V> next() {
            return nextNode();
        }
    }
}
