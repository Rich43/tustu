package java.lang.invoke;

import java.lang.invoke.BoundMethodHandle;
import java.lang.invoke.LambdaForm;
import java.lang.invoke.MethodHandleImpl;
import java.lang.ref.SoftReference;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.ConcurrentHashMap;
import sun.invoke.util.Wrapper;

/* loaded from: rt.jar:java/lang/invoke/LambdaFormEditor.class */
class LambdaFormEditor {
    final LambdaForm lambdaForm;
    private static final int MIN_CACHE_ARRAY_SIZE = 4;
    private static final int MAX_CACHE_ARRAY_SIZE = 16;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !LambdaFormEditor.class.desiredAssertionStatus();
    }

    private LambdaFormEditor(LambdaForm lambdaForm) {
        this.lambdaForm = lambdaForm;
    }

    static LambdaFormEditor lambdaFormEditor(LambdaForm lambdaForm) {
        return new LambdaFormEditor(lambdaForm.uncustomize());
    }

    /* loaded from: rt.jar:java/lang/invoke/LambdaFormEditor$Transform.class */
    private static final class Transform extends SoftReference<LambdaForm> {
        final long packedBytes;
        final byte[] fullBytes;
        private static final boolean STRESS_TEST = false;
        private static final int PACKED_BYTE_SIZE = 4;
        private static final int PACKED_BYTE_MASK = 15;
        private static final int PACKED_BYTE_MAX_LENGTH = 16;
        private static final byte[] NO_BYTES;
        static final /* synthetic */ boolean $assertionsDisabled;

        /* loaded from: rt.jar:java/lang/invoke/LambdaFormEditor$Transform$Kind.class */
        private enum Kind {
            NO_KIND,
            BIND_ARG,
            ADD_ARG,
            DUP_ARG,
            SPREAD_ARGS,
            FILTER_ARG,
            FILTER_RETURN,
            FILTER_RETURN_TO_ZERO,
            COLLECT_ARGS,
            COLLECT_ARGS_TO_VOID,
            COLLECT_ARGS_TO_ARRAY,
            FOLD_ARGS,
            FOLD_ARGS_TO_VOID,
            PERMUTE_ARGS
        }

        static {
            $assertionsDisabled = !LambdaFormEditor.class.desiredAssertionStatus();
            NO_BYTES = new byte[0];
        }

        private static long packedBytes(byte[] bArr) {
            if (bArr.length > 16) {
                return 0L;
            }
            long j2 = 0;
            int i2 = 0;
            for (int i3 = 0; i3 < bArr.length; i3++) {
                int i4 = bArr[i3] & 255;
                i2 |= i4;
                j2 |= i4 << (i3 * 4);
            }
            if (!inRange(i2)) {
                return 0L;
            }
            return j2;
        }

        private static long packedBytes(int i2, int i3) {
            if ($assertionsDisabled || inRange(i2 | i3)) {
                return (i2 << 0) | (i3 << 4);
            }
            throw new AssertionError();
        }

        private static long packedBytes(int i2, int i3, int i4) {
            if ($assertionsDisabled || inRange(i2 | i3 | i4)) {
                return (i2 << 0) | (i3 << 4) | (i4 << 8);
            }
            throw new AssertionError();
        }

        private static long packedBytes(int i2, int i3, int i4, int i5) {
            if ($assertionsDisabled || inRange(i2 | i3 | i4 | i5)) {
                return (i2 << 0) | (i3 << 4) | (i4 << 8) | (i5 << 12);
            }
            throw new AssertionError();
        }

        private static boolean inRange(int i2) {
            if ($assertionsDisabled || (i2 & 255) == i2) {
                return (i2 & (-16)) == 0;
            }
            throw new AssertionError();
        }

        private static byte[] fullBytes(int... iArr) {
            byte[] bArr = new byte[iArr.length];
            int i2 = 0;
            for (int i3 : iArr) {
                int i4 = i2;
                i2++;
                bArr[i4] = bval(i3);
            }
            if ($assertionsDisabled || packedBytes(bArr) == 0) {
                return bArr;
            }
            throw new AssertionError();
        }

        private byte byteAt(int i2) {
            if (this.packedBytes == 0) {
                if (i2 >= this.fullBytes.length) {
                    return (byte) 0;
                }
                return this.fullBytes[i2];
            }
            if (!$assertionsDisabled && this.fullBytes != null) {
                throw new AssertionError();
            }
            if (i2 > 16) {
                return (byte) 0;
            }
            return (byte) ((r0 >>> (i2 * 4)) & 15);
        }

        Kind kind() {
            return Kind.values()[byteAt(0)];
        }

        private Transform(long j2, byte[] bArr, LambdaForm lambdaForm) {
            super(lambdaForm);
            this.packedBytes = j2;
            this.fullBytes = bArr;
        }

        private Transform(long j2) {
            this(j2, null, null);
            if (!$assertionsDisabled && j2 == 0) {
                throw new AssertionError();
            }
        }

        private Transform(byte[] bArr) {
            this(0L, bArr, null);
        }

        private static byte bval(int i2) {
            if ($assertionsDisabled || (i2 & 255) == i2) {
                return (byte) i2;
            }
            throw new AssertionError();
        }

        private static byte bval(Kind kind) {
            return bval(kind.ordinal());
        }

        static Transform of(Kind kind, int i2) {
            byte bBval = bval(kind);
            if (inRange(bBval | i2)) {
                return new Transform(packedBytes(bBval, i2));
            }
            return new Transform(fullBytes(bBval, i2));
        }

        static Transform of(Kind kind, int i2, int i3) {
            byte bOrdinal = (byte) kind.ordinal();
            if (inRange(bOrdinal | i2 | i3)) {
                return new Transform(packedBytes(bOrdinal, i2, i3));
            }
            return new Transform(fullBytes(bOrdinal, i2, i3));
        }

        static Transform of(Kind kind, int i2, int i3, int i4) {
            byte bOrdinal = (byte) kind.ordinal();
            if (inRange(bOrdinal | i2 | i3 | i4)) {
                return new Transform(packedBytes(bOrdinal, i2, i3, i4));
            }
            return new Transform(fullBytes(bOrdinal, i2, i3, i4));
        }

        static Transform of(Kind kind, int... iArr) {
            return ofBothArrays(kind, iArr, NO_BYTES);
        }

        static Transform of(Kind kind, int i2, byte[] bArr) {
            return ofBothArrays(kind, new int[]{i2}, bArr);
        }

        static Transform of(Kind kind, int i2, int i3, byte[] bArr) {
            return ofBothArrays(kind, new int[]{i2, i3}, bArr);
        }

        private static Transform ofBothArrays(Kind kind, int[] iArr, byte[] bArr) {
            byte[] bArr2 = new byte[1 + iArr.length + bArr.length];
            int i2 = 0 + 1;
            bArr2[0] = bval(kind);
            for (int i3 : iArr) {
                int i4 = i2;
                i2++;
                bArr2[i4] = bval(i3);
            }
            for (byte b2 : bArr) {
                int i5 = i2;
                i2++;
                bArr2[i5] = b2;
            }
            long jPackedBytes = packedBytes(bArr2);
            if (jPackedBytes != 0) {
                return new Transform(jPackedBytes);
            }
            return new Transform(bArr2);
        }

        Transform withResult(LambdaForm lambdaForm) {
            return new Transform(this.packedBytes, this.fullBytes, lambdaForm);
        }

        public boolean equals(Object obj) {
            return (obj instanceof Transform) && equals((Transform) obj);
        }

        public boolean equals(Transform transform) {
            return this.packedBytes == transform.packedBytes && Arrays.equals(this.fullBytes, transform.fullBytes);
        }

        public int hashCode() {
            if (this.packedBytes != 0) {
                if ($assertionsDisabled || this.fullBytes == null) {
                    return Long.hashCode(this.packedBytes);
                }
                throw new AssertionError();
            }
            return Arrays.hashCode(this.fullBytes);
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            long j2 = this.packedBytes;
            if (j2 != 0) {
                sb.append("(");
                while (j2 != 0) {
                    sb.append(j2 & 15);
                    j2 >>>= 4;
                    if (j2 != 0) {
                        sb.append(",");
                    }
                }
                sb.append(")");
            }
            if (this.fullBytes != null) {
                sb.append("unpacked");
                sb.append(Arrays.toString(this.fullBytes));
            }
            LambdaForm lambdaForm = get();
            if (lambdaForm != null) {
                sb.append(" result=");
                sb.append((Object) lambdaForm);
            }
            return sb.toString();
        }
    }

    private LambdaForm getInCache(Transform transform) {
        Transform transform2;
        if (!$assertionsDisabled && transform.get() != null) {
            throw new AssertionError();
        }
        Object obj = this.lambdaForm.transformCache;
        Transform transform3 = null;
        if (obj instanceof ConcurrentHashMap) {
            transform3 = (Transform) ((ConcurrentHashMap) obj).get(transform);
        } else {
            if (obj == null) {
                return null;
            }
            if (obj instanceof Transform) {
                Transform transform4 = (Transform) obj;
                if (transform4.equals(transform)) {
                    transform3 = transform4;
                }
            } else {
                Transform[] transformArr = (Transform[]) obj;
                int i2 = 0;
                while (true) {
                    if (i2 >= transformArr.length || (transform2 = transformArr[i2]) == null) {
                        break;
                    }
                    if (transform2.equals(transform)) {
                        transform3 = transform2;
                        break;
                    }
                    i2++;
                }
            }
        }
        if (!$assertionsDisabled && transform3 != null && !transform.equals(transform3)) {
            throw new AssertionError();
        }
        if (transform3 != null) {
            return transform3.get();
        }
        return null;
    }

    private LambdaForm putInCache(Transform transform, LambdaForm lambdaForm) {
        Transform[] transformArr;
        int i2;
        int i3;
        Transform transform2;
        Transform transformWithResult = transform.withResult(lambdaForm);
        int i4 = 0;
        while (true) {
            Object obj = this.lambdaForm.transformCache;
            if (obj instanceof ConcurrentHashMap) {
                ConcurrentHashMap concurrentHashMap = (ConcurrentHashMap) obj;
                Transform transform3 = (Transform) concurrentHashMap.putIfAbsent(transformWithResult, transformWithResult);
                if (transform3 == null) {
                    return lambdaForm;
                }
                LambdaForm lambdaForm2 = transform3.get();
                if (lambdaForm2 != null) {
                    return lambdaForm2;
                }
                if (concurrentHashMap.replace(transformWithResult, transform3, transformWithResult)) {
                    return lambdaForm;
                }
            } else {
                if (!$assertionsDisabled && i4 != 0) {
                    throw new AssertionError();
                }
                synchronized (this.lambdaForm) {
                    Object obj2 = this.lambdaForm.transformCache;
                    if (!(obj2 instanceof ConcurrentHashMap)) {
                        if (obj2 == null) {
                            this.lambdaForm.transformCache = transformWithResult;
                            return lambdaForm;
                        }
                        if (obj2 instanceof Transform) {
                            Transform transform4 = (Transform) obj2;
                            if (transform4.equals(transformWithResult)) {
                                LambdaForm lambdaForm3 = transform4.get();
                                if (lambdaForm3 == null) {
                                    this.lambdaForm.transformCache = transformWithResult;
                                    return lambdaForm;
                                }
                                return lambdaForm3;
                            }
                            if (transform4.get() == null) {
                                this.lambdaForm.transformCache = transformWithResult;
                                return lambdaForm;
                            }
                            transformArr = new Transform[4];
                            transformArr[0] = transform4;
                            this.lambdaForm.transformCache = transformArr;
                        } else {
                            transformArr = (Transform[]) obj2;
                        }
                        int length = transformArr.length;
                        i2 = -1;
                        i3 = 0;
                        while (i3 < length && (transform2 = transformArr[i3]) != null) {
                            if (transform2.equals(transformWithResult)) {
                                LambdaForm lambdaForm4 = transform2.get();
                                if (lambdaForm4 == null) {
                                    transformArr[i3] = transformWithResult;
                                    return lambdaForm;
                                }
                                return lambdaForm4;
                            }
                            if (i2 < 0 && transform2.get() == null) {
                                i2 = i3;
                            }
                            i3++;
                        }
                        if (i3 < length || i2 >= 0) {
                            break;
                        }
                        if (length < 16) {
                            transformArr = (Transform[]) Arrays.copyOf(transformArr, Math.min(length * 2, 16));
                            this.lambdaForm.transformCache = transformArr;
                            break;
                        }
                        ConcurrentHashMap concurrentHashMap2 = new ConcurrentHashMap(32);
                        for (Transform transform5 : transformArr) {
                            concurrentHashMap2.put(transform5, transform5);
                        }
                        this.lambdaForm.transformCache = concurrentHashMap2;
                    }
                }
            }
            i4++;
        }
        transformArr[i2 >= 0 ? i2 : i3] = transformWithResult;
        return lambdaForm;
    }

    private LambdaFormBuffer buffer() {
        return new LambdaFormBuffer(this.lambdaForm);
    }

    private BoundMethodHandle.SpeciesData oldSpeciesData() {
        return BoundMethodHandle.speciesData(this.lambdaForm);
    }

    private BoundMethodHandle.SpeciesData newSpeciesData(LambdaForm.BasicType basicType) {
        return oldSpeciesData().extendWith(basicType);
    }

    BoundMethodHandle bindArgumentL(BoundMethodHandle boundMethodHandle, int i2, Object obj) {
        if ($assertionsDisabled || boundMethodHandle.speciesData() == oldSpeciesData()) {
            return boundMethodHandle.copyWithExtendL(bindArgumentType(boundMethodHandle, i2, LambdaForm.BasicType.L_TYPE), bindArgumentForm(1 + i2), obj);
        }
        throw new AssertionError();
    }

    BoundMethodHandle bindArgumentI(BoundMethodHandle boundMethodHandle, int i2, int i3) {
        if ($assertionsDisabled || boundMethodHandle.speciesData() == oldSpeciesData()) {
            return boundMethodHandle.copyWithExtendI(bindArgumentType(boundMethodHandle, i2, LambdaForm.BasicType.I_TYPE), bindArgumentForm(1 + i2), i3);
        }
        throw new AssertionError();
    }

    BoundMethodHandle bindArgumentJ(BoundMethodHandle boundMethodHandle, int i2, long j2) {
        if ($assertionsDisabled || boundMethodHandle.speciesData() == oldSpeciesData()) {
            return boundMethodHandle.copyWithExtendJ(bindArgumentType(boundMethodHandle, i2, LambdaForm.BasicType.J_TYPE), bindArgumentForm(1 + i2), j2);
        }
        throw new AssertionError();
    }

    BoundMethodHandle bindArgumentF(BoundMethodHandle boundMethodHandle, int i2, float f2) {
        if ($assertionsDisabled || boundMethodHandle.speciesData() == oldSpeciesData()) {
            return boundMethodHandle.copyWithExtendF(bindArgumentType(boundMethodHandle, i2, LambdaForm.BasicType.F_TYPE), bindArgumentForm(1 + i2), f2);
        }
        throw new AssertionError();
    }

    BoundMethodHandle bindArgumentD(BoundMethodHandle boundMethodHandle, int i2, double d2) {
        if ($assertionsDisabled || boundMethodHandle.speciesData() == oldSpeciesData()) {
            return boundMethodHandle.copyWithExtendD(bindArgumentType(boundMethodHandle, i2, LambdaForm.BasicType.D_TYPE), bindArgumentForm(1 + i2), d2);
        }
        throw new AssertionError();
    }

    private MethodType bindArgumentType(BoundMethodHandle boundMethodHandle, int i2, LambdaForm.BasicType basicType) {
        if (!$assertionsDisabled && boundMethodHandle.form.uncustomize() != this.lambdaForm) {
            throw new AssertionError();
        }
        if (!$assertionsDisabled && boundMethodHandle.form.names[1 + i2].type != basicType) {
            throw new AssertionError();
        }
        if ($assertionsDisabled || LambdaForm.BasicType.basicType(boundMethodHandle.type().parameterType(i2)) == basicType) {
            return boundMethodHandle.type().dropParameterTypes(i2, i2 + 1);
        }
        throw new AssertionError();
    }

    LambdaForm bindArgumentForm(int i2) {
        Transform transformOf = Transform.of(Transform.Kind.BIND_ARG, i2);
        LambdaForm inCache = getInCache(transformOf);
        if (inCache != null) {
            if ($assertionsDisabled || inCache.parameterConstraint(0) == newSpeciesData(this.lambdaForm.parameterType(i2))) {
                return inCache;
            }
            throw new AssertionError();
        }
        LambdaFormBuffer lambdaFormBufferBuffer = buffer();
        lambdaFormBufferBuffer.startEdit();
        BoundMethodHandle.SpeciesData speciesDataOldSpeciesData = oldSpeciesData();
        BoundMethodHandle.SpeciesData speciesDataNewSpeciesData = newSpeciesData(this.lambdaForm.parameterType(i2));
        LambdaForm.Name nameParameter = this.lambdaForm.parameter(0);
        LambdaForm.NamedFunction namedFunction = speciesDataNewSpeciesData.getterFunction(speciesDataOldSpeciesData.fieldCount());
        if (i2 != 0) {
            lambdaFormBufferBuffer.replaceFunctions(speciesDataOldSpeciesData.getterFunctions(), speciesDataNewSpeciesData.getterFunctions(), nameParameter);
            LambdaForm.Name nameWithConstraint = nameParameter.withConstraint(speciesDataNewSpeciesData);
            lambdaFormBufferBuffer.renameParameter(0, nameWithConstraint);
            lambdaFormBufferBuffer.replaceParameterByNewExpression(i2, new LambdaForm.Name(namedFunction, nameWithConstraint));
        } else {
            if (!$assertionsDisabled && speciesDataOldSpeciesData != BoundMethodHandle.SpeciesData.EMPTY) {
                throw new AssertionError();
            }
            LambdaForm.Name nameWithConstraint2 = new LambdaForm.Name(LambdaForm.BasicType.L_TYPE).withConstraint(speciesDataNewSpeciesData);
            lambdaFormBufferBuffer.replaceParameterByNewExpression(0, new LambdaForm.Name(namedFunction, nameWithConstraint2));
            lambdaFormBufferBuffer.insertParameter(0, nameWithConstraint2);
        }
        return putInCache(transformOf, lambdaFormBufferBuffer.endEdit());
    }

    LambdaForm addArgumentForm(int i2, LambdaForm.BasicType basicType) {
        Transform transformOf = Transform.of(Transform.Kind.ADD_ARG, i2, basicType.ordinal());
        LambdaForm inCache = getInCache(transformOf);
        if (inCache != null) {
            if (!$assertionsDisabled && inCache.arity != this.lambdaForm.arity + 1) {
                throw new AssertionError();
            }
            if ($assertionsDisabled || inCache.parameterType(i2) == basicType) {
                return inCache;
            }
            throw new AssertionError();
        }
        LambdaFormBuffer lambdaFormBufferBuffer = buffer();
        lambdaFormBufferBuffer.startEdit();
        lambdaFormBufferBuffer.insertParameter(i2, new LambdaForm.Name(basicType));
        return putInCache(transformOf, lambdaFormBufferBuffer.endEdit());
    }

    LambdaForm dupArgumentForm(int i2, int i3) {
        Transform transformOf = Transform.of(Transform.Kind.DUP_ARG, i2, i3);
        LambdaForm inCache = getInCache(transformOf);
        if (inCache != null) {
            if ($assertionsDisabled || inCache.arity == this.lambdaForm.arity - 1) {
                return inCache;
            }
            throw new AssertionError();
        }
        LambdaFormBuffer lambdaFormBufferBuffer = buffer();
        lambdaFormBufferBuffer.startEdit();
        if (!$assertionsDisabled && this.lambdaForm.parameter(i2).constraint != null) {
            throw new AssertionError();
        }
        if (!$assertionsDisabled && this.lambdaForm.parameter(i3).constraint != null) {
            throw new AssertionError();
        }
        lambdaFormBufferBuffer.replaceParameterByCopy(i3, i2);
        return putInCache(transformOf, lambdaFormBufferBuffer.endEdit());
    }

    LambdaForm spreadArgumentsForm(int i2, Class<?> cls, int i3) throws IllegalArgumentException {
        Class<?> componentType = cls.getComponentType();
        Class<?> cls2 = cls;
        if (!componentType.isPrimitive()) {
            cls2 = Object[].class;
        }
        LambdaForm.BasicType basicType = LambdaForm.BasicType.basicType(componentType);
        int iOrdinal = basicType.ordinal();
        if (basicType.basicTypeClass() != componentType && componentType.isPrimitive()) {
            iOrdinal = LambdaForm.BasicType.TYPE_LIMIT + Wrapper.forPrimitiveType(componentType).ordinal();
        }
        Transform transformOf = Transform.of(Transform.Kind.SPREAD_ARGS, i2, iOrdinal, i3);
        LambdaForm inCache = getInCache(transformOf);
        if (inCache != null) {
            if ($assertionsDisabled || inCache.arity == (this.lambdaForm.arity - i3) + 1) {
                return inCache;
            }
            throw new AssertionError();
        }
        LambdaFormBuffer lambdaFormBufferBuffer = buffer();
        lambdaFormBufferBuffer.startEdit();
        if (!$assertionsDisabled && i2 > 255) {
            throw new AssertionError();
        }
        if (!$assertionsDisabled && i2 + i3 > this.lambdaForm.arity) {
            throw new AssertionError();
        }
        if (!$assertionsDisabled && i2 <= 0) {
            throw new AssertionError();
        }
        LambdaForm.Name name = new LambdaForm.Name(LambdaForm.BasicType.L_TYPE);
        LambdaForm.Name name2 = new LambdaForm.Name(MethodHandleImpl.Lazy.NF_checkSpreadArgument, name, Integer.valueOf(i3));
        int iArity = this.lambdaForm.arity();
        int i4 = iArity + 1;
        lambdaFormBufferBuffer.insertExpression(iArity, name2);
        MethodHandle methodHandleArrayElementGetter = MethodHandles.arrayElementGetter(cls2);
        for (int i5 = 0; i5 < i3; i5++) {
            lambdaFormBufferBuffer.insertExpression(i4 + i5, new LambdaForm.Name(methodHandleArrayElementGetter, name, Integer.valueOf(i5)));
            lambdaFormBufferBuffer.replaceParameterByCopy(i2 + i5, i4 + i5);
        }
        lambdaFormBufferBuffer.insertParameter(i2, name);
        return putInCache(transformOf, lambdaFormBufferBuffer.endEdit());
    }

    LambdaForm collectArgumentsForm(int i2, MethodType methodType) {
        int iParameterCount = methodType.parameterCount();
        boolean z2 = methodType.returnType() == Void.TYPE;
        if (iParameterCount == 1 && !z2) {
            return filterArgumentForm(i2, LambdaForm.BasicType.basicType(methodType.parameterType(0)));
        }
        LambdaForm.BasicType[] basicTypeArrBasicTypes = LambdaForm.BasicType.basicTypes(methodType.parameterList());
        Transform.Kind kind = z2 ? Transform.Kind.COLLECT_ARGS_TO_VOID : Transform.Kind.COLLECT_ARGS;
        if (z2 && iParameterCount == 0) {
            i2 = 1;
        }
        Transform transformOf = Transform.of(kind, i2, iParameterCount, LambdaForm.BasicType.basicTypesOrd(basicTypeArrBasicTypes));
        LambdaForm inCache = getInCache(transformOf);
        if (inCache != null) {
            if (!$assertionsDisabled) {
                if (inCache.arity != (this.lambdaForm.arity - (z2 ? 0 : 1)) + iParameterCount) {
                    throw new AssertionError();
                }
            }
            return inCache;
        }
        return putInCache(transformOf, makeArgumentCombinationForm(i2, methodType, false, z2));
    }

    LambdaForm collectArgumentArrayForm(int i2, MethodHandle methodHandle) {
        MethodType methodTypeType = methodHandle.type();
        int iParameterCount = methodTypeType.parameterCount();
        if (!$assertionsDisabled && methodHandle.intrinsicName() != MethodHandleImpl.Intrinsic.NEW_ARRAY) {
            throw new AssertionError();
        }
        Class<?> componentType = methodTypeType.returnType().getComponentType();
        LambdaForm.BasicType basicType = LambdaForm.BasicType.basicType(componentType);
        int iOrdinal = basicType.ordinal();
        if (basicType.basicTypeClass() != componentType) {
            if (!componentType.isPrimitive()) {
                return null;
            }
            iOrdinal = LambdaForm.BasicType.TYPE_LIMIT + Wrapper.forPrimitiveType(componentType).ordinal();
        }
        if (!$assertionsDisabled && !methodTypeType.parameterList().equals(Collections.nCopies(iParameterCount, componentType))) {
            throw new AssertionError();
        }
        Transform transformOf = Transform.of(Transform.Kind.COLLECT_ARGS_TO_ARRAY, i2, iParameterCount, iOrdinal);
        LambdaForm inCache = getInCache(transformOf);
        if (inCache != null) {
            if ($assertionsDisabled || inCache.arity == (this.lambdaForm.arity - 1) + iParameterCount) {
                return inCache;
            }
            throw new AssertionError();
        }
        LambdaFormBuffer lambdaFormBufferBuffer = buffer();
        lambdaFormBufferBuffer.startEdit();
        if (!$assertionsDisabled && i2 + 1 > this.lambdaForm.arity) {
            throw new AssertionError();
        }
        if (!$assertionsDisabled && i2 <= 0) {
            throw new AssertionError();
        }
        LambdaForm.Name[] nameArr = new LambdaForm.Name[iParameterCount];
        for (int i3 = 0; i3 < iParameterCount; i3++) {
            nameArr[i3] = new LambdaForm.Name(i2 + i3, basicType);
        }
        LambdaForm.Name name = new LambdaForm.Name(methodHandle, nameArr);
        int iArity = this.lambdaForm.arity();
        lambdaFormBufferBuffer.insertExpression(iArity, name);
        int i4 = i2 + 1;
        for (LambdaForm.Name name2 : nameArr) {
            int i5 = i4;
            i4++;
            lambdaFormBufferBuffer.insertParameter(i5, name2);
        }
        if (!$assertionsDisabled && lambdaFormBufferBuffer.lastIndexOf(name) != iArity + nameArr.length) {
            throw new AssertionError();
        }
        lambdaFormBufferBuffer.replaceParameterByCopy(i2, iArity + nameArr.length);
        return putInCache(transformOf, lambdaFormBufferBuffer.endEdit());
    }

    LambdaForm filterArgumentForm(int i2, LambdaForm.BasicType basicType) {
        Transform transformOf = Transform.of(Transform.Kind.FILTER_ARG, i2, basicType.ordinal());
        LambdaForm inCache = getInCache(transformOf);
        if (inCache != null) {
            if (!$assertionsDisabled && inCache.arity != this.lambdaForm.arity) {
                throw new AssertionError();
            }
            if ($assertionsDisabled || inCache.parameterType(i2) == basicType) {
                return inCache;
            }
            throw new AssertionError();
        }
        return putInCache(transformOf, makeArgumentCombinationForm(i2, MethodType.methodType(this.lambdaForm.parameterType(i2).basicTypeClass(), basicType.basicTypeClass()), false, false));
    }

    private LambdaForm makeArgumentCombinationForm(int i2, MethodType methodType, boolean z2, boolean z3) {
        LambdaForm.Name[] nameArr;
        LambdaFormBuffer lambdaFormBufferBuffer = buffer();
        lambdaFormBufferBuffer.startEdit();
        int iParameterCount = methodType.parameterCount();
        int i3 = z3 ? 0 : 1;
        if (!$assertionsDisabled && i2 > 255) {
            throw new AssertionError();
        }
        if (!$assertionsDisabled) {
            if (i2 + i3 + (z2 ? iParameterCount : 0) > this.lambdaForm.arity) {
                throw new AssertionError();
            }
        }
        if (!$assertionsDisabled && i2 <= 0) {
            throw new AssertionError();
        }
        if (!$assertionsDisabled && methodType != methodType.basicType()) {
            throw new AssertionError();
        }
        if (!$assertionsDisabled && methodType.returnType() == Void.TYPE && !z3) {
            throw new AssertionError();
        }
        BoundMethodHandle.SpeciesData speciesDataOldSpeciesData = oldSpeciesData();
        BoundMethodHandle.SpeciesData speciesDataNewSpeciesData = newSpeciesData(LambdaForm.BasicType.L_TYPE);
        LambdaForm.Name nameParameter = this.lambdaForm.parameter(0);
        lambdaFormBufferBuffer.replaceFunctions(speciesDataOldSpeciesData.getterFunctions(), speciesDataNewSpeciesData.getterFunctions(), nameParameter);
        LambdaForm.Name nameWithConstraint = nameParameter.withConstraint(speciesDataNewSpeciesData);
        lambdaFormBufferBuffer.renameParameter(0, nameWithConstraint);
        LambdaForm.Name name = new LambdaForm.Name(speciesDataNewSpeciesData.getterFunction(speciesDataOldSpeciesData.fieldCount()), nameWithConstraint);
        Object[] objArr = new Object[1 + iParameterCount];
        objArr[0] = name;
        if (z2) {
            nameArr = new LambdaForm.Name[0];
            System.arraycopy(this.lambdaForm.names, i2 + i3, objArr, 1, iParameterCount);
        } else {
            nameArr = new LambdaForm.Name[iParameterCount];
            LambdaForm.BasicType[] basicTypeArrBasicTypes = LambdaForm.BasicType.basicTypes(methodType.parameterList());
            for (int i4 = 0; i4 < basicTypeArrBasicTypes.length; i4++) {
                nameArr[i4] = new LambdaForm.Name(i2 + i4, basicTypeArrBasicTypes[i4]);
            }
            System.arraycopy(nameArr, 0, objArr, 1, iParameterCount);
        }
        LambdaForm.Name name2 = new LambdaForm.Name(methodType, objArr);
        int iArity = this.lambdaForm.arity();
        lambdaFormBufferBuffer.insertExpression(iArity + 0, name);
        lambdaFormBufferBuffer.insertExpression(iArity + 1, name2);
        int i5 = i2 + i3;
        for (LambdaForm.Name name3 : nameArr) {
            int i6 = i5;
            i5++;
            lambdaFormBufferBuffer.insertParameter(i6, name3);
        }
        if (!$assertionsDisabled && lambdaFormBufferBuffer.lastIndexOf(name2) != iArity + 1 + nameArr.length) {
            throw new AssertionError();
        }
        if (!z3) {
            lambdaFormBufferBuffer.replaceParameterByCopy(i2, iArity + 1 + nameArr.length);
        }
        return lambdaFormBufferBuffer.endEdit();
    }

    LambdaForm filterReturnForm(LambdaForm.BasicType basicType, boolean z2) {
        LambdaForm.Name name;
        Transform transformOf = Transform.of(z2 ? Transform.Kind.FILTER_RETURN_TO_ZERO : Transform.Kind.FILTER_RETURN, basicType.ordinal());
        LambdaForm inCache = getInCache(transformOf);
        if (inCache != null) {
            if (!$assertionsDisabled && inCache.arity != this.lambdaForm.arity) {
                throw new AssertionError();
            }
            if ($assertionsDisabled || inCache.returnType() == basicType) {
                return inCache;
            }
            throw new AssertionError();
        }
        LambdaFormBuffer lambdaFormBufferBuffer = buffer();
        lambdaFormBufferBuffer.startEdit();
        int length = this.lambdaForm.names.length;
        if (z2) {
            if (basicType == LambdaForm.BasicType.V_TYPE) {
                name = null;
            } else {
                name = new LambdaForm.Name(LambdaForm.constantZero(basicType), new Object[0]);
            }
        } else {
            BoundMethodHandle.SpeciesData speciesDataOldSpeciesData = oldSpeciesData();
            BoundMethodHandle.SpeciesData speciesDataNewSpeciesData = newSpeciesData(LambdaForm.BasicType.L_TYPE);
            LambdaForm.Name nameParameter = this.lambdaForm.parameter(0);
            lambdaFormBufferBuffer.replaceFunctions(speciesDataOldSpeciesData.getterFunctions(), speciesDataNewSpeciesData.getterFunctions(), nameParameter);
            LambdaForm.Name nameWithConstraint = nameParameter.withConstraint(speciesDataNewSpeciesData);
            lambdaFormBufferBuffer.renameParameter(0, nameWithConstraint);
            LambdaForm.Name name2 = new LambdaForm.Name(speciesDataNewSpeciesData.getterFunction(speciesDataOldSpeciesData.fieldCount()), nameWithConstraint);
            length++;
            lambdaFormBufferBuffer.insertExpression(length, name2);
            LambdaForm.BasicType basicTypeReturnType = this.lambdaForm.returnType();
            if (basicTypeReturnType == LambdaForm.BasicType.V_TYPE) {
                name = new LambdaForm.Name(MethodType.methodType(basicType.basicTypeClass()), name2);
            } else {
                name = new LambdaForm.Name(MethodType.methodType(basicType.basicTypeClass(), basicTypeReturnType.basicTypeClass()), name2, this.lambdaForm.names[this.lambdaForm.result]);
            }
        }
        if (name != null) {
            int i2 = length;
            int i3 = length + 1;
            lambdaFormBufferBuffer.insertExpression(i2, name);
        }
        lambdaFormBufferBuffer.setResult(name);
        return putInCache(transformOf, lambdaFormBufferBuffer.endEdit());
    }

    LambdaForm foldArgumentsForm(int i2, boolean z2, MethodType methodType) {
        int iParameterCount = methodType.parameterCount();
        Transform.Kind kind = z2 ? Transform.Kind.FOLD_ARGS_TO_VOID : Transform.Kind.FOLD_ARGS;
        Transform transformOf = Transform.of(kind, i2, iParameterCount);
        LambdaForm inCache = getInCache(transformOf);
        if (inCache != null) {
            if (!$assertionsDisabled) {
                if (inCache.arity != this.lambdaForm.arity - (kind == Transform.Kind.FOLD_ARGS ? 1 : 0)) {
                    throw new AssertionError();
                }
            }
            return inCache;
        }
        return putInCache(transformOf, makeArgumentCombinationForm(i2, methodType, true, z2));
    }

    LambdaForm permuteArgumentsForm(int i2, int[] iArr) {
        if (!$assertionsDisabled && i2 != 1) {
            throw new AssertionError();
        }
        int length = this.lambdaForm.names.length;
        int length2 = iArr.length;
        int iMax = 0;
        boolean z2 = true;
        for (int i3 = 0; i3 < iArr.length; i3++) {
            int i4 = iArr[i3];
            if (i4 != i3) {
                z2 = false;
            }
            iMax = Math.max(iMax, i4 + 1);
        }
        if (!$assertionsDisabled && i2 + iArr.length != this.lambdaForm.arity) {
            throw new AssertionError();
        }
        if (z2) {
            return this.lambdaForm;
        }
        Transform transformOf = Transform.of(Transform.Kind.PERMUTE_ARGS, iArr);
        LambdaForm inCache = getInCache(transformOf);
        if (inCache != null) {
            if ($assertionsDisabled || inCache.arity == i2 + iMax) {
                return inCache;
            }
            throw new AssertionError(inCache);
        }
        LambdaForm.BasicType[] basicTypeArr = new LambdaForm.BasicType[iMax];
        for (int i5 = 0; i5 < length2; i5++) {
            basicTypeArr[iArr[i5]] = this.lambdaForm.names[i2 + i5].type;
        }
        if (!$assertionsDisabled && i2 + length2 != this.lambdaForm.arity) {
            throw new AssertionError();
        }
        if (!$assertionsDisabled && !permutedTypesMatch(iArr, basicTypeArr, this.lambdaForm.names, i2)) {
            throw new AssertionError();
        }
        int i6 = 0;
        while (i6 < length2 && iArr[i6] == i6) {
            i6++;
        }
        LambdaForm.Name[] nameArr = new LambdaForm.Name[(length - length2) + iMax];
        System.arraycopy(this.lambdaForm.names, 0, nameArr, 0, i2 + i6);
        int i7 = length - this.lambdaForm.arity;
        System.arraycopy(this.lambdaForm.names, i2 + length2, nameArr, i2 + iMax, i7);
        int length3 = nameArr.length - i7;
        int i8 = this.lambdaForm.result;
        if (i8 >= i2) {
            if (i8 < i2 + length2) {
                i8 = iArr[i8 - i2] + i2;
            } else {
                i8 = (i8 - length2) + iMax;
            }
        }
        for (int i9 = i6; i9 < length2; i9++) {
            LambdaForm.Name name = this.lambdaForm.names[i2 + i9];
            int i10 = iArr[i9];
            LambdaForm.Name name2 = nameArr[i2 + i10];
            if (name2 == null) {
                LambdaForm.Name name3 = new LambdaForm.Name(basicTypeArr[i10]);
                name2 = name3;
                nameArr[i2 + i10] = name3;
            } else if (!$assertionsDisabled && name2.type != basicTypeArr[i10]) {
                throw new AssertionError();
            }
            for (int i11 = length3; i11 < nameArr.length; i11++) {
                nameArr[i11] = nameArr[i11].replaceName(name, name2);
            }
        }
        for (int i12 = i2 + i6; i12 < length3; i12++) {
            if (nameArr[i12] == null) {
                nameArr[i12] = LambdaForm.argument(i12, basicTypeArr[i12 - i2]);
            }
        }
        for (int i13 = this.lambdaForm.arity; i13 < this.lambdaForm.names.length; i13++) {
            int i14 = (i13 - this.lambdaForm.arity) + length3;
            LambdaForm.Name name4 = this.lambdaForm.names[i13];
            LambdaForm.Name name5 = nameArr[i14];
            if (name4 != name5) {
                for (int i15 = i14 + 1; i15 < nameArr.length; i15++) {
                    nameArr[i15] = nameArr[i15].replaceName(name4, name5);
                }
            }
        }
        return putInCache(transformOf, new LambdaForm(this.lambdaForm.debugName, length3, nameArr, i8));
    }

    static boolean permutedTypesMatch(int[] iArr, LambdaForm.BasicType[] basicTypeArr, LambdaForm.Name[] nameArr, int i2) {
        for (int i3 = 0; i3 < iArr.length; i3++) {
            if (!$assertionsDisabled && !nameArr[i2 + i3].isParam()) {
                throw new AssertionError();
            }
            if (!$assertionsDisabled && nameArr[i2 + i3].type != basicTypeArr[iArr[i3]]) {
                throw new AssertionError();
            }
        }
        return true;
    }
}
