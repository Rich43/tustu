package com.sun.jna;

import java.util.Map;
import java.util.WeakHashMap;

/* loaded from: JavaFTD2XX.jar:com/sun/jna/NativeMappedConverter.class */
public class NativeMappedConverter implements TypeConverter {
    private static Map converters = new WeakHashMap();
    private final Class type;
    private final Class nativeType;
    private final NativeMapped instance;
    static Class class$com$sun$jna$NativeMapped;
    static Class class$com$sun$jna$Pointer;

    public static NativeMappedConverter getInstance(Class cls) {
        NativeMappedConverter nativeMappedConverter;
        synchronized (converters) {
            NativeMappedConverter nmc = (NativeMappedConverter) converters.get(cls);
            if (nmc == null) {
                nmc = new NativeMappedConverter(cls);
                converters.put(cls, nmc);
            }
            nativeMappedConverter = nmc;
        }
        return nativeMappedConverter;
    }

    public NativeMappedConverter(Class type) throws Throwable {
        Class clsClass$;
        Class clsClass$2;
        if (class$com$sun$jna$NativeMapped == null) {
            clsClass$ = class$("com.sun.jna.NativeMapped");
            class$com$sun$jna$NativeMapped = clsClass$;
        } else {
            clsClass$ = class$com$sun$jna$NativeMapped;
        }
        if (!clsClass$.isAssignableFrom(type)) {
            StringBuffer stringBufferAppend = new StringBuffer().append("Type must derive from ");
            if (class$com$sun$jna$NativeMapped == null) {
                clsClass$2 = class$("com.sun.jna.NativeMapped");
                class$com$sun$jna$NativeMapped = clsClass$2;
            } else {
                clsClass$2 = class$com$sun$jna$NativeMapped;
            }
            throw new IllegalArgumentException(stringBufferAppend.append((Object) clsClass$2).toString());
        }
        this.type = type;
        this.instance = defaultValue();
        this.nativeType = this.instance.nativeType();
    }

    static Class class$(String x0) throws Throwable {
        try {
            return Class.forName(x0);
        } catch (ClassNotFoundException x1) {
            throw new NoClassDefFoundError().initCause(x1);
        }
    }

    public NativeMapped defaultValue() {
        try {
            return (NativeMapped) this.type.newInstance();
        } catch (IllegalAccessException e2) {
            String msg = new StringBuffer().append("Not allowed to create an instance of ").append((Object) this.type).append(", requires a public, no-arg constructor: ").append((Object) e2).toString();
            throw new IllegalArgumentException(msg);
        } catch (InstantiationException e3) {
            String msg2 = new StringBuffer().append("Can't create an instance of ").append((Object) this.type).append(", requires a no-arg constructor: ").append((Object) e3).toString();
            throw new IllegalArgumentException(msg2);
        }
    }

    @Override // com.sun.jna.FromNativeConverter
    public Object fromNative(Object nativeValue, FromNativeContext context) {
        return this.instance.fromNative(nativeValue, context);
    }

    @Override // com.sun.jna.FromNativeConverter, com.sun.jna.ToNativeConverter
    public Class nativeType() {
        return this.nativeType;
    }

    @Override // com.sun.jna.ToNativeConverter
    public Object toNative(Object value, ToNativeContext context) {
        Class clsClass$;
        if (value == null) {
            if (class$com$sun$jna$Pointer == null) {
                clsClass$ = class$("com.sun.jna.Pointer");
                class$com$sun$jna$Pointer = clsClass$;
            } else {
                clsClass$ = class$com$sun$jna$Pointer;
            }
            if (clsClass$.isAssignableFrom(this.nativeType)) {
                return null;
            }
            value = defaultValue();
        }
        return ((NativeMapped) value).toNative();
    }
}
