package com.sun.org.apache.xml.internal.serialize;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import org.xml.sax.ContentHandler;
import org.xml.sax.DocumentHandler;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/serialize/Serializer.class */
public interface Serializer {
    void setOutputByteStream(OutputStream outputStream);

    void setOutputCharStream(Writer writer);

    void setOutputFormat(OutputFormat outputFormat);

    DocumentHandler asDocumentHandler() throws IOException;

    ContentHandler asContentHandler() throws IOException;

    DOMSerializer asDOMSerializer() throws IOException;
}
