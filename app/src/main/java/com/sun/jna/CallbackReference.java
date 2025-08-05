package com.sun.jna;

import com.sun.jna.Library;
import com.sun.jna.Structure;
import com.sun.org.apache.xalan.internal.xsltc.compiler.Constants;
import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

/* loaded from: JavaFTD2XX.jar:com/sun/jna/CallbackReference.class */
class CallbackReference extends WeakReference {
    static final Map callbackMap = new WeakHashMap();
    static final Map directCallbackMap = new WeakHashMap();
    static final Map allocations = new WeakHashMap();
    private static final Method PROXY_CALLBACK_METHOD;
    Pointer cbstruct;
    CallbackProxy proxy;
    Method method;
    static Class array$Ljava$lang$Object;
    static Class class$com$sun$jna$CallbackProxy;
    static Class class$com$sun$jna$AltCallingConvention;
    static Class class$com$sun$jna$Structure;
    static Class class$com$sun$jna$Structure$ByValue;
    static Class class$com$sun$jna$Pointer;
    static Class class$com$sun$jna$NativeMapped;
    static Class class$java$lang$String;
    static Class class$com$sun$jna$WString;
    static Class array$Ljava$lang$String;
    static Class array$Lcom$sun$jna$WString;
    static Class class$com$sun$jna$Callback;
    static Class class$java$lang$Boolean;
    static Class class$java$lang$Void;
    static Class class$java$lang$Byte;
    static Class class$java$lang$Short;
    static Class class$java$lang$Character;
    static Class class$java$lang$Integer;
    static Class class$java$lang$Long;
    static Class class$java$lang$Float;
    static Class class$java$lang$Double;

    static {
        Class clsClass$;
        Class<?> clsClass$2;
        try {
            if (class$com$sun$jna$CallbackProxy == null) {
                clsClass$ = class$("com.sun.jna.CallbackProxy");
                class$com$sun$jna$CallbackProxy = clsClass$;
            } else {
                clsClass$ = class$com$sun$jna$CallbackProxy;
            }
            Class<?>[] clsArr = new Class[1];
            if (array$Ljava$lang$Object == null) {
                clsClass$2 = class$("[Ljava.lang.Object;");
                array$Ljava$lang$Object = clsClass$2;
            } else {
                clsClass$2 = array$Ljava$lang$Object;
            }
            clsArr[0] = clsClass$2;
            PROXY_CALLBACK_METHOD = clsClass$.getMethod(Callback.METHOD_NAME, clsArr);
        } catch (Exception e2) {
            throw new Error("Error looking up CallbackProxy.callback() method");
        }
    }

    static Class class$(String x0) throws Throwable {
        try {
            return Class.forName(x0);
        } catch (ClassNotFoundException x1) {
            throw new NoClassDefFoundError().initCause(x1);
        }
    }

    public static Callback getCallback(Class type, Pointer p2) {
        return getCallback(type, p2, false);
    }

    private static Callback getCallback(Class type, Pointer p2, boolean direct) {
        Class clsClass$;
        if (p2 == null) {
            return null;
        }
        if (!type.isInterface()) {
            throw new IllegalArgumentException("Callback type must be an interface");
        }
        Map map = direct ? directCallbackMap : callbackMap;
        synchronized (map) {
            for (Callback cb : map.keySet()) {
                if (type.isAssignableFrom(cb.getClass())) {
                    CallbackReference cbref = (CallbackReference) map.get(cb);
                    Pointer cbp = cbref != null ? cbref.getTrampoline() : getNativeFunctionPointer(cb);
                    if (p2.equals(cbp)) {
                        return cb;
                    }
                }
            }
            if (class$com$sun$jna$AltCallingConvention == null) {
                clsClass$ = class$("com.sun.jna.AltCallingConvention");
                class$com$sun$jna$AltCallingConvention = clsClass$;
            } else {
                clsClass$ = class$com$sun$jna$AltCallingConvention;
            }
            int ctype = clsClass$.isAssignableFrom(type) ? 1 : 0;
            Map foptions = new HashMap();
            Map options = Native.getLibraryOptions(type);
            if (options != null) {
                foptions.putAll(options);
            }
            foptions.put("invoking-method", getCallbackMethod(type));
            NativeFunctionHandler h2 = new NativeFunctionHandler(p2, ctype, foptions);
            Callback cb2 = (Callback) Proxy.newProxyInstance(type.getClassLoader(), new Class[]{type}, h2);
            map.put(cb2, null);
            return cb2;
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:19:0x006a, code lost:
    
        r10 = false;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private CallbackReference(com.sun.jna.Callback r8, int r9, boolean r10) throws java.lang.Throwable {
        /*
            Method dump skipped, instructions count: 551
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sun.jna.CallbackReference.<init>(com.sun.jna.Callback, int, boolean):void");
    }

    /* JADX WARN: Code restructure failed: missing block: B:52:0x00f4, code lost:
    
        if (r0.isAssignableFrom(r5) != false) goto L53;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private java.lang.Class getNativeType(java.lang.Class r5) throws java.lang.Throwable {
        /*
            Method dump skipped, instructions count: 271
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sun.jna.CallbackReference.getNativeType(java.lang.Class):java.lang.Class");
    }

    private static Method checkMethod(Method m2) {
        if (m2.getParameterTypes().length > 256) {
            String msg = new StringBuffer().append("Method signature exceeds the maximum parameter count: ").append((Object) m2).toString();
            throw new UnsupportedOperationException(msg);
        }
        return m2;
    }

    static Class findCallbackClass(Class type) throws Throwable {
        Class clsClass$;
        Class clsClass$2;
        Class clsClass$3;
        if (class$com$sun$jna$Callback == null) {
            clsClass$ = class$("com.sun.jna.Callback");
            class$com$sun$jna$Callback = clsClass$;
        } else {
            clsClass$ = class$com$sun$jna$Callback;
        }
        if (!clsClass$.isAssignableFrom(type)) {
            throw new IllegalArgumentException(new StringBuffer().append(type.getName()).append(" is not derived from com.sun.jna.Callback").toString());
        }
        if (type.isInterface()) {
            return type;
        }
        Class[] ifaces = type.getInterfaces();
        int i2 = 0;
        while (true) {
            if (i2 >= ifaces.length) {
                break;
            }
            if (class$com$sun$jna$Callback == null) {
                clsClass$3 = class$("com.sun.jna.Callback");
                class$com$sun$jna$Callback = clsClass$3;
            } else {
                clsClass$3 = class$com$sun$jna$Callback;
            }
            if (!clsClass$3.isAssignableFrom(ifaces[i2])) {
                i2++;
            } else {
                try {
                    getCallbackMethod(ifaces[i2]);
                    return ifaces[i2];
                } catch (IllegalArgumentException e2) {
                    if (class$com$sun$jna$Callback == null) {
                        clsClass$2 = class$("com.sun.jna.Callback");
                        class$com$sun$jna$Callback = clsClass$2;
                    } else {
                        clsClass$2 = class$com$sun$jna$Callback;
                    }
                    if (clsClass$2.isAssignableFrom(type.getSuperclass())) {
                        return findCallbackClass(type.getSuperclass());
                    }
                    return type;
                }
            }
        }
    }

    private static Method getCallbackMethod(Callback callback) {
        return getCallbackMethod(findCallbackClass(callback.getClass()));
    }

    private static Method getCallbackMethod(Class cls) throws SecurityException {
        Method[] pubMethods = cls.getDeclaredMethods();
        Method[] classMethods = cls.getMethods();
        Set pmethods = new HashSet(Arrays.asList(pubMethods));
        pmethods.retainAll(Arrays.asList(classMethods));
        Iterator i2 = pmethods.iterator();
        while (i2.hasNext()) {
            if (Callback.FORBIDDEN_NAMES.contains(((Method) i2.next()).getName())) {
                i2.remove();
            }
        }
        Method[] methods = (Method[]) pmethods.toArray(new Method[pmethods.size()]);
        if (methods.length == 1) {
            return checkMethod(methods[0]);
        }
        for (Method m2 : methods) {
            if (Callback.METHOD_NAME.equals(m2.getName())) {
                return checkMethod(m2);
            }
        }
        throw new IllegalArgumentException("Callback must implement a single public method, or one public method named 'callback'");
    }

    public Pointer getTrampoline() {
        return this.cbstruct.getPointer(0L);
    }

    protected void finalize() {
        dispose();
    }

    protected synchronized void dispose() {
        if (this.cbstruct != null) {
            Native.freeNativeCallback(this.cbstruct.peer);
            this.cbstruct.peer = 0L;
            this.cbstruct = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Callback getCallback() {
        return (Callback) get();
    }

    private static Pointer getNativeFunctionPointer(Callback cb) throws IllegalArgumentException {
        if (Proxy.isProxyClass(cb.getClass())) {
            Object handler = Proxy.getInvocationHandler(cb);
            if (handler instanceof NativeFunctionHandler) {
                return ((NativeFunctionHandler) handler).getPointer();
            }
            return null;
        }
        return null;
    }

    public static Pointer getFunctionPointer(Callback cb) {
        return getFunctionPointer(cb, false);
    }

    private static Pointer getFunctionPointer(Callback cb, boolean direct) throws IllegalArgumentException {
        Pointer trampoline;
        if (cb == null) {
            return null;
        }
        Pointer fp = getNativeFunctionPointer(cb);
        if (fp != null) {
            return fp;
        }
        int callingConvention = cb instanceof AltCallingConvention ? 1 : 0;
        Map map = direct ? directCallbackMap : callbackMap;
        synchronized (map) {
            CallbackReference cbref = (CallbackReference) map.get(cb);
            if (cbref == null) {
                cbref = new CallbackReference(cb, callingConvention, direct);
                map.put(cb, cbref);
            }
            trampoline = cbref.getTrampoline();
        }
        return trampoline;
    }

    /* loaded from: JavaFTD2XX.jar:com/sun/jna/CallbackReference$DefaultCallbackProxy.class */
    private class DefaultCallbackProxy implements CallbackProxy {
        private Method callbackMethod;
        private ToNativeConverter toNative;
        private FromNativeConverter[] fromNative;
        private final CallbackReference this$0;

        public DefaultCallbackProxy(CallbackReference callbackReference, Method callbackMethod, TypeMapper mapper) throws Throwable {
            Class clsClass$;
            Class clsClass$2;
            this.this$0 = callbackReference;
            this.callbackMethod = callbackMethod;
            Class[] argTypes = callbackMethod.getParameterTypes();
            Class returnType = callbackMethod.getReturnType();
            this.fromNative = new FromNativeConverter[argTypes.length];
            if (CallbackReference.class$com$sun$jna$NativeMapped == null) {
                clsClass$ = CallbackReference.class$("com.sun.jna.NativeMapped");
                CallbackReference.class$com$sun$jna$NativeMapped = clsClass$;
            } else {
                clsClass$ = CallbackReference.class$com$sun$jna$NativeMapped;
            }
            if (clsClass$.isAssignableFrom(returnType)) {
                this.toNative = NativeMappedConverter.getInstance(returnType);
            } else if (mapper != null) {
                this.toNative = mapper.getToNativeConverter(returnType);
            }
            for (int i2 = 0; i2 < this.fromNative.length; i2++) {
                if (CallbackReference.class$com$sun$jna$NativeMapped == null) {
                    clsClass$2 = CallbackReference.class$("com.sun.jna.NativeMapped");
                    CallbackReference.class$com$sun$jna$NativeMapped = clsClass$2;
                } else {
                    clsClass$2 = CallbackReference.class$com$sun$jna$NativeMapped;
                }
                if (clsClass$2.isAssignableFrom(argTypes[i2])) {
                    this.fromNative[i2] = new NativeMappedConverter(argTypes[i2]);
                } else if (mapper != null) {
                    this.fromNative[i2] = mapper.getFromNativeConverter(argTypes[i2]);
                }
            }
            if (!callbackMethod.isAccessible()) {
                try {
                    callbackMethod.setAccessible(true);
                } catch (SecurityException e2) {
                    throw new IllegalArgumentException(new StringBuffer().append("Callback method is inaccessible, make sure the interface is public: ").append((Object) callbackMethod).toString());
                }
            }
        }

        private Object invokeCallback(Object[] args) throws Throwable {
            Class[] paramTypes = this.callbackMethod.getParameterTypes();
            Object[] callbackArgs = new Object[args.length];
            for (int i2 = 0; i2 < args.length; i2++) {
                Class type = paramTypes[i2];
                Object arg = args[i2];
                if (this.fromNative[i2] != null) {
                    FromNativeContext context = new CallbackParameterContext(type, this.callbackMethod, args, i2);
                    callbackArgs[i2] = this.fromNative[i2].fromNative(arg, context);
                } else {
                    callbackArgs[i2] = convertArgument(arg, type);
                }
            }
            Object result = null;
            Callback cb = this.this$0.getCallback();
            if (cb != null) {
                try {
                    result = convertResult(this.callbackMethod.invoke(cb, callbackArgs));
                } catch (IllegalAccessException e2) {
                    Native.getCallbackExceptionHandler().uncaughtException(cb, e2);
                } catch (IllegalArgumentException e3) {
                    Native.getCallbackExceptionHandler().uncaughtException(cb, e3);
                } catch (InvocationTargetException e4) {
                    Native.getCallbackExceptionHandler().uncaughtException(cb, e4.getTargetException());
                }
            }
            for (int i3 = 0; i3 < callbackArgs.length; i3++) {
                if ((callbackArgs[i3] instanceof Structure) && !(callbackArgs[i3] instanceof Structure.ByValue)) {
                    ((Structure) callbackArgs[i3]).autoWrite();
                }
            }
            return result;
        }

        @Override // com.sun.jna.CallbackProxy
        public Object callback(Object[] args) {
            try {
                return invokeCallback(args);
            } catch (Throwable t2) {
                Native.getCallbackExceptionHandler().uncaughtException(this.this$0.getCallback(), t2);
                return null;
            }
        }

        /* JADX WARN: Removed duplicated region for block: B:26:0x008b  */
        /* JADX WARN: Removed duplicated region for block: B:64:0x0172  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        private java.lang.Object convertArgument(java.lang.Object r8, java.lang.Class r9) throws java.lang.Throwable {
            /*
                Method dump skipped, instructions count: 398
                To view this dump change 'Code comments level' option to 'DEBUG'
            */
            throw new UnsupportedOperationException("Method not decompiled: com.sun.jna.CallbackReference.DefaultCallbackProxy.convertArgument(java.lang.Object, java.lang.Class):java.lang.Object");
        }

        private Object convertResult(Object value) throws Throwable {
            Class clsClass$;
            Class clsClass$2;
            Class clsClass$3;
            Class clsClass$4;
            Class clsClass$5;
            Class clsClass$6;
            Class clsClass$7;
            Class clsClass$8;
            Class clsClass$9;
            Class clsClass$10;
            if (this.toNative != null) {
                value = this.toNative.toNative(value, new CallbackResultContext(this.callbackMethod));
            }
            if (value == null) {
                return null;
            }
            Class cls = value.getClass();
            if (CallbackReference.class$com$sun$jna$Structure == null) {
                clsClass$ = CallbackReference.class$("com.sun.jna.Structure");
                CallbackReference.class$com$sun$jna$Structure = clsClass$;
            } else {
                clsClass$ = CallbackReference.class$com$sun$jna$Structure;
            }
            if (clsClass$.isAssignableFrom(cls)) {
                if (CallbackReference.class$com$sun$jna$Structure$ByValue == null) {
                    clsClass$10 = CallbackReference.class$("com.sun.jna.Structure$ByValue");
                    CallbackReference.class$com$sun$jna$Structure$ByValue = clsClass$10;
                } else {
                    clsClass$10 = CallbackReference.class$com$sun$jna$Structure$ByValue;
                }
                if (clsClass$10.isAssignableFrom(cls)) {
                    return value;
                }
                return ((Structure) value).getPointer();
            }
            if (cls != Boolean.TYPE) {
                if (CallbackReference.class$java$lang$Boolean == null) {
                    clsClass$2 = CallbackReference.class$(Constants.BOOLEAN_CLASS);
                    CallbackReference.class$java$lang$Boolean = clsClass$2;
                } else {
                    clsClass$2 = CallbackReference.class$java$lang$Boolean;
                }
                if (cls != clsClass$2) {
                    if (CallbackReference.class$java$lang$String == null) {
                        clsClass$3 = CallbackReference.class$("java.lang.String");
                        CallbackReference.class$java$lang$String = clsClass$3;
                    } else {
                        clsClass$3 = CallbackReference.class$java$lang$String;
                    }
                    if (cls != clsClass$3) {
                        if (CallbackReference.class$com$sun$jna$WString == null) {
                            clsClass$5 = CallbackReference.class$("com.sun.jna.WString");
                            CallbackReference.class$com$sun$jna$WString = clsClass$5;
                        } else {
                            clsClass$5 = CallbackReference.class$com$sun$jna$WString;
                        }
                        if (cls != clsClass$5) {
                            if (CallbackReference.array$Ljava$lang$String == null) {
                                clsClass$6 = CallbackReference.class$("[Ljava.lang.String;");
                                CallbackReference.array$Ljava$lang$String = clsClass$6;
                            } else {
                                clsClass$6 = CallbackReference.array$Ljava$lang$String;
                            }
                            if (cls != clsClass$6) {
                                if (CallbackReference.class$com$sun$jna$WString == null) {
                                    clsClass$8 = CallbackReference.class$("com.sun.jna.WString");
                                    CallbackReference.class$com$sun$jna$WString = clsClass$8;
                                } else {
                                    clsClass$8 = CallbackReference.class$com$sun$jna$WString;
                                }
                                if (cls != clsClass$8) {
                                    if (CallbackReference.class$com$sun$jna$Callback == null) {
                                        clsClass$9 = CallbackReference.class$("com.sun.jna.Callback");
                                        CallbackReference.class$com$sun$jna$Callback = clsClass$9;
                                    } else {
                                        clsClass$9 = CallbackReference.class$com$sun$jna$Callback;
                                    }
                                    if (clsClass$9.isAssignableFrom(cls)) {
                                        return CallbackReference.getFunctionPointer((Callback) value);
                                    }
                                    return value;
                                }
                            }
                            if (CallbackReference.array$Ljava$lang$String == null) {
                                clsClass$7 = CallbackReference.class$("[Ljava.lang.String;");
                                CallbackReference.array$Ljava$lang$String = clsClass$7;
                            } else {
                                clsClass$7 = CallbackReference.array$Ljava$lang$String;
                            }
                            StringArray sa = cls == clsClass$7 ? new StringArray((String[]) value) : new StringArray((WString[]) value);
                            CallbackReference.allocations.put(value, sa);
                            return sa;
                        }
                    }
                    Object obj = value;
                    if (CallbackReference.class$com$sun$jna$WString == null) {
                        clsClass$4 = CallbackReference.class$("com.sun.jna.WString");
                        CallbackReference.class$com$sun$jna$WString = clsClass$4;
                    } else {
                        clsClass$4 = CallbackReference.class$com$sun$jna$WString;
                    }
                    return CallbackReference.getNativeString(obj, cls == clsClass$4);
                }
            }
            return Boolean.TRUE.equals(value) ? Function.INTEGER_TRUE : Function.INTEGER_FALSE;
        }

        @Override // com.sun.jna.CallbackProxy
        public Class[] getParameterTypes() {
            return this.callbackMethod.getParameterTypes();
        }

        @Override // com.sun.jna.CallbackProxy
        public Class getReturnType() {
            return this.callbackMethod.getReturnType();
        }
    }

    /* loaded from: JavaFTD2XX.jar:com/sun/jna/CallbackReference$NativeFunctionHandler.class */
    private static class NativeFunctionHandler implements InvocationHandler {
        private Function function;
        private Map options;

        public NativeFunctionHandler(Pointer address, int callingConvention, Map options) {
            this.function = new Function(address, callingConvention);
            this.options = options;
        }

        @Override // java.lang.reflect.InvocationHandler
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if (Library.Handler.OBJECT_TOSTRING.equals(method)) {
                String str = new StringBuffer().append("Proxy interface to ").append((Object) this.function).toString();
                Method m2 = (Method) this.options.get("invoking-method");
                Class cls = CallbackReference.findCallbackClass(m2.getDeclaringClass());
                return new StringBuffer().append(str).append(" (").append(cls.getName()).append(")").toString();
            }
            if (Library.Handler.OBJECT_HASHCODE.equals(method)) {
                return new Integer(hashCode());
            }
            if (Library.Handler.OBJECT_EQUALS.equals(method)) {
                Object o2 = args[0];
                if (o2 == null || !Proxy.isProxyClass(o2.getClass())) {
                    return Boolean.FALSE;
                }
                return Function.valueOf(Proxy.getInvocationHandler(o2) == this);
            }
            if (Function.isVarArgs(method)) {
                args = Function.concatenateVarArgs(args);
            }
            return this.function.invoke(method.getReturnType(), args, this.options);
        }

        public Pointer getPointer() {
            return this.function;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:77:0x0158  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private static boolean isAllowableNativeType(java.lang.Class r4) throws java.lang.Throwable {
        /*
            Method dump skipped, instructions count: 378
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sun.jna.CallbackReference.isAllowableNativeType(java.lang.Class):boolean");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static Pointer getNativeString(Object value, boolean wide) {
        if (value != null) {
            NativeString ns = new NativeString(value.toString(), wide);
            allocations.put(value, ns);
            return ns.getPointer();
        }
        return null;
    }
}
