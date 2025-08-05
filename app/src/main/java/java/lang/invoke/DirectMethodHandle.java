package java.lang.invoke;

import com.sun.org.apache.bcel.internal.Constants;
import java.lang.invoke.LambdaForm;
import java.lang.invoke.MemberName;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import sun.invoke.util.ValueConversions;
import sun.invoke.util.VerifyAccess;
import sun.invoke.util.VerifyType;
import sun.invoke.util.Wrapper;
import sun.misc.Unsafe;

/* loaded from: rt.jar:java/lang/invoke/DirectMethodHandle.class */
class DirectMethodHandle extends MethodHandle {
    final MemberName member;
    private static final MemberName.Factory IMPL_NAMES;
    private static byte AF_GETFIELD;
    private static byte AF_PUTFIELD;
    private static byte AF_GETSTATIC;
    private static byte AF_PUTSTATIC;
    private static byte AF_GETSTATIC_INIT;
    private static byte AF_PUTSTATIC_INIT;
    private static byte AF_LIMIT;
    private static int FT_LAST_WRAPPER;
    private static int FT_UNCHECKED_REF;
    private static int FT_CHECKED_REF;
    private static int FT_LIMIT;
    private static final LambdaForm[] ACCESSOR_FORMS;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !DirectMethodHandle.class.desiredAssertionStatus();
        IMPL_NAMES = MemberName.getFactory();
        AF_GETFIELD = (byte) 0;
        AF_PUTFIELD = (byte) 1;
        AF_GETSTATIC = (byte) 2;
        AF_PUTSTATIC = (byte) 3;
        AF_GETSTATIC_INIT = (byte) 4;
        AF_PUTSTATIC_INIT = (byte) 5;
        AF_LIMIT = (byte) 6;
        FT_LAST_WRAPPER = Wrapper.values().length - 1;
        FT_UNCHECKED_REF = Wrapper.OBJECT.ordinal();
        FT_CHECKED_REF = FT_LAST_WRAPPER + 1;
        FT_LIMIT = FT_LAST_WRAPPER + 2;
        ACCESSOR_FORMS = new LambdaForm[afIndex(AF_LIMIT, false, 0)];
    }

    private DirectMethodHandle(MethodType methodType, LambdaForm lambdaForm, MemberName memberName) {
        super(methodType, lambdaForm);
        if (!memberName.isResolved()) {
            throw new InternalError();
        }
        if (memberName.getDeclaringClass().isInterface() && memberName.isMethod() && !memberName.isAbstract()) {
            MemberName memberName2 = new MemberName((Class<?>) Object.class, memberName.getName(), memberName.getMethodType(), memberName.getReferenceKind());
            MemberName memberNameResolveOrNull = MemberName.getFactory().resolveOrNull(memberName2.getReferenceKind(), memberName2, null);
            if (memberNameResolveOrNull != null && memberNameResolveOrNull.isPublic()) {
                if (!$assertionsDisabled && memberName.getReferenceKind() != memberNameResolveOrNull.getReferenceKind()) {
                    throw new AssertionError();
                }
                memberName = memberNameResolveOrNull;
            }
        }
        this.member = memberName;
    }

    static DirectMethodHandle make(byte b2, Class<?> cls, MemberName memberName) {
        MethodType methodOrFieldType = memberName.getMethodOrFieldType();
        if (!memberName.isStatic()) {
            if (!memberName.getDeclaringClass().isAssignableFrom(cls) || memberName.isConstructor()) {
                throw new InternalError(memberName.toString());
            }
            methodOrFieldType = methodOrFieldType.insertParameterTypes(0, cls);
        }
        if (!memberName.isField()) {
            switch (b2) {
                case 7:
                    MemberName memberNameAsSpecial = memberName.asSpecial();
                    return new Special(methodOrFieldType, preparedLambdaForm(memberNameAsSpecial), memberNameAsSpecial);
                case 9:
                    return new Interface(methodOrFieldType, preparedLambdaForm(memberName), memberName, cls);
                default:
                    return new DirectMethodHandle(methodOrFieldType, preparedLambdaForm(memberName), memberName);
            }
        }
        LambdaForm lambdaFormPreparedFieldLambdaForm = preparedFieldLambdaForm(memberName);
        if (memberName.isStatic()) {
            return new StaticAccessor(methodOrFieldType, lambdaFormPreparedFieldLambdaForm, memberName, MethodHandleNatives.staticFieldBase(memberName), MethodHandleNatives.staticFieldOffset(memberName));
        }
        long jObjectFieldOffset = MethodHandleNatives.objectFieldOffset(memberName);
        if ($assertionsDisabled || jObjectFieldOffset == ((int) jObjectFieldOffset)) {
            return new Accessor(methodOrFieldType, lambdaFormPreparedFieldLambdaForm, memberName, (int) jObjectFieldOffset);
        }
        throw new AssertionError();
    }

    static DirectMethodHandle make(Class<?> cls, MemberName memberName) {
        byte referenceKind = memberName.getReferenceKind();
        if (referenceKind == 7) {
            referenceKind = 5;
        }
        return make(referenceKind, cls, memberName);
    }

    static DirectMethodHandle make(MemberName memberName) {
        if (memberName.isConstructor()) {
            return makeAllocator(memberName);
        }
        return make(memberName.getDeclaringClass(), memberName);
    }

    static DirectMethodHandle make(Method method) {
        return make(method.getDeclaringClass(), new MemberName(method));
    }

    static DirectMethodHandle make(Field field) {
        return make(field.getDeclaringClass(), new MemberName(field));
    }

    private static DirectMethodHandle makeAllocator(MemberName memberName) {
        if (!$assertionsDisabled && (!memberName.isConstructor() || !memberName.getName().equals(Constants.CONSTRUCTOR_NAME))) {
            throw new AssertionError();
        }
        Class<?> declaringClass = memberName.getDeclaringClass();
        MemberName memberNameAsConstructor = memberName.asConstructor();
        if (!$assertionsDisabled && (!memberNameAsConstructor.isConstructor() || memberNameAsConstructor.getReferenceKind() != 8)) {
            throw new AssertionError(memberNameAsConstructor);
        }
        MethodType methodTypeChangeReturnType = memberNameAsConstructor.getMethodType().changeReturnType(declaringClass);
        LambdaForm lambdaFormPreparedLambdaForm = preparedLambdaForm(memberNameAsConstructor);
        MemberName memberNameAsSpecial = memberNameAsConstructor.asSpecial();
        if ($assertionsDisabled || memberNameAsSpecial.getMethodType().returnType() == Void.TYPE) {
            return new Constructor(methodTypeChangeReturnType, lambdaFormPreparedLambdaForm, memberNameAsConstructor, memberNameAsSpecial, declaringClass);
        }
        throw new AssertionError();
    }

    @Override // java.lang.invoke.MethodHandle
    BoundMethodHandle rebind() {
        return BoundMethodHandle.makeReinvoker(this);
    }

    @Override // java.lang.invoke.MethodHandle
    MethodHandle copyWith(MethodType methodType, LambdaForm lambdaForm) {
        if ($assertionsDisabled || getClass() == DirectMethodHandle.class) {
            return new DirectMethodHandle(methodType, lambdaForm, this.member);
        }
        throw new AssertionError();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // java.lang.invoke.MethodHandle
    public String internalProperties() {
        return "\n& DMH.MN=" + ((Object) internalMemberName());
    }

    @Override // java.lang.invoke.MethodHandle
    @ForceInline
    MemberName internalMemberName() {
        return this.member;
    }

    private static LambdaForm preparedLambdaForm(MemberName memberName) {
        int i2;
        if (!$assertionsDisabled && !memberName.isInvocable()) {
            throw new AssertionError(memberName);
        }
        MethodType methodTypeBasicType = memberName.getInvocationType().basicType();
        if (!$assertionsDisabled && memberName.isMethodHandleInvoke()) {
            throw new AssertionError(memberName);
        }
        switch (memberName.getReferenceKind()) {
            case 5:
                i2 = 0;
                break;
            case 6:
                i2 = 1;
                break;
            case 7:
                i2 = 2;
                break;
            case 8:
                i2 = 3;
                break;
            case 9:
                i2 = 4;
                break;
            default:
                throw new InternalError(memberName.toString());
        }
        if (i2 == 1 && shouldBeInitialized(memberName)) {
            preparedLambdaForm(methodTypeBasicType, i2);
            i2 = 5;
        }
        LambdaForm lambdaFormPreparedLambdaForm = preparedLambdaForm(methodTypeBasicType, i2);
        maybeCompile(lambdaFormPreparedLambdaForm, memberName);
        if (!$assertionsDisabled && !lambdaFormPreparedLambdaForm.methodType().dropParameterTypes(0, 1).equals((Object) memberName.getInvocationType().basicType())) {
            throw new AssertionError(Arrays.asList(memberName, memberName.getInvocationType().basicType(), lambdaFormPreparedLambdaForm, lambdaFormPreparedLambdaForm.methodType()));
        }
        return lambdaFormPreparedLambdaForm;
    }

    private static LambdaForm preparedLambdaForm(MethodType methodType, int i2) {
        LambdaForm lambdaFormCachedLambdaForm = methodType.form().cachedLambdaForm(i2);
        if (lambdaFormCachedLambdaForm != null) {
            return lambdaFormCachedLambdaForm;
        }
        return methodType.form().setCachedLambdaForm(i2, makePreparedLambdaForm(methodType, i2));
    }

    private static LambdaForm makePreparedLambdaForm(MethodType methodType, int i2) {
        String str;
        String str2;
        int i3;
        int i4;
        boolean z2 = i2 == 5;
        boolean z3 = i2 == 3;
        boolean z4 = i2 == 4;
        switch (i2) {
            case 0:
                str = "linkToVirtual";
                str2 = "DMH.invokeVirtual";
                break;
            case 1:
                str = "linkToStatic";
                str2 = "DMH.invokeStatic";
                break;
            case 2:
                str = "linkToSpecial";
                str2 = "DMH.invokeSpecial";
                break;
            case 3:
                str = "linkToSpecial";
                str2 = "DMH.newInvokeSpecial";
                break;
            case 4:
                str = "linkToInterface";
                str2 = "DMH.invokeInterface";
                break;
            case 5:
                str = "linkToStatic";
                str2 = "DMH.invokeStaticInit";
                break;
            default:
                throw new InternalError("which=" + i2);
        }
        MethodType methodTypeAppendParameterTypes = methodType.appendParameterTypes(MemberName.class);
        if (z3) {
            methodTypeAppendParameterTypes = methodTypeAppendParameterTypes.insertParameterTypes(0, Object.class).changeReturnType(Void.TYPE);
        }
        try {
            MemberName memberNameResolveOrFail = IMPL_NAMES.resolveOrFail((byte) 6, new MemberName((Class<?>) MethodHandle.class, str, methodTypeAppendParameterTypes, (byte) 6), null, NoSuchMethodException.class);
            int iParameterCount = 1 + methodType.parameterCount();
            int i5 = iParameterCount;
            if (z3) {
                i3 = i5;
                i5++;
            } else {
                i3 = -1;
            }
            int i6 = i3;
            int i7 = i5;
            int i8 = i5 + 1;
            if (z4) {
                i4 = i8;
                i8++;
            } else {
                i4 = -1;
            }
            int i9 = i4;
            int i10 = i8;
            int i11 = i8 + 1;
            LambdaForm.Name[] nameArrArguments = LambdaForm.arguments(i11 - iParameterCount, methodType.invokerType());
            if (!$assertionsDisabled && nameArrArguments.length != i11) {
                throw new AssertionError();
            }
            if (z3) {
                nameArrArguments[i6] = new LambdaForm.Name(Lazy.NF_allocateInstance, nameArrArguments[0]);
                nameArrArguments[i7] = new LambdaForm.Name(Lazy.NF_constructorMethod, nameArrArguments[0]);
            } else if (z2) {
                nameArrArguments[i7] = new LambdaForm.Name(Lazy.NF_internalMemberNameEnsureInit, nameArrArguments[0]);
            } else {
                nameArrArguments[i7] = new LambdaForm.Name(Lazy.NF_internalMemberName, nameArrArguments[0]);
            }
            if (!$assertionsDisabled && findDirectMethodHandle(nameArrArguments[i7]) != nameArrArguments[0]) {
                throw new AssertionError();
            }
            Object[] objArrCopyOfRange = Arrays.copyOfRange(nameArrArguments, 1, i7 + 1, Object[].class);
            if (z4) {
                nameArrArguments[i9] = new LambdaForm.Name(Lazy.NF_checkReceiver, nameArrArguments[0], nameArrArguments[1]);
                objArrCopyOfRange[0] = nameArrArguments[i9];
            }
            if (!$assertionsDisabled && objArrCopyOfRange[objArrCopyOfRange.length - 1] != nameArrArguments[i7]) {
                throw new AssertionError();
            }
            int i12 = -2;
            if (z3) {
                if (!$assertionsDisabled && objArrCopyOfRange[objArrCopyOfRange.length - 2] != nameArrArguments[i6]) {
                    throw new AssertionError();
                }
                System.arraycopy(objArrCopyOfRange, 0, objArrCopyOfRange, 1, objArrCopyOfRange.length - 2);
                objArrCopyOfRange[0] = nameArrArguments[i6];
                i12 = i6;
            }
            nameArrArguments[i10] = new LambdaForm.Name(memberNameResolveOrFail, objArrCopyOfRange);
            LambdaForm lambdaForm = new LambdaForm(str2 + "_" + LambdaForm.shortenSignature(LambdaForm.basicTypeSignature(methodType)), iParameterCount, nameArrArguments, i12);
            lambdaForm.compileToBytecode();
            return lambdaForm;
        } catch (ReflectiveOperationException e2) {
            throw MethodHandleStatics.newInternalError(e2);
        }
    }

    static Object findDirectMethodHandle(LambdaForm.Name name) {
        if (name.function == Lazy.NF_internalMemberName || name.function == Lazy.NF_internalMemberNameEnsureInit || name.function == Lazy.NF_constructorMethod) {
            if ($assertionsDisabled || name.arguments.length == 1) {
                return name.arguments[0];
            }
            throw new AssertionError();
        }
        return null;
    }

    private static void maybeCompile(LambdaForm lambdaForm, MemberName memberName) {
        if (VerifyAccess.isSamePackage(memberName.getDeclaringClass(), MethodHandle.class)) {
            lambdaForm.compileToBytecode();
        }
    }

    @ForceInline
    static Object internalMemberName(Object obj) {
        return ((DirectMethodHandle) obj).member;
    }

    static Object internalMemberNameEnsureInit(Object obj) {
        DirectMethodHandle directMethodHandle = (DirectMethodHandle) obj;
        directMethodHandle.ensureInitialized();
        return directMethodHandle.member;
    }

    static boolean shouldBeInitialized(MemberName memberName) {
        switch (memberName.getReferenceKind()) {
            case 2:
            case 4:
            case 6:
            case 8:
                Class<?> declaringClass = memberName.getDeclaringClass();
                if (declaringClass == ValueConversions.class || declaringClass == MethodHandleImpl.class || declaringClass == Invokers.class) {
                    return false;
                }
                if (VerifyAccess.isSamePackage(MethodHandle.class, declaringClass) || VerifyAccess.isSamePackage(ValueConversions.class, declaringClass)) {
                    if (MethodHandleStatics.UNSAFE.shouldBeInitialized(declaringClass)) {
                        MethodHandleStatics.UNSAFE.ensureClassInitialized(declaringClass);
                        return false;
                    }
                    return false;
                }
                return MethodHandleStatics.UNSAFE.shouldBeInitialized(declaringClass);
            case 3:
            case 5:
            case 7:
            default:
                return false;
        }
    }

    /* loaded from: rt.jar:java/lang/invoke/DirectMethodHandle$EnsureInitialized.class */
    private static class EnsureInitialized extends ClassValue<WeakReference<Thread>> {
        static final EnsureInitialized INSTANCE = new EnsureInitialized();

        private EnsureInitialized() {
        }

        @Override // java.lang.ClassValue
        protected /* bridge */ /* synthetic */ WeakReference<Thread> computeValue(Class cls) {
            return computeValue((Class<?>) cls);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.lang.ClassValue
        protected WeakReference<Thread> computeValue(Class<?> cls) {
            MethodHandleStatics.UNSAFE.ensureClassInitialized(cls);
            if (MethodHandleStatics.UNSAFE.shouldBeInitialized(cls)) {
                return new WeakReference<>(Thread.currentThread());
            }
            return null;
        }
    }

    private void ensureInitialized() {
        if (checkInitialized(this.member)) {
            if (this.member.isField()) {
                updateForm(preparedFieldLambdaForm(this.member));
            } else {
                updateForm(preparedLambdaForm(this.member));
            }
        }
    }

    private static boolean checkInitialized(MemberName memberName) {
        Class<?> declaringClass = memberName.getDeclaringClass();
        WeakReference<Thread> weakReference = EnsureInitialized.INSTANCE.get(declaringClass);
        if (weakReference == null) {
            return true;
        }
        if (weakReference.get() == Thread.currentThread()) {
            if (MethodHandleStatics.UNSAFE.shouldBeInitialized(declaringClass)) {
                return false;
            }
        } else {
            MethodHandleStatics.UNSAFE.ensureClassInitialized(declaringClass);
        }
        if (!$assertionsDisabled && MethodHandleStatics.UNSAFE.shouldBeInitialized(declaringClass)) {
            throw new AssertionError();
        }
        EnsureInitialized.INSTANCE.remove(declaringClass);
        return true;
    }

    static void ensureInitialized(Object obj) {
        ((DirectMethodHandle) obj).ensureInitialized();
    }

    /* loaded from: rt.jar:java/lang/invoke/DirectMethodHandle$Special.class */
    static class Special extends DirectMethodHandle {
        @Override // java.lang.invoke.DirectMethodHandle, java.lang.invoke.MethodHandle
        /* bridge */ /* synthetic */ Object internalProperties() {
            return super.internalProperties();
        }

        private Special(MethodType methodType, LambdaForm lambdaForm, MemberName memberName) {
            super(methodType, lambdaForm, memberName);
        }

        @Override // java.lang.invoke.MethodHandle
        boolean isInvokeSpecial() {
            return true;
        }

        @Override // java.lang.invoke.DirectMethodHandle, java.lang.invoke.MethodHandle
        MethodHandle copyWith(MethodType methodType, LambdaForm lambdaForm) {
            return new Special(methodType, lambdaForm, this.member);
        }
    }

    /* loaded from: rt.jar:java/lang/invoke/DirectMethodHandle$Interface.class */
    static class Interface extends DirectMethodHandle {
        private final Class<?> refc;
        static final /* synthetic */ boolean $assertionsDisabled;

        @Override // java.lang.invoke.DirectMethodHandle, java.lang.invoke.MethodHandle
        /* bridge */ /* synthetic */ Object internalProperties() {
            return super.internalProperties();
        }

        static {
            $assertionsDisabled = !DirectMethodHandle.class.desiredAssertionStatus();
        }

        private Interface(MethodType methodType, LambdaForm lambdaForm, MemberName memberName, Class<?> cls) {
            super(methodType, lambdaForm, memberName);
            if (!$assertionsDisabled && !cls.isInterface()) {
                throw new AssertionError(cls);
            }
            this.refc = cls;
        }

        @Override // java.lang.invoke.DirectMethodHandle, java.lang.invoke.MethodHandle
        MethodHandle copyWith(MethodType methodType, LambdaForm lambdaForm) {
            return new Interface(methodType, lambdaForm, this.member, this.refc);
        }

        Object checkReceiver(Object obj) {
            if (!this.refc.isInstance(obj)) {
                throw new IncompatibleClassChangeError(String.format("Class %s does not implement the requested interface %s", obj.getClass().getName(), this.refc.getName()));
            }
            return obj;
        }
    }

    /* loaded from: rt.jar:java/lang/invoke/DirectMethodHandle$Constructor.class */
    static class Constructor extends DirectMethodHandle {
        final MemberName initMethod;
        final Class<?> instanceClass;
        static final /* synthetic */ boolean $assertionsDisabled;

        @Override // java.lang.invoke.DirectMethodHandle, java.lang.invoke.MethodHandle
        /* bridge */ /* synthetic */ Object internalProperties() {
            return super.internalProperties();
        }

        static {
            $assertionsDisabled = !DirectMethodHandle.class.desiredAssertionStatus();
        }

        private Constructor(MethodType methodType, LambdaForm lambdaForm, MemberName memberName, MemberName memberName2, Class<?> cls) {
            super(methodType, lambdaForm, memberName);
            this.initMethod = memberName2;
            this.instanceClass = cls;
            if (!$assertionsDisabled && !memberName2.isResolved()) {
                throw new AssertionError();
            }
        }

        @Override // java.lang.invoke.DirectMethodHandle, java.lang.invoke.MethodHandle
        MethodHandle copyWith(MethodType methodType, LambdaForm lambdaForm) {
            return new Constructor(methodType, lambdaForm, this.member, this.initMethod, this.instanceClass);
        }
    }

    static Object constructorMethod(Object obj) {
        return ((Constructor) obj).initMethod;
    }

    static Object allocateInstance(Object obj) throws InstantiationException {
        return MethodHandleStatics.UNSAFE.allocateInstance(((Constructor) obj).instanceClass);
    }

    /* loaded from: rt.jar:java/lang/invoke/DirectMethodHandle$Accessor.class */
    static class Accessor extends DirectMethodHandle {
        final Class<?> fieldType;
        final int fieldOffset;

        @Override // java.lang.invoke.DirectMethodHandle, java.lang.invoke.MethodHandle
        /* bridge */ /* synthetic */ Object internalProperties() {
            return super.internalProperties();
        }

        private Accessor(MethodType methodType, LambdaForm lambdaForm, MemberName memberName, int i2) {
            super(methodType, lambdaForm, memberName);
            this.fieldType = memberName.getFieldType();
            this.fieldOffset = i2;
        }

        @Override // java.lang.invoke.DirectMethodHandle
        Object checkCast(Object obj) {
            return this.fieldType.cast(obj);
        }

        @Override // java.lang.invoke.DirectMethodHandle, java.lang.invoke.MethodHandle
        MethodHandle copyWith(MethodType methodType, LambdaForm lambdaForm) {
            return new Accessor(methodType, lambdaForm, this.member, this.fieldOffset);
        }
    }

    @ForceInline
    static long fieldOffset(Object obj) {
        return ((Accessor) obj).fieldOffset;
    }

    @ForceInline
    static Object checkBase(Object obj) {
        obj.getClass();
        return obj;
    }

    /* loaded from: rt.jar:java/lang/invoke/DirectMethodHandle$StaticAccessor.class */
    static class StaticAccessor extends DirectMethodHandle {
        private final Class<?> fieldType;
        private final Object staticBase;
        private final long staticOffset;

        @Override // java.lang.invoke.DirectMethodHandle, java.lang.invoke.MethodHandle
        /* bridge */ /* synthetic */ Object internalProperties() {
            return super.internalProperties();
        }

        private StaticAccessor(MethodType methodType, LambdaForm lambdaForm, MemberName memberName, Object obj, long j2) {
            super(methodType, lambdaForm, memberName);
            this.fieldType = memberName.getFieldType();
            this.staticBase = obj;
            this.staticOffset = j2;
        }

        @Override // java.lang.invoke.DirectMethodHandle
        Object checkCast(Object obj) {
            return this.fieldType.cast(obj);
        }

        @Override // java.lang.invoke.DirectMethodHandle, java.lang.invoke.MethodHandle
        MethodHandle copyWith(MethodType methodType, LambdaForm lambdaForm) {
            return new StaticAccessor(methodType, lambdaForm, this.member, this.staticBase, this.staticOffset);
        }
    }

    @ForceInline
    static Object nullCheck(Object obj) {
        obj.getClass();
        return obj;
    }

    @ForceInline
    static Object staticBase(Object obj) {
        return ((StaticAccessor) obj).staticBase;
    }

    @ForceInline
    static long staticOffset(Object obj) {
        return ((StaticAccessor) obj).staticOffset;
    }

    @ForceInline
    static Object checkCast(Object obj, Object obj2) {
        return ((DirectMethodHandle) obj).checkCast(obj2);
    }

    Object checkCast(Object obj) {
        return this.member.getReturnType().cast(obj);
    }

    private static int afIndex(byte b2, boolean z2, int i2) {
        return (b2 * FT_LIMIT * 2) + (z2 ? FT_LIMIT : 0) + i2;
    }

    private static int ftypeKind(Class<?> cls) {
        if (cls.isPrimitive()) {
            return Wrapper.forPrimitiveType(cls).ordinal();
        }
        if (VerifyType.isNullReferenceConversion(Object.class, cls)) {
            return FT_UNCHECKED_REF;
        }
        return FT_CHECKED_REF;
    }

    private static LambdaForm preparedFieldLambdaForm(MemberName memberName) {
        byte b2;
        Class<?> fieldType = memberName.getFieldType();
        boolean zIsVolatile = memberName.isVolatile();
        switch (memberName.getReferenceKind()) {
            case 1:
                b2 = AF_GETFIELD;
                break;
            case 2:
                b2 = AF_GETSTATIC;
                break;
            case 3:
                b2 = AF_PUTFIELD;
                break;
            case 4:
                b2 = AF_PUTSTATIC;
                break;
            default:
                throw new InternalError(memberName.toString());
        }
        if (shouldBeInitialized(memberName)) {
            preparedFieldLambdaForm(b2, zIsVolatile, fieldType);
            if (!$assertionsDisabled && AF_GETSTATIC_INIT - AF_GETSTATIC != AF_PUTSTATIC_INIT - AF_PUTSTATIC) {
                throw new AssertionError();
            }
            b2 = (byte) (b2 + (AF_GETSTATIC_INIT - AF_GETSTATIC));
        }
        LambdaForm lambdaFormPreparedFieldLambdaForm = preparedFieldLambdaForm(b2, zIsVolatile, fieldType);
        maybeCompile(lambdaFormPreparedFieldLambdaForm, memberName);
        if (!$assertionsDisabled && !lambdaFormPreparedFieldLambdaForm.methodType().dropParameterTypes(0, 1).equals((Object) memberName.getInvocationType().basicType())) {
            throw new AssertionError(Arrays.asList(memberName, memberName.getInvocationType().basicType(), lambdaFormPreparedFieldLambdaForm, lambdaFormPreparedFieldLambdaForm.methodType()));
        }
        return lambdaFormPreparedFieldLambdaForm;
    }

    private static LambdaForm preparedFieldLambdaForm(byte b2, boolean z2, Class<?> cls) {
        int iAfIndex = afIndex(b2, z2, ftypeKind(cls));
        LambdaForm lambdaForm = ACCESSOR_FORMS[iAfIndex];
        if (lambdaForm != null) {
            return lambdaForm;
        }
        LambdaForm lambdaFormMakePreparedFieldLambdaForm = makePreparedFieldLambdaForm(b2, z2, ftypeKind(cls));
        ACCESSOR_FORMS[iAfIndex] = lambdaFormMakePreparedFieldLambdaForm;
        return lambdaFormMakePreparedFieldLambdaForm;
    }

    private static LambdaForm makePreparedFieldLambdaForm(byte b2, boolean z2, int i2) {
        MethodType methodType;
        MethodType methodType2;
        int i3;
        int i4;
        int i5;
        int i6;
        int i7;
        boolean z3 = (b2 & 1) == (AF_GETFIELD & 1);
        boolean z4 = b2 >= AF_GETSTATIC;
        boolean z5 = b2 >= AF_GETSTATIC_INIT;
        boolean z6 = i2 == FT_CHECKED_REF;
        Wrapper wrapper = z6 ? Wrapper.OBJECT : Wrapper.values()[i2];
        Class<?> clsPrimitiveType = wrapper.primitiveType();
        if (!$assertionsDisabled) {
            if (ftypeKind(z6 ? String.class : clsPrimitiveType) != i2) {
                throw new AssertionError();
            }
        }
        String strPrimitiveSimpleName = wrapper.primitiveSimpleName();
        String str = Character.toUpperCase(strPrimitiveSimpleName.charAt(0)) + strPrimitiveSimpleName.substring(1);
        if (z2) {
            str = str + "Volatile";
        }
        String str2 = (z3 ? "get" : "put") + str;
        if (z3) {
            methodType = MethodType.methodType(clsPrimitiveType, Object.class, Long.TYPE);
        } else {
            methodType = MethodType.methodType(Void.TYPE, Object.class, Long.TYPE, clsPrimitiveType);
        }
        try {
            MemberName memberNameResolveOrFail = IMPL_NAMES.resolveOrFail((byte) 5, new MemberName((Class<?>) Unsafe.class, str2, methodType, (byte) 5), null, NoSuchMethodException.class);
            if (z3) {
                methodType2 = MethodType.methodType(clsPrimitiveType);
            } else {
                methodType2 = MethodType.methodType(Void.TYPE, clsPrimitiveType);
            }
            MethodType methodTypeBasicType = methodType2.basicType();
            if (!z4) {
                methodTypeBasicType = methodTypeBasicType.insertParameterTypes(0, Object.class);
            }
            int iParameterCount = 1 + methodTypeBasicType.parameterCount();
            char c2 = z4 ? (char) 65535 : (char) 1;
            int i8 = z3 ? -1 : iParameterCount - 1;
            int i9 = iParameterCount;
            if (z4) {
                i3 = i9;
                i9++;
            } else {
                i3 = -1;
            }
            int i10 = i3;
            int i11 = i9;
            int i12 = i9 + 1;
            if (c2 >= 0) {
                i4 = i12;
                i12++;
            } else {
                i4 = -1;
            }
            int i13 = i4;
            if (z5) {
                i5 = i12;
                i12++;
            } else {
                i5 = -1;
            }
            int i14 = i5;
            if (!z6 || z3) {
                i6 = -1;
            } else {
                i6 = i12;
                i12++;
            }
            int i15 = i6;
            int i16 = i12;
            int i17 = i12 + 1;
            if (z6 && z3) {
                i7 = i17;
                i17++;
            } else {
                i7 = -1;
            }
            int i18 = i7;
            int i19 = i17 - 1;
            LambdaForm.Name[] nameArrArguments = LambdaForm.arguments(i17 - iParameterCount, methodTypeBasicType.invokerType());
            if (z5) {
                nameArrArguments[i14] = new LambdaForm.Name(Lazy.NF_ensureInitialized, nameArrArguments[0]);
            }
            if (z6 && !z3) {
                nameArrArguments[i15] = new LambdaForm.Name(Lazy.NF_checkCast, nameArrArguments[0], nameArrArguments[i8]);
            }
            Object[] objArr = new Object[1 + methodType.parameterCount()];
            if (!$assertionsDisabled) {
                if (objArr.length != (z3 ? 3 : 4)) {
                    throw new AssertionError();
                }
            }
            objArr[0] = MethodHandleStatics.UNSAFE;
            if (z4) {
                LambdaForm.Name name = new LambdaForm.Name(Lazy.NF_staticBase, nameArrArguments[0]);
                nameArrArguments[i10] = name;
                objArr[1] = name;
                LambdaForm.Name name2 = new LambdaForm.Name(Lazy.NF_staticOffset, nameArrArguments[0]);
                nameArrArguments[i11] = name2;
                objArr[2] = name2;
            } else {
                LambdaForm.Name name3 = new LambdaForm.Name(Lazy.NF_checkBase, nameArrArguments[c2]);
                nameArrArguments[i13] = name3;
                objArr[1] = name3;
                LambdaForm.Name name4 = new LambdaForm.Name(Lazy.NF_fieldOffset, nameArrArguments[0]);
                nameArrArguments[i11] = name4;
                objArr[2] = name4;
            }
            if (!z3) {
                objArr[3] = z6 ? nameArrArguments[i15] : nameArrArguments[i8];
            }
            for (Object obj : objArr) {
                if (!$assertionsDisabled && obj == null) {
                    throw new AssertionError();
                }
            }
            nameArrArguments[i16] = new LambdaForm.Name(memberNameResolveOrFail, objArr);
            if (z6 && z3) {
                nameArrArguments[i18] = new LambdaForm.Name(Lazy.NF_checkCast, nameArrArguments[0], nameArrArguments[i16]);
            }
            for (LambdaForm.Name name5 : nameArrArguments) {
                if (!$assertionsDisabled && name5 == null) {
                    throw new AssertionError();
                }
            }
            String str3 = str2 + (z4 ? "Static" : "Field");
            if (z6) {
                str3 = str3 + "Cast";
            }
            if (z5) {
                str3 = str3 + "Init";
            }
            return new LambdaForm(str3, iParameterCount, nameArrArguments, i19);
        } catch (ReflectiveOperationException e2) {
            throw MethodHandleStatics.newInternalError(e2);
        }
    }

    /* loaded from: rt.jar:java/lang/invoke/DirectMethodHandle$Lazy.class */
    private static class Lazy {
        static final LambdaForm.NamedFunction NF_internalMemberName;
        static final LambdaForm.NamedFunction NF_internalMemberNameEnsureInit;
        static final LambdaForm.NamedFunction NF_ensureInitialized;
        static final LambdaForm.NamedFunction NF_fieldOffset;
        static final LambdaForm.NamedFunction NF_checkBase;
        static final LambdaForm.NamedFunction NF_staticBase;
        static final LambdaForm.NamedFunction NF_staticOffset;
        static final LambdaForm.NamedFunction NF_checkCast;
        static final LambdaForm.NamedFunction NF_allocateInstance;
        static final LambdaForm.NamedFunction NF_constructorMethod;
        static final LambdaForm.NamedFunction NF_checkReceiver;
        static final /* synthetic */ boolean $assertionsDisabled;

        private Lazy() {
        }

        static {
            $assertionsDisabled = !DirectMethodHandle.class.desiredAssertionStatus();
            try {
                LambdaForm.NamedFunction namedFunction = new LambdaForm.NamedFunction(DirectMethodHandle.class.getDeclaredMethod("internalMemberName", Object.class));
                NF_internalMemberName = namedFunction;
                LambdaForm.NamedFunction namedFunction2 = new LambdaForm.NamedFunction(DirectMethodHandle.class.getDeclaredMethod("internalMemberNameEnsureInit", Object.class));
                NF_internalMemberNameEnsureInit = namedFunction2;
                LambdaForm.NamedFunction namedFunction3 = new LambdaForm.NamedFunction(DirectMethodHandle.class.getDeclaredMethod("ensureInitialized", Object.class));
                NF_ensureInitialized = namedFunction3;
                LambdaForm.NamedFunction namedFunction4 = new LambdaForm.NamedFunction(DirectMethodHandle.class.getDeclaredMethod("fieldOffset", Object.class));
                NF_fieldOffset = namedFunction4;
                LambdaForm.NamedFunction namedFunction5 = new LambdaForm.NamedFunction(DirectMethodHandle.class.getDeclaredMethod("checkBase", Object.class));
                NF_checkBase = namedFunction5;
                LambdaForm.NamedFunction namedFunction6 = new LambdaForm.NamedFunction(DirectMethodHandle.class.getDeclaredMethod("staticBase", Object.class));
                NF_staticBase = namedFunction6;
                LambdaForm.NamedFunction namedFunction7 = new LambdaForm.NamedFunction(DirectMethodHandle.class.getDeclaredMethod("staticOffset", Object.class));
                NF_staticOffset = namedFunction7;
                LambdaForm.NamedFunction namedFunction8 = new LambdaForm.NamedFunction(DirectMethodHandle.class.getDeclaredMethod("checkCast", Object.class, Object.class));
                NF_checkCast = namedFunction8;
                LambdaForm.NamedFunction namedFunction9 = new LambdaForm.NamedFunction(DirectMethodHandle.class.getDeclaredMethod("allocateInstance", Object.class));
                NF_allocateInstance = namedFunction9;
                LambdaForm.NamedFunction namedFunction10 = new LambdaForm.NamedFunction(DirectMethodHandle.class.getDeclaredMethod("constructorMethod", Object.class));
                NF_constructorMethod = namedFunction10;
                LambdaForm.NamedFunction namedFunction11 = new LambdaForm.NamedFunction(new MemberName(Interface.class.getDeclaredMethod("checkReceiver", Object.class)));
                NF_checkReceiver = namedFunction11;
                for (LambdaForm.NamedFunction namedFunction12 : new LambdaForm.NamedFunction[]{namedFunction, namedFunction2, namedFunction3, namedFunction4, namedFunction5, namedFunction6, namedFunction7, namedFunction8, namedFunction9, namedFunction10, namedFunction11}) {
                    if (!$assertionsDisabled && !InvokerBytecodeGenerator.isStaticallyInvocable(namedFunction12.member)) {
                        throw new AssertionError(namedFunction12);
                    }
                    namedFunction12.resolve();
                }
            } catch (ReflectiveOperationException e2) {
                throw MethodHandleStatics.newInternalError(e2);
            }
        }
    }
}
