package com.sun.xml.internal.ws.client;

import com.sun.xml.internal.ws.util.JAXWSUtils;
import java.io.IOException;
import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import javax.xml.namespace.QName;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceException;

/* loaded from: rt.jar:com/sun/xml/internal/ws/client/SCAnnotations.class */
final class SCAnnotations {
    final ArrayList<QName> portQNames = new ArrayList<>();
    final ArrayList<Class> classes = new ArrayList<>();

    SCAnnotations(final Class<?> sc) {
        AccessController.doPrivileged(new PrivilegedAction<Void>() { // from class: com.sun.xml.internal.ws.client.SCAnnotations.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public Void run2() throws SecurityException {
                WebServiceClient wsc = (WebServiceClient) sc.getAnnotation(WebServiceClient.class);
                if (wsc == null) {
                    throw new WebServiceException("Service Interface Annotations required, exiting...");
                }
                String tns = wsc.targetNamespace();
                try {
                    JAXWSUtils.getFileOrURL(wsc.wsdlLocation());
                    for (Method method : sc.getDeclaredMethods()) {
                        WebEndpoint webEndpoint = (WebEndpoint) method.getAnnotation(WebEndpoint.class);
                        if (webEndpoint != null) {
                            String endpointName = webEndpoint.name();
                            QName portQName = new QName(tns, endpointName);
                            SCAnnotations.this.portQNames.add(portQName);
                        }
                        Class<?> seiClazz = method.getReturnType();
                        if (seiClazz != Void.TYPE) {
                            SCAnnotations.this.classes.add(seiClazz);
                        }
                    }
                    return null;
                } catch (IOException e2) {
                    throw new WebServiceException(e2);
                }
            }
        });
    }
}
