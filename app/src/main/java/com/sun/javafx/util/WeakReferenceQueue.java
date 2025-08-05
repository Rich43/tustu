package com.sun.javafx.util;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.Iterator;

/* loaded from: jfxrt.jar:com/sun/javafx/util/WeakReferenceQueue.class */
public class WeakReferenceQueue<E> {
    private final ReferenceQueue garbage = new ReferenceQueue();
    private Object strongRef = new Object();
    private ListEntry head = new ListEntry(this.strongRef, this.garbage);
    int size = 0;

    public void add(E obj) {
        cleanup();
        this.size++;
        new ListEntry(obj, this.garbage).insert(this.head.prev);
    }

    public void remove(E obj) {
        cleanup();
        ListEntry listEntry = this.head.next;
        while (true) {
            ListEntry entry = listEntry;
            if (entry != this.head) {
                Object other = entry.get();
                if (other == obj) {
                    this.size--;
                    entry.remove();
                    return;
                }
                listEntry = entry.next;
            } else {
                return;
            }
        }
    }

    public void cleanup() {
        while (true) {
            ListEntry entry = (ListEntry) this.garbage.poll();
            if (entry != null) {
                this.size--;
                entry.remove();
            } else {
                return;
            }
        }
    }

    public Iterator<? super E> iterator() {
        return new Iterator() { // from class: com.sun.javafx.util.WeakReferenceQueue.1
            private ListEntry index;
            private Object next = null;

            {
                this.index = WeakReferenceQueue.this.head;
            }

            @Override // java.util.Iterator
            public boolean hasNext() {
                ListEntry nextIndex;
                this.next = null;
                while (this.next == null && (nextIndex = this.index.prev) != WeakReferenceQueue.this.head) {
                    this.next = nextIndex.get();
                    if (this.next == null) {
                        WeakReferenceQueue.this.size--;
                        nextIndex.remove();
                    }
                }
                return this.next != null;
            }

            @Override // java.util.Iterator
            public Object next() {
                hasNext();
                this.index = this.index.prev;
                return this.next;
            }

            @Override // java.util.Iterator
            public void remove() {
                if (this.index != WeakReferenceQueue.this.head) {
                    ListEntry nextIndex = this.index.next;
                    WeakReferenceQueue.this.size--;
                    this.index.remove();
                    this.index = nextIndex;
                }
            }
        };
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/util/WeakReferenceQueue$ListEntry.class */
    private static class ListEntry extends WeakReference {
        ListEntry prev;
        ListEntry next;

        public ListEntry(Object o2, ReferenceQueue queue) {
            super(o2, queue);
            this.prev = this;
            this.next = this;
        }

        public void insert(ListEntry where) {
            this.prev = where;
            this.next = where.next;
            where.next = this;
            this.next.prev = this;
        }

        public void remove() {
            this.prev.next = this.next;
            this.next.prev = this.prev;
            this.next = this;
            this.prev = this;
        }
    }
}
