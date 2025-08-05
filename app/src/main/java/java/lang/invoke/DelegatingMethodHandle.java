package java.lang.invoke;

import java.lang.invoke.LambdaForm;
import java.util.Arrays;

/* loaded from: rt.jar:java/lang/invoke/DelegatingMethodHandle.class */
abstract class DelegatingMethodHandle extends MethodHandle {
    static final LambdaForm.NamedFunction NF_getTarget;
    static final /* synthetic */ boolean $assertionsDisabled;

    protected abstract MethodHandle getTarget();

    @Override // java.lang.invoke.MethodHandle
    abstract MethodHandle asTypeUncached(MethodType methodType);

    static {
        $assertionsDisabled = !DelegatingMethodHandle.class.desiredAssertionStatus();
        try {
            NF_getTarget = new LambdaForm.NamedFunction(DelegatingMethodHandle.class.getDeclaredMethod("getTarget", new Class[0]));
        } catch (ReflectiveOperationException e2) {
            throw MethodHandleStatics.newInternalError(e2);
        }
    }

    protected DelegatingMethodHandle(MethodHandle methodHandle) {
        this(methodHandle.type(), methodHandle);
    }

    protected DelegatingMethodHandle(MethodType methodType, MethodHandle methodHandle) {
        super(methodType, chooseDelegatingForm(methodHandle));
    }

    protected DelegatingMethodHandle(MethodType methodType, LambdaForm lambdaForm) {
        super(methodType, lambdaForm);
    }

    @Override // java.lang.invoke.MethodHandle
    MemberName internalMemberName() {
        return getTarget().internalMemberName();
    }

    @Override // java.lang.invoke.MethodHandle
    boolean isInvokeSpecial() {
        return getTarget().isInvokeSpecial();
    }

    @Override // java.lang.invoke.MethodHandle
    Class<?> internalCallerClass() {
        return getTarget().internalCallerClass();
    }

    @Override // java.lang.invoke.MethodHandle
    MethodHandle copyWith(MethodType methodType, LambdaForm lambdaForm) {
        throw MethodHandleStatics.newIllegalArgumentException("do not use this");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // java.lang.invoke.MethodHandle
    public String internalProperties() {
        return "\n& Class=" + getClass().getSimpleName() + "\n& Target=" + getTarget().debugString();
    }

    @Override // java.lang.invoke.MethodHandle
    BoundMethodHandle rebind() {
        return getTarget().rebind();
    }

    private static LambdaForm chooseDelegatingForm(MethodHandle methodHandle) {
        if (methodHandle instanceof SimpleMethodHandle) {
            return methodHandle.internalForm();
        }
        return makeReinvokerForm(methodHandle, 8, DelegatingMethodHandle.class, NF_getTarget);
    }

    static LambdaForm makeReinvokerForm(MethodHandle methodHandle, int i2, Object obj, LambdaForm.NamedFunction namedFunction) {
        String str;
        switch (i2) {
            case 7:
                str = "BMH.reinvoke";
                break;
            case 8:
                str = "MH.delegate";
                break;
            default:
                str = "MH.reinvoke";
                break;
        }
        return makeReinvokerForm(methodHandle, i2, obj, str, true, namedFunction, null);
    }

    static LambdaForm makeReinvokerForm(MethodHandle methodHandle, int i2, Object obj, String str, boolean z2, LambdaForm.NamedFunction namedFunction, LambdaForm.NamedFunction namedFunction2) {
        int i3;
        int i4;
        LambdaForm lambdaFormCachedLambdaForm;
        MethodType methodTypeBasicType = methodHandle.type().basicType();
        boolean z3 = i2 < 0 || methodTypeBasicType.parameterSlotCount() > 253;
        boolean z4 = namedFunction2 != null;
        if (!z3 && (lambdaFormCachedLambdaForm = methodTypeBasicType.form().cachedLambdaForm(i2)) != null) {
            return lambdaFormCachedLambdaForm;
        }
        int iParameterCount = 1 + methodTypeBasicType.parameterCount();
        int i5 = iParameterCount;
        if (z4) {
            i3 = i5;
            i5++;
        } else {
            i3 = -1;
        }
        int i6 = i3;
        if (z3) {
            i4 = -1;
        } else {
            i4 = i5;
            i5++;
        }
        int i7 = i4;
        int i8 = i5;
        int i9 = i5 + 1;
        LambdaForm.Name[] nameArrArguments = LambdaForm.arguments(i9 - iParameterCount, methodTypeBasicType.invokerType());
        if (!$assertionsDisabled && nameArrArguments.length != i9) {
            throw new AssertionError();
        }
        nameArrArguments[0] = nameArrArguments[0].withConstraint(obj);
        if (z4) {
            nameArrArguments[i6] = new LambdaForm.Name(namedFunction2, nameArrArguments[0]);
        }
        if (z3) {
            nameArrArguments[i8] = new LambdaForm.Name(methodHandle, Arrays.copyOfRange(nameArrArguments, 1, iParameterCount, Object[].class));
        } else {
            nameArrArguments[i7] = new LambdaForm.Name(namedFunction, nameArrArguments[0]);
            Object[] objArrCopyOfRange = Arrays.copyOfRange(nameArrArguments, 0, iParameterCount, Object[].class);
            objArrCopyOfRange[0] = nameArrArguments[i7];
            nameArrArguments[i8] = new LambdaForm.Name(methodTypeBasicType, objArrCopyOfRange);
        }
        LambdaForm lambdaForm = new LambdaForm(str, iParameterCount, nameArrArguments, z2);
        if (!z3) {
            lambdaForm = methodTypeBasicType.form().setCachedLambdaForm(i2, lambdaForm);
        }
        return lambdaForm;
    }
}
