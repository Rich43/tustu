package com.sun.xml.internal.ws.api.server;

import com.sun.istack.internal.NotNull;

/* loaded from: rt.jar:com/sun/xml/internal/ws/api/server/ServiceDefinition.class */
public interface ServiceDefinition extends Iterable<SDDocument> {
    @NotNull
    SDDocument getPrimary();

    void addFilter(@NotNull SDDocumentFilter sDDocumentFilter);
}
