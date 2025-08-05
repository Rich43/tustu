package jdk.nashorn.internal.runtime.arrays;

import jdk.nashorn.internal.runtime.Context;
import jdk.nashorn.internal.runtime.ScriptRuntime;
import jdk.nashorn.internal.runtime.linker.Bootstrap;

/* loaded from: nashorn.jar:jdk/nashorn/internal/runtime/arrays/IteratorAction.class */
public abstract class IteratorAction<T> {
    protected final Object self;
    protected Object thisArg;
    protected final Object callbackfn;
    protected T result;
    protected long index;
    private final ArrayLikeIterator<Object> iter;

    protected abstract boolean forEach(Object obj, double d2) throws Throwable;

    public IteratorAction(Object self, Object callbackfn, Object thisArg, T initialResult) {
        this(self, callbackfn, thisArg, initialResult, ArrayLikeIterator.arrayLikeIterator(self));
    }

    public IteratorAction(Object self, Object callbackfn, Object thisArg, T initialResult, ArrayLikeIterator<Object> iter) {
        this.self = self;
        this.callbackfn = callbackfn;
        this.result = initialResult;
        this.iter = iter;
        this.thisArg = thisArg;
    }

    protected void applyLoopBegin(ArrayLikeIterator<Object> iterator) {
    }

    public final T apply() {
        boolean strict = Bootstrap.isStrictCallable(this.callbackfn);
        this.thisArg = (this.thisArg != ScriptRuntime.UNDEFINED || strict) ? this.thisArg : Context.getGlobal();
        applyLoopBegin(this.iter);
        boolean reverse = this.iter.isReverse();
        while (this.iter.hasNext()) {
            Object val = this.iter.next();
            this.index = this.iter.nextIndex() + (reverse ? 1 : -1);
            try {
                if (!forEach(val, this.index)) {
                    return this.result;
                }
            } catch (Error | RuntimeException e2) {
                throw e2;
            } catch (Throwable t2) {
                throw new RuntimeException(t2);
            }
        }
        return this.result;
    }
}
