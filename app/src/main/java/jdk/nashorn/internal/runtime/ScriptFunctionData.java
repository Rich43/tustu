package jdk.nashorn.internal.runtime;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import jdk.nashorn.internal.lookup.Lookup;

/* loaded from: nashorn.jar:jdk/nashorn/internal/runtime/ScriptFunctionData.class */
public abstract class ScriptFunctionData implements Serializable {
    static final int MAX_ARITY = 250;
    protected final String name;
    protected transient LinkedList<CompiledFunction> code = new LinkedList<>();
    protected int flags;
    private int arity;
    private volatile transient GenericInvokers genericInvokers;
    private static final MethodHandle BIND_VAR_ARGS;
    public static final int IS_STRICT = 1;
    public static final int IS_BUILTIN = 2;
    public static final int IS_CONSTRUCTOR = 4;
    public static final int NEEDS_CALLEE = 8;
    public static final int USES_THIS = 16;
    public static final int IS_VARIABLE_ARITY = 32;
    public static final int IS_PROPERTY_ACCESSOR = 64;
    public static final int IS_STRICT_OR_BUILTIN = 3;
    public static final int IS_BUILTIN_CONSTRUCTOR = 6;
    private static final long serialVersionUID = 4252901245508769114L;
    static final /* synthetic */ boolean $assertionsDisabled;

    abstract boolean needsCallee();

    abstract CompiledFunction getBest(MethodType methodType, ScriptObject scriptObject, Collection<CompiledFunction> collection, boolean z2);

    abstract MethodType getGenericType();

    static {
        $assertionsDisabled = !ScriptFunctionData.class.desiredAssertionStatus();
        BIND_VAR_ARGS = findOwnMH("bindVarArgs", Object[].class, Object[].class, Object[].class);
    }

    ScriptFunctionData(String name, int arity, int flags) {
        this.name = name;
        this.flags = flags;
        setArity(arity);
    }

    final int getArity() {
        return this.arity;
    }

    final boolean isVariableArity() {
        return (this.flags & 32) != 0;
    }

    final boolean isPropertyAccessor() {
        return (this.flags & 64) != 0;
    }

    void setArity(int arity) {
        if (arity < 0 || arity > 250) {
            throw new IllegalArgumentException(String.valueOf(arity));
        }
        this.arity = arity;
    }

    CompiledFunction bind(CompiledFunction originalInv, ScriptFunction fn, Object self, Object[] args) {
        MethodHandle boundInvoker = bindInvokeHandle(originalInv.createComposableInvoker(), fn, self, args);
        if (isConstructor()) {
            return new CompiledFunction(boundInvoker, bindConstructHandle(originalInv.createComposableConstructor(), fn, args), null);
        }
        return new CompiledFunction(boundInvoker);
    }

    public final boolean isStrict() {
        return (this.flags & 1) != 0;
    }

    protected String getFunctionName() {
        return getName();
    }

    final boolean isBuiltin() {
        return (this.flags & 2) != 0;
    }

    final boolean isConstructor() {
        return (this.flags & 4) != 0;
    }

    final boolean needsWrappedThis() {
        return (this.flags & 16) != 0 && (this.flags & 3) == 0;
    }

    String toSource() {
        return "function " + (this.name == null ? "" : this.name) + "() { [native code] }";
    }

    String getName() {
        return this.name;
    }

    public String toString() {
        return this.name.isEmpty() ? "<anonymous>" : this.name;
    }

    public String toStringVerbose() {
        StringBuilder sb = new StringBuilder();
        sb.append("name='").append(this.name.isEmpty() ? "<anonymous>" : this.name).append("' ").append(this.code.size()).append(" invokers=").append((Object) this.code);
        return sb.toString();
    }

    final CompiledFunction getBestInvoker(MethodType callSiteType, ScriptObject runtimeScope) {
        return getBestInvoker(callSiteType, runtimeScope, CompiledFunction.NO_FUNCTIONS);
    }

    final CompiledFunction getBestInvoker(MethodType callSiteType, ScriptObject runtimeScope, Collection<CompiledFunction> forbidden) {
        CompiledFunction cf = getBest(callSiteType, runtimeScope, forbidden);
        if ($assertionsDisabled || cf != null) {
            return cf;
        }
        throw new AssertionError();
    }

    final CompiledFunction getBestConstructor(MethodType callSiteType, ScriptObject runtimeScope, Collection<CompiledFunction> forbidden) {
        if (!isConstructor()) {
            throw ECMAErrors.typeError("not.a.constructor", toSource());
        }
        CompiledFunction cf = getBest(callSiteType.insertParameterTypes(1, Object.class), runtimeScope, forbidden);
        return cf;
    }

    protected void ensureCompiled() {
    }

    final MethodHandle getGenericInvoker(ScriptObject runtimeScope) {
        GenericInvokers lgenericInvokers = ensureGenericInvokers();
        MethodHandle invoker = lgenericInvokers.invoker;
        if (invoker == null) {
            MethodHandle methodHandleCreateGenericInvoker = createGenericInvoker(runtimeScope);
            invoker = methodHandleCreateGenericInvoker;
            lgenericInvokers.invoker = methodHandleCreateGenericInvoker;
        }
        return invoker;
    }

    private MethodHandle createGenericInvoker(ScriptObject runtimeScope) {
        return makeGenericMethod(getGeneric(runtimeScope).createComposableInvoker());
    }

    final MethodHandle getGenericConstructor(ScriptObject runtimeScope) {
        GenericInvokers lgenericInvokers = ensureGenericInvokers();
        MethodHandle constructor = lgenericInvokers.constructor;
        if (constructor == null) {
            MethodHandle methodHandleCreateGenericConstructor = createGenericConstructor(runtimeScope);
            constructor = methodHandleCreateGenericConstructor;
            lgenericInvokers.constructor = methodHandleCreateGenericConstructor;
        }
        return constructor;
    }

    private MethodHandle createGenericConstructor(ScriptObject runtimeScope) {
        return makeGenericMethod(getGeneric(runtimeScope).createComposableConstructor());
    }

    private GenericInvokers ensureGenericInvokers() {
        GenericInvokers lgenericInvokers = this.genericInvokers;
        if (lgenericInvokers == null) {
            GenericInvokers genericInvokers = new GenericInvokers();
            lgenericInvokers = genericInvokers;
            this.genericInvokers = genericInvokers;
        }
        return lgenericInvokers;
    }

    private static MethodType widen(MethodType cftype) {
        Class<?>[] paramTypes = new Class[cftype.parameterCount()];
        for (int i2 = 0; i2 < cftype.parameterCount(); i2++) {
            paramTypes[i2] = cftype.parameterType(i2).isPrimitive() ? cftype.parameterType(i2) : Object.class;
        }
        return Lookup.MH.type(cftype.returnType(), paramTypes);
    }

    CompiledFunction lookupExactApplyToCall(MethodType type) {
        Iterator<CompiledFunction> it = this.code.iterator();
        while (it.hasNext()) {
            CompiledFunction cf = it.next();
            if (cf.isApplyToCall()) {
                MethodType cftype = cf.type();
                if (cftype.parameterCount() == type.parameterCount() && widen(cftype).equals((Object) widen(type))) {
                    return cf;
                }
            }
        }
        return null;
    }

    CompiledFunction pickFunction(MethodType callSiteType, boolean canPickVarArg) {
        Iterator<CompiledFunction> it = this.code.iterator();
        while (it.hasNext()) {
            CompiledFunction candidate = it.next();
            if (candidate.matchesCallSite(callSiteType, canPickVarArg)) {
                return candidate;
            }
        }
        return null;
    }

    final CompiledFunction getBest(MethodType callSiteType, ScriptObject runtimeScope, Collection<CompiledFunction> forbidden) {
        return getBest(callSiteType, runtimeScope, forbidden, true);
    }

    boolean isValidCallSite(MethodType callSiteType) {
        return callSiteType.parameterCount() >= 2 && callSiteType.parameterType(0).isAssignableFrom(ScriptFunction.class);
    }

    CompiledFunction getGeneric(ScriptObject runtimeScope) {
        return getBest(getGenericType(), runtimeScope, CompiledFunction.NO_FUNCTIONS, false);
    }

    ScriptObject allocate(PropertyMap map) {
        return null;
    }

    PropertyMap getAllocatorMap(ScriptObject prototype) {
        return null;
    }

    ScriptFunctionData makeBoundFunctionData(ScriptFunction fn, Object self, Object[] args) {
        Object[] allArgs = args == null ? ScriptRuntime.EMPTY_ARRAY : args;
        int length = args == null ? 0 : args.length;
        int boundFlags = this.flags & (-9) & (-17);
        List<CompiledFunction> boundList = new LinkedList<>();
        ScriptObject runtimeScope = fn.getScope();
        CompiledFunction bindTarget = new CompiledFunction(getGenericInvoker(runtimeScope), getGenericConstructor(runtimeScope), null);
        boundList.add(bind(bindTarget, fn, self, allArgs));
        return new FinalScriptFunctionData(this.name, Math.max(0, getArity() - length), boundList, boundFlags);
    }

    private Object convertThisObject(Object thiz) {
        return needsWrappedThis() ? wrapThis(thiz) : thiz;
    }

    static Object wrapThis(Object thiz) {
        if (!(thiz instanceof ScriptObject)) {
            if (JSType.nullOrUndefined(thiz)) {
                return Context.getGlobal();
            }
            if (isPrimitiveThis(thiz)) {
                return Context.getGlobal().wrapAsObject(thiz);
            }
        }
        return thiz;
    }

    static boolean isPrimitiveThis(Object obj) {
        return JSType.isString(obj) || (obj instanceof Number) || (obj instanceof Boolean);
    }

    private MethodHandle bindInvokeHandle(MethodHandle originalInvoker, ScriptFunction targetFn, Object self, Object[] args) {
        MethodHandle boundInvoker;
        MethodHandle noArgBoundInvoker;
        boolean isTargetBound = targetFn.isBoundFunction();
        boolean needsCallee = needsCallee(originalInvoker);
        if (!$assertionsDisabled && needsCallee != needsCallee()) {
            throw new AssertionError((Object) "callee contract violation 2");
        }
        if (!$assertionsDisabled && isTargetBound && needsCallee) {
            throw new AssertionError();
        }
        Object boundSelf = isTargetBound ? null : convertThisObject(self);
        if (isVarArg(originalInvoker)) {
            if (isTargetBound) {
                noArgBoundInvoker = originalInvoker;
            } else if (needsCallee) {
                noArgBoundInvoker = Lookup.MH.insertArguments(originalInvoker, 0, targetFn, boundSelf);
            } else {
                noArgBoundInvoker = Lookup.MH.bindTo(originalInvoker, boundSelf);
            }
            if (args.length > 0) {
                boundInvoker = varArgBinder(noArgBoundInvoker, args);
            } else {
                boundInvoker = noArgBoundInvoker;
            }
        } else {
            int argInsertPos = isTargetBound ? 1 : 0;
            Object[] boundArgs = new Object[Math.min(originalInvoker.type().parameterCount() - argInsertPos, args.length + (isTargetBound ? 0 : needsCallee ? 2 : 1))];
            int next = 0;
            if (!isTargetBound) {
                if (needsCallee) {
                    next = 0 + 1;
                    boundArgs[0] = targetFn;
                }
                int i2 = next;
                next++;
                boundArgs[i2] = boundSelf;
            }
            System.arraycopy(args, 0, boundArgs, next, boundArgs.length - next);
            boundInvoker = Lookup.MH.insertArguments(originalInvoker, argInsertPos, boundArgs);
        }
        if (isTargetBound) {
            return boundInvoker;
        }
        return Lookup.MH.dropArguments(boundInvoker, 0, Object.class);
    }

    private static MethodHandle bindConstructHandle(MethodHandle originalConstructor, ScriptFunction fn, Object[] args) {
        Object[] boundArgs;
        if (!$assertionsDisabled && originalConstructor == null) {
            throw new AssertionError();
        }
        MethodHandle calleeBoundConstructor = fn.isBoundFunction() ? originalConstructor : Lookup.MH.dropArguments(Lookup.MH.bindTo(originalConstructor, fn), 0, ScriptFunction.class);
        if (args.length == 0) {
            return calleeBoundConstructor;
        }
        if (isVarArg(calleeBoundConstructor)) {
            return varArgBinder(calleeBoundConstructor, args);
        }
        int maxArgCount = calleeBoundConstructor.type().parameterCount() - 1;
        if (args.length <= maxArgCount) {
            boundArgs = args;
        } else {
            boundArgs = new Object[maxArgCount];
            System.arraycopy(args, 0, boundArgs, 0, maxArgCount);
        }
        return Lookup.MH.insertArguments(calleeBoundConstructor, 1, boundArgs);
    }

    private static MethodHandle makeGenericMethod(MethodHandle mh) {
        MethodType type = mh.type();
        MethodType newType = makeGenericType(type);
        return type.equals((Object) newType) ? mh : mh.asType(newType);
    }

    private static MethodType makeGenericType(MethodType type) {
        MethodType newType = type.generic();
        if (isVarArg(type)) {
            newType = newType.changeParameterType(type.parameterCount() - 1, Object[].class);
        }
        if (needsCallee(type)) {
            newType = newType.changeParameterType(0, ScriptFunction.class);
        }
        return newType;
    }

    Object invoke(ScriptFunction fn, Object self, Object... arguments) throws Throwable {
        MethodHandle mh = getGenericInvoker(fn.getScope());
        Object selfObj = convertThisObject(self);
        Object[] args = arguments == null ? ScriptRuntime.EMPTY_ARRAY : arguments;
        DebuggerSupport.notifyInvoke(mh);
        if (isVarArg(mh)) {
            if (needsCallee(mh)) {
                return (Object) mh.invokeExact(fn, selfObj, args);
            }
            return (Object) mh.invokeExact(selfObj, args);
        }
        int paramCount = mh.type().parameterCount();
        if (needsCallee(mh)) {
            switch (paramCount) {
                case 2:
                    return (Object) mh.invokeExact(fn, selfObj);
                case 3:
                    return (Object) mh.invokeExact(fn, selfObj, getArg(args, 0));
                case 4:
                    return (Object) mh.invokeExact(fn, selfObj, getArg(args, 0), getArg(args, 1));
                case 5:
                    return (Object) mh.invokeExact(fn, selfObj, getArg(args, 0), getArg(args, 1), getArg(args, 2));
                case 6:
                    return (Object) mh.invokeExact(fn, selfObj, getArg(args, 0), getArg(args, 1), getArg(args, 2), getArg(args, 3));
                case 7:
                    return (Object) mh.invokeExact(fn, selfObj, getArg(args, 0), getArg(args, 1), getArg(args, 2), getArg(args, 3), getArg(args, 4));
                case 8:
                    return (Object) mh.invokeExact(fn, selfObj, getArg(args, 0), getArg(args, 1), getArg(args, 2), getArg(args, 3), getArg(args, 4), getArg(args, 5));
                default:
                    return mh.invokeWithArguments(withArguments(fn, selfObj, paramCount, args));
            }
        }
        switch (paramCount) {
            case 1:
                return (Object) mh.invokeExact(selfObj);
            case 2:
                return (Object) mh.invokeExact(selfObj, getArg(args, 0));
            case 3:
                return (Object) mh.invokeExact(selfObj, getArg(args, 0), getArg(args, 1));
            case 4:
                return (Object) mh.invokeExact(selfObj, getArg(args, 0), getArg(args, 1), getArg(args, 2));
            case 5:
                return (Object) mh.invokeExact(selfObj, getArg(args, 0), getArg(args, 1), getArg(args, 2), getArg(args, 3));
            case 6:
                return (Object) mh.invokeExact(selfObj, getArg(args, 0), getArg(args, 1), getArg(args, 2), getArg(args, 3), getArg(args, 4));
            case 7:
                return (Object) mh.invokeExact(selfObj, getArg(args, 0), getArg(args, 1), getArg(args, 2), getArg(args, 3), getArg(args, 4), getArg(args, 5));
            default:
                return mh.invokeWithArguments(withArguments(null, selfObj, paramCount, args));
        }
    }

    Object construct(ScriptFunction fn, Object... arguments) throws Throwable {
        MethodHandle mh = getGenericConstructor(fn.getScope());
        Object[] args = arguments == null ? ScriptRuntime.EMPTY_ARRAY : arguments;
        DebuggerSupport.notifyInvoke(mh);
        if (isVarArg(mh)) {
            if (needsCallee(mh)) {
                return (Object) mh.invokeExact(fn, args);
            }
            return (Object) mh.invokeExact(args);
        }
        int paramCount = mh.type().parameterCount();
        if (needsCallee(mh)) {
            switch (paramCount) {
                case 1:
                    return (Object) mh.invokeExact(fn);
                case 2:
                    return (Object) mh.invokeExact(fn, getArg(args, 0));
                case 3:
                    return (Object) mh.invokeExact(fn, getArg(args, 0), getArg(args, 1));
                case 4:
                    return (Object) mh.invokeExact(fn, getArg(args, 0), getArg(args, 1), getArg(args, 2));
                case 5:
                    return (Object) mh.invokeExact(fn, getArg(args, 0), getArg(args, 1), getArg(args, 2), getArg(args, 3));
                case 6:
                    return (Object) mh.invokeExact(fn, getArg(args, 0), getArg(args, 1), getArg(args, 2), getArg(args, 3), getArg(args, 4));
                case 7:
                    return (Object) mh.invokeExact(fn, getArg(args, 0), getArg(args, 1), getArg(args, 2), getArg(args, 3), getArg(args, 4), getArg(args, 5));
                default:
                    return mh.invokeWithArguments(withArguments(fn, paramCount, args));
            }
        }
        switch (paramCount) {
            case 0:
                return (Object) mh.invokeExact();
            case 1:
                return (Object) mh.invokeExact(getArg(args, 0));
            case 2:
                return (Object) mh.invokeExact(getArg(args, 0), getArg(args, 1));
            case 3:
                return (Object) mh.invokeExact(getArg(args, 0), getArg(args, 1), getArg(args, 2));
            case 4:
                return (Object) mh.invokeExact(getArg(args, 0), getArg(args, 1), getArg(args, 2), getArg(args, 3));
            case 5:
                return (Object) mh.invokeExact(getArg(args, 0), getArg(args, 1), getArg(args, 2), getArg(args, 3), getArg(args, 4));
            case 6:
                return (Object) mh.invokeExact(getArg(args, 0), getArg(args, 1), getArg(args, 2), getArg(args, 3), getArg(args, 4), getArg(args, 5));
            default:
                return mh.invokeWithArguments(withArguments(null, paramCount, args));
        }
    }

    private static Object getArg(Object[] args, int i2) {
        return i2 < args.length ? args[i2] : ScriptRuntime.UNDEFINED;
    }

    private static Object[] withArguments(ScriptFunction fn, int argCount, Object[] args) {
        Object[] finalArgs = new Object[argCount];
        int nextArg = 0;
        if (fn != null) {
            nextArg = 0 + 1;
            finalArgs[0] = fn;
        }
        int i2 = 0;
        while (i2 < args.length && nextArg < argCount) {
            int i3 = nextArg;
            nextArg++;
            int i4 = i2;
            i2++;
            finalArgs[i3] = args[i4];
        }
        while (nextArg < argCount) {
            int i5 = nextArg;
            nextArg++;
            finalArgs[i5] = ScriptRuntime.UNDEFINED;
        }
        return finalArgs;
    }

    private static Object[] withArguments(ScriptFunction fn, Object self, int argCount, Object[] args) {
        Object[] finalArgs = new Object[argCount];
        int nextArg = 0;
        if (fn != null) {
            nextArg = 0 + 1;
            finalArgs[0] = fn;
        }
        int i2 = nextArg;
        int nextArg2 = nextArg + 1;
        finalArgs[i2] = self;
        int i3 = 0;
        while (i3 < args.length && nextArg2 < argCount) {
            int i4 = nextArg2;
            nextArg2++;
            int i5 = i3;
            i3++;
            finalArgs[i4] = args[i5];
        }
        while (nextArg2 < argCount) {
            int i6 = nextArg2;
            nextArg2++;
            finalArgs[i6] = ScriptRuntime.UNDEFINED;
        }
        return finalArgs;
    }

    private static MethodHandle varArgBinder(MethodHandle mh, Object[] args) {
        if (!$assertionsDisabled && args == null) {
            throw new AssertionError();
        }
        if ($assertionsDisabled || args.length > 0) {
            return Lookup.MH.filterArguments(mh, mh.type().parameterCount() - 1, Lookup.MH.bindTo(BIND_VAR_ARGS, args));
        }
        throw new AssertionError();
    }

    protected static boolean needsCallee(MethodHandle mh) {
        return needsCallee(mh.type());
    }

    static boolean needsCallee(MethodType type) {
        int length = type.parameterCount();
        if (length == 0) {
            return false;
        }
        Class<?> param0 = type.parameterType(0);
        return param0 == ScriptFunction.class || (param0 == Boolean.TYPE && length > 1 && type.parameterType(1) == ScriptFunction.class);
    }

    protected static boolean isVarArg(MethodHandle mh) {
        return isVarArg(mh.type());
    }

    static boolean isVarArg(MethodType type) {
        return type.parameterType(type.parameterCount() - 1).isArray();
    }

    public boolean inDynamicContext() {
        return false;
    }

    private static Object[] bindVarArgs(Object[] array1, Object[] array2) {
        if (array2 == null) {
            return (Object[]) array1.clone();
        }
        int l2 = array2.length;
        if (l2 == 0) {
            return (Object[]) array1.clone();
        }
        int l1 = array1.length;
        Object[] concat = new Object[l1 + l2];
        System.arraycopy(array1, 0, concat, 0, l1);
        System.arraycopy(array2, 0, concat, l1, l2);
        return concat;
    }

    private static MethodHandle findOwnMH(String name, Class<?> rtype, Class<?>... types) {
        return Lookup.MH.findStatic(MethodHandles.lookup(), ScriptFunctionData.class, name, Lookup.MH.type(rtype, types));
    }

    /* loaded from: nashorn.jar:jdk/nashorn/internal/runtime/ScriptFunctionData$GenericInvokers.class */
    private static final class GenericInvokers {
        volatile MethodHandle invoker;
        volatile MethodHandle constructor;

        private GenericInvokers() {
        }
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        this.code = new LinkedList<>();
    }
}
