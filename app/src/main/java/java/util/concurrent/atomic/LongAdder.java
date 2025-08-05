package java.util.concurrent.atomic;

import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.concurrent.atomic.Striped64;

/* loaded from: rt.jar:java/util/concurrent/atomic/LongAdder.class */
public class LongAdder extends Striped64 implements Serializable {
    private static final long serialVersionUID = 7249069246863182397L;

    public void add(long j2) {
        int length;
        Striped64.Cell cell;
        Striped64.Cell[] cellArr = this.cells;
        if (cellArr == null) {
            long j3 = this.base;
            if (casBase(j3, j3 + j2)) {
                return;
            }
        }
        boolean z2 = true;
        if (cellArr != null && (length = cellArr.length - 1) >= 0 && (cell = cellArr[getProbe() & length]) != null) {
            long j4 = cell.value;
            boolean zCas = cell.cas(j4, j4 + j2);
            z2 = zCas;
            if (zCas) {
                return;
            }
        }
        longAccumulate(j2, null, z2);
    }

    public void increment() {
        add(1L);
    }

    public void decrement() {
        add(-1L);
    }

    public long sum() {
        Striped64.Cell[] cellArr = this.cells;
        long j2 = this.base;
        if (cellArr != null) {
            for (Striped64.Cell cell : cellArr) {
                if (cell != null) {
                    j2 += cell.value;
                }
            }
        }
        return j2;
    }

    public void reset() {
        Striped64.Cell[] cellArr = this.cells;
        this.base = 0L;
        if (cellArr != null) {
            for (Striped64.Cell cell : cellArr) {
                if (cell != null) {
                    cell.value = 0L;
                }
            }
        }
    }

    public long sumThenReset() {
        Striped64.Cell[] cellArr = this.cells;
        long j2 = this.base;
        this.base = 0L;
        if (cellArr != null) {
            for (Striped64.Cell cell : cellArr) {
                if (cell != null) {
                    j2 += cell.value;
                    cell.value = 0L;
                }
            }
        }
        return j2;
    }

    public String toString() {
        return Long.toString(sum());
    }

    @Override // java.lang.Number
    public long longValue() {
        return sum();
    }

    @Override // java.lang.Number
    public int intValue() {
        return (int) sum();
    }

    @Override // java.lang.Number
    public float floatValue() {
        return sum();
    }

    @Override // java.lang.Number
    public double doubleValue() {
        return sum();
    }

    /* loaded from: rt.jar:java/util/concurrent/atomic/LongAdder$SerializationProxy.class */
    private static class SerializationProxy implements Serializable {
        private static final long serialVersionUID = 7249069246863182397L;
        private final long value;

        SerializationProxy(LongAdder longAdder) {
            this.value = longAdder.sum();
        }

        private Object readResolve() {
            LongAdder longAdder = new LongAdder();
            longAdder.base = this.value;
            return longAdder;
        }
    }

    private Object writeReplace() {
        return new SerializationProxy(this);
    }

    private void readObject(ObjectInputStream objectInputStream) throws InvalidObjectException {
        throw new InvalidObjectException("Proxy required");
    }
}
