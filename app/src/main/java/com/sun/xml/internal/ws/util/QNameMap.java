package com.sun.xml.internal.ws.util;

import com.sun.istack.internal.NotNull;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import javax.xml.namespace.QName;

/* loaded from: rt.jar:com/sun/xml/internal/ws/util/QNameMap.class */
public final class QNameMap<V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final int MAXIMUM_CAPACITY = 1073741824;
    transient Entry<V>[] table;
    transient int size;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    static final /* synthetic */ boolean $assertionsDisabled;
    private Set<Entry<V>> entrySet = null;
    private transient Iterable<V> views = new Iterable<V>() { // from class: com.sun.xml.internal.ws.util.QNameMap.1
        @Override // java.lang.Iterable, java.util.List
        public Iterator<V> iterator() {
            return new ValueIterator();
        }
    };
    private int threshold = 12;

    static {
        $assertionsDisabled = !QNameMap.class.desiredAssertionStatus();
    }

    public QNameMap() {
        this.table = new Entry[16];
        this.table = new Entry[16];
    }

    public void put(String namespaceUri, String localname, V value) {
        if (!$assertionsDisabled && localname == null) {
            throw new AssertionError();
        }
        if (!$assertionsDisabled && namespaceUri == null) {
            throw new AssertionError();
        }
        int hash = hash(localname);
        int i2 = indexFor(hash, this.table.length);
        Entry<V> entry = this.table[i2];
        while (true) {
            Entry<V> e2 = entry;
            if (e2 != null) {
                if (e2.hash != hash || !localname.equals(e2.localName) || !namespaceUri.equals(e2.nsUri)) {
                    entry = e2.next;
                } else {
                    e2.value = value;
                    return;
                }
            } else {
                addEntry(hash, namespaceUri, localname, value, i2);
                return;
            }
        }
    }

    public void put(QName name, V value) {
        put(name.getNamespaceURI(), name.getLocalPart(), value);
    }

    public V get(@NotNull String nsUri, String localPart) {
        Entry<V> e2 = getEntry(nsUri, localPart);
        if (e2 == null) {
            return null;
        }
        return e2.value;
    }

    public V get(QName name) {
        return get(name.getNamespaceURI(), name.getLocalPart());
    }

    public int size() {
        return this.size;
    }

    public QNameMap<V> putAll(QNameMap<? extends V> map) {
        int newCapacity;
        int numKeysToBeAdded = map.size();
        if (numKeysToBeAdded == 0) {
            return this;
        }
        if (numKeysToBeAdded > this.threshold) {
            int targetCapacity = numKeysToBeAdded;
            if (targetCapacity > 1073741824) {
                targetCapacity = 1073741824;
            }
            int length = this.table.length;
            while (true) {
                newCapacity = length;
                if (newCapacity >= targetCapacity) {
                    break;
                }
                length = newCapacity << 1;
            }
            if (newCapacity > this.table.length) {
                resize(newCapacity);
            }
        }
        for (Entry<? extends V> e2 : map.entrySet()) {
            put(e2.nsUri, e2.localName, e2.getValue());
        }
        return this;
    }

    public QNameMap<V> putAll(Map<QName, ? extends V> map) {
        for (Map.Entry<QName, ? extends V> e2 : map.entrySet()) {
            QName qn = e2.getKey();
            put(qn.getNamespaceURI(), qn.getLocalPart(), e2.getValue());
        }
        return this;
    }

    private static int hash(String x2) {
        int h2 = x2.hashCode();
        int h3 = h2 + ((h2 << 9) ^ (-1));
        int h4 = h3 ^ (h3 >>> 14);
        int h5 = h4 + (h4 << 4);
        return h5 ^ (h5 >>> 10);
    }

    private static int indexFor(int h2, int length) {
        return h2 & (length - 1);
    }

    private void addEntry(int hash, String nsUri, String localName, V value, int bucketIndex) {
        Entry<V> e2 = this.table[bucketIndex];
        this.table[bucketIndex] = new Entry<>(hash, nsUri, localName, value, e2);
        int i2 = this.size;
        this.size = i2 + 1;
        if (i2 >= this.threshold) {
            resize(2 * this.table.length);
        }
    }

    private void resize(int newCapacity) {
        Entry[] oldTable = this.table;
        int oldCapacity = oldTable.length;
        if (oldCapacity == 1073741824) {
            this.threshold = Integer.MAX_VALUE;
            return;
        }
        Entry[] newTable = new Entry[newCapacity];
        transfer(newTable);
        this.table = newTable;
        this.threshold = newCapacity;
    }

    private void transfer(Entry<V>[] newTable) {
        Entry<V>[] src = this.table;
        int newCapacity = newTable.length;
        for (int j2 = 0; j2 < src.length; j2++) {
            Entry<V> e2 = src[j2];
            if (e2 != null) {
                src[j2] = null;
                do {
                    Entry<V> next = e2.next;
                    int i2 = indexFor(e2.hash, newCapacity);
                    e2.next = newTable[i2];
                    newTable[i2] = e2;
                    e2 = next;
                } while (e2 != null);
            }
        }
    }

    public Entry<V> getOne() {
        for (Entry<V> e2 : this.table) {
            if (e2 != null) {
                return e2;
            }
        }
        return null;
    }

    public Collection<QName> keySet() {
        Set<QName> r2 = new HashSet<>();
        for (Entry<V> e2 : entrySet()) {
            r2.add(e2.createQName());
        }
        return r2;
    }

    public Iterable<V> values() {
        return this.views;
    }

    /* loaded from: rt.jar:com/sun/xml/internal/ws/util/QNameMap$HashIterator.class */
    private abstract class HashIterator<E> implements Iterator<E> {
        Entry<V> next;
        int index;

        HashIterator() {
            Entry<V>[] t2 = QNameMap.this.table;
            int i2 = t2.length;
            Entry<V> n2 = null;
            if (QNameMap.this.size != 0) {
                while (i2 > 0) {
                    i2--;
                    Entry<V> entry = t2[i2];
                    n2 = entry;
                    if (entry != null) {
                        break;
                    }
                }
            }
            this.next = n2;
            this.index = i2;
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            return this.next != null;
        }

        Entry<V> nextEntry() {
            Entry<V> e2 = this.next;
            if (e2 == null) {
                throw new NoSuchElementException();
            }
            Entry<V> n2 = e2.next;
            Entry<V>[] t2 = QNameMap.this.table;
            int i2 = this.index;
            while (n2 == null && i2 > 0) {
                i2--;
                n2 = t2[i2];
            }
            this.index = i2;
            this.next = n2;
            return e2;
        }

        @Override // java.util.Iterator
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    /* loaded from: rt.jar:com/sun/xml/internal/ws/util/QNameMap$ValueIterator.class */
    private class ValueIterator extends QNameMap<V>.HashIterator<V> {
        private ValueIterator() {
            super();
        }

        @Override // java.util.Iterator
        public V next() {
            return nextEntry().value;
        }
    }

    public boolean containsKey(@NotNull String nsUri, String localName) {
        return getEntry(nsUri, localName) != null;
    }

    public boolean isEmpty() {
        return this.size == 0;
    }

    /* loaded from: rt.jar:com/sun/xml/internal/ws/util/QNameMap$Entry.class */
    public static final class Entry<V> {
        public final String nsUri;
        public final String localName;
        V value;
        final int hash;
        Entry<V> next;

        Entry(int h2, String nsUri, String localName, V v2, Entry<V> n2) {
            this.value = v2;
            this.next = n2;
            this.nsUri = nsUri;
            this.localName = localName;
            this.hash = h2;
        }

        public QName createQName() {
            return new QName(this.nsUri, this.localName);
        }

        public V getValue() {
            return this.value;
        }

        public V setValue(V newValue) {
            V oldValue = this.value;
            this.value = newValue;
            return oldValue;
        }

        public boolean equals(Object o2) {
            if (!(o2 instanceof Entry)) {
                return false;
            }
            Entry e2 = (Entry) o2;
            String k1 = this.nsUri;
            String k2 = e2.nsUri;
            String k3 = this.localName;
            String k4 = e2.localName;
            if (k1.equals(k2) && k3.equals(k4)) {
                Object v1 = getValue();
                Object v2 = e2.getValue();
                if (v1 == v2) {
                    return true;
                }
                if (v1 != null && v1.equals(v2)) {
                    return true;
                }
                return false;
            }
            return false;
        }

        public int hashCode() {
            return this.localName.hashCode() ^ (this.value == null ? 0 : this.value.hashCode());
        }

        public String toString() {
            return '\"' + this.nsUri + "\",\"" + this.localName + "\"=" + ((Object) getValue());
        }
    }

    public Set<Entry<V>> entrySet() {
        Set<Entry<V>> es = this.entrySet;
        if (es != null) {
            return es;
        }
        EntrySet entrySet = new EntrySet();
        this.entrySet = entrySet;
        return entrySet;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Iterator<Entry<V>> newEntryIterator() {
        return new EntryIterator();
    }

    /* loaded from: rt.jar:com/sun/xml/internal/ws/util/QNameMap$EntryIterator.class */
    private class EntryIterator extends QNameMap<V>.HashIterator<Entry<V>> {
        private EntryIterator() {
            super();
        }

        @Override // java.util.Iterator
        public Entry<V> next() {
            return nextEntry();
        }
    }

    /* loaded from: rt.jar:com/sun/xml/internal/ws/util/QNameMap$EntrySet.class */
    private class EntrySet extends AbstractSet<Entry<V>> {
        private EntrySet() {
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.List
        public Iterator<Entry<V>> iterator() {
            return QNameMap.this.newEntryIterator();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean contains(Object o2) {
            if (!(o2 instanceof Entry)) {
                return false;
            }
            Entry<V> e2 = (Entry) o2;
            Entry<V> candidate = QNameMap.this.getEntry(e2.nsUri, e2.localName);
            return candidate != null && candidate.equals(e2);
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean remove(Object o2) {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public int size() {
            return QNameMap.this.size;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Entry<V> getEntry(@NotNull String nsUri, String localName) {
        Entry<V> e2;
        int hash = hash(localName);
        int i2 = indexFor(hash, this.table.length);
        Entry<V> entry = this.table[i2];
        while (true) {
            e2 = entry;
            if (e2 == null || (localName.equals(e2.localName) && nsUri.equals(e2.nsUri))) {
                break;
            }
            entry = e2.next;
        }
        return e2;
    }

    public String toString() {
        StringBuilder buf = new StringBuilder();
        buf.append('{');
        for (Entry<V> e2 : entrySet()) {
            if (buf.length() > 1) {
                buf.append(',');
            }
            buf.append('[');
            buf.append((Object) e2);
            buf.append(']');
        }
        buf.append('}');
        return buf.toString();
    }
}
