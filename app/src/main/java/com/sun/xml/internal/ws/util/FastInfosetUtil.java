package com.sun.xml.internal.ws.util;

import com.sun.xml.internal.ws.streaming.XMLReaderException;
import com.sun.xml.internal.ws.streaming.XMLStreamReaderException;
import java.io.InputStream;
import javax.xml.stream.XMLStreamReader;

/* loaded from: rt.jar:com/sun/xml/internal/ws/util/FastInfosetUtil.class */
public class FastInfosetUtil {
    public static XMLStreamReader createFIStreamReader(InputStream in) {
        if (FastInfosetReflection.fiStAXDocumentParser_new == null) {
            throw new XMLReaderException("fastinfoset.noImplementation", new Object[0]);
        }
        try {
            Object sdp = FastInfosetReflection.fiStAXDocumentParser_new.newInstance(new Object[0]);
            FastInfosetReflection.fiStAXDocumentParser_setStringInterning.invoke(sdp, Boolean.TRUE);
            FastInfosetReflection.fiStAXDocumentParser_setInputStream.invoke(sdp, in);
            return (XMLStreamReader) sdp;
        } catch (Exception e2) {
            throw new XMLStreamReaderException(e2);
        }
    }
}
