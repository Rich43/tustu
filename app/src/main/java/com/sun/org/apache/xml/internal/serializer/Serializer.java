package com.sun.org.apache.xml.internal.serializer;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.util.Properties;
import org.xml.sax.ContentHandler;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/serializer/Serializer.class */
public interface Serializer {
    void setOutputStream(OutputStream outputStream);

    OutputStream getOutputStream();

    void setWriter(Writer writer);

    Writer getWriter();

    void setOutputFormat(Properties properties);

    Properties getOutputFormat();

    ContentHandler asContentHandler() throws IOException;

    DOMSerializer asDOMSerializer() throws IOException;

    boolean reset();
}
