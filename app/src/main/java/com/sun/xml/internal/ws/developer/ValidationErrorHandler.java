package com.sun.xml.internal.ws.developer;

import com.sun.xml.internal.ws.api.message.Packet;
import org.xml.sax.ErrorHandler;

/* loaded from: rt.jar:com/sun/xml/internal/ws/developer/ValidationErrorHandler.class */
public abstract class ValidationErrorHandler implements ErrorHandler {
    protected Packet packet;

    public void setPacket(Packet packet) {
        this.packet = packet;
    }
}
