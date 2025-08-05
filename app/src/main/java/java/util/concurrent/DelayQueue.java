package java.util.concurrent;

import java.util.AbstractQueue;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.PriorityQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/* loaded from: rt.jar:java/util/concurrent/DelayQueue.class */
public class DelayQueue<E extends Delayed> extends AbstractQueue<E> implements BlockingQueue<E> {
    private final transient ReentrantLock lock = new ReentrantLock();

    /* renamed from: q, reason: collision with root package name */
    private final PriorityQueue<E> f12581q = new PriorityQueue<>();
    private Thread leader = null;
    private final Condition available = this.lock.newCondition();

    public DelayQueue() {
    }

    public DelayQueue(Collection<? extends E> collection) {
        addAll(collection);
    }

    @Override // java.util.AbstractQueue, java.util.AbstractCollection, java.util.Collection, java.util.List
    public boolean add(E e2) {
        return offer((DelayQueue<E>) e2);
    }

    @Override // java.util.Queue
    public boolean offer(E e2) {
        ReentrantLock reentrantLock = this.lock;
        reentrantLock.lock();
        try {
            this.f12581q.offer(e2);
            if (this.f12581q.peek() == e2) {
                this.leader = null;
                this.available.signal();
            }
            return true;
        } finally {
            reentrantLock.unlock();
        }
    }

    @Override // java.util.concurrent.BlockingQueue
    public void put(E e2) {
        offer((DelayQueue<E>) e2);
    }

    @Override // java.util.concurrent.BlockingQueue
    public boolean offer(E e2, long j2, TimeUnit timeUnit) {
        return offer((DelayQueue<E>) e2);
    }

    @Override // java.util.Queue
    public E poll() {
        ReentrantLock reentrantLock = this.lock;
        reentrantLock.lock();
        try {
            E ePeek = this.f12581q.peek();
            if (ePeek == null || ePeek.getDelay(TimeUnit.NANOSECONDS) > 0) {
                return null;
            }
            E ePoll = this.f12581q.poll();
            reentrantLock.unlock();
            return ePoll;
        } finally {
            reentrantLock.unlock();
        }
    }

    @Override // java.util.concurrent.BlockingQueue
    /* renamed from: take */
    public E take2() throws InterruptedException {
        ReentrantLock reentrantLock = this.lock;
        reentrantLock.lockInterruptibly();
        while (true) {
            try {
                E ePeek = this.f12581q.peek();
                if (ePeek == null) {
                    this.available.await();
                } else {
                    long delay = ePeek.getDelay(TimeUnit.NANOSECONDS);
                    if (delay <= 0) {
                        break;
                    }
                    if (this.leader != null) {
                        this.available.await();
                    } else {
                        Thread threadCurrentThread = Thread.currentThread();
                        this.leader = threadCurrentThread;
                        try {
                            this.available.awaitNanos(delay);
                            if (this.leader == threadCurrentThread) {
                                this.leader = null;
                            }
                        } catch (Throwable th) {
                            if (this.leader == threadCurrentThread) {
                                this.leader = null;
                            }
                            throw th;
                        }
                    }
                }
            } catch (Throwable th2) {
                if (this.leader == null && this.f12581q.peek() != null) {
                    this.available.signal();
                }
                reentrantLock.unlock();
                throw th2;
            }
        }
        E ePoll = this.f12581q.poll();
        if (this.leader == null && this.f12581q.peek() != null) {
            this.available.signal();
        }
        reentrantLock.unlock();
        return ePoll;
    }

    @Override // java.util.concurrent.BlockingQueue
    /* renamed from: poll */
    public E poll2(long j2, TimeUnit timeUnit) throws InterruptedException {
        long nanos = timeUnit.toNanos(j2);
        ReentrantLock reentrantLock = this.lock;
        reentrantLock.lockInterruptibly();
        while (true) {
            try {
                E ePeek = this.f12581q.peek();
                if (ePeek != null) {
                    long delay = ePeek.getDelay(TimeUnit.NANOSECONDS);
                    if (delay <= 0) {
                        E ePoll = this.f12581q.poll();
                        if (this.leader == null && this.f12581q.peek() != null) {
                            this.available.signal();
                        }
                        reentrantLock.unlock();
                        return ePoll;
                    }
                    if (nanos <= 0) {
                        if (this.leader == null && this.f12581q.peek() != null) {
                            this.available.signal();
                        }
                        reentrantLock.unlock();
                        return null;
                    }
                    if (nanos < delay || this.leader != null) {
                        nanos = this.available.awaitNanos(nanos);
                    } else {
                        Thread threadCurrentThread = Thread.currentThread();
                        this.leader = threadCurrentThread;
                        try {
                            nanos -= delay - this.available.awaitNanos(delay);
                            if (this.leader == threadCurrentThread) {
                                this.leader = null;
                            }
                        } catch (Throwable th) {
                            if (this.leader == threadCurrentThread) {
                                this.leader = null;
                            }
                            throw th;
                        }
                    }
                } else {
                    if (nanos <= 0) {
                        return null;
                    }
                    nanos = this.available.awaitNanos(nanos);
                }
            } finally {
                if (this.leader == null && this.f12581q.peek() != null) {
                    this.available.signal();
                }
                reentrantLock.unlock();
            }
        }
    }

    @Override // java.util.Queue
    public E peek() {
        ReentrantLock reentrantLock = this.lock;
        reentrantLock.lock();
        try {
            return this.f12581q.peek();
        } finally {
            reentrantLock.unlock();
        }
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public int size() {
        ReentrantLock reentrantLock = this.lock;
        reentrantLock.lock();
        try {
            return this.f12581q.size();
        } finally {
            reentrantLock.unlock();
        }
    }

    private E peekExpired() {
        E ePeek = this.f12581q.peek();
        if (ePeek == null || ePeek.getDelay(TimeUnit.NANOSECONDS) > 0) {
            return null;
        }
        return ePeek;
    }

    @Override // java.util.concurrent.BlockingQueue
    public int drainTo(Collection<? super E> collection) {
        if (collection == null) {
            throw new NullPointerException();
        }
        if (collection == this) {
            throw new IllegalArgumentException();
        }
        ReentrantLock reentrantLock = this.lock;
        reentrantLock.lock();
        int i2 = 0;
        while (true) {
            try {
                Delayed delayedPeekExpired = peekExpired();
                if (delayedPeekExpired != null) {
                    collection.add(delayedPeekExpired);
                    this.f12581q.poll();
                    i2++;
                } else {
                    return i2;
                }
            } finally {
                reentrantLock.unlock();
            }
        }
    }

    @Override // java.util.concurrent.BlockingQueue
    public int drainTo(Collection<? super E> collection, int i2) {
        if (collection == null) {
            throw new NullPointerException();
        }
        if (collection == this) {
            throw new IllegalArgumentException();
        }
        if (i2 <= 0) {
            return 0;
        }
        ReentrantLock reentrantLock = this.lock;
        reentrantLock.lock();
        int i3 = 0;
        while (i3 < i2) {
            try {
                Delayed delayedPeekExpired = peekExpired();
                if (delayedPeekExpired == null) {
                    break;
                }
                collection.add(delayedPeekExpired);
                this.f12581q.poll();
                i3++;
            } finally {
                reentrantLock.unlock();
            }
        }
        return i3;
    }

    @Override // java.util.AbstractQueue, java.util.AbstractCollection, java.util.Collection, java.util.List
    public void clear() {
        ReentrantLock reentrantLock = this.lock;
        reentrantLock.lock();
        try {
            this.f12581q.clear();
        } finally {
            reentrantLock.unlock();
        }
    }

    @Override // java.util.concurrent.BlockingQueue
    public int remainingCapacity() {
        return Integer.MAX_VALUE;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
    public Object[] toArray() {
        ReentrantLock reentrantLock = this.lock;
        reentrantLock.lock();
        try {
            return this.f12581q.toArray();
        } finally {
            reentrantLock.unlock();
        }
    }

    @Override // java.util.AbstractCollection, java.util.Collection
    public <T> T[] toArray(T[] tArr) {
        ReentrantLock reentrantLock = this.lock;
        reentrantLock.lock();
        try {
            T[] tArr2 = (T[]) this.f12581q.toArray(tArr);
            reentrantLock.unlock();
            return tArr2;
        } catch (Throwable th) {
            reentrantLock.unlock();
            throw th;
        }
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public boolean remove(Object obj) {
        ReentrantLock reentrantLock = this.lock;
        reentrantLock.lock();
        try {
            boolean zRemove = this.f12581q.remove(obj);
            reentrantLock.unlock();
            return zRemove;
        } catch (Throwable th) {
            reentrantLock.unlock();
            throw th;
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:8:0x0024, code lost:
    
        r0.remove();
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    void removeEQ(java.lang.Object r4) {
        /*
            r3 = this;
            r0 = r3
            java.util.concurrent.locks.ReentrantLock r0 = r0.lock
            r5 = r0
            r0 = r5
            r0.lock()
            r0 = r3
            java.util.PriorityQueue<E extends java.util.concurrent.Delayed> r0 = r0.f12581q     // Catch: java.lang.Throwable -> L34
            java.util.Iterator r0 = r0.iterator()     // Catch: java.lang.Throwable -> L34
            r6 = r0
        L11:
            r0 = r6
            boolean r0 = r0.hasNext()     // Catch: java.lang.Throwable -> L34
            if (r0 == 0) goto L2d
            r0 = r4
            r1 = r6
            java.lang.Object r1 = r1.next()     // Catch: java.lang.Throwable -> L34
            if (r0 != r1) goto L11
            r0 = r6
            r0.remove()     // Catch: java.lang.Throwable -> L34
            goto L2d
        L2d:
            r0 = r5
            r0.unlock()
            goto L3d
        L34:
            r7 = move-exception
            r0 = r5
            r0.unlock()
            r0 = r7
            throw r0
        L3d:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.DelayQueue.removeEQ(java.lang.Object):void");
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.List
    public Iterator<E> iterator() {
        return new Itr(toArray());
    }

    /* loaded from: rt.jar:java/util/concurrent/DelayQueue$Itr.class */
    private class Itr implements Iterator<E> {
        final Object[] array;
        int cursor;
        int lastRet = -1;

        Itr(Object[] objArr) {
            this.array = objArr;
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            return this.cursor < this.array.length;
        }

        @Override // java.util.Iterator
        public E next() {
            if (this.cursor >= this.array.length) {
                throw new NoSuchElementException();
            }
            this.lastRet = this.cursor;
            Object[] objArr = this.array;
            int i2 = this.cursor;
            this.cursor = i2 + 1;
            return (E) objArr[i2];
        }

        @Override // java.util.Iterator
        public void remove() {
            if (this.lastRet < 0) {
                throw new IllegalStateException();
            }
            DelayQueue.this.removeEQ(this.array[this.lastRet]);
            this.lastRet = -1;
        }
    }
}
