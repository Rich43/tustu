package com.sun.xml.internal.ws.api.server;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;

/* loaded from: rt.jar:com/sun/xml/internal/ws/api/server/EndpointComponent.class */
public interface EndpointComponent {
    @Nullable
    <T> T getSPI(@NotNull Class<T> cls);
}
