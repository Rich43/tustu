package java.util.concurrent.atomic;

import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.concurrent.atomic.Striped64;
import java.util.function.LongBinaryOperator;

/* loaded from: rt.jar:java/util/concurrent/atomic/LongAccumulator.class */
public class LongAccumulator extends Striped64 implements Serializable {
    private static final long serialVersionUID = 7249069246863182397L;
    private final LongBinaryOperator function;
    private final long identity;

    public LongAccumulator(LongBinaryOperator longBinaryOperator, long j2) {
        this.function = longBinaryOperator;
        this.identity = j2;
        this.base = j2;
    }

    public void accumulate(long j2) {
        int length;
        Striped64.Cell cell;
        Striped64.Cell[] cellArr = this.cells;
        if (cellArr == null) {
            LongBinaryOperator longBinaryOperator = this.function;
            long j3 = this.base;
            long jApplyAsLong = longBinaryOperator.applyAsLong(j3, j2);
            if (j2 == j3 || casBase(j3, jApplyAsLong)) {
                return;
            }
        }
        boolean z2 = true;
        if (cellArr != null && (length = cellArr.length - 1) >= 0 && (cell = cellArr[getProbe() & length]) != null) {
            LongBinaryOperator longBinaryOperator2 = this.function;
            long j4 = cell.value;
            boolean z3 = j2 == j4 || cell.cas(j4, longBinaryOperator2.applyAsLong(j4, j2));
            z2 = z3;
            if (z3) {
                return;
            }
        }
        longAccumulate(j2, this.function, z2);
    }

    public long get() {
        Striped64.Cell[] cellArr = this.cells;
        long jApplyAsLong = this.base;
        if (cellArr != null) {
            for (Striped64.Cell cell : cellArr) {
                if (cell != null) {
                    jApplyAsLong = this.function.applyAsLong(jApplyAsLong, cell.value);
                }
            }
        }
        return jApplyAsLong;
    }

    public void reset() {
        Striped64.Cell[] cellArr = this.cells;
        this.base = this.identity;
        if (cellArr != null) {
            for (Striped64.Cell cell : cellArr) {
                if (cell != null) {
                    cell.value = this.identity;
                }
            }
        }
    }

    public long getThenReset() {
        Striped64.Cell[] cellArr = this.cells;
        long jApplyAsLong = this.base;
        this.base = this.identity;
        if (cellArr != null) {
            for (Striped64.Cell cell : cellArr) {
                if (cell != null) {
                    long j2 = cell.value;
                    cell.value = this.identity;
                    jApplyAsLong = this.function.applyAsLong(jApplyAsLong, j2);
                }
            }
        }
        return jApplyAsLong;
    }

    public String toString() {
        return Long.toString(get());
    }

    @Override // java.lang.Number
    public long longValue() {
        return get();
    }

    @Override // java.lang.Number
    public int intValue() {
        return (int) get();
    }

    @Override // java.lang.Number
    public float floatValue() {
        return get();
    }

    @Override // java.lang.Number
    public double doubleValue() {
        return get();
    }

    /* loaded from: rt.jar:java/util/concurrent/atomic/LongAccumulator$SerializationProxy.class */
    private static class SerializationProxy implements Serializable {
        private static final long serialVersionUID = 7249069246863182397L;
        private final long value;
        private final LongBinaryOperator function;
        private final long identity;

        SerializationProxy(LongAccumulator longAccumulator) {
            this.function = longAccumulator.function;
            this.identity = longAccumulator.identity;
            this.value = longAccumulator.get();
        }

        private Object readResolve() {
            LongAccumulator longAccumulator = new LongAccumulator(this.function, this.identity);
            longAccumulator.base = this.value;
            return longAccumulator;
        }
    }

    private Object writeReplace() {
        return new SerializationProxy(this);
    }

    private void readObject(ObjectInputStream objectInputStream) throws InvalidObjectException {
        throw new InvalidObjectException("Proxy required");
    }
}
