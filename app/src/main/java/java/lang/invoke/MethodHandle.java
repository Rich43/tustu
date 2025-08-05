package java.lang.invoke;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.invoke.MethodHandleImpl;
import java.util.Arrays;
import java.util.List;

/* loaded from: rt.jar:java/lang/invoke/MethodHandle.class */
public abstract class MethodHandle {
    private final MethodType type;
    final LambdaForm form;
    MethodHandle asTypeCache;
    byte customizationCount;
    private static final long FORM_OFFSET;
    static final /* synthetic */ boolean $assertionsDisabled;

    @Target({ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    /* loaded from: rt.jar:java/lang/invoke/MethodHandle$PolymorphicSignature.class */
    @interface PolymorphicSignature {
    }

    @PolymorphicSignature
    public final native Object invokeExact(Object... objArr) throws Throwable;

    @PolymorphicSignature
    public final native Object invoke(Object... objArr) throws Throwable;

    @PolymorphicSignature
    final native Object invokeBasic(Object... objArr) throws Throwable;

    @PolymorphicSignature
    static native Object linkToVirtual(Object... objArr) throws Throwable;

    @PolymorphicSignature
    static native Object linkToStatic(Object... objArr) throws Throwable;

    @PolymorphicSignature
    static native Object linkToSpecial(Object... objArr) throws Throwable;

    @PolymorphicSignature
    static native Object linkToInterface(Object... objArr) throws Throwable;

    abstract MethodHandle copyWith(MethodType methodType, LambdaForm lambdaForm);

    abstract BoundMethodHandle rebind();

    static {
        $assertionsDisabled = !MethodHandle.class.desiredAssertionStatus();
        MethodHandleImpl.initStatics();
        try {
            FORM_OFFSET = MethodHandleStatics.UNSAFE.objectFieldOffset(MethodHandle.class.getDeclaredField("form"));
        } catch (ReflectiveOperationException e2) {
            throw MethodHandleStatics.newInternalError(e2);
        }
    }

    public MethodType type() {
        return this.type;
    }

    MethodHandle(MethodType methodType, LambdaForm lambdaForm) {
        methodType.getClass();
        lambdaForm.getClass();
        this.type = methodType;
        this.form = lambdaForm.uncustomize();
        this.form.prepare();
    }

    public Object invokeWithArguments(Object... objArr) throws Throwable {
        MethodType methodTypeGenericMethodType = MethodType.genericMethodType(objArr == null ? 0 : objArr.length);
        return (Object) methodTypeGenericMethodType.invokers().spreadInvoker(0).invokeExact(asType(methodTypeGenericMethodType), objArr);
    }

    public Object invokeWithArguments(List<?> list) throws Throwable {
        return invokeWithArguments(list.toArray());
    }

    public MethodHandle asType(MethodType methodType) {
        if (methodType == this.type) {
            return this;
        }
        MethodHandle methodHandleAsTypeCached = asTypeCached(methodType);
        if (methodHandleAsTypeCached != null) {
            return methodHandleAsTypeCached;
        }
        return asTypeUncached(methodType);
    }

    private MethodHandle asTypeCached(MethodType methodType) {
        MethodHandle methodHandle = this.asTypeCache;
        if (methodHandle != null && methodType == methodHandle.type) {
            return methodHandle;
        }
        return null;
    }

    MethodHandle asTypeUncached(MethodType methodType) {
        if (!this.type.isConvertibleTo(methodType)) {
            throw new WrongMethodTypeException("cannot convert " + ((Object) this) + " to " + ((Object) methodType));
        }
        MethodHandle methodHandleMakePairwiseConvert = MethodHandleImpl.makePairwiseConvert(this, methodType, true);
        this.asTypeCache = methodHandleMakePairwiseConvert;
        return methodHandleMakePairwiseConvert;
    }

    public MethodHandle asSpreader(Class<?> cls, int i2) {
        MethodType methodTypeAsSpreaderChecks = asSpreaderChecks(cls, i2);
        int iParameterCount = type().parameterCount();
        int i3 = iParameterCount - i2;
        BoundMethodHandle boundMethodHandleRebind = asType(methodTypeAsSpreaderChecks).rebind();
        return boundMethodHandleRebind.copyWith(methodTypeAsSpreaderChecks.replaceParameterTypes(i3, iParameterCount, cls), boundMethodHandleRebind.editor().spreadArgumentsForm(1 + i3, cls, i2));
    }

    private MethodType asSpreaderChecks(Class<?> cls, int i2) {
        spreadArrayChecks(cls, i2);
        int iParameterCount = type().parameterCount();
        if (iParameterCount < i2 || i2 < 0) {
            throw MethodHandleStatics.newIllegalArgumentException("bad spread array length");
        }
        Class<?> componentType = cls.getComponentType();
        MethodType methodTypeType = type();
        boolean z2 = true;
        boolean z3 = false;
        int i3 = iParameterCount - i2;
        while (true) {
            if (i3 >= iParameterCount) {
                break;
            }
            Class<?> clsParameterType = methodTypeType.parameterType(i3);
            if (clsParameterType != componentType) {
                z2 = false;
                if (!MethodType.canConvert(componentType, clsParameterType)) {
                    z3 = true;
                    break;
                }
            }
            i3++;
        }
        if (z2) {
            return methodTypeType;
        }
        MethodType methodTypeAsSpreaderType = methodTypeType.asSpreaderType(cls, i2);
        if (!z3) {
            return methodTypeAsSpreaderType;
        }
        asType(methodTypeAsSpreaderType);
        throw MethodHandleStatics.newInternalError("should not return", null);
    }

    private void spreadArrayChecks(Class<?> cls, int i2) {
        Class<?> componentType = cls.getComponentType();
        if (componentType == null) {
            throw MethodHandleStatics.newIllegalArgumentException("not an array type", cls);
        }
        if ((i2 & 127) != i2) {
            if ((i2 & 255) != i2) {
                throw MethodHandleStatics.newIllegalArgumentException("array length is not legal", Integer.valueOf(i2));
            }
            if (!$assertionsDisabled && i2 < 128) {
                throw new AssertionError();
            }
            if (componentType == Long.TYPE || componentType == Double.TYPE) {
                throw MethodHandleStatics.newIllegalArgumentException("array length is not legal for long[] or double[]", Integer.valueOf(i2));
            }
        }
    }

    public MethodHandle asCollector(Class<?> cls, int i2) {
        asCollectorChecks(cls, i2);
        int iParameterCount = type().parameterCount() - 1;
        BoundMethodHandle boundMethodHandleRebind = rebind();
        MethodType methodTypeAsCollectorType = type().asCollectorType(cls, i2);
        MethodHandle methodHandleVarargsArray = MethodHandleImpl.varargsArray(cls, i2);
        LambdaForm lambdaFormCollectArgumentArrayForm = boundMethodHandleRebind.editor().collectArgumentArrayForm(1 + iParameterCount, methodHandleVarargsArray);
        if (lambdaFormCollectArgumentArrayForm != null) {
            return boundMethodHandleRebind.copyWith(methodTypeAsCollectorType, lambdaFormCollectArgumentArrayForm);
        }
        return boundMethodHandleRebind.copyWithExtendL(methodTypeAsCollectorType, boundMethodHandleRebind.editor().collectArgumentsForm(1 + iParameterCount, methodHandleVarargsArray.type().basicType()), methodHandleVarargsArray);
    }

    boolean asCollectorChecks(Class<?> cls, int i2) {
        spreadArrayChecks(cls, i2);
        int iParameterCount = type().parameterCount();
        if (iParameterCount != 0) {
            Class<?> clsParameterType = type().parameterType(iParameterCount - 1);
            if (clsParameterType == cls) {
                return true;
            }
            if (clsParameterType.isAssignableFrom(cls)) {
                return false;
            }
        }
        throw MethodHandleStatics.newIllegalArgumentException("array type not assignable to trailing argument", this, cls);
    }

    public MethodHandle asVarargsCollector(Class<?> cls) {
        cls.getClass();
        boolean zAsCollectorChecks = asCollectorChecks(cls, 0);
        if (isVarargsCollector() && zAsCollectorChecks) {
            return this;
        }
        return MethodHandleImpl.makeVarargsCollector(this, cls);
    }

    public boolean isVarargsCollector() {
        return false;
    }

    public MethodHandle asFixedArity() {
        if ($assertionsDisabled || !isVarargsCollector()) {
            return this;
        }
        throw new AssertionError();
    }

    public MethodHandle bindTo(Object obj) {
        return bindArgumentL(0, this.type.leadingReferenceParameter().cast(obj));
    }

    public String toString() {
        return MethodHandleStatics.DEBUG_METHOD_HANDLE_NAMES ? "MethodHandle" + debugString() : standardString();
    }

    String standardString() {
        return "MethodHandle" + ((Object) this.type);
    }

    String debugString() {
        return ((Object) this.type) + " : " + ((Object) internalForm()) + internalProperties();
    }

    BoundMethodHandle bindArgumentL(int i2, Object obj) {
        return rebind().bindArgumentL(i2, obj);
    }

    MethodHandle setVarargs(MemberName memberName) throws IllegalAccessException {
        if (!memberName.isVarargs()) {
            return this;
        }
        Class<?> clsLastParameterType = type().lastParameterType();
        if (clsLastParameterType.isArray()) {
            return MethodHandleImpl.makeVarargsCollector(this, clsLastParameterType);
        }
        throw memberName.makeAccessException("cannot make variable arity", null);
    }

    MethodHandle viewAsType(MethodType methodType, boolean z2) {
        if (!$assertionsDisabled && !viewAsTypeChecks(methodType, z2)) {
            throw new AssertionError();
        }
        BoundMethodHandle boundMethodHandleRebind = rebind();
        if ($assertionsDisabled || !(boundMethodHandleRebind instanceof DirectMethodHandle)) {
            return boundMethodHandleRebind.copyWith(methodType, boundMethodHandleRebind.form);
        }
        throw new AssertionError();
    }

    boolean viewAsTypeChecks(MethodType methodType, boolean z2) {
        if (z2) {
            if (!$assertionsDisabled && !type().isViewableAs(methodType, true)) {
                throw new AssertionError(Arrays.asList(this, methodType));
            }
            return true;
        }
        if (!$assertionsDisabled && !type().basicType().isViewableAs(methodType.basicType(), true)) {
            throw new AssertionError(Arrays.asList(this, methodType));
        }
        return true;
    }

    LambdaForm internalForm() {
        return this.form;
    }

    MemberName internalMemberName() {
        return null;
    }

    Class<?> internalCallerClass() {
        return null;
    }

    MethodHandleImpl.Intrinsic intrinsicName() {
        return MethodHandleImpl.Intrinsic.NONE;
    }

    MethodHandle withInternalMemberName(MemberName memberName, boolean z2) {
        if (memberName != null) {
            return MethodHandleImpl.makeWrappedMember(this, memberName, z2);
        }
        if (internalMemberName() == null) {
            return this;
        }
        BoundMethodHandle boundMethodHandleRebind = rebind();
        if ($assertionsDisabled || boundMethodHandleRebind.internalMemberName() == null) {
            return boundMethodHandleRebind;
        }
        throw new AssertionError();
    }

    boolean isInvokeSpecial() {
        return false;
    }

    Object internalValues() {
        return null;
    }

    Object internalProperties() {
        return "";
    }

    void updateForm(LambdaForm lambdaForm) {
        if (!$assertionsDisabled && lambdaForm.customized != null && lambdaForm.customized != this) {
            throw new AssertionError();
        }
        if (this.form == lambdaForm) {
            return;
        }
        lambdaForm.prepare();
        MethodHandleStatics.UNSAFE.putObject(this, FORM_OFFSET, lambdaForm);
        MethodHandleStatics.UNSAFE.fullFence();
    }

    void customize() {
        if (this.form.customized == null) {
            updateForm(this.form.customize(this));
        } else if (!$assertionsDisabled && this.form.customized != this) {
            throw new AssertionError();
        }
    }
}
