package com.sun.xml.internal.bind.api;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;

/* loaded from: rt.jar:com/sun/xml/internal/bind/api/ClassResolver.class */
public abstract class ClassResolver {
    @Nullable
    public abstract Class<?> resolveElementName(@NotNull String str, @NotNull String str2) throws Exception;
}
