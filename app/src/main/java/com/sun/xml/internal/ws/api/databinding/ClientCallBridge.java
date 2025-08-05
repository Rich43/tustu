package com.sun.xml.internal.ws.api.databinding;

import com.sun.xml.internal.ws.api.message.Packet;
import com.sun.xml.internal.ws.api.model.JavaMethod;
import java.lang.reflect.Method;

/* loaded from: rt.jar:com/sun/xml/internal/ws/api/databinding/ClientCallBridge.class */
public interface ClientCallBridge {
    Packet createRequestPacket(com.oracle.webservices.internal.api.databinding.JavaCallInfo javaCallInfo);

    com.oracle.webservices.internal.api.databinding.JavaCallInfo readResponse(Packet packet, com.oracle.webservices.internal.api.databinding.JavaCallInfo javaCallInfo) throws Throwable;

    Method getMethod();

    JavaMethod getOperationModel();
}
