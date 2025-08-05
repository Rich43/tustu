package com.sun.xml.internal.ws.api.model.wsdl;

import java.util.List;
import javax.xml.namespace.QName;
import org.xml.sax.Locator;

/* loaded from: rt.jar:com/sun/xml/internal/ws/api/model/wsdl/WSDLExtensible.class */
public interface WSDLExtensible extends WSDLObject {
    Iterable<WSDLExtension> getExtensions();

    <T extends WSDLExtension> Iterable<T> getExtensions(Class<T> cls);

    <T extends WSDLExtension> T getExtension(Class<T> cls);

    void addExtension(WSDLExtension wSDLExtension);

    boolean areRequiredExtensionsUnderstood();

    void addNotUnderstoodExtension(QName qName, Locator locator);

    List<? extends WSDLExtension> getNotUnderstoodExtensions();
}
