package jdk.nashorn.internal.runtime.arrays;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Array;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import jdk.internal.dynalink.CallSiteDescriptor;
import jdk.internal.dynalink.linker.GuardedInvocation;
import jdk.internal.dynalink.linker.LinkRequest;
import jdk.nashorn.internal.codegen.CompilerConstants;
import jdk.nashorn.internal.codegen.types.Type;
import jdk.nashorn.internal.objects.Global;
import jdk.nashorn.internal.runtime.JSType;
import jdk.nashorn.internal.runtime.PropertyDescriptor;
import jdk.nashorn.internal.runtime.ScriptRuntime;
import jdk.nashorn.internal.runtime.UnwarrantedOptimismException;

/* loaded from: nashorn.jar:jdk/nashorn/internal/runtime/arrays/ArrayData.class */
public abstract class ArrayData {
    protected static final int CHUNK_SIZE = 32;
    public static final ArrayData EMPTY_ARRAY;
    private long length;
    protected static final CompilerConstants.Call THROW_UNWARRANTED;
    static final /* synthetic */ boolean $assertionsDisabled;

    public abstract ArrayData copy();

    public abstract Object[] asObjectArray();

    /*  JADX ERROR: Failed to decode insn: 0x0007: MOVE_MULTI
        java.lang.ArrayIndexOutOfBoundsException: arraycopy: source index -1 out of bounds for object array[6]
        	at java.base/java.lang.System.arraycopy(Native Method)
        	at jadx.plugins.input.java.data.code.StackState.insert(StackState.java:52)
        	at jadx.plugins.input.java.data.code.CodeDecodeState.insert(CodeDecodeState.java:137)
        	at jadx.plugins.input.java.data.code.JavaInsnsRegister.dup2x1(JavaInsnsRegister.java:313)
        	at jadx.plugins.input.java.data.code.JavaInsnData.decode(JavaInsnData.java:46)
        	at jadx.core.dex.instructions.InsnDecoder.lambda$process$0(InsnDecoder.java:50)
        	at jadx.plugins.input.java.data.code.JavaCodeReader.visitInstructions(JavaCodeReader.java:85)
        	at jadx.core.dex.instructions.InsnDecoder.process(InsnDecoder.java:46)
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:158)
        	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:458)
        	at jadx.core.ProcessClass.process(ProcessClass.java:69)
        	at jadx.core.ProcessClass.generateCode(ProcessClass.java:109)
        	at jadx.core.dex.nodes.ClassNode.generateClassCode(ClassNode.java:401)
        	at jadx.core.dex.nodes.ClassNode.decompile(ClassNode.java:389)
        	at jadx.core.dex.nodes.ClassNode.getCode(ClassNode.java:339)
        */
    protected final long increaseLength() {
        /*
            r6 = this;
            r0 = r6
            r1 = r0
            long r1 = r1.length
            r2 = 1
            long r1 = r1 + r2
            // decode failed: arraycopy: source index -1 out of bounds for object array[6]
            r0.length = r1
            return r-1
        */
        throw new UnsupportedOperationException("Method not decompiled: jdk.nashorn.internal.runtime.arrays.ArrayData.increaseLength():long");
    }

    /*  JADX ERROR: Failed to decode insn: 0x0007: MOVE_MULTI
        java.lang.ArrayIndexOutOfBoundsException: arraycopy: source index -1 out of bounds for object array[6]
        	at java.base/java.lang.System.arraycopy(Native Method)
        	at jadx.plugins.input.java.data.code.StackState.insert(StackState.java:52)
        	at jadx.plugins.input.java.data.code.CodeDecodeState.insert(CodeDecodeState.java:137)
        	at jadx.plugins.input.java.data.code.JavaInsnsRegister.dup2x1(JavaInsnsRegister.java:313)
        	at jadx.plugins.input.java.data.code.JavaInsnData.decode(JavaInsnData.java:46)
        	at jadx.core.dex.instructions.InsnDecoder.lambda$process$0(InsnDecoder.java:50)
        	at jadx.plugins.input.java.data.code.JavaCodeReader.visitInstructions(JavaCodeReader.java:85)
        	at jadx.core.dex.instructions.InsnDecoder.process(InsnDecoder.java:46)
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:158)
        	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:458)
        	at jadx.core.ProcessClass.process(ProcessClass.java:69)
        	at jadx.core.ProcessClass.generateCode(ProcessClass.java:109)
        	at jadx.core.dex.nodes.ClassNode.generateClassCode(ClassNode.java:401)
        	at jadx.core.dex.nodes.ClassNode.decompile(ClassNode.java:389)
        	at jadx.core.dex.nodes.ClassNode.getCode(ClassNode.java:339)
        */
    protected final long decreaseLength() {
        /*
            r6 = this;
            r0 = r6
            r1 = r0
            long r1 = r1.length
            r2 = 1
            long r1 = r1 - r2
            // decode failed: arraycopy: source index -1 out of bounds for object array[6]
            r0.length = r1
            return r-1
        */
        throw new UnsupportedOperationException("Method not decompiled: jdk.nashorn.internal.runtime.arrays.ArrayData.decreaseLength():long");
    }

    public abstract ArrayData shiftLeft(int i2);

    public abstract ArrayData shiftRight(int i2);

    public abstract ArrayData ensure(long j2);

    public abstract ArrayData shrink(long j2);

    public abstract ArrayData set(int i2, Object obj, boolean z2);

    public abstract ArrayData set(int i2, int i3, boolean z2);

    public abstract ArrayData set(int i2, double d2, boolean z2);

    public abstract int getInt(int i2);

    public abstract double getDouble(int i2);

    public abstract Object getObject(int i2);

    public abstract boolean has(int i2);

    public abstract ArrayData delete(int i2);

    public abstract ArrayData delete(long j2, long j3);

    public abstract ArrayData convert(Class<?> cls);

    public abstract Object pop();

    public abstract ArrayData slice(long j2, long j3);

    static {
        $assertionsDisabled = !ArrayData.class.desiredAssertionStatus();
        EMPTY_ARRAY = new UntouchedArrayData();
        THROW_UNWARRANTED = CompilerConstants.staticCall(MethodHandles.lookup(), ArrayData.class, "throwUnwarranted", Void.TYPE, ArrayData.class, Integer.TYPE, Integer.TYPE);
    }

    /* loaded from: nashorn.jar:jdk/nashorn/internal/runtime/arrays/ArrayData$UntouchedArrayData.class */
    private static class UntouchedArrayData extends ContinuousArrayData {
        static final /* synthetic */ boolean $assertionsDisabled;

        static {
            $assertionsDisabled = !ArrayData.class.desiredAssertionStatus();
        }

        private UntouchedArrayData() {
            super(0L);
        }

        private ArrayData toRealArrayData() {
            return new IntArrayData(0);
        }

        private ArrayData toRealArrayData(int index) {
            IntArrayData newData = new IntArrayData(index + 1);
            return new DeletedRangeArrayFilter(newData, 0L, index);
        }

        @Override // jdk.nashorn.internal.runtime.arrays.ContinuousArrayData, jdk.nashorn.internal.runtime.arrays.ArrayData
        public ContinuousArrayData copy() {
            if ($assertionsDisabled || length() == 0) {
                return this;
            }
            throw new AssertionError();
        }

        @Override // jdk.nashorn.internal.runtime.arrays.ArrayData
        public Object asArrayOfType(Class<?> componentType) {
            return Array.newInstance(componentType, 0);
        }

        @Override // jdk.nashorn.internal.runtime.arrays.ArrayData
        public Object[] asObjectArray() {
            return ScriptRuntime.EMPTY_ARRAY;
        }

        @Override // jdk.nashorn.internal.runtime.arrays.ArrayData
        public ArrayData ensure(long safeIndex) {
            if (!$assertionsDisabled && safeIndex < 0) {
                throw new AssertionError();
            }
            if (safeIndex >= 131072) {
                return new SparseArrayData(this, safeIndex + 1);
            }
            return toRealArrayData((int) safeIndex);
        }

        @Override // jdk.nashorn.internal.runtime.arrays.ArrayData
        public ArrayData convert(Class<?> type) {
            return toRealArrayData().convert(type);
        }

        @Override // jdk.nashorn.internal.runtime.arrays.ArrayData
        public ArrayData delete(int index) {
            return new DeletedRangeArrayFilter(this, index, index);
        }

        @Override // jdk.nashorn.internal.runtime.arrays.ArrayData
        public ArrayData delete(long fromIndex, long toIndex) {
            return new DeletedRangeArrayFilter(this, fromIndex, toIndex);
        }

        @Override // jdk.nashorn.internal.runtime.arrays.ArrayData
        public ArrayData shiftLeft(int by2) {
            return this;
        }

        @Override // jdk.nashorn.internal.runtime.arrays.ArrayData
        public ArrayData shiftRight(int by2) {
            return this;
        }

        @Override // jdk.nashorn.internal.runtime.arrays.ArrayData
        public ArrayData shrink(long newLength) {
            return this;
        }

        @Override // jdk.nashorn.internal.runtime.arrays.ArrayData
        public ArrayData set(int index, Object value, boolean strict) {
            return toRealArrayData(index).set(index, value, strict);
        }

        @Override // jdk.nashorn.internal.runtime.arrays.ArrayData
        public ArrayData set(int index, int value, boolean strict) {
            return toRealArrayData(index).set(index, value, strict);
        }

        @Override // jdk.nashorn.internal.runtime.arrays.ArrayData
        public ArrayData set(int index, double value, boolean strict) {
            return toRealArrayData(index).set(index, value, strict);
        }

        @Override // jdk.nashorn.internal.runtime.arrays.ArrayData
        public int getInt(int index) {
            throw new ArrayIndexOutOfBoundsException(index);
        }

        @Override // jdk.nashorn.internal.runtime.arrays.ArrayData
        public double getDouble(int index) {
            throw new ArrayIndexOutOfBoundsException(index);
        }

        @Override // jdk.nashorn.internal.runtime.arrays.ArrayData
        public Object getObject(int index) {
            throw new ArrayIndexOutOfBoundsException(index);
        }

        @Override // jdk.nashorn.internal.runtime.arrays.ArrayData
        public boolean has(int index) {
            return false;
        }

        @Override // jdk.nashorn.internal.runtime.arrays.ArrayData
        public Object pop() {
            return ScriptRuntime.UNDEFINED;
        }

        @Override // jdk.nashorn.internal.runtime.arrays.ArrayData
        public ArrayData push(boolean strict, Object item) {
            return toRealArrayData().push(strict, item);
        }

        @Override // jdk.nashorn.internal.runtime.arrays.ArrayData
        public ArrayData slice(long from, long to) {
            return this;
        }

        @Override // jdk.nashorn.internal.runtime.arrays.ContinuousArrayData
        public ContinuousArrayData fastConcat(ContinuousArrayData otherData) {
            return otherData.copy();
        }

        public String toString() {
            return getClass().getSimpleName();
        }

        @Override // jdk.nashorn.internal.runtime.arrays.ContinuousArrayData
        public MethodHandle getElementGetter(Class<?> returnType, int programPoint) {
            return null;
        }

        @Override // jdk.nashorn.internal.runtime.arrays.ContinuousArrayData
        public MethodHandle getElementSetter(Class<?> elementType) {
            return null;
        }

        @Override // jdk.nashorn.internal.runtime.arrays.ContinuousArrayData
        public Class<?> getElementType() {
            return Integer.TYPE;
        }

        @Override // jdk.nashorn.internal.runtime.arrays.ContinuousArrayData
        public Class<?> getBoxedElementType() {
            return Integer.class;
        }
    }

    protected ArrayData(long length) {
        this.length = length;
    }

    public static ArrayData initialArray() {
        return new IntArrayData();
    }

    protected static void throwUnwarranted(ArrayData data, int programPoint, int index) {
        throw new UnwarrantedOptimismException(data.getObject(index), programPoint);
    }

    protected static int alignUp(int size) {
        return ((size + 32) - 1) & (-32);
    }

    public static ArrayData allocate(long length) {
        if (length == 0) {
            return new IntArrayData();
        }
        if (length >= 131072) {
            return new SparseArrayData(EMPTY_ARRAY, length);
        }
        return new DeletedRangeArrayFilter(new IntArrayData((int) length), 0L, length - 1);
    }

    public static ArrayData allocate(Object array) {
        Class<?> clazz = array.getClass();
        if (clazz == int[].class) {
            return new IntArrayData((int[]) array, ((int[]) array).length);
        }
        if (clazz == double[].class) {
            return new NumberArrayData((double[]) array, ((double[]) array).length);
        }
        return new ObjectArrayData((Object[]) array, ((Object[]) array).length);
    }

    public static ArrayData allocate(int[] array) {
        return new IntArrayData(array, array.length);
    }

    public static ArrayData allocate(double[] array) {
        return new NumberArrayData(array, array.length);
    }

    public static ArrayData allocate(Object[] array) {
        return new ObjectArrayData(array, array.length);
    }

    public static ArrayData allocate(ByteBuffer buf) {
        return new ByteBufferArrayData(buf);
    }

    public static ArrayData freeze(ArrayData underlying) {
        return new FrozenArrayFilter(underlying);
    }

    public static ArrayData seal(ArrayData underlying) {
        return new SealedArrayFilter(underlying);
    }

    public static ArrayData preventExtension(ArrayData underlying) {
        return new NonExtensibleArrayFilter(underlying);
    }

    public static ArrayData setIsLengthNotWritable(ArrayData underlying) {
        return new LengthNotWritableFilter(underlying);
    }

    public final long length() {
        return this.length;
    }

    public Object asArrayOfType(Class<?> componentType) {
        return JSType.convertArray(asObjectArray(), componentType);
    }

    public void setLength(long length) {
        this.length = length;
    }

    public ArrayData setEmpty(int index) {
        return this;
    }

    public ArrayData setEmpty(long lo, long hi) {
        return this;
    }

    public Type getOptimisticType() {
        return Type.OBJECT;
    }

    public int getIntOptimistic(int index, int programPoint) {
        throw new UnwarrantedOptimismException(getObject(index), programPoint, getOptimisticType());
    }

    public double getDoubleOptimistic(int index, int programPoint) {
        throw new UnwarrantedOptimismException(getObject(index), programPoint, getOptimisticType());
    }

    public boolean canDelete(int index, boolean strict) {
        return true;
    }

    public boolean canDelete(long longIndex, boolean strict) {
        return true;
    }

    public final ArrayData safeDelete(long fromIndex, long toIndex, boolean strict) {
        if (fromIndex <= toIndex && canDelete(fromIndex, strict)) {
            return delete(fromIndex, toIndex);
        }
        return this;
    }

    public PropertyDescriptor getDescriptor(Global global, int index) {
        return global.newDataDescriptor(getObject(index), true, true, true);
    }

    public ArrayData push(boolean strict, Object... items) {
        if (items.length == 0) {
            return this;
        }
        Class<?> widest = widestType(items);
        ArrayData newData = convert(widest);
        long pos = newData.length;
        for (Object item : items) {
            newData = newData.ensure(pos);
            long j2 = pos;
            pos = j2 + 1;
            newData.set((int) j2, item, strict);
        }
        return newData;
    }

    public ArrayData push(boolean strict, Object item) {
        return push(strict, item);
    }

    public ArrayData fastSplice(int start, int removed, int added) throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    static Class<?> widestType(Object... items) {
        if (!$assertionsDisabled && items.length <= 0) {
            throw new AssertionError();
        }
        Class<?> widest = Integer.class;
        for (Object item : items) {
            if (item == null) {
                return Object.class;
            }
            Class<?> itemClass = item.getClass();
            if (itemClass == Double.class || itemClass == Float.class || itemClass == Long.class) {
                if (widest == Integer.class) {
                    widest = Double.class;
                }
            } else if (itemClass != Integer.class && itemClass != Short.class && itemClass != Byte.class) {
                return Object.class;
            }
        }
        return widest;
    }

    protected List<Long> computeIteratorKeys() {
        List<Long> keys = new ArrayList<>();
        long len = length();
        long jNextIndex = 0;
        while (true) {
            long i2 = jNextIndex;
            if (i2 < len) {
                if (has((int) i2)) {
                    keys.add(Long.valueOf(i2));
                }
                jNextIndex = nextIndex(i2);
            } else {
                return keys;
            }
        }
    }

    public Iterator<Long> indexIterator() {
        return computeIteratorKeys().iterator();
    }

    public static int nextSize(int size) {
        return alignUp(size + 1) * 2;
    }

    long nextIndex(long index) {
        return index + 1;
    }

    static Object invoke(MethodHandle mh, Object arg) {
        try {
            return (Object) mh.invoke(arg);
        } catch (Error | RuntimeException e2) {
            throw e2;
        } catch (Throwable t2) {
            throw new RuntimeException(t2);
        }
    }

    public GuardedInvocation findFastCallMethod(Class<? extends ArrayData> clazz, CallSiteDescriptor desc, LinkRequest request) {
        return null;
    }

    public GuardedInvocation findFastGetMethod(Class<? extends ArrayData> clazz, CallSiteDescriptor desc, LinkRequest request, String operator) {
        return null;
    }

    public GuardedInvocation findFastGetIndexMethod(Class<? extends ArrayData> clazz, CallSiteDescriptor desc, LinkRequest request) {
        return null;
    }

    public GuardedInvocation findFastSetIndexMethod(Class<? extends ArrayData> clazz, CallSiteDescriptor desc, LinkRequest request) {
        return null;
    }
}
