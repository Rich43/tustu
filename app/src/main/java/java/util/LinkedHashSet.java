package java.util;

import java.io.Serializable;

/* loaded from: rt.jar:java/util/LinkedHashSet.class */
public class LinkedHashSet<E> extends HashSet<E> implements Set<E>, Cloneable, Serializable {
    private static final long serialVersionUID = -2851667679971038690L;

    public LinkedHashSet(int i2, float f2) {
        super(i2, f2, true);
    }

    public LinkedHashSet(int i2) {
        super(i2, 0.75f, true);
    }

    public LinkedHashSet() {
        super(16, 0.75f, true);
    }

    public LinkedHashSet(Collection<? extends E> collection) {
        super(Math.max(2 * collection.size(), 11), 0.75f, true);
        addAll(collection);
    }

    @Override // java.util.HashSet, java.util.Collection, java.lang.Iterable
    public Spliterator<E> spliterator() {
        return Spliterators.spliterator(this, 17);
    }
}
