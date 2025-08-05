package com.sun.jna.win32;

import com.sun.jna.FunctionMapper;
import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;
import com.sun.jna.NativeMappedConverter;
import com.sun.jna.Pointer;
import java.lang.reflect.Method;

/* loaded from: JavaFTD2XX.jar:com/sun/jna/win32/StdCallFunctionMapper.class */
public class StdCallFunctionMapper implements FunctionMapper {
    static Class class$com$sun$jna$NativeMapped;

    protected int getArgumentNativeStackSize(Class cls) throws Throwable {
        Class clsClass$;
        if (class$com$sun$jna$NativeMapped == null) {
            clsClass$ = class$("com.sun.jna.NativeMapped");
            class$com$sun$jna$NativeMapped = clsClass$;
        } else {
            clsClass$ = class$com$sun$jna$NativeMapped;
        }
        if (clsClass$.isAssignableFrom(cls)) {
            cls = NativeMappedConverter.getInstance(cls).nativeType();
        }
        if (cls.isArray()) {
            return Pointer.SIZE;
        }
        try {
            return Native.getNativeSize(cls);
        } catch (IllegalArgumentException e2) {
            throw new IllegalArgumentException(new StringBuffer().append("Unknown native stack allocation size for ").append((Object) cls).toString());
        }
    }

    static Class class$(String x0) throws Throwable {
        try {
            return Class.forName(x0);
        } catch (ClassNotFoundException x1) {
            throw new NoClassDefFoundError().initCause(x1);
        }
    }

    @Override // com.sun.jna.FunctionMapper
    public String getFunctionName(NativeLibrary library, Method method) {
        String name = method.getName();
        int pop = 0;
        Class[] argTypes = method.getParameterTypes();
        for (Class cls : argTypes) {
            pop += getArgumentNativeStackSize(cls);
        }
        String decorated = new StringBuffer().append(name).append("@").append(pop).toString();
        try {
            name = library.getFunction(decorated, 1).getName();
        } catch (UnsatisfiedLinkError e2) {
            try {
                name = library.getFunction(new StringBuffer().append("_").append(decorated).toString(), 1).getName();
            } catch (UnsatisfiedLinkError e3) {
            }
        }
        return name;
    }
}
