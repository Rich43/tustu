package java.util.concurrent.atomic;

import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.concurrent.atomic.Striped64;

/* loaded from: rt.jar:java/util/concurrent/atomic/DoubleAdder.class */
public class DoubleAdder extends Striped64 implements Serializable {
    private static final long serialVersionUID = 7249069246863182397L;

    public void add(double d2) {
        int length;
        Striped64.Cell cell;
        Striped64.Cell[] cellArr = this.cells;
        if (cellArr == null) {
            long j2 = this.base;
            if (casBase(j2, Double.doubleToRawLongBits(Double.longBitsToDouble(j2) + d2))) {
                return;
            }
        }
        boolean z2 = true;
        if (cellArr != null && (length = cellArr.length - 1) >= 0 && (cell = cellArr[getProbe() & length]) != null) {
            long j3 = cell.value;
            boolean zCas = cell.cas(j3, Double.doubleToRawLongBits(Double.longBitsToDouble(j3) + d2));
            z2 = zCas;
            if (zCas) {
                return;
            }
        }
        doubleAccumulate(d2, null, z2);
    }

    public double sum() {
        Striped64.Cell[] cellArr = this.cells;
        double dLongBitsToDouble = Double.longBitsToDouble(this.base);
        if (cellArr != null) {
            for (Striped64.Cell cell : cellArr) {
                if (cell != null) {
                    dLongBitsToDouble += Double.longBitsToDouble(cell.value);
                }
            }
        }
        return dLongBitsToDouble;
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

    public double sumThenReset() {
        Striped64.Cell[] cellArr = this.cells;
        double dLongBitsToDouble = Double.longBitsToDouble(this.base);
        this.base = 0L;
        if (cellArr != null) {
            for (Striped64.Cell cell : cellArr) {
                if (cell != null) {
                    long j2 = cell.value;
                    cell.value = 0L;
                    dLongBitsToDouble += Double.longBitsToDouble(j2);
                }
            }
        }
        return dLongBitsToDouble;
    }

    public String toString() {
        return Double.toString(sum());
    }

    @Override // java.lang.Number
    public double doubleValue() {
        return sum();
    }

    @Override // java.lang.Number
    public long longValue() {
        return (long) sum();
    }

    @Override // java.lang.Number
    public int intValue() {
        return (int) sum();
    }

    @Override // java.lang.Number
    public float floatValue() {
        return (float) sum();
    }

    /* loaded from: rt.jar:java/util/concurrent/atomic/DoubleAdder$SerializationProxy.class */
    private static class SerializationProxy implements Serializable {
        private static final long serialVersionUID = 7249069246863182397L;
        private final double value;

        SerializationProxy(DoubleAdder doubleAdder) {
            this.value = doubleAdder.sum();
        }

        private Object readResolve() {
            DoubleAdder doubleAdder = new DoubleAdder();
            doubleAdder.base = Double.doubleToRawLongBits(this.value);
            return doubleAdder;
        }
    }

    private Object writeReplace() {
        return new SerializationProxy(this);
    }

    private void readObject(ObjectInputStream objectInputStream) throws InvalidObjectException {
        throw new InvalidObjectException("Proxy required");
    }
}
