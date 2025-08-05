package org.apache.commons.math3.util;

import java.util.Iterator;
import org.apache.commons.math3.exception.MathUnsupportedOperationException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.ZeroException;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/util/IntegerSequence.class */
public class IntegerSequence {
    private IntegerSequence() {
    }

    public static Range range(int start, int end) {
        return range(start, end, 1);
    }

    public static Range range(int start, int max, int step) {
        return new Range(start, max, step);
    }

    /* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/util/IntegerSequence$Range.class */
    public static class Range implements Iterable<Integer> {
        private final int size;
        private final int start;
        private final int max;
        private final int step;

        public Range(int start, int max, int step) {
            this.start = start;
            this.max = max;
            this.step = step;
            int s2 = ((max - start) / step) + 1;
            this.size = s2 < 0 ? 0 : s2;
        }

        public int size() {
            return this.size;
        }

        @Override // java.lang.Iterable, java.util.List
        public Iterator<Integer> iterator() {
            return Incrementor.create().withStart(this.start).withMaximalCount(this.max + (this.step > 0 ? 1 : -1)).withIncrement(this.step);
        }
    }

    /* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/util/IntegerSequence$Incrementor.class */
    public static class Incrementor implements Iterator<Integer> {
        private static final MaxCountExceededCallback CALLBACK = new MaxCountExceededCallback() { // from class: org.apache.commons.math3.util.IntegerSequence.Incrementor.1
            @Override // org.apache.commons.math3.util.IntegerSequence.Incrementor.MaxCountExceededCallback
            public void trigger(int max) throws MaxCountExceededException {
                throw new MaxCountExceededException(Integer.valueOf(max));
            }
        };
        private final int init;
        private final int maximalCount;
        private final int increment;
        private final MaxCountExceededCallback maxCountCallback;
        private int count;

        /* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/util/IntegerSequence$Incrementor$MaxCountExceededCallback.class */
        public interface MaxCountExceededCallback {
            void trigger(int i2) throws MaxCountExceededException;
        }

        private Incrementor(int start, int max, int step, MaxCountExceededCallback cb) throws NullArgumentException {
            this.count = 0;
            if (cb == null) {
                throw new NullArgumentException();
            }
            this.init = start;
            this.maximalCount = max;
            this.increment = step;
            this.maxCountCallback = cb;
            this.count = start;
        }

        public static Incrementor create() {
            return new Incrementor(0, 0, 1, CALLBACK);
        }

        public Incrementor withStart(int start) {
            return new Incrementor(start, this.maximalCount, this.increment, this.maxCountCallback);
        }

        public Incrementor withMaximalCount(int max) {
            return new Incrementor(this.init, max, this.increment, this.maxCountCallback);
        }

        public Incrementor withIncrement(int step) {
            if (step == 0) {
                throw new ZeroException();
            }
            return new Incrementor(this.init, this.maximalCount, step, this.maxCountCallback);
        }

        public Incrementor withCallback(MaxCountExceededCallback cb) {
            return new Incrementor(this.init, this.maximalCount, this.increment, cb);
        }

        public int getMaximalCount() {
            return this.maximalCount;
        }

        public int getCount() {
            return this.count;
        }

        public boolean canIncrement() {
            return canIncrement(1);
        }

        public boolean canIncrement(int nTimes) {
            int finalCount = this.count + (nTimes * this.increment);
            return this.increment < 0 ? finalCount > this.maximalCount : finalCount < this.maximalCount;
        }

        public void increment(int nTimes) throws MaxCountExceededException {
            if (nTimes <= 0) {
                throw new NotStrictlyPositiveException(Integer.valueOf(nTimes));
            }
            if (!canIncrement(0)) {
                this.maxCountCallback.trigger(this.maximalCount);
            }
            this.count += nTimes * this.increment;
        }

        public void increment() throws MaxCountExceededException {
            increment(1);
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            return canIncrement(0);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.util.Iterator
        public Integer next() throws MaxCountExceededException {
            int value = this.count;
            increment();
            return Integer.valueOf(value);
        }

        @Override // java.util.Iterator
        public void remove() {
            throw new MathUnsupportedOperationException();
        }
    }
}
