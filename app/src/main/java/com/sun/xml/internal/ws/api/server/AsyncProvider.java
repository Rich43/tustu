package com.sun.xml.internal.ws.api.server;

import com.sun.istack.internal.NotNull;
import javax.xml.ws.WebServiceContext;

/* loaded from: rt.jar:com/sun/xml/internal/ws/api/server/AsyncProvider.class */
public interface AsyncProvider<T> {
    void invoke(@NotNull T t2, @NotNull AsyncProviderCallback<T> asyncProviderCallback, @NotNull WebServiceContext webServiceContext);
}
