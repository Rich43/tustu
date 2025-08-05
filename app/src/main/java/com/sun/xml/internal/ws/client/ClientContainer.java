package com.sun.xml.internal.ws.client;

import com.sun.xml.internal.ws.api.ResourceLoader;
import com.sun.xml.internal.ws.api.server.Container;
import java.net.MalformedURLException;
import java.net.URL;

/* loaded from: rt.jar:com/sun/xml/internal/ws/client/ClientContainer.class */
final class ClientContainer extends Container {
    private final ResourceLoader loader = new ResourceLoader() { // from class: com.sun.xml.internal.ws.client.ClientContainer.1
        @Override // com.sun.xml.internal.ws.api.ResourceLoader
        public URL getResource(String resource) throws MalformedURLException {
            ClassLoader cl = Thread.currentThread().getContextClassLoader();
            if (cl == null) {
                cl = getClass().getClassLoader();
            }
            return cl.getResource("META-INF/" + resource);
        }
    };

    ClientContainer() {
    }

    @Override // com.sun.xml.internal.ws.api.server.Container, com.sun.xml.internal.ws.api.Component
    public <T> T getSPI(Class<T> cls) {
        T t2 = (T) super.getSPI(cls);
        if (t2 != null) {
            return t2;
        }
        if (cls == ResourceLoader.class) {
            return cls.cast(this.loader);
        }
        return null;
    }
}
