package com.sun.xml.internal.org.jvnet.staxex;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import javax.activation.DataHandler;
import javax.activation.DataSource;

/* loaded from: rt.jar:com/sun/xml/internal/org/jvnet/staxex/StreamingDataHandler.class */
public abstract class StreamingDataHandler extends DataHandler implements Closeable {
    public abstract InputStream readOnce() throws IOException;

    public abstract void moveTo(File file) throws IOException;

    public abstract void close() throws IOException;

    public StreamingDataHandler(Object o2, String s2) {
        super(o2, s2);
    }

    public StreamingDataHandler(URL url) {
        super(url);
    }

    public StreamingDataHandler(DataSource dataSource) {
        super(dataSource);
    }
}
