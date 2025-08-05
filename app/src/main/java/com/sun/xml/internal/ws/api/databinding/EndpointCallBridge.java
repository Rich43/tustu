package com.sun.xml.internal.ws.api.databinding;

import com.sun.xml.internal.ws.api.message.Packet;
import com.sun.xml.internal.ws.api.model.JavaMethod;

/* loaded from: rt.jar:com/sun/xml/internal/ws/api/databinding/EndpointCallBridge.class */
public interface EndpointCallBridge {
    com.oracle.webservices.internal.api.databinding.JavaCallInfo deserializeRequest(Packet packet);

    Packet serializeResponse(com.oracle.webservices.internal.api.databinding.JavaCallInfo javaCallInfo);

    JavaMethod getOperationModel();
}
