package com.sun.xml.internal.ws.api;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;

/* loaded from: rt.jar:com/sun/xml/internal/ws/api/Component.class */
public interface Component {
    @Nullable
    <S> S getSPI(@NotNull Class<S> cls);
}
