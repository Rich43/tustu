package java.util.concurrent.atomic;

import com.efiAnalytics.plugin.ecu.ControllerParameter;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.function.BinaryOperator;
import java.util.function.UnaryOperator;
import sun.misc.Unsafe;

/* loaded from: rt.jar:java/util/concurrent/atomic/AtomicReferenceArray.class */
public class AtomicReferenceArray<E> implements Serializable {
    private static final long serialVersionUID = -6209656149925076980L;
    private static final Unsafe unsafe;
    private static final int base;
    private static final int shift;
    private static final long arrayFieldOffset;
    private final Object[] array;

    static {
        try {
            unsafe = Unsafe.getUnsafe();
            arrayFieldOffset = unsafe.objectFieldOffset(AtomicReferenceArray.class.getDeclaredField(ControllerParameter.PARAM_CLASS_ARRAY));
            base = unsafe.arrayBaseOffset(Object[].class);
            int iArrayIndexScale = unsafe.arrayIndexScale(Object[].class);
            if ((iArrayIndexScale & (iArrayIndexScale - 1)) != 0) {
                throw new Error("data type scale not a power of two");
            }
            shift = 31 - Integer.numberOfLeadingZeros(iArrayIndexScale);
        } catch (Exception e2) {
            throw new Error(e2);
        }
    }

    private long checkedByteOffset(int i2) {
        if (i2 < 0 || i2 >= this.array.length) {
            throw new IndexOutOfBoundsException("index " + i2);
        }
        return byteOffset(i2);
    }

    private static long byteOffset(int i2) {
        return (i2 << shift) + base;
    }

    public AtomicReferenceArray(int i2) {
        this.array = new Object[i2];
    }

    public AtomicReferenceArray(E[] eArr) {
        this.array = Arrays.copyOf(eArr, eArr.length, Object[].class);
    }

    public final int length() {
        return this.array.length;
    }

    public final E get(int i2) {
        return getRaw(checkedByteOffset(i2));
    }

    private E getRaw(long j2) {
        return (E) unsafe.getObjectVolatile(this.array, j2);
    }

    public final void set(int i2, E e2) {
        unsafe.putObjectVolatile(this.array, checkedByteOffset(i2), e2);
    }

    public final void lazySet(int i2, E e2) {
        unsafe.putOrderedObject(this.array, checkedByteOffset(i2), e2);
    }

    public final E getAndSet(int i2, E e2) {
        return (E) unsafe.getAndSetObject(this.array, checkedByteOffset(i2), e2);
    }

    public final boolean compareAndSet(int i2, E e2, E e3) {
        return compareAndSetRaw(checkedByteOffset(i2), e2, e3);
    }

    private boolean compareAndSetRaw(long j2, E e2, E e3) {
        return unsafe.compareAndSwapObject(this.array, j2, e2, e3);
    }

    public final boolean weakCompareAndSet(int i2, E e2, E e3) {
        return compareAndSet(i2, e2, e3);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public final E getAndUpdate(int i2, UnaryOperator<E> unaryOperator) {
        E raw;
        long jCheckedByteOffset = checkedByteOffset(i2);
        do {
            raw = getRaw(jCheckedByteOffset);
        } while (!compareAndSetRaw(jCheckedByteOffset, raw, unaryOperator.apply(raw)));
        return raw;
    }

    public final E updateAndGet(int i2, UnaryOperator<E> unaryOperator) {
        E raw;
        E e2;
        long jCheckedByteOffset = checkedByteOffset(i2);
        do {
            raw = getRaw(jCheckedByteOffset);
            e2 = (E) unaryOperator.apply(raw);
        } while (!compareAndSetRaw(jCheckedByteOffset, raw, e2));
        return e2;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public final E getAndAccumulate(int i2, E e2, BinaryOperator<E> binaryOperator) {
        E raw;
        long jCheckedByteOffset = checkedByteOffset(i2);
        do {
            raw = getRaw(jCheckedByteOffset);
        } while (!compareAndSetRaw(jCheckedByteOffset, raw, binaryOperator.apply(raw, e2)));
        return raw;
    }

    public final E accumulateAndGet(int i2, E e2, BinaryOperator<E> binaryOperator) {
        E raw;
        E e3;
        long jCheckedByteOffset = checkedByteOffset(i2);
        do {
            raw = getRaw(jCheckedByteOffset);
            e3 = (E) binaryOperator.apply(raw, e2);
        } while (!compareAndSetRaw(jCheckedByteOffset, raw, e3));
        return e3;
    }

    public String toString() {
        int length = this.array.length - 1;
        if (length == -1) {
            return "[]";
        }
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        int i2 = 0;
        while (true) {
            sb.append((Object) getRaw(byteOffset(i2)));
            if (i2 == length) {
                return sb.append(']').toString();
            }
            sb.append(',').append(' ');
            i2++;
        }
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        Object objCopyOf = objectInputStream.readFields().get(ControllerParameter.PARAM_CLASS_ARRAY, (Object) null);
        if (objCopyOf == null || !objCopyOf.getClass().isArray()) {
            throw new InvalidObjectException("Not array type");
        }
        if (objCopyOf.getClass() != Object[].class) {
            objCopyOf = Arrays.copyOf((Object[]) objCopyOf, Array.getLength(objCopyOf), Object[].class);
        }
        unsafe.putObjectVolatile(this, arrayFieldOffset, objCopyOf);
    }
}
