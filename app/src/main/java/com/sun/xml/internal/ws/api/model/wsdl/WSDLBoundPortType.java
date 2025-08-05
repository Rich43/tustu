package com.sun.xml.internal.ws.api.model.wsdl;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import com.sun.xml.internal.ws.api.BindingID;
import com.sun.xml.internal.ws.api.model.ParameterBinding;
import javax.jws.WebParam;
import javax.jws.soap.SOAPBinding;
import javax.xml.namespace.QName;

/* loaded from: rt.jar:com/sun/xml/internal/ws/api/model/wsdl/WSDLBoundPortType.class */
public interface WSDLBoundPortType extends WSDLFeaturedObject, WSDLExtensible {
    QName getName();

    @NotNull
    WSDLModel getOwner();

    WSDLBoundOperation get(QName qName);

    QName getPortTypeName();

    WSDLPortType getPortType();

    Iterable<? extends WSDLBoundOperation> getBindingOperations();

    @NotNull
    SOAPBinding.Style getStyle();

    BindingID getBindingId();

    @Nullable
    WSDLBoundOperation getOperation(String str, String str2);

    ParameterBinding getBinding(QName qName, String str, WebParam.Mode mode);
}
