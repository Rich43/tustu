package com.sun.xml.internal.ws.api;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import com.sun.xml.internal.ws.api.addressing.WSEndpointReference;
import com.sun.xml.internal.ws.api.server.Container;
import com.sun.xml.internal.ws.client.WSServiceDelegate;
import java.lang.reflect.Field;
import java.net.URL;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import javax.xml.bind.JAXBContext;
import javax.xml.namespace.QName;
import javax.xml.ws.Dispatch;
import javax.xml.ws.Service;
import javax.xml.ws.WebServiceFeature;
import javax.xml.ws.spi.ServiceDelegate;

/* loaded from: rt.jar:com/sun/xml/internal/ws/api/WSService.class */
public abstract class WSService extends ServiceDelegate implements ComponentRegistry {
    private final Set<Component> components = new CopyOnWriteArraySet();
    protected static final ThreadLocal<InitParams> INIT_PARAMS = new ThreadLocal<>();
    protected static final InitParams EMPTY_PARAMS = new InitParams();

    public abstract <T> T getPort(WSEndpointReference wSEndpointReference, Class<T> cls, WebServiceFeature... webServiceFeatureArr);

    public abstract <T> Dispatch<T> createDispatch(QName qName, WSEndpointReference wSEndpointReference, Class<T> cls, Service.Mode mode, WebServiceFeature... webServiceFeatureArr);

    public abstract Dispatch<Object> createDispatch(QName qName, WSEndpointReference wSEndpointReference, JAXBContext jAXBContext, Service.Mode mode, WebServiceFeature... webServiceFeatureArr);

    @NotNull
    public abstract Container getContainer();

    protected WSService() {
    }

    @Override // com.sun.xml.internal.ws.api.Component
    @Nullable
    public <S> S getSPI(@NotNull Class<S> cls) {
        Iterator<Component> it = this.components.iterator();
        while (it.hasNext()) {
            S s2 = (S) it.next().getSPI(cls);
            if (s2 != null) {
                return s2;
            }
        }
        return (S) getContainer().getSPI(cls);
    }

    @Override // com.sun.xml.internal.ws.api.ComponentRegistry
    @NotNull
    public Set<Component> getComponents() {
        return this.components;
    }

    public static WSService create(URL wsdlDocumentLocation, QName serviceName) {
        return new WSServiceDelegate(wsdlDocumentLocation, serviceName, (Class<? extends Service>) Service.class, new WebServiceFeature[0]);
    }

    public static WSService create(QName serviceName) {
        return create(null, serviceName);
    }

    public static WSService create() {
        return create(null, new QName(WSService.class.getName(), "dummy"));
    }

    /* loaded from: rt.jar:com/sun/xml/internal/ws/api/WSService$InitParams.class */
    public static final class InitParams {
        private Container container;

        public void setContainer(Container c2) {
            this.container = c2;
        }

        public Container getContainer() {
            return this.container;
        }
    }

    public static Service create(URL wsdlDocumentLocation, QName serviceName, InitParams properties) {
        if (INIT_PARAMS.get() != null) {
            throw new IllegalStateException("someone left non-null InitParams");
        }
        INIT_PARAMS.set(properties);
        try {
            Service svc = Service.create(wsdlDocumentLocation, serviceName);
            if (INIT_PARAMS.get() != null) {
                throw new IllegalStateException("Service " + ((Object) svc) + " didn't recognize InitParams");
            }
            INIT_PARAMS.set(null);
            return svc;
        } catch (Throwable th) {
            INIT_PARAMS.set(null);
            throw th;
        }
    }

    public static WSService unwrap(final Service svc) {
        return (WSService) AccessController.doPrivileged(new PrivilegedAction<WSService>() { // from class: com.sun.xml.internal.ws.api.WSService.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            public WSService run() throws SecurityException, IllegalArgumentException {
                try {
                    Field f2 = svc.getClass().getField("delegate");
                    f2.setAccessible(true);
                    Object delegate = f2.get(svc);
                    if (!(delegate instanceof WSService)) {
                        throw new IllegalArgumentException();
                    }
                    return (WSService) delegate;
                } catch (IllegalAccessException e2) {
                    IllegalAccessError x2 = new IllegalAccessError(e2.getMessage());
                    x2.initCause(e2);
                    throw x2;
                } catch (NoSuchFieldException e3) {
                    AssertionError x3 = new AssertionError((Object) "Unexpected service API implementation");
                    x3.initCause(e3);
                    throw x3;
                }
            }
        });
    }
}
