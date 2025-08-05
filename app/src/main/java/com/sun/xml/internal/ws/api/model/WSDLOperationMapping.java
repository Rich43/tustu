package com.sun.xml.internal.ws.api.model;

import com.sun.xml.internal.ws.api.model.wsdl.WSDLBoundOperation;
import javax.xml.namespace.QName;

/* loaded from: rt.jar:com/sun/xml/internal/ws/api/model/WSDLOperationMapping.class */
public interface WSDLOperationMapping {
    WSDLBoundOperation getWSDLBoundOperation();

    JavaMethod getJavaMethod();

    QName getOperationName();
}
