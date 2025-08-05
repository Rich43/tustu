package com.sun.xml.internal.ws.api.server;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import com.sun.xml.internal.ws.api.message.Packet;
import java.security.Principal;

/* loaded from: rt.jar:com/sun/xml/internal/ws/api/server/WebServiceContextDelegate.class */
public interface WebServiceContextDelegate {
    Principal getUserPrincipal(@NotNull Packet packet);

    boolean isUserInRole(@NotNull Packet packet, String str);

    @NotNull
    String getEPRAddress(@NotNull Packet packet, @NotNull WSEndpoint wSEndpoint);

    @Nullable
    String getWSDLAddress(@NotNull Packet packet, @NotNull WSEndpoint wSEndpoint);
}
