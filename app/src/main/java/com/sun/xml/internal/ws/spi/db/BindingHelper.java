package com.sun.xml.internal.ws.spi.db;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import com.sun.xml.internal.bind.api.impl.NameConverter;
import java.lang.reflect.Type;

/* loaded from: rt.jar:com/sun/xml/internal/ws/spi/db/BindingHelper.class */
public class BindingHelper {
    @NotNull
    public static String mangleNameToVariableName(@NotNull String localName) {
        return NameConverter.standard.toVariableName(localName);
    }

    @NotNull
    public static String mangleNameToClassName(@NotNull String localName) {
        return NameConverter.standard.toClassName(localName);
    }

    @NotNull
    public static String mangleNameToPropertyName(@NotNull String localName) {
        return NameConverter.standard.toPropertyName(localName);
    }

    @Nullable
    public static Type getBaseType(@NotNull Type type, @NotNull Class baseType) {
        return Utils.REFLECTION_NAVIGATOR.getBaseClass(type, baseType);
    }

    public static <T> Class<T> erasure(Type t2) {
        return (Class) Utils.REFLECTION_NAVIGATOR.erasure(t2);
    }
}
