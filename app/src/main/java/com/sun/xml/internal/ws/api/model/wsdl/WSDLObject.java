package com.sun.xml.internal.ws.api.model.wsdl;

import com.sun.istack.internal.NotNull;
import org.xml.sax.Locator;

/* loaded from: rt.jar:com/sun/xml/internal/ws/api/model/wsdl/WSDLObject.class */
public interface WSDLObject {
    @NotNull
    Locator getLocation();
}
