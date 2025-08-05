package com.oracle.webservices.internal.impl.internalspi.encoding;

import com.sun.xml.internal.ws.api.SOAPVersion;
import com.sun.xml.internal.ws.api.message.AttachmentSet;
import com.sun.xml.internal.ws.api.message.Message;
import java.io.IOException;
import java.io.InputStream;

/* loaded from: rt.jar:com/oracle/webservices/internal/impl/internalspi/encoding/StreamDecoder.class */
public interface StreamDecoder {
    Message decode(InputStream inputStream, String str, AttachmentSet attachmentSet, SOAPVersion sOAPVersion) throws IOException;
}
