package com.sun.xml.internal.ws.api.server;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;

/* loaded from: rt.jar:com/sun/xml/internal/ws/api/server/AsyncProviderCallback.class */
public interface AsyncProviderCallback<T> {
    void send(@Nullable T t2);

    void sendError(@NotNull Throwable th);
}
