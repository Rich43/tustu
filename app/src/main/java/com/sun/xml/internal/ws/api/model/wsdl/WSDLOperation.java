package com.sun.xml.internal.ws.api.model.wsdl;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import javax.xml.namespace.QName;

/* loaded from: rt.jar:com/sun/xml/internal/ws/api/model/wsdl/WSDLOperation.class */
public interface WSDLOperation extends WSDLObject, WSDLExtensible {
    @NotNull
    QName getName();

    @NotNull
    WSDLInput getInput();

    @Nullable
    WSDLOutput getOutput();

    boolean isOneWay();

    Iterable<? extends WSDLFault> getFaults();

    @Nullable
    WSDLFault getFault(QName qName);

    @NotNull
    QName getPortTypeName();

    String getParameterOrder();
}
