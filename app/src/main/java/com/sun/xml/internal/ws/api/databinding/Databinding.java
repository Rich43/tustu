package com.sun.xml.internal.ws.api.databinding;

import com.sun.xml.internal.ws.api.message.MessageContextFactory;
import com.sun.xml.internal.ws.api.message.Packet;
import com.sun.xml.internal.ws.api.pipe.ContentType;
import com.sun.xml.internal.ws.wsdl.DispatchException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;

/* loaded from: rt.jar:com/sun/xml/internal/ws/api/databinding/Databinding.class */
public interface Databinding extends com.oracle.webservices.internal.api.databinding.Databinding {
    EndpointCallBridge getEndpointBridge(Packet packet) throws DispatchException;

    ClientCallBridge getClientBridge(Method method);

    void generateWSDL(WSDLGenInfo wSDLGenInfo);

    ContentType encode(Packet packet, OutputStream outputStream) throws IOException;

    void decode(InputStream inputStream, String str, Packet packet) throws IOException;

    MessageContextFactory getMessageContextFactory();
}
