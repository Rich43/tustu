package jdk.nashorn.internal.runtime;

import com.sun.org.apache.xerces.internal.impl.xs.SchemaSymbols;
import java.lang.invoke.CallSite;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.invoke.MutableCallSite;
import java.lang.invoke.SwitchPoint;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Supplier;
import java.util.logging.Level;
import jdk.internal.dynalink.linker.GuardedInvocation;
import jdk.nashorn.internal.codegen.CompilationException;
import jdk.nashorn.internal.codegen.Compiler;
import jdk.nashorn.internal.codegen.TypeMap;
import jdk.nashorn.internal.codegen.types.ArrayType;
import jdk.nashorn.internal.codegen.types.Type;
import jdk.nashorn.internal.ir.FunctionNode;
import jdk.nashorn.internal.lookup.Lookup;
import jdk.nashorn.internal.objects.annotations.SpecializedFunction;
import jdk.nashorn.internal.runtime.events.RecompilationEvent;
import jdk.nashorn.internal.runtime.linker.Bootstrap;
import jdk.nashorn.internal.runtime.logging.DebugLogger;
import org.icepdf.core.pobjects.graphics.Separation;

/* loaded from: nashorn.jar:jdk/nashorn/internal/runtime/CompiledFunction.class */
final class CompiledFunction {
    private static final MethodHandle NEWFILTER;
    private static final MethodHandle RELINK_COMPOSABLE_INVOKER;
    private static final MethodHandle HANDLE_REWRITE_EXCEPTION;
    private static final MethodHandle RESTOF_INVOKER;
    private final DebugLogger log;
    static final Collection<CompiledFunction> NO_FUNCTIONS;
    private MethodHandle invoker;
    private MethodHandle constructor;
    private OptimismInfo optimismInfo;
    private final int flags;
    private final MethodType callSiteType;
    private final Specialization specialization;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !CompiledFunction.class.desiredAssertionStatus();
        NEWFILTER = findOwnMH("newFilter", Object.class, Object.class, Object.class);
        RELINK_COMPOSABLE_INVOKER = findOwnMH("relinkComposableInvoker", Void.TYPE, CallSite.class, CompiledFunction.class, Boolean.TYPE);
        HANDLE_REWRITE_EXCEPTION = findOwnMH("handleRewriteException", MethodHandle.class, CompiledFunction.class, OptimismInfo.class, RewriteException.class);
        RESTOF_INVOKER = MethodHandles.exactInvoker(MethodType.methodType((Class<?>) Object.class, (Class<?>) RewriteException.class));
        NO_FUNCTIONS = Collections.emptySet();
    }

    CompiledFunction(MethodHandle invoker) {
        this(invoker, null, null);
    }

    static CompiledFunction createBuiltInConstructor(MethodHandle invoker, Specialization specialization) {
        return new CompiledFunction(Lookup.MH.insertArguments(invoker, 0, false), createConstructorFromInvoker(Lookup.MH.insertArguments(invoker, 0, true)), specialization);
    }

    CompiledFunction(MethodHandle invoker, MethodHandle constructor, Specialization specialization) {
        this(invoker, constructor, 0, null, specialization, DebugLogger.DISABLED_LOGGER);
    }

    CompiledFunction(MethodHandle invoker, MethodHandle constructor, int flags, MethodType callSiteType, Specialization specialization, DebugLogger log) {
        this.specialization = specialization;
        if (specialization != null && specialization.isOptimistic()) {
            this.invoker = Lookup.MH.insertArguments(invoker, invoker.type().parameterCount() - 1, 1);
            throw new AssertionError((Object) "Optimistic (UnwarrantedOptimismException throwing) builtin functions are currently not in use");
        }
        this.invoker = invoker;
        this.constructor = constructor;
        this.flags = flags;
        this.callSiteType = callSiteType;
        this.log = log;
    }

    CompiledFunction(MethodHandle invoker, RecompilableScriptFunctionData functionData, Map<Integer, Type> invalidatedProgramPoints, MethodType callSiteType, int flags) {
        this(invoker, null, flags, callSiteType, null, functionData.getLogger());
        if ((flags & 2048) != 0) {
            this.optimismInfo = new OptimismInfo(functionData, invalidatedProgramPoints);
        } else {
            this.optimismInfo = null;
        }
    }

    static CompiledFunction createBuiltInConstructor(MethodHandle invoker) {
        return new CompiledFunction(Lookup.MH.insertArguments(invoker, 0, false), createConstructorFromInvoker(Lookup.MH.insertArguments(invoker, 0, true)), null);
    }

    boolean isSpecialization() {
        return this.specialization != null;
    }

    boolean hasLinkLogic() {
        return getLinkLogicClass() != null;
    }

    Class<? extends SpecializedFunction.LinkLogic> getLinkLogicClass() {
        if (isSpecialization()) {
            Class<? extends SpecializedFunction.LinkLogic> linkLogicClass = this.specialization.getLinkLogicClass();
            if ($assertionsDisabled || !SpecializedFunction.LinkLogic.isEmpty(linkLogicClass)) {
                return linkLogicClass;
            }
            throw new AssertionError((Object) "empty link logic classes should have been removed by nasgen");
        }
        return null;
    }

    int getFlags() {
        return this.flags;
    }

    boolean isOptimistic() {
        if (isSpecialization()) {
            return this.specialization.isOptimistic();
        }
        return false;
    }

    boolean isApplyToCall() {
        return (this.flags & 4096) != 0;
    }

    boolean isVarArg() {
        return isVarArgsType(this.invoker.type());
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        Class<? extends SpecializedFunction.LinkLogic> linkLogicClass = getLinkLogicClass();
        sb.append("[invokerType=").append((Object) this.invoker.type()).append(" ctor=").append((Object) this.constructor).append(" weight=").append(weight()).append(" linkLogic=").append(linkLogicClass != null ? linkLogicClass.getSimpleName() : Separation.COLORANT_NONE);
        return sb.toString();
    }

    boolean needsCallee() {
        return ScriptFunctionData.needsCallee(this.invoker);
    }

    MethodHandle createComposableInvoker() {
        return createComposableInvoker(false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public MethodHandle getConstructor() {
        if (this.constructor == null) {
            this.constructor = createConstructorFromInvoker(createInvokerForPessimisticCaller());
        }
        return this.constructor;
    }

    private MethodHandle createInvokerForPessimisticCaller() {
        return createInvoker(Object.class, -1);
    }

    private static MethodHandle createConstructorFromInvoker(MethodHandle invoker) {
        boolean needsCallee = ScriptFunctionData.needsCallee(invoker);
        MethodHandle swapped = needsCallee ? swapCalleeAndThis(invoker) : invoker;
        MethodHandle returnsObject = Lookup.MH.asType(swapped, swapped.type().changeReturnType(Object.class));
        MethodType ctorType = returnsObject.type();
        Class<?>[] ctorArgs = ctorType.dropParameterTypes(0, 1).parameterArray();
        MethodHandle filtered = Lookup.MH.foldArguments(Lookup.MH.dropArguments(NEWFILTER, 2, ctorArgs), returnsObject);
        return needsCallee ? Lookup.MH.foldArguments(filtered, ScriptFunction.ALLOCATE) : Lookup.MH.filterArguments(filtered, 0, ScriptFunction.ALLOCATE);
    }

    private static MethodHandle swapCalleeAndThis(MethodHandle mh) {
        MethodType type = mh.type();
        if (!$assertionsDisabled && type.parameterType(0) != ScriptFunction.class) {
            throw new AssertionError(type);
        }
        if (!$assertionsDisabled && type.parameterType(1) != Object.class) {
            throw new AssertionError(type);
        }
        MethodType newType = type.changeParameterType(0, Object.class).changeParameterType(1, ScriptFunction.class);
        int[] reorder = new int[type.parameterCount()];
        reorder[0] = 1;
        if (!$assertionsDisabled && reorder[1] != 0) {
            throw new AssertionError();
        }
        for (int i2 = 2; i2 < reorder.length; i2++) {
            reorder[i2] = i2;
        }
        return MethodHandles.permuteArguments(mh, newType, reorder);
    }

    MethodHandle createComposableConstructor() {
        return createComposableInvoker(true);
    }

    boolean hasConstructor() {
        return this.constructor != null;
    }

    MethodType type() {
        return this.invoker.type();
    }

    int weight() {
        return weight(type());
    }

    private static int weight(MethodType type) {
        if (isVarArgsType(type)) {
            return Integer.MAX_VALUE;
        }
        int weight = Type.typeFor(type.returnType()).getWeight();
        for (int i2 = 0; i2 < type.parameterCount(); i2++) {
            Class<?> paramType = type.parameterType(i2);
            int pweight = Type.typeFor(paramType).getWeight() * 2;
            weight += pweight;
        }
        return weight + type.parameterCount();
    }

    static boolean isVarArgsType(MethodType type) {
        if ($assertionsDisabled || type.parameterCount() >= 1) {
            return type.parameterType(type.parameterCount() - 1) == Object[].class;
        }
        throw new AssertionError(type);
    }

    static boolean moreGenericThan(MethodType mt0, MethodType mt1) {
        return weight(mt0) > weight(mt1);
    }

    boolean betterThanFinal(CompiledFunction other, MethodType callSiteMethodType) {
        if (other == null) {
            return true;
        }
        return betterThanFinal(this, other, callSiteMethodType);
    }

    private static boolean betterThanFinal(CompiledFunction cf, CompiledFunction other, MethodType callSiteMethodType) {
        MethodType thisMethodType = cf.type();
        MethodType otherMethodType = other.type();
        int thisParamCount = getParamCount(thisMethodType);
        int otherParamCount = getParamCount(otherMethodType);
        int callSiteRawParamCount = getParamCount(callSiteMethodType);
        boolean csVarArg = callSiteRawParamCount == Integer.MAX_VALUE;
        int callSiteParamCount = csVarArg ? callSiteRawParamCount : callSiteRawParamCount - 1;
        int thisDiscardsParams = Math.max(callSiteParamCount - thisParamCount, 0);
        int otherDiscardsParams = Math.max(callSiteParamCount - otherParamCount, 0);
        if (thisDiscardsParams < otherDiscardsParams) {
            return true;
        }
        if (thisDiscardsParams > otherDiscardsParams) {
            return false;
        }
        boolean thisVarArg = thisParamCount == Integer.MAX_VALUE;
        boolean otherVarArg = otherParamCount == Integer.MAX_VALUE;
        if (!thisVarArg || !otherVarArg || !csVarArg) {
            Type[] thisType = toTypeWithoutCallee(thisMethodType, 0);
            Type[] otherType = toTypeWithoutCallee(otherMethodType, 0);
            Type[] callSiteType = toTypeWithoutCallee(callSiteMethodType, 1);
            int narrowWeightDelta = 0;
            int widenWeightDelta = 0;
            int minParamsCount = Math.min(Math.min(thisParamCount, otherParamCount), callSiteParamCount);
            for (int i2 = 0; i2 < minParamsCount; i2++) {
                int callSiteParamWeight = getParamType(i2, callSiteType, csVarArg).getWeight();
                int thisParamWeightDelta = getParamType(i2, thisType, thisVarArg).getWeight() - callSiteParamWeight;
                int otherParamWeightDelta = getParamType(i2, otherType, otherVarArg).getWeight() - callSiteParamWeight;
                narrowWeightDelta += Math.max(-thisParamWeightDelta, 0) - Math.max(-otherParamWeightDelta, 0);
                widenWeightDelta += Math.max(thisParamWeightDelta, 0) - Math.max(otherParamWeightDelta, 0);
            }
            if (!thisVarArg) {
                for (int i3 = callSiteParamCount; i3 < thisParamCount; i3++) {
                    narrowWeightDelta += Math.max(Type.OBJECT.getWeight() - thisType[i3].getWeight(), 0);
                }
            }
            if (!otherVarArg) {
                for (int i4 = callSiteParamCount; i4 < otherParamCount; i4++) {
                    narrowWeightDelta -= Math.max(Type.OBJECT.getWeight() - otherType[i4].getWeight(), 0);
                }
            }
            if (narrowWeightDelta < 0) {
                return true;
            }
            if (narrowWeightDelta > 0) {
                return false;
            }
            if (widenWeightDelta < 0) {
                return true;
            }
            if (widenWeightDelta > 0) {
                return false;
            }
        }
        if (thisParamCount == callSiteParamCount && otherParamCount != callSiteParamCount) {
            return true;
        }
        if (thisParamCount != callSiteParamCount && otherParamCount == callSiteParamCount) {
            return false;
        }
        if (thisVarArg) {
            if (!otherVarArg) {
                return true;
            }
        } else if (otherVarArg) {
            return false;
        }
        int fnParamDelta = thisParamCount - otherParamCount;
        if (fnParamDelta < 0) {
            return true;
        }
        if (fnParamDelta > 0) {
            return false;
        }
        int callSiteRetWeight = Type.typeFor(callSiteMethodType.returnType()).getWeight();
        int thisRetWeightDelta = Type.typeFor(thisMethodType.returnType()).getWeight() - callSiteRetWeight;
        int otherRetWeightDelta = Type.typeFor(otherMethodType.returnType()).getWeight() - callSiteRetWeight;
        int widenRetDelta = Math.max(thisRetWeightDelta, 0) - Math.max(otherRetWeightDelta, 0);
        if (widenRetDelta < 0) {
            return true;
        }
        if (widenRetDelta > 0) {
            return false;
        }
        int narrowRetDelta = Math.max(-thisRetWeightDelta, 0) - Math.max(-otherRetWeightDelta, 0);
        if (narrowRetDelta < 0) {
            return true;
        }
        if (narrowRetDelta > 0) {
            return false;
        }
        if (cf.isSpecialization() != other.isSpecialization()) {
            return cf.isSpecialization();
        }
        if (cf.isSpecialization() && other.isSpecialization()) {
            return cf.getLinkLogicClass() != null;
        }
        throw new AssertionError((Object) (((Object) thisMethodType) + " identically applicable to " + ((Object) otherMethodType) + " for " + ((Object) callSiteMethodType)));
    }

    private static Type[] toTypeWithoutCallee(MethodType type, int thisIndex) {
        int paramCount = type.parameterCount();
        Type[] t2 = new Type[paramCount - thisIndex];
        for (int i2 = thisIndex; i2 < paramCount; i2++) {
            t2[i2 - thisIndex] = Type.typeFor(type.parameterType(i2));
        }
        return t2;
    }

    private static Type getParamType(int i2, Type[] paramTypes, boolean isVarArg) {
        int fixParamCount = paramTypes.length - (isVarArg ? 1 : 0);
        if (i2 < fixParamCount) {
            return paramTypes[i2];
        }
        if ($assertionsDisabled || isVarArg) {
            return ((ArrayType) paramTypes[paramTypes.length - 1]).getElementType();
        }
        throw new AssertionError();
    }

    boolean matchesCallSite(MethodType other, boolean pickVarArg) {
        if (other.equals((Object) this.callSiteType)) {
            return true;
        }
        MethodType type = type();
        int fnParamCount = getParamCount(type);
        boolean isVarArg = fnParamCount == Integer.MAX_VALUE;
        if (isVarArg) {
            return pickVarArg;
        }
        int csParamCount = getParamCount(other);
        boolean csIsVarArg = csParamCount == Integer.MAX_VALUE;
        if (csIsVarArg && isApplyToCall()) {
            return false;
        }
        int thisThisIndex = needsCallee() ? 1 : 0;
        int fnParamCountNoCallee = fnParamCount - thisThisIndex;
        int minParams = Math.min(csParamCount - 1, fnParamCountNoCallee);
        for (int i2 = 0; i2 < minParams; i2++) {
            Type fnType = Type.typeFor(type.parameterType(i2 + thisThisIndex));
            Type csType = csIsVarArg ? Type.OBJECT : Type.typeFor(other.parameterType(i2 + 1));
            if (!fnType.isEquivalentTo(csType)) {
                return false;
            }
        }
        for (int i3 = minParams; i3 < fnParamCountNoCallee; i3++) {
            if (!Type.typeFor(type.parameterType(i3 + thisThisIndex)).isEquivalentTo(Type.OBJECT)) {
                return false;
            }
        }
        return true;
    }

    private static int getParamCount(MethodType type) {
        int paramCount = type.parameterCount();
        if (type.parameterType(paramCount - 1).isArray()) {
            return Integer.MAX_VALUE;
        }
        return paramCount;
    }

    private boolean canBeDeoptimized() {
        return this.optimismInfo != null;
    }

    private MethodHandle createComposableInvoker(boolean isConstructor) throws RuntimeException {
        MethodHandle handle = getInvokerOrConstructor(isConstructor);
        if (!canBeDeoptimized()) {
            return handle;
        }
        CallSite cs = new MutableCallSite(handle.type());
        relinkComposableInvoker(cs, this, isConstructor);
        return cs.dynamicInvoker();
    }

    /* loaded from: nashorn.jar:jdk/nashorn/internal/runtime/CompiledFunction$HandleAndAssumptions.class */
    private static class HandleAndAssumptions {
        final MethodHandle handle;
        final SwitchPoint assumptions;

        HandleAndAssumptions(MethodHandle handle, SwitchPoint assumptions) {
            this.handle = handle;
            this.assumptions = assumptions;
        }

        GuardedInvocation createInvocation() {
            return new GuardedInvocation(this.handle, this.assumptions);
        }
    }

    private synchronized HandleAndAssumptions getValidOptimisticInvocation(Supplier<MethodHandle> invocationSupplier) {
        MethodHandle handle;
        SwitchPoint assumptions;
        while (true) {
            handle = invocationSupplier.get();
            assumptions = canBeDeoptimized() ? this.optimismInfo.optimisticAssumptions : null;
            if (assumptions == null || !assumptions.hasBeenInvalidated()) {
                break;
            }
            try {
                wait();
            } catch (InterruptedException e2) {
            }
        }
        return new HandleAndAssumptions(handle, assumptions);
    }

    private static void relinkComposableInvoker(CallSite cs, CompiledFunction inv, final boolean constructor) throws RuntimeException {
        MethodHandle target;
        HandleAndAssumptions handleAndAssumptions = inv.getValidOptimisticInvocation(new Supplier<MethodHandle>() { // from class: jdk.nashorn.internal.runtime.CompiledFunction.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.function.Supplier
            public MethodHandle get() {
                return CompiledFunction.this.getInvokerOrConstructor(constructor);
            }
        });
        MethodHandle handle = handleAndAssumptions.handle;
        SwitchPoint assumptions = handleAndAssumptions.assumptions;
        if (assumptions == null) {
            target = handle;
        } else {
            MethodHandle relink = MethodHandles.insertArguments(RELINK_COMPOSABLE_INVOKER, 0, cs, inv, Boolean.valueOf(constructor));
            target = assumptions.guardWithTest(handle, MethodHandles.foldArguments(cs.dynamicInvoker(), relink));
        }
        cs.setTarget(target.asType(cs.type()));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public MethodHandle getInvokerOrConstructor(boolean selectCtor) {
        return selectCtor ? getConstructor() : createInvokerForPessimisticCaller();
    }

    GuardedInvocation createFunctionInvocation(final Class<?> callSiteReturnType, final int callerProgramPoint) {
        return getValidOptimisticInvocation(new Supplier<MethodHandle>() { // from class: jdk.nashorn.internal.runtime.CompiledFunction.2
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.function.Supplier
            public MethodHandle get() {
                return CompiledFunction.this.createInvoker(callSiteReturnType, callerProgramPoint);
            }
        }).createInvocation();
    }

    GuardedInvocation createConstructorInvocation() {
        return getValidOptimisticInvocation(new Supplier<MethodHandle>() { // from class: jdk.nashorn.internal.runtime.CompiledFunction.3
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.function.Supplier
            public MethodHandle get() {
                return CompiledFunction.this.getConstructor();
            }
        }).createInvocation();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public MethodHandle createInvoker(Class<?> callSiteReturnType, int callerProgramPoint) {
        boolean isOptimistic = canBeDeoptimized();
        MethodHandle handleRewriteException = isOptimistic ? createRewriteExceptionHandler() : null;
        MethodHandle inv = this.invoker;
        if (UnwarrantedOptimismException.isValid(callerProgramPoint)) {
            inv = changeReturnType(OptimisticReturnFilters.filterOptimisticReturnValue(inv, callSiteReturnType, callerProgramPoint), callSiteReturnType);
            if (callSiteReturnType.isPrimitive() && handleRewriteException != null) {
                handleRewriteException = OptimisticReturnFilters.filterOptimisticReturnValue(handleRewriteException, callSiteReturnType, callerProgramPoint);
            }
        } else if (isOptimistic) {
            inv = changeReturnType(inv, callSiteReturnType);
        }
        if (isOptimistic) {
            if (!$assertionsDisabled && handleRewriteException == null) {
                throw new AssertionError();
            }
            MethodHandle typedHandleRewriteException = changeReturnType(handleRewriteException, inv.type().returnType());
            return Lookup.MH.catchException(inv, RewriteException.class, typedHandleRewriteException);
        }
        return inv;
    }

    private MethodHandle createRewriteExceptionHandler() {
        return Lookup.MH.foldArguments(RESTOF_INVOKER, Lookup.MH.insertArguments(HANDLE_REWRITE_EXCEPTION, 0, this, this.optimismInfo));
    }

    private static MethodHandle changeReturnType(MethodHandle mh, Class<?> newReturnType) {
        return Bootstrap.getLinkerServices().asType(mh, mh.type().changeReturnType(newReturnType));
    }

    private static MethodHandle handleRewriteException(CompiledFunction function, OptimismInfo oldOptimismInfo, RewriteException re) {
        return function.handleRewriteException(oldOptimismInfo, re);
    }

    private static List<String> toStringInvalidations(Map<Integer, Type> ipp) {
        String strValueOf;
        if (ipp == null) {
            return Collections.emptyList();
        }
        List<String> list = new ArrayList<>();
        for (Map.Entry<Integer, Type> entry : ipp.entrySet()) {
            char bct = entry.getValue().getBytecodeStackType();
            switch (entry.getValue().getBytecodeStackType()) {
                case 'A':
                    strValueOf = "object";
                    break;
                case 'B':
                case 'C':
                case 'E':
                case 'F':
                case 'G':
                case 'H':
                default:
                    strValueOf = String.valueOf(bct);
                    break;
                case 'D':
                    strValueOf = SchemaSymbols.ATTVAL_DOUBLE;
                    break;
                case 'I':
                    strValueOf = "int";
                    break;
                case 'J':
                    strValueOf = SchemaSymbols.ATTVAL_LONG;
                    break;
            }
            String type = strValueOf;
            StringBuilder sb = new StringBuilder();
            sb.append('[').append("program point: ").append((Object) entry.getKey()).append(" -> ").append(type).append(']');
            list.add(sb.toString());
        }
        return list;
    }

    private void logRecompile(String reason, FunctionNode fn, MethodType type, Map<Integer, Type> ipp) {
        if (this.log.isEnabled()) {
            this.log.info(reason, DebugLogger.quote(fn.getName()), " signature: ", type);
            this.log.indent();
            for (String str : toStringInvalidations(ipp)) {
                this.log.fine(str);
            }
            this.log.unindent();
        }
    }

    private synchronized MethodHandle handleRewriteException(OptimismInfo oldOptInfo, RewriteException re) throws CompilationException {
        if (this.log.isEnabled()) {
            this.log.info(new RecompilationEvent(Level.INFO, re, re.getReturnValueNonDestructive()), "caught RewriteException ", re.getMessageShort());
            this.log.indent();
        }
        MethodType type = type();
        MethodType ct = type.parameterType(0) == ScriptFunction.class ? type : type.insertParameterTypes(0, ScriptFunction.class);
        OptimismInfo currentOptInfo = this.optimismInfo;
        boolean shouldRecompile = currentOptInfo != null && currentOptInfo.requestRecompile(re);
        OptimismInfo effectiveOptInfo = currentOptInfo != null ? currentOptInfo : oldOptInfo;
        FunctionNode fn = effectiveOptInfo.reparse();
        boolean cached = fn.isCached();
        Compiler compiler = effectiveOptInfo.getCompiler(fn, ct, re);
        if (!shouldRecompile) {
            logRecompile("Rest-of compilation [STANDALONE] ", fn, ct, effectiveOptInfo.invalidatedProgramPoints);
            return restOfHandle(effectiveOptInfo, compiler.compile(fn, cached ? Compiler.CompilationPhases.COMPILE_CACHED_RESTOF : Compiler.CompilationPhases.COMPILE_ALL_RESTOF), currentOptInfo != null);
        }
        logRecompile("Deoptimizing recompilation (up to bytecode) ", fn, ct, effectiveOptInfo.invalidatedProgramPoints);
        FunctionNode fn2 = compiler.compile(fn, cached ? Compiler.CompilationPhases.RECOMPILE_CACHED_UPTO_BYTECODE : Compiler.CompilationPhases.COMPILE_UPTO_BYTECODE);
        this.log.fine("Reusable IR generated");
        this.log.info("Generating and installing bytecode from reusable IR...");
        logRecompile("Rest-of compilation [CODE PIPELINE REUSE] ", fn2, ct, effectiveOptInfo.invalidatedProgramPoints);
        FunctionNode normalFn = compiler.compile(fn2, Compiler.CompilationPhases.GENERATE_BYTECODE_AND_INSTALL);
        if (effectiveOptInfo.data.usePersistentCodeCache()) {
            RecompilableScriptFunctionData data = effectiveOptInfo.data;
            int functionNodeId = data.getFunctionNodeId();
            TypeMap typeMap = data.typeMap(ct);
            Type[] paramTypes = typeMap == null ? null : typeMap.getParameterTypes(functionNodeId);
            String cacheKey = CodeStore.getCacheKey(Integer.valueOf(functionNodeId), paramTypes);
            compiler.persistClassInfo(cacheKey, normalFn);
        }
        boolean canBeDeoptimized = normalFn.canBeDeoptimized();
        if (this.log.isEnabled()) {
            this.log.unindent();
            this.log.info("Done.");
            DebugLogger debugLogger = this.log;
            Object[] objArr = new Object[6];
            objArr[0] = "Recompiled '";
            objArr[1] = fn2.getName();
            objArr[2] = "' (";
            objArr[3] = Debug.id(this);
            objArr[4] = ") ";
            objArr[5] = canBeDeoptimized ? "can still be deoptimized." : " is completely deoptimized.";
            debugLogger.info(objArr);
            this.log.finest("Looking up invoker...");
        }
        MethodHandle newInvoker = effectiveOptInfo.data.lookup(fn2);
        this.invoker = newInvoker.asType(type.changeReturnType(newInvoker.type().returnType()));
        this.constructor = null;
        this.log.info("Done: ", this.invoker);
        MethodHandle restOf = restOfHandle(effectiveOptInfo, compiler.compile(fn2, Compiler.CompilationPhases.GENERATE_BYTECODE_AND_INSTALL_RESTOF), canBeDeoptimized);
        if (!canBeDeoptimized) {
            this.optimismInfo = null;
        } else {
            effectiveOptInfo.newOptimisticAssumptions();
        }
        notifyAll();
        return restOf;
    }

    private MethodHandle restOfHandle(OptimismInfo info, FunctionNode restOfFunction, boolean canBeDeoptimized) {
        if (!$assertionsDisabled && info == null) {
            throw new AssertionError();
        }
        if (!$assertionsDisabled && !restOfFunction.getCompileUnit().getUnitClassName().contains("restOf")) {
            throw new AssertionError();
        }
        MethodHandle restOf = changeReturnType(info.data.lookupCodeMethod(restOfFunction.getCompileUnit().getCode(), Lookup.MH.type(restOfFunction.getReturnType().getTypeClass(), RewriteException.class)), Object.class);
        if (!canBeDeoptimized) {
            return restOf;
        }
        return Lookup.MH.catchException(restOf, RewriteException.class, createRewriteExceptionHandler());
    }

    /* loaded from: nashorn.jar:jdk/nashorn/internal/runtime/CompiledFunction$OptimismInfo.class */
    private static class OptimismInfo {
        private final RecompilableScriptFunctionData data;
        private final Map<Integer, Type> invalidatedProgramPoints;
        private SwitchPoint optimisticAssumptions;
        private final DebugLogger log;

        OptimismInfo(RecompilableScriptFunctionData data, Map<Integer, Type> invalidatedProgramPoints) {
            this.data = data;
            this.log = data.getLogger();
            this.invalidatedProgramPoints = invalidatedProgramPoints == null ? new TreeMap<>() : invalidatedProgramPoints;
            newOptimisticAssumptions();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void newOptimisticAssumptions() {
            this.optimisticAssumptions = new SwitchPoint();
        }

        boolean requestRecompile(RewriteException e2) {
            String name;
            Type retType = e2.getReturnType();
            Type previousFailedType = this.invalidatedProgramPoints.put(Integer.valueOf(e2.getProgramPoint()), retType);
            if (previousFailedType != null && !previousFailedType.narrowerThan(retType)) {
                StackTraceElement[] stack = e2.getStackTrace();
                if (stack.length == 0) {
                    name = this.data.getName();
                } else {
                    name = stack[0].getClassName() + "." + stack[0].getMethodName();
                }
                String functionId = name;
                this.log.info("RewriteException for an already invalidated program point ", Integer.valueOf(e2.getProgramPoint()), " in ", functionId, ". This is okay for a recursive function invocation, but a bug otherwise.");
                return false;
            }
            SwitchPoint.invalidateAll(new SwitchPoint[]{this.optimisticAssumptions});
            return true;
        }

        Compiler getCompiler(FunctionNode fn, MethodType actualCallSiteType, RewriteException e2) {
            return this.data.getCompiler(fn, actualCallSiteType, e2.getRuntimeScope(), this.invalidatedProgramPoints, getEntryPoints(e2));
        }

        private static int[] getEntryPoints(RewriteException e2) {
            int[] entryPoints;
            int[] prevEntryPoints = e2.getPreviousContinuationEntryPoints();
            if (prevEntryPoints == null) {
                entryPoints = new int[1];
            } else {
                int l2 = prevEntryPoints.length;
                entryPoints = new int[l2 + 1];
                System.arraycopy(prevEntryPoints, 0, entryPoints, 1, l2);
            }
            entryPoints[0] = e2.getProgramPoint();
            return entryPoints;
        }

        FunctionNode reparse() {
            return this.data.reparse();
        }
    }

    private static Object newFilter(Object result, Object allocation) {
        return ((result instanceof ScriptObject) || !JSType.isPrimitive(result)) ? result : allocation;
    }

    private static MethodHandle findOwnMH(String name, Class<?> rtype, Class<?>... types) {
        return Lookup.MH.findStatic(MethodHandles.lookup(), CompiledFunction.class, name, Lookup.MH.type(rtype, types));
    }
}
