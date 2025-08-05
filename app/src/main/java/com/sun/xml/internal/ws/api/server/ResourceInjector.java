package com.sun.xml.internal.ws.api.server;

import com.sun.istack.internal.NotNull;
import com.sun.xml.internal.ws.server.DefaultResourceInjector;

/* loaded from: rt.jar:com/sun/xml/internal/ws/api/server/ResourceInjector.class */
public abstract class ResourceInjector {
    public static final ResourceInjector STANDALONE = new DefaultResourceInjector();

    public abstract void inject(@NotNull WSWebServiceContext wSWebServiceContext, @NotNull Object obj);
}
