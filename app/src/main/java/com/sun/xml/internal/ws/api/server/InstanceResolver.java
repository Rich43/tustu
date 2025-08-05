package com.sun.xml.internal.ws.api.server;

import com.sun.istack.internal.NotNull;
import com.sun.xml.internal.ws.api.message.Packet;
import com.sun.xml.internal.ws.resources.ServerMessages;
import com.sun.xml.internal.ws.resources.WsservletMessages;
import com.sun.xml.internal.ws.server.ServerRtException;
import com.sun.xml.internal.ws.server.SingletonResolver;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.ws.Provider;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.WebServiceException;

/* loaded from: rt.jar:com/sun/xml/internal/ws/api/server/InstanceResolver.class */
public abstract class InstanceResolver<T> {
    private static final Logger logger;
    static final /* synthetic */ boolean $assertionsDisabled;

    @NotNull
    public abstract T resolve(@NotNull Packet packet);

    static {
        $assertionsDisabled = !InstanceResolver.class.desiredAssertionStatus();
        logger = Logger.getLogger("com.sun.xml.internal.ws.server");
    }

    public void postInvoke(@NotNull Packet request, @NotNull T servant) {
    }

    public void start(@NotNull WSWebServiceContext wsc, @NotNull WSEndpoint endpoint) {
        start(wsc);
    }

    public void start(@NotNull WebServiceContext wsc) {
    }

    public void dispose() {
    }

    public static <T> InstanceResolver<T> createSingleton(T singleton) {
        if (!$assertionsDisabled && singleton == null) {
            throw new AssertionError();
        }
        InstanceResolver ir = createFromInstanceResolverAnnotation(singleton.getClass());
        if (ir == null) {
            ir = new SingletonResolver(singleton);
        }
        return ir;
    }

    public static <T> InstanceResolver<T> createDefault(@NotNull Class<T> clazz, boolean bool) {
        return createDefault(clazz);
    }

    public static <T> InstanceResolver<T> createDefault(@NotNull Class<T> clazz) {
        InstanceResolver<T> ir = createFromInstanceResolverAnnotation(clazz);
        if (ir == null) {
            ir = new SingletonResolver(createNewInstance(clazz));
        }
        return ir;
    }

    public static <T> InstanceResolver<T> createFromInstanceResolverAnnotation(@NotNull Class<T> clazz) {
        for (Annotation a2 : clazz.getAnnotations()) {
            InstanceResolverAnnotation ira = (InstanceResolverAnnotation) a2.annotationType().getAnnotation(InstanceResolverAnnotation.class);
            if (ira != null) {
                Class<? extends InstanceResolver> ir = ira.value();
                try {
                    return ir.getConstructor(Class.class).newInstance(clazz);
                } catch (IllegalAccessException e2) {
                    throw new WebServiceException(ServerMessages.FAILED_TO_INSTANTIATE_INSTANCE_RESOLVER(ir.getName(), a2.annotationType(), clazz.getName()));
                } catch (InstantiationException e3) {
                    throw new WebServiceException(ServerMessages.FAILED_TO_INSTANTIATE_INSTANCE_RESOLVER(ir.getName(), a2.annotationType(), clazz.getName()));
                } catch (NoSuchMethodException e4) {
                    throw new WebServiceException(ServerMessages.FAILED_TO_INSTANTIATE_INSTANCE_RESOLVER(ir.getName(), a2.annotationType(), clazz.getName()));
                } catch (InvocationTargetException e5) {
                    throw new WebServiceException(ServerMessages.FAILED_TO_INSTANTIATE_INSTANCE_RESOLVER(ir.getName(), a2.annotationType(), clazz.getName()));
                }
            }
        }
        return null;
    }

    protected static <T> T createNewInstance(Class<T> cl) {
        try {
            return cl.newInstance();
        } catch (IllegalAccessException e2) {
            logger.log(Level.SEVERE, e2.getMessage(), (Throwable) e2);
            throw new ServerRtException(WsservletMessages.ERROR_IMPLEMENTOR_FACTORY_NEW_INSTANCE_FAILED(cl), new Object[0]);
        } catch (InstantiationException e3) {
            logger.log(Level.SEVERE, e3.getMessage(), (Throwable) e3);
            throw new ServerRtException(WsservletMessages.ERROR_IMPLEMENTOR_FACTORY_NEW_INSTANCE_FAILED(cl), new Object[0]);
        }
    }

    @NotNull
    public Invoker createInvoker() {
        return new Invoker() { // from class: com.sun.xml.internal.ws.api.server.InstanceResolver.1
            @Override // com.sun.xml.internal.ws.api.server.Invoker
            public void start(@NotNull WSWebServiceContext wsc, @NotNull WSEndpoint endpoint) {
                InstanceResolver.this.start(wsc, endpoint);
            }

            @Override // com.sun.xml.internal.ws.api.server.Invoker
            public void dispose() {
                InstanceResolver.this.dispose();
            }

            /* JADX WARN: Multi-variable type inference failed */
            @Override // com.sun.xml.internal.ws.server.sei.Invoker
            public Object invoke(Packet p2, Method m2, Object... args) throws IllegalAccessException, InvocationTargetException {
                Object objResolve = InstanceResolver.this.resolve(p2);
                try {
                    Object objInvoke = MethodUtil.invoke(objResolve, m2, args);
                    InstanceResolver.this.postInvoke(p2, objResolve);
                    return objInvoke;
                } catch (Throwable th) {
                    InstanceResolver.this.postInvoke(p2, objResolve);
                    throw th;
                }
            }

            /* JADX WARN: Multi-variable type inference failed */
            @Override // com.sun.xml.internal.ws.api.server.Invoker
            public <U> U invokeProvider(@NotNull Packet packet, U u2) {
                Object objResolve = InstanceResolver.this.resolve(packet);
                try {
                    U u3 = (U) ((Provider) objResolve).invoke(u2);
                    InstanceResolver.this.postInvoke(packet, objResolve);
                    return u3;
                } catch (Throwable th) {
                    InstanceResolver.this.postInvoke(packet, objResolve);
                    throw th;
                }
            }

            public String toString() {
                return "Default Invoker over " + InstanceResolver.this.toString();
            }
        };
    }
}
