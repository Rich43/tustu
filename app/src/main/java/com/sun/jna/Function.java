package com.sun.jna;

import com.sun.jna.Structure;
import com.sun.org.apache.xalan.internal.xsltc.compiler.Constants;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Map;

/* loaded from: JavaFTD2XX.jar:com/sun/jna/Function.class */
public class Function extends Pointer {
    public static final int MAX_NARGS = 256;
    public static final int C_CONVENTION = 0;
    public static final int ALT_CONVENTION = 1;
    private static final int MASK_CC = 3;
    public static final int THROW_LAST_ERROR = 4;
    static final Integer INTEGER_TRUE = new Integer(-1);
    static final Integer INTEGER_FALSE = new Integer(0);
    private NativeLibrary library;
    private final String functionName;
    int callFlags;
    final Map options;
    static final String OPTION_INVOKING_METHOD = "invoking-method";
    static Class class$com$sun$jna$NativeMapped;
    static Class array$Lcom$sun$jna$Structure$ByReference;
    static Class array$Lcom$sun$jna$Structure;
    static Class class$java$lang$Void;
    static Class class$java$lang$Boolean;
    static Class class$java$lang$Byte;
    static Class class$java$lang$Short;
    static Class class$java$lang$Character;
    static Class class$java$lang$Integer;
    static Class class$java$lang$Long;
    static Class class$java$lang$Float;
    static Class class$java$lang$Double;
    static Class class$java$lang$String;
    static Class class$com$sun$jna$WString;
    static Class class$com$sun$jna$Pointer;
    static Class class$com$sun$jna$Structure;
    static Class class$com$sun$jna$Structure$ByValue;
    static Class class$com$sun$jna$Callback;
    static Class array$Ljava$lang$String;
    static Class array$Lcom$sun$jna$WString;
    static Class array$Lcom$sun$jna$Pointer;
    static Class class$java$lang$Object;
    static Class array$Lcom$sun$jna$NativeMapped;
    static Class class$com$sun$jna$Structure$ByReference;

    /* loaded from: JavaFTD2XX.jar:com/sun/jna/Function$PostCallRead.class */
    public interface PostCallRead {
        void read();
    }

    public static Function getFunction(String libraryName, String functionName) {
        return NativeLibrary.getInstance(libraryName).getFunction(functionName);
    }

    public static Function getFunction(String libraryName, String functionName, int callFlags) {
        return NativeLibrary.getInstance(libraryName).getFunction(functionName, callFlags);
    }

    public static Function getFunction(Pointer p2) {
        return getFunction(p2, 0);
    }

    public static Function getFunction(Pointer p2, int callFlags) {
        return new Function(p2, callFlags);
    }

    Function(NativeLibrary library, String functionName, int callFlags) throws IllegalArgumentException {
        checkCallingConvention(callFlags & 3);
        if (functionName == null) {
            throw new NullPointerException("Function name must not be null");
        }
        this.library = library;
        this.functionName = functionName;
        this.callFlags = callFlags;
        this.options = library.options;
        try {
            this.peer = library.getSymbolAddress(functionName);
        } catch (UnsatisfiedLinkError e2) {
            throw new UnsatisfiedLinkError(new StringBuffer().append("Error looking up function '").append(functionName).append("': ").append(e2.getMessage()).toString());
        }
    }

    Function(Pointer functionAddress, int callFlags) throws IllegalArgumentException {
        checkCallingConvention(callFlags & 3);
        if (functionAddress == null || functionAddress.peer == 0) {
            throw new NullPointerException("Function address may not be null");
        }
        this.functionName = functionAddress.toString();
        this.callFlags = callFlags;
        this.peer = functionAddress.peer;
        this.options = Collections.EMPTY_MAP;
    }

    private void checkCallingConvention(int convention) throws IllegalArgumentException {
        switch (convention) {
            case 0:
            case 1:
                return;
            default:
                throw new IllegalArgumentException(new StringBuffer().append("Unrecognized calling convention: ").append(convention).toString());
        }
    }

    public String getName() {
        return this.functionName;
    }

    public int getCallingConvention() {
        return this.callFlags & 3;
    }

    public Object invoke(Class returnType, Object[] inArgs) {
        return invoke(returnType, inArgs, this.options);
    }

    public Object invoke(Class returnType, Object[] inArgs, Map options) throws Throwable {
        Class clsClass$;
        Class clsClass$2;
        Class clsClass$3;
        FromNativeContext context;
        Object[] args = new Object[0];
        if (inArgs != null) {
            if (inArgs.length > 256) {
                throw new UnsupportedOperationException("Maximum argument count is 256");
            }
            args = new Object[inArgs.length];
            System.arraycopy(inArgs, 0, args, 0, args.length);
        }
        TypeMapper mapper = (TypeMapper) options.get(Library.OPTION_TYPE_MAPPER);
        Method invokingMethod = (Method) options.get(OPTION_INVOKING_METHOD);
        boolean allowObjects = Boolean.TRUE.equals(options.get(Library.OPTION_ALLOW_OBJECTS));
        for (int i2 = 0; i2 < args.length; i2++) {
            args[i2] = convertArgument(args, i2, invokingMethod, mapper, allowObjects);
        }
        Class nativeType = returnType;
        FromNativeConverter resultConverter = null;
        if (class$com$sun$jna$NativeMapped == null) {
            clsClass$ = class$("com.sun.jna.NativeMapped");
            class$com$sun$jna$NativeMapped = clsClass$;
        } else {
            clsClass$ = class$com$sun$jna$NativeMapped;
        }
        if (clsClass$.isAssignableFrom(returnType)) {
            NativeMappedConverter tc = NativeMappedConverter.getInstance(returnType);
            resultConverter = tc;
            nativeType = tc.nativeType();
        } else if (mapper != null) {
            resultConverter = mapper.getFromNativeConverter(returnType);
            if (resultConverter != null) {
                nativeType = resultConverter.nativeType();
            }
        }
        Object result = invoke(args, nativeType, allowObjects);
        if (resultConverter != null) {
            if (invokingMethod != null) {
                context = new MethodResultContext(returnType, this, inArgs, invokingMethod);
            } else {
                context = new FunctionResultContext(returnType, this, inArgs);
            }
            result = resultConverter.fromNative(result, context);
        }
        if (inArgs != null) {
            for (int i3 = 0; i3 < inArgs.length; i3++) {
                Object inArg = inArgs[i3];
                if (inArg != null) {
                    if (inArg instanceof Structure) {
                        if (!(inArg instanceof Structure.ByValue)) {
                            ((Structure) inArg).autoRead();
                        }
                    } else if (args[i3] instanceof PostCallRead) {
                        ((PostCallRead) args[i3]).read();
                        if (args[i3] instanceof PointerArray) {
                            PointerArray array = (PointerArray) args[i3];
                            if (array$Lcom$sun$jna$Structure$ByReference == null) {
                                clsClass$3 = class$("[Lcom.sun.jna.Structure$ByReference;");
                                array$Lcom$sun$jna$Structure$ByReference = clsClass$3;
                            } else {
                                clsClass$3 = array$Lcom$sun$jna$Structure$ByReference;
                            }
                            if (clsClass$3.isAssignableFrom(inArg.getClass())) {
                                Class type = inArg.getClass().getComponentType();
                                Structure[] ss = (Structure[]) inArg;
                                for (int si = 0; si < ss.length; si++) {
                                    Pointer p2 = array.getPointer(Pointer.SIZE * si);
                                    ss[si] = Structure.updateStructureByReference(type, ss[si], p2);
                                }
                            }
                        }
                    } else {
                        if (array$Lcom$sun$jna$Structure == null) {
                            clsClass$2 = class$("[Lcom.sun.jna.Structure;");
                            array$Lcom$sun$jna$Structure = clsClass$2;
                        } else {
                            clsClass$2 = array$Lcom$sun$jna$Structure;
                        }
                        if (clsClass$2.isAssignableFrom(inArg.getClass())) {
                            Structure.autoRead((Structure[]) inArg);
                        }
                    }
                }
            }
        }
        return result;
    }

    static Class class$(String x0) throws Throwable {
        try {
            return Class.forName(x0);
        } catch (ClassNotFoundException x1) {
            throw new NoClassDefFoundError().initCause(x1);
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:11:0x0027  */
    /* JADX WARN: Removed duplicated region for block: B:19:0x0059  */
    /* JADX WARN: Removed duplicated region for block: B:31:0x0095  */
    /* JADX WARN: Removed duplicated region for block: B:39:0x00ce  */
    /* JADX WARN: Removed duplicated region for block: B:47:0x0107  */
    /* JADX WARN: Removed duplicated region for block: B:55:0x0140  */
    /* JADX WARN: Removed duplicated region for block: B:63:0x0178  */
    /* JADX WARN: Removed duplicated region for block: B:71:0x01b0  */
    /* JADX WARN: Removed duplicated region for block: B:79:0x01e8  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    java.lang.Object invoke(java.lang.Object[] r8, java.lang.Class r9, boolean r10) throws java.lang.Throwable {
        /*
            Method dump skipped, instructions count: 1154
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sun.jna.Function.invoke(java.lang.Object[], java.lang.Class, boolean):java.lang.Object");
    }

    private Pointer invokePointer(int callFlags, Object[] args) {
        long ptr = Native.invokePointer(this.peer, callFlags, args);
        if (ptr == 0) {
            return null;
        }
        return new Pointer(ptr);
    }

    private Object convertArgument(Object[] args, int index, Method invokingMethod, TypeMapper mapper, boolean allowObjects) throws Throwable {
        Class clsClass$;
        Class clsClass$2;
        Class clsClass$3;
        Class clsClass$4;
        Class clsClass$5;
        Class clsClass$6;
        Class clsClass$7;
        Class clsClass$8;
        Class clsClass$9;
        ToNativeContext context;
        Object arg = args[index];
        if (arg != null) {
            Class type = arg.getClass();
            ToNativeConverter converter = null;
            if (class$com$sun$jna$NativeMapped == null) {
                clsClass$9 = class$("com.sun.jna.NativeMapped");
                class$com$sun$jna$NativeMapped = clsClass$9;
            } else {
                clsClass$9 = class$com$sun$jna$NativeMapped;
            }
            if (clsClass$9.isAssignableFrom(type)) {
                converter = NativeMappedConverter.getInstance(type);
            } else if (mapper != null) {
                converter = mapper.getToNativeConverter(type);
            }
            if (converter != null) {
                if (invokingMethod != null) {
                    context = new MethodParameterContext(this, args, index, invokingMethod);
                } else {
                    context = new FunctionParameterContext(this, args, index);
                }
                arg = converter.toNative(arg, context);
            }
        }
        if (arg == null || isPrimitiveArray(arg.getClass())) {
            return arg;
        }
        Class argClass = arg.getClass();
        if (arg instanceof Structure) {
            Structure struct = (Structure) arg;
            struct.autoWrite();
            if (struct instanceof Structure.ByValue) {
                Class ptype = struct.getClass();
                if (invokingMethod != null) {
                    Class[] ptypes = invokingMethod.getParameterTypes();
                    if (!isVarArgs(invokingMethod) || index < ptypes.length - 1) {
                        ptype = ptypes[index];
                    } else {
                        Class etype = ptypes[ptypes.length - 1].getComponentType();
                        if (class$java$lang$Object == null) {
                            clsClass$8 = class$(Constants.OBJECT_CLASS);
                            class$java$lang$Object = clsClass$8;
                        } else {
                            clsClass$8 = class$java$lang$Object;
                        }
                        if (etype != clsClass$8) {
                            ptype = etype;
                        }
                    }
                }
                if (class$com$sun$jna$Structure$ByValue == null) {
                    clsClass$7 = class$("com.sun.jna.Structure$ByValue");
                    class$com$sun$jna$Structure$ByValue = clsClass$7;
                } else {
                    clsClass$7 = class$com$sun$jna$Structure$ByValue;
                }
                if (clsClass$7.isAssignableFrom(ptype)) {
                    return struct;
                }
            }
            return struct.getPointer();
        }
        if (arg instanceof Callback) {
            return CallbackReference.getFunctionPointer((Callback) arg);
        }
        if (arg instanceof String) {
            return new NativeString((String) arg, false).getPointer();
        }
        if (arg instanceof WString) {
            return new NativeString(arg.toString(), true).getPointer();
        }
        if (arg instanceof Boolean) {
            return Boolean.TRUE.equals(arg) ? INTEGER_TRUE : INTEGER_FALSE;
        }
        if (array$Ljava$lang$String == null) {
            clsClass$ = class$("[Ljava.lang.String;");
            array$Ljava$lang$String = clsClass$;
        } else {
            clsClass$ = array$Ljava$lang$String;
        }
        if (clsClass$ == argClass) {
            return new StringArray((String[]) arg);
        }
        if (array$Lcom$sun$jna$WString == null) {
            clsClass$2 = class$("[Lcom.sun.jna.WString;");
            array$Lcom$sun$jna$WString = clsClass$2;
        } else {
            clsClass$2 = array$Lcom$sun$jna$WString;
        }
        if (clsClass$2 == argClass) {
            return new StringArray((WString[]) arg);
        }
        if (array$Lcom$sun$jna$Pointer == null) {
            clsClass$3 = class$("[Lcom.sun.jna.Pointer;");
            array$Lcom$sun$jna$Pointer = clsClass$3;
        } else {
            clsClass$3 = array$Lcom$sun$jna$Pointer;
        }
        if (clsClass$3 == argClass) {
            return new PointerArray((Pointer[]) arg);
        }
        if (array$Lcom$sun$jna$NativeMapped == null) {
            clsClass$4 = class$("[Lcom.sun.jna.NativeMapped;");
            array$Lcom$sun$jna$NativeMapped = clsClass$4;
        } else {
            clsClass$4 = array$Lcom$sun$jna$NativeMapped;
        }
        if (clsClass$4.isAssignableFrom(argClass)) {
            return new NativeMappedArray((NativeMapped[]) arg);
        }
        if (array$Lcom$sun$jna$Structure == null) {
            clsClass$5 = class$("[Lcom.sun.jna.Structure;");
            array$Lcom$sun$jna$Structure = clsClass$5;
        } else {
            clsClass$5 = array$Lcom$sun$jna$Structure;
        }
        if (clsClass$5.isAssignableFrom(argClass)) {
            Structure[] ss = (Structure[]) arg;
            Class type2 = argClass.getComponentType();
            if (class$com$sun$jna$Structure$ByReference == null) {
                clsClass$6 = class$("com.sun.jna.Structure$ByReference");
                class$com$sun$jna$Structure$ByReference = clsClass$6;
            } else {
                clsClass$6 = class$com$sun$jna$Structure$ByReference;
            }
            boolean byRef = clsClass$6.isAssignableFrom(type2);
            if (byRef) {
                Pointer[] pointers = new Pointer[ss.length + 1];
                for (int i2 = 0; i2 < ss.length; i2++) {
                    pointers[i2] = ss[i2] != null ? ss[i2].getPointer() : null;
                }
                return new PointerArray(pointers);
            }
            if (ss.length == 0) {
                throw new IllegalArgumentException("Structure array must have non-zero length");
            }
            if (ss[0] == null) {
                Structure.newInstance(type2).toArray(ss);
                return ss[0].getPointer();
            }
            Structure.autoWrite(ss);
            return ss[0].getPointer();
        }
        if (argClass.isArray()) {
            throw new IllegalArgumentException(new StringBuffer().append("Unsupported array argument type: ").append((Object) argClass.getComponentType()).toString());
        }
        if (allowObjects) {
            return arg;
        }
        if (!Native.isSupportedNativeType(arg.getClass())) {
            throw new IllegalArgumentException(new StringBuffer().append("Unsupported argument type ").append(arg.getClass().getName()).append(" at parameter ").append(index).append(" of function ").append(getName()).toString());
        }
        return arg;
    }

    private boolean isPrimitiveArray(Class argClass) {
        return argClass.isArray() && argClass.getComponentType().isPrimitive();
    }

    public void invoke(Object[] args) throws Throwable {
        Class clsClass$;
        if (class$java$lang$Void == null) {
            clsClass$ = class$("java.lang.Void");
            class$java$lang$Void = clsClass$;
        } else {
            clsClass$ = class$java$lang$Void;
        }
        invoke(clsClass$, args);
    }

    private String invokeString(int callFlags, Object[] args, boolean wide) {
        Pointer ptr = invokePointer(callFlags, args);
        String s2 = null;
        if (ptr != null) {
            if (wide) {
                s2 = ptr.getString(0L, wide);
            } else {
                s2 = ptr.getString(0L);
            }
        }
        return s2;
    }

    @Override // com.sun.jna.Pointer
    public String toString() {
        if (this.library != null) {
            return new StringBuffer().append("native function ").append(this.functionName).append("(").append(this.library.getName()).append(")@0x").append(Long.toHexString(this.peer)).toString();
        }
        return new StringBuffer().append("native function@0x").append(Long.toHexString(this.peer)).toString();
    }

    public Object invokeObject(Object[] args) throws Throwable {
        Class clsClass$;
        if (class$java$lang$Object == null) {
            clsClass$ = class$(Constants.OBJECT_CLASS);
            class$java$lang$Object = clsClass$;
        } else {
            clsClass$ = class$java$lang$Object;
        }
        return invoke(clsClass$, args);
    }

    public Pointer invokePointer(Object[] args) throws Throwable {
        Class clsClass$;
        if (class$com$sun$jna$Pointer == null) {
            clsClass$ = class$("com.sun.jna.Pointer");
            class$com$sun$jna$Pointer = clsClass$;
        } else {
            clsClass$ = class$com$sun$jna$Pointer;
        }
        return (Pointer) invoke(clsClass$, args);
    }

    public String invokeString(Object[] args, boolean wide) throws Throwable {
        Class clsClass$;
        if (wide) {
            if (class$com$sun$jna$WString == null) {
                clsClass$ = class$("com.sun.jna.WString");
                class$com$sun$jna$WString = clsClass$;
            } else {
                clsClass$ = class$com$sun$jna$WString;
            }
        } else if (class$java$lang$String == null) {
            clsClass$ = class$("java.lang.String");
            class$java$lang$String = clsClass$;
        } else {
            clsClass$ = class$java$lang$String;
        }
        Object o2 = invoke(clsClass$, args);
        if (o2 != null) {
            return o2.toString();
        }
        return null;
    }

    public int invokeInt(Object[] args) throws Throwable {
        Class clsClass$;
        if (class$java$lang$Integer == null) {
            clsClass$ = class$(Constants.INTEGER_CLASS);
            class$java$lang$Integer = clsClass$;
        } else {
            clsClass$ = class$java$lang$Integer;
        }
        return ((Integer) invoke(clsClass$, args)).intValue();
    }

    public long invokeLong(Object[] args) throws Throwable {
        Class clsClass$;
        if (class$java$lang$Long == null) {
            clsClass$ = class$("java.lang.Long");
            class$java$lang$Long = clsClass$;
        } else {
            clsClass$ = class$java$lang$Long;
        }
        return ((Long) invoke(clsClass$, args)).longValue();
    }

    public float invokeFloat(Object[] args) throws Throwable {
        Class clsClass$;
        if (class$java$lang$Float == null) {
            clsClass$ = class$("java.lang.Float");
            class$java$lang$Float = clsClass$;
        } else {
            clsClass$ = class$java$lang$Float;
        }
        return ((Float) invoke(clsClass$, args)).floatValue();
    }

    public double invokeDouble(Object[] args) throws Throwable {
        Class clsClass$;
        if (class$java$lang$Double == null) {
            clsClass$ = class$(Constants.DOUBLE_CLASS);
            class$java$lang$Double = clsClass$;
        } else {
            clsClass$ = class$java$lang$Double;
        }
        return ((Double) invoke(clsClass$, args)).doubleValue();
    }

    public void invokeVoid(Object[] args) throws Throwable {
        Class clsClass$;
        if (class$java$lang$Void == null) {
            clsClass$ = class$("java.lang.Void");
            class$java$lang$Void = clsClass$;
        } else {
            clsClass$ = class$java$lang$Void;
        }
        invoke(clsClass$, args);
    }

    @Override // com.sun.jna.Pointer
    public boolean equals(Object o2) {
        if (o2 == this) {
            return true;
        }
        if (o2 != null && o2.getClass() == getClass()) {
            Function other = (Function) o2;
            return other.callFlags == this.callFlags && other.options.equals(this.options) && other.peer == this.peer;
        }
        return false;
    }

    @Override // com.sun.jna.Pointer
    public int hashCode() {
        return this.callFlags + this.options.hashCode() + super.hashCode();
    }

    static Object[] concatenateVarArgs(Object[] inArgs) {
        if (inArgs != null && inArgs.length > 0) {
            Object lastArg = inArgs[inArgs.length - 1];
            Class argType = lastArg != null ? lastArg.getClass() : null;
            if (argType != null && argType.isArray()) {
                Object[] varArgs = (Object[]) lastArg;
                Object[] fullArgs = new Object[inArgs.length + varArgs.length];
                System.arraycopy(inArgs, 0, fullArgs, 0, inArgs.length - 1);
                System.arraycopy(varArgs, 0, fullArgs, inArgs.length - 1, varArgs.length);
                fullArgs[fullArgs.length - 1] = null;
                inArgs = fullArgs;
            }
        }
        return inArgs;
    }

    static boolean isVarArgs(Method m2) {
        try {
            Method v2 = m2.getClass().getMethod("isVarArgs", new Class[0]);
            return Boolean.TRUE.equals(v2.invoke(m2, new Object[0]));
        } catch (IllegalAccessException | IllegalArgumentException | NoSuchMethodException | SecurityException | InvocationTargetException e2) {
            return false;
        }
    }

    /* loaded from: JavaFTD2XX.jar:com/sun/jna/Function$NativeMappedArray.class */
    private static class NativeMappedArray extends Memory implements PostCallRead {
        private final NativeMapped[] original;

        public NativeMappedArray(NativeMapped[] arg) {
            super(Native.getNativeSize(arg.getClass(), arg));
            this.original = arg;
            arg.getClass().getComponentType();
            setValue(0L, this.original, this.original.getClass());
        }

        @Override // com.sun.jna.Function.PostCallRead
        public void read() {
            getValue(0L, this.original.getClass(), this.original);
        }
    }

    /* loaded from: JavaFTD2XX.jar:com/sun/jna/Function$PointerArray.class */
    private static class PointerArray extends Memory implements PostCallRead {
        private final Pointer[] original;

        public PointerArray(Pointer[] arg) {
            super(Pointer.SIZE * (arg.length + 1));
            this.original = arg;
            for (int i2 = 0; i2 < arg.length; i2++) {
                setPointer(i2 * Pointer.SIZE, arg[i2]);
            }
            setPointer(Pointer.SIZE * arg.length, null);
        }

        @Override // com.sun.jna.Function.PostCallRead
        public void read() {
            read(0L, this.original, 0, this.original.length);
        }
    }

    static Boolean valueOf(boolean b2) {
        return b2 ? Boolean.TRUE : Boolean.FALSE;
    }
}
