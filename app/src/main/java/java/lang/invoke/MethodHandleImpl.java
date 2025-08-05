package java.lang.invoke;

import com.efiAnalytics.plugin.ecu.ControllerParameter;
import java.io.IOException;
import java.io.InputStream;
import java.lang.invoke.BoundMethodHandle;
import java.lang.invoke.LambdaForm;
import java.lang.invoke.MemberName;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Array;
import java.net.URLConnection;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import sun.invoke.empty.Empty;
import sun.invoke.util.ValueConversions;
import sun.invoke.util.VerifyType;
import sun.invoke.util.Wrapper;
import sun.reflect.CallerSensitive;
import sun.reflect.Reflection;

/* loaded from: rt.jar:java/lang/invoke/MethodHandleImpl.class */
abstract class MethodHandleImpl {
    private static final int MAX_ARITY;
    private static final Function<MethodHandle, LambdaForm> PRODUCE_BLOCK_INLINING_FORM;
    private static final Function<MethodHandle, LambdaForm> PRODUCE_REINVOKER_FORM;
    static MethodHandle[] FAKE_METHOD_HANDLE_INVOKE;
    private static final Object[] NO_ARGS_ARRAY;
    private static final int FILL_ARRAYS_COUNT = 11;
    private static final int LEFT_ARGS = 10;
    private static final MethodHandle[] FILL_ARRAY_TO_RIGHT;
    private static final ClassValue<MethodHandle[]> TYPED_COLLECTORS;
    static final int MAX_JVM_ARITY = 255;
    static final /* synthetic */ boolean $assertionsDisabled;

    /* loaded from: rt.jar:java/lang/invoke/MethodHandleImpl$Intrinsic.class */
    enum Intrinsic {
        SELECT_ALTERNATIVE,
        GUARD_WITH_CATCH,
        NEW_ARRAY,
        ARRAY_LOAD,
        ARRAY_STORE,
        IDENTITY,
        ZERO,
        NONE
    }

    MethodHandleImpl() {
    }

    static {
        $assertionsDisabled = !MethodHandleImpl.class.desiredAssertionStatus();
        final Object[] objArr = {255};
        AccessController.doPrivileged(new PrivilegedAction<Void>() { // from class: java.lang.invoke.MethodHandleImpl.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public Void run2() {
                objArr[0] = Integer.getInteger(MethodHandleImpl.class.getName() + ".MAX_ARITY", 255);
                return null;
            }
        });
        MAX_ARITY = ((Integer) objArr[0]).intValue();
        PRODUCE_BLOCK_INLINING_FORM = new Function<MethodHandle, LambdaForm>() { // from class: java.lang.invoke.MethodHandleImpl.2
            @Override // java.util.function.Function
            public LambdaForm apply(MethodHandle methodHandle) {
                return DelegatingMethodHandle.makeReinvokerForm(methodHandle, 9, CountingWrapper.class, "reinvoker.dontInline", false, DelegatingMethodHandle.NF_getTarget, CountingWrapper.NF_maybeStopCounting);
            }
        };
        PRODUCE_REINVOKER_FORM = new Function<MethodHandle, LambdaForm>() { // from class: java.lang.invoke.MethodHandleImpl.3
            @Override // java.util.function.Function
            public LambdaForm apply(MethodHandle methodHandle) {
                return DelegatingMethodHandle.makeReinvokerForm(methodHandle, 8, DelegatingMethodHandle.class, DelegatingMethodHandle.NF_getTarget);
            }
        };
        FAKE_METHOD_HANDLE_INVOKE = new MethodHandle[2];
        NO_ARGS_ARRAY = new Object[0];
        FILL_ARRAY_TO_RIGHT = new MethodHandle[MAX_ARITY + 1];
        TYPED_COLLECTORS = new ClassValue<MethodHandle[]>() { // from class: java.lang.invoke.MethodHandleImpl.4
            @Override // java.lang.ClassValue
            protected /* bridge */ /* synthetic */ MethodHandle[] computeValue(Class cls) {
                return computeValue((Class<?>) cls);
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.lang.ClassValue
            protected MethodHandle[] computeValue(Class<?> cls) {
                return new MethodHandle[256];
            }
        };
    }

    static void initStatics() {
        MemberName.Factory.INSTANCE.getClass();
    }

    static MethodHandle makeArrayElementAccessor(Class<?> cls, boolean z2) {
        if (cls == Object[].class) {
            return z2 ? ArrayAccessor.OBJECT_ARRAY_SETTER : ArrayAccessor.OBJECT_ARRAY_GETTER;
        }
        if (!cls.isArray()) {
            throw MethodHandleStatics.newIllegalArgumentException("not an array: " + ((Object) cls));
        }
        MethodHandle[] methodHandleArr = ArrayAccessor.TYPED_ACCESSORS.get(cls);
        boolean z3 = z2;
        MethodHandle methodHandle = methodHandleArr[z3 ? 1 : 0];
        if (methodHandle != null) {
            return methodHandle;
        }
        MethodHandle accessor = ArrayAccessor.getAccessor(cls, z2);
        MethodType methodTypeCorrectType = ArrayAccessor.correctType(cls, z2);
        if (accessor.type() != methodTypeCorrectType) {
            if (!$assertionsDisabled && accessor.type().parameterType(0) != Object[].class) {
                throw new AssertionError();
            }
            if (!$assertionsDisabled) {
                if ((z2 ? accessor.type().parameterType(2) : accessor.type().returnType()) != Object.class) {
                    throw new AssertionError();
                }
            }
            if (!$assertionsDisabled && !z2 && methodTypeCorrectType.parameterType(0).getComponentType() != methodTypeCorrectType.returnType()) {
                throw new AssertionError();
            }
            accessor = accessor.viewAsType(methodTypeCorrectType, false);
        }
        MethodHandle methodHandleMakeIntrinsic = makeIntrinsic(accessor, z2 ? Intrinsic.ARRAY_STORE : Intrinsic.ARRAY_LOAD);
        synchronized (methodHandleArr) {
            if (methodHandleArr[z3 ? 1 : 0] == null) {
                methodHandleArr[z3 ? 1 : 0] = methodHandleMakeIntrinsic;
            } else {
                methodHandleMakeIntrinsic = methodHandleArr[z3 ? 1 : 0];
            }
        }
        return methodHandleMakeIntrinsic;
    }

    /* loaded from: rt.jar:java/lang/invoke/MethodHandleImpl$ArrayAccessor.class */
    static final class ArrayAccessor {
        static final int GETTER_INDEX = 0;
        static final int SETTER_INDEX = 1;
        static final int INDEX_LIMIT = 2;
        static final ClassValue<MethodHandle[]> TYPED_ACCESSORS;
        static final MethodHandle OBJECT_ARRAY_GETTER;
        static final MethodHandle OBJECT_ARRAY_SETTER;
        static final /* synthetic */ boolean $assertionsDisabled;

        ArrayAccessor() {
        }

        static {
            $assertionsDisabled = !MethodHandleImpl.class.desiredAssertionStatus();
            TYPED_ACCESSORS = new ClassValue<MethodHandle[]>() { // from class: java.lang.invoke.MethodHandleImpl.ArrayAccessor.1
                @Override // java.lang.ClassValue
                protected /* bridge */ /* synthetic */ MethodHandle[] computeValue(Class cls) {
                    return computeValue((Class<?>) cls);
                }

                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.lang.ClassValue
                protected MethodHandle[] computeValue(Class<?> cls) {
                    return new MethodHandle[2];
                }
            };
            MethodHandle[] methodHandleArr = TYPED_ACCESSORS.get(Object[].class);
            MethodHandle methodHandleMakeIntrinsic = MethodHandleImpl.makeIntrinsic(getAccessor(Object[].class, false), Intrinsic.ARRAY_LOAD);
            OBJECT_ARRAY_GETTER = methodHandleMakeIntrinsic;
            methodHandleArr[0] = methodHandleMakeIntrinsic;
            MethodHandle methodHandleMakeIntrinsic2 = MethodHandleImpl.makeIntrinsic(getAccessor(Object[].class, true), Intrinsic.ARRAY_STORE);
            OBJECT_ARRAY_SETTER = methodHandleMakeIntrinsic2;
            methodHandleArr[1] = methodHandleMakeIntrinsic2;
            if (!$assertionsDisabled && !InvokerBytecodeGenerator.isStaticallyInvocable(OBJECT_ARRAY_GETTER.internalMemberName())) {
                throw new AssertionError();
            }
            if (!$assertionsDisabled && !InvokerBytecodeGenerator.isStaticallyInvocable(OBJECT_ARRAY_SETTER.internalMemberName())) {
                throw new AssertionError();
            }
        }

        static int getElementI(int[] iArr, int i2) {
            return iArr[i2];
        }

        static long getElementJ(long[] jArr, int i2) {
            return jArr[i2];
        }

        static float getElementF(float[] fArr, int i2) {
            return fArr[i2];
        }

        static double getElementD(double[] dArr, int i2) {
            return dArr[i2];
        }

        static boolean getElementZ(boolean[] zArr, int i2) {
            return zArr[i2];
        }

        static byte getElementB(byte[] bArr, int i2) {
            return bArr[i2];
        }

        static short getElementS(short[] sArr, int i2) {
            return sArr[i2];
        }

        static char getElementC(char[] cArr, int i2) {
            return cArr[i2];
        }

        static Object getElementL(Object[] objArr, int i2) {
            return objArr[i2];
        }

        static void setElementI(int[] iArr, int i2, int i3) {
            iArr[i2] = i3;
        }

        static void setElementJ(long[] jArr, int i2, long j2) {
            jArr[i2] = j2;
        }

        static void setElementF(float[] fArr, int i2, float f2) {
            fArr[i2] = f2;
        }

        static void setElementD(double[] dArr, int i2, double d2) {
            dArr[i2] = d2;
        }

        static void setElementZ(boolean[] zArr, int i2, boolean z2) {
            zArr[i2] = z2;
        }

        static void setElementB(byte[] bArr, int i2, byte b2) {
            bArr[i2] = b2;
        }

        static void setElementS(short[] sArr, int i2, short s2) {
            sArr[i2] = s2;
        }

        static void setElementC(char[] cArr, int i2, char c2) {
            cArr[i2] = c2;
        }

        static void setElementL(Object[] objArr, int i2, Object obj) {
            objArr[i2] = obj;
        }

        static String name(Class<?> cls, boolean z2) {
            Class<?> componentType = cls.getComponentType();
            if (componentType == null) {
                throw MethodHandleStatics.newIllegalArgumentException("not an array", cls);
            }
            return (!z2 ? "getElement" : "setElement") + Wrapper.basicTypeChar(componentType);
        }

        static MethodType type(Class<?> cls, boolean z2) {
            Class<?> componentType = cls.getComponentType();
            Class<?> cls2 = cls;
            if (!componentType.isPrimitive()) {
                cls2 = Object[].class;
                componentType = Object.class;
            }
            if (!z2) {
                return MethodType.methodType(componentType, cls2, Integer.TYPE);
            }
            return MethodType.methodType(Void.TYPE, cls2, Integer.TYPE, componentType);
        }

        static MethodType correctType(Class<?> cls, boolean z2) {
            Class<?> componentType = cls.getComponentType();
            if (!z2) {
                return MethodType.methodType(componentType, cls, Integer.TYPE);
            }
            return MethodType.methodType(Void.TYPE, cls, Integer.TYPE, componentType);
        }

        static MethodHandle getAccessor(Class<?> cls, boolean z2) {
            try {
                return MethodHandles.Lookup.IMPL_LOOKUP.findStatic(ArrayAccessor.class, name(cls, z2), type(cls, z2));
            } catch (ReflectiveOperationException e2) {
                throw MethodHandleStatics.uncaughtException(e2);
            }
        }
    }

    static MethodHandle makePairwiseConvert(MethodHandle methodHandle, MethodType methodType, boolean z2, boolean z3) {
        if (methodType == methodHandle.type()) {
            return methodHandle;
        }
        return makePairwiseConvertByEditor(methodHandle, methodType, z2, z3);
    }

    private static int countNonNull(Object[] objArr) {
        int i2 = 0;
        for (Object obj : objArr) {
            if (obj != null) {
                i2++;
            }
        }
        return i2;
    }

    static MethodHandle makePairwiseConvertByEditor(MethodHandle methodHandle, MethodType methodType, boolean z2, boolean z3) {
        MethodHandle methodHandleBindTo;
        MethodHandle methodHandleBindTo2;
        Object[] objArrComputeValueConversions = computeValueConversions(methodType, methodHandle.type(), z2, z3);
        int iCountNonNull = countNonNull(objArrComputeValueConversions);
        if (iCountNonNull == 0) {
            return methodHandle.viewAsType(methodType, z2);
        }
        MethodType methodTypeBasicType = methodType.basicType();
        MethodType methodTypeBasicType2 = methodHandle.type().basicType();
        BoundMethodHandle boundMethodHandleRebind = methodHandle.rebind();
        for (int i2 = 0; i2 < objArrComputeValueConversions.length - 1; i2++) {
            Object obj = objArrComputeValueConversions[i2];
            if (obj != null) {
                if (obj instanceof Class) {
                    methodHandleBindTo2 = Lazy.MH_castReference.bindTo(obj);
                } else {
                    methodHandleBindTo2 = (MethodHandle) obj;
                }
                Class<?> clsParameterType = methodTypeBasicType.parameterType(i2);
                iCountNonNull--;
                if (iCountNonNull == 0) {
                    methodTypeBasicType2 = methodType;
                } else {
                    methodTypeBasicType2 = methodTypeBasicType2.changeParameterType(i2, clsParameterType);
                }
                boundMethodHandleRebind = boundMethodHandleRebind.copyWithExtendL(methodTypeBasicType2, boundMethodHandleRebind.editor().filterArgumentForm(1 + i2, LambdaForm.BasicType.basicType(clsParameterType)), methodHandleBindTo2).rebind();
            }
        }
        Object obj2 = objArrComputeValueConversions[objArrComputeValueConversions.length - 1];
        if (obj2 != null) {
            if (obj2 instanceof Class) {
                if (obj2 == Void.TYPE) {
                    methodHandleBindTo = null;
                } else {
                    methodHandleBindTo = Lazy.MH_castReference.bindTo(obj2);
                }
            } else {
                methodHandleBindTo = (MethodHandle) obj2;
            }
            Class<?> clsReturnType = methodTypeBasicType.returnType();
            if (!$assertionsDisabled) {
                iCountNonNull--;
                if (iCountNonNull != 0) {
                    throw new AssertionError();
                }
            }
            if (methodHandleBindTo == null) {
                boundMethodHandleRebind = boundMethodHandleRebind.copyWith(methodType, boundMethodHandleRebind.editor().filterReturnForm(LambdaForm.BasicType.basicType(clsReturnType), true));
            } else {
                BoundMethodHandle boundMethodHandleRebind2 = boundMethodHandleRebind.rebind();
                boundMethodHandleRebind = boundMethodHandleRebind2.copyWithExtendL(methodType, boundMethodHandleRebind2.editor().filterReturnForm(LambdaForm.BasicType.basicType(clsReturnType), false), methodHandleBindTo);
            }
        }
        if (!$assertionsDisabled && iCountNonNull != 0) {
            throw new AssertionError();
        }
        if ($assertionsDisabled || boundMethodHandleRebind.type().equals((Object) methodType)) {
            return boundMethodHandleRebind;
        }
        throw new AssertionError();
    }

    static MethodHandle makePairwiseConvertIndirect(MethodHandle methodHandle, MethodType methodType, boolean z2, boolean z3) {
        LambdaForm.Name name;
        LambdaForm.Name name2;
        if (!$assertionsDisabled && methodHandle.type().parameterCount() != methodType.parameterCount()) {
            throw new AssertionError();
        }
        Object[] objArrComputeValueConversions = computeValueConversions(methodType, methodHandle.type(), z2, z3);
        int iParameterCount = methodType.parameterCount();
        int iCountNonNull = countNonNull(objArrComputeValueConversions);
        boolean z4 = objArrComputeValueConversions[iParameterCount] != null;
        boolean z5 = methodType.returnType() == Void.TYPE;
        if (z4 && z5) {
            iCountNonNull--;
            z4 = false;
        }
        int i2 = 1 + iParameterCount;
        int i3 = i2 + iCountNonNull + 1;
        int i4 = !z4 ? -1 : i3 - 1;
        int i5 = (!z4 ? i3 : i4) - 1;
        int i6 = z5 ? -1 : i3 - 1;
        MethodType methodTypeInvokerType = methodType.basicType().invokerType();
        LambdaForm.Name[] nameArrArguments = LambdaForm.arguments(i3 - i2, methodTypeInvokerType);
        Object[] objArr = new Object[0 + iParameterCount];
        int i7 = i2;
        for (int i8 = 0; i8 < iParameterCount; i8++) {
            Object obj = objArrComputeValueConversions[i8];
            if (obj == null) {
                objArr[0 + i8] = nameArrArguments[1 + i8];
            } else {
                if (obj instanceof Class) {
                    name2 = new LambdaForm.Name(Lazy.MH_castReference, (Class) obj, nameArrArguments[1 + i8]);
                } else {
                    name2 = new LambdaForm.Name((MethodHandle) obj, nameArrArguments[1 + i8]);
                }
                if (!$assertionsDisabled && nameArrArguments[i7] != null) {
                    throw new AssertionError();
                }
                int i9 = i7;
                i7++;
                nameArrArguments[i9] = name2;
                if (!$assertionsDisabled && objArr[0 + i8] != null) {
                    throw new AssertionError();
                }
                objArr[0 + i8] = name2;
            }
        }
        if (!$assertionsDisabled && i7 != i5) {
            throw new AssertionError();
        }
        nameArrArguments[i5] = new LambdaForm.Name(methodHandle, objArr);
        Object obj2 = objArrComputeValueConversions[iParameterCount];
        if (!z4) {
            if (!$assertionsDisabled && i5 != nameArrArguments.length - 1) {
                throw new AssertionError();
            }
        } else {
            if (obj2 == Void.TYPE) {
                name = new LambdaForm.Name(LambdaForm.constantZero(LambdaForm.BasicType.basicType(methodType.returnType())), new Object[0]);
            } else if (obj2 instanceof Class) {
                name = new LambdaForm.Name(Lazy.MH_castReference, (Class) obj2, nameArrArguments[i5]);
            } else {
                MethodHandle methodHandle2 = (MethodHandle) obj2;
                if (methodHandle2.type().parameterCount() == 0) {
                    name = new LambdaForm.Name(methodHandle2, new Object[0]);
                } else {
                    name = new LambdaForm.Name(methodHandle2, nameArrArguments[i5]);
                }
            }
            if (!$assertionsDisabled && nameArrArguments[i4] != null) {
                throw new AssertionError();
            }
            nameArrArguments[i4] = name;
            if (!$assertionsDisabled && i4 != nameArrArguments.length - 1) {
                throw new AssertionError();
            }
        }
        return SimpleMethodHandle.make(methodType, new LambdaForm("convert", methodTypeInvokerType.parameterCount(), nameArrArguments, i6));
    }

    /* JADX WARN: Multi-variable type inference failed */
    @ForceInline
    static <T, U> T castReference(Class<? extends T> cls, U u2) {
        if (u2 != 0 && !cls.isInstance(u2)) {
            throw newClassCastException(cls, u2);
        }
        return u2;
    }

    private static ClassCastException newClassCastException(Class<?> cls, Object obj) {
        return new ClassCastException("Cannot cast " + obj.getClass().getName() + " to " + cls.getName());
    }

    static Object[] computeValueConversions(MethodType methodType, MethodType methodType2, boolean z2, boolean z3) {
        int iParameterCount = methodType.parameterCount();
        Object[] objArr = new Object[iParameterCount + 1];
        int i2 = 0;
        while (i2 <= iParameterCount) {
            boolean z4 = i2 == iParameterCount;
            Class<?> clsReturnType = z4 ? methodType2.returnType() : methodType.parameterType(i2);
            Class<?> clsReturnType2 = z4 ? methodType.returnType() : methodType2.parameterType(i2);
            if (!VerifyType.isNullConversion(clsReturnType, clsReturnType2, z2)) {
                objArr[i2] = valueConversion(clsReturnType, clsReturnType2, z2, z3);
            }
            i2++;
        }
        return objArr;
    }

    static MethodHandle makePairwiseConvert(MethodHandle methodHandle, MethodType methodType, boolean z2) {
        return makePairwiseConvert(methodHandle, methodType, z2, false);
    }

    static Object valueConversion(Class<?> cls, Class<?> cls2, boolean z2, boolean z3) {
        MethodHandle methodHandleUnboxExact;
        MethodHandle methodHandleUnboxCast;
        if (!$assertionsDisabled && VerifyType.isNullConversion(cls, cls2, z2)) {
            throw new AssertionError();
        }
        if (cls2 == Void.TYPE) {
            return cls2;
        }
        if (cls.isPrimitive()) {
            if (cls == Void.TYPE) {
                return Void.TYPE;
            }
            if (cls2.isPrimitive()) {
                methodHandleUnboxExact = ValueConversions.convertPrimitive(cls, cls2);
            } else {
                Wrapper wrapperForPrimitiveType = Wrapper.forPrimitiveType(cls);
                methodHandleUnboxExact = ValueConversions.boxExact(wrapperForPrimitiveType);
                if (!$assertionsDisabled && methodHandleUnboxExact.type().parameterType(0) != wrapperForPrimitiveType.primitiveType()) {
                    throw new AssertionError();
                }
                if (!$assertionsDisabled && methodHandleUnboxExact.type().returnType() != wrapperForPrimitiveType.wrapperType()) {
                    throw new AssertionError();
                }
                if (!VerifyType.isNullConversion(wrapperForPrimitiveType.wrapperType(), cls2, z2)) {
                    MethodType methodType = MethodType.methodType(cls2, cls);
                    if (z2) {
                        methodHandleUnboxExact = methodHandleUnboxExact.asType(methodType);
                    } else {
                        methodHandleUnboxExact = makePairwiseConvert(methodHandleUnboxExact, methodType, false);
                    }
                }
            }
        } else if (cls2.isPrimitive()) {
            Wrapper wrapperForPrimitiveType2 = Wrapper.forPrimitiveType(cls2);
            if (z3 || cls == wrapperForPrimitiveType2.wrapperType()) {
                methodHandleUnboxExact = ValueConversions.unboxExact(wrapperForPrimitiveType2, z2);
            } else {
                if (z2) {
                    methodHandleUnboxCast = ValueConversions.unboxWiden(wrapperForPrimitiveType2);
                } else {
                    methodHandleUnboxCast = ValueConversions.unboxCast(wrapperForPrimitiveType2);
                }
                methodHandleUnboxExact = methodHandleUnboxCast;
            }
        } else {
            return cls2;
        }
        if ($assertionsDisabled || methodHandleUnboxExact.type().parameterCount() <= 1) {
            return methodHandleUnboxExact;
        }
        throw new AssertionError((Object) ("pc" + ((Object) Arrays.asList(cls.getSimpleName(), cls2.getSimpleName(), methodHandleUnboxExact))));
    }

    static MethodHandle makeVarargsCollector(MethodHandle methodHandle, Class<?> cls) {
        MethodType methodTypeType = methodHandle.type();
        int iParameterCount = methodTypeType.parameterCount() - 1;
        if (methodTypeType.parameterType(iParameterCount) != cls) {
            methodHandle = methodHandle.asType(methodTypeType.changeParameterType(iParameterCount, cls));
        }
        return new AsVarargsCollector(methodHandle.asFixedArity(), cls);
    }

    /* loaded from: rt.jar:java/lang/invoke/MethodHandleImpl$AsVarargsCollector.class */
    private static final class AsVarargsCollector extends DelegatingMethodHandle {
        private final MethodHandle target;
        private final Class<?> arrayType;

        @Stable
        private MethodHandle asCollectorCache;
        static final /* synthetic */ boolean $assertionsDisabled;

        static {
            $assertionsDisabled = !MethodHandleImpl.class.desiredAssertionStatus();
        }

        AsVarargsCollector(MethodHandle methodHandle, Class<?> cls) {
            this(methodHandle.type(), methodHandle, cls);
        }

        AsVarargsCollector(MethodType methodType, MethodHandle methodHandle, Class<?> cls) {
            super(methodType, methodHandle);
            this.target = methodHandle;
            this.arrayType = cls;
            this.asCollectorCache = methodHandle.asCollector(cls, 0);
        }

        @Override // java.lang.invoke.MethodHandle
        public boolean isVarargsCollector() {
            return true;
        }

        @Override // java.lang.invoke.DelegatingMethodHandle
        protected MethodHandle getTarget() {
            return this.target;
        }

        @Override // java.lang.invoke.MethodHandle
        public MethodHandle asFixedArity() {
            return this.target;
        }

        @Override // java.lang.invoke.MethodHandle
        MethodHandle setVarargs(MemberName memberName) {
            return memberName.isVarargs() ? this : asFixedArity();
        }

        @Override // java.lang.invoke.DelegatingMethodHandle, java.lang.invoke.MethodHandle
        public MethodHandle asTypeUncached(MethodType methodType) {
            MethodType methodTypeType = type();
            int iParameterCount = methodTypeType.parameterCount() - 1;
            int iParameterCount2 = methodType.parameterCount();
            if (iParameterCount2 == iParameterCount + 1 && methodTypeType.parameterType(iParameterCount).isAssignableFrom(methodType.parameterType(iParameterCount))) {
                MethodHandle methodHandleAsType = asFixedArity().asType(methodType);
                this.asTypeCache = methodHandleAsType;
                return methodHandleAsType;
            }
            MethodHandle methodHandle = this.asCollectorCache;
            if (methodHandle != null && methodHandle.type().parameterCount() == iParameterCount2) {
                MethodHandle methodHandleAsType2 = methodHandle.asType(methodType);
                this.asTypeCache = methodHandleAsType2;
                return methodHandleAsType2;
            }
            try {
                MethodHandle methodHandleAsCollector = asFixedArity().asCollector(this.arrayType, iParameterCount2 - iParameterCount);
                if (!$assertionsDisabled && methodHandleAsCollector.type().parameterCount() != iParameterCount2) {
                    throw new AssertionError((Object) ("newArity=" + iParameterCount2 + " but collector=" + ((Object) methodHandleAsCollector)));
                }
                this.asCollectorCache = methodHandleAsCollector;
                MethodHandle methodHandleAsType3 = methodHandleAsCollector.asType(methodType);
                this.asTypeCache = methodHandleAsType3;
                return methodHandleAsType3;
            } catch (IllegalArgumentException e2) {
                throw new WrongMethodTypeException("cannot build collector", e2);
            }
        }

        @Override // java.lang.invoke.MethodHandle
        boolean viewAsTypeChecks(MethodType methodType, boolean z2) {
            super.viewAsTypeChecks(methodType, true);
            if (!z2 && !$assertionsDisabled && !type().lastParameterType().getComponentType().isAssignableFrom(methodType.lastParameterType().getComponentType())) {
                throw new AssertionError(Arrays.asList(this, methodType));
            }
            return true;
        }
    }

    static MethodHandle makeSpreadArguments(MethodHandle methodHandle, Class<?> cls, int i2, int i3) throws IllegalArgumentException {
        MethodType methodTypeType = methodHandle.type();
        for (int i4 = 0; i4 < i3; i4++) {
            Class<?> clsSpreadArgElementType = VerifyType.spreadArgElementType(cls, i4);
            if (clsSpreadArgElementType == null) {
                clsSpreadArgElementType = Object.class;
            }
            methodTypeType = methodTypeType.changeParameterType(i2 + i4, clsSpreadArgElementType);
        }
        MethodHandle methodHandleAsType = methodHandle.asType(methodTypeType);
        MethodType methodTypeReplaceParameterTypes = methodTypeType.replaceParameterTypes(i2, i2 + i3, cls);
        MethodType methodTypeInvokerType = methodTypeReplaceParameterTypes.invokerType();
        LambdaForm.Name[] nameArrArguments = LambdaForm.arguments(i3 + 2, methodTypeInvokerType);
        int iParameterCount = methodTypeInvokerType.parameterCount();
        int[] iArr = new int[methodTypeType.parameterCount()];
        int i5 = 0;
        int i6 = 1;
        while (i5 < methodTypeType.parameterCount() + 1) {
            methodTypeInvokerType.parameterType(i5);
            if (i5 == i2) {
                MethodHandle methodHandleArrayElementGetter = MethodHandles.arrayElementGetter(cls);
                LambdaForm.Name name = nameArrArguments[i6];
                int i7 = iParameterCount;
                iParameterCount++;
                nameArrArguments[i7] = new LambdaForm.Name(Lazy.NF_checkSpreadArgument, name, Integer.valueOf(i3));
                for (int i8 = 0; i8 < i3; i8++) {
                    iArr[i5] = iParameterCount;
                    int i9 = iParameterCount;
                    iParameterCount++;
                    nameArrArguments[i9] = new LambdaForm.Name(methodHandleArrayElementGetter, name, Integer.valueOf(i8));
                    i5++;
                }
            } else if (i5 < iArr.length) {
                iArr[i5] = i6;
            }
            i5++;
            i6++;
        }
        if (!$assertionsDisabled && iParameterCount != nameArrArguments.length - 1) {
            throw new AssertionError();
        }
        LambdaForm.Name[] nameArr = new LambdaForm.Name[methodTypeType.parameterCount()];
        for (int i10 = 0; i10 < methodTypeType.parameterCount(); i10++) {
            nameArr[i10] = nameArrArguments[iArr[i10]];
        }
        nameArrArguments[nameArrArguments.length - 1] = new LambdaForm.Name(methodHandleAsType, nameArr);
        return SimpleMethodHandle.make(methodTypeReplaceParameterTypes, new LambdaForm("spread", methodTypeInvokerType.parameterCount(), nameArrArguments));
    }

    static void checkSpreadArgument(Object obj, int i2) {
        if (obj == null) {
            if (i2 == 0) {
                return;
            }
        } else if (obj instanceof Object[]) {
            if (((Object[]) obj).length == i2) {
                return;
            }
        } else if (Array.getLength(obj) == i2) {
            return;
        }
        throw MethodHandleStatics.newIllegalArgumentException("array is not of length " + i2);
    }

    /* loaded from: rt.jar:java/lang/invoke/MethodHandleImpl$Lazy.class */
    static class Lazy {
        private static final Class<?> MHI = MethodHandleImpl.class;
        private static final MethodHandle[] ARRAYS = MethodHandleImpl.makeArrays();
        private static final MethodHandle[] FILL_ARRAYS = MethodHandleImpl.makeFillArrays();
        static final LambdaForm.NamedFunction NF_checkSpreadArgument;
        static final LambdaForm.NamedFunction NF_guardWithCatch;
        static final LambdaForm.NamedFunction NF_throwException;
        static final LambdaForm.NamedFunction NF_profileBoolean;
        static final MethodHandle MH_castReference;
        static final MethodHandle MH_selectAlternative;
        static final MethodHandle MH_copyAsPrimitiveArray;
        static final MethodHandle MH_fillNewTypedArray;
        static final MethodHandle MH_fillNewArray;
        static final MethodHandle MH_arrayIdentity;

        Lazy() {
        }

        static {
            try {
                NF_checkSpreadArgument = new LambdaForm.NamedFunction(MHI.getDeclaredMethod("checkSpreadArgument", Object.class, Integer.TYPE));
                NF_guardWithCatch = new LambdaForm.NamedFunction(MHI.getDeclaredMethod("guardWithCatch", MethodHandle.class, Class.class, MethodHandle.class, Object[].class));
                NF_throwException = new LambdaForm.NamedFunction(MHI.getDeclaredMethod("throwException", Throwable.class));
                NF_profileBoolean = new LambdaForm.NamedFunction(MHI.getDeclaredMethod("profileBoolean", Boolean.TYPE, int[].class));
                NF_checkSpreadArgument.resolve();
                NF_guardWithCatch.resolve();
                NF_throwException.resolve();
                NF_profileBoolean.resolve();
                MH_castReference = MethodHandles.Lookup.IMPL_LOOKUP.findStatic(MHI, "castReference", MethodType.methodType(Object.class, Class.class, Object.class));
                MH_copyAsPrimitiveArray = MethodHandles.Lookup.IMPL_LOOKUP.findStatic(MHI, "copyAsPrimitiveArray", MethodType.methodType(Object.class, Wrapper.class, Object[].class));
                MH_arrayIdentity = MethodHandles.Lookup.IMPL_LOOKUP.findStatic(MHI, "identity", MethodType.methodType((Class<?>) Object[].class, (Class<?>) Object[].class));
                MH_fillNewArray = MethodHandles.Lookup.IMPL_LOOKUP.findStatic(MHI, "fillNewArray", MethodType.methodType(Object[].class, Integer.class, Object[].class));
                MH_fillNewTypedArray = MethodHandles.Lookup.IMPL_LOOKUP.findStatic(MHI, "fillNewTypedArray", MethodType.methodType(Object[].class, Object[].class, Integer.class, Object[].class));
                MH_selectAlternative = MethodHandleImpl.makeIntrinsic(MethodHandles.Lookup.IMPL_LOOKUP.findStatic(MHI, "selectAlternative", MethodType.methodType(MethodHandle.class, Boolean.TYPE, MethodHandle.class, MethodHandle.class)), Intrinsic.SELECT_ALTERNATIVE);
            } catch (ReflectiveOperationException e2) {
                throw MethodHandleStatics.newInternalError(e2);
            }
        }
    }

    static MethodHandle makeCollectArguments(MethodHandle methodHandle, MethodHandle methodHandle2, int i2, boolean z2) {
        MethodType methodTypeType = methodHandle.type();
        MethodType methodTypeType2 = methodHandle2.type();
        int iParameterCount = methodTypeType2.parameterCount();
        Class<?> clsReturnType = methodTypeType2.returnType();
        MethodType methodTypeDropParameterTypes = methodTypeType.dropParameterTypes(i2, i2 + (clsReturnType == Void.TYPE ? 0 : 1));
        if (!z2) {
            methodTypeDropParameterTypes = methodTypeDropParameterTypes.insertParameterTypes(i2, methodTypeType2.parameterList());
        }
        MethodType methodTypeInvokerType = methodTypeDropParameterTypes.invokerType();
        LambdaForm.Name[] nameArrArguments = LambdaForm.arguments(2, methodTypeInvokerType);
        int length = nameArrArguments.length - 2;
        int length2 = nameArrArguments.length - 1;
        nameArrArguments[length] = new LambdaForm.Name(methodHandle2, (LambdaForm.Name[]) Arrays.copyOfRange(nameArrArguments, 1 + i2, 1 + i2 + iParameterCount));
        LambdaForm.Name[] nameArr = new LambdaForm.Name[methodTypeType.parameterCount()];
        System.arraycopy(nameArrArguments, 1, nameArr, 0, i2);
        int i3 = 1 + i2;
        int i4 = 0 + i2;
        if (clsReturnType != Void.TYPE) {
            i4++;
            nameArr[i4] = nameArrArguments[length];
        }
        if (z2) {
            System.arraycopy(nameArrArguments, i3, nameArr, i4, iParameterCount);
            i4 += iParameterCount;
        }
        int i5 = i3 + iParameterCount;
        int length3 = nameArr.length - i4;
        System.arraycopy(nameArrArguments, i5, nameArr, i4, length3);
        if (!$assertionsDisabled && i5 + length3 != length) {
            throw new AssertionError();
        }
        nameArrArguments[length2] = new LambdaForm.Name(methodHandle, nameArr);
        return SimpleMethodHandle.make(methodTypeDropParameterTypes, new LambdaForm("collect", methodTypeInvokerType.parameterCount(), nameArrArguments));
    }

    @LambdaForm.Hidden
    static MethodHandle selectAlternative(boolean z2, MethodHandle methodHandle, MethodHandle methodHandle2) {
        if (z2) {
            return methodHandle;
        }
        return methodHandle2;
    }

    @LambdaForm.Hidden
    static boolean profileBoolean(boolean z2, int[] iArr) {
        boolean z3 = z2;
        try {
            iArr[z3 ? 1 : 0] = Math.addExact(iArr[z3 ? 1 : 0], 1);
        } catch (ArithmeticException e2) {
            iArr[z3 ? 1 : 0] = iArr[z3 ? 1 : 0] / 2;
        }
        return z2;
    }

    static MethodHandle makeGuardWithTest(MethodHandle methodHandle, MethodHandle methodHandle2, MethodHandle methodHandle3) {
        BoundMethodHandle boundMethodHandleInvokeBasic;
        MethodType methodTypeType = methodHandle2.type();
        if (!$assertionsDisabled && (!methodHandle.type().equals((Object) methodTypeType.changeReturnType(Boolean.TYPE)) || !methodHandle3.type().equals((Object) methodTypeType))) {
            throw new AssertionError();
        }
        LambdaForm lambdaFormMakeGuardWithTestForm = makeGuardWithTestForm(methodTypeType.basicType());
        try {
            if (MethodHandleStatics.PROFILE_GWT) {
                boundMethodHandleInvokeBasic = BoundMethodHandle.speciesData_LLLL().constructor().invokeBasic(methodTypeType, lambdaFormMakeGuardWithTestForm, methodHandle, profile(methodHandle2), profile(methodHandle3), new int[2]);
            } else {
                boundMethodHandleInvokeBasic = BoundMethodHandle.speciesData_LLL().constructor().invokeBasic(methodTypeType, lambdaFormMakeGuardWithTestForm, methodHandle, profile(methodHandle2), profile(methodHandle3));
            }
            if ($assertionsDisabled || boundMethodHandleInvokeBasic.type() == methodTypeType) {
                return boundMethodHandleInvokeBasic;
            }
            throw new AssertionError();
        } catch (Throwable th) {
            throw MethodHandleStatics.uncaughtException(th);
        }
    }

    static MethodHandle profile(MethodHandle methodHandle) {
        if (MethodHandleStatics.DONT_INLINE_THRESHOLD >= 0) {
            return makeBlockInlningWrapper(methodHandle);
        }
        return methodHandle;
    }

    static MethodHandle makeBlockInlningWrapper(MethodHandle methodHandle) {
        return new CountingWrapper(methodHandle, PRODUCE_BLOCK_INLINING_FORM.apply(methodHandle), PRODUCE_BLOCK_INLINING_FORM, PRODUCE_REINVOKER_FORM, MethodHandleStatics.DONT_INLINE_THRESHOLD);
    }

    /* loaded from: rt.jar:java/lang/invoke/MethodHandleImpl$CountingWrapper.class */
    static class CountingWrapper extends DelegatingMethodHandle {
        private final MethodHandle target;
        private int count;
        private Function<MethodHandle, LambdaForm> countingFormProducer;
        private Function<MethodHandle, LambdaForm> nonCountingFormProducer;
        private volatile boolean isCounting;
        static final LambdaForm.NamedFunction NF_maybeStopCounting;

        private CountingWrapper(MethodHandle methodHandle, LambdaForm lambdaForm, Function<MethodHandle, LambdaForm> function, Function<MethodHandle, LambdaForm> function2, int i2) {
            super(methodHandle.type(), lambdaForm);
            this.target = methodHandle;
            this.count = i2;
            this.countingFormProducer = function;
            this.nonCountingFormProducer = function2;
            this.isCounting = i2 > 0;
        }

        @Override // java.lang.invoke.DelegatingMethodHandle
        @LambdaForm.Hidden
        protected MethodHandle getTarget() {
            return this.target;
        }

        @Override // java.lang.invoke.DelegatingMethodHandle, java.lang.invoke.MethodHandle
        public MethodHandle asTypeUncached(MethodType methodType) {
            MethodHandle countingWrapper;
            MethodHandle methodHandleAsType = this.target.asType(methodType);
            if (this.isCounting) {
                countingWrapper = new CountingWrapper(methodHandleAsType, this.countingFormProducer.apply(methodHandleAsType), this.countingFormProducer, this.nonCountingFormProducer, MethodHandleStatics.DONT_INLINE_THRESHOLD);
            } else {
                countingWrapper = methodHandleAsType;
            }
            MethodHandle methodHandle = countingWrapper;
            this.asTypeCache = methodHandle;
            return methodHandle;
        }

        boolean countDown() {
            if (this.count <= 0) {
                if (this.isCounting) {
                    this.isCounting = false;
                    return true;
                }
                return false;
            }
            this.count--;
            return false;
        }

        @LambdaForm.Hidden
        static void maybeStopCounting(Object obj) {
            CountingWrapper countingWrapper = (CountingWrapper) obj;
            if (countingWrapper.countDown()) {
                LambdaForm lambdaFormApply = countingWrapper.nonCountingFormProducer.apply(countingWrapper.target);
                lambdaFormApply.compileToBytecode();
                countingWrapper.updateForm(lambdaFormApply);
            }
        }

        static {
            try {
                NF_maybeStopCounting = new LambdaForm.NamedFunction(CountingWrapper.class.getDeclaredMethod("maybeStopCounting", Object.class));
            } catch (ReflectiveOperationException e2) {
                throw MethodHandleStatics.newInternalError(e2);
            }
        }
    }

    static LambdaForm makeGuardWithTestForm(MethodType methodType) {
        int i2;
        int i3;
        BoundMethodHandle.SpeciesData speciesDataSpeciesData_LLL;
        LambdaForm lambdaFormCachedLambdaForm = methodType.form().cachedLambdaForm(17);
        if (lambdaFormCachedLambdaForm != null) {
            return lambdaFormCachedLambdaForm;
        }
        int iParameterCount = 1 + methodType.parameterCount();
        int i4 = iParameterCount + 1;
        int i5 = i4 + 1;
        int i6 = i5 + 1;
        if (MethodHandleStatics.PROFILE_GWT) {
            i2 = i6;
            i6++;
        } else {
            i2 = -1;
        }
        int i7 = i2;
        int i8 = i6;
        int i9 = i6 + 1;
        if (i7 != -1) {
            i3 = i9;
            i9++;
        } else {
            i3 = -1;
        }
        int i10 = i3;
        int i11 = i9 - 1;
        int i12 = i9;
        int i13 = i9 + 1;
        int i14 = i13 + 1;
        if (!$assertionsDisabled && i13 != i12 + 1) {
            throw new AssertionError();
        }
        MethodType methodTypeInvokerType = methodType.invokerType();
        LambdaForm.Name[] nameArrArguments = LambdaForm.arguments(i14 - iParameterCount, methodTypeInvokerType);
        if (i7 != -1) {
            speciesDataSpeciesData_LLL = BoundMethodHandle.speciesData_LLLL();
        } else {
            speciesDataSpeciesData_LLL = BoundMethodHandle.speciesData_LLL();
        }
        BoundMethodHandle.SpeciesData speciesData = speciesDataSpeciesData_LLL;
        nameArrArguments[0] = nameArrArguments[0].withConstraint(speciesData);
        nameArrArguments[iParameterCount] = new LambdaForm.Name(speciesData.getterFunction(0), nameArrArguments[0]);
        nameArrArguments[i4] = new LambdaForm.Name(speciesData.getterFunction(1), nameArrArguments[0]);
        nameArrArguments[i5] = new LambdaForm.Name(speciesData.getterFunction(2), nameArrArguments[0]);
        if (i7 != -1) {
            nameArrArguments[i7] = new LambdaForm.Name(speciesData.getterFunction(3), nameArrArguments[0]);
        }
        Object[] objArrCopyOfRange = Arrays.copyOfRange(nameArrArguments, 0, iParameterCount, Object[].class);
        MethodType methodTypeBasicType = methodType.changeReturnType(Boolean.TYPE).basicType();
        objArrCopyOfRange[0] = nameArrArguments[iParameterCount];
        nameArrArguments[i8] = new LambdaForm.Name(methodTypeBasicType, objArrCopyOfRange);
        if (i10 != -1) {
            nameArrArguments[i10] = new LambdaForm.Name(Lazy.NF_profileBoolean, nameArrArguments[i8], nameArrArguments[i7]);
        }
        nameArrArguments[i12] = new LambdaForm.Name(Lazy.MH_selectAlternative, nameArrArguments[i11], nameArrArguments[i4], nameArrArguments[i5]);
        objArrCopyOfRange[0] = nameArrArguments[i12];
        nameArrArguments[i13] = new LambdaForm.Name(methodType, objArrCopyOfRange);
        return methodType.form().setCachedLambdaForm(17, new LambdaForm("guard", methodTypeInvokerType.parameterCount(), nameArrArguments, true));
    }

    private static LambdaForm makeGuardWithCatchForm(MethodType methodType) {
        MethodType methodTypeInvokerType = methodType.invokerType();
        LambdaForm lambdaFormCachedLambdaForm = methodType.form().cachedLambdaForm(16);
        if (lambdaFormCachedLambdaForm != null) {
            return lambdaFormCachedLambdaForm;
        }
        int iParameterCount = 1 + methodType.parameterCount();
        int i2 = iParameterCount + 1;
        int i3 = i2 + 1;
        int i4 = i3 + 1;
        int i5 = i4 + 1;
        int i6 = i5 + 1;
        int i7 = i6 + 1;
        int i8 = i7 + 1;
        LambdaForm.Name[] nameArrArguments = LambdaForm.arguments((i8 + 1) - iParameterCount, methodTypeInvokerType);
        BoundMethodHandle.SpeciesData speciesDataSpeciesData_LLLLL = BoundMethodHandle.speciesData_LLLLL();
        nameArrArguments[0] = nameArrArguments[0].withConstraint(speciesDataSpeciesData_LLLLL);
        nameArrArguments[iParameterCount] = new LambdaForm.Name(speciesDataSpeciesData_LLLLL.getterFunction(0), nameArrArguments[0]);
        nameArrArguments[i2] = new LambdaForm.Name(speciesDataSpeciesData_LLLLL.getterFunction(1), nameArrArguments[0]);
        nameArrArguments[i3] = new LambdaForm.Name(speciesDataSpeciesData_LLLLL.getterFunction(2), nameArrArguments[0]);
        nameArrArguments[i4] = new LambdaForm.Name(speciesDataSpeciesData_LLLLL.getterFunction(3), nameArrArguments[0]);
        nameArrArguments[i5] = new LambdaForm.Name(speciesDataSpeciesData_LLLLL.getterFunction(4), nameArrArguments[0]);
        MethodHandle methodHandleBasicInvoker = MethodHandles.basicInvoker(methodType.changeReturnType(Object.class));
        Object[] objArr = new Object[methodHandleBasicInvoker.type().parameterCount()];
        objArr[0] = nameArrArguments[i4];
        System.arraycopy(nameArrArguments, 1, objArr, 1, iParameterCount - 1);
        nameArrArguments[i6] = new LambdaForm.Name(makeIntrinsic(methodHandleBasicInvoker, Intrinsic.GUARD_WITH_CATCH), objArr);
        nameArrArguments[i7] = new LambdaForm.Name(Lazy.NF_guardWithCatch, nameArrArguments[iParameterCount], nameArrArguments[i2], nameArrArguments[i3], nameArrArguments[i6]);
        nameArrArguments[i8] = new LambdaForm.Name(MethodHandles.basicInvoker(MethodType.methodType(methodType.rtype(), (Class<?>) Object.class)), nameArrArguments[i5], nameArrArguments[i7]);
        return methodType.form().setCachedLambdaForm(16, new LambdaForm("guardWithCatch", methodTypeInvokerType.parameterCount(), nameArrArguments));
    }

    static MethodHandle makeGuardWithCatch(MethodHandle methodHandle, Class<? extends Throwable> cls, MethodHandle methodHandle2) {
        MethodHandle methodHandleIdentity;
        MethodType methodTypeType = methodHandle.type();
        LambdaForm lambdaFormMakeGuardWithCatchForm = makeGuardWithCatchForm(methodTypeType.basicType());
        MethodHandle methodHandleAsType = varargsArray(methodTypeType.parameterCount()).asType(methodTypeType.changeReturnType(Object[].class));
        Class<?> clsReturnType = methodTypeType.returnType();
        if (clsReturnType.isPrimitive()) {
            if (clsReturnType == Void.TYPE) {
                methodHandleIdentity = ValueConversions.ignore();
            } else {
                methodHandleIdentity = ValueConversions.unboxExact(Wrapper.forPrimitiveType(methodTypeType.returnType()));
            }
        } else {
            methodHandleIdentity = MethodHandles.identity(Object.class);
        }
        try {
            BoundMethodHandle boundMethodHandleInvokeBasic = BoundMethodHandle.speciesData_LLLLL().constructor().invokeBasic(methodTypeType, lambdaFormMakeGuardWithCatchForm, methodHandle, cls, methodHandle2, methodHandleAsType, methodHandleIdentity);
            if ($assertionsDisabled || boundMethodHandleInvokeBasic.type() == methodTypeType) {
                return boundMethodHandleInvokeBasic;
            }
            throw new AssertionError();
        } catch (Throwable th) {
            throw MethodHandleStatics.uncaughtException(th);
        }
    }

    @LambdaForm.Hidden
    static Object guardWithCatch(MethodHandle methodHandle, Class<? extends Throwable> cls, MethodHandle methodHandle2, Object... objArr) throws Throwable {
        try {
            return methodHandle.asFixedArity().invokeWithArguments(objArr);
        } catch (Throwable th) {
            if (cls.isInstance(th)) {
                return methodHandle2.asFixedArity().invokeWithArguments(prepend(th, objArr));
            }
            throw th;
        }
    }

    @LambdaForm.Hidden
    private static Object[] prepend(Object obj, Object[] objArr) {
        Object[] objArr2 = new Object[objArr.length + 1];
        objArr2[0] = obj;
        System.arraycopy(objArr, 0, objArr2, 1, objArr.length);
        return objArr2;
    }

    static MethodHandle throwException(MethodType methodType) {
        if (!$assertionsDisabled && !Throwable.class.isAssignableFrom(methodType.parameterType(0))) {
            throw new AssertionError();
        }
        int iParameterCount = methodType.parameterCount();
        if (iParameterCount > 1) {
            return MethodHandles.dropArguments(throwException(methodType.dropParameterTypes(1, iParameterCount)), 1, methodType.parameterList().subList(1, iParameterCount));
        }
        return makePairwiseConvert(Lazy.NF_throwException.resolvedHandle(), methodType, false, true);
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: T extends java.lang.Throwable */
    static <T extends Throwable> Empty throwException(T t2) throws Throwable {
        throw t2;
    }

    static MethodHandle fakeMethodHandleInvoke(MemberName memberName) {
        boolean z2;
        if (!$assertionsDisabled && !memberName.isMethodHandleInvoke()) {
            throw new AssertionError();
        }
        switch (memberName.getName()) {
            case "invoke":
                z2 = false;
                break;
            case "invokeExact":
                z2 = true;
                break;
            default:
                throw new InternalError(memberName.getName());
        }
        MethodHandle methodHandle = FAKE_METHOD_HANDLE_INVOKE[z2 ? 1 : 0];
        if (methodHandle != null) {
            return methodHandle;
        }
        MethodHandle methodHandleBindTo = throwException(MethodType.methodType(Object.class, UnsupportedOperationException.class, MethodHandle.class, Object[].class)).bindTo(new UnsupportedOperationException("cannot reflectively invoke MethodHandle"));
        if (!memberName.getInvocationType().equals((Object) methodHandleBindTo.type())) {
            throw new InternalError(memberName.toString());
        }
        MethodHandle methodHandleAsVarargsCollector = methodHandleBindTo.withInternalMemberName(memberName, false).asVarargsCollector(Object[].class);
        if (!$assertionsDisabled && !memberName.isVarargs()) {
            throw new AssertionError();
        }
        FAKE_METHOD_HANDLE_INVOKE[z2 ? 1 : 0] = methodHandleAsVarargsCollector;
        return methodHandleAsVarargsCollector;
    }

    static MethodHandle bindCaller(MethodHandle methodHandle, Class<?> cls) {
        return BindCaller.bindCaller(methodHandle, cls);
    }

    /* loaded from: rt.jar:java/lang/invoke/MethodHandleImpl$BindCaller.class */
    private static class BindCaller {
        private static ClassValue<MethodHandle> CV_makeInjectedInvoker;
        private static final MethodHandle MH_checkCallerClass;
        private static final byte[] T_BYTES;
        static final /* synthetic */ boolean $assertionsDisabled;

        private BindCaller() {
        }

        static {
            $assertionsDisabled = !MethodHandleImpl.class.desiredAssertionStatus();
            CV_makeInjectedInvoker = new ClassValue<MethodHandle>() { // from class: java.lang.invoke.MethodHandleImpl.BindCaller.1
                @Override // java.lang.ClassValue
                protected /* bridge */ /* synthetic */ MethodHandle computeValue(Class cls) {
                    return computeValue((Class<?>) cls);
                }

                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.lang.ClassValue
                protected MethodHandle computeValue(Class<?> cls) {
                    return BindCaller.makeInjectedInvoker(cls);
                }
            };
            if (!$assertionsDisabled && !checkCallerClass(BindCaller.class, BindCaller.class)) {
                throw new AssertionError();
            }
            try {
                MH_checkCallerClass = MethodHandles.Lookup.IMPL_LOOKUP.findStatic(BindCaller.class, "checkCallerClass", MethodType.methodType(Boolean.TYPE, Class.class, Class.class));
                if (!$assertionsDisabled && !(boolean) MH_checkCallerClass.invokeExact(BindCaller.class, BindCaller.class)) {
                    throw new AssertionError();
                }
                final Object[] objArr = {null};
                AccessController.doPrivileged(new PrivilegedAction<Void>() { // from class: java.lang.invoke.MethodHandleImpl.BindCaller.2
                    /* JADX WARN: Can't rename method to resolve collision */
                    /* JADX WARN: Finally extract failed */
                    @Override // java.security.PrivilegedAction
                    /* renamed from: run */
                    public Void run2() {
                        try {
                            String name = T.class.getName();
                            String str = name.substring(name.lastIndexOf(46) + 1) + ".class";
                            URLConnection uRLConnectionOpenConnection = T.class.getResource(str).openConnection();
                            int contentLength = uRLConnectionOpenConnection.getContentLength();
                            byte[] bArr = new byte[contentLength];
                            InputStream inputStream = uRLConnectionOpenConnection.getInputStream();
                            Throwable th = null;
                            try {
                                if (inputStream.read(bArr) != contentLength) {
                                    throw new IOException(str);
                                }
                                if (inputStream != null) {
                                    if (0 != 0) {
                                        try {
                                            inputStream.close();
                                        } catch (Throwable th2) {
                                            th.addSuppressed(th2);
                                        }
                                    } else {
                                        inputStream.close();
                                    }
                                }
                                objArr[0] = bArr;
                                return null;
                            } catch (Throwable th3) {
                                if (inputStream != null) {
                                    if (0 != 0) {
                                        try {
                                            inputStream.close();
                                        } catch (Throwable th4) {
                                            th.addSuppressed(th4);
                                        }
                                    } else {
                                        inputStream.close();
                                    }
                                }
                                throw th3;
                            }
                        } catch (IOException e2) {
                            throw new InternalError(e2);
                        }
                    }
                });
                T_BYTES = (byte[]) objArr[0];
            } catch (Throwable th) {
                throw new InternalError(th);
            }
        }

        static MethodHandle bindCaller(MethodHandle methodHandle, Class<?> cls) {
            if (cls == null || cls.isArray() || cls.isPrimitive() || cls.getName().startsWith("java.") || cls.getName().startsWith("sun.")) {
                throw new InternalError();
            }
            return restoreToType(CV_makeInjectedInvoker.get(cls).bindTo(prepareForInvoker(methodHandle)), methodHandle, cls);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static MethodHandle makeInjectedInvoker(Class<?> cls) {
            Class<?> clsDefineAnonymousClass = MethodHandleStatics.UNSAFE.defineAnonymousClass(cls, T_BYTES, null);
            if (cls.getClassLoader() != clsDefineAnonymousClass.getClassLoader()) {
                throw new InternalError(cls.getName() + " (CL)");
            }
            if (cls.getProtectionDomain() != clsDefineAnonymousClass.getProtectionDomain()) {
                throw new InternalError(cls.getName() + " (PD)");
            }
            try {
                (void) MethodHandles.Lookup.IMPL_LOOKUP.findStatic(clsDefineAnonymousClass, "init", MethodType.methodType(Void.TYPE)).invokeExact();
                try {
                    MethodHandle methodHandleFindStatic = MethodHandles.Lookup.IMPL_LOOKUP.findStatic(clsDefineAnonymousClass, "invoke_V", MethodType.methodType(Object.class, MethodHandle.class, Object[].class));
                    try {
                        (Object) methodHandleFindStatic.invokeExact(prepareForInvoker(MH_checkCallerClass), new Object[]{cls, clsDefineAnonymousClass});
                        return methodHandleFindStatic;
                    } catch (Throwable th) {
                        throw new InternalError(th);
                    }
                } catch (ReflectiveOperationException e2) {
                    throw MethodHandleStatics.uncaughtException(e2);
                }
            } finally {
                Error errorUncaughtException = MethodHandleStatics.uncaughtException(e2);
            }
        }

        private static MethodHandle prepareForInvoker(MethodHandle methodHandle) {
            MethodHandle methodHandleAsFixedArity = methodHandle.asFixedArity();
            MethodType methodTypeType = methodHandleAsFixedArity.type();
            int iParameterCount = methodTypeType.parameterCount();
            MethodHandle methodHandleAsType = methodHandleAsFixedArity.asType(methodTypeType.generic());
            methodHandleAsType.internalForm().compileToBytecode();
            MethodHandle methodHandleAsSpreader = methodHandleAsType.asSpreader(Object[].class, iParameterCount);
            methodHandleAsSpreader.internalForm().compileToBytecode();
            return methodHandleAsSpreader;
        }

        private static MethodHandle restoreToType(MethodHandle methodHandle, MethodHandle methodHandle2, Class<?> cls) {
            MethodType methodTypeType = methodHandle2.type();
            MethodHandle methodHandleAsCollector = methodHandle.asCollector(Object[].class, methodTypeType.parameterCount());
            return new WrappedMember(methodHandleAsCollector.asType(methodTypeType), methodTypeType, methodHandle2.internalMemberName(), methodHandle2.isInvokeSpecial(), cls);
        }

        @CallerSensitive
        private static boolean checkCallerClass(Class<?> cls, Class<?> cls2) {
            Class<?> callerClass = Reflection.getCallerClass();
            if (callerClass != cls && callerClass != cls2) {
                throw new InternalError("found " + callerClass.getName() + ", expected " + cls.getName() + (cls == cls2 ? "" : ", or else " + cls2.getName()));
            }
            return true;
        }

        /* loaded from: rt.jar:java/lang/invoke/MethodHandleImpl$BindCaller$T.class */
        private static class T {
            private T() {
            }

            static void init() {
            }

            static Object invoke_V(MethodHandle methodHandle, Object[] objArr) throws Throwable {
                return (Object) methodHandle.invokeExact(objArr);
            }
        }
    }

    /* loaded from: rt.jar:java/lang/invoke/MethodHandleImpl$WrappedMember.class */
    private static final class WrappedMember extends DelegatingMethodHandle {
        private final MethodHandle target;
        private final MemberName member;
        private final Class<?> callerClass;
        private final boolean isInvokeSpecial;

        private WrappedMember(MethodHandle methodHandle, MethodType methodType, MemberName memberName, boolean z2, Class<?> cls) {
            super(methodType, methodHandle);
            this.target = methodHandle;
            this.member = memberName;
            this.callerClass = cls;
            this.isInvokeSpecial = z2;
        }

        @Override // java.lang.invoke.DelegatingMethodHandle, java.lang.invoke.MethodHandle
        MemberName internalMemberName() {
            return this.member;
        }

        @Override // java.lang.invoke.DelegatingMethodHandle, java.lang.invoke.MethodHandle
        Class<?> internalCallerClass() {
            return this.callerClass;
        }

        @Override // java.lang.invoke.DelegatingMethodHandle, java.lang.invoke.MethodHandle
        boolean isInvokeSpecial() {
            return this.isInvokeSpecial;
        }

        @Override // java.lang.invoke.DelegatingMethodHandle
        protected MethodHandle getTarget() {
            return this.target;
        }

        @Override // java.lang.invoke.DelegatingMethodHandle, java.lang.invoke.MethodHandle
        public MethodHandle asTypeUncached(MethodType methodType) {
            MethodHandle methodHandleAsType = this.target.asType(methodType);
            this.asTypeCache = methodHandleAsType;
            return methodHandleAsType;
        }
    }

    static MethodHandle makeWrappedMember(MethodHandle methodHandle, MemberName memberName, boolean z2) {
        if (memberName.equals(methodHandle.internalMemberName()) && z2 == methodHandle.isInvokeSpecial()) {
            return methodHandle;
        }
        return new WrappedMember(methodHandle, methodHandle.type(), memberName, z2, null);
    }

    /* loaded from: rt.jar:java/lang/invoke/MethodHandleImpl$IntrinsicMethodHandle.class */
    private static final class IntrinsicMethodHandle extends DelegatingMethodHandle {
        private final MethodHandle target;
        private final Intrinsic intrinsicName;

        IntrinsicMethodHandle(MethodHandle methodHandle, Intrinsic intrinsic) {
            super(methodHandle.type(), methodHandle);
            this.target = methodHandle;
            this.intrinsicName = intrinsic;
        }

        @Override // java.lang.invoke.DelegatingMethodHandle
        protected MethodHandle getTarget() {
            return this.target;
        }

        @Override // java.lang.invoke.MethodHandle
        Intrinsic intrinsicName() {
            return this.intrinsicName;
        }

        @Override // java.lang.invoke.DelegatingMethodHandle, java.lang.invoke.MethodHandle
        public MethodHandle asTypeUncached(MethodType methodType) {
            MethodHandle methodHandleAsType = this.target.asType(methodType);
            this.asTypeCache = methodHandleAsType;
            return methodHandleAsType;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Override // java.lang.invoke.DelegatingMethodHandle, java.lang.invoke.MethodHandle
        public String internalProperties() {
            return super.internalProperties() + "\n& Intrinsic=" + ((Object) this.intrinsicName);
        }

        @Override // java.lang.invoke.MethodHandle
        public MethodHandle asCollector(Class<?> cls, int i2) {
            if (this.intrinsicName == Intrinsic.IDENTITY) {
                return MethodHandleImpl.varargsArray(cls, i2).asType(type().asCollectorType(cls, i2));
            }
            return super.asCollector(cls, i2);
        }
    }

    static MethodHandle makeIntrinsic(MethodHandle methodHandle, Intrinsic intrinsic) {
        if (intrinsic == methodHandle.intrinsicName()) {
            return methodHandle;
        }
        return new IntrinsicMethodHandle(methodHandle, intrinsic);
    }

    static MethodHandle makeIntrinsic(MethodType methodType, LambdaForm lambdaForm, Intrinsic intrinsic) {
        return new IntrinsicMethodHandle(SimpleMethodHandle.make(methodType, lambdaForm), intrinsic);
    }

    private static MethodHandle findCollector(String str, int i2, Class<?> cls, Class<?>... clsArr) {
        try {
            return MethodHandles.Lookup.IMPL_LOOKUP.findStatic(MethodHandleImpl.class, str, MethodType.genericMethodType(i2).changeReturnType(cls).insertParameterTypes(0, clsArr));
        } catch (ReflectiveOperationException e2) {
            return null;
        }
    }

    private static Object[] makeArray(Object... objArr) {
        return objArr;
    }

    private static Object[] array() {
        return NO_ARGS_ARRAY;
    }

    private static Object[] array(Object obj) {
        return makeArray(obj);
    }

    private static Object[] array(Object obj, Object obj2) {
        return makeArray(obj, obj2);
    }

    private static Object[] array(Object obj, Object obj2, Object obj3) {
        return makeArray(obj, obj2, obj3);
    }

    private static Object[] array(Object obj, Object obj2, Object obj3, Object obj4) {
        return makeArray(obj, obj2, obj3, obj4);
    }

    private static Object[] array(Object obj, Object obj2, Object obj3, Object obj4, Object obj5) {
        return makeArray(obj, obj2, obj3, obj4, obj5);
    }

    private static Object[] array(Object obj, Object obj2, Object obj3, Object obj4, Object obj5, Object obj6) {
        return makeArray(obj, obj2, obj3, obj4, obj5, obj6);
    }

    private static Object[] array(Object obj, Object obj2, Object obj3, Object obj4, Object obj5, Object obj6, Object obj7) {
        return makeArray(obj, obj2, obj3, obj4, obj5, obj6, obj7);
    }

    private static Object[] array(Object obj, Object obj2, Object obj3, Object obj4, Object obj5, Object obj6, Object obj7, Object obj8) {
        return makeArray(obj, obj2, obj3, obj4, obj5, obj6, obj7, obj8);
    }

    private static Object[] array(Object obj, Object obj2, Object obj3, Object obj4, Object obj5, Object obj6, Object obj7, Object obj8, Object obj9) {
        return makeArray(obj, obj2, obj3, obj4, obj5, obj6, obj7, obj8, obj9);
    }

    private static Object[] array(Object obj, Object obj2, Object obj3, Object obj4, Object obj5, Object obj6, Object obj7, Object obj8, Object obj9, Object obj10) {
        return makeArray(obj, obj2, obj3, obj4, obj5, obj6, obj7, obj8, obj9, obj10);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static MethodHandle[] makeArrays() {
        ArrayList arrayList = new ArrayList();
        while (true) {
            MethodHandle methodHandleFindCollector = findCollector(ControllerParameter.PARAM_CLASS_ARRAY, arrayList.size(), Object[].class, new Class[0]);
            if (methodHandleFindCollector == null) {
                break;
            }
            arrayList.add(makeIntrinsic(methodHandleFindCollector, Intrinsic.NEW_ARRAY));
        }
        if ($assertionsDisabled || arrayList.size() == 11) {
            return (MethodHandle[]) arrayList.toArray(new MethodHandle[MAX_ARITY + 1]);
        }
        throw new AssertionError();
    }

    private static Object[] fillNewArray(Integer num, Object[] objArr) {
        Object[] objArr2 = new Object[num.intValue()];
        fillWithArguments(objArr2, 0, objArr);
        return objArr2;
    }

    private static Object[] fillNewTypedArray(Object[] objArr, Integer num, Object[] objArr2) {
        Object[] objArrCopyOf = Arrays.copyOf(objArr, num.intValue());
        if (!$assertionsDisabled && objArrCopyOf.getClass() == Object[].class) {
            throw new AssertionError();
        }
        fillWithArguments(objArrCopyOf, 0, objArr2);
        return objArrCopyOf;
    }

    private static void fillWithArguments(Object[] objArr, int i2, Object... objArr2) {
        System.arraycopy(objArr2, 0, objArr, i2, objArr2.length);
    }

    private static Object[] fillArray(Integer num, Object[] objArr, Object obj) {
        fillWithArguments(objArr, num.intValue(), obj);
        return objArr;
    }

    private static Object[] fillArray(Integer num, Object[] objArr, Object obj, Object obj2) {
        fillWithArguments(objArr, num.intValue(), obj, obj2);
        return objArr;
    }

    private static Object[] fillArray(Integer num, Object[] objArr, Object obj, Object obj2, Object obj3) {
        fillWithArguments(objArr, num.intValue(), obj, obj2, obj3);
        return objArr;
    }

    private static Object[] fillArray(Integer num, Object[] objArr, Object obj, Object obj2, Object obj3, Object obj4) {
        fillWithArguments(objArr, num.intValue(), obj, obj2, obj3, obj4);
        return objArr;
    }

    private static Object[] fillArray(Integer num, Object[] objArr, Object obj, Object obj2, Object obj3, Object obj4, Object obj5) {
        fillWithArguments(objArr, num.intValue(), obj, obj2, obj3, obj4, obj5);
        return objArr;
    }

    private static Object[] fillArray(Integer num, Object[] objArr, Object obj, Object obj2, Object obj3, Object obj4, Object obj5, Object obj6) {
        fillWithArguments(objArr, num.intValue(), obj, obj2, obj3, obj4, obj5, obj6);
        return objArr;
    }

    private static Object[] fillArray(Integer num, Object[] objArr, Object obj, Object obj2, Object obj3, Object obj4, Object obj5, Object obj6, Object obj7) {
        fillWithArguments(objArr, num.intValue(), obj, obj2, obj3, obj4, obj5, obj6, obj7);
        return objArr;
    }

    private static Object[] fillArray(Integer num, Object[] objArr, Object obj, Object obj2, Object obj3, Object obj4, Object obj5, Object obj6, Object obj7, Object obj8) {
        fillWithArguments(objArr, num.intValue(), obj, obj2, obj3, obj4, obj5, obj6, obj7, obj8);
        return objArr;
    }

    private static Object[] fillArray(Integer num, Object[] objArr, Object obj, Object obj2, Object obj3, Object obj4, Object obj5, Object obj6, Object obj7, Object obj8, Object obj9) {
        fillWithArguments(objArr, num.intValue(), obj, obj2, obj3, obj4, obj5, obj6, obj7, obj8, obj9);
        return objArr;
    }

    private static Object[] fillArray(Integer num, Object[] objArr, Object obj, Object obj2, Object obj3, Object obj4, Object obj5, Object obj6, Object obj7, Object obj8, Object obj9, Object obj10) {
        fillWithArguments(objArr, num.intValue(), obj, obj2, obj3, obj4, obj5, obj6, obj7, obj8, obj9, obj10);
        return objArr;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static MethodHandle[] makeFillArrays() {
        ArrayList arrayList = new ArrayList();
        arrayList.add(null);
        while (true) {
            MethodHandle methodHandleFindCollector = findCollector("fillArray", arrayList.size(), Object[].class, Integer.class, Object[].class);
            if (methodHandleFindCollector == null) {
                break;
            }
            arrayList.add(methodHandleFindCollector);
        }
        if ($assertionsDisabled || arrayList.size() == 11) {
            return (MethodHandle[]) arrayList.toArray(new MethodHandle[0]);
        }
        throw new AssertionError();
    }

    private static Object copyAsPrimitiveArray(Wrapper wrapper, Object... objArr) {
        Object objMakeArray = wrapper.makeArray(objArr.length);
        wrapper.copyArrayUnboxing(objArr, 0, objMakeArray, 0, objArr.length);
        return objMakeArray;
    }

    static MethodHandle varargsArray(int i2) throws RuntimeException {
        MethodHandle methodHandle = Lazy.ARRAYS[i2];
        if (methodHandle != null) {
            return methodHandle;
        }
        MethodHandle methodHandleFindCollector = findCollector(ControllerParameter.PARAM_CLASS_ARRAY, i2, Object[].class, new Class[0]);
        if (methodHandleFindCollector != null) {
            methodHandleFindCollector = makeIntrinsic(methodHandleFindCollector, Intrinsic.NEW_ARRAY);
        }
        if (methodHandleFindCollector != null) {
            MethodHandle methodHandle2 = methodHandleFindCollector;
            Lazy.ARRAYS[i2] = methodHandle2;
            return methodHandle2;
        }
        MethodHandle methodHandleBuildVarargsArray = buildVarargsArray(Lazy.MH_fillNewArray, Lazy.MH_arrayIdentity, i2);
        if (!$assertionsDisabled && !assertCorrectArity(methodHandleBuildVarargsArray, i2)) {
            throw new AssertionError();
        }
        MethodHandle methodHandleMakeIntrinsic = makeIntrinsic(methodHandleBuildVarargsArray, Intrinsic.NEW_ARRAY);
        Lazy.ARRAYS[i2] = methodHandleMakeIntrinsic;
        return methodHandleMakeIntrinsic;
    }

    private static boolean assertCorrectArity(MethodHandle methodHandle, int i2) {
        if ($assertionsDisabled || methodHandle.type().parameterCount() == i2) {
            return true;
        }
        throw new AssertionError((Object) ("arity != " + i2 + ": " + ((Object) methodHandle)));
    }

    static <T> T[] identity(T[] tArr) {
        return tArr;
    }

    private static MethodHandle buildVarargsArray(MethodHandle methodHandle, MethodHandle methodHandle2, int i2) throws RuntimeException {
        MethodHandle methodHandleCollectArguments;
        int iMin = Math.min(i2, 10);
        int i3 = i2 - iMin;
        MethodHandle methodHandleAsCollector = methodHandle.bindTo(Integer.valueOf(i2)).asCollector(Object[].class, iMin);
        MethodHandle methodHandleCollectArguments2 = methodHandle2;
        if (i3 > 0) {
            MethodHandle methodHandleFillToRight = fillToRight(10 + i3);
            if (methodHandleCollectArguments2 == Lazy.MH_arrayIdentity) {
                methodHandleCollectArguments2 = methodHandleFillToRight;
            } else {
                methodHandleCollectArguments2 = MethodHandles.collectArguments(methodHandleCollectArguments2, 0, methodHandleFillToRight);
            }
        }
        if (methodHandleCollectArguments2 == Lazy.MH_arrayIdentity) {
            methodHandleCollectArguments = methodHandleAsCollector;
        } else {
            methodHandleCollectArguments = MethodHandles.collectArguments(methodHandleCollectArguments2, 0, methodHandleAsCollector);
        }
        return methodHandleCollectArguments;
    }

    private static MethodHandle fillToRight(int i2) {
        MethodHandle methodHandle = FILL_ARRAY_TO_RIGHT[i2];
        if (methodHandle != null) {
            return methodHandle;
        }
        MethodHandle methodHandleBuildFiller = buildFiller(i2);
        if (!$assertionsDisabled && !assertCorrectArity(methodHandleBuildFiller, (i2 - 10) + 1)) {
            throw new AssertionError();
        }
        FILL_ARRAY_TO_RIGHT[i2] = methodHandleBuildFiller;
        return methodHandleBuildFiller;
    }

    private static MethodHandle buildFiller(int i2) {
        if (i2 <= 10) {
            return Lazy.MH_arrayIdentity;
        }
        int i3 = i2 % 10;
        int i4 = i2 - i3;
        if (i3 == 0) {
            i3 = 10;
            i4 = i2 - 10;
            if (FILL_ARRAY_TO_RIGHT[i4] == null) {
                for (int i5 = 0; i5 < i4; i5 += 10) {
                    if (i5 > 10) {
                        fillToRight(i5);
                    }
                }
            }
        }
        if (i4 < 10) {
            i4 = 10;
            i3 = i2 - 10;
        }
        if (!$assertionsDisabled && i3 <= 0) {
            throw new AssertionError();
        }
        MethodHandle methodHandleFillToRight = fillToRight(i4);
        MethodHandle methodHandleBindTo = Lazy.FILL_ARRAYS[i3].bindTo(Integer.valueOf(i4));
        if (!$assertionsDisabled && methodHandleFillToRight.type().parameterCount() != (1 + i4) - 10) {
            throw new AssertionError();
        }
        if (!$assertionsDisabled && methodHandleBindTo.type().parameterCount() != 1 + i3) {
            throw new AssertionError();
        }
        if (i4 == 10) {
            return methodHandleBindTo;
        }
        return MethodHandles.collectArguments(methodHandleBindTo, 0, methodHandleFillToRight);
    }

    static MethodHandle varargsArray(Class<?> cls, int i2) {
        MethodHandle methodHandleBuildVarargsArray;
        Class<?> componentType = cls.getComponentType();
        if (componentType == null) {
            throw new IllegalArgumentException("not an array: " + ((Object) cls));
        }
        if (i2 >= 126) {
            int iStackSlots = i2;
            if (iStackSlots <= 254 && componentType.isPrimitive()) {
                iStackSlots *= Wrapper.forPrimitiveType(componentType).stackSlots();
            }
            if (iStackSlots > 254) {
                throw new IllegalArgumentException("too many arguments: " + cls.getSimpleName() + ", length " + i2);
            }
        }
        if (componentType == Object.class) {
            return varargsArray(i2);
        }
        MethodHandle[] methodHandleArr = TYPED_COLLECTORS.get(componentType);
        MethodHandle methodHandle = i2 < methodHandleArr.length ? methodHandleArr[i2] : null;
        if (methodHandle != null) {
            return methodHandle;
        }
        if (i2 == 0) {
            methodHandleBuildVarargsArray = MethodHandles.constant(cls, Array.newInstance(cls.getComponentType(), 0));
        } else if (componentType.isPrimitive()) {
            methodHandleBuildVarargsArray = buildVarargsArray(Lazy.MH_fillNewArray, buildArrayProducer(cls), i2);
        } else {
            methodHandleBuildVarargsArray = buildVarargsArray(Lazy.MH_fillNewTypedArray.bindTo(Arrays.copyOf(NO_ARGS_ARRAY, 0, cls.asSubclass(Object[].class))), Lazy.MH_arrayIdentity, i2);
        }
        MethodHandle methodHandleMakeIntrinsic = makeIntrinsic(methodHandleBuildVarargsArray.asType(MethodType.methodType(cls, (List<Class<?>>) Collections.nCopies(i2, componentType))), Intrinsic.NEW_ARRAY);
        if (!$assertionsDisabled && !assertCorrectArity(methodHandleMakeIntrinsic, i2)) {
            throw new AssertionError();
        }
        if (i2 < methodHandleArr.length) {
            methodHandleArr[i2] = methodHandleMakeIntrinsic;
        }
        return methodHandleMakeIntrinsic;
    }

    private static MethodHandle buildArrayProducer(Class<?> cls) {
        Class<?> componentType = cls.getComponentType();
        if ($assertionsDisabled || componentType.isPrimitive()) {
            return Lazy.MH_copyAsPrimitiveArray.bindTo(Wrapper.forPrimitiveType(componentType));
        }
        throw new AssertionError();
    }

    static void assertSame(Object obj, Object obj2) {
        if (obj != obj2) {
            throw MethodHandleStatics.newInternalError(String.format("mh1 != mh2: mh1 = %s (form: %s); mh2 = %s (form: %s)", obj, ((MethodHandle) obj).form, obj2, ((MethodHandle) obj2).form));
        }
    }
}
