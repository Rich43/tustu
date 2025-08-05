package jdk.nashorn.internal.runtime.linker;

import com.sun.org.apache.xalan.internal.templates.Constants;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.Map;
import javax.script.Bindings;
import jdk.internal.dynalink.CallSiteDescriptor;
import jdk.internal.dynalink.linker.GuardedInvocation;
import jdk.internal.dynalink.linker.LinkRequest;
import jdk.internal.dynalink.linker.LinkerServices;
import jdk.internal.dynalink.linker.TypeBasedGuardingDynamicLinker;
import jdk.internal.dynalink.support.CallSiteDescriptorFactory;
import jdk.nashorn.api.scripting.JSObject;
import jdk.nashorn.api.scripting.ScriptObjectMirror;
import jdk.nashorn.internal.lookup.MethodHandleFactory;
import jdk.nashorn.internal.lookup.MethodHandleFunctionality;
import jdk.nashorn.internal.objects.Global;
import jdk.nashorn.internal.runtime.Context;
import jdk.nashorn.internal.runtime.JSType;
import jdk.nashorn.internal.runtime.ScriptRuntime;

/* loaded from: nashorn.jar:jdk/nashorn/internal/runtime/linker/JSObjectLinker.class */
final class JSObjectLinker implements TypeBasedGuardingDynamicLinker {
    private final NashornBeansLinker nashornBeansLinker;
    private static final MethodHandleFunctionality MH;
    private static final MethodHandle IS_JSOBJECT_GUARD;
    private static final MethodHandle JSOBJECTLINKER_GET;
    private static final MethodHandle JSOBJECTLINKER_PUT;
    private static final MethodHandle JSOBJECT_GETMEMBER;
    private static final MethodHandle JSOBJECT_SETMEMBER;
    private static final MethodHandle JSOBJECT_CALL;
    private static final MethodHandle JSOBJECT_SCOPE_CALL;
    private static final MethodHandle JSOBJECT_CALL_TO_APPLY;
    private static final MethodHandle JSOBJECT_NEW;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !JSObjectLinker.class.desiredAssertionStatus();
        MH = MethodHandleFactory.getFunctionality();
        IS_JSOBJECT_GUARD = findOwnMH_S("isJSObject", Boolean.TYPE, Object.class);
        JSOBJECTLINKER_GET = findOwnMH_S("get", Object.class, MethodHandle.class, Object.class, Object.class);
        JSOBJECTLINKER_PUT = findOwnMH_S("put", Void.TYPE, Object.class, Object.class, Object.class);
        JSOBJECT_GETMEMBER = findJSObjectMH_V("getMember", Object.class, String.class);
        JSOBJECT_SETMEMBER = findJSObjectMH_V("setMember", Void.TYPE, String.class, Object.class);
        JSOBJECT_CALL = findJSObjectMH_V(Constants.ELEMNAME_CALL_STRING, Object.class, Object.class, Object[].class);
        JSOBJECT_SCOPE_CALL = findOwnMH_S("jsObjectScopeCall", Object.class, JSObject.class, Object.class, Object[].class);
        JSOBJECT_CALL_TO_APPLY = findOwnMH_S("callToApply", Object.class, MethodHandle.class, JSObject.class, Object.class, Object[].class);
        JSOBJECT_NEW = findJSObjectMH_V("newObject", Object.class, Object[].class);
    }

    JSObjectLinker(NashornBeansLinker nashornBeansLinker) {
        this.nashornBeansLinker = nashornBeansLinker;
    }

    @Override // jdk.internal.dynalink.linker.TypeBasedGuardingDynamicLinker
    public boolean canLinkType(Class<?> type) {
        return canLinkTypeStatic(type);
    }

    static boolean canLinkTypeStatic(Class<?> type) {
        return Map.class.isAssignableFrom(type) || Bindings.class.isAssignableFrom(type) || JSObject.class.isAssignableFrom(type);
    }

    @Override // jdk.internal.dynalink.linker.GuardingDynamicLinker
    public GuardedInvocation getGuardedInvocation(LinkRequest request, LinkerServices linkerServices) throws Exception {
        GuardedInvocation inv;
        LinkRequest requestWithoutContext = request.withoutRuntimeContext();
        Object self = requestWithoutContext.getReceiver();
        CallSiteDescriptor desc = requestWithoutContext.getCallSiteDescriptor();
        if (desc.getNameTokenCount() < 2 || !"dyn".equals(desc.getNameToken(0))) {
            return null;
        }
        if (self instanceof JSObject) {
            GuardedInvocation inv2 = lookup(desc, request, linkerServices);
            inv = inv2.replaceMethods(linkerServices.filterInternalObjects(inv2.getInvocation()), inv2.getGuard());
        } else if ((self instanceof Map) || (self instanceof Bindings)) {
            GuardedInvocation beanInv = this.nashornBeansLinker.getGuardedInvocation(request, linkerServices);
            inv = new GuardedInvocation(beanInv.getInvocation(), NashornGuards.combineGuards(beanInv.getGuard(), NashornGuards.getNotJSObjectGuard()));
        } else {
            throw new AssertionError();
        }
        return Bootstrap.asTypeSafeReturn(inv, linkerServices, desc);
    }

    private GuardedInvocation lookup(CallSiteDescriptor desc, LinkRequest request, LinkerServices linkerServices) throws Exception {
        int c2;
        String operator = CallSiteDescriptorFactory.tokenizeOperators(desc).get(0);
        c2 = desc.getNameTokenCount();
        switch (operator) {
            case "getProp":
            case "getElem":
            case "getMethod":
                if (c2 > 2) {
                    return findGetMethod(desc);
                }
                return findGetIndexMethod(this.nashornBeansLinker.getGuardedInvocation(request, linkerServices));
            case "setProp":
            case "setElem":
                return c2 > 2 ? findSetMethod(desc) : findSetIndexMethod();
            case "call":
                return findCallMethod(desc);
            case "new":
                return findNewMethod(desc);
            default:
                return null;
        }
    }

    private static GuardedInvocation findGetMethod(CallSiteDescriptor desc) {
        String name = desc.getNameToken(2);
        MethodHandle getter = MH.insertArguments(JSOBJECT_GETMEMBER, 1, name);
        return new GuardedInvocation(getter, IS_JSOBJECT_GUARD);
    }

    private static GuardedInvocation findGetIndexMethod(GuardedInvocation inv) {
        MethodHandle getter = MH.insertArguments(JSOBJECTLINKER_GET, 0, inv.getInvocation());
        return inv.replaceMethods(getter, inv.getGuard());
    }

    private static GuardedInvocation findSetMethod(CallSiteDescriptor desc) {
        MethodHandle getter = MH.insertArguments(JSOBJECT_SETMEMBER, 1, desc.getNameToken(2));
        return new GuardedInvocation(getter, IS_JSOBJECT_GUARD);
    }

    private static GuardedInvocation findSetIndexMethod() {
        return new GuardedInvocation(JSOBJECTLINKER_PUT, IS_JSOBJECT_GUARD);
    }

    private static GuardedInvocation findCallMethod(CallSiteDescriptor desc) {
        MethodHandle mh = NashornCallSiteDescriptor.isScope(desc) ? JSOBJECT_SCOPE_CALL : JSOBJECT_CALL;
        if (NashornCallSiteDescriptor.isApplyToCall(desc)) {
            mh = MH.insertArguments(JSOBJECT_CALL_TO_APPLY, 0, mh);
        }
        MethodType type = desc.getMethodType();
        return new GuardedInvocation(type.parameterType(type.parameterCount() - 1) == Object[].class ? mh : MH.asCollector(mh, Object[].class, type.parameterCount() - 2), IS_JSOBJECT_GUARD);
    }

    private static GuardedInvocation findNewMethod(CallSiteDescriptor desc) {
        MethodHandle func = MH.asCollector(JSOBJECT_NEW, Object[].class, desc.getMethodType().parameterCount() - 1);
        return new GuardedInvocation(func, IS_JSOBJECT_GUARD);
    }

    private static boolean isJSObject(Object self) {
        return self instanceof JSObject;
    }

    private static Object get(MethodHandle fallback, Object jsobj, Object key) throws Throwable {
        if (key instanceof Integer) {
            return ((JSObject) jsobj).getSlot(((Integer) key).intValue());
        }
        if (key instanceof Number) {
            int index = getIndex((Number) key);
            if (index > -1) {
                return ((JSObject) jsobj).getSlot(index);
            }
            return ((JSObject) jsobj).getMember(JSType.toString(key));
        }
        if (JSType.isString(key)) {
            String name = key.toString();
            if (name.indexOf(40) != -1) {
                return (Object) fallback.invokeExact(jsobj, name);
            }
            return ((JSObject) jsobj).getMember(name);
        }
        return null;
    }

    private static void put(Object jsobj, Object key, Object value) {
        if (key instanceof Integer) {
            ((JSObject) jsobj).setSlot(((Integer) key).intValue(), value);
            return;
        }
        if (!(key instanceof Number)) {
            if (JSType.isString(key)) {
                ((JSObject) jsobj).setMember(key.toString(), value);
            }
        } else {
            int index = getIndex((Number) key);
            if (index > -1) {
                ((JSObject) jsobj).setSlot(index, value);
            } else {
                ((JSObject) jsobj).setMember(JSType.toString(key), value);
            }
        }
    }

    private static int getIndex(Number n2) {
        double value = n2.doubleValue();
        if (JSType.isRepresentableAsInt(value)) {
            return (int) value;
        }
        return -1;
    }

    private static Object callToApply(MethodHandle mh, JSObject obj, Object thiz, Object... args) {
        if (!$assertionsDisabled && args.length < 2) {
            throw new AssertionError();
        }
        Object receiver = args[0];
        Object[] arguments = new Object[args.length - 1];
        System.arraycopy(args, 1, arguments, 0, arguments.length);
        try {
            return (Object) mh.invokeExact(obj, thiz, new Object[]{receiver, arguments});
        } catch (Error | RuntimeException e2) {
            throw e2;
        } catch (Throwable e3) {
            throw new RuntimeException(e3);
        }
    }

    private static Object jsObjectScopeCall(JSObject jsObj, Object thiz, Object[] args) {
        Object modifiedThiz;
        if (thiz == ScriptRuntime.UNDEFINED && !jsObj.isStrictFunction()) {
            Global global = Context.getGlobal();
            modifiedThiz = ScriptObjectMirror.wrap(global, global);
        } else {
            modifiedThiz = thiz;
        }
        return jsObj.call(modifiedThiz, args);
    }

    private static MethodHandle findJSObjectMH_V(String name, Class<?> rtype, Class<?>... types) {
        return MH.findVirtual(MethodHandles.lookup(), JSObject.class, name, MH.type(rtype, types));
    }

    private static MethodHandle findOwnMH_S(String name, Class<?> rtype, Class<?>... types) {
        return MH.findStatic(MethodHandles.lookup(), JSObjectLinker.class, name, MH.type(rtype, types));
    }
}
