package jdk.nashorn.internal.runtime.arrays;

import java.lang.reflect.Array;
import jdk.nashorn.internal.runtime.ScriptRuntime;

/* loaded from: nashorn.jar:jdk/nashorn/internal/runtime/arrays/DeletedRangeArrayFilter.class */
final class DeletedRangeArrayFilter extends ArrayFilter {
    private long lo;
    private long hi;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !DeletedRangeArrayFilter.class.desiredAssertionStatus();
    }

    DeletedRangeArrayFilter(ArrayData underlying, long lo, long hi) {
        super(maybeSparse(underlying, hi));
        this.lo = lo;
        this.hi = hi;
    }

    private static ArrayData maybeSparse(ArrayData underlying, long hi) {
        if (hi < 131072 || (underlying instanceof SparseArrayData)) {
            return underlying;
        }
        return new SparseArrayData(underlying, underlying.length());
    }

    private boolean isEmpty() {
        return this.lo > this.hi;
    }

    private boolean isDeleted(int index) {
        long longIndex = ArrayIndex.toLongIndex(index);
        return this.lo <= longIndex && longIndex <= this.hi;
    }

    @Override // jdk.nashorn.internal.runtime.arrays.ArrayData
    public ArrayData copy() {
        return new DeletedRangeArrayFilter(this.underlying.copy(), this.lo, this.hi);
    }

    @Override // jdk.nashorn.internal.runtime.arrays.ArrayFilter, jdk.nashorn.internal.runtime.arrays.ArrayData
    public Object[] asObjectArray() {
        Object[] value = super.asObjectArray();
        if (this.lo < 2147483647L) {
            int end = (int) Math.min(this.hi + 1, 2147483647L);
            for (int i2 = (int) this.lo; i2 < end; i2++) {
                value[i2] = ScriptRuntime.UNDEFINED;
            }
        }
        return value;
    }

    @Override // jdk.nashorn.internal.runtime.arrays.ArrayFilter, jdk.nashorn.internal.runtime.arrays.ArrayData
    public Object asArrayOfType(Class<?> componentType) throws ArrayIndexOutOfBoundsException, IllegalArgumentException {
        Object value = super.asArrayOfType(componentType);
        Object undefValue = convertUndefinedValue(componentType);
        if (this.lo < 2147483647L) {
            int end = (int) Math.min(this.hi + 1, 2147483647L);
            for (int i2 = (int) this.lo; i2 < end; i2++) {
                Array.set(value, i2, undefValue);
            }
        }
        return value;
    }

    @Override // jdk.nashorn.internal.runtime.arrays.ArrayFilter, jdk.nashorn.internal.runtime.arrays.ArrayData
    public ArrayData ensure(long safeIndex) {
        if (safeIndex >= 131072 && safeIndex >= length()) {
            return new SparseArrayData(this, safeIndex + 1);
        }
        return super.ensure(safeIndex);
    }

    @Override // jdk.nashorn.internal.runtime.arrays.ArrayFilter, jdk.nashorn.internal.runtime.arrays.ArrayData
    public ArrayData shiftLeft(int by2) {
        super.shiftLeft(by2);
        this.lo = Math.max(0L, this.lo - by2);
        this.hi = Math.max(-1L, this.hi - by2);
        return isEmpty() ? getUnderlying() : this;
    }

    @Override // jdk.nashorn.internal.runtime.arrays.ArrayFilter, jdk.nashorn.internal.runtime.arrays.ArrayData
    public ArrayData shiftRight(int by2) {
        super.shiftRight(by2);
        long len = length();
        this.lo = Math.min(len, this.lo + by2);
        this.hi = Math.min(len - 1, this.hi + by2);
        return isEmpty() ? getUnderlying() : this;
    }

    @Override // jdk.nashorn.internal.runtime.arrays.ArrayFilter, jdk.nashorn.internal.runtime.arrays.ArrayData
    public ArrayData shrink(long newLength) {
        super.shrink(newLength);
        this.lo = Math.min(newLength, this.lo);
        this.hi = Math.min(newLength - 1, this.hi);
        return isEmpty() ? getUnderlying() : this;
    }

    @Override // jdk.nashorn.internal.runtime.arrays.ArrayFilter, jdk.nashorn.internal.runtime.arrays.ArrayData
    public ArrayData set(int index, Object value, boolean strict) {
        long longIndex = ArrayIndex.toLongIndex(index);
        if (longIndex < this.lo || longIndex > this.hi) {
            return super.set(index, value, strict);
        }
        if (longIndex > this.lo && longIndex < this.hi) {
            return getDeletedArrayFilter().set(index, value, strict);
        }
        if (longIndex == this.lo) {
            this.lo++;
        } else {
            if (!$assertionsDisabled && longIndex != this.hi) {
                throw new AssertionError();
            }
            this.hi--;
        }
        return isEmpty() ? getUnderlying().set(index, value, strict) : super.set(index, value, strict);
    }

    @Override // jdk.nashorn.internal.runtime.arrays.ArrayFilter, jdk.nashorn.internal.runtime.arrays.ArrayData
    public ArrayData set(int index, int value, boolean strict) {
        long longIndex = ArrayIndex.toLongIndex(index);
        if (longIndex < this.lo || longIndex > this.hi) {
            return super.set(index, value, strict);
        }
        if (longIndex > this.lo && longIndex < this.hi) {
            return getDeletedArrayFilter().set(index, value, strict);
        }
        if (longIndex == this.lo) {
            this.lo++;
        } else {
            if (!$assertionsDisabled && longIndex != this.hi) {
                throw new AssertionError();
            }
            this.hi--;
        }
        return isEmpty() ? getUnderlying().set(index, value, strict) : super.set(index, value, strict);
    }

    @Override // jdk.nashorn.internal.runtime.arrays.ArrayFilter, jdk.nashorn.internal.runtime.arrays.ArrayData
    public ArrayData set(int index, double value, boolean strict) {
        long longIndex = ArrayIndex.toLongIndex(index);
        if (longIndex < this.lo || longIndex > this.hi) {
            return super.set(index, value, strict);
        }
        if (longIndex > this.lo && longIndex < this.hi) {
            return getDeletedArrayFilter().set(index, value, strict);
        }
        if (longIndex == this.lo) {
            this.lo++;
        } else {
            if (!$assertionsDisabled && longIndex != this.hi) {
                throw new AssertionError();
            }
            this.hi--;
        }
        return isEmpty() ? getUnderlying().set(index, value, strict) : super.set(index, value, strict);
    }

    @Override // jdk.nashorn.internal.runtime.arrays.ArrayFilter, jdk.nashorn.internal.runtime.arrays.ArrayData
    public boolean has(int index) {
        return super.has(index) && !isDeleted(index);
    }

    private ArrayData getDeletedArrayFilter() {
        ArrayData deleteFilter = new DeletedArrayFilter(getUnderlying());
        deleteFilter.delete(this.lo, this.hi);
        return deleteFilter;
    }

    @Override // jdk.nashorn.internal.runtime.arrays.ArrayFilter, jdk.nashorn.internal.runtime.arrays.ArrayData
    public ArrayData delete(int index) {
        long longIndex = ArrayIndex.toLongIndex(index);
        this.underlying.setEmpty(index);
        if (longIndex + 1 == this.lo) {
            this.lo = longIndex;
        } else if (longIndex - 1 == this.hi) {
            this.hi = longIndex;
        } else if (longIndex < this.lo || this.hi < longIndex) {
            return getDeletedArrayFilter().delete(index);
        }
        return this;
    }

    @Override // jdk.nashorn.internal.runtime.arrays.ArrayFilter, jdk.nashorn.internal.runtime.arrays.ArrayData
    public ArrayData delete(long fromIndex, long toIndex) {
        if (fromIndex > this.hi + 1 || toIndex < this.lo - 1) {
            return getDeletedArrayFilter().delete(fromIndex, toIndex);
        }
        this.lo = Math.min(fromIndex, this.lo);
        this.hi = Math.max(toIndex, this.hi);
        this.underlying.setEmpty(this.lo, this.hi);
        return this;
    }

    @Override // jdk.nashorn.internal.runtime.arrays.ArrayFilter, jdk.nashorn.internal.runtime.arrays.ArrayData
    public Object pop() {
        int index = ((int) length()) - 1;
        if (super.has(index)) {
            boolean isDeleted = isDeleted(index);
            Object value = super.pop();
            this.lo = Math.min(index + 1, this.lo);
            this.hi = Math.min(index, this.hi);
            return isDeleted ? ScriptRuntime.UNDEFINED : value;
        }
        return super.pop();
    }

    @Override // jdk.nashorn.internal.runtime.arrays.ArrayData
    public ArrayData slice(long from, long to) {
        return new DeletedRangeArrayFilter(this.underlying.slice(from, to), Math.max(0L, this.lo - from), Math.max(0L, this.hi - from));
    }
}
