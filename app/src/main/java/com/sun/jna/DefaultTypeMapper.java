package com.sun.jna;

import com.sun.org.apache.xalan.internal.xsltc.compiler.Constants;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* loaded from: JavaFTD2XX.jar:com/sun/jna/DefaultTypeMapper.class */
public class DefaultTypeMapper implements TypeMapper {
    private List toNativeConverters = new ArrayList();
    private List fromNativeConverters = new ArrayList();
    static Class class$java$lang$Boolean;
    static Class class$java$lang$Byte;
    static Class class$java$lang$Character;
    static Class class$java$lang$Short;
    static Class class$java$lang$Integer;
    static Class class$java$lang$Long;
    static Class class$java$lang$Float;
    static Class class$java$lang$Double;

    /* loaded from: JavaFTD2XX.jar:com/sun/jna/DefaultTypeMapper$Entry.class */
    private static class Entry {
        public Class type;
        public Object converter;

        public Entry(Class type, Object converter) {
            this.type = type;
            this.converter = converter;
        }
    }

    private Class getAltClass(Class cls) throws Throwable {
        Class clsClass$;
        Class clsClass$2;
        Class clsClass$3;
        Class clsClass$4;
        Class clsClass$5;
        Class clsClass$6;
        Class clsClass$7;
        Class clsClass$8;
        if (class$java$lang$Boolean == null) {
            clsClass$ = class$(Constants.BOOLEAN_CLASS);
            class$java$lang$Boolean = clsClass$;
        } else {
            clsClass$ = class$java$lang$Boolean;
        }
        if (cls == clsClass$) {
            return Boolean.TYPE;
        }
        if (cls == Boolean.TYPE) {
            if (class$java$lang$Boolean != null) {
                return class$java$lang$Boolean;
            }
            Class clsClass$9 = class$(Constants.BOOLEAN_CLASS);
            class$java$lang$Boolean = clsClass$9;
            return clsClass$9;
        }
        if (class$java$lang$Byte == null) {
            clsClass$2 = class$("java.lang.Byte");
            class$java$lang$Byte = clsClass$2;
        } else {
            clsClass$2 = class$java$lang$Byte;
        }
        if (cls == clsClass$2) {
            return Byte.TYPE;
        }
        if (cls == Byte.TYPE) {
            if (class$java$lang$Byte != null) {
                return class$java$lang$Byte;
            }
            Class clsClass$10 = class$("java.lang.Byte");
            class$java$lang$Byte = clsClass$10;
            return clsClass$10;
        }
        if (class$java$lang$Character == null) {
            clsClass$3 = class$("java.lang.Character");
            class$java$lang$Character = clsClass$3;
        } else {
            clsClass$3 = class$java$lang$Character;
        }
        if (cls == clsClass$3) {
            return Character.TYPE;
        }
        if (cls == Character.TYPE) {
            if (class$java$lang$Character != null) {
                return class$java$lang$Character;
            }
            Class clsClass$11 = class$("java.lang.Character");
            class$java$lang$Character = clsClass$11;
            return clsClass$11;
        }
        if (class$java$lang$Short == null) {
            clsClass$4 = class$("java.lang.Short");
            class$java$lang$Short = clsClass$4;
        } else {
            clsClass$4 = class$java$lang$Short;
        }
        if (cls == clsClass$4) {
            return Short.TYPE;
        }
        if (cls == Short.TYPE) {
            if (class$java$lang$Short != null) {
                return class$java$lang$Short;
            }
            Class clsClass$12 = class$("java.lang.Short");
            class$java$lang$Short = clsClass$12;
            return clsClass$12;
        }
        if (class$java$lang$Integer == null) {
            clsClass$5 = class$(Constants.INTEGER_CLASS);
            class$java$lang$Integer = clsClass$5;
        } else {
            clsClass$5 = class$java$lang$Integer;
        }
        if (cls == clsClass$5) {
            return Integer.TYPE;
        }
        if (cls == Integer.TYPE) {
            if (class$java$lang$Integer != null) {
                return class$java$lang$Integer;
            }
            Class clsClass$13 = class$(Constants.INTEGER_CLASS);
            class$java$lang$Integer = clsClass$13;
            return clsClass$13;
        }
        if (class$java$lang$Long == null) {
            clsClass$6 = class$("java.lang.Long");
            class$java$lang$Long = clsClass$6;
        } else {
            clsClass$6 = class$java$lang$Long;
        }
        if (cls == clsClass$6) {
            return Long.TYPE;
        }
        if (cls == Long.TYPE) {
            if (class$java$lang$Long != null) {
                return class$java$lang$Long;
            }
            Class clsClass$14 = class$("java.lang.Long");
            class$java$lang$Long = clsClass$14;
            return clsClass$14;
        }
        if (class$java$lang$Float == null) {
            clsClass$7 = class$("java.lang.Float");
            class$java$lang$Float = clsClass$7;
        } else {
            clsClass$7 = class$java$lang$Float;
        }
        if (cls == clsClass$7) {
            return Float.TYPE;
        }
        if (cls == Float.TYPE) {
            if (class$java$lang$Float != null) {
                return class$java$lang$Float;
            }
            Class clsClass$15 = class$("java.lang.Float");
            class$java$lang$Float = clsClass$15;
            return clsClass$15;
        }
        if (class$java$lang$Double == null) {
            clsClass$8 = class$(Constants.DOUBLE_CLASS);
            class$java$lang$Double = clsClass$8;
        } else {
            clsClass$8 = class$java$lang$Double;
        }
        if (cls == clsClass$8) {
            return Double.TYPE;
        }
        if (cls == Double.TYPE) {
            if (class$java$lang$Double != null) {
                return class$java$lang$Double;
            }
            Class clsClass$16 = class$(Constants.DOUBLE_CLASS);
            class$java$lang$Double = clsClass$16;
            return clsClass$16;
        }
        return null;
    }

    static Class class$(String x0) throws Throwable {
        try {
            return Class.forName(x0);
        } catch (ClassNotFoundException x1) {
            throw new NoClassDefFoundError().initCause(x1);
        }
    }

    public void addToNativeConverter(Class cls, ToNativeConverter converter) throws Throwable {
        this.toNativeConverters.add(new Entry(cls, converter));
        Class alt = getAltClass(cls);
        if (alt != null) {
            this.toNativeConverters.add(new Entry(alt, converter));
        }
    }

    public void addFromNativeConverter(Class cls, FromNativeConverter converter) throws Throwable {
        this.fromNativeConverters.add(new Entry(cls, converter));
        Class alt = getAltClass(cls);
        if (alt != null) {
            this.fromNativeConverters.add(new Entry(alt, converter));
        }
    }

    protected void addTypeConverter(Class cls, TypeConverter converter) throws Throwable {
        addFromNativeConverter(cls, converter);
        addToNativeConverter(cls, converter);
    }

    private Object lookupConverter(Class javaClass, List converters) {
        Iterator i2 = converters.iterator();
        while (i2.hasNext()) {
            Entry entry = (Entry) i2.next();
            if (entry.type.isAssignableFrom(javaClass)) {
                return entry.converter;
            }
        }
        return null;
    }

    @Override // com.sun.jna.TypeMapper
    public FromNativeConverter getFromNativeConverter(Class javaType) {
        return (FromNativeConverter) lookupConverter(javaType, this.fromNativeConverters);
    }

    @Override // com.sun.jna.TypeMapper
    public ToNativeConverter getToNativeConverter(Class javaType) {
        return (ToNativeConverter) lookupConverter(javaType, this.toNativeConverters);
    }
}
