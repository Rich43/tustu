package com.sun.xml.internal.ws.api.server;

import com.sun.istack.internal.NotNull;

/* loaded from: rt.jar:com/sun/xml/internal/ws/api/server/WebModule.class */
public abstract class WebModule extends Module {
    @NotNull
    public abstract String getContextPath();
}
