package com.sun.jna.win32;

import com.sun.jna.DefaultTypeMapper;
import com.sun.jna.FromNativeContext;
import com.sun.jna.StringArray;
import com.sun.jna.ToNativeContext;
import com.sun.jna.TypeConverter;
import com.sun.jna.TypeMapper;
import com.sun.jna.WString;
import com.sun.org.apache.xalan.internal.xsltc.compiler.Constants;

/* loaded from: JavaFTD2XX.jar:com/sun/jna/win32/W32APITypeMapper.class */
public class W32APITypeMapper extends DefaultTypeMapper {
    public static final TypeMapper UNICODE = new W32APITypeMapper(true);
    public static final TypeMapper ASCII = new W32APITypeMapper(false);
    static Class class$com$sun$jna$WString;
    static Class class$java$lang$String;
    static Class array$Ljava$lang$String;
    static Class class$java$lang$Integer;
    static Class class$java$lang$Boolean;

    protected W32APITypeMapper(boolean unicode) throws Throwable {
        Class clsClass$;
        Class clsClass$2;
        Class clsClass$3;
        if (unicode) {
            TypeConverter stringConverter = new TypeConverter(this) { // from class: com.sun.jna.win32.W32APITypeMapper.1
                private final W32APITypeMapper this$0;

                {
                    this.this$0 = this;
                }

                @Override // com.sun.jna.ToNativeConverter
                public Object toNative(Object value, ToNativeContext context) {
                    if (value == null) {
                        return null;
                    }
                    if (value instanceof String[]) {
                        return new StringArray((String[]) value, true);
                    }
                    return new WString(value.toString());
                }

                @Override // com.sun.jna.FromNativeConverter
                public Object fromNative(Object value, FromNativeContext context) {
                    if (value == null) {
                        return null;
                    }
                    return value.toString();
                }

                @Override // com.sun.jna.FromNativeConverter, com.sun.jna.ToNativeConverter
                public Class nativeType() throws Throwable {
                    if (W32APITypeMapper.class$com$sun$jna$WString != null) {
                        return W32APITypeMapper.class$com$sun$jna$WString;
                    }
                    Class clsClass$4 = W32APITypeMapper.class$("com.sun.jna.WString");
                    W32APITypeMapper.class$com$sun$jna$WString = clsClass$4;
                    return clsClass$4;
                }
            };
            if (class$java$lang$String == null) {
                clsClass$2 = class$("java.lang.String");
                class$java$lang$String = clsClass$2;
            } else {
                clsClass$2 = class$java$lang$String;
            }
            addTypeConverter(clsClass$2, stringConverter);
            if (array$Ljava$lang$String == null) {
                clsClass$3 = class$("[Ljava.lang.String;");
                array$Ljava$lang$String = clsClass$3;
            } else {
                clsClass$3 = array$Ljava$lang$String;
            }
            addToNativeConverter(clsClass$3, stringConverter);
        }
        TypeConverter booleanConverter = new TypeConverter(this) { // from class: com.sun.jna.win32.W32APITypeMapper.2
            private final W32APITypeMapper this$0;

            {
                this.this$0 = this;
            }

            @Override // com.sun.jna.ToNativeConverter
            public Object toNative(Object value, ToNativeContext context) {
                return new Integer(Boolean.TRUE.equals(value) ? 1 : 0);
            }

            @Override // com.sun.jna.FromNativeConverter
            public Object fromNative(Object value, FromNativeContext context) {
                return ((Integer) value).intValue() != 0 ? Boolean.TRUE : Boolean.FALSE;
            }

            @Override // com.sun.jna.FromNativeConverter, com.sun.jna.ToNativeConverter
            public Class nativeType() throws Throwable {
                if (W32APITypeMapper.class$java$lang$Integer != null) {
                    return W32APITypeMapper.class$java$lang$Integer;
                }
                Class clsClass$4 = W32APITypeMapper.class$(Constants.INTEGER_CLASS);
                W32APITypeMapper.class$java$lang$Integer = clsClass$4;
                return clsClass$4;
            }
        };
        if (class$java$lang$Boolean == null) {
            clsClass$ = class$(Constants.BOOLEAN_CLASS);
            class$java$lang$Boolean = clsClass$;
        } else {
            clsClass$ = class$java$lang$Boolean;
        }
        addTypeConverter(clsClass$, booleanConverter);
    }

    static Class class$(String x0) throws Throwable {
        try {
            return Class.forName(x0);
        } catch (ClassNotFoundException x1) {
            throw new NoClassDefFoundError().initCause(x1);
        }
    }
}
