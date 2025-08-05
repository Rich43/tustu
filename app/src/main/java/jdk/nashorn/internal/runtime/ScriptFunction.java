package jdk.nashorn.internal.runtime;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.invoke.SwitchPoint;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.atomic.LongAdder;
import jdk.internal.dynalink.CallSiteDescriptor;
import jdk.internal.dynalink.linker.GuardedInvocation;
import jdk.internal.dynalink.linker.LinkRequest;
import jdk.internal.dynalink.support.Guards;
import jdk.nashorn.internal.codegen.ApplySpecialization;
import jdk.nashorn.internal.codegen.CompilerConstants;
import jdk.nashorn.internal.lookup.Lookup;
import jdk.nashorn.internal.objects.Global;
import jdk.nashorn.internal.objects.NativeFunction;
import jdk.nashorn.internal.objects.annotations.SpecializedFunction;
import jdk.nashorn.internal.runtime.linker.Bootstrap;
import jdk.nashorn.internal.runtime.linker.NashornCallSiteDescriptor;

/* loaded from: nashorn.jar:jdk/nashorn/internal/runtime/ScriptFunction.class */
public class ScriptFunction extends ScriptObject {
    public static final MethodHandle G$PROTOTYPE;
    public static final MethodHandle S$PROTOTYPE;
    public static final MethodHandle G$LENGTH;
    public static final MethodHandle G$NAME;
    public static final MethodHandle INVOKE_SYNC;
    static final MethodHandle ALLOCATE;
    private static final MethodHandle WRAPFILTER;
    private static final MethodHandle SCRIPTFUNCTION_GLOBALFILTER;
    public static final CompilerConstants.Call GET_SCOPE;
    private static final MethodHandle IS_FUNCTION_MH;
    private static final MethodHandle IS_APPLY_FUNCTION;
    private static final MethodHandle IS_NONSTRICT_FUNCTION;
    private static final MethodHandle ADD_ZEROTH_ELEMENT;
    private static final MethodHandle WRAP_THIS;
    private static final PropertyMap anonmap$;
    private static final PropertyMap strictmodemap$;
    private static final PropertyMap boundfunctionmap$;
    private static final PropertyMap map$;
    private static final Object LAZY_PROTOTYPE;
    private final ScriptObject scope;
    private final ScriptFunctionData data;
    protected PropertyMap allocatorMap;
    protected Object prototype;
    private static LongAdder constructorCount;
    private static LongAdder invokes;
    private static LongAdder allocations;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !ScriptFunction.class.desiredAssertionStatus();
        G$PROTOTYPE = findOwnMH_S("G$prototype", Object.class, Object.class);
        S$PROTOTYPE = findOwnMH_S("S$prototype", Void.TYPE, Object.class, Object.class);
        G$LENGTH = findOwnMH_S("G$length", Integer.TYPE, Object.class);
        G$NAME = findOwnMH_S("G$name", Object.class, Object.class);
        INVOKE_SYNC = findOwnMH_S("invokeSync", Object.class, ScriptFunction.class, Object.class, Object.class, Object[].class);
        ALLOCATE = findOwnMH_V("allocate", Object.class, new Class[0]);
        WRAPFILTER = findOwnMH_S("wrapFilter", Object.class, Object.class);
        SCRIPTFUNCTION_GLOBALFILTER = findOwnMH_S("globalFilter", Object.class, Object.class);
        GET_SCOPE = CompilerConstants.virtualCallNoLookup(ScriptFunction.class, "getScope", ScriptObject.class, new Class[0]);
        IS_FUNCTION_MH = findOwnMH_S("isFunctionMH", Boolean.TYPE, Object.class, ScriptFunctionData.class);
        IS_APPLY_FUNCTION = findOwnMH_S("isApplyFunction", Boolean.TYPE, Boolean.TYPE, Object.class, Object.class);
        IS_NONSTRICT_FUNCTION = findOwnMH_S("isNonStrictFunction", Boolean.TYPE, Object.class, Object.class, ScriptFunctionData.class);
        ADD_ZEROTH_ELEMENT = findOwnMH_S("addZerothElement", Object[].class, Object[].class, Object.class);
        WRAP_THIS = Lookup.MH.findStatic(MethodHandles.lookup(), ScriptFunctionData.class, "wrapThis", Lookup.MH.type(Object.class, Object.class));
        LAZY_PROTOTYPE = new Object();
        anonmap$ = PropertyMap.newMap();
        ArrayList<Property> properties = new ArrayList<>(3);
        properties.add(AccessorProperty.create("prototype", 6, G$PROTOTYPE, S$PROTOTYPE));
        properties.add(AccessorProperty.create("length", 7, G$LENGTH, null));
        properties.add(AccessorProperty.create("name", 7, G$NAME, null));
        map$ = PropertyMap.newMap(properties);
        strictmodemap$ = createStrictModeMap(map$);
        boundfunctionmap$ = createBoundFunctionMap(strictmodemap$);
        if (Context.DEBUG) {
            constructorCount = new LongAdder();
            invokes = new LongAdder();
            allocations = new LongAdder();
        }
    }

    private static PropertyMap createStrictModeMap(PropertyMap map) {
        PropertyMap newMap = map.addPropertyNoHistory(map.newUserAccessors("arguments", 6));
        return newMap.addPropertyNoHistory(map.newUserAccessors("caller", 6));
    }

    private static PropertyMap createBoundFunctionMap(PropertyMap strictModeMap) {
        return strictModeMap.deleteProperty(strictModeMap.findProperty("prototype"));
    }

    private static boolean isStrict(int flags) {
        return (flags & 1) != 0;
    }

    private static PropertyMap getMap(boolean strict) {
        return strict ? strictmodemap$ : map$;
    }

    private ScriptFunction(ScriptFunctionData data, PropertyMap map, ScriptObject scope, Global global) {
        super(map);
        if (Context.DEBUG) {
            constructorCount.increment();
        }
        this.data = data;
        this.scope = scope;
        setInitialProto(global.getFunctionPrototype());
        this.prototype = LAZY_PROTOTYPE;
        if (!$assertionsDisabled && this.objectSpill != null) {
            throw new AssertionError();
        }
        if (isStrict() || isBoundFunction()) {
            ScriptFunction typeErrorThrower = global.getTypeErrorThrower();
            initUserAccessors("arguments", 6, typeErrorThrower, typeErrorThrower);
            initUserAccessors("caller", 6, typeErrorThrower, typeErrorThrower);
        }
    }

    private ScriptFunction(String name, MethodHandle methodHandle, PropertyMap map, ScriptObject scope, Specialization[] specs, int flags, Global global) {
        this(new FinalScriptFunctionData(name, methodHandle, specs, flags), map, scope, global);
    }

    private ScriptFunction(String name, MethodHandle methodHandle, ScriptObject scope, Specialization[] specs, int flags) {
        this(name, methodHandle, getMap(isStrict(flags)), scope, specs, flags, Global.instance());
    }

    protected ScriptFunction(String name, MethodHandle invokeHandle, Specialization[] specs) {
        this(name, invokeHandle, map$, null, specs, 6, Global.instance());
    }

    protected ScriptFunction(String name, MethodHandle invokeHandle, PropertyMap map, Specialization[] specs) {
        this(name, invokeHandle, map.addAll(map$), null, specs, 6, Global.instance());
    }

    public static ScriptFunction create(Object[] constants, int index, ScriptObject scope) {
        RecompilableScriptFunctionData data = (RecompilableScriptFunctionData) constants[index];
        return new ScriptFunction(data, getMap(data.isStrict()), scope, Global.instance());
    }

    public static ScriptFunction create(Object[] constants, int index) {
        return create(constants, index, null);
    }

    public static ScriptFunction createAnonymous() {
        return new ScriptFunction("", GlobalFunctions.ANONYMOUS, anonmap$, (Specialization[]) null);
    }

    private static ScriptFunction createBuiltin(String name, MethodHandle methodHandle, Specialization[] specs, int flags) {
        ScriptFunction func = new ScriptFunction(name, methodHandle, (ScriptObject) null, specs, flags);
        func.setPrototype(ScriptRuntime.UNDEFINED);
        func.deleteOwnProperty(func.getMap().findProperty("prototype"));
        return func;
    }

    public static ScriptFunction createBuiltin(String name, MethodHandle methodHandle, Specialization[] specs) {
        return createBuiltin(name, methodHandle, specs, 2);
    }

    public static ScriptFunction createBuiltin(String name, MethodHandle methodHandle) {
        return createBuiltin(name, methodHandle, null);
    }

    public static ScriptFunction createStrictBuiltin(String name, MethodHandle methodHandle) {
        return createBuiltin(name, methodHandle, null, 3);
    }

    /* loaded from: nashorn.jar:jdk/nashorn/internal/runtime/ScriptFunction$Bound.class */
    private static class Bound extends ScriptFunction {
        private final ScriptFunction target;

        Bound(ScriptFunctionData boundData, ScriptFunction target) {
            super(boundData, ScriptFunction.boundfunctionmap$, (ScriptObject) null, Global.instance());
            setPrototype(ScriptRuntime.UNDEFINED);
            this.target = target;
        }

        @Override // jdk.nashorn.internal.runtime.ScriptFunction
        protected ScriptFunction getTargetFunction() {
            return this.target;
        }
    }

    public final ScriptFunction createBound(Object self, Object[] args) {
        return new Bound(this.data.makeBoundFunctionData(this, self, args), getTargetFunction());
    }

    public final ScriptFunction createSynchronized(Object sync) {
        MethodHandle mh = Lookup.MH.insertArguments(INVOKE_SYNC, 0, this, sync);
        return createBuiltin(getName(), mh);
    }

    @Override // jdk.nashorn.internal.runtime.ScriptObject
    public String getClassName() {
        return "Function";
    }

    @Override // jdk.nashorn.internal.runtime.ScriptObject
    public boolean isInstance(ScriptObject instance) {
        Object basePrototype = getTargetFunction().getPrototype();
        if (!(basePrototype instanceof ScriptObject)) {
            throw ECMAErrors.typeError("prototype.not.an.object", ScriptRuntime.safeToString(getTargetFunction()), ScriptRuntime.safeToString(basePrototype));
        }
        ScriptObject proto = instance.getProto();
        while (true) {
            ScriptObject proto2 = proto;
            if (proto2 != null) {
                if (proto2 != basePrototype) {
                    proto = proto2.getProto();
                } else {
                    return true;
                }
            } else {
                return false;
            }
        }
    }

    protected ScriptFunction getTargetFunction() {
        return this;
    }

    final boolean isBoundFunction() {
        return getTargetFunction() != this;
    }

    public final void setArity(int arity) {
        this.data.setArity(arity);
    }

    public final boolean isStrict() {
        return this.data.isStrict();
    }

    public final boolean needsWrappedThis() {
        return this.data.needsWrappedThis();
    }

    private static boolean needsWrappedThis(Object fn) {
        if (fn instanceof ScriptFunction) {
            return ((ScriptFunction) fn).needsWrappedThis();
        }
        return false;
    }

    final Object invoke(Object self, Object... arguments) throws Throwable {
        if (Context.DEBUG) {
            invokes.increment();
        }
        return this.data.invoke(this, self, arguments);
    }

    final Object construct(Object... arguments) throws Throwable {
        return this.data.construct(this, arguments);
    }

    private Object allocate() {
        if (Context.DEBUG) {
            allocations.increment();
        }
        if (!$assertionsDisabled && isBoundFunction()) {
            throw new AssertionError();
        }
        ScriptObject prototype = getAllocatorPrototype();
        ScriptObject object = this.data.allocate(getAllocatorMap(prototype));
        if (object != null) {
            object.setInitialProto(prototype);
        }
        return object;
    }

    private synchronized PropertyMap getAllocatorMap(ScriptObject prototype) {
        if (this.allocatorMap == null || this.allocatorMap.isInvalidSharedMapFor(prototype)) {
            this.allocatorMap = this.data.getAllocatorMap(prototype);
        }
        return this.allocatorMap;
    }

    private ScriptObject getAllocatorPrototype() {
        Object prototype = getPrototype();
        if (prototype instanceof ScriptObject) {
            return (ScriptObject) prototype;
        }
        return Global.objectPrototype();
    }

    @Override // jdk.nashorn.internal.runtime.ScriptObject
    public final String safeToString() {
        return toSource();
    }

    public final String toString() {
        return this.data.toString();
    }

    public final String toSource() {
        return this.data.toSource();
    }

    public final Object getPrototype() {
        if (this.prototype == LAZY_PROTOTYPE) {
            this.prototype = new PrototypeObject(this);
        }
        return this.prototype;
    }

    public final synchronized void setPrototype(Object newPrototype) {
        if ((newPrototype instanceof ScriptObject) && newPrototype != this.prototype && this.allocatorMap != null) {
            this.allocatorMap = null;
        }
        this.prototype = newPrototype;
    }

    public final MethodHandle getBoundInvokeHandle(Object self) {
        return Lookup.MH.bindTo(bindToCalleeIfNeeded(this.data.getGenericInvoker(this.scope)), self);
    }

    private MethodHandle bindToCalleeIfNeeded(MethodHandle methodHandle) {
        return ScriptFunctionData.needsCallee(methodHandle) ? Lookup.MH.bindTo(methodHandle, this) : methodHandle;
    }

    public final String getName() {
        return this.data.getName();
    }

    public final ScriptObject getScope() {
        return this.scope;
    }

    public static Object G$prototype(Object self) {
        return self instanceof ScriptFunction ? ((ScriptFunction) self).getPrototype() : ScriptRuntime.UNDEFINED;
    }

    public static void S$prototype(Object self, Object prototype) {
        if (self instanceof ScriptFunction) {
            ((ScriptFunction) self).setPrototype(prototype);
        }
    }

    public static int G$length(Object self) {
        if (self instanceof ScriptFunction) {
            return ((ScriptFunction) self).data.getArity();
        }
        return 0;
    }

    public static Object G$name(Object self) {
        if (self instanceof ScriptFunction) {
            return ((ScriptFunction) self).getName();
        }
        return ScriptRuntime.UNDEFINED;
    }

    public static ScriptObject getPrototype(ScriptFunction constructor) {
        if (constructor != null) {
            Object proto = constructor.getPrototype();
            if (proto instanceof ScriptObject) {
                return (ScriptObject) proto;
            }
            return null;
        }
        return null;
    }

    public static long getConstructorCount() {
        return constructorCount.longValue();
    }

    public static long getInvokes() {
        return invokes.longValue();
    }

    public static long getAllocations() {
        return allocations.longValue();
    }

    @Override // jdk.nashorn.internal.runtime.ScriptObject
    protected GuardedInvocation findNewMethod(CallSiteDescriptor desc, LinkRequest request) {
        MethodType type = desc.getMethodType();
        if (!$assertionsDisabled && (desc.getMethodType().returnType() != Object.class || NashornCallSiteDescriptor.isOptimistic(desc))) {
            throw new AssertionError();
        }
        CompiledFunction cf = this.data.getBestConstructor(type, this.scope, CompiledFunction.NO_FUNCTIONS);
        GuardedInvocation bestCtorInv = cf.createConstructorInvocation();
        return new GuardedInvocation(pairArguments(bestCtorInv.getInvocation(), type), getFunctionGuard(this, cf.getFlags()), bestCtorInv.getSwitchPoints(), (Class<? extends Throwable>) null);
    }

    private static Object wrapFilter(Object obj) {
        if ((obj instanceof ScriptObject) || !ScriptFunctionData.isPrimitiveThis(obj)) {
            return obj;
        }
        return Context.getGlobal().wrapAsObject(obj);
    }

    private static Object globalFilter(Object object) {
        return Context.getGlobal();
    }

    private static SpecializedFunction.LinkLogic getLinkLogic(Object self, Class<? extends SpecializedFunction.LinkLogic> linkLogicClass) {
        if (linkLogicClass == null) {
            return SpecializedFunction.LinkLogic.EMPTY_INSTANCE;
        }
        if (!Context.getContextTrusted().getEnv()._optimistic_types) {
            return null;
        }
        Object wrappedSelf = wrapFilter(self);
        if (wrappedSelf instanceof OptimisticBuiltins) {
            if (wrappedSelf != self && ((OptimisticBuiltins) wrappedSelf).hasPerInstanceAssumptions()) {
                return null;
            }
            return ((OptimisticBuiltins) wrappedSelf).getLinkLogic(linkLogicClass);
        }
        return null;
    }

    /* JADX WARN: Code restructure failed: missing block: B:100:0x035c, code lost:
    
        r3 = getFunctionGuard(r9, r22.getFlags());
     */
    /* JADX WARN: Code restructure failed: missing block: B:101:0x0368, code lost:
    
        r3 = r20;
     */
    /* JADX WARN: Code restructure failed: missing block: B:103:0x0371, code lost:
    
        return new jdk.internal.dynalink.linker.GuardedInvocation(r0, r3, r29, r26);
     */
    /* JADX WARN: Code restructure failed: missing block: B:59:0x01c5, code lost:
    
        r0 = r22.createFunctionInvocation(r0.returnType(), r21);
        r0 = r0.getInvocation();
     */
    /* JADX WARN: Code restructure failed: missing block: B:60:0x01e0, code lost:
    
        if (r9.data.needsCallee() == false) goto L67;
     */
    /* JADX WARN: Code restructure failed: missing block: B:62:0x01e5, code lost:
    
        if (r0 == false) goto L66;
     */
    /* JADX WARN: Code restructure failed: missing block: B:64:0x01ec, code lost:
    
        if (needsWrappedThis() == false) goto L66;
     */
    /* JADX WARN: Code restructure failed: missing block: B:65:0x01ef, code lost:
    
        r19 = jdk.nashorn.internal.lookup.Lookup.MH.filterArguments(r0, 1, jdk.nashorn.internal.runtime.ScriptFunction.SCRIPTFUNCTION_GLOBALFILTER);
     */
    /* JADX WARN: Code restructure failed: missing block: B:66:0x0209, code lost:
    
        r19 = r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:68:0x0217, code lost:
    
        if (r9.data.isBuiltin() == false) goto L72;
     */
    /* JADX WARN: Code restructure failed: missing block: B:70:0x0226, code lost:
    
        if ("extend".equals(r9.data.getName()) == false) goto L72;
     */
    /* JADX WARN: Code restructure failed: missing block: B:71:0x0229, code lost:
    
        r19 = jdk.nashorn.internal.lookup.Lookup.MH.dropArguments(jdk.nashorn.internal.lookup.Lookup.MH.bindTo(r0, r10.getLookup()), 0, r0.parameterType(0), r0.parameterType(1));
     */
    /* JADX WARN: Code restructure failed: missing block: B:73:0x025d, code lost:
    
        if (r0 == false) goto L77;
     */
    /* JADX WARN: Code restructure failed: missing block: B:75:0x0264, code lost:
    
        if (needsWrappedThis() == false) goto L77;
     */
    /* JADX WARN: Code restructure failed: missing block: B:76:0x0267, code lost:
    
        r0 = jdk.nashorn.internal.lookup.Lookup.MH.filterArguments(r0, 0, jdk.nashorn.internal.runtime.ScriptFunction.SCRIPTFUNCTION_GLOBALFILTER);
        r19 = jdk.nashorn.internal.lookup.Lookup.MH.dropArguments(r0, 0, r0.parameterType(0));
     */
    /* JADX WARN: Code restructure failed: missing block: B:77:0x029a, code lost:
    
        r19 = jdk.nashorn.internal.lookup.Lookup.MH.dropArguments(r0, 0, r0.parameterType(0));
     */
    /* JADX WARN: Code restructure failed: missing block: B:79:0x02b5, code lost:
    
        if (r0 != false) goto L86;
     */
    /* JADX WARN: Code restructure failed: missing block: B:81:0x02bc, code lost:
    
        if (needsWrappedThis() == false) goto L86;
     */
    /* JADX WARN: Code restructure failed: missing block: B:83:0x02ca, code lost:
    
        if (jdk.nashorn.internal.runtime.ScriptFunctionData.isPrimitiveThis(r11.getArguments()[1]) == false) goto L85;
     */
    /* JADX WARN: Code restructure failed: missing block: B:84:0x02cd, code lost:
    
        r19 = jdk.nashorn.internal.lookup.Lookup.MH.filterArguments(r19, 1, jdk.nashorn.internal.runtime.ScriptFunction.WRAPFILTER);
     */
    /* JADX WARN: Code restructure failed: missing block: B:85:0x02e7, code lost:
    
        r20 = getNonStrictFunctionGuard(r9);
     */
    /* JADX WARN: Code restructure failed: missing block: B:87:0x02ef, code lost:
    
        if (r0 == false) goto L91;
     */
    /* JADX WARN: Code restructure failed: missing block: B:89:0x02f6, code lost:
    
        if (jdk.nashorn.internal.runtime.linker.NashornCallSiteDescriptor.isApplyToCall(r10) == false) goto L91;
     */
    /* JADX WARN: Code restructure failed: missing block: B:90:0x02f9, code lost:
    
        r19 = jdk.nashorn.internal.lookup.Lookup.MH.asCollector(r19, java.lang.Object[].class, r0.parameterCount() - 2);
     */
    /* JADX WARN: Code restructure failed: missing block: B:91:0x030d, code lost:
    
        r0 = pairArguments(r19, r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:92:0x031a, code lost:
    
        if (r0.getSwitchPoints() == null) goto L94;
     */
    /* JADX WARN: Code restructure failed: missing block: B:93:0x031d, code lost:
    
        r0.addAll(java.util.Arrays.asList(r0.getSwitchPoints()));
     */
    /* JADX WARN: Code restructure failed: missing block: B:95:0x0334, code lost:
    
        if (r0.isEmpty() == false) goto L97;
     */
    /* JADX WARN: Code restructure failed: missing block: B:96:0x0337, code lost:
    
        r0 = null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:97:0x033b, code lost:
    
        r0 = (java.lang.invoke.SwitchPoint[]) r0.toArray(new java.lang.invoke.SwitchPoint[r0.size()]);
     */
    /* JADX WARN: Code restructure failed: missing block: B:98:0x034f, code lost:
    
        r29 = r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:99:0x0359, code lost:
    
        if (r20 != null) goto L101;
     */
    @Override // jdk.nashorn.internal.runtime.ScriptObject
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    protected jdk.internal.dynalink.linker.GuardedInvocation findCallMethod(jdk.internal.dynalink.CallSiteDescriptor r10, jdk.internal.dynalink.linker.LinkRequest r11) {
        /*
            Method dump skipped, instructions count: 882
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: jdk.nashorn.internal.runtime.ScriptFunction.findCallMethod(jdk.internal.dynalink.CallSiteDescriptor, jdk.internal.dynalink.linker.LinkRequest):jdk.internal.dynalink.linker.GuardedInvocation");
    }

    private GuardedInvocation createApplyOrCallCall(boolean isApply, CallSiteDescriptor desc, LinkRequest request, Object[] args) {
        MethodType descType = desc.getMethodType();
        int paramCount = descType.parameterCount();
        if (descType.parameterType(paramCount - 1).isArray()) {
            return createVarArgApplyOrCallCall(isApply, desc, request, args);
        }
        boolean passesThis = paramCount > 2;
        boolean passesArgs = paramCount > 3;
        int realArgCount = passesArgs ? paramCount - 3 : 0;
        Object appliedFn = args[1];
        boolean appliedFnNeedsWrappedThis = needsWrappedThis(appliedFn);
        SwitchPoint applyToCallSwitchPoint = Global.getBuiltinFunctionApplySwitchPoint();
        boolean isApplyToCall = NashornCallSiteDescriptor.isApplyToCall(desc);
        boolean isFailedApplyToCall = isApplyToCall && applyToCallSwitchPoint.hasBeenInvalidated();
        MethodType appliedType = descType.dropParameterTypes(0, 1);
        if (!passesThis) {
            appliedType = appliedType.insertParameterTypes(1, Object.class);
        } else if (appliedFnNeedsWrappedThis) {
            appliedType = appliedType.changeParameterType(1, Object.class);
        }
        MethodType dropArgs = Lookup.MH.type(Void.TYPE, new Class[0]);
        if (isApply && !isFailedApplyToCall) {
            int pc = appliedType.parameterCount();
            for (int i2 = 3; i2 < pc; i2++) {
                dropArgs = dropArgs.appendParameterTypes(appliedType.parameterType(i2));
            }
            if (pc > 3) {
                appliedType = appliedType.dropParameterTypes(3, pc);
            }
        }
        if (isApply || isFailedApplyToCall) {
            if (passesArgs) {
                appliedType = appliedType.changeParameterType(2, Object[].class);
                if (isFailedApplyToCall) {
                    appliedType = appliedType.dropParameterTypes(3, paramCount - 1);
                }
            } else {
                appliedType = appliedType.insertParameterTypes(2, Object[].class);
            }
        }
        CallSiteDescriptor appliedDesc = desc.changeMethodType(appliedType);
        Object[] appliedArgs = new Object[isApply ? 3 : appliedType.parameterCount()];
        appliedArgs[0] = appliedFn;
        appliedArgs[1] = passesThis ? appliedFnNeedsWrappedThis ? ScriptFunctionData.wrapThis(args[2]) : args[2] : ScriptRuntime.UNDEFINED;
        if (isApply && !isFailedApplyToCall) {
            appliedArgs[2] = passesArgs ? NativeFunction.toApplyArgs(args[3]) : ScriptRuntime.EMPTY_ARRAY;
        } else if (passesArgs) {
            if (isFailedApplyToCall) {
                Object[] tmp = new Object[args.length - 3];
                System.arraycopy(args, 3, tmp, 0, tmp.length);
                appliedArgs[2] = NativeFunction.toApplyArgs(tmp);
            } else {
                if (!$assertionsDisabled && isApply) {
                    throw new AssertionError();
                }
                System.arraycopy(args, 3, appliedArgs, 2, args.length - 3);
            }
        } else if (isFailedApplyToCall) {
            appliedArgs[2] = ScriptRuntime.EMPTY_ARRAY;
        }
        LinkRequest appliedRequest = request.replaceArguments(appliedDesc, appliedArgs);
        try {
            GuardedInvocation appliedInvocation = Bootstrap.getLinkerServices().getGuardedInvocation(appliedRequest);
            if (!$assertionsDisabled && appliedRequest == null) {
                throw new AssertionError();
            }
            Class<?> applyFnType = descType.parameterType(0);
            MethodHandle inv = appliedInvocation.getInvocation();
            if (isApply && !isFailedApplyToCall) {
                inv = passesArgs ? Lookup.MH.filterArguments(inv, 2, NativeFunction.TO_APPLY_ARGS) : Lookup.MH.insertArguments(inv, 2, ScriptRuntime.EMPTY_ARRAY);
            }
            if (isApplyToCall) {
                if (isFailedApplyToCall) {
                    Context.getContextTrusted().getLogger(ApplySpecialization.class).info("Collection arguments to revert call to apply in " + appliedFn);
                    inv = Lookup.MH.asCollector(inv, Object[].class, realArgCount);
                } else {
                    appliedInvocation = appliedInvocation.addSwitchPoint(applyToCallSwitchPoint);
                }
            }
            if (!passesThis) {
                inv = bindImplicitThis(appliedFn, inv);
            } else if (appliedFnNeedsWrappedThis) {
                inv = Lookup.MH.filterArguments(inv, 1, WRAP_THIS);
            }
            MethodHandle inv2 = Lookup.MH.dropArguments(inv, 0, applyFnType);
            for (int i3 = 0; i3 < dropArgs.parameterCount(); i3++) {
                inv2 = Lookup.MH.dropArguments(inv2, 4 + i3, dropArgs.parameterType(i3));
            }
            MethodHandle guard = appliedInvocation.getGuard();
            if (!passesThis && guard.type().parameterCount() > 1) {
                guard = bindImplicitThis(appliedFn, guard);
            }
            MethodType guardType = guard.type();
            MethodHandle guard2 = Lookup.MH.dropArguments(guard, 0, descType.parameterType(0));
            MethodHandle applyFnGuard = Lookup.MH.insertArguments(IS_APPLY_FUNCTION, 2, this);
            return appliedInvocation.replaceMethods(inv2, Lookup.MH.foldArguments(Lookup.MH.dropArguments(applyFnGuard, 2, guardType.parameterArray()), guard2));
        } catch (Error | RuntimeException e2) {
            throw e2;
        } catch (Exception e3) {
            throw new RuntimeException(e3);
        }
    }

    private GuardedInvocation createVarArgApplyOrCallCall(boolean isApply, CallSiteDescriptor desc, LinkRequest request, Object[] args) {
        MethodType descType = desc.getMethodType();
        int paramCount = descType.parameterCount();
        Object[] varArgs = (Object[]) args[paramCount - 1];
        int copiedArgCount = args.length - 1;
        int varArgCount = varArgs.length;
        Object[] spreadArgs = new Object[copiedArgCount + varArgCount];
        System.arraycopy(args, 0, spreadArgs, 0, copiedArgCount);
        System.arraycopy(varArgs, 0, spreadArgs, copiedArgCount, varArgCount);
        MethodType spreadType = descType.dropParameterTypes(paramCount - 1, paramCount).appendParameterTypes(Collections.nCopies(varArgCount, Object.class));
        CallSiteDescriptor spreadDesc = desc.changeMethodType(spreadType);
        LinkRequest spreadRequest = request.replaceArguments(spreadDesc, spreadArgs);
        GuardedInvocation spreadInvocation = createApplyOrCallCall(isApply, spreadDesc, spreadRequest, spreadArgs);
        return spreadInvocation.replaceMethods(pairArguments(spreadInvocation.getInvocation(), descType), spreadGuardArguments(spreadInvocation.getGuard(), descType));
    }

    private static MethodHandle spreadGuardArguments(MethodHandle guard, MethodType descType) {
        MethodHandle arrayConvertingGuard;
        MethodType guardType = guard.type();
        int guardParamCount = guardType.parameterCount();
        int descParamCount = descType.parameterCount();
        int spreadCount = (guardParamCount - descParamCount) + 1;
        if (spreadCount <= 0) {
            return guard;
        }
        if (guardType.parameterType(guardParamCount - 1).isArray()) {
            arrayConvertingGuard = Lookup.MH.filterArguments(guard, guardParamCount - 1, NativeFunction.TO_APPLY_ARGS);
        } else {
            arrayConvertingGuard = guard;
        }
        return ScriptObject.adaptHandleToVarArgCallSite(arrayConvertingGuard, descParamCount);
    }

    private static MethodHandle bindImplicitThis(Object fn, MethodHandle mh) {
        MethodHandle bound;
        if ((fn instanceof ScriptFunction) && ((ScriptFunction) fn).needsWrappedThis()) {
            bound = Lookup.MH.filterArguments(mh, 1, SCRIPTFUNCTION_GLOBALFILTER);
        } else {
            bound = mh;
        }
        return Lookup.MH.insertArguments(bound, 1, ScriptRuntime.UNDEFINED);
    }

    MethodHandle getCallMethodHandle(MethodType type, String bindName) {
        return pairArguments(bindToNameIfNeeded(bindToCalleeIfNeeded(this.data.getGenericInvoker(this.scope)), bindName), type);
    }

    private static MethodHandle bindToNameIfNeeded(MethodHandle methodHandle, String bindName) {
        if (bindName == null) {
            return methodHandle;
        }
        MethodType methodType = methodHandle.type();
        int parameterCount = methodType.parameterCount();
        boolean isVarArg = parameterCount > 0 && methodType.parameterType(parameterCount - 1).isArray();
        return isVarArg ? Lookup.MH.filterArguments(methodHandle, 1, Lookup.MH.insertArguments(ADD_ZEROTH_ELEMENT, 1, bindName)) : Lookup.MH.insertArguments(methodHandle, 1, bindName);
    }

    private static MethodHandle getFunctionGuard(ScriptFunction function, int flags) {
        if ($assertionsDisabled || function.data != null) {
            return function.data.isBuiltin() ? Guards.getIdentityGuard(function) : Lookup.MH.insertArguments(IS_FUNCTION_MH, 1, function.data);
        }
        throw new AssertionError();
    }

    private static MethodHandle getNonStrictFunctionGuard(ScriptFunction function) {
        if ($assertionsDisabled || function.data != null) {
            return Lookup.MH.insertArguments(IS_NONSTRICT_FUNCTION, 2, function.data);
        }
        throw new AssertionError();
    }

    private static boolean isFunctionMH(Object self, ScriptFunctionData data) {
        return (self instanceof ScriptFunction) && ((ScriptFunction) self).data == data;
    }

    private static boolean isNonStrictFunction(Object self, Object arg, ScriptFunctionData data) {
        return (self instanceof ScriptFunction) && ((ScriptFunction) self).data == data && (arg instanceof ScriptObject);
    }

    private static boolean isApplyFunction(boolean appliedFnCondition, Object self, Object expectedSelf) {
        return appliedFnCondition && self == expectedSelf;
    }

    private static Object[] addZerothElement(Object[] args, Object value) {
        Object[] src = args == null ? ScriptRuntime.EMPTY_ARRAY : args;
        Object[] result = new Object[src.length + 1];
        System.arraycopy(src, 0, result, 1, src.length);
        result[0] = value;
        return result;
    }

    private static Object invokeSync(ScriptFunction func, Object sync, Object self, Object... args) throws Throwable {
        Object objInvoke;
        Object syncObj = sync == ScriptRuntime.UNDEFINED ? self : sync;
        synchronized (syncObj) {
            objInvoke = func.invoke(self, args);
        }
        return objInvoke;
    }

    private static MethodHandle findOwnMH_S(String name, Class<?> rtype, Class<?>... types) {
        return Lookup.MH.findStatic(MethodHandles.lookup(), ScriptFunction.class, name, Lookup.MH.type(rtype, types));
    }

    private static MethodHandle findOwnMH_V(String name, Class<?> rtype, Class<?>... types) {
        return Lookup.MH.findVirtual(MethodHandles.lookup(), ScriptFunction.class, name, Lookup.MH.type(rtype, types));
    }
}
