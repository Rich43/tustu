package org.apache.commons.math3.util;

import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.util.IntegerSequence;

@Deprecated
/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/util/Incrementor.class */
public class Incrementor {
    private int maximalCount;
    private int count;
    private final MaxCountExceededCallback maxCountCallback;

    /* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/util/Incrementor$MaxCountExceededCallback.class */
    public interface MaxCountExceededCallback {
        void trigger(int i2) throws MaxCountExceededException;
    }

    public Incrementor() {
        this(0);
    }

    public Incrementor(int max) {
        this(max, new MaxCountExceededCallback() { // from class: org.apache.commons.math3.util.Incrementor.1
            @Override // org.apache.commons.math3.util.Incrementor.MaxCountExceededCallback
            public void trigger(int max2) throws MaxCountExceededException {
                throw new MaxCountExceededException(Integer.valueOf(max2));
            }
        });
    }

    public Incrementor(int max, MaxCountExceededCallback cb) throws NullArgumentException {
        this.count = 0;
        if (cb == null) {
            throw new NullArgumentException();
        }
        this.maximalCount = max;
        this.maxCountCallback = cb;
    }

    public void setMaximalCount(int max) {
        this.maximalCount = max;
    }

    public int getMaximalCount() {
        return this.maximalCount;
    }

    public int getCount() {
        return this.count;
    }

    public boolean canIncrement() {
        return this.count < this.maximalCount;
    }

    public void incrementCount(int value) throws MaxCountExceededException {
        for (int i2 = 0; i2 < value; i2++) {
            incrementCount();
        }
    }

    public void incrementCount() throws MaxCountExceededException {
        int i2 = this.count + 1;
        this.count = i2;
        if (i2 > this.maximalCount) {
            this.maxCountCallback.trigger(this.maximalCount);
        }
    }

    public void resetCount() {
        this.count = 0;
    }

    public static Incrementor wrap(final IntegerSequence.Incrementor incrementor) {
        return new Incrementor() { // from class: org.apache.commons.math3.util.Incrementor.2
            private IntegerSequence.Incrementor delegate;

            {
                this.delegate = incrementor;
                super.setMaximalCount(this.delegate.getMaximalCount());
                super.incrementCount(this.delegate.getCount());
            }

            @Override // org.apache.commons.math3.util.Incrementor
            public void setMaximalCount(int max) {
                super.setMaximalCount(max);
                this.delegate = this.delegate.withMaximalCount(max);
            }

            @Override // org.apache.commons.math3.util.Incrementor
            public void resetCount() {
                super.resetCount();
                this.delegate = this.delegate.withStart(0);
            }

            @Override // org.apache.commons.math3.util.Incrementor
            public void incrementCount() throws MaxCountExceededException {
                super.incrementCount();
                this.delegate.increment();
            }
        };
    }
}
