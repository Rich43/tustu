package jdk.nashorn.internal.runtime.linker;

import com.sun.org.apache.xalan.internal.templates.Constants;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import jdk.internal.dynalink.CallSiteDescriptor;
import jdk.internal.dynalink.linker.GuardedInvocation;
import jdk.internal.dynalink.linker.LinkRequest;
import jdk.internal.dynalink.linker.LinkerServices;
import jdk.internal.dynalink.linker.TypeBasedGuardingDynamicLinker;
import jdk.internal.dynalink.support.CallSiteDescriptorFactory;
import jdk.nashorn.internal.lookup.MethodHandleFactory;
import jdk.nashorn.internal.lookup.MethodHandleFunctionality;
import jdk.nashorn.internal.runtime.JSType;

/* loaded from: nashorn.jar:jdk/nashorn/internal/runtime/linker/BrowserJSObjectLinker.class */
final class BrowserJSObjectLinker implements TypeBasedGuardingDynamicLinker {
    private static final ClassLoader myLoader;
    private static final String JSOBJECT_CLASS = "netscape.javascript.JSObject";
    private static volatile Class<?> jsObjectClass;
    private final NashornBeansLinker nashornBeansLinker;
    private static final MethodHandleFunctionality MH;
    private static final MethodHandle IS_JSOBJECT_GUARD;
    private static final MethodHandle JSOBJECTLINKER_GET;
    private static final MethodHandle JSOBJECTLINKER_PUT;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !BrowserJSObjectLinker.class.desiredAssertionStatus();
        myLoader = BrowserJSObjectLinker.class.getClassLoader();
        MH = MethodHandleFactory.getFunctionality();
        IS_JSOBJECT_GUARD = findOwnMH_S("isJSObject", Boolean.TYPE, Object.class);
        JSOBJECTLINKER_GET = findOwnMH_S("get", Object.class, MethodHandle.class, Object.class, Object.class);
        JSOBJECTLINKER_PUT = findOwnMH_S("put", Void.TYPE, Object.class, Object.class, Object.class);
    }

    BrowserJSObjectLinker(NashornBeansLinker nashornBeansLinker) {
        this.nashornBeansLinker = nashornBeansLinker;
    }

    @Override // jdk.internal.dynalink.linker.TypeBasedGuardingDynamicLinker
    public boolean canLinkType(Class<?> type) {
        return canLinkTypeStatic(type);
    }

    static boolean canLinkTypeStatic(Class<?> type) {
        if (jsObjectClass != null && jsObjectClass.isAssignableFrom(type)) {
            return true;
        }
        Class<?> superclass = type;
        while (true) {
            Class<?> clazz = superclass;
            if (clazz != null) {
                if (clazz.getClassLoader() == myLoader && clazz.getName().equals(JSOBJECT_CLASS)) {
                    jsObjectClass = clazz;
                    return true;
                }
                superclass = clazz.getSuperclass();
            } else {
                return false;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void checkJSObjectClass() {
        if (!$assertionsDisabled && jsObjectClass == null) {
            throw new AssertionError((Object) "netscape.javascript.JSObject not found!");
        }
    }

    @Override // jdk.internal.dynalink.linker.GuardingDynamicLinker
    public GuardedInvocation getGuardedInvocation(LinkRequest request, LinkerServices linkerServices) throws Exception {
        LinkRequest requestWithoutContext = request.withoutRuntimeContext();
        Object self = requestWithoutContext.getReceiver();
        CallSiteDescriptor desc = requestWithoutContext.getCallSiteDescriptor();
        checkJSObjectClass();
        if (desc.getNameTokenCount() < 2 || !"dyn".equals(desc.getNameToken(0))) {
            return null;
        }
        if (jsObjectClass.isInstance(self)) {
            GuardedInvocation inv = lookup(desc, request, linkerServices);
            return Bootstrap.asTypeSafeReturn(inv.replaceMethods(linkerServices.filterInternalObjects(inv.getInvocation()), inv.getGuard()), linkerServices, desc);
        }
        throw new AssertionError();
    }

    private GuardedInvocation lookup(CallSiteDescriptor desc, LinkRequest request, LinkerServices linkerServices) throws Exception {
        int c2;
        GuardedInvocation inv;
        String operator = CallSiteDescriptorFactory.tokenizeOperators(desc).get(0);
        c2 = desc.getNameTokenCount();
        try {
            inv = this.nashornBeansLinker.getGuardedInvocation(request, linkerServices);
        } catch (Throwable th) {
            inv = null;
        }
        switch (operator) {
            case "getProp":
            case "getElem":
            case "getMethod":
                return c2 > 2 ? findGetMethod(desc, inv) : findGetIndexMethod(inv);
            case "setProp":
            case "setElem":
                return c2 > 2 ? findSetMethod(desc, inv) : findSetIndexMethod();
            case "call":
                return findCallMethod(desc);
            default:
                return null;
        }
    }

    private static GuardedInvocation findGetMethod(CallSiteDescriptor desc, GuardedInvocation inv) {
        if (inv != null) {
            return inv;
        }
        String name = desc.getNameToken(2);
        MethodHandle getter = MH.insertArguments(JSObjectHandles.JSOBJECT_GETMEMBER, 1, name);
        return new GuardedInvocation(getter, IS_JSOBJECT_GUARD);
    }

    private static GuardedInvocation findGetIndexMethod(GuardedInvocation inv) {
        MethodHandle getter = MH.insertArguments(JSOBJECTLINKER_GET, 0, inv.getInvocation());
        return inv.replaceMethods(getter, inv.getGuard());
    }

    private static GuardedInvocation findSetMethod(CallSiteDescriptor desc, GuardedInvocation inv) {
        if (inv != null) {
            return inv;
        }
        MethodHandle getter = MH.insertArguments(JSObjectHandles.JSOBJECT_SETMEMBER, 1, desc.getNameToken(2));
        return new GuardedInvocation(getter, IS_JSOBJECT_GUARD);
    }

    private static GuardedInvocation findSetIndexMethod() {
        return new GuardedInvocation(JSOBJECTLINKER_PUT, IS_JSOBJECT_GUARD);
    }

    private static GuardedInvocation findCallMethod(CallSiteDescriptor desc) {
        MethodHandle call = MH.insertArguments(JSObjectHandles.JSOBJECT_CALL, 1, Constants.ELEMNAME_CALL_STRING);
        return new GuardedInvocation(MH.asCollector(call, Object[].class, desc.getMethodType().parameterCount() - 1), IS_JSOBJECT_GUARD);
    }

    private static boolean isJSObject(Object self) {
        return jsObjectClass.isInstance(self);
    }

    private static Object get(MethodHandle fallback, Object jsobj, Object key) throws Throwable {
        if (key instanceof Integer) {
            return (Object) JSObjectHandles.JSOBJECT_GETSLOT.invokeExact(jsobj, ((Integer) key).intValue());
        }
        if (key instanceof Number) {
            int index = getIndex((Number) key);
            if (index > -1) {
                return (Object) JSObjectHandles.JSOBJECT_GETSLOT.invokeExact(jsobj, index);
            }
            return null;
        }
        if (JSType.isString(key)) {
            String name = key.toString();
            if (name.indexOf(40) != -1) {
                return (Object) fallback.invokeExact(jsobj, name);
            }
            return (Object) JSObjectHandles.JSOBJECT_GETMEMBER.invokeExact(jsobj, name);
        }
        return null;
    }

    private static void put(Object jsobj, Object key, Object value) throws Throwable {
        if (key instanceof Integer) {
            (void) JSObjectHandles.JSOBJECT_SETSLOT.invokeExact(jsobj, ((Integer) key).intValue(), value);
        } else if (key instanceof Number) {
            (void) JSObjectHandles.JSOBJECT_SETSLOT.invokeExact(jsobj, getIndex((Number) key), value);
        } else if (JSType.isString(key)) {
            (void) JSObjectHandles.JSOBJECT_SETMEMBER.invokeExact(jsobj, key.toString(), value);
        }
    }

    private static int getIndex(Number n2) {
        double value = n2.doubleValue();
        if (JSType.isRepresentableAsInt(value)) {
            return (int) value;
        }
        return -1;
    }

    private static MethodHandle findOwnMH_S(String name, Class<?> rtype, Class<?>... types) {
        return MH.findStatic(MethodHandles.lookup(), BrowserJSObjectLinker.class, name, MH.type(rtype, types));
    }

    /* loaded from: nashorn.jar:jdk/nashorn/internal/runtime/linker/BrowserJSObjectLinker$JSObjectHandles.class */
    static class JSObjectHandles {
        static final MethodHandle JSOBJECT_GETMEMBER = findJSObjectMH_V("getMember", Object.class, String.class).asType(BrowserJSObjectLinker.MH.type(Object.class, Object.class, String.class));
        static final MethodHandle JSOBJECT_GETSLOT = findJSObjectMH_V("getSlot", Object.class, Integer.TYPE).asType(BrowserJSObjectLinker.MH.type(Object.class, Object.class, Integer.TYPE));
        static final MethodHandle JSOBJECT_SETMEMBER = findJSObjectMH_V("setMember", Void.TYPE, String.class, Object.class).asType(BrowserJSObjectLinker.MH.type(Void.TYPE, Object.class, String.class, Object.class));
        static final MethodHandle JSOBJECT_SETSLOT = findJSObjectMH_V("setSlot", Void.TYPE, Integer.TYPE, Object.class).asType(BrowserJSObjectLinker.MH.type(Void.TYPE, Object.class, Integer.TYPE, Object.class));
        static final MethodHandle JSOBJECT_CALL = findJSObjectMH_V(Constants.ELEMNAME_CALL_STRING, Object.class, String.class, Object[].class).asType(BrowserJSObjectLinker.MH.type(Object.class, Object.class, String.class, Object[].class));

        JSObjectHandles() {
        }

        private static MethodHandle findJSObjectMH_V(String name, Class<?> rtype, Class<?>... types) {
            BrowserJSObjectLinker.checkJSObjectClass();
            return BrowserJSObjectLinker.MH.findVirtual(MethodHandles.publicLookup(), BrowserJSObjectLinker.jsObjectClass, name, BrowserJSObjectLinker.MH.type(rtype, types));
        }
    }
}
