package jdk.nashorn.internal.runtime.arrays;

import java.lang.reflect.Array;
import jdk.nashorn.internal.runtime.BitVector;
import jdk.nashorn.internal.runtime.ScriptRuntime;
import jdk.nashorn.internal.runtime.UnwarrantedOptimismException;

/* loaded from: nashorn.jar:jdk/nashorn/internal/runtime/arrays/UndefinedArrayFilter.class */
final class UndefinedArrayFilter extends ArrayFilter {
    private final BitVector undefined;

    UndefinedArrayFilter(ArrayData underlying) {
        super(underlying);
        this.undefined = new BitVector(underlying.length());
    }

    @Override // jdk.nashorn.internal.runtime.arrays.ArrayData
    public ArrayData copy() {
        UndefinedArrayFilter copy = new UndefinedArrayFilter(this.underlying.copy());
        copy.getUndefined().copy(this.undefined);
        return copy;
    }

    @Override // jdk.nashorn.internal.runtime.arrays.ArrayFilter, jdk.nashorn.internal.runtime.arrays.ArrayData
    public Object[] asObjectArray() {
        Object[] value = super.asObjectArray();
        for (int i2 = 0; i2 < value.length; i2++) {
            if (this.undefined.isSet(i2)) {
                value[i2] = ScriptRuntime.UNDEFINED;
            }
        }
        return value;
    }

    @Override // jdk.nashorn.internal.runtime.arrays.ArrayFilter, jdk.nashorn.internal.runtime.arrays.ArrayData
    public Object asArrayOfType(Class<?> componentType) throws ArrayIndexOutOfBoundsException, IllegalArgumentException {
        Object value = super.asArrayOfType(componentType);
        Object undefValue = convertUndefinedValue(componentType);
        int l2 = Array.getLength(value);
        for (int i2 = 0; i2 < l2; i2++) {
            if (this.undefined.isSet(i2)) {
                Array.set(value, i2, undefValue);
            }
        }
        return value;
    }

    @Override // jdk.nashorn.internal.runtime.arrays.ArrayFilter, jdk.nashorn.internal.runtime.arrays.ArrayData
    public ArrayData shiftLeft(int by2) {
        super.shiftLeft(by2);
        this.undefined.shiftLeft(by2, length());
        return this;
    }

    @Override // jdk.nashorn.internal.runtime.arrays.ArrayFilter, jdk.nashorn.internal.runtime.arrays.ArrayData
    public ArrayData shiftRight(int by2) {
        super.shiftRight(by2);
        this.undefined.shiftRight(by2, length());
        return this;
    }

    @Override // jdk.nashorn.internal.runtime.arrays.ArrayFilter, jdk.nashorn.internal.runtime.arrays.ArrayData
    public ArrayData ensure(long safeIndex) {
        if (safeIndex >= 131072 && safeIndex >= length()) {
            return new SparseArrayData(this, safeIndex + 1);
        }
        super.ensure(safeIndex);
        this.undefined.resize(length());
        return this;
    }

    @Override // jdk.nashorn.internal.runtime.arrays.ArrayFilter, jdk.nashorn.internal.runtime.arrays.ArrayData
    public ArrayData shrink(long newLength) {
        super.shrink(newLength);
        this.undefined.resize(length());
        return this;
    }

    @Override // jdk.nashorn.internal.runtime.arrays.ArrayFilter, jdk.nashorn.internal.runtime.arrays.ArrayData
    public ArrayData set(int index, Object value, boolean strict) {
        this.undefined.clear(index);
        if (value == ScriptRuntime.UNDEFINED) {
            this.undefined.set(index);
            return this;
        }
        return super.set(index, value, strict);
    }

    @Override // jdk.nashorn.internal.runtime.arrays.ArrayFilter, jdk.nashorn.internal.runtime.arrays.ArrayData
    public ArrayData set(int index, int value, boolean strict) {
        this.undefined.clear(index);
        return super.set(index, value, strict);
    }

    @Override // jdk.nashorn.internal.runtime.arrays.ArrayFilter, jdk.nashorn.internal.runtime.arrays.ArrayData
    public ArrayData set(int index, double value, boolean strict) {
        this.undefined.clear(index);
        return super.set(index, value, strict);
    }

    @Override // jdk.nashorn.internal.runtime.arrays.ArrayFilter, jdk.nashorn.internal.runtime.arrays.ArrayData
    public int getInt(int index) {
        if (this.undefined.isSet(index)) {
            return 0;
        }
        return super.getInt(index);
    }

    @Override // jdk.nashorn.internal.runtime.arrays.ArrayFilter, jdk.nashorn.internal.runtime.arrays.ArrayData
    public int getIntOptimistic(int index, int programPoint) {
        if (this.undefined.isSet(index)) {
            throw new UnwarrantedOptimismException(ScriptRuntime.UNDEFINED, programPoint);
        }
        return super.getIntOptimistic(index, programPoint);
    }

    @Override // jdk.nashorn.internal.runtime.arrays.ArrayFilter, jdk.nashorn.internal.runtime.arrays.ArrayData
    public double getDouble(int index) {
        if (this.undefined.isSet(index)) {
            return Double.NaN;
        }
        return super.getDouble(index);
    }

    @Override // jdk.nashorn.internal.runtime.arrays.ArrayFilter, jdk.nashorn.internal.runtime.arrays.ArrayData
    public double getDoubleOptimistic(int index, int programPoint) {
        if (this.undefined.isSet(index)) {
            throw new UnwarrantedOptimismException(ScriptRuntime.UNDEFINED, programPoint);
        }
        return super.getDoubleOptimistic(index, programPoint);
    }

    @Override // jdk.nashorn.internal.runtime.arrays.ArrayFilter, jdk.nashorn.internal.runtime.arrays.ArrayData
    public Object getObject(int index) {
        if (this.undefined.isSet(index)) {
            return ScriptRuntime.UNDEFINED;
        }
        return super.getObject(index);
    }

    @Override // jdk.nashorn.internal.runtime.arrays.ArrayFilter, jdk.nashorn.internal.runtime.arrays.ArrayData
    public ArrayData delete(int index) {
        this.undefined.clear(index);
        return super.delete(index);
    }

    @Override // jdk.nashorn.internal.runtime.arrays.ArrayFilter, jdk.nashorn.internal.runtime.arrays.ArrayData
    public Object pop() {
        long index = length() - 1;
        if (super.has((int) index)) {
            boolean isUndefined = this.undefined.isSet(index);
            Object value = super.pop();
            return isUndefined ? ScriptRuntime.UNDEFINED : value;
        }
        return super.pop();
    }

    @Override // jdk.nashorn.internal.runtime.arrays.ArrayData
    public ArrayData slice(long from, long to) {
        ArrayData newArray = this.underlying.slice(from, to);
        UndefinedArrayFilter newFilter = new UndefinedArrayFilter(newArray);
        newFilter.getUndefined().copy(this.undefined);
        newFilter.getUndefined().shiftLeft(from, newFilter.length());
        return newFilter;
    }

    private BitVector getUndefined() {
        return this.undefined;
    }
}
