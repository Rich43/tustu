package java.util.concurrent.atomic;

import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.concurrent.atomic.Striped64;
import java.util.function.DoubleBinaryOperator;

/* loaded from: rt.jar:java/util/concurrent/atomic/DoubleAccumulator.class */
public class DoubleAccumulator extends Striped64 implements Serializable {
    private static final long serialVersionUID = 7249069246863182397L;
    private final DoubleBinaryOperator function;
    private final long identity;

    public DoubleAccumulator(DoubleBinaryOperator doubleBinaryOperator, double d2) {
        this.function = doubleBinaryOperator;
        long jDoubleToRawLongBits = Double.doubleToRawLongBits(d2);
        this.identity = jDoubleToRawLongBits;
        this.base = jDoubleToRawLongBits;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public void accumulate(double d2) {
        int length;
        Striped64.Cell cell;
        Striped64.Cell[] cellArr = this.cells;
        if (cellArr == null) {
            DoubleBinaryOperator doubleBinaryOperator = this.function;
            long j2 = this.base;
            long jDoubleToRawLongBits = Double.doubleToRawLongBits(doubleBinaryOperator.applyAsDouble(Double.longBitsToDouble(j2), d2));
            if (d2 == j2 || casBase(j2, jDoubleToRawLongBits)) {
                return;
            }
        }
        boolean z2 = true;
        if (cellArr != null && (length = cellArr.length - 1) >= 0 && (cell = cellArr[getProbe() & length]) != null) {
            DoubleBinaryOperator doubleBinaryOperator2 = this.function;
            long j3 = cell.value;
            boolean z3 = d2 == j3 || cell.cas(j3, Double.doubleToRawLongBits(doubleBinaryOperator2.applyAsDouble(Double.longBitsToDouble(j3), d2)));
            z2 = z3;
            if (z3) {
                return;
            }
        }
        doubleAccumulate(d2, this.function, z2);
    }

    public double get() {
        Striped64.Cell[] cellArr = this.cells;
        double dLongBitsToDouble = Double.longBitsToDouble(this.base);
        if (cellArr != null) {
            for (Striped64.Cell cell : cellArr) {
                if (cell != null) {
                    dLongBitsToDouble = this.function.applyAsDouble(dLongBitsToDouble, Double.longBitsToDouble(cell.value));
                }
            }
        }
        return dLongBitsToDouble;
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

    public double getThenReset() {
        Striped64.Cell[] cellArr = this.cells;
        double dLongBitsToDouble = Double.longBitsToDouble(this.base);
        this.base = this.identity;
        if (cellArr != null) {
            for (Striped64.Cell cell : cellArr) {
                if (cell != null) {
                    double dLongBitsToDouble2 = Double.longBitsToDouble(cell.value);
                    cell.value = this.identity;
                    dLongBitsToDouble = this.function.applyAsDouble(dLongBitsToDouble, dLongBitsToDouble2);
                }
            }
        }
        return dLongBitsToDouble;
    }

    public String toString() {
        return Double.toString(get());
    }

    @Override // java.lang.Number
    public double doubleValue() {
        return get();
    }

    @Override // java.lang.Number
    public long longValue() {
        return (long) get();
    }

    @Override // java.lang.Number
    public int intValue() {
        return (int) get();
    }

    @Override // java.lang.Number
    public float floatValue() {
        return (float) get();
    }

    /* loaded from: rt.jar:java/util/concurrent/atomic/DoubleAccumulator$SerializationProxy.class */
    private static class SerializationProxy implements Serializable {
        private static final long serialVersionUID = 7249069246863182397L;
        private final double value;
        private final DoubleBinaryOperator function;
        private final long identity;

        SerializationProxy(DoubleAccumulator doubleAccumulator) {
            this.function = doubleAccumulator.function;
            this.identity = doubleAccumulator.identity;
            this.value = doubleAccumulator.get();
        }

        private Object readResolve() {
            DoubleAccumulator doubleAccumulator = new DoubleAccumulator(this.function, Double.longBitsToDouble(this.identity));
            doubleAccumulator.base = Double.doubleToRawLongBits(this.value);
            return doubleAccumulator;
        }
    }

    private Object writeReplace() {
        return new SerializationProxy(this);
    }

    private void readObject(ObjectInputStream objectInputStream) throws InvalidObjectException {
        throw new InvalidObjectException("Proxy required");
    }
}
