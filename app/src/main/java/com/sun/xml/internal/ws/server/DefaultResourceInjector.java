package com.sun.xml.internal.ws.server;

import com.sun.istack.internal.NotNull;
import com.sun.xml.internal.ws.api.server.ResourceInjector;
import com.sun.xml.internal.ws.api.server.WSWebServiceContext;
import com.sun.xml.internal.ws.util.InjectionPlan;
import javax.xml.ws.WebServiceContext;

/* loaded from: rt.jar:com/sun/xml/internal/ws/server/DefaultResourceInjector.class */
public final class DefaultResourceInjector extends ResourceInjector {
    @Override // com.sun.xml.internal.ws.api.server.ResourceInjector
    public void inject(@NotNull WSWebServiceContext context, @NotNull Object instance) {
        InjectionPlan.buildInjectionPlan(instance.getClass(), WebServiceContext.class, false).inject((InjectionPlan) instance, (Object) context);
    }
}
