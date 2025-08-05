package java.util.concurrent;

import java.awt.Event;
import java.io.Serializable;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;
import java.util.function.Predicate;

/* loaded from: rt.jar:java/util/concurrent/CopyOnWriteArraySet.class */
public class CopyOnWriteArraySet<E> extends AbstractSet<E> implements Serializable {
    private static final long serialVersionUID = 5457747651344034263L;

    /* renamed from: al, reason: collision with root package name */
    private final CopyOnWriteArrayList<E> f12579al;

    public CopyOnWriteArraySet() {
        this.f12579al = new CopyOnWriteArrayList<>();
    }

    public CopyOnWriteArraySet(Collection<? extends E> collection) {
        if (collection.getClass() == CopyOnWriteArraySet.class) {
            this.f12579al = new CopyOnWriteArrayList<>(((CopyOnWriteArraySet) collection).f12579al);
        } else {
            this.f12579al = new CopyOnWriteArrayList<>();
            this.f12579al.addAllAbsent(collection);
        }
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public int size() {
        return this.f12579al.size();
    }

    @Override // java.util.AbstractCollection, java.util.Collection
    public boolean isEmpty() {
        return this.f12579al.isEmpty();
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public boolean contains(Object obj) {
        return this.f12579al.contains(obj);
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
    public Object[] toArray() {
        return this.f12579al.toArray();
    }

    @Override // java.util.AbstractCollection, java.util.Collection
    public <T> T[] toArray(T[] tArr) {
        return (T[]) this.f12579al.toArray(tArr);
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
    public void clear() {
        this.f12579al.clear();
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public boolean remove(Object obj) {
        return this.f12579al.remove(obj);
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
    public boolean add(E e2) {
        return this.f12579al.addIfAbsent(e2);
    }

    @Override // java.util.AbstractCollection, java.util.Collection
    public boolean containsAll(Collection<?> collection) {
        return this.f12579al.containsAll(collection);
    }

    @Override // java.util.AbstractCollection, java.util.Collection
    public boolean addAll(Collection<? extends E> collection) {
        return this.f12579al.addAllAbsent(collection) > 0;
    }

    @Override // java.util.AbstractSet, java.util.AbstractCollection, java.util.Collection
    public boolean removeAll(Collection<?> collection) {
        return this.f12579al.removeAll(collection);
    }

    @Override // java.util.AbstractCollection, java.util.Collection
    public boolean retainAll(Collection<?> collection) {
        return this.f12579al.retainAll(collection);
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.List
    public Iterator<E> iterator() {
        return this.f12579al.iterator();
    }

    /* JADX WARN: Code restructure failed: missing block: B:24:0x0072, code lost:
    
        r0[r13] = true;
     */
    @Override // java.util.AbstractSet, java.util.Collection, java.util.List
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public boolean equals(java.lang.Object r5) {
        /*
            r4 = this;
            r0 = r5
            r1 = r4
            if (r0 != r1) goto L7
            r0 = 1
            return r0
        L7:
            r0 = r5
            boolean r0 = r0 instanceof java.util.Set
            if (r0 != 0) goto L10
            r0 = 0
            return r0
        L10:
            r0 = r5
            java.util.Set r0 = (java.util.Set) r0
            java.util.Set r0 = (java.util.Set) r0
            r6 = r0
            r0 = r6
            java.util.Iterator r0 = r0.iterator()
            r7 = r0
            r0 = r4
            java.util.concurrent.CopyOnWriteArrayList<E> r0 = r0.f12579al
            java.lang.Object[] r0 = r0.getArray()
            r8 = r0
            r0 = r8
            int r0 = r0.length
            r9 = r0
            r0 = r9
            boolean[] r0 = new boolean[r0]
            r10 = r0
            r0 = 0
            r11 = r0
        L36:
            r0 = r7
            boolean r0 = r0.hasNext()
            if (r0 == 0) goto L83
            int r11 = r11 + 1
            r0 = r11
            r1 = r9
            if (r0 <= r1) goto L4b
            r0 = 0
            return r0
        L4b:
            r0 = r7
            java.lang.Object r0 = r0.next()
            r12 = r0
            r0 = 0
            r13 = r0
        L56:
            r0 = r13
            r1 = r9
            if (r0 >= r1) goto L81
            r0 = r10
            r1 = r13
            r0 = r0[r1]
            if (r0 != 0) goto L7b
            r0 = r12
            r1 = r8
            r2 = r13
            r1 = r1[r2]
            boolean r0 = eq(r0, r1)
            if (r0 == 0) goto L7b
            r0 = r10
            r1 = r13
            r2 = 1
            r0[r1] = r2
            goto L36
        L7b:
            int r13 = r13 + 1
            goto L56
        L81:
            r0 = 0
            return r0
        L83:
            r0 = r11
            r1 = r9
            if (r0 != r1) goto L8e
            r0 = 1
            goto L8f
        L8e:
            r0 = 0
        L8f:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.CopyOnWriteArraySet.equals(java.lang.Object):boolean");
    }

    @Override // java.util.Collection
    public boolean removeIf(Predicate<? super E> predicate) {
        return this.f12579al.removeIf(predicate);
    }

    @Override // java.lang.Iterable
    public void forEach(Consumer<? super E> consumer) {
        this.f12579al.forEach(consumer);
    }

    @Override // java.util.Collection, java.lang.Iterable
    public Spliterator<E> spliterator() {
        return Spliterators.spliterator(this.f12579al.getArray(), Event.INSERT);
    }

    private static boolean eq(Object obj, Object obj2) {
        return obj == null ? obj2 == null : obj.equals(obj2);
    }
}
