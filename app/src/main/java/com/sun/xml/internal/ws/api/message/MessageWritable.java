package com.sun.xml.internal.ws.api.message;

import com.oracle.webservices.internal.api.message.ContentType;
import java.io.IOException;
import java.io.OutputStream;
import javax.xml.ws.soap.MTOMFeature;

/* loaded from: rt.jar:com/sun/xml/internal/ws/api/message/MessageWritable.class */
public interface MessageWritable {
    ContentType getContentType();

    ContentType writeTo(OutputStream outputStream) throws IOException;

    void setMTOMConfiguration(MTOMFeature mTOMFeature);
}
