package jdk.nashorn.internal.runtime.arrays;

import jdk.nashorn.internal.objects.Global;
import jdk.nashorn.internal.runtime.ECMAErrors;
import jdk.nashorn.internal.runtime.ScriptRuntime;

/* loaded from: nashorn.jar:jdk/nashorn/internal/runtime/arrays/NonExtensibleArrayFilter.class */
final class NonExtensibleArrayFilter extends ArrayFilter {
    NonExtensibleArrayFilter(ArrayData underlying) {
        super(underlying);
    }

    @Override // jdk.nashorn.internal.runtime.arrays.ArrayData
    public ArrayData copy() {
        return new NonExtensibleArrayFilter(this.underlying.copy());
    }

    @Override // jdk.nashorn.internal.runtime.arrays.ArrayData
    public ArrayData slice(long from, long to) {
        return new NonExtensibleArrayFilter(this.underlying.slice(from, to));
    }

    private ArrayData extensionCheck(boolean strict, int index) {
        if (strict) {
            throw ECMAErrors.typeError(Global.instance(), "object.non.extensible", String.valueOf(index), ScriptRuntime.safeToString(this));
        }
        return this;
    }

    @Override // jdk.nashorn.internal.runtime.arrays.ArrayFilter, jdk.nashorn.internal.runtime.arrays.ArrayData
    public ArrayData set(int index, Object value, boolean strict) {
        if (has(index)) {
            return this.underlying.set(index, value, strict);
        }
        return extensionCheck(strict, index);
    }

    @Override // jdk.nashorn.internal.runtime.arrays.ArrayFilter, jdk.nashorn.internal.runtime.arrays.ArrayData
    public ArrayData set(int index, int value, boolean strict) {
        if (has(index)) {
            return this.underlying.set(index, value, strict);
        }
        return extensionCheck(strict, index);
    }

    @Override // jdk.nashorn.internal.runtime.arrays.ArrayFilter, jdk.nashorn.internal.runtime.arrays.ArrayData
    public ArrayData set(int index, double value, boolean strict) {
        if (has(index)) {
            return this.underlying.set(index, value, strict);
        }
        return extensionCheck(strict, index);
    }
}
