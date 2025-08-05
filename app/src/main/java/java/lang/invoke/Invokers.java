package java.lang.invoke;

import java.lang.invoke.BoundMethodHandle;
import java.lang.invoke.LambdaForm;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Array;
import java.util.Arrays;

/* loaded from: rt.jar:java/lang/invoke/Invokers.class */
class Invokers {
    private final MethodType targetType;

    @Stable
    private final MethodHandle[] invokers = new MethodHandle[3];
    static final int INV_EXACT = 0;
    static final int INV_GENERIC = 1;
    static final int INV_BASIC = 2;
    static final int INV_LIMIT = 3;
    private static final int MH_LINKER_ARG_APPENDED = 1;
    private static final LambdaForm.NamedFunction NF_checkExactType;
    private static final LambdaForm.NamedFunction NF_checkGenericType;
    private static final LambdaForm.NamedFunction NF_getCallSiteTarget;
    private static final LambdaForm.NamedFunction NF_checkCustomized;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !Invokers.class.desiredAssertionStatus();
        try {
            LambdaForm.NamedFunction namedFunction = new LambdaForm.NamedFunction(Invokers.class.getDeclaredMethod("checkExactType", Object.class, Object.class));
            NF_checkExactType = namedFunction;
            LambdaForm.NamedFunction namedFunction2 = new LambdaForm.NamedFunction(Invokers.class.getDeclaredMethod("checkGenericType", Object.class, Object.class));
            NF_checkGenericType = namedFunction2;
            LambdaForm.NamedFunction namedFunction3 = new LambdaForm.NamedFunction(Invokers.class.getDeclaredMethod("getCallSiteTarget", Object.class));
            NF_getCallSiteTarget = namedFunction3;
            LambdaForm.NamedFunction namedFunction4 = new LambdaForm.NamedFunction(Invokers.class.getDeclaredMethod("checkCustomized", Object.class));
            NF_checkCustomized = namedFunction4;
            for (LambdaForm.NamedFunction namedFunction5 : new LambdaForm.NamedFunction[]{namedFunction, namedFunction2, namedFunction3, namedFunction4}) {
                if (!$assertionsDisabled && !InvokerBytecodeGenerator.isStaticallyInvocable(namedFunction5.member)) {
                    throw new AssertionError(namedFunction5);
                }
                namedFunction5.resolve();
            }
        } catch (ReflectiveOperationException e2) {
            throw MethodHandleStatics.newInternalError(e2);
        }
    }

    Invokers(MethodType methodType) {
        this.targetType = methodType;
    }

    MethodHandle exactInvoker() {
        MethodHandle methodHandleCachedInvoker = cachedInvoker(0);
        return methodHandleCachedInvoker != null ? methodHandleCachedInvoker : setCachedInvoker(0, makeExactOrGeneralInvoker(true));
    }

    MethodHandle genericInvoker() {
        MethodHandle methodHandleCachedInvoker = cachedInvoker(1);
        return methodHandleCachedInvoker != null ? methodHandleCachedInvoker : setCachedInvoker(1, makeExactOrGeneralInvoker(false));
    }

    MethodHandle basicInvoker() {
        MethodHandle methodHandleCachedInvoker = cachedInvoker(2);
        if (methodHandleCachedInvoker != null) {
            return methodHandleCachedInvoker;
        }
        MethodType methodTypeBasicType = this.targetType.basicType();
        if (methodTypeBasicType != this.targetType) {
            return setCachedInvoker(2, methodTypeBasicType.invokers().basicInvoker());
        }
        MethodHandle methodHandleCachedMethodHandle = methodTypeBasicType.form().cachedMethodHandle(0);
        if (methodHandleCachedMethodHandle == null) {
            DirectMethodHandle directMethodHandleMake = DirectMethodHandle.make(invokeBasicMethod(methodTypeBasicType));
            if (!$assertionsDisabled && !checkInvoker(directMethodHandleMake)) {
                throw new AssertionError();
            }
            methodHandleCachedMethodHandle = methodTypeBasicType.form().setCachedMethodHandle(0, directMethodHandleMake);
        }
        return setCachedInvoker(2, methodHandleCachedMethodHandle);
    }

    private MethodHandle cachedInvoker(int i2) {
        return this.invokers[i2];
    }

    private synchronized MethodHandle setCachedInvoker(int i2, MethodHandle methodHandle) {
        MethodHandle methodHandle2 = this.invokers[i2];
        if (methodHandle2 != null) {
            return methodHandle2;
        }
        this.invokers[i2] = methodHandle;
        return methodHandle;
    }

    private MethodHandle makeExactOrGeneralInvoker(boolean z2) {
        MethodType methodType = this.targetType;
        MethodHandle methodHandleWithInternalMemberName = BoundMethodHandle.bindSingle(methodType.invokerType(), invokeHandleForm(methodType, false, z2 ? 11 : 13), methodType).withInternalMemberName(MemberName.makeMethodHandleInvoke(z2 ? "invokeExact" : "invoke", methodType), false);
        if (!$assertionsDisabled && !checkInvoker(methodHandleWithInternalMemberName)) {
            throw new AssertionError();
        }
        maybeCompileToBytecode(methodHandleWithInternalMemberName);
        return methodHandleWithInternalMemberName;
    }

    private void maybeCompileToBytecode(MethodHandle methodHandle) {
        if (this.targetType == this.targetType.erase() && this.targetType.parameterCount() < 10) {
            methodHandle.form.compileToBytecode();
        }
    }

    static MemberName invokeBasicMethod(MethodType methodType) {
        if (!$assertionsDisabled && methodType != methodType.basicType()) {
            throw new AssertionError();
        }
        try {
            return MethodHandles.Lookup.IMPL_LOOKUP.resolveOrFail((byte) 5, MethodHandle.class, "invokeBasic", methodType);
        } catch (ReflectiveOperationException e2) {
            throw MethodHandleStatics.newInternalError("JVM cannot find invoker for " + ((Object) methodType), e2);
        }
    }

    private boolean checkInvoker(MethodHandle methodHandle) {
        if (!$assertionsDisabled && !this.targetType.invokerType().equals((Object) methodHandle.type())) {
            throw new AssertionError(Arrays.asList(this.targetType, this.targetType.invokerType(), methodHandle));
        }
        if (!$assertionsDisabled && methodHandle.internalMemberName() != null && !methodHandle.internalMemberName().getMethodType().equals((Object) this.targetType)) {
            throw new AssertionError();
        }
        if ($assertionsDisabled || !methodHandle.isVarargsCollector()) {
            return true;
        }
        throw new AssertionError();
    }

    MethodHandle spreadInvoker(int i2) {
        int iParameterCount = this.targetType.parameterCount() - i2;
        MethodType methodType = this.targetType;
        Class<?> clsImpliedRestargType = impliedRestargType(methodType, i2);
        if (methodType.parameterSlotCount() <= 253) {
            return genericInvoker().asSpreader(clsImpliedRestargType, iParameterCount);
        }
        return MethodHandles.filterArgument(MethodHandles.invoker(methodType.replaceParameterTypes(i2, methodType.parameterCount(), clsImpliedRestargType)), 0, MethodHandles.insertArguments(Lazy.MH_asSpreader, 1, clsImpliedRestargType, Integer.valueOf(iParameterCount)));
    }

    private static Class<?> impliedRestargType(MethodType methodType, int i2) {
        int iParameterCount;
        if (methodType.isGeneric() || i2 >= (iParameterCount = methodType.parameterCount())) {
            return Object[].class;
        }
        Class<?> clsParameterType = methodType.parameterType(i2);
        for (int i3 = i2 + 1; i3 < iParameterCount; i3++) {
            if (clsParameterType != methodType.parameterType(i3)) {
                throw MethodHandleStatics.newIllegalArgumentException("need homogeneous rest arguments", methodType);
            }
        }
        return clsParameterType == Object.class ? Object[].class : Array.newInstance(clsParameterType, 0).getClass();
    }

    public String toString() {
        return "Invokers" + ((Object) this.targetType);
    }

    static MemberName methodHandleInvokeLinkerMethod(String str, MethodType methodType, Object[] objArr) {
        int i2;
        LambdaForm lambdaFormInvokeHandleForm;
        switch (str) {
            case "invokeExact":
                i2 = 10;
                break;
            case "invoke":
                i2 = 12;
                break;
            default:
                throw new InternalError("not invoker: " + str);
        }
        if (methodType.parameterSlotCount() <= 253) {
            lambdaFormInvokeHandleForm = invokeHandleForm(methodType, false, i2);
            objArr[0] = methodType;
        } else {
            lambdaFormInvokeHandleForm = invokeHandleForm(methodType, true, i2);
        }
        return lambdaFormInvokeHandleForm.vmentry;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v38, types: [java.lang.Object[], java.lang.invoke.LambdaForm$Name[]] */
    /* JADX WARN: Type inference failed for: r0v44, types: [java.lang.Object[]] */
    /* JADX WARN: Type inference failed for: r0v47 */
    /* JADX WARN: Type inference failed for: r0v73 */
    /* JADX WARN: Type inference failed for: r2v10, types: [java.lang.invoke.LambdaForm$Name] */
    /* JADX WARN: Type inference failed for: r2v3 */
    /* JADX WARN: Type inference failed for: r5v1, types: [java.lang.Object[]] */
    /* JADX WARN: Type inference failed for: r5v4, types: [java.lang.Object[]] */
    /* JADX WARN: Type inference failed for: r5v6, types: [java.lang.Object[]] */
    /* JADX WARN: Type inference failed for: r5v8, types: [java.lang.Object[]] */
    /* JADX WARN: Type inference failed for: r8v1 */
    /* JADX WARN: Type inference failed for: r8v4 */
    /* JADX WARN: Type inference failed for: r8v6 */
    /* JADX WARN: Type inference failed for: r8v9 */
    private static LambdaForm invokeHandleForm(MethodType methodType, boolean z2, int i2) {
        boolean z3;
        boolean z4;
        boolean z5;
        String str;
        int i3;
        int i4;
        LambdaForm lambdaFormCachedLambdaForm;
        if (!z2) {
            methodType = methodType.basicType();
            z3 = true;
        } else {
            z3 = false;
        }
        switch (i2) {
            case 10:
                z4 = true;
                z5 = false;
                str = "invokeExact_MT";
                break;
            case 11:
                z4 = false;
                z5 = false;
                str = "exactInvoker";
                break;
            case 12:
                z4 = true;
                z5 = true;
                str = "invoke_MT";
                break;
            case 13:
                z4 = false;
                z5 = true;
                str = "invoker";
                break;
            default:
                throw new InternalError();
        }
        if (z3 && (lambdaFormCachedLambdaForm = methodType.form().cachedLambdaForm(i2)) != null) {
            return lambdaFormCachedLambdaForm;
        }
        int i5 = 0 + (z4 ? 0 : 1);
        int iParameterCount = i5 + 1 + methodType.parameterCount();
        int i6 = iParameterCount + ((!z4 || z2) ? 0 : 1);
        int i7 = iParameterCount;
        if (z2) {
            i3 = -1;
        } else {
            i3 = i7;
            i7++;
        }
        int i8 = i3;
        int i9 = i7;
        int i10 = i7 + 1;
        if (MethodHandleStatics.CUSTOMIZE_THRESHOLD >= 0) {
            i4 = i10;
            i10++;
        } else {
            i4 = -1;
        }
        int i11 = i4;
        int i12 = i10;
        int i13 = i10 + 1;
        MethodType methodTypeInvokerType = methodType.invokerType();
        if (z4) {
            if (!z2) {
                methodTypeInvokerType = methodTypeInvokerType.appendParameterTypes(MemberName.class);
            }
        } else {
            methodTypeInvokerType = methodTypeInvokerType.invokerType();
        }
        ?? Arguments = LambdaForm.arguments(i13 - i6, methodTypeInvokerType);
        if (!$assertionsDisabled && Arguments.length != i13) {
            throw new AssertionError(Arrays.asList(methodType, Boolean.valueOf(z2), Integer.valueOf(i2), Integer.valueOf(i13), Integer.valueOf(Arguments.length)));
        }
        if (i8 >= i6) {
            if (!$assertionsDisabled && Arguments[i8] != 0) {
                throw new AssertionError();
            }
            BoundMethodHandle.SpeciesData speciesDataSpeciesData_L = BoundMethodHandle.speciesData_L();
            Arguments[0] = Arguments[0].withConstraint(speciesDataSpeciesData_L);
            Arguments[i8] = new LambdaForm.Name(speciesDataSpeciesData_L.getterFunction(0), (Object[]) new Object[]{Arguments[0]});
        }
        MethodType methodTypeBasicType = methodType.basicType();
        ?? CopyOfRange = Arrays.copyOfRange(Arguments, i5, iParameterCount, Object[].class);
        MethodType methodType2 = z2 ? methodType : Arguments[i8];
        if (!z5) {
            Arguments[i9] = new LambdaForm.Name(NF_checkExactType, (Object[]) new Object[]{Arguments[i5], methodType2});
        } else {
            Arguments[i9] = new LambdaForm.Name(NF_checkGenericType, (Object[]) new Object[]{Arguments[i5], methodType2});
            CopyOfRange[0] = Arguments[i9];
        }
        if (i11 != -1) {
            Arguments[i11] = new LambdaForm.Name(NF_checkCustomized, (Object[]) new Object[]{CopyOfRange[0]});
        }
        Arguments[i12] = new LambdaForm.Name(methodTypeBasicType, (Object[]) CopyOfRange);
        LambdaForm lambdaForm = new LambdaForm(str, i6, Arguments);
        if (z4) {
            lambdaForm.compileToBytecode();
        }
        if (z3) {
            lambdaForm = methodType.form().setCachedLambdaForm(i2, lambdaForm);
        }
        return lambdaForm;
    }

    static WrongMethodTypeException newWrongMethodTypeException(MethodType methodType, MethodType methodType2) {
        return new WrongMethodTypeException("expected " + ((Object) methodType2) + " but found " + ((Object) methodType));
    }

    @ForceInline
    static void checkExactType(Object obj, Object obj2) {
        MethodType methodType = (MethodType) obj2;
        MethodType methodTypeType = ((MethodHandle) obj).type();
        if (methodTypeType != methodType) {
            throw newWrongMethodTypeException(methodType, methodTypeType);
        }
    }

    @ForceInline
    static Object checkGenericType(Object obj, Object obj2) {
        return ((MethodHandle) obj).asType((MethodType) obj2);
    }

    static MemberName linkToCallSiteMethod(MethodType methodType) {
        return callSiteForm(methodType, false).vmentry;
    }

    static MemberName linkToTargetMethod(MethodType methodType) {
        return callSiteForm(methodType, true).vmentry;
    }

    private static LambdaForm callSiteForm(MethodType methodType, boolean z2) {
        int i2;
        MethodType methodTypeBasicType = methodType.basicType();
        int i3 = z2 ? 15 : 14;
        LambdaForm lambdaFormCachedLambdaForm = methodTypeBasicType.form().cachedLambdaForm(i3);
        if (lambdaFormCachedLambdaForm != null) {
            return lambdaFormCachedLambdaForm;
        }
        int iParameterCount = 0 + methodTypeBasicType.parameterCount();
        int i4 = iParameterCount + 1;
        int i5 = iParameterCount + 1;
        int i6 = z2 ? -1 : iParameterCount;
        if (z2) {
            i2 = iParameterCount;
        } else {
            i2 = i5;
            i5++;
        }
        int i7 = i2;
        int i8 = i5;
        int i9 = i5 + 1;
        Class<?>[] clsArr = new Class[1];
        clsArr[0] = z2 ? MethodHandle.class : CallSite.class;
        LambdaForm.Name[] nameArrArguments = LambdaForm.arguments(i9 - i4, methodTypeBasicType.appendParameterTypes(clsArr));
        if (!$assertionsDisabled && nameArrArguments.length != i9) {
            throw new AssertionError();
        }
        if (!$assertionsDisabled && nameArrArguments[iParameterCount] == null) {
            throw new AssertionError();
        }
        if (!z2) {
            nameArrArguments[i7] = new LambdaForm.Name(NF_getCallSiteTarget, nameArrArguments[i6]);
        }
        Object[] objArrCopyOfRange = Arrays.copyOfRange(nameArrArguments, 0, iParameterCount + 1, Object[].class);
        System.arraycopy(objArrCopyOfRange, 0, objArrCopyOfRange, 1, objArrCopyOfRange.length - 1);
        objArrCopyOfRange[0] = nameArrArguments[i7];
        nameArrArguments[i8] = new LambdaForm.Name(methodTypeBasicType, objArrCopyOfRange);
        LambdaForm lambdaForm = new LambdaForm(z2 ? "linkToTargetMethod" : "linkToCallSite", i4, nameArrArguments);
        lambdaForm.compileToBytecode();
        return methodTypeBasicType.form().setCachedLambdaForm(i3, lambdaForm);
    }

    @ForceInline
    static Object getCallSiteTarget(Object obj) {
        return ((CallSite) obj).getTarget();
    }

    @ForceInline
    static void checkCustomized(Object obj) {
        MethodHandle methodHandle = (MethodHandle) obj;
        if (methodHandle.form.customized == null) {
            maybeCustomize(methodHandle);
        }
    }

    @DontInline
    static void maybeCustomize(MethodHandle methodHandle) {
        byte b2 = methodHandle.customizationCount;
        if (b2 >= MethodHandleStatics.CUSTOMIZE_THRESHOLD) {
            methodHandle.customize();
        } else {
            methodHandle.customizationCount = (byte) (b2 + 1);
        }
    }

    /* loaded from: rt.jar:java/lang/invoke/Invokers$Lazy.class */
    private static class Lazy {
        private static final MethodHandle MH_asSpreader;

        private Lazy() {
        }

        static {
            try {
                MH_asSpreader = MethodHandles.Lookup.IMPL_LOOKUP.findVirtual(MethodHandle.class, "asSpreader", MethodType.methodType(MethodHandle.class, Class.class, Integer.TYPE));
            } catch (ReflectiveOperationException e2) {
                throw MethodHandleStatics.newInternalError(e2);
            }
        }
    }
}
