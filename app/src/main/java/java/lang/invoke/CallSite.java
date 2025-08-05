package java.lang.invoke;

import java.lang.invoke.MethodHandles;

/* loaded from: rt.jar:java/lang/invoke/CallSite.class */
public abstract class CallSite {
    MethodHandle target;
    private static final MethodHandle GET_TARGET;
    private static final MethodHandle THROW_UCS;
    private static final long TARGET_OFFSET;

    public abstract MethodHandle getTarget();

    public abstract void setTarget(MethodHandle methodHandle);

    public abstract MethodHandle dynamicInvoker();

    static {
        MethodHandleImpl.initStatics();
        try {
            GET_TARGET = MethodHandles.Lookup.IMPL_LOOKUP.findVirtual(CallSite.class, "getTarget", MethodType.methodType(MethodHandle.class));
            THROW_UCS = MethodHandles.Lookup.IMPL_LOOKUP.findStatic(CallSite.class, "uninitializedCallSite", MethodType.methodType((Class<?>) Object.class, (Class<?>) Object[].class));
            try {
                TARGET_OFFSET = MethodHandleStatics.UNSAFE.objectFieldOffset(CallSite.class.getDeclaredField("target"));
            } catch (Exception e2) {
                throw new Error(e2);
            }
        } catch (ReflectiveOperationException e3) {
            throw MethodHandleStatics.newInternalError(e3);
        }
    }

    CallSite(MethodType methodType) {
        this.target = makeUninitializedCallSite(methodType);
    }

    CallSite(MethodHandle methodHandle) {
        methodHandle.type();
        this.target = methodHandle;
    }

    CallSite(MethodType methodType, MethodHandle methodHandle) throws Throwable {
        this(methodType);
        MethodHandle methodHandle2 = (MethodHandle) methodHandle.invokeWithArguments((ConstantCallSite) this);
        checkTargetChange(this.target, methodHandle2);
        this.target = methodHandle2;
    }

    public MethodType type() {
        return this.target.type();
    }

    void checkTargetChange(MethodHandle methodHandle, MethodHandle methodHandle2) {
        MethodType methodTypeType = methodHandle.type();
        if (!methodHandle2.type().equals((Object) methodTypeType)) {
            throw wrongTargetType(methodHandle2, methodTypeType);
        }
    }

    private static WrongMethodTypeException wrongTargetType(MethodHandle methodHandle, MethodType methodType) {
        return new WrongMethodTypeException(String.valueOf(methodHandle) + " should be of type " + ((Object) methodType));
    }

    MethodHandle makeDynamicInvoker() {
        return MethodHandles.foldArguments(MethodHandles.exactInvoker(type()), GET_TARGET.bindArgumentL(0, this));
    }

    private static Object uninitializedCallSite(Object... objArr) {
        throw new IllegalStateException("uninitialized call site");
    }

    private MethodHandle makeUninitializedCallSite(MethodType methodType) {
        MethodType methodTypeBasicType = methodType.basicType();
        MethodHandle methodHandleCachedMethodHandle = methodTypeBasicType.form().cachedMethodHandle(2);
        if (methodHandleCachedMethodHandle == null) {
            methodHandleCachedMethodHandle = methodTypeBasicType.form().setCachedMethodHandle(2, THROW_UCS.asType(methodTypeBasicType));
        }
        return methodHandleCachedMethodHandle.viewAsType(methodType, false);
    }

    void setTargetNormal(MethodHandle methodHandle) {
        MethodHandleNatives.setCallSiteTargetNormal(this, methodHandle);
    }

    MethodHandle getTargetVolatile() {
        return (MethodHandle) MethodHandleStatics.UNSAFE.getObjectVolatile(this, TARGET_OFFSET);
    }

    void setTargetVolatile(MethodHandle methodHandle) {
        MethodHandleNatives.setCallSiteTargetVolatile(this, methodHandle);
    }

    static CallSite makeSite(MethodHandle methodHandle, String str, MethodType methodType, Object obj, Class<?> cls) {
        BootstrapMethodError bootstrapMethodError;
        Object objInvokeExact;
        MethodHandles.Lookup lookupIn = MethodHandles.Lookup.IMPL_LOOKUP.in(cls);
        try {
            Object objMaybeReBox = maybeReBox(obj);
            if (objMaybeReBox == null) {
                objInvokeExact = (Object) methodHandle.invoke(lookupIn, str, methodType);
            } else if (!objMaybeReBox.getClass().isArray()) {
                objInvokeExact = (Object) methodHandle.invoke(lookupIn, str, methodType, objMaybeReBox);
            } else {
                Object[] objArr = (Object[]) objMaybeReBox;
                maybeReBoxElements(objArr);
                switch (objArr.length) {
                    case 0:
                        objInvokeExact = (Object) methodHandle.invoke(lookupIn, str, methodType);
                        break;
                    case 1:
                        objInvokeExact = (Object) methodHandle.invoke(lookupIn, str, methodType, objArr[0]);
                        break;
                    case 2:
                        objInvokeExact = (Object) methodHandle.invoke(lookupIn, str, methodType, objArr[0], objArr[1]);
                        break;
                    case 3:
                        objInvokeExact = (Object) methodHandle.invoke(lookupIn, str, methodType, objArr[0], objArr[1], objArr[2]);
                        break;
                    case 4:
                        objInvokeExact = (Object) methodHandle.invoke(lookupIn, str, methodType, objArr[0], objArr[1], objArr[2], objArr[3]);
                        break;
                    case 5:
                        objInvokeExact = (Object) methodHandle.invoke(lookupIn, str, methodType, objArr[0], objArr[1], objArr[2], objArr[3], objArr[4]);
                        break;
                    case 6:
                        objInvokeExact = (Object) methodHandle.invoke(lookupIn, str, methodType, objArr[0], objArr[1], objArr[2], objArr[3], objArr[4], objArr[5]);
                        break;
                    default:
                        if (3 + objArr.length > 254) {
                            throw new BootstrapMethodError("too many bootstrap method arguments");
                        }
                        methodHandle.type();
                        MethodType methodTypeGenericMethodType = MethodType.genericMethodType(3 + objArr.length);
                        objInvokeExact = (Object) methodTypeGenericMethodType.invokers().spreadInvoker(3).invokeExact(methodHandle.asType(methodTypeGenericMethodType), lookupIn, str, methodType, objArr);
                        break;
                }
            }
            if (objInvokeExact instanceof CallSite) {
                CallSite callSite = (CallSite) objInvokeExact;
                if (!callSite.getTarget().type().equals((Object) methodType)) {
                    throw wrongTargetType(callSite.getTarget(), methodType);
                }
                return callSite;
            }
            throw new ClassCastException("bootstrap method failed to produce a CallSite");
        } catch (Throwable th) {
            if (th instanceof BootstrapMethodError) {
                bootstrapMethodError = (BootstrapMethodError) th;
            } else {
                bootstrapMethodError = new BootstrapMethodError("call site initialization exception", th);
            }
            throw bootstrapMethodError;
        }
    }

    private static Object maybeReBox(Object obj) {
        int iIntValue;
        if ((obj instanceof Integer) && (iIntValue = ((Integer) obj).intValue()) == ((byte) iIntValue)) {
            obj = Integer.valueOf(iIntValue);
        }
        return obj;
    }

    private static void maybeReBoxElements(Object[] objArr) {
        for (int i2 = 0; i2 < objArr.length; i2++) {
            objArr[i2] = maybeReBox(objArr[i2]);
        }
    }
}
