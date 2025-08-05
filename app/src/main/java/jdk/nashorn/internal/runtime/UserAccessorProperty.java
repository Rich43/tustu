package jdk.nashorn.internal.runtime;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.concurrent.Callable;
import jdk.nashorn.internal.lookup.Lookup;
import jdk.nashorn.internal.runtime.linker.Bootstrap;

/* loaded from: nashorn.jar:jdk/nashorn/internal/runtime/UserAccessorProperty.class */
public final class UserAccessorProperty extends SpillProperty {
    private static final long serialVersionUID = -5928687246526840321L;
    private static final MethodHandles.Lookup LOOKUP;
    private static final MethodHandle INVOKE_OBJECT_GETTER;
    private static final MethodHandle INVOKE_INT_GETTER;
    private static final MethodHandle INVOKE_NUMBER_GETTER;
    private static final MethodHandle INVOKE_OBJECT_SETTER;
    private static final MethodHandle INVOKE_INT_SETTER;
    private static final MethodHandle INVOKE_NUMBER_SETTER;
    private static final Object OBJECT_GETTER_INVOKER_KEY;
    private static final Object OBJECT_SETTER_INVOKER_KEY;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !UserAccessorProperty.class.desiredAssertionStatus();
        LOOKUP = MethodHandles.lookup();
        INVOKE_OBJECT_GETTER = findOwnMH_S("invokeObjectGetter", Object.class, Accessors.class, MethodHandle.class, Object.class);
        INVOKE_INT_GETTER = findOwnMH_S("invokeIntGetter", Integer.TYPE, Accessors.class, MethodHandle.class, Integer.TYPE, Object.class);
        INVOKE_NUMBER_GETTER = findOwnMH_S("invokeNumberGetter", Double.TYPE, Accessors.class, MethodHandle.class, Integer.TYPE, Object.class);
        INVOKE_OBJECT_SETTER = findOwnMH_S("invokeObjectSetter", Void.TYPE, Accessors.class, MethodHandle.class, String.class, Object.class, Object.class);
        INVOKE_INT_SETTER = findOwnMH_S("invokeIntSetter", Void.TYPE, Accessors.class, MethodHandle.class, String.class, Object.class, Integer.TYPE);
        INVOKE_NUMBER_SETTER = findOwnMH_S("invokeNumberSetter", Void.TYPE, Accessors.class, MethodHandle.class, String.class, Object.class, Double.TYPE);
        OBJECT_GETTER_INVOKER_KEY = new Object();
        OBJECT_SETTER_INVOKER_KEY = new Object();
    }

    /* loaded from: nashorn.jar:jdk/nashorn/internal/runtime/UserAccessorProperty$Accessors.class */
    static final class Accessors {
        Object getter;
        Object setter;

        Accessors(Object getter, Object setter) {
            set(getter, setter);
        }

        final void set(Object getter, Object setter) {
            this.getter = getter;
            this.setter = setter;
        }

        public String toString() {
            return "[getter=" + this.getter + " setter=" + this.setter + ']';
        }
    }

    private static MethodHandle getObjectGetterInvoker() {
        return Context.getGlobal().getDynamicInvoker(OBJECT_GETTER_INVOKER_KEY, new Callable<MethodHandle>() { // from class: jdk.nashorn.internal.runtime.UserAccessorProperty.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.concurrent.Callable
            public MethodHandle call() throws Exception {
                return UserAccessorProperty.getINVOKE_UA_GETTER(Object.class, -1);
            }
        });
    }

    static MethodHandle getINVOKE_UA_GETTER(Class<?> returnType, int programPoint) {
        if (UnwarrantedOptimismException.isValid(programPoint)) {
            int flags = 8 | (programPoint << 11);
            return Bootstrap.createDynamicInvoker("dyn:call", flags, returnType, Object.class, Object.class);
        }
        return Bootstrap.createDynamicInvoker("dyn:call", Object.class, Object.class, Object.class);
    }

    private static MethodHandle getObjectSetterInvoker() {
        return Context.getGlobal().getDynamicInvoker(OBJECT_SETTER_INVOKER_KEY, new Callable<MethodHandle>() { // from class: jdk.nashorn.internal.runtime.UserAccessorProperty.2
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.concurrent.Callable
            public MethodHandle call() throws Exception {
                return UserAccessorProperty.getINVOKE_UA_SETTER(Object.class);
            }
        });
    }

    static MethodHandle getINVOKE_UA_SETTER(Class<?> valueType) {
        return Bootstrap.createDynamicInvoker("dyn:call", Void.TYPE, Object.class, Object.class, valueType);
    }

    UserAccessorProperty(String key, int flags, int slot) {
        super(key, flags, slot);
    }

    private UserAccessorProperty(UserAccessorProperty property) {
        super(property);
    }

    private UserAccessorProperty(UserAccessorProperty property, Class<?> newType) {
        super(property, newType);
    }

    @Override // jdk.nashorn.internal.runtime.SpillProperty, jdk.nashorn.internal.runtime.AccessorProperty, jdk.nashorn.internal.runtime.Property
    public Property copy() {
        return new UserAccessorProperty(this);
    }

    @Override // jdk.nashorn.internal.runtime.SpillProperty, jdk.nashorn.internal.runtime.AccessorProperty, jdk.nashorn.internal.runtime.Property
    public Property copy(Class<?> newType) {
        return new UserAccessorProperty(this, newType);
    }

    void setAccessors(ScriptObject sobj, PropertyMap map, Accessors gs) {
        try {
            (void) super.getSetter(Object.class, map).invokeExact(sobj, gs);
        } catch (Error | RuntimeException t2) {
            throw t2;
        } catch (Throwable t3) {
            throw new RuntimeException(t3);
        }
    }

    Accessors getAccessors(ScriptObject sobj) {
        try {
            Object gs = (Object) super.getGetter(Object.class).invokeExact(sobj);
            return (Accessors) gs;
        } catch (Error | RuntimeException t2) {
            throw t2;
        } catch (Throwable t3) {
            throw new RuntimeException(t3);
        }
    }

    @Override // jdk.nashorn.internal.runtime.Property
    protected Class<?> getLocalType() {
        return Object.class;
    }

    @Override // jdk.nashorn.internal.runtime.Property
    public boolean hasGetterFunction(ScriptObject sobj) {
        return getAccessors(sobj).getter != null;
    }

    @Override // jdk.nashorn.internal.runtime.Property
    public boolean hasSetterFunction(ScriptObject sobj) {
        return getAccessors(sobj).setter != null;
    }

    @Override // jdk.nashorn.internal.runtime.AccessorProperty, jdk.nashorn.internal.runtime.Property
    public int getIntValue(ScriptObject self, ScriptObject owner) {
        return ((Integer) getObjectValue(self, owner)).intValue();
    }

    @Override // jdk.nashorn.internal.runtime.AccessorProperty, jdk.nashorn.internal.runtime.Property
    public double getDoubleValue(ScriptObject self, ScriptObject owner) {
        return ((Double) getObjectValue(self, owner)).doubleValue();
    }

    @Override // jdk.nashorn.internal.runtime.AccessorProperty, jdk.nashorn.internal.runtime.Property
    public Object getObjectValue(ScriptObject self, ScriptObject owner) {
        try {
            return invokeObjectGetter(getAccessors(owner != null ? owner : self), getObjectGetterInvoker(), self);
        } catch (Error | RuntimeException t2) {
            throw t2;
        } catch (Throwable t3) {
            throw new RuntimeException(t3);
        }
    }

    @Override // jdk.nashorn.internal.runtime.AccessorProperty, jdk.nashorn.internal.runtime.Property
    public void setValue(ScriptObject self, ScriptObject owner, int value, boolean strict) {
        setValue(self, owner, Integer.valueOf(value), strict);
    }

    @Override // jdk.nashorn.internal.runtime.AccessorProperty, jdk.nashorn.internal.runtime.Property
    public void setValue(ScriptObject self, ScriptObject owner, double value, boolean strict) {
        setValue(self, owner, Double.valueOf(value), strict);
    }

    @Override // jdk.nashorn.internal.runtime.AccessorProperty, jdk.nashorn.internal.runtime.Property
    public void setValue(ScriptObject self, ScriptObject owner, Object value, boolean strict) {
        try {
            invokeObjectSetter(getAccessors(owner != null ? owner : self), getObjectSetterInvoker(), strict ? getKey() : null, self, value);
        } catch (Error | RuntimeException t2) {
            throw t2;
        } catch (Throwable t3) {
            throw new RuntimeException(t3);
        }
    }

    @Override // jdk.nashorn.internal.runtime.AccessorProperty, jdk.nashorn.internal.runtime.Property
    public MethodHandle getGetter(Class<?> type) {
        return Lookup.filterReturnType(INVOKE_OBJECT_GETTER, type);
    }

    @Override // jdk.nashorn.internal.runtime.AccessorProperty, jdk.nashorn.internal.runtime.Property
    public MethodHandle getOptimisticGetter(Class<?> type, int programPoint) {
        if (type == Integer.TYPE) {
            return INVOKE_INT_GETTER;
        }
        if (type == Double.TYPE) {
            return INVOKE_NUMBER_GETTER;
        }
        if ($assertionsDisabled || type == Object.class) {
            return INVOKE_OBJECT_GETTER;
        }
        throw new AssertionError();
    }

    @Override // jdk.nashorn.internal.runtime.SpillProperty, jdk.nashorn.internal.runtime.AccessorProperty, jdk.nashorn.internal.runtime.Property
    void initMethodHandles(Class<?> structure) {
        throw new UnsupportedOperationException();
    }

    @Override // jdk.nashorn.internal.runtime.Property
    public ScriptFunction getGetterFunction(ScriptObject sobj) {
        Object value = getAccessors(sobj).getter;
        if (value instanceof ScriptFunction) {
            return (ScriptFunction) value;
        }
        return null;
    }

    @Override // jdk.nashorn.internal.runtime.AccessorProperty, jdk.nashorn.internal.runtime.Property
    public MethodHandle getSetter(Class<?> type, PropertyMap currentMap) {
        if (type == Integer.TYPE) {
            return INVOKE_INT_SETTER;
        }
        if (type == Double.TYPE) {
            return INVOKE_NUMBER_SETTER;
        }
        if ($assertionsDisabled || type == Object.class) {
            return INVOKE_OBJECT_SETTER;
        }
        throw new AssertionError();
    }

    @Override // jdk.nashorn.internal.runtime.Property
    public ScriptFunction getSetterFunction(ScriptObject sobj) {
        Object value = getAccessors(sobj).setter;
        if (value instanceof ScriptFunction) {
            return (ScriptFunction) value;
        }
        return null;
    }

    MethodHandle getAccessorsGetter() {
        return super.getGetter(Object.class).asType(MethodType.methodType((Class<?>) Accessors.class, (Class<?>) Object.class));
    }

    private static Object invokeObjectGetter(Accessors gs, MethodHandle invoker, Object self) throws Throwable {
        Object func = gs.getter;
        if (func instanceof ScriptFunction) {
            return (Object) invoker.invokeExact(func, self);
        }
        return ScriptRuntime.UNDEFINED;
    }

    private static int invokeIntGetter(Accessors gs, MethodHandle invoker, int programPoint, Object self) throws Throwable {
        Object func = gs.getter;
        if (func instanceof ScriptFunction) {
            return (int) invoker.invokeExact(func, self);
        }
        throw new UnwarrantedOptimismException(ScriptRuntime.UNDEFINED, programPoint);
    }

    private static double invokeNumberGetter(Accessors gs, MethodHandle invoker, int programPoint, Object self) throws Throwable {
        Object func = gs.getter;
        if (func instanceof ScriptFunction) {
            return (double) invoker.invokeExact(func, self);
        }
        throw new UnwarrantedOptimismException(ScriptRuntime.UNDEFINED, programPoint);
    }

    private static void invokeObjectSetter(Accessors gs, MethodHandle invoker, String name, Object self, Object value) throws Throwable {
        Object func = gs.setter;
        if (func instanceof ScriptFunction) {
            (void) invoker.invokeExact(func, self, value);
        } else if (name != null) {
            throw ECMAErrors.typeError("property.has.no.setter", name, ScriptRuntime.safeToString(self));
        }
    }

    private static void invokeIntSetter(Accessors gs, MethodHandle invoker, String name, Object self, int value) throws Throwable {
        Object func = gs.setter;
        if (func instanceof ScriptFunction) {
            (void) invoker.invokeExact(func, self, value);
        } else if (name != null) {
            throw ECMAErrors.typeError("property.has.no.setter", name, ScriptRuntime.safeToString(self));
        }
    }

    private static void invokeNumberSetter(Accessors gs, MethodHandle invoker, String name, Object self, double value) throws Throwable {
        Object func = gs.setter;
        if (func instanceof ScriptFunction) {
            (void) invoker.invokeExact(func, self, value);
        } else if (name != null) {
            throw ECMAErrors.typeError("property.has.no.setter", name, ScriptRuntime.safeToString(self));
        }
    }

    private static MethodHandle findOwnMH_S(String name, Class<?> rtype, Class<?>... types) {
        return Lookup.MH.findStatic(LOOKUP, UserAccessorProperty.class, name, Lookup.MH.type(rtype, types));
    }
}
