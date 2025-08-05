package com.sun.xml.internal.ws.api.client;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import com.sun.xml.internal.ws.api.WSFeatureList;
import com.sun.xml.internal.ws.developer.WSBindingProvider;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.xml.ws.WebServiceFeature;

/* loaded from: rt.jar:com/sun/xml/internal/ws/api/client/ServiceInterceptor.class */
public abstract class ServiceInterceptor {
    public List<WebServiceFeature> preCreateBinding(@NotNull WSPortInfo port, @Nullable Class<?> serviceEndpointInterface, @NotNull WSFeatureList defaultFeatures) {
        return Collections.emptyList();
    }

    public void postCreateProxy(@NotNull WSBindingProvider bp2, @NotNull Class<?> serviceEndpointInterface) {
    }

    public void postCreateDispatch(@NotNull WSBindingProvider bp2) {
    }

    public static ServiceInterceptor aggregate(final ServiceInterceptor... interceptors) {
        if (interceptors.length == 1) {
            return interceptors[0];
        }
        return new ServiceInterceptor() { // from class: com.sun.xml.internal.ws.api.client.ServiceInterceptor.1
            @Override // com.sun.xml.internal.ws.api.client.ServiceInterceptor
            public List<WebServiceFeature> preCreateBinding(@NotNull WSPortInfo port, @Nullable Class<?> portInterface, @NotNull WSFeatureList defaultFeatures) {
                List<WebServiceFeature> r2 = new ArrayList<>();
                for (ServiceInterceptor si : interceptors) {
                    r2.addAll(si.preCreateBinding(port, portInterface, defaultFeatures));
                }
                return r2;
            }

            @Override // com.sun.xml.internal.ws.api.client.ServiceInterceptor
            public void postCreateProxy(@NotNull WSBindingProvider bp2, @NotNull Class<?> serviceEndpointInterface) {
                for (ServiceInterceptor si : interceptors) {
                    si.postCreateProxy(bp2, serviceEndpointInterface);
                }
            }

            @Override // com.sun.xml.internal.ws.api.client.ServiceInterceptor
            public void postCreateDispatch(@NotNull WSBindingProvider bp2) {
                for (ServiceInterceptor si : interceptors) {
                    si.postCreateDispatch(bp2);
                }
            }
        };
    }
}
