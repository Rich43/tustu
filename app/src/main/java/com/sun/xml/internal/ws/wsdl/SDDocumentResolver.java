package com.sun.xml.internal.ws.wsdl;

import com.sun.istack.internal.Nullable;
import com.sun.xml.internal.ws.api.server.SDDocument;

/* loaded from: rt.jar:com/sun/xml/internal/ws/wsdl/SDDocumentResolver.class */
public interface SDDocumentResolver {
    @Nullable
    SDDocument resolve(String str);
}
