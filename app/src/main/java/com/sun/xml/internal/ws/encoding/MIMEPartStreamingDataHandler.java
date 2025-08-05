package com.sun.xml.internal.ws.encoding;

import com.sun.xml.internal.org.jvnet.mimepull.MIMEPart;
import com.sun.xml.internal.ws.developer.StreamingDataHandler;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.activation.DataSource;

/* loaded from: rt.jar:com/sun/xml/internal/ws/encoding/MIMEPartStreamingDataHandler.class */
public class MIMEPartStreamingDataHandler extends StreamingDataHandler {
    private final StreamingDataSource ds;

    public MIMEPartStreamingDataHandler(MIMEPart part) {
        super(new StreamingDataSource(part));
        this.ds = (StreamingDataSource) getDataSource();
    }

    @Override // com.sun.xml.internal.org.jvnet.staxex.StreamingDataHandler
    public InputStream readOnce() throws IOException {
        return this.ds.readOnce();
    }

    @Override // com.sun.xml.internal.org.jvnet.staxex.StreamingDataHandler
    public void moveTo(File file) throws IOException {
        this.ds.moveTo(file);
    }

    @Override // com.sun.xml.internal.org.jvnet.staxex.StreamingDataHandler, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        this.ds.close();
    }

    /* loaded from: rt.jar:com/sun/xml/internal/ws/encoding/MIMEPartStreamingDataHandler$StreamingDataSource.class */
    private static final class StreamingDataSource implements DataSource {
        private final MIMEPart part;

        StreamingDataSource(MIMEPart part) {
            this.part = part;
        }

        @Override // javax.activation.DataSource
        public InputStream getInputStream() throws IOException {
            return this.part.read();
        }

        InputStream readOnce() throws IOException {
            try {
                return this.part.readOnce();
            } catch (Exception e2) {
                throw new MyIOException(e2);
            }
        }

        void moveTo(File file) throws IOException {
            this.part.moveTo(file);
        }

        @Override // javax.activation.DataSource
        public OutputStream getOutputStream() throws IOException {
            return null;
        }

        @Override // javax.activation.DataSource
        public String getContentType() {
            return this.part.getContentType();
        }

        @Override // javax.activation.DataSource
        public String getName() {
            return "";
        }

        public void close() throws IOException {
            this.part.close();
        }
    }

    /* loaded from: rt.jar:com/sun/xml/internal/ws/encoding/MIMEPartStreamingDataHandler$MyIOException.class */
    private static final class MyIOException extends IOException {
        private final Exception linkedException;

        MyIOException(Exception linkedException) {
            this.linkedException = linkedException;
        }

        @Override // java.lang.Throwable
        public Throwable getCause() {
            return this.linkedException;
        }
    }
}
