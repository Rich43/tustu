package com.sun.xml.internal.ws.transport.http.server;

import com.sun.istack.internal.NotNull;
import com.sun.xml.internal.ws.api.server.BoundEndpoint;
import com.sun.xml.internal.ws.api.server.Container;
import com.sun.xml.internal.ws.api.server.Module;
import java.util.ArrayList;
import java.util.List;

/* loaded from: rt.jar:com/sun/xml/internal/ws/transport/http/server/ServerContainer.class */
class ServerContainer extends Container {
    private final Module module = new Module() { // from class: com.sun.xml.internal.ws.transport.http.server.ServerContainer.1
        private final List<BoundEndpoint> endpoints = new ArrayList();

        @Override // com.sun.xml.internal.ws.api.server.Module
        @NotNull
        public List<BoundEndpoint> getBoundEndpoints() {
            return this.endpoints;
        }
    };

    ServerContainer() {
    }

    @Override // com.sun.xml.internal.ws.api.server.Container, com.sun.xml.internal.ws.api.Component
    public <T> T getSPI(Class<T> cls) {
        T t2 = (T) super.getSPI(cls);
        if (t2 != null) {
            return t2;
        }
        if (cls == Module.class) {
            return cls.cast(this.module);
        }
        return null;
    }
}
