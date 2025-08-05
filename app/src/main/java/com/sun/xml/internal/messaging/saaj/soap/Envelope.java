package com.sun.xml.internal.messaging.saaj.soap;

import java.io.IOException;
import java.io.OutputStream;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.transform.Source;

/* loaded from: rt.jar:com/sun/xml/internal/messaging/saaj/soap/Envelope.class */
public interface Envelope extends SOAPEnvelope {
    Source getContent();

    void output(OutputStream outputStream) throws IOException;

    void output(OutputStream outputStream, boolean z2) throws IOException;
}
