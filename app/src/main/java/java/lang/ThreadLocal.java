package java.lang;

import java.lang.ref.WeakReference;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;
import jdk.internal.misc.TerminatingThreadLocal;

/* loaded from: rt.jar:java/lang/ThreadLocal.class */
public class ThreadLocal<T> {
    private final int threadLocalHashCode = nextHashCode();
    private static AtomicInteger nextHashCode = new AtomicInteger();
    private static final int HASH_INCREMENT = 1640531527;

    private static int nextHashCode() {
        return nextHashCode.getAndAdd(HASH_INCREMENT);
    }

    protected T initialValue() {
        return null;
    }

    public static <S> ThreadLocal<S> withInitial(Supplier<? extends S> supplier) {
        return new SuppliedThreadLocal(supplier);
    }

    public T get() {
        ThreadLocalMap.Entry entry;
        ThreadLocalMap map = getMap(Thread.currentThread());
        if (map != null && (entry = map.getEntry(this)) != null) {
            return (T) entry.value;
        }
        return setInitialValue();
    }

    boolean isPresent() {
        ThreadLocalMap map = getMap(Thread.currentThread());
        return (map == null || map.getEntry(this) == null) ? false : true;
    }

    private T setInitialValue() {
        T tInitialValue = initialValue();
        Thread threadCurrentThread = Thread.currentThread();
        ThreadLocalMap map = getMap(threadCurrentThread);
        if (map == null) {
            createMap(threadCurrentThread, tInitialValue);
        } else {
            map.set(this, tInitialValue);
        }
        if (this instanceof TerminatingThreadLocal) {
            TerminatingThreadLocal.register((TerminatingThreadLocal) this);
        }
        return tInitialValue;
    }

    public void set(T t2) {
        Thread threadCurrentThread = Thread.currentThread();
        ThreadLocalMap map = getMap(threadCurrentThread);
        if (map == null) {
            createMap(threadCurrentThread, t2);
        } else {
            map.set(this, t2);
        }
    }

    public void remove() {
        ThreadLocalMap map = getMap(Thread.currentThread());
        if (map == null) {
            return;
        }
        map.remove(this);
    }

    ThreadLocalMap getMap(Thread thread) {
        return thread.threadLocals;
    }

    void createMap(Thread thread, T t2) {
        thread.threadLocals = new ThreadLocalMap((ThreadLocal<?>) this, (Object) t2);
    }

    static ThreadLocalMap createInheritedMap(ThreadLocalMap threadLocalMap) {
        return new ThreadLocalMap(threadLocalMap);
    }

    T childValue(T t2) {
        throw new UnsupportedOperationException();
    }

    /* loaded from: rt.jar:java/lang/ThreadLocal$SuppliedThreadLocal.class */
    static final class SuppliedThreadLocal<T> extends ThreadLocal<T> {
        private final Supplier<? extends T> supplier;

        SuppliedThreadLocal(Supplier<? extends T> supplier) {
            this.supplier = (Supplier) Objects.requireNonNull(supplier);
        }

        @Override // java.lang.ThreadLocal
        protected T initialValue() {
            return this.supplier.get();
        }
    }

    /* loaded from: rt.jar:java/lang/ThreadLocal$ThreadLocalMap.class */
    static class ThreadLocalMap {
        private static final int INITIAL_CAPACITY = 16;
        private Entry[] table;
        private int size;
        private int threshold;

        /* loaded from: rt.jar:java/lang/ThreadLocal$ThreadLocalMap$Entry.class */
        static class Entry extends WeakReference<ThreadLocal<?>> {
            Object value;

            Entry(ThreadLocal<?> threadLocal, Object obj) {
                super(threadLocal);
                this.value = obj;
            }
        }

        private void setThreshold(int i2) {
            this.threshold = (i2 * 2) / 3;
        }

        private static int nextIndex(int i2, int i3) {
            if (i2 + 1 < i3) {
                return i2 + 1;
            }
            return 0;
        }

        private static int prevIndex(int i2, int i3) {
            return i2 - 1 >= 0 ? i2 - 1 : i3 - 1;
        }

        ThreadLocalMap(ThreadLocal<?> threadLocal, Object obj) {
            this.size = 0;
            this.table = new Entry[16];
            this.table[((ThreadLocal) threadLocal).threadLocalHashCode & 15] = new Entry(threadLocal, obj);
            this.size = 1;
            setThreshold(16);
        }

        private ThreadLocalMap(ThreadLocalMap threadLocalMap) {
            ThreadLocal<?> threadLocal;
            int i2;
            this.size = 0;
            Entry[] entryArr = threadLocalMap.table;
            int length = entryArr.length;
            setThreshold(length);
            this.table = new Entry[length];
            for (Entry entry : entryArr) {
                if (entry != null && (threadLocal = entry.get()) != null) {
                    Entry entry2 = new Entry(threadLocal, threadLocal.childValue(entry.value));
                    int iNextIndex = ((ThreadLocal) threadLocal).threadLocalHashCode & (length - 1);
                    while (true) {
                        i2 = iNextIndex;
                        if (this.table[i2] == null) {
                            break;
                        } else {
                            iNextIndex = nextIndex(i2, length);
                        }
                    }
                    this.table[i2] = entry2;
                    this.size++;
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public Entry getEntry(ThreadLocal<?> threadLocal) {
            int length = ((ThreadLocal) threadLocal).threadLocalHashCode & (this.table.length - 1);
            Entry entry = this.table[length];
            if (entry != null && entry.get() == threadLocal) {
                return entry;
            }
            return getEntryAfterMiss(threadLocal, length, entry);
        }

        private Entry getEntryAfterMiss(ThreadLocal<?> threadLocal, int i2, Entry entry) {
            Entry[] entryArr = this.table;
            int length = entryArr.length;
            while (entry != null) {
                ThreadLocal<?> threadLocal2 = entry.get();
                if (threadLocal2 == threadLocal) {
                    return entry;
                }
                if (threadLocal2 == null) {
                    expungeStaleEntry(i2);
                } else {
                    i2 = nextIndex(i2, length);
                }
                entry = entryArr[i2];
            }
            return null;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void set(ThreadLocal<?> threadLocal, Object obj) {
            Entry[] entryArr = this.table;
            int length = entryArr.length;
            int i2 = ((ThreadLocal) threadLocal).threadLocalHashCode & (length - 1);
            Entry entry = entryArr[i2];
            while (true) {
                Entry entry2 = entry;
                if (entry2 != null) {
                    ThreadLocal<?> threadLocal2 = entry2.get();
                    if (threadLocal2 == threadLocal) {
                        entry2.value = obj;
                        return;
                    } else if (threadLocal2 != null) {
                        int iNextIndex = nextIndex(i2, length);
                        i2 = iNextIndex;
                        entry = entryArr[iNextIndex];
                    } else {
                        replaceStaleEntry(threadLocal, obj, i2);
                        return;
                    }
                } else {
                    entryArr[i2] = new Entry(threadLocal, obj);
                    int i3 = this.size + 1;
                    this.size = i3;
                    if (!cleanSomeSlots(i2, i3) && i3 >= this.threshold) {
                        rehash();
                        return;
                    }
                    return;
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void remove(ThreadLocal<?> threadLocal) {
            Entry[] entryArr = this.table;
            int length = entryArr.length;
            int i2 = ((ThreadLocal) threadLocal).threadLocalHashCode & (length - 1);
            Entry entry = entryArr[i2];
            while (true) {
                Entry entry2 = entry;
                if (entry2 != null) {
                    if (entry2.get() != threadLocal) {
                        int iNextIndex = nextIndex(i2, length);
                        i2 = iNextIndex;
                        entry = entryArr[iNextIndex];
                    } else {
                        entry2.clear();
                        expungeStaleEntry(i2);
                        return;
                    }
                } else {
                    return;
                }
            }
        }

        private void replaceStaleEntry(ThreadLocal<?> threadLocal, Object obj, int i2) {
            Entry[] entryArr = this.table;
            int length = entryArr.length;
            int i3 = i2;
            int iPrevIndex = prevIndex(i2, length);
            while (true) {
                int i4 = iPrevIndex;
                Entry entry = entryArr[i4];
                if (entry == null) {
                    break;
                }
                if (entry.get() == null) {
                    i3 = i4;
                }
                iPrevIndex = prevIndex(i4, length);
            }
            int iNextIndex = nextIndex(i2, length);
            while (true) {
                int i5 = iNextIndex;
                Entry entry2 = entryArr[i5];
                if (entry2 != null) {
                    ThreadLocal<?> threadLocal2 = entry2.get();
                    if (threadLocal2 == threadLocal) {
                        entry2.value = obj;
                        entryArr[i5] = entryArr[i2];
                        entryArr[i2] = entry2;
                        if (i3 == i2) {
                            i3 = i5;
                        }
                        cleanSomeSlots(expungeStaleEntry(i3), length);
                        return;
                    }
                    if (threadLocal2 == null && i3 == i2) {
                        i3 = i5;
                    }
                    iNextIndex = nextIndex(i5, length);
                } else {
                    entryArr[i2].value = null;
                    entryArr[i2] = new Entry(threadLocal, obj);
                    if (i3 != i2) {
                        cleanSomeSlots(expungeStaleEntry(i3), length);
                        return;
                    }
                    return;
                }
            }
        }

        private int expungeStaleEntry(int i2) {
            Entry[] entryArr = this.table;
            int length = entryArr.length;
            entryArr[i2].value = null;
            entryArr[i2] = null;
            this.size--;
            int iNextIndex = nextIndex(i2, length);
            while (true) {
                int i3 = iNextIndex;
                Entry entry = entryArr[i3];
                if (entry != null) {
                    ThreadLocal<?> threadLocal = entry.get();
                    if (threadLocal != null) {
                        int iNextIndex2 = ((ThreadLocal) threadLocal).threadLocalHashCode & (length - 1);
                        if (iNextIndex2 != i3) {
                            entryArr[i3] = null;
                            while (entryArr[iNextIndex2] != null) {
                                iNextIndex2 = nextIndex(iNextIndex2, length);
                            }
                            entryArr[iNextIndex2] = entry;
                        }
                    } else {
                        entry.value = null;
                        entryArr[i3] = null;
                        this.size--;
                    }
                    iNextIndex = nextIndex(i3, length);
                } else {
                    return i3;
                }
            }
        }

        private boolean cleanSomeSlots(int i2, int i3) {
            int i4;
            boolean z2 = false;
            Entry[] entryArr = this.table;
            int length = entryArr.length;
            do {
                i2 = nextIndex(i2, length);
                Entry entry = entryArr[i2];
                if (entry != null && entry.get() == null) {
                    i3 = length;
                    z2 = true;
                    i2 = expungeStaleEntry(i2);
                }
                i4 = i3 >>> 1;
                i3 = i4;
            } while (i4 != 0);
            return z2;
        }

        private void rehash() {
            expungeStaleEntries();
            if (this.size >= this.threshold - (this.threshold / 4)) {
                resize();
            }
        }

        private void resize() {
            int i2;
            Entry[] entryArr = this.table;
            int length = entryArr.length * 2;
            Entry[] entryArr2 = new Entry[length];
            int i3 = 0;
            for (Entry entry : entryArr) {
                if (entry != null) {
                    ThreadLocal<?> threadLocal = entry.get();
                    if (threadLocal != null) {
                        int iNextIndex = ((ThreadLocal) threadLocal).threadLocalHashCode & (length - 1);
                        while (true) {
                            i2 = iNextIndex;
                            if (entryArr2[i2] == null) {
                                break;
                            } else {
                                iNextIndex = nextIndex(i2, length);
                            }
                        }
                        entryArr2[i2] = entry;
                        i3++;
                    } else {
                        entry.value = null;
                    }
                }
            }
            setThreshold(length);
            this.size = i3;
            this.table = entryArr2;
        }

        private void expungeStaleEntries() {
            Entry[] entryArr = this.table;
            int length = entryArr.length;
            for (int i2 = 0; i2 < length; i2++) {
                Entry entry = entryArr[i2];
                if (entry != null && entry.get() == null) {
                    expungeStaleEntry(i2);
                }
            }
        }
    }
}
