package com.sun.jna;

import com.sun.org.apache.xalan.internal.xsltc.compiler.Constants;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;
import org.icepdf.core.util.PdfOps;

/* loaded from: JavaFTD2XX.jar:com/sun/jna/Library.class */
public interface Library {
    public static final String OPTION_TYPE_MAPPER = "type-mapper";
    public static final String OPTION_FUNCTION_MAPPER = "function-mapper";
    public static final String OPTION_INVOCATION_MAPPER = "invocation-mapper";
    public static final String OPTION_STRUCTURE_ALIGNMENT = "structure-alignment";
    public static final String OPTION_ALLOW_OBJECTS = "allow-objects";
    public static final String OPTION_CALLING_CONVENTION = "calling-convention";

    /* renamed from: com.sun.jna.Library$1, reason: invalid class name */
    /* loaded from: JavaFTD2XX.jar:com/sun/jna/Library$1.class */
    static class AnonymousClass1 {
        static Class class$java$lang$Object;
        static Class class$com$sun$jna$AltCallingConvention;

        static Class class$(String x0) throws Throwable {
            try {
                return Class.forName(x0);
            } catch (ClassNotFoundException x1) {
                throw new NoClassDefFoundError().initCause(x1);
            }
        }
    }

    /* loaded from: JavaFTD2XX.jar:com/sun/jna/Library$Handler.class */
    public static class Handler implements InvocationHandler {
        static final Method OBJECT_TOSTRING;
        static final Method OBJECT_HASHCODE;
        static final Method OBJECT_EQUALS;
        private final NativeLibrary nativeLibrary;
        private final Class interfaceClass;
        private final Map options;
        private FunctionMapper functionMapper;
        private final InvocationMapper invocationMapper;
        private final Map functions = new WeakHashMap();

        static {
            Class clsClass$;
            Class clsClass$2;
            Class clsClass$3;
            Class<?> clsClass$4;
            try {
                if (AnonymousClass1.class$java$lang$Object == null) {
                    clsClass$ = AnonymousClass1.class$(Constants.OBJECT_CLASS);
                    AnonymousClass1.class$java$lang$Object = clsClass$;
                } else {
                    clsClass$ = AnonymousClass1.class$java$lang$Object;
                }
                OBJECT_TOSTRING = clsClass$.getMethod("toString", new Class[0]);
                if (AnonymousClass1.class$java$lang$Object == null) {
                    clsClass$2 = AnonymousClass1.class$(Constants.OBJECT_CLASS);
                    AnonymousClass1.class$java$lang$Object = clsClass$2;
                } else {
                    clsClass$2 = AnonymousClass1.class$java$lang$Object;
                }
                OBJECT_HASHCODE = clsClass$2.getMethod("hashCode", new Class[0]);
                if (AnonymousClass1.class$java$lang$Object == null) {
                    clsClass$3 = AnonymousClass1.class$(Constants.OBJECT_CLASS);
                    AnonymousClass1.class$java$lang$Object = clsClass$3;
                } else {
                    clsClass$3 = AnonymousClass1.class$java$lang$Object;
                }
                Class<?>[] clsArr = new Class[1];
                if (AnonymousClass1.class$java$lang$Object == null) {
                    clsClass$4 = AnonymousClass1.class$(Constants.OBJECT_CLASS);
                    AnonymousClass1.class$java$lang$Object = clsClass$4;
                } else {
                    clsClass$4 = AnonymousClass1.class$java$lang$Object;
                }
                clsArr[0] = clsClass$4;
                OBJECT_EQUALS = clsClass$3.getMethod("equals", clsArr);
            } catch (Exception e2) {
                throw new Error("Error retrieving Object.toString() method");
            }
        }

        /* loaded from: JavaFTD2XX.jar:com/sun/jna/Library$Handler$FunctionNameMap.class */
        private static class FunctionNameMap implements FunctionMapper {
            private final Map map;

            public FunctionNameMap(Map map) {
                this.map = new HashMap(map);
            }

            @Override // com.sun.jna.FunctionMapper
            public String getFunctionName(NativeLibrary library, Method method) {
                String name = method.getName();
                if (this.map.containsKey(name)) {
                    return (String) this.map.get(name);
                }
                return name;
            }
        }

        public Handler(String libname, Class interfaceClass, Map options) throws Throwable {
            Class clsClass$;
            if (libname != null && "".equals(libname.trim())) {
                throw new IllegalArgumentException(new StringBuffer().append("Invalid library name \"").append(libname).append(PdfOps.DOUBLE_QUOTE__TOKEN).toString());
            }
            this.interfaceClass = interfaceClass;
            Map options2 = new HashMap(options);
            if (AnonymousClass1.class$com$sun$jna$AltCallingConvention == null) {
                clsClass$ = AnonymousClass1.class$("com.sun.jna.AltCallingConvention");
                AnonymousClass1.class$com$sun$jna$AltCallingConvention = clsClass$;
            } else {
                clsClass$ = AnonymousClass1.class$com$sun$jna$AltCallingConvention;
            }
            int callingConvention = clsClass$.isAssignableFrom(interfaceClass) ? 1 : 0;
            if (options2.get(Library.OPTION_CALLING_CONVENTION) == null) {
                options2.put(Library.OPTION_CALLING_CONVENTION, new Integer(callingConvention));
            }
            this.options = options2;
            this.nativeLibrary = NativeLibrary.getInstance(libname, options2);
            this.functionMapper = (FunctionMapper) options2.get(Library.OPTION_FUNCTION_MAPPER);
            if (this.functionMapper == null) {
                this.functionMapper = new FunctionNameMap(options2);
            }
            this.invocationMapper = (InvocationMapper) options2.get(Library.OPTION_INVOCATION_MAPPER);
        }

        public NativeLibrary getNativeLibrary() {
            return this.nativeLibrary;
        }

        public String getLibraryName() {
            return this.nativeLibrary.getName();
        }

        public Class getInterfaceClass() {
            return this.interfaceClass;
        }

        /* loaded from: JavaFTD2XX.jar:com/sun/jna/Library$Handler$FunctionInfo.class */
        private static class FunctionInfo {
            InvocationHandler handler;
            Function function;
            boolean isVarArgs;
            Map options;

            private FunctionInfo() {
            }

            FunctionInfo(AnonymousClass1 x0) {
                this();
            }
        }

        @Override // java.lang.reflect.InvocationHandler
        public Object invoke(Object proxy, Method method, Object[] inArgs) throws Throwable {
            FunctionInfo f2;
            if (OBJECT_TOSTRING.equals(method)) {
                return new StringBuffer().append("Proxy interface to ").append((Object) this.nativeLibrary).toString();
            }
            if (OBJECT_HASHCODE.equals(method)) {
                return new Integer(hashCode());
            }
            if (OBJECT_EQUALS.equals(method)) {
                Object o2 = inArgs[0];
                if (o2 == null || !Proxy.isProxyClass(o2.getClass())) {
                    return Boolean.FALSE;
                }
                return Function.valueOf(Proxy.getInvocationHandler(o2) == this);
            }
            synchronized (this.functions) {
                f2 = (FunctionInfo) this.functions.get(method);
                if (f2 == null) {
                    f2 = new FunctionInfo(null);
                    f2.isVarArgs = Function.isVarArgs(method);
                    if (this.invocationMapper != null) {
                        f2.handler = this.invocationMapper.getInvocationHandler(this.nativeLibrary, method);
                    }
                    if (f2.handler == null) {
                        String methodName = this.functionMapper.getFunctionName(this.nativeLibrary, method);
                        if (methodName == null) {
                            methodName = method.getName();
                        }
                        f2.function = this.nativeLibrary.getFunction(methodName, method);
                        f2.options = new HashMap(this.options);
                        f2.options.put("invoking-method", method);
                    }
                    this.functions.put(method, f2);
                }
            }
            if (f2.isVarArgs) {
                inArgs = Function.concatenateVarArgs(inArgs);
            }
            if (f2.handler != null) {
                return f2.handler.invoke(proxy, method, inArgs);
            }
            return f2.function.invoke(method.getReturnType(), inArgs, f2.options);
        }
    }
}
