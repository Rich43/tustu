package com.sun.xml.internal.ws.api.server;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import javax.xml.namespace.QName;

/* loaded from: rt.jar:com/sun/xml/internal/ws/api/server/PortAddressResolver.class */
public abstract class PortAddressResolver {
    @Nullable
    public abstract String getAddressFor(@NotNull QName qName, @NotNull String str);

    @Nullable
    public String getAddressFor(@NotNull QName serviceName, @NotNull String portName, String currentAddress) {
        return getAddressFor(serviceName, portName);
    }
}
