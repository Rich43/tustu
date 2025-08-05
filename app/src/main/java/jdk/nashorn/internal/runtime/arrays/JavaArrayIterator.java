package jdk.nashorn.internal.runtime.arrays;

import java.lang.reflect.Array;

/* loaded from: nashorn.jar:jdk/nashorn/internal/runtime/arrays/JavaArrayIterator.class */
class JavaArrayIterator extends ArrayLikeIterator<Object> {
    protected final Object array;
    protected final long length;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !JavaArrayIterator.class.desiredAssertionStatus();
    }

    protected JavaArrayIterator(Object array, boolean includeUndefined) {
        super(includeUndefined);
        if (!$assertionsDisabled && !array.getClass().isArray()) {
            throw new AssertionError((Object) "expecting Java array object");
        }
        this.array = array;
        this.length = Array.getLength(array);
    }

    protected boolean indexInArray() {
        return this.index < this.length;
    }

    @Override // java.util.Iterator
    public Object next() {
        return Array.get(this.array, (int) bumpIndex());
    }

    @Override // jdk.nashorn.internal.runtime.arrays.ArrayLikeIterator
    public long getLength() {
        return this.length;
    }

    @Override // java.util.Iterator
    public boolean hasNext() {
        return indexInArray();
    }

    @Override // jdk.nashorn.internal.runtime.arrays.ArrayLikeIterator, java.util.Iterator
    public void remove() {
        throw new UnsupportedOperationException("remove");
    }
}
