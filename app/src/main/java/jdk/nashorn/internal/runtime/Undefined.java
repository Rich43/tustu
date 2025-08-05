package jdk.nashorn.internal.runtime;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import jdk.internal.dynalink.CallSiteDescriptor;
import jdk.internal.dynalink.linker.GuardedInvocation;
import jdk.internal.dynalink.support.CallSiteDescriptorFactory;
import jdk.internal.dynalink.support.Guards;
import jdk.nashorn.internal.lookup.Lookup;
import jdk.nashorn.internal.runtime.linker.NashornCallSiteDescriptor;

/* loaded from: nashorn.jar:jdk/nashorn/internal/runtime/Undefined.class */
public final class Undefined extends DefaultPropertyAccess {
    private static final Undefined UNDEFINED = new Undefined();
    private static final Undefined EMPTY = new Undefined();
    private static final MethodHandle UNDEFINED_GUARD = Guards.getIdentityGuard(UNDEFINED);
    private static final MethodHandle GET_METHOD = findOwnMH("get", Object.class, Object.class);
    private static final MethodHandle SET_METHOD = Lookup.MH.insertArguments(findOwnMH("set", Void.TYPE, Object.class, Object.class, Integer.TYPE), 3, 2);

    private Undefined() {
    }

    public static Undefined getUndefined() {
        return UNDEFINED;
    }

    public static Undefined getEmpty() {
        return EMPTY;
    }

    public String getClassName() {
        return "Undefined";
    }

    public String toString() {
        return "undefined";
    }

    public static GuardedInvocation lookup(CallSiteDescriptor desc) {
        String operator = CallSiteDescriptorFactory.tokenizeOperators(desc).get(0);
        switch (operator) {
            case "new":
            case "call":
                String name = NashornCallSiteDescriptor.getFunctionDescription(desc);
                String msg = name != null ? "not.a.function" : "cant.call.undefined";
                throw ECMAErrors.typeError(msg, name);
            case "callMethod":
                throw lookupTypeError("cant.read.property.of.undefined", desc);
            case "getProp":
            case "getElem":
            case "getMethod":
                if (desc.getNameTokenCount() < 3) {
                    return findGetIndexMethod(desc);
                }
                return findGetMethod(desc);
            case "setProp":
            case "setElem":
                if (desc.getNameTokenCount() < 3) {
                    return findSetIndexMethod(desc);
                }
                return findSetMethod(desc);
            default:
                return null;
        }
    }

    private static ECMAException lookupTypeError(String msg, CallSiteDescriptor desc) {
        String name = desc.getNameToken(2);
        String[] strArr = new String[1];
        strArr[0] = (name == null || name.isEmpty()) ? null : name;
        return ECMAErrors.typeError(msg, strArr);
    }

    private static GuardedInvocation findGetMethod(CallSiteDescriptor desc) {
        return new GuardedInvocation(Lookup.MH.insertArguments(GET_METHOD, 1, desc.getNameToken(2)), UNDEFINED_GUARD).asType(desc);
    }

    private static GuardedInvocation findGetIndexMethod(CallSiteDescriptor desc) {
        return new GuardedInvocation(GET_METHOD, UNDEFINED_GUARD).asType(desc);
    }

    private static GuardedInvocation findSetMethod(CallSiteDescriptor desc) {
        return new GuardedInvocation(Lookup.MH.insertArguments(SET_METHOD, 1, desc.getNameToken(2)), UNDEFINED_GUARD).asType(desc);
    }

    private static GuardedInvocation findSetIndexMethod(CallSiteDescriptor desc) {
        return new GuardedInvocation(SET_METHOD, UNDEFINED_GUARD).asType(desc);
    }

    @Override // jdk.nashorn.internal.runtime.DefaultPropertyAccess, jdk.nashorn.internal.runtime.PropertyAccess
    public Object get(Object key) {
        throw ECMAErrors.typeError("cant.read.property.of.undefined", ScriptRuntime.safeToString(key));
    }

    @Override // jdk.nashorn.internal.runtime.DefaultPropertyAccess, jdk.nashorn.internal.runtime.PropertyAccess
    public void set(Object key, Object value, int flags) {
        throw ECMAErrors.typeError("cant.set.property.of.undefined", ScriptRuntime.safeToString(key));
    }

    @Override // jdk.nashorn.internal.runtime.PropertyAccess
    public boolean delete(Object key, boolean strict) {
        throw ECMAErrors.typeError("cant.delete.property.of.undefined", ScriptRuntime.safeToString(key));
    }

    @Override // jdk.nashorn.internal.runtime.DefaultPropertyAccess, jdk.nashorn.internal.runtime.PropertyAccess
    public boolean has(Object key) {
        return false;
    }

    @Override // jdk.nashorn.internal.runtime.DefaultPropertyAccess, jdk.nashorn.internal.runtime.PropertyAccess
    public boolean hasOwnProperty(Object key) {
        return false;
    }

    private static MethodHandle findOwnMH(String name, Class<?> rtype, Class<?>... types) {
        return Lookup.MH.findVirtual(MethodHandles.lookup(), Undefined.class, name, Lookup.MH.type(rtype, types));
    }
}
