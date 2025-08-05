package sun.misc;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

/* loaded from: rt.jar:sun/misc/SoftCache.class */
public class SoftCache extends AbstractMap implements Map {
    private Map hash;
    private ReferenceQueue queue;
    private Set entrySet;

    /* loaded from: rt.jar:sun/misc/SoftCache$ValueCell.class */
    private static class ValueCell extends SoftReference {
        private static Object INVALID_KEY = new Object();
        private static int dropped = 0;
        private Object key;

        static /* synthetic */ int access$210() {
            int i2 = dropped;
            dropped = i2 - 1;
            return i2;
        }

        private ValueCell(Object obj, Object obj2, ReferenceQueue referenceQueue) {
            super(obj2, referenceQueue);
            this.key = obj;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static ValueCell create(Object obj, Object obj2, ReferenceQueue referenceQueue) {
            if (obj2 == null) {
                return null;
            }
            return new ValueCell(obj, obj2, referenceQueue);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static Object strip(Object obj, boolean z2) {
            if (obj == null) {
                return null;
            }
            ValueCell valueCell = (ValueCell) obj;
            Object obj2 = valueCell.get();
            if (z2) {
                valueCell.drop();
            }
            return obj2;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean isValid() {
            return this.key != INVALID_KEY;
        }

        private void drop() {
            super.clear();
            this.key = INVALID_KEY;
            dropped++;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processQueue() {
        while (true) {
            ValueCell valueCell = (ValueCell) this.queue.poll();
            if (valueCell != null) {
                if (valueCell.isValid()) {
                    this.hash.remove(valueCell.key);
                } else {
                    ValueCell.access$210();
                }
            } else {
                return;
            }
        }
    }

    public SoftCache(int i2, float f2) {
        this.queue = new ReferenceQueue();
        this.entrySet = null;
        this.hash = new HashMap(i2, f2);
    }

    public SoftCache(int i2) {
        this.queue = new ReferenceQueue();
        this.entrySet = null;
        this.hash = new HashMap(i2);
    }

    public SoftCache() {
        this.queue = new ReferenceQueue();
        this.entrySet = null;
        this.hash = new HashMap();
    }

    @Override // java.util.AbstractMap, java.util.Map
    public int size() {
        return entrySet().size();
    }

    @Override // java.util.AbstractMap, java.util.Map
    public boolean isEmpty() {
        return entrySet().isEmpty();
    }

    @Override // java.util.AbstractMap, java.util.Map
    public boolean containsKey(Object obj) {
        return ValueCell.strip(this.hash.get(obj), false) != null;
    }

    protected Object fill(Object obj) {
        return null;
    }

    @Override // java.util.AbstractMap, java.util.Map
    public Object get(Object obj) {
        processQueue();
        Object objFill = this.hash.get(obj);
        if (objFill == null) {
            objFill = fill(obj);
            if (objFill != null) {
                this.hash.put(obj, ValueCell.create(obj, objFill, this.queue));
                return objFill;
            }
        }
        return ValueCell.strip(objFill, false);
    }

    @Override // java.util.AbstractMap, java.util.Map
    public Object put(Object obj, Object obj2) {
        processQueue();
        return ValueCell.strip(this.hash.put(obj, ValueCell.create(obj, obj2, this.queue)), true);
    }

    @Override // java.util.AbstractMap, java.util.Map
    public Object remove(Object obj) {
        processQueue();
        return ValueCell.strip(this.hash.remove(obj), true);
    }

    @Override // java.util.AbstractMap, java.util.Map
    public void clear() {
        processQueue();
        this.hash.clear();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean valEquals(Object obj, Object obj2) {
        return obj == null ? obj2 == null : obj.equals(obj2);
    }

    /* loaded from: rt.jar:sun/misc/SoftCache$Entry.class */
    private class Entry implements Map.Entry {
        private Map.Entry ent;
        private Object value;

        Entry(Map.Entry entry, Object obj) {
            this.ent = entry;
            this.value = obj;
        }

        @Override // java.util.Map.Entry
        public Object getKey() {
            return this.ent.getKey();
        }

        @Override // java.util.Map.Entry
        public Object getValue() {
            return this.value;
        }

        @Override // java.util.Map.Entry
        public Object setValue(Object obj) {
            return this.ent.setValue(ValueCell.create(this.ent.getKey(), obj, SoftCache.this.queue));
        }

        @Override // java.util.Map.Entry
        public boolean equals(Object obj) {
            if (!(obj instanceof Map.Entry)) {
                return false;
            }
            Map.Entry entry = (Map.Entry) obj;
            return SoftCache.valEquals(this.ent.getKey(), entry.getKey()) && SoftCache.valEquals(this.value, entry.getValue());
        }

        @Override // java.util.Map.Entry
        public int hashCode() {
            Object key = getKey();
            return (key == null ? 0 : key.hashCode()) ^ (this.value == null ? 0 : this.value.hashCode());
        }
    }

    /* loaded from: rt.jar:sun/misc/SoftCache$EntrySet.class */
    private class EntrySet extends AbstractSet {
        Set hashEntries;

        private EntrySet() {
            this.hashEntries = SoftCache.this.hash.entrySet();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.List
        public Iterator iterator() {
            return new Iterator() { // from class: sun.misc.SoftCache.EntrySet.1
                Iterator hashIterator;
                Entry next = null;

                {
                    this.hashIterator = EntrySet.this.hashEntries.iterator();
                }

                @Override // java.util.Iterator
                public boolean hasNext() {
                    while (this.hashIterator.hasNext()) {
                        Map.Entry entry = (Map.Entry) this.hashIterator.next();
                        ValueCell valueCell = (ValueCell) entry.getValue();
                        Object obj = null;
                        if (valueCell != null) {
                            Object obj2 = valueCell.get();
                            obj = obj2;
                            if (obj2 == null) {
                            }
                        }
                        this.next = SoftCache.this.new Entry(entry, obj);
                        return true;
                    }
                    return false;
                }

                @Override // java.util.Iterator
                public Object next() {
                    if (this.next == null && !hasNext()) {
                        throw new NoSuchElementException();
                    }
                    Entry entry = this.next;
                    this.next = null;
                    return entry;
                }

                @Override // java.util.Iterator
                public void remove() {
                    this.hashIterator.remove();
                }
            };
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public boolean isEmpty() {
            return !iterator().hasNext();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public int size() {
            int i2 = 0;
            Iterator it = iterator();
            while (it.hasNext()) {
                i2++;
                it.next();
            }
            return i2;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean remove(Object obj) {
            SoftCache.this.processQueue();
            if (obj instanceof Entry) {
                return this.hashEntries.remove(((Entry) obj).ent);
            }
            return false;
        }
    }

    @Override // java.util.AbstractMap, java.util.Map
    public Set entrySet() {
        if (this.entrySet == null) {
            this.entrySet = new EntrySet();
        }
        return this.entrySet;
    }
}
