package java.lang.invoke;

import com.sun.org.apache.xml.internal.dtm.DTMManager;
import java.lang.ref.SoftReference;
import sun.invoke.util.Wrapper;

/* loaded from: rt.jar:java/lang/invoke/MethodTypeForm.class */
final class MethodTypeForm {
    final int[] argToSlotTable;
    final int[] slotToArgTable;
    final long argCounts;
    final long primCounts;
    final MethodType erasedType;
    final MethodType basicType;

    @Stable
    final SoftReference<MethodHandle>[] methodHandles;
    static final int MH_BASIC_INV = 0;
    static final int MH_NF_INV = 1;
    static final int MH_UNINIT_CS = 2;
    static final int MH_LIMIT = 3;

    @Stable
    final SoftReference<LambdaForm>[] lambdaForms;
    static final int LF_INVVIRTUAL = 0;
    static final int LF_INVSTATIC = 1;
    static final int LF_INVSPECIAL = 2;
    static final int LF_NEWINVSPECIAL = 3;
    static final int LF_INVINTERFACE = 4;
    static final int LF_INVSTATIC_INIT = 5;
    static final int LF_INTERPRET = 6;
    static final int LF_REBIND = 7;
    static final int LF_DELEGATE = 8;
    static final int LF_DELEGATE_BLOCK_INLINING = 9;
    static final int LF_EX_LINKER = 10;
    static final int LF_EX_INVOKER = 11;
    static final int LF_GEN_LINKER = 12;
    static final int LF_GEN_INVOKER = 13;
    static final int LF_CS_LINKER = 14;
    static final int LF_MH_LINKER = 15;
    static final int LF_GWC = 16;
    static final int LF_GWT = 17;
    static final int LF_LIMIT = 18;
    public static final int NO_CHANGE = 0;
    public static final int ERASE = 1;
    public static final int WRAP = 2;
    public static final int UNWRAP = 3;
    public static final int INTS = 4;
    public static final int LONGS = 5;
    public static final int RAW_RETURN = 6;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !MethodTypeForm.class.desiredAssertionStatus();
    }

    public MethodType erasedType() {
        return this.erasedType;
    }

    public MethodType basicType() {
        return this.basicType;
    }

    private boolean assertIsBasicType() {
        if ($assertionsDisabled || this.erasedType == this.basicType) {
            return true;
        }
        throw new AssertionError((Object) ("erasedType: " + ((Object) this.erasedType) + " != basicType: " + ((Object) this.basicType)));
    }

    public MethodHandle cachedMethodHandle(int i2) {
        if (!$assertionsDisabled && !assertIsBasicType()) {
            throw new AssertionError();
        }
        SoftReference<MethodHandle> softReference = this.methodHandles[i2];
        if (softReference != null) {
            return softReference.get();
        }
        return null;
    }

    public synchronized MethodHandle setCachedMethodHandle(int i2, MethodHandle methodHandle) {
        MethodHandle methodHandle2;
        SoftReference<MethodHandle> softReference = this.methodHandles[i2];
        if (softReference != null && (methodHandle2 = softReference.get()) != null) {
            return methodHandle2;
        }
        this.methodHandles[i2] = new SoftReference<>(methodHandle);
        return methodHandle;
    }

    public LambdaForm cachedLambdaForm(int i2) {
        if (!$assertionsDisabled && !assertIsBasicType()) {
            throw new AssertionError();
        }
        SoftReference<LambdaForm> softReference = this.lambdaForms[i2];
        if (softReference != null) {
            return softReference.get();
        }
        return null;
    }

    public synchronized LambdaForm setCachedLambdaForm(int i2, LambdaForm lambdaForm) {
        LambdaForm lambdaForm2;
        SoftReference<LambdaForm> softReference = this.lambdaForms[i2];
        if (softReference != null && (lambdaForm2 = softReference.get()) != null) {
            return lambdaForm2;
        }
        this.lambdaForms[i2] = new SoftReference<>(lambdaForm);
        return lambdaForm;
    }

    protected MethodTypeForm(MethodType methodType) {
        int[] iArr;
        int[] iArr2;
        this.erasedType = methodType;
        Class<?>[] clsArrPtypes = methodType.ptypes();
        int length = clsArrPtypes.length;
        int i2 = 1;
        int i3 = 1;
        int i4 = 0;
        int i5 = 0;
        int i6 = 0;
        int i7 = 0;
        Class<?>[] clsArr = clsArrPtypes;
        for (int i8 = 0; i8 < clsArrPtypes.length; i8++) {
            Class<?> cls = clsArrPtypes[i8];
            if (cls != Object.class) {
                i4++;
                Wrapper wrapperForPrimitiveType = Wrapper.forPrimitiveType(cls);
                i5 = wrapperForPrimitiveType.isDoubleWord() ? i5 + 1 : i5;
                if (wrapperForPrimitiveType.isSubwordOrInt() && cls != Integer.TYPE) {
                    clsArr = clsArr == clsArrPtypes ? (Class[]) clsArr.clone() : clsArr;
                    clsArr[i8] = Integer.TYPE;
                }
            }
        }
        int i9 = length + i5;
        Class<?> clsReturnType = methodType.returnType();
        Class<?> cls2 = clsReturnType;
        if (clsReturnType != Object.class) {
            i6 = 0 + 1;
            Wrapper wrapperForPrimitiveType2 = Wrapper.forPrimitiveType(clsReturnType);
            i7 = wrapperForPrimitiveType2.isDoubleWord() ? 0 + 1 : i7;
            if (wrapperForPrimitiveType2.isSubwordOrInt() && clsReturnType != Integer.TYPE) {
                cls2 = Integer.TYPE;
            }
            if (clsReturnType == Void.TYPE) {
                i3 = 0;
                i2 = 0;
            } else {
                i3 = 1 + i7;
            }
        }
        if (clsArrPtypes == clsArr && cls2 == clsReturnType) {
            this.basicType = methodType;
            if (i5 != 0) {
                int i10 = length + i5;
                iArr = new int[i10 + 1];
                iArr2 = new int[1 + length];
                iArr2[0] = i10;
                for (int i11 = 0; i11 < clsArrPtypes.length; i11++) {
                    if (Wrapper.forBasicType(clsArrPtypes[i11]).isDoubleWord()) {
                        i10--;
                    }
                    i10--;
                    iArr[i10] = i11 + 1;
                    iArr2[1 + i11] = i10;
                }
                if (!$assertionsDisabled && i10 != 0) {
                    throw new AssertionError();
                }
            } else if (i4 != 0) {
                if (!$assertionsDisabled && length != i9) {
                    throw new AssertionError();
                }
                MethodTypeForm methodTypeFormForm = MethodType.genericMethodType(length).form();
                if (!$assertionsDisabled && this == methodTypeFormForm) {
                    throw new AssertionError();
                }
                iArr = methodTypeFormForm.slotToArgTable;
                iArr2 = methodTypeFormForm.argToSlotTable;
            } else {
                int i12 = length;
                iArr = new int[i12 + 1];
                iArr2 = new int[1 + length];
                iArr2[0] = i12;
                for (int i13 = 0; i13 < length; i13++) {
                    i12--;
                    iArr[i12] = i13 + 1;
                    iArr2[1 + i13] = i12;
                }
            }
            this.primCounts = pack(i7, i6, i5, i4);
            this.argCounts = pack(i3, i2, i9, length);
            this.argToSlotTable = iArr2;
            this.slotToArgTable = iArr;
            if (i9 >= 256) {
                throw MethodHandleStatics.newIllegalArgumentException("too many arguments");
            }
            if (!$assertionsDisabled && this.basicType != methodType) {
                throw new AssertionError();
            }
            this.lambdaForms = new SoftReference[18];
            this.methodHandles = new SoftReference[3];
            return;
        }
        this.basicType = MethodType.makeImpl(cls2, clsArr, true);
        MethodTypeForm methodTypeFormForm2 = this.basicType.form();
        if (!$assertionsDisabled && this == methodTypeFormForm2) {
            throw new AssertionError();
        }
        this.primCounts = methodTypeFormForm2.primCounts;
        this.argCounts = methodTypeFormForm2.argCounts;
        this.argToSlotTable = methodTypeFormForm2.argToSlotTable;
        this.slotToArgTable = methodTypeFormForm2.slotToArgTable;
        this.methodHandles = null;
        this.lambdaForms = null;
    }

    private static long pack(int i2, int i3, int i4, int i5) {
        if (!$assertionsDisabled && ((i2 | i3 | i4 | i5) & DTMManager.IDENT_DTM_DEFAULT) != 0) {
            throw new AssertionError();
        }
        return (((i2 << 16) | i3) << 32) | (i4 << 16) | i5;
    }

    private static char unpack(long j2, int i2) {
        if ($assertionsDisabled || i2 <= 3) {
            return (char) (j2 >> ((3 - i2) * 16));
        }
        throw new AssertionError();
    }

    public int parameterCount() {
        return unpack(this.argCounts, 3);
    }

    public int parameterSlotCount() {
        return unpack(this.argCounts, 2);
    }

    public int returnCount() {
        return unpack(this.argCounts, 1);
    }

    public int returnSlotCount() {
        return unpack(this.argCounts, 0);
    }

    public int primitiveParameterCount() {
        return unpack(this.primCounts, 3);
    }

    public int longPrimitiveParameterCount() {
        return unpack(this.primCounts, 2);
    }

    public int primitiveReturnCount() {
        return unpack(this.primCounts, 1);
    }

    public int longPrimitiveReturnCount() {
        return unpack(this.primCounts, 0);
    }

    public boolean hasPrimitives() {
        return this.primCounts != 0;
    }

    public boolean hasNonVoidPrimitives() {
        if (this.primCounts == 0) {
            return false;
        }
        if (primitiveParameterCount() != 0) {
            return true;
        }
        return (primitiveReturnCount() == 0 || returnCount() == 0) ? false : true;
    }

    public boolean hasLongPrimitives() {
        return (longPrimitiveParameterCount() | longPrimitiveReturnCount()) != 0;
    }

    public int parameterToArgSlot(int i2) {
        return this.argToSlotTable[1 + i2];
    }

    public int argSlotToParameter(int i2) {
        return this.slotToArgTable[i2] - 1;
    }

    static MethodTypeForm findForm(MethodType methodType) {
        MethodType methodTypeCanonicalize = canonicalize(methodType, 1, 1);
        if (methodTypeCanonicalize == null) {
            return new MethodTypeForm(methodType);
        }
        return methodTypeCanonicalize.form();
    }

    public static MethodType canonicalize(MethodType methodType, int i2, int i3) {
        Class<?>[] clsArrPtypes = methodType.ptypes();
        Class<?>[] clsArrCanonicalizeAll = canonicalizeAll(clsArrPtypes, i3);
        Class<?> clsReturnType = methodType.returnType();
        Class<?> clsCanonicalize = canonicalize(clsReturnType, i2);
        if (clsArrCanonicalizeAll == null && clsCanonicalize == null) {
            return null;
        }
        if (clsCanonicalize == null) {
            clsCanonicalize = clsReturnType;
        }
        if (clsArrCanonicalizeAll == null) {
            clsArrCanonicalizeAll = clsArrPtypes;
        }
        return MethodType.makeImpl(clsCanonicalize, clsArrCanonicalizeAll, true);
    }

    static Class<?> canonicalize(Class<?> cls, int i2) {
        if (cls != Object.class) {
            if (!cls.isPrimitive()) {
                switch (i2) {
                    case 1:
                    case 6:
                        return Object.class;
                    case 3:
                        Class<?> clsAsPrimitiveType = Wrapper.asPrimitiveType(cls);
                        if (clsAsPrimitiveType != cls) {
                            return clsAsPrimitiveType;
                        }
                        return null;
                    default:
                        return null;
                }
            }
            if (cls == Void.TYPE) {
                switch (i2) {
                    case 2:
                        return Void.class;
                    case 6:
                        return Integer.TYPE;
                    default:
                        return null;
                }
            }
            switch (i2) {
                case 2:
                    return Wrapper.asWrapperType(cls);
                case 3:
                default:
                    return null;
                case 4:
                    if (cls == Integer.TYPE || cls == Long.TYPE) {
                        return null;
                    }
                    if (cls == Double.TYPE) {
                        return Long.TYPE;
                    }
                    return Integer.TYPE;
                case 5:
                    if (cls == Long.TYPE) {
                        return null;
                    }
                    return Long.TYPE;
                case 6:
                    if (cls == Integer.TYPE || cls == Long.TYPE || cls == Float.TYPE || cls == Double.TYPE) {
                        return null;
                    }
                    return Integer.TYPE;
            }
        }
        return null;
    }

    static Class<?>[] canonicalizeAll(Class<?>[] clsArr, int i2) {
        Class<?>[] clsArr2 = null;
        int length = clsArr.length;
        for (int i3 = 0; i3 < length; i3++) {
            Class<?> clsCanonicalize = canonicalize(clsArr[i3], i2);
            if (clsCanonicalize == Void.TYPE) {
                clsCanonicalize = null;
            }
            if (clsCanonicalize != null) {
                if (clsArr2 == null) {
                    clsArr2 = (Class[]) clsArr.clone();
                }
                clsArr2[i3] = clsCanonicalize;
            }
        }
        return clsArr2;
    }

    public String toString() {
        return "Form" + ((Object) this.erasedType);
    }
}
