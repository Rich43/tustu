package java.beans;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;

/* loaded from: rt.jar:java/beans/WeakIdentityMap.class */
abstract class WeakIdentityMap<T> {
    private static final int MAXIMUM_CAPACITY = 1073741824;
    private static final Object NULL = new Object();
    private final ReferenceQueue<Object> queue = new ReferenceQueue<>();
    private volatile Entry<T>[] table = newTable(8);
    private int threshold = 6;
    private int size = 0;

    protected abstract T create(Object obj);

    WeakIdentityMap() {
    }

    public T get(Object obj) {
        removeStaleEntries();
        if (obj == null) {
            obj = NULL;
        }
        int iHashCode = obj.hashCode();
        Entry<T>[] entryArr = this.table;
        Entry<T> entry = entryArr[getIndex(entryArr, iHashCode)];
        while (true) {
            Entry<T> entry2 = entry;
            if (entry2 != null) {
                if (entry2.isMatched(obj, iHashCode)) {
                    return (T) ((Entry) entry2).value;
                }
                entry = ((Entry) entry2).next;
            } else {
                synchronized (NULL) {
                    int index = getIndex(this.table, iHashCode);
                    for (Entry<T> entry3 = this.table[index]; entry3 != null; entry3 = ((Entry) entry3).next) {
                        if (entry3.isMatched(obj, iHashCode)) {
                            return (T) ((Entry) entry3).value;
                        }
                    }
                    T tCreate = create(obj);
                    this.table[index] = new Entry<>(obj, iHashCode, tCreate, this.queue, this.table[index]);
                    int i2 = this.size + 1;
                    this.size = i2;
                    if (i2 >= this.threshold) {
                        if (this.table.length == 1073741824) {
                            this.threshold = Integer.MAX_VALUE;
                        } else {
                            removeStaleEntries();
                            Entry<T>[] entryArrNewTable = newTable(this.table.length * 2);
                            transfer(this.table, entryArrNewTable);
                            if (this.size >= this.threshold / 2) {
                                this.table = entryArrNewTable;
                                this.threshold *= 2;
                            } else {
                                transfer(entryArrNewTable, this.table);
                            }
                        }
                    }
                    return tCreate;
                }
            }
        }
    }

    private void removeStaleEntries() {
        Reference<? extends Object> referencePoll = this.queue.poll();
        if (referencePoll != null) {
            synchronized (NULL) {
                do {
                    Entry<T> entry = (Entry) referencePoll;
                    int index = getIndex(this.table, ((Entry) entry).hash);
                    Entry<T> entry2 = this.table[index];
                    Entry<T> entry3 = entry2;
                    while (true) {
                        if (entry3 == null) {
                            break;
                        }
                        Entry<T> entry4 = ((Entry) entry3).next;
                        if (entry3 == entry) {
                            if (entry2 == entry) {
                                this.table[index] = entry4;
                            } else {
                                ((Entry) entry2).next = entry4;
                            }
                            ((Entry) entry).value = null;
                            ((Entry) entry).next = null;
                            this.size--;
                        } else {
                            entry2 = entry3;
                            entry3 = entry4;
                        }
                    }
                    referencePoll = this.queue.poll();
                } while (referencePoll != null);
            }
        }
    }

    private void transfer(Entry<T>[] entryArr, Entry<T>[] entryArr2) {
        for (int i2 = 0; i2 < entryArr.length; i2++) {
            Entry<T> entry = entryArr[i2];
            entryArr[i2] = null;
            while (entry != null) {
                Entry<T> entry2 = ((Entry) entry).next;
                if (entry.get() != null) {
                    int index = getIndex(entryArr2, ((Entry) entry).hash);
                    ((Entry) entry).next = entryArr2[index];
                    entryArr2[index] = entry;
                } else {
                    ((Entry) entry).value = null;
                    ((Entry) entry).next = null;
                    this.size--;
                }
                entry = entry2;
            }
        }
    }

    private Entry<T>[] newTable(int i2) {
        return new Entry[i2];
    }

    private static int getIndex(Entry<?>[] entryArr, int i2) {
        return i2 & (entryArr.length - 1);
    }

    /* loaded from: rt.jar:java/beans/WeakIdentityMap$Entry.class */
    private static class Entry<T> extends WeakReference<Object> {
        private final int hash;
        private volatile T value;
        private volatile Entry<T> next;

        Entry(Object obj, int i2, T t2, ReferenceQueue<Object> referenceQueue, Entry<T> entry) {
            super(obj, referenceQueue);
            this.hash = i2;
            this.value = t2;
            this.next = entry;
        }

        boolean isMatched(Object obj, int i2) {
            return this.hash == i2 && obj == get();
        }
    }
}
