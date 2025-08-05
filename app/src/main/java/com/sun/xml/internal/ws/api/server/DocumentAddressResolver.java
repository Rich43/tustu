package com.sun.xml.internal.ws.api.server;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;

/* loaded from: rt.jar:com/sun/xml/internal/ws/api/server/DocumentAddressResolver.class */
public interface DocumentAddressResolver {
    @Nullable
    String getRelativeAddressFor(@NotNull SDDocument sDDocument, @NotNull SDDocument sDDocument2);
}
